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
import com.eclipserunner.model.ILaunchConfigurationNode;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.impl.LaunchConfigurationNode;
import com.eclipserunner.model.impl.RunnerModel;

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
	private static final String BOOKMARK_ATTR           = "bookmark";

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

		// configuration name to node maping
		Map<String, ILaunchConfigurationNode> launchConfigurationNodes = new HashMap<String, ILaunchConfigurationNode>();

		// populating model
		IRunnerModel runnerModel = RunnerModel.getDefault();

		// read categories
		NodeList categoryNodeList = runnerNode.getElementsByTagName(CATEGORY_NODE_NAME);
		for (int categoryIndex = 0; categoryIndex < categoryNodeList.getLength(); categoryIndex++) {
			Element categoryElement = (Element) categoryNodeList.item(categoryIndex);
			String categoryName = categoryElement.getAttribute(NAME_ATTR);

			// create category if not exists
			ILaunchConfigurationCategory launchConfigurationCategory = runnerModel.getLaunchConfigurationCategory(categoryName);
			if (launchConfigurationCategory == null) {
				launchConfigurationCategory = runnerModel.addLaunchConfigurationCategory(categoryName);
			}

			// read configurations and map them to categories
			NodeList launchConfigurationNodeList = categoryElement.getElementsByTagName(CONFIGURATION_NODE_NAME);
			for (int j = 0; j < launchConfigurationNodeList.getLength(); j++) {
				Element configurationElement = (Element) launchConfigurationNodeList.item(j);
				String configurationName = configurationElement.getAttribute(NAME_ATTR);
				boolean isBookmarked = Boolean.valueOf(configurationElement.getAttribute(BOOKMARK_ATTR));

				// create empty Node
				ILaunchConfigurationNode launchConfigurationNode = new LaunchConfigurationNode();
				launchConfigurationNode.setLaunchConfigurationCategory(launchConfigurationCategory);
				launchConfigurationNode.setBookmarked(isBookmarked);

				launchConfigurationNodes.put(configurationName, launchConfigurationNode);
			}
		}

		// get all configurations from launch manager
		for (ILaunchConfiguration launchConfiguration : getLaunchManager().getLaunchConfigurations()) {

			// get configuration metadata
			String launchConfigurationName = launchConfiguration.getName();

			ILaunchConfigurationNode launchConfigurationNode = launchConfigurationNodes.get(launchConfigurationName);
			if (launchConfigurationNode == null) {
				launchConfigurationNode = new LaunchConfigurationNode();
				launchConfigurationNode.setLaunchConfigurationCategory(runnerModel.getUncategorizedCategory());
			}
			launchConfigurationNode.setLaunchConfiguration(launchConfiguration);

			ILaunchConfigurationCategory launchConfigurationCategory = launchConfigurationNode.getLaunchConfigurationCategory();
			launchConfigurationCategory.add(launchConfigurationNode);
		}
	}

	private static ILaunchManager getLaunchManager() {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		return launchManager;
	}

	/**
	 * Load plugin default state.
	 * 
	 * @throws CoreException
	 */
	public static void readDefaultState() throws CoreException {
		IRunnerModel runnerModel = RunnerModel.getDefault();
		ILaunchManager launchManager = getLaunchManager();
		for (ILaunchConfiguration launchConfiguration : launchManager.getLaunchConfigurations()) {
			LaunchConfigurationNode launchConfigurationNode = new LaunchConfigurationNode();
			launchConfigurationNode.setLaunchConfiguration(launchConfiguration);
			runnerModel.getUncategorizedCategory().add(launchConfigurationNode);
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
				Document document = createCategorDocument(runnerModel.getLaunchConfigurationCategories());
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

		Element runnerElement = document.createElement(ROOT_NODE_NAME);
		runnerElement.setAttribute(VERSION_ATTR, VERSION_VALUE);
		document.appendChild(runnerElement);

		// create the category nodes
		for (ILaunchConfigurationCategory launchConfigurationCategory : launchConfigurationCategories) {
			runnerElement.appendChild(
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
		Element categoryElement = document.createElement(CATEGORY_NODE_NAME);
		categoryElement.setAttribute(NAME_ATTR, category.getName());
		for (ILaunchConfigurationNode launchConfigurationNode : category.getLaunchConfigurationNodes()) {
			categoryElement.appendChild(
					createConfigurationElement(launchConfigurationNode, document)
			);
		}
		return categoryElement;
	}

	public static Element createConfigurationElement(ILaunchConfigurationNode launchConfigurationNode, Document document) {
		Element launchConfigurationElement = document.createElement(CONFIGURATION_NODE_NAME);
		ILaunchConfiguration launchConfiguration = launchConfigurationNode.getLaunchConfiguration();
		launchConfigurationElement.setAttribute(NAME_ATTR, launchConfiguration.getName());
		launchConfigurationElement.setAttribute(BOOKMARK_ATTR, Boolean.toString(launchConfigurationNode.isBookmarked()));
		return launchConfigurationElement;
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
