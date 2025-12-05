package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.nio.file.Paths;
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
//import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportDoklady2011_bck extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportDoklady.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private Number idDoklad;
  private Number idDokladKamil;
  private Number idDokladPodnikatel;
  private Number idKtgSpolecnost;
  private java.sql.Date datum;
  private java.sql.Date datumMustek;
  private String rkcFlag;
  private boolean baseDoklad;

  private boolean flagIfrs = false;
  private String userMustek;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;
  private String localeSpol;
  private Number idSubkonsolidace;
  private Number krok;
  
  private String dbLink, topasId;
  
  private boolean mis = false;
  private int jenomPs = 0;
  
  private String specialDoklad;

  private double kapital = 0.0;

  private boolean noDetail = true;
  private int pocetNezarazenych = 0;
  private int pocetTestICO = 0;
  private double bilanceRozdil = 0;
  private double sumaZiskZtrata = 0;
  
  private boolean sCNB = false;

  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  private CellStyle styleJtfg = null,
                        styleMis = null;
    
  public ESExportDoklady2011_bck(ApplicationModule dokladyModule,
                         Number idDoklad,
                         String userMustek,
                         Number idDokladKamil,
                         int jenomPs)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    this.userMustek = userMustek;
    this.idDokladKamil = idDokladKamil;
    this.jenomPs = jenomPs;
    init();
  }

  public ESExportDoklady2011_bck(ApplicationModule dokladyModule,
                         Number idDoklad,
                         String userMustek,
                         Number idDokladKamil)
  {
    this(dokladyModule,
         idDoklad,
         userMustek,
         idDokladKamil,
         0);
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
      baseDoklad = "1".equals(row.getAttribute("CBasedoklad"));
      dbLink = (String) row.getAttribute("SDblink");
      topasId = (String) row.getAttribute("TopasId");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=10");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDokladPodnikatel = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje podnikatelsk� doklad pro "+idDoklad);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    if(idKtgSpolecnost!=null && (
       idKtgSpolecnost.intValue()==1 ||
       idKtgSpolecnost.intValue()==1001
      )) 
     {
       sCNB=true;
     }
    
    //NEW
    //vo.remove();
    logger.info("ExportDoklady:nazevSpol="+nazevSpol+",idDoklad="+idDoklad+",datum="+datum+",datumMustek="+datumMustek);  
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

    mis = (krok!=null && krok.intValue()==90);

    specialDoklad = idSubkonsolidace==null ? "" : "Sub";
    if(mis) specialDoklad += "Mis";
    if(jenomPs==-1) specialDoklad += "-PS";
    if(flagIfrs) specialDoklad = "IFRS"+specialDoklad;

    setFileName ( idDoklad+"@Bilance"+specialDoklad+"_"+datum+".xlsx" );

    // MIGRATED: Platform-independent path construction
    String mustekDir = (userMustek==null || "master".equals(userMustek)) ? "" : userMustek;
    String relativePath = mustekDir.isEmpty()
        ? Paths.get(souborPredponaSpol+"_"+idKtgSpolecnost, getFileName()).toString()
        : Paths.get(souborPredponaSpol+"_"+idKtgSpolecnost, mustekDir, getFileName()).toString();
    setFileRelativeName( relativePath );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona ;
    /*if ( datum.compareTo(datumMustek) < 0 ){
          sablona = (localeSpol==null || localeSpol.length()<1) ? 
                     "SablonaBilance2010"+(sCNB?"CNB":"")+"_cs_CZ.xlsx" :
                     "SablonaBilance2010"+(sCNB?"CNB":"")+"_"+localeSpol+".xlsx";
    if(jenomPs==-1) sablona = (localeSpol==null || localeSpol.length()<1) ? 
                     "SablonaBilance2010-PS_cs_CZ.xlsx" :
                     "SablonaBilance2010-PS_"+localeSpol+".xlsx";
    }
    else {
    */
          sablona = (localeSpol==null || localeSpol.length()<1) ? 
                     "SablonaBilance"+(sCNB?"CNB":"")+"_cs_CZ.xlsx" :
                     "SablonaBilance"+(sCNB?"CNB":"")+"_"+localeSpol+".xlsx";
          if(jenomPs==-1) sablona = (localeSpol==null || localeSpol.length()<1) ? 
                       "SablonaBilance-PS_cs_CZ.xlsx" :
                       "SablonaBilance-PS_"+localeSpol+".xlsx";
  //}
    setSablona( Constants.SABLONY_FILES_PATH+sablona );
