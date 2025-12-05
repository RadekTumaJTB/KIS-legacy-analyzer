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

public class ESExportProjektKartaSS extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProjektKartaSS.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private Number idBoss;
  
  private int listNr = 0;
  private int rowNr = 0;
  
  private CellStyle green,red,lightBlue;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private SimpleDateFormat sdfHeader = new SimpleDateFormat("yyyy-MM-dd");
  
  private boolean hasData = false;
  
  public ESExportProjektKartaSS(ApplicationModule dokladyModule, Number idBoss)
  {
    dm = dokladyModule;
    this.idBoss = idBoss;
    init();
  }

  private void init() {
    logger.info("ExportProjektKartaSS");  

    setFileName ( "ProjektKartaSS_"+sdfHeader.format(new java.util.Date())+".xlsx" );
    setFileRelativeName( Constants.DIR_PROJEKTY_KATRA+(idBoss==null?"\\kontr":"\\"+idBoss)+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputCast(String where) 
  {
    Number oldId = null;
    int colNr = 0;
    
    setCellValue( listNr, rowNr, colNr++, "ID" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "Projekt" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "Projekt manager" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "Datum" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "Status" , lightBlue );
    setCellValue( listNr, rowNr, colNr++, "Holding" , lightBlue );
    
    rowNr++;
  
    ViewObject vo = dm.findViewObject("VwKtgProjektsimpleView1");
    vo.clearCache();
    vo.setWhereClause((idBoss!=null?"ID_MNGSEGMENTBOSS = "+idBoss+" AND ":"")+"ID_STATUS NOT IN (5,6,7) AND "+where);
    vo.setOrderByClause("S_NAZEV");
    if(vo.hasNext()) hasData = true;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      colNr = 0;

      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id") , null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("SNazev") , null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Pmanager") , null );
      colNr++;
      oracle.jbo.domain.Date datum = (oracle.jbo.domain.Date) row.getAttribute("DtMemorandumdalsi");
      String dt = sdf.format(datum.dateValue());
      setCellValue( listNr, rowNr, colNr, dt , null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Status") , null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Mngsegment") , null );
      colNr++;
      
      rowNr++;
    }
    vo.closeRowSet();
    
    rowNr+=2;
  }

  private void outputDetail()
  {
    int colNr = 0;

    wb.setSheetName(listNr, "Projekt memorandum");

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));

    setCellValue( listNr, rowNr, 0, "Po datumu zadn" , red );
    for(int i=1; i<6; i++) setCellValue( listNr, rowNr, i, (String)null , red );
    rowNr++;
    //setCellValue( listNr, rowNr, 0, "Seznam spolenost, jejich doklady Vmi zatm nebyly schvleny, akoliv u schvleny bt mly, a seznam datum doklad, k nim schvlen chyb." , orange );
    outputCast( "NL_DNYDOMEMORANDA > 0" );

    setCellValue( listNr, rowNr, 0, "V nejblich 5ti dnech vypr datum zadn" , green );
    for(int i=1; i<6; i++) setCellValue( listNr, rowNr, i, (String)null , green );
    rowNr++;
    //setCellValue( listNr, rowNr, 0, "Seznam spolenost, jejich doklady Vmi zatm nebyly schvleny a v nejblich dnech by schvleny bt mly. Zatm nevyprel termn schvlen, ale brzy vypr. Tak je zde seznam datum doklad, k nim bude schvlen poteba." , orange );
    outputCast( "NL_DNYDOMEMORANDA BETWEEN -5 AND 0");

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
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.projekt.ProjektModule","ProjektModuleLocal");
/*
      ESExportProjektKartaSS ed = new ESExportProjektKartaSS(dm,
                                               "224720",
                                               "cs_CZ",
                                               0);
*/
      ESExportProjektKartaSS ed = new ESExportProjektKartaSS(dm, new Number(34)/*null*/);
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
