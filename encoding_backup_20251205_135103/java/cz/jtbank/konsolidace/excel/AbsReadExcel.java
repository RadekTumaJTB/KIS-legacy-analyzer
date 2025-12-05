package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public abstract class AbsReadExcel {

  static Logger logger = Logger.getLogger(AbsReadExcel.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_DEFAULT)); }

  public String getExportRedir() 
  {
    return "excelservlet/"+System.currentTimeMillis()+"?file=" + getFileRelativeName();
  }
  
  protected Workbook wb; 
  
  protected boolean checkMax(int aList, int aRow, int aColumn) 
  {
    if(aColumn>254 || aRow>65535/* || aList>31*/) return false;
    return true;
  }

  protected double getDoubleValue( int aList, int aRow, int aColumn ) {
    if(!checkMax(aList, aRow, aColumn)) return 0;

    Sheet sheet  = wb.getSheetAt(aList);
    if(sheet == null) return 0;

    Row row  = sheet.getRow(aRow);
    if(row == null) return 0;

    Cell cell = row.getCell(aColumn);
    if(cell == null) return 0;

    if(cell.getCellType() != CellType.NUMERIC) return 0;

    return cell.getNumericCellValue();
  }

  protected int getIntValue( int aList, int aRow, int aColumn ) {
    if(!checkMax(aList, aRow, aColumn)) return 0;

    Sheet sheet  = wb.getSheetAt(aList);
    if(sheet == null) return 0;

    Row row  = sheet.getRow(aRow);
    if(row == null) return 0;

    Cell cell = row.getCell(aColumn);
    if(cell == null) return 0;

    if(cell.getCellType() != CellType.NUMERIC) return 0;

    return (int)cell.getNumericCellValue();
  }

  static NumberFormat nf = NumberFormat.getNumberInstance();
  static {
    nf.setGroupingUsed(false);
    nf.setMinimumFractionDigits(0);
  }

  protected String getStringValue( int aList, int aRow, int aColumn ) {
    if(!checkMax(aList, aRow, aColumn)) return null;

    Sheet sheet  = wb.getSheetAt(aList);
    if(sheet == null) return null;

    Row row  = sheet.getRow(aRow);
    if(row == null) return null;

    Cell cell = row.getCell(aColumn);
    if(cell == null) return null;

    if(cell.getCellType() != CellType.STRING)
      if(cell.getCellType() == CellType.NUMERIC) return nf.format( cell.getNumericCellValue() );
      else return null;

    return cell.getStringCellValue();
  }

  private String fileName, relativeFileName, absolutFileName;
  
  public String getFileName() {
      return  (fileName);
  }

  public String getFileRelativeName() {
      return  ( relativeFileName );
  }

  public String getFileAbsoluteName() {
      return  ( absolutFileName );
  }

  public void setFileName(String fileName) {
      this.fileName=fileName;
  }

  public void setFileRelativeName(String relativeFileName) {
      this.relativeFileName=relativeFileName;
  }

  public void setFileAbsoluteName(String absolutFileName) {
      this.absolutFileName=absolutFileName;
  }

  public static String IN_DIR = Constants.XLS_FILES_PATH;
    
  private String newFile = null;  
  private File fFileOutput = null; // posledni vytvoreny

  public File getLastFileOutput () {
      return ( fFileOutput );
  }
    
  protected abstract boolean readData () throws KisException;

  public void excelRead() throws IOException, KisException {
    try (FileInputStream fIn = new FileInputStream( getFileAbsoluteName() )) {
      // XSSF supports .xlsx format - no need for POIFSFileSystem
      wb = new XSSFWorkbook(fIn);

      readData();

    } catch ( IOException e ) {
        e.printStackTrace();
        throw e;
    }
  }
  
  protected int getListNr(String zacatek) 
  {
    int cnt = wb.getNumberOfSheets();
    for(int i=0; i<cnt; i++) 
    {
      if(wb.getSheetName(i).startsWith(zacatek)) return i;
    }
    return 0;
  }


}
