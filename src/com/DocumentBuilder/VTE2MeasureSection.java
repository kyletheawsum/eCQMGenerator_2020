package com.DocumentBuilder;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.Measures.VTE;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureParameters;
import com.xmlEditTool.MeasureSets;

public class VTE2MeasureSection extends Elements {
	private static Document doc;
	private static Element elem;
	private static String reportingPeriodStart;
	private static String reportingPeriodEnd;
	
	private static String admission;
	public static String discharge;
	
	public VTE2MeasureSection(String ccn) throws DOMException, ParseException
	{
		try {
			reportingPeriodStart = MeasureSets.rptPrd[0];
			reportingPeriodEnd = MeasureSets.rptPrd[1];
			admission = MeasureSets.startDate();
			discharge = MeasureSets.endDate();
			
			VTE.condition += "_VTE-2_" + discharge;
			XmlDocumentBuilder.setPatientID(VTE.condition);
			
			doc = XmlDocumentBuilder.documentFactory();
			elem = XmlDocumentBuilder.clinicalDocumentRoot();
			XmlDocumentBuilder.qrdaHeader(ccn);
			qrdaBody();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			FileCreator.errorMsgTxtbox.setText(e.toString());
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - VTE-2 QRDA body", e.getCause());
		}
	}
	
	private void qrdaBody() throws ParseException 
	{
		// qrda body 
		Element componentTopLevel = element(doc, elem, "component");
		
		// Comment qrdaBody
		comment(doc, componentTopLevel, "QRDA Body");
		
		Element structuredBody = element(doc, componentTopLevel, "structuredBody");
		
		Element componentSection = element(doc, structuredBody, "component");
		
		Element section = element(doc, componentSection, "section");

		// Comment measureSection
		comment(doc, section, "\n\t*****************************************************************\n"
							+ "\tMeasure Section\n"
							+ "\t*****************************************************************\n\t");
		
		comment(doc, section, "This is the templateId for Measure Section");
		element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.24.2.2");
		
		comment(doc, section, "This is the templateId for Measure Section QDM");
		element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.24.2.3");
		
		comment(doc, section, "LOINC code for \"Measure document\"");
		element(doc, section, "code", "code", "55186-1", "codeSystem", "2.16.840.1.113883.6.1");		
		
		/**
		 * measure section
		 */
		measureSection(section);
		reportingParametersSection(doc, structuredBody, reportingPeriodStart, reportingPeriodEnd);
		
		try {
			patientData(structuredBody);
		} catch (DOMException e) {
			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
		}
	}
		
		private static void measureSection(Element ele)
		{
			element(doc, ele, "title", "Measure Section");
			
			element(doc, ele, "text");
			
			Element entry = element(doc, ele, "entry");
			
			Element org = element(doc, entry, "organizer", "classCode", "CLUSTER", "moodCode", "EVN");
			
			comment(doc, org, "This is the templateId for the Measure Reference");
			
			element(doc, org, "templateId", "root", "2.16.840.1.113883.10.20.24.3.98");
			
			comment(doc, org, "This is the templateId for the eMeasure Reference QDM");
			
			element(doc, org, "templateId", "root", "2.16.840.1.113883.10.20.24.3.97");
			
			element(doc, org, "id", "root", "40280381-4b9a-3825-014b-c21e526d0806");
			
			element(doc, org, "statusCode", "code", "completed");
			
			Element ref = element(doc, org, "reference", "typeCode", "REFR");
			
			Element exDoc = element(doc, ref, "externalDocument", "classCode", "DOC", "moodCode", "EVN");
			
			comment(doc, exDoc, "version specific identifier for eCQM: CMS: 108v7");
			
			element(doc, exDoc, "id", "root", "2.16.840.1.113883.4.738", "extension", "40280382-6963-bf5e-0169-df3a02da3a73");
			
			comment(doc, exDoc, "This is the title of the eMeasure");
			
			element(doc, exDoc, "text", "VTE-2");
		}
		
		private static void patientData(Element ele) throws ParseException
		{
			Element component = element(doc, ele, "component");

			comment(doc, component, "\n\t*****************************************************************\n"
					+ "\tPatient Data Section\n"
					+ "\t*****************************************************************\n\t");

			Element section = element(doc, component, "section");

			element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.17.2.4");

			comment(doc, section, "Updated extension for 2018 HQR11.1");
			element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.24.2.1", "extension", "2018-10-01");

			comment(doc, section, "Updated extension for 2018 HQR11.1");
			element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.24.2.1.1", "extension", "2019-02-01");

			element(doc, section, "code", "code", "55188-7", "codeSystem", "2.16.840.1.113883.6.1");

			element(doc, section, "title", "Patient Data");

			element(doc, section, "text");
			
			comment(doc, section, "Start of Inpatient Encounter");
			
			inpatientEncounter(section);
					
			paymentSection(doc, section);
		}
		
