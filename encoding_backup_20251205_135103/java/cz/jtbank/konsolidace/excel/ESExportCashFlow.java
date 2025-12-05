package cz.jtbank.konsolidace.excel;

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
import cz.jtbank.konsolidace.projekt.common.ProjektModule;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportCashFlow extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportCashFlow.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;

  private String dir;

  public ESExportCashFlow(ApplicationModule dokladyModule)
  {
    logger.info("ESExportCashFlow");  
    dm = dokladyModule;
    dir = Constants.DIR_POZICE_MU;
    init();
  }

  private void init() {
    setFileName ( "CashFlow.xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaCashFlow.xlsx" );
  }
  
  private void outputCashFlow(boolean credit) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    int listNr=credit?0:1;
    int rowNr=3;
    int colNr=1;

    ViewObject vo = dm.findViewObject("VwCashflowView1");
    vo.clearCache();
    String where = "LOWER(TRIM(STAV_SMLOUVY))='aktivn' AND LOWER(TRIM(CHAR_CASTKY))='spltka' AND DATUM_PREDPISU>SYSDATE";
    where += " AND LOWER(TRIM(TYP_SMLOUVY))"+(credit?"=":"<>")+"'kreditn'";
    vo.setWhereClause(where);
    vo.setOrderByClause("DATUM_PREDPISU,NAZEV");
    while(vo.hasNext() && rowNr<Constants.MAX_POCET_RADKU_EXCEL) 
    {
      colNr = 1;
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Nazev") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CharCastky") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("TypSmlouvy") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DatumPredpisu");
      if(hlpDt!=null) setCellValue( listNr, rowNr, colNr, sdf.format(hlpDt.dateValue()) , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Castka");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue() , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mena") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Uhrada");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue() , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("KUhrade");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue() , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Counterparty") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Smlouva") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Poradi");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CmlNazev") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Predmet") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("VarSymbol") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("BuSpolecnosti") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("UcetSalda") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Protiucet") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CharSmlouvy") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("ManazerId");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("ProjektId");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("DruhSmlouvy") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("PopisDruhu") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("StavSmlouvy") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CClenscope8") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CInterni") , null ); 

/*
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Stavid");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("P1v0");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
*/

      rowNr++;
    }
    if (vo.hasNext()) {

      setCellValue( listNr, rowNr, 1, "D A T A    N E J S O U   K O M P L E T N  - poet dk pesahuje monosti excelu" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      wb.cloneSheet(0);
      start = System.currentTimeMillis();    
      outputCashFlow(true);
      wb.setSheetName(0,"Kredit");
      outputCashFlow(false);
      wb.setSheetName(1,"Debet");
      end = System.currentTimeMillis();    
      logger.debug("CashFlow:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      
      ESExportCashFlow ed = new ESExportCashFlow(dm);
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
