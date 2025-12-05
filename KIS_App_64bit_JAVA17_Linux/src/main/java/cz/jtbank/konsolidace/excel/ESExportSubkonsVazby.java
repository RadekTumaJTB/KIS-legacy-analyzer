package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import cz.jtbank.konsolidace.common.Constants;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;
import java.util.*;
import java.text.SimpleDateFormat;

public class ESExportSubkonsVazby extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsVazby.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_VAZBY)); }

  private static final int PULKA = 9;
  //private static final String WHERE_KURZOVE_ROZDILY = " c_typ = 'K' and nl_radek = 53 and nl_poradiList = 5 ";

  private String[] listy;
  private String lsTxt, psTxt, sumTxt;

  private ApplicationModule dm;
  private int subkonsId;
  private java.sql.Date datum;

  private String nazevSub;
  private String menaSub;
  private String souborPredpona;

  private int dokladId;
  private int parentId;
  
  private boolean blue;

  int idSpol, idSpolProti;
  Number idVazby, idVazbyPar;

  private String localeSpol;
  
  private Map levaSumy, pravaSumy;
  private Map levaRadkySumy, pravaRadkySumy;
  private Map kontrMap;
  
  private int listNr = 0;
  private int rowNr = 3;
  
  private CellStyle styleBold;
  private CellStyle styleRed;
  
  private String errorUserIndex = "Mezi vazbami se vyskytla vazba se zakrtnutou 1 nebo 2, kter se ve vyprovanch sub-konsolidan vazbch nesm vyskytovat!!!";

  public ESExportSubkonsVazby(ApplicationModule dokladyModule,
                              int subkonsId,
                              java.sql.Date datum,
                              boolean blue)
  {
    dm = dokladyModule;
    this.subkonsId = subkonsId;
    this.datum = datum;
    this.blue = blue;
    init();
    setLocaleDep();
  }

  private boolean isVyjimka = false;
  
  private void init() {
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+subkonsId);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSub = (String) row.getAttribute("SNazev");
      menaSub = (String) row.getAttribute("SMena");
      souborPredpona = (String) row.getAttribute("SSouborpredpona");
      localeSpol = "cs_CZ";//(String) row.getAttribute("SLocale");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    String special = blue ? "Blue" : "";
    setFileName ( "SubVazby"+special+subkonsId+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredpona+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona = (localeSpol==null || localeSpol.length()<1) ? 
                     "SablonaSubkonsVV.xlsx" :
                     "SablonaSubkonsVV_"+localeSpol+".xlsx";
    setSablona( Constants.SABLONY_FILES_PATH+sablona );
   
    isVyjimka = (subkonsId==10150);
  }

  protected void outputHeader() 
  {
    String nazevSpol = null, nazevSpolProti = null;
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = " + idSpol);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("SNazev");
    }
    vo.closeRowSet();
    vo.clearCache();
    vo.setWhereClause("ID = " + idSpolProti);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpolProti = (String) row.getAttribute("SNazev");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    if(styleBold == null) 
    {
      Font font = wb.createFont();
      font.setBold(true);
      styleBold = wb.createCellStyle();
      styleBold.setFont(font);
    }

    if(styleRed == null) 
    {
      Font font = wb.createFont();
      font.setColor(IndexedColors.RED.getIndex());
      styleRed = wb.createCellStyle();
      styleRed.setFont(font);
    }

    setCellValue(listNr,rowNr,0,nazevSpol,styleBold);
    setCellValue(listNr,rowNr,PULKA,nazevSpolProti,styleBold);
    
    rowNr++;
  }

  protected void outputSumy() 
  {
    setCellValue(listNr,rowNr,0,sumTxt,null);
    
    HashSet meny = new HashSet();
    if(!levaSumy.isEmpty())
      meny.addAll(levaSumy.keySet());
    if(!pravaSumy.isEmpty())
      meny.addAll(pravaSumy.keySet());
    
    if(!meny.isEmpty()) 
    {
      Iterator iter = meny.iterator();
 
      while(iter.hasNext()) 
      {
        String mena = (String) iter.next();
        Number castkaLeva = (Number) levaSumy.get(mena);
        Number castkaPrava = (Number) pravaSumy.get(mena);
        setCellValue(listNr,rowNr,3,mena,null);
        double valLeva = (castkaLeva==null) ? 0.0 : castkaLeva.doubleValue();
        setCellValue(listNr,rowNr,5,valLeva,null);
        setCellValue(listNr,rowNr,PULKA+3,mena,null);
        double valPrava = (castkaPrava==null) ? 0.0 : castkaPrava.doubleValue();
        setCellValue(listNr,rowNr,PULKA+5,valPrava,null);
        rowNr++;
      }
    }
    rowNr+=2;
  }

  protected void outputSumyRadky() 
  {
    setCellValue(listNr,rowNr,0,"sum",styleBold);
    setCellValue(listNr,rowNr,PULKA+1,"kontrola x subkons.excelu",styleBold);
    rowNr++;
    
    int origRowNr = rowNr;
    int levaRowNr = rowNr, pravaRowNr = rowNr;

    if(!levaRadkySumy.isEmpty()) {
      Iterator iter = levaRadkySumy.keySet().iterator();
      while(iter.hasNext()) 
      {
        String popis = (String) iter.next();
        Number val = (Number) levaRadkySumy.get(popis);
        
        Number kontrVal = (Number) kontrMap.get(popis);
        kontrMap.remove(popis);
        
        if(popis!=null && val!=null) 
        {
          setCellValue(listNr,rowNr,0,popis,null);
          setCellValue(listNr,rowNr,PULKA-1,val.doubleValue(),null);
          
          if(kontrVal!=null) {
            CellStyle style = Math.round(val.doubleValue())==Math.round(kontrVal.doubleValue()) ? null : styleRed;
            setCellValue(listNr,rowNr,PULKA+1,kontrVal.doubleValue(),style);
          }
          rowNr++;
          levaRowNr++;
        }
      }
    }
    if(!kontrMap.isEmpty()) {
      Iterator iter = kontrMap.keySet().iterator();
      while(iter.hasNext()) 
      {
        String popis = (String) iter.next();
        Number kontrVal = (Number) kontrMap.get(popis);
        
        if(popis!=null && kontrVal!=null) 
        {
          setCellValue(listNr,rowNr,0,popis,null);
          setCellValue(listNr,rowNr,PULKA+1,kontrVal.doubleValue(),styleRed);
          rowNr++;
          levaRowNr++;
        }
      }
    }

    rowNr = origRowNr;
    if(!pravaRadkySumy.isEmpty()) {
      Iterator iter = pravaRadkySumy.keySet().iterator();
      while(iter.hasNext()) 
      {
        String popis = (String) iter.next();
        Number val = (Number) pravaRadkySumy.get(popis);
        
        if(popis!=null && val!=null) 
        {
          setCellValue(listNr,rowNr,PULKA,popis,null);
          setCellValue(listNr,rowNr,2*PULKA-1,val.doubleValue(),null);
          rowNr++;
          pravaRowNr++;
        }
      }
    }
    
    rowNr = 1+(levaRowNr>pravaRowNr ? levaRowNr : pravaRowNr);

    setCellValue(listNr,rowNr,0,"sum KR",styleBold);
    setCellValue(listNr,rowNr,2*PULKA,sumKr,null);
    setCellValue(listNr,rowNr,2*PULKA+1,sumP123,null);
    setCellValue(listNr,rowNr,2*PULKA+2,sumKr,null);
  }
  
  private Number kurz, eliminace, pasiva123;
  private double sumKr = 0, sumP123 = 0;
  private Number kurzVyjimka;

  private Number lastList, lastRadek;
  private String lastMena;
  private boolean lastLeva;
  
  private void setKontrola() 
  {
    kontrMap = new TreeMap();
    
    ViewObject vo = dm.findViewObject("VwKpDokladvvexcelView1");
    vo.clearCache();
    vo.setWhereClause("IDDOKLAD = "+dokladId);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      
      String key = listy[list] + " - " + radek;
      if(!(list==2 && radek==123) && !(list==5 && radek==53))
        kontrMap.put(key, castkaLocal);
    }
    vo.closeRowSet();
  }
  
  private void getKurz(boolean leva, Number list, Number radek, String mena, /*novinka*/ Number castka, boolean vyjimkaTest) 
  {
    eliminace = null;
    kurz = null;
    
    String where = null;

    //if(!(lastLeva==leva && list.equals(lastList) && radek.equals(lastRadek) && mena.equals(lastMena))) {
    if(vyjimkaTest && isVyjimka) {
      kurz = kurzVyjimka;
    }
    else {
      ViewObject vo = dm.findViewObject("KpDatDokladsubkonvazbydenikKurzView1");
      vo.clearCache();
      if(leva) {
        where = "ID_DOKLAD = " + dokladId +" AND C_TYP = 'V' "+
                "and id_ktgUcetniSpolecnostFrom = "+idSpol+" and id_ktgUcetniSpolecnostTo = "+idSpolProti+" "+
                "and nl_radek = "+radek+" and nl_poradiList = "+list+" and s_mena = '"+mena+"'";
      }
      else 
      {
        where = "ID_DOKLAD = " + dokladId +" AND C_TYP = 'V' "+
                "and id_ktgUcetniSpolecnostFrom = "+idSpolProti+" and id_ktgUcetniSpolecnostTo = "+idSpol+" "+
                "and nl_radek = "+radek+" and nl_poradiList = "+list+" and s_mena = '"+mena+"'";
      }
      vo.setWhereClause(where);
      if(vo.hasNext()) 
      {
        Row row = vo.next();
  
        kurz = (Number) row.getAttribute("Kurz");
        //eliminace = (Number) row.getAttribute("Eliminace");
      }
      else 
      {
        kurz = kurzVyjimka;
        String errMsg = "Neni k dispozici kurz tam, kde byt ma!!! WHERE na Kp_Dat_DokladSubKonVazbyDenik je "+where+". Pouzivam kurz z 'K': "+kurzVyjimka;
        logger.error(errMsg);
        System.out.println(errMsg);
      }
      vo.closeRowSet();
    }
    
    if(kurz!=null && castka!=null && kurz.doubleValue()!=0.0) 
    {
      eliminace = castka.divide(kurz);
      eliminace = eliminace.multiply(-1);
    }
    
    try {
      eliminace.doubleValue();
    }
    catch(NullPointerException e) { 
      //System.out.println(eliminace);
      //e.printStackTrace();
System.out.println("castka="+castka+",kurz="+kurz+",where="+where);
      String errMsg = "Neni k dispozici kurz tam, kde byt ma!!! WHERE na Kp_Dat_DokladSubKonVazbyDenik je "+where+". Pouzivam kurz 1";
      logger.error(errMsg);
      //System.out.println(errMsg);
      eliminace = castka;   
    }

    lastLeva = leva;
    lastList = list;
    lastRadek = radek;
    lastMena = mena;
  }

  private void getPasiva123(Number list, Number radek) 
  {
    pasiva123 = null;
  
    ViewObject vo = dm.findViewObject("KpDatDokladsubkonvazbydenikView2");
    vo.clearCache();
    String where = "ID_DOKLAD = " + dokladId +" AND C_TYP = 'K' "+
              "and id_ktgUcetniSpolecnostFrom = "+idSpol+" and id_ktgUcetniSpolecnostTo = "+idSpolProti+" "+
              Utils.getWhereHospodarskyVysledek(dokladId)+
              "and nl_parporadilist = "+list+" and nl_parradek = "+radek+" ";
    if(idVazby != null) where += "and id_dokladVazby = "+idVazby;
    else where += "and id_dokladVazbyPar = "+idVazbyPar;
    vo.setWhereClause(where);
    if(vo.hasNext()) 
    {
      Row row = vo.next();

      pasiva123 = (Number) row.getAttribute("NdCastka");
    }
    vo.closeRowSet();
    
    
  }

  private void outputPresne() throws KisException
  {
//    System.out.println(idSpol+":"+idSpolProti);
//    System.out.println(idVazby+":"+idVazbyPar);
    
    ViewObject vo = dm.findViewObject("VwKpDokladvazbyvyparovaneView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idVazby+" and s_locale = '"+localeSpol+"'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
/*
if(null!=row.getAttribute("NlUserindexvazba1") && !"0".equals(row.getAttribute("NlUserindexvazba1").toString())) 
{
  throw new KisException(errorUserIndex+" (idSpol,idSpolProti,idVazby,idVazbyPar)=("+idSpol+","+idSpolProti+","+idVazby+","+idVazbyPar+")",
                         new KisException(Constants.ERR_MESSAGE_ONLY));
}
if(null!=row.getAttribute("NlUserindexvazba1") && !"0".equals(row.getAttribute("NlUserindexvazba1").toString())) 
{
  throw new KisException(errorUserIndex+" (idSpol,idSpolProti,idVazby,idVazbyPar)=("+idSpol+","+idSpolProti+","+idVazby+","+idVazbyPar+")",
                         new KisException(Constants.ERR_MESSAGE_ONLY));
}
*/
      Number idNum = (Number) row.getAttribute("Id");
      String levaRadekText = (String) row.getAttribute("SPopis");
      String levaMena = (String) row.getAttribute("SMena");
      String levaUcet = (String) row.getAttribute("SUcet");
      Number levaCastkaMena = (Number) row.getAttribute("NdCastkamena");
      Number levaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
      Number levaList = (Number) row.getAttribute("NlPoradilist");
      Number levaRadek = (Number) row.getAttribute("NlRadek");

      String pravaRadekText = (String) row.getAttribute("ParSPopis");
      String pravaMena = (String) row.getAttribute("ParSMena");
      String pravaUcet = (String) row.getAttribute("ParSUcet");
      Number pravaCastkaMena = (Number) row.getAttribute("ParNdCastkamena");
      Number pravaCastkaLocal = (Number) row.getAttribute("ParNdCastkalocal");
      Number pravaList = (Number) row.getAttribute("ParNlPoradilist");
      Number pravaRadek = (Number) row.getAttribute("ParNlRadek");

      setRadekSumyLeva(levaMena, levaCastkaMena);
      setRadekSumyPrava(pravaMena, pravaCastkaMena);
      
      double kr = 0;
      int koef = 1;

      getKurz(true, levaList, levaRadek, levaMena, levaCastkaLocal, true);

      int colNr = 0;
      if(levaRadek!=null) setCellValue(listNr,rowNr,colNr,levaRadek.toString(),null);
      colNr++;
      if(levaList!=null) {
        setCellValue(listNr,rowNr,colNr,listy[levaList.intValue()],null);
        if(levaList.intValue()==2 || levaList.intValue()==4 || levaList.intValue()==5) koef = -1;
      }
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaRadekText,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaMena,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaUcet,null);
      colNr++;
      if(levaCastkaMena!=null) setCellValue(listNr,rowNr,colNr,levaCastkaMena.doubleValue(),null);
      colNr++;
      if(levaCastkaLocal!=null) setCellValue(listNr,rowNr,colNr,levaCastkaLocal.doubleValue(),null);
      colNr++;
      if(kurz!=null) setCellValue(listNr,rowNr,colNr,kurz.doubleValue(),null);
      colNr++;
      if(eliminace!=null) {
        setCellValue(listNr,rowNr,colNr,koef*eliminace.doubleValue(),null);
        if(levaList!=null) {
          if(levaList.intValue()==1 || levaList.intValue()==2 || levaList.intValue()==5) kr = eliminace.doubleValue();
          setSumyLevaRadky(levaRadek, listy[levaList.intValue()], koef);
        }
      }
      colNr++;

      koef = 1;
      getKurz(false, pravaList, pravaRadek, pravaMena, pravaCastkaLocal, false/*true*/);

      colNr = PULKA;
      if(pravaRadek!=null) setCellValue(listNr,rowNr,colNr,pravaRadek.toString(),null);
      colNr++;
      if(pravaList!=null) {
        setCellValue(listNr,rowNr,colNr,listy[pravaList.intValue()],null);
        if(pravaList.intValue()==2 || pravaList.intValue()==4 || pravaList.intValue()==5) koef = -1;
      }
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaRadekText,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaMena,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaUcet,null);
      colNr++;
      if(pravaCastkaMena!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaMena.doubleValue(),null);
      colNr++;
      if(pravaCastkaLocal!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaLocal.doubleValue(),null);
      colNr++;
      if(kurz!=null) setCellValue(listNr,rowNr,colNr,kurz.doubleValue(),null);
      colNr++;
      if(eliminace!=null) {
        setCellValue(listNr,rowNr,colNr,koef*eliminace.doubleValue(),null);
        if(pravaList!=null) {//setSumyPravaRadky(pravaRadek, listy[pravaList.intValue()], koef);
          if(pravaList.intValue()==1 || pravaList.intValue()==2 || pravaList.intValue()==5) kr += eliminace.doubleValue();
          setSumyLevaRadky(pravaRadek, listy[pravaList.intValue()], koef);
        }
      }

      colNr = 2*PULKA;
      setCellValue(listNr,rowNr,colNr,kr,null);
      colNr++;
      getPasiva123(levaList, levaRadek/*, idNum*/);

      if(pasiva123!=null) {
        setCellValue(listNr,rowNr,colNr,pasiva123.doubleValue(),null);
        sumP123 += pasiva123.doubleValue();
      }
      sumKr += kr;
      colNr++;
      setCellValue(listNr,rowNr,colNr,kr,null);
      colNr++;
      
      rowNr++;
    }
    vo.closeRowSet();
  }

  private void outputObracene() throws KisException
  {
//    System.out.println(idSpol+":"+idSpolProti);
//    System.out.println(idVazby+":"+idVazbyPar);
    
    int origRowNr = rowNr;
    int levaRow = rowNr, pravaRow = rowNr;
    int colNr = 0;
    
    Number levaList = null, levaRadek = null;
    Number idNum = null;
    double kr = 0;
    ViewObject vo = dm.findViewObject("VwKpDokladvazbynevyparovaneView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idVazby+" and s_locale = '"+localeSpol+"'");
    vo.setOrderByClause("nl_poradilist, nl_radek, s_mena");
    int pocetRadkuLeva = vo.getRowCount();
    int pocetRadkuPrava = 0;
    double levaCM = 0.0;
    double levaCL = 0.0;
    double pravaCM = 0.0;
    double pravaCL = 0.0;
    double levaEli = 0.0;
    double pravaEli = 0.0;
    double kurzAgrLeva = 0.0;
    double kurzAgrPrava = 0.0;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
/*
if(null!=row.getAttribute("NlUserindexvazba") && !"0".equals(row.getAttribute("NlUserindexvazba").toString())) 
{
  throw new KisException(errorUserIndex+" L:(idSpol,idSpolProti,idVazby,idVazbyPar)=("+idSpol+","+idSpolProti+","+idVazby+","+idVazbyPar+")",
                         new KisException(Constants.ERR_MESSAGE_ONLY));
}
*/    
      idNum = (Number) row.getAttribute("Id");
      String levaRadekText = (String) row.getAttribute("SPopis");
      String levaMena = (String) row.getAttribute("SMena");
      String levaUcet = (String) row.getAttribute("SUcet");
      Number levaCastkaMena = (Number) row.getAttribute("NdCastkamena");
      Number levaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
      levaList = (Number) row.getAttribute("NlPoradilist");
      levaRadek = (Number) row.getAttribute("NlRadek");

      setRadekSumyLeva(levaMena, levaCastkaMena);
      
      int koef = 1;

      getKurz(true, levaList, levaRadek, levaMena, levaCastkaLocal, true);

      colNr = 0;
      if(levaRadek!=null) setCellValue(listNr,rowNr,colNr,levaRadek.toString(),null);
      colNr++;
      if(levaList!=null) {
        setCellValue(listNr,rowNr,colNr,listy[levaList.intValue()],null);
        if(levaList.intValue()==2 || levaList.intValue()==4 || levaList.intValue()==5) koef = -1;
      }
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaRadekText,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaMena,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaUcet,null);
      colNr++;
      if(levaCastkaMena!=null) {
        setCellValue(listNr,rowNr,colNr,levaCastkaMena.doubleValue(),null);
        levaCM += levaCastkaMena.doubleValue();
      }
      colNr++;
      if(levaCastkaLocal!=null) {
        setCellValue(listNr,rowNr,colNr,levaCastkaLocal.doubleValue(),null);
        levaCL += levaCastkaLocal.doubleValue();
      }
      colNr++;
      if(kurz!=null) {
        setCellValue(listNr,rowNr,colNr,kurz.doubleValue(),null);
        kurzAgrLeva = kurz.doubleValue();
      }
      colNr++;
      if(eliminace!=null) {
        if(pocetRadkuLeva == 1) setCellValue(listNr,rowNr,colNr,koef*eliminace.doubleValue(),null);
        else levaEli += koef*eliminace.doubleValue();
        
        if(levaList!=null) {
          if(levaList.intValue()==1 || levaList.intValue()==2 || levaList.intValue()==5) kr += eliminace.doubleValue();
          setSumyLevaRadky(levaRadek, listy[levaList.intValue()], koef);
        }
      }
      colNr++;
      
      rowNr++;
      levaRow++;
    }
    vo.closeRowSet();

    rowNr = origRowNr;
    vo.clearCache();
    vo.setWhereClause("s_locale = '"+localeSpol+"' and id_par = "+idVazbyPar+
                      " and id_doklad = ( select id_doklad from DB_JT.KP_Dat_dokladvazby where id = Abs ( "+idVazbyPar+" ) )");
    vo.setOrderByClause("nl_poradilist, nl_radek, s_mena");
    pocetRadkuPrava = vo.getRowCount();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
