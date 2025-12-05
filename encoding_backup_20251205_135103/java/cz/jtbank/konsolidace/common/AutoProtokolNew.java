package cz.jtbank.konsolidace.common;
import cz.jtbank.konsolidace.doklady.common.*;
import cz.jtbank.konsolidace.excel.*;
import cz.jtbank.konsolidace.mail.*;
import java.text.*;
import oracle.jbo.*;
import java.sql.*;
import oracle.jbo.domain.Number;
import oracle.jbo.server.*;
import oracle.jbo.client.Configuration;
import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cz.jtbank.konsolidace.common.Logging;

public class AutoProtokolNew
{
  private static final Logger logger = LoggerFactory.getLogger(AutoProtokolNew.class);
  static {
    Logging.addAppenderToLogger(AutoProtokolNew.class.getName(), Logging.getAppender(Logging.LOG_AUTO_GEN));
  }

  private static AutoProtokolNew instance;

  protected ApplicationModule am = null;

  private StringBuffer protokol;
  private java.util.Date startDate;

  private List<Number> ids;
  private java.sql.Date datum;
  private java.sql.Date datumDnes;
  private String dtString;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private SimpleDateFormat sdfHod = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy");

  private Calendar cCheck;
  
  private Mail mail;

  private AutoProtokolNew()
  {
  }
  
  public static AutoProtokolNew getInstance() 
  {
    if(instance == null) instance = new AutoProtokolNew();
    return instance;
  }
  
  public void init(Number[] ids, java.sql.Date datum) 
  {
    startDate = new java.util.Date();
    this.ids = Arrays.asList(ids);
    this.datum = datum;
    cCheck = Calendar.getInstance();
    if(datum == null)
		cCheck.setTimeInMillis ( System.currentTimeMillis() );
	else
		cCheck.setTime(datum);	
  }
  
  private Mail getMail() 
  {
    if(mail==null) mail = new Mail();
    return mail;
  }
  
  public boolean checkProtokol(ApplicationModule am) 
  {
    if(ids == null || datum == null) return false;
    
    this.am = am;
  
    int cnt = ((DokladyModule)am).checkAutoGenFronta(0);
    if(cnt != 0) return false;
    
    try {
      SystemStatus.getInstance().setStatus("Generov�n� protokol�");
      
      createProtokol(ids, Constants.ALL);
      List<String> fileName = new ArrayList<>();
      fileName.add( saveLogFile(null, Constants.ALL, 0) );
      sendEmailProtokol((String[])fileName.toArray(new String[] {}), Constants.ALL);
  
      createProtokol(ids, Constants.ALL_SPECIFIC);
      fileName.clear();
      fileName.add( saveLogFile(null, Constants.ALL_SPECIFIC, 0) );
      sendEmailProtokol((String[])fileName.toArray(new String[] {}), Constants.ALL_SPECIFIC);
      
      saveUserProtokols(Constants.UCETNI);
      saveUserProtokols(Constants.NADRIZENA_UCETNI);
      saveUserProtokols(Constants.OO);
      saveUserProtokols(Constants.SEF_SEGMENTU);
      
      ViewObject voSkup = am.findViewObject("KpKtgUcetniskupinaView1");
      voSkup.clearCache();
      while(voSkup.hasNext()) 
      {
        Row row = voSkup.next();
        int id = ((Number) row.getAttribute("Id")).intValue();
        String skupina = (String) row.getAttribute("SKod");
        saveUserProtokolsSkupina(id, skupina);
      }
      voSkup.closeRowSet();
      
      createProtokolMustek(ids);
      fileName.clear();
      fileName.add( saveLogFile(null, Constants.MUSTEK, 0) );
      ESExportChybyMustku ecm = new ESExportChybyMustku(am,1);
      try 
      {
        ecm.excelOutput();
        if(ecm.getFileName()!=null)
          fileName.add( ecm.getFileAbsoluteName() );      
      }
      catch (Exception e) { logger.error("Chyby mustku 1", e); };

      ecm = new ESExportChybyMustku(am,3);
      try 
      {
        ecm.excelOutput();
        if(ecm.getFileName()!=null)
          fileName.add( ecm.getFileAbsoluteName() );      
      }
      catch (Exception e) { logger.error("Chyby mustku 3", e); };

      ecm = new ESExportChybyMustku(am,"100,101,102,103,104,105,106");
      try 
      {
        ecm.excelOutput();
        if(ecm.getFileName()!=null)
          fileName.add( ecm.getFileAbsoluteName() );      
      }
      catch (Exception e) { logger.error("Chyby mustku 2007", e); };
/*      
      ESExportPodnikatelUcty epu = new ESExportPodnikatelUcty(am);
      try 
      {
        epu.excelOutput();
        if(epu.getFileName()!=null)
          fileName.add( epu.getFileAbsoluteName() );      
      }
      catch (Exception e) { logger.error("Chybejici ucty v mustcich podnik.", e); };
*/      
System.gc();
      if(fileName.size()>1)
        sendEmailProtokol((String[])fileName.toArray(new String[] {}), Constants.MUSTEK);
  
      createProtokolUnifUcty(ids);
      fileName.clear();
      fileName.add( saveLogFile(null, Constants.UNIF_UCET, 0) );
      ESExportUnifUcty euu = new ESExportUnifUcty(am);
      try 
      {
        euu.excelOutput();
        if(euu.getFileName()!=null)
          fileName.add( euu.getFileAbsoluteName() );      
      }
      catch (Exception e) { logger.error("Unif. ucty", e); };
      if(euu.getFileName()!=null)
        sendEmailProtokol((String[])fileName.toArray(new String[] {}), Constants.UNIF_UCET);
      
      logger.debug("PROTOKOL - Dokonceno generovani protokolu");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (protokoly) CHYBA!",t);
      t.printStackTrace();
    }
System.gc();      
    try {
      SystemStatus.getInstance().setStatus("Kontrola schvalov�k�");
      checkSchvalovak();
      logger.debug("PROTOKOL - Dokoncena kontrola schvalovaku");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (SL) CHYBA!",t);
      t.printStackTrace();
    }
System.gc();      
    try {
      SystemStatus.getInstance().setStatus("Kontrola budget�");
      //esc vypnute koli chybe 06.12.2010      checkBudgetDenni();
      checkBudgetDenni(); // od 18.10.2011
System.gc();
//esc Mesicni pocital iba projekty .. 
//esc 10/2011 potrebujeme ich vypnut tak vypnem uz tu
//esc 10/2011     checkBudgetMesicni();
//System.gc();
      checkBudgetSchvalovani();
      logger.debug("PROTOKOL - Dokoncena kontrola budgetu");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (budgety) CHYBA!",t);
      t.printStackTrace();
    }
      
    try {
      SystemStatus.getInstance().setStatus("Kontrola zpr�v karet projekt�");
      checkProjektMemorandum();
      logger.debug("PROTOKOL - Dokoncena kontrola informacnich zprav karet projektu");

      SystemStatus.getInstance().setStatus("Kontrola p�ecen�n� projekt�");
      checkProjektPreceneni();
      logger.debug("PROTOKOL - Dokoncena kontrola preceneni projektu");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (projekty) CHYBA!",t);
      t.printStackTrace();
    }

    
    try {
      SystemStatus.getInstance().setStatus("Kontrola zpr�v karet SPV");
      checkSpolecnostZprava();
      logger.debug("PROTOKOL - Dokoncena kontrola zprav karet SPV");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (karty SPV) CHYBA!",t);
      t.printStackTrace();
    }
      
    try {
      SystemStatus.getInstance().setStatus("Kontrola administrovan�ch spole�nost�");
      checkEviSpolecnostOR3M();
      logger.debug("PROTOKOL - Dokoncena kontrola evi. spol. - OR do mesice");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (admin. spol.) CHYBA!",t);
      t.printStackTrace();
    }
      
    try {
      SystemStatus.getInstance().setStatus("Kontrola schvalov�n� doklad�");
      checkSpolecnostZamekGen();
      logger.debug("PROTOKOL - Excel se schvalenimi a zamitnutimi");
  
      SystemStatus.getInstance().setStatus("Kontrola spole�nost� kons. skupiny 4");
      checkSpolecnostSkupina4();
      logger.debug("PROTOKOL - Excel se spolecnostmi vs. skupina 4");
      
      SystemStatus.getInstance().setStatus("Kontrola souladu admin. a evid. spole�nost�");
      checkSpolecnostiSoulad();
      logger.debug("PROTOKOL - Excel s nesouladem ve spolecnostech adm. vs. evi.");
    
      checkZamekVSClose();
      logger.debug("PROTOKOL - Nesoulad zamyk�n� a last close date");

      checkZamekExterni();
      logger.debug("PROTOKOL - Excel s doklady externich spolecnosti bez zamceni");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (doklady a spol.) CHYBA!",t);
      t.printStackTrace();
    }
      
    try {
      SystemStatus.getInstance().setStatus("Kontrola zm�n v protistran�ch v prim�rn�ch syst�mech");
      checkZmenyProtistran();
      logger.debug("PROTOKOL - Excel se zm�nami v protistran�ch v prim�rn�ch syst�mech");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (protistrany) CHYBA!",t);
      t.printStackTrace();
    }
      
    try {
      SystemStatus.getInstance().setStatus("Kontrola chyb�j�c�ch n�kladov�ch ��t� v p�evodov�ch m�stc�ch budget�");
      checkBudgetMustekNaklad();
      logger.debug("PROTOKOL - Excel s chyb�j�c�mi n�kladov�mi ��ty v p�evodov�ch m�stc�ch budget�");
    } 
    catch(Throwable t) 
    {
      logger.error("AutoProtokolNew (mustky budg.) CHYBA!",t);
      t.printStackTrace();
    }
    
    SystemStatus.getInstance().setStatus("Majetek mail - zjistovani zmen");
    checkZmenaMajetUcasti();
    
    ids=null; datum=null;
    mail=null;
    
    return true;
  }
  
  private boolean isEnabledEmailByDay() 
  {
    boolean ret = false;
  
    Calendar cal = Calendar.getInstance();
    int denTydne = cal.get(Calendar.DAY_OF_WEEK);
    int hodina = cal.get(Calendar.HOUR_OF_DAY);
    if(hodina<8) denTydne--;
    if(denTydne==0) denTydne=7;

	if (logger.isDebugEnabled()){
		logger.debug("ENABLED{}-{}", denTydne, hodina);
		logger.debug("am={}←am", am==null?"null":am.toString());
	}
    ViewObject vo = am.findViewObject("KpKalendarakciView1");
	if (logger.isDebugEnabled()){
		logger.debug("vo={}←vo", vo==null?"null":vo.toString());
	}	
    vo.clearCache();
    vo.setWhereClause("ID = "+denTydne+" AND C_POSILATEMAILY = '1'");
    if(vo.hasNext()) 
    {

      logger.debug("ENABLEDEmailByDay:TRUE");
      ret = true;
    }
    vo.closeRowSet();

    return ret;
  }
  
  private void sendEmailProtokol(String[] fileName, int userType) 
  {
    if(!isEnabledEmailByDay()) return;
  
    Set set = new HashSet();
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    if(userType==Constants.ALL) vo.setWhereClause("ID_MSGTYPE = 2");
    else if(userType==Constants.ALL_SPECIFIC) vo.setWhereClause("ID_MSGTYPE = 20");
    else if(userType==Constants.MUSTEK) vo.setWhereClause("ID_MSGTYPE = 22");
    else if(userType==Constants.UNIF_UCET) vo.setWhereClause("ID_MSGTYPE = 25");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number userId = (Number) row.getAttribute("IdKtgappuser");
      set.add(userId);
    }
    vo.closeRowSet();
    
