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

public class ESExportSpolecnostSkupina4 extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSpolecnostSkupina4.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  
  private CellStyle lightGray;
  
  private int listNr = 0;

  boolean empty = true;

  public ESExportSpolecnostSkupina4(ApplicationModule dokladyModule)
  {
    dm = dokladyModule;
    init();
  }

  private void init() {
    logger.info("ExportSpolecnostSkupina4");  

    setFileName ( "ExportSpolecnostSkupina4.xlsx" );
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
    setCellValue( listNr, rowNr, colNr, "Intern tovan nearchivn spolenosti chybjc v kons. skupin 4" , lightGray ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "I" , lightGray ); colNr++;
    
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    String where = "KpKtgUcetnispolecnost.DT_DATUMARCHIVACE is null "+
               "and KpKtgUcetnispolecnost.id_cisSubject <> 200 "+
               "and (select count(*) "+
                    "from db_jt.kp_ktg_ucetniSpolecnost_link l "+
                    "where l.ID_KTGUCETNISPOLECNOST = KpKtgUcetnispolecnost.id "+
                      "and l.C_EXTSYSTEM='M' "+
                      "and sysdate between l.DT_PLATNOSTOD and l.DT_PLATNOSTDO) = 0 "+
               "and db_jt.f_jeSpolecnostClenSkupiny( KpKtgUcetnispolecnost.ID, 4, sysdate) <> 1 "+
               "and KpKtgUcetnispolecnost.id not in (9999,1024,1025,1020,6402) "+
               "and KpKtgUcetnispolecnost.c_extSystem <> 'M' ";
    vo.setWhereClause(where);
    vo.setOrderByClause("KpKtgUcetnispolecnost.s_nazev");
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
      ESExportSpolecnostSkupina4 ed = new ESExportSpolecnostSkupina4(dm);
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
