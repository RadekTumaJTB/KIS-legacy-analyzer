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

public class ESExportDokument extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportDokument.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private String where;
  private String orderBy;

  public ESExportDokument(ApplicationModule dokladyModule,
                          String where,
                          String orderBy)
  {
    dm = dokladyModule;
//    this.where = where;
//esc 06.02.2011
//vzor z jsp: where = where.toLowerCase().replaceAll("VwDatSchvalovak.".toLowerCase(),"VwDatSchvalovak_2012.");
/*zaloha this.where = where.replaceAll("VwDatSchvalovak.","VwDatSchvalovakAll.");
this.where = this.where.replaceAll("VWDATSCHVALOVAK.","VwDatSchvalovakAll.");
**
this.where = where.replaceAll("VwDatSchvalovak_2012.","VwDatSchvalovakAll.");
this.where = where.replaceAll("VwDatSchvalovak.","VwDatSchvalovakAll.");
*/
this.where = where.toLowerCase().replaceAll("VwDatSchvalovak_2012.".toLowerCase(),"VwDatSchvalovakAll.");
this.where = this.where.toLowerCase().replaceAll("VwDatSchvalovak.".toLowerCase(),"VwDatSchvalovakAll.");
//    logger.info("ExportDokument:Thiswhere="+this.where);  
    this.orderBy = orderBy;
    init();
  }

  private void init() {

    logger.info("ExportDokument:WHERE: "+where);  

    setFileName ( "Dokument_"+(System.currentTimeMillis())+".xlsx" );
    setFileRelativeName( Constants.DIR_SL+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputSL()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int listNr = 0;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*40));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Typ transakce/Budget" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*4));
    setCellValue( listNr, rowNr, colNr, "Typizovan transakce" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Typ. od" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Typ. do" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum zadn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Zadavatel" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*40));
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*4));
    setCellValue( listNr, rowNr, colNr, "Existence psemn objednvky" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum zatovn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*40));
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "slo dokladu" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum splatnosti/zatovn" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Poadovn datum hrady" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Reln hrada" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Suma dk" , lightBlue ); colNr++;
///
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Prefakturace" , lightBlue ); colNr++;    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*45));
    setCellValue( listNr, rowNr, colNr, "Odbor" , lightBlue ); colNr++;    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*45));
    setCellValue( listNr, rowNr, colNr, "Projekt" , lightBlue ); colNr++;    //23.02 2012
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;    
///
//    vo = dm.findViewObject("VwDatSchvalovakView1");
    vo = dm.findViewObject("VwDatSchvalovakAllView1");
    vo.clearCache();
    vo.setWhereClause(where);
    vo.setOrderByClause(orderBy);
    CellStyle style = null;
    
    oracle.jbo.domain.Date hlpDt = null;
    oracle.jbo.domain.Number  hlpNum = null;
    String anoNe = null;
    String datum = null;
    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id"), style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Typtran"), null );
      colNr++;
      anoNe = "1".equals(row.getAttribute("CTypizovanatran")) ? "ANO" : "NE";
      setCellValue( listNr, rowNr, colNr, anoNe, null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date)row.getAttribute("DtTypizovanaod");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : null;
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date)row.getAttribute("DtTypizovanado");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : null;
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date)row.getAttribute("DtDatumzadani");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : null;
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Zadavatel"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPisemne"), null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date)row.getAttribute("DtDatumzauctovani");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : null;
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Protistrana"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SCislodokladu"), null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date)row.getAttribute("DtDatumsplatnosti");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : null;
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date)row.getAttribute("DtPozaddatumuhrady");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : null;
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      hlpDt = (oracle.jbo.domain.Date)row.getAttribute("DtRealdatumuhrady");
      datum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : null;
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("Sumaradku")!=null?((Number)row.getAttribute("Sumaradku")).doubleValue():0.0, null );
      colNr++;
///
      
      hlpNum = (oracle.jbo.domain.Number)row.getAttribute("DrIdRadku");
      if ( hlpNum!=null )
      {
      anoNe = "1".equals(row.getAttribute("DrCKprefakt")) ? "ANO" : "NE";
      setCellValue( listNr, rowNr, colNr, anoNe, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("DrOdbor")!=null?((String)row.getAttribute("DrOdbor")): "", null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("DrProjekt")!=null?((String)row.getAttribute("DrProjekt")): "", null );
      colNr++;      
      setCellValue( listNr, rowNr, colNr, row.getAttribute("DrNdCastka")!=null?((Number)row.getAttribute("DrNdCastka")).doubleValue():0.0, null );
      colNr++;      
      }
      else {colNr++;      colNr++;      colNr++;      }
///
      
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
    outputSL();
    wb.setSheetName(0,"SL");
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModule","DokumentModuleLocal");
      ESExportDokument ed = new ESExportDokument(dm,"","ID");
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
