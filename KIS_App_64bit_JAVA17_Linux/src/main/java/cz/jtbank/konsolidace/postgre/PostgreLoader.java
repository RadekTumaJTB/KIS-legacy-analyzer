package cz.jtbank.konsolidace.postgre;

import java.sql.*;
import java.util.Properties;
import cz.jtbank.konsolidace.common.KisException;

import oracle.jbo.*;
import cz.jtbank.konsolidace.pb.common.PbModule;

public class PostgreLoader 
{
  public void loadFromPostgre(PbModule pm) throws KisException 
  {
/*
    String SQL = "SELECT convert(osoby.privatni_banker,'win1250','utf8'), "+
                        "osoby.osoba_id, "+
                        "trim(trim(convert(coalesce(osoby.titul,NULL,''),'win1250','utf8')||' '||convert(osoby.jmeno,'win1250','utf8')||' '||convert(osoby.prijmeni,'win1250','utf8'))), "+
                        "osoby.rc, "+
                        "osoby.scp_klient_id, "+
                        "convert(osoby.ulice,'win1250','utf8'), "+
                        "convert(osoby.mesto,'win1250','utf8'), "+
                        "osoby.psc, "+
                        "osoby.cizinec_rc_ico "+
                 //"select count(*) "+
                 "FROM osoby osoby "+
                 "WHERE (osoby.privatni_banker Is Not Null) "+
                 "ORDER BY osoby.jmeno";
*/
    String SQL = "SELECT osoby.privatni_banker, "+
                        "osoby.osoba_id, "+
                        "trim(trim(coalesce(osoby.titul,NULL,'')||' '||osoby.jmeno||' '||osoby.prijmeni)), "+
                        "osoby.rc, "+
                        "osoby.scp_klient_id, "+
                        "osoby.ulice, "+
                        "osoby.mesto, "+
                        "osoby.psc, "+
                        "osoby.cizinec_rc_ico "+
                 "FROM osoby osoby "+
                 "WHERE (osoby.privatni_banker Is Not Null) "+
                 "ORDER BY osoby.jmeno";

    Properties props = new Properties();
    props.setProperty("user","turzikova");
    props.setProperty("password","XuzOzorW");
    //props.setProperty("ssl","false");
    //props.setProperty("charSet","UTF-8");
    try {
      Class.forName("org.postgresql.Driver");
    }
    catch (ClassNotFoundException e) {
      throw new KisException("PostgreSQL JDBC Driver not found", e);
    }

    try (Connection con = DriverManager.getConnection("jdbc:postgresql://192.168.2.25:5432/jtam",props);
         Statement st = con.createStatement()) {
//      st.execute("SET client_encoding = 'UNICODE';");
      st.execute("SET client_encoding to unicode;");

      try (ResultSet rs = st.executeQuery(SQL)) {
        boolean first = true;

        while(rs.next())
        {
          if(first) { first=false; pm.deleteAllEPerspektiva(); }
          int idOsoba = rs.getInt(2);
          String banker = rs.getString(1);
          String ico = rs.getString(4);
          String nazev = rs.getString(3);
          String scp = rs.getString(5);
          String ulice = rs.getString(6);
          String mesto = rs.getString(7);
          String psc = rs.getString(8);
          String ico2 = rs.getString(9);

          pm.insertEPerspektiva(idOsoba,
                                banker,
                                ico,
                                nazev,
                                scp,
                                ulice,
                                mesto,
                                psc,
                                ico2);
        }
      }
    }
    catch (Exception s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo volďż˝nďż˝ procedury loadFromPostgre",s);
    }
  }
  
  public static void main(String[] argv)
  {
    PbModule pm = (PbModule) oracle.jbo.client.Configuration.createRootApplicationModule("cz.jtbank.konsolidace.pb.PbModule","PbModuleLocal");

    PostgreLoader l = new PostgreLoader();
    try {
      l.loadFromPostgre(pm);
    } catch (Exception e) 
    {
      e.printStackTrace();
    }
  }
}