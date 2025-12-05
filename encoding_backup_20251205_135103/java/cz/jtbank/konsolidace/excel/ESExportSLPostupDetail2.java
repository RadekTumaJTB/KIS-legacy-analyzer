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

public class ESExportSLPostupDetail2 extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSLPostupDetail2.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  public ESExportSLPostupDetail2(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportSLPostupDetail2:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    dir = Constants.DIR_SL_POSTUP;
    init();
  }

  private void init() {
    setFileName ( "SLPostupDetail2_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputSLPostupDetail2() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    int listNr=0;
    int rowNr=0;
    int colNr=0;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    Font font = wb.createFont();
    font.setColor(IndexedColors.RED.getIndex());
    CellStyle styleAlarm = wb.createCellStyle();
    styleAlarm.setFont(font);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Splatnost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Definice" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Zadn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Po splatnosti" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Zadavatel" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Zpracovn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Po splatnosti" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Dn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Schvalujc" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Schvlen" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Po splatnosti" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Dn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "etn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Zatovn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Po splatnosti" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Dn" , lightBlue ); colNr++;
    
    rowNr++;

    ViewObject vo = dm.findViewObject("VwRepSchvalovakpostupView1");
    vo.clearCache();
    String where = "DT_DATUMZADANI BETWEEN ADD_MONTHS(TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy'),-3) AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')";
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      String strDatum=null;
      boolean pozde = false;
      
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumsplatnosti");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Appuser") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumzadani");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      pozde = "1".equals(row.getAttribute("A0"));
      setCellValue( listNr, rowNr, colNr, strDatum , pozde?styleAlarm:null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, pozde?"ANO":"NE", null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Zadavatel") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumzadaniradku");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      pozde = "1".equals(row.getAttribute("Alarm1"));
      setCellValue( listNr, rowNr, colNr, strDatum , pozde?styleAlarm:null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, pozde?"ANO":"NE", null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("D1");
      setCellValue( listNr, rowNr, colNr, (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Schvalujici") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumschvaleni");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      pozde = "1".equals(row.getAttribute("Alarm2"));
      setCellValue( listNr, rowNr, colNr, strDatum , pozde?styleAlarm:null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, pozde?"ANO":"NE", null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("D2");
      setCellValue( listNr, rowNr, colNr, (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ucetni") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumzauctovani");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      pozde = "1".equals(row.getAttribute("Alarm3"));
      setCellValue( listNr, rowNr, colNr, strDatum , pozde?styleAlarm:null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, pozde?"ANO":"NE", null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("D3");
      setCellValue( listNr, rowNr, colNr, (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;

      rowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputSLPostupDetail2();
      end = System.currentTimeMillis();    
      logger.debug("SLPostupDetail2:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModule","DokumentModuleLocal");
//    ESExportSLPostup ed = new ESExportSLPostup(dm,new java.sql.Date(System.currentTimeMillis()));
      ESExportSLPostupDetail2 ed = new ESExportSLPostupDetail2(dm,new java.sql.Date(System.currentTimeMillis()));
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
