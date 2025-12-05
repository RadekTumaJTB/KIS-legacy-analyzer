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

public class ESExportChybyMustku extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportChybyMustku.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private int idCisDoklad;
  private String seznamDokladu;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private boolean empty = true;

  private CellStyle lightBlue;

  public ESExportChybyMustku(ApplicationModule dokladyModule, int idCisDoklad)
  {
    dm = dokladyModule;
    this.idCisDoklad = idCisDoklad;
    init();
  }

  public ESExportChybyMustku(ApplicationModule dokladyModule, String seznamDokladu)
  {
    dm = dokladyModule;
    this.seznamDokladu = seznamDokladu;
    init();
  }

  private void init() {
    logger.info("ExportChybyMustku");  

    setFileName ( "BilanceChybyMustku"+(seznamDokladu==null?""+idCisDoklad:"2007")+".xlsx" );
    setFileRelativeName( Constants.DIR_CHYBY_MUSTKU+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputSuma()
  {
    int listNr = 0;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "ID spol." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*70));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum gen." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*12));
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    if(seznamDokladu!=null) 
    {
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
      setCellValue( listNr, rowNr, colNr, "ID cis. doklad" , lightBlue ); colNr++;
    }
    
    vo = dm.findViewObject("VwKpDokladautoChybamustkuSumView1");
    vo.clearCache();
    if(seznamDokladu==null) vo.setWhereClause("ID_CISDOKLAD = "+idCisDoklad);
    else vo.setWhereClause("ID_CISDOKLAD IN ("+seznamDokladu+")");
    if( vo.hasNext() ) empty = false;
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
      if(seznamDokladu!=null) setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdCisdoklad"), null );
    }
    if ( pocet <= 0 ) {
      setCellValue( listNr, rowNr+1, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( listNr, rowNr+2, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputDetail()
  {
    int listNr = 1;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    //setCellValue( listNr, 0, 0, "Detail prozatimne vypnut kvuli mnozstvi dat pri zavadeni unifikovanych uctu..." , null );
    //if(true) return;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "ID spol." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*70));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum gen." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*12));
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka local" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Doklad" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "ID intern" , lightBlue ); colNr++;
    
    vo = dm.findViewObject("VwKpDokladautoChybamustkuView1");
    vo.clearCache();
    if(seznamDokladu==null) vo.setWhereClause("ID_CISDOKLAD = "+idCisDoklad);
    else vo.setWhereClause("ID_CISDOKLAD IN ("+seznamDokladu+")");
    if( vo.hasNext() ) empty = false;
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
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkamena")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkalocal")).doubleValue(), null );
      colNr++;
      date = ((Date)row.getAttribute("Datum")).dateValue();
      datum = ( date == null ) ? "" : sdf.format(date);
      setCellValue( listNr, rowNr, colNr, datum, null );               
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdDoklad"), null );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdInterni"), null );                        
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
    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.cloneSheet(0);
    wb.setSheetName(0,"Suma");
    outputSuma();
    wb.setSheetName(1,"Detail");
    outputDetail();
    
    return !empty;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportChybyMustku ed = new ESExportChybyMustku(dm,"100,101,102,103,104,105,106");
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