    if(!set.isEmpty()) {
      Mail mail = getMail();
      mail.sendProtokol(am,set,fileName,userType);
    }
  }
  
  private void saveUserProtokols(int typeUser) throws KisException
  {
    Set set = new HashSet();
    Mail mail = getMail();
    
    String kdo = null, oraKdo = null;
    if(typeUser==Constants.UCETNI) { 
      kdo="ID_ZODPOVEDNAUCETNI"; 
      oraKdo="IdZodpovednaucetni"; 
    }
    else if(typeUser==Constants.OO) { 
      kdo="ID_ODPOVEDNAOSOBA"; 
      oraKdo="IdOdpovednaosoba"; 
    }
    else if(typeUser==Constants.TOP) { 
      kdo="ID_TOPMNG"; 
      oraKdo="IdTopmng"; 
    }
    else if(typeUser==Constants.SEF_SEGMENTU) { 
      kdo="ID_MNGSEGMENTBOSS"; 
      oraKdo="IdMngsegmentboss"; 
    }
  
    ViewObject voUser;
    if(typeUser != Constants.NADRIZENA_UCETNI) {
      voUser = am.findViewObject("KpKtgUcetnispolecnostView1");
      voUser.clearCache();
      voUser.setWhereClause(kdo+" IS NOT NULL AND C_AUTOMATICKEGENEROVANI = '1'");
      while(voUser.hasNext()) 
      {
        Row rowUser = voUser.next();
        Number ucId = (Number) rowUser.getAttribute(oraKdo);
        set.add(ucId);
      }
      voUser.closeRowSet();
    }
    else 
    {
      voUser = am.findViewObject("KtgAppuserView1");
      voUser.clearCache();
      voUser.setWhereClause("ID_NADRIZENAUCETNI IS NOT NULL");
      while(voUser.hasNext()) 
      {
        Row rowUser = voUser.next();
        Number ucId = (Number) rowUser.getAttribute("IdNadrizenaucetni");
        set.add(ucId);
      }
      voUser.closeRowSet();
    }

    voUser = am.findViewObject("KpKtgUcetnispolecnostView1");
    voUser.clearCache();
    Iterator<Number> iter = set.iterator();
    while(iter.hasNext())
    {
      List<Number> myIds = new ArrayList<>();
      Number ucId = iter.next();
      String whereKdo;
      if(typeUser == Constants.NADRIZENA_UCETNI) whereKdo = "ID_ZODPOVEDNAUCETNI IN (SELECT uIn.ID FROM DB_JT.KTG_APPUSER uIn WHERE uIn.ID_NADRIZENAUCETNI = "+ucId+")";
      else whereKdo = kdo+" = "+ucId;
      voUser.setWhereClause(whereKdo+" AND C_AUTOMATICKEGENEROVANI = '1'");
      while(voUser.hasNext()) 
      {
        Row rowUser = voUser.next();
        Number spolId = (Number) rowUser.getAttribute("Id");
        myIds.add(spolId);
      }
      voUser.closeRowSet();

      //createProtokol(myIds, Constants.UCETNI);
      createProtokol(myIds, typeUser);
      //neposilat protokol, co nic neobsahuje
      if(protokol.indexOf("clsLogRowOk")>0 || protokol.indexOf("clsLogRowNotOk")>0) {
        String[] fileName = { saveLogFile(ucId, typeUser, 0) };
        if(isEnabledEmailByDay())
          mail.sendProtokol(am, ucId, fileName, typeUser);
      }
    }
  }

  private void saveUserProtokolsSkupina(int idSkupina, String skupina) throws KisException
  {
    Set set = new HashSet();
    Mail mail = getMail();
    
    ViewObject voUser = am.findViewObject("KpRelAppuserskupinaView1");
    voUser.clearCache();
    voUser.setWhereClause("ID_UCETNISKUPINA = "+idSkupina);
    while(voUser.hasNext()) 
    {
      Row rowUser = voUser.next();
      Number ucId = (Number) rowUser.getAttribute("IdKtgappuser");
      set.add(ucId);
    }
    voUser.closeRowSet();

    List<Number> myIds = new ArrayList<>();
    voUser = am.findViewObject("KpKtgUcetnispolecnostView1");
    voUser.clearCache();
    voUser.setWhereClause("C_AUTOMATICKEGENEROVANI = '1' AND ID_CISSUBJECT <> 200 AND db_jt.f_jeSpolecnostClenSkupiny(ID,"+idSkupina+",SYSDATE) = 1");
    while(voUser.hasNext()) 
    {
      Row rowUser = voUser.next();
      Number spolId = (Number) rowUser.getAttribute("Id");
      myIds.add(spolId);
    }
    voUser.closeRowSet();

    Iterator<Number> iter = set.iterator();
    while(iter.hasNext())
    {
      Number ucId = iter.next();

      createProtokol(myIds, Constants.SKUPINY);
      //neposilat protokol, co nic neobsahuje
      if(protokol.indexOf("clsLogRowOk")>0 || protokol.indexOf("clsLogRowNotOk")>0) {
        String fileName = saveLogFile(ucId, Constants.SKUPINY, idSkupina);
        if(isEnabledEmailByDay())
          mail.sendProtokolSkupiny(am, ucId, fileName, skupina);
      }
    }
  }

  private void checkZmenaMajetUcasti() {
    try {
      Mail m = getMail();
      ESExportMajetekZmena ed = new ESExportMajetekZmena(am);
      ed.excelOutput();
      
      m.sendMajetekZmeny ( am, ed.getFileAbsoluteName() );
      
    } catch (Exception e) { logger.error("Chyba zmena majetek ucast", e); };
      
  }

  private String checkLastCloseDate(Number idSpol)
  {
    oracle.jbo.domain.Date lastCloseDate = null;
   
    ViewObject voSpol = am.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();
    voSpol.setWhereClause("ID = " + idSpol);
    if(voSpol.hasNext()) 
    {
      Row rowSpol = voSpol.next();
      lastCloseDate = (oracle.jbo.domain.Date) rowSpol.getAttribute("DtLastclosedate");
    }
    voSpol.closeRowSet();
    
    if(lastCloseDate == null) 
    {
      return "Nen� nastaven term�n uzav�en� ��etn�ho obdob�!";
    }
    
    Calendar cur = Calendar.getInstance();
    int curDay = cur.get(Calendar.DAY_OF_MONTH);
    cur.set(Calendar.DAY_OF_MONTH,10);
    Calendar lcd = Calendar.getInstance();
    lcd.setTime(lastCloseDate.dateValue());
    int yearDiff = 0, dayDiff = 0;
    yearDiff = cur.get(Calendar.YEAR)-lcd.get(Calendar.YEAR);
    if(yearDiff == 0) 
    {
      dayDiff = cur.get(Calendar.DAY_OF_YEAR)-lcd.get(Calendar.DAY_OF_YEAR);
    }
    else
    {
      dayDiff = yearDiff * lcd.getActualMaximum(Calendar.DAY_OF_YEAR);
      dayDiff += cur.get(Calendar.DAY_OF_YEAR)-lcd.get(Calendar.DAY_OF_YEAR);
    }
    
    if((curDay > 10 && dayDiff > 11) || dayDiff > 41) 
    {
      return "Pro�l� term�n uzav�en� ��etn�ho obdob�! Naposledy bylo uzav�eno ��etn� obdob� k "+sdf.format(lastCloseDate.dateValue());
    }
    
    return "";
  }
  
  private boolean isOOClosed(String idSpol,
                             String dtClose) 
  {
    ViewObject vo = am.findViewObject("KpDatGenerovanizamekView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idSpol+
                 " and C_ZAMCENO = '1'"+
                 " and ID_CISDOKLAD = 1"+
                 " and mod(ID_CISZAMEK,100) = 2"+
                 " and DT_DATUM > to_date('"+dtClose+"','dd.mm.yyyy')");
    boolean ret = vo.hasNext();
    vo.closeRowSet();
    
    return ret;
  }
  
  private void checkZamekVSClose()
  {
    Set set = new TreeSet();
    Set oos = new HashSet();
    ViewObject voSpol = am.findViewObject("VwKtgUcetnispolecnostView1");
    voSpol.clearCache();
    voSpol.setWhereClause("dt_datumArchivace is null"+
                     " and exists (select null "+
                                  "from db_jt.kp_dat_generovaniZamek gz "+
                                  "where VwKtgUcetnispolecnost.id = gz.ID_KTGUCETNISPOLECNOST "+
                                    "and gz.C_ZAMCENO = '1' "+
                                    "and gz.ID_CISDOKLAD = 1 "+
                                    "and gz.DT_DATUM > VwKtgUcetnispolecnost.dt_lastCloseDate)");
    while(voSpol.hasNext()) 
    {
      Row rowSpol = voSpol.next();
      String nazev = (String) rowSpol.getAttribute("SNazev");
      String id = rowSpol.getAttribute("Id").toString();
      String zu = (String) rowSpol.getAttribute("Zodpovednaucetni");
      String nu = (String) rowSpol.getAttribute("Nadrizenaucetni");
      Number idOO = (Number) rowSpol.getAttribute("IdOdpovednaosoba");
      oracle.jbo.domain.Date dtOra = (oracle.jbo.domain.Date) rowSpol.getAttribute("DtLastclosedate");
      String dt = dtOra==null ? "" : sdf.format(dtOra.dateValue());
      set.add(nazev+" ("+id+") [v ��etnictv� "+dt+"] Z�:"+zu+", N�:"+nu);
      
      if(isOOClosed(id, dt)) oos.add(idOO);
    }
    voSpol.closeRowSet();
    
    if(!set.isEmpty()) 
    {
      Mail mail = getMail();
      mail.sendZamekPoCloseDate(am, set, oos);
    }
  }

  private String getWhereSchvaleno(Number idSpol, int typeUser) 
  {
    String where = "";
  
    ViewObject voVyjimka = am.findViewObject("KpTmpVyjimkyprotokoluView1");
    voVyjimka.clearCache();
    voVyjimka.setWhereClause("ID_KTGUCETNISPOLECNOST = " + idSpol);
    boolean std = true;
    if(voVyjimka.hasNext()) 
    {
      std = false;
      Row rowVyjimka = voVyjimka.next();
      String periodicita = (String) rowVyjimka.getAttribute("CPeriodicita");
      String keDni = (String) rowVyjimka.getAttribute("SKedni");
      if("M".equals(periodicita)) 
      {
        where = " AND DT_DATUM + " + keDni +" < sysdate"; 
                //" AND DT_DATUM >= trunc(sysdate,'yyyy')-1";
      }
      else if("Q".equals(periodicita)) 
      {
        where = //" AND DT_DATUM + " + keDni +" < add_months(to_date('01.'||to_char(sysdate,'q')*3||'.'||to_char(sysdate,'yyyy'),'dd.mm.yyyy'),1)"+
                " AND add_months(trunc(DT_DATUM,'q'),3) + " + keDni +" < sysdate";
                //" AND DT_DATUM >= trunc(sysdate,'yyyy')-1";
      }
      else if("Y".equals(periodicita)) 
      {
        where = //" AND DT_DATUM between trunc(add_months(sysdate,-12),'yyyy') and trunc(sysdate,'yyyy')"+
                " AND sysdate > to_date('"+keDni+"'||to_char(sysdate,'yyyy'),'dd.mm.yyyy')";
      }
      else if("V".equals(periodicita)) 
      {
        where = " AND 1=0";
      }
      else std = true;
    }
    voVyjimka.closeRowSet();
    
    if(std) {
      String dniNavic = "";
      if(typeUser == Constants.UCETNI) dniNavic = " + "+Constants.DNI_UCETNI;
      else if(typeUser == Constants.OO) dniNavic = " + "+Constants.DNI_OO;
      else if(typeUser == Constants.TOP) dniNavic = " + "+Constants.DNI_TOP;
      else if(typeUser == Constants.SEF_SEGMENTU) dniNavic = " + "+Constants.DNI_TOP;
      
      if(cCheck.get(Calendar.DAY_OF_MONTH)==31 && cCheck.get(Calendar.MONTH)==Calendar.DECEMBER) 
      {
        dniNavic += " + 31";
      }
              
      if(typeUser == Constants.TOP || typeUser == Constants.SEF_SEGMENTU) 
      {
        where = //" AND add_months(trunc(DT_DATUM,'q'),3)" + dniNavic +" < sysdate"+
                " AND db_jt.f_getDatumSchavleniTop("+idSpol+",DT_DATUM) < sysdate";
                //" AND DT_DATUM >= trunc(sysdate,'yyyy')-1";
      }
      else 
      {
        where = " AND DT_DATUM" + dniNavic +" < sysdate"; 
                //" AND DT_DATUM >= trunc(sysdate,'yyyy')-1";
      }
    }
    
    return where;
  }

  private String checkSchvaleno(Number idSpol, int typeUser)
  {
    StringBuffer buf = new StringBuffer();
    oracle.jbo.domain.Date dtDatum = null;
    Number idDoklad = null;
    
    ViewObject vo = am.findViewObject("KpDatDokladView2");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = " + idSpol +
                      " AND ID_KTGUCETNISPOLECNOST = ID_SUBKONSOLIDACE "+
                      getWhereSchvaleno(idSpol, typeUser) +
//                      " AND NL_KROK = 1 AND EXISTS (SELECT NULL FROM DB_JT.KP_KTG_DOKLAD KKD WHERE KKD.ID = KpDatDoklad.ID_KTGDOKLAD AND KKD.ID_CISDOKLAD = 1)" +
                      " AND C_BASEDOKLAD = '1' AND EXISTS (SELECT NULL FROM DB_JT.KP_KTG_DOKLAD KKD WHERE KKD.ID = KpDatDoklad.ID_KTGDOKLAD AND KKD.ID_CISDOKLAD = 1)" +
                      " AND DT_DATUM >= to_date('01.07.2005','dd.mm.yyyy')" +
                      " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_GENEROVANIZAMEK z" +
                                  " WHERE z.ID_CISDOKLAD = 1" +
                                    " AND z.ID_KTGUCETNISPOLECNOST = KpDatDoklad.ID_KTGUCETNISPOLECNOST" +
                                    " AND z.DT_DATUM = KpDatDoklad.DT_DATUM" +
                                    " AND z.C_ZAMCENO <> '1'" +
                                    " AND mod(z.ID_CISZAMEK,100) = "+typeUser+
                                    " AND (z.NL_KROK = KpDatDoklad.NL_KROK or z.NL_KROK is null)) "+
                      " AND ("+typeUser+" = "+Constants.UCETNI+" "+
                      " OR EXISTS (SELECT NULL FROM DB_JT.KP_DAT_GENEROVANIZAMEK z" +
                                  " WHERE z.ID_CISDOKLAD = 1" +
                                    " AND z.ID_KTGUCETNISPOLECNOST = KpDatDoklad.ID_KTGUCETNISPOLECNOST" +
                                    " AND z.DT_DATUM = KpDatDoklad.DT_DATUM" +
                                    " AND z.C_ZAMCENO = '1'" +
                                    " AND mod(z.ID_CISZAMEK,100) = "+typeUser+"-1"+
                                    " AND (z.NL_KROK = KpDatDoklad.NL_KROK or z.NL_KROK is null))) "+
                      " AND (select c_online from db_jt.kp_ktg_ucetniSpolecnost s where s.ID = KpDatDoklad.ID_KTGUCETNISPOLECNOST) = '1' "
                     );
    vo.setOrderByClause("DT_DATUM");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      dtDatum = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      buf.append("Neschv�len� doklady "+Constants.kym[typeUser]+" pro term�n "+sdf.format(dtDatum.dateValue())+"<br>");
    }
    vo.closeRowSet();
    
    return buf.toString();
  }

  private String checkZamitnuto(Number idSpol, int typeUser)
  {
    StringBuffer buf = new StringBuffer();
    oracle.jbo.domain.Date dtDatum = null;
    Number idDoklad = null;
    
    ViewObject vo = am.findViewObject("KpDatGenzamekzamitnutiView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = " + idSpol +
                      " AND DT_DATUM >= trunc(sysdate,'yyyy')-1"+
                      " AND ID_CISDOKLAD = 1" +
                      " AND mod(ID_CISZAMEK,100) = "+typeUser);
    vo.setOrderByClause("DT_DATUM");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      dtDatum = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      String duvod = (String) row.getAttribute("SDuvod");
      buf.append("<font color='red'>Zam�tnut� doklady "+Constants.kym[typeUser]+" pro term�n "+sdf.format(dtDatum.dateValue())+": "+duvod+"</font><br>");
    }
    vo.closeRowSet();
    
    return buf.toString();
  }

  private void createProtokol(List<Number> idsLocal, int typeUser) throws KisException
  {
    protokol = new StringBuffer();

    getLogHeader();
    getLogTableHeader();
  
    ViewObject voDoklad = am.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    ViewObject voProtokol = am.findViewObject("KpDatDokladprotokolView1");
    voProtokol.clearCache();
    ViewObject voSpol = am.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();
    Iterator iter = idsLocal.iterator(); 
    while(iter.hasNext()) 
    {
      Number idSpol = (Number) iter.next();
      String nazevSpol = null;
      String rkcFlag = null;
      String whereSpol = "ID = " + idSpol;
      if(typeUser == Constants.ALL) 
      {
        whereSpol += " AND ID_CISKATSPOL NOT IN "+Constants.DIS_CISKATSPOL;
      }
      else if(typeUser == Constants.ALL_SPECIFIC) 
      {
        whereSpol += " AND ID_CISKATSPOL IN "+Constants.DIS_CISKATSPOL;
      }
      voSpol.setWhereClause(whereSpol);
      boolean exSpol = false;
      if(voSpol.hasNext()) 
      {
        exSpol = true;
        Row rowSpol = voSpol.next();
        nazevSpol = (String) rowSpol.getAttribute("SNazev");
        if(!"1".equals(rowSpol.getAttribute("COnline"))) {
          nazevSpol += " <div style='color: red'>NE ON-LINE!</div>";
          String duvod = (String) rowSpol.getAttribute("SDuvodoffline");
          if(duvod!=null) nazevSpol+=" ("+duvod+")";
        }
        rkcFlag = (String) rowSpol.getAttribute("CRkc");
      }
      voSpol.closeRowSet();
    
      if(exSpol) {
        String where = "ID_KTGUCETNISPOLECNOST = " + idSpol + 
                       " AND DT_DATUM = TO_DATE('"+ sdf.format(datum) +"','DD.MM.YYYY')"+
                       " AND DT_VYTVORENO > SYSDATE-1/2 AND S_UZIVATEL = '"+Constants.AUTOMAT_USER+"'"+
                       " AND EXISTS (SELECT NULL FROM DB_JT.KP_KTG_DOKLAD KKD WHERE KKD.ID = KpDatDoklad.ID_KTGDOKLAD AND KKD.ID_CISDOKLAD = 1)";
        voDoklad.setWhereClause( where + " AND C_BASEDOKLAD = '1'");
//        voDoklad.setWhereClause( where + " AND NL_KROK = 1");
        boolean addToLog = true;
        if(voDoklad.hasNext()) 
        {
          Row rowDoklad = voDoklad.next();
          Number idDoklad = (Number)rowDoklad.getAttribute("Id");
          oracle.jbo.domain.Date dtVytvoreno = (oracle.jbo.domain.Date) rowDoklad.getAttribute("DtVytvoreno");
          voProtokol.setWhereClause("ID_DOKLAD = "+idDoklad+
                                    " AND C_KOD > '4'");
          //BILANCE
          StringBuffer buf = new StringBuffer("Bilance: ");
          if(!voProtokol.hasNext()) 
          {
            buf.append("OK!<br>");
          }
          while(voProtokol.hasNext()) 
          {
            Row rowProtokol = voProtokol.next();
            String hlaska = (String) rowProtokol.getAttribute("SHlaska");
            if(hlaska!=null && hlaska.indexOf("[NE-")>-1) 
            {
              hlaska = "<span style='color:gray'>" + hlaska + "</span>";
            }
            buf.append(hlaska+"<br>");
          }
          voProtokol.closeRowSet();
          //MAJETKOVE UCASTI
          String muk = "Majetkov� ��asti: ";
          try { 
            muk += ((DokladyModule)am).majetekUcastKontrola(idDoklad.intValue());
          } 
          catch (KisException e) {
            muk += "Chyba vol�n� procedury majetekUcastKontrola!";
          }
          //LAST CLOSE DATE
          String lcd = checkLastCloseDate(idSpol);
          //SCHVALENO
          String schvalenoUcet = checkSchvaleno(idSpol,Constants.UCETNI);
          String schvalenoOO = (typeUser==Constants.UCETNI || typeUser==Constants.NADRIZENA_UCETNI || typeUser==Constants.SKUPINY) ? "" : checkSchvaleno(idSpol,Constants.OO);
          String schvalenoSS = (typeUser==Constants.UCETNI || typeUser==Constants.NADRIZENA_UCETNI || typeUser==Constants.SKUPINY) ? "" : checkSchvaleno(idSpol,Constants.SEF_SEGMENTU);
          schvalenoUcet += checkZamitnuto(idSpol,Constants.UCETNI);
          schvalenoOO += checkZamitnuto(idSpol,Constants.OO);
          schvalenoSS += checkZamitnuto(idSpol,Constants.SEF_SEGMENTU);
          //if(typeUser==Constants.UCETNI && schvalenoUcet.indexOf("Neschv�len� doklady "+Constants.kym[Constants.UCETNI])<0) addToLog = false;
          if(typeUser==Constants.OO && schvalenoOO.indexOf("doklady "+Constants.kym[Constants.OO])<0 
             && schvalenoUcet.indexOf("doklady "+Constants.kym[Constants.UCETNI])<0) addToLog = false;
          if(typeUser==Constants.SEF_SEGMENTU && schvalenoSS.indexOf("doklady "+Constants.kym[Constants.SEF_SEGMENTU])<0 
             && schvalenoOO.indexOf("doklady "+Constants.kym[Constants.OO])<0 
             && schvalenoUcet.indexOf("doklady "+Constants.kym[Constants.UCETNI])<0) addToLog = false;
          if(addToLog) {
            genLogRow(dtVytvoreno,
                      new String[] { buf.toString(), muk, lcd, schvalenoUcet, schvalenoOO, schvalenoSS },
                      "",nazevSpol,idSpol.toString(),idDoklad.toString(),rkcFlag);
          }
        }
        else
        {
          String msg = getFromFronta(idSpol, datum, nazevSpol);
          if(msg != null) {
            genLogErrorRow(msg);
          }
        }
        voDoklad.closeRowSet();
        
        // IFRS zmeny
/*
        voDoklad.setWhereClause( where + " AND NL_KROK = 2");
        if(voDoklad.hasNext()) 
        {
          Row rowDoklad = voDoklad.next();
          Number idDoklad = (Number)rowDoklad.getAttribute("Id");
          oracle.jbo.domain.Date dtVytvoreno = (oracle.jbo.domain.Date) rowDoklad.getAttribute("DtVytvoreno");
          voProtokol.setWhereClause("ID_DOKLAD = "+idDoklad+
                                    " AND C_KOD > '4'");
          //BILANCE IFRS
          StringBuffer buf = new StringBuffer("Bilance: ");
          if(!voProtokol.hasNext()) 
          {
            buf.append("OK!");
          }
          while(voProtokol.hasNext()) 
          {
            Row rowProtokol = voProtokol.next();
            String hlaska = (String) rowProtokol.getAttribute("SHlaska");
            buf.append(hlaska+"<br>");
          }
          voProtokol.closeRowSet();
          if(addToLog) {
            genLogRow(dtVytvoreno,
                      new String[] { buf.toString() },
                      "IFRS",nazevSpol,idSpol.toString(),idDoklad.toString(),rkcFlag);
          }
        }
        voDoklad.closeRowSet();
*/
      }
    }

    getLogTableBottom();
    getLogBottom();
  }

  private void createProtokolMustek(List<Number> idsLocal) 
  {
    protokol = new StringBuffer();

    getLogHeader();
    getLogTableHeader();
  
    ViewObject voDoklad = am.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    ViewObject voProtokol = am.findViewObject("KpDatDokladprotokolView1");
    voProtokol.clearCache();
    voProtokol.setOrderByClause("S_HLASKA");
    ViewObject voSpol = am.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();
    Iterator iter = idsLocal.iterator(); 
    while(iter.hasNext()) 
    {
      Number idSpol = (Number) iter.next();
      String nazevSpol = null;
      String rkcFlag = null;
      String whereSpol = "ID = " + idSpol;
      voSpol.setWhereClause(whereSpol);
      boolean exSpol = false;
      if(voSpol.hasNext()) 
      {
        exSpol = true;
        Row rowSpol = voSpol.next();
        nazevSpol = (String) rowSpol.getAttribute("SNazev");
        if(!"1".equals(rowSpol.getAttribute("COnline"))) {
          nazevSpol += " <div style='color: red'>NE ON-LINE!</div>";
          String duvod = (String) rowSpol.getAttribute("SDuvodoffline");
          if(duvod!=null) nazevSpol+=" ("+duvod+")";
        }
        rkcFlag = (String) rowSpol.getAttribute("CRkc");
      }
      voSpol.closeRowSet();
    
      if(exSpol) {
        String where = "ID_KTGUCETNISPOLECNOST = " + idSpol + 
                       " AND DT_DATUM = TO_DATE('"+ sdf.format(datum) +"','DD.MM.YYYY')"+
                       " AND DT_VYTVORENO > SYSDATE-1/2 AND S_UZIVATEL = '"+Constants.AUTOMAT_USER+"'"+
                       " AND EXISTS (SELECT NULL FROM DB_JT.KP_KTG_DOKLAD KKD WHERE KKD.ID = KpDatDoklad.ID_KTGDOKLAD AND KKD.ID_CISDOKLAD = 1)";
        voDoklad.setWhereClause( where + " AND C_BASEDOKLAD = '1'");
//        voDoklad.setWhereClause( where + " AND NL_KROK = 1");
        boolean t = false;
        boolean addToLog = true;
        if(voDoklad.hasNext()) 
        {
          t=true;
          Row rowDoklad = voDoklad.next();
          Number idDoklad = (Number)rowDoklad.getAttribute("Id");
          oracle.jbo.domain.Date dtVytvoreno = (oracle.jbo.domain.Date) rowDoklad.getAttribute("DtVytvoreno");
          voProtokol.setWhereClause("ID_DOKLAD = "+idDoklad+
                                    " AND C_KOD > '4'"+
                                    " AND lower(S_HLASKA) like '%mustek%'");
          //MUSTKY
          StringBuffer buf = new StringBuffer();
          if(!voProtokol.hasNext()) 
          {
            addToLog=false;
          }
          String lastHlaska = null;
          while(voProtokol.hasNext()) 
          {
            Row rowProtokol = voProtokol.next();
            String hlaska = (String) rowProtokol.getAttribute("SHlaska");
            if(hlaska!=null && !hlaska.equals(lastHlaska))
              buf.append(hlaska+"<br>");
            lastHlaska = hlaska;
          }
          voProtokol.closeRowSet();

          if(addToLog) {
            genLogRow(dtVytvoreno,
                      new String[] { buf.toString() },
                      "",nazevSpol,idSpol.toString(),idDoklad.toString(),rkcFlag);
          }
        }
        voDoklad.closeRowSet();
      }
    }

    getLogTableBottom();
    getLogBottom();
  }

  private void createProtokolUnifUcty(List<Number> idsLocal) 
  {
    protokol = new StringBuffer();

    getLogHeader();
    getLogTableHeader();
  
    ViewObject voDoklad = am.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    ViewObject voProtokol = am.findViewObject("KpDatDokladprotokolView1");
    voProtokol.clearCache();
    voProtokol.setOrderByClause("S_HLASKA");
    ViewObject voSpol = am.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();
    Iterator iter = idsLocal.iterator(); 
    while(iter.hasNext()) 
    {
      Number idSpol = (Number) iter.next();
      String nazevSpol = null;
      String rkcFlag = null;
      String whereSpol = "ID = " + idSpol;
      voSpol.setWhereClause(whereSpol);
      boolean exSpol = false;
      if(voSpol.hasNext()) 
      {
        exSpol = true;
        Row rowSpol = voSpol.next();
        nazevSpol = (String) rowSpol.getAttribute("SNazev");
        if(!"1".equals(rowSpol.getAttribute("COnline"))) {
          nazevSpol += " <div style='color: red'>NE ON-LINE!</div>";
          String duvod = (String) rowSpol.getAttribute("SDuvodoffline");
          if(duvod!=null) nazevSpol+=" ("+duvod+")";
        }
        rkcFlag = (String) rowSpol.getAttribute("CRkc");
      }
      voSpol.closeRowSet();
    
      if(exSpol) {
        String where = "ID_KTGUCETNISPOLECNOST = " + idSpol + 
                       " AND DT_DATUM = TO_DATE('"+ sdf.format(datum) +"','DD.MM.YYYY')"+
                       " AND DT_VYTVORENO > SYSDATE-1/2 AND S_UZIVATEL = '"+Constants.AUTOMAT_USER+"'"+
                       " AND EXISTS (SELECT NULL FROM DB_JT.KP_KTG_DOKLAD KKD WHERE KKD.ID = KpDatDoklad.ID_KTGDOKLAD AND KKD.ID_CISDOKLAD = 1)";
        voDoklad.setWhereClause( where + " AND C_BASEDOKLAD = '1'");
//        voDoklad.setWhereClause( where + " AND NL_KROK = 1");
        boolean t = false;
        boolean addToLog = true;
        if(voDoklad.hasNext()) 
        {
          t=true;
          Row rowDoklad = voDoklad.next();
          Number idDoklad = (Number)rowDoklad.getAttribute("Id");
          oracle.jbo.domain.Date dtVytvoreno = (oracle.jbo.domain.Date) rowDoklad.getAttribute("DtVytvoreno");
          voProtokol.setWhereClause("ID_DOKLAD = "+idDoklad+
                                    " AND lower(S_HLASKA) like '%nenalezen unifikovan�%'");
          //UNIF. UCTY
          StringBuffer buf = new StringBuffer();
          if(!voProtokol.hasNext()) 
          {
            addToLog=false;
          }
          String lastHlaska = null;
          while(voProtokol.hasNext()) 
          {
            Row rowProtokol = voProtokol.next();
            String hlaska = (String) rowProtokol.getAttribute("SHlaska");
            if(hlaska!=null && !hlaska.equals(lastHlaska))
              buf.append(hlaska+"<br>");
            lastHlaska = hlaska;
          }
          voProtokol.closeRowSet();

          if(addToLog) {
            genLogRow(dtVytvoreno,
                      new String[] { buf.toString() },
                      "",nazevSpol,idSpol.toString(),idDoklad.toString(),rkcFlag);
          }
        }
        voDoklad.closeRowSet();
      }
    }

    getLogTableBottom();
    getLogBottom();
  }

  private String getFromFronta(Number idSpol, java.sql.Date datum, String nazevSpol) 
  {
    String ret = "Nebylo mo�no vytvo�it doklad pro spole�nost "+nazevSpol+" ("+idSpol+").";
    String where = "IDSPOLECNOST = " + idSpol + 
                   " AND ADATUM = TO_DATE('"+ sdf.format(datum) +"','DD.MM.YYYY')"+
                   " AND DATUM > SYSDATE-1 AND AUZIVATEL = '"+Constants.AUTOMAT_USER+"' AND AJENOMPS = 0";
    ViewObject voFronta = am.findViewObject("VwKpDokladfrontaView1");
    voFronta.clearCache();
    voFronta.setWhereClause(where);
    if(voFronta.hasNext()) 
    {
      Row rowFronta = voFronta.next();
      String stav = (String) rowFronta.getAttribute("SStav");
      String sqlErr = (String) rowFronta.getAttribute("SSqlerror");
      if(stav != null) 
      {
        if(stav.startsWith("Zam"))
          ret = "Zam�en� doklad pro spole�nost "+nazevSpol+" ("+idSpol+") nen� mo�no p�egenerovat.";
        else if(stav.startsWith("DB"))
          ret = "Datab�zov� chyba : "+sqlErr+" u spole�nosti "+nazevSpol+" ("+idSpol+").";
        else if(stav.startsWith("Exp"))
          ret = "Chyba p�i exportu do excelu u spole�nosti "+nazevSpol+" ("+idSpol+").";
      }
    }
    voFronta.closeRowSet();
    return ret;
  }
  
  private void getLogHeader() 
  {
    protokol.append("<html>\n<head>\n<LINK REL=STYLESHEET TYPE=\"text/css\" HREF=\"https://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/bc4j.css\">\n");
    //protokol.append("<html>\n<head>\n<LINK REL=STYLESHEET TYPE=\"text/css\" HREF=\"bc4j.css\">\n");
    protokol.append("<META HTTP-EQUIV='PRAGMA' CONTENT='NO-CACHE'>\n");
    protokol.append("<meta http-equiv='Content-Type' content='text/html; charset=windows-1250'>\n");
    protokol.append("<script>\n");
    protokol.append("function switchCB() {\n" +
               "cb = document.getElementById('cb');\n" +
               "arr = document.getElementsByTagName('tr');\n" +
               "for(i=0; i<arr.length; i++) {\n" +
               "if(arr[i].name == 'hideMe') {\n" +
               "if(cb.checked == true) { arr[i].style.display = 'block'; } \n" +
               "else { arr[i].style.display = 'none'; }\n" +
               "}}}\n");
    protokol.append("function switchRKC() {\n" +
               "rkc = document.getElementById('rkc');\n" +
               "arr = document.getElementsByTagName('tr');\n" +
               "for(i=0; i<arr.length; i++) {\n" +
               "if(arr[i].id == 'rkc0') {\n" +
               "if(rkc.checked == true) { arr[i].style.display = 'block'; } \n" +
               "else { arr[i].style.display = 'none'; }\n" +
               "}}}\n");
    protokol.append("function switchError() {\n" +
               "err = document.getElementById('error');\n" +
               "arr = document.getElementsByTagName('tr');\n" +
               "for(i=0; i<arr.length; i++) {\n" +
               "if(arr[i].id == 'error') {\n" +
               "if(err.checked == true) { arr[i].style.display = 'block'; } \n" +
               "else { arr[i].style.display = 'none'; }\n" +
               "}}}\n");
    protokol.append("</script>\n");
    protokol.append("</head>\n<body>\n");
  }

  private void getLogTableHeader() 
  {
    String ret = "<form><input type='checkbox' id='cb' checked onClick='javascript:switchCB()'>Zobrazit logy, kter� jsou v po��dku.\n";
    ret += "<input type='checkbox' id='rkc' checked onClick='javascript:switchRKC()'>Zobrazit i ne-RKC spole�nosti.\n";
    ret += "<input type='checkbox' id='error' checked onClick='javascript:switchError()'>Zobrazit i chybov� hl�ky.</form>\n";
    ret += "Generov�n� k "+sdf.format(datum)+"\n";
    ret += "<table class=\"clsTable\">\n"+
           "<tr class=\"clsTableRow\">\n"+
           "<th class=\"clsTableHeader\">Log</th>\n"+
           "<th class=\"clsTableHeader\">Dogenerov�no v</th>\n"+
           "<th class=\"clsTableHeader\">Spole�nost</th>\n"+
           "<th class=\"clsTableHeader\">V�sledek</th>\n"+
           "<th class=\"clsTableHeader\">Id spol.</th>\n"+
           "<th class=\"clsTableHeader\">Detailn� log</th>\n"+
           "</tr>\n";
    protokol.append(ret);
  }

  private void getLogTableBottom() 
  {
    protokol.append("</table>\n");
  }

  private void getLogBottom() 
  {
    protokol.append("</body>\n</html>\n");
  }

  private boolean isOk(String[] sa) 
  {
    if(!sa[0].startsWith("OK")) return false;
    for(int i=1; i<sa.length; i++) 
    {
      if(sa[i].indexOf("uzav�en� ��etn�ho obdob�")>=0) return false;
      if(sa[i].indexOf("Neschv�len� doklad")>=0) return false;
    }
    return true;
  }

  private void genLogRow(oracle.jbo.domain.Date start,
                         String[] warning,
                         String typeDoklad,
                         String nazevSpol,
                         String idSpol,
                         String idDoklad,
                         String rkcFlag)
  {
    logger.debug("PROTOKOL (normal) {}", idDoklad);
    
    String id = "rkc"+rkcFlag;
    
    String trHeader;
    if(isOk(warning)) {
      trHeader = "<tr class='clsLogRowOk' name='hideMe' id='"+id+"'>\n";
    }
    else {
      trHeader = "<tr class='clsLogRowNotOk' id='"+id+"'>\n";
    }
    protokol.append(trHeader);
    protokol.append("<td rowspan="+warning.length+">");
    protokol.append(typeDoklad);
    protokol.append("</td>\n");  
    protokol.append("<td rowspan="+warning.length+">");
    protokol.append(sdfHod.format(new java.util.Date(start.timestampValue().getTime())));
    protokol.append("</td>\n");  
    protokol.append("<td rowspan="+warning.length+">");
    protokol.append(nazevSpol);
    protokol.append("</td>\n");  
    protokol.append("<td>");
    protokol.append(warning[0]);
    protokol.append("</td>\n");  
    protokol.append("<td rowspan="+warning.length+">");
    protokol.append(idSpol);
    protokol.append("</td>\n");  
    protokol.append("<td rowspan="+warning.length+">");
    protokol.append("<a href=\"LogsDokladDetail.jsp?idDoklad="+idDoklad+"\">"+idDoklad+"</a>");
    protokol.append("</td>\n");  
    protokol.append("</tr>\n");
    
    for(int i=1;i<warning.length;i++) 
    {
      protokol.append(trHeader);
      protokol.append("<td>");
      protokol.append(warning[i]);
      protokol.append("</td>\n");  
      protokol.append("</tr>\n");
    }
  }

  private void genLogErrorRow(String errorMsg)
  {
    logger.debug("PROTOKOL (error)");
    
    if(errorMsg.startsWith("Zam")) {
      protokol.append("<tr id='error'>\n");
      protokol.append("<td></td>\n");  
    }
    else {
      protokol.append("<tr class='clsLogRowErr' id='error'>\n");
      protokol.append("<td>Probl�m</td>\n");  
    }
    protokol.append("<td colspan='5'>");
    protokol.append(errorMsg);
    protokol.append("</td>\n");  
    protokol.append("</tr>\n");  
  }

  private SimpleDateFormat sdfName = new SimpleDateFormat("yyyy_MM_dd_HH_mm");

  private String getFileName(Number userId, int typeUser, int idSkupina) 
  {
    String apendix = typeUser==Constants.ALL ? "" : "_"+typeUser;
    String userDir = userId==null ? "" : userId.toString()+"\\";
    if(typeUser==Constants.ALL_SPECIFIC) 
    {
      apendix = "_Specific";
      userDir = "specific\\";
    }
    if(idSkupina>0) apendix = "-Sk"+idSkupina;
    String fileName = Constants.PROTOKOL_FILES_PATH+userDir+"P_"+sdfName.format(startDate)+apendix+".html";
    
    return fileName;
  }

  private String saveLogFile(Number userId, int typeUser, int idSkupina) 
  {
    String fileName = getFileName(userId, typeUser, idSkupina);
    FileOutputStream fos = null;
    try {
      File file = new File(fileName);
      logger.debug("PROTOKOL - ukladani souboru {}", file.getName());
      if ( !file.getParentFile().exists() ) file.getParentFile().mkdirs();
      fos = new FileOutputStream(file);
      fos.write(protokol.toString().getBytes());      
    }
    catch (IOException ex)
    {
      logger.error("Save log file", ex);
      ex.printStackTrace();
    }
    finally 
    {
      try {
        if(fos != null) fos.close();
      }
      catch(IOException ioe) {}
    }
    return fileName;
  }

  Comparator c = new Comparator() 
  {
    public int compare(Object obj1, Object obj2) 
    {
      String o1 = (String) obj1;
      String o2 = (String) obj2;
      String t1 = o1.substring(o1.indexOf('(')+1);
      String t2 = o2.substring(o2.indexOf('(')+1);
      return t1.compareTo(t2);
    }
  };

  public void checkSchvalovak(ApplicationModule am) 
  {
    if(this.am == null) //{logger.debug("! am = NULL");
			this.am = am;
    
	//if(this.am == null) {logger.debug("!!! am = NULL");							this.am = am;}

	checkSchvalovak();
  }

  public void checkOther(ApplicationModule am) 
  {
    if(this.am == null) this.am = am;
    checkEviSpolecnostOR3M();
    checkSpolecnostZamekGen();
    checkSpolecnostSkupina4();
    checkSpolecnostiSoulad();
    checkZamekVSClose();
    checkZamekExterni();
    checkZmenyProtistran();
    checkBudgetMustekNaklad();
    checkBudgetSchvalovani();
  }
  
  //esc2010 pre rucne spustenie v pripade chyby OALL8 is in an inconsistent state
  // nahradne riesenie  nazov je iba prebraty , kontroluje hlavne Odbory
  public void checkBudgetprojekt(ApplicationModule am) 
  {
  System.gc();
    if(this.am == null) this.am = am;
   try {
      SystemStatus.getInstance().setStatus("manual- Kontrola budget� Denni start ");
      logger.debug("AutoProtokolNew MAN- kontrola budgetu Denni");      
      checkBudgetDenni(); System.gc();
//12/2010      logger.debug("PROTOKOL MAN- kontrola budgetu Mesicni");
//12/2010      checkBudgetMesicni();
//12/2010      logger.debug("PROTOKOL MAN- kontrola budgetu Schvalovani");
//12/2010      checkBudgetSchvalovani();
      SystemStatus.getInstance().setStatus("manual- Kontrola budget� Denni konec");
      logger.debug("PROTOKOL MAN- Dokoncena kontrola budgetu");
System.gc();
    } 
    catch(Throwable t) 
    {
      logger.debug("checkBudgetprojekt (budgety) CHYBA!");
      logger.error("checkBudgetprojekt (budgety) CHYBA!",t);
      t.printStackTrace();
      System.gc();
    }
   
      
    try {
      SystemStatus.getInstance().setStatus("manual - Kontrola zpr�v karet projekt�");
      checkProjektMemorandum();
      logger.debug("PROTOKOL MAN- Dokoncena kontrola informacnich zprav karet projektu");

      SystemStatus.getInstance().setStatus("Kontrola p�ecen�n� projekt�");
      checkProjektPreceneni();
      logger.debug("PROTOKOL MAN- Dokoncena kontrola preceneni projektu");
    } 
    catch(Throwable t) 
    {
      logger.debug("checkBudgetprojekt (projekty) CHYBA!");
      logger.error("checkBudgetprojekt (projekty) CHYBA!",t);
      t.printStackTrace();
    }

    try {
      SystemStatus.getInstance().setStatus("manual - Kontrola zpr�v karet SPV");
      checkSpolecnostZprava();
      logger.debug("PROTOKOL MAN - Dokoncena kontrola zprav karet SPV");
    } 
    catch(Throwable t) 
    {
      logger.debug("checkBudgetprojekt (karty SPV) CHYBA!");
      logger.error("checkBudgetprojekt (karty SPV) CHYBA!",t);
      t.printStackTrace();
    }
  }
  //esc2010 pre rucne spustenie v pripade chyby OALL8 is in an inconsistent state
  
  
  private boolean jePodrizenySS(Number osoba, Number boss) 
  {
    boolean ret = false;
    String sqlStr = null;
    try {
      sqlStr = "select 1 from db_jt.kp_ktg_ucetniSpolecnost s where s.ID_MNGSEGMENTBOSS = "+boss+" and s.ID_ODPOVEDNAOSOBA = "+osoba+" union "+
               "select 2 from db_jt.kp_ktg_projekt p where p.ID_MNGSEGMENTBOSS = "+boss+" and p.ID_PMANAGER = "+osoba+" union "+
               "select 3 from db_jt.kp_ktg_odbor o where o.ID_MNGSEGMENT in (select b.ID_MNGSEGMENT from db_jt.kp_dat_mngSegmentBoss b where b.id_boss = "+boss+" and sysdate between b.DT_PLATNOSTOD and b.DT_PLATNOSTDO) and o.ID_ODPOVEDNAOSOBA = "+osoba;
      ViewObject vo = am.createViewObjectFromQueryStmt("QueryDemo", sqlStr);
      vo.clearCache();
      ret = vo.hasNext();
      vo.closeRowSet();
      vo.remove();
    }
    catch(Exception e) 
    {
      logger.error("Tak on-line select nefachci :-( {}", sqlStr, e);
    }
    return ret;
  }

  private void checkSchvalovak()
  {
    if(!isEnabledEmailByDay()) return;
logger.debug("Mail check Schvalovak");	
	Mail mail = getMail();
    //ZADAVATELE

    Set setZad = new HashSet();
    ViewObject voZad = am.findViewObject("VwDatSchvalovakView1");
    voZad.clearCache();

    voZad.setWhereClause("ID_ZADAVATEL <> ID_APPUSER AND ID_CISSTATUS=0"+
                         "AND NOT EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT)");

    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idZad = (Number) rowZad.getAttribute("IdZadavatel");
      setZad.add(idZad);
    }


    voZad.closeRowSet();
	logger.debug("checkSchvalovak - mail.sendDocZadavatel");	
  mail.sendDocZadavatel(am, setZad);
  
    //ZAKLAD
    Set setIds = new HashSet();
    ViewObject voRadek = am.findViewObject("VwDatSchvalovakradekView1");


    voRadek.clearCache();
    voRadek.setWhereClause("ID_CISSTATUS=0 AND DT_ZAMITNUTO IS NULL AND C_DOCZAMITNUTO <> '1'");

