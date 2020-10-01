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

public class STK5MeasureSection extends Elements {
	private static Document doc;
	private static Element elem;
	private static String reportingPeriodStart;
	private static String reportingPeriodEnd;
	
	private static String admission;
	public static String discharge;
	
	public STK5MeasureSection(String ccn) throws DOMException, ParseException
	{
		try {
			reportingPeriodStart = MeasureSets.rptPrd[0];
			reportingPeriodEnd = MeasureSets.rptPrd[1];
			admission = MeasureSets.startDate();
			discharge = MeasureSets.endDate();
			discharge = MeasureSets.convertSecondDate(admission, 3000000);
			
			STK.condition += "_STK-5_" + discharge;			
			XmlDocumentBuilder.setPatientID(STK.condition);

			doc = XmlDocumentBuilder.documentFactory();
			elem = XmlDocumentBuilder.clinicalDocumentRoot();
			XmlDocumentBuilder.qrdaHeader(ccn);
			qrdaBody();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			FileCreator.errorMsgTxtbox.setText(e.toString());
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - STK-5 QRDA body", e.getCause());
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
	
		private static void measureSection(Element ele) 
		{
			// Measure Section title & start
			element(doc, ele, "title", "Measure Section");
			
			// text
			element(doc, ele, "text");
				
			// beginning entry
			Element entry = element(doc, ele, "entry");
			
			Element organizer = element(doc, entry, "organizer", "classCode", "CLUSTER", "moodCode", "EVN");
			
			// Measure Reference Template ID
			comment(doc, organizer, "This is the templateId for Measure Reference");
			element(doc, organizer, "templateId", "root", "2.16.840.1.113883.10.20.24.3.98");
	
			// eMeasure Reference QDM Template ID
			comment(doc, organizer, "This is the templateId for eMeasure Reference QDM");
			element(doc, organizer, "templateId", "root", "2.16.840.1.113883.10.20.24.3.97");
	
			element(doc, organizer, "id", "root", "40280381-4b9a-3825-014b-c21e526d0806");
	
			element(doc, organizer, "statusCode", "code", "completed");
			
			Element reference = element(doc, organizer, "reference", "typeCode", "REFR");
			
			Element externalDocument = element(doc, reference, "externalDocument", "classCode", "DOC", "moodCode", "EVN");
			
			comment(doc, externalDocument, "SHALL: This is the version specific identifier for eMeasure:   QualityMeasureDocument/id  it is a GUID");
			element(doc, externalDocument, "id", "root", "2.16.840.1.113883.4.738", "extension", "40280382-68d3-a5fe-0169-265f86bb2032");
			
			comment(doc, externalDocument, "Title of the eMeasure");
			element(doc, externalDocument, "text", "STK-5 : Antithrombotic Therapy By End of Hospital Day 2");
		}
		
		private static void patientData(Element ele) throws DOMException, ParseException
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
			// Denominator Exception is within the Inpatient Encounter
			inpatientEncounter(section);
			
			if(FileCreator.numerator == true) {
				numerator(section);
			}
			
			if(FileCreator.denominatorException == true) {
				denominatorException(section);
			}

			paymentSection(doc, section);
		}
			
			private static void inpatientEncounter(Element ele) throws ParseException 
			{
				comment(doc, ele, "QDM Datatype: Encounter, Performed");
				
				Element entry = element(doc, ele, "entry");
				
				Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN");
				
				comment(doc, act, "Encounter Performed Act (V2)");
				
				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.133", "extension", "2017-08-01");
				
				element(doc, act, "id", "root", "ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7");
				
				element(doc, act, "code", "code", "ENC", "codeSystem", "2.16.840.1.113883.5.6", "codeSystemName", "ActClass", "displayName", "Encounter");
				
				Element entryRelationship = element(doc, act, "entryRelationship", "typeCode", "SUBJ");
				
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
				
				//comment(doc, encounter, "DENOMINATOR");
				if(FileCreator.denominatorExclusion == true) {
					comment(doc, encounter, "Start of Discharge Disposition Start Here");
					String endDateTime = MeasureSets.convertSecondDate(admission, 1000000);
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getDenominatorExclusion(FileCreator.measureSetDropdownBx.getSelectedIndex()));
						if(temp.toUpperCase().contains("COMFORT")) {
							denominatorExclusion(doc, ele, "comfort", null, null);
						}
						if(temp.toLowerCase().contains("duration")) {
							denominatorExclusion(doc, effTime, "duration", null, null);
							MeasureSets.convertSecondDate(admission, 1000000);
							endDateTime = MeasureSets.convertSecondDate(admission, 1000000);
						}
						if(temp.toLowerCase().contains("therapy")) {
							denominatorExclusion(doc, ele, "therapy", null, null);
						}
					}
					else {
						String[] types = MeasureParameters.getDenominatorExclusion(FileCreator.measureSetDropdownBx.getSelectedIndex());
						String type = types[FileCreator.denexBox.getSelectedIndex()];
						
						if(type.toUpperCase().contains("COMFORT")) {
							denominatorExclusion(doc, ele, "comfort", null, null);
						}
						if(type.toLowerCase().contains("duration")) {
							denominatorExclusion(doc, effTime, "duration", null, null);
							endDateTime = MeasureSets.convertSecondDate(admission, 1000000);
						}
						if(type.toLowerCase().contains("therapy")) {
							denominatorExclusion(doc, ele, "therapy", null, null);
						}
					}
					// End datetime to make sure it's still added when denex = true and selection != 'duration < 2 days'
					element(doc, effTime, "high", "value", endDateTime);
				}
				else {
					// End datetime
					element(doc, effTime, "high", "value", discharge);
				}
				
