package cz.jtbank.konsolidace.common;

import java.io.File;
import java.net.*;
//esc
//import oracle.jbo.server.ApplicationModuleImpl;
import cz.jtbank.konsolidace.doklady.common.DokladyModule;
import oracle.jbo.server.DBTransaction;
import oracle.jbo.ConnectionMetadata;
//esc
 
public class Constants 
{
  private static String hostName = null; 
  
  //esc
   /*
   cz.jtbank.konsolidace.doklady.common.DokladyModule sm =
       (cz.jtbank.konsolidace.doklady.common.DokladyModule) DokladyModule.useApplicationModule();
  //ApplicationModuleImpl sm = (ApplicationModuleImpl) ;          // muze byt libovolny je to jedno pouzivam jenom pro zisk connection
  DBTransaction dbTran = sm.getDBTransaction();
  ConnectionMetadata ConData =  dbTran.getConnectionMetadata();
  
  public String DBName =    ConData.getJdbcURL();
  *///esc
  
  public static String getHostName() 
  {
    try {
      if(hostName==null) hostName = java.net.InetAddress.getLocalHost().getHostName();
    }
    catch (UnknownHostException uhe) {}
    
    return hostName;
  }
  
  public static final String PROJECT_PATH = "konsolidace";

  /**
   * MIGRATION NOTE (2025-12-05): Replaced hardcoded Windows paths with PathConstants.
   * Original getDisk() method returned "D:\\" or "C:\\" based on hostname.
   * Now uses platform-independent paths from PathConstants helper class.
   * @deprecated Use PathConstants methods instead
   */
  @Deprecated
  private static String getDisk() {
    // Legacy method - maintained for backward compatibility
    // Returns base path with platform-specific separator
    String ret = PathConstants.getRootPath() + File.separator;
    System.out.println("INFO: Using platform-independent base path: " + ret);
    return ret;
  }

  // MIGRATED: Platform-independent paths using PathConstants
  public static String ROOT_FILES_PATH        = PathConstants.getRootPath() + File.separator;
  public static String XLS_FILES_PATH         = PathConstants.getXlsPath() + File.separator;
  public static String CSV_FILES_PATH         = PathConstants.getCsvPath() + File.separator;
  public static String EXPORT_FILES_PATH      = PathConstants.getExportPath() + File.separator;
  public static String PROTOKOL_FILES_PATH    = PathConstants.getProtokolPath() + File.separator;
  public static String SABLONY_FILES_PATH     = PathConstants.getSablonyPath() + File.separator;
  public static String SABLONY_CARTESIS       = PathConstants.getSablonyCartesisPath() + File.separator;
  public static String DOC_FILES_PATH         = PathConstants.getDocFilesPath() + File.separator;
  public static String EVI_FILES_PATH         = PathConstants.getEviFilesPath() + File.separator;
  public static String ARCHIV_FILES_PATH      = PathConstants.getArchivPath() + File.separator;
  