//SCHVALOVATELE  - RADEK
logger.debug("checkSchvalovak - SCHVALOVATELE  - RADEK");	    
    while(voRadek.hasNext()) 
    {
      Row rowRadek = voRadek.next();
      int doc = ((Number) rowRadek.getAttribute("IdCisdokument")).intValue();
      oracle.jbo.domain.Date dtOdborOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoodbor");
      oracle.jbo.domain.Date dtProjektOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoprojekt");
      oracle.jbo.domain.Date dtUcspolOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoucspol");
      oracle.jbo.domain.Date dtTopOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenotop");
      Number odborOO = (Number) rowRadek.getAttribute("IdOdboroo");
      Number projektMan = (Number) rowRadek.getAttribute("IdProjektman");
      Number ucspolOO = (Number) rowRadek.getAttribute("IdUcspoloo");
      Number ucspolTop = (Number) rowRadek.getAttribute("IdUcspoltop");

      if(dtOdborOO==null && odborOO!=null) {
        setIds.add(odborOO);
      }
      if(dtProjektOO==null && projektMan!=null) {
        setIds.add(projektMan);
      }
      if(dtUcspolOO==null && ucspolOO!=null) {
        setIds.add(ucspolOO);
      }
      if(dtTopOO==null && ucspolTop!=null) {
        if(doc!=4 || ((dtOdborOO!=null || odborOO==null) && (dtProjektOO!=null || projektMan==null) && (dtUcspolOO!=null || ucspolOO==null)))
          setIds.add(ucspolTop);
      }
    }

 
    voRadek.closeRowSet();