System.out.println ( Constants.SABLONY_FILES_PATH+sablona );    
  }

  private int listNr = 0;
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    String special = datumMustek==null ? "" : ", mustek z "+sdf.format(datumMustek);
    special += flagIfrs ? " /IFRS" : "";
    if(jenomPs==-1) {
      special += " - zahajovac� bilance";
    }
    //else setCellValue(10, 1, 6, nazevSpol + "  : " + menaSpol + "  ke dni " + sdf.format(datum) + special, null);
    outputZahlavi(nazevSpol,menaSpol,datum,special,jenomPs);
    
    for(listNr = 0; listNr<(jenomPs==-1?2:5); listNr++) {
      int colNr = 2;
      setCellValue(listNr,6,colNr,menaSpol,style);
      colNr++;
      if(listNr==0) 
      {
        if(sCNB) colNr++;
        setCellValue(listNr,6,colNr,menaSpol,style);
        colNr++;

        if(sCNB) colNr++;
        setCellValue(listNr,6,colNr,menaSpol,style);
        colNr++;
      }
    }
  }

  private void outputPrvniListy() 
  {
    CellStyle style = null;
    int lastRadek = 0, lastList = 0;
    double castka1 = 0.0D, castka2 = 0.0D;
    double castkaCzk = 0.0D;
    double castka1opr = 0.0D, castka2opr = 0.0D;
    double castkaCzkopr = 0.0D;
    
    ViewObject vo = dm.findViewObject("VwKpDokladView1");
    vo.clearCache();
    //NEW
    //ViewObject vo = dm.createViewObject("VwKpDokladView"+idDoklad,"cz.jtbank.konsolidace.doklady.VwKpDokladView");
    vo.setWhereClause("ID_DOKLAD = " + idDoklad + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
    int rowNr;    
    int koef = 1;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      String mena = (String)row.getAttribute("SMena");
      int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double opravnaCastkaLocal = ((Number)row.getAttribute("NdOpravnacastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      double opravnaCastkaMena = ((Number)row.getAttribute("NdOpravnacastkamena")).doubleValue();
      
      if(jenomPs==-1 && list>2) continue;

      if(lastList != list || lastRadek != radek) 
      {
        castka1 = 0.0D;
        castka2 = 0.0D;
        castka1opr = 0.0D;
        castka2opr = 0.0D;
        castkaCzk = 0.0D;
        castkaCzkopr = 0.0D;
      }
      
      //kdyz je mena spolecnosti mepna => v prvnim sloupci bude lokalni castka
      if(menaSpol.equals(mena)) 
      {
        castka1 = castkaLocal;
        castka1opr = opravnaCastkaLocal;
      }
      //jinak se nascitavaji hodnoty do druheho sloupce
      else 
      {
        castka2 += castkaLocal;
        castka2opr += opravnaCastkaLocal;
      }
      //treti sloupec
      //kdyz je mena spolecnosti CZK
      if("CZK".equals(menaSpol)) 
      {
        //a mena je CZK, pak se lokalni castka pricte do tretiho sloupce
        if("CZK".equals(mena)) 
        {
          castkaCzk += castkaLocal;
          castkaCzkopr += opravnaCastkaLocal;
        }
        //a mena neni CZK, pak se lokalni castka odecte od tretiho sloupce
        else 
        {
          castkaCzk -= castkaLocal;
          castkaCzkopr -= opravnaCastkaLocal;
        }
      }
      //kdyz mena spolecnosti neni CZK
      else 
      {
        //do tretiho slouce se vlozi castka v mene CZK
        if("CZK".equals(mena)) 
        {
          castkaCzk = castkaMena;
          castkaCzkopr = opravnaCastkaMena;
        }
      }

      listNr = list - 1;
      rowNr = radek + 6;
      int colNr = 2;
      double castka123 = 0.0,
             castkaCzk123 = 0.0;
      if(jenomPs==-1 && list==2 && radek==123) 
      {
        rowNr--;
        castka123 = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr).getNumericCellValue();
        if(sCNB) castkaCzk123 = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)(colNr+1)).getNumericCellValue();
      }

      setCellValue(listNr,rowNr,colNr,castka123+koef*castka1,style);
      if(sCNB) {
        colNr++;
        setCellValue(listNr,rowNr,colNr,castkaCzk123+koef*castkaCzk,style);
      }

      if(list == 1) {
        colNr++;
        setCellValue(listNr,rowNr,colNr,koef*castka1opr,style);
        if(sCNB) {
          colNr++;
          if(sCNB) setCellValue(listNr,rowNr,colNr,koef*castkaCzkopr,style);
        }
      }

      lastList = list;
      lastRadek = radek;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
/*    
    sumaZiskZtrata = wb.getSheetAt(Constants.SUMA_Z_S_LISTNR).
                        getRow(Constants.SUMA_Z_S_ROWNR).
                        getCell(Constants.SUMA_Z_S_COLNR).getNumericCellValue();
*/
  }

  private void clearPrvniListy() 
  {
    for(int i=0; i<5; i++) 
    {
      if(i==0) 
      {
        for(int j=0; j<200; j++);
          //wb.getSheetAt(i).getRow(j).getCell((short)7).re
      }
      else 
      {
        
      }
    }
  }

  private void outputBilance() 
  {
    CellStyle style = null;
      
    int rowNr = 14;
    int colNr;
    
    CellStyle[] styles;
    CellStyle[] localStyle = new CellStyle[9];
    CellStyle[] menaStyle = new CellStyle[9];
    for(short i=8; i<=16; i++) {
      localStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr)!=null && wb.getSheetAt(listNr).getRow(rowNr).getCell(i)!=null) ? wb.getSheetAt(listNr).getRow(rowNr).getCell(i).getCellStyle() : null;
      menaStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr+1)!=null && wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i)!=null) ? wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i).getCellStyle() : null;
      clearCell(listNr,rowNr,i);
      clearCell(listNr,rowNr+1,i);
    }
    
    double sumaAkt = 0.0D, sumaAktOpr = 0.0D;
    double sumaPas = 0.0D, sumaPasOpr = 0.0D;
    
    String viewBilance = "VwKpDokladView1";
    ViewObject vo = dm.findViewObject(viewBilance);
    vo.clearCache();
    //NEW
    //String viewBilance = genPS?"VwKpDokladbilancepocstavView":"VwKpDokladView";
    //ViewObject vo = dm.createViewObject(viewBilance+idDoklad,"cz.jtbank.konsolidace.doklady."+viewBilance);
    vo.setWhereClause("ID_DOKLAD = " + idDoklad + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String listNazev = (String) row.getAttribute("SList");
      String radekText = (String) row.getAttribute("SRadektext");
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      double koef = ((Number)row.getAttribute("Koef")).doubleValue();
      double castkaLocal = koef*((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double castkaMena = koef*((Number)row.getAttribute("NdCastkamena")).doubleValue();
      double opravnaCastkaLocal = koef*((Number)row.getAttribute("NdOpravnacastkalocal")).doubleValue();
      double opravnaCastkaMena = koef*((Number)row.getAttribute("NdOpravnacastkamena")).doubleValue();
      String mena = (String)row.getAttribute("SMena");

      if(mena.equals(menaSpol)) 
      {
        if("Aktiva".equals(listNazev)) {
          sumaAkt += castkaLocal;
          sumaAktOpr += opravnaCastkaLocal;
        }
        else if("Pasiva".equals(listNazev)) {
          sumaPas += castkaLocal;
          sumaPasOpr += opravnaCastkaLocal;
        }
        if ( "Vysledovka".equals(listNazev) && radek < 156 ) {
          sumaZiskZtrata += castkaLocal;
        }
        styles = localStyle;
      }
      else 
      {
        styles = menaStyle;
      }
      colNr = 8;
//System.out.println("listNr="+listNr+", rowNr="+rowNr+", colNr="+colNr+", listNazev="+listNazev);
      setCellValue(listNr,rowNr,colNr,listNazev,styles[0]);
      colNr++;
      if(mena.equals(menaSpol)) {
        setCellValue(listNr,rowNr,colNr,radekText,styles[1]);
      }
      colNr+=2;
      setCellValue(listNr,rowNr,colNr,""+radek,styles[3]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,castkaLocal,styles[4]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,castkaMena,styles[5]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,opravnaCastkaLocal,styles[6]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,opravnaCastkaMena,styles[7]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,mena,styles[8]);
      
      rowNr++;
    }
    setCellValue(listNr,8,12,sumaAkt,null);
    setCellValue(listNr,8,13,sumaAktOpr,null);
    setCellValue(listNr,9,12,sumaPas,null);
    setCellValue(listNr,9,13,sumaPasOpr,null);
    setCellValue(listNr,8,11,sumaAkt+sumaAktOpr,null);
    setCellValue(listNr,9,11,sumaPas+sumaPasOpr,null);
    bilanceRozdil = sumaAkt+sumaAktOpr+sumaPas+sumaPasOpr;
    setCellValue(listNr,10,11,bilanceRozdil,null);

    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
  }

  private static  CellStyle styleOk, styleError;

  private void outputDetail()
  {
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=2;
    ViewObject vo;
    
    //NEW
    //String viewDetail = (idKtgSpolecnost.intValue()<99)?"ViewExcelDetailCZB":"ViewExcelDetail";
    //ViewObject vo = dm.createViewObject(viewDetail+idDoklad,"cz.jtbank.konsolidace.doklady."+viewDetail);
//    if ( idKtgSpolecnost.intValue() < 99  ) {
//      vo = dm.findViewObject("ViewExcelDetailCZB1");
//    } else {
      vo = dm.findViewObject("ViewExcelDetail1");
      vo.clearCache();
//    }
    String whereDet = "ID_DOKLAD = "+ idDoklad
                 + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "");
//    vo.setWhereClause(whereDet);
    CellStyle style;
    
    if ( styleOk == null ) styleOk = wb.getSheetAt(listNr).getRow(3).getCell((short)1).getCellStyle(); 
    if ( styleError == null ) styleError = wb.getSheetAt(listNr).getRow(4).getCell((short)1).getCellStyle();
    
    clearCell(listNr,3,1);
    clearCell(listNr,4,1);
    
    boolean mocVelky = true;//vo.getRowCount()>Constants.MAX_POCET_RADKU_EXCEL;
/*
    if(mocVelky) 
    {
*/
      whereDet += " AND (ID_INTERNI IS NULL OR ID_INTERNI<0 OR TESTICO IS NOT NULL)";
      vo.clearCache();
      vo.setWhereClause(whereDet);
//    }

    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      noDetail = false;
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
        
      if ( idInterni<=0 )                 
        pocetNezarazenych++;

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
      if(clenJTFG) setCellValue(listNr,rowNr,colNr,"�len JTFG kons.",null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SUcetunif"),null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SPopisoriginal"),null);
      colNr++;
      if ( testICO != null ) {
        pocetTestICO++;
        setCellValue( listNr, rowNr, colNr, testICO, styleError );
      }
    }
