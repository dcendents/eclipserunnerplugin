package com.eclipserunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.eclipserunner.model.ILaunchConfigurationCategory;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.RunnerModel;

/**
 * Helper class for saving and loading plugin state.
 * 
 * @author bary
 */
public class RunnerStateExternalizer {

	private static final String XML_VERSION_ATTR        = "version";
	private static final String XML_VERSION_VALUE       = "1.0";

	private static final String VERSION_ATTR            = "version";
	private static final String VERSION_VALUE           = "1.0.0";

	private static final String NAME_ATTR               = "name";

	private static final String ROOT_NODE_NAME          = "runner";
	private static final String CATEGORY_NODE_NAME      = "category";
	private static final String CONFIGURATION_NODE_NAME = "launchConfiguration";


	/**
	 * Load plugin state from given file.
	 * 
	 * @param inputFile Plugin state file.
	 * @throws CoreException
	 */
	public static void readStateFromFile(File inputFile) throws CoreException {
		if (!inputFile.exists()) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "File not found '" + inputFile.getAbsolutePath() + "'"));
		}
		if (inputFile.length() == 0) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "File contains no data '" + inputFile.getAbsolutePath() + "'"));
		}

		Document document = openStateFile(inputFile);
		Element runnerNode = document.getDocumentElement();
		String readVersion = runnerNode.getAttribute(VERSION_ATTR);
		if (!readVersion.equals(VERSION_VALUE)) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "State file version '" + readVersion + "' not supported"));
		}

		// configuration to category mapping
		Map<String, String> configurationCategories = new HashMap<String, String>();

		// populating model
		IRunnerModel runnerModel = RunnerModel.getDefault();

		// read categories
		NodeList categoryNodeList = runnerNode.getElementsByTagName(CATEGORY_NODE_NAME);
		for (int categoryIndex = 0; categoryIndex < categoryNodeList.getLength(); categoryIndex++) {
			Element categoryElement = (Element) categoryNodeList.item(categoryIndex);
			String categoryName = categoryElement.getAttribute(NAME_ATTR);

			// create empty categories
			if (runnerModel.getLaunchConfigurationCategory(categoryName) == null) {
				runnerModel.addLaunchConfigurationCategory(categoryName);
			}

			// read configurations and map them to categories
			NodeList launchConfigurationNodeList = categoryElement.getElementsByTagName(CONFIGURATION_NODE_NAME);
			for (int j = 0; j < launchConfigurationNodeList.getLength(); j++) {
				Element configurationElement = (Element) launchConfigurationNodeList.item(j);
				String configurationName = configurationElement.getAttribute(NAME_ATTR);

				configurationCategories.put(configurationName, categoryName);
			}
		}

		// get "fresh" configurations and add to categories
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		for (ILaunchConfiguration configuration : launchManager.getLaunchConfigurations()) {
			ILaunchConfigurationCategory launchConfigurationCategory = null;

			String configurationName = configuration.getName();
			String categoryName = configurationCategories.get(configurationName);

			if (categoryName == null) {
				launchConfigurationCategory = runnerModel.getUncategorizedCategory();
			}
			if (launchConfigurationCategory == null) {
				launchConfigurationCategory = runnerModel.getLaunchConfigurationCategory(categoryName);
			}

			launchConfigurationCategory.add(configuration);
		}
	}

	/**
	 * Load plugin default state.
	 * 
	 * @throws CoreException
	 */
	public static void readDefaultState() throws CoreException {
		IRunnerModel runnerModel = RunnerModel.getDefault();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		for (ILaunchConfiguration configuration : launchManager.getLaunchConfigurations()) {
			runnerModel.getUncategorizedCategory().add(configuration);
		}
	}

	private static Document openStateFile(File inputFile) throws CoreException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(inputFile);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(inputStream);
		} catch (Exception e) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "Failed to load runner state", e));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
	}

	/**
	 * Save plugin state into given file.
	 * 
	 * @param outputFile Plugin state file.
	 * @throws CoreException
	 */
	public static void writeStateToFile(File outputFile) throws CoreException {
		try {
			IRunnerModel runnerModel = RunnerModel.getDefault();
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			try {
				Document document = createCategorDocument(runnerModel.getLaunchConfigurationCategorySet());
				writeDocument(document, outputStream);
				outputStream.flush();
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "Failed to save runner state", e));
		}
	}

	private static Document createCategorDocument(Set<ILaunchConfigurationCategory> launchConfigurationCategories) throws CoreException {
		Document document = createDocument();

		Element runnerNode = document.createElement(ROOT_NODE_NAME);
		runnerNode.setAttribute(VERSION_ATTR, VERSION_VALUE);
		document.appendChild(runnerNode);

		// create the category nodes
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategories) {
			runnerNode.appendChild(
					createCategoryElement(launchConfigurationCategory, document)
			);
		}

		return document;
	}

	private static Document createDocument() throws CoreException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			return documentBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "Failed to create document", e));
		}
	}

	public static Element createCategoryElement(ILaunchConfigurationCategory category, Document document) {
		Element categoryNode = document.createElement(CATEGORY_NODE_NAME);
		categoryNode.setAttribute(NAME_ATTR, category.getName());
		for (ILaunchConfiguration launchConfiguration : category.getLaunchConfigurationSet()) {
			categoryNode.appendChild(
					createConfigurationElement(launchConfiguration, document)
			);
		}
		return categoryNode;
	}

	public static Element createConfigurationElement(ILaunchConfiguration launchConfiguration, Document document) {
		Element launchConfigurationNode = document.createElement(CONFIGURATION_NODE_NAME);
		launchConfigurationNode.setAttribute(NAME_ATTR, launchConfiguration.getName());
		return launchConfigurationNode;
	}

	private static void writeDocument(Document document, OutputStream outputStream) throws CoreException {
		Source source = new DOMSource(document);
		Result result = new StreamResult(outputStream);
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(XML_VERSION_ATTR, XML_VERSION_VALUE);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "Failed to write document", e));
		}
	}

}
