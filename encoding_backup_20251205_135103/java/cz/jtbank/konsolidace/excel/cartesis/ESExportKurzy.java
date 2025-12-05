package cz.jtbank.konsolidace.excel.cartesis;

import cz.jtbank.konsolidace.csv.*;
import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportKurzy extends AbsCsvDoklad 
{
  static Logger logger = Logger.getLogger(ESExportKurzy.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  protected static SimpleDateFormat sdfMesic = new SimpleDateFormat("yyyy.MM");

  public ESExportKurzy(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportKurzy:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    init();
  }

  private void init() {
    setFileName ( "Kurzy_"+(datum==null?"ALL":datum.toString())+".csv" );
    setFileRelativeName( Constants.DIR_CARTESIS+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }
  
  private void writeHeader() throws KisException, IOException {
						
      setValue("D_EXCH-PE");
      setComma();
      setValue("D_EXCH-VE");
      setComma();
      setValue("D_EXCH-CU");
      setComma();
      setValue("D_EXCH-RU");
      setComma();
      setValue("D_EXCH-TY");
      setComma();
      setValue("P_CONVRATE");
      setNewLine();
	  
  }
  
  private void outputKurzy() throws KisException, IOException {

    ViewObject vo = dm.findViewObject("DatKurzlistExportView1");
    vo.clearCache();
    String where = datum==null ? "" : "to_char(DT_DATUM,'yyyy.mm') = '"+sdfMesic.format(datum)+"' AND KURZ<>0";
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      oracle.jbo.domain.Date dat = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      String banka = (String)row.getAttribute("Banka");
      String mena = (String)row.getAttribute("Mena");
      Number kurz = (Number)row.getAttribute("Kurz");
      Number typ = (Number)row.getAttribute("Typ");


      setValue(sdfMesic.format(dat.dateValue()));
      setComma();
      setValue(banka);
      setComma();
      setValue(mena);
      setComma();
	  setComma();
	  if ( typ.intValue() == 0 ) 
	      setValue("0-CR");
	  else		  
		setValue("1-AR");
      setComma();
      setValue( kurz.toString().replace(',', '.'));
      setComma();
	  setNewLine();
      

    }
    vo.closeRowSet();
  }
    
  protected boolean outputData () throws KisException, IOException
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
	  writeHeader();
      outputKurzy();
      end = System.currentTimeMillis();    
      logger.debug("Kurzy:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.cartesis.CartesisModule","CartesisModuleLocal");

      ESExportKurzy ed = new ESExportKurzy(dm,new java.sql.Date(106,12,31));
      ed.createCsv();
      ed.csvOutput();
	  
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

/*
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

public class ESExportKurzy extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportKurzy.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  protected static SimpleDateFormat sdfMesic = new SimpleDateFormat("yyyy.MM");

  public ESExportKurzy(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportKurzy:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    init();
  }

  private void init() {
    setFileName ( "Kurzy_"+(datum==null?"ALL":datum.toString())+".xlsx" );
    setFileRelativeName( Constants.DIR_CARTESIS+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_CARTESIS+"SablonaKurzy.xlsx" );
  }
  
  private void outputKurzy() {
    int listNr=0;
    int rowNr=2;
    int colNr=0;

    ViewObject vo = dm.findViewObject("DatKurzlistExportView1");
	System.out.println ( vo.getDefFullName() + "   " + vo.getDefName() );
    vo.clearCache();
    String where = datum==null ? "" : "to_char(DT_DATUM,'yyyy.mm') = '"+sdfMesic.format(datum)+"' AND KURZ<>0";
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      colNr=0;
      oracle.jbo.domain.Date dat = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      String banka = (String)row.getAttribute("Banka");
      String mena = (String)row.getAttribute("Mena");
      Number kurz = (Number)row.getAttribute("Kurz");
      Number typ = (Number)row.getAttribute("Typ");
      
      setCellValue( listNr, rowNr, colNr, sdfMesic.format(dat.dateValue()), null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, banka, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, mena, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, "", null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, ""+typ, null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, kurz.doubleValue(), null, true ); 

      rowNr++;
    }
    vo.closeRowSet();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputKurzy();
      end = System.currentTimeMillis();    
      logger.debug("Kurzy:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.cartesis.CartesisModule","CartesisModuleLocal");

      ESExportKurzy ed = new ESExportKurzy(dm,new java.sql.Date(106,12,31));
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
*/