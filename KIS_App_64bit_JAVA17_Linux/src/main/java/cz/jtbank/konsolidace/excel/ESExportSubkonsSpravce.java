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

public class ESExportSubkonsSpravce extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsSpravce.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;

  private java.sql.Date datum;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private CellStyle lightBlue, styleBold;

  private int listNr = 0;
  private int rowNr=0;
  private int colNr = 0;

  public ESExportSubkonsSpravce(ApplicationModule subModule,
                               java.sql.Date datum)
  {
    dm = subModule;
    this.datum = datum;
    init();
  }

  private void init() {
    logger.info("ExportSubkonsSpravce:datum="+datum);  

    setFileName ( "SubkonsSpravce_"+datum+".xlsx" );
    setFileRelativeName( Constants.DIR_KONS_SPRAVCE+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputListy()
  {
    CellStyle style = null;

    colNr=0;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Id" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*60));
    setCellValue( listNr, rowNr, colNr, "Nzev" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "I" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Pevodov mstek" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna etnictv" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Rezidentstv" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Kons. od" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Kons. do" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Sprvce pro konsolidaci" , styleBold ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "e-mail" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "telefon" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Ext. etn/kontaktn osoba za spolenost" , styleBold ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "e-mail" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "telefon" , lightBlue ); colNr++;
//esc 03.2010
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Odpovedna Osoba" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Segment" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Sef Segmentu" , lightBlue ); colNr++;    
    
    ViewObject vo = dm.findViewObject("KpKtgUcetniskupinaView1");
    vo.clearCache();
    //vo.setWhereClause("ID < 3");
    while(vo.hasNext()) 
    {
      vo.next();
      if(vo.hasNext()) wb.cloneSheet(0);
    }
    vo.closeRowSet();

    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String nazev = (String) row.getAttribute("SKod");
      Number idSkup = (Number) row.getAttribute("Id");

      wb.setSheetName(listNr,nazev);
      
      outputSkup(idSkup);
      
      listNr++;
    }
    vo.closeRowSet();
  }
  
  private void outputSkup(Number idSkup) {
    rowNr = 1;
    ViewObject vo = dm.findViewObject("VwKpUcspolspravcekonsolidaceView1");
    vo.clearCache();
    String strDatum = "TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')";
    String where = "ID_KTGUCETNISKUPINA = "+idSkup+
             " AND ("+strDatum+" BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO"+
               " OR DT_PLATNOSTDO between trunc("+strDatum+",'yyyy') and trunc(add_months("+strDatum+",12),'yyyy')-1)";
    for(int i=1; i<4; i+=2) {
      if(i==1) setCellValue( listNr, rowNr++, 0, "PLN" , styleBold );
      else if(i==3) setCellValue( listNr, rowNr++, 0, "EQUITY" , styleBold );
      vo.setWhereClause(where+" AND ID_CISSUBTYPCLENSTVI="+i);
      while ( vo.hasNext() ) {
        Row row = vo.next();
        colNr = 0;
        
        setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SIco") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Subject") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SCountry") , null );
        colNr++;
        oracle.jbo.domain.Date hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostod");
        String dtOd = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostdo");
        String dtDo = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, dtOd , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, dtDo , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spravcekonsolidace") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SkEmail") , null );
        colNr++;
        String telefon = (String)row.getAttribute("SkTelefon");
        if(telefon==null);
        else if(telefon.startsWith("881")) telefon=telefon.replaceFirst("881","00420 221 710 ");
        else if(telefon.startsWith("883")) telefon=telefon.replaceFirst("883","00421 259 418 ");
        setCellValue( listNr, rowNr, colNr, telefon , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Externiucetni") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("EuEmail") , null );
        colNr++;
        telefon = (String)row.getAttribute("EuTelefon");
        if(telefon==null);
        else if(telefon.startsWith("881")) telefon=telefon.replaceFirst("881","00420 221 710 ");
        else if(telefon.startsWith("883")) telefon=telefon.replaceFirst("883","00421 259 418 ");
        setCellValue( listNr, rowNr, colNr, telefon , null );
        colNr++;
//esc 03.2010
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("OdpovednaOsoba") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("MngSegment") , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SefSegmentu") , null );
        colNr++;
  
        rowNr++;
      }
      vo.closeRowSet();
    }
  }

  protected boolean outputData () 
  {
    if(lightBlue==null) {
      lightBlue = wb.createCellStyle();
      lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    if(styleBold==null) {
      Font font = wb.createFont();
      //font.setFontHeightInPoints((short) 14);
      font.setBold(true);
      styleBold = wb.createCellStyle();
      styleBold.setFont(font);
      styleBold.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      styleBold.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    outputListy();

    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.subkons.SubkonsModule","SubkonsModuleLocal");
      ESExportSubkonsSpravce ed = new ESExportSubkonsSpravce(dm,
                                                             new java.sql.Date(System.currentTimeMillis()));
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
