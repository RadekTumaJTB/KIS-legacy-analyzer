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

import java.sql.*;
import oracle.jbo.server.*;
import cz.jtbank.konsolidace.common.KisException;

public class ESExportUverove extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportUverove.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  private double kapital = 0.0;
  private int ucSkup;

  private String dir;

//esc  public ESExportUverove(ApplicationModuleImpl dokladyModule,
  public ESExportUverove(ApplicationModule dokladyModule,
                         java.sql.Date datum,
                         int ucSkup)
  {

logger.info("ESExportUverove:datum="+datum+" UcSkup:"+ucSkup);  

    dm = dokladyModule;
    this.datum = datum;
    this.ucSkup = ucSkup;
    dir = Constants.DIR_POZICE_MU+ucSkup;
    init();
  }

  private void init() {
    ViewObject voAdm = dm.findViewObject("KpDatKapitalskupinyMaxView1");  //doklady
    voAdm.clearCache();
    voAdm.setWhereClauseParam(0,new Integer(ucSkup));
    voAdm.setWhereClauseParam(1,datum);
    if(voAdm.hasNext()) 
    {
      Row rowAdm = voAdm.next();
      Number numKap = (Number) rowAdm.getAttribute("NdCastka");
      if(numKap != null) 
        kapital = numKap.doubleValue();
    }
    voAdm.closeRowSet();

    setFileName ( "UverovePozice_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaUverove.xlsx" );
  }
  
