package com.Measures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.logging.Level;

import org.w3c.dom.DOMException;

import com.DocumentBuilder.STK2MeasureSection;
import com.DocumentBuilder.STK3MeasureSection;
import com.DocumentBuilder.STK5MeasureSection;
import com.DocumentBuilder.STK6MeasureSection;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureSets;

public class STK {
	private static int numOfFiles = Integer.parseInt(FileCreator.numOfFilesTxtbox.getText());
	public static String excelPath = MeasureSets.excelPath.replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\");
	private static String den = "_DENOM_";
	private static String denex = "_DENEX_";
	private static String num = "_NUM_";
	private static String denec = "_DENEC_";
	public static String condition;
	
	public static void editSTK2() {
		if(FileCreator.allMeasuresChckbx.isSelected()) {
			for(int x = 0; x < MeasureSets.providerList.length; x++) {
				createFiles1(x);
			}
		}
		else {
			createFiles1(0);
		}
	}
	
	private static void createFiles1(int x) {
		try {
			FileCreator.errorMsgTxtbox.setText("Creating STK-2");
			do {
				for(int i = 1; i < numOfFiles+1; i++) {
					if(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-2/") >= numOfFiles)
						break;
					if(FileCreator.chckbxRandomize.isSelected()) {		 
						FileCreator.denominator = true;
						int exclusion = MeasureSets.dEnEx();
						if(exclusion == 1) {
							FileCreator.denominatorExclusion = true;
						}
						else if(exclusion == 0) {
							FileCreator.denominatorExclusion = false;
							if(MeasureSets.nUm() == 1) {
								FileCreator.numerator = true;
							}
						}
						if(MeasureSets.dEnEc() == 1) {
							FileCreator.denominatorException = true;
						}
					}
					if(FileCreator.denominatorException == true)
						FileCreator.numerator = false;
					if(FileCreator.denominatorExclusion == true)
						FileCreator.denominatorException = false;
					
					condition = "IPP_1";
					if(FileCreator.denominator == true) 			condition += den+"1";
					else											condition += den+"0";						
					if(FileCreator.denominatorExclusion == true) 	condition += denex+"1";
					else											condition += denex+"0";
					if(FileCreator.numerator == true) 				condition += num+"1";
					else											condition += num+"0";
					if(FileCreator.denominatorException == true) 	condition += denec+"1";
					else											condition += denec+"0";
								
//					condition += "_STK-2_" + STK2MeasureSection.discharge;			
//					XmlDocumentBuilder.setPatientID(condition);
					
					if(FileCreator.allMeasuresChckbx.isSelected()) {
						STK2MeasureSection stk2 = new STK2MeasureSection(MeasureSets.providerList[x]);
					}
					else {
						STK2MeasureSection stk2 = new STK2MeasureSection(MeasureSets.providerList[i-1]);
					}
					
					excelPath = excelPath.replaceAll("Measure Rules.*", "");
					File file = new File(excelPath + "Files/STK-2/" + condition + ".xml");
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					out.println(STK2MeasureSection.returnDoc());
					out.close();
					
					if(FileCreator.chckbxRandomize.isSelected())
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[x], condition, file.length()/1000+" kb"});
					else
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[i-1], condition, file.length()/1000+" kb"});
					
					if(FileCreator.chckbxRandomize.isSelected()) {
						FileCreator.denominator = false;
						FileCreator.denominatorExclusion = false;
						FileCreator.numerator = false;
						FileCreator.denominatorException = false;
					}
				}
			} while(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-2/") < numOfFiles);
			
		} catch (IOException | DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur STK-2 file creation", e);
		}
		FileCreator.errorMsgTxtbox.setText("Files Completed");
	}
	
	public static void editSTK3() {
		if(FileCreator.allMeasuresChckbx.isSelected()) {
			for(int x = 0; x < MeasureSets.providerList.length; x++) {
				createFiles2(x);
			}
		}
		else {
			createFiles2(0);
		}
	}
	
	private static void createFiles2(int x) {
		try {
			FileCreator.errorMsgTxtbox.setText("Creating STK-3");
			do {
				for(int i = 1; i < numOfFiles+1; i++) {
					if(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-3/") >= numOfFiles)
						break;
					if(FileCreator.chckbxRandomize.isSelected()) {		
						FileCreator.denominator = true;
						int exclusion = MeasureSets.dEnEx();
						if(exclusion == 1) {
							FileCreator.denominatorExclusion = true;
						}
						else if(exclusion == 0) {
							FileCreator.denominatorExclusion = false;
							if(MeasureSets.nUm() == 1) {
								FileCreator.numerator = true;
							}
						}
						if(MeasureSets.dEnEc() == 1) {
							FileCreator.denominatorException = true;
						}
					}
					if(FileCreator.denominatorException == true)
						FileCreator.numerator = false;
					if(FileCreator.denominatorExclusion == true)
						FileCreator.denominatorException = false;

					condition = "IPP_1";
					if(FileCreator.denominator == true) 			condition += den+"1";
					else											condition += den+"0";						
					if(FileCreator.denominatorExclusion == true) 	condition += denex+"1";
					else											condition += denex+"0";
					if(FileCreator.numerator == true) 				condition += num+"1";
					else											condition += num+"0";
					if(FileCreator.denominatorException == true) 	condition += denec+"1";
					else											condition += denec+"0";
					
//					condition += "_STK-3_" + STK3MeasureSection.discharge;			
//					XmlDocumentBuilder.setPatientID(condition);
					
					if(FileCreator.allMeasuresChckbx.isSelected()) {
						STK3MeasureSection stk3 = new STK3MeasureSection(MeasureSets.providerList[x]);
					}
					else {
						STK3MeasureSection stk3 = new STK3MeasureSection(MeasureSets.providerList[i-1]);
					}

					excelPath = excelPath.replaceAll("Measure Rules.*", "");
					File file = new File(excelPath + "Files/STK-3/" + condition + ".xml");
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					out.println(STK3MeasureSection.returnDoc());
					out.close();
					
					if(FileCreator.chckbxRandomize.isSelected())
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[x], condition, file.length()/1000+" kb"});
					else
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[i-1], condition, file.length()/1000+" kb"});
					
					if(FileCreator.chckbxRandomize.isSelected()) {
						FileCreator.denominator = false;
						FileCreator.denominatorExclusion = false;
						FileCreator.numerator = false;
						FileCreator.denominatorException = false;
					}
				}
			} while(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-3/") < numOfFiles);
			
		} catch (IOException | DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur STK-3 file creation", e);
		}
		FileCreator.errorMsgTxtbox.setText("Files Completed");
	}

	public static void editSTK5() {
		if(FileCreator.allMeasuresChckbx.isSelected()) {
			for(int x = 0; x < MeasureSets.providerList.length; x++) {
				createFiles3(x);
			}
		}
		else {
			createFiles3(0);
		}
	}
	
	private static void createFiles3(int x) {
		try {
			FileCreator.errorMsgTxtbox.setText("Creating STK-5");
			do{
				for(int i = 1; i < numOfFiles+1; i++) {
					if(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-5/") >= numOfFiles)
						break;
					if(FileCreator.chckbxRandomize.isSelected()) {		 
						FileCreator.denominator = true;
						int exclusion = MeasureSets.dEnEx();
						if(exclusion == 1) {
							FileCreator.denominatorExclusion = true;
						}
						else if(exclusion == 0) {
							FileCreator.denominatorExclusion = false;
							if(MeasureSets.nUm() == 1) {
								FileCreator.numerator = true;
							}
						}
						if(MeasureSets.dEnEc() == 1) {
							FileCreator.denominatorException = true;
						}
					}
					if(FileCreator.denominatorException == true)
						FileCreator.numerator = false;
					if(FileCreator.denominatorExclusion == true)
						FileCreator.denominatorException = false;

					condition = "IPP_1";
					if(FileCreator.denominator == true) 			condition += den+"1";
					else											condition += den+"0";						
					if(FileCreator.denominatorExclusion == true) 	condition += denex+"1";
					else											condition += denex+"0";
					if(FileCreator.numerator == true) 				condition += num+"1";
					else											condition += num+"0";
					if(FileCreator.denominatorException == true) 	condition += denec+"1";
					else											condition += denec+"0";
									
//					condition += "_STK-5_" + STK5MeasureSection.discharge;			
//					XmlDocumentBuilder.setPatientID(condition);
					
					if(FileCreator.allMeasuresChckbx.isSelected()) {
						STK5MeasureSection stk5 = new STK5MeasureSection(MeasureSets.providerList[x]);
					}
					else {
						STK5MeasureSection stk5 = new STK5MeasureSection(MeasureSets.providerList[i-1]);
					}
					
					excelPath = excelPath.replaceAll("Measure Rules.*", "");
					File file = new File(excelPath + "Files/STK-5/" + condition + ".xml");
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					out.println(STK5MeasureSection.returnDoc());
					out.close();
					
					if(FileCreator.chckbxRandomize.isSelected())
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[x], condition, file.length()/1000+" kb"});
					else
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[i-1], condition, file.length()/1000+" kb"});
					
					if(FileCreator.chckbxRandomize.isSelected()) {
						FileCreator.denominator = false;
						FileCreator.denominatorExclusion = false;
						FileCreator.numerator = false;
						FileCreator.denominatorException = false;
					}
				}
			} while(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-5/") < numOfFiles);

		} catch (IOException | DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur STK-5 file creation", e);
		}
		FileCreator.errorMsgTxtbox.setText("Files Completed");
	}

	public static void editSTK6() {
		if(FileCreator.allMeasuresChckbx.isSelected()) {
			for(int x = 0; x < MeasureSets.providerList.length; x++) {
				createFiles4(x);
			}
		}
		else {
			createFiles4(0);
		}
	}
	
	private static void createFiles4(int x) {
		try {
			FileCreator.errorMsgTxtbox.setText("Creating STK-6");
			do {
				for(int i = 1; i < numOfFiles+1; i++) {
					if(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-6/") >= numOfFiles)
						break;
					if(FileCreator.chckbxRandomize.isSelected()) {		 
						FileCreator.denominator = true;
						int exclusion = MeasureSets.dEnEx();
						if(exclusion == 1) {
							FileCreator.denominatorExclusion = true;
						}
						else if(exclusion == 0) {
							FileCreator.denominatorExclusion = false;
							if(MeasureSets.nUm() == 1) {
								FileCreator.numerator = true;
							}
						}
						if(MeasureSets.dEnEc() == 1) {
							FileCreator.denominatorException = true;
						}
					}
					if(FileCreator.denominatorException == true)
						FileCreator.numerator = false;
					if(FileCreator.denominatorExclusion == true)
						FileCreator.denominatorException = false;

					condition = "IPP_1";
					if(FileCreator.denominator == true) 			condition += den+"1";
					else											condition += den+"0";						
					if(FileCreator.denominatorExclusion == true) 	condition += denex+"1";
					else											condition += denex+"0";
					if(FileCreator.numerator == true) 				condition += num+"1";
					else											condition += num+"0";
					if(FileCreator.denominatorException == true) 	condition += denec+"1";
					else											condition += denec+"0";
								
//					condition += "_STK-6_" + STK6MeasureSection.discharge;			
//					XmlDocumentBuilder.setPatientID(condition);
					
					if(FileCreator.allMeasuresChckbx.isSelected()) {
						STK6MeasureSection stk6 = new STK6MeasureSection(MeasureSets.providerList[x]);
					}
					else {
						STK6MeasureSection stk6 = new STK6MeasureSection(MeasureSets.providerList[i-1]);
					}
					
					excelPath = excelPath.replaceAll("Measure Rules.*", "");
					File file = new File(excelPath + "Files/STK-6/" + condition + ".xml");
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					out.println(STK6MeasureSection.returnDoc());
					out.close();
					
					if(FileCreator.chckbxRandomize.isSelected())
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[x], condition, file.length()/1000+" kb"});
					else
						FileCreator.model.addRow(new Object[] {MeasureSets.providerList[i-1], condition, file.length()/1000+" kb"});
					
					if(FileCreator.chckbxRandomize.isSelected()) {
						FileCreator.denominator = false;
						FileCreator.denominatorExclusion = false;
						FileCreator.numerator = false;
						FileCreator.denominatorException = false;
					}
				}
			} while(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/STK-6/") < numOfFiles);

		} catch (IOException | DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur STK-6 file creation", e);
		}
		FileCreator.errorMsgTxtbox.setText("Files Completed");
	}

}
