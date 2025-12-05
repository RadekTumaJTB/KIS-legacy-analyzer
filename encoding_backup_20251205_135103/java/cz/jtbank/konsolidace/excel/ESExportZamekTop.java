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

public class ESExportZamekTop extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportZamekTop.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private Number idTop;
  private List data;
  private List dataRows;
  
  private int listNr = 0;
  private int rowNr = 0;
  
  private boolean empty = true;
  
  private CellStyle green,yellow,red,orange;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");//"dd.MM.yyyy, hh:mm:ss");
  private SimpleDateFormat sdfHeader = new SimpleDateFormat("yyyy-MM-dd");
  
  public ESExportZamekTop(ApplicationModule dokladyModule, Number idTop)
  {
    dm = dokladyModule;
    this.idTop = idTop;
    init();
  }

  private void init() {
    logger.info("ExportZamekTop");  

    setFileName ( "PrehledZamekSS_"+sdfHeader.format(new java.util.Date())+".xlsx" );
    setFileRelativeName( Constants.DIR_ZAMEK_GENEROVANI+"\\"+idTop+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputCast(String where) 
  {
    Number oldId = null;
    int colNr;
  
    ViewObject vo = dm.findViewObject("VwKpSpolecnostzamekgenView1");
    vo.clearCache();
    vo.setWhereClause("ID_TOPMNG = "+idTop+" AND SCHVALENOTOP is null AND c_online = '1' AND "+where);
    if(vo.hasNext()) empty = false;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      colNr = 0;

      String spolecnost = (String) row.getAttribute("Spolecnost");
      Number id = (Number) row.getAttribute("Id");
      if(!id.equals(oldId)) 
      {
        oldId = id;
        rowNr++;
        setCellValue( listNr, rowNr, colNr, spolecnost , null );
      }
      
      oracle.jbo.domain.Date datum = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      String dt = sdf.format(datum.dateValue());
      
      colNr = 1+data.indexOf(dt);
      dataRows.set(data.indexOf(dt),Boolean.TRUE);
      setCellValue( listNr, rowNr, colNr, dt , null );

    }
    vo.closeRowSet();
    
    rowNr+=2;
  }

  private void outputDetail()
  {
    int colNr = 0;

    wb.setSheetName(listNr, "Schvalovani dokladu");

    wb.getSheetAt(listNr).setColumnWidth((short)0,(short)(256*50));

    data = new ArrayList();
    dataRows = new ArrayList();
    ViewObject voDt = dm.findViewObject("VwKpSpolecnostzamekgendatumView1");
    voDt.clearCache();
    voDt.setWhereClause("");
    while(voDt.hasNext()) 
    {
      Row row = voDt.next();
      String dt = sdf.format(((oracle.jbo.domain.Date)row.getAttribute("DtDatum")).dateValue());
      data.add(dt);
      dataRows.add(Boolean.FALSE);
    }
    voDt.closeRowSet();
    
    for(int i=1; i<=data.size(); i++) wb.getSheetAt(listNr).setColumnWidth((short)i,(short)(256*11));

    setCellValue( listNr, rowNr, 0, "Po datumu schvlen - mte schvalovat" , red );
    for(int i=1; i<=data.size(); i++) setCellValue( listNr, rowNr, i, (String)null , red );
    rowNr++;
    setCellValue( listNr, rowNr, 0, "Seznam spolenost, jejich doklady Vmi zatm nebyly schvleny, akoliv u schvleny bt mly, a seznam datum doklad, k nim schvlen chyb." , orange );
    outputCast( "ROZDILTOP > 0 AND (ZODPOVEDNAUCETNI IS NULL OR SCHVALENOUCETNI IS NOT NULL)"+
                             " AND (ODPOVEDNAOSOBA IS NULL OR SCHVALENOOO IS NOT NULL)"+
                             " AND ZAMITNUTOUCETNI IS NULL AND ZAMITNUTOOO IS NULL AND ZAMITNUTOTOP IS NULL");

    setCellValue( listNr, rowNr, 0, "Po datumu schvlen - nemuste schvalovat (doposud neschvlila etn (e nadzen etn) nebo OO (e kontroling))" , yellow );
    for(int i=1; i<=data.size(); i++) setCellValue( listNr, rowNr, i, (String)null , yellow );
    rowNr++;
    setCellValue( listNr, rowNr, 0, "Seznam spolenost, jejich doklady nebyly doposud schvleny etnmi nebo OO ani Vmi. Zatm je schvalovat nemuste, to bude poteba a po schvlen tchto doklad etn i OO. Tak je zde seznam datum doklad, k nim schvlen chyb." , orange );
    outputCast( "ROZDILTOP > 0 AND (ZODPOVEDNAUCETNI IS NOT NULL AND SCHVALENOUCETNI IS NULL"+
                             " OR ODPOVEDNAOSOBA IS NOT NULL AND SCHVALENOOO IS NULL)"+
                             " AND ZAMITNUTOUCETNI IS NULL AND ZAMITNUTOOO IS NULL AND ZAMITNUTOTOP IS NULL");

    setCellValue( listNr, rowNr, 0, "V nejblich 5ti dnech vypr datum schvlen - mte schvalovat" , green );
    for(int i=1; i<=data.size(); i++) setCellValue( listNr, rowNr, i, (String)null , green );
    rowNr++;
    setCellValue( listNr, rowNr, 0, "Seznam spolenost, jejich doklady Vmi zatm nebyly schvleny a v nejblich dnech by schvleny bt mly. Zatm nevyprel termn schvlen, ale brzy vypr. Tak je zde seznam datum doklad, k nim bude schvlen poteba." , orange );
    outputCast( "ROZDILTOP BETWEEN -5 AND 0 AND (ZODPOVEDNAUCETNI IS NULL OR SCHVALENOUCETNI IS NOT NULL)"+
                             " AND (ODPOVEDNAOSOBA IS NULL OR SCHVALENOOO IS NOT NULL)"+
                             " AND ZAMITNUTOUCETNI IS NULL AND ZAMITNUTOOO IS NULL AND ZAMITNUTOTOP IS NULL");

    short i=1;
    for(Iterator iter=dataRows.iterator(); iter.hasNext(); i++) 
    {
      Boolean b = (Boolean) iter.next();
      if(!b.booleanValue()) wb.getSheetAt(listNr).setColumnWidth(i,(short)(0));
    }

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

    yellow = wb.createCellStyle();
    yellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    yellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    yellow.setFont(font);

    red = wb.createCellStyle();
    red.setFillForegroundColor(IndexedColors.RED.getIndex());
    red.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    red.setFont(font);

    Font font2 = wb.createFont();
    font2.setFontHeightInPoints((short) 8);
    font2.setBold(true);
    font2.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
    orange = wb.createCellStyle();
    orange.setFont(font2);

    outputDetail();
    
    return !empty;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
/*
      ESExportZamekTop ed = new ESExportZamekTop(dm,
                                               "224720",
                                               "cs_CZ",
                                               0);
*/
      ESExportZamekTop ed = new ESExportZamekTop(dm, new Number(98));
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
