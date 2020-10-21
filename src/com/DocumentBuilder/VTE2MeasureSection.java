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

				// Denominator Exception
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
					discharge = MeasureSets.convertSecondDate(admission, 1000000);
				} else {
					discharge = MeasureSets.convertSecondDate(discharge, 1000000);
				}
				
				if(FileCreator.numerator == true) {
					if(Integer.parseInt(discharge.substring(6, 8)) < Integer.parseInt(admission.substring(6, 8))) {
						discharge = discharge.replace(discharge.substring(6, 8), Integer.toString(Integer.parseInt(admission.substring(6, 8) + 1)));
					}
				}
				
				if(FileCreator.denominatorExclusion == true) {
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getDenominatorExclusion(15));
						
						// diagnosis - entry relationship in encounter
						if(temp.contains("Diagnose")) {
							denominatorExclusion(enc, "Diagnosis", null, null); }
						
						// procedure - separate entry in section
						if(temp.toLowerCase().contains("surgery")) {
							denominatorExclusion(ele, "Procedure", MeasureParameters.denexValues.get(index).toString(), temp); }
						
						// observation - comfort measures
						if(temp.toLowerCase().contains("comfort")) {
							denominatorExclusion(ele, "observation", null, null); }

						else {}
					}
					else {
						// diagnosis - entry relationship in encounter
						if(FileCreator.denexBox.getSelectedItem().toString().contains("Diagnose")) {
							denominatorExclusion(enc, "Diagnosis", null, null); }
						
						// procedure - separate entry in section
						if(FileCreator.denexBox.getSelectedItem().toString().toLowerCase().contains("surgery")) {
							denominatorExclusion(ele, "Procedure", MeasureParameters.denexValues.get(FileCreator.denexBox.getSelectedIndex()).toString(), FileCreator.denexBox.getSelectedItem().toString()); }
						
						// observation - comfort measures
						if(FileCreator.denexBox.getSelectedItem().toString().toLowerCase().contains("comfort")) {
							denominatorExclusion(ele, "observation", null, null); }

						else {}
					}
				}
				// End Denominator Exclusion
				
				if(FileCreator.numerator == true) {
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getNumerator(16));
						
						// Medication Administered
						numerator(ele, temp.contains("NOT"));
					}
					else {
						// Medication Administered
						numerator(ele,  FileCreator.numBox.getSelectedItem().toString().contains("NOT"));
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
				
				private static void denominatorExclusion(Element ele, String type, String codeName, String displayName) throws ParseException 
				{
					switch(type) {
					case "Diagnosis":
						/**
						 * Mental Health Diagnosis
						 */
						comment(doc, ele, "DENOMINATOR EXCLUSION - QDM Attribute: PRINCIPAL DIAGNOSIS");
						
						Element qdmEr = element(doc, ele, "entryRelationship", "typeCode", "REFR");

						Element diagObs = element(doc, qdmEr, "observation", "classCode", "OBS", "moodCode", "EVN");

						element(doc, diagObs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.152", "extension", "2017-08-01");

						element(doc, diagObs, "id", "root", "92587992-6147-467e-8ce7-b080ef569df4");

						element(doc, diagObs, "code", "code", "8319008", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Principal Diagnosis", "codeSystemName", "SNOMED-CT");

						element(doc, diagObs, "value", "code", "195165005", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Principal Diagnosis", "codeSystemName", "SNOMED-CT", "xsi:type", "CD");	//sdtc:valueSet="2.16.840.1.113883.3.117.1.7.1.212"
						/**
						 * END Mental Health Diagnosis
						 */
						break;
					case "Procedure":
						/**
						 * Procedure Performed
						 * applies to `section` element
						 */
						comment(doc, ele, "DENOMINATOR EXCLUSIONS - PROCEDURE PERFORMED");

						Element entry = element(doc, ele, "entry", "typeCode", "DRIV");

						Element proc = element(doc, entry, "procedure", "classCode", "PROC", "moodCode", "EVN");

						element(doc, proc, "templateId", "root", "2.16.840.1.113883.10.20.22.4.14", "extension", "2014-06-09");

						element(doc, proc, "templateId", "root", "2.16.840.1.113883.10.20.24.3.64", "extension", "2017-08-01");

						element(doc, proc, "id", "root", "d68b7e32-7810-4f5b-9cc2-acd54b0fd85d");

						comment(doc, proc, "CHANGE HERE FOR DIFFERENT PROCEDURES");

						element(doc, proc, "code", "code", codeName, "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", displayName, "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.255");

						element(doc, proc, "text", "Procedure, Performed");

						element(doc, proc, "statusCode", "code", "completed");

						Element effTm = element(doc, proc, "effectiveTime");

						comment(doc, effTm, "Procedure Start Time");

						element(doc, effTm, "low", "value", MeasureSets.convertSecondDate(admission, 1000000));

						comment(doc, effTm, "Procedure End Time");

						element(doc, effTm, "high", "value", MeasureSets.convertSecondDate(admission, 2000000));

						comment(doc, proc, "QDM ATTRIBUTE: ORDINALITY");

						element(doc, proc, "priorityCode", "code", "63161005", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Principal");

						comment(doc, proc, "QDM ATTRIBUTE: METHOD");

						element(doc, proc, "methodCode", "code", "446223002", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Hand assisted laparoscopic right colectomy");

						comment(doc, proc, "QDM ATTRIBUTE: ANATOMICAL APPROACH SITE");

						element(doc, proc, "approachCode", "code", "14742008", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "large intestinal structure");

						comment(doc, proc, "QDM ATTRIBUTE: ANATOMICAL LOCATION SITE");

						element(doc, proc, "targetSiteCode", "code", "71854001", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "colon");

						comment(doc, proc, "QDM ATTRIBUTE: INCISION DATETIME");

						Element procEr = element(doc, proc, "entryRelationship", "typeCode", "REFR");

						Element erProc = element(doc, procEr, "procedure", "classCode", "PROC", "moodCode", "EVN");

						element(doc, erProc, "templateId", "root", "2.16.840.1.113883.10.20.24.3.89");

						element(doc, erProc, "id", "root", "2d5dc123-13ca-477d-9263-4a9c504186a1");

						element(doc, erProc, "code", "code", "34896006", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "incision");

						element(doc, erProc, "effectiveTime", "value", MeasureSets.convertSecondDate(admission, 1010000));
						/**
						 * END Procedure Performed
						 */
						break;
					case "comfort":
						comment(doc, ele, "DENOMINATOR EXCLUSION: COMFORT MEASURES");

						Element comfortEntry = element(doc, ele, "entry");

						Element act = element(doc, comfortEntry, "act", "classCode", "ACT", "moodCode", "EVN");

						element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.22.4.12", "extension", "2014-06-09");
						
						element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.32", "extension", "2017-08-01");

						element(doc, act, "id", "root", "db734647-fc99-424c-a864-7e3cda82e703");

						element(doc, act, "code", "code", "133918004", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Comfort measures (regime/therapy)");	// change here for dif procedures

						element(doc, act, "statusCode", "code", "completed");

						Element comfortEffTm = element(doc, act, "effectiveTime");

						element(doc, comfortEffTm, "low", "value", MeasureSets.convertSecondDate(admission, 1000000));

						element(doc, comfortEffTm, "high", "value", MeasureSets.convertSecondDate(admission, 2000000));
						/**
						 * END Comfort Measures
						 */
						break;
					default:
						break;
					}
				}
				
				private static void numerator(Element ele, boolean negation) throws DOMException, ParseException
				{
					/**
					 * MEDICATION ADMINISTERED / MEDICATION ADMINISTERED NOT DONE
					 */
					comment(doc, ele, "NUMERATOR - MEDICATION");

					Element medEntry = element(doc, ele, "entry");

					Element subAdmin = doc.createElement("substanceAdministration");
					((Element)subAdmin).setAttribute("classCode", "SBADM");
					((Element)subAdmin).setAttribute("moodCode", "EVN");
					
					if(negation == true) {
						// negation indicator if not done
						((Element)subAdmin).setAttribute("negationInd", "true");
					}
					medEntry.appendChild(subAdmin);
					
					element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.22.4.16", "extension", "2014-06-09");

					element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.24.3.42", "extension", "2017-08-01");

					element(doc, subAdmin, "id", "root", "9069c123-80ad-47c8-a633-9dc02018ae56");

					element(doc, subAdmin, "statusCode", "code", "completed");

					Element entryEffTm = element(doc, subAdmin, "effectiveTime", "xsi:type", "IVL_TS");

					element(doc, entryEffTm, "low", "value", MeasureSets.convertSecondDate(admission, 10000));

					element(doc, entryEffTm, "high", "value", MeasureSets.convertSecondDate(admission, 10000));

					comment(doc, subAdmin, "QDM ATTRIBUTE: FREQUENCY");

					Element freqEffTm = element(doc, subAdmin, "effectiveTime", "xsi:type", "PIVL_TS", "institutionSpecified", "true", "operator", "A");

					element(doc, freqEffTm, "period", "value", "6", "unit", "h");

					comment(doc, subAdmin, "QDM ATTRIBUTE: DOSE");

					element(doc, subAdmin, "doseQuantity", "value", "1");

					Element con = element(doc, subAdmin, "consumable");

					Element manProd = element(doc, con, "manufacturedProduct", "classCode", "MANU");

					comment(doc, manProd, "Conforms to C-CDA R2.1 Medication Information (V2)");

					element(doc, manProd, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");

					element(doc, manProd, "id", "root", "37bfe02a-3e97-4bd6-9197-bbd0ed0de79e");

					Element manMat = element(doc, manProd, "manufacturedMaterial");

					
					Element materialCode = doc.createElement("code");
					if(negation == true) {
						((Element)materialCode).setAttribute("nullFlavor", "NA");
						((Element)materialCode).setAttribute("sdtc:valueSet", "2.16.840.1.113762.1.4.1045.39");
					} else {
						((Element)materialCode).setAttribute("code", "855288");
						((Element)materialCode).setAttribute("codeSystem", "2.16.840.1.113883.6.88");
						//((Element)materialCode).setAttribute("codeSystemName", "RxNorm");
						//((Element)materialCode).setAttribute("displayName", "100 ML heparin sodium, porcine 100 UNT/ML Injection");
						//((Element)materialCode).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.218");
					}
					manMat.appendChild(materialCode);		
					
					element(doc, materialCode, "originalText", "None of value set: Low Dose Unfractionated Heparin for VTE Prophylaxis");

					Element medAuth = element(doc, subAdmin, "author");

					element(doc, medAuth, "templateId", "root", "2.16.840.1.113883.10.20.24.3.155", "extension", "2017-08-01");

					element(doc, medAuth, "time", "value", MeasureSets.convertSecondDate(admission, 10000));

					Element medAssAuth = element(doc, medAuth, "assignedAuthor");

					element(doc, medAssAuth, "id", "nullFlavor", "NA");

					
					if(negation == true) {
						comment(doc, subAdmin, "REASON FOR \"NOT\" DONE");

						Element medErNeg = element(doc, subAdmin, "entryRelationship", "typeCode", "RSON");

						Element medNegObs = element(doc, medErNeg, "observation", "classCode", "OBS", "moodCode", "EVN");

						element(doc, medNegObs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.88", "extension", "2017-08-01");

						element(doc, medNegObs, "code", "code", "77301-0", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Reason care action performed or not");

						element(doc, medNegObs, "value", "code", "182903008", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Drug declined by patient – reason unknown (situation)", "xsi:type", "CD");
						//((Element)devNegValue).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.93");
					}
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
