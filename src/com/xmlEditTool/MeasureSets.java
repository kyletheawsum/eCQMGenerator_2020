package com.xmlEditTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.Measures.*;

public class MeasureSets {
	public static int quarter, numOfFiles;
	public static String date, excelPath, startDate, endDate;
	public static String[] providerList;
	public static String[] rptPrd = new String[2];
	private static List<String> providers = new ArrayList<String>();
	private static String measure;
	
//	static RunED2 ed2; 
//	static RunPC05 pc05; 
//	static RunSTK2 stk2;
//	static RunSTK3 stk3; 
//	static RunSTK5 stk5; 
//	static RunSTK6 stk6; 
//	static RunVTE1 vte1; 
//	static RunVTE2 vte2;

	public MeasureSets() {
		FileCreator.LOGGER.log(Level.INFO, "Instantiate MeasureSets");
		date = FileCreator.yearTxtbox.getText();
		quarter = FileCreator.quarterDropdownBx.getSelectedIndex();
		reportingPeriod(FileCreator.quarterDropdownBx.getSelectedIndex());
		numOfFiles = Integer.parseInt(FileCreator.numOfFilesTxtbox.getText());
		excelPath = FileCreator.excelFilePathTxtbox.getText();
		FileCreator.LOGGER.log(Level.INFO, date);
		FileCreator.LOGGER.log(Level.INFO, Integer.toString(numOfFiles));
		FileCreator.LOGGER.log(Level.INFO, Integer.toString(quarter));
	}
	
	static void assignVariables() {
		FileCreator.LOGGER.log(Level.INFO, "Instantiate MeasureSets");
		date = FileCreator.yearTxtbox.getText();
		quarter = FileCreator.quarterDropdownBx.getSelectedIndex();
		reportingPeriod(FileCreator.quarterDropdownBx.getSelectedIndex());
		numOfFiles = Integer.parseInt(FileCreator.numOfFilesTxtbox.getText());
		excelPath = FileCreator.excelFilePathTxtbox.getText();
		FileCreator.LOGGER.log(Level.INFO, date);
		FileCreator.LOGGER.log(Level.INFO, Integer.toString(numOfFiles));
		FileCreator.LOGGER.log(Level.INFO, Integer.toString(quarter));
	}
		
	public static void editXmls(int x) {
		try {
			reportingPeriod(FileCreator.quarterDropdownBx.getSelectedIndex());
			switch(x) {
			case 1: ED.editED2();measure = "ED-2";break;
			case 2: PC.editPC05();measure = "PC-05";break;
			case 3:	STK.editSTK2();measure = "STK-2";break;
			case 4: STK.editSTK3();measure = "STK-3";break;
			case 5: STK.editSTK5();measure = "STK-5";break;
			case 6: STK.editSTK6();measure = "STK-6";break;
			case 7: VTE.editVTE1();measure = "VTE-1";break;
			case 8: VTE.editVTE2();measure = "VTE-2";break;
			} 
		} catch (Exception e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - Measure Set", e);
		}
	}
	
	/*
//	public static void joinThreads() {
//		if(ami.isAlive()) {
//			try { ami.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(cac.isAlive()) {
//			try { cac.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(ed1.isAlive()) {
//			try { ed1.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(ed2.isAlive()) {
//			try { ed2.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(ed3.isAlive()) {
//			try { ed3.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(ehdi.isAlive()) {
//			try { ehdi.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(pc01.isAlive()) {
//			try { pc01.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(pc05.isAlive()) {
//			try { pc05.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(stk2.isAlive()) {
//			try { stk2.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(stk3.isAlive()) {
//			try { stk3.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(stk5.isAlive()) {
//			try { stk5.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(stk6.isAlive()) {
//			try { stk6.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(stk8.isAlive()) {
//			try { stk8.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(stk10.isAlive()) {
//			try { stk10.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(vte1.isAlive()) {
//			try { vte1.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//		if(vte2.isAlive()) {
//			try { vte2.wait();
//			} catch (InterruptedException e) {	FileCreator.LOGGER.log(Level.SEVERE, "Exception occur on Thread", e);	}
//		}
//	}
	*/
	
	public static String[] reportingPeriod(int quarter) {
		switch(quarter) {
		case 1:
			rptPrd[0] = date+"0101";
			rptPrd[1] = date+"0331";
			break;
		case 2:
			rptPrd[0] = date+"0401";
			rptPrd[1] = date+"0630";
			break;
		case 3:
			rptPrd[0] = date+"0701";
			rptPrd[1] = date+"0930";
			break;
		case 4:
			rptPrd[0] = date+"1001";
			rptPrd[1] = date+"1231";
			break;
		}
		return rptPrd;
	}
	
