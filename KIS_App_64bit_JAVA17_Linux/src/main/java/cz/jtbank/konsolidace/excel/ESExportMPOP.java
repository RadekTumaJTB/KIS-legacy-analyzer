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

public class ESExportMPOP extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportMPOP.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private Number idDoklad;
  private Number idDokladKamil;
  private Number idKtgSpolecnost;
  private java.sql.Date datum;
  private java.sql.Date datumMustek;
  private String rkcFlag;

  private boolean flagIfrs = false;
  private String userMustek;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;
  private String localeSpol;
  private Number idSubkonsolidace;
  private Number krok;
  
  private int jenomPs = 0;
  
  private String specialDoklad;

  private double kapital = 0.0;

  private CellStyle styleJtfg = null;

  public ESExportMPOP(ApplicationModule dokladyModule,
                         Number idDoklad)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    init();
  }

  private void init() {
    ViewObject vo = dm.findViewObject("VwKpDokladzahlaviView1");
    vo.clearCache();
    //NEW
    //ViewObject vo = dm.createViewObject("VwKpDokladzahlaviView"+idDoklad,"cz.jtbank.konsolidace.doklady.VwKpDokladzahlaviView");
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
      flagIfrs = krok.intValue()==2;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
    logger.info("ExportMPOP:nazevSpol="+nazevSpol+",idDoklad="+idDoklad+",datum="+datum+",datumMustek="+datumMustek);  
    ViewObject voAdm = dm.findViewObject("KpDatKapitalskupinyMaxView1");
    voAdm.clearCache();
    voAdm.setWhereClauseParam(0,new Integer(-2));
    voAdm.setWhereClauseParam(1,datum);
    if(voAdm.hasNext()) 
    {
      Row rowAdm = voAdm.next();
      Number numKap = (Number) rowAdm.getAttribute("NdCastka");
      if(numKap != null) 
        kapital = numKap.doubleValue();
    }
    voAdm.closeRowSet();

    setFileName ( "BilanceMPOP_"+idDoklad+".xlsx" );
    setFileRelativeName( Constants.DIR_MPOP+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona = "SablonaMPOP.xlsx";
    setSablona( Constants.SABLONY_FILES_PATH+sablona );
  }

  private int listNr = 0;
  
  private void outputMPOP() 
  {
    CellStyle style = null;

    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
      
    int rowNr = 5;
    int colNr;
    
    if(wb.getNumberOfSheets()<=listNr) return;
    
    Number hlpNum = null;
    
    ViewObject vo = dm.findViewObject("VwKpDokladspolodborprojektView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad+ ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
    while(vo.hasNext() && pocet-- > 0 ) 
    {
      Row row = vo.next();
      String list = (String) row.getAttribute("List");
      String ucet = (String) row.getAttribute("SUcet");
      String mena = (String) row.getAttribute("SMena");
      String spolecnost = (String) row.getAttribute("Spolecnost");
      String odbor = (String) row.getAttribute("Odbor");
      String projekt = (String) row.getAttribute("Projekt");
      String ico = (String) row.getAttribute("SIco");

      hlpNum = (Number) row.getAttribute("IdKtgodbor");
      String idOdbor = hlpNum==null ? "" : hlpNum.toString();
      hlpNum = (Number) row.getAttribute("IdKtgprojekt");
      String idProjekt = hlpNum==null ? "" : hlpNum.toString();

      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      double castkaCZK = ((Number)row.getAttribute("NdCastkaczk")).doubleValue();

      style = null;
      colNr = 0;
      setCellValue(listNr,rowNr,colNr,list,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,ucet,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String) row.getAttribute("Textradek"),style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,castkaMena,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,mena,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,castkaLocal,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,castkaCZK,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,ico,style);
      colNr++;
      boolean clenJTFG = "1".equals(row.getAttribute("Clenjtfgkons"));
      setCellValue(listNr,rowNr,colNr,spolecnost,(clenJTFG ? styleJtfg : style));
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Projektano"), null );
      colNr++;
      setCellValue(listNr,rowNr,colNr,idProjekt,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,projekt,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,idOdbor,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,odbor,style);
      colNr++;
      if(clenJTFG) setCellValue(listNr,rowNr,colNr,"len JTFG kons.",null);
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SSpravce"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SKo"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegment"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegmentboss"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Pmanager"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("MngsegmentPs"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("MngsegmentbossPs"), null );
      
      rowNr++;
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
    Font font = wb.createFont();
    font.setColor(IndexedColors.BLUE.getIndex());
    styleJtfg = wb.createCellStyle();
    styleJtfg.setFont(font);
    
    String special = flagIfrs ? " /IFRS" : "";
    setCellValue(0,1,0,nazevSpol+" "+special+" k "+datum,null);

    outputMPOP();
    logger.info("Dogenerovan list suma Mena/Protistrana/Odbor/Projekt");
    listNr++;

    return true;
  }
 
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportMPOP ed = new ESExportMPOP(dm,new Number(678527));
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
