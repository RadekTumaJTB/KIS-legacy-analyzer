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

public class ESExportOdbor extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportOdbor.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportOdbor(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    dm = dokladyModule;
    this.datum = datum;
    init();
  }

  private void init() {
    logger.info("ExportOdbor:datum="+datum);  

    setFileName ( "Odbory_"+datum+".xlsx" );
    setFileRelativeName( Constants.DIR_ODBORY+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputDetail()
  {
    int listNr = 0;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Nzev" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Kd" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Odpovdn osoba" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Od" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Do" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "sek" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Holding" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "rove 1" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Budgeting" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Archivn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Sponzor" , lightBlue ); colNr++;
    
    vo = dm.findViewObject("VwKtgOdborView1");
    vo.clearCache();
    vo.setWhereClause("");
    CellStyle style = null;
    
    while ( vo.hasNext() ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id"), style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SKod"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Odpovednaosoba"), null );
      colNr++;
      oracle.jbo.domain.Date hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostod");
      String dt = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
      setCellValue( listNr, rowNr, colNr, dt, null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostdo");
      dt = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
      setCellValue( listNr, rowNr, colNr, dt, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Usek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegment"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdDocuroven1")).doubleValue(), null );
      colNr++;
      String bud = "1".equals(row.getAttribute("CBudgeting")) ? "ANO" : "NE";
      String arch = "1".equals(row.getAttribute("CArchivni")) ? "ANO" : "NE";
      setCellValue( listNr, rowNr, colNr, bud, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, arch, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Sponzor"), null );
      colNr++;
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
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModule","DokumentModuleLocal");
      ESExportOdbor ed = new ESExportOdbor(dm, new java.sql.Date(System.currentTimeMillis()));
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