/*    
    if ( pocet <= 0 ) {
      setCellValue( listNr, ++rowNr, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( listNr, ++rowNr, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
*/    
    if ( mocVelky ) {

      setCellValue( listNr, ++rowNr, 0, "D A T A    N E J S O U   K O M P L E T N � - tento list obsahuje pouze neza�azen� transakce" , null );
      setCellValue( listNr, ++rowNr, 0, "Detail je dostupn� v�hradn� p�es tla��tko D u dokladu, vygenerov�n�m excelu s kompletn�m detailem, p��p. p��mo v�b�rem SYU�" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
  }

  private void outputZustatky () 
  {
    int rowNr=2;
    double czkValue = 0; //esc 9.9.10
    ViewObject vo = dm.findViewObject("VwKpDokladzustatkyuctuView1");
    vo.clearCache();
    //NEW
    //ViewObject vo = dm.createViewObject("VwKpDokladzustatkyuctuView"+idDoklad,"cz.jtbank.konsolidace.doklady.VwKpDokladzustatkyuctuView");
    vo.setWhereClause("ID_DOKLAD = " + idDoklad+ ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
System.out.println ( "ID_DOKLAD = " + idDoklad+ ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));    
    while ( vo.hasNext() ) {
      Row row = vo.next();
      rowNr++;
      int colNr = 0;
      colNr++;
      setCellValue( listNr, rowNr, colNr, nazevSpol, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("CastkaCZK")).doubleValue(), null );
      czkValue = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );
      setCellValue( listNr, rowNr, colNr, czkValue, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkalocal")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, menaSpol, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkamena")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SPopisoriginal"),null);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
  }

  private void outputUverovaPozice() {
    java.util.SortedMap mSumy = new java.util.TreeMap();
    int rowNr=2;
    
    ViewObject vo = dm.findViewObject("VwKpDokladuverovapoziceView1");
    vo.clearCache();
    //NEW
    //ViewObject vo = dm.createViewObject("VwKpDokladuverovapoziceView"+idDoklad,"cz.jtbank.konsolidace.doklady.VwKpDokladuverovapoziceView");
    String whereUp = "ID_DOKLAD = " + idDoklad+ ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")+
                     " AND (S_LISTGROUPCODE like 'S3%' OR S_LISTGROUPCODE like 'N%' OR S_LISTGROUPCODE IS NULL)";
    vo.setWhereClause(whereUp);
    String key;
    double cLokal, cEkvivalent, pom;
//System.out.println("datum="+datum+",idDoklad="+idDoklad);
    while ( vo.hasNext() ) {
      Row row = vo.next();
      rowNr++;
      int colNr = 3;
      int idProti = row.getAttribute("IdUnifproti")==null ? 0 : ((Number)row.getAttribute("IdUnifproti")).intValue();
      colNr++;                
      setCellValue( listNr, rowNr, colNr, nazevSpol, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;                
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;               
      //cEkvivalent = ((Number)row.getAttribute("CastkaCZK")).doubleValue();
      cEkvivalent = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );
      setCellValue( listNr, rowNr, colNr, cEkvivalent, null );
      colNr++;
      cLokal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      setCellValue( listNr, rowNr, colNr, cLokal, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, menaSpol, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkamena")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUnifprotiIco"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUnifproti"), null );
      colNr++;
      key = (String)row.getAttribute("SListgroup");
      setCellValue( listNr, rowNr, colNr, key, null );
      colNr++;
//      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SEvidovatprotistranu"), null );
      colNr++;
      double kategorieKoef = row.getAttribute("NdKategorieKoef")!=null ? ((Number)row.getAttribute("NdKategorieKoef")).doubleValue() : 0;
      setCellValue( listNr, rowNr, colNr, kategorieKoef, null );
      colNr++;
      double angazovanost = (idProti==29390 || idProti==46033 || idProti==29419) ? 0 : cEkvivalent * kategorieKoef;
      setCellValue( listNr, rowNr, colNr, angazovanost, null );
      colNr++;
      if(kapital != 0.0) 
        setCellValue( listNr, rowNr, colNr, 100.0*angazovanost/kapital, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SPopisoriginal"),null);
        
      if ( key != null ) {
        Object o = mSumy.get(key);    
        double[] sumy;
        if ( o == null ) {
          sumy = new double[2];
          sumy[0] = cLokal;
          sumy[1] = cEkvivalent;
        } else {
          sumy = (double[]) o;
          sumy[0] += cLokal;
          sumy[1] += cEkvivalent;
        }
        mSumy.put ( key, sumy );
      }
        
    }
    
    rowNr = 2;
    for ( Iterator i = mSumy.keySet().iterator(); i.hasNext(); ) {
      String k = (String) i.next();
      double[] c = (double[]) mSumy.get(k);

      rowNr++;
      int colNr = 0;
      setCellValue( listNr, rowNr, colNr, k, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[1], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[0], null );
    }            
    
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
  }

  private void outputUPSouhrn() {
    CellStyle styleBold, styleSkup, styleSpol;    
    Font font1 = wb.createFont();
    font1.setFontHeight((short) 200);
    font1.setBold(true);
    Font font2 = wb.createFont();
    font2.setFontHeight((short) 175);
    font2.setBold(true);
    styleBold = wb.createCellStyle();
    styleBold.setFont(font1);
    styleSkup = wb.createCellStyle();
    styleSkup.setFont(font1);
    styleSkup.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    styleSkup.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleSpol = wb.createCellStyle();
    styleSpol.setFont(font2);
    styleSpol.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    styleSpol.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    //esc
    //org.apache.poi.hssf.usermodel.Workbook.Workbook    wb = new Workbook();
    //org.apache.poi.hssf.usermodel.Workbook.DataFormat  df = wb.createDataFormat();
    //

    int rowNr=5;
    int colNr=0;

    String kod = "";    
    double kap20 = kapital * 0.2;
    double kap25 = kapital * 0.25;
    double sumaSkup = 0.0;
    double sumaProti = 0.0;
    double maxAngazovanostSkup = 0.0;
    double maxAngazovanostSpol = 0.0;
            
    Skupina lastGroup = null;
    Protistrana lastProti = null;
    Spolecnost lastSpol = null;
    Radek lastRadek = null;
           
    setCellValue( listNr, 0, 2, sdf.format(datum), null );
    
    setCellValue( listNr, 0, 8, kapital, null );
    setCellValue( listNr, 1, 8, kap25, null );
    setCellValue( listNr, 2, 8, kap20, null );

    BeanListSkupina bls = new BeanListSkupina();
    
    ViewObject vo = dm.findViewObject("VwKpDokladuverovapoziceView1");
    vo.clearCache();
    String whereUp = "ID_DOKLAD = " + idDoklad+ ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")+
                     " AND (S_LISTGROUPCODE like 'S3%' OR S_LISTGROUPCODE like 'N%' OR S_LISTGROUPCODE IS NULL)";
    vo.setWhereClause(whereUp);
    vo.setOrderByClause("S_LISTGROUP, S_UNIFPROTI_ICO, NAZEV_SPOL, S_POPIS");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int idProti = row.getAttribute("IdUnifproti")==null ? 0 : ((Number)row.getAttribute("IdUnifproti")).intValue();
      String group = (String)row.getAttribute("SListgroup");
      String proti = (String)row.getAttribute("SUnifproti");
      String protiIco = (String)row.getAttribute("SUnifprotiIco");
      String nazevSpol = (String)row.getAttribute("nazevSpol");
      String popis = (String)row.getAttribute("SPopis");
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      String mena = (String)row.getAttribute("SMena");
      //double castkaCzk = ((Number)row.getAttribute("CastkaCZK")).doubleValue();
      double castkaCzk = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );
      kod = (String)row.getAttribute("SListgroupcode");
      double kategorieKoef = row.getAttribute("NdKategorieKoef")!=null ? ((Number)row.getAttribute("NdKategorieKoef")).doubleValue() : 0;
      double angazovanost = (idProti==29390 || idProti==46033 || idProti==29419) ? 0 : castkaCzk * kategorieKoef;
      if(kod == null) kod = "";
      if(group==null) group = "";
      if(protiIco==null) protiIco = "";
      if(proti==null) proti = "";
      if(nazevSpol==null) nazevSpol = "?";
      if(popis==null) popis = "?";

      if(lastGroup == null || !lastGroup.nazev.equals(group)) 
      {
        lastGroup = new Skupina();
        lastGroup.nazev = group;
        lastGroup.suma = 0.0;
        lastGroup.cerpat = !kod.startsWith("S")?kap25:kap20;
        if("".equals(group)) { 
          lastGroup.notSkupina = true;
          lastGroup.suma = Double.NEGATIVE_INFINITY;
        }
        bls.list.add(lastGroup);
        lastProti = null;
        lastSpol = null;
        lastRadek = null;
      }
      if(lastProti == null || !lastProti.ico.equals(protiIco)) 
      {
        lastProti = new Protistrana();
        lastProti.nazev = proti;
        lastProti.ico = protiIco;
        lastProti.suma = 0.0;
        lastProti.cerpat = !kod.startsWith("S")?kap25:kap20;
        if("".equals(proti)) {
          lastProti.notProti = true;
          lastProti.suma = Double.NEGATIVE_INFINITY;
        }
        lastGroup.protistrany.add(lastProti);
        lastSpol = null;
        lastRadek = null;
      }
      if(lastSpol == null || !lastSpol.nazev.equals(nazevSpol)) 
      {
        lastSpol = new Spolecnost();
        lastSpol.nazev = nazevSpol;
        lastProti.spolecnosti.add(lastSpol);
        lastRadek = null;
      }
/*      
      if(lastRadek!=null && lastRadek.popis.equals(popis)) 
      {
        popis = "";
      }
*/
      lastRadek = new Radek();
      lastRadek.popis = popis;
      lastRadek.castkaMena = castkaMena;
      lastRadek.mena = mena;
      lastRadek.castkaCzk = castkaCzk;
      lastRadek.cistaAng = angazovanost;
      lastSpol.radky.add(lastRadek);
      if(!lastGroup.notSkupina)
        lastGroup.suma += angazovanost;
      if(!lastProti.notProti)
        lastProti.suma += angazovanost;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    bls.sortList();

    Iterator iterGroup = bls.list.iterator();
    while(iterGroup.hasNext()) 
    {
      lastGroup = (Skupina) iterGroup.next();
      setCellValue( listNr, rowNr, 0, lastGroup.nazev, styleBold );
      Iterator iterProti = lastGroup.protistrany.iterator();
      while(iterProti.hasNext()) 
      {
        lastProti = (Protistrana) iterProti.next();
        setCellValue( listNr, rowNr, 1, lastProti.nazev, null );
        setCellValue( listNr, rowNr, 2, lastProti.ico, null );
        Iterator iterSpol = lastProti.spolecnosti.iterator();
        while(iterSpol.hasNext()) 
        {
          lastSpol = (Spolecnost) iterSpol.next();
          setCellValue( listNr, rowNr, 3, lastSpol.nazev, null );
          Iterator iterRadek = lastSpol.radky.iterator();
          while(iterRadek.hasNext()) 
          {
            lastRadek = (Radek) iterRadek.next();
            setCellValue( listNr, rowNr, 4, lastRadek.popis, null );
            setCellValue( listNr, rowNr, 5, lastRadek.castkaMena, null );
            setCellValue( listNr, rowNr, 6, lastRadek.mena, null );
            setCellValue( listNr, rowNr, 7, lastRadek.castkaCzk, null );
            setCellValue( listNr, rowNr, 8, lastRadek.cistaAng, null );
            rowNr++;
          }
        }
        if(!lastProti.notProti) {
          for(int i=2;i<=7;i++) setCellValue( listNr, rowNr, i, (String)null, styleSpol );
          setCellValue( listNr, rowNr, 1, "Celkem spole�nost " + lastProti.nazev, styleSpol );
          setCellValue( listNr, rowNr, 8, lastProti.suma, styleSpol );
          if(kapital!=0.0) setCellValue( listNr, rowNr, 9, 100.0*lastProti.suma/kapital, styleSpol );
          setCellValue( listNr, rowNr, 10, lastProti.cerpat - lastProti.suma, styleSpol );
        }
        if(100.0*lastProti.suma/kapital>maxAngazovanostSpol) maxAngazovanostSpol = 100.0*lastProti.suma/kapital;
        rowNr++;
      }
      if(!lastGroup.notSkupina) {
        for(int i=1;i<=7;i++) setCellValue( listNr, rowNr, i, (String)null, styleSkup );
        setCellValue( listNr, rowNr, 0, "Celkem skupina " + lastGroup.nazev, styleSkup );
        setCellValue( listNr, rowNr, 8, lastGroup.suma, styleSkup );
        if(kapital!=0.0) setCellValue( listNr, rowNr, 9, 100.0*lastGroup.suma/kapital, styleSkup );
        setCellValue( listNr, rowNr, 10, lastGroup.cerpat - lastGroup.suma, styleSkup );
      }
      if(maxAngazovanostSkup==0.0) maxAngazovanostSkup = 100.0*lastGroup.suma/kapital;
      rowNr+=3;
    }

    setCellValue( listNr, 1, 9, "Skupina", null );
    setCellValue( listNr, 2, 9, "Spole�nost", null );
    setCellValue( listNr, 1, 10, maxAngazovanostSkup, null );
    setCellValue( listNr, 2, 10, maxAngazovanostSpol, null );
  }

  private void outputMenovaPozice() {
    int rowNr=2;
    ViewObject vo = dm.findViewObject("VwKpDokladmenovapoziceView1");
    vo.clearCache();
    //NEW
    //ViewObject vo = dm.createViewObject("VwKpDokladmenovapoziceView"+idDoklad,"cz.jtbank.konsolidace.doklady.VwKpDokladmenovapoziceView");
    vo.setWhereClause("ID_DOKLAD = " + idDoklad+ " AND C_MENOVAPOZICE='1' "+((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));

    java.util.SortedMap map = new java.util.TreeMap();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      rowNr++;
      int colNr = 5;
      double lokalValue = 0, menaValue = 0, czkValue = 0; 
      String sMena = null;

      colNr++;
      setCellValue( listNr, rowNr, colNr, nazevSpol, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;
      //czkValue = ((Number)row.getAttribute("CastkaCZK")).doubleValue();
      czkValue = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );
      setCellValue( listNr, rowNr, colNr, czkValue, null );
      colNr++;
      lokalValue = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      setCellValue( listNr, rowNr, colNr, lokalValue, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, menaSpol, null );
      colNr++;
      menaValue = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      setCellValue( listNr, rowNr, colNr, menaValue, null );
      colNr++;
      sMena = (String)row.getAttribute("SMena");
      setCellValue( listNr, rowNr, colNr, sMena, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SPopisoriginal"),null);
      double[][] castky = null;
      Object v = map.get(sMena);
      if ( v != null )
          castky = ( double[][] ) v;
      else
          castky = new double[2][3];
      
      if ( menaValue > 0 ) {
          castky[0][0] += lokalValue;
          castky[0][1] += menaValue;
          castky[0][2] += czkValue;
      } else {
          castky[1][0] += lokalValue;
          castky[1][1] += menaValue;
          castky[1][2] += czkValue;
      }                    
      map.put ( sMena, castky );
    }
          
    rowNr=2;
    for ( Iterator i = map.keySet().iterator(); i.hasNext(); ) {
      String k = (String) i.next();
      double[][] c = (double[][]) map.get(k);
      rowNr++;
      int colNr = 0;
      setCellValue( listNr, rowNr, colNr, "Dlouh� pozice", null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[0][2], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[0][0], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[0][1], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, k, null );
      rowNr++;
      colNr = 0;
      setCellValue( listNr, rowNr, colNr, "Kr�tk� pozice", null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[1][2], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[1][0], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[1][1], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, k, null );
    }

    double lokalSumaDlouhePozice = 0, lokalSumaKratkePozice = 0;
    double czkSumaDlouhePozice = 0, czkSumaKratkePozice = 0;
    
    rowNr += 3;
    for ( Iterator i = map.keySet().iterator(); i.hasNext(); ) {
        String k = (String) i.next();
        double[][] c = (double[][]) map.get(k);
        rowNr++;
        int colNr = 0;
        setCellValue( listNr, rowNr, colNr, "Agregace", null );                
        colNr++;
        setCellValue( listNr, rowNr, colNr, c[0][2]+c[1][2], null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, c[0][0]+c[1][0], null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, c[0][1]+c[1][1], null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, k, null );
        if ( ! k.equals ( menaSpol ) ) {
          if ( ( c[0][0]+c[1][0] ) > 0 ) { 
            czkSumaDlouhePozice += c[0][2]+c[1][2];
            lokalSumaDlouhePozice += c[0][0]+c[1][0];
          } else {
            czkSumaKratkePozice += c[0][2]+c[1][2];
            lokalSumaKratkePozice += c[0][0]+c[1][0];
          }
        }
    }

    rowNr += 4;
    int colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Vysledek", null );                
    rowNr++;
    setCellValue( listNr, rowNr, colNr, "Suma dlouhych pozic", null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkSumaDlouhePozice, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalSumaDlouhePozice, null );
    rowNr++;
    colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Suma kratkych pozic", null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkSumaKratkePozice, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalSumaKratkePozice, null );
    rowNr++;
    rowNr++;
    colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Vetsi z hodnot", null );
    double czkVetsi = Math.abs(czkSumaKratkePozice);
    double lokalVetsi = Math.abs(lokalSumaKratkePozice);
    if ( Math.abs( lokalSumaDlouhePozice ) > Math.abs( lokalSumaKratkePozice ) ) {
        czkVetsi = Math.abs(czkSumaDlouhePozice);
        lokalVetsi = Math.abs(lokalSumaDlouhePozice);
    }
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkVetsi, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalVetsi, null );
    rowNr++;
    colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Kap. pozadavek 8%", null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkVetsi*.08, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalVetsi*.08, null );
          
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
  }

  private void outputProtistrany() 
  {
    int rowNrL1 = 2;
    ViewObject vo = dm.findViewObject("VwKpDokladprotistranaView1");
    vo.clearCache();
    //NEW
    //ViewObject vo = dm.createViewObject("VwKpDokladprotistranaView"+idDoklad,"cz.jtbank.konsolidace.doklady.VwKpDokladprotistranaView");
    vo.setWhereClause("ID_DOKLAD = " + idDoklad+ ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
    while(vo.hasNext()) 
    {
      Row row = vo.next();    
      
      String ucet = (String) row.getAttribute("SUcet");
      String popis = (String) row.getAttribute("SPopis");
      //double castkaCzk = ((Number) row.getAttribute("CastkaCZK")).doubleValue();
      double castkaCzk = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );
      double castkaLocal = ((Number) row.getAttribute("Ndcastkalocal")).doubleValue();
      double castkaMena = ((Number) row.getAttribute("Ndcastkamena")).doubleValue();
      String mena = (String) row.getAttribute("SMena");
      String unifprotiIco = (String) row.getAttribute("UnifprotiIco");
      String unifproti = (String) row.getAttribute("Unifproti");
      String listgroup = (String) row.getAttribute("Listgroup");
      String ekonomSektor = (String) row.getAttribute("SEkonomSektor");
      String evidovatprotistranu = (String) row.getAttribute("SEvidovatprotistranu");
      String reportKod = (String) row.getAttribute("ReportKod");
      boolean clenJTFG = "1".equals(row.getAttribute("Clenjtfgkons"));
      String ucetUnif = (String) row.getAttribute("SUcetunif");
	  
	  String kodKategorieProtistrana = (String) row.getAttribute("SKodkategorieprotistrana");
	  String typSubject = (String) row.getAttribute("Typsubject");
	  String zeme = (String) row.getAttribute("SCountry");
	  String flagZeme = (String) row.getAttribute("CCountry");
	  String flagCountryParties = (String) row.getAttribute("CTypecounterparties");
	  
	  Number limitCountryParties = (Number) row.getAttribute("NdLimitcounterpartiesdetal");
	  
	  String sLimit = ( limitCountryParties == null ) ? "" : limitCountryParties.toString();
      	  
	  //esc 12/09
    String okec = (String) row.getAttribute("SOkec"); //hodnoty su uz nove, ine ako OKEC
    String ppf_acc = (String) row.getAttribute("SAccPpf");
    String ppf_info = (String) row.getAttribute("SAccPpfInfo");
    
      rowNrL1++;
      int colNr = 0;
      setCellValue( listNr, rowNrL1, colNr, reportKod, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, ucet, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, popis, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, castkaCzk, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, mena, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, castkaLocal, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, castkaMena, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, unifprotiIco, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, unifproti, (clenJTFG ? styleJtfg : null) );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, evidovatprotistranu, null );
      colNr++;
      if(clenJTFG) setCellValue(listNr,rowNrL1,colNr,"�len JTFG kons.",null);
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, ucetUnif, null );
      colNr++;
      setCellValue(listNr,rowNrL1,colNr,(String)row.getAttribute("SPopisoriginal"),null);

      colNr++;
      setCellValue( listNr, rowNrL1, colNr, kodKategorieProtistrana, (  ( unifprotiIco != null && kodKategorieProtistrana == null) ? styleError : null ) );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, typSubject, (  ( "1".equals ( flagCountryParties ) && typSubject != null && "??".equals( typSubject ) && kodKategorieProtistrana != null) ? styleError : null ) );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, zeme, (  ( "1".equals ( flagZeme ) && unifprotiIco != null && zeme == null ) ? styleError : null ) );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, flagZeme, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, flagCountryParties, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, sLimit, null );
