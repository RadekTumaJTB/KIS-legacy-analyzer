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

public class ESExportSpolecnostKartaSS extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSpolecnostKartaSS.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private Number idBoss;
  
  private int listNr = 0;
  private int rowNr = 0;
  
  private CellStyle green,red,lightBlue;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private SimpleDateFormat sdfHeader = new SimpleDateFormat("yyyy-MM-dd");
  
  private boolean hasData = false;
  
  public ESExportSpolecnostKartaSS(ApplicationModule dokladyModule, Number idBoss)
  {
    dm = dokladyModule;
    this.idBoss = idBoss;
    init();
  }

  private void init() {
    logger.info("ExportSpolecnostKartaSS");  

    setFileName ( "SpolecnostKartaSS_"+sdfHeader.format(new java.util.Date())+".xlsx" );
    setFileRelativeName( Constants.DIR_PROJEKTY_KATRA+(idBoss==null?"\\kontr":"\\"+idBoss)+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputCast(String where) 
  {
    Number oldId = null;
    int colNr = 0;
    
    setCellValue( listNr, rowNr, colNr++, "Spolenost" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "Zprva" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "00" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "Datum" , lightBlue );
    
    rowNr++;
  
    ViewObject vo = dm.findViewObject("VwRelSpolecnostKartaTypView1");
    vo.clearCache();
    vo.setWhereClause((idBoss!=null?"ID_MNGSEGMENTBOSS = "+idBoss+" AND ":"")+where);
    vo.setOrderByClause("S_NAZEV,S_POPIS");
    if(vo.hasNext()) hasData = true;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      colNr = 0;

      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Spolecnost") , null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("KartaTyp") , null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Odpovednaosoba") , null );
      colNr++;
      oracle.jbo.domain.Date datum = (oracle.jbo.domain.Date) row.getAttribute("DtZpravadalsi");
      String dt = sdf.format(datum.dateValue());
      setCellValue( listNr, rowNr, colNr, dt , null );
      
      rowNr++;
    }
    vo.closeRowSet();
    
    rowNr+=2;
  }

  private void outputDetail()
  {
    int colNr = 0;

    wb.setSheetName(listNr, "Projekt memorandum");

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*60));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));

    setCellValue( listNr, rowNr, 0, "Po datumu zadn" , red );
    for(int i=1; i<4; i++) setCellValue( listNr, rowNr, i, (String)null , red );
    rowNr++;
    //setCellValue( listNr, rowNr, 0, "Seznam spolenost, jejich doklady Vmi zatm nebyly schvleny, akoliv u schvleny bt mly, a seznam datum doklad, k nim schvlen chyb." , orange );
    outputCast( "(TRUNC (SYSDATE) - dt_zpravaDalsi) > 0" );

    setCellValue( listNr, rowNr, 0, "V nejblich 5ti dnech vypr datum zadn" , green );
    for(int i=1; i<4; i++) setCellValue( listNr, rowNr, i, (String)null , green );
    rowNr++;
    //setCellValue( listNr, rowNr, 0, "Seznam spolenost, jejich doklady Vmi zatm nebyly schvleny a v nejblich dnech by schvleny bt mly. Zatm nevyprel termn schvlen, ale brzy vypr. Tak je zde seznam datum doklad, k nim bude schvlen poteba." , orange );
    outputCast( "(TRUNC (SYSDATE) - dt_zpravaDalsi) BETWEEN -5 AND 0");

    dm.getTransaction().commit();
  }

  protected boolean outputData () 
  {
    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 14);
    font.setBold(true);

    green = wb.createCellStyle();
    green.setFillForegroundColor(IndexedColors.GREEN.getIndex());
    green.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    green.setFont(font);

    red = wb.createCellStyle();
    red.setFillForegroundColor(IndexedColors.RED.getIndex());
    red.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    red.setFont(font);
    
    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    outputDetail();
    
    return hasData;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.ucskup.UcSkupModule","UcSkupModuleLocal");
      ESExportSpolecnostKartaSS ed = new ESExportSpolecnostKartaSS(dm, new Number(3)/*null*/);
      ed.excelOutput();
      System.out.println(ed.getFileName());

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