	public static String startDate() {
		Random rn = new Random();
		int hour = rn.nextInt(15 - 6 + 1) + 6;
		int minute = rn.nextInt(30);
		String[] month = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		switch(FileCreator.quarterDropdownBx.getSelectedIndex()) {
		case 1:
			startDate = date + month[rn.nextInt(3)];
			if(startDate.contains("02")) 
				startDate = startDate + String.format("%02d", (rn.nextInt(27-1) + 1));
			 else 
				startDate = startDate + String.format("%02d", (rn.nextInt(30-1) + 1));
			break;
		case 2:
			startDate = date + month[(rn.nextInt(6-3)+3)];
			if(startDate.contains("05")) 
				startDate = startDate + String.format("%02d", (rn.nextInt(30-1) + 1));
			 else 
				startDate = startDate + String.format("%02d", (rn.nextInt(29-1) + 1));
			break;
		case 3:
			startDate = date + month[(rn.nextInt(9-6)+6)];
			if(startDate.contains("09")) 
				startDate = startDate + String.format("%02d", (rn.nextInt(29-1) + 1));
			 else 
				startDate = startDate + String.format("%02d", (rn.nextInt(30-1) + 1));
			break;
		case 4:
			startDate = date + month[(rn.nextInt(12-9)+9)];
			if(startDate.contains("11")) 
				startDate = startDate + String.format("%02d", (rn.nextInt(29-1) + 1));
			 else 
				startDate = startDate + String.format("%02d", (rn.nextInt(30-1) + 1));
			break;
		}
		
		startDate = startDate + String.format("%02d", hour)+String.format("%02d", minute)+"00";
		return startDate;
	}
	
	public static String endDate() {
		String endDate = null;
		Random rn = new Random();
		int hour = rn.nextInt(9) + 14;
		int minute = rn.nextInt(60-30) + 30;
		int ellapsed = (rn.nextInt(6-1)+1);
		int sec = rn.nextInt(60);
		int temp = ellapsed + Integer.parseInt(startDate.substring(6, 8));
		
		String month = startDate.substring(4,6);
		switch(month) {
		case "01":
			if(temp > 31) {
				do {temp--;	} 
				while (temp > 31);
			}
			break;
		case "02":
			if(temp > 28) {
				do {temp--;	} 
				while (temp > 28);
			}
			break;
		case "03":
			if(temp > 31) {
				do {temp--;	} 
				while (temp > 31);
			}
			break;
		case "04":
			if(temp > 30) {
				do {temp--;	} 
				while (temp > 30);
			}
			break;
		case "05":
			if(temp > 31) {
				do {temp--;	} 
				while (temp > 31);
			}
			break;
		case "06":
			if(temp > 30) {
				do {temp--;	} 
				while (temp > 30);
			}
			break;
		case "07":
			if(temp > 31) {
				do {temp--;	} 
				while (temp > 31);
			}
			break;
		case "08":
			if(temp > 31) {
				do {temp--;	} 
				while (temp > 31);
			}
			break;
		case "09":
			if(temp > 30) {
				do {temp--;	} 
				while (temp > 30);
			}
			break;
		case "10":
			if(temp > 31) {
				do {temp--;	} 
				while (temp > 31);
			}
			break;
		case "11":
			if(temp > 30) {
				do {temp--;	} 
				while (temp > 30);
			}
			break;
		case "12":
			if(temp > 31) {
				do {temp--;	} 
				while (temp > 31);
			}
			break;
		}
		
		endDate = date + month + String.format("%02d", temp) + String.format("%02d", hour)+String.format("%02d", minute)+String.format("%02d", sec);
		return endDate;
	}

	public static String convertFirstDate(String initial, int offset) throws ParseException {
		String temp = Long.toString(Long.parseLong(initial) - offset);
		if(Long.parseLong(temp.substring(8, 10)) > 23) {
			temp = temp.replace(temp.substring(8, 12), "00");
		}
		if(Long.parseLong(temp.substring(10,12)) > 58) {
			temp = temp.replace(temp.substring(10, 12), "58");
		}
		if(Long.parseLong(temp.substring(12, 14)) > 58) {
			temp = temp.replace(temp.substring(12, 14), "00");
		}
		return temp;
	}

	public static String convertSecondDate(String initial, int offset) throws ParseException {
		String temp = Long.toString(Long.parseLong(initial) + offset);
		if(Long.parseLong(temp.substring(8, 10)) > 23 || Long.parseLong(temp.substring(10, 12)) > 58) {
			temp = temp.replace(temp.substring(8, 12), "2358");
		}
		if(Long.parseLong(temp.substring(12, 14)) > 58) {
			temp = temp.replace(temp.substring(12, 14), "00");
		}
		return temp;
	}
		