			private static void inpatientEncounter(Element ele) throws ParseException
			{
				comment(doc, ele, "ENCOUNTER PERFORMED");
				comment(doc, ele, "Encounter, Performed: Encounter Inpatient\" using \"Encounter Inpatient SNOMEDCT Value Set (2.16.840.1.113883.3.666.5.307)");

				Element entry = element(doc, ele, "entry");

				Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN");

				comment(doc, act, "Enouncter performed Act");

				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.133", "extension", "2017-08-01");

				element(doc, act, "id", "root", "ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7");

				element(doc, act, "code", "code", "ENC", "codeSystem", "2.16.840.1.113883.5.6", "displayName", "Encounter", "codeSystemName", "ActClass");

				Element er = element(doc, act, "entryRelationship", "typeCode", "SUBJ");

				Element enc = element(doc, er, "encounter", "classCode", "ENC", "moodCode", "EVN");

				comment(doc, enc, "Encounter activities template");
				element(doc, enc, "templateId", "root", "2.16.840.1.113883.10.20.22.4.49", "extension", "2015-08-01");

				comment(doc, enc, "Encounter performed template");
				element(doc, enc, "templateId", "root", "2.16.840.1.113883.10.20.24.3.23", "extension", "2017-08-01");

				element(doc, enc, "id", "root", "12345678-9d11-439e-92b3-5d9815ff4de1");

				element(doc, enc, "code", "code", "32485007", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED-CT", "displayName", "Hospital admission (procedure)"); //"sdtc:valueSet", "2.16.840.1.113883.3.666.5.307");

				element(doc, enc, "text", "Encounter, Performed: Inpatient");

				element(doc, enc, "statusCode", "code", "completed");

				comment(doc, enc, "Length of Stay");

				Element effTm = element(doc, enc, "effectiveTime");

				comment(doc, effTm, "Attribute: admission datetime");
				element(doc, effTm, "low", "value", admission);

				comment(doc, effTm, "Attribute: discharge datetime");
				element(doc, effTm, "high", "value", MeasureSets.convertSecondDate(discharge, 1000000));

				// Denominator 
				if(FileCreator.denominatorException == true) {
					FileCreator.denominatorExclusion = false;
					denominator(enc, admission, MeasureSets.convertSecondDate(admission, 100000));
				}
				else {
//					if(FileCreator.denominatorExclusion == true) {
//						denominator(ele, admission, discharge);
//					}
//					else {
//						denominator(ele, admission, discharge);
//					}
					denominator(enc, admission, discharge);
				}						
				
				// Denominator Exclusion
				if(FileCreator.denominatorExclusion == true) {
					FileCreator.denominatorException = false;
					denominatorExclusion(ele);
				}
				// End Denominator Exclusion
				
				if(FileCreator.numerator == true) {
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getNumerator(16));
						
						// Medication Administered
						if(temp.contains("Medication")) {
							numerator(ele, "med", temp.contains("NOT"));
						}
						
						// Device Applied
						if(temp.contains("Device")) {
							numerator(ele, "device", temp.contains("NOT"));
						}
						
						else {}
					}
					else {
						// Medication Administered
						if(FileCreator.numBox.getSelectedItem().toString().toLowerCase().contains("medication")) {
							numerator(ele, "med", FileCreator.numBox.getSelectedItem().toString().contains("NOT"));
						}
						
						// Device Applied
						if(FileCreator.numBox.getSelectedItem().toString().toLowerCase().contains("device")) {
							numerator(ele, "device", FileCreator.numBox.getSelectedItem().toString().contains("NOT"));
						}
						
						else {}
					}
				}			
			}
				
				private static void denominator(Element ele, String admit, String disch) 
				{
					comment(doc, ele, "DENOMINATOR");

					Element par = element(doc, ele, "participant", "typeCode", "LOC");

					element(doc, par, "templateId", "root", "2.16.840.1.113883.10.20.24.3.100", "extension", "2017-08-01");

					comment(doc, par, "Location Period");

					Element time = element(doc, par, "time");

					element(doc, time, "low", "value", admit);

					element(doc, time, "high", "value", disch);

					Element parRole = element(doc, par, "participantRole", "classCode", "SDLOC");

					element(doc, parRole, "code", "code", "309904001", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", "Intensive care unit (environment)");

					comment(doc, ele, "QDM Attribute: Encounter Diagnosis");
					Element diagEr = element(doc, ele, "entryRelationship", "typeCode", "REFR");
					
					Element act = element(doc, diagEr, "act", "classCode", "ACT", "moodCode", "EVN");
					
					element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.22.4.80", "extension", "2015-08-01");
					
					element(doc, act, "code", "code", "29308-4", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "DIAGNOSIS");
					
					element(doc, act, "statusCode", "code", "active");
					
					Element er = element(doc, act, "entryRelationship", "typeCode", "SUBJ");
					
					Element obs = element(doc, er, "observation", "classCode", "OBS", "moodCode", "EVN");
					
					comment(doc, obs, "Problem Observation (V3)");
					element(doc, obs, "templateId", "root", "2.16.840.1.113883.10.20.22.4.4", "extension", "2015-08-01");
					
					element(doc, obs, "id", "root", "ab1791b0-5c71-11db-b0de-0800200c9a66");
					
					element(doc, obs, "code", "code", "64572001", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Condition");
					
					comment(doc, obs, "The statusCode reflects the status of the observation itself");
					
					element(doc, obs, "statusCode", "code", "completed");
					
					Element effTm = element(doc, obs, "effectiveTime");
					
					comment(doc, effTm, "The low value reflects the date of onset");
					element(doc, effTm, "low", "value", admit);
					
					comment(doc, effTm, "The high value reflects when the problem was known to be resolved");
					element(doc, effTm, "high", "value", disch);
					
					element(doc, obs, "value", "code", "427.31", "codeSystem", "2.16.840.1.113883.6.103", "displayName", "Atrial fibrillation", "xsi:type", "CD");
				}
				
				
				
	static int index;
	private static String getRand(String[] stArr) {
		index = 0;
		index = (int) (Math.random() * (stArr.length - 2)) + 2; //  Random r = new Random(); r.nextInt(stArr.length-2) + 2; 
		return stArr[index].toString();
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
	
	public static String returnDoc() 
	{
		try {
			return transformDocument();
		} catch (TransformerException | ParserConfigurationException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - PC-01 file", e);
			return e.toString();
		}
	}
}
