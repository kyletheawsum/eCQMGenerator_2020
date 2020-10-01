package com.xmlEditTool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MeasureParameters {
	private static String excelPath = FileCreator.excelFilePathTxtbox.getText();
	public static ArrayList<String> denexValues = new ArrayList<String>();
	public static ArrayList<String> numValues = new ArrayList<String>();
	public static ArrayList<String> denecValues = new ArrayList<String>();
	public static ArrayList<String> stratValues = new ArrayList<String>();

	public static String[] getIPP(int measureSheet) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			InputStream ExcelFile1 = new FileInputStream(excelPath);
			Workbook wb = new XSSFWorkbook(ExcelFile1);	
			Sheet sheet = wb.getSheetAt(measureSheet);		
			
			for(int j = 0; j <= sheet.getLastRowNum();j++) {
				Row row2 = sheet.getRow(j);
				Cell cell2;
				if((cell2 = row2.getCell(1)) == null)
					break;
				
				list.add(cell2.toString());
			}
			String[] ipp = new String[list.size()];
			ipp = list.toArray(ipp);
			wb.close();
			return ipp;
		} catch (IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - retrieving IPP", e);
			return null;
		}
		
	}
	
	public static String[] getDenominator(int measureSheet) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			InputStream ExcelFile1 = new FileInputStream(excelPath);
			Workbook wb = new XSSFWorkbook(ExcelFile1);	
			Sheet sheet = wb.getSheetAt(measureSheet);		
			
			for(int j = 0; j <= sheet.getLastRowNum();j++) {
				Row row2 = sheet.getRow(j);
				Cell cell, cell2;
				try {
					while((cell2 = row2.getCell(3)) != null) {
						list.add(cell2.toString());
						cell = row2.getCell(3);
						values.add(cell.toString());		
						//System.out.println(cell2.toString() + ":" + cell.toString());
						break;
					}
				} catch(Exception e) {break;}		
			}
			String[] denom = new String[list.size()];
			denom = list.toArray(denom);
			wb.close();
			return denom;
		} catch (IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - retrieving Denominator", e);
			return null;
		}
	}
	
	public static String[] getDenominatorExclusion(int measureSheet) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			InputStream ExcelFile1 = new FileInputStream(excelPath);
			Workbook wb = new XSSFWorkbook(ExcelFile1);	
			Sheet sheet = wb.getSheetAt(measureSheet);		
			
			for(int j = 0; j <= sheet.getLastRowNum();j++) {
				Row row2 = sheet.getRow(j);
				Cell cell, cell2;
				try {
					while((cell2 = row2.getCell(4)) != null) {
						list.add(cell2.toString());
						cell = row2.getCell(5);
						denexValues.add(cell.toString());		
						//System.out.println(cell2.toString() + ":" + cell.toString());
						break;
					}
				} catch(Exception e) {break;}
			}
			String[] denex = new String[list.size()];
			denex = list.toArray(denex);
			wb.close();
			return denex;
		} catch (IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - retrieving Denominator Exclusion", e);
			return null;
		}
	}
	
	public static String[] getNumerator(int measureSheet) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			InputStream ExcelFile1 = new FileInputStream(excelPath);
			Workbook wb = new XSSFWorkbook(ExcelFile1);	
			Sheet sheet = wb.getSheetAt(measureSheet);		
			
			for(int j = 0; j <= sheet.getLastRowNum();j++) {
				Row row2 = sheet.getRow(j);
				Cell cell, cell2;
				try {
					while((cell2 = row2.getCell(6)) != null) {
						list.add(cell2.toString());
						cell = row2.getCell(7);
						numValues.add(cell.toString());		
						//System.out.println(cell2.toString() + ":" + cell.toString());
						break;
					}
				} catch(Exception e) {break;}
			}
			String[] num = new String[list.size()];
			num = list.toArray(num);
			wb.close();
			return num;
		} catch (IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - retrieving Numerator", e);
			return null;
		}
	}
	
	public static String[] getDenominatorException(int measureSheet) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			InputStream ExcelFile1 = new FileInputStream(excelPath);
			Workbook wb = new XSSFWorkbook(ExcelFile1);	
			Sheet sheet = wb.getSheetAt(measureSheet);		
			
			for(int j = 0; j <= sheet.getLastRowNum();j++) {
				Row row2 = sheet.getRow(j);
				Cell cell, cell2;
				try {
					while((cell2 = row2.getCell(8)) != null) {
						list.add(cell2.toString());
						cell = row2.getCell(9);
						denecValues.add(cell.toString());		
						//System.out.println(cell2.toString() + ":" + cell.toString());
						break;
					}
				} catch(Exception e) {break;}
			}
			String[] denec = new String[list.size()];
			denec = list.toArray(denec);
			wb.close();
			return denec;
		} catch (IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - retrieving Denominator Exception", e);
			return null;
		}
	}
	
	public static String[] getStratification(int measureSheet) {
		try {
			ArrayList<String> list = new ArrayList<String>();
			InputStream ExcelFile1 = new FileInputStream(excelPath);
			Workbook wb = new XSSFWorkbook(ExcelFile1);	
			Sheet sheet = wb.getSheetAt(measureSheet);		
			
			for(int j = 0; j < sheet.getLastRowNum();j++) {
				Row row2 = sheet.getRow(j);
				Cell cell, cell2;
				try {
					while((cell2 = row2.getCell(6)) != null) {
						list.add(cell2.toString());
						cell = row2.getCell(7);
						stratValues.add(cell.toString());		
						//System.out.println(cell2.toString() + ":" + cell.toString());
						break;
					}
				} catch(Exception e) {break;}		}
			String[] denec = new String[list.size()];
			denec = list.toArray(denec);
			wb.close();
			return denec;
		} catch (IOException e) {
			FileCreator.LOGGER.log(Level.SEVERE, "Exception occur - retrieving Stratification", e);
			return null;
		}
	}
	
}
