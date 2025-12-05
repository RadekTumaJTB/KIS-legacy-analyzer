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

public class ESExportBilanceDetail extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportBilanceDetail.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private String idDoklad;
  private Number idKtgSpolecnost;
  private String ucet;
  private String locale;
  private String user;
  
  private String dDatum, dDatumMustek;

  private boolean bezIC;
  private boolean jenIfrs;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;
  private String localeSpol;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportBilanceDetail(ApplicationModule dokladyModule,
                               String idDoklad,
                               String locale,
                               String ucet,
                               String user,
                               boolean bezIC,
                               boolean jenIfrs)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    this.locale = locale;
    this.ucet = ucet;
    this.user = user;
    this.bezIC = bezIC;
    this.jenIfrs = jenIfrs;
    init();
  }

  private void init() {
    ViewObject vo = dm.findViewObject("VwKpDokladzahlaviView1");
    vo.clearCache();
    vo.setWhereClause("DOKLADID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("Spolecnostnazev");
      menaSpol = (String) row.getAttribute("SMena");
      idKtgSpolecnost = (Number) row.getAttribute("Ucetnispolecnostid");
      localeSpol = (String) row.getAttribute("SLocale");
      dDatum = sdf.format(((oracle.jbo.domain.Date) row.getAttribute("DtDatum")).dateValue());
      dDatumMustek = sdf.format(((oracle.jbo.domain.Date) row.getAttribute("DtPlatnostmustek")).dateValue());
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    logger.info("ExportBilanceDetail:nazevSpol="+nazevSpol+",idDoklad="+idDoklad);  

    setFileName ( "BilanceDetail_"+idDoklad+".xlsx" );
    setFileRelativeName( Constants.DIR_BILANCE_DETAIL+"\\"+user+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private static  CellStyle styleOk, styleError;

  private void outputDetail()
  {
    int listNr = 0;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=2;
    int colNr = 0;
    ViewObject vo;

    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 14);
    font.setBold(true);
    CellStyle styleBold = wb.createCellStyle();
    styleBold.setFont(font);

    String nadpis = "Detail bilance "+ nazevSpol +" / "+ menaSpol +" k "+ dDatum +", mďż˝stek z "+dDatumMustek;
    setCellValue( listNr, 0, 0, nadpis , styleBold );

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*12));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*3));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*3));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));

    colNr=0;
    setCellValue( listNr, rowNr, colNr, "Internďż˝ ID" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "ďż˝ďż˝dek text" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "ďż˝ďż˝et" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "orig." , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Mďż˝na" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "ďż˝ďż˝stka lokďż˝lnďż˝" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "ďż˝ďż˝stka v mďż˝nďż˝" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Externďż˝ ID" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Vybranďż˝ protistrana" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Protistrana Iďż˝" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Banka" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Banka Iďż˝" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Ekon. objekt" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Ekon. objekt Iďż˝" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Emitent" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Emitent Iďż˝" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Odbor" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Projekt" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Mďż˝novďż˝ pozice" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "ďż˝vďż˝rovďż˝ pozice" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Evidovat protistranu" , lightBlue ); colNr++;
    
    vo = dm.findViewObject("ViewExcelDetail1");
    vo.clearCache();
    String whereDet = "ID_DOKLAD = "+ idDoklad +
                      " AND (S_UCET LIKE '"+ucet+"%' OR S_UCETUNIF LIKE '"+ucet+"%')" +
                      " and s_locale = '"+locale+"'";
    if(bezIC) whereDet += " AND ID_KTGSPOLECNOST = -2";
    if(jenIfrs) whereDet += " AND C_EXTSYSTEM='I'";
    vo.setWhereClause(whereDet);
    CellStyle style;

    CellStyle styleOk = wb.createCellStyle();
    styleOk.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    styleOk.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    CellStyle styleError = wb.createCellStyle();
    styleError.setFillForegroundColor(IndexedColors.RED.getIndex());
    styleError.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      Number idIntNum = (Number)row.getAttribute("IdInterni");
      //int idInterni = idIntNum==null?-1:idIntNum.intValue();
      double idInterni = idIntNum==null?-1:idIntNum.doubleValue(); //esc 19.5.2010
      String testICO = (String)row.getAttribute("Testico");
      if ( idInterni>0 && testICO==null ) {
        style = styleOk;
      } else {
        style = styleError;
      }
        
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id"), style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Textradek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ucet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkalocal")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkamena")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("IdExtsystem"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      java.sql.Date date = ((Date)row.getAttribute("Datum")).dateValue();
      String datum = ( date == null ) ? "" : sdf.format(date);
      colNr++;
      setCellValue( listNr, rowNr, colNr, datum, null );               
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUnifspol"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistrana"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaico"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaEo"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaicoEo"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaBanka"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaicoBanka"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaEmitent"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaicoEmitent"), null );
      colNr++;
      String lhlp = row.getAttribute("IdKtgodbor")==null ? "" : ((Number)row.getAttribute("IdKtgodbor")).toString();
      setCellValue( listNr, rowNr, colNr, lhlp, null );
      colNr++;
      lhlp = row.getAttribute("IdKtgprojekt")==null ? "" : ((Number)row.getAttribute("IdKtgprojekt")).toString();
      setCellValue( listNr, rowNr, colNr, lhlp, null );
      colNr++;
      lhlp = row.getAttribute("Xidsl")==null ? "" : ((Number)row.getAttribute("Xidsl")).toString();
      setCellValue( listNr, rowNr, colNr, lhlp, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CMenovapozice"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CUverovapozice"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SEvidovatprotistranu"), null );
      colNr++;
      if ( testICO != null ) {
        setCellValue( listNr, rowNr, colNr, testICO, styleError );
      }
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
    outputDetail();
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportBilanceDetail ed = new ESExportBilanceDetail(dm,
                                               "1232377",
                                               "cs_CZ",
                                               "",
                                               "a",
                                               false,
                                               false);
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
