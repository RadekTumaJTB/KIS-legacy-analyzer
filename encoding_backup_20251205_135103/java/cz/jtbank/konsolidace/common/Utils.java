package cz.jtbank.konsolidace.common;

import cz.jtbank.konsolidace.admin.*;
import cz.jtbank.konsolidace.admin.common.*;
import java.net.*;
import java.util.Date;
import java.util.Calendar;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.http.*;
import oracle.jbo.*;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.domain.Number;
import oracle.jbo.server.DBTransaction;
import java.sql.*;
import java.io.*;
import oracle.jbo.server.DBTransaction;

/*esc*/
import cz.jtbank.konsolidace.admin.KpParametryViewImpl.*;


public class Utils 
{
//esc

/**
   * 
   * Container's getter for KpParametryView1
   */
/*
 static KpParametryViewImpl getKpParametryView1()
  {
    return (KpParametryViewImpl)findViewObject("KpParametryView1");
  }
*/
//esc  






  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  public static int restartRunning = 0;


  public static java.sql.Date getDate ( int den, int mesic, int rok ) 
  {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, den);
    cal.set(Calendar.MONTH, mesic-1);
    cal.set(Calendar.YEAR, rok);
    return new java.sql.Date(cal.getTimeInMillis());
  }


  public static java.sql.Date getLastDateMBAsDate() 
  {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH,-1);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    return new java.sql.Date(cal.getTimeInMillis());
  }

  public static String getLastDateMB()
  {
    LocalDate date = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
    return date.format(DATE_FORMATTER);
  }

  public static String getLastDateNextMonth()
  {
    LocalDate date = LocalDate.now().plusMonths(1).withDayOfMonth(LocalDate.now().plusMonths(1).lengthOfMonth());
    return date.format(DATE_FORMATTER);
  }

  public static String getTodaysDate()
  {
    return LocalDate.now().format(DATE_FORMATTER);
  }

  public static String date2String(java.util.Date d)
  {
    LocalDate localDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return localDate.format(DATE_FORMATTER);
  }


  public static java.sql.Date getLastDate() 
  {
    Calendar cal = Calendar.getInstance();
    if(cal.get(Calendar.HOUR_OF_DAY) < 18) {
      cal.add(Calendar.DAY_OF_YEAR,-1);
    }
    cal.set(Calendar.HOUR_OF_DAY,0);
    cal.set(Calendar.MINUTE,0);
    cal.set(Calendar.SECOND,0);
    cal.set(Calendar.MILLISECOND,0);
    return new java.sql.Date(cal.getTimeInMillis());
  }

  public static String getLastDateAsString()
  {
    return date2String(getLastDate());
  }
  
  public static int getNumber(DBTransaction tran, String stm)
  {
    int ret = 0;
    try (Statement st = tran.createStatement(1))
    {
      st.execute(stm);
      try (ResultSet rs = st.getResultSet()) {
        rs.next();
        ret = rs.getInt(1);
      }
    }
    catch (Exception e)
    {
      //zatim
      e.printStackTrace();
    }
    return ret;
  }
  
  private static String convertUserName(String user, boolean nameOnly) 
  {
    if(user == null) 
    {
      user = "!!!CHYBA!!!";
    }
    else if(nameOnly) 
    {
      user = user.substring(user.indexOf('/')+1);
      int zavinac = user.indexOf('@');
      if(zavinac>0) user = user.substring(0,zavinac);
    }

    return user;
  }
  
  public static String getUserName(HttpServletRequest request, boolean nameOnly) 
  {
    if(request == null || request.getUserPrincipal()==null) return  "!!!CHYBA!!!";
    String user = request.getUserPrincipal().getName();
    return convertUserName(user, nameOnly);
  }
  
  public static String getUserName(ApplicationModule am, boolean nameOnly) 
  {
    if(am == null) return  "!!!CHYBA!!!";
    String user = ((ApplicationModuleImpl) am).getUserPrincipalName();
    return convertUserName(user, nameOnly);
  }
  
  private static String inetAdr = "tweek.pa.jtfg.com";
  
  public static String getInetAddress() 
  {
    if(inetAdr==null) 
    {
      try {
        inetAdr = InetAddress.getLocalHost().getCanonicalHostName();
      } catch (UnknownHostException e) {}
    }
    
    return inetAdr;
  }

  public static String getLocale(HttpSession session) 
  {
    String loc = (String) session.getAttribute("kis.locale");
    if(loc == null) 
    {
      loc = "cs_CZ";
      session.setAttribute("kis.locale", loc);
    }
    return loc;
  }

  public static void setLocale(HttpSession session, String loc) 
  {
    session.setAttribute("kis.locale", loc);
  }
  
  public static String getWhereDokladIds(java.sql.Date date, int ucSkup)
  {
    String dateString = date2String(date);
    String where = null;
    if(ucSkup == -2) //banka
    {
      where = "SELECT D.ID " +
              "FROM DB_JT.KP_DAT_DOKLAD D, " +
                   "DB_JT.KP_KTG_UCETNISPOLECNOST S " +
              "WHERE D.ID_KTGUCETNISPOLECNOST = S.ID " +
                "AND S.ID IN (1,5000) " +
                "AND D.DT_DATUM = TO_DATE('"+dateString+"','DD.MM.YYYY') " +
                "AND D.NL_KROK IN (1,2) AND D.C_BASEDOKLAD = '1'";
    }
    else if(ucSkup == -1) 
    {
      where = "SELECT D.ID "+
              "FROM DB_JT.KP_DAT_DOKLAD D "+
              "WHERE db_jt.f_jeSpolecnostClenKonsolidace(D.ID_KTGUCETNISPOLECNOST, 10001, TO_DATE('"+dateString+"','DD.MM.YYYY'))=1 "+
                "AND D.DT_DATUM = TO_DATE('"+dateString+"','DD.MM.YYYY') "+
                "AND D.NL_KROK IN (1,2) AND D.C_BASEDOKLAD = '1' ";
    }
    else if (ucSkup == 0) 
    {
      where = "SELECT D.ID " +
              "FROM DB_JT.KP_DAT_DOKLAD D " +
              "WHERE D.DT_DATUM = TO_DATE('"+dateString+"','DD.MM.YYYY') " +
                "AND D.NL_KROK IN (1,2) AND D.C_BASEDOKLAD = '1'";
    }
    else //ucSkup=1 .. RKC
    {
      where = "select distinct d.id "+
              "from db_jt.kp_rel_subkonsolidaceclen c, "+
                   "db_jt.kp_ktg_subkonsolidace s, "+
	                 "db_jt.kp_ktg_ucetniSpolecnost us, "+
	                 "DB_JT.KP_DAT_DOKLAD D "+
              "where us.id = c.id_ktgUcetniSpolecnost "+
                "and s.id_ktgUcetniSpolecnost = c.id_ktgsubkonsolidace "+
                "and c.c_clen = '1' "+
                "and c.ID_CISSUBTYPCLENSTVI <> 3 "+ //? = 1 - pozdeji se mozna zmeni dle pozadavku zda jen 100% nebo i min
                "and s.id_ktgUcetniSkupina = "+ucSkup+" "+
                "and D.ID_KTGUCETNISPOLECNOST = US.ID "+
                "AND D.DT_DATUM = TO_DATE('"+dateString+"','DD.MM.YYYY') "+
                "AND (D.DT_DATUM >= C.DT_ClenstviOD OR C.DT_ClenstviOD IS NULL) "+
                "AND (D.DT_DATUM <= C.DT_ClenstviDO OR C.DT_ClenstviDO IS NULL) "+
                "AND D.NL_KROK IN (1,2) AND D.C_BASEDOKLAD = '1'";
/*
      where = "SELECT D.ID " +
              "FROM DB_JT.KP_DAT_DOKLAD D, " +
                   "DB_JT.KP_REL_UCETNISKUPINACLEN C " +
              "WHERE D.ID_KTGUCETNISPOLECNOST = C.ID_KTGUCETNISPOLECNOST " +
                "AND C.ID_KTGUCETNISKUPINA = "+ucSkup+" " +
                "AND D.DT_DATUM = TO_DATE('"+dateString+"','DD.MM.YYYY') " +
                "AND (D.DT_DATUM >= C.DT_PLATNOSTOD OR C.DT_PLATNOSTOD IS NULL) " +
                "AND (D.DT_DATUM <= C.DT_PLATNOSTDO OR C.DT_PLATNOSTDO IS NULL) " +
                "AND D.NL_KROK = 1 " +
                "AND c.ID_CISSUBTYPCLENSTVI <> 3 " +
                "AND TO_DATE('"+dateString+"','DD.MM.YYYY') BETWEEN NVL(C.DT_PLATNOSTOD, TO_DATE('01.01.2004','DD.MM.YYYY')) AND " +
                                                                   "NVL(C.DT_PLATNOSTDO, TO_DATE('01.01.2999','DD.MM.YYYY')) ";
*/
    }
    return where;
  }
  
  public static String getDocSchvaleno() 
  {
  /* najde NESCHVALENE SL ... !!! */
/*    String where = "(EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK DR "+
		                        "WHERE VWDATSCHVALOVAK.ID = DR.ID_DOKUMENT AND "+
		                            "((DR.DT_SCHVALENOODBOR IS NULL AND ID_ODBOROO IS NOT NULL) OR "+
			                           "(DR.DT_SCHVALENOPROJEKT IS NULL AND ID_PROJEKTMAN IS NOT NULL) OR "+
			                           "(DR.DT_SCHVALENOUCSPOL IS NULL AND ID_UCSPOLOO IS NOT NULL) OR "+
			                           "(DR.DT_SCHVALENOTOP IS NULL AND ID_UCSPOLTOP IS NOT NULL))) "+
                   "OR (SELECT COUNT(*) FROM DB_JT.KP_DAT_DOKUMENTRADEK DR WHERE VWDATSCHVALOVAK.ID = DR.ID_DOKUMENT) = 0"+
                   "OR (ID_GESTOR IS NOT NULL AND DT_GESTORSCHVALENO IS NULL))";*/
    
	String where = "(EXISTS (SELECT NULL FROM DB_JT.KP_DAT_DOKUMENTRADEK DR,DB_JT.KP_DAT_DOKUMENT DOC "+  /*--esc pridane 20.08 2008 */
		                        "WHERE DOC.id = DR.id_dokument and DOC.id_cisstatus != 2 and VWDATSCHVALOVAK.ID = DR.ID_DOKUMENT AND "+
		                            "((DR.DT_SCHVALENOODBOR IS NULL AND ID_ODBOROO IS NOT NULL) OR "+
			                           "(DR.DT_SCHVALENOPROJEKT IS NULL AND ID_PROJEKTMAN IS NOT NULL) OR "+
			                           "(DR.DT_SCHVALENOUCSPOL IS NULL AND ID_UCSPOLOO IS NOT NULL) OR "+
			                           "(DR.DT_SCHVALENOTOP IS NULL AND ID_UCSPOLTOP IS NOT NULL))) "+
                   "OR (SELECT COUNT(*) FROM DB_JT.KP_DAT_DOKUMENTRADEK DR WHERE VWDATSCHVALOVAK.ID = DR.ID_DOKUMENT) = 0 "+
                   "OR (ID_GESTOR IS NOT NULL AND DT_GESTORSCHVALENO IS NULL)"+
				   "OR (ID_NAVYSENI_MB IS NOT NULL AND DT_NAVYSENI_SCHVALENO  IS NULL) )" ; /*--esc pridane 18.06 2009 */ 
    return where;
  }
