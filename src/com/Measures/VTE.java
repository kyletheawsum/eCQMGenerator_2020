package com.Measures;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.logging.Level;

import org.w3c.dom.DOMException;

import com.DocumentBuilder.VTE1MeasureSection;
import com.DocumentBuilder.VTE2MeasureSection;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureSets;

public class VTE {
	
	private static int numOfFiles = Integer.parseInt(FileCreator.numOfFilesTxtbox.getText());
	public static String excelPath = MeasureSets.excelPath.replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\");
	private static String den = "_DENOM_";
	private static String denex = "_DENEX_";
	private static String num = "_NUM_";
	public static String condition;
	
	public static void editVTE1() {
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
			FileCreator.errorMsgTxtbox.setText("Creating VTE-1");
			do {
				for(int i = 1; i < numOfFiles+1; i++) {
					if(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/VTE-1/") >= numOfFiles)
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
					}

					condition = "IPP_1";
					if(FileCreator.denominator == true) 			condition += den+"1";
					else											condition += den+"0";						
					if(FileCreator.denominatorExclusion == true) 	condition += denex+"1";
					else											condition += denex+"0";
					if(FileCreator.numerator == true) 				condition += num+"1";
					else											condition += num+"0";
												
					if(FileCreator.allMeasuresChckbx.isSelected()) {
						VTE1MeasureSection vte1 = new VTE1MeasureSection(MeasureSets.providerList[x]);
					}
					else {
						VTE1MeasureSection vte1 = new VTE1MeasureSection(MeasureSets.providerList[i-1]);
					}
					
					excelPath = excelPath.replaceAll("Measure Rules.*", "");
					File file = new File(excelPath + "Files/VTE-1/" + condition + ".xml");
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					out.println(VTE1MeasureSection.returnDoc());
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
			} while(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/VTE-1/") < numOfFiles);
			
		} catch (IOException | DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur VTE-1 file creation", e);
		}
		FileCreator.errorMsgTxtbox.setText("Files Completed");
	}
	
	public static void editVTE2() {
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
			FileCreator.errorMsgTxtbox.setText("Creating VTE-2");
			do {
				for(int i = 1; i < numOfFiles+1; i++) {
					if(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/VTE-2/") >= numOfFiles)
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
					}

					condition = "IPP_1";
					if(FileCreator.denominator == true) 			condition += den+"1";
					else											condition += den+"0";						
					if(FileCreator.denominatorExclusion == true) 	condition += denex+"1";
					else											condition += denex+"0";
					if(FileCreator.numerator == true) 				condition += num+"1";
					else											condition += num+"0";
												
					if(FileCreator.allMeasuresChckbx.isSelected()) {
						VTE2MeasureSection vte2 = new VTE2MeasureSection(MeasureSets.providerList[x]);
					}
					else {
						VTE2MeasureSection vte2 = new VTE2MeasureSection(MeasureSets.providerList[i-1]);
					}
					
					excelPath = excelPath.replaceAll("Measure Rules.*", "");
					File file = new File(excelPath + "Files/VTE-2/" + condition + ".xml");
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					out.println(VTE2MeasureSection.returnDoc());
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
			} while(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/VTE-2/") < numOfFiles);
			
		} catch (IOException | DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur VTE-2 file creation", e);
		}
		FileCreator.errorMsgTxtbox.setText("Files Completed");
	}
}