logger.debug("checkSchvalovak - SCHVALOVATELE  - RADEK close");	    
//GESTOR  - esc 03.10.2016  nepotrebujeme , navyse to slo dlho
/*    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT AND DT_ZAMITNUTO IS NULL) AND DT_GESTORZAMITNUTO IS NULL AND DT_ZADAVATELZAMITNUTO IS NULL AND DT_UCETNIZAMITNUTO IS NULL");


    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      oracle.jbo.domain.Date dtGestor = (oracle.jbo.domain.Date) rowZad.getAttribute("DtGestorschvaleno");
      Number gestor = (Number) rowZad.getAttribute("IdGestor");

      if(dtGestor==null && gestor!=null) {
        setIds.add(gestor);
      }
    }
    voZad.closeRowSet();
*/
//GESTOR END
logger.debug("checkSchvalovak - sendDocToDo");	    
	mail.sendDocToDo(am, setIds);
    
//Typizovane SL
logger.debug("Mail TYPIZOVANE  START"); 
    int docDniT = mail.getPocetDni(am,10);
    //Set setNamesT = new TreeSet(c);
     Set<Number> setNamesT = new HashSet<>();
     @SuppressWarnings("unchecked")
     Map<Number, HashMap> mapSLT = new HashMap<>();
    //voRadek =   VwDatSchvalovakradekView1
    voRadek.setWhereClause("ID_CISSTATUS=2 AND ID_CISDOKUMENT =1 AND DT_ZAMITNUTO IS NULL AND C_DOCZAMITNUTO <> '1' "+
							//" AND "+
		 
							 //zaujima nas typizovany SL, ktory nebol schvalovany dodatocne koli zmenam							
							" and dt_schvalenoodbor is null and  dt_schvalenoprojekt is null and  dt_schvalenotop is null and  dt_schvalenoucspol is null and  dt_gestorschvaleno is null "+
							//" dt_datumzadaniradek > sysdate-"+docDniT+
//esc 17.9.2009 			" and dt_datumzadani > sysdate-"+docDniT+
							" and dt_datumzadani > sysdate-1"+
							" AND ID_TYPIZLINK is NOT NULL ");        
                                                       //" AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENT D WHERE VwDatSchvalovakRadek.ID_DOKUMENT = D.ID_TYPIZLINK)");        
    
    String sWHERE = voRadek.getWhereClause();
    voRadek.setOrderByClause("ID_DOKUMENT");
    while(voRadek.hasNext()) 
      {
        Row rowRadek = voRadek.next();
        oracle.jbo.domain.Date dtOdborOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoodbor");
        oracle.jbo.domain.Date dtProjektOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoprojekt");
        oracle.jbo.domain.Date dtUcspolOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoucspol");
        oracle.jbo.domain.Date dtTopOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenotop");
        Number odborOO = (Number) rowRadek.getAttribute("IdOdboroo");
        Number projektMan = (Number) rowRadek.getAttribute("IdProjektman");
        Number ucspolOO = (Number) rowRadek.getAttribute("IdUcspoloo");
        Number ucspolTop = (Number) rowRadek.getAttribute("IdUcspoltop");
        String nOdborOO = (String) rowRadek.getAttribute("Odboroo"); 
        String nProjektMan = (String) rowRadek.getAttribute("Projektman"); 
        String nUcspolOO = (String) rowRadek.getAttribute("Ucspoloo"); 
        String nUcspolTop = (String) rowRadek.getAttribute("Ucspoltop"); 
        Number idDoc = (Number) rowRadek.getAttribute("IdDokument");

        oracle.jbo.domain.Date dtGestor = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtGestorschvaleno");
        Number gestorId = (Number) rowRadek.getAttribute("IdGestor");
        String nGestor = (String) rowRadek.getAttribute("Gestor");
        
        if(dtGestor!=null && gestorId!=null) {
                  if(gestorId.intValue()>0) {
            //setNamesT.add(gestorId); //            addSLToNameSet(mapSLT, gestorId, idDoc);
             SchvalovakData.addSchvalovakRadek( (HashMap) mapSLT, gestorId, rowRadek);
                    }
        }
        
        //if(dtOdborOO!=null && odborOO!=null) {
         if(odborOO!=null) {
          if(odborOO.intValue()>0) {
            //setNamesT.add(odborOO);//                                  addSLToNameSet(mapSLT, odborOO, idDoc);
             SchvalovakData.addSchvalovakRadek((HashMap)mapSLT, odborOO, rowRadek);
          }
        }
        //if(dtProjektOO!=null && projektMan!=null) {
         if(projektMan!=null) {
          if(projektMan.intValue()>0) {
            //setNamesT.add(projektMan);//                      addSLToNameSet(mapSLT, projektMan, idDoc);             addSLToNameObj(mapSLT, projektMan, idDoc);          
             SchvalovakData.addSchvalovakRadek((HashMap)mapSLT, projektMan, rowRadek);
          }
        }
        //if(dtUcspolOO!=null && ucspolOO!=null) {
         if(ucspolOO!=null) {
          if(ucspolOO.intValue()>0) {
            //setNamesT.add(ucspolOO);//                      addSLToNameSet(mapSLT, ucspolOO, idDoc);             addSLToNameObj(mapSLT, ucspolOO, idDoc);          
             SchvalovakData.addSchvalovakRadek((HashMap)mapSLT, ucspolOO, rowRadek);
          }
        }
        //if(dtTopOO!=null && ucspolTop!=null) {
         if(ucspolTop!=null) {
          if(ucspolTop.intValue()>0) {
            //setNamesT.add(ucspolTop);//                   addSLToNameSet(mapSLT, ucspolTop, idDoc);             addSLToNameObj(mapSLT, ucspolTop, idDoc);          
             SchvalovakData.addSchvalovakRadek((HashMap)mapSLT, ucspolTop, rowRadek);            
          }
        }
      }
      voRadek.closeRowSet();
