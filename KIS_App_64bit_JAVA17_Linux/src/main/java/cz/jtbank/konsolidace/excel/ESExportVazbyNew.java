package cz.jtbank.konsolidace.excel;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.*;
import cz.jtbank.konsolidace.common.Constants;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;
import java.util.*;
import java.text.SimpleDateFormat;

public class ESExportVazbyNew extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportVazbyNew.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_VAZBY)); }

  private static final int PULKA = 10;

  private String[] listy;
  private String lsTxt, psTxt, sumTxt;

  private ApplicationModule dm;
  private Number idDoklad;
  private Number ucetniSkupina;
  private Number idKtgSpolecnost;
  private java.sql.Date datum;

  private String userMustek;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;
  private String localeSpol;
  private Number idSubkonsolidace;
  
  private HashMap levaSumy, pravaSumy;
  
  private int listNr = 0;
  private int rowNr = 3;
  
  private CellStyle styleBold;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportVazbyNew(ApplicationModule dokladyModule,
                          Number idDoklad,
                          Number ucetniSkupina,
                          String userMustek)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    this.ucetniSkupina = ucetniSkupina;
    this.userMustek = userMustek;
    init();
    setLocaleDep();
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
      localeSpol = (String) row.getAttribute("SLocale");
      idSubkonsolidace = (Number) row.getAttribute("IdSubkonsolidace");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    String whereClen = "ID_KTGUCETNISPOLECNOST = "+idKtgSpolecnost+" AND C_CLEN = '1' AND ID_KTGSUBKONSOLIDACE IN (SELECT ID_KTGUCETNISPOLECNOST FROM DB_JT.KP_KTG_SUBKONSOLIDACE WHERE ID_KTGUCETNISKUPINA = "+ucetniSkupina+")";
    ViewObject voClen = dm.findViewObject("KpRelSubkonsolidaceclenView1");
    voClen.clearCache();
    voClen.setWhereClause(whereClen);
    voClen.setOrderByClause("DT_ClenstviOD");
    while(voClen.hasNext()) 
    {
      Row rowClen = voClen.next();
      nazevSpol += rowClen.getAttribute("DtClenstviod")==null ? "" : " "+sdf.format(((oracle.jbo.domain.Date)rowClen.getAttribute("DtClenstviod")).dateValue())+" - ";
      nazevSpol += rowClen.getAttribute("DtClenstvido")==null ? "" : sdf.format(((oracle.jbo.domain.Date)rowClen.getAttribute("DtClenstvido")).dateValue());
      nazevSpol += " ("+rowClen.getAttribute("IdKtgsubkonsolidace")+")";
      nazevSpol += " - " + ("1".equals(rowClen.getAttribute("CPropisujvazby"))?"":"ne") + "propisovat vazby";
    }
    voClen.closeRowSet();

    String mustekDir = (userMustek==null || "master".equals(userMustek)) ? "" : userMustek+"\\";
    String subkonsDoklad = idSubkonsolidace==null ? "" : "Sub";//idSubkonsolidace.toString();
    setFileName ( idDoklad+"@Vazby"+subkonsDoklad+"_Skup"+ucetniSkupina+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredponaSpol+"_"+idKtgSpolecnost+"\\"+mustekDir+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona = (localeSpol==null || localeSpol.length()<1) ? 
                     "SablonaVVNew.xlsx" :
                     "SablonaVVNew_"+localeSpol+".xlsx";
    setSablona( Constants.SABLONY_FILES_PATH+sablona );
  }

  protected void outputHeader(Number idSpolProti) 
  {
    String nazevSpolProti = null;
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = " + idSpolProti);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpolProti = (String) row.getAttribute("SNazev");
      if(!"1".equals(row.getAttribute("COnline"))) nazevSpolProti += " - NE ON-LINE";
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    String whereClen = "ID_KTGUCETNISPOLECNOST = "+idSpolProti+" AND C_CLEN = '1' AND ID_KTGSUBKONSOLIDACE IN (SELECT ID_KTGUCETNISPOLECNOST FROM DB_JT.KP_KTG_SUBKONSOLIDACE WHERE ID_KTGUCETNISKUPINA = "+ucetniSkupina+")";
    ViewObject voClen = dm.findViewObject("KpRelSubkonsolidaceclenView1");
    voClen.clearCache();
    voClen.setWhereClause(whereClen);
    voClen.setOrderByClause("DT_ClenstviOD");
    while(voClen.hasNext()) 
    {
      Row rowClen = voClen.next();
      nazevSpolProti += " "+sdf.format(((oracle.jbo.domain.Date)rowClen.getAttribute("DtClenstviod")).dateValue())+" - ";
      nazevSpolProti += sdf.format(((oracle.jbo.domain.Date)rowClen.getAttribute("DtClenstvido")).dateValue());
      nazevSpolProti += " ("+rowClen.getAttribute("IdKtgsubkonsolidace")+")";
      nazevSpolProti += " - " + ("1".equals(rowClen.getAttribute("CPropisujvazby"))?"":"ne") + "propisovat vazby";
    }
    voClen.closeRowSet();

    if(styleBold == null) 
    {
      //Cell cell = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)0);
      Font font = wb.createFont();
      font.setBold(true);
      //styleBold = cell.getCellStyle();
      //if(styleBold == null) {
        styleBold = wb.createCellStyle();
      //}
      styleBold.setFont(font);
    }

    setCellValue(listNr,rowNr,0,nazevSpol,styleBold);
    setCellValue(listNr,rowNr,PULKA,nazevSpolProti,styleBold);
    
    rowNr++;
  }

  protected void outputVyparovane(Number idSpolProti) {
    int colNr;
    
    ViewObject vo = dm.findViewObject("VwKpDokladvazbyvyparovaneView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad +
                      " and id_ktgUcetniSpolecnostFrom = " + idKtgSpolecnost +
                      " and id_ktgUcetniSpolecnostTo = " + idSpolProti +
                      " and id_ktgUcetniSkupina = " + ucetniSkupina
                      + " and (nvl(nl_userIndexVazba_1,0) = 0)"
                      + " and (nvl(nl_userIndexVazba_2,0) = 0)"
                      + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")
                      );                      
    while(vo.hasNext()) 
    {
      colNr = 0;
      Row row = vo.next();
      
      String levaRadekText = (String) row.getAttribute("SPopis");
      String levaMena = (String) row.getAttribute("SMena");
      String levaUcet = (String) row.getAttribute("SUcet");
      Number levaCastkaMena = (Number) row.getAttribute("NdCastkamena");
      Number levaCastkaLocal = (Number) row.getAttribute("NdCastkalocal");
      Number levaCastkaVyparovat = (Number) row.getAttribute("NdCastkavyparovat");
      Number levaList = (Number) row.getAttribute("NlPoradilist");
      Number levaRadek = (Number) row.getAttribute("NlRadek");
      Number levaCastkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");

      String pravaRadekText = (String) row.getAttribute("ParSPopis");
      String pravaMena = (String) row.getAttribute("ParSMena");
      String pravaUcet = (String) row.getAttribute("ParSUcet");
      Number pravaCastkaMena = (Number) row.getAttribute("ParNdCastkamena");
      Number pravaCastkaLocal = (Number) row.getAttribute("ParNdCastkalocal");
      Number pravaCastkaVyparovat = (Number) row.getAttribute("ParNdCastkavyparovat");
      Number pravaList = (Number) row.getAttribute("ParNlPoradilist");
      Number pravaRadek = (Number) row.getAttribute("ParNlRadek");
      Number pravaCastkaMenaOrig = (Number) row.getAttribute("ParNdCastkamenaoriginal");

      String levaUcetUnif = (String) row.getAttribute("SUctyoriginal1");
      String pravaUcetUnif = (String) row.getAttribute("SUctyoriginal2");

      setRadekSumyLeva(levaMena, levaCastkaMena);
      setRadekSumyPrava(pravaMena, pravaCastkaMena);

      if(levaRadek!=null) setCellValue(listNr,rowNr,colNr,levaRadek.toString(),null);
      colNr++;
      if(levaList!=null) setCellValue(listNr,rowNr,colNr,listy[levaList.intValue()],null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaRadekText,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaMena,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaUcet,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,levaUcetUnif,null);
      colNr++;
      if(levaCastkaMena!=null) setCellValue(listNr,rowNr,colNr,levaCastkaMena.doubleValue(),null);
      colNr++;
      if(levaCastkaLocal!=null) setCellValue(listNr,rowNr,colNr,levaCastkaLocal.doubleValue(),null);
      colNr++;
      if(levaCastkaVyparovat!=null) setCellValue(listNr,rowNr,colNr,levaCastkaVyparovat.doubleValue(),null);
      colNr++;
      if(levaCastkaMenaOrig!=null) setCellValue(listNr,rowNr,colNr,levaCastkaMenaOrig.doubleValue(),null);
      colNr++;

      colNr = PULKA;
      if(pravaRadek!=null) setCellValue(listNr,rowNr,colNr,pravaRadek.toString(),null);
      colNr++;
      if(pravaList!=null) setCellValue(listNr,rowNr,colNr,listy[pravaList.intValue()],null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaRadekText,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaMena,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaUcet,null);
      colNr++;
      setCellValue(listNr,rowNr,colNr,pravaUcetUnif,null);
      colNr++;
      if(pravaCastkaMena!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaMena.doubleValue(),null);
      colNr++;
      if(pravaCastkaLocal!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaLocal.doubleValue(),null);
      colNr++;
      if(pravaCastkaVyparovat!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaVyparovat.doubleValue(),null);
      colNr++;
      if(pravaCastkaMenaOrig!=null) setCellValue(listNr,rowNr,colNr,pravaCastkaMenaOrig.doubleValue(),null);
      colNr++;
      
      rowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  protected int outputNevyparovane(Number idSpolProti, boolean leva) {
    int colNr;
    int stranaRowNr = rowNr;
    
    ViewObject vo = dm.findViewObject("VwKpDokladvazbynevyparovaneView1");
    vo.clearCache();
    if(leva) {
      vo.setWhereClause("ID_DOKLAD = " + idDoklad +
                        " and id_ktgUcetniSpolecnostFrom = " + idKtgSpolecnost +
                        " and id_ktgUcetniSpolecnostTo = " + idSpolProti +
                        " and id_ktgUcetniSkupina = " + ucetniSkupina +
                        " and (nvl(id_par,0) = 0 or nvl(nl_userIndexVazba,0) <> 0)"
                        + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")
                        );
    }
    else {
      vo.setWhereClause("ID_DOKLAD = " + idDoklad +
                        " and id_ktgUcetniSpolecnostFrom = " + idSpolProti +
                        " and id_ktgUcetniSpolecnostTo = " + idKtgSpolecnost +
                        " and id_ktgUcetniSkupina = " + ucetniSkupina +
                        " and (nvl(id_par,0) = 0 or nvl(nl_userIndexVazba,0) <> 0)"
                        + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")
                        );
    }
    while(vo.hasNext()) 
    {
      colNr = leva ? 0 : PULKA;
      Row row = vo.next();
      
      String radekText = (String) row.getAttribute("SPopis");
      String mena = (String) row.getAttribute("SMena");
      String ucet = (String) row.getAttribute("SUcet");
      Number castkaMena = (Number) row.getAttribute("NdCastkamena");
      Number castkaLocal = (Number) row.getAttribute("NdCastkalocal");
      Number castkaVyparovat = (Number) row.getAttribute("NdCastkavyparovat");
      Number list = (Number) row.getAttribute("NlPoradilist");
      Number radek = (Number) row.getAttribute("NlRadek");
      Number castkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");
      String userPar = "";
      Number userIndexVazba = (Number) row.getAttribute("NlUserindexvazba");
      if(userIndexVazba==null || userIndexVazba.intValue()==0) userPar="";
      else if(userIndexVazba.intValue()==1) userPar="1 - ";
      else if(userIndexVazba.intValue()==2) userPar="2 - ";
      String ucetUnif = (String) row.getAttribute("SUctyoriginal");

      if(leva)
        setRadekSumyLeva(mena, castkaMena);
      else
        setRadekSumyPrava(mena, castkaMena);

      if(radek!=null) setCellValue(listNr,stranaRowNr,colNr,userPar+radek.toString(),null);
      colNr++;
      if(list!=null) setCellValue(listNr,stranaRowNr,colNr,listy[list.intValue()],null);
      colNr++;
      setCellValue(listNr,stranaRowNr,colNr,radekText,null);
      colNr++;
      setCellValue(listNr,stranaRowNr,colNr,mena,null);
      colNr++;
      setCellValue(listNr,stranaRowNr,colNr,ucet,null);
      colNr++;
      setCellValue(listNr,stranaRowNr,colNr,ucetUnif,null);
      colNr++;
      if(castkaMena!=null) setCellValue(listNr,stranaRowNr,colNr,castkaMena.doubleValue(),null);
      colNr++;
      if(castkaLocal!=null) setCellValue(listNr,stranaRowNr,colNr,castkaLocal.doubleValue(),null);
      colNr++;
      if(castkaVyparovat!=null) setCellValue(listNr,stranaRowNr,colNr,castkaVyparovat.doubleValue(),null);
      colNr++;
      if(castkaMenaOrig!=null) setCellValue(listNr,stranaRowNr,colNr,castkaMenaOrig.doubleValue(),null);
      colNr++;
      
      stranaRowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    return stranaRowNr;
  }

  private Set pars = new HashSet();

  protected int outputVyparovaneNevyparovane(Number idSpolProti, int listCnt) {
    int colNr;
    int levaRowNr = rowNr;
    int pravaRowNr = rowNr;
    int levaIdPar = -1;
    int listCntPrava = -1;
    double sumLeva = 0;
    double sumPrava = 0;
    
    ViewObject vo = dm.findViewObject("VwKpDokladvazbynevyparovaneView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad +
                      " and id_ktgUcetniSpolecnostFrom = " + idKtgSpolecnost +
                      " and id_ktgUcetniSpolecnostTo = " + idSpolProti +
                      " and id_ktgUcetniSkupina = " + ucetniSkupina +
                      " and id_par < 0 " +
                      " and nl_poradiList = " + listCnt
                      + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")
                      + " and (nvl(nl_userIndexVazba,0) = 0)"
                      );
    while(vo.hasNext()) 
    {
      colNr = 0;
      Row row = vo.next();
      
      String radekText = (String) row.getAttribute("SPopis");
      String mena = (String) row.getAttribute("SMena");
      String ucet = (String) row.getAttribute("SUcet");
      Number castkaMena = (Number) row.getAttribute("NdCastkamena");
      Number castkaLocal = (Number) row.getAttribute("NdCastkalocal");
      Number castkaVyparovat = (Number) row.getAttribute("NdCastkavyparovat");
      Number list = (Number) row.getAttribute("NlPoradilist");
      Number radek = (Number) row.getAttribute("NlRadek");
      Number idPar = (Number) row.getAttribute("IdPar");
      String ucetUnif = (String) row.getAttribute("SUctyoriginal");
      if(idPar!=null && levaIdPar<0) {
        levaIdPar = - idPar.intValue();
      }
      Number castkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");

      setRadekSumyLeva(mena, castkaMena);
      
      sumLeva += castkaLocal == null ? 0 : castkaLocal.doubleValue();

      if(radek!=null) setCellValue(listNr,levaRowNr,colNr,radek.toString(),null);
      colNr++;
      if(list!=null) setCellValue(listNr,levaRowNr,colNr,listy[list.intValue()],null);
      colNr++;
      setCellValue(listNr,levaRowNr,colNr,radekText,null);
      colNr++;
      setCellValue(listNr,levaRowNr,colNr,mena,null);
      colNr++;
      setCellValue(listNr,levaRowNr,colNr,ucet,null);
      colNr++;
      setCellValue(listNr,levaRowNr,colNr,ucetUnif,null);
      colNr++;
      if(castkaMena!=null) setCellValue(listNr,levaRowNr,colNr,castkaMena.doubleValue(),null);
      colNr++;
      if(castkaLocal!=null) setCellValue(listNr,levaRowNr,colNr,castkaLocal.doubleValue(),null);
      colNr++;
      if(castkaVyparovat!=null) setCellValue(listNr,levaRowNr,colNr,castkaVyparovat.doubleValue(),null);
      colNr++;
      if(castkaMenaOrig!=null) setCellValue(listNr,levaRowNr,colNr,castkaMenaOrig.doubleValue(),null);
      colNr++;
      
      levaRowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    if(levaIdPar > 0) {
      vo.clearCache();
      vo.setWhereClause("ID_DOKLAD = " + idDoklad +" AND ID = "+levaIdPar);//NOVINKA
      if(vo.hasNext()) 
      {
        Row row = vo.next();
        Number list = (Number) row.getAttribute("NlPoradilist");
        listCntPrava = list==null ? -1 : list.intValue();
      }
      vo.closeRowSet();
      dm.getTransaction().commit();

      if(listCntPrava > 0) {
        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = " + idDoklad +
                          " and id_ktgUcetniSpolecnostFrom = " + idSpolProti +
                          " and id_ktgUcetniSpolecnostTo = " + idKtgSpolecnost +
                          " and id_ktgUcetniSkupina = " + ucetniSkupina +
                          " and id_par < 0 " +
                          " and nl_poradiList = " + listCntPrava
                          + ((localeSpol!=null && localeSpol.length()>0) ? " and s_locale = '"+localeSpol+"'" : "")
                          + " and (nvl(nl_userIndexVazba,0) = 0)"
                          );
        while(vo.hasNext()) 
        {
          colNr = PULKA;
          Row row = vo.next();
    
          String radekText = (String) row.getAttribute("SPopis");
          String mena = (String) row.getAttribute("SMena");
          String ucet = (String) row.getAttribute("SUcet");
          Number castkaMena = (Number) row.getAttribute("NdCastkamena");
          Number castkaLocal = (Number) row.getAttribute("NdCastkalocal");
          Number castkaVyparovat = (Number) row.getAttribute("NdCastkavyparovat");
          Number list = (Number) row.getAttribute("NlPoradilist");
          Number radek = (Number) row.getAttribute("NlRadek");
          Number castkaMenaOrig = (Number) row.getAttribute("NdCastkamenaoriginal");
          String ucetUnif = (String) row.getAttribute("SUctyoriginal");
    
          setRadekSumyPrava(mena, castkaMena);

          sumPrava += castkaLocal == null ? 0 : castkaLocal.doubleValue();
    
          if(radek!=null) setCellValue(listNr,pravaRowNr,colNr,radek.toString(),null);
          colNr++;
          if(list!=null) setCellValue(listNr,pravaRowNr,colNr,listy[list.intValue()],null);
          colNr++;
          setCellValue(listNr,pravaRowNr,colNr,radekText,null);
          colNr++;
          setCellValue(listNr,pravaRowNr,colNr,mena,null);
          colNr++;
          setCellValue(listNr,pravaRowNr,colNr,ucet,null);
          colNr++;
          setCellValue(listNr,levaRowNr,colNr,ucetUnif,null);
          colNr++;
          if(castkaMena!=null) setCellValue(listNr,pravaRowNr,colNr,castkaMena.doubleValue(),null);
          colNr++;
          if(castkaLocal!=null) setCellValue(listNr,pravaRowNr,colNr,castkaLocal.doubleValue(),null);
          colNr++;
          if(castkaVyparovat!=null) setCellValue(listNr,pravaRowNr,colNr,castkaVyparovat.doubleValue(),null);
          colNr++;
          if(castkaMenaOrig!=null) setCellValue(listNr,pravaRowNr,colNr,castkaMenaOrig.doubleValue(),null);
          colNr++;
          
          pravaRowNr++;
        }
        vo.closeRowSet();
        dm.getTransaction().commit();
        
        setCellValue(listNr,rowNr,2*PULKA,lsTxt,null);
        setCellValue(listNr,rowNr+1,2*PULKA,psTxt,null);
        setCellValue(listNr,rowNr,2*PULKA+1,sumLeva,null);
        setCellValue(listNr,rowNr+1,2*PULKA+1,sumPrava,null);
      }
    }
    
    return levaRowNr > pravaRowNr ? levaRowNr : pravaRowNr;
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
        setCellValue(listNr,rowNr,6,valLeva,null);
        setCellValue(listNr,rowNr,PULKA+3,mena,null);
        double valPrava = (castkaPrava==null) ? 0.0 : castkaPrava.doubleValue();
        setCellValue(listNr,rowNr,PULKA+6,valPrava,null);
        rowNr++;
      }
    }
    rowNr+=2;
  }
  
  protected boolean outputData () 
  {
    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();
    
    boolean generovat = false;

    ViewObject vo = dm.findViewObject("VwKpGuidokladvazbyspolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = " + idDoklad +
                      " and id_ktgUcetniSpolecnostFrom = " + idKtgSpolecnost+
                      " and id_ktgUcetniSkupina = " + ucetniSkupina);
    vo.setOrderByClause("id_ktgUcetniSpolecnostTo");
    while(vo.hasNext()) 
    {
      generovat = true;
      Row row = vo.next();
      Number idSpolProti = (Number) row.getAttribute("IdKtgucetnispolecnostto");
      levaSumy = new HashMap();
      pravaSumy = new HashMap();
      outputHeader(idSpolProti);
      outputVyparovane(idSpolProti);
      rowNr++;
      for(int i=1; i<=5; i++) 
      {
        int oldRowNr = rowNr;
        rowNr = outputVyparovaneNevyparovane(idSpolProti, i);
        if(oldRowNr != rowNr) rowNr++;
      }
      int levaRow = outputNevyparovane(idSpolProti, true);
      int pravaRow = outputNevyparovane(idSpolProti, false);
      rowNr = (levaRow>pravaRow ? levaRow : pravaRow)+1;      
      outputSumy();
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    end = System.currentTimeMillis();    
    logger.debug("vazby:"+((end-start)/1000.0)+"s");
    
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
  
  public static void vazbyZTmp() 
  {
    ApplicationModule am = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
  }
  
  public static void main(String[] argv) 
  {
    try {
      ESExportVazbyNew ev = new ESExportVazbyNew(Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal"),
                                                 new Number(1199378),
                                                 new Number(2),
                                                 null);
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