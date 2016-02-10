package com.ensense.insense.core.webservice.util;

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.xs.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import jlibs.xml.sax.XMLDocument;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jlibs.xml.xsd.XSInstance;
import jlibs.xml.xsd.XSParser;

/**
* <h1>RestServiceUtil</h1>
* XMLGeneratorFromXSD contains the methods, helps to generate the XML from XSD
*
*/
public class XMLGeneratorFromXSD {
	/**
	 * It returns the generated XML from XSD
	 * @param requestObjectName This will be the root element of the XML
	 * @param generateOptionalElements This is the flag to decide the optional elements are needed
	 * @param filename this is the XSD file name
	 * @return String this is the request XML
	 */
	public static String generateXML(String requestObjectName, Boolean generateOptionalElements, String filename) {
		String requestXML = "";
		try {
			//String filename = "retirement-plan-transaction-rs-v1.0.xsd";
			// instance.
			
			final Document doc = loadXsdDocument(filename);

			// Find the docs root element and use it to find the targetNamespace
			final Element rootElem = doc.getDocumentElement();
			String targetNamespace = null;
			if (rootElem != null && rootElem.getNodeName().equals("xsd:schema")) {
				targetNamespace = rootElem.getAttribute("targetNamespace");
			}

			// Parse the file into an XSModel object
			XSModel xsModel = new XSParser().parse(filename);
			

			// Define defaults for the XML generation
			XSInstance instance = new XSInstance();
			instance.minimumElementsGenerated = 1;
			instance.maximumElementsGenerated = 1;
			instance.generateDefaultAttributes = true;
			instance.generateOptionalAttributes = true;
			instance.maximumRecursionDepth = 0;
			instance.generateAllChoices = true;
			instance.showContentModel = false;
			instance.generateOptionalElements = generateOptionalElements;
			instance.minimumListItemsGenerated = 1;
			
			// Build the sample xml doc
			// Replace first param to XMLDoc with a file input stream to write
			// to file
			if(!requestObjectName.isEmpty() && requestObjectName!= null){
			StringWriter writer = new StringWriter();
			//requestObjectName = requestObjectName.substring(0, 1).toUpperCase() + requestObjectName.substring(1);
			QName rootElement = new QName(targetNamespace, requestObjectName);
			StreamResult streamResult = new StreamResult(System.out);
			XSElementDeclaration root = getRoot(targetNamespace,requestObjectName,xsModel);
			if( root == null){
				requestObjectName = requestObjectName.substring(0, 1).toUpperCase() + requestObjectName.substring(1);
				rootElement = new QName(targetNamespace, requestObjectName);
				root = getRoot(targetNamespace,requestObjectName,xsModel);
			}
			
			if (root != null){
			XMLDocument sampleXml = new XMLDocument(
					new StreamResult(writer), true, 4, null);
			instance.generate(xsModel, rootElement, sampleXml);
			requestXML = writer.getBuffer().toString().replaceAll("|\r", "");
			}
			}
			return requestXML;
		} catch (TransformerConfigurationException e) {
			
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		//requestXML = requestXML.replaceAll(">\\s*<", "><");
		return requestXML;
	}

	
	public static XSElementDeclaration getRoot(String targetNamespace, String requestObjectName, XSModel xsModel) {
		QName rootElement = new QName(targetNamespace, requestObjectName);
 		XSElementDeclaration root = xsModel.getElementDeclaration(rootElement.getLocalPart(), rootElement.getNamespaceURI());
		return root;
	}
	/**
	 * This is to load the XSD file as a document
	 * @param inputName This is the XSD file name
	 * @return Document
	 */
	public static Document loadXsdDocument(String inputName) {
		final String filename = inputName;

		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		Document doc = null;

		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final File inputFile = new File(filename);
			doc = builder.parse(inputFile);
		} catch (final Exception e) {
			e.printStackTrace();
			// throw new ContentLoadException(msg);
		}
		return doc;
	}
}