/*
  public static String getDocSchvaleno(int userId) 
  {
    String where = "(EXISTS (SELECT NULL FROM DB_JT.VW_DAT_SCHVALOVAKRADEK DR "+
		                        "WHERE VWDATSCHVALOVAK.ID = DR.ID_DOKUMENT AND "+
		                            "((DR.DT_SCHVALENOODBOR IS NULL AND ID_ODBOROO = "+userId+") OR "+
			                           "(DR.DT_SCHVALENOPROJEKT IS NULL AND ID_PROJEKTMAN = "+userId+") OR "+
			                           "(DR.DT_SCHVALENOUCSPOL IS NULL AND ID_UCSPOLOO = "+userId+") OR "+
			                           "(DR.DT_SCHVALENOTOP IS NULL AND ID_UCSPOLTOP = "+userId+"))) "+
                   "OR (SELECT COUNT(*) FROM DB_JT.VW_DAT_SCHVALOVAKRADEK DR WHERE VWDATSCHVALOVAK.ID = DR.ID_DOKUMENT) = 0)";
    
    return where;
  }
*/
  public static String getDocPrefakt() 
  {
    String where = "EXISTS (SELECT NULL FROM DB_JT.VW_DAT_SCHVALOVAKRADEK DR "+
		                       "WHERE VWDATSCHVALOVAK.ID = DR.ID_DOKUMENT AND "+
			                           "VWDATSCHVALOVAK.ID_KTGUCETNISPOLECNOST <> DR.ID_KTGUCETNISPOLECNOST "+
                                 "AND DR.C_KPREFAKT = '1')";
    
    return where;
  }
  
  public static String fileExists(int idDoc) 
  {
    File dir = new File(Constants.DOC_FILES_PATH);
    String[] list = dir.list();
    for(int i=0; i<list.length; i++) {
      if(list[i].startsWith(idDoc+".")) 
      {
        return list[i];
      }
    }
    return null;
  
  }
  
  public static String getWhereKamilRadek(int idRadek) {
    String wherePrehled = null;
    if(idRadek==10000) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10001 AND 10007";
    else if(idRadek==10008) wherePrehled = " AND (ID_CISRADEKTYP BETWEEN 10009 AND 10012 OR ID_CISRADEKTYP BETWEEN 10014 AND 10019)";  
    else if(idRadek==10013) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10014 AND 10019";  
    else if(idRadek==10102) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10100 AND 10101";
    else if(idRadek==10105) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10103 AND 10104";
    else if(idRadek==10110) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10106 AND 10109";
    else if(idRadek==10115) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10111 AND 10114";
    else if(idRadek==10116) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10100 AND 10114";
    else if(idRadek==10118) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10100 AND 10117";
    else if(idRadek==10120) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10100 AND 10119";
    else if(idRadek==10122) wherePrehled = " AND ID_CISRADEKTYP BETWEEN 10100 AND 10121";
    else wherePrehled = " AND ID_CISRADEKTYP = "+idRadek;
    return wherePrehled;
  }
  
  /*
   * idDokladu pro pripad budouci zmeny radku hospodarskeho vysledku
   */
  public static String getWhereHospodarskyVysledek(int idDoklad) 
  {
    String ret = " and nl_radek = 123 and nl_poradiList = 2 ";

    return ret;
  }

  /*
   * idDokladu pro pripad budouci zmeny radku kurzovych rozdilu
   */
  public static String getWhereKurzoveRozdily(int idDoklad) 
  {
    String ret = " and nl_radek = 53 and nl_poradiList = 5 ";

    return ret;
  }