/*  excel netrbea, lebo nemozeme poslat vsetkym zoznam cudzich SL
      String cestaT = null;
      if(datum == null)       datum = new java.sql.Date(System.currentTimeMillis());
      logger.info("Export pouzite Typizovane SL datum {}", datum);
      ESExportSlTypizovanePouzite et = new ESExportSlTypizovanePouzite(am ,datum);
      try 
      {
        et.excelOutput();
        cestaT = et.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        logger.error("ERROR: Export Typizovane SL", e);
        e.printStackTrace(); 
      }                   

            mail.sendDocTypiz(am, setNamesT,cestaT);
*/            
        mail.sendDocTypizUsed(am, (HashMap)mapSLT);
         
      logger.debug("Mail TYPIZOVANE  KONEC");


//Typizovane SL end

    int docDni;
    //KONTROLING
    docDni = mail.getPocetDni(am,10);
    Set<Number> setKontr = new HashSet<>();

    Set<Number> setNames = new TreeSet<>(c);
    @SuppressWarnings("unchecked")
    Map<Number, HashMap> mapSL = new HashMap<>();
    
    voRadek.clearCache();
    voRadek.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT IN (1,3) AND DT_ZAMITNUTO IS NULL AND C_DOCZAMITNUTO <> '1' AND dt_datumzadaniradek < sysdate-"+docDni);
    voRadek.setOrderByClause("ID_DOKUMENT");
    while(voRadek.hasNext()) 
    {
      Row rowRadek = voRadek.next();
      oracle.jbo.domain.Date dtOdborOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoodbor");
      oracle.jbo.domain.Date dtProjektOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoprojekt");
      oracle.jbo.domain.Date dtUcspolOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoucspol");
      oracle.jbo.domain.Date dtTopOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenotop");
      Number odborOO = (Number) rowRadek.getAttribute("IdOdboroo");
      Number projektMan = (Number) rowRadek.getAttribute("IdProjektman");
      Number ucspolOO = (Number) rowRadek.getAttribute("IdUcspoloo");
      Number ucspolTop = (Number) rowRadek.getAttribute("IdUcspoltop");
      String nOdborOO = (String) rowRadek.getAttribute("Odboroo"); 
      String nProjektMan = (String) rowRadek.getAttribute("Projektman"); 
      String nUcspolOO = (String) rowRadek.getAttribute("Ucspoloo"); 
      String nUcspolTop = (String) rowRadek.getAttribute("Ucspoltop"); 
      Number idDoc = (Number) rowRadek.getAttribute("IdDokument");

      oracle.jbo.domain.Date dtGestor = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtGestorschvaleno");
      Number gestorId = (Number) rowRadek.getAttribute("IdGestor");
      String nGestor = (String) rowRadek.getAttribute("Gestor");
      if(dtGestor==null && gestorId!=null) {
        if(gestorId.intValue()>0) {
          setNames.add(nGestor);
          addSLToName(mapSL, nGestor, idDoc);
        }
      }
      
      if(dtOdborOO==null && odborOO!=null) {
        if(odborOO.intValue()>0) {
          setNames.add(nOdborOO);
          addSLToName(mapSL, nOdborOO, idDoc);
        }
      }
      if(dtProjektOO==null && projektMan!=null) {
        if(projektMan.intValue()>0) {
          setNames.add(nProjektMan);
          addSLToName(mapSL, nProjektMan, idDoc);
        }
      }
      if(dtUcspolOO==null && ucspolOO!=null) {
        if(ucspolOO.intValue()>0) {
          setNames.add(nUcspolOO);
          addSLToName(mapSL, nUcspolOO, idDoc);
        }
      }
      if(dtTopOO==null && ucspolTop!=null) {
        if(ucspolTop.intValue()>0) {
          setNames.add(nUcspolTop);
          addSLToName(mapSL, nUcspolTop, idDoc);
        }
      }
    }
    voRadek.closeRowSet();

    Set<Number> setNamesBud = new TreeSet<>(c);
    @SuppressWarnings("unchecked")
    Map<Number, HashMap> mapSLBud = new HashMap<>();
    
    voRadek.clearCache();
    voRadek.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT = 4 AND DT_ZAMITNUTO IS NULL AND C_DOCZAMITNUTO <> '1' AND dt_datumzadaniradek < sysdate-"+docDni);
    voRadek.setOrderByClause("ID_DOKUMENT");
    while(voRadek.hasNext()) 
    {
      Row rowRadek = voRadek.next();
      oracle.jbo.domain.Date dtOdborOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoodbor");
      oracle.jbo.domain.Date dtProjektOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoprojekt");
      oracle.jbo.domain.Date dtUcspolOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoucspol");
      oracle.jbo.domain.Date dtTopOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenotop");
      Number odborOO = (Number) rowRadek.getAttribute("IdOdboroo");
      Number projektMan = (Number) rowRadek.getAttribute("IdProjektman");
      Number ucspolOO = (Number) rowRadek.getAttribute("IdUcspoloo");
      Number ucspolTop = (Number) rowRadek.getAttribute("IdUcspoltop");
      String nOdborOO = (String) rowRadek.getAttribute("Odboroo"); 
      String nProjektMan = (String) rowRadek.getAttribute("Projektman"); 
      String nUcspolOO = (String) rowRadek.getAttribute("Ucspoloo"); 
      String nUcspolTop = (String) rowRadek.getAttribute("Ucspoltop"); 
      Number idDoc = (Number) rowRadek.getAttribute("IdDokument");

      oracle.jbo.domain.Date dtGestor = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtGestorschvaleno");
      Number gestorId = (Number) rowRadek.getAttribute("IdGestor");
      String nGestor = (String) rowRadek.getAttribute("Gestor");
      if(dtGestor==null && gestorId!=null) {
        if(gestorId.intValue()>0) {
          setNamesBud.add(nGestor);
          addSLToName(mapSLBud, nGestor, idDoc);
        }
      }
      
      if(dtOdborOO==null && odborOO!=null) {
        if(odborOO.intValue()>0) {
          setNamesBud.add(nOdborOO);
          addSLToName(mapSLBud, nOdborOO, idDoc);
        }
      }
      if(dtProjektOO==null && projektMan!=null) {
        if(projektMan.intValue()>0) {
          setNamesBud.add(nProjektMan);
          addSLToName(mapSLBud, nProjektMan, idDoc);
        }
      }
      if(dtUcspolOO==null && ucspolOO!=null) {
        if(ucspolOO.intValue()>0) {
          setNamesBud.add(nUcspolOO);
          addSLToName(mapSLBud, nUcspolOO, idDoc);
        }
      }
      if(dtTopOO==null && ucspolTop!=null) {
        if(ucspolTop.intValue()>0) {
          setNamesBud.add(nUcspolTop);
          addSLToName(mapSLBud, nUcspolTop, idDoc);
        }
      }
    }
    voRadek.closeRowSet();

    ViewObject voKontr = am.findViewObject("KpKtgEmailzpravyView1");
    voKontr.clearCache();
    voKontr.setWhereClause("ID_MSGTYPE = 5");
    while(voKontr.hasNext()) 
    {
      Row rowKontr = voKontr.next();
      Number userId = (Number) rowKontr.getAttribute("IdKtgappuser");
      setKontr.add(userId);
    }
    voKontr.closeRowSet();

    String cesta = null;
if(datum == null)	datum = new java.sql.Date(System.currentTimeMillis());
datumDnes = new java.sql.Date(System.currentTimeMillis());
logger.info("Export postup SL datum {}", datumDnes);
    ESExportSLPostup ep = new ESExportSLPostup(am ,datumDnes);
    try 
    {
      ep.excelOutput();
      cesta = ep.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("ERROR: Export postup SL", e);
      e.printStackTrace(); //pro zacatek
    }
///esc 11 2009
//esc      
 logger.info("Export SL Hlava datum {}", datumDnes);
    String cesta3 = null;
     //ESExportSLHlava ep3 = new ESExportSLHlava(am ,datum);
     ESExportSLHlava ep3 = new ESExportSLHlava(am ,datumDnes);
     try 
     {
       ep3.excelOutput();
       cesta3 = ep3.getFileAbsoluteName();
     }
     catch(Exception e) 
     {
       logger.error("ERROR: Export SL Hlava cesta3: {}", cesta3, e);
       e.printStackTrace(); //pro zacatek
     }

logger.info("Export hlava SL DETAIL datum {}", datumDnes);
    String cesta2 = null;
    ESExportSLPostupDetail ep2 = new ESExportSLPostupDetail(am, datumDnes);
    try {
            ep2.excelOutput();
            cesta2 = ep2.getFileAbsoluteName();
         }
         catch (Exception e) 
         {
               logger.error("ERROR: Export hlava SL DETAIL SL cesta2: {}", cesta2, e);
               e.printStackTrace(); //pro zacatek
          }

           //esc

        //KONTROLING
    if(ep.getFileName()!=null)
      //mail.sendDocKontr(am, setKontr, setNames, mapSL, setNamesBud, mapSLBud, cesta);
      mail.sendDocKontr(am, setKontr, setNames, mapSL, setNamesBud, mapSLBud, cesta,  cesta2 , cesta3 );
