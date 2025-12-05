package cz.jtbank.konsolidace.jobs;

import cz.jtbank.konsolidace.common.*;
//import cz.jtbank.konsolidace.postgre.*;
import java.io.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.client.*;
import oracle.jbo.domain.Number;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import cz.jtbank.konsolidace.protistrany.common.ProtistranyModule;
import cz.jtbank.konsolidace.doklady.common.DokladyModule;
import cz.jtbank.konsolidace.pb.common.PbModule;
import cz.jtbank.konsolidace.common.Utils;
import cz.jtbank.konsolidace.common.SystemStatus;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class GenerateAll extends Thread 
{
  static Logger logger = Logger.getLogger(GenerateAll.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_AUTO_GEN)); }

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
  private boolean povoleno;
  private Date datumDo;
  private Date datumMustek;

  private Date nextStartProces = new Date(0L);
  private long pauza;

  private boolean newParam;
  private boolean konec = false;

  ApplicationModule am = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.admin.AdminModule","AdminModuleLocal");
  ProtistranyModule pm = (ProtistranyModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.protistrany.ProtistranyModule","ProtistranyModuleLocal");  
  DokladyModule dm = (DokladyModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
  PbModule pbm = (PbModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.pb.PbModule","PbModuleLocal");

  private static GenerateAll instance;
  
  public static GenerateAll getInstance()
  {
    if(instance == null) instance = new GenerateAll();
    return instance;
  }

  public static boolean runs() 
  {
    if(instance==null) return false;
    if(!instance.isAlive()) return false;

    return true;
  }
  
  public static void reset() 
  {
    if(instance!=null) 
    {
      instance.interrupt();
      instance = null;
    }
    getInstance().start();
  }

  private GenerateAll()
  {
    resetParams();
    logger.debug("----------Vytvoreni instance GenerateAll----------");
  }
  
  private boolean isReloadProtistranyEnabled() {
    boolean reloadEnabled = false;
    ViewObject vo = am.findViewObject("KpParametryView1");
    vo.clearCache();
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      reloadEnabled = "1".equals(row.getAttribute("CReloadprotistran"));
    }
    vo.closeRowSet();
    return reloadEnabled;
  }

  public void resetParams() {
    logger.debug("zacatek resetParams");  
    ViewObject vo = am.findViewObject("KpParametryView1");
    vo.clearCache();
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      povoleno = "1".equals(row.getAttribute("CNgPovoleno"));
      logger.info("povoleno="+povoleno);
      datumDo = ((oracle.jbo.domain.Date) row.getAttribute("DtNgDatumDo")).dateValue();
      logger.info("datum od="+datumDo);
      String hodiny = (String) row.getAttribute("SNgHodiny");
      logger.info("hodiny="+hodiny);
      Number maxradku = (Number) row.getAttribute("NlMaxxls");
      if(maxradku!=null) Constants.MAX_POCET_RADKU_EXCEL = maxradku.intValue();
      setTime(hodiny);

      oracle.jbo.domain.Date oraDM = (oracle.jbo.domain.Date) row.getAttribute("DtNgDatumMustek");
      if(oraDM != null) {
        datumMustek = oraDM.dateValue();
      }
      else {
        java.util.Calendar c = java.util.GregorianCalendar.getInstance();
        c.setTimeInMillis ( System.currentTimeMillis() );
        c.set ( c.HOUR_OF_DAY, 0 );
        c.set ( c.MINUTE, 0 );
        c.set ( c.SECOND, 0 );
        datumMustek = new java.sql.Date(c.getTimeInMillis());
      }
      logger.info("datumMustek="+datumMustek);
    }
    else 
    {
      logger.error("Chyba v resetParams!");
    }
    vo.closeRowSet();
    logger.debug("konec resetParams");  
  }
  
  private void setTime(String sTime) {
    // pauzu nastavit tak aby se spustil ve spravnou chvili
    java.util.Calendar ted = java.util.GregorianCalendar.getInstance();        
    java.util.Calendar c = java.util.GregorianCalendar.getInstance();
    ted.setTimeInMillis ( System.currentTimeMillis() );
    c.setTimeInMillis ( System.currentTimeMillis() );

    int dvojtecka = sTime.indexOf(':');
    int hodiny = Integer.parseInt(sTime.substring(0,dvojtecka) );
    int minuty = Integer.parseInt(sTime.substring(dvojtecka+1) );
    c.set ( c.HOUR_OF_DAY, hodiny );
    c.set ( c.MINUTE, minuty );
    c.set ( c.SECOND, 0 );
    if ( ted.after( c ) )
      c.add( c.DAY_OF_MONTH, 1 );

    pauza = c.getTimeInMillis() - ted.getTimeInMillis();
    nextStartProces.setTime( System.currentTimeMillis() + pauza );
    logger.info("v setTime nastavena PAUZA na "+ msToTime(pauza));
  }

  public void newParam() {
    logger.info("volani funkce newParam");
    newParam = true;
    synchronized ( this ) {
      this.notify();
    }        
  }
    
  public void konec() {
    logger.info("volani funkce konec");
    konec = true;
  }
  
  public void run() {
    logger.debug("start proces automatickeho generovani dokladu");          
    System.out.println("   --->> start proces automatickeho generovani dokladu");
    resetParams();            
    for (;;) {
      newParam = false;
      try {
        synchronized ( this ) {
          logger.debug("Cekam na " + nextStartProces + ", newParam=" + newParam);
          this.wait(pauza);
          logger.debug( "konec cekani pokus o start, newParam=" + newParam +", konec="+konec);
          if ( konec ) break;
          if ( newParam ) {
            newParam = false;
            this.wait(1000);
            resetParams();            
            continue;
          }
          pauza = 24*60*60*1000L; // jednou za den
          if ( !povoleno ) {
            nextStartProces.setTime( System.currentTimeMillis() + pauza );
            continue;
          }
        }
        nextStartProces.setTime( System.currentTimeMillis() + pauza );
        
        SystemStatus.getInstance().reset();
        SystemStatus.getInstance().setStatus("Generov�n� spu�t�no");
        reloadProtistrany();
        generujAll();
//12.01.2010 zrusene MIS        generujMis();
generujDenni();
//12.01.2010 zrusene MIS       generujDenniMis();
        SystemStatus.getInstance().setStatus("Automatick� generov�n� doklad�");
      } 
      catch ( java.lang.InterruptedException e ) {
          e.printStackTrace();
          break;
      } 
      catch ( KisException e ) {
          e.printStackTrace();
      }
      finally {
          // upravim kolik budu cekat - zkracuji o dobu co zabrala generujAll()
          pauza = nextStartProces.getTime() - System.currentTimeMillis();
          if ( pauza < 0 ) pauza = 5000;
          logger.debug("PAUZA="+msToTime(pauza)+"");
      }
    }
    System.out.println("   *** >> konec thread");
    logger.debug("konec run");
  }
  
  private void reloadProtistrany() {
    if(isReloadProtistranyEnabled()) {
      SystemStatus.getInstance().setStatus("Reload protistran atp.");
      logger.debug("Start reload protistran");
      try {
        pm.reloadProtistrany();
      } catch ( Exception e ) {      
        e.printStackTrace();
        logger.error("Reload protistran:\n",e);
      }        
      finally {
        pm.getTransaction().commit();
      }
      logger.debug("Konec reload protistran");
    }
    else 
    {
      logger.debug("Reload protistran je vypnuty");
    }
/* --esc 05 / 2012    
    try 
    {
      logger.debug("Start load PB klientu z ePerspektivy");
      PostgreLoader pload = new PostgreLoader();
      pload.loadFromPostgre(pbm);
      logger.debug("Konec load PB klientu z ePerspektivy");
    }
    catch (Exception e) 
    {
      logger.error("Reload protistran - loadFromPostgre:\n",e);
    }
*/
    try 
    {
      logger.debug("Start reload PB klientu");
      pbm.reloadKlienti();
      logger.debug("Konec reload PB klientu");
    }
    catch (Exception e) 
    {
      logger.error("Reload protistran - reloadKlienti:\n",e);
    }
  }

  private void generujAll() throws KisException, InterruptedException {
  /* 18.4.2007 - inovovana verze */
    SystemStatus.getInstance().setStatus("Vkl�d�n� do fronty");
    logger.debug("Start vkladani dokladu do fronty");

    boolean uspech = false;
    long cnt = 0;
    while(!uspech) {
      try { //Mazani tmp. tabulky pro generovani vzajemnych vazeb a jine inicializace
        dm.startGenerovaniAuto();
        uspech = true;
      }
      catch(Exception e) 
      {
        e.printStackTrace();
        cnt++;
        if(cnt<12) {
          logger.error("Selhalo volani dm.startGenerovaniAuto() - cekam 10 minut na dalsi spusteni.", e);
          synchronized (this) {
            wait(600000);
          }
        }
        else 
        {
          logger.error("Zcela selhalo volani dm.startGenerovaniAuto(). Automaticke generovani nebude spusteno!!!", e);
          throw new KisException("Zcela selhalo volani dm.startGenerovaniAuto(). Automaticke generovani nebude spusteno!!!",e);
        }
      }
      finally {
        dm.getTransaction().commit();
      }
    }
    
    List ids = new ArrayList();
    LocalDate localDate = datumDo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String strDatum = localDate.format(DATE_FORMATTER);
    try {
      ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
      vo.clearCache();
      vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1' AND (ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL+" OR TO_DATE('"+strDatum+"','dd.mm.yyyy')=ADD_MONTHS(TRUNC(TO_DATE('"+strDatum+"','dd.mm.yyyy'),'Q'),3)-1)");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        ids.add((Number)row.getAttribute("Id"));
      }
      vo.closeRowSet();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
      logger.error("Vkladani dokladu do fronty:\n",e);
    }
    finally {
      dm.getTransaction().commit();
    }
    Iterator iter = ids.iterator();
    while(iter.hasNext()) {
      Number idSpol = (Number) iter.next();
      logger.info("Pro idSpol="+idSpol+",datum="+datumDo+",datumMustek="+datumMustek);  
      try {
        dm.genDoklad(Constants.AUTOMAT_USER,
                     1,
                     idSpol.intValue(),
                     2,
                     datumDo,
                     datumMustek,
                     0,
                     true,
                     "0");
      } catch ( Exception e ) {
        e.printStackTrace();
        logger.error("Vkladani dokladu do fronty:\n",e);
      } 
    }
    logger.debug("Konec vkladani dokladu do fronty");

    Number[] idsArr = (Number[]) ids.toArray(new Number[] {});
    AutoCounterNew.getInstance().init(idsArr);
    AutoProtokolNew.getInstance().init(idsArr, datumDo);
  
    try {
      logger.debug("Start mazani starych dokladu");
//      dm.deleteStareDoklady();
    } catch ( Exception e ) {
      e.printStackTrace();
      logger.error("Mazani starych dokladu:\n",e);
    } 
    logger.debug("Konec mazani starych dokladu");
  }

  private void generujDenni() {
    if ( ! Utils.generujDenniDoklad(new java.util.Date()) ) {
      logger.debug("Denni se negeneruji");
      return;
    }
    logger.debug("Start vkladani Dennich dokladu do fronty pro men./uv. pozice");
    List ids = new ArrayList();
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");

    try {
      vo.clearCache();
    //--esc povodna verzia
	//	vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1'"+ 
	//						" AND ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL);
	
	//--	verzia pre obmedzene generovanie na zaklade datumu, priznaku ....
	if ( Utils.generujDenniDokladAll(new java.util.Date()) )	{    
			logger.debug("Generuji se Denni pro Vsechny #300");
			vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1'"+ 
							" AND ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL);
	  }
	//esc  29.1.2008 - obmedzime denni a MIS denni doklady 
	//esc  p�es tyden generovat JEN pro spolecnosti, kter� jsou RKC a maj� budget
	  else {
			logger.debug("Generuji se Denni pro RKC a Budget #33");
			vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1'"+ 
							" AND ID_CISKATSPOL NOT IN ( "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL+" ) "+
							" AND ( C_RKC = '1' " +
							" 	    OR  		" + 
							"		ID  IN (select ID_KTGUCETNISPOLECNOST from DB_JT.KP_KTG_ODBOR where C_BUDGETING = '1') )"  								
							);
			}
 
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        ids.add((Number)row.getAttribute("Id"));
      }
      vo.closeRowSet();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
      logger.error("Vkladani Dennich dokladu do fronty pro men./uv. pozice:\n",e);
    }
    
    finally {
      dm.getTransaction().commit();
    }
    java.sql.Date lastDate = Utils.getLastDate();
    Iterator iter = ids.iterator();
    while(iter.hasNext()) {
      Number idSpol = (Number) iter.next();
      try {
        logger.info("M/U pro idSpol="+idSpol+",datum="+lastDate+",datumMustek="+lastDate);  
        dm.genDoklad(Constants.DENNI_USER,
                     1,
                     idSpol.intValue(),
                     2,
                     lastDate,
                     lastDate,
                     2,
                     true,
                     "0");
      } catch ( Exception e ) {
        e.printStackTrace();
        logger.error("Vkladani Denniho dokladu do fronty:\n",e);
      } 
    }

