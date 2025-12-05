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

public class ESExportBudgetPrekroceni extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportBudgetPrekroceni.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private Set set;
  private Number user;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportBudgetPrekroceni(Set set, Number user)
  {
    this.set = set;
    this.user = user;
    init();
  }

  private void init() {
    logger.info("ExportBudgetPrekroceni");  

    setFileName ( "BudgetPrekroceni.xlsx" );
    setFileRelativeName( Constants.DIR_BUDGET_PREKROCENI+"\\"+user+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputDetail()
  {
    int listNr = 0;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=0;
    int colNr = 0;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*90));
    setCellValue( listNr, rowNr, colNr, "Budget" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*60));
    setCellValue( listNr, rowNr, colNr, "Typ transakce" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*18));
    setCellValue( listNr, rowNr, colNr, "Schvleno" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*18));
    setCellValue( listNr, rowNr, colNr, "Vyerpno" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*18));
    setCellValue( listNr, rowNr, colNr, "Vyerpno + SL" , lightBlue ); colNr++;
 
    Iterator iter = set.iterator();
    while(iter.hasNext()) 
    {
      Object[] arr = (Object[]) iter.next();
      String budget = (String)arr[0];
      String tt = (String)arr[1];
      double c = arr[2]==null ? 0 : ((Number)arr[2]).doubleValue();
      double cZd = arr[3]==null ? 0 : ((Number)arr[3]).doubleValue();
      double cSch = arr[4]==null ? 0 : ((Number)arr[4]).doubleValue();
      rowNr++;
      colNr=0;
      
      setCellValue( listNr, rowNr, colNr, budget, null );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, tt, null );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, c, null );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, cZd, null );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, cZd+cSch, null );                        
      colNr++;
    }
  }

  protected boolean outputData () 
  {
    outputDetail();
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
    /*
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportBudgetPrekroceni ed = new ESExportBudgetPrekroceni(dm);
      ed.excelOutput();

      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=ed.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
*/      
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
}
