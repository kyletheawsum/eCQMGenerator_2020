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

import com.Measures.STK;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureParameters;
import com.xmlEditTool.MeasureSets;

public class STK2MeasureSection extends Elements{
	private static Document doc;
	private static Element elem;
	private static String reportingPeriodStart;
	private static String reportingPeriodEnd;
	
	private static String admission;
	private static String discharge;
	
	public STK2MeasureSection(String ccn) throws DOMException, ParseException
	{
		try {
			reportingPeriodStart = MeasureSets.rptPrd[0];
			reportingPeriodEnd = MeasureSets.rptPrd[1];
			admission = MeasureSets.startDate();
			discharge = MeasureSets.endDate();
			
			STK.condition += "_STK-2_" + discharge;			
			XmlDocumentBuilder.setPatientID(STK.condition);

			doc = XmlDocumentBuilder.documentFactory();
			elem = XmlDocumentBuilder.clinicalDocumentRoot();
			XmlDocumentBuilder.qrdaHeader(ccn);
			qrdaBody();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			FileCreator.errorMsgTxtbox.setText(e.toString());
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - STK-2 QRDA body", e.getCause());
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
			Element text = element(doc, ele, "text");
			
			Element table = element(doc, text, "table", "border", "1", "width", "100%");
			
			Element tableHead = element(doc, table, "thead");
			
			Element trHead = element(doc, tableHead, "tr");
					
			String[] heads = {"eMeasure Title", "Version neutral identifier", "eMeasure Version Number", "NQF eMeasure Number", "Version specific identifier"};
			for(String head : heads) {
				element(doc, trHead, "th", head);
			}	
			
			Element tableBody = element(doc, table, "tbody");
			
			Element trBody = element(doc, tableBody, "tr");
			
			String[] bodies = {"Discharged on Antithrombotic Therapy", "42bf391f-38a3-4c0f-9ece-dcd47e9609d9", "5.2", "0435","40280382-5abd-fa46-015b-168d32422577"};
			for(String body : bodies) {
				element(doc, trBody, "td", body);
			}	
	
			// beginning entry
			Element entry = element(doc, ele, "entry");
			
			Element organizer = element(doc, entry, "organizer", "classCode", "CLUSTER", "moodCode", "EVN");
			
			// Measure Reference Template ID
			comment(doc, organizer, "This is the templateId for Measure Reference");
			element(doc, organizer, "templateId", "root", "2.16.840.1.113883.10.20.24.3.98");
	
			// eMeasure Reference QDM Template ID
			comment(doc, organizer, "This is the templateId for eMeasure Reference QDM");
			element(doc, organizer, "templateId", "root", "2.16.840.1.113883.10.20.24.3.97");
	
			element(doc, organizer, "id", "root", "7511717d-2498-43ec-99c9-766490421aa0");
	
			element(doc, organizer, "statusCode", "code", "completed");
			
			Element reference = element(doc, organizer, "reference", "typeCode", "REFR");
			
			Element externalDocument = element(doc, reference, "externalDocument", "classCode", "DOC", "moodCode", "EVN");
			
			
			String[] cmts = {"SHALL: This is the version specific identifier for eMeasure:   QualityMeasureDocument/id  it is a GUID", "SHOULD: This is the NQF Number, root is an NQF OID and for    eMeasure Number and extension is the eMeasure's NQF number"};
			String[] roots = {"2.16.840.1.113883.4.738", "2.16.840.1.113883.3.560.1"};
			String[] exts = {"40280382-68d3-a5fe-0169-25a50ef41f38", "0435"};					// changes per measure
			for(int i = 0; i < 2; i++) {
				comment(doc, externalDocument, cmts[i]);
				element(doc, externalDocument, "id", "root", roots[i], "extension", exts[i]);
			}
			
			comment(doc, externalDocument, "Title of the eMeasure");
			element(doc, externalDocument, "text", "Discharged on Antithrombotic Therapy");
			
			element(doc, externalDocument, "setId", "root", "40280382-610b-e7a4-0161-8afcb0532d8a");
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
			
			Element text = element(doc, section, "text");
			
			Element table = element(doc, text, "table", "border", "1", "width", "100%");
	
			Element tableHead = element(doc, table, "thead");
			
			Element trHead = element(doc, tableHead, "tr");
	
			element(doc, trHead, "th", "Data Element - Value");
			
			Element tableBody = element(doc, table, "tbody");
			
			String[] arr = {"Intervention, Performed: Palliative Care - palliative care (regime/therapy)",
							"Patient Characteristic Clinical Trial Participant: Clinical Trial Participant - Clinical Trial Participant (person)",
							"Diagnosis, Active: Hemorrhagic Stroke - Intracerebral hemorrhage",
							"Intervention, Order: Palliative Care - palliative care (regime/therapy)",
							"Encounter, Performed: Inpatient Encounter - hospital admission, emergency, indirect (procedure)",
							"Encounter, Performed: Emergency Department Visit - Emergency Department visit (procedure)",
							"Patient Characteristic Payer: Payer - Medicaid Applicant",
							"Transfer To: Rehabilitation Facility - Rehabilitation Hospital (environment)",
							"Procedure, Performed: Rehabilitation Services - Orofacial Myofunctional Assessment using Computer",
							"Diagnosis, Active: Ischemic Stroke - Cerebral infarction due to thrombosis of left posterior cerebral artery"};	
			for(int i = 0; i < 10; i++) {
				Element trBody = element(doc, tableBody, "tr");
				element(doc, trBody, "td", arr[i]);
			}
	
			// Measure Calculation Starts Here
			comment(doc, section, "Measure Calculations Start Here");
			
			comment(doc, section, "Start of Inpatient Encounter");
			// Denominator Exception is within the Inpatient Encounter
			inpatientEncounter(section);
			
			if(FileCreator.denominatorException == true) {
				denominatorException(section);
			}
	
			if(FileCreator.numerator == true) {
				numerator(section);
			}
	
			paymentSection(doc, section);
		}
		
			private static void inpatientEncounter(Element ele) throws DOMException, ParseException
			{
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
				
				element(doc, encounter, "code", "code", "32485007", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Non-Elective Inpatient Encounter", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.424");
				
				element(doc, encounter, "text", "Non-Elective Inpatient Encounter");
				
				element(doc, encounter, "statusCode", "code", "completed");
				
				Element effTime = element(doc, encounter, "effectiveTime");
				
				// Start datetime
				comment(doc, effTime, "QDM Attribute: Relevant Period - Admission datetime");
				element(doc, effTime, "low", "value", admission);
				
				// End datetime
				comment(doc, effTime, "QDM Attribute: Relevant Period - Discharge datetime");
				element(doc, effTime, "high", "value", discharge);
				
				/**
				 * Denominator (need to re-think this denominator thing.
				 * Looks awfully like the Denominator Exception.
				 */
				//comment(doc, encounter, "DENOMINATOR");
				comment(doc, encounter, "Start of Discharge Disposition Start Here");
				if(FileCreator.denominatorExclusion == true) {
					if(FileCreator.chckbxRandomize.isSelected()) {
						String temp = getRand(MeasureParameters.getDenominatorExclusion(FileCreator.measureSetDropdownBx.getSelectedIndex()));
						if(temp.toUpperCase().contains("COMFORT")) {
							denominatorExclusion(doc, ele, "comfort", null, null);
						}
						else {
							denominatorExclusion(doc, encounter, "discharge", MeasureParameters.denexValues.get(index).toString(), temp);
						}
					}
					else {
						String type = MeasureParameters.denexValues.get(FileCreator.denexBox.getSelectedIndex()).toString();
						if(type.toUpperCase().contains("COMFORT")) {
							denominatorExclusion(doc, ele, "comfort", null, null);
						}
						else {
							denominatorExclusion(doc, encounter, "discharge", MeasureParameters.denexValues.get(FileCreator.denexBox.getSelectedIndex()).toString(), FileCreator.denexBox.getSelectedItem().toString());
						}
					}
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
				
				element(doc, observation, "value", "code", "195216008", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CT", "displayName", "Left sided cerebral hemisphere cerebrovascular accident (disorder)", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.247", "xsi:type", "CD");
				
				comment(doc, encounter, "END OF PRINCIPAL DIAG CODES START HERE");
			}
			
			// DENOMINATOR EXCLUSION
			private static void denominatorExclusion(Document doc, Element ele, String type, String codeName, String displayName) throws ParseException {
				//element(doc, ele, "sdtc:dischargeDispositionCode", "code", "428361000124106", "codeSystem", "2.16.840.1.113883.6.96", "displayName", "Discharge to home for hospice care (procedure)", "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.209");
				
				comment(doc, ele, "DENOMINATOR EXCLUSION");
				
				switch(type) {
				case "discharge": // discharge disposition
					element(doc, ele, "sdtc:dischargeDispositionCode", "code", codeName, "codeSystem", "2.16.840.1.113883.6.96", "displayName", displayName);//, "sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.209");
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
			
			private static void comfortMeasures(Element ele) throws ParseException {
				comment(doc, ele, "Start of Comfort Measures during Hospitalization");
				comment(doc, ele, "Intervention Order");
				Element entry = element(doc, ele, "entry");
				
				Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "RQO");
				comment(doc, act, "Conforms to C-CDA R2.1 Planned Act (VA) template");
				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.22.4.39", "extension", "2014-06-09");
				
				comment(doc, act, "Intervention Order (V4)");
				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.31", "extension", "2017-08-01");
				
				element(doc, act, "id", "root", "db734647-fc99-424c-a864-7e3cda82e703");
				
				// Intervention
				comment(doc, act, "Intervention");
				// Pull Code from spreadsheet here
				element(doc, act, "code", "code", "133918004", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED CD", "displayName", "Comfort measures (regime/therapy)"); //, "sdtc:valueSet", "1.3.6.1.4.1.33895.1.3.0.45");
				
				element(doc, act, "statusCode", "code", "active");
				
				Element author = element(doc, act, "author");
				//comment(doc, author, "C-CDA R2.1 Author Participation");
				element(doc, author, "templateId", "root", "2.16.840.1.113883.10.20.24.3.155", "extension", "2017-08-01");
				
				element(doc, author, "time", "value", MeasureSets.convertFirstDate(discharge, 20000));
				Element assignedAuthor = element(doc, author, "assignedAuthor");
				element(doc, assignedAuthor, "id", "nullFlavor", "NA");
			}
			
		private static void denominatorException(Element ele) throws DOMException, ParseException
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
	
			element(doc, subAdminEffTm, "low", "value", discharge);
	
			comment(doc, subAdminEffTm, "QDM Attributes: End datetime");
	
			//element(doc, subAdminEffTm, "high", "value", MeasureSets.convertSecondDate(admission, 3000));
			element(doc, subAdminEffTm, "high", "nullFlavor", "UNK");
			
			comment(doc, subAdmin, "QDM Attribute: Frequency");
	
			Element freqEffTm = element(doc, subAdmin, "effectiveTime", "institutionSpecified", "true", "operator", "A", "xsi:type", "PIVL_TS");
			
			element(doc, freqEffTm, "period", "value", "6", "unit", "h");
	
//			comment(doc, subAdmin, "QDM Attribute: Route");
//	
//			comment(doc, subAdmin, "EVENT_TYPE_START");
//	
//			element(doc, subAdmin, "routeCode", "code", "C38288", "codeSystem", "2.16.840.1.113883.3.26.1.1", "codeSystemName", "NCI Thesaurus", "displayName", "ORAL");
//	
//			comment(doc, subAdmin, "EVENT_TYPE_END");
	
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
	
			element(doc, author, "templateId", "root", "2.16.840.1.113883.10.20.22.4.119");//, "extension", "2017-08-01");
	
			element(doc, author, "time", "value", discharge);
	
			Element assignedAuthor = element(doc, author, "assignedAuthor");
	
			element(doc, assignedAuthor, "id", "nullFlavor", "NA");
	
			comment(doc, act, "REASON for \"Not Done\"");
	
			Element er = element(doc, act, "entryRelationship", "typeCode", "RSON");
	
			Element obs = element(doc, er, "observation", "classCode", "OBS", "moodCode", "EVN");
	
			element(doc, obs, "templateId", "root", "2.16.840.1.113883.10.20.24.3.88", "extension", "2017-08-01");
	
			element(doc, obs, "code", "code", "77301-0", "codeSystem", "2.16.840.1.113883.6.1", "codeSystemName", "LOINC", "displayName", "Reason care action performed or not");
	
			comment(doc, obs, "NEGATION_RATIONALE_START");
	
			element(doc, obs, "value", "code", "182903008", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMEDCT", "displayName", "Drug declined by patient - reason unknown (situation)", "xsi:type", "CD");
	
			comment(doc, obs, "NEGATION_RATIONALE_END");
			
			comment(doc, ele, "DENOMINATOR EXCEPTION END");
		}
		
		private static void numerator(Element ele) {
			comment(doc, ele, "NUMERATOR START");
	
			comment(doc, ele, "Medication, Discharge: Antithrombotic Therapy\\\" starts during Occurrence A of $EncounterInpatientNonElective");
	
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
			
	//			Comment route = doc.createComment("QDM Attribute: Route");
	//			subAdmin.appendChild(route);
	//			
	//			Element routeCode = doc.createElement("routeCode");
	//			((Element)routeCode).setAttribute("code", "C38288");
	//			((Element)routeCode).setAttribute("codeSystem", "2.16.840.1.113883.3.26.1.1");
	//			((Element)routeCode).setAttribute("codeSystemName", "NCI Thesaurus");
	//			((Element)routeCode).setAttribute("displayName", "ORAL");
	//			//((Element)routeCode).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.222");
	//			subAdmin.appendChild(routeCode);
			
			comment(doc, sub, "QDM Attribute: Dose");
	
			element(doc, sub, "doseQuantity", "value", "1");
	
			Element con = element(doc, sub, "consumable");
	
			Element man = element(doc, con, "manufacturedProduct", "classCode", "MANU");
	
			comment(doc, man, "Conforms to C-CDA R2 Medication Information (V2)");
	
			element(doc, man, "templateId", "root", "2.16.840.1.113883.10.20.22.4.23", "extension", "2014-06-09");
	
			element(doc, man, "id", "root", "37bfe02a-3e97-4bd6-9197-bbd0ed0de79e");
	
			Element manF = element(doc, man, "manufacturedMaterial");
	
			element(doc, manF, "code", "code", "1536467", "codeSystem", "2.16.840.1.113883.6.88", "codeSystemName", "RxNorm", "displayName", "Aspirin 325 MG / Citric Acid 1000 MG / Sodium Bicarbonate 1700 MG Effervescent Oral Tablet");
	
			Element auth = element(doc, sub, "author");
	
			element(doc, auth, "templateId", "root", "2.16.840.1.113883.10.20.22.4.119");
	
			element(doc, auth, "time", "value", admission);
	
			Element asAuth = element(doc, auth, "assignedAuthor");
	
			element(doc, asAuth, "id", "nullFlavor", "NA");
	
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