  // MIGRATED: Platform-independent directory separators
  public static String DIR_TEMP               = PathConstants.getDirTemp();
  public static String DIR_DATA               = PathConstants.getDirData();
  public static String DIR_ADMIN_NAKLADY      = DIR_TEMP+"adminNaklady";
  public static String DIR_BILANCE_DETAIL     = DIR_TEMP+"bilanceDetail";
  public static String DIR_BUDGET_PREKROCENI  = DIR_TEMP+"budPres";
  public static String DIR_BUDGET_SCHVALOVANI = DIR_TEMP+"budSchval";
  public static String DIR_DOKLADY_DETAIL     = DIR_TEMP+"dokladyDetail";
  public static String DIR_EVI_OR             = DIR_TEMP+"eviOr";
  public static String DIR_CHYBY_MUSTKU       = DIR_TEMP+"chybyMustku";
  public static String DIR_KAP_OR             = DIR_TEMP+"kap";
  public static String DIR_KONS_SPRAVCE       = DIR_TEMP+"konsSpravce";
  public static String DIR_MPOP               = DIR_TEMP+"mpop";
  public static String DIR_MUSTKY             = DIR_TEMP+"mustky";
  public static String DIR_PB_KLIENTI         = DIR_TEMP+"pbKlientiA";
  public static String DIR_PROJEKTY_KATRA     = DIR_TEMP+"projektKarta";
  public static String DIR_PROJEKTY_SLDEV     = DIR_TEMP+"projektySLDev";
  public static String DIR_PROJEKTY_TRANSAKCE = DIR_TEMP+"projektyTran";
  public static String DIR_PROTI_GROUP        = DIR_TEMP+"protiGroup";
  public static String DIR_SL                 = DIR_TEMP+"slOutput";
  public static String DIR_SL_POSTUP          = DIR_TEMP+"slPostup";
  public static String DIR_SPOL_PREDAV        = DIR_TEMP+"spolPredav";
  public static String DIR_UNIFIKOVANE_UCTY   = DIR_TEMP+"unifUcty";
  public static String DIR_ZAMEK_GENEROVANI   = DIR_TEMP+"zamekGen";
  public static String DIR_ZMENY_PROTISTRAN   = DIR_TEMP+"zmenyProtistran";
  public static String DIR_PODNIKATEL_UCTY    = DIR_TEMP+"podnikatelUcty";
  public static String DIR_ZAMEK_PROTOKOL     = DIR_TEMP+"zamekProtokol";
  public static String DIR_ZMENA_MAJ_UCAST    = DIR_TEMP+"zmenaMU";

  public static String DIR_BUDGET_STD         = DIR_DATA+"budget";
  public static String DIR_BUDGET_NAKLAD      = DIR_DATA+"budgetMustekNaklad";
  public static String DIR_BUDGET_PROJEKT     = DIR_DATA+"budgetProjekt";
  public static String DIR_EMISE              = DIR_DATA+"emise";
  public static String DIR_IFRS_ZMENY         = DIR_DATA+"ifrszmeny";
  public static String DIR_DOKLAD_KAMIL       = DIR_DATA+"kamil";
  public static String DIR_SUBKONSOLIDACE     = DIR_DATA+"konsolidace";
  public static String DIR_KONSOLIDACNI_ZMENY = DIR_DATA+"konszmeny";
  public static String DIR_MAJETKOVE_UCASTI   = DIR_DATA+"majetek";
  public static String DIR_ODBORY             = DIR_DATA+"odbory";
  public static String DIR_POZICE_MU          = DIR_DATA+"pozice_";
  public static String DIR_PROJEKTY_DOKLADY   = DIR_DATA+"projektDoklad";
  public static String DIR_PROJEKTY           = DIR_DATA+"projekty";
  public static String DIR_PROJEKTY_CF        = DIR_DATA+"projektyCF";
  public static String DIR_UVERY              = DIR_DATA+"projektyUvery";
  public static String DIR_PROTI_OSOBY        = DIR_DATA+"protiOsoby";
  public static String DIR_SPOLECNOSTI        = DIR_DATA+"spolecnosti";
  public static String DIR_CARTESIS           = DIR_DATA+"CARTESIS";


  // MIGRATED: Platform-independent path separator
  public static String DIR_POZICE_MU_LOGS     = DIR_DATA+"muProtokol"+File.separator+"pozice_";

  public static final String[] KATEGORIE = {"ID_KATEGORIE IN (10,20)"/*0=CZ*/,
                                            "ID_KATEGORIE = 120"/*1=SK*/,
                                            "ID_KATEGORIE = 220"/*2=ZAHR*/,
                                            "ID_KATEGORIE = 30"/*3=MAN CZ*/,
                                            "ID_KATEGORIE = 130"/*4=MAN SK*/,
                                            "ID_KATEGORIE = 230"/*5=MAN ZAHR*/,
                                            "ID_KATEGORIE IN (10,20,120,220)"/*6=Specific*/,
                                            "ID_KATEGORIE IN (10,20,120,220)"/*7=ALL*/
                                            };
  
