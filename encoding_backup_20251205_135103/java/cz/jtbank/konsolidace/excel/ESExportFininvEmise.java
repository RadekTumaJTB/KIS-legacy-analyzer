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

public class ESExportFininvEmise extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportFininvEmise.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportFininvEmise(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    dm = dokladyModule;
    this.datum = datum;
    init();
  }

  private void init() {
    logger.info("ExportFininvEmise:datum="+datum);  

    setFileName ( "Emise_"+datum+".xlsx" );
    setFileRelativeName( Constants.DIR_EMISE+"\\"+getFileName() );
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

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Emitujc spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "I" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Rezident" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "ISIN" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Od" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Do" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Nenul." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Kus" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Nominl" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Objem" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Finann inv." , lightBlue ); colNr++;
    
    vo = dm.findViewObject("VwFininvemiseExportView1");
    vo.clearCache();
    vo.setWhereClause("TO_DATE('"+sdf.format(datum)+"','DD.MM.YYYY') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    CellStyle style = null;
    
    while ( vo.hasNext() ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SIco"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SCountry"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SIsin"), null );
      colNr++;
      oracle.jbo.domain.Date hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostod");
      String dt = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
      setCellValue( listNr, rowNr, colNr, dt, null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostdo");
      dt = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
      setCellValue( listNr, rowNr, colNr, dt, null );
      colNr++;
      String nenul = "1".equals(row.getAttribute("CNenulovy")) ? "ANO" : "NE";
      setCellValue( listNr, rowNr, colNr, nenul, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NlPocetkusu")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NlNominal")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdZakladnijmeni")).doubleValue(), null );
      colNr++;
      String fininv = "F".equals(row.getAttribute("CInvestice")) ? "ANO" : "NE";
      setCellValue( listNr, rowNr, colNr, fininv, null );
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
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.fininv.FininvModule","FininvModuleLocal");
      ESExportFininvEmise ed = new ESExportFininvEmise(dm, new java.sql.Date(System.currentTimeMillis()));
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
