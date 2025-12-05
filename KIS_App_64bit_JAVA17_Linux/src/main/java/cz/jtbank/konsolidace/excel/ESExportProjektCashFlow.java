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

public class ESExportProjektCashFlow extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProjektCashFlow.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  private int idProjekt;
  private String projektName;

  private String dir;

  private CellStyle styleBold;

  public ESExportProjektCashFlow(ApplicationModule dokladyModule,
                                 int idProjekt,
                                 java.sql.Date datum)
  {
    logger.info("ESExportProjektCashFlow:idProjekt="+idProjekt+",datum="+datum);  
    dm = dokladyModule;
    this.idProjekt = idProjekt;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_PROJEKTY_CF+"\\"+idProjekt;
    init();
  }

  private void init() {
    setFileName ( "ProjektCF_"+datum+".xlsx" );
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

    if(styleBold == null) 
    {
      Font font = wb.createFont();
      font.setBold(true);
      font.setFontHeightInPoints((short)16);
      styleBold = wb.createCellStyle();
      styleBold.setFont(font);
    }
    setCellValue( listNr, rowNr, colNr, "CASH FLOW PROJEKTU: "+projektName , styleBold );
    rowNr+=2;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*12));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*8));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*35));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));

    colNr=0;
    setCellValue( listNr, rowNr, colNr, "Varianta" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Kurz k CZK" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "stka v CZK" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "IN/OUT" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Typ" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Poznmka" , lightBlue ); colNr++;
    
    rowNr++;

    int typ = 0;
    String datum = "";
    double castka = 0.0;
    String mena = "";
    String inout = "";
    int inoutTyp = 0;
    String poznamka = "";

    ViewObject vo = dm.findViewObject("KpDatProjektcashflowView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGPROJEKT = "+idProjekt);
    ViewObject voTyp = dm.findViewObject("KpCisProjektcashflowtypView1");
    voTyp.clearCache();
    ViewObject voIOTyp = dm.findViewObject("KpCisProjektinouttypView1");
    voIOTyp.clearCache();
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdTyp");
      typ = hlpNum==null ? -1 : hlpNum.intValue();
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastka");
      castka = hlpNum==null ? 0.0 : hlpNum.doubleValue();
      mena = row.getAttribute("SMena")==null?"":(String)row.getAttribute("SMena");
      inout = row.getAttribute("SInout")==null?"":(String)row.getAttribute("SInout");
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdInouttyp");
      inoutTyp = hlpNum==null ? -1 : hlpNum.intValue();
      poznamka = row.getAttribute("SPoznamka")==null?"":(String)row.getAttribute("SPoznamka");
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("XRate");
      double xrate = hlpNum==null ? 0.0 : hlpNum.doubleValue();
      double castkaCZK = castka * xrate;

      //
      voTyp.setWhereClause("ID = "+typ);
      if(voTyp.hasNext()) 
      {
        Row rowTyp = voTyp.next();
        setCellValue( listNr, rowNr, colNr, (String) rowTyp.getAttribute("SPopis") , null ); 
      }
      voTyp.closeRowSet();
      colNr++;
      //
      setCellValue( listNr, rowNr, colNr, datum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castka , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, mena , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, xrate , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, castkaCZK , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, inout , null ); 
      colNr++;
      //
      voIOTyp.setWhereClause("ID = "+inoutTyp);
      if(voIOTyp.hasNext()) 
      {
        Row rowIOTyp = voIOTyp.next();
        setCellValue( listNr, rowNr, colNr, (String) rowIOTyp.getAttribute("SPopis") , null ); 
      }
      voIOTyp.closeRowSet();
      colNr++;
      //
      setCellValue( listNr, rowNr, colNr, poznamka , null ); 
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
      ESExportProjektCashFlow ed = new ESExportProjektCashFlow(dm,
                                                 1000666,
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
