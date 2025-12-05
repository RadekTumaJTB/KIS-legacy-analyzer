package cz.jtbank.konsolidace.excel;

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

public class ESExportSubkonsVazbyUser extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsVazbyUser.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_VAZBY)); }

  private static final int PULKA = 9;

  private String[] listy;
  private String lsTxt, psTxt, sumTxt;

  private ApplicationModule dm;
  private int subkonsId;
  private java.sql.Date datum;

  private String nazevSub;
  private String menaSub;
  private String souborPredpona;

  Number idSpol, idSpolProti;
  Number idVazby, idVazbyPar;

  private String localeSpol;
  
  private Map levaSumy, pravaSumy;
  
  private int listNr = 0;
  private int rowNr = 3;
  
  private CellStyle styleBold;
  private CellStyle styleRed;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private Set clenove = new HashSet();
  private Number skupina;

  public ESExportSubkonsVazbyUser(ApplicationModule dokladyModule,
                              int subkonsId,
                              java.sql.Date datum)
  {
    dm = dokladyModule;
    this.subkonsId = subkonsId;
    this.datum = datum;
    init();
    setLocaleDep();
  }
  
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
    
    vo = dm.findViewObject("VwKpSubkonsolidaceView1");
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+subkonsId);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      skupina = (Number) row.getAttribute("IdKtgucetniskupina");
    }
    vo.closeRowSet();

    setFileName ( "SubVazbyUser"+subkonsId+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredpona+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona = (localeSpol==null || localeSpol.length()<1) ? 
                     "SablonaSubkonsVVUser.xlsx" :
                     "SablonaSubkonsVVUser_"+localeSpol+".xlsx";
    setSablona( Constants.SABLONY_FILES_PATH+sablona );
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
    if(levaSumy!=null && !levaSumy.isEmpty())
      meny.addAll(levaSumy.keySet());
    if(pravaSumy!=null && !pravaSumy.isEmpty())
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
  
  private void outputPresne()
  {
    System.out.println(idSpol+":"+idSpolProti);
    System.out.println(idVazby+":"+idVazbyPar);
    
    boolean pravaStrana = false;
    
    ViewObject vo = dm.findViewObject("VwKpDokladvazbyView1");
    vo.clearCache();
    vo.setWhereClause("KpDatDokladvazby.ID = "+idVazby+" AND S_LOCALE = '"+localeSpol+"'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      pravaStrana = ((Number) row.getAttribute("NlUserindexvazba")).intValue() > 0;
      
      Number idNum = (Number) row.getAttribute("Id");
      String levaRadekText = (String) row.getAttribute("SPopis");
      String levaMena = (String) row.getAttribute("SMena");
      String levaUcet = (String) row.getAttribute("SUcet");
      Number levaCastkaMena = (Number) row.getAttribute("NdCastkamena");
      Number levaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
      Number levaList = (Number) row.getAttribute("NlPoradilist");
      Number levaRadek = (Number) row.getAttribute("NlRadek");
      Number levaCastkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");
      Number levaCastkaLocalOrig = (Number) row.getAttribute("NdCastkalocaloriginal");

      setRadekSumyLeva(levaMena, levaCastkaMena);

      int colNr = 0;
      if(levaRadek!=null) setCellValue(listNr,rowNr,colNr,levaRadek.toString(),null);
      colNr++;
      if(levaList!=null) {
        setCellValue(listNr,rowNr,colNr,listy[levaList.intValue()],null);
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
      if(levaCastkaMenaOrig!=null) setCellValue(listNr,rowNr,colNr,levaCastkaMenaOrig.doubleValue(),null);
      colNr++;
      if(levaCastkaLocalOrig!=null) setCellValue(listNr,rowNr,colNr,levaCastkaLocalOrig.doubleValue(),null);
      colNr++;
    }
    vo.closeRowSet();

    if(pravaStrana) {
      vo.clearCache();
      vo.setWhereClause("KpDatDokladvazby.ID = "+idVazbyPar+" AND S_LOCALE = '"+localeSpol+"'");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        
        String pravaRadekText = (String) row.getAttribute("SPopis");
        String pravaMena = (String) row.getAttribute("SMena");
        String pravaUcet = (String) row.getAttribute("SUcet");
        Number pravaCastkaMena = (Number) row.getAttribute("NdCastkamena");
        Number pravaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
        Number pravaList = (Number) row.getAttribute("NlPoradilist");
        Number pravaRadek = (Number) row.getAttribute("NlRadek");
        Number pravaCastkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");
        Number pravaCastkaLocalOrig = (Number) row.getAttribute("NdCastkalocaloriginal");
  
        setRadekSumyPrava(pravaMena, pravaCastkaMena);
        
        int colNr = PULKA;
        if(pravaRadek!=null) setCellValue(listNr,rowNr,colNr,pravaRadek.toString(),null);
        colNr++;
        if(pravaList!=null) {
          setCellValue(listNr,rowNr,colNr,listy[pravaList.intValue()],null);
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
        if(pravaCastkaMenaOrig!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaMenaOrig.doubleValue(),null);
        colNr++;
        if(pravaCastkaLocalOrig!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaLocalOrig.doubleValue(),null);
        colNr++;
      }
      vo.closeRowSet();
    }

    rowNr+=2;
  }

  private Set pars = new HashSet();
  private Set spols = new HashSet();

  private void outputObracene() 
  {
    System.out.println(idSpol+":"+idSpolProti);
    System.out.println(idVazby+":"+idVazbyPar);
    
    boolean pravaStrana = false;
    int origRowNr = rowNr, levaRowNr = rowNr;
    
    ViewObject vo = dm.findViewObject("VwKpDokladvazbyView1");
    vo.clearCache();
    vo.setWhereClause("KpDatDokladvazby.ID_PAR = "+idVazbyPar+" AND S_LOCALE = '"+localeSpol+"'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      pravaStrana = row.getAttribute("NlUserindexvazba")!=null && ((Number) row.getAttribute("NlUserindexvazba")).intValue() > 0;
      
      Number idNum = (Number) row.getAttribute("Id");
      String levaRadekText = (String) row.getAttribute("SPopis");
      String levaMena = (String) row.getAttribute("SMena");
      String levaUcet = (String) row.getAttribute("SUcet");
      Number levaCastkaMena = (Number) row.getAttribute("NdCastkamena");
      Number levaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
      Number levaList = (Number) row.getAttribute("NlPoradilist");
      Number levaRadek = (Number) row.getAttribute("NlRadek");
      Number levaCastkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");
      Number levaCastkaLocalOrig = (Number) row.getAttribute("NdCastkalocaloriginal");

      setRadekSumyLeva(levaMena, levaCastkaMena);

      int colNr = 0;
      if(levaRadek!=null) setCellValue(listNr,rowNr,colNr,levaRadek.toString(),null);
      colNr++;
      if(levaList!=null) {
        setCellValue(listNr,rowNr,colNr,listy[levaList.intValue()],null);
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
      if(levaCastkaMenaOrig!=null) setCellValue(listNr,rowNr,colNr,levaCastkaMenaOrig.doubleValue(),null);
      colNr++;
      if(levaCastkaLocalOrig!=null) setCellValue(listNr,rowNr,colNr,levaCastkaLocalOrig.doubleValue(),null);
      colNr++;
      rowNr++;
    }
    vo.closeRowSet();
    levaRowNr=rowNr;

    if(pravaStrana) {
      Number pravaPar = null;
      vo.clearCache();
      vo.setWhereClause("KpDatDokladvazby.ID = ABS("+idVazbyPar+") AND S_LOCALE = '"+localeSpol+"'");
      if(vo.hasNext()) 
      {
        Row row = vo.next();
        pravaPar = (Number) row.getAttribute("IdPar");
      }
      vo.closeRowSet();

      rowNr = origRowNr;
      vo.clearCache();
      vo.setWhereClause("KpDatDokladvazby.ID_PAR = "+pravaPar+" AND S_LOCALE = '"+localeSpol+"'");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        
        String pravaRadekText = (String) row.getAttribute("SPopis");
        String pravaMena = (String) row.getAttribute("SMena");
        String pravaUcet = (String) row.getAttribute("SUcet");
        Number pravaCastkaMena = (Number) row.getAttribute("NdCastkamena");
        Number pravaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
        Number pravaList = (Number) row.getAttribute("NlPoradilist");
        Number pravaRadek = (Number) row.getAttribute("NlRadek");
        Number pravaCastkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");
        Number pravaCastkaLocalOrig = (Number) row.getAttribute("NdCastkalocaloriginal");
  
        setRadekSumyPrava(pravaMena, pravaCastkaMena);
        
        int colNr = PULKA;
        if(pravaRadek!=null) setCellValue(listNr,rowNr,colNr,pravaRadek.toString(),null);
        colNr++;
        if(pravaList!=null) {
          setCellValue(listNr,rowNr,colNr,listy[pravaList.intValue()],null);
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
        if(pravaCastkaMenaOrig!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaMenaOrig.doubleValue(),null);
        colNr++;
        if(pravaCastkaLocalOrig!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaLocalOrig.doubleValue(),null);
        colNr++;
        rowNr++;
      }
      vo.closeRowSet();
    }

    rowNr = (rowNr>levaRowNr ? rowNr : levaRowNr) + 1;
  }

  private void setClenove(Number idSub) 
  {
    Set set = new HashSet();
    if(idSub == null) idSub = new Number(subkonsId);
  
    ViewObject vo = dm.findViewObject("KpRelSubkonsolidaceclenSpecial1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGSUBKONSOLIDACE = "+idSub/*+
                      " AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO"*/);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number idSpolTmp = (Number) row.getAttribute("IdKtgucetnispolecnost");
      String sub = (String) row.getAttribute("CSubkonsolidace");
      set.add(new Object[] {idSpolTmp, sub});
    }
    vo.closeRowSet();
    
    Iterator iter = set.iterator();
    while(iter.hasNext()) 
    {
      Object[] objArr = (Object[]) iter.next();
      Number idSpolTmp = (Number) objArr[0];
      boolean isSub = "1".equals(objArr[1]);
      if(!isSub) 
      {
        clenove.add(idSpolTmp);
      }
/**/
      else 
      {
        setClenove(idSpolTmp);
      }
/**/
    }
  }

  protected boolean outputData () 
  {
    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();
    
    setClenove(null);
    
    boolean generovat = false;
    Number lastSpol = null, lastSpolProti = null;
    
    Iterator iter = clenove.iterator();
    while(iter.hasNext()) 
    {
      idSpol = (Number) iter.next();

      ViewObject vo = dm.findViewObject("KpDatDokladvazbySpecialView2");
      vo.clearCache();
      vo.setWhereClause("ID_KTGUCETNISPOLECNOSTFROM = " + idSpol +
                        " AND NL_USERINDEXVAZBA IS NOT NULL AND NL_USERINDEXVAZBA <> 0"+
                        " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                        " AND ID_KTGUCETNISKUPINA = "+skupina+
                        //" AND db_jt.kap_subkon.f_getSpolecnaSubkonsolidace( ID_KTGUCETNISPOLECNOSTFROM, ID_KTGUCETNISPOLECNOSTTO, "+skupina+", TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy'))="+subkonsId);
                        " AND db_jt.kap_subkon.f_isRokSpolecnaSubkonsolidace( "+subkonsId+", ID_KTGUCETNISPOLECNOSTFROM, ID_KTGUCETNISPOLECNOSTTO, "+skupina+", TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy'))="+subkonsId);
      vo.setOrderByClause("ID_KTGUCETNISPOLECNOSTTO");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        idSpolProti = (Number) row.getAttribute("IdKtgucetnispolecnostto");
        int indexVazba = ((Number) row.getAttribute("NlUserindexvazba")).intValue();

        idVazby = (Number) row.getAttribute("Id");
        idVazbyPar = (Number) row.getAttribute("IdPar");
        
        //KONTROLNI BLOK
        if(!clenove.contains(idSpolProti)) continue;
        if(spols.contains(idSpolProti+":"+idSpol)) continue;
        else spols.add(idSpol+":"+idSpolProti);
        if(pars.contains(idVazbyPar)) continue;
        else pars.add(idVazbyPar);

        generovat = true;
  
        if(!idSpol.equals(lastSpol) || !idSpolProti.equals(lastSpolProti)) {
          if(lastSpol!=null) outputSumy();
          levaSumy = new HashMap();
          pravaSumy = new HashMap();
          outputHeader();
          lastSpol = idSpol;
          lastSpolProti = idSpolProti;
        }
        //if(idVazby!=null && idVazbyPar!=null && idVazby.intValue()>0 && idVazbyPar.intValue()>0) 
        if(false) //CHECK!!!
        {
          outputPresne();
        }
        //if(idVazby!=null && idVazbyPar!=null && idVazby.intValue()>0 && idVazbyPar.intValue()<0) 
        else
        {
          outputObracene();
        }
      }
      vo.closeRowSet();
      dm.getTransaction().commit();
      if(!iter.hasNext() && lastSpol!=null) outputSumy();
    }
    
    end = System.currentTimeMillis();    
    logger.debug("vazbySubkons12:"+((end-start)/1000.0)+"s");
    
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
      ESExportSubkonsVazbyUser ev = new ESExportSubkonsVazbyUser(Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal"),
                                                 10023,
                                                 new java.sql.Date(106,8,30));
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