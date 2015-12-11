package com.ability.ease.auto.dataStructure.common.AttibuteXMLParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CreateUIAttributeXMLFromMappings {
	/*public enum ObjectType{
		ENDPOINT("endpoint"), ACCOUNTS("accounts"),POLICIES("policies");

		String value;

		ObjectType(String s){
			this.value=s;
		}
		public String getValue() {
	        return value;
	    } 
	}*/

	/*String locator;
	String displayName;
	String epTypeString;*/
	//List<AttributeNode> lsattrNodes=new ArrayList<AttributeNode>();


	/*public void CreateUIAttributeXMLFromMappingsMethod() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{
		String mappingXMLFileName=null;


		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(mappingXMLFileName.trim()));
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		System.out.println("XPath Xpression:"+xpathExpression);
		XPathExpression expr = xpath.compile(xpathExpression);
		NodeList lsAttributes=(NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		for(int i=0;i<lsAttributes.getLength();i++){
			Node attrNode=lsAttributes.item(i);
		    NamedNodeMap mapAttributes=attrNode.getAttributes();
		    locator=mapAttributes.getNamedItem("etname").getNodeValue();
		    displayName=mapAttributes.getNamedItem("displayname").getNodeValue();

		    lsattrNodes.add(new AttributeNode(locator, displayName));
		}
		CreateUIAttributeXML(lsattrNodes);
	}

	public void CreateUIAttributeXML(List<AttributeNode> ls){
		if(ls!=null){
			try {

				DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

				DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

				Document document = documentBuilder.newDocument();

				// root element 'UIAttributes'
				Element root = document.createElement("UIAttributes");
				document.appendChild(root);

				for(AttributeNode node:ls){
					// UIAttribute element
					Element UIAttribute = document.createElement("UIAttribute");

					root.appendChild(UIAttribute);

					//you can also use staff.setAttribute("id", "1") for this

					// attrName element
					Element attrName = document.createElement("locator");
					attrName.appendChild(document.createTextNode(node.locator.trim()));
					UIAttribute.appendChild(attrName);

					// style element
					Element style = document.createElement("style");
					style.appendChild(document.createTextNode("Text"));
					UIAttribute.appendChild(style);

					// displayName element
					Element displayName = document.createElement("displayName");
					displayName.appendChild(document.createTextNode(node.displayName.trim()));
					UIAttribute.appendChild(displayName);

				}
				// create the xml file
				//transform the DOM Object to an XML File
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(new File(epTypeString+"_UIAttributes.xml"));

				// If you use
				// StreamResult result = new StreamResult(System.out);
				// the output will be pushed to the standard output ...
				// You can use that for debugging 

				transformer.transform(domSource, streamResult);

				System.out.println("Done creating UIAttribute XML File");

			} catch (ParserConfigurationException pce) {
				pce.printStackTrace();
			} catch (TransformerException tfe) {
				tfe.printStackTrace();
			}
		}
	}

	private class AttributeNode{
		String locator,displayName;

		AttributeNode(String l,String dn){
			this.locator=l;
			this.displayName=dn;
		}

	}*/

	public String[] CreateUIAttribuitesDescriptionPropsFromXML(String mappingXMLFileName, String xpathexpression) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException{

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(mappingXMLFileName.trim()));

		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		if(xpathexpression==null||xpathexpression.trim().equals(""))
			xpathexpression="//UIAttribute//displayName";

		XPathExpression expr = xpath.compile(xpathexpression);
		NodeList lsAttributes=(NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		String[] descprops=new String[lsAttributes.getLength()];

		for(int i=0;i<lsAttributes.getLength();i++){

			Node attrNode=lsAttributes.item(i);

			NodeList displayname=attrNode.getChildNodes();
			if(!displayname.item(0).getNodeValue().trim().equalsIgnoreCase(""))	
				descprops[i]=new String(displayname.item(0).getNodeValue().trim());
		}		  	
		return descprops;
	}
}