				comment(doc, encounter, "End of Discharge Disposition Start Here");
				
				// Start of principal diagnostic codes
				comment(doc, encounter, "START OF PRINCIPAL DIAG CODES START HERE");
				comment(doc, encounter, "QDM Attribute: Principal Diagnosis");
				
				Element diagEntryRelationship = element(doc, encounter, "entryRelationship", "typeCode", "REFR");
				
				Element observation = element(doc, diagEntryRelationship, "observation", "classCode", "OBS", "moodCode", "EVN");
				
				element(doc, observation, "templateId", "root", "2.16.840.1.113883.10.20.24.3.152", "extension", "2017-08-01");
				
				element(doc, observation, "id", "root", "92587992-6147-467e-8ce7-b080ef569df4");
				
				// Diagnosis Code
				element(doc, observation, "code", "code", "8319008", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED", "displayName", "Principal Diagnosis");
				
				element(doc, observation, "value", "code", "230692004", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", "Infarction - precerebral (disorder)", "xsi:type", "CD"); //, "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.247",
				
				comment(doc, encounter, "END OF PRINCIPAL DIAG CODES START HERE");
			}
				
				private static void denominatorExclusion(Document doc, Element ele, String type, String codeName, String displayName) throws DOMException, ParseException
				{
					comment(doc, ele, "DENOMINATOR EXCLUSION");
					
					switch(type) {
					case "therapy":
						therapy(ele);
						break;
					case "duration":
						comment(doc, ele, "Discharge Data less than 2 days");
						break;
					case "comfort":
						comfortMeasures(ele);
						break;
					default:
						break;
					}
				}
				
				private static void therapy(Element ele) throws DOMException, ParseException
				{
					comment(doc, ele, "DENOMINATOR");
					comment(doc, ele, "QDM Datatype: Medication, Administered and Substance Administration");
					
					Element entry = element(doc, ele, "entry");
					
					Element sub = element(doc, entry, "substanceAdministration", "classCode", "SBADM", "moodCode", "EVN");
					
					comment(doc, sub, "C-CDA R2.1 Medication Activity (V2)");
					element(doc, sub, "templateId", "root", "2.16.840.1.113883.10.20.22.4.16", "extension", "2014-06-09");
					
					comment(doc, sub, "Medication Administered (V4)");
					element(doc, sub, "templateId", "root", "2.16.840.1.113883.10.20.24.3.42", "extension", "2017-08-01");
					
					element(doc, sub, "id", "root", "9069c123-80ad-47c8-a633-9dc02018ae56");
					
					element(doc, sub, "statusCode", "code", "completed");
					
					Element effTm = element(doc, sub, "effectiveTime", "xsi:type", "IVL_TS");
					
					element(doc, effTm, "low", "value", MeasureSets.convertSecondDate(admission, 1000));
					
					element(doc, effTm, "high", "value", MeasureSets.convertSecondDate(admission, 1000));
					
					comment(doc, sub, "QDM Attribute: Frequency");					
					Element freqEffTm = element(doc, sub, "effectiveTime", "xsi:type", "PIVL_TS", "institutionSpecified", "true", "operator", "A");
					
					element(doc, freqEffTm, "period", "value", "6", "unit", "h");
					
					comment(doc, sub, "QDM Attribute: Dosage");
					element(doc, sub, "doseQuantity", "value", "1");
					
					Element con = element(doc, sub, "consumable");
					
					Element manP = element(doc, con, "manufacturedProduct", "classCode", "MANU");
					
					comment(doc, manP, "Conforms to C-CDA R2 Medication Information (V2)");
					element(doc, manP, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");
					
					element(doc, manP, "id", "root", "37bfe02a-3e97-4bd6-9197-bbd0ed0de79e");
					
					Element manM = element(doc, manP, "manufacturedMaterial");
					
					element(doc, manM, "code", "code", "1804799", "codeSystem", "2.16.840.1.113883.6.88", "codeSystemName", "RXNORM", "displayName", "alteplase 100 MG Injection");
				}
				
				private static void comfortMeasures(Element ele) throws DOMException, ParseException
				{				
					Comment intervention = doc.createComment(" OR: $InterventionComfortMeasures <= 1 day(s) starts after start of Occurrence A of $EncounterInpatientNonElective");
					ele.appendChild(intervention);
					
					Comment interPerf = doc.createComment("Intervention Performed");
					ele.appendChild(interPerf);
					
					Element entry = doc.createElement("entry");
					ele.appendChild(entry);
					
					Element act = doc.createElement("act");
					((Element)act).setAttribute("classCode", "ACT");
					((Element)act).setAttribute("moodCode", "RQO");
					entry.appendChild(act);
					
					Comment conformation = doc.createComment("Conforms to C-CDA R2.1 Procedure Activity Act (V2)");
					act.appendChild(conformation);
					
					Element tempId1  = doc.createElement("templateId");
					((Element)tempId1).setAttribute("root", "2.16.840.1.113883.10.20.22.4.39");
					((Element)tempId1).setAttribute("extension", "2014-06-09");
					act.appendChild(tempId1);
					
					//Intervention performed
					act.appendChild(interPerf);
					
					Element tempId2  = doc.createElement("templateId");
					((Element)tempId2).setAttribute("root", "2.16.840.1.113883.10.20.24.3.31");
					((Element)tempId2).setAttribute("extension", "2017-08-01");
					act.appendChild(tempId2);
					
					Element id = doc.createElement("id");
					((Element)id).setAttribute("root", "db734647-fc99-424c-a864-7e3cda82e703");
					act.appendChild(id);
					
					Element code = doc.createElement("code");
					((Element)code).setAttribute("code", "133918004");
					((Element)code).setAttribute("codeSystem", "2.16.840.1.113883.6.96");
					((Element)code).setAttribute("codeSystemName", "SNOMED-CT");
					((Element)code).setAttribute("displayName", "Comfort measures (regime/therapy)");
					((Element)code).setAttribute("sdtc:valueSet", "1.3.6.1.4.1.33895.1.3.0.45");
					act.appendChild(code);
					
					Element statusCode = doc.createElement("statusCode");
					((Element)statusCode).setAttribute("code", "active");
					act.appendChild(statusCode);
					
					Element author = doc.createElement("author");
					act.appendChild(author);
					
					Element authTmpId = doc.createElement("templateId");
					((Element)authTmpId).setAttribute("root", "2.16.840.1.113883.10.20.24.3.155");
					((Element)authTmpId).setAttribute("extension", "2017-08-01");
					author.appendChild(authTmpId);
					
					Element authTm = doc.createElement("time");
					((Element)authTm).setAttribute("value", admission);
					author.appendChild(authTm);
					
					
					Element assignedAuthor = doc.createElement("assignedAuthor");
					author.appendChild(assignedAuthor);
					
					Element assignedAuthId = doc.createElement("id");
					((Element)assignedAuthId).setAttribute("nullFlavor", "NA");
					assignedAuthor.appendChild(assignedAuthId);
				}
			
			private static void denominatorException(Element ele) throws DOMException, ParseException
			{
				//element(doc, ele, "sdtc:dischargeDispositionCde", "code", "306701001", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to Acute Care Facility", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.87");
				//element(doc, ele, "sdtc:dischargeDispositionCde", "code", "428371000124100", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to healthcare facility for hospice care (procedure)", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.207");
				//element(doc, ele, "sdtc:dischargeDispositionCode", "code", "428361000124107", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to home for hospice care (procedure)", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.209");
				comment(doc, ele, "DENOMINATOR EXCEPTION START");

				comment(doc, ele, "Medication, Order not done: Patient Refusal");

				Element entry = element(doc, ele, "entry");

//					Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN", "negationInd", "true");
//	
//					comment(doc, act, "Discharge Medication - Active Medication");
//	
//					element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.105", "extension", "2016-02-01");
//	
//					element(doc, act, "id", "root", "60f33340-591f-4459-9fa2-1c93e014a6e2");
//	
//					element(doc, act, "code", "code", "75311-1", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Discharge medication");
//	
//					Element entryRelationship = element(doc, act, "entryRelationship", "typeCode", "SUBJ");

				Element subAdmin = element(doc, entry, "substanceAdministration", "classCode", "SBADM", "moodCode", "RQO", "negationInd", "true");

				comment(doc, subAdmin, "Conforms to C-CDA R2 Medication Activity (V2)");

				element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.22.4.42", "extension", "2014-06-09");
				
				element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.24.3.47", "extension", "2018-10-01");
				
				element(doc, subAdmin, "id", "root", "9a5f4d94-ccad-4d57-80ea-27737545c7bb");

				element(doc, subAdmin, "text", "Medication active: 0.09 MG/ACTUAT inhalant solution, 2 puffs QID PRN wheezing");
	
				element(doc, subAdmin, "statusCode", "code", "active");

				Element subAdminEffTm = element(doc, subAdmin, "effectiveTime", "xsi:type", "IVL_TS");
				
				comment(doc, subAdminEffTm, "QDM Attributes: Start datetime");
				element(doc, subAdminEffTm, "low", "value", MeasureSets.convertSecondDate(admission, 10000));

				comment(doc, subAdminEffTm, "QDM Attributes: End datetime");
				//element(doc, subAdminEffTm, "high", "value", MeasureSets.convertSecondDate(admission, 3000));
				element(doc, subAdminEffTm, "high", "nullFlavor", "UNK");
				
				comment(doc, subAdmin, "QDM Attribute: Frequency");
				Element freqEffTm = element(doc, subAdmin, "effectiveTime", "institutionSpecified", "true", "operator", "A", "xsi:type", "PIVL_TS");
				
				element(doc, freqEffTm, "period", "value", "6", "unit", "h");

//					comment(doc, subAdmin, "QDM Attribute: Route");
//	
//					comment(doc, subAdmin, "EVENT_TYPE_START");
//	
//					element(doc, subAdmin, "routeCode", "code", "C38288", "codeSystem", "2.16.840.1.113883.3.26.1.1", "codeSystemName", "NCI Thesaurus", "displayName", "ORAL");
//	
//					comment(doc, subAdmin, "EVENT_TYPE_END");

				comment(doc, subAdmin, "QDM Attribute: Dose");
				element(doc, subAdmin, "doseQuantity", "value", "1");

				Element consumable = element(doc, subAdmin, "consumable");

				Element manufacturedProduct = element(doc, consumable, "manufacturedProduct", "classCode", "MANU");
				
				comment(doc, manufacturedProduct, "Conforms to C-CDA R2 Medication Information (V2)");

				element(doc, manufacturedProduct, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");

				element(doc, manufacturedProduct, "id", "root", "37bfe02a-3e97-4bd6-9197-bbd0ed0de79e");
				
				Element manufacturedMaterial = element(doc, manufacturedProduct, "manufacturedMaterial");
				
				element(doc, manufacturedMaterial, "code", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.201", "nullFlavor", "NA");

				Element author = element(doc, subAdmin, "author");

				element(doc, author, "templateId", "root", "2.16.840.1.113883.10.20.24.3.155", "extension", "2017-08-01");

				element(doc, author, "time", "value", MeasureSets.convertSecondDate(admission, 10000));

				Element assignedAuthor = element(doc, author, "assignedAuthor");

				element(doc, assignedAuthor, "id", "nullFlavor", "NA");

				comment(doc, subAdmin, "REASON FOR NOT DONE");

				Element er = element(doc, subAdmin, "entryRelationship", "typeCode", "RSON");

				Element obs = element(doc, er, "observation", "classCode", "OBS", "moodCode", "EVN");

				element(doc, obs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.88", "extension", "2017-08-01");

				element(doc, obs, "code", "code", "77301-0", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Reason care action performed or not");

				comment(doc, obs, "NEGATION_RATIONALE_START");

				element(doc, obs, "value", "code", "183944003", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", "Procedure refused (situation)", "xsi:type", "CD");

				comment(doc, obs, "NEGATION_RATIONALE_END");
				
				comment(doc, ele, "DENOMINATOR EXCEPTION END");
			}
				
			private static void numerator(Element ele) throws DOMException, ParseException
			{
				comment(doc, ele, "NUMERATOR START");
				
				comment(doc, ele, "QDM Datatype: Medication, Administered and Substance Administration");
				
				Element entry = element(doc, ele, "entry");
				
				Element subAdmin = element(doc, entry, "substanceAdministration", "classCode", "SBADM", "moodCode", "EVN");
			
				element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.22.4.16", "extension", "2014-06-09");
			
				comment(doc, subAdmin, "Medication Administered (V4)");
				element(doc, subAdmin, "templateId", "root", "2.16.840.1.113883.10.20.24.3.42", "extension", "2017-08-01");
			
				element(doc, subAdmin, "id", "root", "9069c123-80ad-47c8-a633-9dc02018ae56");
			
				element(doc, subAdmin, "statusCode", "code", "completed");
			
				Element effTm = element(doc, subAdmin, "effectiveTime", "xsi:type", "IVL_TS");
			
				int time = (int) (Math.random() * 1000000) + 1000;
				element(doc, effTm, "low", "value", MeasureSets.convertSecondDate(admission, time));
				
				element(doc, effTm, "high", "value", MeasureSets.convertSecondDate(admission, time + 200));
			
				comment(doc, subAdmin, "QDM Attribute: Frequency");
			
				Element freqEffTm = element(doc, subAdmin, "effectiveTime", "institutionSpecified", "true", "operator", "A", "xsi:type", "PIVL_TS");
			
				element(doc, freqEffTm, "period", "value", "6", "unit", "h");
			
				comment(doc, subAdmin, "QDM Attribute: Dosage");
				element(doc, subAdmin, "doseQuantity", "value", "1");
			
				Element con = element(doc, subAdmin, "consumable");
			
				Element manProd = element(doc, con, "manufacturedProduct", "classCode", "MANU");
				
				comment(doc, manProd, "Medication Information (consolidation) template");
			
				element(doc, manProd, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");
			
				Element manMat = element(doc, manProd, "manufacturedMaterial");
			
				element(doc, manMat, "code", "code", "1037179", "codeSystem", "2.16.840.1.113883.6.88", "codeSystemName", "RXNORM", "displayName", "dabigatran etexilate 75 MG Oral Capsule");
			
				comment(doc, ele, "NUMERATOR END");
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
