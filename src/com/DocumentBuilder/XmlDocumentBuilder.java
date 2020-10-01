package com.DocumentBuilder;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xmlEditTool.MeasureSets;

public class XmlDocumentBuilder extends Elements {
	public static Document doc;
	public static Element elem;
	private static Element patientRole;
	private static Element patient;
	private static String ccnExtension;
	static String birthTime = "19960125";
	//static String birthtime = FileCreator.DoBTextbox.getText(); 
	public static String patientID = "";
	
	private static void setCCN(String ccExtension)
	{
		ccnExtension = ccExtension;
	}
	
	public static void setPatientID(String patientId) {
		patientID = patientId;
	}
	
	public static Document documentFactory() throws ParserConfigurationException 
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		Comment cmt = doc.createComment("Date: July 17, 2019 This QRDA file is written based on CMS72v2_CatI_QrdaSample.xml from eMeasure April 1, 2013 release. "
				+ "It is used for functional test for msr_id (160) of Measure Engine 4.1\n");
		doc.appendChild(cmt);
		
		return doc;
	}

	public static Element clinicalDocumentRoot() throws ParserConfigurationException 
	{
		// Create person root element
		elem = doc.createElement("ClinicalDocument");
		((Element)elem).setAttribute("xmlns", "urn:hl7-org:v3");
		((Element)elem).setAttribute("xmlns:sdtc", "urn:hl7-org:sdtc");
		((Element)elem).setAttribute("xmlns:voc", "urn:hl7-org:v3/voc");
		((Element)elem).setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		((Element)elem).setAttribute("xsi:schemaLocation", "urn:hl7-org:v3 CDA/infrastructure/cda/CDA_SDTC.xsd");
		doc.appendChild(elem);		
		return elem;
	}
	
	public static void qrdaHeader(String CCN) throws ParserConfigurationException
	{
		// QRDA Header Comment
		comment(doc, elem, "QRDA Header");
		
		// realmCode US
		element(doc, elem, "realmCode", "code", "US");
		
		// typeId
		element(doc, elem, "typeId", "extension", "POCD_HD000040", 
												"root", "2.16.840.1.113883.1.3");
		
		setCCN(CCN);
		
		// templateId
		String[] comments = {"US Realm Header (V3)", "QRDA Category I Framework (V4)", "QDM-Based QRDA (V6)", "QRDA Category I Report - CMS (V6)"};
		String[] roots = {"2.16.840.1.113883.10.20.22.1.1", "2.16.840.1.113883.10.20.24.1.1", "2.16.840.1.113883.10.20.24.1.2", "2.16.840.1.113883.10.20.24.1.3"};
		String[] extensions = {"2015-08-01", "2017-08-01", "2018-10-01", "2019-02-01"};
		
		for(int i = 0; i < 4; i++)
		{
			comment(doc, elem, comments[i]);
			element(doc, elem, "templateId", "root", roots[i], 
											"extension", extensions[i]);
		}
		
		qrdaDocumentTypeCodes();
			recordTarget();
			author();
			custodian();
			informationRecipient();
			participant();
			documentationOf();
	}
	
	private static void qrdaDocumentTypeCodes()
	{
		element(doc, elem, "id", "root", "203e0b60-0c5c-4881-9f65-82338b201c60");
		
		comment(doc, elem, "QRDA document type code");
		
		// QRDA document type code
		element(doc, elem, "code", "code", "55182-0", 
											"codeSystem", "2.16.840.1.113883.6.1", 
											"codeSystemName", "LOINC", 
											"displayName", "Quality Measure Report");
		
		// QRDA title
		element(doc, elem, "title", "QRDA Incidence Report for 2020 Reporting Period for HQR");
		
		// Effective time
		element(doc, elem, "effectiveTime", "value", "20200331124411");
		
		// confidentiality code
		element(doc, elem, "confidentialityCode", "code", "N", 
															"codeSystem", "2.16.840.1.113883.5.25");
		
		// language code
		element(doc, elem, "languageCode", "code", "en");		
	}
	
		private static void recordTarget()
		{
			// start of patient
			comment(doc, elem, "REPORTED PATIENT");
			
			// record target
			Element target = element(doc, elem, "recordTarget");
			
			// patient role
			Element patientRole = element(doc, target, "patientRole");
			
			// patient id
			element(doc, patientRole, "id", "root", "2.16.840.1.113883.3.249.15", 
														"extension", randPatId(patientID));
			
			// medicare HIC number
			element(doc, patientRole, "id", "root", "2.16.840.1.113883.4.572", 
														"extension", ccnExtension + "HIC number");
			
			// Medicare Beneficiary Identifier
			element(doc, patientRole, "id", "root", "2.16.840.1.113883.4.927", 
													 "extension", "Medicare_Beneficiary_Identifier_goes_here");
			
			
			patientAddr(patientRole, "H");
			telecom(patientRole, "HP");
			patient(patientRole);
		}
		
			private static void patient(Element elem)
			{
				// patient
				Element patient = element(doc, elem, "patient");
				
				// name
				Element name = element(doc, patient, "name");
				
				// prefix
				element(doc, name, "prefix", "Mr.");
				
				// patient first name
				element(doc, name, "given", "Ronald");
				
				// patient middle name
				element(doc, name, "given", "qualifier", "CL", "Ulysses");

				// patient last name
				element(doc, name, "family", "Swanson");
				
				// gender code
				element(doc, patient, "administrativeGenderCode", "code", "M", 
																			"codeSystem", "2.16.840.1.113883.5.1");
				
				// birth time
				element(doc, patient, "birthTime", "value", birthTime);
				
				// marital status code
				element(doc, patient, "maritalStatusCode", "code", "M", 
																	"codeSystem", "2.16.840.1.113883.5.2", 
																	"codeSystemName", "MaritalStatusCode", 
																	"displayName", "Married");
				
				// race code
				element(doc, patient, "raceCode", "code", "2106-3", 
												"codeSystem", "2.16.840.1.113883.6.238", 
												"displayName", "White");
				
				// sdtc race code
				element(doc, patient, "sdtc:raceCode", "code", "2054-5", 
																"codeSystem", "2.16.840.1.113883.6.238", 
																"displayName", "Black or African American");
				
				// ethnic group code
				element(doc, patient, "ethnicGroupCode", "code", "2186-5", 
																	"codeSystem", "2.16.840.1.113883.6.238", 
																	"displayName", "Not Hispanic or Latino");
			}
			
		private static void author() 
		{
			comment(doc, elem, "Author who is a person");
			
			// author
			Element author = element(doc, elem, "author");
			
			// author time
			element(doc, author, "time", "value", "20200331124411");
			
			Element assAuth = element(doc, author, "assignedAuthor");
			// author id
			element(doc, assAuth, "id", "root", "2.16.840.1.113883.4.6", 
												"extension", "1234567893");
			
			// author code
			element(doc, assAuth, "code", "code", "200000000X", 
												  "codeSystem", "2.16.840.1.113883.6.101", 
												  "displayName", "Allopathic &amp; Osteopathic Physicians");
			
			// author address
			hospAddr(assAuth,"WP");
			
			// author telecom
			telecom(assAuth, "WP");
			
			comment(doc, assAuth, "Quality Manager");
			
			// assigned person
			Element assignedPerson = element(doc, assAuth, "assignedPerson");
			
			// assigned person name
			Element name = element(doc, assignedPerson, "name");
			
			element(doc, name, "given", "Anne");
			
			element(doc, name, "family", "Perkins");
			
			element(doc, name, "suffix", "RN");	
		}
		
		private static void custodian()
		{
			comment(doc, elem, "Custodian");
			
			// custodian
			Element custodian = element(doc, elem, "custodian");
					
			// assignedCustodian
			Element assignedCustodian = element(doc, custodian, "assignedCustodian");
			
			// representedCustodianOrganization
			Element representedCustodianOrganization = element(doc, assignedCustodian, "representedCustodianOrganization");
			
			//NPI
			comment(doc, representedCustodianOrganization, "National Provider Identifier");
			element(doc, representedCustodianOrganization, "id", "root", "2.16.840.1.113883.4.6", "nullFlavor", "NA");
			
			// CMS CCN
			comment(doc, representedCustodianOrganization, "CMS Certification CCN");
			element(doc, representedCustodianOrganization, "id", "root", "2.16.840.1.113883.4.336", 
																		  "extension", ccnExtension);
			
			//Tax ID Number
			comment(doc, representedCustodianOrganization, "Tax ID Number");
			element(doc, representedCustodianOrganization, "id", "root", "2.16.840.1.113883.4.2", "extension", "222222289");
			
			// Joint Commission's HCO
			comment(doc, representedCustodianOrganization, "Joint Commission's HCO");
			element(doc, representedCustodianOrganization, "id", "root", "1.3.6.1.4.1.33895", "extension", "3333333");
			
			// custodian organization name
			element(doc, representedCustodianOrganization, "name", "George Washington Memorial Hospital");
			
			// custodian telephone
			telecom(representedCustodianOrganization, "WP");
			
			// custodian address
			hospAddr(representedCustodianOrganization, "WP");
		}
		
		private static void informationRecipient()
		{
			// information recipient
			Element informationRecipient = element(doc, elem, "informationRecipient");
			
			// intended recipient
			Element intendedRecipient = element(doc, informationRecipient, "intendedRecipient");
			
			// information recipient id
			element(doc, intendedRecipient, "id", "root", "2.16.840.1.113883.3.249.7", 
														   "extension", "HQR_PI");
		}
		
		private static void participant()
		{
			// participant
			Element participant = element(doc, elem, "participant", "typeCode", "DEV");
			
			// associated entity
			Element associatedEntity = element(doc, participant, "associatedEntity", "classCode", "RGPR");
			
			comment(doc, associatedEntity, "CMS EHR Certification Number");
			
			// participant id (should contain    15E  )
			element(doc, associatedEntity, "id", "root", "2.16.840.1.113883.3.2074.1", 
														  "extension", "A015E01CFES9EAB");
		}
		
		private static void documentationOf()
		{
			comment(doc, elem, "NPI and TIN may not be applicable to hospitals, the entire documentationOf element is an optional field");
			
			// documentation element
			Element documentationOf = element(doc, elem, "documentationOf", "typeCode", "DOC");
			
			// service event
			Element serviceEvent = element(doc, documentationOf, "serviceEvent", "classCode", "PCPR");
			
			comment(doc, serviceEvent, "Care Provision");
			
			// effective time
			Element effTm = element(doc, serviceEvent, "effectiveTime");
			
			// start of reporting period
			element(doc, effTm, "low", "value", MeasureSets.rptPrd[0]);
			
			// end of reporting period
			element(doc, effTm, "high", "value", MeasureSets.rptPrd[1]);
			
			// performer
			Element perf = element(doc, serviceEvent, "performer", "typeCode", "PRF");
			
			//assigned entity
			Element assignedEntity = element(doc, perf, "assignedEntity");
			
			comment(doc, assignedEntity, "This is the provider NPI");
			
			comment(doc, assignedEntity, "NPI may not be applicable, hospitals may submit nullFlavor");
			
			// provider NPI
			element(doc, assignedEntity, "id", "root", "2.16.840.1.113883.4.6", "nullFlavor", "NA");
			
			comment(doc, assignedEntity, "TIN may not be applicable, hospitals may submit nullFlavor");
			// represented organization
			Element representedOrganization = element(doc, assignedEntity, "representedOrganization");
			
			// represented organization id
			element(doc, representedOrganization, "id", "root", "2.16.840.1.113883.4.2", "nullFlavor", "NA");
		}
	
	
	private static void telecom(Element ele, String primary)					// universal telecom 
	{
		if(primary == "HP") {
			Element telecom = doc.createElement("telecom");
			((Element)telecom).setAttribute("use", "HP");
			((Element)telecom).setAttribute("value", "tel:(781)555-1212");
			ele.appendChild(telecom);
		}
		if(primary == "WP") {
			Element telecom = doc.createElement("telecom");
			((Element)telecom).setAttribute("use", "WP");
			((Element)telecom).setAttribute("value", "tel:(555)555-1212");
			ele.appendChild(telecom);

		}
	}

	private static void patientAddr(Element ele, String use) 					// universal PATIENT address
	{
		Element addr = doc.createElement("addr");
		if(use == "H") 
			((Element)addr).setAttribute("use", "HP");
		if(use == "WP")
			((Element)addr).setAttribute("use", "WP");
		ele.appendChild(addr);
		
		Element street = doc.createElement("streetAddressLine");
		street.appendChild(doc.createTextNode("11350 Constitution Hwy"));
		addr.appendChild(street);
		
		Element city = doc.createElement("city");
		city.appendChild(doc.createTextNode("Montpelier Station"));
		addr.appendChild(city);
		
		Element state = doc.createElement("state");
		state.appendChild(doc.createTextNode("VA"));
		addr.appendChild(state);
		
		Element postCd = doc.createElement("postalCode");
		postCd.appendChild(doc.createTextNode("22957"));
		addr.appendChild(postCd);
		
		Element country = doc.createElement("country");
		country.appendChild(doc.createTextNode("US"));
		addr.appendChild(country);
	}
	
	private static void hospAddr(Element ele, String use) 									// universal HOSPITAL address
	{
		Element addr = doc.createElement("addr");
		((Element)addr).setAttribute("use", use);
		ele.appendChild(addr);
		
		Element street = doc.createElement("streetAddressLine");
		street.appendChild(doc.createTextNode("1776 Memorial Ave"));
		addr.appendChild(street);
		
		Element street2 = doc.createElement("streetAddressLine");
		street2.appendChild(doc.createTextNode("Suite 1776"));
		addr.appendChild(street2);		
		
		Element city = doc.createElement("city");
		city.appendChild(doc.createTextNode("Mt Vernon"));
		addr.appendChild(city);
		
		Element state = doc.createElement("state");
		state.appendChild(doc.createTextNode("VA"));
		addr.appendChild(state);
		
		Element postCd = doc.createElement("postalCode");
		postCd.appendChild(doc.createTextNode("22309"));
		addr.appendChild(postCd);
		
		Element country = doc.createElement("country");
		country.appendChild(doc.createTextNode("US"));
		addr.appendChild(country);
	}

	private static String transformDocument() throws TransformerException, ParserConfigurationException 
	{
		// Transform document to XML string
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		StringWriter writer = new StringWriter();
		
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		
		String personXmlStringValue = writer.getBuffer().toString();
		
		return personXmlStringValue;
	}
	
	private static String randPatId(String title) {
		if (title == "") {
			String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			StringBuilder builder = new StringBuilder();
			int count = 64;
			while(count-- != 0) {
				int index = (int)(Math.random()*chars.length());
				builder.append(chars.charAt(index));
			}
			
			return builder.toString();
		}
		else {
			return title;
		}
	}
}
