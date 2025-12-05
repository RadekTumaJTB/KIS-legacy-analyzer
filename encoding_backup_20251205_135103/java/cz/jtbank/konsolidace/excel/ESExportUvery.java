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

public class ESExportUvery extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportUvery.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  public ESExportUvery(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportUvery:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_UVERY;
    init();
  }

  private void init() {
    setFileName ( "Uvery_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputUvery() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    int listNr=0;
    int rowNr=0;
    int colNr=0;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    Font font = wb.createFont();
    font.setColor(IndexedColors.BLUE.getIndex());
    CellStyle styleJtfg = wb.createCellStyle();
    styleJtfg.setFont(font);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Projekt" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Projekt" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Odbor" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Odbor" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Typ kategorie" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*12));
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka v mn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka v CZK" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*7));
    setCellValue( listNr, rowNr, colNr, "rok v %" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Open date" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "End date" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*12));
    setCellValue( listNr, rowNr, colNr, "et as. rozlien" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "as. rozlien v mn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "as. rozlien v CZK" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "len JTFG Konsolidace" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "Id protistrany BIS" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "K.O." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Sprvce" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Segment subjektu" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Segment projektu" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Scoring ref. 1" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "- popis" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Klasifikace" , lightBlue ); colNr++;
    
    rowNr++;

    String strDatum = "",
           openDate = "",
           endDate = "";
    double castka = 0,
           castkaCZK = 0,
           urok = 0,
           crMena = 0,
           crCZK = 0;
  
    ViewObject vo = dm.findViewObject("VwKtgUverView1");
    vo.clearCache();
    String where = "ID_DOKLAD = DB_JT.F_GETMAXDOKLAD(ID_KTGUCETNISPOLECNOST, TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy'))";
    try {
      where = "ID_DOKLAD IN ("+((ProjektModule)dm).getMaxDoklad(1, datum, 2)+","+((ProjektModule)dm).getMaxDoklad(5000, datum, 2)+")";
    }
    catch (KisException kex) {}
    //where += " AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD_CR AND DT_PLATNOSTDO_CR";
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      boolean czSk = "1".equals(row.getAttribute("IdKtgucetnispolecnost").toString());
      String idSpol =  czSk ? "CZ" : "SK";
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("Datum");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtOpendate");
      openDate = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtEnddate");
      endDate = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      Number hlpNum = null;
      hlpNum = (Number) row.getAttribute("NdCastkamena");
      castka = hlpNum==null ? 0 : hlpNum.doubleValue();
      hlpNum = (Number) row.getAttribute("NdCastkaczk");
      castkaCZK = hlpNum==null ? 0 : hlpNum.doubleValue();
      hlpNum = (Number) row.getAttribute("NdUrok");
      urok = hlpNum==null ? 0 : hlpNum.doubleValue();
      hlpNum = (Number) row.getAttribute("NdCastkamenaCr");
      crMena = hlpNum==null ? 0 : hlpNum.doubleValue();
      hlpNum = (Number) row.getAttribute("NdCastkaczkCr");
      crCZK = hlpNum==null ? 0 : hlpNum.doubleValue();
      boolean clenJTFG = "1".equals(row.getAttribute("Clenjtfgkons"));

      setCellValue( listNr, rowNr, colNr, idSpol , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("IdKtgprojekt")==null?"":row.getAttribute("IdKtgprojekt").toString() , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Projekt") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("IdKtgodbor")==null?"":row.getAttribute("IdKtgodbor").toString() , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Odbor") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Typkategorie") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost") , (clenJTFG ? styleJtfg : null) ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castka , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castkaCZK , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, urok , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, openDate , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, endDate , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetCr") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, crMena , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, crCZK , null ); 
      colNr++;
      setCellValue(listNr,rowNr,colNr,clenJTFG?"ANO":"NE",null);
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNumber0") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ko") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spravce") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegmentspol") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegmentproj") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Scoringref1") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("DocDescription") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Klasifikace") , null ); 

      rowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputUvery();
      end = System.currentTimeMillis();    
      logger.debug("Uvery:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.projekt.ProjektModule","ProjektModuleLocal");
//      ESExportUvery ed = new ESExportUvery(dm,new java.sql.Date(System.currentTimeMillis()));
      ESExportUvery ed = new ESExportUvery(dm,new java.sql.Date(106,11,31));
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
