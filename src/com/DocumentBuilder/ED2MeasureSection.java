package com.DocumentBuilder;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.Random;
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

import com.Measures.ED;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureSets;

public class ED2MeasureSection extends Elements {
	private static Document doc;
	private static Element elem;
	private static String reportingPeriodStart;
	private static String reportingPeriodEnd;
	
	private static String admission;
	public static String discharge;
	
	public ED2MeasureSection(String ccn) throws DOMException, ParseException
	{
		try {
			reportingPeriodStart = MeasureSets.rptPrd[0];
			reportingPeriodEnd = MeasureSets.rptPrd[1];
			admission = MeasureSets.startDate();
			discharge = MeasureSets.endDate();
			
			ED.condition += "ED-2_" + discharge;
			XmlDocumentBuilder.setPatientID(ED.condition);
			
			doc = XmlDocumentBuilder.documentFactory();
			elem = XmlDocumentBuilder.clinicalDocumentRoot();
			XmlDocumentBuilder.qrdaHeader(ccn);
			qrdaBody();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			FileCreator.errorMsgTxtbox.setText(e.toString());
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - ED-2 QRDA body", e.getCause());
		}
	}

	private void qrdaBody() 
	{
		Element componentTopLevel = doc.createElement("component");
		elem.appendChild(componentTopLevel);
		
		Comment qrdaBody = doc.createComment("QRDA Body");
		componentTopLevel.appendChild(qrdaBody);
		
		Element structuredBody = doc.createElement("structuredBody");
		componentTopLevel.appendChild(structuredBody);
		
		Element componentSection = doc.createElement("component");
		structuredBody.appendChild(componentSection);
		
		Element section = doc.createElement("section");
		componentSection.appendChild(section);

		Comment measureSection = doc.createComment("\n\t*****************************************************************\n"
													+ "\tMeasure Section\n"
													+ "\t*****************************************************************\n\t");
		section.appendChild(measureSection);
		
		Comment templateIdForMeasureSection = doc.createComment("This is the templateId for Measure Section");
		section.appendChild(templateIdForMeasureSection);
		Element measureSectionTempId = doc.createElement("templateId");
		((Element)measureSectionTempId).setAttribute("root", "2.16.840.1.113883.10.20.24.2.2");
		section.appendChild(measureSectionTempId);
		
		Comment templateIdForMeasureSectionQDM = doc.createComment("This is the templateId for Measure Section QDM");
		section.appendChild(templateIdForMeasureSectionQDM);
		Element measureSectionTempIdQDM = doc.createElement("templateId");
		((Element)measureSectionTempIdQDM).setAttribute("root", "2.16.840.1.113883.10.20.24.2.3");
		section.appendChild(measureSectionTempIdQDM);
		
		Comment loincCode = doc.createComment("LOINC Code for \"Measure Document\". This stays the same for all QRDA measure sections.");
		section.appendChild(loincCode);
		Element code = doc.createElement("code");
		((Element)code).setAttribute("code", "55186-1");
		((Element)code).setAttribute("codeSystem", "2.16.840.1.113883.6.1");
		section.appendChild(code);
		
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
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode("Measure Section"));
			ele.appendChild(title);
			
			Element text = doc.createElement("text");
			ele.appendChild(text);
				
			Element entry = doc.createElement("entry");
			ele.appendChild(entry);
			
			Element org = element(doc, entry, "organizer", "classCode", "CLUSTER", "moodCode", "EVN");

			comment(doc, org, "This is the templateId for Measure Reference");
			element(doc, org, "templateId", "root", "2.16.840.1.113883.10.20.24.3.98");

			comment(doc, org, "This is the templateId for eMeasure Reference QDM");
			element(doc, org, "templateId", "root", "2.16.840.1.113883.10.20.24.3.97");

			element(doc, org, "id", "root", "40280381-4b9a-3825-014b-c21e526d0806");

			element(doc, org, "statucCode", "code", "completed");

			Element ref = element(doc, org, "reference", "typeCode", "REFR");

			Element exDoc = element(doc, ref, "externalDocument", "classCode", "DOC", "moodCode", "EVN");

			comment(doc, exDoc, "SHALL: This is the version specific identifier for eMeasure: CMD108v8");
			element(doc, exDoc, "id", "root", "2.16.840.1.113883.4.738", "extension", "40280382-68d3-a5fe-0169-06ff09260e87");
			