/*
if(null!=row.getAttribute("NlUserindexvazba") && !"0".equals(row.getAttribute("NlUserindexvazba").toString())) 
{
  throw new KisException(errorUserIndex+" P:(idSpol,idSpolProti,idVazby,idVazbyPar)=("+idSpol+","+idSpolProti+","+idVazby+","+idVazbyPar+")",
                         new KisException(Constants.ERR_MESSAGE_ONLY));
}
*/    
      String pravaRadekText = (String) row.getAttribute("SPopis");
      String pravaMena = (String) row.getAttribute("SMena");
      String pravaUcet = (String) row.getAttribute("SUcet");
      Number pravaCastkaMena = (Number) row.getAttribute("NdCastkamena");
      Number pravaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
      Number pravaList = (Number) row.getAttribute("NlPoradilist");
      Number pravaRadek = (Number) row.getAttribute("NlRadek");

      setRadekSumyPrava(pravaMena, pravaCastkaMena);
      
      int koef = 1;

      getKurz(false, pravaList, pravaRadek, pravaMena, pravaCastkaLocal, false/*true*/);

      colNr = PULKA;
      if(pravaRadek!=null) setCellValue(listNr,rowNr,colNr,pravaRadek.toString(),null);
      colNr++;
      if(pravaList!=null) {
        setCellValue(listNr,rowNr,colNr,listy[pravaList.intValue()],null);
        if(pravaList.intValue()==2 || pravaList.intValue()==4 || pravaList.intValue()==5) koef = -1;
      }
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaRadekText,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaMena,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaUcet,null);
      colNr++;
      if(pravaCastkaMena!=null) {
        setCellValue(listNr,rowNr,colNr,pravaCastkaMena.doubleValue(),null);
        pravaCM += pravaCastkaMena.doubleValue();
      }
      colNr++;
      if(pravaCastkaLocal!=null) {
        setCellValue(listNr,rowNr,colNr,pravaCastkaLocal.doubleValue(),null);
        pravaCL += pravaCastkaLocal.doubleValue();
      }
      colNr++;
      if(kurz!=null) {
        setCellValue(listNr,rowNr,colNr,kurz.doubleValue(),null);
        kurzAgrPrava = kurz.doubleValue();
      }
      colNr++;
      if(eliminace!=null) {
        if(pocetRadkuPrava == 1) setCellValue(listNr,rowNr,colNr,koef*eliminace.doubleValue(),null);
        else pravaEli += koef*eliminace.doubleValue();
        
        if(pravaList!=null) {//setSumyPravaRadky(pravaRadek, listy[pravaList.intValue()], koef);
          if(pravaList.intValue()==1 || pravaList.intValue()==2 || pravaList.intValue()==5) kr += eliminace.doubleValue();
          setSumyLevaRadky(pravaRadek, listy[pravaList.intValue()], koef);
        }
      }
      colNr++;
      
      rowNr++;
      pravaRow++;
    }
    vo.closeRowSet();

    if(pocetRadkuLeva>1 || pocetRadkuPrava>1) 
    {
      origRowNr = levaRow>pravaRow ? levaRow : pravaRow;
      rowNr = origRowNr + 1;
      colNr=5;
      setCellValue(listNr,origRowNr,colNr,levaCM,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,levaCL,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,kurzAgrLeva,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,levaEli,null);
      colNr++;
      colNr=PULKA+5;
      setCellValue(listNr,origRowNr,colNr,pravaCM,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,pravaCL,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,kurzAgrPrava,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,pravaEli,null);
      colNr++;
    }
    else {
      rowNr = levaRow>pravaRow ? levaRow : pravaRow;
    }
      
    colNr = 2*PULKA;
    setCellValue(listNr,origRowNr,colNr,kr,null);
    colNr++;
    getPasiva123(levaList, levaRadek/*, idNum*/);

    if(pasiva123!=null) {
      setCellValue(listNr,origRowNr,colNr,pasiva123.doubleValue(),null);
      sumP123 += pasiva123.doubleValue();
    }
    sumKr += kr;
    colNr++;
    setCellValue(listNr,origRowNr,colNr,kr,null);
    colNr++;
  }

  private void outputNullLeva() throws KisException
  {
//    System.out.println(idSpol+":"+idSpolProti);
//    System.out.println(idVazby+":"+idVazbyPar);
    
    int origRowNr = rowNr;
    int levaRow = rowNr, pravaRow = rowNr;
    int colNr = 0;
    
    Number levaList = null, levaRadek = null;
    Number idNum = null;
    double kr = 0;
    StringBuffer buf = new StringBuffer();
    ViewObject vo = dm.findViewObject("VwKpDokladvazbynevyparovaneView1");
    vo.clearCache();
    vo.setWhereClause("s_locale = '"+localeSpol+"' and id_par = "+idVazbyPar+
                      " and id_doklad = ( select id_doklad from DB_JT.KP_Dat_dokladvazby where id = Abs("+idVazbyPar+" ) )");
    vo.setOrderByClause("nl_poradilist, nl_radek, s_mena");
    int pocetRadkuLeva = vo.getRowCount();

    int pocetRadkuPrava = 0;
    double levaCM = 0.0;
    double levaCL = 0.0;
    double pravaCM = 0.0;
    double pravaCL = 0.0;
    double levaEli = 0.0;
    double pravaEli = 0.0;
    double kurzAgrLeva = 0.0;
    double kurzAgrPrava = 0.0;
    
    Number firstLevaRadek = null;
    
    while(vo.hasNext()) 
    {
      Row row = vo.next();
/*
if(null!=row.getAttribute("NlUserindexvazba") && !"0".equals(row.getAttribute("NlUserindexvazba").toString())) 
{
  throw new KisException(errorUserIndex+" L:(idSpol,idSpolProti,idVazby,idVazbyPar)=("+idSpol+","+idSpolProti+","+idVazby+","+idVazbyPar+")",
                         new KisException(Constants.ERR_MESSAGE_ONLY));
}
*/    
      int id = ((Number) row.getAttribute("Id")).intValue();
      if(buf.length()>0) buf.append(", ");
      buf.append(-id);
      
      idNum = (Number) row.getAttribute("Id");
      String levaRadekText = (String) row.getAttribute("SPopis");
      String levaMena = (String) row.getAttribute("SMena");
      String levaUcet = (String) row.getAttribute("SUcet");
      Number levaCastkaMena = (Number) row.getAttribute("NdCastkamena");
      Number levaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
      levaList = (Number) row.getAttribute("NlPoradilist");
      levaRadek = (Number) row.getAttribute("NlRadek");
      if ( firstLevaRadek == null ) firstLevaRadek = levaRadek;

      setRadekSumyLeva(levaMena, levaCastkaMena);
      
      int koef = 1;

      getKurz(true, levaList, levaRadek, levaMena, levaCastkaLocal, true);

      colNr = 0;
      if(levaRadek!=null) setCellValue(listNr,rowNr,colNr,levaRadek.toString(),null);
      colNr++;
      if(levaList!=null) {
        setCellValue(listNr,rowNr,colNr,listy[levaList.intValue()],null);
        if(levaList.intValue()==2 || levaList.intValue()==4 || levaList.intValue()==5) koef = -1;
      }
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaRadekText,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaMena,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaUcet,null);
      colNr++;
      if(levaCastkaMena!=null) {
        setCellValue(listNr,rowNr,colNr,levaCastkaMena.doubleValue(),null);
        levaCM += levaCastkaMena.doubleValue();
      }
      colNr++;
      if(levaCastkaLocal!=null) {
        setCellValue(listNr,rowNr,colNr,levaCastkaLocal.doubleValue(),null);
        levaCL += levaCastkaLocal.doubleValue();
      }
      colNr++;
      if(kurz!=null) {
        setCellValue(listNr,rowNr,colNr,kurz.doubleValue(),null);
        kurzAgrLeva = kurz.doubleValue();
      }
      colNr++;
      if(eliminace!=null) {
        if(pocetRadkuLeva == 1) setCellValue(listNr,rowNr,colNr,koef*eliminace.doubleValue(),null);
        else levaEli += koef*eliminace.doubleValue();
        
        if(levaList!=null) {
          if(levaList.intValue()==1 || levaList.intValue()==2 || levaList.intValue()==5) kr += eliminace.doubleValue();
          setSumyLevaRadky(levaRadek, listy[levaList.intValue()], koef);
        }
      }
      colNr++;
      
      rowNr++;
      levaRow++;
    }
    vo.closeRowSet();

    if(buf.length()>0) {
      rowNr = origRowNr;
      vo.clearCache();
      vo.setWhereClause("s_locale = '"+localeSpol+"' and id_par in ("+buf.toString()+")");
      vo.setOrderByClause("nl_poradilist, nl_radek, s_mena");
      pocetRadkuPrava = vo.getRowCount();
      while(vo.hasNext()) 
      {
        Row row = vo.next();
/*
if(null!=row.getAttribute("NlUserindexvazba") && !"0".equals(row.getAttribute("NlUserindexvazba").toString())) 
{
  throw new KisException(errorUserIndex+" P:(idSpol,idSpolProti,idVazby,idVazbyPar)=("+idSpol+","+idSpolProti+","+idVazby+","+idVazbyPar+")",
                         new KisException(Constants.ERR_MESSAGE_ONLY));
}
*/      
        String pravaRadekText = (String) row.getAttribute("SPopis");
        String pravaMena = (String) row.getAttribute("SMena");
        String pravaUcet = (String) row.getAttribute("SUcet");
        Number pravaCastkaMena = (Number) row.getAttribute("NdCastkamena");
        Number pravaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
        Number pravaList = (Number) row.getAttribute("NlPoradilist");
        Number pravaRadek = (Number) row.getAttribute("NlRadek");

        setRadekSumyPrava(pravaMena, pravaCastkaMena);
        
        int koef = 1;
  
        getKurz(false, pravaList, pravaRadek, pravaMena, pravaCastkaLocal, false/*true*/);
  
        colNr = PULKA;
        if(pravaRadek!=null) setCellValue(listNr,rowNr,colNr,pravaRadek.toString(),null);
        colNr++;
        if(pravaList!=null) {
          setCellValue(listNr,rowNr,colNr,listy[pravaList.intValue()],null);
          if(pravaList.intValue()==2 || pravaList.intValue()==4 || pravaList.intValue()==5) koef = -1;
        }
        colNr++;
        setCellValue(listNr,rowNr,colNr,pravaRadekText,null);
        colNr++;
        setCellValue(listNr,rowNr,colNr,pravaMena,null);
        colNr++;
        setCellValue(listNr,rowNr,colNr,pravaUcet,null);
        colNr++;
        if(pravaCastkaMena!=null) {
          setCellValue(listNr,rowNr,colNr,pravaCastkaMena.doubleValue(),null);
          pravaCM += pravaCastkaMena.doubleValue();
        }
        colNr++;
        if(pravaCastkaLocal!=null) {
          setCellValue(listNr,rowNr,colNr,pravaCastkaLocal.doubleValue(),null);
          pravaCL += pravaCastkaLocal.doubleValue();
        }
        colNr++;
        if(kurz!=null) {
          setCellValue(listNr,rowNr,colNr,kurz.doubleValue(),null);
          kurzAgrPrava = kurz.doubleValue();
        }
        colNr++;
        if(eliminace!=null) {
          if(pocetRadkuPrava == 1) setCellValue(listNr,rowNr,colNr,koef*eliminace.doubleValue(),null);
          else pravaEli += koef*eliminace.doubleValue();
          
          if(pravaList!=null) {//setSumyPravaRadky(pravaRadek, listy[pravaList.intValue()], koef);
            if(pravaList.intValue()==1 || pravaList.intValue()==2 || pravaList.intValue()==5) kr += eliminace.doubleValue();
            setSumyLevaRadky(pravaRadek, listy[pravaList.intValue()], koef);
          }
        }
        colNr++;
        
        rowNr++;
        pravaRow++;
      }
      vo.closeRowSet();
    }
    
    if(pocetRadkuLeva>1 || pocetRadkuPrava>1) 
    {
      origRowNr = levaRow>pravaRow ? levaRow : pravaRow;
      rowNr = origRowNr + 1;
      colNr=5;
      setCellValue(listNr,origRowNr,colNr,levaCM,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,levaCL,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,kurzAgrLeva,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,levaEli,null);
      colNr++;
      colNr=PULKA+5;
      setCellValue(listNr,origRowNr,colNr,pravaCM,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,pravaCL,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,kurzAgrPrava,null);
      colNr++;
      setCellValue(listNr,origRowNr,colNr,pravaEli,null);
      colNr++;
    }
    else {
      rowNr = levaRow>pravaRow ? levaRow : pravaRow;
    }

    colNr = 2*PULKA;
    setCellValue(listNr,origRowNr,colNr,kr,null);
    colNr++;
    
    //getPasiva123(levaList, levaRadek/*, idNum*/);
    // Jarda uprava kvuli Arthur, Bradley & Smith Limited  X Banka, aktiva 51 castka 201 561.21
    getPasiva123(levaList, firstLevaRadek);
    
