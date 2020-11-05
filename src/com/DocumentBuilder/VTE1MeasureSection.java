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

public class VTE1MeasureSection extends Elements {
	private static Document doc;
	private static Element elem;
	private static String reportingPeriodStart;
	private static String reportingPeriodEnd;
	
	private static String admission;
	public static String discharge;
	
	public VTE1MeasureSection(String ccn) throws DOMException, ParseException
	{
		try {
			reportingPeriodStart = MeasureSets.rptPrd[0];
			reportingPeriodEnd = MeasureSets.rptPrd[1];
			admission = MeasureSets.startDate();
			discharge = MeasureSets.endDate();
			
			VTE.condition += "_VTE-1_" + discharge;
			XmlDocumentBuilder.setPatientID(VTE.condition);
			
			doc = XmlDocumentBuilder.documentFactory();
			elem = XmlDocumentBuilder.clinicalDocumentRoot();
			XmlDocumentBuilder.qrdaHeader(ccn);
			qrdaBody();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			FileCreator.errorMsgTxtbox.setText(e.toString());
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - VTE-1 QRDA body", e.getCause());
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
		
		private static void measureSection(Element ele) {
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
			
			element(doc, exDoc, "id", "root", "2.16.840.1.113883.4.738", "extension", "40280382-6963-bf5e-0169-7333b8990827");
			
			comment(doc, exDoc, "This is the title of the eMeasure");
			
			element(doc, exDoc, "text", "VTE-1");
		}
		
		private static void patientData(Element ele) throws ParseException 
		{
			comment(doc, ele, "\n\t*****************************************************************\n"
					+ "\tPatient Data Section\n"
					+ "\t*****************************************************************\n\t");
			
			Element componentSection = element(doc, ele, "component");
			
			Element section = element(doc, componentSection, "section");

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

			// Measure Calculation Starts Here
			comment(doc, section, "Measure Calculations Start Here");
			
			comment(doc, section, "Start of Inpatient Encounter");
			
			inpatientEncounter(section);
			
			if(FileCreator.numerator == true) {
				if(FileCreator.chckbxRandomize.isSelected()) {
					String temp = getRand(MeasureParameters.getNumerator(7));
					
					// Medication Administered
					if(temp.contains("Medication")) {
						numerator(section, "med", temp.contains("NOT"));}
					
					// Device Applied
					if(temp.contains("Device")) {
						numerator(section, "device", temp.contains("NOT"));}
					
					else {}
				}
				else {
					// Medication Administered
					if(FileCreator.numBox.getSelectedItem().toString().toLowerCase().contains("medication")) {
						numerator(section, "med", FileCreator.numBox.getSelectedItem().toString().contains("NOT"));}
					
					// Device Applied
					if(FileCreator.numBox.getSelectedItem().toString().toLowerCase().contains("device")) {
						numerator(section, "device", FileCreator.numBox.getSelectedItem().toString().contains("NOT"));}
					
					else {}
				}
			}
			
			paymentSection(doc, section);
		}
			
			private static void inpatientEncounter(Element ele) throws ParseException 
			{
				comment(doc, ele, "QDM Datatype: Encounter, Performed");
				
				Element entry = element(doc, ele, "entry");
				
				Element encPerfAct = element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN");
				
				comment(doc, encPerfAct, "Encounter Performed Act (V2)");
				
				element(doc, encPerfAct, "templateId", "root", "2.16.840.1.113883.10.20.24.3.133", "extension", "2017-08-01");
				
				element(doc, encPerfAct, "id", "root", "ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7");
				
				element(doc, encPerfAct, "code", "code", "ENC", "codeSystem", "2.16.840.1.113883.5.6", "codeSystemName", "ActClass", "displayName", "Encounter");
				
				Element entryRelationship = element(doc, encPerfAct, "entryRelationship", "typeCode", "SUBJ");
				
				Element encounter = element(doc, entryRelationship, "encounter", "classCode", "ENC", "moodCode", "EVN");
				
				comment(doc, encounter, "Conforms to C-CDA R2.1 Encounter Activity (V3)");
				element(doc, encounter, "templateId", "root", "2.16.840.1.113883.10.20.22.4.49", "extension", "2015-08-01");
				
				comment(doc, encounter, "Encounter Performed (V4) templateId");
				element(doc, encounter, "templateId", "root", "2.16.840.1.113883.10.20.24.3.23", "extension", "2017-08-01");
				
				element(doc, encounter, "id", "root", "12345678-9d11-439e-92b3-5d9815ff4de1");
				
				element(doc, encounter, "code", "code", "32485007", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Hospital Admission (procedure)"/*, "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.424"*/);
				
				element(doc, encounter, "text", "Encounter, Performed");
				
				element(doc, encounter, "statusCode", "code", "completed");
				
				Element effTime = element(doc, encounter, "effectiveTime");
				
				// Start datetime
				comment(doc, effTime, "QDM Attribute: Relevant Period - Admission datetime");
				element(doc, effTime, "low", "value", admission);
				
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

				comment(doc, effTime, "QDM Attribute: Relevant Period - Discharge datetime");
				element(doc, effTime, "high", "value", discharge);
				
				if(FileCreator.denominatorExclusion == true) {
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getDenominatorExclusion(7));
						
						// ICU - participant in encounter
						if(temp.toUpperCase().contains("ICU")) {
							denominatorExclusion(encounter, "ICU", null, null);	}
						
						// diagnosis - entry relationship in encounter
						if(temp.contains("Diagnose")) {
							denominatorExclusion(encounter, "Diagnosis", null, null); }
						
						// procedure - separate entry in section
						if(temp.toLowerCase().contains("surgery")) {
							denominatorExclusion(ele, "Procedure", MeasureParameters.denexValues.get(index).toString(), temp); }
						
						// observation - comfort measures
						if(temp.toLowerCase().contains("comfort")) {
							denominatorExclusion(ele, "observation", null, null); }

						else {}
					}
					else {
						// ICU - participant in encounter
						if(FileCreator.denexBox.getSelectedItem().toString().toUpperCase().contains("ICU")) {
							denominatorExclusion(encounter, "ICU", null, null); }
						
						// diagnosis - entry relationship in encounter
						if(FileCreator.denexBox.getSelectedItem().toString().contains("Diagnose")) {
							denominatorExclusion(encounter, "Diagnosis", null, null); }
						
						// procedure - separate entry in section
						if(FileCreator.denexBox.getSelectedItem().toString().toLowerCase().contains("surgery")) {
							denominatorExclusion(ele, "Procedure", MeasureParameters.denexValues.get(FileCreator.denexBox.getSelectedIndex()).toString(), FileCreator.denexBox.getSelectedItem().toString()); }
						
						// observation - comfort measures
						if(FileCreator.denexBox.getSelectedItem().toString().toLowerCase().contains("comfort")) {
							denominatorExclusion(ele, "observation", null, null); }

						else {}
					}
				}
			}
			
				private static void denominatorExclusion(Element ele, String type, String codeName, String displayName) throws ParseException 
				{
					switch(type) {
					case "ICU":
						/**
						 * ICU Admission
						 */
						comment(doc, ele, "DENOMINATOR EXCLUSION");

						Element participant = element(doc, ele, "participant", "typeCode", "LOC");

						comment(doc, participant, "Facility Location Template");

						element(doc, participant, "templateId", "root", "2.16.840.1.113883.10.20.24.3.100", "extension", "2017-08-01");

						Element time = element(doc, participant, "time");

						comment(doc, time, "LOCATION PERIOD");

						element(doc, time, "low", "value", admission);

						element(doc, time, "high", "value", MeasureSets.convertSecondDate(admission, 1000000));

						comment(doc, participant, "INTENSIVE CARE UNIT");

						Element partRole = element(doc, participant, "participantRole", "classCode", "SDLOC");

						element(doc, partRole, "code", "code", "309904001", "codeSystem", "2.16.840.1.113883.6.259", "codeSystemName", "HSLOC", "displayName", "Intensive Care Unit");

//						Element addr = element(doc, partRole, "addr");
//						element(doc, addr, "streetAddressLine", "1776 Memorial Ave.");
//						element(doc, addr, "city", "Arlington");
//						element(doc, addr, "state", "VA");
//						element(doc, addr, "postalCode", "22096");
//						element(doc, addr, "country", "US");
//						element(doc, partRole, "telecom", "nullFlavor", "UNK"); 
//						Element playEntity = element(doc, partRole, "playingEntity", "classCode", "PLC");
//						element(doc, playEntity, "name", "Arlington Memorial Hospital");
						/**
						 * END ICU Admission
						 */
						break;
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
				
			private static void numerator(Element ele, String type, boolean negation) throws DOMException, ParseException
			{
				switch(type) {
				case "device":
					/**
					 * DEVICE APPLIED / DEVICE APPLIED NOT DONE
					 */
					comment(doc, ele, "NUMERATOR - DEVICE");

					Element deviceEntry = element(doc, ele, "entry");
					
					Element devProc = doc.createElement("procedure");
					((Element)devProc).setAttribute("classCode", "PROC");
					((Element)devProc).setAttribute("moodCode", "EVN");
					
					if(negation == true) {
						// negation indicator if not done
						((Element)devProc).setAttribute("negationInd", "true");
					}
					deviceEntry.appendChild(devProc);
					
					element(doc, devProc, "templateId", "root", "2.16.840.1.113883.10.20.22.4.14", "extension", "2014-06-09");

					element(doc, devProc, "templateId", "root", "2.16.840.1.113883.10.20.24.3.7", "extension", "2017-08-01");

					element(doc, devProc, "id", "root", "20ebe7a7-a493-447d-922b-5d98fa8b96da");

					element(doc, devProc, "code", "code", "360030002", "displayName", "application of device", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED-CT");

					element(doc, devProc, "statusCode", "code", "completed");

					Element effTm = element(doc, devProc, "effectiveTime");

					comment(doc, effTm, "QDM Attribute: Start datetime");

					element(doc, effTm, "low", "value", MeasureSets.convertSecondDate(admission, 10000));

					comment(doc, effTm, "QDM Attribute: Stop datetime");

					element(doc, effTm, "high", "value", MeasureSets.convertSecondDate(admission, 10000));

					Element author = element(doc, devProc, "author");

					element(doc, author, "templateId", "root", "2.16.840.1.113883.10.20.24.3.155", "extension", "2017-08-01");

					element(doc, author, "time", "value", MeasureSets.convertSecondDate(admission, 10000));

					Element assAuth = element(doc, author, "assignedAuthor");

					element(doc, assAuth, "id", "nullFlavor", "NA");

					Element participant = element(doc, devProc, "participant", "typeCode", "DEV");

					Element partRole = element(doc, participant, "participantRole", "classCode", "MANU");

					Element playDev = element(doc, partRole, "playingDevice", "classCode", "DEV");

					comment(doc, playDev, "DEVICE");

					element(doc, playDev, "code", "code", "442111003", "codeSystem", "2.16.840.1.113883.6.96"); //"sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.256");
					
					if(negation == true) {
						comment(doc, devProc, "REASON FOR \"NOT\" DONE");

						Element devErNeg = element(doc, devProc, "entryRelationship", "typeCode", "RSON");

						Element devNegObs = element(doc, devErNeg, "observation", "classCode", "OBS", "moodCode", "EVN");

						element(doc, devNegObs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.88", "extension", "2017-08-01");

						element(doc, devNegObs, "code", "code", "77301-0", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Reason care action performed or not");

						element(doc, devNegObs, "value", "code", "182897004", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Drug declined by patient – side effects (situation)", "xsi:type", "CD");
						//((Element)devNegValue).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.93");
					}
					/**
					 * END DEVICE APPLIED / DEVICE APPLIED NOT DONE
					 */
					break;
				case "med":
					/**
					 * DEVICE APPLIED / DEVICE APPLIED NOT DONE
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
					break;
				default:
					break;
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
