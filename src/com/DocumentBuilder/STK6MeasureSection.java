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

import com.Measures.STK;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureParameters;
import com.xmlEditTool.MeasureSets;

public class STK6MeasureSection extends Elements {
	private static Document doc;
	private static Element elem;
	private static String reportingPeriodStart;
	private static String reportingPeriodEnd;
	
	private static String admission;
	private static String discharge;
	
	public STK6MeasureSection(String ccn) throws DOMException, ParseException
	{
		try {
			reportingPeriodStart = MeasureSets.rptPrd[0];
			reportingPeriodEnd = MeasureSets.rptPrd[1];
			admission = MeasureSets.startDate();
			discharge = MeasureSets.endDate();
			
			STK.condition += "_STK-6_" + discharge;			
			XmlDocumentBuilder.setPatientID(STK.condition);

			doc = XmlDocumentBuilder.documentFactory();
			elem = XmlDocumentBuilder.clinicalDocumentRoot();
			XmlDocumentBuilder.qrdaHeader(ccn);
			qrdaBody();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			FileCreator.errorMsgTxtbox.setText(e.toString());
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - STK-3 QRDA body", e.getCause());
		}
	}
	
	private void qrdaBody() 
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
		
		// Element measureSectionTempId
		comment(doc, section, "This is the templateId for Measure Section");
		element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.24.2.2");		
		
		// Element measureSectionTempIdQDM
		comment(doc, section, "This is the templateId for Measure Section QDM");
		element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.24.2.3");
		
		// loincCode
		comment(doc, section, "LOINC Code for \"Measure Document\". This stays the same for all QRDA measure sections.");
		element(doc, section, "code", "code", "55186-1", "codeSystem", "2.16.840.1.113883.6.1");
		
		/**
		 * measure section
		 */
		measureSection(section);
				
		reportingParametersSection(doc, structuredBody, reportingPeriodStart, reportingPeriodEnd);
		
