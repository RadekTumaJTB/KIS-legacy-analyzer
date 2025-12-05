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

public class ESExportEviOR3M extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportEviOR3M.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  
  private CellStyle lightGray;
  
  private int listNr = 0;

  public ESExportEviOR3M(ApplicationModule dokladyModule)
  {
    dm = dokladyModule;
    init();
  }

  private void init() {
    logger.info("ExportOR3M");  

    setFileName ( "EviOR3MPrehled.xlsx" );
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
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));

    colNr=0;    
    setCellValue( listNr, rowNr, colNr++, "SPOLENOST" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "Stt" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "Kompletn" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "V limitu" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "Akcion" , lightGray );
    setCellValue( listNr, rowNr, colNr++, "V limitu" , lightGray );
    
    ViewObject vo = dm.findViewObject("VwKpEviobchodnirejstrik3mView1");
    vo.clearCache();
    while ( vo.hasNext() ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;

      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Stat"), style );
      colNr++;
      Date dt = (Date) row.getAttribute("Kompletni");
      String dtKom = dt==null ? "-" : sdf.format(dt.dateValue());
      dt = (Date) row.getAttribute("Akcionar");
      String dtAkc = dt==null ? "-" : sdf.format(dt.dateValue());
      setCellValue( listNr, rowNr, colNr, dtKom, style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("K3m"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtAkc, style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("A3m"), style );
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
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.evi.EviModule","EviModuleLocal");
      ESExportEviOR3M ed = new ESExportEviOR3M(dm);
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
