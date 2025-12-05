package cz.jtbank.konsolidace.doklady;
import cz.jtbank.konsolidace.common.*;
import cz.jtbank.konsolidace.csv.*;
import cz.jtbank.konsolidace.dokument.VwDatSchvalovakradekViewImpl;
import cz.jtbank.konsolidace.dokument.VwRepSchvalovakhlavaViewImpl;
import cz.jtbank.konsolidace.dokument.VwRepSchvalovakhlavadetailViewImpl;
import cz.jtbank.konsolidace.jobs.*;
import java.io.*;
//import java.sql.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Statement;
import java.sql.ResultSet;
import cz.jtbank.konsolidace.excel.*;
import cz.jtbank.konsolidace.excel.cartesis.*;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;
import cz.jtbank.konsolidace.admin.KpParametryViewImpl;
import cz.jtbank.konsolidace.subkons.KpKtgSubkonsolidaceetapaViewImpl;
import cz.jtbank.konsolidace.subkons.VwKpSubkonsolidaceViewImpl;
import cz.jtbank.konsolidace.users.KtgAppuserViewImpl;
import cz.jtbank.konsolidace.users.KpKtgEmailzpravyViewImpl;
import cz.jtbank.konsolidace.fininv.KpCisMenaViewImpl;
import cz.jtbank.konsolidace.protistrany.VwProtistranaViewImpl;
import cz.jtbank.konsolidace.ucskup.KpDatAuditViewImpl;
import cz.jtbank.konsolidace.dokument.VwDatSchvalovakViewImpl;
import cz.jtbank.konsolidace.dokument.VwKtgOdborViewImpl;
import cz.jtbank.konsolidace.dokument.KpDatDoczastupViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetpolozkaViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgettransakceViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetpolozkadata1ViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetpolozkadata2ViewImpl;
import cz.jtbank.konsolidace.dokument.KpCisTyptransakceViewImpl;
import cz.jtbank.konsolidace.admin.KpKalendarakciViewImpl;
import cz.jtbank.konsolidace.subkons.KpRelSubkonsolidaceclenViewImpl;
import cz.jtbank.konsolidace.projekt.KpKtgProjektViewImpl;
import cz.jtbank.konsolidace.budget.KpDatBudgetzastupViewImpl;
import cz.jtbank.konsolidace.dokument.KpCisTyptransakceEditViewImpl;
import cz.jtbank.konsolidace.projekt.VwKtgProjektViewImpl;
import cz.jtbank.konsolidace.subkons.KpRelSubkonsolidaceclenSpecialImpl;
import cz.jtbank.konsolidace.subkons.KpDatDokladvazbySpecialViewImpl;
import cz.jtbank.konsolidace.subkons.VwKpDokladvazbyViewImpl;
import cz.jtbank.konsolidace.evi.VwKpEviobchodnirejstrik3mViewImpl;

import cz.jtbank.konsolidace.mail.Mail;
import cz.jtbank.konsolidace.ucskup.KpKtgUcetniskupinaViewImpl;
import cz.jtbank.konsolidace.users.KpRelAppuserskupinaViewImpl;
import cz.jtbank.konsolidace.evi.VwKtgEviadminspolecnostViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetschvalovaniViewImpl;
import cz.jtbank.konsolidace.protistrany.VwProtistranahistorieViewImpl;
import cz.jtbank.konsolidace.budget.KpCisTyptransakcegroupViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetpolozkadata1PARViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetpolozkadata2PARViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetProjektpolozkadata1PARViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetProjektpolozkadata2PARViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetprojektViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetprojekttransakceViewImpl;
import cz.jtbank.konsolidace.budget.VwDatBudgetprojtransakcedocViewImpl;
import cz.jtbank.konsolidace.projekt.KpRelProjektucspolViewImpl;
import cz.jtbank.konsolidace.dokument.VwRepSchvalovakpostupViewImpl;
import cz.jtbank.konsolidace.ifrs.KpDatMngsegmentbossViewImpl;
import cz.jtbank.konsolidace.mustky.ListDokladViewImpl;
import cz.jtbank.konsolidace.projekt.VwKtgProjektsimpleViewImpl;
import cz.jtbank.konsolidace.ifrs.KpCisMngsegmentViewImpl;
import cz.jtbank.konsolidace.ucskup.VwKtgUcetnispolecnostViewImpl;
import cz.jtbank.konsolidace.ucskup.VwRelSpolecnostKartaTypViewImpl;
import cz.jtbank.konsolidace.subkons.KpDatSpolecnostlistViewImpl;
import cz.jtbank.konsolidace.subkons.KpDatSpolecnostlistdetailViewImpl;
import cz.jtbank.konsolidace.ifrs.KpDatSegmentzastupViewImpl;
import cz.jtbank.konsolidace.cartesis.VwRepSpolecnostcartesisViewImpl;
import cz.jtbank.konsolidace.cartesis.VwRepOsobycartesisViewImpl;
import cz.jtbank.konsolidace.cartesis.KpDatZdaFlowZustatekViewImpl;
import cz.jtbank.konsolidace.cartesis.KpDatZdaFlowZustatekLocalViewImpl;
import cz.jtbank.konsolidace.cartesis.KpDatZdaFlowProtistranaViewImpl;
import cz.jtbank.konsolidace.cartesis.KpDatZdaFlowProtistranaLocalViewImpl;
import cz.jtbank.konsolidace.cartesis.KpDatZdaFlowSegmentLocalViewImpl;
import cz.jtbank.konsolidace.dokument.VwRepSchvalovakpostupDetailViewImpl;
import cz.jtbank.konsolidace.dokument.VwDatSchvalovakView_OLDImpl;
import cz.jtbank.konsolidace.dokument.VwDatSchvalovakView2012Impl;

//  ---------------------------------------------------------------
//  ---    File generated by Oracle Business Components for Java.
//  ---------------------------------------------------------------

public class DokladyModuleImpl extends ApplicationModuleImpl implements cz.jtbank.konsolidace.doklady.common.DokladyModule  
{
  static Logger loggerAuto = Logger.getLogger(DokladyModuleImpl.class);
  static { loggerAuto.addAppender(Logging.getAppender(Logging.LOG_AUTO_GEN)); }
  
  static Logger logger = Logger.getLogger(DokladyModuleImpl.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_DEFAULT)); }
  
  private static boolean tenOk = true;

  /**
   * 
   * This is the default constructor (do not remove)
   */
  public DokladyModuleImpl()
  {
//System.out.println("konstruktor DokladyModuleImpl()");
  }





  /**
   * 
   * Sample main for debugging Business Components code using the tester.
   */
  public static void main(String[] args)
  {
    DokladyModuleImpl dm = (DokladyModuleImpl)
      oracle.jbo.client.Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
    try {
      //dm.vazbyMan(242724);
      //dm.exportDokladyProjekty();
      System.out.println(dm.getZamekCheckboxy(1001,"2006-05-31"));
    }
    catch(Exception e) 
    {
      e.printStackTrace();
    }
    //launchTester("cz.jtbank.konsolidace.doklady", "DokladyModuleLocal");
  }

  private java.text.DateFormat df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.MEDIUM);

  public String getLogFiles(boolean all, boolean delete) 
  {
    String dirName = Constants.PROTOKOL_FILES_PATH;
    File dir = new File(dirName);
    StringBuffer buf = new StringBuffer();
    File[] arr = dir.listFiles();
    if(arr != null) {
      for( int i=0; i<arr.length; i++ ) 
      {
        if(arr[i].isDirectory()) continue;
        if(delete && arr[i].getName().indexOf(".log")>=0) {
          buf.append("<a href=\"LogsDelete.jsp?file=" + arr[i].getName() + "\" title=\"Smazat\">X</a>&nbsp;");
        }
        if(all || arr[i].getName().startsWith("P_")) {
          if(!all || delete || !(arr[i].getName().startsWith("P_"))) {
            buf.append("<a href=\"logsservlet?file=" + arr[i].getName() + "\">" + arr[i].getName()+ "</a>");
            buf.append("&nbsp;" + df.format( new java.util.Date(arr[i].lastModified())) + "<br>\n");
          }
        }
      }
    }
    return buf.toString();
  }

  public void deleteStareDoklady() throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_frontaDeleteDoklady; end;",0);
      st.execute();
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_frontaDeleteDoklady",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }
  
  private String getUser() 
  {
    String user = cz.jtbank.konsolidace.common.Utils.getUserName(this, false);
    return user;
  }
