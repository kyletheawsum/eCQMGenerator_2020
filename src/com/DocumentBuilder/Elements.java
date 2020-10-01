package com.DocumentBuilder;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Elements {
	
	static Comment comment(Document doc, Document elem, String comment ) {
		Comment cmt = doc.createComment(comment);
		elem.appendChild(cmt);
		
		return cmt;
	}
	
	static Comment comment(Document doc, Element elem, String comment ) {
		Comment cmt = doc.createComment(comment);
		elem.appendChild(cmt);
		
		return cmt;
	}

	// elements with text node
	static Element element(Document doc, Element appendedElement, String elementName, String textNode) {
		Element createdElement = doc.createElement(elementName);
		createdElement.appendChild(doc.createTextNode(textNode));
		appendedElement.appendChild(createdElement);

		return createdElement;
	}
	
	// elements with attribute and text node
	static Element element(Document doc, Element appendedElement, String elementName, String attributeName, String attributeValue, String textNode) {
		Element createdElement = doc.createElement(elementName);
		createdElement.setAttribute(attributeName, attributeValue);
		createdElement.appendChild(doc.createTextNode(textNode));
		appendedElement.appendChild(createdElement);

		return createdElement;
	}

	
	// element with no attribute
	static Element element(Document doc, Element appendedElement, String elementName) {
		Element createdElement = doc.createElement(elementName);					
		appendedElement.appendChild(createdElement);
		
		return createdElement;
	}
	
	// element with one attribute
	static Element element(Document doc, Element appendedElement, String elementName, String attributeName, String attributeValue) {
		Element createdElement = doc.createElement(elementName);				
		((Element)createdElement).setAttribute(attributeName, attributeValue);
		appendedElement.appendChild(createdElement);
		
		return createdElement;
	}
	
	// element with two attributes
	static Element element(Document doc, Element appendedElement, String elementName, String attributeName1, String attributeValue1, String attributeName2, String attributeValue2) {
		Element createdElement = doc.createElement(elementName);					
		((Element)createdElement).setAttribute(attributeName1, attributeValue1);
		((Element)createdElement).setAttribute(attributeName2, attributeValue2);
		appendedElement.appendChild(createdElement);
		
		return createdElement;
	}
	
	// element with three attributes
	static Element element(Document doc, Element appendedElement, String elementName, String attributeName1, String attributeValue1, String attributeName2, String attributeValue2, String attributeName3, String attributeValue3) {
		Element createdElement = doc.createElement(elementName);					
		((Element)createdElement).setAttribute(attributeName1, attributeValue1);
		((Element)createdElement).setAttribute(attributeName2, attributeValue2);
		((Element)createdElement).setAttribute(attributeName3, attributeValue3);
		appendedElement.appendChild(createdElement);
		
		return createdElement;
	}

	// element with four attributes
	static Element element(Document doc, Element appendedElement, String elementName, String attributeName1, String attributeValue1, String attributeName2, String attributeValue2, String attributeName3, String attributeValue3, String attributeName4, String attributeValue4) {
		Element createdElement = doc.createElement(elementName);					
		((Element)createdElement).setAttribute(attributeName1, attributeValue1);
		((Element)createdElement).setAttribute(attributeName2, attributeValue2);
		((Element)createdElement).setAttribute(attributeName3, attributeValue3);
		((Element)createdElement).setAttribute(attributeName4, attributeValue4);
		appendedElement.appendChild(createdElement);
		
		return createdElement;
	}
	
	// element with five attributes
	static Element element(Document doc, Element appendedElement, String elementName, String attributeName1, String attributeValue1, String attributeName2, String attributeValue2, String attributeName3, String attributeValue3, String attributeName4, String attributeValue4, String attributeName5, String attributeValue5) {
		Element createdElement = doc.createElement(elementName);					
		((Element)createdElement).setAttribute(attributeName1, attributeValue1);
		((Element)createdElement).setAttribute(attributeName2, attributeValue2);
		((Element)createdElement).setAttribute(attributeName3, attributeValue3);
		((Element)createdElement).setAttribute(attributeName4, attributeValue4);
		((Element)createdElement).setAttribute(attributeName5, attributeValue5);
		appendedElement.appendChild(createdElement);
		
		return createdElement;
	}
	
	// element with six attributes
	static Element element(Document doc, Element appendedElement, String elementName, String attributeName1, String attributeValue1, String attributeName2, String attributeValue2, String attributeName3, String attributeValue3, String attributeName4, String attributeValue4, String attributeName5, String attributeValue5, String attributeName6, String attributeValue6) {
		Element createdElement = doc.createElement(elementName);					
		((Element)createdElement).setAttribute(attributeName1, attributeValue1);
		((Element)createdElement).setAttribute(attributeName2, attributeValue2);
		((Element)createdElement).setAttribute(attributeName3, attributeValue3);
		((Element)createdElement).setAttribute(attributeName4, attributeValue4);
		((Element)createdElement).setAttribute(attributeName5, attributeValue5);
		((Element)createdElement).setAttribute(attributeName6, attributeValue6);
		appendedElement.appendChild(createdElement);
		
		return createdElement;
	}
	
	static void reportingParametersSection(Document doc, Element elem, String rptPrdStart, String rptPrdEnd) 
	{
		Elements.comment(doc, elem, "<!-- 				 \r\n" + 
				"        ***************************************************************** 				 \r\n" + 
				"        Reporting Parameters Section 				 \r\n" + 
				"        ***************************************************************** 			 \r\n" + 
				"      -->");
		
		// component
		Element component = Elements.element(doc, elem, "component");
		
		// component section
		Element section = Elements.element(doc, component, "section");
		
		// template id 1
		Elements.element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.17.2.1");
		
		// template id 2
		Elements.element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.17.2.1.1", 
												 	 "extension", "2016-03-01");
		
		// reporting parameters code
		Elements.element(doc, section, "code", "code", "55187-9", 
											   "codeSystem", "2.16.840.1.113883.6.1");
		
		// reporting parameters title
		Elements.element(doc, section, "title", "Reporting Parameters");
		
		// reporting parameters period
		Element text = Elements.element(doc, section, "text");
		
		Element list = Elements.element(doc, text, "list");
		
		Elements.element(doc, list, "item", "Reporting period: 01 Jan 2020 - 31 Mar 2020");
		
		// reporting parameters entry
		Element entry = Elements.element(doc, section, "entry", "typeCode", "DRIV");
		
		// reporting parameters act
		Element act = Elements.element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN");
		
		Elements.comment(doc, act, "Reporting Parameters Act");
		
		Elements.element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.17.3.8");
		
		Elements.comment(doc, act, "Reporting Parameters Act CMS");
		
		Elements.element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.17.3.8.1", "extension", "2016-03-01");
		
		// act id
		Elements.element(doc, act, "id", "root", "4731a580-9397-4446-8926-75358370e042");
		
		// reporting parameters act code
		Elements.element(doc, act, "code", "code", "252116004", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Observation Parameters");
		
		Elements.comment(doc, act, "Reporting Period START/END Date");
		
		Element effectiveTime = Elements.element(doc, act, "effectiveTime");
		
		Elements.element(doc, effectiveTime, "low", "value", rptPrdStart);
		
		Elements.element(doc, effectiveTime, "high", "value", rptPrdEnd);
	}
	
	static void paymentSection(Document doc, Element elem)
	{	
		Elements.comment(doc, elem, "QDM Datatype: Patient Characteristic, Payer");
		
		// payer entry
		Element entry = Elements.element(doc, elem, "entry");
		
		// observation
		Element observation = Elements.element(doc, entry, "observation", "classCode", "OBS", "moodCode", "EVN");
		
		// template id
		Elements.element(doc, observation, "templateId", "root", "2.16.840.1.113883.10.20.24.3.55");
		
		// id
		Elements.element(doc, observation, "id", "root", "4ddf1cc3-e325-472e-ad76-b2c66a5ee164");
		
		// code
		Elements.element(doc, observation, "code", "code", "48768-6", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Payment source");
		
		// status code
		Elements.element(doc, observation, "statusCode", "code", "completed");
		
		// effective time
		Element effectiveTime = Elements.element(doc, observation, "effectiveTime");
		
		Elements.element(doc, effectiveTime, "low", "value", "20200101");
		
		Elements.element(doc, effectiveTime, "high", "value", "20201231");
		
		Elements.element(doc, observation, "value", "code", "1", "codeSystem", "2.16.840.1.113883.3.221.5", "codeSystemName", "Source of Payment Typology", "displayName", "Medicare", "xsi:type", "CD");
	}
}
