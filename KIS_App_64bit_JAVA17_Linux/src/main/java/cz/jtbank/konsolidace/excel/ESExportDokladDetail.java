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

public class ESExportDokladDetail extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportDokladDetail.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private String where;
  private String whereSum;

  public ESExportDokladDetail(ApplicationModule dokladyModule,
                              String where,
                              String whereSum)
  {
    dm = dokladyModule;
    this.where = where;
    this.whereSum = whereSum;
    init();
  }

  private void init() {
    logger.info("ExportDokladDetail:where="+where);  

    setFileName ( "DokladDetail_"+(System.currentTimeMillis())+".xlsx" );
    setFileRelativeName( Constants.DIR_DOKLADY_DETAIL+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputSumy()
  {
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int listNr = 0;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(1));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, ". dku" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "dek" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka lokl" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna lokl" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka CZK" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Projekt" , lightBlue ); colNr++;
	setCellValue( listNr, rowNr, colNr, "Orig. ucet" , lightBlue ); colNr++;
	setCellValue( listNr, rowNr, colNr, "Popis uctu" , lightBlue ); colNr++;
    vo = dm.findViewObject("VwKpDokladdetailDslrsumView1");
    vo.clearCache();
    vo.setWhereClause(whereSum);
    vo.setOrderByClause("NL_PORADILIST, NL_RADEK");
    CellStyle style = null;
    
    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("NlPoradilist")+" - "+row.getAttribute("NlRadek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Textradek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkamena")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkalocal")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMenaucspol"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkaczk")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Protistrana"), null );
      colNr++;
		Object oo = row.getAttribute("IdKtgprojekt");
		if ( oo != null )
			setCellValue( listNr, rowNr, colNr, ((Number)oo).toString(), null );
		else
			setCellValue( listNr, rowNr, colNr, "", null );
		;			
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetoriginal"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopisoriginal"), null );
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
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int listNr = 1;
    int rowNr=0;
    int colNr = 0;
    ViewObject vo;

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(1));
    setCellValue( listNr, rowNr, colNr, "ID" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, ". dku" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "dek" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "et" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka lokl" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna lokl" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "stka CZK" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Projekt" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Odbor" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Id tran." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*40));
    setCellValue( listNr, rowNr, colNr, "Nklad (typ. tran.)" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
    
    vo = dm.findViewObject("VwKpDokladdetailDslrView1");
    vo.clearCache();
    vo.setWhereClause(where);
    vo.setOrderByClause("NL_PORADILIST, NL_RADEK");
    CellStyle style = null;
    
    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id"), style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("NlPoradilist")+" - "+row.getAttribute("NlRadek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Textradek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkamena")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkalocal")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMenaucspol"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkaczk")).doubleValue(), null );
      colNr++;
      String datum = "";
      oracle.jbo.domain.Date uDatum = (oracle.jbo.domain.Date)row.getAttribute("Datum");
      if(uDatum!=null) datum = sdf.format(uDatum.dateValue());
      setCellValue( listNr, rowNr, colNr, datum, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Protistrana"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("IdKtgprojekt")==null?"":row.getAttribute("IdKtgprojekt").toString(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("IdKtgodbor")==null?"":row.getAttribute("IdKtgodbor").toString(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("SFaktura")==null?"":(String)row.getAttribute("SFaktura"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("Typtransakce")==null?"":(String)row.getAttribute("Typtransakce"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("Xidsl")==null?"":row.getAttribute("Xidsl").toString(), null );
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
    wb.cloneSheet(0);
    outputSumy();
    wb.setSheetName(0,"Sumy");
    outputDetail();
    wb.setSheetName(1,"Detail");
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportDokladDetail ed = new ESExportDokladDetail(dm,"DT_DATUM = TO_DATE('30.06.2007','dd.mm.yyyy') AND S_LOCALE = 'cs_CZ' AND ID_KTGUCETNISPOLECNOST = (1037) ",
                                                            "DT_DATUM = TO_DATE('30.06.2007','dd.mm.yyyy') AND S_LOCALE = 'cs_CZ' AND ID_KTGUCETNISPOLECNOST = (1037)");
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
