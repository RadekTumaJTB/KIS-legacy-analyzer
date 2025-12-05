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

public class ESExportProjekty extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProjekty.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  public ESExportProjekty(ApplicationModule dokladyModule,
                             java.sql.Date datum)
  {
    logger.info("ESExportProjekty:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_PROJEKTY;
    init();
  }

  private void init() {
    setFileName ( "Projekty_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputProjekty() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    int listNr=0;
    int rowNr=0;
    int colNr=0;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*35));
    setCellValue( listNr, rowNr, colNr, "Nzev" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "ID" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "Star ID" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*18));
    setCellValue( listNr, rowNr, colNr, "Status" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Zmna statusu" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Navrhuje" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "f segmentu" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Sponzor" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "TopMng" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Projekt manager" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "Kategorie" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "Holding" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Subjekt" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna projektu" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Pecenn - start" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Pecenn - frekvence" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Pecenn - dal" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "IM - dal" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "IM - msc" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "IM - splnno" , lightBlue ); colNr++;

    rowNr++;

    String id = "";
    String dtZmenaStat = "";
    double naklady = 0.0;
    double investice = 0.0;
    double oceneni = 0.0;
    String dtStartOceneni = "";
    String dtKonecOceneni = "";
    String dtMDalsi = "";
    int mMesicu = 0;
    String mSplneno = "";
  
    ViewObject vo = dm.findViewObject("VwKtgProjektView1");
    vo.clearCache();
    vo.setWhereClause("");
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      id = row.getAttribute("Id")==null?"":((oracle.jbo.domain.Number)row.getAttribute("Id")).toString();
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumstatuszmena");
      dtZmenaStat = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      Number hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastkapreceneni");
      oceneni = hlpNum==null ? 0.0 : hlpNum.doubleValue();
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtStartoceneni");
      dtStartOceneni = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      if(dtStartOceneni==null) dtStartOceneni="";
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtKonecoceneni");
      dtKonecOceneni = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      if(dtKonecOceneni==null) dtKonecOceneni="";
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtMemorandumdalsi");
      dtMDalsi = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      if(dtMDalsi==null) dtMDalsi="";
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NlMemorandummesicu");
      mMesicu = hlpNum==null ? 0 : hlpNum.intValue();
      mSplneno = "1".equals(row.getAttribute("CMemorandumsplneno")) ? "ANO" : "NE";

      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, id , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SCisloold") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Status") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtZmenaStat , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Navrhuje") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegmentboss") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Sponzor") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Top") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Pmanager") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Kategorie") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegment") , null ); 
      colNr++;
      String spol = (String)row.getAttribute("UcspolExport");
      setCellValue( listNr, rowNr, colNr, spol , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMenanaklady") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtStartOceneni , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Frekvence") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtKonecOceneni , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtMDalsi , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, mMesicu==0 ? "" : ""+mMesicu , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, mSplneno , null ); 
      colNr++;

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
      logger.debug("projekty:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.projekt.ProjektModule","ProjektModuleLocal");
      ESExportProjekty ed = new ESExportProjekty(dm,
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
