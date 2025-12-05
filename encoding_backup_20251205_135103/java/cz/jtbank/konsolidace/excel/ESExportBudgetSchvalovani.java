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

public class ESExportBudgetSchvalovani extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportBudgetSchvalovani.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportBudgetSchvalovani(ApplicationModule dokladyModule)
  {
    dm = dokladyModule;
    init();
  }

  private void init() {
    logger.info("ExportBudgetSchvalovani");  

    setFileName ( "BudgetSchvalovani.xlsx" );
    setFileRelativeName( Constants.DIR_BUDGET_SCHVALOVANI+"\\"+getFileName() );
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

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "rove" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*100));
    setCellValue( listNr, rowNr, colNr, "Budget" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Osoba" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "Poet" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "M u schvalovat?" , lightBlue ); colNr++;
    
    vo = dm.findViewObject("VwDatBudgetschvalovaniView1");
    vo.clearCache();
    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      String uroven = row.getAttribute("Uroven").toString();
      String us = null;
      if("1".equals(uroven)) us="OO odbor";
      else if("2".equals(uroven)) us="Gestor";
      else if("3".equals(uroven)) us="OO sek/spol.";
      else us="f seg.";
      setCellValue( listNr, rowNr, colNr, us, null );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Budget"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Osoba"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Pocet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, "1".equals(row.getAttribute("Nizsi")) ? "ANO" : "NE", null );
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
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportBudgetSchvalovani ed = new ESExportBudgetSchvalovani(dm);
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
