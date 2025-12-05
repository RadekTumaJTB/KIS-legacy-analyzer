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

public class ESExportKAPOperacniRiziko extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportKAPOperacniRiziko.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportKAPOperacniRiziko(ApplicationModule dokladyModule)
  {
    dm = dokladyModule;
    init();
  }

  private void init() {
    setFileName ( "KAPOperacniRiziko.xlsx" );
    setFileRelativeName( Constants.DIR_KAP_OR+"\\"+getFileName() );
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

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*12));
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*70));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "P/C" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Business linie" , lightBlue ); colNr++;

    vo = dm.findViewObject("TestOperacnirizikoView1");
    vo.clearCache();
    String whereDet = "";
    vo.setWhereClause(whereDet);
    CellStyle style = null;
    
    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;

      String ucet = (String) row.getAttribute("SUcet");
      Number ndCastka = (Number)row.getAttribute("NdCastka");
      String spolecnost = (String)row.getAttribute("Spolecnost");
      String pc = (String)row.getAttribute("CIndicatorPc");
      String bl = (String)row.getAttribute("KodBL");
        
      setCellValue( listNr, rowNr, colNr, ucet , style );                        
      colNr++;
      if(ndCastka!=null) setCellValue( listNr, rowNr, colNr, ndCastka.doubleValue() , style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, spolecnost , style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, pc , style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, bl , style );                        
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
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.kapital.KapitalModule","KapitalModuleLocal");
      ESExportKAPOperacniRiziko ed = new ESExportKAPOperacniRiziko(dm);
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