/*  
  public static boolean isTester(String user) 
  {
    user = user.substring(user.indexOf('/')+1);
    if(Constants.ADMIN_USER.equalsIgnoreCase(user)) return true;
    else if("jitka".equalsIgnoreCase(user)) return true;
    return false;
  }
*/  

/*esc Neomezeny set generuje jenom v patek*/  
  public static boolean generujDenniDokladAll ( Date d ) {
    boolean ret = false;
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    int denOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
    switch ( denOfWeek ) {
      case 6: // patek
        if ( hourOfDay > 16  )
					ret = true; 
					//break;
    }
//	  if (	  //cz.jtbank.konsolidace.admin.KpParametryViewRowImpl	  getCNgPovoleno())	  )
//			{ 		ret = true; 			}

//esc 15.02.2008
//	ret = true; //docasne zapnem vsetko denne doklady pre vsetkych
/*    ViewObject vo = getKpParametryView1();
    vo.clearCache();
    String DenniAll = null;
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      DenniAll = (String) row.getAttribute("CMENUMUBANKA");
    }
    vo.closeRowSet();
*/
//
    return ( ret );
  }

  public static boolean generujDenniDoklad ( Date d ) {
    boolean ret = true;
     
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    int denOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
//esc 08.02.2008 - povolim generovanie vzdy teda aj cez vikend      
    switch ( denOfWeek ) {
      case 1:  // nedele
        ret = false; break;
      case 2: // pondeli
        if ( hourOfDay < 18 ) ret = false; 
        break;
	  case 7: // sobota
        if ( hourOfDay > 12 ) ret = false; 
        break;
    }
    return ( ret );
  }

//nvl  is_null ;-)
    public static String nvl(String ret) 
    {
      if (ret == null) ret = "";        
      return ret;
    }



  public static void main(String[] argv) 
  {
    //System.out.println(getLastDateMB());
    //System.out.println( getInetAddress() );
    //System.out.println( getDate ( 31, 12, 2007 ) );
    System.out.println( getTodaysDate() );
    //Date d = Utils.getDate(31,8,2007);
    Date d = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    int denOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
    System.out.println( Utils.date2String(d ) + "  " + denOfWeek + "   " + hourOfDay + "   " + Utils.generujDenniDoklad(new Date()) );
  }

  public static boolean isEmpty(Object o)
  {
    if (o == null )
    {
      return true;
    }
    if (o instanceof String && ((String)o).length() == 0)
    {
      return true;
    }
    return false;
  }
  
  public static String getString(Object o)
  {
    if (o == null )
    {
      return "";
    }
    if (o instanceof String)
    {
      return (String)o;
    }
    return o.toString();
  }

}