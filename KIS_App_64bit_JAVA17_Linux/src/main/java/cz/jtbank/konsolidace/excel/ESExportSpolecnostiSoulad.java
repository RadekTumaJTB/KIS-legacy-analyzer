package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
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

public class ESExportSpolecnostiSoulad extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSpolecnostiSoulad.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  
  private CellStyle lightGray;
  
  private int listNr = 0;

  boolean empty = true;

  public ESExportSpolecnostiSoulad(ApplicationModule dokladyModule)
  {
    dm = dokladyModule;
    init();
  }

  private void init() {
    logger.info("ExportSpolecnostiSoulad4");  

    setFileName ( "ExportSpolecnostiSoulad.xlsx" );
    setFileRelativeName( getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputDetail()
  {
    int rowNr=0;
    int colNr = 0;
    CellStyle style = null;
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "ID" , lightGray ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*100));
    setCellValue( listNr, rowNr, colNr, "Administrovan spolenosti bez propojen na tut evidovanou spolenost" , lightGray ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "I" , lightGray ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*40));
    setCellValue( listNr, rowNr, colNr, "Administrtor" , lightGray ); colNr++;
    
    ViewObject vo = dm.findViewObject("VwKtgEviadminspolecnostView1");
    vo.clearCache();
    String where = "KpKtgEviadminspolecnost.ID_KTGUCETNISPOLECNOST is null "+
               "and exists (select null "+
                           "from db_jt.kp_ktg_ucetniSpolecnost s "+
			                     "where s.S_ICO = KpKtgEviadminspolecnost.S_ICO "+
			                       "and s.ID_ADMINISTRATOR <> 231) ";
    vo.setWhereClause(where);
    vo.setOrderByClause("KpKtgEviadminspolecnost.s_nazev");
    while ( vo.hasNext() ) {
      empty = false;
      Row row = vo.next();
      rowNr++;
      colNr = 0;

      setCellValue( listNr, rowNr, colNr, row.getAttribute("Id").toString(), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SIco"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Jmeno"), style );
      colNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  protected boolean outputData () 
  {
    lightGray = wb.createCellStyle();
    lightGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightGray.setFillPattern(FillPatternType.SOLID_FOREGROUND);

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
      ESExportSpolecnostiSoulad ed = new ESExportSpolecnostiSoulad(dm);
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
