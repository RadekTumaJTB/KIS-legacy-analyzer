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

public class ESExportEviDodavatelNaklad extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportEviDodavatelNaklad.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  public ESExportEviDodavatelNaklad(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportEviDodavatelNaklad:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    dir = Constants.DIR_ADMIN_NAKLADY;
    init();
  }

  private void init() {
    setFileName ( "AdminNaklady_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputEviDodavatelNaklad() {
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

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Admin. spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
    
    rowNr++;

    ViewObject vo = dm.findViewObject("KpDatEviadmindodavatelView1");
    vo.clearCache();
    String where = "DT_DATUM = TRUNC(TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy'),'Q')";
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Number hlpNum = null;
      String strDatum=null;

      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("AdminSpolecnost") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastkanaklady");
      setCellValue( listNr, rowNr, colNr, (hlpNum==null?0.0:hlpNum.doubleValue()) , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdDokument");
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
      outputEviDodavatelNaklad();
      end = System.currentTimeMillis();    
      logger.debug("EviDodavatelNaklad:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.evi.EviModule","EviModuleLocal");
//      ESExportEviDodavatelNaklad ed = new ESExportEviDodavatelNaklad(dm,new java.sql.Date(System.currentTimeMillis()));
      ESExportEviDodavatelNaklad ed = new ESExportEviDodavatelNaklad(dm,new java.sql.Date(System.currentTimeMillis()));
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