  public static final int SUMA_Z_S_LISTNR           = 1;
  public static final int SUMA_Z_S_ROWNR            = 129;
  public static final short SUMA_Z_S_COLNR          = 2;
  
// public static final short ROW_PODNIKATEL_PASIVA    = 66; // radek od ktereho jsou pasiva na smichanem listu aktiv a pasiv , dokld nl_krok = 10
  public static final short ROW_PODNIKATEL_PASIVA    = 67; // radek od ktereho jsou pasiva na smichanem listu aktiv a pasiv , dokld nl_krok = 10
  
  public static final int USERS_READ_BILANCE        = 1;
  public static final int USERS_GEN_BILANCE         = 2;
  
  public static final String AUTOMAT_USER     = "automat";
  public static final String DENNI_USER       = "denni";
  
  public static final String ADMIN_USER       = "a";
  public static final String MASTER_BUDGET	  = "JT8100001";		// Milos Badida - hlavny schvalovatel navyseni budgetov
  
  //public static int MAX_POCET_RADKU_EXCEL = 50000;
  public static int MAX_POCET_RADKU_EXCEL = 65535;
  
  public static final int ALL = 0;
  public static final int UCETNI = 1;
  public static final int OO = 2;
  public static final int TOP = 6;
  public static final int NADRIZENA_UCETNI = 4;
  public static final int SPRAVCE = 5;
  public static final int SEF_SEGMENTU = 3;
  public static final int ALL_SPECIFIC = 10;
  public static final int SKUPINY = 20;
  public static final int MUSTEK = 30;
  public static final int UNIF_UCET = 40;
  
  public static final String ERR_MESSAGE_ONLY = "Error message only!";
  
/**/  
  public static final String[] kym = new String[]
//    {"","zodpov�dnou ��etn�","odpov�dnou osobou","��fem segmentu"};
      {"","zodpov�dnou ��etn�","odpov�dnou osobou","odpov�dnou osobou holdingu"};
/**/
    
//  public static final String WHERE_SPOL_DOC = "ID_KATEGORIE<>300 AND C_EXTSYSTEM<>'M' AND DT_DATUMARCHIVACE IS NULL AND ID_CISKATSPOL<>5 AND ID_CISKATSPOL<>10";
  public static final String WHERE_SPOL_DOC = "C_DOCDISPLAY = '1'";
  
  public static final String DIS_CISKATSPOL = "(SELECT ID FROM DB_JT.KP_CIS_KATSPOL WHERE C_VYRADIT = '1')";
  
  // MIGRATED: Platform-independent JAZN XML path
  // Windows: C:\Oracle\product\j2ee10\j2ee\OC4J_app\application-deployments\kis\jazn-data.xml
  // Linux:   /opt/oracle/j2ee10/j2ee/OC4J_app/application-deployments/kis/jazn-data.xml
  // Can be overridden via system property: -Dkis.paths.jazn.xml=/custom/path/jazn-data.xml
  public static final String JAZN_XML_FILE    = PathConstants.getJaznXmlPath();
  public static final String[] KIS_ADMINS = ("tweek".equalsIgnoreCase(getHostName())) ? 
                                             new String[] {
                                                          "stastny@jtfg.com",     /*SmrM 07.11.2025*/
                                                          "smrecek@jtbank.cz",    /*esc 24.10 2020*/
                                                          "db_admin@jtbank.cz"} :
                                             new String[] {"stastny@jtfg.com",    /*SmrM 07.11.2025*/
                                                          "smrecek@jtbank.cz"};   /*esc 24.10 2020*/

  public static final String KONTROLING_SK = ("tweek".equalsIgnoreCase(getHostName())) ? "controllingSR@jtfg.sk" : "scholtz@jtbank.cz";
  
  public static final int DNI_UCETNI = 15;
  public static final int DNI_OO = 20;
  public static final int DNI_TOP = 25;
}