	public static void getIDs() {
		providerList = new String[0];
		providers.clear();
		if(FileCreator.ccnTextbox.getText().equals("")) {
			try {
				InputStream ExcelFile1 = new FileInputStream(FileCreator.excelFilePathTxtbox.getText());
				Workbook wb = new XSSFWorkbook(ExcelFile1);
				Sheet sheet = wb.getSheetAt(0);		
				//Row row = sheet.getRow(0);
				
				for(int j = 1; j < sheet.getLastRowNum()+1;j++) {
					Row row2 = sheet.getRow(j);
					Cell cell2 = row2.getCell(0);
					if(cell2!=null) {
						providers.add(cell2.toString().replace(".0", ""));
					}
				}
				
				wb.close();
				for(int i = 0; i < providers.size(); i++) {
					if(providers.get(i).length() < 6) {
						providers.set(i, "0" + providers.get(i));
					}
				}
			} catch (IOException e) {
				FileCreator.errorMsgTxtbox.setText(e.toString());
				FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - Provider IDs", e);
			}
		}
		else {
			String[] temp = FileCreator.ccnTextbox.getText().trim().toString().split(",");
			for(String t : temp) {
				providers.add(t);
			}
		}
		
		if(FileCreator.allMeasuresChckbx.isSelected()) {

		}
		else {
			if(providers.size() > numOfFiles) {
				int x = providers.size() - numOfFiles;
				for(int i = 0; i < x; i++) {
					providers.remove(x-i);
				}
			}
			else if(providers.size() < numOfFiles) {
				int p = providers.size();
				int x = numOfFiles-providers.size();
				for(int i = 0; i < x; i++) {
					providers.add(p+i, providers.get(i).toString());
				}
			}
			else {
				
			}
		}
			
		providerList = new String[providers.size()];
		providerList = providers.toArray(providerList);
	}
	
