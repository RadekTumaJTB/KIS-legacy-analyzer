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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportDetailDokladu extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportDetailDokladu.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private Number idDoklad;
  private Number idKtgSpolecnost;
  private java.sql.Date datum;
  private java.sql.Date datumMustek;
  private String rkcFlag;

  private boolean flagIfrs;
  private String userMustek;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;
  private String localeSpol;
  private Number idSubkonsolidace;
  private Number krok;
  
  private String specialDoklad;

  private double kapital = 0.0;

  public ESExportDetailDokladu(ApplicationModule dokladyModule,
                               Number idDoklad)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
//    this.flagIfrs = flagIfrs;
//    this.userMustek = userMustek;
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
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
      idKtgSpolecnost = (Number) row.getAttribute("Ucetnispolecnostid");
      datum = ((oracle.jbo.domain.Date) row.getAttribute("DtDatum")).dateValue();
      datumMustek = ((oracle.jbo.domain.Date) row.getAttribute("DtPlatnostmustek")).dateValue();
      rkcFlag = (String) row.getAttribute("CRkc");
      localeSpol = (String) row.getAttribute("SLocale");
      idSubkonsolidace = (Number) row.getAttribute("IdSubkonsolidace");
      krok = (Number) row.getAttribute("NlKrok");
      userMustek = (String) row.getAttribute("SUzivatelmustek");
      flagIfrs = "2".equals(krok.toString());
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
    logger.info("ExportDetailDokladu:nazevSpol="+nazevSpol+",idDoklad="+idDoklad+",datum="+datum+",datumMustek="+datumMustek);  

    specialDoklad = idSubkonsolidace==null ? "" : "Sub";
    if(krok!=null && krok.intValue()==90) specialDoklad += "Mis";
    if(!flagIfrs) {
      setFileName ( idDoklad+"@BilanceDetail"+specialDoklad+"_"+datum+".xlsx" );
    }
    else {
      setFileName ( idDoklad+"@BilanceIFRSDetail"+specialDoklad+"_"+datum+".xlsx" );
    }
    String mustekDir = (userMustek==null || "master".equals(userMustek)) ? "" : userMustek+"\\";
    setFileRelativeName( souborPredponaSpol+"_"+idKtgSpolecnost+"\\"+mustekDir+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona = (localeSpol==null || localeSpol.length()<1) ? 
                     "SablonaDetailBilance.xlsx" :
                     "SablonaDetailBilance_"+localeSpol+".xlsx";
    setSablona( Constants.SABLONY_FILES_PATH+sablona );
  }
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    String special = datumMustek==null ? "" : ", mustek z "+sdf.format(datumMustek);
    special += flagIfrs ? " /IFRS" : "";
    setCellValue(0, 0, 0, nazevSpol + "  : " + menaSpol + "  ke dni " + datum + special, null);
  }

  private static  CellStyle styleOk, styleError;

  private void outputDetail()
  {
    //Constants.MAX_POCET_RADKU_EXCEL = 10000;//TEST!!!
  
    int listNr = 0;
    int rowNr=2;

    ViewObject vo = dm.findViewObject("ViewExcelDetail1");
    vo.clearCache();
    String whereDet = "ID_DOKLAD = "+ idDoklad
                      + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "");
    vo.setWhereClause(whereDet);

    CellStyle style;
    
    if ( styleOk == null ) styleOk = wb.getSheetAt(0).getRow(3).getCell((short)1).getCellStyle(); 
    if ( styleError == null ) styleError = wb.getSheetAt(0).getRow(4).getCell((short)1).getCellStyle();
    clearCell(0,3,1);
    clearCell(0,4,1);
    
    for(int i=0; i<vo.getRowCount()/Constants.MAX_POCET_RADKU_EXCEL;i++) {
      wb.cloneSheet(0);
      wb.setSheetName(i+1,"Detail c. "+(i+2));
    }
    wb.setSheetName(0,"Detail c. 1");

    while ( vo.hasNext() ) {
      Row row = vo.next();
      
      if(rowNr-3>Constants.MAX_POCET_RADKU_EXCEL) 
      {
        /*rowNr = 2;
        listNr++;*/
        break;
      }
      int colNr = 1;
      
      Number idIntNum = (Number)row.getAttribute("IdInterni");
      //int idInterni = idIntNum==null?-1:idIntNum.intValue();
            double idInterni = idIntNum==null?-1:idIntNum.doubleValue(); //esc 19.5.2010
      String testICO = (String)row.getAttribute("Testico");
      if ( idInterni>0 && testICO==null ) {
        style = styleOk;
      } else {
        style = styleError;
      }
        
      rowNr++;
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id"), style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Textradek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ucet"), null );
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
      String datum = ( date == null ) ? "" : date.toString();
      colNr++;
      setCellValue( listNr, rowNr, colNr, datum, null );               
      colNr++;
      boolean clenJTFG = "1".equals(row.getAttribute("Clenjtfgkons"));
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUnifspol"), (clenJTFG ? styleJtfg : null) );
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
      if(clenJTFG) setCellValue(listNr,rowNr,colNr,"len JTFG kons.",null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SUcetunif"),null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SPopisoriginal"),null);
      if ( testICO != null ) {
        setCellValue( listNr, rowNr, colNr, testICO, styleError );
      }
    }    
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    if(rowNr-3>Constants.MAX_POCET_RADKU_EXCEL) 
    {
      setCellValue( listNr, ++rowNr, 0, "D A T A    N E J S O U   K O M P L E T N " , null );
      setCellValue( listNr, ++rowNr, 0, "Poet zznam pevyuje monosti Excelu" , null );
    }
  }
  
  private CellStyle styleJtfg = null;
    
  protected boolean outputData () 
  {
    Font font = wb.createFont();
    font.setColor(IndexedColors.BLUE.getIndex());
    styleJtfg = wb.createCellStyle();
    styleJtfg.setFont(font);

    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputDetail();
    logger.info("Dogenerovan list detail"); 

    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportDetailDokladu ed = new ESExportDetailDokladu(dm,
                                               new Number(1774775));
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
