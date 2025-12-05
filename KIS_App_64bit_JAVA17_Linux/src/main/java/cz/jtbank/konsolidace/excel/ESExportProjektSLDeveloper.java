package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportProjektSLDeveloper extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProjektSLDeveloper.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  private int idProjekt;
  private String projektName;

  private String dir;

  private CellStyle styleBold;

  public ESExportProjektSLDeveloper(ApplicationModule dokladyModule,
                                 int idProjekt,
                                 java.sql.Date datum)
  {
    logger.info("ESExportProjektSLDeveloper:idProjekt="+idProjekt+",datum="+datum);  
    dm = dokladyModule;
    this.idProjekt = idProjekt;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_PROJEKTY_SLDEV+"\\"+idProjekt;
    init();
  }

  private void init() {
    setFileName ( "ProjektSLDeveloper_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );

    ViewObject vo = dm.findViewObject("KpKtgProjektView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idProjekt);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      projektName = (String)row.getAttribute("SNazev");
    }
    vo.closeRowSet();
  }
  
  private void outputProjekty() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    int listNr=0;
    int rowNr=0;
    int colNr=0;

    Font font = wb.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short)16);
    styleBold = wb.createCellStyle();
    styleBold.setFont(font);

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    setCellValue( listNr, rowNr, colNr, "Development SL: "+idProjekt+" / "+projektName , styleBold );
    rowNr+=2;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum zadn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Typ transakce" , lightBlue ); colNr++;
    
    rowNr++;

    double castka = 0.0;
    String datum = "";

    ViewObject vo = dm.findViewObject("VwDatSlprojektdeveloperView1");
    vo.clearCache();
    vo.setWhereClause("ID_PROJEKT = "+idProjekt);
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumzadani");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastka");
      castka = hlpNum==null ? 0.0 : hlpNum.doubleValue();

      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdDokument") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castka , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, datum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Typtransakce") , null ); 

      rowNr++;
    }
    vo.closeRowSet();
    
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputProjekty();
      end = System.currentTimeMillis();    
      logger.debug("projektCF/"+idProjekt+":"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.projekt.ProjektModule","ProjektModuleLocal");
      ESExportProjektSLDeveloper ed = new ESExportProjektSLDeveloper(dm,
                                                 1001932,
                                                 new java.sql.Date(106,5,30));
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