/*ZACATEK-PROZATIMNI RESENI!!!*/
/*
    logger.debug("TEMP-Start vkladani dokladu do fronty pro men./uv. pozice");
    List idsTemp = new ArrayList();
    try {
      vo.clearCache();
      vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1' AND ID_CISKATSPOL IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL);
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        idsTemp.add((Number)row.getAttribute("Id"));
      }
      vo.closeRowSet();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
      logger.error("Vkladani dokladu do fronty pro men./uv. pozice:\n",e);
    }
    finally {
      dm.getTransaction().commit();
    }
    iter = idsTemp.iterator();
    while(iter.hasNext()) {
      Number idSpol = (Number) iter.next();
      try {
        logger.info("M/U pro idSpol="+idSpol+",datum="+lastDate+",datumMustek="+lastDate);  
        dm.genDoklad(Constants.DENNI_USER,
                     1,
                     idSpol.intValue(),
                     2,
                     lastDate,
                     lastDate,
                     0,
                     true,
                     "0");
      } catch ( Exception e ) {
        e.printStackTrace();
        logger.error("Vkladani dokladu do fronty:\n",e);
      } 
    }
    
    ids.addAll(idsTemp);
*/
/*KONEC-PROZATIMNI RESENI!!!*/

    logger.debug("Konec vkladani dennich dokladu do fronty pro men./uv. pozice");

    Number[] idsArr = (Number[]) ids.toArray(new Number[] {});
    AutoMUCounter.getInstance().init(idsArr);
    MUProtokol.getInstance().init(idsArr, lastDate);
  }

  private void generujDenniMis() {
    if ( ! Utils.generujDenniDoklad(new java.util.Date()) ) {
      logger.debug("Denni se negeneruji");
      return;
    }
  
    logger.debug("Start vkladani dokladu do fronty pro MIS");
    List ids = new ArrayList();
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
/*
    try {
      vo.clearCache();
      vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1'"+ 
	  " AND ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL);
*/

try {
      vo.clearCache();
    //--esc povodna verzia
	//	vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1'"+ 
	//						" AND ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL);
	
	//--	verzia pre obmedzene generovanie na zaklade datumu, priznaku ....
	if ( Utils.generujDenniDokladAll(new java.util.Date()) )	{    
			logger.debug("Generuji se Denni pro Vsechny #300");
			vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1'"+ 
							" AND ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL);
	  }
	//esc  29.1.2008 - obmedzime denni a MIS denni doklady 
	//esc  p�es tyden generovat JEN pro spolecnosti, kter� jsou RKC a maj� budget
	  else {
			logger.debug("Generuji se Denni pro RKC a Budget #33");
			vo.setWhereClause("ID_KATEGORIE<>300 AND C_AUTOMATICKEGENEROVANI = '1'"+ 
							" AND ID_CISKATSPOL NOT IN ( "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL+" ) "+
							" AND ( C_RKC = '1' " +
							" 	    OR  		" + 
							"		ID  IN (select ID_KTGUCETNISPOLECNOST from DB_JT.KP_KTG_ODBOR where C_BUDGETING = '1') )"  								
							);
			}
 
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        ids.add((Number)row.getAttribute("Id"));
      }
      vo.closeRowSet();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
      logger.error("Vkladani dokladu do fronty pro MIS:\n",e);
    }
    finally {
      dm.getTransaction().commit();
    }
    java.sql.Date lastDate = Utils.getLastDate();
    Iterator iter = ids.iterator();
    while(iter.hasNext()) {
      Number idSpol = (Number) iter.next();
      try {
        logger.info("MIS pro idSpol="+idSpol+",datum="+lastDate+",datumMustek="+lastDate);  
        dm.genDoklad(Constants.DENNI_USER,
                     1,
                     idSpol.intValue(),
                     90,
                     lastDate,
                     lastDate,
                     0,
                     true,
                     "0");
      } catch ( Exception e ) {
        e.printStackTrace();
        logger.error("Vkladani dokladu do fronty:\n",e);
      } 
    }
    logger.debug("Konec vkladani dokladu do fronty pro MIS");
  }

  private void generujMis() {  
    logger.debug("Start vkladani dokladu do fronty pro MIS - mesicni");
    List ids = new ArrayList();
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");

    LocalDate localDate = datumDo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String strDatum = localDate.format(DATE_FORMATTER);
    try {
      vo.clearCache();
      vo.setWhereClause("ID IN ( select distinct mis.id_ktgUcetniSpolecnost "+
                               " from db_jt.kp_dat_misTransakce mis "+
                               " where mis.dt_datum between trunc(to_date('"+strDatum+"','dd.mm.yyyy'),'yyyy') and to_date('"+strDatum+"','dd.mm.yyyy') )"+
                        " AND C_AUTOMATICKEGENEROVANI = '1'");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        ids.add((Number)row.getAttribute("Id"));
      }
      vo.closeRowSet();
    }
    catch(Exception e) 
    {
      e.printStackTrace();
      logger.error("Vkladani dokladu do fronty pro MIS - mesicni:\n",e);
    }
    finally {
      dm.getTransaction().commit();
    }
    Iterator iter = ids.iterator();
    while(iter.hasNext()) {
      Number idSpol = (Number) iter.next();
      try {
        logger.info("MIS/m pro idSpol="+idSpol+",datum="+datumDo+",datumMustek="+datumMustek);  
        dm.genDoklad("MIS",
                     1,
                     idSpol.intValue(),
                     90,
                     datumDo,
                     datumMustek,
                     0,
                     true,
                     "0");
      } catch ( Exception e ) {
        e.printStackTrace();
        logger.error("Vkladani dokladu do fronty:\n",e);
      } 
    }
    logger.debug("Konec vkladani dokladu do fronty pro MIS - mesicni");
  }
  
  private String msToTime(long ms) 
  {
    long secTotal = ms/1000;
    long hrs = secTotal/3600;
    long min = (secTotal-hrs*3600)/60;
    long sec = secTotal-hrs*3600-min*60;
    return hrs+"h, "+min+"m, "+sec+"s";
  }
  
  public static void main(String[] argv) 
  {
    GenerateAll ga = GenerateAll.getInstance();
    ga.resetParams();
    ga.generujMis();
  }
}