			comment(doc, exDoc, "SHOULD This is the title of the eMeasure");
			element(doc, exDoc, "text", "ED-2: Median Admit Decision Time to ED Departure Time for Admitted Patients");
		}
		
		private static void patientData(Element ele) throws ParseException 
		{
			Comment measureSection = doc.createComment("\n\t*****************************************************************\n"
					+ "\"Patient Data Section\n"
					+ "\t*****************************************************************\n\t");
			ele.appendChild(measureSection);

			Element componentSection = doc.createElement("component");
			ele.appendChild(componentSection);

			Element section = doc.createElement("section");
			componentSection.appendChild(section);
			
			Element tmpId = doc.createElement("templateId");
			((Element)tmpId).setAttribute("root", "2.16.840.1.113883.10.20.17.2.4");
			section.appendChild(tmpId);
			
			String[] cmt = {"Updated extension for HQR11.1","Updated templateID and extension for HQR11.1"};
			String[] root = {"2.16.840.1.113883.10.20.24.2.1","2.16.840.1.113883.10.20.24.2.1.1"};
			String[] ext = {"2018-10-01","2019-02-01"};
			for(int i = 0; i < 2; i++) {
				Comment c = doc.createComment(cmt[i]);
				section.appendChild(c);
				Element e = doc.createElement("templateId");
				((Element)e).setAttribute("root", root[i]);
				((Element)e).setAttribute("extension", ext[i]);
				section.appendChild(e);
			}
			
			Element code = doc.createElement("code");
			((Element)code).setAttribute("code", "55188-7");
			((Element)code).setAttribute("codeSystem", "2.16.840.1.113883.6.1");
			section.appendChild(code);
			
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode("Patient Data"));
			section.appendChild(title);
			
			Element text = doc.createElement("text");
			section.appendChild(text);
						
			edEncounter(section);
			inpatientEncounter(section);
			//admitToHospital(section);
			
			paymentSection(doc, section);
		}
		
			private static void inpatientEncounter(Element ele) throws DOMException, ParseException
			{
				Comment inpatientEncounter = doc.createComment("Encounter, Performed: Encounter Inpatient");
				ele.appendChild(inpatientEncounter);
				
				Element entry = doc.createElement("entry");
				ele.appendChild(entry);
				
				Element act = element(doc, entry, "act", "classCode", "ACT", "moodCode", "EVN");

				comment(doc, act, "Encounter performed Act");

				element(doc, act, "templateId", "root", "2.16.840.1.113883.10.20.24.3.133", "extension", "2017-08-01");

				element(doc, act, "id", "root", "ec8a6ff8-ed4b-4f7e-82c3-e98e58b45de7");

				element(doc, act, "code", "code", "ENC", "codeSystem", "2.16.840.1.113883.5.6", "codeSystemName", "Encounter", "displayName", "ActClass");
				
				Element er = element(doc, act, "entryRelationship", "typeCode", "SUBJ");
				
				Element enc = element(doc, er, "encounter", "classCode", "ENC", "moodCode", "EVN");

				comment(doc, enc, "Encounter activities template");
				element(doc, enc, "templateId", "root", "2.16.840.1.113883.10.20.22.4.49", "extension", "2015-08-01");
				
				comment(doc, enc, "Encounter performed template");
				element(doc, enc, "templateId", "root", "2.16.840.1.113883.10.20.24.3.23", "extension", "2017-08-01");

				
				Element encounterCode = doc.createElement("code");
				((Element)encounterCode).setAttribute("code", "8715000");
				((Element)encounterCode).setAttribute("codeSystem", "2.16.840.1.113883.6.96");
				((Element)encounterCode).setAttribute("codeSystemName", "SNOMED-CT");
				((Element)encounterCode).setAttribute("displayName", "hospital admission, from remote area, by means of special transportation");
				//((Element)encounterCode).setAttribute("sdtc:valueSet", "2.16.840.1.113883.3.666.5.307");
				enc.appendChild(encounterCode);
				
				Element encounterText = doc.createElement("text");
				encounterText.appendChild(doc.createTextNode("Encounter, Performed: Encounter Inpatient"));
				enc.appendChild(encounterText);
				
				Element encounterStatusCode = doc.createElement("statusCode");
				((Element)encounterStatusCode).setAttribute("code", "completed");
				enc.appendChild(encounterStatusCode);
				
				Comment lengthOfStay = doc.createComment("Length Of Stay");
				enc.appendChild(lengthOfStay);
				
				Element effectiveTime = doc.createElement("effectiveTime");
				enc.appendChild(effectiveTime);
				
				Comment admissionTm = doc.createComment("Attribute: admission datetime");
				effectiveTime.appendChild(admissionTm);
				
				Element low = doc.createElement("low");
				((Element)low).setAttribute("value", admission);
				effectiveTime.appendChild(low);
				
				Comment dischargeTm = doc.createComment("Attribute: discharge datetime");
				effectiveTime.appendChild(dischargeTm);
				
				Element high = doc.createElement("high");
				((Element)high).setAttribute("value", discharge);
				effectiveTime.appendChild(high);
				
				//stratification(enc, FileCreator.stratificationType);
			}
		
			private static void edEncounter(Element ele) throws DOMException, ParseException
			{
				comment(doc, ele, "Encounter, Performed: Emergency Department Visit");

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

				comment(doc, enc, "Encounter Performed template");
				element(doc, enc, "templateId", "root", "2.16.840.1.113883.10.20.24.3.23", "extension", "2017-08-01");

				element(doc, enc, "id", "root", "12345678-9d11-439e-92b3-5d9815ff4de1");

				element(doc, enc, "code", "code", "4525004", "codeSystem", "2.16.840.1.113883.6.96", "codeSystemName", "SNOMED-CT", "displayName", "Emergency Department visit (procedure)");//"sdtc:valueSet", "2.16.840.1.113883.3.117.1.7.1.292");

				element(doc, enc, "text", "Encounter, Performed: Emergency Department Visit");

				element(doc, enc, "statusCode", "code", "completed");

				comment(doc, enc, "Length of Stay");

				Element effTm = element(doc, enc, "effectiveTime");

				comment(doc, effTm, "Attribute: admission datetime");
				element(doc, effTm, "low", "value", MeasureSets.convertFirstDate(admission, 5500));

				comment(doc, effTm, "Attribute: discharge datetime");
				element(doc, effTm, "high", "value", MeasureSets.convertFirstDate(admission, 5000));
				
//				if(FileCreator.denominatorExclusion == true) {
//					denominatorExclusion(enc);
//				}
				
				Element par = element(doc, enc, "participant", "typeCode", "LCO");

				comment(doc, par, "Facility Location Template");

				element(doc, par, "templateId", "root", "2.16.840.1.113883.10.20.24.3.100", "extension", "2017-08-01");

				Element time = element(doc, par, "time");

				comment(doc, time, "Attribute: facility location arrival datetime");
				/**
				 * change in Facility Location Departure is Measure Observation
				 */
				int difference = 1000;
				if(FileCreator.numerator == true) {
					Random rd = new Random();
					difference = rd.nextInt(11100) + 1000;				
				}
				element(doc, time, "low", "value", MeasureSets.convertFirstDate(admission, difference));
				
				comment(doc, time, "Attribute: facility location departure datetime");
				element(doc, time, "high", "value", MeasureSets.convertFirstDate(admission, 1000));

				Element parRole = element(doc, par, "participantRole", "classCode", "SDLOC");

				element(doc, parRole, "code", "code", "4525004", "codeSystem", "2.16.840.1.113883.5.111", "codeSystemName", "SNOMEDCT", "displayName", "Emergency department patient visit (procedure)");

//				Element addr = doc.createElement("addr");
//				participantRole.appendChild(addr);
//				
//				Element streetAddressLine = doc.createElement("streetAddressLine");
//				streetAddressLine.appendChild(doc.createTextNode("1776 Memorial Ave"));
//				addr.appendChild(streetAddressLine);
//				
//				Element city = doc.createElement("city");
//				city.appendChild(doc.createTextNode("Mt Vernon"));
//				addr.appendChild(city);
//				
//				Element state = doc.createElement("state");
//				state.appendChild(doc.createTextNode("VA"));
//				addr.appendChild(state);
//				
//				Element postCd = doc.createElement("postalCode");
//				postCd.appendChild(doc.createTextNode("22309"));
//				addr.appendChild(postCd);
//				
//				Element country = doc.createElement("country");
//				country.appendChild(doc.createTextNode("US"));
//				addr.appendChild(country);
//				
//				Element telecom = doc.createElement("telecom");
//				((Element)telecom).setAttribute("nullFlavor", "UNK");
//				participantRole.appendChild(telecom);
//				
//				Element playingEntity = doc.createElement("playingEntity");
//				((Element)playingEntity).setAttribute("classCode", "PLC");
//				participantRole.appendChild(playingEntity);
//				
//				Element name = doc.createElement("name");
//				name.appendChild(doc.createTextNode("George Washington Memorial Hospital"));
//				playingEntity.appendChild(name);
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
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - Ed-2b file", e);
			return e.toString();
		}
	}
}