///esc 11 2009
    //TOP
    docDni = mail.getPocetDni(am,11);
    @SuppressWarnings("unchecked")
    Map<Number, HashMap> mapTop = new HashMap<>();
    
    voRadek.clearCache();
    voRadek.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT IN (1,3,4) AND DT_ZAMITNUTO IS NULL AND C_DOCZAMITNUTO <> '1' AND dt_datumzadaniradek < sysdate-"+docDni);
    voRadek.setOrderByClause("ID_DOKUMENT");
    while(voRadek.hasNext()) 
    {
      Row rowRadek = voRadek.next();
      oracle.jbo.domain.Date dtOdborOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoodbor");
      oracle.jbo.domain.Date dtProjektOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoprojekt");
      oracle.jbo.domain.Date dtUcspolOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoucspol");
      oracle.jbo.domain.Date dtTopOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenotop");
      Number odborOO = (Number) rowRadek.getAttribute("IdOdboroo");
      Number projektMan = (Number) rowRadek.getAttribute("IdProjektman");
      Number ucspolOO = (Number) rowRadek.getAttribute("IdUcspoloo");
      Number ucspolTop = (Number) rowRadek.getAttribute("IdUcspoltop");
      String nOdborOO = (String) rowRadek.getAttribute("Odboroo"); 
      String nProjektMan = (String) rowRadek.getAttribute("Projektman"); 
      String nUcspolOO = (String) rowRadek.getAttribute("Ucspoloo"); 
      String nUcspolTop = (String) rowRadek.getAttribute("Ucspoltop"); 
      Number top = (Number) rowRadek.getAttribute("IdTop");
      Number idDoc = (Number) rowRadek.getAttribute("IdDokument");

      oracle.jbo.domain.Date dtGestor = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtGestorschvaleno");
      Number gestorId = (Number) rowRadek.getAttribute("IdGestor");
      String nGestor = (String) rowRadek.getAttribute("Gestor");
      if(dtGestor==null && gestorId!=null) {
        if(gestorId.intValue()>0 && jePodrizenySS(gestorId,top)) {
          addToTop(mapTop,top,nGestor,idDoc);
        }
      }
      
      if(dtOdborOO==null && odborOO!=null) {
        if(odborOO.intValue()>0 && jePodrizenySS(odborOO,top)) {
          addToTop(mapTop,top,nOdborOO,idDoc);
        }
      }
      if(dtProjektOO==null && projektMan!=null) {
        if(projektMan.intValue()>0 && jePodrizenySS(projektMan,top)) {
          addToTop(mapTop,top,nProjektMan,idDoc);
        }
      }
      if(dtUcspolOO==null && ucspolOO!=null) {
        if(ucspolOO.intValue()>0 && jePodrizenySS(ucspolOO,top)) {
          addToTop(mapTop,top,nUcspolOO,idDoc);
        }
      }
      if(dtTopOO==null && ucspolTop!=null) {
        if(ucspolTop.intValue()>0 && jePodrizenySS(ucspolTop,top)) {
          addToTop(mapTop,top,nUcspolTop,idDoc);
        }
      }
    }
    voRadek.closeRowSet();
    mail.sendDocTop(am, mapTop);

    //TOP ALL
    docDni = mail.getPocetDni(am,12);
    Set<Number> setTop = new HashSet<>();
    setNames = new TreeSet<>(c);
    @SuppressWarnings("unchecked")
    mapSL = new HashMap<>();
    
    voRadek.clearCache();
    voRadek.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT IN (1,3,4) AND DT_ZAMITNUTO IS NULL AND C_DOCZAMITNUTO <> '1' AND dt_datumzadaniradek < sysdate-"+docDni);
    voRadek.setOrderByClause("ID_DOKUMENT");
    while(voRadek.hasNext()) 
    {
      Row rowRadek = voRadek.next();
      oracle.jbo.domain.Date dtOdborOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoodbor");
      oracle.jbo.domain.Date dtProjektOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoprojekt");
      oracle.jbo.domain.Date dtUcspolOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenoucspol");
      oracle.jbo.domain.Date dtTopOO = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtSchvalenotop");
      Number odborOO = (Number) rowRadek.getAttribute("IdOdboroo");
      Number projektMan = (Number) rowRadek.getAttribute("IdProjektman");
      Number ucspolOO = (Number) rowRadek.getAttribute("IdUcspoloo");
      Number ucspolTop = (Number) rowRadek.getAttribute("IdUcspoltop");
      String nOdborOO = (String) rowRadek.getAttribute("Odboroo"); 
      String nProjektMan = (String) rowRadek.getAttribute("Projektman"); 
      String nUcspolOO = (String) rowRadek.getAttribute("Ucspoloo"); 
      String nUcspolTop = (String) rowRadek.getAttribute("Ucspoltop"); 
      Number idDoc = (Number) rowRadek.getAttribute("IdDokument");

      oracle.jbo.domain.Date dtGestor = (oracle.jbo.domain.Date) rowRadek.getAttribute("DtGestorschvaleno");
      Number gestorId = (Number) rowRadek.getAttribute("IdGestor");
      String nGestor = (String) rowRadek.getAttribute("Gestor");
      if(dtGestor==null && gestorId!=null) {
        if(gestorId.intValue()>0) {
          setNames.add(nGestor);
          addSLToName(mapSL, nGestor, idDoc);
        }
      }
      
      if(dtOdborOO==null && odborOO!=null) {
        if(odborOO.intValue()>0) 
        {
          setNames.add(nOdborOO);
          addSLToName(mapSL, nOdborOO, idDoc);
        }
      }
      if(dtProjektOO==null && projektMan!=null) {
        if(projektMan.intValue()>0) {
          setNames.add(nProjektMan);
          addSLToName(mapSL, nProjektMan, idDoc);
        }
      }
      if(dtUcspolOO==null && ucspolOO!=null) {
        if(ucspolOO.intValue()>0) {
          setNames.add(nUcspolOO);
          addSLToName(mapSL, nUcspolOO, idDoc);
        }
      }
      if(dtTopOO==null && ucspolTop!=null) {
        if(ucspolTop.intValue()>0) {
          setNames.add(nUcspolTop);
          addSLToName(mapSL, nUcspolTop, idDoc);
        }
      }
    }
    voRadek.closeRowSet();
    
    ViewObject voTopAll = am.findViewObject("KpDatMngsegmentbossView1");
    voTopAll.clearCache();
    voTopAll.setWhereClause("SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(voTopAll.hasNext()) 
    {
      Row rowTopAll = voTopAll.next();
      Number userId = (Number) rowTopAll.getAttribute("IdBoss");
      setTop.add(userId);
    }
    voTopAll.closeRowSet();
    
    mail.sendDocTopAll(am, setTop, setNames, mapSL);

    //KONTROLING NEDOKONCENE A NESCHVALENE
    docDni = mail.getPocetDni(am,15);
    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapZadNe = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=1 AND NOT EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT)"+
                         " AND DT_GESTORZAMITNUTO IS NULL AND DT_ZADAVATELZAMITNUTO IS NULL AND DT_UCETNIZAMITNUTO IS NULL AND dt_datumzadani < sysdate-"+docDni);
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      Number idZad = (Number) rowZad.getAttribute("IdZadavatel");
      String zad = (String) rowZad.getAttribute("Zadavatel");
      if(idZad==null) zad = (String) rowZad.getAttribute("Appuser");
      addSLToName(mapZadNe, zad, idDoc);
    }
    voZad.closeRowSet();

    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapZadMis = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=3 AND NOT EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT)"+
                         " AND DT_GESTORZAMITNUTO IS NULL AND DT_ZADAVATELZAMITNUTO IS NULL AND DT_UCETNIZAMITNUTO IS NULL AND dt_datumzadani < sysdate-"+docDni);
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      String zad = (String) rowZad.getAttribute("Zadavatel");
      addSLToName(mapZadMis, zad, idDoc);
    }
    voZad.closeRowSet();

    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapZadBud = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=4 AND NOT EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT)"+
                         " AND DT_GESTORZAMITNUTO IS NULL AND DT_ZADAVATELZAMITNUTO IS NULL AND DT_UCETNIZAMITNUTO IS NULL AND dt_datumzadani < sysdate-"+docDni);
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      String zad = (String) rowZad.getAttribute("Zadavatel");
      addSLToName(mapZadBud, zad, idDoc);
    }
    voZad.closeRowSet();

    docDni = mail.getPocetDni(am,21);
    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapZadNeschval = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=1 AND DT_GESTORZAMITNUTO IS NULL AND DT_ZADAVATELZAMITNUTO IS NULL AND DT_UCETNIZAMITNUTO IS NULL"+
                    " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT AND DT_ZAMITNUTO < sysdate-"+docDni+")");
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      String zad = (String) rowZad.getAttribute("Zadavatel");
      addSLToName(mapZadNeschval, zad, idDoc);
    }
    voZad.closeRowSet();

    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapZadMisNeschval = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=3 AND DT_GESTORZAMITNUTO IS NULL AND DT_ZADAVATELZAMITNUTO IS NULL AND DT_UCETNIZAMITNUTO IS NULL"+
                    " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT AND DT_ZAMITNUTO < sysdate-"+docDni+")");
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      String zad = (String) rowZad.getAttribute("Zadavatel");
      addSLToName(mapZadMisNeschval, zad, idDoc);
    }
    voZad.closeRowSet();

    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapZadBudNeschval = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=4 AND DT_GESTORZAMITNUTO IS NULL AND DT_ZADAVATELZAMITNUTO IS NULL AND DT_UCETNIZAMITNUTO IS NULL"+
                    " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK r WHERE VwDatSchvalovak.ID = R.ID_DOKUMENT AND DT_ZAMITNUTO < sysdate-"+docDni+")");
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      String zad = (String) rowZad.getAttribute("Zadavatel");
      addSLToName(mapZadBudNeschval, zad, idDoc);
    }
    voZad.closeRowSet();