//System.out.println ( "jzjz 4  ................................ " + pasiva123 + "   " + levaList + "   " +  levaRadek );    
    if(pasiva123!=null) {
      setCellValue(listNr,origRowNr,colNr,pasiva123.doubleValue(),null);
      sumP123 += pasiva123.doubleValue();
    }
    sumKr += kr;
    colNr++;
    setCellValue(listNr,origRowNr,colNr,kr,null);
    colNr++;
  }

  protected boolean outputData () throws KisException
  {
    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();
    
    boolean generovat = false;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                            " AND NL_KROK = "+(!blue?200:600));
    if(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      dokladId = ((Number)rowDoklad.getAttribute("Id")).intValue();
    }
    voDoklad.closeRowSet();
System.out.println(dokladId);

    levaRadkySumy = new TreeMap();    
    pravaRadkySumy = new TreeMap();    

    int lastSpol = -1, lastSpolProti = -1;
    ViewObject vo = dm.findViewObject("KpDatDokladsubkonvazbydenikView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + dokladId +
                      " and c_typ = 'K' " + Utils.getWhereKurzoveRozdily(dokladId));
    vo.setOrderByClause("id_ktgUcetniSpolecnostFrom, id_ktgUcetniSpolecnostTo");
    while(vo.hasNext()) 
    {
      generovat = true;
      Row row = vo.next();
      idSpol = ((Number) row.getAttribute("IdKtgucetnispolecnostfrom")).intValue();
//if ( idSpol != 1 && idSpol != 4503 ) continue;      jarda test
      idSpolProti = ((Number) row.getAttribute("IdKtgucetnispolecnostto")).intValue();
//if ( idSpolProti != 1 && idSpolProti != 4503 ) continue;      jarda test
      kurzVyjimka = (Number) row.getAttribute("NdKurz");

      idVazby = (Number) row.getAttribute("IdDokladvazby");
      idVazbyPar = (Number) row.getAttribute("IdDokladvazbypar");
      if(lastSpol!=idSpol || lastSpolProti!=idSpolProti) {
        if(lastSpol>0) outputSumy();
        levaSumy = new HashMap();
        pravaSumy = new HashMap();
        outputHeader();
        lastSpol = idSpol;
        lastSpolProti = idSpolProti;
        lastList = null;
        lastRadek = null;
        lastMena = null;
      }
      if(idVazby!=null && idVazbyPar!=null && idVazby.intValue()>0 && idVazbyPar.intValue()>0) 
      {
        outputPresne();
      }
      if(idVazby!=null && idVazbyPar!=null && idVazby.intValue()>0 && idVazbyPar.intValue()<0) 
      {
        outputObracene();
      }
      else if(idVazby==null && idVazbyPar!=null && idVazbyPar.intValue()<0) 
      {
        outputNullLeva();
      }
      rowNr++;
      if(!vo.hasNext()) outputSumy();
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    setKontrola();
    
    outputSumyRadky();

    end = System.currentTimeMillis();    
    logger.debug("vazbySubkons:"+((end-start)/1000.0)+"s");
    
    return generovat;
  }  
  
  private void setRadekSumyLeva(String mena, Number castka) 
  {
    if(levaSumy.containsKey(mena)) 
    {
      Number origCastka = (Number) levaSumy.get(mena);
      levaSumy.put(mena, origCastka.add(castka));
    }
    else 
    {
      levaSumy.put(mena, castka);
    }
  }

  private void setRadekSumyPrava(String mena, Number castka) 
  {
    if(pravaSumy.containsKey(mena)) 
    {
      Number origCastka = (Number) pravaSumy.get(mena);
      pravaSumy.put(mena, origCastka.add(castka));
    }
    else 
    {
      pravaSumy.put(mena, castka);
    }
  }

  private void setSumyLevaRadky(Number radek, String popis, int koef) 
  {
    String key = popis+" - "+radek;
    Number castka = eliminace.multiply(koef);
if(radek.intValue()==54 && popis.startsWith("V")) System.out.println(">>>>>>>>>>"+castka);
    if(levaRadkySumy.containsKey(key)) 
    {
      Number origCastka = (Number) levaRadkySumy.get(key);
      levaRadkySumy.put(key, origCastka.add(castka));
    }
    else 
    {
      levaRadkySumy.put(key, castka);
    }
  }

  private void setSumyPravaRadky(Number radek, String popis, int koef) 
  {
    String key = popis+" - "+radek;
    Number castka = eliminace.multiply(koef);
if(radek.intValue()==54 && popis.startsWith("V")) System.out.println(">>>>>>>>>>"+castka);
    if(pravaRadkySumy.containsKey(key)) 
    {
      Number origCastka = (Number) pravaRadkySumy.get(key);
      pravaRadkySumy.put(key, origCastka.add(castka));
    }
    else 
    {
      pravaRadkySumy.put(key, castka);
    }
  }
  
  private void setLocaleDep() 
  {
    if("en_US".equals(localeSpol)) {
      listy = new String[] {"","Assets","Liabilities","3","4","P&L"};
      sumTxt = "Sums by currencies";
      lsTxt = "Left side (comp. cur.):";
      psTxt = "Right side (comp. cur.):";
    }
    else {
      listy = new String[] {"","Aktiva","Pasiva","Podrozvahov aktiva","Podrozvahov pasiva","Vsledovka"};
      sumTxt = "Sumy po mnch";
      lsTxt = "Lev (mna spol.):";
      psTxt = "Prav (mna spol.):";
    }
  }
  
  public static void main(String[] argv) 
  {
    try {
      ESExportSubkonsVazby ev = new ESExportSubkonsVazby(Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal"),
                                                 10026,
                                                 new java.sql.Date(106,11,31),
                                                 false);
      ev.excelOutput();
      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=ev.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
}