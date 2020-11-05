package com.Measures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.logging.Level;

import org.w3c.dom.DOMException;

import com.DocumentBuilder.ED2MeasureSection;
import com.xmlEditTool.FileCreator;
import com.xmlEditTool.MeasureSets;

public class ED {
	
	private static int numOfFiles = Integer.parseInt(FileCreator.numOfFilesTxtbox.getText());
	public static String excelPath = MeasureSets.excelPath.replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\");
	private static String den = "_MSRPOP_";
	private static String denex = "_MSRPOPEX_";
	private static String num = "_MSROBS_";
	public static String denec = FileCreator.stratification;
	public static String condition;
	
	public static void editED2() throws FileNotFoundException, IOException, DOMException, ParseException {
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
			FileCreator.errorMsgTxtbox.setText("Creating ED-2");
			do {
				for(int i = 1; i < numOfFiles+1; i++) {
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
						if(stratiType1() == 0) {
							FileCreator.denominatorException = true;
							FileCreator.stratificationType = 1;
							denec = "_STRAT_1";
						}
						if(stratiType1() == 1) {
							FileCreator.denominatorException = true;
							FileCreator.stratificationType = 2;
							denec = "_STRAT_2";
						}
					}
					
					condition = "IPP_1";
					if(FileCreator.denominator == true) 			condition += den+"1";
					else											condition += den+"0";
					if(FileCreator.denominatorExclusion == true) 	condition += denex+"1";
					else											condition += denex+"0";
					if(FileCreator.numerator == true) 				condition += num+"1";
					else											condition += num+"0";
					if(FileCreator.denominatorException == true) 	condition += denec+"1";
					else											condition += denec+"0";
					
					if(FileCreator.allMeasuresChckbx.isSelected()) {
						ED2MeasureSection ed2 = new ED2MeasureSection(MeasureSets.providerList[x]);
					}
					else {
						ED2MeasureSection ed2 = new ED2MeasureSection(MeasureSets.providerList[i-1]);
					}

					excelPath = excelPath.replaceAll("Measure Rules.*", "");
					File file = new File(excelPath + "Files/ED-2/" + condition + ".xml");
					PrintStream out = new PrintStream(new FileOutputStream(file));
					
					out.println(ED2MeasureSection.returnDoc());
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
			} while(MeasureSets.getSizeOfCurrentDirectory(excelPath + "Files/ED-2/") < numOfFiles);
			
		} catch(IOException | DOMException | ParseException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - ED2 file creation", e);
		}
		FileCreator.errorMsgTxtbox.setText("Files Completed");
	}
	
	private static int stratiType1() {
		return (int) Math.floor(Math.random() * Math.floor(2));
	}
}
