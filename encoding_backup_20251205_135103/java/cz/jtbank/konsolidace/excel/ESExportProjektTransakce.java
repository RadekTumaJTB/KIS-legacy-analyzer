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

public class ESExportProjektTransakce extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProjektTransakce.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  private int idProjekt;
  private String projektName;

  private String dir;

  private CellStyle styleBold;

  public ESExportProjektTransakce(ApplicationModule dokladyModule,
                                 int idProjekt,
                                 java.sql.Date datum)
  {
    logger.info("ESExportProjektTransakce:idProjekt="+idProjekt+",datum="+datum);  
    dm = dokladyModule;
    this.idProjekt = idProjekt;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_PROJEKTY_TRANSAKCE+"\\"+idProjekt;
    init();
  }

  private void init() {
    setFileName ( "ProjektTran_"+datum+".xlsx" );
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

    wb.cloneSheet(0);
    wb.setSheetName(0,"MIS do 2004");
    wb.setSheetName(1,"MIS od 2005");

    int listNr=0;
    int rowNr=0;
    int colNr=0;

    if(styleBold == null) 
    {
      Font font = wb.createFont();
      font.setBold(true);
      font.setFontHeightInPoints((short)16);
      styleBold = wb.createCellStyle();
      styleBold.setFont(font);
    }
    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    setCellValue( listNr, rowNr, colNr, "NKLADY PROJEKTU: "+projektName , styleBold );
    rowNr+=2;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));

    colNr=0;
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Zdroj" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Dodavatel" , lightBlue ); colNr++;
    
    rowNr++;

    double castka = 0.0;
    String datum = "";

    ViewObject vo = dm.findViewObject("KpDatProjektnakladydetailView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGPROJEKT = "+idProjekt);
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastka");
      castka = hlpNum==null ? 0.0 : hlpNum.doubleValue();

      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castka , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("STyp") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, datum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SSpolecnost") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SDodavatele") , null ); 
      colNr++;

      rowNr++;
    }
    vo.closeRowSet();

    listNr=1;
    rowNr=0;
    colNr=0;
    
    setCellValue( listNr, rowNr, colNr, "TRANSAKCE PROJEKTU: "+projektName , styleBold );
    rowNr+=2;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*12));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));

    colNr=0;
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "stka v mn" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "stka local" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
    
    rowNr++;

    double castkaMena = 0.0, castkaLocal = 0.0;

    vo = dm.findViewObject("VwKpProjekttransakceView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGPROJEKT = "+idProjekt);
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("Datum");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastkamena");
      castkaMena = hlpNum==null ? 0.0 : hlpNum.doubleValue();
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastkalocal");
      castkaLocal = hlpNum==null ? 0.0 : hlpNum.doubleValue();

      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castkaMena , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castkaLocal , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, datum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUnifspol") , null ); 
      colNr++;
      String lhlp = row.getAttribute("Xidsl")==null ? "" : ((Number)row.getAttribute("Xidsl")).toString();
      setCellValue( listNr, rowNr, colNr, lhlp, null );
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
      logger.debug("projektCF/"+idProjekt+":"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.projekt.ProjektModule","ProjektModuleLocal");
      ESExportProjektTransakce ed = new ESExportProjektTransakce(dm,
                                                 1000377,
                                                 new java.sql.Date(104,8,30));
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
