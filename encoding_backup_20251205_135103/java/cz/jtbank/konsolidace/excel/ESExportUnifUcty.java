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

public class ESExportUnifUcty extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportUnifUcty.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private boolean empty = true;

  public ESExportUnifUcty(ApplicationModule dokladyModule)
  {
    dm = dokladyModule;
    init();
  }

  private void init() {
    logger.info("ExportUnifUcty");  

    setFileName ( "BilanceUnifUcty.xlsx" );
    setFileRelativeName( Constants.DIR_UNIFIKOVANE_UCTY+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputDetail()
  {
    int listNr = 0;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "ID spol." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*70));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum gen." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*12));
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "Doklad" , lightBlue ); colNr++;
    
    vo = dm.findViewObject("VwKpDokladautoUnifuctyView1");
    vo.clearCache();
    if(vo.hasNext()) empty = false;
    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdKtgucetnispolecnost"), null );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev"), null );
      colNr++;
      java.sql.Date date = ((Date)row.getAttribute("DtDatum")).dateValue();
      String datum = ( date == null ) ? "" : sdf.format(date);
      setCellValue( listNr, rowNr, colNr, datum, null );               
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdDoklad"), null );                        
      colNr++;
    }
    if ( pocet <= 0 ) {
      setCellValue( listNr, rowNr+1, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( listNr, rowNr+2, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  protected boolean outputData () 
  {
    outputDetail();
    
    return !empty;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportUnifUcty ed = new ESExportUnifUcty(dm);
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
