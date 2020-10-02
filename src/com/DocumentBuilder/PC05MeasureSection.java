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

import com.Measures.PC;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureParameters;
import com.xmlEditTool.MeasureSets;

public class PC05MeasureSection extends Elements {
	private static Document doc;
	private static Element elem;
	private static String reportingPeriodStart;
	private static String reportingPeriodEnd;
	private static String admission;
	public static String discharge;
	
	public PC05MeasureSection(String ccn) throws DOMException, ParseException
	{
		try {
			reportingPeriodStart = MeasureSets.rptPrd[0];
			reportingPeriodEnd = MeasureSets.rptPrd[1];
			admission = MeasureSets.startDate();
			discharge = MeasureSets.endDate();
			
			PC.condition += "_PC-05_" + discharge;			
			XmlDocumentBuilder.setPatientID(PC.condition);

			doc = XmlDocumentBuilder.documentFactory();
			elem = XmlDocumentBuilder.clinicalDocumentRoot();
			XmlDocumentBuilder.qrdaHeader(ccn);
			qrdaBody();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			FileCreator.errorMsgTxtbox.setText(e.toString());
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - PC-05 QRDA body", e.getCause());
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
			
			comment(doc, exDoc, "version specific identifier for eCQM: CMS: 009v8");
			
			element(doc, exDoc, "id", "root", "2.16.840.1.113883.4.738", "extension", "40280382-68d3-a5fe-0169-1622707d1a33");
			
			comment(doc, exDoc, "This is the title of the eMeasure");
			
			element(doc, exDoc, "text", "PC-05 (BF)");
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
			assessmentPerformed(section);
			
			if(FileCreator.numerator == true) {
				numerator(section);
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
				
				comment(doc, effTime, "QDM Attribute: Relevant Period - Discharge datetime");
				element(doc, effTime, "high", "value", discharge);
				
				if(FileCreator.denominatorExclusion == true) {
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getDenominatorExclusion(FileCreator.measureSetDropdownBx.getSelectedIndex()));
						if(temp.contains("NICU")) {
							denominatorExclusion(encounter, "NICU", MeasureParameters.denexValues.get(index).toString(), temp);
						}
						else {
							denominatorExclusion(encounter, "discharge", MeasureParameters.denexValues.get(index).toString(), temp);
						}
					}
					else {
						String type = MeasureParameters.denexValues.get(FileCreator.denexBox.getSelectedIndex()).toString();
						if(type.contains("NICU")) {
							denominatorExclusion(encounter, "NICU", type, FileCreator.denexBox.getSelectedItem().toString());
						}
						else {
							denominatorExclusion(encounter, "discahrge", type, FileCreator.denexBox.getSelectedItem().toString());
						}
					}
				}
				// Start of principal diagnostic codes
				comment(doc, encounter, "START OF ENCOUNTER DIAG START HERE");
				comment(doc, encounter, "QDM Attribute: Encounter Diagnosis");
				
				Element diagEntryRelationship = element(doc, encounter, "entryRelationship", "typeCode", "REFR");
				
				Element diagAct = element(doc, diagEntryRelationship, "act", "classCode", "ACT", "moodCode", "EVN");
				
				element(doc, diagAct, "templateId", "root", "2.16.840.1.113883.10.20.22.4.80", "extension", "2015-08-01");

				element(doc, diagAct, "code", "code", "29308-4", "codeSystem", "2.16.840.1.113883.6.1",	"codeSystemName", "LOINC", "displayName", "DIAGNOSIS");
				
				element(doc, diagAct, "statusCode", "code", "active");

				Element diagEr = element(doc, diagAct, "entryRelationship", "typeCode", "SUBJ");
				
				Element obs = element(doc, diagEr, "observation", "classCode", "OBS", "moodCode", "EVN");
				
				comment(doc, obs, "Problem Observation");
				
				element(doc, obs, "templateId", "root", "2.16.840.1.113883.10.20.22.4.4", "extension", "2015-08-01");
	
				element(doc, obs, "id", "root", "ab1791b0-5c71-11db-b0de-0800200c9a66");
				
				element(doc, obs, "code", "code", "64572001", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Condition");

				element(doc, obs, "statusCode", "code", "completed");
				
				Element diagEffTm = element(doc, obs, "effectiveTime");
				
				comment(doc, diagEffTm, "ONSET DATETIME");

				element(doc, diagEffTm, "low", "value", MeasureSets.convertSecondDate(admission, 1111111));

				comment(doc, diagEffTm, "RESOLVED DATETIME");

				element(doc, diagEffTm, "high", "value", MeasureSets.convertSecondDate(admission, 2222222));
				
				element(doc, obs, "value", "code", "Z38.00", "codeSystem", "2.16.840.1.113883.6.90", "displayName", "Single liveborn infant  delivered vaginally", "xsi:type", "CD");
				
				//diagnosis(ele);
				
			}
			
			private static void assessmentPerformed(Element ele) throws DOMException, ParseException
			{
				comment(doc, ele, "Assessment, Performed: Estimated Gestational Age at Birth (result >= 37 week(s))\" starts during Occurrence A of $EncounterInpatient");
				
				Element entry = element(doc, ele, "entry", "typeCode", "DRIV");
				
				Element obs = element(doc, entry, "observation", "classCode", "OBS", "moodCode", "EVN");
				
				comment(doc, obs, "Assessment Performed");

				element(doc, obs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.144", "extension", "2017-08-01");
				
				element(doc, obs, "id", "root", "d55c711a-f958-4807-bdb6-45fe5b6195c0");
				
				element(doc, obs, "code", "code", "412726003", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "LOINC", "displayName", "Length of gestation at birth (observable entity)");
				
				element(doc, obs, "text", "Assessment Performed: Length of gestation at birth (observable entity)");

				element(doc, obs, "statusCode", "code", "completed");

				comment(doc, obs, "QDM Attribute: Result");

				element(doc, obs, "value", "xsi:type", "PQ", "value", "38", "unit", "wk");

				Element author = element(doc, obs, "author");
				
				element(doc, author, "templateId", "root", "2.16.840.1.113883.10.20.24.3.155", "extension", "2017-08-01");
				
				element(doc, author, "time", "value", discharge);
				
				Element assignedAuthor = element(doc, author, "assignedAuthor");
				
				element(doc, assignedAuthor, "id", "nullFlavor", "NA");
			}
				
				private static void diagnosis(Element ele) throws DOMException, ParseException
				{
					Comment diagStartCmt = doc.createComment("Diagnosis: Single Live Birth\" using \"Single Live Birth SNOMEDCT Value Set (2.16.840.1.113883.3.117.1.7.1.25)");
					ele.appendChild(diagStartCmt);
					
					Element diagEntry = doc.createElement("entry");
					ele.appendChild(diagEntry);
					
					Element diagAct = doc.createElement("act");
					((Element)diagAct).setAttribute("classCode", "ACT");
					((Element)diagAct).setAttribute("moodCode", "EVN");
					diagEntry.appendChild(diagAct);
					
					Comment cmtDiagTempId1 = doc.createComment("Conforms to C-CDA R2 Procedure Activity Procedure (V2)");
					diagAct.appendChild(cmtDiagTempId1);
					Element diagTemplateId1 = doc.createElement("templateId");
					((Element)diagTemplateId1).setAttribute("root", "2.16.840.1.113883.10.20.22.4.3");
					((Element)diagTemplateId1).setAttribute("extension", "2015-08-01");
					diagAct.appendChild(diagTemplateId1);
					
					Comment cmtDiagTempId2 = doc.createComment("Diagnosis Concern Act");
					diagAct.appendChild(cmtDiagTempId2);
					Element diagTemplateId2 = doc.createElement("templateId");
					((Element)diagTemplateId2).setAttribute("root", "2.16.840.1.113883.10.20.24.3.137");
					((Element)diagTemplateId2).setAttribute("extension", "2016-08-01");
					diagAct.appendChild(diagTemplateId2);
					
					Element diagId = doc.createElement("id");
					((Element)diagId).setAttribute("root", "ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7");
					diagAct.appendChild(diagId);
					
					Element diagCode = doc.createElement("code");
					((Element)diagCode).setAttribute("code", "CONC");
					((Element)diagCode).setAttribute("codeSystem", "2.16.840.1.113883.5.6");
					((Element)diagCode).setAttribute("displayName", "Concern");
					diagAct.appendChild(diagCode);
					
					Comment cmtDiagStatusCode = doc.createComment("The statusCode represents whether this is an active (active) or resolved (completed) diagnosis");
					diagAct.appendChild(cmtDiagStatusCode);
					Element diagStatusCode = doc.createElement("statusCode");
					((Element)diagStatusCode).setAttribute("code", "active");
					diagAct.appendChild(diagStatusCode);
					
					Comment cmtDiagAuthorTm = doc.createComment("This is the time that the diagnosis is authored in the patient's chart");
					diagAct.appendChild(cmtDiagAuthorTm);
					Element diagAuthorTm = doc.createElement("effectiveTime");
					diagAct.appendChild(diagAuthorTm);
					
					Comment cmtEffctiveTm = doc.createComment("equivalent to author/time in the contained observation");
					diagAuthorTm.appendChild(cmtEffctiveTm);
					
					Element diagAuthLow = doc.createElement("low");
					((Element)diagAuthLow).setAttribute("value", MeasureSets.convertSecondDate(admission, 1500));
					diagAuthorTm.appendChild(diagAuthLow);
					
					Element diagAuthHigh= doc.createElement("high");
					((Element)diagAuthHigh).setAttribute("value", MeasureSets.convertSecondDate(admission, 1500));
					diagAuthorTm.appendChild(diagAuthHigh);
					
					Element diagEntryRelationship = doc.createElement("entryRelationship");
					((Element)diagEntryRelationship).setAttribute("typeCode", "SUBJ");
					diagAct.appendChild(diagEntryRelationship);
					
					Comment diagnosisStart = doc.createComment("Diagnosis");
					diagEntryRelationship.appendChild(diagnosisStart);
					
					Element diagObservation = doc.createElement("observation");
					((Element)diagObservation).setAttribute("classCode", "OBS");
					((Element)diagObservation).setAttribute("moodCode", "EVN");
					diagEntryRelationship.appendChild(diagObservation);
					
					Element obsTempId1 = doc.createElement("templateId");
					((Element)obsTempId1).setAttribute("root", "2.16.840.1.113883.10.20.22.4.4");
					((Element)obsTempId1).setAttribute("extension", "2015-08-01");
					diagObservation.appendChild(obsTempId1);
					
					Comment diagTemplate = doc.createComment("Diagnosis Template");
					diagObservation.appendChild(diagTemplate);
					Element obsTempId2 = doc.createElement("templateId");
					((Element)obsTempId2).setAttribute("root", "2.16.840.1.113883.10.20.24.3.135");
					diagObservation.appendChild(obsTempId2);
					
					Element obsId = doc.createElement("id");
					((Element)obsId).setAttribute("root", "e5d9e01e-d778-40ba-9bd0-351d0222b26c");
					diagObservation.appendChild(obsId);
					
					Element obsCode = doc.createElement("code");
					((Element)obsCode).setAttribute("code", "29308-4");
					((Element)obsCode).setAttribute("displayName", "diagnosis");
					((Element)obsCode).setAttribute("codeSystem", "2.16.840.1.113883.6.1");
					((Element)obsCode).setAttribute("codeSystemName", "LOINC");
					diagObservation.appendChild(obsCode);
					
					Element obsTranslation = doc.createElement("translation");
					((Element)obsTranslation).setAttribute("code", "75323-6");
					((Element)obsTranslation).setAttribute("codeSystem", "2.16.840.1.113883.6.1");
					((Element)obsTranslation).setAttribute("codeSystemName", "LOINC");
					((Element)obsTranslation).setAttribute("displayName", "Condition");
					obsCode.appendChild(obsTranslation);
					
					Element obsStatusCode = doc.createElement("statusCode");
					((Element)obsStatusCode).setAttribute("code", "completed");
					diagObservation.appendChild(obsStatusCode);
					
					Element obsEffectiveTime = doc.createElement("effectiveTime");
					diagObservation.appendChild(obsEffectiveTime);
					
					Comment cmtOnsetTm = doc.createComment("Onset Datetime (starts during Encounter)");
					obsEffectiveTime.appendChild(cmtOnsetTm);
					Element onsetTm = doc.createElement("low");
					((Element)onsetTm).setAttribute("value", MeasureSets.convertSecondDate(admission, 1000));
					obsEffectiveTime.appendChild(onsetTm);
					
					Comment cmtAbateTm = doc.createComment("Abatement Datetime");
					obsEffectiveTime.appendChild(cmtAbateTm);
					Element abateTm = doc.createElement("high");
					((Element)abateTm).setAttribute("value", MeasureSets.convertSecondDate(admission, 1500));
					obsEffectiveTime.appendChild(abateTm);
					
					Comment cmtDiagnosis = doc.createComment("Diagnosis: Single Live Birth\" using \"Single Live Birth SNOMEDCT Value Set (2.16.840.1.113883.3.117.1.7.1.25)");
					diagObservation.appendChild(cmtDiagnosis);
					Element diagnosis = doc.createElement("value");
					((Element)diagnosis).setAttribute("xsi:type", "CD");
					((Element)diagnosis).setAttribute("code", "169826009");
					((Element)diagnosis).setAttribute("codeSystem", "2.16.840.1.113883.6.96");
					((Element)diagnosis).setAttribute("codeSystemName", "SNOWMED-CT");
					((Element)diagnosis).setAttribute("displayName", "Diabetic gangrene (disorder)");		// changes per measure
					((Element)diagnosis).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.25");
					diagObservation.appendChild(diagnosis);
					
					Comment targetSiteCmt = doc.createComment("QDM Attribute Anatomical Location Site");
					diagObservation.appendChild(targetSiteCmt);
					Element qdmAttributeAnatomicalLocationSite = doc.createElement("targetSiteCode");
					((Element)qdmAttributeAnatomicalLocationSite).setAttribute("code", "10001005");
					((Element)qdmAttributeAnatomicalLocationSite).setAttribute("codeSystem", "2.16.840.1.113883.6.96");
					((Element)qdmAttributeAnatomicalLocationSite).setAttribute("codeSystemName", "SNOMED-CT");
					((Element)qdmAttributeAnatomicalLocationSite).setAttribute("displayName", "foot");
					((Element)qdmAttributeAnatomicalLocationSite).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.666.5.2256");
					diagObservation.appendChild(qdmAttributeAnatomicalLocationSite);
					
					Comment reasonCmt = doc.createComment("QDM Attirbute: Reason");
					diagAct.appendChild(reasonCmt);
					
					Element reasonEntryRelationship = doc.createElement("entryRelationship");
					((Element)reasonEntryRelationship).setAttribute("typeCode", "RSON");
					diagAct.appendChild(reasonEntryRelationship);
					
					Element reasonObservation = doc.createElement("observation");
					((Element)reasonObservation).setAttribute("classCode", "OBS");
					((Element)reasonObservation).setAttribute("moodCode", "EVN");
					reasonEntryRelationship.appendChild(reasonObservation);
					
					Element reasonTempId = doc.createElement("templateId");
					((Element)reasonTempId).setAttribute("root", "2.16.840.1.113883.10.20.24.3.88");
					((Element)reasonTempId).setAttribute("extension", "2014-12-01");
					reasonObservation.appendChild(reasonTempId);
					
					Element reasonId = doc.createElement("id");
					((Element)reasonId).setAttribute("root", "aa348c15-ce6b-4988-b33b-7ae730c710e2");
					reasonObservation.appendChild(reasonId);
					
					Element reasonCode = doc.createElement("code");
					((Element)reasonCode).setAttribute("code", "77301-0");
					((Element)reasonCode).setAttribute("codeSystem", "2.16.840.1.113883.6.1");
					((Element)reasonCode).setAttribute("codeSystemName", "LOINC");
					((Element)reasonCode).setAttribute("displayName", "Reason care action performed or not");
					reasonObservation.appendChild(reasonCode);
					
					Element reasonStatusCode = doc.createElement("statusCode");
					((Element)reasonStatusCode).setAttribute("code", "completed");
					reasonObservation.appendChild(reasonStatusCode);
					
					Element reasonEffectiveTime = doc.createElement("effectiveTime");
					reasonObservation.appendChild(reasonEffectiveTime);
					
					Element reasonTm = doc.createElement("low");
					((Element)reasonTm).setAttribute("value", MeasureSets.convertSecondDate(admission, 1000));
					reasonEffectiveTime.appendChild(reasonTm);
					
					Comment reasonAbateTmCmt = doc.createComment("Abatement Datetime");
					reasonEffectiveTime.appendChild(reasonAbateTmCmt);
					Element reasonAbateTm = doc.createElement("high");
					((Element)reasonAbateTm).setAttribute("value", MeasureSets.convertSecondDate(admission, 1500));
					reasonEffectiveTime.appendChild(reasonAbateTm);
					
					Element reasonValue = doc.createElement("value");
					((Element)reasonValue).setAttribute("xsi:type", "CD");
					((Element)reasonValue).setAttribute("code", "3950001");
					((Element)reasonValue).setAttribute("codeSystem", "2.16.840.1.113883.6.96");
					((Element)reasonValue).setAttribute("codeSystemName", "SNOWMED-CT");
					((Element)reasonValue).setAttribute("displayName", "Birth (finding)");
					((Element)reasonValue).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.70");
					reasonObservation.appendChild(reasonValue);
					
					Comment severityCmt = doc.createComment("QDM Attribute: Severity");
					diagAct.appendChild(severityCmt);
					
					Element severityEntryRelationship = doc.createElement("entryRelationship");
					((Element)severityEntryRelationship).setAttribute("typeCode", "REFR");
					diagAct.appendChild(severityEntryRelationship);
					
					Element severityObservation = doc.createElement("observation");
					((Element)severityObservation).setAttribute("classCode", "OBS");
					((Element)severityObservation).setAttribute("moodCode", "EVN");
					severityEntryRelationship.appendChild(severityObservation);
					
					Element severityTempId = doc.createElement("templateId");
					((Element)severityTempId).setAttribute("root", "2.16.840.1.113883.10.20.22.4.8");
					((Element)severityTempId).setAttribute("extension", "2014-06-09");
					severityObservation.appendChild(severityTempId);
					
					Element severityCode = doc.createElement("code");
					((Element)severityCode).setAttribute("code", "SEV");
					((Element)severityCode).setAttribute("codeSystem", "2.16.840.1.113883.5.4");
					((Element)severityCode).setAttribute("codeSystemName", "ActCode");
					((Element)severityCode).setAttribute("displayName", "Severity Observation");
					severityObservation.appendChild(severityCode);
					
					Element severityStatusCode = doc.createElement("statusCode");
					((Element)severityStatusCode).setAttribute("code", "completed");
					severityObservation.appendChild(severityStatusCode);
					
					Element severityValue = doc.createElement("value");
					((Element)severityValue).setAttribute("xsi:type", "CD");
					((Element)severityValue).setAttribute("code", "371924009");
					((Element)severityValue).setAttribute("codeSystem", "2.16.840.1.113883.6.96");
					((Element)severityValue).setAttribute("codeSystemName", "SNOWMED-CT");
					((Element)severityValue).setAttribute("displayName", "Moderate to severe");		// changes per measure
					severityObservation.appendChild(severityValue);
				}
				
				private static void denominatorExclusion(Element ele, String type, String codeName, String displayName) throws ParseException
				{
					comment(doc, ele, "DENOMINATOR EXCLUSION");
					switch(type) {
					case "NICU":
						nicu(ele);
						break;
					case "discharge":
						comment(doc, ele, "Start Discharge Disposition");
						element(doc, ele, "sdtc:dischargeDispositionCode", "code", codeName, "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", displayName);
						comment(doc, ele, "End Discharge Disposition");
						break;
					default:
						break;
					}
				}
					
					private static void nicu(Element ele) {
						comment(doc, ele, "Start Facility Location");
						Element par = element(doc, ele, "participant", "typeCode", "LOC");
						
						comment(doc, par, "Facility Location Template");
						element(doc, par, "templateId", "root", "2.16.840.1.113883.10.20.24.3.100", "extension", "2017-08-01");
						
						Element time = element(doc, par, "time");
						
						comment(doc, time, "QDM Attribute: Location Period");
						element(doc, time, "low", "value", admission);
						
						element(doc, time, "high", "value", discharge);
						
						Element parRole = element(doc, par, "participantRole", "classCode", "SDLOC");
						
						element(doc, parRole, "code", "code", "405269005", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", "Neonatal intensive care unit (environment)");
					}
					
					
			private static void numerator(Element ele) throws DOMException, ParseException 
			{
				comment(doc, ele, "Numerator");
				comment(doc, ele, "QDM Datatype: Medication, Administered and Substance Administration");
				Element entry = element(doc, ele, "entry");
				
				Element subAdmin = element(doc, entry, "substanceAdministration", "classCode", "SBADM", "moodCode", "EVN");
				
				element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.22.4.16", "extension", "2014-06-09");
				
				element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.24.3.42", "extension", "2017-08-01");
				
				element(doc, subAdmin, "id", "root", "9069c123-80ad-47c8-a633-9dc02018ae56");

				element(doc, subAdmin, "statusCode", "code", "completed");

				Element effTm = element(doc, subAdmin, "effectiveTime", "xsi:type", "IVL_TS");

				element(doc, effTm, "low", "value", MeasureSets.convertSecondDate(admission, 1500));

				element(doc, effTm, "high", "value", MeasureSets.convertSecondDate(admission, 1500));
				
				comment(doc, subAdmin, "QDM Attribute: Frequency");
				Element freqEffTm = element(doc, subAdmin, "effectiveTime", "xsi:type", "PIVL_TS", "institutionSpecified", "true", "operator", "A");
				
				element(doc, freqEffTm, "period", "value", "6", "unit", "h");
				
				comment(doc, subAdmin, "QDM Attribute: Dosage");
				element(doc, subAdmin, "doseQuantity", "value", "1");
				
				Element con = element(doc, subAdmin, "consumable");
				
				Element man = element(doc, con, "manufacturedProduct", "classCode", "MANU");
				
				element(doc, man, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");
				
				element(doc, man, "id", "root", "37bfe02a-3e97-4bd6-9197-bbd0ed0de79e");
				
				Element manMat = element(doc, man, "manufacturedMaterial");

				element(doc, manMat, "code", "code", "226789007", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "RxNorm", "displayName", "Breast milk (substance)");
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