//--
    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapGesNeschval = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=1 AND DT_GESTORZAMITNUTO < sysdate-2");
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      String ges = (String) rowZad.getAttribute("Gestor");
      addSLToName(mapGesNeschval, ges, idDoc);
    }
    voZad.closeRowSet();

    @SuppressWarnings("unchecked")
    Map<String, HashMap> mapUctoNeschval = new HashMap<>();
    voZad.clearCache();
    voZad.setWhereClause("ID_CISSTATUS=0 AND ID_CISDOKUMENT=1 AND DT_UCETNIZAMITNUTO < sysdate-2");
    while(voZad.hasNext()) 
    {
      Row rowZad = voZad.next();
      Number idDoc = (Number) rowZad.getAttribute("Id");
      String zad = (String) rowZad.getAttribute("Zadavatel");
      addSLToName(mapUctoNeschval, zad, idDoc);
    }
    voZad.closeRowSet();

    mail.sendDocKontrNedokoncene(am, setKontr, mapZadNe, mapZadNeschval, mapZadMis, mapZadMisNeschval, mapGesNeschval, mapZadBud, mapZadBudNeschval, mapUctoNeschval);
  }
  
  private void addToTop(Map map, Number top, String name, Number sl) 
  {
    if(map.containsKey(top)) 
    {
      Map sm = (Map)map.get(top);
      if(name!=null)
        addSLToName(sm,name,sl);
    }
    else
    {
      Map sm = new TreeMap(c);
      if(name!=null)
        addSLToName(sm,name,sl);
      map.put(top,sm);
    }
  }

    private void addSLToNameSet(Map map, Number UserID, Number sl) 
    {
      if(map.containsKey(UserID)) 
      {
         Set setSL = (Set)map.get(UserID);
         if(sl!=null && UserID!=null )                 
         setSL.add(sl);
      }
      else 
      {
        Set setSL = new HashSet();
        if(sl!=null && UserID!=null)
            setSL.add(sl);
        if(UserID!=null) map.put(UserID,setSL);
      }
    }

  private void addSLToName(Map map, String name, Number sl) 
  {
    if(map.containsKey(name)) 
    {
      StringBuffer sb = (StringBuffer)map.get(name);
      if(sl!=null && name!=null && sb.indexOf(sl.toString())<0)
        sb.append(", "+sl.toString());
    }
    else 
    {
      StringBuffer sb = new StringBuffer();
      if(sl!=null && name!=null)
        sb.append(sl.toString());
      if(name!=null) map.put(name,sb);
    }
  }
  
  private Comparator cc = new Comparator() 
  {
    public int compare(Object o1, Object o2) 
    {
      Object[] sa1 = (Object[]) o1;
      Object[] sa2 = (Object[]) o2;
      int comp = ((String)sa1[0]).compareTo((String)sa2[0]);
      if(comp!=0) return comp;
      else return ((String)sa1[1]).compareTo((String)sa2[1]);
    }
  };

  private Number getZastupBudget(Number userId, int idBud) {
    Number zastup = null;

    ViewObject voZastup = am.findViewObject("KpDatBudgetzastupView1");
    voZastup.clearCache();
    voZastup.setWhereClause("ID_KOHO = "+userId + " AND SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO AND C_FWDEMAIL = '1' AND (ID_BUDGET IS NULL OR ID_BUDGET = "+idBud+")");
//esc 25.07.2012
logger.debug("CheckBudget-getZastupBudget : ID_KOHO = {} AND SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO AND C_FWDEMAIL = '1' AND (ID_BUDGET IS NULL OR ID_BUDGET = {})", userId, idBud);
    while(voZastup.hasNext()) 
    {
      Row row = voZastup.next();
      zastup = (Number) row.getAttribute("IdKdo");
    }
    voZastup.closeRowSet();
    
    return zastup;
  }

  @SuppressWarnings("unchecked")
  private Map<Number, HashMap> addPrekroceni(Map<Number, HashMap> map, Number user, String budget, String tt, Number castka, Number castkaZd, Number castkaSch, int idBud)
  {
//esc 25.07.2012
logger.debug("CheckBudget-addPrekroceni komu: {} co: {}", user, budget);
    Set set = null;
    if(map.containsKey(user)) 
    {
      set = (Set) map.get(user);
    }
    else 
    {
      set = new TreeSet(cc);
      map.put(user, set);
    }
    set.add( new Object[] { budget, tt, castka, castkaZd, castkaSch } );
    
    Number zastup = getZastupBudget(user,idBud);
    if(zastup != null) addPrekroceni(map, zastup, budget, tt, castka, castkaZd, castkaSch, idBud);
    
    return map;
  }

  private void checkBudgetDenni() 
  {
    if(!isEnabledEmailByDay()) return;

    Mail mail = getMail();

    Set<Number> setIds = new HashSet<>();

    @SuppressWarnings("unchecked")
    Map<Number, HashMap> map = new HashMap<>();
    
   java.sql.Date datumKontrola = Utils.getLastDate();
//ODBOR
    ViewObject vo = am.findViewObject("VwDatBudgetpolozkaView1");
//esc 19.12  nekontrolovat PEREX
//NPER01  a NPER02
    vo.setWhereClause("SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO"+
                      " AND ID_CISTYPTRAN NOT in ( 200,201)" +    
                      " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_BUDGET B WHERE B.ID = VWDATBUDGETPOLOZKA.ID_BUDGET AND SYSDATE-1 BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO AND B.ID_KTGODBOR IS NOT NULL)");
    vo.clearCache();
    ViewObject voZd = am.findViewObject("VwDatBudgetpolozkadata1PARView1");
    voZd.clearCache();
    ViewObject voSch = am.findViewObject("VwDatBudgetpolozkadata2PARView1");
    voSch.clearCache();
    while(vo.hasNext()) 
    {
      Row row = vo.next();

      Number castka = (Number) row.getAttribute("NdCastka");
      Number odborOO = (Number) row.getAttribute("IdOdboroo");
      Number ucspolOO = (Number) row.getAttribute("IdUcspoloo");
      Number ucspolTop = (Number) row.getAttribute("IdUcspoltop");
      Number gestor = (Number) row.getAttribute("IdGestor");
      int idBud = ((Number) row.getAttribute("IdBudget")).intValue();
      int idTran = ((Number) row.getAttribute("IdCistyptran")).intValue();
      String bud = (String) row.getAttribute("Budget");
      String tran = (String) row.getAttribute("Typtran");
      
      Number castkaZd = null;
      Number castkaSch = null;
      
      voZd.setWhereClauseParam(0,sdf.format(datumKontrola));
      voZd.setWhereClauseParam(1,sdf.format(datumKontrola));
      voZd.setWhereClauseParam(2,new Integer(idBud));
      voZd.setWhereClauseParam(3,new Integer(idTran));
      if(voZd.hasNext()) 
      {
        Row rowZd = voZd.next();
        castkaZd = (Number) rowZd.getAttribute("NdCastkalocalZd");
      }
      voZd.closeRowSet();

      voSch.setWhereClauseParam(0,sdf.format(datumKontrola));
      voSch.setWhereClauseParam(1,sdf.format(datumKontrola));
      voSch.setWhereClauseParam(2,new Integer(idBud));
      voSch.setWhereClauseParam(3,new Integer(idTran));
      if(voSch.hasNext()) 
      {
        Row rowSch = voSch.next();
        castkaSch = (Number) rowSch.getAttribute("NdCastkalocalSch");
      }
      voSch.closeRowSet();
      
      double c = castka==null ? 0 : castka.doubleValue();
      double cZd = castkaZd==null ? 0 : castkaZd.doubleValue();
      double cSch = castkaSch==null ? 0 : castkaSch.doubleValue();
     
      setIds.clear();
      if(cZd+cSch > c) {
        if(odborOO!=null) addPrekroceni(map, odborOO, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(odborOO);
        if(gestor!=null) addPrekroceni(map, gestor, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(gestor);
      }
      if(cZd > c) {
        if(ucspolOO!=null) addPrekroceni(map, ucspolOO, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(ucspolOO);
        if(ucspolTop!=null) addPrekroceni(map, ucspolTop, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(ucspolTop);
      }
          
      //mail.sendBudgetDenni(am, setIds, bud, tran);
    }
    //PROJEKT
//esc 10/2011 zrusena kontrola budgetov projektov
// bude nova kontrola, iba na celkovu sumu  budgetu
/*    vo.closeRowSet();
    vo.setWhereClause("SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO"+
                      " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_BUDGET B WHERE B.ID = VWDATBUDGETPOLOZKA.ID_BUDGET AND SYSDATE-1 BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO AND B.ID_KTGPROJEKT IS NOT NULL)");
    vo.clearCache();
    voZd = am.findViewObject("VwDatBudgetProjektpolozkadata1PARView1");
    voZd.clearCache();
    voSch = am.findViewObject("VwDatBudgetProjektpolozkadata2PARView1");
    voSch.clearCache();
    while(vo.hasNext()) 
    {
      Row row = vo.next();

      Number castka = (Number) row.getAttribute("NdCastka");
      Number odborOO = (Number) row.getAttribute("IdOdboroo");
      Number ucspolOO = (Number) row.getAttribute("IdUcspoloo");
      Number ucspolTop = (Number) row.getAttribute("IdUcspoltop");
      Number gestor = (Number) row.getAttribute("IdGestor");
      int idBud = ((Number) row.getAttribute("IdBudget")).intValue();
      int idTran = ((Number) row.getAttribute("IdCistyptran")).intValue();
      String bud = (String) row.getAttribute("Budget");
      String tran = (String) row.getAttribute("Typtran");
      
      Number castkaZd = null;
      Number castkaSch = null;
      
      voZd.setWhereClauseParam(0,sdf.format(datumKontrola));
      voZd.setWhereClauseParam(1,new Integer(idBud));
      voZd.setWhereClauseParam(2,new Integer(idTran));
      if(voZd.hasNext()) 
      {
        Row rowZd = voZd.next();
        castkaZd = (Number) rowZd.getAttribute("NdCastkalocalZd");
      }
      voZd.closeRowSet();

      voSch.setWhereClauseParam(0,sdf.format(datumKontrola));
      voSch.setWhereClauseParam(1,new Integer(idBud));
      voSch.setWhereClauseParam(2,new Integer(idTran));
      if(voSch.hasNext()) 
      {
        Row rowSch = voSch.next();
        castkaSch = (Number) rowSch.getAttribute("NdCastkalocalSch");
      }
      voSch.closeRowSet();
      
      double c = castka==null ? 0 : castka.doubleValue();
      double cZd = castkaZd==null ? 0 : castkaZd.doubleValue();
      double cSch = castkaSch==null ? 0 : castkaSch.doubleValue();
     
      setIds.clear();
      if(cZd+cSch > c) {
        if(odborOO!=null) addPrekroceni(map, odborOO, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(odborOO);
        if(gestor!=null) addPrekroceni(map, gestor, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(gestor);
      }
      if(cZd > c) {
        if(ucspolOO!=null) addPrekroceni(map, ucspolOO, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(ucspolOO);
        if(ucspolTop!=null) addPrekroceni(map, ucspolTop, bud, tran, castka, castkaZd, castkaSch, idBud); //setIds.add(ucspolTop);
      }
          
      //mail.sendBudgetDenni(am, setIds, bud, tran);
    }
    vo.closeRowSet();
 esc projekty*/   
    if(!map.isEmpty()) 
    {
      Iterator iter = map.keySet().iterator();
      while(iter.hasNext()) 
      {
        Number user = (Number) iter.next();
        Set set = (Set) map.get(user);
        String cesta = null;
        if(set!=null && !set.isEmpty()) 
        {
          ESExportBudgetPrekroceni ep = new ESExportBudgetPrekroceni(set,user);
          try 
          {
            ep.excelOutput();
            cesta = ep.getFileAbsoluteName();
          }
          catch(Exception e) 
          {
            logger.error("Budget denni", e);
            e.printStackTrace(); //pro zacatek
          }
          if(ep.getFileName()!=null)
            mail.sendBudgetDenni(am, user, cesta);
        }
      }
    }
  }
  
  private Set getZatupceBudget(Number userId) 
  {
    Set set = new HashSet();
    
    ViewObject voZastup = am.findViewObject("KpDatBudgetzastupView1");
    voZastup.clearCache();    
    voZastup.setWhereClause("ID_KOHO = "+userId + " AND SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO AND C_FWDEMAIL = '1'");
    while(voZastup.hasNext()) 
    {
      Row row = voZastup.next();
      Number zastup = (Number) row.getAttribute("IdKdo");
      set.add(zastup);
    }
    voZastup.closeRowSet();
    
    return set;
  }
  
  private void checkBudgetMesicni() 
  {
// pusta sa iba koncom mesiaca
    //if(!isEnabledEmailByDay()) return;

    Calendar curDate = Calendar.getInstance();
    int denMesice = curDate.get(Calendar.DAY_OF_MONTH);
    int lastDenMesice = curDate.getMaximum(Calendar.DAY_OF_MONTH);
    int hodinaDne = curDate.get(Calendar.HOUR_OF_DAY);
    if(denMesice!=1 && (denMesice!=lastDenMesice || hodinaDne<20)) return;

    Mail mail = getMail();

//ODBORY
    Set setOJ = new HashSet(),
        setSpol = new HashSet(),
        setGest = new HashSet();

    //Nacitani odpovednych osob
    ViewObject vo = am.findViewObject("VwDatBudgetView1");
/*
    vo.clearCache();
    vo.setWhereClause("SYSDATE-1 BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number odborOO = (Number) row.getAttribute("IdOdboroo");
      Number ucspolOO = (Number) row.getAttribute("IdUcspoloo");

      if(odborOO!=null) {
        setOJ.add(odborOO);
        setOJ.addAll( getZatupceBudget(odborOO) );
      }
      if(ucspolOO!=null) {
        setSpol.add(ucspolOO);
        setSpol.addAll( getZatupceBudget(ucspolOO) );
      }
    }
    vo.closeRowSet();
    
    vo = am.findViewObject("VwDatBudgetpolozkaView1");
    vo.clearCache();
    vo.setWhereClause("SYSDATE-1 BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO"+
                     " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_BUDGET B WHERE B.ID = VWDATBUDGETPOLOZKA.ID_BUDGET AND SYSDATE-1 BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO AND B.ID_KTGODBOR IS NOT NULL)");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number gestor = (Number) row.getAttribute("IdGestor");

      if(gestor!=null) {
        setGest.add(gestor);
        setGest.addAll( getZatupceBudget(gestor) );
      }
    }
    vo.closeRowSet();
    
    //GENEROVANI EXCELU
    java.sql.Date dd = new java.sql.Date(System.currentTimeMillis());
    String cesta = null;
    Number userId = null;
    //OO OJ
    Iterator iter = setOJ.iterator();
    while(iter.hasNext()) 
    {
      userId = (Number)iter.next();
      ESExportBudgetStd be = new ESExportBudgetStd(am, dd, userId.intValue(), ESExportBudgetStd.EXPORT_TYPE_OO_OJ, true, false);
      try 
      {
        be.excelOutput();
        cesta = be.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        e.printStackTrace(); //pro zacatek
      }
      mail.sendBudgetMesicni(am, userId, cesta, 18);
    }
    //OO Spol.
    iter = setSpol.iterator();
    while(iter.hasNext()) 
    {
      userId = (Number)iter.next();
      ESExportBudgetStd be = new ESExportBudgetStd(am, dd, userId.intValue(), ESExportBudgetStd.EXPORT_TYPE_OO_OJ, true, false);
      try 
      {
        be.excelOutput();
        cesta = be.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        e.printStackTrace(); //pro zacatek
      }
      mail.sendBudgetMesicni(am, userId, cesta, 19);
    }
    //Gestor
    iter = setGest.iterator();
    while(iter.hasNext()) 
    {
      userId = (Number)iter.next();
      ESExportBudgetStd be = new ESExportBudgetStd(am, dd, userId.intValue(), ESExportBudgetStd.EXPORT_TYPE_GESTOR, true, false);
      try 
      {
        be.excelOutput();
        cesta = be.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        e.printStackTrace(); //pro zacatek
      }
      mail.sendBudgetMesicni(am, userId, cesta, 20);
    }
*/    
//PROJEKTY
    setOJ.clear();
    setSpol.clear();
    setGest.clear();
    
    //Nacitani odpovednych osob
    vo = am.findViewObject("VwDatBudgetprojektView1");
    vo.clearCache();
    vo.setWhereClause("SYSDATE-1 BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number odborOO = (Number) row.getAttribute("IdPmanager");
      Number ucspolOO = (Number) row.getAttribute("IdUcspoloo");

      if(odborOO!=null) {
        setOJ.add(odborOO);
        setOJ.addAll( getZatupceBudget(odborOO) );
      }
      if(ucspolOO!=null) {
        setSpol.add(ucspolOO);
        setSpol.addAll( getZatupceBudget(ucspolOO) );
      }
    }
    vo.closeRowSet();

    
    vo = am.findViewObject("VwDatBudgetpolozkaView1");
    vo.clearCache();
    vo.setWhereClause("SYSDATE-1 BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO"+
                     " AND EXISTS (SELECT NULL FROM DB_JT.KP_DAT_BUDGET B WHERE B.ID = VWDATBUDGETPOLOZKA.ID_BUDGET AND SYSDATE-1 BETWEEN B.DT_PLATNOSTOD AND B.DT_PLATNOSTDO AND B.ID_KTGPROJEKT IS NOT NULL)");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number gestor = (Number) row.getAttribute("IdGestor");

      if(gestor!=null) {
        setGest.add(gestor);
        setGest.addAll( getZatupceBudget(gestor) );
      }
    }
    vo.closeRowSet();
    
    //GENEROVANI EXCELU
    java.sql.Date dd = new java.sql.Date(System.currentTimeMillis());
    String cesta = null;
    Number userId = null;
    //OO OJ
    Iterator iter = setOJ.iterator();
    while(iter.hasNext()) 
    {
      userId = (Number)iter.next();
      ESExportBudgetProjekt be = new ESExportBudgetProjekt(am, dd, userId.intValue(), ESExportBudgetStd.EXPORT_TYPE_OO_OJ, true, false);
      try 
      {
        be.excelOutput();
        cesta = be.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        logger.error("Budget mesicni 1", e);
        e.printStackTrace(); //pro zacatek
      }
      if(be.getFileName()!=null)
        mail.sendBudgetMesicni(am, userId, cesta, 18);
    }
    //OO Spol.
    iter = setSpol.iterator();
    while(iter.hasNext()) 
    {
      userId = (Number)iter.next();
      ESExportBudgetProjekt be = new ESExportBudgetProjekt(am, dd, userId.intValue(), ESExportBudgetStd.EXPORT_TYPE_OO_OJ, true, false);
      try 
      {
        be.excelOutput();
        cesta = be.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        logger.error("Budget mesicni 2", e);
        e.printStackTrace(); //pro zacatek
      }
      if(be.getFileName()!=null)
        mail.sendBudgetMesicni(am, userId, cesta, 19);
    }
    //Gestor
    iter = setGest.iterator();
    while(iter.hasNext()) 
    {
      userId = (Number)iter.next();
      ESExportBudgetProjekt be = new ESExportBudgetProjekt(am, dd, userId.intValue(), ESExportBudgetStd.EXPORT_TYPE_GESTOR, true, false);
      try 
      {
        be.excelOutput();
        cesta = be.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        logger.error("Budget mesicni 3", e);
        e.printStackTrace(); //pro zacatek
      }
      if(be.getFileName()!=null)
        mail.sendBudgetMesicni(am, userId, cesta, 20);
    }
  }

  private void checkBudgetSchvalovani() 
  {
    if(!isEnabledEmailByDay()) return;

    Mail mail = getMail();

    Map map = new TreeMap();
    
    ViewObject vo = am.findViewObject("VwDatBudgetschvalovaniView1");
    vo.clearCache();
    vo.setWhereClause("");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      //Number uroven = (Number) row.getAttribute("Uroven");
      String uroven = row.getAttribute("Uroven").toString();
      Number idBudget = (Number) row.getAttribute("IdBudget");
      String budget = (String) row.getAttribute("Budget");
      Number idOsoba = (Number) row.getAttribute("IdOsoba");
      String osoba = (String) row.getAttribute("Osoba");
      Number pocet = (Number) row.getAttribute("Pocet");
      int muze = Integer.parseInt((String)row.getAttribute("Nizsi"));
      @SuppressWarnings("unchecked")
      Map<Number, Set[]> mapOsoby = null;
      if(map.containsKey(uroven))
        mapOsoby = (Map<Number, Set[]>) map.get(uroven);
      else {
        mapOsoby = new HashMap<>();
        map.put(uroven, mapOsoby);
      }
      Set[] sets = null;
      if(mapOsoby.containsKey(idOsoba))
        sets = mapOsoby.get(idOsoba);
      else {
        sets = new Set[] { new TreeSet<>(), new TreeSet<>() };
        mapOsoby.put(idOsoba, sets);
      }
      sets[muze].add(idBudget+"@"+budget+" ...("+pocet+")");
      
    }
    vo.closeRowSet();
    String cesta = null;
    ESExportBudgetSchvalovani be = new ESExportBudgetSchvalovani(am);
    try 
    {
      be.excelOutput();
      cesta = be.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Budget schvalovani", e);
      e.printStackTrace(); //pro zacatek
    }
    if(be.getFileName()!=null)
      mail.sendBudgetSchvalovani(am, map, cesta);
  }

  private void checkProjektPreceneni() 
  {
    if(!isEnabledEmailByDay()) return;

    List<String> projekty = new ArrayList<>();
    Mail mail = getMail();
    
    ViewObject vo = am.findViewObject("VwKtgProjektView1");
    vo.clearCache();
    vo.setWhereClause("ID_STATUS NOT IN (5,6,7)"+
                 " AND NOT EXISTS (SELECT NULL"+
                                 " FROM DB_JT.KP_DAT_MISTRANSAKCE m"+
				                         " WHERE m.ID_KTGPROJEKT = VwKtgProjekt.ID"+
  		                             " and m.C_STRANA = 'M'"+
		                               " and m.DT_DATUM between VwKtgProjekt.DT_STARTOCENENI and VwKtgProjekt.DT_KONECOCENENI)"+
                 " and DT_STARTOCENENI is not null"+
                 " and DT_KONECOCENENI is not null");
    vo.setOrderByClause("S_NAZEV");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number) row.getAttribute("Id");
      String nazev = (String) row.getAttribute("SNazev");
      Number idPMan = (Number) row.getAttribute("IdPmanager");
      String PMan = (String) row.getAttribute("Pmanager");

      if(idPMan!=null) {
        mail.sendProjektPreceneni(am, nazev, id.intValue(), idPMan);
        projekty.add(id+" - "+nazev+" ("+PMan+")");
      }
    }
    vo.closeRowSet();
    
    if(!projekty.isEmpty()) 
    {
      mail.sendProjektPreceneniKontroling(am, projekty);
    }
  }
  
  private void checkProjektMemorandum() 
  {
    if(!isEnabledEmailByDay()) return;

    @SuppressWarnings("unchecked")
    Map<Number, HashMap> pman = new HashMap<>();
    Mail mail = getMail();
    
    ViewObject vo = am.findViewObject("VwKtgProjektView1");
    vo.clearCache();
    vo.setWhereClause("C_MEMORANDUMSPLNENO <> '1' AND ID_STATUS NOT IN (5,6,7)");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number) row.getAttribute("Id");
      String nazev = (String) row.getAttribute("SNazev");
      Number idPMan = (Number) row.getAttribute("IdPmanager");
      String PMan = (String) row.getAttribute("Pmanager");

      if(idPMan!=null) {
        Set proj = null;
        if(pman.containsKey(idPMan)) {
          proj = (Set) pman.get(idPMan);
        }
        else 
        {
          proj = new TreeSet();
          pman.put(idPMan, proj);
        }
        proj.add(id+" - "+nazev);
      }
    }
    vo.closeRowSet();
    
    if(!pman.isEmpty()) 
    {
      mail.sendProjektMemorandum(am, pman);
    }

    ViewObject voBoss = am.findViewObject("KpDatMngsegmentbossView1");
    voBoss.clearCache();
    voBoss.setWhereClause("SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(voBoss.hasNext()) {
      Row row = voBoss.next();
      Number idBoss = (Number) row.getAttribute("IdBoss");
      String cesta = null;
      ESExportProjektKartaSS ess = new ESExportProjektKartaSS(am,idBoss);
      try 
      {
        ess.excelOutput();
        if(ess.getFileName()!=null) {
          cesta = ess.getFileAbsoluteName();
        }
      }
      catch(Exception e) 
      {
        logger.error("Projekt Karta OOH", e);
        e.printStackTrace(); //pro zacatek
      }
      if(ess.getFileName()!=null)
      {
        mail.sendProjektMemorandumSS(am,idBoss,cesta);
      }
    }
    voBoss.closeRowSet();

    String cesta = null;
    ESExportProjektKartaSS ess = new ESExportProjektKartaSS(am,null);
    try 
    {
      ess.excelOutput();
      if(ess.getFileName()!=null) {
        cesta = ess.getFileAbsoluteName();
      }
    }
    catch(Exception e) 
    {
      logger.error("Projekt Karta Kontroling", e);
      e.printStackTrace(); //pro zacatek
    }
    if(ess.getFileName()!=null)
    {
      mail.sendProjektMemorandumKontroling(am,cesta);
    }
  }

  private void checkSpolecnostZprava() 
  {
    if(!isEnabledEmailByDay()) return;

    @SuppressWarnings("unchecked")
    Map<Number, HashMap> oos = new HashMap<>();
    Mail mail = getMail();
    
    ViewObject vo = am.findViewObject("VwRelSpolecnostKartaTypView1");
    vo.clearCache();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      if("1".equals(row.getAttribute("CZpravasplneno"))) continue;
      Number id = (Number) row.getAttribute("Id");
      String nazev = (String) row.getAttribute("Spolecnost");
      Number idOO = (Number) row.getAttribute("IdOdpovednaosoba");
      String karta = (String) row.getAttribute("KartaTyp");

      if(idOO!=null) {
        Set spolKarta = null;
        if(oos.containsKey(idOO)) {
          spolKarta = (Set) oos.get(idOO);
        }
        else 
        {
          spolKarta = new TreeSet();
          oos.put(idOO, spolKarta);
        }
        spolKarta.add(nazev+" ("+id+") / "+karta);
      }
    }
    vo.closeRowSet();
    
    if(!oos.isEmpty()) 
    {
      mail.sendSpolecnostZprava(am, oos);
    }

    ViewObject voBoss = am.findViewObject("KpDatMngsegmentbossView1");
    voBoss.clearCache();
    voBoss.setWhereClause("SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(voBoss.hasNext()) {
      Row row = voBoss.next();
      Number idBoss = (Number) row.getAttribute("IdBoss");
      String cesta = null;
      ESExportSpolecnostKartaSS ess = new ESExportSpolecnostKartaSS(am,idBoss);
      try 
      {
        ess.excelOutput();
        if(ess.getFileName()!=null) {
          cesta = ess.getFileAbsoluteName();
        }
      }
      catch(Exception e) 
      {
        logger.error("SPV Karta OOH", e);
        e.printStackTrace(); //pro zacatek
      }
      if(ess.getFileName()!=null)
      {
        mail.sendSpolecnostZpravaSS(am,idBoss,cesta);
      }
    }
    voBoss.closeRowSet();

    String cesta = null;
    ESExportSpolecnostKartaSS ess = new ESExportSpolecnostKartaSS(am,null);
    try 
    {
      ess.excelOutput();
      if(ess.getFileName()!=null) {
        cesta = ess.getFileAbsoluteName();
      }
    }
    catch(Exception e) 
    {
      logger.error("SPV Karta Kontroling", e);
      e.printStackTrace(); //pro zacatek
    }
    if(ess.getFileName()!=null)
    {
      mail.sendSpolecnostZpravaKontroling(am,cesta);
    }
  }

  private void checkEviSpolecnostOR3M() 
  {
    String cesta = null;
    ESExportEviOR1M ez = new ESExportEviOR1M(am);
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Evi OR 3M", e);
      e.printStackTrace(); //pro zacatek
    }
    if(cesta!=null && !ez.isEmpty()) 
      getMail().sendEviSpolecnostOR1M(am, cesta);
