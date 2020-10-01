package com.xmlEditTool;
//C:\Users\KyleStevens\Documents\eCQM Generator\Measure Rules 2020.xlsx
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JTable;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class FileCreator {
	public final static Logger LOGGER = Logger.getLogger(FileCreator.class.getName());
	private JFrame frame;
	static JTextField excelFilePathTxtbox;
	
	public static JTextField numOfFilesTxtbox = new JTextField();
	static JTextField yearTxtbox = new JTextField();
	private static String[] quarter = {"Quarter","Q1","Q2","Q3","Q4"};
	static JComboBox quarterDropdownBx = new JComboBox(quarter);
	static JTextField ccnTextbox;
	//public static JTextField DoBTextbox;

	public static JCheckBox allMeasuresChckbx = new JCheckBox("All Measures");
	
	static String[] measureSet = {"Measure Set", "ED-2", "PC-05", "STK-2", "STK-3", "STK-5", "STK-6", "VTE-1", "VTE-2"};
	public static JComboBox measureSetDropdownBx = new JComboBox(measureSet);
	
	public static DefaultTableModel model = new DefaultTableModel();
	private JTable table = new JTable(model);
	private JScrollPane js = new JScrollPane(table);
	
	// IPP Dropdown
	private static String[] ipp = {"Initial Population", "Yes", "No"};
	private static JComboBox ippBox = new JComboBox(ipp);
	
	public static JCheckBox chckbxRandomize = new JCheckBox("Randomize");
	
	// DENOM Dropdown
	private static String[] denom = new String[0];
	private static JComboBox denomBox = new JComboBox(denom);
	public static boolean denominator = false;

	// DENEX Dropdown
	private static String[] denex = new String[0];
	public static JComboBox denexBox = new JComboBox(denex);
	public static boolean denominatorExclusion = false;

	// NUM Dropdown
	private static String[] num = new String[0];
	public static JComboBox numBox = new JComboBox(num);
	public static boolean numerator = false;

	// DENEC Dropdown
	private static String[] denec = new String[0];
	public static JComboBox denecBox = new JComboBox(denec);
	public static boolean denominatorException = false;

	private static JCheckBox createZipCheckbox = new JCheckBox("Create ZIP");
	private JButton createFileBtn = new JButton("Create");

	public static JCheckBox deleteAllChckbx = new JCheckBox("Delete All");
	public static JCheckBox deleteZipChckbx = new JCheckBox("Delete Zips");
	private JButton deleteBtn = new JButton("Delete");
	
	private static JButton clearBtn = new JButton("Clear");

	public static JTextField errorMsgTxtbox;

	public static int stratificationType;
	public static String stratification;
	/**
	 * Create the application.
	 */
	public FileCreator() {
		LOGGER.info("Logger Name:" + LOGGER.getName());
		frame = new JFrame("2020 eCQM Generator");
		frame.setBounds(100, 100, 785, 725);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel excelPathHeader = new JLabel("Excel File Path");
		excelPathHeader.setFont(new Font("Segoe WP Semibold", Font.BOLD, 12));
		excelPathHeader.setBounds(20, 11, 127, 14);
		frame.getContentPane().add(excelPathHeader);
		excelFilePathTxtbox = new JTextField();
		excelFilePathTxtbox.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
		excelFilePathTxtbox.setBounds(20, 30, 451, 20);
		frame.getContentPane().add(excelFilePathTxtbox);
		excelFilePathTxtbox.setColumns(10);
		
		/*
		 * UI Column 1 
		 */
		JButton loadFileBtn = new JButton("Load File");
		loadFileBtn.setFont(new Font("Dialog", Font.PLAIN, 11));
		loadFileBtn.setBounds(476, 29, 89, 23);
		frame.getContentPane().add(loadFileBtn);
		loadFileBtn.addActionListener(new ListenToLoad());

		JLabel numOfFilesHeader = new JLabel("Num. Of Files");
		numOfFilesHeader.setFont(new Font("Segoe WP Semibold", Font.BOLD, 12));
		numOfFilesHeader.setBounds(479, 61, 127, 14);
		frame.getContentPane().add(numOfFilesHeader);
		numOfFilesTxtbox.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
		numOfFilesTxtbox.setBounds(479, 77, 86, 20);
		frame.getContentPane().add(numOfFilesTxtbox);
		numOfFilesTxtbox.setColumns(5);
						
		JLabel yearHeader = new JLabel("Year: ");
		yearHeader.setFont(new Font("Segoe WP Semibold", Font.BOLD, 12));
		yearHeader.setBounds(479, 108, 127, 14);
		frame.getContentPane().add(yearHeader);
		yearTxtbox.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
		yearTxtbox.setBounds(479, 122, 86, 20);
		frame.getContentPane().add(yearTxtbox);
		yearTxtbox.setColumns(4);

		quarterDropdownBx.setBounds(479, 153, 97, 20);
		frame.getContentPane().add(quarterDropdownBx);
		quarterDropdownBx.setSelectedIndex(0);
		
		JLabel fileListHeader = new JLabel("Files");
		fileListHeader.setFont(new Font("Segoe WP Semibold", Font.BOLD, 12));
		fileListHeader.setBounds(20, 59, 127, 14);
		frame.getContentPane().add(fileListHeader);
		
		table.setShowHorizontalLines(false);
		js.setBounds(20, 78, 449, 523);
		model.addColumn("Provider");
		model.addColumn("Condition");
		model.addColumn("Size");
		table.getColumn("Provider").setPreferredWidth(55);
		table.getColumn("Condition").setPreferredWidth(250);
		table.getColumn("Size").setPreferredWidth(25);
		js.setVisible(true);
		frame.getContentPane().add(js);
				
		JTextArea resultsHeader = new JTextArea();
		resultsHeader.setForeground(Color.RED);
		resultsHeader.setBackground(SystemColor.control);
		resultsHeader.setEditable(false);
		resultsHeader.setBounds(385, 415, 127, 22);
		frame.getContentPane().add(resultsHeader);
		
		errorMsgTxtbox = new JTextField();
		errorMsgTxtbox.setForeground(Color.RED);
		errorMsgTxtbox.setFont(new Font("Book Antiqua", Font.BOLD, 14));
		errorMsgTxtbox.setEditable(false);
		errorMsgTxtbox.setBounds(20, 612, 451, 20);
		frame.getContentPane().add(errorMsgTxtbox);
		errorMsgTxtbox.setColumns(10);
				
		JLabel ccnHeader = new JLabel("CCN:");
		ccnHeader.setFont(new Font("Segoe WP Semibold", Font.BOLD, 12));
		ccnHeader.setBounds(479, 184, 127, 14);
		frame.getContentPane().add(ccnHeader);
		
		ccnTextbox = new JTextField();
		ccnTextbox.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
		ccnTextbox.setBounds(479, 199, 86, 20);
		frame.getContentPane().add(ccnTextbox);
		ccnTextbox.setColumns(10);
		
//		JLabel DoBHeader = new JLabel("Date of Birth (format YYYYMMDD):");
//		DoBHeader.setFont(new Font("Dialog", Font.BOLD, 12));
//		DoBHeader.setBounds(479, 223, 200, 14);
//		frame.getContentPane().add(DoBHeader);
//
//		DoBTextbox = new JTextField();
//		DoBTextbox.setFont(new Font("Book Antiqua", Font.PLAIN, 13));
//		DoBTextbox.setColumns(10);
//		DoBTextbox.setBounds(479, 237, 86, 20);
//		frame.getContentPane().add(DoBTextbox);
		/*
		 * END UI Column 1
		 */
		
		/*
		 * UI Column 2
		 */
		allMeasuresChckbx.setBounds(615, 29, 116, 23);
		frame.getContentPane().add(allMeasuresChckbx);
		allMeasuresChckbx.setVisible(true);
		
		measureSetDropdownBx.setBounds(616, 59, 127, 20);
		frame.getContentPane().add(measureSetDropdownBx);
		measureSetDropdownBx.addActionListener(new MeasureDropdown());
		
		ippBox.setBounds(616, 106, 127, 20);
		frame.getContentPane().add(ippBox);
		ippBox.addActionListener(new MeasureDropdown());
		
		chckbxRandomize.setBounds(615, 137, 97, 23);
		frame.getContentPane().add(chckbxRandomize);
		chckbxRandomize.setVisible(false);

		denomBox.setBounds(616, 167, 127, 20);		
		frame.getContentPane().add(denomBox);
		denomBox.setVisible(false);

		denexBox.setBounds(479, 268, 264, 20);		
		frame.getContentPane().add(denexBox);
		denexBox.setVisible(false);
		
		numBox.setBounds(479, 299, 264, 20);		
		frame.getContentPane().add(numBox);
		numBox.setVisible(false);
		
		denecBox.setBounds(479, 330, 264, 20);
		frame.getContentPane().add(denecBox);
		denecBox.setVisible(false);
				
		createZipCheckbox.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		createZipCheckbox.setBounds(627, 415, 97, 23);
		frame.getContentPane().add(createZipCheckbox);
		//createZipCheckbox.setVisible(false);

		createFileBtn.setForeground(Color.RED);
		createFileBtn.setFont(new Font("Segoe WP Semibold", Font.PLAIN, 20));
		createFileBtn.setBounds(597, 445, 146, 56);
		frame.getContentPane().add(createFileBtn);
		createFileBtn.addActionListener(new ListenToCreate());
		
		deleteAllChckbx.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		deleteAllChckbx.setBounds(627, 508, 104, 23);
		frame.getContentPane().add(deleteAllChckbx);
		
		deleteZipChckbx.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		deleteZipChckbx.setBounds(627, 534, 104, 23);
		frame.getContentPane().add(deleteZipChckbx);
		
		deleteBtn.setForeground(Color.BLACK);
		deleteBtn.setFont(new Font("Dialog", Font.PLAIN, 14));
		deleteBtn.setBounds(615, 564, 116, 37);
		frame.getContentPane().add(deleteBtn);
		deleteBtn.addActionListener(new ListenToDelete());
		
		clearBtn.setForeground(Color.BLACK);
		clearBtn.setFont(new Font("Dialog", Font.PLAIN, 14));
		clearBtn.setBounds(621, 608, 106, 27);
		frame.getContentPane().add(clearBtn);
		clearBtn.addActionListener(new ListenToClear());
		/*
		 * END UI Column 2
		 */
	}
	
	class ListenToLoad implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MeasureSets.checkFilePath(excelFilePathTxtbox.getText());
		}
		
	}

	class MeasureDropdown implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0)  {
			ippBox.setVisible(true);denomBox.setVisible(false);denexBox.setVisible(false);numBox.setVisible(false);denecBox.setVisible(false);chckbxRandomize.setVisible(false);
			denomBox.removeAllItems();denexBox.removeAllItems();numBox.removeAllItems();denecBox.removeAllItems();
			ipp = new String[0]; denom = new String[0]; denex = new String[0]; num = new String[0]; denec = new String[0];
			
			MeasureSets.getFilesInFolder();

			if(ippBox.getSelectedIndex() == 1) {
				chckbxRandomize.setVisible(true);
				denomBox.setVisible(true);
				denom = MeasureParameters.getDenominator(measureSetDropdownBx.getSelectedIndex());
				for(String s : denom) {	denomBox.addItem(s);}
				denomBox.addActionListener(new DenomDropdown());
			}
			//createZipCheckbox.setVisible(true);
		}
	}
	
	class DenomDropdown implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			denexBox.removeAllItems();numBox.removeAllItems();denecBox.removeAllItems();
			if(chckbxRandomize.isSelected()) {
				denominator = true;
				denomBox.setVisible(false);
			}
			denom = new String[0];
			if(denomBox.getSelectedIndex() == 1) {
				denominator = true;
				denex = MeasureParameters.getDenominatorExclusion(measureSetDropdownBx.getSelectedIndex());
				for(String s : denex) {denexBox.addItem(s);}
				denexBox.setVisible(true);

				denexBox.addActionListener(new DenexDropdown());
			}
		}
	}
	
	class DenexDropdown implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			numBox.removeAllItems();denecBox.removeAllItems();
			denex = new String[0];
			if(denexBox.getSelectedIndex() == 1) {
				numBox.setVisible(true);
				num = MeasureParameters.getNumerator(measureSetDropdownBx.getSelectedIndex());
				for(String s : num) {numBox.addItem(s);}
				numBox.addActionListener(new NumDropdown());
			}
			else if(denexBox.getSelectedIndex() > 1) {
				denominatorExclusion = true;
			}
			denec = MeasureParameters.getDenominatorException(measureSetDropdownBx.getSelectedIndex());
			for(String s : denec) {denecBox.addItem(s);}
			denecBox.setVisible(true);
			denecBox.addActionListener(new DenecDropdown());
		}
	}
	
	class NumDropdown implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			num = new String[0];
			if(numBox.getSelectedIndex() > 1)
				numerator = true;
		}
	}
	
	class DenecDropdown implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(denecBox.getSelectedIndex() > 1)
				denominatorException = true;
		}
	}
	
	class ListenToDelete implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(deleteAllChckbx.isSelected()) {
				for(int k = 1; k < measureSet.length; k++) {
					MeasureSets.deleteFiles(k);	}
			}
			else {MeasureSets.deleteFiles(measureSetDropdownBx.getSelectedIndex());	}
			
			if(deleteZipChckbx.isSelected())
				MeasureSets.deleteZip();

			for(int i = (model.getRowCount() - 1); i >= 0; i--)
				model.removeRow(i);
		}
	}
	
	class ListenToClear implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			numOfFilesTxtbox.setText(null);
			yearTxtbox.setText(null);
			ccnTextbox.setText(null);
			quarterDropdownBx.setSelectedIndex(0);
			allMeasuresChckbx.setSelected(false);
			measureSetDropdownBx.setSelectedIndex(0);
			ippBox.setSelectedIndex(0);
			chckbxRandomize.setSelected(false);	chckbxRandomize.setVisible(false);
			denomBox.setVisible(false);
			createZipCheckbox.setSelected(false);
			//DoBTextbox.setText(null);
			cleanup();
		}
	}
	
	class ListenToCreate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			errorMsgTxtbox.setText(null);
			errorMsgTxtbox.setText("Creating Files. . ." + (String) measureSetDropdownBx.getItemAt(measureSetDropdownBx.getSelectedIndex()));

			MeasureSets.createFolders();
			
			// create log
			createLog();
			
			// log new session
			LOGGER.info("Measure Set: " + measureSetDropdownBx.getItemAt(measureSetDropdownBx.getSelectedIndex()));
			LOGGER.info("Beginning File Creation : " + MeasureSets.getCurrentDate());
			
			// remove previous data rows, if any
			for(int i = (model.getRowCount() - 1); i >= 0; i--)
				model.removeRow(i);
			
			// check all value fields are populated
			checkForEmptyBoxes();
			
			// set parameters for creating the files			
			//@SuppressWarnings("unused")
			//MeasureSets assignVariables = new MeasureSets();
			MeasureSets.assignVariables();
			
			// For ED Measures ONLY!!!
			if(chckbxRandomize.isSelected()) {}
			else {stratifications();}
			
			try {
				long startTime = System.currentTimeMillis();

				// get Provider IDs
				MeasureSets.getIDs();
				
				/** MAIN METHOD CALL **/ 
				createFiles();
				//MeasureSets.editXmls(measureSetDropdownBx.getSelectedIndex());
				/** MAIN METHOD CALL **/
				
				long endTime = System.currentTimeMillis();
				LOGGER.info("Files Completed : " + MeasureSets.getCurrentDate()+"\n");
					
				LOGGER.info("Number of Files : " + (allMeasuresChckbx.isSelected() ? (new Integer(numOfFilesTxtbox.getText()) * 16) : numOfFilesTxtbox.getText()));
				LOGGER.info("Time : "+Math.round((endTime - startTime)/1000)+" sec");
			} catch(Exception e) {
				errorMsgTxtbox.setText(e.toString()); System.out.println(e.toString());
				System.out.println(e.getStackTrace());
				//LOGGER.log(Level.SEVERE, "Exception occur", e.getMessage().toString());
				LOGGER.log(Level.SEVERE, "Exception - Files could not be created. Please check log", e);
			}
			
			
			zipFilesIfSelected();
			
			cleanup();
			closeLog();
		}
	}
	
	private static void createFiles() {
		if(allMeasuresChckbx.isSelected()) {
			ippBox.setSelectedIndex(1);
			chckbxRandomize.setSelected(true);
			for(int i = 1; i <= measureSet.length; i++) {
				MeasureSets.numOfFiles = 0;
				MeasureSets.numOfFiles = Integer.parseInt(FileCreator.numOfFilesTxtbox.getText());
				MeasureSets.editXmls(i);
			}
		}
		else {MeasureSets.editXmls(measureSetDropdownBx.getSelectedIndex());}
	}
	
	private static void createLog() {
		try {
			String fp = excelFilePathTxtbox.getText().replaceAll("Measure Rules.*.xlsx", "log.log");
			FileHandler fh = new FileHandler(fp);
			fh.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fh);
			LOGGER.setUseParentHandlers(false);
			LOGGER.setLevel(Level.FINEST);
		} catch (SecurityException | IOException e1) {
			errorMsgTxtbox.setText(e1.toString()); System.out.println(e1.toString());
		}
	}
		
	private static boolean checkForEmptyBoxes() {
		boolean result = true;
		if(yearTxtbox.getText().isEmpty()) {
			errorMsgTxtbox.setText("Please enter a valid year"); result = false;}
		else if((quarterDropdownBx.getSelectedIndex() == 0)) {
			errorMsgTxtbox.setText("Please select a valid quarter"); result = false;}
		else if(numOfFilesTxtbox.getText().isEmpty()) {
			errorMsgTxtbox.setText("Please enter a valid number of files"); result = false;}
		else if(excelFilePathTxtbox.getText().isEmpty()) {
			errorMsgTxtbox.setText("Please enter a valid file path"); result = false;}
		return result;
	}
	
	private static void zipFilesIfSelected() {
		if(createZipCheckbox.isSelected()) {
			if(allMeasuresChckbx.isSelected()) {
				MeasureSets.zipAllFiles();
			}
			else {
				MeasureSets.zipFiles(measureSetDropdownBx.getSelectedIndex());
				LOGGER.log(Level.INFO, "Files Zipped : " + MeasureSets.getCurrentDate());
			}
		}
	}
	
	private static void cleanup() {
		denomBox.removeAllItems();denexBox.removeAllItems();numBox.removeAllItems();denecBox.removeAllItems();
		denominator = false; denominatorExclusion = false; denominatorException = false; numerator = false; 
		
		//ipp = new String[0]; 						// commented out so user does not have to select IPP=Yes to create random files for each measure set
		denom = new String[0]; 
		denex = new String[0]; 
		num = new String[0]; 
		denec = new String[0];
		
		//ippBox.setVisible(false);
		denomBox.setVisible(false);
		denexBox.setVisible(false);
		numBox.setVisible(false);
		denecBox.setVisible(false);
		//createZipCheckbox.setVisible(false);
		//chckbxRandomize.setVisible(false);		// commented out so user does not have to select "Random" to create random files for each measure set
		
		//ippBox.setSelectedIndex(0);				// commented out so user does not have to select IPP=Yes to create random files for each measure set
		//chckbxRandomize.setSelected(false);		// commented out so user does not have to select "Random" to create random files for each measure set
		deleteZipChckbx.setSelected(false); 
		deleteAllChckbx.setSelected(false);
		//createZipCheckbox.setSelected(false);
		//DoBTextbox.setText("");
	}
	
	private static void closeLog() {
		// close the logger
		for(Handler h:LOGGER.getHandlers())
		    h.close();   //must call h.close or a .LCK file will remain.
	}
	
	private static void stratifications() {
		if(measureSetDropdownBx.getSelectedItem().equals("ED-1")) {
			if(denecBox.getSelectedItem().toString().contains("1 - ")) {
				stratificationType = 1;
				stratification = "_STRAT_1";
			}
			if(denecBox.getSelectedItem().toString().contains("2 - ")) {
				stratificationType = 2;
				stratification = "_STRAT_2";
			}
		}
		
		if(measureSetDropdownBx.getSelectedItem().equals("ED-2")) {
			if(denecBox.getSelectedItem().toString().contains("1 - ")) {
				stratificationType = 1;
				stratification = "_STRAT_1";
			}
			if(denecBox.getSelectedItem().toString().contains("2 - ")) {
				stratificationType = 2;
				stratification = "_STRAT_2";
			}
		}
		
		if(measureSetDropdownBx.getSelectedItem().equals("ED-3")) {
			if(denecBox.getSelectedItem().toString().contains("1 - ")) {
				stratificationType = 1;
				stratification = "_STRAT_1";
			}
			if(denecBox.getSelectedItem().toString().contains("2 - ")) {
				stratificationType = 2;
				stratification = "_STRAT_2";
			}
			if(denecBox.getSelectedItem().toString().contains("3 - ")) {
				stratificationType = 3;
				stratification = "_STRAT_3";
			}
		}
	}
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileCreator window = new FileCreator();
					window.frame.setVisible(true);
					excelFilePathTxtbox.setText(System.getProperty("user.dir")+"\\");					
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.log(Level.SEVERE, this.getClass().getName(), e);
					LOGGER.log(Level.SEVERE, "Exception", e);
				}
			}
		});
	}
}
