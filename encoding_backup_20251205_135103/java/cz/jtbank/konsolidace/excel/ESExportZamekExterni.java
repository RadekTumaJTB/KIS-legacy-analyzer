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

public class ESExportZamekExterni extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportZamekExterni.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportZamekExterni(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportZamekExterni:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    dir = Constants.DIR_ZAMEK_PROTOKOL;
    init();
  }

  public ESExportZamekExterni(ApplicationModule dokladyModule)
  {
    logger.info("ESExportZamekExterni:bez datumu");  
    dm = dokladyModule;
    this.datum = null;
    dir = Constants.DIR_ZAMEK_PROTOKOL;
    init();
  }

  private void init() {
    if(datum!=null )setFileName ( "ZamekExterni_"+datum+".xlsx" );
    else setFileName ( "ZamekExterni.xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputZamekExterni() {
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

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "Id" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*60));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Z" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "SK" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "OO" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "OOH" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Doklad" , lightBlue ); colNr++;

    rowNr++;

    ViewObject vo = dm.findViewObject("VwKpUcspolpocetzamkuView1");
    vo.clearCache();
    String where = "nl_pocetSchvaleni=0 and c_extSystem='M'"+
                   (datum!=null ? " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')" : "" );
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      String strDatum=null;

      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ucetni") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Sk") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Oo") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ss") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdDoklad") , null ); 

      rowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputZamekExterni();
      end = System.currentTimeMillis();    
      logger.debug("ZamekExterni:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");

      ESExportZamekExterni ed = new ESExportZamekExterni(dm);
      //ESExportZamekExterni ed = new ESExportZamekExterni(dm, new java.sql.Date(106,11,31));
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
