package com.ot.pet.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.ss.usermodel.Hyperlink;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import sun.security.krb5.internal.crypto.RsaMd5CksumType;

public class ExtractDMVs {
	
	public static String inputfile = null;
	public static String outputfile = null;
	public static String connectionURL = null;
	public static String dbHostName = null;
	public static String dbInstanceName = null;
	public static String dbUserName = null;
	public static String dbPassword = null;
	public static String query = null;
	public static String dbName = null;
	public static String resultsPath = null;
	public static String queryPath = null;
	public static Statement stmnt = null;
	static ResultSet rs = null;
	public static String dmvQuerydir = null;
	public static HSSFWorkbook workbook = null; 
	public static HSSFSheet sheet = null;
	public static HSSFCellStyle cellStyle = null;
	
	public static void main(String[] args) throws IOException {
		inputfile = args[0];
		outputfile= args[1];
		resultsPath= args[2];
		startProcess();
	}

	private static void startProcess() throws IOException {
		// TODO Auto-generated method stub
		initialize();
		createQueryStatement();
		fireDMVs();
	}

	private static void createQueryStatement() {
		// TODO Auto-generated method stub
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			String connectionURL = "jdbc:sqlserver://" + dbHostName +"\\" + dbInstanceName + ";databaseName=" + dbName + ";user=" + dbUserName + ";password=" + dbPassword;
			Connection con = DriverManager.getConnection(connectionURL);
			stmnt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void initialize() throws IOException {
		FileInputStream fis = new FileInputStream(inputfile);
		Properties prop = new Properties();
		prop.load(fis);
		dbHostName = prop.getProperty("HOSTNAME");
		dbInstanceName = prop.getProperty("INSTANCENAME");
		dbName = prop.getProperty("DBNAME");
		dbUserName =prop.getProperty("USERNAME");
		dbPassword = prop.getProperty("PASSWORD");
		//resultsPath = prop.getProperty("Target_Results_Path");
		dmvQuerydir = prop.getProperty("DMV_QUERY_DIRECTORY");
	}
	
	private static void fireDMVs() throws IOException {
		// TODO Auto-generated method stub
		
		FileOutputStream fos = null;
		String value = null;
		int columnCount = 0;
		int position = 0;
		String colName = null;
		String dmvNameWithoutExtension = null;
		fos = new FileOutputStream(resultsPath + File.separator + outputfile +".xls");
		workbook = new HSSFWorkbook();
		
		cellStyle = workbook.createCellStyle();
        HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 10);
        hSSFFont.setItalic(true);
        hSSFFont.setColor(HSSFColor.BLUE.index);
        cellStyle.setFont(hSSFFont);
        
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
		int currentCellIndex = -1;
		
		try {
			
			File[] queryList = getQueriesInDir(dmvQuerydir);
			for (File queryName : queryList) {
				
				query = getQueryText(queryName);
				String dmvName = queryName.getName();
				System.out.println(dmvName);
				dmvNameWithoutExtension = dmvName.substring(0,dmvName.lastIndexOf("."));
			
				//query = getQueryText(dmvQuerydir + File.separator + "DMV_AllWaitInfo.sql");
				rs = stmnt.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();
				columnCount = rsmd.getColumnCount();
				
				sheet = workbook.createSheet(dmvNameWithoutExtension);
				position = 0;
				
				Row rowID = sheet.createRow(position);
				for (int i = 1 ; i <= columnCount ; i++) {
					Cell cell = rowID.createCell(i-1);
					colName = rsmd.getColumnName(i);
					cell.setCellValue(colName.toUpperCase());
					cell.setCellStyle(headerStyle);
				}
				while (rs.next()) {
					position++;
					currentCellIndex = -1;
					rowID = sheet.createRow(position);
					
					for (int i = 1 ; i <= columnCount ; i++) {
						value = rs.getString(i);
						if (value == null) {
							value = "null";
						}
						currentCellIndex = insertcell(rowID,currentCellIndex,value);
					}
				}
			}
			workbook.write(fos);
			
			System.out.println("Extraction Completed");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

private static int insertcell(Row rowID, int currentCellIndex, String value) throws IOException {
	currentCellIndex++;
	Cell cell = rowID.createCell(currentCellIndex);
	if (value.length() < 32767) {
		cell.setCellValue(value);
	}
	else if (value.length() >= 32767) {
		String linkFile = sheet.getRow(0).getCell(currentCellIndex).toString().toLowerCase() + "_" + (rowID.getRowNum()+ 1);
		FileUtils.writeStringToFile(new File(resultsPath + File.separator +linkFile + ".txt"), value);
		cell.setCellValue(linkFile);
		cell.setCellStyle(cellStyle);
		linkFileToCell(cell, linkFile);
	}
	return currentCellIndex;
}

private static void linkFileToCell(Cell targetCell, String linkFile) {
	HSSFHyperlink file_link=new HSSFHyperlink(HSSFHyperlink.LINK_FILE);
	file_link.setAddress("./" + linkFile + ".txt");
	targetCell.setHyperlink(file_link);
}

public static String getQueryText(File queryName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(queryName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}

public static File[] getQueriesInDir( String dirName){
    File dir = new File(dirName);

    return dir.listFiles(new FilenameFilter() { 
             public boolean accept(File dir, String filename)
                  { return filename.endsWith(".sql"); }
    } );

}

}