/*  
    if(!isEnabledEmailByDay()) return;

    Set kompl = new TreeSet();
    Set akc = new TreeSet();
    Mail mail = getMail();
    
    ViewObject vo = am.findViewObject("VwKpEviobchodnirejstrik3mView1");
    vo.clearCache();
    vo.setWhereClause("K2M = '1'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String spolecnost = (String) row.getAttribute("Spolecnost");
      String stat = (String) row.getAttribute("Stat");
      oracle.jbo.domain.Date datum = (oracle.jbo.domain.Date) row.getAttribute("Kompletni");
      String dt = datum!=null ? sdf.format(datum.dateValue()) : "-";

      kompl.add(spolecnost+" ("+stat+") "+dt);
    }
    vo.closeRowSet();
    vo.clearCache();
    vo.setWhereClause("A2M = '1'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String spolecnost = (String) row.getAttribute("Spolecnost");
      String stat = (String) row.getAttribute("Stat");
      oracle.jbo.domain.Date datum = (oracle.jbo.domain.Date) row.getAttribute("Akcionar");
      String dt = datum!=null ? sdf.format(datum.dateValue()) : "-";

      akc.add(spolecnost+" ("+stat+") "+dt);
    }
    vo.closeRowSet();
    
    if(!kompl.isEmpty() || !akc.isEmpty()) 
    {
      mail.sendEviSpolecnostOR3M(am, kompl, akc);
    }
*/    
  }

  private void checkSpolecnostZamekGen() 
  {
    if(!isEnabledEmailByDay()) return;
    
    String cesta = null;
    ESExportZamekGen ez = new ESExportZamekGen(am, false);
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Spolecnost zamek gen. 1", e);
      e.printStackTrace(); //pro zacatek
    }
    if(ez.getFileName()!=null) 
      getMail().sendSpolecnostZamekGen(am, cesta, false);
      
    cesta = null;
    ez = new ESExportZamekGen(am, true);
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Spolecnost zamek gen. 2", e);
      e.printStackTrace(); //pro zacatek
    }
    if(ez.getFileName()!=null) 
      getMail().sendSpolecnostZamekGen(am, cesta, true);

    //informovat top 5 dni predem
    Set top = new HashSet();
    
    ViewObject vo = am.findViewObject("VwKpSpolecnostzamekgenView1");
    vo.clearCache();
    vo.setWhereClause("SCHVALENOTOP is null");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number idTop = (Number) row.getAttribute("IdTopmng");
      top.add(idTop);
    }
    vo.closeRowSet();
    
    Iterator iter = top.iterator();
    while(iter.hasNext()) 
    {
      Number idTop = (Number)iter.next();
      if(idTop==null) continue;
      ESExportZamekTop ezt = new ESExportZamekTop(am, idTop);
      try 
      {
        ezt.excelOutput();
        cesta = ezt.getFileAbsoluteName();
      }
      catch(Exception e) 
      {
        logger.error("Spolecnost zamek gen. OOH pro idTop={}", idTop, e);
        e.printStackTrace(); //pro zacatek
      }
      if(ezt.getFileName()!=null) 
        getMail().sendSpolecnostZamekGenSS(am, idTop, cesta);
    }

  }

  private void checkSpolecnostSkupina4() 
  {
    String cesta = null;
    ESExportSpolecnostSkupina4 ez = new ESExportSpolecnostSkupina4(am);
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Spolecnost skupina 4", e);
      e.printStackTrace(); //pro zacatek
    }
    if(cesta!=null && !ez.isEmpty()) 
      getMail().sendSpolecnostSkupina4(am, cesta);
  }

  private void checkSpolecnostiSoulad() 
  {
    String cesta = null;
    ESExportSpolecnostiSoulad ez = new ESExportSpolecnostiSoulad(am);
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Spolecnosti soulad", e);
      e.printStackTrace(); //pro zacatek
    }
    if(cesta!=null && !ez.isEmpty()) 
      getMail().sendSpolecnostiSoulad(am, cesta);
  }
  
  private void checkZamekExterni() 
  {
    String cesta = null;
    ESExportZamekExterni ez = new ESExportZamekExterni(am);
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Ext. spol. bez zamku", e);
      e.printStackTrace(); //pro zacatek
    }
    if(cesta!=null) 
      getMail().sendSpolecnostZamekExterni(am, cesta);
  }

  private void checkZmenyProtistran() 
  {
    String cesta = null;
    ESExportZmenyProtistran ez = new ESExportZmenyProtistran(am, new java.sql.Date(System.currentTimeMillis()));
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Zmeny protistran", e);
      e.printStackTrace(); //pro zacatek
    }
    if(cesta!=null && !ez.isEmpty()) 
      getMail().sendProtistranyZmeny(am, cesta);
  }

  private void checkBudgetMustekNaklad() 
  {
    String cesta = null;
    ESExportBudgetMustekNaklad ez = new ESExportBudgetMustekNaklad(am);
    try 
    {
      ez.excelOutput();
      cesta = ez.getFileAbsoluteName();
    }
    catch(Exception e) 
    {
      logger.error("Budget mustek naklad", e);
      e.printStackTrace(); //pro zacatek
    }
    if(cesta!=null && !ez.isEmpty()) 
      getMail().sendBudgetMustekNaklad(am, cesta);
  }
  
  public static void main(String[] argv) 
  {
    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
/*
    List<Number> ids = new ArrayList<>();
    ids.add( new Number(1) );
    ids.add( new Number(1001) );
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().init((Number[])ids.toArray(new Number[] {}), new java.sql.Date(new java.sql.Date(106,2,31).getTime()));
    AutoProtokolNew.getInstance().createProtokol(ids, Constants.ALL);
    AutoProtokolNew.getInstance().saveLogFile(null, Constants.ALL, 0);
*/
/*
    AutoProtokolNew.getInstance().createProtokol(AutoProtokolNew.getInstance().ids, Constants.ALL);
    String fileName = AutoProtokolNew.getInstance().saveLogFile(null, Constants.ALL);
    AutoProtokolNew.getInstance().sendEmailProtokol(fileName);
*/
/*
    Number[] ids = new Number[] 
    {
      new Number(1046)
    };
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().init(ids, new java.sql.Date(new java.sql.Date(105,1,21).getTime()));
    AutoProtokolNew.getInstance().createProtokol(Arrays.asList(ids),Constants.UCETNI);
*/
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkBudgetMesicni();
*/

    AutoProtokolNew.getInstance().datum = new java.sql.Date(System.currentTimeMillis());
    //AutoProtokolNew.getInstance().checkSchvalovak(dm);

/*
    AutoProtokolNew.getInstance().am = dm;
    System.out.println(AutoProtokolNew.getInstance().checkSchvaleno(new Number(3001),Constants.UCETNI));
    System.out.println(AutoProtokolNew.getInstance().checkSchvaleno(new Number(3001),Constants.OO));
*/
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().datum = new java.sql.Date(new java.sql.Date(106,3,9).getTime());
    AutoProtokolNew.getInstance().checkBudgetDenni();
*/   
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkProjektPreceneni();
*/
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkEviSpolecnostOR3M();
*/
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkSpolecnostZamekGen();
*/    
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().cCheck = Calendar.getInstance();
    System.out.println(AutoProtokolNew.getInstance().checkSchvaleno(new Number(5007),Constants.OO));
*/    
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkSpolecnostSkupina4();
    AutoProtokolNew.getInstance().checkSpolecnostiSoulad();
*/    
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkZmenyProtistran();
*/
//    AutoProtokolNew.getInstance().am = dm;
    //AutoProtokolNew.getInstance().datum = new java.sql.Date(new java.util.Date(106,3,30).getTime());
//    AutoProtokolNew.getInstance().checkBudgetDenni();
/*
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkProjektMemorandum();
*/    
    //AutoProtokolNew.getInstance().checkOther(dm);
    AutoProtokolNew.getInstance().am = dm;
    AutoProtokolNew.getInstance().checkZamekVSClose();
    //AutoProtokolNew.getInstance().checkSpolecnostZprava();
    //AutoProtokolNew.getInstance().am = dm;
    //System.out.print(AutoProtokolNew.getInstance().jePodrizenySS(new Number(10), new Number(24)));
//    AutoProtokolNew.getInstance().am = dm;
//    AutoProtokolNew.getInstance().checkZamekExterni();
  }
}