		try {
			patientData(structuredBody);
		} catch (DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
		}
	}
		
		private static void measureSection(Element ele) {
			element(doc, ele, "title", "Measure Section");
			
			element(doc, ele, "text");
			
			Element entry = element(doc, ele, "entry");	

			Element org = element(doc, entry, "organizer", "classCode", "CLUSTER", "moodCode", "EVN");

			comment(doc, org, "This is the templateId for Measure Reference");
			element(doc, org, "templateId", "root", "2.16.840.1.113883.10.20.24.3.98");

			comment(doc, org, "This is the templateId for eMeasure Reference QDM");
			element(doc, org, "templateId", "root", "2.16.840.1.113883.10.20.24.3.97");

			element(doc, org, "id", "root", "40280382-68d3-a5fe-0169-35dcb77428c0");

			element(doc, org, "statusCode", "code", "completed");

			Element ref = element(doc, org, "reference", "typeCode", "REFR");

			Element exDoc = element(doc, ref, "externalDocument", "classCode", "DOC", "moodCode", "EVN");

			comment(doc, exDoc, "version specific identifier for eMeasure");
			element(doc, exDoc, "id", "root", "2.16.840.1.113883.4.738", "extension", "40280382-68d3-a5fe-0169-35dcb77428c0");

			comment(doc, exDoc, "Title of the eMeasure");
			element(doc, exDoc, "text", "STK-6: Primary PCI Received Within 90 Minutes of Hospital Arrival");
		}
		
		private static void patientData(Element ele) throws DOMException, ParseException
		{
			Element comp = element(doc, ele, "component");

			Comment patData = doc.createComment("\n\t*****************************************************************\n"
					+ "\tPatient Data Section\n"
					+ "\t*****************************************************************\n\t");
			comp.appendChild(patData);

			Element section = element(doc, comp, "section");
			
			element(doc, section, "templateId", "root", "2.16.840.1.113883.10.20.17.2.4");

			
			String[] cmt = {"Updated extension for HQR11.1","Updated templateID and extension for HQR11.1"};
			String[] root = {"2.16.840.1.113883.10.20.24.2.1","2.16.840.1.113883.10.20.24.2.1.1"};
			String[] ext = {"2018-10-01","2019-02-01"};
			for(int i = 0; i < 2; i++) {
				comment(doc, section, cmt[i]);
				element(doc, section, "templateId", "root", root[i], "extension", ext[i]);
			}
			
			element(doc, section, "code", "code", "55188-7", "codeSystem", "2.16.840.1.113883.6.1");

			element(doc, section, "title", "Patient Data");

			element(doc, section, "text");

			
			inpatientEncounter(section);
			
			if(FileCreator.denominatorException == true) {
				denominatorException(section);
			}

			if(FileCreator.numerator == true) {
					numerator(section);
			}

			
			paymentSection(doc, section);
		}
			
			private static void inpatientEncounter(Element ele) throws ParseException
			{
				comment(doc, ele, "ENCOUNTER PERFORMED - HOSPITAL ADMISSION");
			
				Element entry = element(doc, ele, "entry");
			
				Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN");
			
				comment(doc, act, "Encounter performed Act");
			
				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.133", "extension", "2017-08-01");
			
				element(doc, act, "id", "root", "ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7");
			
				element(doc, act, "code", "code", "ENC", "codeSystem", "2.16.840.1.113883.5.6", "codeSystemName", "Encounter", "displayName", "ActClass");
				
				Element er = element(doc, act, "entryRelationship", "typeCode", "SUBJ");
			
				Element enc = element(doc, er, "encounter", "classCode", "ENC", "moodCode", "EVN");
			
				comment(doc, enc, "Encounter activities template");
				element(doc, enc, "templateId", "root", "2.16.840.1.113883.10.20.22.4.49", 	"extension", "2015-08-01");
			
				comment(doc, enc, "Encounter performed template");
				element(doc, enc, "templateId", "root", "2.16.840.1.113883.10.20.24.3.23", "extension", "2017-08-01");
			
				element(doc, enc, "id", "root", "12345678-9d11-439e-92b3-5d9815ff4de1");
			
				element(doc, enc, "code", "code", "32485007", "codeSystem", "2.16.840.1.113883.6.96",	"codeSystemName", "SNOMEDCT", "displayName", "Hospital admission (procedure)");
			
				element(doc, enc, "text", "Encounter, Performed");
			
				element(doc, enc, "statusCode", "code", "completed");
			
				comment(doc, enc, "Length Of Stay");
			
				Element eff = element(doc, enc, "effectiveTime");
			
				comment(doc, eff, "Attribute: admission datetime (or encounter start)");
				element(doc, eff, "low", "value", admission);
			
				comment(doc, eff, "Attribute: discharge datetime (or encounter end)");
				element(doc, eff, "high", "value", discharge);
			
				if(FileCreator.denominatorExclusion == true) {
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getDenominatorExclusion(FileCreator.measureSetDropdownBx.getSelectedIndex()));
						if(temp.toUpperCase().contains("COMFORT")) {
							denominatorExclusion(doc, ele, "comfort", null, null);
						}
						else {
							denominatorExclusion(doc, enc, "discharge", MeasureParameters.denexValues.get(index).toString(), temp);
						}
					}
					else {
						String type = MeasureParameters.denexValues.get(FileCreator.denexBox.getSelectedIndex()).toString();
						if(type.toUpperCase().contains("COMFORT")) {
							denominatorExclusion(doc, ele, "comfort", null, null);
						}
						else {
							denominatorExclusion(doc, enc, "discharge", MeasureParameters.denexValues.get(FileCreator.denexBox.getSelectedIndex()).toString(), FileCreator.denexBox.getSelectedItem().toString());
						}
					}
				}			
				
				comment(doc, enc, "QDM Attribute: Principal Dianosis");
				
				Element encEr = element(doc, enc, "entryRelationship", "typeCode", "REFR");
			
				Element obs = element(doc, encEr, "observation", "classCode", "OBS", "moodCode", "EVN");
			
				element(doc, obs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.152", "extension", "2017-08-01");
			
				element(doc, obs, "id", "root", "92587992-6147-467e-8ce7-b080ef569df4");
			
				element(doc, obs, "code", "code", "8319008", "codeSystem", "2.16.840.1.113883.6.96",	"codeSystemName", "SNOMED", "displayName", "Principal Diagnosis");
				
				element(doc, obs, "value", "code", "230694003", "codeSystem", "2.16.840.1.113883.6.96",	"codeSystemName", "SNOMEDCT", "displayName", "Total anterior cerebral circulation infarction (disorder)", "xsi:type", "CD");
			}
				
				// DENOMINATOR EXCLUSION
				private static void denominatorExclusion(Document doc, Element ele, String type, String codeName, String displayName) throws ParseException {
					//element(doc, ele, "sdtc:dischargeDispositionCode", "code", "428361000124106", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to home for hospice care (procedure)", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.209");
					
					comment(doc, ele, "DENOMINATOR EXCLUSION");
					
					switch(type) {
					case "discharge": // discharge disposition
						element(doc, ele, "sdtc:dischargeDispositionCode", "code", codeName, "codeSystem", "2.16.840.1.113883.6.96", "displayName", displayName, "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.309");
						//((Element)code).setAttribute("codeSystemName", "SNOMED-CT");
						//((Element)code).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.309");
						break;
					case "comfort":
						comfortMeasures(ele);
						break;
					default:
						break;
					}
				}
			
			private static void denominatorException(Element ele) throws ParseException
			{
				//element(doc, ele, "sdtc:dischargeDispositionCde", "code", "306701001", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to Acute Care Facility", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.87");
				//element(doc, ele, "sdtc:dischargeDispositionCde", "code", "428371000124100", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to healthcare facility for hospice care (procedure)", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.207");
				//element(doc, ele, "sdtc:dischargeDispositionCode", "code", "428361000124107", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to home for hospice care (procedure)", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.209");
				comment(doc, ele, "DENOMINATOR EXCEPTION START");

				comment(doc, ele, "Medication, Discharge not done: Patient Refusal");

				Element entry = element(doc, ele, "entry");

				Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "RQO", "negationInd", "true");

				comment(doc, act, "Discharge Medication - Active Medication");

				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.105", "extension", "2018-10-01");

				element(doc, act, "id", "root", "60f33340-591f-4459-9fa2-1c93e014a6e2");

				element(doc, act, "code", "code", "75311-1", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Discharge medication");

				Element entryRelationship = element(doc, act, "entryRelationship", "typeCode", "SUBJ");

				Element subAdmin = element(doc, entryRelationship, "substanceAdministration", "classCode", "SBADM", "moodCode", "EVN");

				comment(doc, subAdmin, "Conforms to C-CDA R2 Medication Activity (V2)");

				element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.22.4.16", "extension", "2014-06-09");

				element(doc, subAdmin, "id", "root", "c0ea7bf3-50e7-4e7a-83a3-e5a9ccbb8541");

				element(doc, subAdmin, "statusCode", "code", "active");

				Element subAdminEffTm = element(doc, subAdmin, "effectiveTime", "xsi:type", "IVL_TS");
				
				comment(doc, subAdminEffTm, "QDM Attributes: Start datetime");

				element(doc, subAdminEffTm, "low", "value", MeasureSets.convertSecondDate(admission, 1000));

				comment(doc, subAdminEffTm, "QDM Attributes: End datetime");

				element(doc, subAdminEffTm, "high", "nullFlavor", "UNK");
				
				comment(doc, subAdmin, "QDM Attribute: Frequency");

				Element freqEffTm = element(doc, subAdmin, "effectiveTime", "institutionSpecified", "true", "operator", "A", "xsi:type", "PIVL_TS");
				
				element(doc, freqEffTm, "period", "value", "6", "unit", "h");
		
				comment(doc, subAdmin, "QDM Attribute: Dose");

				element(doc, subAdmin, "doseQuantity", "value", "1");

				Element consumable = element(doc, subAdmin, "consumable");

				Element manufacturedProduct = element(doc, consumable, "manufacturedProduct", "classCode", "MANU");
				
				comment(doc, manufacturedProduct, "Conforms to C-CDA R2 Medication Information (V2)");

				element(doc, manufacturedProduct, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");

				element(doc, manufacturedProduct, "id", "root", "37bfe02a-3e97-4bd6-9197-bbd0ed0de79e");
				
				Element manufacturedMaterial = element(doc, manufacturedProduct, "manufacturedMaterial");
				
				element(doc, manufacturedMaterial, "code", "sdtc:valueSet", "2.16.840.1.113762.1.4.1110.19", "nullFlavor", "NA");

				Element author = element(doc, subAdmin, "author");

				element(doc, author, "templateId", "root", "2.16.840.1.113883.10.20.22.4.119");

				element(doc, author, "time", "value", MeasureSets.convertSecondDate(admission, 1000));

				Element assignedAuthor = element(doc, author, "assignedAuthor");

				element(doc, assignedAuthor, "id", "nullFlavor", "NA");

				comment(doc, act, "REASON FOR NOT DONE");

				Element er = element(doc, act, "entryRelationship", "typeCode", "RSON");

				Element obs = element(doc, er, "observation", "classCode", "OBS", "moodCode", "EVN");

				element(doc, obs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.88", "extension", "2017-08-01");

				element(doc, obs, "code", "code", "77301-0", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Reason care action performed or not");

				comment(doc, obs, "NEGATION_RATIONALE_START");

				element(doc, obs, "value", "code", "410534003", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", "Procedure refused (situation)", "xsi:type", "CD");

				comment(doc, obs, "NEGATION_RATIONALE_END");
				
				comment(doc, ele, "DENOMINATOR EXCEPTION END");
			}
				
			private static void numerator(Element ele)
			{
				comment(doc, ele, "NUMERATOR START");

				comment(doc, ele, "Medication, Discharge: Statin Medication");

				Element entry = element(doc, ele, "entry");

				Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "RQO");

				comment(doc, act, "Discharge Medication (V4)");
				
				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.105", "extension", "2018-10-01");

				element(doc, act, "id", "root", "60f33340-591f-4459-9fa2-1c93e014a6e2");

				element(doc, act, "code", "code", "75311-1", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Discharge medications");

				Element entryRelationship = element(doc, act, "entryRelationship", "typeCode", "SUBJ");

				//substanceAdministration (subAdmin)
				Element sub = element(doc, entryRelationship, "substanceAdministration", "classCode", "SBADM", "moodCode", "EVN");

				comment(doc, sub, "Medication Activity (V2)");

				element(doc, sub, "templateId", "root", "2.16.840.1.113883.10.20.22.4.16", "extension", "2014-06-09");

				element(doc, sub, "id", "root", "c0ea7bf3-50e7-4e7a-83a3-e5a9ccbb8541");

				element(doc, sub, "statusCode", "code", "active");

				Element subEffTm = element(doc, sub, "effectiveTime", "xsi:type", "IVL_TS");

				comment(doc, subEffTm, "QDM Attribute: Start datetime");

				element(doc, subEffTm, "low", "value", discharge);

				comment(doc, subEffTm, "QDM Attribute: Stop datetime");

				element(doc, subEffTm, "high", "nullFlavor", "UNK");

				comment(doc, sub, "QDM Attribute: Frequency");

				Element freqTm = element(doc, sub, "effectiveTime", "xsi:type", "PIVL_TS", "institutionSpecified", "true", "operator", "A");

				element(doc, freqTm, "period", "value", "6", "unit", "h");
				
//				Comment route = doc.createComment("QDM Attribute: Route");
//				subAdmin.appendChild(route);
//				
//				Element routeCode = doc.createElement("routeCode");
//				((Element)routeCode).setAttribute("code", "C38288");
//				((Element)routeCode).setAttribute("codeSystem", "2.16.840.1.113883.3.26.1.1");
//				((Element)routeCode).setAttribute("codeSystemName", "NCI Thesaurus");
//				((Element)routeCode).setAttribute("displayName", "ORAL");
//				//((Element)routeCode).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.222");
//				subAdmin.appendChild(routeCode);
				
				comment(doc, sub, "QDM Attribute: Dose");

				element(doc, sub, "doseQuantity", "value", "1");

				Element con = element(doc, sub, "consumable");

				Element man = element(doc, con, "manufacturedProduct", "classCode", "MANU");

				comment(doc, man, "Conforms to C-CDA R2 Medication Information (V2)");

				element(doc, man, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");

				element(doc, man, "id", "root", "37bfe02a-3e97-4bd6-9197-bbd0ed0de79e");

				Element manF = element(doc, man, "manufacturedMaterial");

				element(doc, manF, "code", "code", "200345", "codeSystem", "2.16.840.1.113883.6.88", "codeSystemName", "RxNorm", "displayName", "Simvastatin 80 MG Oral Tablet");

				Element auth = element(doc, sub, "author");

				element(doc, auth, "templateId", "root", "2.16.840.1.113883.10.20.22.4.119");

				element(doc, auth, "time", "value", admission);

				Element asAuth = element(doc, auth, "assignedAuthor");

				element(doc, asAuth, "id", "nullFlavor", "NA");

				comment(doc, ele, "NUMERATOR END");
			}
			
				private static void comfortMeasures(Element ele) throws ParseException 
				{
					comment(doc, ele, "Start of Comfort Measures during Hospitalization");
					comment(doc, ele, "Intervention Performed");
					Element entry = element(doc, ele, "entry");
					
					Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN");
					comment(doc, act, "Conforms to C-CDA R2.1 Planned Act (VA) template");
					element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.22.4.12", "extension", "2014-06-09");
					
					comment(doc, act, "Intervention Performed (V4) template");
					element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.32", "extension", "2017-08-01");
					element(doc, act, "id", "root", "db734647-fc99-424c-a864-7e3cda82e703");
					
					// Intervention
					comment(doc, act, "Intervention");
					// Pull Code from spreadsheet here
					element(doc, act, "code", "code", "385763009", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", "Hospice Care (regime/therapy)", "sdtc:valueSet", "1.3.6.1.4.1.33895.1.3.0.45");
					
					element(doc, act, "statusCode", "code", "completed");
					
					Element effTm = element(doc, act, "effectiveTime");
					
					element(doc, effTm, "low", "value", MeasureSets.convertSecondDate(admission, 1000000));
					
					element(doc, effTm, "high", "value", MeasureSets.convertSecondDate(admission, 1000000));
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