	public static void zipFiles(int m) {
		try {
			String excelPath = FileCreator.excelFilePathTxtbox.getText().replace("/", "\\").replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\");
			String filePath = excelPath + "Files/"+FileCreator.measureSetDropdownBx.getItemAt(m).toString();
			File dir = new File(filePath);
			//File[] dirListing = dir.listFiles();
			//File measureDirectory = new File(dirListing[m].toString());
			File[] listFiles = dir.listFiles();
			String[] filesToZip = new String[listFiles.length];
			for(int j = 0; j < listFiles.length; j++) {
				filesToZip[j] = listFiles[j].getName();
			}
			
			byte[] bytes = new byte[2000];
			File zipDir = new File(excelPath + "/Zipped Files/");
			if(!zipDir.exists()) {
				new File(excelPath + "/Zipped Files/").mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(excelPath + "/Zipped Files/"+ measure + "_" + getCurrentDate().replaceAll(" ", "_") +".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			for(int i = 0; i < listFiles.length; i++) {
				FileInputStream fis = new FileInputStream(dir +"/"+ filesToZip[i]);
				zos.putNextEntry(new ZipEntry(filesToZip[i]));
				
				int length;
				while((length = fis.read(bytes)) > 0) {
					zos.write(bytes, 0, length);
				}
				
				zos.closeEntry();
				fis.close();
			}
			zos.close();
		} catch(IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - Zipping Files", e);
		}
	}
	
	public static void zipAllFiles() {
		try {
			String excelPath = FileCreator.excelFilePathTxtbox.getText().replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\");
			File zippedFile = new File(excelPath + "/Zipped Files/");
			if(!zippedFile.exists())
				zippedFile.mkdir();
			
			FileOutputStream fos = new FileOutputStream(excelPath + "/Zipped Files/All_Zipped_" + getCurrentDate().replaceAll(" ", "_") +".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for(int m = 1; m < 17; m++) {
				String filePath = excelPath + "/Files/"+FileCreator.measureSetDropdownBx.getItemAt(m).toString();
				File dir = new File(filePath);
				File[] listFiles = dir.listFiles();
				String[] filesToZip = new String[listFiles.length];
				for(int j = 0; j < listFiles.length; j++) {
					filesToZip[j] = listFiles[j].getName();
				}
				byte[] bytes = new byte[2000];
				for(int i = 0; i < listFiles.length; i++) {
					FileInputStream fis = new FileInputStream(dir +"/"+ filesToZip[i]);
					zos.putNextEntry(new ZipEntry(filesToZip[i]));
					
					int length;
					while((length = fis.read(bytes)) > 0) {
						zos.write(bytes, 0, length);
					}
					
					zos.closeEntry();
					fis.close();
				}
			}
			zos.close();
		} catch (IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - Zipping Files", e);
		}
	}
	
	public static String getCurrentDate() {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
		Date date = new Date();
		String timeStamp = sdf.format(date);
		return timeStamp;
	}
	
	public static int dEnEx() {
		return (int) Math.floor(Math.random() * Math.floor(2));
	}
	
	public static int nUm() {
		return (int) Math.floor(Math.random() * Math.floor(2));
	}
	
	public static int dEnEc() {
		return (int) Math.floor(Math.random() * Math.floor(2));
	}
	
	static void createFolders() {
		for(int i = 1; i < FileCreator.measureSet.length; i++) {
			String filePath = FileCreator.excelFilePathTxtbox.getText().replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\") + "/Files/" + FileCreator.measureSet[i];
			File dir = new File(filePath);
			if(!dir.exists())
				new File(filePath).mkdirs();
		}
	}
	
	static void deleteFiles(int m) {
		String excelPath = FileCreator.excelFilePathTxtbox.getText().replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\");
		String filePath = excelPath + "/Files/"+FileCreator.measureSetDropdownBx.getItemAt(m).toString();
		File dir = new File(filePath);
		File[] listFiles = dir.listFiles();
		
		for(int i = 0; i < listFiles.length; i++) {
			listFiles[i].delete();
		}
		FileCreator.errorMsgTxtbox.setText("Files Deleted");
		FileCreator.LOGGER.info("Files Deleted");
	}
	
	static void deleteZip() {
		String zipPath = FileCreator.excelFilePathTxtbox.getText().replaceAll("Measure Rules.*.xlsx", "Zipped Files");
		File dir = new File(zipPath);
		File[] listFiles = dir.listFiles();
		
		for(int i = 0; i < listFiles.length; i++) {
			listFiles[i].delete();
		}
		FileCreator.errorMsgTxtbox.setText("Files Deleted");
		FileCreator.LOGGER.info("Zips Deleted");
	}
	
	static void getFilesInFolder() {
		if(FileCreator.allMeasuresChckbx.isSelected()) {
			
		} else {
			String excelPath = FileCreator.excelFilePathTxtbox.getText().replaceAll("Measure Rules.*.xlsx", "").replace("\\\\", "\\");
			String filePath = excelPath + "/Files/"+FileCreator.measureSetDropdownBx.getItemAt(FileCreator.measureSetDropdownBx.getSelectedIndex()).toString();
			File dir = new File(filePath);
			File[] listFiles = dir.listFiles();
			
			for(int i = (FileCreator.model.getRowCount() - 1); i >= 0; i--) {
				FileCreator.model.removeRow(i);
			}
			
			if(!(listFiles == null)) {
				for(File file : listFiles) {
					FileCreator.model.addRow(new Object[] {"", file.getName(), file.length()/1000+" kb"});
				}
			}
		}
	}
	
	public static int getSizeOfCurrentDirectory(String filePath) {
		File file = new File(filePath);
		File[] size = file.listFiles();
		return size.length;
	}
	
	static boolean checkFilePath(String filePath) {
		File file = new File(filePath);
		boolean exists = false;
		if(file.exists()) {
			exists = true;
			FileCreator.errorMsgTxtbox.setText("File loaded successfully.");
		} else {
			FileCreator.errorMsgTxtbox.setText("The specified file does not exist.");
		}
		return exists;
	}
	
}


//class RunED2 extends Thread {
//	public void run() {
//		try {
//			ED.editED2();
//		} catch (FileNotFoundException e) {
//			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
//		} catch (DOMException e) {
//			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
//		} catch (IOException e) {
//			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
//		} catch (ParseException e) {
//			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
//		}
//	}
//}
//
//class RunPC05 extends Thread {
//	public void run() {
//		try {
//			PC.editPC05();
//		} catch (DOMException e) {
//			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
//		} catch (IOException e) {
//			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
//		} catch (ParseException e) {
//			FileCreator.LOGGER.log(Level.SEVERE, e.toString());
//		}
//	}
//}
//
//class RunSTK2 extends Thread {
//	public void run() {
//		STK.editSTK2();
//	}
//}
//
//class RunSTK3 extends Thread {
//	public void run() {
//		STK.editSTK3();
//	}
//}
//
//class RunSTK5 extends Thread {
//	public void run() {
//		STK.editSTK5();
//	}
//}
//
//class RunSTK6 extends Thread {
//	public void run() {
//		STK.editSTK6();
//	}
//}
//
//class RunVTE1 extends Thread {
//	public void run() {
//		VTE.editVTE1();
//	}
//}
//
//class RunVTE2 extends Thread {
//	public void run() {
//		VTE.editVTE2();
//	}
//}