/*
  public void calculateDokladOld() throws KisException
  {
    Date startDate, endDate;
    int dokladId = -1, dokladIdIfrs = -1;
    int dokladIdKamil = -1, dokladIdIfrsKamil = -1;
    boolean isAutomat = false;
    boolean isDenni = false;
    int restGenUser = 0;
    int idRadku = -1;
    int jenomPs = 0;
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    try {
      st = dbTran.createCallableStatement("begin db_jt.p_recompilePackage(?); end;",0);
      st.registerOutParameter(1, Types.INTEGER);
      st.execute();
      int pocetInvalidnich = st.getInt(1);
      if(pocetInvalidnich>0) {
        logger.debug("db_jt.p_recompilePackage vrátilo hodnotu "+pocetInvalidnich);
      }
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.p_recompilePackage",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { }
    }

    startDate = new Date();
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_calculate_doklad (?,?,?,?,?,?,?,?); end;",0);
      st.registerOutParameter(1, Types.INTEGER);
      st.registerOutParameter(2, Types.INTEGER);
      st.registerOutParameter(3, Types.VARCHAR);
      st.registerOutParameter(4, Types.INTEGER);
      st.registerOutParameter(5, Types.INTEGER);
      st.registerOutParameter(6, Types.INTEGER);
      st.registerOutParameter(7, Types.INTEGER);
      st.registerOutParameter(8, Types.INTEGER);
      st.execute();
      dokladId = st.getInt(1);
      dokladIdIfrs = st.getInt(2);
      isAutomat = Constants.AUTOMAT_USER.equals(st.getString(3));
      isDenni = Constants.DENNI_USER.equals(st.getString(3));
      restGenUser = st.getInt(4);
      idRadku = st.getInt(5);
      jenomPs = st.getInt(6);
      dokladIdKamil = st.getInt(7);
      dokladIdIfrsKamil = st.getInt(8);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      endDate = new Date();
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_calculate_doklad",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_calculate_doklad",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { }
    }
    
    if(dokladId>0) {
      logger.debug("dokladId="+dokladId);

      //Doklad existuje, vygenerovat excely atp.
      logger.debug("dokladId="+dokladId+",dokladIdIfrs="+dokladIdIfrs+",isAutomat="+isAutomat+",restGenUser="+restGenUser+",idRadku="+idRadku+",jenomPs="+jenomPs+",dokladIdKamil="+dokladIdKamil+",dokladIdIfrsKamil="+dokladIdIfrsKamil);
      //System.out.println("dokladId="+dokladId+",dokladIdIfrs="+dokladIdIfrs+",isAutomat="+isAutomat+",restGenUser="+restGenUser+",idRadku="+idRadku+",jenomPs="+jenomPs);
      
      if(!isDenni) {
        logger.info("calculateDoklad - generovani excelu pro dokladId="+dokladId+", dokladIdIfrs="+dokladIdIfrs);
        ExcelThread eThread = new ExcelThread(this,dokladId,dokladIdIfrs,isAutomat,restGenUser,idRadku,dokladIdKamil,dokladIdIfrsKamil);
        eThread.run(); //Provizorni reseni!
        eThread = null; System.gc(); // POKUS O UVOLNENI PAMETI
        //VAZBY manualni
        if(!isDenni && !isAutomat) {
          Number krok = (Number)getParameterFromDoklad(dokladId, "NlKrok");
          logger.info("calculateDoklad - Volani vazbyMan");
          if(krok!=null && krok.intValue()!=90) {
            vazbyMan(dokladId);
          }
        }
      }

      deleteFrontaRadek(idRadku);

      if(!isAutomat && !isDenni) {
        addSetDoklady(dokladId);
        addSetDoklady(dokladIdIfrs);
      }
    }
    
    //test zda skoncilo automaticke generovani
    if(AutoCounterNew.getInstance().checkFinished(this)) {
      checkFrontaKonecGenerovani(Constants.AUTOMAT_USER);
      logger.info("calculateDoklad - Volani doplnekGenerovani");
      doplnekGenerovani();
      logger.info("calculateDoklad - Volani vazbyAuto");
      vazbyAuto(null, 0);
    }

    //je uz potreba udelat protokol?
    AutoProtokolNew.getInstance().checkProtokol(this);
    
    //test zda skoncilo automaticke generovani M/U pozic
    if(AutoMUCounter.getInstance().checkFinished(this)) {
      logger.info("calculateDoklad - Volani genMU");
      genMU();
      MUProtokol.getInstance().createProtokol(this,-1);
      MUProtokol.getInstance().createProtokol(this,1);
      MUProtokol.getInstance().createProtokol(this,0);
      logger.info("calculateDoklad - Volani konecGenerovaniAuto");
      konecGenerovaniAuto();
      denniSpousteni();
    }
    
    if(isDenni && checkAutoGenFronta(99)==0) {
      checkFrontaKonecGenerovani(Constants.DENNI_USER);
      ulozDokladyProjekty();
    }
    
    //Databazova chyba
    if(dokladId == -10) 
    {
      endDate = new Date();
      deleteFrontaRadek(idRadku);
    }
  }
*/

  public void calculateDoklad(java.sql.Connection con) throws KisException
  {
    Date startDate, endDate;
    int dokladId = -1, dokladIdIfrs = -1;
    int dokladIdKamil = -1, dokladIdIfrsKamil = -1;
    boolean isAutomat = false;
    boolean isDenni = false;
    int restGenUser = 0;
    int idRadku = -1;
    int jenomPs = 0;
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    try {
      String sqlStm = "begin db_jt.p_recompilePackage(?); end;";
      if(con==null) {
        st = dbTran.createCallableStatement(sqlStm,0);
      }
      else 
      {
        st = con.prepareCall(sqlStm);
      }
      st.registerOutParameter(1, Types.INTEGER);
      st.execute();
      int pocetInvalidnich = st.getInt(1);
      if(pocetInvalidnich>1) {
        logger.debug("db_jt.p_recompilePackage vrátilo hodnotu "+pocetInvalidnich);
      }
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.p_recompilePackage",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { }
    }

    startDate = new Date();
    try {
      String sqlStm = "begin db_jt.kap_prim.p_calculate_doklad (?,?,?,?,?,?,?,?); end;";
      if(con==null) {
        st = dbTran.createCallableStatement(sqlStm,0);
      }
      else 
      {
        st = con.prepareCall(sqlStm);
      }
      //st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_calculate_doklad (?,?,?,?,?,?,?,?); end;",0);
	/*procedure p_calculate_doklad (aId_1 			out int,    	1
									aId_2 			out int, 		2
									aUzivatel 		out varchar2,	3 
									aPocet 		    out int, 		4
									aIdFronta 	in 	out int, 		5
									aJenomPs 		out int, 		6	
									aId_Kamil1 		out int, 		7
									aId_Kamil2 		out int ) as	8
       */									
      st.registerOutParameter(1, Types.INTEGER);
      st.registerOutParameter(2, Types.INTEGER);
      st.registerOutParameter(3, Types.VARCHAR);
      st.registerOutParameter(4, Types.INTEGER);
      st.registerOutParameter(5, Types.INTEGER);
      st.registerOutParameter(6, Types.INTEGER);
      st.registerOutParameter(7, Types.INTEGER);
      st.registerOutParameter(8, Types.INTEGER);
      st.execute();
      dokladId = st.getInt(1);
      dokladIdIfrs = st.getInt(2);
      isAutomat = Constants.AUTOMAT_USER.equals(st.getString(3));
      isDenni = Constants.DENNI_USER.equals(st.getString(3));
      restGenUser = st.getInt(4);
      idRadku = st.getInt(5);
      jenomPs = st.getInt(6);
      dokladIdKamil = st.getInt(7);
      dokladIdIfrsKamil = st.getInt(8);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      endDate = new Date();
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_calculate_doklad",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_calculate_doklad",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    
    //test zda skoncilo automaticke generovani
    if(AutoCounterNew.getInstance().checkFinished(this)) {
      checkFrontaKonecGenerovani(Constants.AUTOMAT_USER);
      logger.info("calculateDoklad - Volani doplnekGenerovani");
      doplnekGenerovani();
      logger.info("calculateDoklad - Volani vazbyAuto");
      SystemStatus.getInstance().setStatus("Automatické generování vazeb");
      try {
        vazbyAuto(null, 0);
      }
      catch(KisException e) 
      {
        logger.error("vazbyAuto(null,0) zkrachovalo",e);
      }
    }

    //je uz potreba udelat protokol?
    AutoProtokolNew.getInstance().checkProtokol(this);
    
    //test zda skoncilo automaticke generovani M/U pozic
    if(AutoMUCounter.getInstance().checkFinished(this)) {
      logger.info("calculateDoklad - Volani genMU");
      SystemStatus.getInstance().setStatus("Generování mìnových a úvìrových pozic");
      genMU();
      MUProtokol.getInstance().createProtokol(this,-1);
      MUProtokol.getInstance().createProtokol(this,1);
      MUProtokol.getInstance().createProtokol(this,0);
    }
    
    if(isDenni && checkAutoGenFronta(99)==0) {
      checkFrontaKonecGenerovani(Constants.DENNI_USER);
    }
    
    if((isDenni || isAutomat) && checkAutoGenFronta(0)==0 && checkAutoGenFronta(2)==0 && checkAutoGenFronta(99)==0) {
      logger.info("calculateDoklad - Volani konecGenerovaniAuto");
      konecGenerovaniAuto();
      logger.info("calculateDoklad - Volani denniSpousteni");
      SystemStatus.getInstance().setStatus("Denní spouštìní na závìr aut. generování");
      denniSpousteni();
      
      //exportCartesis();  //AUTOMATICKY EXCPORT EXCELU PRO CARTESIS

	  //esc  1208-2   zrusit  ... tenOK = false - nastavene v topExportNove;
	  // vypnuty OLAP 3.12.08
/*
      if(tenOk) {
        logger.info("calculateDoklad - reload data pro OLAP");
        try {
          ReloadDataOLAPThread olap = new ReloadDataOLAPThread();
          olap.start();
        }
        catch(Throwable t) 
        {
          logger.error("calculateDoklad - reload data pro OLAP - thread se nepodaril, zkousim seriove",t);
          reloadDataOLAP();
          logger.info("calculateDoklad - reload data pro OLAP - seriove dokonceno");
          SystemStatus.getInstance().reset();
        }
      }
*/
/*esc 10/2011 uz sa nepouziva
      logger.info("calculateDoklad - Volani nocniGenerovaniSubkonsolidaci");
      SystemStatus.getInstance().setStatus("Automatické generování subkonsolidací");
      nocniGenerovaniSubkonsolidaci();
*/      
      /*
      logger.info("calculateDoklad - uloz doklady projekty");
      SystemStatus.getInstance().setStatus("Automatické generování dokladù projektù");
      ulozDokladyProjekty();
      SystemStatus.getInstance().setStatus("Konec seriového generování (ještì bìží paralelní procesy!)");
      */
    }
    
    if(dokladId>0 || dokladId == -10) {
      GeneratorThread.getInstance().setDalsiHned();
    }

    //Databazova chyba
    if(dokladId == -10) 
    {
      endDate = new Date();
      deleteFrontaRadek(idRadku);
    }
//SystemStatus.getInstance().setStatus("Závìr aut. generování( calculateDoklad)");    
  }

  /*
   * PROZATIMNI RESENI - NEZ SE PODARI VYMYSLET THREAD
   */
  private void reloadDataOLAP() //throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      logger.info("reloadDataOLAP - reload dat pro OLAP - probíhá sériovì");
      SystemStatus.getInstance().setStatus("Reload dat pro OLAP - probíhá sériovì");
      st = dbTran.createCallableStatement("begin db_jt.kap_guiv.p_reloadDataOLAP; end;",0);
      st.execute();
      logger.info("reloadDataOLAP - dokonceno seriove");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_guiv.p_reloadDataOLAP ",s);
      //throw new KisException("Selhalo volání procedury db_jt.kap_guiv.p_reloadDataOLAP ",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) {}
    }
  }

  public void exportDoklad() throws KisException
  {
    Number hlpNum = null;
    boolean hned = false;

    ViewObject vo = getVwKpDokladfrontaView1();
    vo.clearCache();
    vo.setWhereClause("S_STAV = 'Export excel' AND IDSUBKONSOLIDACE IS NULL");
    vo.setOrderByClause("ID ASC");
    
//logger.debug("DokladyModuleImpl.exportDoklad() - select");  
int pocetcyklu = 1;    

    int idFronta = -9;

    while(vo.hasNext()) 
    {
      hned = true;
      Row row = vo.next();
      
      hlpNum = (Number) row.getAttribute("Id");
      
      if ( idFronta == hlpNum.intValue() ) {
        // chyba komponent - nemuze delat dvakrat to same
        vo.closeRowSet();
        return;
      }
      
      idFronta = hlpNum.intValue();
      
      int idRadku = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Aid1");
      int dokladId = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Aid2");
      int dokladIdIfrs = hlpNum==null ? -1 : hlpNum.intValue();
      String uzivatel = (String) row.getAttribute("Auzivatel");
      String spolecnost = (String) row.getAttribute("SNazev");
      hlpNum = (Number) row.getAttribute("Ajenomps");
      int jenomPs = hlpNum==null ? -10 : hlpNum.intValue();
      String aGF = (String)row.getAttribute("Aguiflag");
      if("1".equals(aGF)) jenomPs = -1;

      String sStav = (String) row.getAttribute("SStav");

      boolean isDenni = Constants.DENNI_USER.equals(uzivatel);
      boolean isAutomat = Constants.AUTOMAT_USER.equals(uzivatel);
      
      logger.debug("exportDoklad pro spolecnost "+spolecnost+" - dokladId="+dokladId+", dokladIdIfrs="+dokladIdIfrs+", uzivatel="+uzivatel+",guiFlag="+aGF+",jenomPs="+jenomPs+" pocetCyklu="+pocetcyklu++ + "  stav " + sStav + "  idRadku " + idRadku );
/*
      if(dokladId<0) {//pokud je doklad id null, pouziju ifrs-doklad id
        dokladId=dokladIdIfrs; 
        dokladIdIfrs=-1;
      }
*/
      if(!isDenni) {
        ExcelThread eThread = new ExcelThread(this,dokladId,dokladIdIfrs,false,0,idRadku,jenomPs,aGF);
        eThread.run(); //Provizorni reseni!
        eThread = null; System.gc(); // POKUS O UVOLNENI PAMETI
        logger.debug("exportDoklad - vygenerovan excel bilance");
  
        if(!isAutomat) {
          if(jenomPs>=0) {
            Number krok = null;
            if(dokladId>0) krok = (Number)getParameterFromDoklad(dokladId, "NlKrok");
            else if(dokladIdIfrs>0) krok = (Number)getParameterFromDoklad(dokladIdIfrs, "NlKrok");
            logger.info("exportDoklad - Volani vazbyMan");
            if(krok!=null && krok.intValue()!=90) {
//              if(dokladId>0) vazbyManAll(dokladId, idRadku);
//              else if(dokladIdIfrs>0) vazbyManAll(dokladIdIfrs, idRadku);
//              if(dokladIdIfrs>0) vazbyManAll(dokladIdIfrs, idRadku);
//              else if(dokladId>0) vazbyManAll(dokladId, idRadku);
              if( getGenerovatVazby(dokladId) ) vazbyManAll(dokladId, idRadku);
              if( getGenerovatVazby(dokladIdIfrs) ) vazbyManAll(dokladIdIfrs, idRadku);
            }
          }
  
          if(dokladId>0) addSetDoklady(dokladId);
          if(dokladIdIfrs>0) addSetDoklady(dokladIdIfrs);
        }
        logger.info("exportDoklad - vygenerovany vazby");
      }

      if(noDeleteDBChyba) noDeleteDBChyba = false;
      else deleteFrontaRadek(idRadku);
      //vo.removeCurrentRow();
//logger.debug("    konec vo.hasNext() " );            
    }
    vo.closeRowSet();
//logger.debug("   DokladyModuleImpl.exportDoklad() - konec select");      
      
    if(hned) ExportExcelThread.getInstance().setDalsiHned();
  }

  protected Object getParameterFromDoklad(int idDoklad, String parameter) 
  {
    Object ret = null;

    ViewObject vo = getKpDatDokladView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      ret = row.getAttribute(parameter);
    }
    vo.closeRowSet();
    
    return ret;
  }

  private boolean isDokladOut(int idDoklad) 
  {
    if(idDoklad<1) return false;
    
    boolean ret = false;
  
    ViewObject vo = getKpDatDokladView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idDoklad+" AND C_SUBSMER = 'O'");
    if(vo.hasNext()) 
    {
      ret = true;
    }
    vo.closeRowSet();
    
    return ret;
  }
  
  private boolean getGenerovatVazby(int idDoklad) 
  {
    ViewObject vo = getKpDatDokladView2();
    vo.clearCache();
    vo.setWhereClause("ID="+idDoklad+" AND C_VAZBY='1'");
    boolean genYes = vo.hasNext();
    vo.closeRowSet();
    return genYes;
  }

  public void checkSubkonsDokladExport() throws KisException
  {
    Number hlpNum = null;

    ViewObject vo = getVwKpDokladfrontaView1();
    vo.clearCache();
    vo.setWhereClause("IDSUBKONSOLIDACE IS NOT NULL AND S_STAV = 'Export excel'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      hlpNum = (Number) row.getAttribute("Id");
      int idRadku = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Aid1");
      int dokladId = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Aid2");
      int dokladIdIfrs = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Ajenomps");
      int jenomPs = hlpNum==null ? -10 : hlpNum.intValue();
      String aGF = (String)row.getAttribute("Aguiflag");
      if("1".equals(aGF)) jenomPs = -1;
      
      logger.debug("checkSubkonsDokladExport - dokladId="+dokladId+", dokladIdIfrs="+dokladIdIfrs+",guiFlag="+aGF);

      if(dokladId<0) {//pokud je doklad id null, pouziju ifrs-doklad id
        dokladId=dokladIdIfrs; 
        dokladIdIfrs=-1;
      }
      ExcelThread eThread = new ExcelThread(this,dokladId,dokladIdIfrs,false,0,idRadku,jenomPs,aGF);
      eThread.run(); //Provizorni reseni!
      eThread = null; System.gc(); // POKUS O UVOLNENI PAMETI
      logger.info("checkSubkonsDokladExport - vygenerovan excel bilance");

      addSetDoklady(dokladId);
      if(dokladIdIfrs>0 && dokladIdIfrs!=dokladId) addSetDoklady(dokladIdIfrs);
      
      /*if(jenomPs>=0) {
        Number[] ucetniSkupiny = getUcetniSkupiny();
    
        for(int i=0; i<ucetniSkupiny.length; i++) {
          ESExportVazbyNew esevn = new ESExportVazbyNew(this, 
                                                        new Number(dokladId),//(dokladIdIfrs>0 ? new Number(dokladIdIfrs) : new Number(dokladId)),
                                                        ucetniSkupiny[i], getUserMustek(dokladId));
          try {
            esevn.excelOutput();
          }
          catch (IOException ex) {
            ex.printStackTrace(); //pro zacatek
          }
          logger.info("checkSubkonsDokladExport - (ne)vygenerovan excel vazby "+ucetniSkupiny[i]);
        }
      }*/
      /*
      if(jenomPs>=0) {
        Number krok = null;
        if(dokladId>0) krok = (Number)getParameterFromDoklad(dokladId, "NlKrok");
        else if(dokladIdIfrs>0) krok = (Number)getParameterFromDoklad(dokladIdIfrs, "NlKrok");
        logger.info("checkSubkonsDokladExport - Volani vazbyMan");
        if(krok!=null && krok.intValue()!=90) {
          if( getGenerovatVazby(dokladId) ) vazbyManAll(dokladId, idRadku);
          if( getGenerovatVazby(dokladIdIfrs) ) vazbyManAll(dokladIdIfrs, idRadku);
        }
      }
      */
  
      if(noDeleteDBChyba) noDeleteDBChyba = false;
      else deleteFrontaRadek(idRadku);
    }
    vo.closeRowSet();
  }

  public void exportExceluVeFronte() throws KisException
  {
    Number hlpNum = null;

    ViewObject vo = getVwKpDokladfrontaView1();
    vo.clearCache();
    vo.setWhereClause("IDSUBKONSOLIDACE IS NULL AND S_STAV = 'Export excel'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      hlpNum = (Number) row.getAttribute("Id");
      int idRadku = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Aid1");
      int dokladId = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Aid2");
      int dokladIdIfrs = hlpNum==null ? -1 : hlpNum.intValue();
      String uzivatel = (String) row.getAttribute("Auzivatel");
      boolean isDenni = Constants.DENNI_USER.equals(uzivatel);
      hlpNum = (Number) row.getAttribute("Ajenomps");
      int jenomPs = hlpNum==null ? -10 : hlpNum.intValue();
      String aGF = (String)row.getAttribute("Aguiflag");
      if("1".equals(aGF)) jenomPs = -1;
      
      logger.debug("exportExceluVeFronte - dokladId="+dokladId+", dokladIdIfrs="+dokladIdIfrs+",guiFlag="+aGF);

      ExcelThread eThread = new ExcelThread(this,dokladId,dokladIdIfrs,false,0,idRadku,jenomPs,aGF);
      eThread.run(); //Provizorni reseni!
      eThread = null; System.gc(); // POKUS O UVOLNENI PAMETI
      logger.info("exportExceluVeFronte - vygenerovan excel bilance");

      if(dokladId>0) addSetDoklady(dokladId);
      if(dokladIdIfrs>0) addSetDoklady(dokladIdIfrs);
      
      if(!isDenni && jenomPs>=0) {
        Number krok = (Number)getParameterFromDoklad(dokladId, "NlKrok");
        logger.info("exportExceluVeFronte - Volani vazbyMan");
        if(krok!=null && krok.intValue()!=90) {
//          if(dokladId>0) vazbyManAll(dokladId, idRadku);
//          else if(dokladIdIfrs>0) vazbyManAll(dokladIdIfrs, idRadku);
//          if(dokladIdIfrs>0) vazbyManAll(dokladIdIfrs, idRadku);
//          else if(dokladId>0) vazbyManAll(dokladId, idRadku);
          if( getGenerovatVazby(dokladId) ) vazbyManAll(dokladId, idRadku);
          if( getGenerovatVazby(dokladIdIfrs) ) vazbyManAll(dokladIdIfrs, idRadku);
        }
      }

      if(noDeleteDBChyba) noDeleteDBChyba = false;
      else deleteFrontaRadek(idRadku);
    }
  }
  
  private static int semaforIdSub = -1;

  public void calculateSubDoklad(int idSubkonsolidace,
                                 java.sql.Date datum,
                                 boolean mis,
                                 String userName) throws KisException
  {
/*
    if(semaforIdSub == idSubkonsolidace) 
      throw new KisException("V tomto okamžiku nelze spustit generování subkonsolidaèního dokladu pro subkonsolidaci "+idSubkonsolidace+", protože je již spuštìno (od nìkoho jiného) a mohlo by dojít ke konfliktu... Zkuste to za chvíli znovu.",
                             new KisException(Constants.ERR_MESSAGE_ONLY));
*/
    if(userName==null) userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      semaforIdSub = idSubkonsolidace;
      st = dbTran.createCallableStatement("begin db_jt.kap_subKon.p_calculate_subKonDoklad(?,?,?,?); end;",0);
      st.setInt(1,0);
      st.setInt(2,idSubkonsolidace);
      st.setDate(3,datum);
      st.setString(4,userName);
      st.execute();
      logger.info("konec procedury db_jt.kap_subKon.p_calculate_subKonDoklad pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+userName);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_subKon.p_calculate_subKonDoklad",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_subKon.p_calculate_subKonDoklad",s);
    }
    finally {
      semaforIdSub = -1;
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }
  
  public void exportSubkonsDoklad() throws KisException
  {
    Number hlpNum = null;
    oracle.jbo.domain.Date hlpDt = null;
    CallableStatement st = null;
    DBTransaction dbTran = this.getDBTransaction();
    
    int idRadku = 0;
    boolean hned = false;
    int idSubkonsolidace = 0;
    java.sql.Date datum = null;
    boolean mis = false;
    String uzivatel = null;

    ViewObject voFronta = getVwKpKonsolidacefrontaView1();
    voFronta.clearCache();
    voFronta.setWhereClause("S_STAV = 'Export excel'");
    voFronta.setOrderByClause("ID ASC");
    while(voFronta.hasNext()) 
    {
      hned = true;
      Row row = voFronta.next();
      
      hlpNum = (Number) row.getAttribute("Id");
      idRadku = hlpNum==null ? -1 : hlpNum.intValue();
      hlpNum = (Number) row.getAttribute("Aidktgsubkonsolidace");
      idSubkonsolidace = hlpNum==null ? -1 : hlpNum.intValue();
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("Adatum");
      datum = hlpDt==null ? null : hlpDt.dateValue();
      uzivatel = (String) row.getAttribute("Auzivatel");
      String spolecnost = (String) row.getAttribute("SNazev");
      
      logger.debug("exportSubkonsDoklad pro spolecnost "+spolecnost+" - idSubkonsolidace="+idSubkonsolidace+", datum="+datum+", uzivatel="+uzivatel);

      ViewObject vo = getVwKpSubkonsolidaceView1();
      vo.clearCache();
      vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idSubkonsolidace+" AND ID_KTGUCETNISKUPINA IN (SELECT ID FROM DB_JT.KP_KTG_UCETNISKUPINA WHERE C_GENERUJSUBKONKAMIL = '1')");
      boolean isKamil = vo.hasNext();
      vo.closeRowSet();

      if(!isKamil) {    
        ESExportSubkonsDoklad essd = new ESExportSubkonsDoklad(this, idSubkonsolidace, datum, mis, false);
        try {
          essd.excelOutput();
          logger.info("ukonceno generovani xls - dokladu pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+uzivatel);
        }
        catch (Exception ex) {
          ex.printStackTrace(); //pro zacatek
          logger.error("Chyba pri generovani xls subkonsolidace "+idSubkonsolidace,ex);
          throw new KisException("Chyba pri generovani xls subkonsolidace "+idSubkonsolidace,ex);
        }
      }
      else
      {
        ESExportSubkonsDokladKamilDoklad essdk = new ESExportSubkonsDokladKamilDoklad(this, idSubkonsolidace, datum, mis);
        try {
          essdk.excelOutput();
          logger.info("ukonceno generovani xls - dokladu Kamil-Doklad pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+uzivatel);
        }
        catch (Exception ex) {
          ex.printStackTrace(); //pro zacatek
          logger.error("Chyba pri generovani xls subkonsolidace Kamil-Doklad "+idSubkonsolidace,ex);
          throw new KisException("Chyba pri generovani xls subkonsolidace Kamil-Doklad "+idSubkonsolidace,ex);
        }
      }
  
      ESExportSubkonsVazby essv = new ESExportSubkonsVazby(this, idSubkonsolidace, datum,false);
      try {
        essv.excelOutput();
        logger.info("ukonceno generovani xls - vazby pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+uzivatel);
      }
      catch (Exception ex) {
        ex.printStackTrace(); //pro zacatek
        logger.error("Chyba pri generovani xls - vazby subkonsolidace "+idSubkonsolidace,ex);
        throw new KisException("Chyba pri generovani xls - vazby subkonsolidace "+idSubkonsolidace,ex);
      }
  
      ESExportSubkonsVazbyUser essvu = new ESExportSubkonsVazbyUser(this, idSubkonsolidace, datum);
      try {
        essvu.excelOutput();
        logger.info("ukonceno generovani xls - vazby user pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+uzivatel);
      }
      catch (Exception ex) {
        ex.printStackTrace(); //pro zacatek
        logger.error("Chyba pri generovani xls - vazby user subkonsolidace "+idSubkonsolidace,ex);
        throw new KisException("Chyba pri generovani xls - vazby user subkonsolidace "+idSubkonsolidace,ex);
      }
  
      try {
        /*
        st = dbTran.createCallableStatement("delete db_jt.kp_dat_konsolidaceFronta where s_stav = 'Export excel' and aIdKtgSubKonsolidace = ?",0);
        st.setInt(1,idSubkonsolidace);
        */
        st = dbTran.createCallableStatement("delete db_jt.kp_dat_konsolidaceFronta where s_stav = 'Export excel' and id = ?",0);
        st.setInt(1,idRadku);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        logger.error("Selhalo mazání db_jt.kp_dat_konsolidaceFronta",s);
      }
      finally {
        try {
          if (st != null) st.close();
          dbTran.commit();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    voFronta.closeRowSet();
    dbTran.commit();
  
    if(hned) ExportSubkonsExcelThread.getInstance().setDalsiHned();
  }

  public void calculateSubDokladModry(int idSubkonsolidace,
                                      java.sql.Date datum) throws KisException
  {
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_subKon.p_calculate_subKonModryDoklad (?,?,?,?); end;",0);
      st.setInt(1,0);
      st.setInt(2,idSubkonsolidace);
      st.setDate(3,datum);
      st.setString(4,userName);
      st.execute();
      logger.info("konec procedury db_jt.kap_subKon.p_calculate_subKonModryDoklad pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+userName);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_subKon.p_calculate_subKonModryDoklad ",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_subKon.p_calculate_subKonModryDoklad ",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }

    ESExportSubkonsDoklad essd = new ESExportSubkonsDoklad(this, idSubkonsolidace, datum, false, true);
    try {
      essd.excelOutput();
      logger.info("ukonceno generovani xls modre - dokladu pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+userName);
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      logger.error("Chyba pri generovani xls modre subkonsolidace "+idSubkonsolidace,ex);
      throw new KisException("Chyba pri generovani xls modre subkonsolidace "+idSubkonsolidace,ex);
    }

    ESExportSubkonsVazby essv = new ESExportSubkonsVazby(this, idSubkonsolidace, datum,true);
    try {
      essv.excelOutput();
      logger.info("ukonceno generovani xls modre - vazby pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+userName);
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      logger.error("Chyba pri generovani xls modre - vazby subkonsolidace "+idSubkonsolidace,ex);
      throw new KisException("Chyba pri generovani xls modre - vazby subkonsolidace "+idSubkonsolidace,ex);
    }
/*
    ESExportSubkonsVazbyUser essvu = new ESExportSubkonsVazbyUser(this, idSubkonsolidace, datum);
    try {
      essvu.excelOutput();
      logger.info("ukonceno generovani xls - vazby user pro "+idSubkonsolidace+" a "+datum+" uzivatelem "+userName);
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      logger.error("Chyba pri generovani xls - vazby user subkonsolidace "+idSubkonsolidace,ex);
      throw new KisException("Chyba pri generovani xls - vazby user subkonsolidace "+idSubkonsolidace,ex);
    }
*/
  }
  
  public void regenerateSubDoklad(int idDoklad) throws KisException
  {
    DBTransaction dbTran = getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_subDokladRegenerace(?,?); end;",0);
      st.setInt(1,idDoklad);
      st.setString(2, getUser());
      st.execute();
      logger.info("regenerateSubDoklad - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volani db_jt.kap_prim.p_subDokladRegenerace ",s);
      throw new KisException("Selhalo volani db_jt.kap_prim.p_subDokladRegenerace ",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void deleteFrontaRadek(int idRadku)
  {
    DBTransaction dbTran = getDBTransaction();
    CallableStatement st = null;
    //Smazat pouzity radek
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_zadost_hotovo (?); end;",0);
      st.setInt(1,idRadku);
      st.execute();
      logger.info("deleteFrontaRadek - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    logger.debug("Smazan radek "+idRadku);
  }

  public void stornoFrontaRadek(int idRadku)
  {
    DBTransaction dbTran = getDBTransaction();
    CallableStatement st = null;
    //Smazat pouzity radek
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_zadost_storno (?,?); end;",0);
      st.setInt(1,idRadku);
      st.setString(2, getUser());
      st.execute();
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    logger.debug("Smazan radek "+idRadku);
  }
  
  private int existujeDoklad(int idCisDoklad, 
                             int idSpol,
                             java.sql.Date genDatum,
                             int krok) 
  {
    int ret = -1;
    ViewObject vo = getKpDatDokladView1();
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idSpol+
                 " AND "+idCisDoklad+" = (select cis.ID_CISDOKLAD from db_jt.kp_ktg_doklad cis where cis.id = ID_KTGDOKLAD)"+
                 " AND DT_DATUM = TO_DATE('"+sdf.format(genDatum)+"','dd.mm.yyyy')"+
                 " AND NL_KROK = "+krok);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      ret = ((Number)row.getAttribute("Id")).intValue();
    }
    vo.closeRowSet();
    return ret;
  }

  public void genDoklad(String user,
                        int idCisDoklad, 
                        int idKtgSpolecnost, 
                        int krok,
                        java.sql.Date genDatum, 
                        java.sql.Date datumMustek, 
                        int jenomPS,
                        boolean master,
                        String guiFlag) throws KisException
  {
    String userName;
    if(user!=null) userName = user;
    else userName = getUser();
    String mustekUserName = master?"master":userName;
    
    //if(idKtgSpolecnost==9999) krok=90;
    int idDoklad = existujeDoklad(idCisDoklad,idKtgSpolecnost,genDatum,1);
    int idDokladIfrs = existujeDoklad(idCisDoklad,idKtgSpolecnost,genDatum,2);
    //if(!"1".equals(guiFlag) || idDoklad<0)
    {
      DBTransaction dbTran = this.getDBTransaction();
      CallableStatement st = null;
      try {
        st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_zadost_generuj_doklad (?,?,?,?,?,?,?,?,?); end;",0);
        st.setInt(1, idCisDoklad);
        st.setInt(2, krok);
        st.setInt(3, idKtgSpolecnost);
        st.setDate(4, genDatum);
        st.setDate(5, datumMustek);
        st.setString(6, mustekUserName);
        st.setString(7, userName);
        st.setInt(8, jenomPS);
        st.setString(9, guiFlag);
        st.execute();
        logger.info("genDoklad - dokonceno");
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        logger.error("Selhalo volání procedury db_jt.kap_prim.p_zadost_generuj_doklad",s);
        throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_zadost_generuj_doklad",s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    /*else 
    {
      genExcelFile(idDoklad,"1");
      if(idDokladIfrs>0) genExcelFile(idDokladIfrs,"1");
    }*/

  }


  public int genDokladManual(int idCisDoklad, 
                             int idKtgSpolecnost, 
                             java.sql.Date genDatum, 
                             boolean master,
                             boolean export,
                             int idSub) throws KisException
  {
    String userName = getUser();
    int doklId = -1;
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_generuj_doklad_manual (?,?,?,?,?,?); end;",0);
      st.registerOutParameter(1, Types.INTEGER);
      st.setInt(2, idCisDoklad);
      st.setInt(3, idKtgSpolecnost);
      st.setInt(4, idSub);
      st.setDate(5, genDatum);
      st.setString(6, userName);
      st.execute();
      doklId = st.getInt(1);
      logger.info("genDokladManual - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_generuj_doklad_manual",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_generuj_doklad_manual",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }

    addSetDoklady(doklId);
    if(export) {
      ESExportDoklady esedPs = new ESExportDoklady(this,
                                                   new Number(doklId),
                                                   //false,
                                                   null,
                                                   null);
      try {
        esedPs.excelOutput();
      }
      catch (IOException ex) {
        ex.printStackTrace(); //pro zacatek
      }
      logger.info("genDokladManual - dogenerovan excel");
    }
    else 
    {
      logger.info("genDokladManual - bez generovani excelu");
    }

    return doklId;
  }

  public void genExcelManual(int idDoklad) throws KisException
  {
/*
    ESExportDoklady esedPs = new ESExportDoklady(this,
                                                 new Number(idDoklad),
                                                 ifrs);
    try {
      esedPs.excelOutput();
    }
    catch (IOException ex) {
      ex.printStackTrace(); //pro zacatek
    }
    
    vazbyMan(idDoklad);
*/    
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_calculate_doklad_manual(?,?); end;",0);
      st.setInt(1, idDoklad);
      st.setString(2, userName);
      st.execute();
      logger.info("genExcel - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_calculate_doklad_manual",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_calculate_doklad_manual",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void cloneManual(int idDoklad, int idSub, java.sql.Date datum) throws KisException
  {
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_clone_doklad_manual(?,?,?,?); end;",0);
      st.setInt(1, idDoklad);
      st.setInt(2, idSub);
      st.setDate(3, datum);
      st.setString(4, userName);
      st.execute();
      logger.info("cloneManual - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_clone_doklad_manual",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_clone_doklad_manual",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void manExcelReadVazby(int idDoklad,
                                int idSpol,
                                int list,
                                int radek,
                                String mena,
                                String ucet,
                                double castkaMena,
                                double castkaLocal) throws KisException
  {
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_gui.p_KpDokladManualReadVazby(?,?,?,?,?,?,?,?,?); end;",0);
      st.setInt(1, idDoklad);
      st.setInt(2, idSpol);
      st.setInt(3, list);
      st.setInt(4, radek);
      st.setString(5, mena);
      st.setString(6, ucet);
      st.setDouble(7, castkaMena);
      st.setDouble(8, castkaLocal);
      st.setString(9, userName);
      st.execute();
      logger.info("manExcelReadVazby - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_KpDokladManualReadVazby",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_KpDokladManualReadVazby",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void genExcelFile(int idDoklad, String ps) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_gui.p_KpDokladManualExport(?,?); end;",0);
      st.setInt(1, idDoklad);
      st.setString(2, ps);
      st.execute();
      logger.info("genExcelFile - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_KpDokladManualExport",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_KpDokladManualExport",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public String bookTest(int id, 
                         java.sql.Date startDatum, 
                         java.sql.Date genDatum) throws KisException
  {
    String retVal = "";
    int doklId;
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin ? := db_jt.f_spinTestHlavniKnihy (?,?,?); end;",0);
      st.registerOutParameter(1,Types.VARCHAR);
      st.setInt(2, id);
      st.setDate(3, startDatum);
      st.setDate(4, genDatum);
      st.execute();
      retVal = st.getString(1);
      logger.info("bookTest - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.f_spinTestHlavniKnihy",s);
      throw new KisException("Selhalo volání procedury db_jt.f_spinTestHlavniKnihy",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    
    return retVal;
  }


  public void manEditBunka(int id,
                           double value) throws KisException
  {
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_gui.p_KPDokladManualEditBunka (?,?,?); end;",0);
      st.setInt(1, id);
      st.setDouble(2, value);
      st.setString(3, userName);
      st.execute();
      logger.info("manEditBunka - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_KPDokladManualEditBunka",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_KPDokladManualEditBunka",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void deleteAgregace(int idDoklad) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("delete db_jt.kp_dat_dokladZdrojDatAgregace where id_doklad = ?",0);
      st.setInt(1, idDoklad);
      st.execute();
      logger.info("deleteAgregace pro "+idDoklad+" - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury deleteAgregace pro "+idDoklad,s);
      throw new KisException("Selhalo volání procedury deleteAgregace pro "+idDoklad,s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    dbTran.commit();
  }

  public void manEditBunka(String akce,
                           int idAgregace,
                           int idDoklad,
                           int idDefBunka,
                           String ucet,
                           String mena,
                           double castkaMena,
                           double castkaLocal,
                           int idSpol) throws KisException
  {
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_gui.p_KPDokladManualEditBunka (?,?,?,?,?,?,?,?,?,?); end;",0);
      st.setString(1, akce);
      st.setInt(2, idAgregace);
      st.setInt(3, idDoklad);
      st.setInt(4, idDefBunka);
      st.setString(5, ucet==null?null:ucet.trim());
      st.setString(6, mena);
      st.setDouble(7, castkaMena);
      st.setDouble(8, castkaLocal);
      if(idSpol>0) 
        st.setInt(9, idSpol);
      else
        st.setNull(9, Types.INTEGER);
      st.setString(10, userName);
      st.execute();
      logger.info("manEditBunka - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_KPDokladManualEditBunka",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_KPDokladManualEditBunka",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void vazbyParovani(int idDoklad,
                            int idSkup,
                            String ucet,
                            int list,
                            int radek,
                            int proti,
                            double castka,
                            double castkaLocal,
                            int idVazby,
                            int indexPM) throws KisException
  {
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_jt.kap_gui.p_KPDokladVazbyParovani(?,?,?,?,?,?,?,?,?,?,?); end;",0);
      st.setInt(1,idDoklad);
      st.setInt(2,idSkup);
      st.setString(3,ucet==null?null:ucet.trim());
      st.setInt(4,list);
      st.setInt(5,radek);
      st.setInt(6,proti);
      st.setDouble(7,castka);
      st.setDouble(8,castkaLocal);
      st.setInt(9,idVazby);
      st.setInt(10,indexPM);
      st.setString(11,getUser());
      st.execute();
      logger.info("Volani vazbyParovani s parametry idDoklad="+idDoklad+",idSkup="+idSkup+",proti="+proti+",ucet="+ucet+",idVazby="+idVazby+",indexPM="+indexPM+",castka="+castka+",castkaLocal="+castkaLocal);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      logger.error("Volani vazbyParovani s parametry idDoklad="+idDoklad+",idSkup="+idSkup+",proti="+proti+",ucet="+ucet+",idVazby="+idVazby+",indexPM="+indexPM);
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_KPDokladVazbyParovani pro idDoklad="+idDoklad+",idSkup="+idSkup,e);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_KPDokladVazbyParovani",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
  }

  public void vazbyMan(int idDoklad, int idSkup) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_vazby.p_calculate_doklad_vazby(?,?,?); end;",0);
      st.setInt(1, idDoklad);
      st.setInt(2, idSkup);
      st.setString(3, getUser());
      st.execute();
      logger.info("vazbyMan - dokonceno idDoklad="+idDoklad+",idSkup="+idSkup);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_vazby.p_calculate_doklad_vazby 3P idDoklad="+idDoklad+",idSkup="+idSkup,s);
      throw new KisException("Selhalo volání procedury db_jt.kap_vazby.p_calculate_doklad_vazby 3P",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void vazbyManJednotlive(int idDoklad, int idProti, int idSkup) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_vazby.p_calculate_doklad_vazby(?,?,?,?); end;",0);
      st.setInt(1, idDoklad);
      st.setInt(2, idProti);
      st.setInt(3, idSkup);
      st.setString(4, getUser());
      st.execute();
      logger.info("vazbyMan - dokonceno idDoklad="+idDoklad+",idSkup="+idSkup+",idProti="+idProti);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_vazby.p_calculate_doklad_vazby 4P idDoklad="+idDoklad+",idSkup="+idSkup+",idProti="+idProti,s);
      throw new KisException("Selhalo volání procedury db_jt.kap_vazby.p_calculate_doklad_vazby 4P",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void vazbyManExcel(int idDoklad, int idSkup) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    try {
      vazbyExportExcel("vazbyManExcel",new Number(idDoklad),new Number(idSkup),false,0);
      /*
      ESExportVazbyNew esevn = new ESExportVazbyNew(this, new Number(idDoklad), new Number(idSkup), getUserMustek(idDoklad));
      try {
        esevn.excelOutput();
      }
      catch (IOException ex) {
        ex.printStackTrace(); //pro zacatek
        logger.error("vazbyManExcel - dokonceno generovani excelu idDoklad="+idDoklad+",idSkup="+idSkup);
      }
      logger.info("vazbyManExcel - dokonceno generovani excelu idDoklad="+idDoklad+",idSkup="+idSkup);
      */
      
      ViewObject voTmp = getKpTmpGuiView1();
      voTmp.clearCache();
      voTmp.setWhereClause("S_UZIVATEL = '"+getUser()+"' AND ID_AKCE=10 AND ID_DOKLADEDITACE = "+idDoklad);
      while(voTmp.hasNext()) 
      {
        Row rowTmp = voTmp.next();
        Number idDokladProti = (Number) rowTmp.getAttribute("IdDoklad");
        vazbyExportExcel("vazbyManExcel(proti)",idDokladProti,new Number(idSkup),false,0);
        /*
        esevn = new ESExportVazbyNew(this, idDokladProti, new Number(idSkup), getUserMustek(idDokladProti.intValue()));
        try {
          esevn.excelOutput();
        }
        catch (IOException ex) {
          ex.printStackTrace(); //pro zacatek
          logger.error("vazbyManExcel - dokonceno generovani excelu (proti) idDoklad="+idDokladProti+",idSkup="+idSkup);
        }
        logger.info("vazbyManExcel - dokonceno generovani excelu (proti) idDoklad="+idDokladProti+",idSkup="+idSkup);
        */
      }
      voTmp.closeRowSet();
    } 
    catch(Exception e) {
      logger.error("Selhalo generování excelù vazby (man.)",e);
      throw new KisException("Selhalo generování excelù vazby (man.)",e);
    }
    finally 
    {
      try {
        st = dbTran.createCallableStatement("DELETE DB_JT.KP_TMP_GUI WHERE S_UZIVATEL=? AND ID_AKCE=10 AND ID_DOKLADEDITACE = ?",0);
        st.setString(1, getUser());
        st.setInt(2, idDoklad);
        st.execute();
        dbTran.commit();
        logger.info("vazbyManExcel - dokonceno mazani KP_TMP_GUI");
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        logger.error("Selhalo mazání DB_JT.KP_TMP_GUI",s);
        throw new KisException("Selhalo mazání DB_JT.KP_TMP_GUI",s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
  }

  private void existujeSubDoklad(int idSubkons, java.sql.Date datum) throws KisException
  {
    boolean maDoklad = false;
    ViewObject voDoklad = getKpDatDokladView1();
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+idSubkons+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                            " AND NL_KROK = 200");
    if(voDoklad.hasNext()) maDoklad=true;
    voDoklad.closeRowSet();
    
    if(!maDoklad) throw new KisException("Neexistuje doklad pro sub-konsolidaci "+idSubkons+" pro datum "+sdf.format(datum)+
                                         "!!! Nejprve je tøeba tento doklad vygenerovat, pak je teprve možno jej exportovat.", 
                                         new KisException(Constants.ERR_MESSAGE_ONLY));
  }

  public void subkons6VazbyExcel(int idSubkons, java.sql.Date datum) throws KisException
  {
    existujeSubDoklad(idSubkons, datum);
  
    ESExportSubkons6Vazby esevn = new ESExportSubkons6Vazby(this, idSubkons, datum);
    try {
      esevn.excelOutput();
      logger.info("subkons6VazbyExcel - dokonceno generovani excelu "+idSubkons);
    } 
    catch(Exception e) {
      logger.error("Selhalo generování excelu subkons6VazbyExcel",e);
      throw new KisException("Selhalo generování excelu subkons6VazbyExcel",e);
    }
  }

  public void subkonsClenExcel(int idSubkons, java.sql.Date datum) throws KisException
  {
    existujeSubDoklad(idSubkons, datum);
  
    ESExportSubkonsDokladClenove4 esevn = new ESExportSubkonsDokladClenove4(this, idSubkons, datum, false);
    try {
      esevn.excelOutput();
      logger.info("subkonsClenExcel - dokonceno generovani excelu "+idSubkons);
    } 
    catch(Exception e) {
      logger.error("Selhalo generování excelu subkonsClenExcel",e);
      throw new KisException("Selhalo generování excelu subkonsClenExcel",e);
    }
  }
  
  private Number[] temporaryUS(int idDoklad, Number[] us) 
  {
    boolean k31122004 = false;
  
    ViewObject vo = getKpDatDokladView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idDoklad+" AND DT_DATUM = TO_DATE('31.12.2004','dd.mm.yyyy')");
    if(vo.hasNext()) k31122004 = true;
    vo.closeRowSet();
    
    if(k31122004) {
      Number[] temp = new Number[us.length+1];
      int i=0;
      for(;i<us.length;i++) temp[i] = us[i];
      temp[i] = new Number(5);
      return temp;
    }
    else 
    {
      return us;
    }
  }

  private void doplnekGenerovani() throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_doplnekGenerovaniAuto; end;",0);
      st.execute();
      logger.info("doplnekGenerovani - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_doplnekGenerovaniAuto",s);
      //throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_doplnekGenerovaniAuto",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  private void vazbyManAll(int idDoklad, int idRadku) throws KisException
  {
    Number[] ucetniSkupiny = getUcetniSkupiny();
    ucetniSkupiny = temporaryUS(idDoklad, ucetniSkupiny);
  
    for(int i=0; i<ucetniSkupiny.length; i++) {
      if(ucetniSkupiny[i] != null) {
        DBTransaction dbTran = this.getDBTransaction();
        CallableStatement st = null;
        try {
          logger.info("vazbyManAll - volani kap_vazby.p_calculate_doklad_vazby pro idDoklad="+idDoklad+" a skup="+ucetniSkupiny[i]);
          st = dbTran.createCallableStatement("begin db_jt.kap_vazby.p_calculate_doklad_vazby(?,?,?); end;",0);
          st.setInt(1, idDoklad);
          st.setInt(2, ucetniSkupiny[i].intValue());
          st.setString(3, getUser());
          st.execute();
          logger.info("vazbyManAll - dokonceno kap_vazby.p_calculate_doklad_vazby pro idDoklad="+idDoklad+" a skup="+ucetniSkupiny[i]);
        }
        catch (SQLException s) {
          s.printStackTrace(); //pro zacatek
          logger.error("Selhalo volání procedury db_jt.kap_vazby.p_calculate_doklad_vazby",s);
          setDbChybaExportExcel(idRadku, s);
          throw new KisException("Selhalo volání procedury db_jt.kap_vazby.p_calculate_doklad_vazby",s);
        }
        finally {
          try {
            if (st != null) st.close();
          } 
          catch (SQLException s) { /* ignore */}
        }
    
        vazbyExportExcel("vazbyManAll",new Number(idDoklad),ucetniSkupiny[i],true,idRadku);
        /*
        boolean cartesis = CARTESIS==ucetniSkupiny[i].intValue();
        if(!cartesis) {
          ESExportVazbyNew esevn = new ESExportVazbyNew(this, new Number(idDoklad), ucetniSkupiny[i], getUserMustek(idDoklad));
          try {
            esevn.excelOutput();
          }
          catch (Throwable ex) {
            logger.error("vazbyManAll - selhalo generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+ucetniSkupiny[i]);
            ex.printStackTrace(); //pro zacatek
            setDbChybaExportExcel(idRadku, ex);
          }
          logger.info("vazbyManAll - dokonceno generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+ucetniSkupiny[i]);
        }
        else {
          CSVExportVazby csv = new CSVExportVazby(this, new Number(idDoklad), ucetniSkupiny[i]);
          try {
            csv.createCsv();
            csv.csvOutput();
          }
          catch (Throwable ex) {
            logger.error("vazbyManAll - selhalo generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+ucetniSkupiny[i]);
            ex.printStackTrace(); //pro zacatek
          }
          logger.info("vazbyManAll - dokonceno generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+ucetniSkupiny[i]);
        }
        */
      }
    }
  }

  private void vazbyExportExcel(String skama, Number idDoklad, Number ucSkup, boolean frontaChyba, int idRadku) 
  {
    boolean cartesis = CARTESIS==ucSkup.intValue();
    if(!cartesis) {
      ESExportVazbyNew esevn = new ESExportVazbyNew(this, idDoklad, ucSkup, getUserMustek(idDoklad.intValue()));
      try {
        esevn.excelOutput();
      }
      catch (Throwable ex) {
        logger.error(skama+" - selhalo generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+ucSkup);
        ex.printStackTrace(); //pro zacatek
        if(frontaChyba) setDbChybaExportExcel(idRadku, ex);
      }
      logger.info(skama+" - dokonceno generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+ucSkup);
    }
    else if(isTopas(idDoklad)) {
      CSVExportVazby csv = new CSVExportVazby(this, idDoklad, ucSkup);
      try {
        csv.createCsv();
        csv.csvOutput();
      }
      catch (Throwable ex) {
        logger.error(skama+" - selhalo generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+ucSkup);
        ex.printStackTrace(); //pro zacatek
        if(frontaChyba) setDbChybaExportExcel(idRadku, ex);
      }
      logger.info(skama+" - dokonceno generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+ucSkup);
    }
  }

  public void vazbyAuto(java.sql.Date datum, int idSkup) throws KisException
  {
    loggerAuto.info("Start generovani vzajemnych vazeb.");
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    java.sql.Date datumManual = null;
    if(datum==null) 
    {
      ViewObject vo = getKpParametryView1();
      vo.clearCache();
      if(vo.hasNext()) 
      {
        Row row = vo.next();
        datumManual = ((oracle.jbo.domain.Date) row.getAttribute("DtNgDatumDo")).dateValue();
      }
      vo.closeRowSet();
    }
    else 
    {
      datumManual = datum;
    }
    try {
      logger.info("vazbyAuto - parametry = "+datum+","+idSkup+","+datumManual);
      st = dbTran.createCallableStatement("begin db_jt.kap_vazby.p_auto_calculate_doklad_vazby(?,?,?); end;",0);
      st.setDate(1, datum);
      st.setInt(2, idSkup);
      st.setDate(3, datumManual);
      st.execute();
      logger.info("vazbyAuto - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_vazby.p_auto_calculate_doklad_vazby",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_vazby.p_auto_calculate_doklad_vazby",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    
    reexportujVazbyTmp(idSkup);
  }
    
  public boolean isTopas(Number idDoklad) 
  {
    ViewObject vo = getKpDatDokladView2();
    vo.clearCache();
    vo.setWhereClause("ID = "+idDoklad+" AND C_BASEDOKLAD = '1'");
    Number idSpol = null;
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idSpol = (Number) row.getAttribute("IdKtgucetnispolecnost");
    }
    vo.closeRowSet();
    
    vo = getKpKtgUcetnispolecnostView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idSpol+" AND TOPAS_ID IS NOT NULL");
    boolean ret = vo.hasNext();
    vo.closeRowSet();
    
    if(ret) 
    {
      vo = getKpRelSubkonsolidaceclenView1();
      vo.clearCache();
      vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idSpol+" AND ID_KTGUCETNISKUPINA = 8");
      ret = vo.hasNext();
      vo.closeRowSet();
    }
    
    return ret;
  }
    
  public void reexportujVazbyTmp(int idSkup) throws KisException {
    ViewObject vo = getKpTmpDokladvazbyView1();
    vo.clearCache();
    vo.setWhereClause("ID_KTGDOKLAD < 1000");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number idDoklad = (Number) row.getAttribute("IdDoklad");
      //boolean topas = isTopas(idDoklad);
      boolean cartesis = idSkup==CARTESIS;
      
      if(idSkup>0) 
      {
        vazbyExportExcel("vazbyAuto(1)",idDoklad,new Number(idSkup),false,0);
        /*
        if(!cartesis) {
          loggerAuto.debug("1 - Generovani xls vzajemnych vazeb pro doklad "+idDoklad+" a ucetni skupinu "+idSkup);
          ESExportVazbyNew esevn = new ESExportVazbyNew(this, idDoklad, new Number(idSkup), getUserMustek(idDoklad.intValue()));
          try {
            esevn.excelOutput();
          }
          catch (Throwable ex) {
            logger.error("vazbyAuto - selhalo generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+idSkup);
            ex.printStackTrace(); //pro zacatek
          }
          logger.info("vazbyAuto - dokonceno generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+idSkup);
        }
        else {
          CSVExportVazby csv = new CSVExportVazby(this, idDoklad, new Number(idSkup));
          try {
            csv.createCsv();
            csv.csvOutput();
          }
          catch (Throwable ex) {
            logger.error("vazbyAuto - selhalo generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+idSkup);
            ex.printStackTrace(); //pro zacatek
          }
          logger.info("vazbyAuto - dokonceno generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+idSkup);
        }
        */
      }
      else {
        Number idSkupTab = (Number) row.getAttribute("IdKtgucetniskupina");
        vazbyExportExcel("vazbyAuto(3)",idDoklad,idSkupTab,false,0);
        /*
        cartesis = CARTESIS==idSkupTab.intValue();
        if(!cartesis) {
          loggerAuto.debug("3 - Generovani xls vzajemnych vazeb pro doklad "+idDoklad+" a ucetni skupinu "+idSkupTab);
          ESExportVazbyNew esevn = new ESExportVazbyNew(this, idDoklad, idSkupTab, getUserMustek(idDoklad.intValue()));
          try {
            esevn.excelOutput();
          }
          catch (Throwable ex) {
            logger.error("vazbyAuto - selhalo generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+idSkupTab);
            ex.printStackTrace(); //pro zacatek
          }
          logger.info("vazbyAuto - dokonceno generovani excelu vazby pro idDoklad="+idDoklad+", skupina="+idSkupTab);
        }
        else {
          CSVExportVazby csv = new CSVExportVazby(this, idDoklad, idSkupTab);
          try {
            csv.createCsv();
            csv.csvOutput();
          }
          catch (Throwable ex) {
            logger.error("vazbyAuto - selhalo generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+idSkupTab);
            ex.printStackTrace(); //pro zacatek
          }
          logger.info("vazbyAuto - dokonceno generovani CSV vazby pro idDoklad="+idDoklad+", skupina="+idSkupTab);
        }
        */
      }
    }
    vo.closeRowSet();
    getDBTransaction().commit();

    deleteTmpDokladVazby();

    loggerAuto.info("Konec generovani vzajemnych vazeb.");
  }

  public void vazbyPregenerovatProblemove(int idSkup, java.sql.Date datum) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_servis.p_vazbyPregenerovatProblemove(?,?); end;",0);
      st.setInt(1, idSkup);
      st.setDate(2, datum);
      st.execute();
      logger.info("vazbyPregenerovatProblemove "+idSkup+","+datum+" - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_servis.p_vazbyPregenerovatProblemove",s);
      throw new KisException("Selhalo volání procedury kap_servis.p_vazbyPregenerovatProblemove",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }
  
  private void deleteTmpDokladVazby() throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
      
    try {
      st = dbTran.createCallableStatement("DELETE FROM DB_JT.KP_TMP_DOKLADVAZBY",0);
      st.execute();
      dbTran.commit();
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo mazani DB_JT.KP_TMP_DOKLADVAZBY",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void startGenerovaniAuto() throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
      
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_startGenerovaniAuto(); end;",0);
      st.execute();
      logger.info("startGenerovaniAuto - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_startGenerovaniAuto",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_startGenerovaniAuto",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public boolean topExportNove() throws KisException {
    //esc 1208-2 03.12.08  -  vypnute aktivity pre OLAP ------
	boolean ok = false;
	return ok;
	//------------------- END --------------------------
/*	DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    boolean ok = false;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_topExport.p_exportNove; end;",0);
      st.execute();
      ok = true;
      logger.info("top export novych dokladu - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_topExport.p_exportNove",s);
   
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { // ignore 
	  }
    }  
	return ok; 
*/
  }

  public void konecGenerovaniAuto() throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
      
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_konecGenerovaniAuto(); end;",0);
      st.execute();
      logger.info("konecGenerovaniAuto - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_konecGenerovaniAuto",s);
      //throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_konecGenerovaniAuto",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }

    //esc 1208-2 zrusene aktivity pre OLAP	
	tenOk = topExportNove();  // vrati vzdy FALSE  - vypnuty OLAP 3.12.08
	
  }

  private void denniSpousteni() throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
      
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_guiv.p_denniSpousteni(); end;",0);
      st.execute();
      logger.info("denniSpousteni - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_guiv.p_denniSpousteni",s);
      //throw new KisException("Selhalo volání procedury db_jt.kap_guiv.p_denniSpousteni",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void refreshBudget() throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
      
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_guiv.p_refreshBudget(); end;",0);
      st.execute();
      logger.info("p_refreshBudget - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_guiv.p_refreshBudget",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_guiv.p_refreshBudget",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  private Number[] getUcetniSkupiny() 
  {
    List list = new ArrayList();
  
    ViewObject vo = getKpParametryView1();
    vo.clearCache();
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      Number idSkupiny = null;
      idSkupiny = (Number) row.getAttribute("IdKtgucetniskupina");
      if(idSkupiny != null) { list.add(idSkupiny); }
      idSkupiny = (Number) row.getAttribute("IdKtgucetniskupina2");
      if(idSkupiny != null) { list.add(idSkupiny); }
      idSkupiny = (Number) row.getAttribute("IdKtgucetniskupina3");
      if(idSkupiny != null) { list.add(idSkupiny); }
      idSkupiny = (Number) row.getAttribute("IdKtgucetniskupina4");
      if(idSkupiny != null) { list.add(idSkupiny); }
      idSkupiny = (Number) row.getAttribute("IdKtgucetniskupina6");
      if(idSkupiny != null) { list.add(idSkupiny); }
      idSkupiny = (Number) row.getAttribute("IdKtgucetniskupina7");
      if(idSkupiny != null) { list.add(idSkupiny); }
      idSkupiny = (Number) row.getAttribute("IdKtgucetniskupina8");
      if(idSkupiny != null) { list.add(idSkupiny); }
    }
    vo.closeRowSet();
    getDBTransaction().commit();
    
    return (Number[]) list.toArray(new Number[list.size()]);
  }

  public void deleteRadicImportu() throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    Statement st = null;
    try {
      st = dbTran.createStatement(0);
      st.execute("DELETE FROM db_dsa.kp_radicImportu");
      dbTran.commit();
      logger.info("kp_radicImportu - smazano");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo mazání tabulky db_dsa.kp_radicImportu",s);
      throw new KisException("Selhalo mazání tabulky db_dsa.kp_radicImportu",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void frontaInit() throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_frontaInit; end;",0);
      st.execute();
      logger.info("frontaInit - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_frontaInit",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_frontaInit",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public String majetekUcastKontrola(int idDoklad) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    String message = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_prim.p_dokladMajetekUcastKontrola (?,?); end;",0);
      st.setInt(1, idDoklad);
      st.registerOutParameter(2, Types.VARCHAR);
      st.execute();
      message = st.getString(2);
      logger.info("majetekUcastKontrola - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_prim.p_dokladMajetekUcastKontrola ",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_prim.p_dokladMajetekUcastKontrola ",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }

    return message;
  }

  /**
   * 
   * Container's getter for VwKpDokladView1
   */
  public VwKpDokladViewImpl getVwKpDokladView1()
  {
    return (VwKpDokladViewImpl)findViewObject("VwKpDokladView1");
  }

  /**
   * 
   * Container's getter for ViewJtSpol1
   */
  public ViewJtSpolImpl getViewJtSpol1()
  {
    return (ViewJtSpolImpl)findViewObject("ViewJtSpol1");
  }




  /**
   * 
   * Container's getter for VwKpDokladvzajemvazbyView1
   */
  public VwKpDokladvzajemvazbyViewImpl getVwKpDokladvzajemvazbyView1()
  {
    return (VwKpDokladvzajemvazbyViewImpl)findViewObject("VwKpDokladvzajemvazbyView1");
  }

  /**
   * 
   * Container's getter for ViewExcelDetail1
   */
  public ViewExcelDetailImpl getViewExcelDetail1()
  {
    return (ViewExcelDetailImpl)findViewObject("ViewExcelDetail1");
  }

  /**
   * 
   * Container's getter for ViewExcelDetailCZB1
   */
  public ViewExcelDetailCZBImpl getViewExcelDetailCZB1()
  {
    return (ViewExcelDetailCZBImpl)findViewObject("ViewExcelDetailCZB1");
  }

  /**
   * 
   * Container's getter for KpDatDokladprotokolView1
   */
  public KpDatDokladprotokolViewImpl getKpDatDokladprotokolView1()
  {
    return (KpDatDokladprotokolViewImpl)findViewObject("KpDatDokladprotokolView1");
  }




  /**
   * 
   * Container's getter for KpDatDokladView1
   */
  public KpDatDokladViewImpl getKpDatDokladView1()
  {
    return (KpDatDokladViewImpl)findViewObject("KpDatDokladView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladmanualView1
   */
  public VwKpDokladmanualViewImpl getVwKpDokladmanualView1()
  {
    return (VwKpDokladmanualViewImpl)findViewObject("VwKpDokladmanualView1");
  }




  /**
   * 
   * Container's getter for VwKpDokladzahlaviView1
   */
  public VwKpDokladzahlaviViewImpl getVwKpDokladzahlaviView1()
  {
    return (VwKpDokladzahlaviViewImpl)findViewObject("VwKpDokladzahlaviView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladfrontaView1
   */
  public VwKpDokladfrontaViewImpl getVwKpDokladfrontaView1()
  {
    return (VwKpDokladfrontaViewImpl)findViewObject("VwKpDokladfrontaView1");
  }





  /**
   * 
   * Container's getter for VwKpDokladbilancepocstavView1
   */
  public VwKpDokladbilancepocstavViewImpl getVwKpDokladbilancepocstavView1()
  {
    return (VwKpDokladbilancepocstavViewImpl)findViewObject("VwKpDokladbilancepocstavView1");
  }


//  public void myReset() 
//  {
//    try {
//      getDBTransaction().reconnect();
//    }
//    catch(Exception e) 
//    {
//      e.printStackTrace();
//    }
//  }

  /**
   * 
   * Container's getter for ViewJtSpolUserPrava1
   */
  public ViewJtSpolUserPravaImpl getViewJtSpolUserPrava1()
  {
    return (ViewJtSpolUserPravaImpl)findViewObject("ViewJtSpolUserPrava1");
  }




  /**
   * 
   * Container's getter for KpKtgUcetnispolecnostView1
   */
  public KpKtgUcetnispolecnostViewImpl getKpKtgUcetnispolecnostView1()
  {
    return (KpKtgUcetnispolecnostViewImpl)findViewObject("KpKtgUcetnispolecnostView1");
  }

  /**
   * 
   * Container's getter for VwKpGuidokladvazbyspolecnostView1
   */
  public VwKpGuidokladvazbyspolecnostViewImpl getVwKpGuidokladvazbyspolecnostView1()
  {
    return (VwKpGuidokladvazbyspolecnostViewImpl)findViewObject("VwKpGuidokladvazbyspolecnostView1");
  }

  /**
   * 
   * Container's getter for KpTmpDokladvazbyView1
   */
  public KpTmpDokladvazbyViewImpl getKpTmpDokladvazbyView1()
  {
    return (KpTmpDokladvazbyViewImpl)findViewObject("KpTmpDokladvazbyView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladuverovapoziceView1
   */
  public VwKpDokladuverovapoziceViewImpl getVwKpDokladuverovapoziceView1()
  {
    return (VwKpDokladuverovapoziceViewImpl)findViewObject("VwKpDokladuverovapoziceView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladmenovapoziceView1
   */
  public VwKpDokladmenovapoziceViewImpl getVwKpDokladmenovapoziceView1()
  {
    return (VwKpDokladmenovapoziceViewImpl)findViewObject("VwKpDokladmenovapoziceView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladzustatkyuctuView1
   */
  public VwKpDokladzustatkyuctuViewImpl getVwKpDokladzustatkyuctuView1()
  {
    return (VwKpDokladzustatkyuctuViewImpl)findViewObject("VwKpDokladzustatkyuctuView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladmenovapoziceSpecialView1
   */
  public VwKpDokladmenovapoziceSpecialViewImpl getVwKpDokladmenovapoziceSpecialView1()
  {
    return (VwKpDokladmenovapoziceSpecialViewImpl)findViewObject("VwKpDokladmenovapoziceSpecialView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladuverovapoziceSpecialView1
   */
  public VwKpDokladuverovapoziceSpecialViewImpl getVwKpDokladuverovapoziceSpecialView1()
  {
    return (VwKpDokladuverovapoziceSpecialViewImpl)findViewObject("VwKpDokladuverovapoziceSpecialView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprotistranaView1
   */
  public VwKpDokladprotistranaViewImpl getVwKpDokladprotistranaView1()
  {
    return (VwKpDokladprotistranaViewImpl)findViewObject("VwKpDokladprotistranaView1");
  }

  /**
   * 
   * Container's getter for KpParametryView1
   */
  public KpParametryViewImpl getKpParametryView1()
  {
    return (KpParametryViewImpl)findViewObject("KpParametryView1");
  }

  private boolean isEnabledByDay() 
  {
    Calendar cal = Calendar.getInstance();
    int denTydne = cal.get(Calendar.DAY_OF_WEEK);
    int hodina = cal.get(Calendar.HOUR_OF_DAY);
    if(hodina<8) denTydne--;
    if(denTydne==0) denTydne=7;
    
    if(denTydne==Calendar.SATURDAY || denTydne==Calendar.SUNDAY) return false;
    
    return true;
  }

  private void genMU() throws KisException 
  {
    if(!isEnabledByDay()) 
    {
      loggerAuto.info("Excely Men./Uv. se dnes negeneruji!");
      return;
    }
  
    loggerAuto.info("Start generovani excelu Men./Uv.");
    java.sql.Date sGenDate = Utils.getLastDate();
    ESExportMenove em = null;
    ESExportUverove eu = null;
/*
    em = new ESExportMenove(this,sGenDate,-2);
    try {
      em.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani menove pozice banky",ex);
    }
    loggerAuto.info("Vygenerovan excel menove pozice banky");

    em = new ESExportMenove(this,sGenDate,-1);
    try {
      em.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani menove pozice 10001",ex);
    }
    loggerAuto.info("Vygenerovan excel menove pozice 10001");
*/   
    em = new ESExportMenove(this,sGenDate,1);
    try {
      em.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani menove pozice RKC",ex);
    }
    loggerAuto.info("Vygenerovan excel menove pozice RKC");
/*    
    em = new ESExportMenove(this,sGenDate,4);
    try {
      em.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani menove pozice US 4",ex);
    }
    loggerAuto.info("Vygenerovan excel menove pozice US 4");
    
*/
    eu = new ESExportUverove(this,sGenDate,-2);
    try {
      eu.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani uverove pozice banky",ex);
    }
    loggerAuto.info("Vygenerovan excel uverove pozice banky");
/*
    eu = new ESExportUverove(this,sGenDate,-1);
    try {
      eu.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani uverove pozice 10001",ex);
    }
    loggerAuto.info("Vygenerovan excel uverove pozice 10001");
*/
    eu = new ESExportUverove(this,sGenDate,1);
    try {
      eu.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani uverove pozice RKC",ex);
    }
    loggerAuto.info("Vygenerovan excel uverove pozice RKC");
/*    
    eu = new ESExportUverove(this,sGenDate,4);
    try {
      eu.excelOutput();
    }
    catch (Exception ex) {
      ex.printStackTrace(); //pro zacatek
      loggerAuto.error("Chyba pri generovani uverove pozice US 4",ex);
    }
    loggerAuto.info("Vygenerovan excel uverove pozice US 4");
*/
  }

  /**
   * 
   * Container's getter for VwSubkonDokladpolozkaView1
   */
  public VwSubkonDokladpolozkaViewImpl getVwSubkonDokladpolozkaView1()
  {
    return (VwSubkonDokladpolozkaViewImpl)findViewObject("VwSubkonDokladpolozkaView1");
  }

  /**
   * 
   * Container's getter for KpKtgSubkonsolidaceetapaView1
   */
  public KpKtgSubkonsolidaceetapaViewImpl getKpKtgSubkonsolidaceetapaView1()
  {
    return (KpKtgSubkonsolidaceetapaViewImpl)findViewObject("KpKtgSubkonsolidaceetapaView1");
  }

  /**
   * 
   * Container's getter for VwKpSubkonsolidaceView1
   */
  public VwKpSubkonsolidaceViewImpl getVwKpSubkonsolidaceView1()
  {
    return (VwKpSubkonsolidaceViewImpl)findViewObject("VwKpSubkonsolidaceView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladpolozkadenikView1
   */
  public VwKpDokladpolozkadenikViewImpl getVwKpDokladpolozkadenikView1()
  {
    return (VwKpDokladpolozkadenikViewImpl)findViewObject("VwKpDokladpolozkadenikView1");
  }
  
  public int checkAutoGenFronta(int jenomPs)
  {
    Statement st = null;
    int cnt = -1;
    String user = Constants.AUTOMAT_USER;
    if(jenomPs==2) user = Constants.DENNI_USER;
    if(jenomPs==99) 
    {
      jenomPs=0;
      user = Constants.DENNI_USER;
    }
    try {
      st = getDBTransaction().createStatement(0);
      ResultSet rs = st.executeQuery("SELECT COUNT(*) AS CNT " +
                                     "FROM DB_JT.VW_KP_DOKLADFRONTA " +
                                     "WHERE (S_STAV = 'Èeká' OR S_STAV = 'Generuje se') " +
                                       "AND AUZIVATEL = '" + user + "' " +
                                       "AND AJENOMPS = "+jenomPs);
      if(rs.next()) 
      {
        cnt = rs.getInt("CNT");
      }
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
    
    return cnt;
  }

  private void checkFrontaKonecGenerovani(String user)
  {
    Statement st = null;
    int cnt = -1;
    try {
      st = getDBTransaction().createStatement(0);
      ResultSet rs = st.executeQuery("SELECT COUNT(*) AS CNT " +
                                     "FROM DB_JT.VW_KP_DOKLADFRONTA " +
                                     "WHERE UPPER(S_STAV) = 'DB CHYBA' " +
                                       "AND AUZIVATEL = '" + user + "'");
      if(rs.next()) 
      {
        cnt = rs.getInt("CNT");
      }
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
    
    if(cnt>0) {
      Mail mail = new Mail();
      mail.sendMail("KIS",
                    Constants.KIS_ADMINS,
                    "KIS - problém pøi automat. generování",
                    "Pøi automatickém generování nastal v KISu problém. Ve frontì zbylo "+cnt+" žádostí o generování ve stavu 'DB chyba' od uživatele "+user+"!",
                    new String[] {});
    }
  }

  /**
   * 
   * Container's getter for KpDatDokladvazbyView1
   */
  public KpDatDokladvazbyViewImpl getKpDatDokladvazbyView1()
  {
    return (KpDatDokladvazbyViewImpl)findViewObject("KpDatDokladvazbyView1");
  }
  
/*  
  public void setZamek(String akce, String idOO) throws KisException
  {
    String[] tokens = idOO.split(":");
    String krok = tokens[0];
    String idDoklad = tokens[1];
    String typeUser = tokens[2];
    
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_jt.kap_gui.p_dokladZamek(?,?,?,?,?); end;",0);
      st.setString(1,akce);
      st.setInt(2,Integer.parseInt(idDoklad));
      st.setInt(3,Integer.parseInt(krok));
      st.setInt(4,Integer.parseInt(typeUser));
      st.setString(5,getUser());
      st.execute();
      logger.info("setZamek - dokonceno");
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_dokladZamek",e);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_dokladZamek",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
    //getTransaction().commit();
  }

  private java.sql.Date getZamceno(int idDoklad, int krok, int typeUser)
  {
    java.sql.Timestamp ts = null;
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin ? := db_jt.f_testDokladIdZamceno(?,?,?); end;",0);
      st.registerOutParameter(1, Types.TIMESTAMP);
      st.setInt(2,idDoklad);
      st.setInt(3,krok);
      st.setInt(4,typeUser);
      st.execute();
      ts = st.getTimestamp(1);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
    if(ts == null) return null;
    
    return new java.sql.Date(ts.getTime());
  }
  
  public String getZamekCheckbox(int idDoklad, int idSpol, int krok, int typeUser)
  {
    String checked = "", timeStamp = null;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss, dd.MM.yyyy");
    
    java.sql.Date tsDate = getZamceno(idDoklad, krok, typeUser);
    if(tsDate != null) {
      checked = "checked";
      timeStamp = sdf.format(tsDate);
    }
    
    String name = krok+":"+idDoklad+":"+typeUser;
    StringBuffer buf = new StringBuffer();
    buf.append("<input type='checkbox' ");
    if(isCurrentUserType(idSpol,typeUser)) 
    {
      buf.append("name='"+name+"' id='"+name+"' ");
      buf.append("onClick='javascript:switchOO(this);' ");
    }
    else 
    {
      buf.append("disabled ");
    }
    buf.append(checked+" ");
    buf.append("title='"+(timeStamp==null?"Ke schválení "+Constants.kym[typeUser]:"Schváleno "+Constants.kym[typeUser]+" v "+timeStamp)+"' ");
    buf.append(">");
    
    return buf.toString();
  }
*/

  public boolean isCurrentUserType(int idSpol, String sloupec) 
  {
    boolean ret = false;
    int userId = currentUsersId();
    if(userId==81) return true;

    String where = null;
    where = sloupec+" = "+userId;
    
    ViewObject vo = getKpKtgUcetnispolecnostView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idSpol+
                      " AND "+where);
    if(vo.hasNext()) 
    {
      ret = true;
    }
    vo.closeRowSet();
    
    return ret;
  }

  public boolean isCurrentUserType(int idSpol, int typeUser) 
  {
    boolean ret = false;
    int userId = currentUsersId();
    if(userId==81) return true;

    String where = null;
    if(typeUser==Constants.UCETNI) where="ID_ZODPOVEDNAUCETNI = "+userId;
    else if(typeUser==Constants.SPRAVCE) where="ID_SPRAVCEKONSOLIDACE = "+userId;
    else if(typeUser==Constants.OO) where="ID_ODPOVEDNAOSOBA = "+userId;
    else if(typeUser==Constants.TOP) where="ID_TOPMNG = "+userId;
    else if(typeUser==Constants.SEF_SEGMENTU) where="ID_MNGSEGMENTBOSS = "+userId;
    else if(typeUser==Constants.NADRIZENA_UCETNI) where="ID_ZODPOVEDNAUCETNI in (select id from db_jt.ktg_appUser au where au.ID_NADRIZENAUCETNI = "+userId+")";
    
    ViewObject vo = getKpKtgUcetnispolecnostView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idSpol+
                      " AND "+where);
    if(vo.hasNext()) 
    {
      ret = true;
    }
    vo.closeRowSet();
    
    return ret;
  }
  
  public boolean isCurrentUserType(int typeUser) 
  {
    boolean ret = false;
    int userId = currentUsersId();
    if(userId==81) return true;
    if(typeUser==-666) return false;

    String where = null;
    if(typeUser==Constants.UCETNI) where="ID_ZODPOVEDNAUCETNI = "+userId;
    else if(typeUser==Constants.OO) where="ID_ODPOVEDNAOSOBA = "+userId;
    else if(typeUser==Constants.TOP) where="ID_TOPMNG = "+userId;
    else if(typeUser==Constants.SEF_SEGMENTU) where="ID_MNGSEGMENTBOSS = "+userId;
    else if(typeUser==Constants.NADRIZENA_UCETNI) where="ID_ZODPOVEDNAUCETNI in (select id from db_jt.ktg_appUser au where au.ID_NADRIZENAUCETNI = "+userId+")";
    
    ViewObject vo = getKpKtgUcetnispolecnostView1();
    vo.clearCache();
    vo.setWhereClause(where);
    if(vo.hasNext()) 
    {
      ret = true;
    }
    vo.closeRowSet();
    
    return ret;
  }

  private String getCurrentUserName() 
  {
    String name = "";
    
    String user = cz.jtbank.konsolidace.common.Utils.getUserName(this, true);
    
    ViewObject voUser = getKtgAppuserView1();
    voUser.clearCache();
    voUser.setWhereClause("S_USERID = '"+user+"'");
    if(voUser.hasNext()) 
    {
      Row rowUser = voUser.next();
      
      String titulyPred = (String) rowUser.getAttribute("STitulypred");
      String krestni = (String) rowUser.getAttribute("SKrestni");
      String prijmeni = (String) rowUser.getAttribute("SPrijmeni");
      String titulyPo = (String) rowUser.getAttribute("STitulypo");
      name += titulyPred==null ? "" : titulyPred;
      name += krestni==null ? "" : " "+krestni;
      name += prijmeni==null ? "" : " "+prijmeni;
      name += titulyPo==null ? "" : " "+titulyPo;
    }
    voUser.closeRowSet();
    return name;
  }
  
/*
 * -------------------------------------------------------------------------------------------------------------------------
 * CHECKBOXY
 * */
  private static SimpleDateFormat sdfRev = new SimpleDateFormat("yyyy-MM-dd");

  private java.sql.Date getDate(String dtDatum) 
  {
    java.util.Date uDatum = null;
    java.sql.Date sDatum = null;
    try 
    {
      uDatum = sdfRev.parse(dtDatum);
    }
    catch(java.text.ParseException pe) {}
    if(uDatum != null) 
    {
      sDatum = new java.sql.Date(uDatum.getTime());
    }
    return sDatum;
  }

  /**/
  public void setZamekNew(String akce, String idOO) throws KisException
  {
    String[] tokens = idOO.split(":");
    String idSpol = tokens[0];
    String dtDatum = tokens[1];
    java.sql.Date datum = getDate(dtDatum);
    String idZamek = tokens[2];
    String krok = tokens[3];
    System.out.println("idOO="+idOO+", idSpol="+idSpol+", datum="+datum+", idZamek="+idZamek+", krok="+krok);
    
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_jt.kap_guiv.p_generovaniZamek(?,?,?,?,?,?,?); end;",0);
      st.setString(1,akce);
      st.setInt(2,1);
      st.setInt(3,Integer.parseInt(idSpol));
      st.setDate(4,datum);
      st.setString(5,getUser());
      st.setInt(6,Integer.parseInt(idZamek));
      st.setInt(7,Integer.parseInt(krok));
      st.execute();
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      throw new KisException("Selhalo volání procedury db_jt.kap_guiv.p_generovaniZamek",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
  }

  public void setZamekZamitnuti(String akce, String idOO, String duvod) throws KisException
  {
    String[] tokens = idOO.split(":");
    String idSpol = tokens[1];
    String dtDatum = tokens[2];
    java.sql.Date datum = getDate(dtDatum);
    String idZamek = tokens[3];
    String krok = tokens[4];
    
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_jt.kap_guiv.p_genZamekZamitnuti(?,?,?,?,?,?,?,?); end;",0);
      st.setString(1,akce);
      st.setInt(2,1);
      st.setInt(3,Integer.parseInt(idSpol));
      st.setDate(4,datum);
      st.setString(5,getUser());
      st.setInt(6,Integer.parseInt(idZamek));
      st.setString(7,duvod);
      st.setInt(8,Integer.parseInt(krok));
      st.execute();
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      throw new KisException("Selhalo volání procedury db_jt.kap_guiv.p_genZamekZamitnuti",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
    
    if("I".equalsIgnoreCase(akce)) 
    {
      Number ucetni = null,
             ucetniBoss = null,
             oo = null,
             ss = null,
             admin = null;
      ViewObject voSpol = getKpKtgUcetnispolecnostView1();
      voSpol.clearCache();
      voSpol.setWhereClause("ID = "+idSpol);
      while(voSpol.hasNext()) 
      {
        Row row = voSpol.next();
        ucetni = (Number) row.getAttribute("IdZodpovednaucetni");
        oo = (Number) row.getAttribute("IdOdpovednaosoba");
        ss = (Number) row.getAttribute("IdMngsegmentboss");
        admin = (Number) row.getAttribute("IdAdministrator");
      }
      voSpol.closeRowSet();

      if(ucetni!=null) {
        ViewObject voUser = getKtgAppuserView1();
        voUser.clearCache();
        voUser.setWhereClause("ID = "+ucetni);
        while(voUser.hasNext()) 
        {
          Row row = voUser.next();
          ucetniBoss = (Number) row.getAttribute("IdNadrizenaucetni");
        }
        voSpol.closeRowSet();
      }
      
      Mail mail = new Mail();
      mail.sendDokladZamitnuti(this, idZamek, idSpol, dtDatum, duvod, ucetni, ucetniBoss, oo, ss, admin);
    }
  }
/*
  private CallableStatement stZamceno;
  private CallableStatement stZamcenoZamit;
  private CallableStatement stMoznoZamceno;
  private CallableStatement stCountDoklad;
  
  private void createSQLStatement ( DBTransaction db ) {
    stZamceno = db.createCallableStatement("begin ? := db_jt.kap_guiv.f_getGenerovaniZamek(?,?,?,?); end;",0);
    stMoznoZamceno = db.createCallableStatement("begin ? := db_jt.f_getSpolecnostDokladyKodStav(?,?); end;",0);
    stCountDoklad = db.createCallableStatement("select count(*) from db_jt.kp_dat_doklad where id = ?",0);
  }
*/
  public boolean existsDoklad ( int idDoklad ) 
  {
    int ret=0;

    CallableStatement stCountDoklad = null;
    try {
      //if ( stCountDoklad == null )
      //  createSQLStatement ( getDBTransaction() );
      stCountDoklad = getDBTransaction().createCallableStatement("select count(*) from db_jt.kp_dat_doklad where id = ?",0);

      stCountDoklad.setInt(1,idDoklad);
      stCountDoklad.execute();
      stCountDoklad.getResultSet().next();
      ret = stCountDoklad.getResultSet().getInt(1);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(stCountDoklad != null) stCountDoklad.close();
      }
      catch(Exception e) {}
    }
    return ( ret == 1 );
  }
    

  private String getZamceno(int idSpol, java.sql.Date datum, int idZam, int krok) 
  {
    String str = null;

    CallableStatement stZamceno = null;
    try {
//      if ( stZamceno == null )
//        createSQLStatement ( getDBTransaction() );
      stZamceno = getDBTransaction().createCallableStatement("begin ? := db_jt.kap_guiv.f_getGenerovaniZamek(?,?,?,?,?); end;",0);
        
      stZamceno.registerOutParameter(1, Types.VARCHAR);
      stZamceno.setInt(2,1);
      stZamceno.setInt(3,idSpol);
      stZamceno.setDate(4,datum);
      stZamceno.setInt(5,idZam);
      stZamceno.setInt(6,krok);
      stZamceno.execute();
      str = stZamceno.getString(1);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(stZamceno != null) stZamceno.close();
      }
      catch(Exception e) {}
    }
    return str;
  }

  private String getZamcenoZamitnuti(int idSpol, java.sql.Date datum, int idZam, int krok) 
  {
    String str = null;

    CallableStatement stZamcenoZamit = null;
    try {
      stZamcenoZamit = getDBTransaction().createCallableStatement("begin ? := db_jt.kap_guiv.f_getGenZamekZamitnuti(?,?,?,?,?); end;",0);
        
      stZamcenoZamit.registerOutParameter(1, Types.VARCHAR);
      stZamcenoZamit.setInt(2,1);
      stZamcenoZamit.setInt(3,idSpol);
      stZamcenoZamit.setDate(4,datum);
      stZamcenoZamit.setInt(5,idZam);
      stZamcenoZamit.setInt(6,krok);
      stZamcenoZamit.execute();
      str = stZamcenoZamit.getString(1);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(stZamcenoZamit != null) stZamcenoZamit.close();
      }
      catch(Exception e) {}
    }
    return str;
  }
/*
  private int getMoznoZamceno(int idSpol, java.sql.Date datum) 
  {
    int ret = -1;

    CallableStatement stMoznoZamceno = null;
    try {
//      if ( stMoznoZamceno == null )
//        createSQLStatement ( getDBTransaction() );
      stMoznoZamceno = getDBTransaction().createCallableStatement("begin ? := db_jt.f_getSpolecnostDokladyKodStav(?,?); end;",0);

      stMoznoZamceno.registerOutParameter(1, Types.INTEGER);
      stMoznoZamceno.setInt(2,idSpol);
      stMoznoZamceno.setDate(3,datum);
      stMoznoZamceno.execute();
      ret = stMoznoZamceno.getInt(1);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(stMoznoZamceno != null) stMoznoZamceno.close();
      }
      catch(Exception e) {}
    }
    return ret;
  }
*/
  public int getTypZamku(int idSpol, java.sql.Date datum) 
  {
    int ret = -1;

    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin ? := db_jt.kap_guiV.f_getZamekTyp(?,?); end;",0);

      st.registerOutParameter(1, Types.INTEGER);
      st.setInt(2,idSpol);
      st.setDate(3,datum);
      st.execute();
      ret = st.getInt(1);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      //logovat
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
    return ret;
  }
/*
  public void closeSQLStatements() 
  {
    try 
    {
      if(stZamceno != null) stZamceno.close();
    }
    catch(Exception e) {}
    try 
    {
      if(stMoznoZamceno != null) stMoznoZamceno.close();
    }
    catch(Exception e) {}
    try 
    {
      if(stCountDoklad != null) stCountDoklad.close();
    }
    catch(Exception e) {}
  }
  
  protected void finalize() 
  {
    closeSQLStatements();
  }
*/

  public String getZamekCheckboxy(int idSpol, String dtDatum)
  {
    java.sql.Date datum = getDate(dtDatum);
    return getZamekCheckboxy(idSpol, datum, 0);
  }
  
  public String getZamekCheckboxy(int idSpol, java.sql.Date datum, int krok) 
  {
    String dtDatum = sdfRev.format(datum);
    StringBuffer buf = new StringBuffer();
    boolean zamitnuto = false;

    int typZamku = getTypZamku(idSpol, datum);

    ViewObject vo = getKpCisZamekView1();
    vo.clearCache();
    vo.setWhereClause("ID_TYPZAMKU = "+typZamku);
    vo.setOrderByClause("ID");
    if(!vo.hasNext()) return "";

    int MAX = vo.getRowCount();
    int[] kymId = new int[MAX];
    String[] kym = new String[MAX];
    String[] texty = new String[MAX];
    String[] textyZamit = new String[MAX];
    String[] sloupec = new String[MAX];

    int index = 0;
    while(vo.hasNext()) {
    
      Row row = vo.next();
      int idZam = ((Number) row.getAttribute("Id")).intValue();
      String popisKym = (String) row.getAttribute("SPopis");
      String sloup = (String) row.getAttribute("SSloupec");
      kymId[index] = idZam;
      texty[index] = getZamceno(idSpol, datum, idZam, krok);
      textyZamit[index] = getZamcenoZamitnuti(idSpol, datum, idZam, krok);
      kym[index] = popisKym;
      sloupec[index] = sloup;
      if(textyZamit[index]!=null && textyZamit[index].length()>0) zamitnuto = true;
      index++;
    }
    vo.closeRowSet();

    if(!zamitnuto) {    
      for(int i=0; i<MAX; i++) {
        boolean chCur = (texty[i]!=null && texty[i].length()>0);
        boolean chNext = i+1<MAX ? (texty[i+1]!=null && texty[i+1].length()>0) : false;
        String checked = chCur ? " checked" : "";
        String name = idSpol+":"+dtDatum+":"+kymId[i]+":"+krok;
        buf.append("\n<input type='checkbox' style='background-color: green' ");
        if(chNext) 
        {
          buf.append("disabled ");
        }
        else if(isCurrentUserType(idSpol,sloupec[i])) 
        {
          buf.append("name='"+name+"' id='"+name+"' ");
          buf.append("onClick='javascript:switchOO(this);' ");
        }
        else 
        {
          buf.append("disabled ");
        }
        buf.append(checked+" ");
        buf.append("title='"+(chCur?"Schváleno "+texty[i]:"Ke schválení "+kym[i])+"'");
        buf.append(">");
        
        //ZAMITNUTI
        if(!chCur && kymId[i]%100!=0) {
          buf.append("\n<input type='checkbox' style='background-color: red' ");
          if(isCurrentUserType(idSpol,sloupec[i])) 
          {
            buf.append("name='Z:"+name+"' id='Z:"+name+"' ");
            buf.append("onClick='javascript:zamitOO(this);' ");
          }
          else 
          {
            buf.append("disabled ");
          }
          buf.append("title='"+"Zamítnout "+kym[i]+"'");
          buf.append(">");
        }
        buf.append("<br>");
        
        if(!chCur) break;
      }
    }
    else 
    {
      for(int i=0; i<MAX; i++) {
        boolean chCur = (texty[i]!=null && texty[i].length()>0);
        boolean chNext = i+1<MAX ? (texty[i]!=null && texty[i].length()>0) : false;
        String checked = chCur ? " checked" : "";
        String name = idSpol+":"+dtDatum+":"+kymId[i]+":"+krok;
        buf.append("\n<input type='checkbox' style='background-color: green' ");
        if(chNext) 
        {
          buf.append("disabled ");
        }
        else if(isCurrentUserType(idSpol,sloupec[i])) 
        {
          buf.append("name='"+name+"' id='"+name+"' ");
          buf.append("onClick='javascript:switchOO(this);' ");
        }
        else 
        {
          buf.append("disabled ");
        }
        buf.append(checked+" ");
        buf.append("title='"+(chCur?"Schváleno "+texty[i]:"Ke schválení "+kym[i])+"'");
        buf.append(">");

        //ZAMITNUTI
        boolean chCurZamit = (textyZamit[i]!=null && textyZamit[i].length()>0);
        if(chCurZamit && kymId[i]%100!=0) {
          buf.append("\n<input type='checkbox' style='background-color: red' ");
          if(isCurrentUserType(idSpol,sloupec[i])) 
          {
            buf.append("name='Z:"+name+"' id='Z:"+name+"' ");
            buf.append("onClick='javascript:zamitOO(this);' ");
          }
          else 
          {
            buf.append("disabled ");
          }
          buf.append("checked ");
          buf.append("title='Zamítnuto "+textyZamit[i]+"'");
          buf.append(">");
        }
        buf.append("<br>");

        if(chCurZamit) break;
      }
    }
    
    buf.append("<a href='DokladyZamekHistorie.jsp?idSpol="+idSpol+"&datum="+dtDatum+"&krok="+krok+"' title='Historie schvalování a zamítání'>H</a>&nbsp;");
    
    return buf.toString();
  }


/*
  public String getZamekCheckboxy(int idSpol, String dtDatum)
  {
    java.sql.Date datum = getDate(dtDatum);
    
    int typZamku = getTypZamku(idSpol, datum);

    StringBuffer buf = new StringBuffer();
    String zam=null, zamNext=null;
    boolean lastCheck = true;

    ViewObject vo = getKpCisZamekView1();
    vo.clearCache();
    vo.setWhereClause("ID_TYPZAMKU = "+typZamku);
    vo.setOrderByClause("ID");
    while(vo.hasNext()) {
      Row row = vo.next();
      int idZamek = ((Number) row.getAttribute("Id")).intValue();
      String popisKym = (String) row.getAttribute("SPopis");
      
      if(zamNext!=null && zamNext.length()>0) zam = zamNext;
      else zam = getZamceno(idSpol, datum, idZamek);
      if(zam!=null && zam.length()>0 && vo.hasNext())
        zamNext = getZamceno(idSpol, datum, idZamek+1);
      else zamNext = null;
      String zamit = getZamcenoZamitnuti(idSpol, datum, idZamek);

      boolean chCur = (zam!=null && zam.length()>0);
      boolean chNext = (zamNext!=null && zamNext.length()>0);
      boolean chCurZamit = (zamit!=null && zamit.length()>0);

      String checked = chCur ? "checked" : " ";
      String name = idSpol+":"+dtDatum+":"+idZamek;
      
      if(lastCheck || chCur) {
        buf.append("\n<input type='checkbox' style='background-color: green' ");
        if(false)//getMoznoZamceno(idSpol, datum) != 0) //TOTO MUSI BYT ODKOMENTOVANO, AZ SE ZACNE POUZIVAT KONTROLA!!!
          buf.append("disabled ");
        else if(chNext) 
          buf.append("disabled ");
        else if(isCurrentUserType(idSpol,idZamek)) //TO BE ADAPTED
        {
          buf.append("name='"+name+"' id='"+name+"' ");
          buf.append("onClick='javascript:switchOO(this);' ");
        }
        else 
          buf.append("disabled ");
        buf.append(checked+" ");
        buf.append("title='"+(chCur?"Schváleno "+zam:"Ke schválení "+popisKym)+"'");
        buf.append(">");
      }
      
      //ZAMITNUTI
      if(!chCur || chCurZamit) {
        buf.append("\n<input type='checkbox' style='background-color: red' ");
        if(isCurrentUserType(idSpol,idZamek)) 
        {
          buf.append("name='Z:"+name+"' id='Z:"+name+"' ");
          buf.append("onClick='javascript:zamitOO(this);' ");
        }
        else 
        {
          buf.append("disabled ");
        }
        
        if(chCurZamit) {
          buf.append("checked ");
          buf.append("title='Zamítnuto "+zamit+"'");
        }
        else buf.append("title='"+"Zamítnout "+popisKym+"'");
        buf.append(">");
      }
      buf.append("<br>");
      
      if(lastCheck) lastCheck = chCur;

      if(chCurZamit) break;
    }
    vo.closeRowSet();

    buf.append("\n<a href='DokladyZamekHistorie.jsp?idSpol="+idSpol+"&datum="+dtDatum+"' title='Historie schvalování a zamítání'>H</a>&nbsp;");
    
    return buf.toString();
  }
*/
  /**
   * 
   * Container's getter for KtgAppuserView1
   */
  public KtgAppuserViewImpl getKtgAppuserView1()
  {
    return (KtgAppuserViewImpl)findViewObject("KtgAppuserView1");
  }

  /**
   * 
   * Container's getter for KpDatSchvalenoooView1
   */
  public KpDatSchvalenoooViewImpl getKpDatSchvalenoooView1()
  {
    return (KpDatSchvalenoooViewImpl)findViewObject("KpDatSchvalenoooView1");
  }

  /**
   * 
   * Container's getter for KpKtgEmailzpravyView1
   */
  public KpKtgEmailzpravyViewImpl getKpKtgEmailzpravyView1()
  {
    return (KpKtgEmailzpravyViewImpl)findViewObject("KpKtgEmailzpravyView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladzdrojdatagregaceView1
   */
  public KpDatDokladzdrojdatagregaceViewImpl getKpDatDokladzdrojdatagregaceView1()
  {
    return (KpDatDokladzdrojdatagregaceViewImpl)findViewObject("KpDatDokladzdrojdatagregaceView1");
  }

  /**
   * 
   * Container's getter for KpCisMenaView1
   */
  public KpCisMenaViewImpl getKpCisMenaView1()
  {
    return (KpCisMenaViewImpl)findViewObject("KpCisMenaView1");
  }

  /**
   * 
   * Container's getter for VwDatDokladzdrojdatagregaceView1
   */
  public VwDatDokladzdrojdatagregaceViewImpl getVwDatDokladzdrojdatagregaceView1()
  {
    return (VwDatDokladzdrojdatagregaceViewImpl)findViewObject("VwDatDokladzdrojdatagregaceView1");
  }

  /**
   * 
   * Container's getter for VwProtistranaView1
   */
  public VwProtistranaViewImpl getVwProtistranaView1()
  {
    return (VwProtistranaViewImpl)findViewObject("VwProtistranaView1");
  }

  /**
   * 
   * Container's getter for KpDatAuditView1
   */
  public KpDatAuditViewImpl getKpDatAuditView1()
  {
    return (KpDatAuditViewImpl)findViewObject("KpDatAuditView1");
  }

  /**
   * 
   * Container's getter for KpTmpGuiView1
   */
  public KpTmpGuiViewImpl getKpTmpGuiView1()
  {
    return (KpTmpGuiViewImpl)findViewObject("KpTmpGuiView1");
  }



    /**
     * 
     * Container's getter for VwKtgOdborView1
     */
  public VwKtgOdborViewImpl getVwKtgOdborView1()
  {
    return (VwKtgOdborViewImpl)findViewObject("VwKtgOdborView1");
  }

  /**
   * 
   * Container's getter for KpEmailParamView1
   */
  public KpEmailParamViewImpl getKpEmailParamView1()
  {
    return (KpEmailParamViewImpl)findViewObject("KpEmailParamView1");
  }

  public String getVazbyAccept(int idDoklad,
                             int idSkup,
                             int idSpol,
                             String pohled) throws KisException
  {
    String ret = null;
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin ? := db_jt.kap_gui.f_getVazbyAkceptaceInfo (?,?,?,?); end;",0);
      st.registerOutParameter(1, Types.VARCHAR);
      st.setInt(2, idDoklad);
      st.setInt(3, idSkup);
      st.setInt(4, idSpol);
      st.setString(5, pohled);
      st.execute();
      ret = st.getString(1);
      //logger.info("getVazbyAccept - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání funkce db_jt.kap_gui.f_getVazbyAkceptaceInfo",s);
      throw new KisException("Selhalo volání funkce db_jt.kap_gui.f_getVazbyAkceptaceInfo",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
    return ret;
  }

  public void changeVazbyAccept(String stav,
                                int idDoklad,
                                int idSkup,
                                int idSpol) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_gui.p_changeVazbyAkceptace(?,?,?,?,?); end;",0);
      st.setString(1, stav);
      st.setInt(2, idDoklad);
      st.setInt(3, idSkup);
      st.setInt(4, idSpol);
      st.setString(5, getUser());
      st.execute();
      logger.info("changeVazbyAccept - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_changeVazbyAkceptace",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_changeVazbyAkceptace",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  /**
   * 
   * Container's getter for CisLocaleView1
   */
  public CisLocaleViewImpl getCisLocaleView1()
  {
    return (CisLocaleViewImpl)findViewObject("CisLocaleView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladvvexcelView1
   */
  public VwKpDokladvvexcelViewImpl getVwKpDokladvvexcelView1()
  {
    return (VwKpDokladvvexcelViewImpl)findViewObject("VwKpDokladvvexcelView1");
  }
  
  private static Map mapDoklady = new HashMap();
  
  public void setSetDoklady(int idKategorie) 
  {
    String idD = getUser() + idKategorie;
    Set setDoklady = null;
    if(mapDoklady.containsKey(idD)) {
      setDoklady = (Set)mapDoklady.get(idD);
      setDoklady.clear();
    }
    else 
    {
      setDoklady = new HashSet();
    }
    ViewObject vo = getKpDatDokladView1();
    vo.clearCache();
    vo.setWhereClause("KpDatDoklad.NL_KROK IN (1,2) AND EXISTS (SELECT NULL FROM DB_JT.KP_KTG_UCETNISPOLECNOST S WHERE S.ID = KpDatDoklad.ID_KTGUCETNISPOLECNOST"+
                      (idKategorie>0?" AND S.ID_KATEGORIE = "+idKategorie:"")+")");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String idDoklad = ((Number)row.getAttribute("Id")).toString();
      setDoklady.add(idDoklad);
    }
    vo.closeRowSet();
    
    mapDoklady.put(idD, setDoklady);
  }

  public void setSetDokladySpolecnost(int idSpol) 
  {
    String idKategorie = null;
    ViewObject voSpol = getKpKtgUcetnispolecnostView1();
    voSpol.clearCache();
    voSpol.setWhereClause("ID = "+idSpol);
    while(voSpol.hasNext()) 
    {
      Row rowSpol = voSpol.next();
      idKategorie = ((Number)rowSpol.getAttribute("IdKategorie")).toString();
    }
    voSpol.closeRowSet();
  
    String idD = getUser() + idKategorie;
    Set setDoklady = null;
    if(mapDoklady.containsKey(idD)) {
      setDoklady = (Set)mapDoklady.get(idD);
    }
    else 
    {
      setDoklady = new HashSet();
    }
    ViewObject vo = getKpDatDokladView1();
    vo.clearCache();
    vo.setWhereClause("KpDatDoklad.NL_KROK IN (1,2) AND KpDatDoklad.ID_KTGUCETNISPOLECNOST = "+idSpol);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String idDoklad = ((Number)row.getAttribute("Id")).toString();
      setDoklady.add(idDoklad);
    }
    vo.closeRowSet();
    
    mapDoklady.put(idD, setDoklady);
  }
  
  private void addSetDoklady(int idDoklad) 
  {
    if(mapDoklady.keySet()==null || idDoklad<1) return;
    String doklad = String.valueOf(idDoklad);
    Iterator iter = mapDoklady.keySet().iterator();
    while(iter.hasNext()) 
    {
      String key = (String)iter.next();
      if(key.startsWith(getUser())) {
        Set setDoklady = (Set)mapDoklady.get(key);
        if(setDoklady!=null) setDoklady.add(doklad);
      }
    }
  }

  public Set getSetDoklady(Number idKategorie) 
  {
    String idD = getUser() + idKategorie;
    return (Set)mapDoklady.get(idD);
  }

  public void deleteDoklad(int idDoklad) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    try {
      st = dbTran.createCallableStatement("delete db_jt.kp_dat_generovaniZamek z "+
                                          "where z.ID_CISDOKLAD = (select kd.ID_CISDOKLAD from db_jt.kp_dat_doklad d, db_jt.kp_ktg_doklad kd where d.id = ? and d.ID_KTGDOKLAD = kd.ID) "+
                                            "and z.ID_KTGUCETNISPOLECNOST = (select d.ID_KTGUCETNISPOLECNOST from db_jt.kp_dat_doklad d where d.id = ?) "+
                                            "and (z.NL_KROK = (select d.NL_KROK from db_jt.kp_dat_doklad d where d.id = ?) or z.NL_KROK is null) "+
                                            "and z.DT_DATUM = (select d.DT_DATUM from db_jt.kp_dat_doklad d where d.id = ?) "+
                                            "and exists (select null from db_jt.kp_dat_doklad where id=? and id_ktgUcetniSpolecnost=id_subkonsolidace and nl_krok in (1,2))",0);
      st.setInt(1, idDoklad);
      st.setInt(2, idDoklad);
      st.setInt(3, idDoklad);
      st.setInt(4, idDoklad);
      st.setInt(5, idDoklad);
      st.execute();
      dbTran.commit();
      logger.info("deleteDoklad zamky - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo mazání db_jt.kp_dat_generovaniZamek",s);
      throw new KisException("Selhalo mazání db_jt.kp_dat_generovaniZamek",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }

    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_doklad.p_delete(?); end;",0);
      st.setInt(1, idDoklad);
      st.execute();
      logger.info("deleteDoklad - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_doklad.p_delete",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_doklad.p_delete",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void deleteDoklad(Set set) throws KisException
  {
    if(set == null || set.isEmpty()) return;
    
    Iterator iter = set.iterator();
    while(iter.hasNext()) 
    {
      String doklad = (String) iter.next();
      if(doklad != null) 
      {
        int idDoklad = Integer.parseInt(doklad);
        if(existsDoklad(idDoklad)) {
          deleteDoklad(idDoklad);
        }
      }
    }
  }

  public void deleteSubkonsDoklad(int idSub, java.sql.Date datum) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;

    try {
      st = dbTran.createCallableStatement("delete db_jt.kp_dat_generovaniZamek z "+
                                          "where z.ID_KTGUCETNISPOLECNOST = ? "+
                                            "and z.DT_DATUM = ? ",0);
      st.setInt(1, idSub);
      st.setDate(2, datum);
      st.execute();
      dbTran.commit();
      logger.info("deleteSubkonsDoklad zamky - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo mazání db_jt.kp_dat_generovaniZamek (sub)",s);
      throw new KisException("Selhalo mazání db_jt.kp_dat_generovaniZamek (sub)",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }

    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_subkon.P_DELETESUBKONSOLIDACEDOKLAD(?,?,?); end;",0);
      st.setInt(1, idSub);
      st.setDate(2, datum);
      st.setString(3, getUser());
      st.execute();
      logger.info("deleteSubkonsDoklad - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_subkon.P_DELETESUBKONSOLIDACEDOKLAD",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_subkon.P_DELETESUBKONSOLIDACEDOKLAD",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void exportExcelBD(String idDoklad,
                            String locale,
                            String ucet,
                            boolean bezIC, 
                            boolean jenIfrs) throws KisException 
  {
    String user = cz.jtbank.konsolidace.common.Utils.getUserName(this, true);
    ESExportBilanceDetail em = new ESExportBilanceDetail(this, idDoklad, locale, ucet, user, bezIC, jenIfrs);
    try {
      em.excelOutput();
    }
    catch (IOException ex) {
      ex.printStackTrace(); //pro zacatek
    }
  }
  
  public String getExcelsBD(final String idDoklad) 
  {
    String user = cz.jtbank.konsolidace.common.Utils.getUserName(this, true);

    FileFilter ffBD = new FileFilter() {
      public boolean accept(File pathname) 
      {
        if(pathname.getName().indexOf(idDoklad)>-1) return true;
        return false;
      }
    };
    StringBuffer buf = new StringBuffer();
    String dirName = Constants.XLS_FILES_PATH + Constants.DIR_BILANCE_DETAIL+"\\" + user;
    File dir = new File(dirName);
    File[] arr = dir.listFiles(ffBD);
    if(arr != null) {
      for( int i=0; i<arr.length; i++ ) 
      {
        String name = arr[i].getName();
        buf.append("<a href=\"FileDeleteOwn.jsp?file=data|5C"+Constants.DIR_BILANCE_DETAIL+"|5C" + user + "|5C" + name + "\">DEL</a>&nbsp;");

        buf.append("<a target=\"_blank\" href=\"excelservlet/B"+idDoklad+"@"+name+"?file="+Constants.DIR_BILANCE_DETAIL+"|5C" + user + "|5C" + name + "\">" + name+ "</a>");
        buf.append("&nbsp;" + df.format( new java.util.Date(arr[i].lastModified())) + "<br>"); 
      }
    }
    return buf.toString();
  }

  public void setManZdrojDat(String akce,
                             int id,
                             int idUcSpol,
                             String mena,
                             String ucet,
                             double castkaMena,
                             double castkaLocal,
                             String popis,
                             java.sql.Date datum,
                             int idUcSpolUnif,
                             int idSpol,
                             int idProjekt,
                             int idOdbor,
                             String setNull,
                             String typ,
                             int idProjektOld,
                             int idOdborOld,
                             java.sql.Date datumOd,
                             java.sql.Date datumDo,
                             String uu) throws KisException
  {
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_guiv.p_KpDatManZdrojDat(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;",0);
      st.setString(1, akce);
      st.setInt(2, id);
      st.setInt(3, idUcSpol);
      st.setString(4, mena);
      st.setString(5, ucet==null?null:ucet.trim());
      st.setDouble(6, castkaMena);
      st.setDouble(7, castkaLocal);
      st.setString(8, popis);
      st.setDate(9,datum);
      if(idUcSpolUnif > 0) st.setInt(10, idUcSpolUnif);
      else st.setNull(10, Types.INTEGER);
      if(idSpol > 0) st.setInt(11, idSpol);
      else st.setNull(11, Types.INTEGER);
      if(idProjekt > 0) st.setInt(12, idProjekt);
      else st.setNull(12, Types.INTEGER);
      if(idOdbor > 0) st.setInt(13, idOdbor);
      else st.setNull(13, Types.INTEGER);
      st.setString(14, setNull);
      st.setString(15, typ);
      if(idProjektOld > 0 || idProjektOld < -1) st.setInt(16, idProjektOld);
      else st.setNull(16, Types.INTEGER);
      if(idOdborOld > 0 || idOdborOld < -1) st.setInt(17, idOdborOld);
      else st.setNull(17, Types.INTEGER);
      st.setString(18, userName);
      st.setDate(19,datumOd);
      st.setDate(20,datumDo);
      st.setString(21, uu==null?null:uu.trim());
      st.execute();
      logger.info("setManZdrojDat - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání pr0cedury db_jt.kap_guiv.p_KpDatManZdrojDat",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_guiv.p_KpDatManZdrojDat",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }

  }

  private static boolean semaforSub = true;

  public void createSubkonsDoklady(int idSkupina, 
                                   java.sql.Date datumOd,
                                   java.sql.Date datumDo) throws KisException
  {
    if(!semaforSub) throw new KisException("V tomto okamžiku nelze spustit generování subkonsolidaèních dokladù, protože je již spuštìno (od nìkoho jiného) a mohlo by dojít ke konfliktu... Zkuste to za chvíli znovu.",
                                        new KisException(Constants.ERR_MESSAGE_ONLY));
    String userName = getUser();
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
    try {
      semaforSub = false;
      st = dbTran.createCallableStatement("begin db_jt.kap_subKon.p_createSubKonDoklady(?,?,?,?); end;",0);
      st.setInt(1, idSkupina);
      st.setDate(2, datumOd);
      st.setDate(3, datumDo);
      st.setString(4, userName);
      st.execute();
      logger.info("createSubkonsDoklady pro "+idSkupina+" ("+datumOd+"-"+datumDo+") - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_subkon.p_createSubKonDoklady",s);
      frontaChyba(-1,"Subkon. vazby","DB chyba",s.getMessage());
      throw new KisException("Selhalo volání procedury db_jt.kap_subkon.p_createSubKonDoklady",s);
    }
    finally {
      semaforSub = true;
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  /**
   * 
   * Container's getter for KpDatManualnizdrojdatView1
   */
  public KpDatManualnizdrojdatViewImpl getKpDatManualnizdrojdatView1()
  {
    return (KpDatManualnizdrojdatViewImpl)findViewObject("KpDatManualnizdrojdatView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladvazbyvyparovaneView1
   */
  public VwKpDokladvazbyvyparovaneViewImpl getVwKpDokladvazbyvyparovaneView1()
  {
    return (VwKpDokladvazbyvyparovaneViewImpl)findViewObject("VwKpDokladvazbyvyparovaneView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladvazbynevyparovaneView1
   */
  public VwKpDokladvazbynevyparovaneViewImpl getVwKpDokladvazbynevyparovaneView1()
  {
    return (VwKpDokladvazbynevyparovaneViewImpl)findViewObject("VwKpDokladvazbynevyparovaneView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladdetailProjektView1
   */
  public VwKpDokladdetailProjektViewImpl getVwKpDokladdetailProjektView1()
  {
    return (VwKpDokladdetailProjektViewImpl)findViewObject("VwKpDokladdetailProjektView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladdetailOdborView1
   */
  public VwKpDokladdetailOdborViewImpl getVwKpDokladdetailOdborView1()
  {
    return (VwKpDokladdetailOdborViewImpl)findViewObject("VwKpDokladdetailOdborView1");
  }

  /**
   * 
   * Container's getter for KpDatDoczastupView1
   */
  public KpDatDoczastupViewImpl getKpDatDoczastupView1()
  {
    return (KpDatDoczastupViewImpl)findViewObject("KpDatDoczastupView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladsubkonvazbydenikView1
   */
  public KpDatDokladsubkonvazbydenikViewImpl getKpDatDokladsubkonvazbydenikView1()
  {
    return (KpDatDokladsubkonvazbydenikViewImpl)findViewObject("KpDatDokladsubkonvazbydenikView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladsubkonvazbydenikKurzView1
   */
  public KpDatDokladsubkonvazbydenikKurzViewImpl getKpDatDokladsubkonvazbydenikKurzView1()
  {
    return (KpDatDokladsubkonvazbydenikKurzViewImpl)findViewObject("KpDatDokladsubkonvazbydenikKurzView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladsubkonvazbydenikView2
   */
  public KpDatDokladsubkonvazbydenikViewImpl getKpDatDokladsubkonvazbydenikView2()
  {
    return (KpDatDokladsubkonvazbydenikViewImpl)findViewObject("KpDatDokladsubkonvazbydenikView2");
  }

  /**
   * 
   * Container's getter for VwKpSubkonsolidaceView2
   */
  public VwKpSubkonsolidaceViewImpl getVwKpSubkonsolidaceView2()
  {
    return (VwKpSubkonsolidaceViewImpl)findViewObject("VwKpSubkonsolidaceView2");
  }

  /**
   * 
   * Container's getter for VwDatBudgetView1
   */
  public VwDatBudgetViewImpl getVwDatBudgetView1()
  {
    return (VwDatBudgetViewImpl)findViewObject("VwDatBudgetView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetpolozkaView1
   */
  public VwDatBudgetpolozkaViewImpl getVwDatBudgetpolozkaView1()
  {
    return (VwDatBudgetpolozkaViewImpl)findViewObject("VwDatBudgetpolozkaView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgettransakceView1
   */
  public VwDatBudgettransakceViewImpl getVwDatBudgettransakceView1()
  {
    return (VwDatBudgettransakceViewImpl)findViewObject("VwDatBudgettransakceView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetpolozkadata1View1
   */
  public VwDatBudgetpolozkadata1ViewImpl getVwDatBudgetpolozkadata1View1()
  {
    return (VwDatBudgetpolozkadata1ViewImpl)findViewObject("VwDatBudgetpolozkadata1View1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetpolozkadata2View1
   */
  public VwDatBudgetpolozkadata2ViewImpl getVwDatBudgetpolozkadata2View1()
  {
    return (VwDatBudgetpolozkadata2ViewImpl)findViewObject("VwDatBudgetpolozkadata2View1");
  }

  /**
   * 
   * Container's getter for KpCisTyptransakceView1
   */
  public KpCisTyptransakceViewImpl getKpCisTyptransakceView1()
  {
    return (KpCisTyptransakceViewImpl)findViewObject("KpCisTyptransakceView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladView2
   */
  public KpDatDokladViewImpl getKpDatDokladView2()
  {
    return (KpDatDokladViewImpl)findViewObject("KpDatDokladView2");
  }

  /**
   * 
   * Container's getter for VwSubkonDokladpolozkaView2
   */
  public VwSubkonDokladpolozkaViewImpl getVwSubkonDokladpolozkaView2()
  {
    return (VwSubkonDokladpolozkaViewImpl)findViewObject("VwSubkonDokladpolozkaView2");
  }

  /**
   * 
   * Container's getter for KpKalendarakciView1
   */
  public KpKalendarakciViewImpl getKpKalendarakciView1()
  {
    return (KpKalendarakciViewImpl)findViewObject("KpKalendarakciView1");
  }

  /**
   * 
   * Container's getter for KpRelSubkonsolidaceclenView1
   */
  public KpRelSubkonsolidaceclenViewImpl getKpRelSubkonsolidaceclenView1()
  {
    return (KpRelSubkonsolidaceclenViewImpl)findViewObject("KpRelSubkonsolidaceclenView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladdetailDslrView1
   */
  public VwKpDokladdetailDslrViewImpl getVwKpDokladdetailDslrView1()
  {
    return (VwKpDokladdetailDslrViewImpl)findViewObject("VwKpDokladdetailDslrView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladdetailDslrsumView1
   */
  public VwKpDokladdetailDslrsumViewImpl getVwKpDokladdetailDslrsumView1()
  {
    return (VwKpDokladdetailDslrsumViewImpl)findViewObject("VwKpDokladdetailDslrsumView1");
  }

  /**
   * 
   * Container's getter for KpKtgProjektView1
   */
  public KpKtgProjektViewImpl getKpKtgProjektView1()
  {
    return (KpKtgProjektViewImpl)findViewObject("KpKtgProjektView1");
  }

  /**
   * 
   * Container's getter for VwKpProjektdokladView1
   */
  public VwKpProjektdokladViewImpl getVwKpProjektdokladView1()
  {
    return (VwKpProjektdokladViewImpl)findViewObject("VwKpProjektdokladView1");
  }

  /**
   * 
   * Container's getter for VwKpProjektdokladdetailView1
   */
  public VwKpProjektdokladdetailViewImpl getVwKpProjektdokladdetailView1()
  {
    return (VwKpProjektdokladdetailViewImpl)findViewObject("VwKpProjektdokladdetailView1");
  }

  private void ulozDokladyProjekty()
  {
    DBTransaction dbTran = getDBTransaction();
    CallableStatement st = null;
    //DENNI DOKLADY
    try {
     logger.info("ulozDokladyProjekty - DENNI DOKLADY  - p_createProjektDoklady(sysdate)");
      st = dbTran.createCallableStatement("begin db_jt.kap_projektDoklad.p_createProjektDoklady(sysdate); end;",0);
      st.execute();
      logger.info("p_createProjektDoklady - dokonceno");
    }
    catch (SQLException s) {
      logger.error("Selhalo volání procedury db_jt.kap_projektDoklad.p_createProjektDoklady",s);
      s.printStackTrace(); //pro zacatek
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { }
    }
    //DOKLADY K DATU GENEROVANI
    try {
     logger.info("ulozDokladyProjekty -  K DATU GENEROVANI - p_createProjektDokladyMesic");
      st = dbTran.createCallableStatement("begin db_jt.kap_projektDoklad.p_createProjektDokladyMesic; end;",0);
      st.execute();
      logger.info("p_createProjektDokladyMesic - dokonceno");
    }
    catch (SQLException s) {
      logger.error("Selhalo volání procedury db_jt.kap_projektDoklad.p_createProjektDokladyMesic",s);
      s.printStackTrace(); //pro zacatek
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { }
    }
    
    exportDokladyProjekty(false);
  }

  public void ulozDokladProjekt(int idProjekt, java.sql.Date datum) throws KisException
  {
    int idDoklad = 0;
    DBTransaction dbTran = getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_projektDoklad.p_createProjektDoklad(?,?,?,'0'); end;",0);
      st.setInt(1, idProjekt);
      st.setDate(2, datum);
      st.registerOutParameter(3, Types.INTEGER);
      st.execute();
      idDoklad = st.getInt(3);
      logger.info("ulozDokladProjekt - dokonceno");
    }
    catch (SQLException s) {
      logger.error("Selhalo volání procedury db_jt.kap_projektDoklad.p_createProjektDoklad",s);
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo volání procedury db_jt.kap_projektDoklad.p_createProjektDoklad",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { }
    }
    
    if(idDoklad > 0) {
      ESExportProjektDoklad epd = new ESExportProjektDoklad(this, idDoklad);
      try 
      {
        epd.excelOutput();
      }
      catch(Exception e) 
      {
        logger.error("Selhal export bilance projektu s dokladem "+idDoklad,e);
        e.printStackTrace();
      }
    }
  }

    public void exportDokladyProjekty( boolean generuj ) {
  logger.debug("exportDokladyProjekty START");
    if ( generuj ) 
      ulozDokladyProjekty();
  
  logger.debug("exportDokladyProjekty vytvorene doklady v DB");
  
    ViewObject vo = getKpDatProjektdokladView1();
    vo.clearCache();
    vo.setWhereClause("DT_DATUMVYTVORENI > SYSDATE-1/2");
  
  logger.debug("exportDokladyProjekty Hladamevytvorene doklady ....");
  
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int id = ((Number)row.getAttribute("Id")).intValue();
      
      ESExportProjektDoklad epd = new ESExportProjektDoklad(this, id);
      try 
      {
        logger.info("Pokus o export bilance projektu s dokladem "+id+". Instance epd="+epd);
        epd.excelOutput();
      }
      catch(Throwable e) 
      {
        logger.error("Selhal export bilance projektu s dokladem "+id,e);
        e.printStackTrace();
      }
    }
    vo.closeRowSet();
    logger.debug("Dokoncen export excelu s projektovymi bilancemi");
  }

  /**
   * 
   * Container's getter for VwTmpProjekttogenView1
   */
  public VwTmpProjekttogenViewImpl getVwTmpProjekttogenView1()
  {
    return (VwTmpProjekttogenViewImpl)findViewObject("VwTmpProjekttogenView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladbilanceprotistranaView1
   */
  public VwKpDokladbilanceprotistranaViewImpl getVwKpDokladbilanceprotistranaView1()
  {
    return (VwKpDokladbilanceprotistranaViewImpl)findViewObject("VwKpDokladbilanceprotistranaView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladbilanceprojektView1
   */
  public VwKpDokladbilanceprojektViewImpl getVwKpDokladbilanceprojektView1()
  {
    return (VwKpDokladbilanceprojektViewImpl)findViewObject("VwKpDokladbilanceprojektView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladbilanceodborView1
   */
  public VwKpDokladbilanceodborViewImpl getVwKpDokladbilanceodborView1()
  {
    return (VwKpDokladbilanceodborViewImpl)findViewObject("VwKpDokladbilanceodborView1");
  }

  protected String getUserMustek(int idDoklad) {
 /*esc 19.02.2008 */
 /*    String userMustek = userMustek = (String)getParameterFromDoklad(idDoklad,"SUzivatelmustek"); */
	String userMustek =  (String)getParameterFromDoklad(idDoklad,"SUzivatelmustek");
    if(userMustek!=null) {
      int lomeno = userMustek.indexOf('/');
      if(lomeno>=0) userMustek = userMustek.substring(lomeno+1);
    }
    return userMustek;
  }

  /**
   * 
   * Container's getter for VwKpDokladmanodborprojektView1
   */
  public VwKpDokladmanodborprojektViewImpl getVwKpDokladmanodborprojektView1()
  {
    return (VwKpDokladmanodborprojektViewImpl)findViewObject("VwKpDokladmanodborprojektView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladspolodborprojektView1
   */
  public VwKpDokladspolodborprojektViewImpl getVwKpDokladspolodborprojektView1()
  {
    return (VwKpDokladspolodborprojektViewImpl)findViewObject("VwKpDokladspolodborprojektView1");
  }

  /**
   * 
   * Container's getter for KpDatBudgetzastupView1
   */
  public KpDatBudgetzastupViewImpl getKpDatBudgetzastupView1()
  {
    return (KpDatBudgetzastupViewImpl)findViewObject("KpDatBudgetzastupView1");
  }

  /**
   * 
   * Container's getter for KpTmpVyjimkyprotokoluView1
   */
  public KpTmpVyjimkyprotokoluViewImpl getKpTmpVyjimkyprotokoluView1()
  {
    return (KpTmpVyjimkyprotokoluViewImpl)findViewObject("KpTmpVyjimkyprotokoluView1");
  }

  public void deleteVyjimkyProtokolu() throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;
      
    try {
      st = dbTran.createPreparedStatement("DELETE FROM DB_JT.KP_TMP_VYJIMKYPROTOKOLU",0);
      st.execute();
      dbTran.commit();
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo mazani DB_JT.KP_TMP_VYJIMKYPROTOKOLU",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void insertVyjimkyProtokolu(int idSpol, String period, String kedni) throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;
      
    try {
      st = dbTran.createPreparedStatement("INSERT INTO DB_JT.KP_TMP_VYJIMKYPROTOKOLU (ID_KTGUCETNISPOLECNOST, C_PERIODICITA, S_KEDNI) VALUES (?,?,?)",0);
      st.setInt(1, idSpol);
      st.setString(2, period);
      st.setString(3, kedni);
      st.execute();
      dbTran.commit();
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhal insert do DB_JT.KP_TMP_VYJIMKYPROTOKOLU",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  public void vyjimkyZamykani(int idSpol, java.sql.Date datum, boolean exists) throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;
    
    try {
      if(!exists) {
        st = dbTran.createPreparedStatement("INSERT INTO DB_JT.kp_dat_vyjimkyzamykanitop (ID_KTGUCETNISPOLECNOST, DT_DATUM) VALUES (?,?)",0);
        st.setInt(1, idSpol);
        st.setDate(2, datum);
        st.execute();
      }
      else 
      {
        st = dbTran.createPreparedStatement("UPDATE DB_JT.kp_dat_vyjimkyzamykanitop SET DT_DATUM = ? WHERE ID_KTGUCETNISPOLECNOST = ? AND KVARTAL = trunc(add_months(sysdate,-3),'q')",0);
        st.setDate(1, datum);
        st.setInt(2, idSpol);
        st.execute();
      }
      dbTran.commit();
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhal insert/update DB_JT.kp_dat_vyjimkyzamykanitop",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  /**
   * 
   * Container's getter for VwKpDokladmanodborprojekt2View1
   */
  public VwKpDokladmanodborprojekt2ViewImpl getVwKpDokladmanodborprojekt2View1()
  {
    return (VwKpDokladmanodborprojekt2ViewImpl)findViewObject("VwKpDokladmanodborprojekt2View1");
  }

  /**
   * 
   * Container's getter for VwKtgProjektView1
   */
  public VwKtgProjektViewImpl getVwKtgProjektView1()
  {
    return (VwKtgProjektViewImpl)findViewObject("VwKtgProjektView1");
  }

  /**
   * 
   * Container's getter for VwCisReadekView1
   */
  public VwCisReadekViewImpl getVwCisReadekView1()
  {
    return (VwCisReadekViewImpl)findViewObject("VwCisReadekView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehledView1
   */
  public VwKpDokladprehledViewImpl getVwKpDokladprehledView1()
  {
    return (VwKpDokladprehledViewImpl)findViewObject("VwKpDokladprehledView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehledrozkladView1
   */
  public VwKpDokladprehledrozkladViewImpl getVwKpDokladprehledrozkladView1()
  {
    return (VwKpDokladprehledrozkladViewImpl)findViewObject("VwKpDokladprehledrozkladView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehledobdobiView1
   */
  public VwKpDokladprehledobdobiViewImpl getVwKpDokladprehledobdobiView1()
  {
    return (VwKpDokladprehledobdobiViewImpl)findViewObject("VwKpDokladprehledobdobiView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehledprotistranaView1
   */
  public VwKpDokladprehledprotistranaViewImpl getVwKpDokladprehledprotistranaView1()
  {
    return (VwKpDokladprehledprotistranaViewImpl)findViewObject("VwKpDokladprehledprotistranaView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehleducetView1
   */
  public VwKpDokladprehleducetViewImpl getVwKpDokladprehleducetView1()
  {
    return (VwKpDokladprehleducetViewImpl)findViewObject("VwKpDokladprehleducetView1");
  }

  /**
   * 
   * Container's getter for KpCisRadektextlangKamilView1
   */
  public KpCisRadektextlangKamilViewImpl getKpCisRadektextlangKamilView1()
  {
    return (KpCisRadektextlangKamilViewImpl)findViewObject("KpCisRadektextlangKamilView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehledobdobimesicView1
   */
  public VwKpDokladprehledobdobimesicViewImpl getVwKpDokladprehledobdobimesicView1()
  {
    return (VwKpDokladprehledobdobimesicViewImpl)findViewObject("VwKpDokladprehledobdobimesicView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehledvazbyView1
   */
  public VwKpDokladprehledvazbyViewImpl getVwKpDokladprehledvazbyView1()
  {
    return (VwKpDokladprehledvazbyViewImpl)findViewObject("VwKpDokladprehledvazbyView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladpolozkadeniksumaView1
   */
  public VwKpDokladpolozkadeniksumaViewImpl getVwKpDokladpolozkadeniksumaView1()
  {
    return (VwKpDokladpolozkadeniksumaViewImpl)findViewObject("VwKpDokladpolozkadeniksumaView1");
  }

  /**
   * 
   * Container's getter for VwKpSubkonprehledclenView1
   */
  public VwKpSubkonprehledclenViewImpl getVwKpSubkonprehledclenView1()
  {
    return (VwKpSubkonprehledclenViewImpl)findViewObject("VwKpSubkonprehledclenView1");
  }

  /**
   * 
   * Container's getter for VwKpSubkonprehledclenView2
   */
  public VwKpSubkonprehledclenViewImpl getVwKpSubkonprehledclenView2()
  {
    return (VwKpSubkonprehledclenViewImpl)findViewObject("VwKpSubkonprehledclenView2");
  }

  /**
   * 
   * Container's getter for KpRelSubkonsolidaceclenSpecial1
   */
  public KpRelSubkonsolidaceclenSpecialImpl getKpRelSubkonsolidaceclenSpecial1()
  {
    return (KpRelSubkonsolidaceclenSpecialImpl)findViewObject("KpRelSubkonsolidaceclenSpecial1");
  }

  /**
   * 
   * Container's getter for KpDatDokladvazbySpecialView1
   */
  public KpDatDokladvazbySpecialViewImpl getKpDatDokladvazbySpecialView1()
  {
    return (KpDatDokladvazbySpecialViewImpl)findViewObject("KpDatDokladvazbySpecialView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladvazbySpecialView2
   */
  public KpDatDokladvazbySpecialViewImpl getKpDatDokladvazbySpecialView2()
  {
    return (KpDatDokladvazbySpecialViewImpl)findViewObject("KpDatDokladvazbySpecialView2");
  }

  /**
   * 
   * Container's getter for VwKpDokladvazbyView1
   */
  public VwKpDokladvazbyViewImpl getVwKpDokladvazbyView1()
  {
    return (VwKpDokladvazbyViewImpl)findViewObject("VwKpDokladvazbyView1");
  }

  /**
   * 
   * Container's getter for VwKpEviobchodnirejstrik3mView1
   */
  public VwKpEviobchodnirejstrik3mViewImpl getVwKpEviobchodnirejstrik3mView1()
  {
    return (VwKpEviobchodnirejstrik3mViewImpl)findViewObject("VwKpEviobchodnirejstrik3mView1");
  }

  /**
   * 
   * Container's getter for VwLogGenerovanizamekView1
   */
  public VwLogGenerovanizamekViewImpl getVwLogGenerovanizamekView1()
  {
    return (VwLogGenerovanizamekViewImpl)findViewObject("VwLogGenerovanizamekView1");
  }

  /**
   * 
   * Container's getter for KpDatGenzamekzamitnutiView1
   */
  public KpDatGenzamekzamitnutiViewImpl getKpDatGenzamekzamitnutiView1()
  {
    return (KpDatGenzamekzamitnutiViewImpl)findViewObject("KpDatGenzamekzamitnutiView1");
  }

  /**
   * 
   * Container's getter for VwKpSpolecnostzamekgenView1
   */
  public VwKpSpolecnostzamekgenViewImpl getVwKpSpolecnostzamekgenView1()
  {
    return (VwKpSpolecnostzamekgenViewImpl)findViewObject("VwKpSpolecnostzamekgenView1");
  }

  /**
   * 
   * Container's getter for VwKpSpolecnostzamekgendatumView1
   */
  public VwKpSpolecnostzamekgendatumViewImpl getVwKpSpolecnostzamekgendatumView1()
  {
    return (VwKpSpolecnostzamekgendatumViewImpl)findViewObject("VwKpSpolecnostzamekgendatumView1");
  }

  /**
   * 
   * Container's getter for KpKtgUcetnispolecnostExtendedView1
   */
  public KpKtgUcetnispolecnostExtendedViewImpl getKpKtgUcetnispolecnostExtendedView1()
  {
    return (KpKtgUcetnispolecnostExtendedViewImpl)findViewObject("KpKtgUcetnispolecnostExtendedView1");
  }

  /**
   * 
   * Container's getter for XMenaView1
   */
  public XMenaViewImpl getXMenaView1()
  {
    return (XMenaViewImpl)findViewObject("XMenaView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladzustatkyuctuproti2View1
   */
  public VwKpDokladzustatkyuctuproti2ViewImpl getVwKpDokladzustatkyuctuproti2View1()
  {
    return (VwKpDokladzustatkyuctuproti2ViewImpl)findViewObject("VwKpDokladzustatkyuctuproti2View1");
  }

  /**
   * 
   * Container's getter for KpDatVyjimkyzamykanitopView1
   */
  public KpDatVyjimkyzamykanitopViewImpl getKpDatVyjimkyzamykanitopView1()
  {
    return (KpDatVyjimkyzamykanitopViewImpl)findViewObject("KpDatVyjimkyzamykanitopView1");
  }

  /**
   * 
   * Container's getter for VwKpVyjimkyzamykanitopView1
   */
  public VwKpVyjimkyzamykanitopViewImpl getVwKpVyjimkyzamykanitopView1()
  {
    return (VwKpVyjimkyzamykanitopViewImpl)findViewObject("VwKpVyjimkyzamykanitopView1");
  }
  
  private boolean noDeleteDBChyba = false;
  
  protected void setDbChybaExportExcel(int idRadku, Throwable e) 
  {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;
    
    try {
      st = dbTran.createPreparedStatement("UPDATE DB_JT.KP_DAT_DOKLADFRONTA SET S_STAV = 'DB chyba', S_SQLERROR=? WHERE ID=?",0);
      st.setString(1, "Export excel - "+e.getMessage());
      st.setInt(2, idRadku);
      st.execute();

      dbTran.commit();
      
      noDeleteDBChyba = true;
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  /**
   * 
   * Container's getter for KpKtgUcetniskupinaView1
   */
  public KpKtgUcetniskupinaViewImpl getKpKtgUcetniskupinaView1()
  {
    return (KpKtgUcetniskupinaViewImpl)findViewObject("KpKtgUcetniskupinaView1");
  }

  /**
   * 
   * Container's getter for KpRelAppuserskupinaView1
   */
  public KpRelAppuserskupinaViewImpl getKpRelAppuserskupinaView1()
  {
    return (KpRelAppuserskupinaViewImpl)findViewObject("KpRelAppuserskupinaView1");
  }

  /**
   * 
   * Container's getter for VwKtgEviadminspolecnostView1
   */
  public VwKtgEviadminspolecnostViewImpl getVwKtgEviadminspolecnostView1()
  {
    return (VwKtgEviadminspolecnostViewImpl)findViewObject("VwKtgEviadminspolecnostView1");
  }

  public int currentUsersId() 
  {
    String user = cz.jtbank.konsolidace.common.Utils.getUserName(this, true);
    Number userId = null;
    
    ViewObject voUser = getKtgAppuserView1();
    voUser.clearCache();
    voUser.setWhereClause("S_USERID = '"+user+"'");
    if(voUser.hasNext()) 
    {
      Row rowUser = voUser.next();
      userId = (Number) rowUser.getAttribute("Id");
    }
    voUser.closeRowSet();
    if(userId == null) return -1;
    
    return userId.intValue();
  }

  /**
   * 
   * Container's getter for KpTmpVazbyVyjimkyView1
   */
  public KpTmpVazbyVyjimkyViewImpl getKpTmpVazbyVyjimkyView1()
  {
    return (KpTmpVazbyVyjimkyViewImpl)findViewObject("KpTmpVazbyVyjimkyView1");
  }
  
  public boolean isUserVyjimkaVazeb(int idSpol) 
  {
    boolean ret = false;
    ViewObject vo = getKpTmpVazbyVyjimkyView1();
    vo.clearCache();
    vo.setWhereClause("KpTmpVazbyVyjimky.ID_KTGAPPUSER = "+currentUsersId()+" AND KpTmpVazbyVyjimky.ID_KTGUCETNISPOLECNOST = "+idSpol);
    if(vo.hasNext()) 
    {
      ret = true;
    }
    vo.closeRowSet();
    return ret;
  }
  
  private void nocniGenerovaniSubkonsolidaci() {
    java.sql.Date datum = null;
    ViewObject vo = getKpParametryView1();
    vo.clearCache();
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      datum = ((oracle.jbo.domain.Date) row.getAttribute("DtGensubkonsolidaci")).dateValue();
    }
    vo.closeRowSet();
    logger.info("Zaèátek generování subkonsolidací k "+datum);

    List subList = new ArrayList();
    ViewObject voSpol = getKpKtgUcetnispolecnostView1();
    voSpol.clearCache();
    voSpol.setWhereClause("ID_CISSUBJECT = 200 AND C_AUTOMATICKEGENEROVANI = '1'");
    voSpol.setOrderByClause("ID");
    while(voSpol.hasNext()) 
    {
      Row rowSpol = voSpol.next();
      Number id = (Number)rowSpol.getAttribute("Id");
      subList.add(id);
    }
    voSpol.closeRowSet();
    
    //Vychytavka app. serveru!
    Iterator iter = subList.iterator();
    while(iter.hasNext()) 
    {
      Number id = (Number) iter.next();
      try {
        logger.error("Pokus o generování subkonsolidace "+id+" k "+datum);
        calculateSubDoklad(id.intValue(), datum, false, Constants.AUTOMAT_USER);
      }
      catch(KisException kex) 
      {
        logger.error("Chyba pøi generování subkonsolidace "+id+" k "+datum,kex);
      }
    }
    
    logger.info("Konec generování subkonsolidací k "+datum);
  }

  /**
   * 
   * Container's getter for VwKpDokladautoChybamustkuView1
   */
  public VwKpDokladautoChybamustkuViewImpl getVwKpDokladautoChybamustkuView1()
  {
    return (VwKpDokladautoChybamustkuViewImpl)findViewObject("VwKpDokladautoChybamustkuView1");
  }

  /**
   * 
   * Container's getter for KpLogUcetnispolecnostOnlineView1
   */
  public KpLogUcetnispolecnostOnlineViewImpl getKpLogUcetnispolecnostOnlineView1()
  {
    return (KpLogUcetnispolecnostOnlineViewImpl)findViewObject("KpLogUcetnispolecnostOnlineView1");
  }

  /**
   * 
   * Container's getter for KpDatKapitalskupinyView1
   */
  public KpDatKapitalskupinyViewImpl getKpDatKapitalskupinyView1()
  {
    return (KpDatKapitalskupinyViewImpl)findViewObject("KpDatKapitalskupinyView1");
  }

  public void kapitalPozice(String akce,
                            int id,
                            int idSkupPoz,
                            String dynamicky,
                            java.sql.Date dtOd,
                            double castka) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;

    //INSERT
    if("I".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("INSERT INTO DB_JT.kp_dat_kapitalSkupiny "+
                                            "(ID_SKUPINAPOZICE, C_DYNAMICKY, DT_PLATNOSTOD, ND_CASTKA) "+
                                            "VALUES (?,?,?,?)",0);
        st.setInt(1, idSkupPoz);
        st.setString(2, dynamicky);
        st.setDate(3, dtOd);
        st.setDouble(4, castka);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury kapitalPozice akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { // ignore 
		}
      }
    }
    
    //UPDATE
    else if("U".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("UPDATE DB_JT.kp_dat_kapitalSkupiny "+
                                            "SET ID_SKUPINAPOZICE=?, C_DYNAMICKY=?, DT_PLATNOSTOD=?, ND_CASTKA=? "+
                                            "WHERE ID=?",0);
        st.setInt(1, idSkupPoz);
        st.setString(2, dynamicky);
        st.setDate(3, dtOd);
        st.setDouble(4, castka);
        st.setInt(5, id);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury kapitalPozice akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    //DELETE
    else if("D".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("DELETE FROM DB_JT.kp_dat_kapitalSkupiny "+
                                            "WHERE ID=?",0);
        st.setInt(1, id);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury kapitalPozice akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    dbTran.commit();
  }

  public void qVazbaProdukt(String akce,
                            int id,
                            int idSpol,
                            String prod1,
                            String prod2) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;

    //INSERT
    if("I".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("INSERT INTO DB_JT.KP_DEF_QUESTORVAZBAPRODUCT "+
                                            "(ID_KTGUCETNISPOLECNOST, S_PRODUCT_1, S_PRODUCT_2) "+
                                            "VALUES (?,?,?)",0);
        st.setInt(1, idSpol);
        st.setString(2, prod1);
        st.setString(3, prod2);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury qVazbaProdukt akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    //UPDATE
    else if("U".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("UPDATE DB_JT.KP_DEF_QUESTORVAZBAPRODUCT "+
                                            "SET ID_KTGUCETNISPOLECNOST=?, S_PRODUCT_1=?, S_PRODUCT_2=? "+
                                            "WHERE ID=?",0);
        st.setInt(1, idSpol);
        st.setString(2, prod1);
        st.setString(3, prod2);
        st.setInt(4, id);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury qVazbaProdukt akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    //DELETE
    else if("D".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("DELETE FROM DB_JT.KP_DEF_QUESTORVAZBAPRODUCT "+
                                            "WHERE ID=?",0);
        st.setInt(1, id);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury qVazbaProdukt akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    dbTran.commit();
  }

  private void frontaChyba(int idFronta,
                           String vychoziStav,
                           String konecnyStav,
                           String hlaska) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;

    if(idFronta>0) {    
      try {
        st = dbTran.createPreparedStatement("UPDATE DB_JT.KP_DAT_DOKLADFRONTA "+
                                            "SET S_STAV=?, S_SQLERROR=? "+
                                            "WHERE ID=?",0);
        st.setString(1, konecnyStav);
        st.setString(2, hlaska);
        st.setInt(3, idFronta);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury frontaChyba.",s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    else 
    {
      try {
        st = dbTran.createPreparedStatement("UPDATE DB_JT.KP_DAT_DOKLADFRONTA "+
                                            "SET S_STAV=?, S_SQLERROR=? "+
                                            "WHERE S_STAV=?",0);
        st.setString(1, konecnyStav);
        st.setString(2, hlaska);
        st.setString(3, vychoziStav);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury frontaChyba.",s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    dbTran.commit();
  }

  /**
   * 
   * Container's getter for KpDatKapitalskupinyMaxView1
   */
  public KpDatKapitalskupinyMaxViewImpl getKpDatKapitalskupinyMaxView1()
  {
    return (KpDatKapitalskupinyMaxViewImpl)findViewObject("KpDatKapitalskupinyMaxView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetschvalovaniView1
   */
  public VwDatBudgetschvalovaniViewImpl getVwDatBudgetschvalovaniView1()
  {
    return (VwDatBudgetschvalovaniViewImpl)findViewObject("VwDatBudgetschvalovaniView1");
  }

  /**
   * 
   * Container's getter for KpDatProjektdokladView1
   */
  public KpDatProjektdokladViewImpl getKpDatProjektdokladView1()
  {
    return (KpDatProjektdokladViewImpl)findViewObject("KpDatProjektdokladView1");
  }

  /**
   * 
   * Container's getter for KpDatProjektdokladView2
   */
  public KpDatProjektdokladViewImpl getKpDatProjektdokladView2()
  {
    return (KpDatProjektdokladViewImpl)findViewObject("KpDatProjektdokladView2");
  }

  /**
   * 
   * Container's getter for VwKpDokladautoUnifuctyView1
   */
  public VwKpDokladautoUnifuctyViewImpl getVwKpDokladautoUnifuctyView1()
  {
    return (VwKpDokladautoUnifuctyViewImpl)findViewObject("VwKpDokladautoUnifuctyView1");
  }

  /**
   * 
   * Container's getter for VwProtistranahistorieView1
   */
  public VwProtistranahistorieViewImpl getVwProtistranahistorieView1()
  {
    return (VwProtistranahistorieViewImpl)findViewObject("VwProtistranahistorieView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetmusteknakladchybiView1
   */
  public VwDatBudgetmusteknakladchybiViewImpl getVwDatBudgetmusteknakladchybiView1()
  {
    return (VwDatBudgetmusteknakladchybiViewImpl)findViewObject("VwDatBudgetmusteknakladchybiView1");
  }

  public void deleteDenniDoklady() throws KisException
  {
    DBTransaction dbTran = getDBTransaction();
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_doklad.p_deleteDatum(sysdate-2,sysdate,'denni'); end;",0);
      st.execute();
      logger.info("deleteDenniDoklady - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volani db_jt.kap_doklad.p_deleteDatum ",s);
      throw new KisException("Selhalo volani db_jt.kap_doklad.p_deleteDatum ",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  /**
   * 
   * Container's getter for KpCisTyptransakcegroupView1
   */
  public KpCisTyptransakcegroupViewImpl getKpCisTyptransakcegroupView1()
  {
    return (KpCisTyptransakcegroupViewImpl)findViewObject("KpCisTyptransakcegroupView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetpolozkadata1PARView1
   */
  public VwDatBudgetpolozkadata1PARViewImpl getVwDatBudgetpolozkadata1PARView1()
  {
    return (VwDatBudgetpolozkadata1PARViewImpl)findViewObject("VwDatBudgetpolozkadata1PARView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetpolozkadata2PARView1
   */
  public VwDatBudgetpolozkadata2PARViewImpl getVwDatBudgetpolozkadata2PARView1()
  {
    return (VwDatBudgetpolozkadata2PARViewImpl)findViewObject("VwDatBudgetpolozkadata2PARView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetProjektpolozkadata1PARView1
   */
  public VwDatBudgetProjektpolozkadata1PARViewImpl getVwDatBudgetProjektpolozkadata1PARView1()
  {
    return (VwDatBudgetProjektpolozkadata1PARViewImpl)findViewObject("VwDatBudgetProjektpolozkadata1PARView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetProjektpolozkadata2PARView1
   */
  public VwDatBudgetProjektpolozkadata2PARViewImpl getVwDatBudgetProjektpolozkadata2PARView1()
  {
    return (VwDatBudgetProjektpolozkadata2PARViewImpl)findViewObject("VwDatBudgetProjektpolozkadata2PARView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetprojektView1
   */
  public VwDatBudgetprojektViewImpl getVwDatBudgetprojektView1()
  {
    return (VwDatBudgetprojektViewImpl)findViewObject("VwDatBudgetprojektView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetprojekttransakceView1
   */
  public VwDatBudgetprojekttransakceViewImpl getVwDatBudgetprojekttransakceView1()
  {
    return (VwDatBudgetprojekttransakceViewImpl)findViewObject("VwDatBudgetprojekttransakceView1");
  }

  /**
   * 
   * Container's getter for VwDatBudgetprojtransakcedocView1
   */
  public VwDatBudgetprojtransakcedocViewImpl getVwDatBudgetprojtransakcedocView1()
  {
    return (VwDatBudgetprojtransakcedocViewImpl)findViewObject("VwDatBudgetprojtransakcedocView1");
  }

  /**
   * 
   * Container's getter for KpRelProjektucspolView1
   */
  public KpRelProjektucspolViewImpl getKpRelProjektucspolView1()
  {
    return (KpRelProjektucspolViewImpl)findViewObject("KpRelProjektucspolView1");
  }
  
  public void refreshLastCloseDate() throws KisException {
    DBTransaction dbTran = this.getDBTransaction();
    CallableStatement st = null;
      
    try {
      st = dbTran.createCallableStatement("begin db_jt.kap_gui.p_updateSpolecnostCloseDate; end;",0);
      st.execute();
      logger.info("refreshLastCloseDate - dokonceno");
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_gui.p_updateSpolecnostCloseDate",s);
      throw new KisException("Selhalo volání procedury db_jt.kap_gui.p_updateSpolecnostCloseDate",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }

  /**
   * 
   * Container's getter for VwOlapDokladtoexportView1
   */
  public VwOlapDokladtoexportViewImpl getVwOlapDokladtoexportView1()
  {
    return (VwOlapDokladtoexportViewImpl)findViewObject("VwOlapDokladtoexportView1");
  }

  /**
   * 
   * Container's getter for KpDefQuestorvazbaproductView1
   */
  public KpDefQuestorvazbaproductViewImpl getKpDefQuestorvazbaproductView1()
  {
    return (KpDefQuestorvazbaproductViewImpl)findViewObject("KpDefQuestorvazbaproductView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladvazbySpecialView3
   */
  public KpDatDokladvazbySubkonsViewImpl getKpDatDokladvazbySpecialView3()
  {
    return (KpDatDokladvazbySubkonsViewImpl)findViewObject("KpDatDokladvazbySpecialView3");
  }

  /**
   * 
   * Container's getter for KpDatDokladvazbySubkonsView1
   */
  public KpDatDokladvazbySubkonsViewImpl getKpDatDokladvazbySubkonsView1()
  {
    return (KpDatDokladvazbySubkonsViewImpl)findViewObject("KpDatDokladvazbySubkonsView1");
  }

  /**
   * 
   * Container's getter for VwRepSchvalovakpostupView1
   */
  public VwRepSchvalovakpostupViewImpl getVwRepSchvalovakpostupView1()
  {
    return (VwRepSchvalovakpostupViewImpl)findViewObject("VwRepSchvalovakpostupView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladvazbyprehledView1
   */
  public VwKpDokladvazbyprehledViewImpl getVwKpDokladvazbyprehledView1()
  {
    return (VwKpDokladvazbyprehledViewImpl)findViewObject("VwKpDokladvazbyprehledView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladvazbyprehledView2
   */
  public VwKpDokladvazbyprehledViewImpl getVwKpDokladvazbyprehledView2()
  {
    return (VwKpDokladvazbyprehledViewImpl)findViewObject("VwKpDokladvazbyprehledView2");
  }

  /**
   * 
   * Container's getter for KpDatVazbyzamekView1
   */
  public KpDatVazbyzamekViewImpl getKpDatVazbyzamekView1()
  {
    return (KpDatVazbyzamekViewImpl)findViewObject("KpDatVazbyzamekView1");
  }
  
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  private void mailVazbyProti(String akce, int idDoklad, int idSkup, int idProti) 
  {
    Number idSpol = null;
    String datum = null;
  
    ViewObject vo = getKpDatDokladView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idSpol = (Number) row.getAttribute("IdKtgucetnispolecnost");
      datum = sdf.format(((oracle.jbo.domain.Date) row.getAttribute("DtDatum")).getData());
    }
    vo.closeRowSet();
    
    String nazevSpol = null, nazevProti = null;
    Number idUcetniProti = null;
    
    vo = getKpKtgUcetnispolecnostView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idSpol);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("SNazev");
    }
    vo.closeRowSet();

    vo.clearCache();
    vo.setWhereClause("ID = "+idProti);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevProti = (String) row.getAttribute("SNazev");
      idUcetniProti = (Number) row.getAttribute("IdZodpovednaucetni");
    }
    vo.closeRowSet();
    
    int kym = currentUsersId();
    String zamkl = null;
    vo = getKtgAppuserView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+kym);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      zamkl = (String) row.getAttribute("SKrestni")+" ";
      zamkl += (String) row.getAttribute("SPrijmeni")+" (";
      zamkl += (String) row.getAttribute("SEmail")+" / ";
      zamkl += (String) row.getAttribute("STelefon")+")";
    }
    vo.closeRowSet();
    
    Mail mail = new Mail();
    mail.sendZamekParovaniVazeb(this, akce, idUcetniProti, nazevSpol, nazevProti, datum, idSkup, zamkl);
  }

  public void vazbyZamek(String akce, int idDoklad, int idSkup, int idProti) throws KisException
  {
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_jt.kap_guiv.p_vazbyZamek(?,?,?,?,?); end;",0);
      st.setString(1, akce);
      st.setInt(2, idDoklad);
      st.setInt(3, idSkup);
      st.setInt(4, idProti);
      st.setString(5, getUser());
      st.execute();
      logger.info("vazbyZamek - dokonceno");
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      logger.error("Selhalo volání procedury db_jt.kap_guiv.p_vazbyZamek",e);
      throw new KisException("Selhalo volání procedury db_jt.kap_guiv.p_vazbyZamek",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
    
    mailVazbyProti(akce, idDoklad, idSkup, idProti);
  }

  /**
   * 
   * Container's getter for KpCisZamekView1
   */
  public KpCisZamekViewImpl getKpCisZamekView1()
  {
    return (KpCisZamekViewImpl)findViewObject("KpCisZamekView1");
  }

  /**
   * 
   * Container's getter for KpDatMngsegmentbossView1
   */
  public KpDatMngsegmentbossViewImpl getKpDatMngsegmentbossView1()
  {
    return (KpDatMngsegmentbossViewImpl)findViewObject("KpDatMngsegmentbossView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladvazbauserindexView1
   */
  public VwKpDokladvazbauserindexViewImpl getVwKpDokladvazbauserindexView1()
  {
    return (VwKpDokladvazbauserindexViewImpl)findViewObject("VwKpDokladvazbauserindexView1");
  }
  
  public String getAppserverDBPassword() 
  {
    String ret = null;
    ViewObject vo = getKpParametryView1();
    vo.clearCache();
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      ret = (String) row.getAttribute("AppserverPassword");
    }
    vo.closeRowSet();
    return ret;
  }

  /**
   * 
   * Container's getter for ListDokladView1
   */
  public ListDokladViewImpl getListDokladView1()
  {
    return (ListDokladViewImpl)findViewObject("ListDokladView1");
  }

  /**
   * 
   * Container's getter for VwKtgProjektsimpleView1
   */
  public VwKtgProjektsimpleViewImpl getVwKtgProjektsimpleView1()
  {
    return (VwKtgProjektsimpleViewImpl)findViewObject("VwKtgProjektsimpleView1");
  }

  /**
   * 
   * Container's getter for VwDatDokladsubkonvazbydenikView1
   */
  public VwDatDokladsubkonvazbydenikViewImpl getVwDatDokladsubkonvazbydenikView1()
  {
    return (VwDatDokladsubkonvazbydenikViewImpl)findViewObject("VwDatDokladsubkonvazbydenikView1");
  }

  /**
   * 
   * Container's getter for VwSubkonDokladpolozkaNewView1
   */
  public VwSubkonDokladpolozkaNewViewImpl getVwSubkonDokladpolozkaNewView1()
  {
    return (VwSubkonDokladpolozkaNewViewImpl)findViewObject("VwSubkonDokladpolozkaNewView1");
  }

  /**
   * 
   * Container's getter for VwDatDokladsubkonvazbydenik2View1
   */
  public VwDatDokladsubkonvazbydenik2ViewImpl getVwDatDokladsubkonvazbydenik2View1()
  {
    return (VwDatDokladsubkonvazbydenik2ViewImpl)findViewObject("VwDatDokladsubkonvazbydenik2View1");
  }

  /**
   * 
   * Container's getter for KpCisMngsegmentView1
   */
  public KpCisMngsegmentViewImpl getKpCisMngsegmentView1()
  {
    return (KpCisMngsegmentViewImpl)findViewObject("KpCisMngsegmentView1");
  }

  /**
   * 
   * Container's getter for VwKtgUcetnispolecnostView1
   */
  public VwKtgUcetnispolecnostViewImpl getVwKtgUcetnispolecnostView1()
  {
    return (VwKtgUcetnispolecnostViewImpl)findViewObject("VwKtgUcetnispolecnostView1");
  }

  /**
   * 
   * Container's getter for VwRelSpolecnostKartaTypView1
   */
  public VwRelSpolecnostKartaTypViewImpl getVwRelSpolecnostKartaTypView1()
  {
    return (VwRelSpolecnostKartaTypViewImpl)findViewObject("VwRelSpolecnostKartaTypView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladautoPodnikatelView1
   */
  public VwKpDokladautoPodnikatelViewImpl getVwKpDokladautoPodnikatelView1()
  {
    return (VwKpDokladautoPodnikatelViewImpl)findViewObject("VwKpDokladautoPodnikatelView1");
  }

  /**
   * 
   * Container's getter for KpCisRadektextlangMilosView1
   */
  public KpCisRadektextlangMilosViewImpl getKpCisRadektextlangMilosView1()
  {
    return (KpCisRadektextlangMilosViewImpl)findViewObject("KpCisRadektextlangMilosView1");
  }

  /**
   * 
   * Container's getter for KpDatSpolecnostlistView1
   */
  public KpDatSpolecnostlistViewImpl getKpDatSpolecnostlistView1()
  {
    return (KpDatSpolecnostlistViewImpl)findViewObject("KpDatSpolecnostlistView1");
  }

  /**
   * 
   * Container's getter for KpDatSpolecnostlistdetailView1
   */
  public KpDatSpolecnostlistdetailViewImpl getKpDatSpolecnostlistdetailView1()
  {
    return (KpDatSpolecnostlistdetailViewImpl)findViewObject("KpDatSpolecnostlistdetailView1");
  }

  /**
   * 
   * Container's getter for VwLogGenerovanizamekprotokolView1
   */
  public VwLogGenerovanizamekprotokolViewImpl getVwLogGenerovanizamekprotokolView1()
  {
    return (VwLogGenerovanizamekprotokolViewImpl)findViewObject("VwLogGenerovanizamekprotokolView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladspolodborprojMisView1
   */
  public VwKpDokladspolodborprojMisViewImpl getVwKpDokladspolodborprojMisView1()
  {
    return (VwKpDokladspolodborprojMisViewImpl)findViewObject("VwKpDokladspolodborprojMisView1");
  }

  /**
   * 
   * Container's getter for VwKpUcspolpocetzamkuView1
   */
  public VwKpUcspolpocetzamkuViewImpl getVwKpUcspolpocetzamkuView1()
  {
    return (VwKpUcspolpocetzamkuViewImpl)findViewObject("VwKpUcspolpocetzamkuView1");
  }

  /**
   * 
   * Container's getter for VwJtSpolecnostuserpravoView1
   */
  public VwJtSpolecnostuserpravoViewImpl getVwJtSpolecnostuserpravoView1()
  {
    return (VwJtSpolecnostuserpravoViewImpl)findViewObject("VwJtSpolecnostuserpravoView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladvazbySkupinyView1
   */
  public KpDatDokladvazbySkupinyViewImpl getKpDatDokladvazbySkupinyView1()
  {
    return (KpDatDokladvazbySkupinyViewImpl)findViewObject("KpDatDokladvazbySkupinyView1");
  }

  /**
   * 
   * Container's getter for KpKtgUcetnispolecnostZamkyView1
   */
  public KpKtgUcetnispolecnostZamkyViewImpl getKpKtgUcetnispolecnostZamkyView1()
  {
    return (KpKtgUcetnispolecnostZamkyViewImpl)findViewObject("KpKtgUcetnispolecnostZamkyView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladsubkonlogdokladyView1
   */
  public KpDatDokladsubkonlogdokladyViewImpl getKpDatDokladsubkonlogdokladyView1()
  {
    return (KpDatDokladsubkonlogdokladyViewImpl)findViewObject("KpDatDokladsubkonlogdokladyView1");
  }

  public void isGenerovaniClosed(int typ, java.sql.Date datum) throws KisException
  {
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_jt.p_isGenerovaniClosed(?,?); end;",0);
      st.setInt(1, typ);
      st.setDate(2, datum);
      st.execute();
      logger.info("isGenerovaniClosed - dokonceno");
    }
    catch (SQLException e) 
    {
      logger.error("Selhalo volání procedury isGenerovaniClosed",e);
      throw new KisException("Selhalo volání procedury isGenerovaniClosed",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
  }

  /**
   * 
   * Container's getter for VwKpKonsolidacefrontaView1
   */
  public VwKpKonsolidacefrontaViewImpl getVwKpKonsolidacefrontaView1()
  {
    return (VwKpKonsolidacefrontaViewImpl)findViewObject("VwKpKonsolidacefrontaView1");
  }

  /**
   * 
   * Container's getter for KpDatSegmentzastupView1
   */
  public KpDatSegmentzastupViewImpl getKpDatSegmentzastupView1()
  {
    return (KpDatSegmentzastupViewImpl)findViewObject("KpDatSegmentzastupView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladrozdilyView1
   */
  public KpDatDokladrozdilyViewImpl getKpDatDokladrozdilyView1()
  {
    return (KpDatDokladrozdilyViewImpl)findViewObject("KpDatDokladrozdilyView1");
  }

  public void ucSpolObaDoklady(String akce,
                               int idSpol,
                               java.sql.Date dtOdOrig,
                               java.sql.Date dtDoOrig,
                               java.sql.Date dtOd,
                               java.sql.Date dtDo) throws KisException
  {
    DBTransaction dbTran = this.getDBTransaction();
    PreparedStatement st = null;

    //INSERT
    if("I".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("INSERT INTO DB_JT.KP_KTG_UCETNISPOLECNOST_ZAMKY "+
                                            "(ID_KTGUCETNISPOLECNOST, DT_PLATNOSTOD, DT_PLATNOSTDO) "+
                                            "VALUES (?,?,?)",0);
        st.setInt(1, idSpol);
        st.setDate(2, dtOd);
        st.setDate(3, dtDo);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury ucSpolObaDoklady akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    //UPDATE
    else if("U".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("UPDATE DB_JT.KP_KTG_UCETNISPOLECNOST_ZAMKY "+
                                            "SET DT_PLATNOSTOD=?, DT_PLATNOSTDO=? "+
                                            "WHERE ID_KTGUCETNISPOLECNOST=?, DT_PLATNOSTOD=?, DT_PLATNOSTDO=?",0);
        st.setDate(1, dtOd);
        st.setDate(2, dtDo);
        st.setInt(3, idSpol);
        st.setDate(4, dtOdOrig);
        st.setDate(5, dtDoOrig);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury ucSpolObaDoklady akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    //DELETE
    else if("D".equals(akce)) {
      try {
        st = dbTran.createPreparedStatement("DELETE FROM DB_JT.KP_KTG_UCETNISPOLECNOST_ZAMKY "+
                                            "WHERE ID_KTGUCETNISPOLECNOST=?, DT_PLATNOSTOD=?, DT_PLATNOSTDO=?",0);
        st.setInt(1, idSpol);
        st.setDate(2, dtOdOrig);
        st.setDate(3, dtDoOrig);
        st.execute();
      }
      catch (SQLException s) {
        s.printStackTrace(); //pro zacatek
        throw new KisException("Selhalo volání procedury ucSpolObaDoklady akce="+akce,s);
      }
      finally {
        try {
          if (st != null) st.close();
        } 
        catch (SQLException s) { /* ignore */}
      }
    }
    
    dbTran.commit();
  }

  /**
   * 
   * Container's getter for VwKpDokladautoChybamustkuSumView1
   */
  public VwKpDokladautoChybamustkuSumViewImpl getVwKpDokladautoChybamustkuSumView1()
  {
    return (VwKpDokladautoChybamustkuSumViewImpl)findViewObject("VwKpDokladautoChybamustkuSumView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladpolozkaView1
   */
  public KpDatDokladpolozkaViewImpl getKpDatDokladpolozkaView1()
  {
    return (KpDatDokladpolozkaViewImpl)findViewObject("KpDatDokladpolozkaView1");
  }

  /**
   * 
   * Container's getter for VwKpDokladprehledprotistranaSum1
   */
  public VwKpDokladprehledprotistranaSumImpl getVwKpDokladprehledprotistranaSum1()
  {
    return (VwKpDokladprehledprotistranaSumImpl)findViewObject("VwKpDokladprehledprotistranaSum1");
  }

  /**
   * 
   * Container's getter for KpDatDokladView3
   */
  public KpDatDokladViewImpl getKpDatDokladView3()
  {
    return (KpDatDokladViewImpl)findViewObject("KpDatDokladView3");
  }

  public void manStartImportPozice(int idSpol, java.sql.Date datum, String mu) throws KisException
  {
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_dsa.loadManual.p_startImport(?,?,?); end;",0);
      st.setInt(1, idSpol);
      st.setDate(2, datum);
      st.setString(3, mu);
      st.execute();
      logger.info("manStartImportPozice - dokonceno");
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      logger.error("Selhalo volání procedury db_dsa.loadManual.p_startImport",e);
      throw new KisException("Selhalo volání procedury db_dsa.loadManual.p_startImport",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
  }

  public void manTestInsertUverova(String ico, String nazev,
                           String ucet, String text, 
                           double castkaCNB, double castka, double castkaLocal,
                           String mena
                           ,double rizikovost         //esc 22.09 2009
                           , String skupina
                           , String banka             //esc 23.02.2011
                           , double cistaangazovanost //esc 25.03 2011
                           ) throws KisException
  {
    
    //47115378  ICO Banky
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_dsa.loadManual.p_testInsertUverovaR(?,?,?,?,?,?,?,?,?,?,?,?,?); end;",0);
      st.setString(1, getUser());
      st.setString(2, ico);
      st.setString(3, nazev);
      st.setString(4, ucet);
      st.setString(5, text);
      st.setDouble(6, castkaCNB);
      st.setDouble(7, castka);
      st.setDouble(8, castkaLocal);
      st.setString(9, mena);
      st.setDouble(10, rizikovost);
      st.setString(11, skupina);    //esc 16.12.2010
      st.setString(12, banka); //esc 23.02.2011
      st.setDouble(13, cistaangazovanost); //esc 28.03.2011
      
      st.execute();
      logger.info("manTestInsertUverova - dokonceno -rizikovost = "+rizikovost+" skupina:"+skupina+" banka: "+banka);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      logger.error("Selhalo volání procedury db_dsa.loadManual.p_testInsertUverovaR",e);
      throw new KisException("Selhalo volání procedury db_dsa.loadManual.p_testInsertUverovaR",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
  }

  public void manTestInsertMenova(String ucet, String text, 
                           double castkaCNB, double castka, double castkaLocal,
                           String mena) throws KisException
  {
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_dsa.loadManual.p_testInsertMenova(?,?,?,?,?,?,?); end;",0);
      st.setString(1, getUser());
      st.setString(2, ucet);
      st.setString(3, text);
      st.setDouble(4, castkaCNB);
      st.setDouble(5, castka);
      st.setDouble(6, castkaLocal);
      st.setString(7, mena);
      st.execute();
      logger.info("manTestInsertMenova - dokonceno:"+text+" - "+castka+" - "+mena);
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      logger.error("Selhalo volání procedury db_dsa.loadManual.p_testInsertMenova",e);
      throw new KisException("Selhalo volání procedury db_dsa.loadManual.p_testInsertMenova",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
  }

  /**
   * 
   * Container's getter for DatKurzlistExportView1
   */
  public DatKurzlistExportViewImpl getDatKurzlistExportView1()
  {
    return (DatKurzlistExportViewImpl)findViewObject("DatKurzlistExportView1");
  }

  /**
   * 
   * Container's getter for KpDatDokladzdrojdatagregaceAgr1View1
   */
  public KpDatDokladzdrojdatagregaceAgr1ViewImpl getKpDatDokladzdrojdatagregaceAgr1View1()
  {
    return (KpDatDokladzdrojdatagregaceAgr1ViewImpl)findViewObject("KpDatDokladzdrojdatagregaceAgr1View1");
  }

  /**
   * 
   * Container's getter for KpDatDokladzdrojdatagregaceVazby1View1
   */
  public KpDatDokladzdrojdatagregaceVazby1ViewImpl getKpDatDokladzdrojdatagregaceVazby1View1()
  {
    return (KpDatDokladzdrojdatagregaceVazby1ViewImpl)findViewObject("KpDatDokladzdrojdatagregaceVazby1View1");
  }

  /**
   * 
   * Container's getter for VwRepSpolecnostcartesisView1
   */
  public VwRepSpolecnostcartesisViewImpl getVwRepSpolecnostcartesisView1()
  {
    return (VwRepSpolecnostcartesisViewImpl)findViewObject("VwRepSpolecnostcartesisView1");
  }

  /**
   * 
   * Container's getter for VwRepOsobycartesisView1
   */
  public VwRepOsobycartesisViewImpl getVwRepOsobycartesisView1()
  {
    return (VwRepOsobycartesisViewImpl)findViewObject("VwRepOsobycartesisView1");
  }
  
  public void exportCartesis() {
    java.sql.Date datum = null;
    ViewObject vo = getKpParametryView1();
    vo.clearCache();
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      datum = ((oracle.jbo.domain.Date) row.getAttribute("DtNgDatumDo")).dateValue();
    }
    vo.closeRowSet();
    
    ESExportKurzy ek = new ESExportKurzy(this, datum);
    try 
    {
      //ek.excelOutput();
      ek.createCsv();
      ek.csvOutput();

    }
    catch (Exception e) 
    {
      logger.error("Selhalo generování excelu smìnných kurzù pro Cartesis",e);
    }

    ESExportProtistrany ep = new ESExportProtistrany(this, datum);
    try 
    {
      ep.excelOutput();
    }
    catch (Exception e) 
    {
      logger.error("Selhalo generování excelu protistran pro Cartesis",e);
    }
  
    ESExportOsoby eo = new ESExportOsoby(this);
    try 
    {
      eo.excelOutput();
    }
    catch (Exception e) 
    {
      logger.error("Selhalo generování excelu osob pro Cartesis",e);
    }
  }

  /**
   * 
   * Container's getter for KpDatDokladzdrojdatagregaceMis1View1
   */
  public KpDatDokladzdrojdatagregaceMis1ViewImpl getKpDatDokladzdrojdatagregaceMis1View1()
  {
    return (KpDatDokladzdrojdatagregaceMis1ViewImpl)findViewObject("KpDatDokladzdrojdatagregaceMis1View1");
  }

  public void muUnif(Number idDoklad) throws KisException
  {
    CallableStatement st = null;
    try {
      st = getDBTransaction().createCallableStatement("begin db_jt.MU_unif (?); end;",0);
      st.setInt(1, idDoklad.intValue());
      st.execute();
      logger.info("muUnif - dokonceno");
    }
    catch (SQLException e) 
    {
      e.printStackTrace();
      logger.error("Selhalo volání procedury db_jt.MU_unif ",e);
      throw new KisException("Selhalo volání procedury db_jt.MU_unif ",e);
    }
    finally 
    {
      try 
      {
        if(st != null) st.close();
      }
      catch(Exception e) {}
    }
  }

  /**
   * 
   * Container's getter for KpDatDokladzdrojdatagregaceShares1View1
   */
  public KpDatDokladzdrojdatagregaceShares1ViewImpl getKpDatDokladzdrojdatagregaceShares1View1()
  {
    return (KpDatDokladzdrojdatagregaceShares1ViewImpl)findViewObject("KpDatDokladzdrojdatagregaceShares1View1");
  }

  /**
   * 
   * Container's getter for KpDatGenerovanizamekView1
   */
  public KpDatGenerovanizamekViewImpl getKpDatGenerovanizamekView1()
  {
    return (KpDatGenerovanizamekViewImpl)findViewObject("KpDatGenerovanizamekView1");
  }

  /**
   * 
   * Container's getter for KpDatZdaFlowZustatekView1
   */
  public KpDatZdaFlowZustatekViewImpl getKpDatZdaFlowZustatekView1()
  {
    return (KpDatZdaFlowZustatekViewImpl)findViewObject("KpDatZdaFlowZustatekView1");
  }

  /**
   * 
   * Container's getter for KpDatZdaFlowZustatekLocalView1
   */
  public KpDatZdaFlowZustatekLocalViewImpl getKpDatZdaFlowZustatekLocalView1()
  {
    return (KpDatZdaFlowZustatekLocalViewImpl)findViewObject("KpDatZdaFlowZustatekLocalView1");
  }

  /**
   * 
   * Container's getter for KpDatZdaFlowProtistranaView1
   */
  public KpDatZdaFlowProtistranaViewImpl getKpDatZdaFlowProtistranaView1()
  {
    return (KpDatZdaFlowProtistranaViewImpl)findViewObject("KpDatZdaFlowProtistranaView1");
  }

  /**
   * 
   * Container's getter for KpDatZdaFlowProtistranaLocalView1
   */
  public KpDatZdaFlowProtistranaLocalViewImpl getKpDatZdaFlowProtistranaLocalView1()
  {
    return (KpDatZdaFlowProtistranaLocalViewImpl)findViewObject("KpDatZdaFlowProtistranaLocalView1");
  }

  /**
   * 
   * Container's getter for KpDatZdaFlowSegmentLocalView1
   */
  public KpDatZdaFlowSegmentLocalViewImpl getKpDatZdaFlowSegmentLocalView1()
  {
    return (KpDatZdaFlowSegmentLocalViewImpl)findViewObject("KpDatZdaFlowSegmentLocalView1");
  }

  /**
   * 
   * Container's getter for VwCashflowView1
   */
  public VwCashflowViewImpl getVwCashflowView1()
  {
    return (VwCashflowViewImpl)findViewObject("VwCashflowView1");
  }

	/**
	 * 
	 * Container's getter for VwRepSchvalovakpostupDetailView1
	 */
	public VwRepSchvalovakpostupDetailViewImpl getVwRepSchvalovakpostupDetailView1() {
    return (VwRepSchvalovakpostupDetailViewImpl)findViewObject("VwRepSchvalovakpostupDetailView1");
	}

    /**Container's getter for VwRepSchvalovakhlavaView1
     */
    public VwRepSchvalovakhlavaViewImpl getVwRepSchvalovakhlavaView1() {
    return (VwRepSchvalovakhlavaViewImpl)findViewObject("VwRepSchvalovakhlavaView1");
    }


    /**Container's getter for VwDatSchvalovakradekView1
     */
    public VwDatSchvalovakradekViewImpl getVwDatSchvalovakradekView1() {
    return (VwDatSchvalovakradekViewImpl)findViewObject("VwDatSchvalovakradekView1");
    }

    /**Container's getter for VwRepSchvalovakhlavadetailView1
     */
    public VwRepSchvalovakhlavadetailViewImpl getVwRepSchvalovakhlavadetailView1() {
    return (VwRepSchvalovakhlavadetailViewImpl)findViewObject("VwRepSchvalovakhlavadetailView1");
    }

  /**
   * 
   * Container's getter for VwKpDokladuverovapozice2011SpecialView1
   */
  public VwKpDokladuverovapozice2011SpecialViewImpl getVwKpDokladuverovapozice2011SpecialView1()
  {
    return (VwKpDokladuverovapozice2011SpecialViewImpl)findViewObject("VwKpDokladuverovapozice2011SpecialView1");
  }

  /**
   * 
   * Container's getter for VwDatSchvalovakView1
   */
  public VwDatSchvalovakViewImpl getVwDatSchvalovakView1()
  {
    return (VwDatSchvalovakViewImpl)findViewObject("VwDatSchvalovakView1");
  }


}




class ExcelThread //extends Thread //Provizorni reseni
{
  static Logger logger = Logger.getLogger(ExcelThread.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_AUTO_GEN)); }
  
  private DokladyModuleImpl dokladyModule;
  private int dokladId;
  private int dokladIdIfrs;
  private boolean isAutomat;
  private int restGenUser;
  private int idRadku;
  private int dokladIdKamil;
  private int dokladIdIfrsKamil;
  private int jenomPs;
  private String guiFlag;
  
  private Number idKtgUcetniSpolecnost;
  private java.sql.Date datum;

  public ExcelThread(DokladyModuleImpl aDokladyModule,
                     int aDokladId,
                     int aDokladIdIfrs,
                     boolean aIsAutomat,
                     int aRestGenUser,
                     int aIdRadku,
                     int aDokladIdKamil,
                     int aDokladIdIfrsKamil,
                     int aPs,
                     String aGF) 
  {
    dokladyModule = aDokladyModule;
    dokladId = aDokladId;
    dokladIdIfrs = aDokladIdIfrs;
    isAutomat = aIsAutomat;
    restGenUser = aRestGenUser;
    idRadku = aIdRadku;
    dokladIdKamil = aDokladIdKamil;
    dokladIdIfrsKamil = aDokladIdIfrsKamil;
    jenomPs = aPs;
    guiFlag = aGF;

    //if(isAutomat) AutogenCounter.getInstance().removeOne();
    
    logger.info(this.getName()+": dokladId="+dokladId);
    logger.info(this.getName()+": dokladIdIfrs="+dokladIdIfrs);
    logger.info(this.getName()+": isAutomat="+isAutomat);
    logger.info(this.getName()+": restGenUser="+restGenUser);
    logger.info(this.getName()+": idRadku="+idRadku);
    logger.info(this.getName()+": jenomPs="+jenomPs);
    logger.info(this.getName()+": guiFlag="+guiFlag);
    
    ViewObject vo = dokladyModule.getVwKpDokladfrontaView1();
    vo.clearCache();
    vo.setWhereClause("ID = "+idRadku);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idKtgUcetniSpolecnost = (Number) row.getAttribute("Idspolecnost");
      datum = ((oracle.jbo.domain.Date) row.getAttribute("Adatum")).dateValue();
    }
    vo.closeRowSet();
  }
  
  public ExcelThread(DokladyModuleImpl aDokladyModule,
                     int aDokladId,
                     int aDokladIdIfrs,
                     boolean aIsAutomat,
                     int aRestGenUser,
                     int aIdRadku,
                     int aPs,
                     String aGF) 
  {
    this(aDokladyModule, 
         aDokladId,
         aDokladIdIfrs,
         aIsAutomat,
         aRestGenUser,
         aIdRadku,
         -1,
         -1,
         aPs,
         aGF);
  }
  
  //Provizorni reseni
  private String getName() 
  {
    return (isAutomat?"Auto":"Man")+":R="+idRadku;
  }

  public void run()
  {
    //uzivatelsky mustek???
    String userMustek = dokladyModule.getUserMustek(dokladId);
    
    //start
    Date startDate, endDate;
  
    startDate = new Date();
    logger.info(this.getName()+": Start generovani excelu");
    if(dokladId > 0) {
      ESExportDoklady esed = new ESExportDoklady(dokladyModule,
                                                 new Number(dokladId),
                                                 //false,
                                                 userMustek,
                                                 dokladIdKamil>0?new Number(dokladIdKamil):null,
                                                 jenomPs);
      //nejdrive smazeme stare soubory
      //if(jenomPs>=0) {
        try {
          esed.deleteAllOriginalFiles();
        }
        catch (IOException ex) {
          logger.error(this.getName()+": Chyba pri mazani vsech xls pro ne-IFRS doklad "+dokladId,ex);
          ex.printStackTrace(); //pro zacatek
        }
      //}

      try {
logger.debug ( this.getName()+"   esed.excelOutput() pred" );
        esed.excelOutput();        
      }
      catch (Throwable ex) {
logger.debug ( this.getName()+"   esed.excelOutput() v catch " + ex.toString() );      
        logger.error(this.getName()+": Chyba pri generovani xls bilance pro doklad "+dokladId,ex);
        ex.printStackTrace(); //pro zacatek
        dokladyModule.setDbChybaExportExcel(idRadku, ex);
      }

      endDate = new Date();
      //if(isAutomat) AutogenProtokol.getInstance().genLogRow(startDate,endDate,esed.getWarning()+muk,esed.getTypeDoklad(),esed.getNazevSpol(),esed.getIdSpol(),esed.getIdDoklad(),esed.getRkcFlag());
      logger.info(this.getName()+": Vygenerovan excel pro "+dokladId);
    }

    //IFRS
    if(dokladIdIfrs > 0) {
      userMustek = dokladyModule.getUserMustek(dokladIdIfrs);
      
      startDate = new Date();
      ESExportDoklady esedIfrs = new ESExportDoklady(dokladyModule,
                                                     new Number(dokladIdIfrs),
                                                     //true,
                                                     userMustek,
                                                     dokladIdIfrsKamil>0?new Number(dokladIdIfrsKamil):null,
                                                     jenomPs);
      try {
logger.debug ( this.getName()+"   esedIfrs.excelOutput() pred" );      
        esedIfrs.excelOutput();
      }
      catch (Throwable ex) {
logger.debug ( this.getName()+"   esed.excelOutput() v catch " + ex.toString() );            
        logger.error(this.getName()+": Chyba pri generovani xls bilance IFRS pro doklad "+dokladIdIfrs,ex);
        ex.printStackTrace(); //pro zacatek
        dokladyModule.setDbChybaExportExcel(idRadku, ex);
      }
      
      endDate = new Date();

      //if(isAutomat) AutogenProtokol.getInstance().genLogRow(startDate,endDate,esedIfrs.getWarning(),esedIfrs.getTypeDoklad(),esedIfrs.getNazevSpol(),esedIfrs.getIdSpol(),esedIfrs.getIdDoklad(),esed.getRkcFlag());
      logger.info(this.getName()+": Vygenerovan excel IFRS pro "+dokladIdIfrs);
    }
    
    ESExportDoklad2007 d2007 = new ESExportDoklad2007(dokladyModule, idKtgUcetniSpolecnost, datum);
    try 
    {
      d2007.excelOutput();
    }
    catch (Throwable ex) {
      logger.error(this.getName()+": Chyba pri generovani xls bilance 2007 pro idSpol="+idKtgUcetniSpolecnost+" a datum="+datum,ex);
      ex.printStackTrace(); //pro zacatek
      dokladyModule.setDbChybaExportExcel(idRadku, ex);
    }
    
    int dte = dokladId;
    if(dokladIdIfrs>0) dte = dokladIdIfrs;
    if(dte>0 && dokladyModule.isTopas(new Number(dte))) 
    {
      CSVExportPackage pack;
      if(!"1".equals(guiFlag)) pack = new CSVExportPackage(dokladyModule, new Number(dte), null);
      else pack = new CSVExportPackage(dokladyModule, new Number(dte), "F000");
      try {
        pack.createCsv();
        pack.csvOutput();
      }
      catch (Throwable t) {
        logger.error(this.getName()+": Chyba pri generovani CSV pro doklad="+dte, t);
        t.printStackTrace();
        dokladyModule.setDbChybaExportExcel(idRadku, t);
      }
    }
  }
  
}