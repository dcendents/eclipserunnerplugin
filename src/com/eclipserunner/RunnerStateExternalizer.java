package com.eclipserunner;

import static com.eclipserunner.Messages.Message_uncategorized;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

import com.eclipserunner.model.ICategoryNode;
import com.eclipserunner.model.ILaunchNode;
import com.eclipserunner.model.IRunnerModel;
import com.eclipserunner.model.RunnerModelProvider;
import com.eclipserunner.model.impl.CategoryNode;
import com.eclipserunner.model.impl.LaunchNode;

/**
 * Helper class for saving and loading plugin state.
 *
 * @author bary
 */
public class RunnerStateExternalizer {

	private static final String XML_VERSION_ATTR    = "version";
	private static final String XML_VERSION_VALUE   = "1.0";

	private static final String VERSION_ATTR        = "version";
	private static final String VERSION_VALUE       = "1.0.0";

	private static final String NAME_ATTR           = "name";
	private static final String BOOKMARK_ATTR       = "bookmark";

	private static final String ROOT_NODE_NAME      = "runner";
	private static final String CATEGORY_NODE_NAME  = "category";
	private static final String LAUNCH_NODE_NAME    = "launchConfiguration";


	/**
	 * Load plugin state from given file.
	 *
	 * @param inputFile Plugin state file.
	 * @throws CoreException
	 */
	public static void readRunnerModelFromFile(File inputFile) throws CoreException {
		if (!inputFile.exists()) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "File not found '" + inputFile.getAbsolutePath() + "'"));
		}
		if (inputFile.length() == 0) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "File contains no data '" + inputFile.getAbsolutePath() + "'"));
		}

		Document runnerModelDocument = createDocumentFromRunnerModelFile(inputFile);
		IRunnerModel runnerModel = RunnerModelProvider.getInstance().getDefaultModel();

		populateRunnerModelFromDocument(runnerModel, runnerModelDocument);
		populateRunnerModelWithRemainingUncategorizedLaunchConfigurations(runnerModel);

	}

	private static Document createDocumentFromRunnerModelFile(File inputFile) throws CoreException {
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

	private static void populateRunnerModelFromDocument(IRunnerModel runnerModel, Document document) throws CoreException {
		Element runnerNode = document.getDocumentElement();
		String readVersion = runnerNode.getAttribute(VERSION_ATTR);
		if (!readVersion.equals(VERSION_VALUE)) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "State file version '" + readVersion + "' not supported"));
		}

		NodeList categoryNodeList = runnerNode.getElementsByTagName(CATEGORY_NODE_NAME);
		for (int categoryIndex = 0; categoryIndex < categoryNodeList.getLength(); categoryIndex++) {
			Element categoryElement = (Element) categoryNodeList.item(categoryIndex);
			String categoryNodeName = categoryElement.getAttribute(NAME_ATTR);

			ICategoryNode categoryNode = null;
			if (Message_uncategorized.equals(categoryNodeName)) {
				categoryNode = runnerModel.getDefaultCategoryNode();
			}
			else {
				categoryNode = new CategoryNode(categoryNodeName);
				runnerModel.addCategoryNode(categoryNode);
			}

			NodeList launchNodeList = categoryElement.getElementsByTagName(LAUNCH_NODE_NAME);
			for (int j = 0; j < launchNodeList.getLength(); j++) {
				Element launchElement = (Element) launchNodeList.item(j);
				ILaunchConfiguration launchConfiguration = getLaunchConfigurationByName(launchElement.getAttribute(NAME_ATTR));

				if (launchConfiguration != null) {
					boolean isBookmarked = Boolean.valueOf(launchElement.getAttribute(BOOKMARK_ATTR));

					ILaunchNode launchNode = new LaunchNode();
					launchNode.setLaunchConfiguration(launchConfiguration);
					launchNode.setBookmarked(isBookmarked);

					categoryNode.add(launchNode);
				}
			}
		}
	}

	private static void populateRunnerModelWithRemainingUncategorizedLaunchConfigurations(IRunnerModel runnerModel) throws CoreException {
		Collection<ICategoryNode> categoryNodes = runnerModel.getCategoryNodes();

		// Get all launch configurations
		List<ILaunchConfiguration> launchConfigurations = new ArrayList<ILaunchConfiguration>(Arrays.asList(getLaunchManager().getLaunchConfigurations()));

		// Skip configurations which are already in model
		for(ICategoryNode categoryNode : categoryNodes) {
			for (ILaunchNode launchNode : categoryNode.getLaunchNodes()) {
				for (Iterator<ILaunchConfiguration> launchConfigurationIterator = launchConfigurations.iterator(); launchConfigurationIterator.hasNext();) {
					ILaunchConfiguration launchConfiguration = launchConfigurationIterator.next();
					if (launchConfiguration.equals(launchNode.getLaunchConfiguration())) {
						launchConfigurationIterator.remove();
						break;
					}
				}
			}
		}

		// add remaining configurations
		for(ILaunchConfiguration launchConfiguration : launchConfigurations) {
			ILaunchNode launchNode = new LaunchNode();
			launchNode.setLaunchConfiguration(launchConfiguration);
			runnerModel.addLaunchNode(launchNode);
		}
	}


	/**
	 * Load plugin default state.
	 *
	 * @throws CoreException
	 */
	public static void readDefaultRunnerModel() throws CoreException {
		IRunnerModel runnerModel = RunnerModelProvider.getInstance().getDefaultModel();
		ILaunchManager launchManager = getLaunchManager();
		for (ILaunchConfiguration launchConfiguration : launchManager.getLaunchConfigurations()) {
			LaunchNode launchNode = new LaunchNode();
			launchNode.setLaunchConfiguration(launchConfiguration);
			runnerModel.getDefaultCategoryNode().add(launchNode);
		}
	}

	/**
	 * Save plugin state into given file.
	 *
	 * @param outputFile Plugin state file.
	 * @throws CoreException
	 */
	public static void writeRunnerModelToFile(File outputFile) throws CoreException {
		try {
			IRunnerModel runnerModel = RunnerModelProvider.getInstance().getDefaultModel();
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			try {
				Document document = createDocumentFromRunnerModel(runnerModel);
				writeDocumentToStream(document, outputStream);
				outputStream.flush();
			} finally {
				outputStream.close();
			}
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR, RunnerPlugin.PLUGIN_ID, "Failed to save runner state", e));
		}
	}

	private static Document createDocumentFromRunnerModel(IRunnerModel runnerModel) throws CoreException {
		Document document = createDocument();

		Element runnerElement = document.createElement(ROOT_NODE_NAME);
		runnerElement.setAttribute(VERSION_ATTR, VERSION_VALUE);
		document.appendChild(runnerElement);

		// create the category nodes
		for (ICategoryNode categoryNode : runnerModel.getCategoryNodes()) {
			runnerElement.appendChild(
					createCategoryElement(categoryNode, document)
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

	private static Element createCategoryElement(ICategoryNode category, Document document) {
		Element categoryElement = document.createElement(CATEGORY_NODE_NAME);
		categoryElement.setAttribute(NAME_ATTR, category.getName());
		for (ILaunchNode launchNode : category.getLaunchNodes()) {
			categoryElement.appendChild(
					createLaunchElement(launchNode, document)
			);
		}
		return categoryElement;
	}

	private static Element createLaunchElement(ILaunchNode launchNode, Document document) {
		Element launchElement = document.createElement(LAUNCH_NODE_NAME);
		ILaunchConfiguration launchConfiguration = launchNode.getLaunchConfiguration();
		launchElement.setAttribute(NAME_ATTR, launchConfiguration.getName());
		launchElement.setAttribute(BOOKMARK_ATTR, Boolean.toString(launchNode.isBookmarked()));
		return launchElement;
	}

	private static void writeDocumentToStream(Document document, OutputStream outputStream) throws CoreException {
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

	private static ILaunchManager getLaunchManager() {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		return launchManager;
	}

	private static ILaunchConfiguration getLaunchConfigurationByName(String name) throws CoreException {
		for(ILaunchConfiguration launchConfiguration : getLaunchManager().getLaunchConfigurations()) {
			if (launchConfiguration.getName().equals(name)) {
				return launchConfiguration;
			}
		}
		return null;
	}

}
