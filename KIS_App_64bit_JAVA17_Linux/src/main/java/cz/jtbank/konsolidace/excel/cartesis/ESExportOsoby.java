package cz.jtbank.konsolidace.excel.cartesis;

import cz.jtbank.konsolidace.excel.*;
import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportOsoby extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportOsoby.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;

  public ESExportOsoby(ApplicationModule dokladyModule)
  {
    logger.info("ESExportOsoby");  
    dm = dokladyModule;
    init();
  }

  private void init() {
    setFileName ( "Osoby_ALL.xlsx" );
    setFileRelativeName( Constants.DIR_CARTESIS+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_CARTESIS+"SablonaOsoby.xlsx" );
  }
  
  private void outputOsoby() {
    int listNr=0;
    int rowNr=2;
    int colNr=0;
    
    int delka_long = 120, delka_short = 30;

    ViewObject vo = dm.findViewObject("VwRepOsobycartesisView1");
    vo.clearCache();
    String where = "";
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      String sID = row.getAttribute("Id").toString();

      if ( "-1".equals ( sID ) ) continue;
      
      colNr=0;
      String jmeno = (String)row.getAttribute("CeleJmeno");
      String nazev_short = jmeno, nazev_long = jmeno;
     
      if ( nazev_short != null && nazev_short.length() > delka_short ) {
        nazev_short = nazev_short.substring(0,delka_short);
      }
      if ( nazev_long != null && nazev_long.length() > delka_long ) {
        nazev_long = nazev_long.substring(0,delka_long);
      }
      
      
      String email = (String)row.getAttribute("SEmail");
      String telefon = (String)row.getAttribute("STelefonformated");
      
      String kontakt = email;
      if(email!=null && telefon!=null) kontakt+=", ";
      if(kontakt!=null && telefon!=null) kontakt+=telefon;
      
      setCellValue( listNr, rowNr, colNr, sID, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_long, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_long, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_short, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_short, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, kontakt, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, kontakt, null ); 

      rowNr++;
    }
    vo.closeRowSet();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputOsoby();
      end = System.currentTimeMillis();    
      logger.debug("Osoby:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.cartesis.CartesisModule","CartesisModuleLocal");

      ESExportOsoby ed = new ESExportOsoby(dm);
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
