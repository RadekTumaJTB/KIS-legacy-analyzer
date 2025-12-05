package cz.jtbank.konsolidace.excel;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import cz.jtbank.konsolidace.common.Constants;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportVazby extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportVazby.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_VAZBY)); }

  private ApplicationModule dm;
  private Number idDoklad;
  private Number idKtgSpolecnost;
  private java.sql.Date datum;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;

  public ESExportVazby(ApplicationModule dokladyModule,
                       Number idDoklad)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    init();
  }
  
  private void init() {
    ViewObject vo = dm.findViewObject("VwKpDokladzahlaviView1");
    vo.clearCache();
    vo.setWhereClause("DOKLADID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("Spolecnostnazev");
      menaSpol = (String) row.getAttribute("SMena");
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
      idKtgSpolecnost = (Number) row.getAttribute("Ucetnispolecnostid");
      datum = ((oracle.jbo.domain.Date) row.getAttribute("DtDatum")).dateValue();
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    setFileName ( "Vazby_"+datum+".xlsx" );
    setFileRelativeName( souborPredponaSpol+"_"+idKtgSpolecnost+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaVV.xlsx" );
  }
  
  protected boolean outputData () 
  {
    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();    
    int listNr, rowNr, colNr, pocetSpol=-1;

    String spol = "";    
    ViewObject vo = dm.findViewObject("VwKpDokladvzajemvazbyView1");
    vo.clearCache();
    vo.setWhereClauseParam(0,idDoklad);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      listNr = ((Number)row.getAttribute("NlPoradilist")).intValue();
      rowNr = ((Number)row.getAttribute("NlRadek")).intValue();
      colNr = ((Number)row.getAttribute("NlSloupec")).intValue();
      String hlp = (String)row.getAttribute("SZkratka" );
      if( !spol.equals(hlp) ) {
        spol = hlp;
        pocetSpol++;
        // aktiva maji i opravne polozky
        setCellValue( 0, 4, 2 + ( pocetSpol * 3 ), spol, null );
        for ( int i = 1; i<5; i++ ) {
          setCellValue( i, 4, 2+pocetSpol, spol, null );
        }
      }

      if ( listNr == 1 ) {
        // aktiva maji i opravne polozky
        colNr = 2 + ( pocetSpol * 3 ) + (colNr==1 ? 0 : 1 );
        rowNr+=7;
      } else {
        colNr = 2 + pocetSpol; 
        rowNr+=6;
      }
      setCellValue ( listNr-1, rowNr, colNr, ((Number)row.getAttribute("NdCastkalocal")).doubleValue(), null );
    }
    outputZahlavi ( nazevSpol, menaSpol, datum );
    for ( int sl = 2 + ( ++pocetSpol * 3 ); sl < 80; sl++ ) {
        for ( int r = 4; r < 200; r++ ) {
            clearCell ( 0, r, sl );
        }
    }

    for ( int sl = 1 + pocetSpol * 3; sl > 1; sl-- ) {
        setCellValue ( 0, 7, sl, menaSpol, null );
    }
    for ( int sl = 1 + pocetSpol; sl > 1; sl-- ) {
        setCellValue ( 1, 6, sl, menaSpol, null );
        setCellValue ( 2, 6, sl, menaSpol, null );
        setCellValue ( 3, 6, sl, menaSpol, null );
        setCellValue ( 4, 6, sl, menaSpol, null );
    }
    for ( int sl = 2 + pocetSpol; sl < 30; sl++ ) {
        for ( int r = 4; r < 200; r++ ) {
            for ( int l = 1; l<5; l++ ) {
                clearCell ( l, r, sl );
            }
        }
    }
    
    vo.closeRowSet();
    
    dm.getTransaction().commit();
    end = System.currentTimeMillis();    
    logger.debug("vazby:"+((end-start)/1000.0)+"s");
    
    return true;
  }  
  
  public static void main(String[] argv) 
  {
    try {
      ESExportVazby ev = new ESExportVazby(Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal"),
                                           new Number(1865828));
      ev.excelOutput();
      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=ev.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
}