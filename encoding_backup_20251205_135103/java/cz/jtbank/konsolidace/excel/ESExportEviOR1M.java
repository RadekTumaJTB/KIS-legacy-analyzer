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

public class ESExportEviOR1M extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportEviOR1M.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private SimpleDateFormat sdfX = new SimpleDateFormat("yyyy-MM-dd");
  
  private CellStyle lightGray;
  
  private int listNr = 0;
  
  boolean empty = true;

  public ESExportEviOR1M(ApplicationModule dokladyModule)
  {
    dm = dokladyModule;
    init();
  }

  private void init() {
    logger.info("ExportOR1M");  

    setFileName ( "EviOR1MPrehled_"+sdfX.format(new java.util.Date())+".xlsx" );
    setFileRelativeName( Constants.DIR_EVI_OR + getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputDetail()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int rowNr=0;
    int colNr = 0;
    CellStyle style = null;
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));

    colNr=0;    
    setCellValue( listNr, rowNr, colNr++, "SPOLENOST" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "Stt" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "Ext. administrtor" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "Kompletn" , lightGray );
    
    ViewObject vo = dm.findViewObject("VwKpEviobchodnirejstrik3mView1");
    vo.clearCache();
    vo.setWhereClause("K2M = '1' AND S_STAT NOT IN ('CZ','SK')");
    while ( vo.hasNext() ) {
      empty = false;
      Row row = vo.next();
      rowNr++;
      colNr = 0;

      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Stat"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Externiadmin"), style );
      colNr++;
      Date dt = (Date) row.getAttribute("Kompletni");
      String dtKom = dt==null ? "-" : sdf.format(dt.dateValue());
      setCellValue( listNr, rowNr, colNr, dtKom, style );
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
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.evi.EviModule","EviModuleLocal");
      ESExportEviOR1M ed = new ESExportEviOR1M(dm);
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