//esc 12/2009
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, okec, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, ppf_acc, null );
      colNr++;
      setCellValue( listNr, rowNrL1, colNr, ppf_info, null );      

    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
  }

  private void outputBilanceProtistrana() 
  {
    CellStyle style = null;
      
    int rowNr = 14;
    int colNr;
    
    if(wb.getNumberOfSheets()<=listNr) return;
    
    CellStyle[] styles;
    CellStyle[] localStyle = new CellStyle[9];
    CellStyle[] menaStyle = new CellStyle[9];
    for(short i=8; i<=16; i++) {
      localStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr)!=null && wb.getSheetAt(listNr).getRow(rowNr).getCell(i)!=null)?wb.getSheetAt(listNr).getRow(rowNr).getCell(i).getCellStyle():null;
      menaStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr+1)!=null && wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i)!=null) ? wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i).getCellStyle() : null;
      clearCell(listNr,rowNr,i);
      clearCell(listNr,rowNr+1,i);
    }
    
    double sumaAkt = 0.0D, sumaAktOpr = 0.0D;
    double sumaPas = 0.0D, sumaPasOpr = 0.0D;

    String lastList = "";
    int lastRadek = 0;
    
    ViewObject vo = dm.findViewObject("VwKpDokladbilanceprotistranaView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad
                      + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String listNazev = (String) row.getAttribute("List");
      String radekText = (String) row.getAttribute("Radektext");
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      String mena = (String)row.getAttribute("SMena");
      String protistrana = (String)row.getAttribute("Protistrana");
      boolean clenJTFG = "1".equals(row.getAttribute("Clenjtfgkons"));

      if("Aktiva".equals(listNazev)) {
        if(sloupec == 1) sumaAkt += castkaLocal;
        else if(sloupec == 3) sumaAktOpr += castkaLocal;
      }
      else if("Pasiva".equals(listNazev)) {
        if(sloupec == 1) sumaPas += castkaLocal;
        else if(sloupec == 3) sumaPasOpr += castkaLocal;
      }
      
      styles = localStyle;
      
      boolean opravna = false;
      if(lastRadek==radek && lastList.equals(listNazev) && sloupec==3) {
        opravna=true;
        rowNr--;
      }
      
      colNr = 8;
      setCellValue(listNr,rowNr,colNr,listNazev,styles[0]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,radekText,styles[1]);
      colNr+=2;
      if(!opravna) setCellValue(listNr,rowNr,colNr,""+radek,styles[3]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,castkaLocal,styles[4]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,castkaMena,styles[5]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(sloupec==3?castkaLocal:0),styles[6]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(sloupec==3?castkaMena:0),styles[7]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,mena,styles[8]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,protistrana,styles[2]);
      colNr++;
      if(!opravna && clenJTFG) setCellValue(listNr,rowNr,colNr,"�len JTFG kons.",null);

      lastList = listNazev;
      lastRadek = radek;
      
      rowNr++;
    }
    setCellValue(listNr,8,12,sumaAkt,null);
    setCellValue(listNr,8,13,sumaAktOpr,null);
    setCellValue(listNr,9,12,sumaPas,null);
    setCellValue(listNr,9,13,sumaPasOpr,null);
    setCellValue(listNr,8,11,sumaAkt+sumaAktOpr,null);
    setCellValue(listNr,9,11,sumaPas+sumaPasOpr,null);
    bilanceRozdil = sumaAkt+sumaAktOpr+sumaPas+sumaPasOpr;
    setCellValue(listNr,10,11,bilanceRozdil,null);

    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputBilanceProjekt() 
  {
    CellStyle style = null;
      
    int rowNr = 14;
    int colNr;
    
    if(wb.getNumberOfSheets()<=listNr) return;
    
    CellStyle[] styles;
    CellStyle[] localStyle = new CellStyle[9];
    CellStyle[] menaStyle = new CellStyle[9];
    for(short i=8; i<=16; i++) {
      localStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr)!=null && wb.getSheetAt(listNr).getRow(rowNr).getCell(i)!=null)?wb.getSheetAt(listNr).getRow(rowNr).getCell(i).getCellStyle():null;
      menaStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr+1)!=null && wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i)!=null) ? wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i).getCellStyle() : null;
      clearCell(listNr,rowNr,i);
      clearCell(listNr,rowNr+1,i);
    }
    
    double sumaAkt = 0.0D, sumaAktOpr = 0.0D;
    double sumaPas = 0.0D, sumaPasOpr = 0.0D;

    String lastList = "";
    int lastRadek = 0;
    
    ViewObject vo = dm.findViewObject("VwKpDokladbilanceprojektView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad
                      + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String listNazev = (String) row.getAttribute("List");
      String radekText = (String) row.getAttribute("Radektext");
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      String mena = (String)row.getAttribute("SMena");
      String projekt = (String)row.getAttribute("Projekt");

      if("Aktiva".equals(listNazev)) {
        if(sloupec == 1) sumaAkt += castkaLocal;
        else if(sloupec == 3) sumaAktOpr += castkaLocal;
      }
      else if("Pasiva".equals(listNazev)) {
        if(sloupec == 1) sumaPas += castkaLocal;
        else if(sloupec == 3) sumaPasOpr += castkaLocal;
      }
      
      styles = localStyle;
      
      boolean opravna = false;
      if(lastRadek==radek && lastList.equals(listNazev) && sloupec==3) {
        opravna=true;
        rowNr--;
      }
      
      colNr = 8;
      setCellValue(listNr,rowNr,colNr,listNazev,styles[0]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,radekText,styles[1]);
      colNr+=2;
      if(!opravna) setCellValue(listNr,rowNr,colNr,""+radek,styles[3]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,castkaLocal,styles[4]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,castkaMena,styles[5]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(sloupec==3?castkaLocal:0),styles[6]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(sloupec==3?castkaMena:0),styles[7]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,mena,styles[8]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,projekt,styles[2]);

      lastList = listNazev;
      lastRadek = radek;
      
      rowNr++;
    }
    setCellValue(listNr,8,12,sumaAkt,null);
    setCellValue(listNr,8,13,sumaAktOpr,null);
    setCellValue(listNr,9,12,sumaPas,null);
    setCellValue(listNr,9,13,sumaPasOpr,null);
    setCellValue(listNr,8,11,sumaAkt+sumaAktOpr,null);
    setCellValue(listNr,9,11,sumaPas+sumaPasOpr,null);
    bilanceRozdil = sumaAkt+sumaAktOpr+sumaPas+sumaPasOpr;
    setCellValue(listNr,10,11,bilanceRozdil,null);

    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputBilanceOdbor() 
  {
    CellStyle style = null;
      
    int rowNr = 14;
    int colNr;
    
    if(wb.getNumberOfSheets()<=listNr) return;
    
    CellStyle[] styles;
    CellStyle[] localStyle = new CellStyle[9];
    CellStyle[] menaStyle = new CellStyle[9];
    for(short i=8; i<=16; i++) {
      localStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr)!=null && wb.getSheetAt(listNr).getRow(rowNr).getCell(i)!=null)?wb.getSheetAt(listNr).getRow(rowNr).getCell(i).getCellStyle():null;
      menaStyle[i-8] = (wb!=null && wb.getSheetAt(listNr)!=null && wb.getSheetAt(listNr).getRow(rowNr+1)!=null && wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i)!=null) ? wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i).getCellStyle() : null;
      clearCell(listNr,rowNr,i);
      clearCell(listNr,rowNr+1,i);
    }
    
    double sumaAkt = 0.0D, sumaAktOpr = 0.0D;
    double sumaPas = 0.0D, sumaPasOpr = 0.0D;

    String lastList = "";
    int lastRadek = 0;
    
    ViewObject vo = dm.findViewObject("VwKpDokladbilanceodborView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad
                      + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : ""));
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String listNazev = (String) row.getAttribute("List");
      String radekText = (String) row.getAttribute("Radektext");
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      String mena = (String)row.getAttribute("SMena");
      String odbor = (String)row.getAttribute("Odbor");

      if("Aktiva".equals(listNazev)) {
        if(sloupec == 1) sumaAkt += castkaLocal;
        else if(sloupec == 3) sumaAktOpr += castkaLocal;
      }
      else if("Pasiva".equals(listNazev)) {
        if(sloupec == 1) sumaPas += castkaLocal;
        else if(sloupec == 3) sumaPasOpr += castkaLocal;
      }
      
      styles = localStyle;
      
      boolean opravna = false;
      if(lastRadek==radek && lastList.equals(listNazev) && sloupec==3) {
        opravna=true;
        rowNr--;
      }
      
      colNr = 8;
      setCellValue(listNr,rowNr,colNr,listNazev,styles[0]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,radekText,styles[1]);
      colNr+=2;
      if(!opravna) setCellValue(listNr,rowNr,colNr,""+radek,styles[3]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,castkaLocal,styles[4]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,castkaMena,styles[5]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(sloupec==3?castkaLocal:0),styles[6]);
      colNr++;
      setCellValue(listNr,rowNr,colNr,(sloupec==3?castkaMena:0),styles[7]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,mena,styles[8]);
      colNr++;
      if(!opravna) setCellValue(listNr,rowNr,colNr,odbor,styles[2]);

      lastList = listNazev;
      lastRadek = radek;
      
      rowNr++;
    }
    setCellValue(listNr,8,12,sumaAkt,null);
    setCellValue(listNr,8,13,sumaAktOpr,null);
    setCellValue(listNr,9,12,sumaPas,null);
    setCellValue(listNr,9,13,sumaPasOpr,null);
    setCellValue(listNr,8,11,sumaAkt+sumaAktOpr,null);
    setCellValue(listNr,9,11,sumaPas+sumaPasOpr,null);
    bilanceRozdil = sumaAkt+sumaAktOpr+sumaPas+sumaPasOpr;
    setCellValue(listNr,10,11,bilanceRozdil,null);

    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputMPOP() 
  {
    CellStyle style = null;

    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
      
    int rowNr = 5;
    int colNr;
    
    if(wb.getNumberOfSheets()<=listNr) return;
    
    Number hlpNum = null;
    
    ViewObject vo;
    if(!mis) vo = dm.findViewObject("VwKpDokladspolodborprojektView1");
    else vo = dm.findViewObject("VwKpDokladspolodborprojMisView1");
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
      boolean cMis = (mis && "1".equals( row.getAttribute("CMis") ));

      hlpNum = (Number) row.getAttribute("IdKtgodbor");
      String idOdbor = hlpNum==null ? "" : hlpNum.toString();
      hlpNum = (Number) row.getAttribute("IdKtgprojekt");
      String idProjekt = hlpNum==null ? "" : hlpNum.toString();

      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      //double castkaCZK = ((Number)row.getAttribute("NdCastkaczk")).doubleValue();
      double castkaCZK = ( row.getAttribute("NdCastkaczk") != null ? ((Number)row.getAttribute("NdCastkaczk")).doubleValue() : 0 );
      if(cMis) style = styleMis;
      else style = null;
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
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Projektano"), style );
      colNr++;
      setCellValue(listNr,rowNr,colNr,idProjekt,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,projekt,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,idOdbor,style);
      colNr++;
      setCellValue(listNr,rowNr,colNr,odbor,style);
      colNr++;
      if(clenJTFG) setCellValue(listNr,rowNr,colNr,"�len JTFG kons.",style);
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), style );
      colNr++;
      if(idKtgSpolecnost.intValue()==1 || idKtgSpolecnost.intValue()==5000) setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SSpravce"), style );
      colNr++;
      if(idKtgSpolecnost.intValue()==1 || idKtgSpolecnost.intValue()==5000) setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SKo"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegment"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegmentboss"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Pmanager"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegment"), style );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegmentboss"), style );
      colNr++;
      if(mis && cMis) setCellValue( listNr, rowNr, colNr, "MIS", style );
      colNr++;
      if(mis && cMis) setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Typtransakce"), style );
      colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SPopisoriginal"),null);
      
      rowNr++;
    }
    if ( pocet <= 0 ) {
      setCellValue( listNr, rowNr+1, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( listNr, rowNr+2, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputKamil() 
  {
    CellStyle style = null;
    if(idDokladKamil==null) return;
/*
    ViewObject vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("ID = " + idDokladKamil);
    int listNr;    
    int rowNr;    
    int koef = 1;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      String mena = (String)row.getAttribute("SMena");
      int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double opravnaCastkaLocal = ((Number)row.getAttribute("NdOpravnacastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      double opravnaCastkaMena = ((Number)row.getAttribute("NdOpravnacastkamena")).doubleValue();

      if(lastList != list || lastRadek != radek) 
      {
        castka1 = 0.0D;
        castka2 = 0.0D;
        castka1opr = 0.0D;
        castka2opr = 0.0D;
        castkaCzk = 0.0D;
        castkaCzkopr = 0.0D;
      }
      
      //kdyz je mena spolecnosti mena => v prvnim sloupci bude lokalni castka
      if(menaSpol.equals(mena)) 
      {
        castka1 = castkaLocal;
        castka1opr = opravnaCastkaLocal;
      }
      //jinak se nascitavaji hodnoty do druheho sloupce
      else 
      {
        castka2 += castkaLocal;
        castka2opr += opravnaCastkaLocal;
      }
      //treti sloupec
      //kdyz je mena spolecnosti CZK
      if("CZK".equals(menaSpol)) 
      {
        //a mena je CZK, pak se lokalni castka pricte do tretiho sloupce
        if("CZK".equals(mena)) 
        {
          castkaCzk += castkaLocal;
          castkaCzkopr += opravnaCastkaLocal;
        }
        //a mena neni CZK, pak se lokalni castka odecte od tretiho sloupce
        else 
        {
          castkaCzk -= castkaLocal;
          castkaCzkopr -= opravnaCastkaLocal;
        }
      }
      //kdyz mena spolecnosti neni CZK
      else 
      {
        //do tretiho slouce se vlozi castka v mene CZK
        if("CZK".equals(mena)) 
        {
          castkaCzk = castkaMena;
          castkaCzkopr = opravnaCastkaMena;
        }
      }

      listNr = list - 1;
      rowNr = radek + 6;
      int colNr = 2;
      setCellValue(listNr,rowNr,colNr,koef*castka1,style);
      colNr++;
      //setCellValue(listNr,rowNr,colNr,koef*castka2,style);
      //colNr++;
      setCellValue(listNr,rowNr,colNr,koef*castkaCzk,style);

      if(list == 1) {
        colNr++;
        setCellValue(listNr,rowNr,colNr,koef*castka1opr,style);
        colNr++;
        //setCellValue(listNr,rowNr,colNr,koef*castka2opr,style);
        //colNr++;
        setCellValue(listNr,rowNr,colNr,koef*castkaCzkopr,style);
      }

      lastList = list;
      lastRadek = radek;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
*/
  }

  private void outputPodnikatel() 
  {
    CellStyle style = null;
    CellStyle pStyle = null;
    
    if(!baseDoklad) {
      wb.removeSheetAt(listNr);
      wb.removeSheetAt(listNr);
      wb.removeSheetAt(listNr);
      return;
    }
    

    ViewObject vo = dm.findViewObject("VwKpDokladView1");
    vo.clearCache();    
    //NEW
    //ViewObject vo = dm.createViewObject("VwKpDokladView"+idDoklad,"cz.jtbank.konsolidace.doklady.VwKpDokladView");
    vo.setWhereClause("ID_DOKLAD = " + idDokladPodnikatel + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")+" AND S_MENA='"+menaSpol+"'");
    int rowNr;   
    int colNr = 8;
    int koef  = 1;
    short i = 8; //esc

    while(vo.hasNext()) 
    {      
      Row row = vo.next();
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      String mena = (String)row.getAttribute("SMena");
      int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double opravnaCastkaLocal = ((Number)row.getAttribute("NdOpravnacastkalocal")).doubleValue();
    
      //esc 03/2011 rowNr = radek + 3;
      rowNr = radek + 13;
      if(list==1 && radek>=Constants.ROW_PODNIKATEL_PASIVA) { 
        rowNr++;
        colNr = 10; //esc 23.03
        koef = -1;
      } else { 
               koef = 1;
               colNr = 8;  //23.03
             }
      
      // esc 03/2011 int colNr = 3;
      //int colNr = 10; Netto
      
      if(list>1){ // vysledovka
              list=2;//TESTOVACI RESENI - POZDEJI ODSTRANIT
              colNr = 10; //esc 23.03
              koef = 1; //esc 24.03
      }

     // style = wb.getSheetAt(listNr+list-1).getRow(rowNr).getCell((short)colNr).getCellStyle() ;                  
      //setCellValue(listNr+list-1,rowNr,colNr,koef*castkaLocal,style);
      setCellValue(listNr+list-1,rowNr,colNr,koef*castkaLocal,style,false,"# ##0");
            
      if(list == 1)
       // setCellValue(listNr+list-1,rowNr,colNr+1,-1*koef*opravnaCastkaLocal,style);
      {
       setCellValue(listNr+list-1,rowNr,colNr+1,-1*koef*opravnaCastkaLocal,style,false,"# ##0");
       }
     }
    
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
    
    listNr+=3;//3 v pripade zjednoduseneho vystupu
    
  }

  private void outputRozdilyIFRS() 
  {
    int rowNr=2;
    Number hlpNum = null;

    ViewObject vo = dm.findViewObject("KpDatDokladrozdilyView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad);
    vo.setOrderByClause("ID");
    if( !vo.hasNext() ) 
    {
      wb.removeSheetAt(listNr);
      listNr--;
    }
    while ( vo.hasNext() ) {
      Row row = vo.next();
      rowNr++;
      int colNr = 0;
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost"), null );
      colNr++;
      hlpNum = (Number)row.getAttribute("NdCastkamena");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMenaIfrs"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetIfrs"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SpolecnostIfrs"), null );
      colNr++;
      hlpNum = (Number)row.getAttribute("NdCastkamenaIfrs");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue(), null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
    listNr++;
  }
  
  private void outputCashFlow() {
    int rowNr=3;

    ViewObject vo = dm.findViewObject("VwCashflowView1");
    vo.clearCache();
    vo.setWhereClause("link='"+dbLink+"' and sid='"+topasId+"'");
    if( !vo.hasNext() ) 
    {
      wb.removeSheetAt(listNr);
      listNr--;
    }
    while(vo.hasNext() && rowNr<Constants.MAX_POCET_RADKU_EXCEL) 
    {
      int colNr = 1;
      Row row = vo.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Nazev") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CharCastky") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("TypSmlouvy") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DatumPredpisu");
      if(hlpDt!=null) setCellValue( listNr, rowNr, colNr, sdf.format(hlpDt.dateValue()) , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Castka");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue() , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mena") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Uhrada");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue() , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("KUhrade");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue() , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Counterparty") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Smlouva") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Poradi");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CmlNazev") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Predmet") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("VarSymbol") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("BuSpolecnosti") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("UcetSalda") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Protiucet") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CharSmlouvy") , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("ManazerId");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("ProjektId");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("DruhSmlouvy") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("PopisDruhu") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("StavSmlouvy") , null ); 
/*
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Stavid");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
      colNr++;
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("P1v0");
      if(hlpNum!=null) setCellValue( listNr, rowNr, colNr, ""+hlpNum , null ); 
*/

      rowNr++;
    }
    if (vo.hasNext()) {

      setCellValue( listNr, rowNr, 1, "D A T A    N E J S O U   K O M P L E T N � - po�et ��dk� p�esahuje mo�nosti excelu" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    listNr++;
  }
  
  protected boolean outputData () 
  {
/*
    if(jenomPs>=0 && wb.getSheetName(10).startsWith("Bil")) {
      for(int i=0;i<5;i++) 
        wb.removeSheetAt(toDel);
    }
*/
    Font font = wb.createFont();
    font.setColor(IndexedColors.BLUE.getIndex());
    styleJtfg = wb.createCellStyle();
    styleJtfg.setFont(font);

    Font font2 = wb.createFont();
    font2.setColor(IndexedColors.GREEN.getIndex());
    styleMis = wb.createCellStyle();
    styleMis.setFont(font2);

    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputPrvniListy();
    logger.info("Dogenerovany prvni listy");
    listNr=5;
    
    if(jenomPs !=  -1) {
      outputBilance();
      logger.info("Dogenerovan list bilance");
      listNr++;
      
      outputDetail();
      logger.info("Dogenerovan list detail"); 
      listNr++;
      
      outputZustatky();
      logger.info("Dogenerovan list zustatky");
      listNr++;
      
      outputProtistrany();
      logger.info("Dogenerovan list protistrany");
      listNr++;
      
      outputMenovaPozice();
      logger.info("Dogenerovan list menova pozice");
      listNr++;
  
      outputUverovaPozice();
      listNr++;
      outputUPSouhrn();
      logger.info("Dogenerovany listy pro uverove pozice");
      listNr++;
      
      outputKamil();
      logger.info("Dogenerovan list Kamil");
      listNr++;
      
      outputBilanceProtistrana();
      logger.info("Dogenerovan list bilance - protistrana");
      listNr++;
      
      outputBilanceProjekt();
      logger.info("Dogenerovan list bilance - projekt");
      listNr++;
      
      outputBilanceOdbor();
      logger.info("Dogenerovan list bilance - odbor");
      listNr++;
      
      outputMPOP();
      logger.info("Dogenerovan list suma Mena/Protistrana/Odbor/Projekt");
      listNr++;

      outputPodnikatel();
      logger.info("Dogenerovany listy bilance podnikatel");

      outputRozdilyIFRS();
      logger.info("Dogenerovan list rozdily primarni vs. IFRS doklad");
      
      outputCashFlow();
      logger.info("Dogenerovan list cash flow");
    }
/*
    else 
    {
      int maxList = 2;
      while(wb.getNumberOfSheets()>maxList) wb.removeSheetAt(maxList);
    }
*/
    return true;
  }

  public String getWarning() {
    StringBuffer ret = new StringBuffer();
    bilanceRozdil = Math.round(bilanceRozdil);
    sumaZiskZtrata = Math.round(sumaZiskZtrata);
    
    if ( bilanceRozdil != 0 )
      ret.append( "Rozd�l bilance=" + bilanceRozdil + "<br>" );
    if ( noDetail ) 
      ret.append( "��dn� ��etn� z�znam.<br>" );
    if ( sumaZiskZtrata != 0 )
      ret.append( "Nesouhlas� zisk ztr�ta v�sledovka/pasiva. Rozd�l="+sumaZiskZtrata+"<br>" );
    if ( pocetNezarazenych != 0 )
      ret.append( "Neza�azen�ch z�znam�="+pocetNezarazenych+"<br>" );
    if ( pocetTestICO != 0 )
      ret.append( "Z�znam� bez I�="+pocetTestICO );
    
    return ( (ret.length() == 0 ) ? "OK" : ret.toString() );
  }
  
  public String getNazevSpol() 
  {
    return nazevSpol;
  }

  public String getIdSpol() 
  {
    return idKtgSpolecnost.toString();
  }

  public String getIdDoklad() 
  {
    return idDoklad.toString();
  }
  
  public String getTypeDoklad()
  {
    String ret = flagIfrs ? "IFRS " : "";
//    ret += flagPs ? "s PS" : "";
    return ret;
  }

  public String getRkcFlag() 
  {
    return rkcFlag;
  }
  
  public void deleteAllOriginalFiles() throws IOException {
    // MIGRATED: Platform-independent file separator
    int indexDir = getFileAbsoluteName().lastIndexOf(File.separator);
    String dirName = getFileAbsoluteName().substring(0,indexDir);
    int podtrzitko = getFileName().lastIndexOf('_');
    int tecka = getFileName().lastIndexOf('.');
    String filter = getFileName().substring(podtrzitko+1,tecka);
    File dir = new File(dirName);
    FileFilter ff = getDeleteFileFilter(filter,specialDoklad);
    File[] arr = dir.listFiles(ff);
    if(arr != null) {
      for( int i=0; i<arr.length; i++ ) 
      {
        logger.info("Mazani souboru "+arr[i].getName()+": "+arr[i].delete());        
        //arr[i].delete();
      }
    }
  }
  
  

  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportDoklady ed = new ESExportDoklady(dm,
                                               new Number(1774775),
                                               null,
                                               null,
                                               0);
      ed.deleteAllOriginalFiles();
      ed.excelOutput();

      // MIGRATED: Platform-independent Excel invocation
      Runtime rt = Runtime.getRuntime();
      String excelPath = System.getProperty("kis.excel.path",
                                           PathConstants.isWindows()
                                           ? "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE"
                                           : "libreoffice --calc");
      String[] callAndArgs = { excelPath, "" };
      callAndArgs[1]=ed.getFileAbsoluteName();
      if (!PathConstants.isLinux() || System.getenv("DISPLAY") != null) {
        Process pExcel = rt.exec(callAndArgs);
      } else {
        logger.info("Skipping Excel auto-open in headless Linux environment. File: " + ed.getFileAbsoluteName());
      }
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
}