//esc 20.05.2011
public int isBanka(int id
                    ) throws KisException
  {
    DBTransaction dbTran = ((ApplicationModuleImpl)dm).getDBTransaction();
    int ret = -1;
    CallableStatement st = null;
    
    try {
      st = dbTran.createCallableStatement("begin db_jt.p_isBanka(?,?); end;",0);      
      st.setInt(1,id);
      st.registerOutParameter(2, Types.INTEGER);
      st.execute();
      ret = st.getInt(2);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo voln procedury db_jt.p_isBanka",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    return ret;
  }

//esc 27.05.2011
// kdyz je feis (neni Queastor) vrati TRUE
public boolean isSpolFeis(int id
                    ) throws KisException
  {
logger.info("ESExportUverove:isSpolFeis: id= "+id );  
    DBTransaction dbTran = ((ApplicationModuleImpl)dm).getDBTransaction();
    boolean ret = false;  // neni FEIS
    int ret1 = 0;         // neni FEIS
    CallableStatement st = null;
    
    try {
      
      String sqlStm = "begin db_jt.p_isSpolecnostFEIS_Doklad(?,?); end;"; /*  st = dbTran.createCallableStatement("begin db_jt.p_isSpolecnostFEIS_Doklad(?,?); end;",0);           */
 //     String sqlStm = "begin db_jt.p_test(?,?); end;";
      
      st = dbTran.createCallableStatement(sqlStm,0);
      st.registerOutParameter(2, Types.INTEGER);
      st.setInt(1,id);
      st.execute();
      ret1 = st.getInt(2);
    }
    catch (SQLException s) {
      //s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo voln procedury db_jt.p_isSpolecnostFEIS_Doklad :: "+id,s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    
    if (ret1 == 1)
        ret = true; // je FEIS
        
   return ret;
  }
  
  
  private void outputUverovaPozice() {
//    java.util.SortedMap mSumy = new java.util.TreeMap();
    int listNr=1;
    int rowNr=4;      /* 02/2010 int rowNr=2;  */

    ViewObject vo = dm.findViewObject("VwKpDokladuverovapozice2011SpecialView1");
    vo.clearCache();
    String whereUp = "ID_DOKLAD IN ( "+Utils.getWhereDokladIds(datum, ucSkup)+" ) AND S_LOCALE = 'cs_CZ' AND (S_LISTGROUPCODE like '";
    switch(ucSkup) {
      case -2: whereUp += "S1"; break;  //banka
      case -1: whereUp += "S2"; break;
      case  1: whereUp += "S3"; break;   //RKC
      case  4: whereUp += "S3"; break;
      default: whereUp += "S3";
    }
    whereUp += "%' OR S_LISTGROUPCODE like 'N%' OR S_LISTGROUPCODE IS NULL)";
//esc 26.08.2011 test        whereUp += " AND s_ucet != '3510010'";
    vo.setWhereClause(whereUp);

//esc dbg
//System.out.println ( "outputUverovaPozice: "+whereUp ); 
logger.info("ESExportUverove:outputUverovaPozice: "+whereUp );  
logger.info("ESExportUverove:outputUverovaPozice getQuery: "+vo.getQuery() );  
    String key;
    double cLokal, cEkvivalent, pom;
//esc
    Double cEkvivalentDouble;
    while ( vo.hasNext() ) {
    
      Row row = vo.next();
      rowNr++;
      int colNr = 0;
      int idProti = row.getAttribute("IdUnifproti")==null ? 0 : ((Number)row.getAttribute("IdUnifproti")).intValue();
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("nazevSpol"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;                
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;               
      //cEkvivalent = ((Number)row.getAttribute("CastkaCZK")).doubleValue();     
      //esc 2.8.2010
      cEkvivalent = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );     
      setCellValue( listNr, rowNr, colNr, cEkvivalent, null );        // CNB fix kurz
      colNr++;
      cLokal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      setCellValue( listNr, rowNr, colNr, cLokal, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("menaSpol"), null );
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
      //setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SEvidovatprotistranu"), null );
      colNr++;
      double kategorieKoef = row.getAttribute("NdKategorieKoef")!=null ? ((Number)row.getAttribute("NdKategorieKoef")).doubleValue() : 0;
      setCellValue( listNr, rowNr, colNr, kategorieKoef, null );    //rizikovost
      colNr++;
      double angazovanost = (idProti==29390 || idProti==46033 || idProti==29419) ? 0 : cEkvivalent * kategorieKoef;
      // J&T BANKA Praha esk nrodn banka J&T BANKA, a.s., poboka zahraninej banky
      //double angazovanost = cEkvivalent * kategorieKoef;
      double cistaangazovanost = row.getAttribute("NdCistaangazovanost")!=null ? ((Number)row.getAttribute("NdCistaangazovanost")).doubleValue() : angazovanost; //esc 03 2011
      angazovanost =  cistaangazovanost;
      setCellValue( listNr, rowNr, colNr, angazovanost, null );
      colNr++;
      if(kapital != 0.0) 
        setCellValue( listNr, rowNr, colNr, 100.0*angazovanost/kapital, null );
      colNr++;
                  
      String CBanka = (String)row.getAttribute("CBanka");
      
//esc 27.05
      //String idDoklad = (String)row.getAttribute("IdDoklad");
      int idDoklad = ((Number) row.getAttribute("IdDoklad")).intValue(); 
      
 logger.debug("UVEROVKA detail ucSkup=" + ucSkup + " idDoklad:"+idDoklad );
 
    try {       
          if ( ucSkup == 1 && (isSpolFeis(idDoklad)) )
          {       
              if ( isBanka(idProti) == 0 )          
                    CBanka = "Y";
              else if ( isBanka(idProti) == 1 )          
                    CBanka = "N";
          }
    } 
    catch(KisException e)  {
              logger.error(e.getMessage() + "ERR: CBanka=" + CBanka+" idDoklad=" + idDoklad, e);
              //logger.error(e.getMessage() + " CBanka=" + CBanka, e);                                 
            }
    finally
           {
              logger.debug("FIN: UVEROVKA detail ucSkup=" + ucSkup + " idDoklad: "+idDoklad );                            
                   // CBanka = "N";
           };      
       
      //setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CBanka"), null );      
      setCellValue( listNr, rowNr, colNr, CBanka, null );      
      colNr++; /* prazdny stlpec BDP/TDP  */
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopisoriginal"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, angazovanost - cistaangazovanost, null );             // skryty stlpec , info o tom ktora angazovanost bola pouzita

logger.debug("ESExportUverove:outputUverovaPozice::"+rowNr+":: idProti:"+idProti +" cEkvivalent:"+cEkvivalent+" cLokal="+cLokal+" key:"+key+" kategorieKoef:"+kategorieKoef+" angazovanost="+angazovanost+" cistaangazovanost="+cistaangazovanost);  
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    logger.debug("ESExportUverove:outputUverovaPozice: COMMIT" );      
  }

  private void outputUPSouhrn() {
    CellStyle styleBold, styleSkup, styleSpol, styleSkupBanka, styleSkupNebanka;
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
    styleSkup.setFillForegroundColor(IndexedColors.ROSE.getIndex());
    styleSkup.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleSpol = wb.createCellStyle();
    styleSpol.setFont(font2);
    styleSpol.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    styleSpol.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    styleSkupBanka = wb.createCellStyle();
    styleSkupBanka.setFont(font1);
    styleSkupBanka.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
    styleSkupBanka.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    
    styleSkupNebanka = wb.createCellStyle();
    styleSkupNebanka.setFont(font1);
    styleSkupNebanka.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    styleSkupNebanka.setFillPattern(FillPatternType.SOLID_FOREGROUND);


    int listNr=0;
    int rowNr=5;    //int rowNr=7;
    int colNr=0;
    String kod = "";    
    /*02/2010 esc*/
    /*double kap15 = kapital * 0.15;
      double kap60 = kapital * 0.6;
      double kap20 = kapital * 0.2; */

    double kap25nebanka = kapital * 0.25; // nebanka
    double kap25banka   = kapital * 0.25; // banka
    //double kursEUR      = 24.605;       //  k 31.10.2010 plati cely rok 2011
    //double kursEUR      = 24.8;         //  k 31.10.2011 plati cely rok 2012
    //double kursEUR      = 25.065;       //  k 31.10.2012 plati cely rok 2013        
    double kursEUR      = 25.72;          //  k 31.10.2013 plati cely rok 2014        
    double limit150mio  = kursEUR * 150000000 ;   //150 mio EUR !
    
    double sumaSkup     = 0.0;
    double sumaProti    = 0.0;
    double maxAngazovanostSkup = 0.0;         //BANKA
    double maxAngazovanostSpol = 0.0;         //BANKA
    //03 2011
    double maxAngazovanostSkupNeBanka = 0.0;  //NEBANKA
    double maxAngazovanostSpolNeBanka = 0.0;  //NEBANKA

    Skupina     lastGroup = null;
    Protistrana lastProti = null;
    Spolecnost  lastSpol  = null;
    Radek       lastRadek = null;
           
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    setCellValue( listNr, 0, 1, sdf.format(datum), null );
    
    if ( kap25banka < limit150mio )
    {
        kap25banka = limit150mio;
    }

logger.debug("outputUPSouhrn: kapital=" + kapital + " kap25nebanka=" + kap25nebanka + " kap25banka=" + kap25banka);

    try {
    setCellValue( listNr, 0, 5, kapital, null );
    setCellValue( listNr, 1, 5, kap25nebanka, null );
    setCellValue( listNr, 2, 5, kap25banka, null );
    } catch(NumberFormatException e)
    {
      logger.error(e.getMessage() + " kapital=" + kapital + " kap25nebanka=" + kap25nebanka + " kap25banka=" + kap25banka);
      throw e;
    }
    /*02/2010*/        /*setCellValue( listNr, 3, 8, kap15, null );    setCellValue( listNr, 4, 8, kap60, null );    */
    BeanListSkupina bls = new BeanListSkupina();
    
    ViewObject vo = dm.findViewObject("VwKpDokladuverovapozice2011SpecialView1");
    vo.clearCache();
    String whereUp = "ID_DOKLAD IN ( "+Utils.getWhereDokladIds(datum, ucSkup)+" ) AND S_LOCALE = 'cs_CZ' AND (S_LISTGROUPCODE like '";
    switch(ucSkup) {
      case -2: whereUp += "S1"; break;
      case -1: whereUp += "S2"; break;
      case 1: whereUp += "S3"; break;
      case 4: whereUp += "S3"; break;
      default: whereUp += "S3";
    }
    whereUp += "%' OR S_LISTGROUPCODE like 'N%' OR S_LISTGROUPCODE IS NULL)";
    vo.setWhereClause(whereUp);
    vo.setOrderByClause("S_LISTGROUP, S_UNIFPROTI_ICO, NAZEV_SPOL, S_POPIS");

logger.debug("ESExportUverove:outputUPSouhrn: whereUp="+ whereUp);      
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int idProti = row.getAttribute("IdUnifproti")==null ? 0 : ((Number)row.getAttribute("IdUnifproti")).intValue();
logger.debug("ESExportUverove:outputUPSouhrn: while245 ... idProti  "+idProti);            

//esc 27.05
//      String idDoklad = (String)row.getAttribute("IdDoklad");
      int idDoklad = ((Number) row.getAttribute("IdDoklad")).intValue(); 
      String group = (String)row.getAttribute("SListgroup");
      String proti = (String)row.getAttribute("SUnifproti");
      String protiIco = (String)row.getAttribute("SUnifprotiIco");
      String nazevSpol = (String)row.getAttribute("nazevSpol");
      String popis = (String)row.getAttribute("SPopis");
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      String mena = (String)row.getAttribute("SMena");
      //double castkaCzk = ((Number)row.getAttribute("CastkaCZK")).doubleValue();
      //esc 2.8.2010
      double castkaCzk = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );     
      kod = (String)row.getAttribute("SListgroupcode");
      double kategorieKoef = row.getAttribute("NdKategorieKoef")!=null ? ((Number)row.getAttribute("NdKategorieKoef")).doubleValue() : 0;
      double angazovanost = (idProti==29390 || idProti==46033 || idProti==29419) ? 0 : castkaCzk * kategorieKoef;
                          /*0047115378	J&TB-concern          0048136450	esk nrodn banka      0035964693	J&T BANKA, a.s., poboka zahraninej banky*/
      //esc 29.3.2011
      double cistaAngazovanost = ( row.getAttribute("NdCistaangazovanost") != null ? ((Number)row.getAttribute("NdCistaangazovanost")).doubleValue() : angazovanost );     
      
      angazovanost = cistaAngazovanost; //esc 27.04.2011
      
      //CBanka pre RKC uverovku, dotiahneme z KIS-u
      String banka = ( row.getAttribute("CBanka") != null ? (String)row.getAttribute("CBanka") : "N");
//esc
      //CBanka pre RKC uverovku, dotiahneme z KIS-u
      //logger.debug("UVEROVKA Souhrn ucSkup=" + ucSkup +" idDoklad=" + idDoklad);
       try {
          if ( (ucSkup == 1 ) && (isSpolFeis(idDoklad)) )
            {        
              if ( isBanka(idProti) == 0 )          
                    banka = "Y";
              else if ( isBanka(idProti) == 1 )          
                    banka = "N";                         
            };            
       } 
       catch(KisException e)  {
              logger.error(e.getMessage() + " CBanka=" + banka+" idDoklad=" + idDoklad, e);              
              //logger.error(e.getMessage() + " CBanka=" + banka+" idDoklad=" + idDoklad);
            }
       finally
           {
              logger.debug("UVEROVKA Souhrn ucSkup=" + ucSkup +" idDoklad=" + idDoklad);
           }            
//
      if(kod == null)     kod = "";
      if(group==null)     group = "";
      if(protiIco==null)  protiIco = "";
      if(proti==null)     proti = "";
      if(nazevSpol==null) nazevSpol = "?";
      if(popis==null)     popis = "?";

      if(lastGroup == null || !lastGroup.nazev.equals(group)) 
      {       
        lastGroup = new Skupina();
        lastGroup.nazev = group;
        lastGroup.suma  = 0.0;                // banka
        lastGroup.sumaNeBanka   = 0.0;        // NEbanka
        lastGroup.cerpat        = kap25banka;         //03 2011 lastGroup.cerpat = !kod.startsWith("S")?kap25:kap20;
        lastGroup.cerpatNeBanka = kap25nebanka;
        
        if (lastGroup.banka == null ) 
            lastGroup.banka = banka;      
        else 
           { if (lastGroup.banka == "N")
                  lastGroup.banka = banka;  
           }
            
logger.debug("ESExportUverove:outputUPSouhrn: SKUPINA : "+ group+"- nazevSpol: "+nazevSpol+" /banka: "+lastGroup.banka);            

        if("".equals(group)) { 
          lastGroup.notSkupina = true;
          lastGroup.suma = Double.NEGATIVE_INFINITY;
          lastGroup.sumaNeBanka  =Double.NEGATIVE_INFINITY; ;      //nebanka              
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
        lastProti.banka = banka;
        //03 2011 lastProti.cerpat = !kod.startsWith("S")?kap25nebanka:kap25banka;

        lastProti.cerpat = banka.equals("Y")?kap25banka:kap25nebanka;
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
logger.debug("ESExportUverove:outputUPSouhrn: SPOLECNOST : "+ nazevSpol);                    
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
      /*if(!lastGroup.notSkupina)
        lastGroup.suma += angazovanost;*/
      //03 2011 lastGroup.suma += angazovanost;
        if (lastProti.banka.equals("Y") ) 
        {
                 if(!lastGroup.notSkupina) lastGroup.suma += angazovanost;   
        }
        else 
        {
                 if(!lastGroup.notSkupina) lastGroup.sumaNeBanka += angazovanost;   
        }
      
      if(!lastProti.notProti)
        lastProti.suma += angazovanost;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
//logger.debug("ESExportUverove:outputUPSouhrn: commit ");                
    bls.sortList();
logger.debug("ESExportUverove:outputUPSouhrn: PRINT ... ");            
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
          setCellValue( listNr, rowNr, 1, "Celkem spolenost " + lastProti.nazev, styleSpol );
          setCellValue( listNr, rowNr, 8, lastProti.suma, styleSpol );
          if(kapital!=0.0) setCellValue( listNr, rowNr, 9, 100.0*lastProti.suma/kapital, styleSpol );
          setCellValue( listNr, rowNr, 10, lastProti.cerpat - lastProti.suma, styleSpol );
        }
        if (lastProti.banka.equals("Y") ) {
          if(100.0*lastProti.suma/kapital>maxAngazovanostSpol)        maxAngazovanostSpol         = 100.0*lastProti.suma/kapital;
        }
        else {
          if(100.0*lastProti.suma/kapital>maxAngazovanostSpolNeBanka) maxAngazovanostSpolNeBanka  = 100.0*lastProti.suma/kapital;
        }
        rowNr++;
      }
      if(!lastGroup.notSkupina) {
        double moznoCerpatSkupBanka = 0.0;
        double moznoCerpatSkupNEBanka = 0.0;
        double moznoCerpatSkup = 0.0;
        
        //moznoCerpatSkup = lastGroup.cerpat - (lastGroup.sumaNeBanka+lastGroup.suma);      
        
        if (lastGroup.suma > 0.0)         
            moznoCerpatSkup = kap25banka  - (lastGroup.suma + lastGroup.sumaNeBanka);
        else
            moznoCerpatSkup = kap25nebanka - (lastGroup.suma + lastGroup.sumaNeBanka);        
            
        if (lastGroup.suma > 0.0) 
        {
              moznoCerpatSkupBanka   = kap25banka - lastGroup.suma  ;
              if ( moznoCerpatSkupBanka > moznoCerpatSkup ) moznoCerpatSkupBanka = moznoCerpatSkup ;              
        }
        if (lastGroup.sumaNeBanka > 0.0 ) 
        {
              moznoCerpatSkupNEBanka = kap25nebanka - lastGroup.sumaNeBanka ;
              if ( moznoCerpatSkupNEBanka > moznoCerpatSkup ) moznoCerpatSkupNEBanka = moznoCerpatSkup ;
        }
                       
        if(lastGroup.suma > 0.0)
        {
          for(int i=1;i<=7;i++) setCellValue( listNr, rowNr, i, (String)null, styleSkupBanka );
          setCellValue( listNr, rowNr, 0, "Celkem skupina " + lastGroup.nazev+ " BANKA", styleSkupBanka );      
          setCellValue( listNr, rowNr, 8, lastGroup.suma, styleSkupBanka );
          if(kapital!=0.0) setCellValue( listNr, rowNr, 9, 100.0*lastGroup.suma/kapital, styleSkupBanka );              
          setCellValue( listNr, rowNr, 10, moznoCerpatSkupBanka , styleSkupBanka );        
          rowNr++;
        }
        
        if(lastGroup.sumaNeBanka > 0.0) 
        {                  
          for(int i=1;i<=7;i++) setCellValue( listNr, rowNr, i, (String)null, styleSkupNebanka );
          setCellValue( listNr, rowNr, 0, "Celkem skupina " + lastGroup.nazev+ " NEBANKA", styleSkupNebanka );
          setCellValue( listNr, rowNr, 8, lastGroup.sumaNeBanka, styleSkupNebanka );
          if(kapital!=0.0) setCellValue( listNr, rowNr, 9, 100.0*lastGroup.sumaNeBanka/kapital, styleSkupNebanka );
          setCellValue( listNr, rowNr, 10,moznoCerpatSkupNEBanka , styleSkupNebanka );
          rowNr++;
        }
        
        for(int i=1;i<=7;i++) setCellValue( listNr, rowNr, i, (String)null, styleSkup );
        setCellValue( listNr, rowNr, 0, "Celkem skupina " + lastGroup.nazev, styleSkup );
        setCellValue( listNr, rowNr, 8, lastGroup.sumaNeBanka+lastGroup.suma, styleSkup );
        if(kapital!=0.0) setCellValue( listNr, rowNr, 9, 100.0*(lastGroup.sumaNeBanka+lastGroup.suma)/kapital, styleSkup );
        setCellValue( listNr, rowNr, 10, moznoCerpatSkup , styleSkup );        

  setCellValue( listNr, rowNr, 11, lastGroup.cerpat , styleSkup );        
        
      }

      //03 2011 if(maxAngazovanostSkup==0.0) maxAngazovanostSkup = 100.0*lastGroup.suma/kapital;
      //if (lastGroup.banka.equals("Y") ) {
          if( 100.0*lastGroup.suma/kapital > maxAngazovanostSkup ) maxAngazovanostSkup                      = 100.0*lastGroup.suma/kapital;
      //}      else {
          if( 100.0*lastGroup.sumaNeBanka/kapital > maxAngazovanostSkupNeBanka ) maxAngazovanostSkupNeBanka = 100.0*lastGroup.sumaNeBanka/kapital;
      //}
      rowNr+=3;
    }
//logger.debug("ESExportUverove:outputUPSouhrn: PRINT ...end ");            
//esc 03 2011
//    setCellValue( listNr, 1, 8, "Spolenost", null );
//    setCellValue( listNr, 2, 8, "Skupina", null );
    logger.debug("maxAngazovanostSpol= "+maxAngazovanostSpol+" maxAngazovanostSkup= "+maxAngazovanostSkup+" maxAngazovanostSpolNeBanka= "+maxAngazovanostSpolNeBanka+" maxAngazovanostSkupNeBanka= "+maxAngazovanostSkupNeBanka);
    
    setCellValue( listNr, 1, 7, maxAngazovanostSpol, null );
    setCellValue( listNr, 2, 7, maxAngazovanostSkup, null );
    setCellValue( listNr, 1, 8, maxAngazovanostSpolNeBanka, null );
    setCellValue( listNr, 2, 8, maxAngazovanostSkupNeBanka, null );        
    setCellValue( listNr, 2, 9, Math.max(maxAngazovanostSkup,maxAngazovanostSkupNeBanka), null );        
  }

  protected boolean outputData () 
  {
    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();    
logger.debug("call: outputUverovaPozice");
    outputUverovaPozice();
logger.debug("call: outputUPSouhrn");
    outputUPSouhrn();

    end = System.currentTimeMillis();    
    logger.debug("uverova p.:"+((end-start)/1000.0)+"s");
    
    return true;
  }

  public static String getDir(int us) 
  {
    String dirUs = Constants.DIR_POZICE_MU+us;
    return getDir(dirUs, "UverovePozice", "muProtokol");
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
System.out.println(Utils.getLastDate());      
//esc      ESExportUverove ed = new ESExportUverove((ApplicationModuleImpl)dm,
      ESExportUverove ed = new ESExportUverove(dm,
                                               Utils.getDate(31,12,1970),
                                               //Utils.getLastDate(),
                                               1);
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

/*
 * 
 select distinct d.id, us.id, us.s_nazev from db_jt.kp_rel_subkonsolidaceclen c, db_jt.kp_ktg_subkonsolidace s, db_jt.kp_ktg_ucetniSpolecnost us, DB_JT.KP_DAT_DOKLAD D 
where us.id = c.id_ktgUcetniSpolecnost and s.id_ktgUcetniSpolecnost = c.id_ktgsubkonsolidace and c.c_clen = '1' and c.ID_CISSUBTYPCLENSTVI <> 3 
and s.id_ktgUcetniSkupina = 1 and D.ID_KTGUCETNISPOLECNOST = US.ID AND D.DT_DATUM = TO_DATE('10.09.2007','DD.MM.YYYY') 
AND (D.DT_DATUM >= C.DT_ClenstviOD OR C.DT_ClenstviOD IS NULL) AND (D.DT_DATUM <= C.DT_ClenstviDO OR C.DT_ClenstviDO IS NULL) 
AND D.NL_KROK IN (1,2) AND D.C_BASEDOKLAD = '1' ;

 */
    
  }
  
}
