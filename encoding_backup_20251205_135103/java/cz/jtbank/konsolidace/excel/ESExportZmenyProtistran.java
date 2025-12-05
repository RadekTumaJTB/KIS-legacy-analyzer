package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportZmenyProtistran extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportZmenyProtistran.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  
  private SimpleDateFormat sdfSim = new SimpleDateFormat("dd.MM.yyyy");
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, kk:mm:ss");

  boolean empty = true;

  public ESExportZmenyProtistran(ApplicationModule dokladyModule, java.sql.Date datum)
  {
    dm = dokladyModule;
    this.datum = datum;
    init();
  }

  private void init() {
    logger.info("ExportZmenyProtistran");  

    setFileName ( "BilanceZmenyProtistran_"+datum+".xlsx" );
    setFileRelativeName( Constants.DIR_ZMENY_PROTISTRAN+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputDetail()
  {
    int listNr = 0;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    CellStyle green = wb.createCellStyle();
    green.setFillForegroundColor(IndexedColors.GREEN.getIndex());
    green.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    CellStyle yellow = wb.createCellStyle();
    yellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    yellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    CellStyle red = wb.createCellStyle();
    red.setFillForegroundColor(IndexedColors.RED.getIndex());
    red.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    
    CellStyle style = null;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Zmna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Databze" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "I" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*70));
    setCellValue( listNr, rowNr, colNr, "Nzev" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*70));
    setCellValue( listNr, rowNr, colNr, "Detail" , lightBlue ); colNr++;
    
    vo = dm.findViewObject("VwProtistranahistorieView1");
    vo.clearCache();
    vo.setWhereClause("DT_DATUMZMENY BETWEEN SYSDATE-1 AND SYSDATE");
    vo.setOrderByClause("DT_DATUMZMENY,ROWID");
    while ( vo.hasNext() && pocet-- > 0 ) {
      empty = false;
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      String akce = (String)row.getAttribute("SAkce");
      if(akce.startsWith("Za")) style = green;
      else if(akce.startsWith("Zm")) style = yellow;
      else if(akce.startsWith("Zr")) style = red;
      else style = null;
      
      setCellValue( listNr, rowNr, colNr, akce, style );                        
      colNr++;
      Timestamp date = ((Date)row.getAttribute("DtDatumzmeny")).timestampValue();
      String datum = ( date == null ) ? "" : sdf.format(new java.util.Date(date.getTime()));
      setCellValue( listNr, rowNr, colNr, datum, null );               
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SDblink"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SIco"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Detailprotistrana"), null );
      colNr++;
    }
    if ( pocet <= 0 ) {
      setCellValue( listNr, rowNr+1, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( listNr, rowNr+2, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  protected boolean outputData () 
  {
    outputDetail();
    
    return !empty;
  }

  public boolean isEmpty() 
  {
    return empty;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportZmenyProtistran ed = new ESExportZmenyProtistran(dm, new java.sql.Date(System.currentTimeMillis()));
      ed.excelOutput();

      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=ed.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
}
