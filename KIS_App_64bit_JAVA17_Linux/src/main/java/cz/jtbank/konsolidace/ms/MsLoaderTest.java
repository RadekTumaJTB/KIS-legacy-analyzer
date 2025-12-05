package cz.jtbank.konsolidace.ms;

import java.util.*;
import java.sql.*;
import cz.jtbank.konsolidace.common.*;

public class MsLoaderTest 
{
  public void loadFromMs() throws KisException 
  {
    String SQL =
      "SELECT 'INSERT INTO kp_dat_eviBanky (id,s_poznamka,dt_platnostod,dt_platnostdo,id_adminspolecnost) "+
      "VALUES ('+convert(varchar,pb_KlientiInfo.id)+','''+substring(pb_KlientiInfo.hodnota,0,249)+''',to_date('''+convert(varchar,pb_KlientiInfo.dat_od,103)+''',''dd/mm/yyyy''),to_date('''+isnull(convert(varchar,pb_KlientiInfo.dat_do,103),'01/01/2999')+''',''dd/mm/yyyy''),'+convert(varchar,pb_KlientiInfo.idklient)+');' "+
      "FROM JTintranet.dbo.pb_KlientiInfo pb_KlientiInfo "+
      "WHERE (pb_KlientiInfo.idtyp=6 AND pb_KlientiInfo.vor=0)";
/*    
      "SELECT 'INSERT INTO kp_ktg_eviStatutarniZastupce (id,id_typ,s_poznamka,dt_platnostod,dt_platnostdo,id_adminspolecnost) "+
      "VALUES ('+convert(varchar,pb_KlientiInfo.id)+',2,'''+substring(pb_KlientiInfo.hodnota,0,249)+''',to_date('''+convert(varchar,pb_KlientiInfo.dat_od,103)+''',''dd/mm/yyyy''),to_date('''+isnull(convert(varchar,pb_KlientiInfo.dat_do,103),'01/01/2999')+''',''dd/mm/yyyy''),'+convert(varchar,pb_KlientiInfo.idklient)+');' "+
      "FROM JTintranet.dbo.pb_KlientiInfo pb_KlientiInfo "+
      "WHERE (pb_KlientiInfo.idtyp=3 AND pb_KlientiInfo.vor=0)";
*/
/*    
      "SELECT 'INSERT INTO KP_DAT_EVIOBCHODNIREJSTRIK (id,id_cisObchodniRejstrik,dt_datum,id_adminspolecnost) "+
      "VALUES ('+convert(varchar,pb_KlientiInfo.id)+','+convert(varchar,pb_KlientiInfo.idtyp)+',to_date('''+convert(varchar,pb_KlientiInfo.dat_od,103)+''',''dd/mm/yyyy''),'+convert(varchar,pb_KlientiInfo.idklient)+');' "+
      "FROM JTintranet.dbo.pb_KlientiInfo pb_KlientiInfo "+
      "WHERE (pb_KlientiInfo.idtyp<7 AND pb_KlientiInfo.vor=1)";
*/
/*    
      "SELECT 'INSERT INTO KP_DAT_EVIDOKUMENT (id,dt_platnostod,dt_platnostdo,s_poznamka,id_adminspolecnost) "+
      "VALUES ('+convert(varchar,pb_KlientiInfo.id)+',to_date('''+convert(varchar,pb_KlientiInfo.dat_od,103)+''',''dd/mm/yyyy''),to_date('''+isnull(convert(varchar,pb_KlientiInfo.dat_do,103),'01/01/2999')+''',''dd/mm/yyyy''),'''+substring(pb_KlientiInfo.hodnota,0,249)+''','+convert(varchar,pb_KlientiInfo.idklient)+');' "+
      "FROM JTintranet.dbo.pb_KlientiInfo pb_KlientiInfo "+
      "WHERE (pb_KlientiInfo.idtyp=9 AND pb_KlientiInfo.vor=0)";
*/
/*    
      "SELECT 'INSERT INTO KP_DAT_EVIKOMISIONARSKESMLOUVY (id,dt_platnostod,dt_platnostdo,s_poznamka,id_adminspolecnost) "+
      "VALUES ('+convert(varchar,pb_KlientiInfo.id)+',to_date('''+convert(varchar,pb_KlientiInfo.dat_od,103)+''',''dd/mm/yyyy''),to_date('''+isnull(convert(varchar,pb_KlientiInfo.dat_do,103),'01/01/2999')+''',''dd/mm/yyyy''),'''+substring(pb_KlientiInfo.hodnota,0,249)+''','+convert(varchar,pb_KlientiInfo.idklient)+');' "+
      "FROM JTintranet.dbo.pb_KlientiInfo pb_KlientiInfo "+
      "WHERE (pb_KlientiInfo.idtyp=7 AND pb_KlientiInfo.vor=0)";
*/
/*    
      "SELECT 'INSERT INTO KP_DAT_EVIKORESPONDENCE (id,dt_platnostod,dt_platnostdo,s_poznamka,id_adminspolecnost) "+
      "VALUES ('+convert(varchar,pb_KlientiInfo.id)+',to_date('''+convert(varchar,pb_KlientiInfo.dat_od,103)+''',''dd/mm/yyyy''),to_date('''+isnull(convert(varchar,pb_KlientiInfo.dat_do,103),'01/01/2999')+''',''dd/mm/yyyy''),'''+substring(pb_KlientiInfo.hodnota,0,249)+''','+convert(varchar,pb_KlientiInfo.idklient)+');' "+
      "FROM JTintranet.dbo.pb_KlientiInfo pb_KlientiInfo "+
      "WHERE (pb_KlientiInfo.idtyp=5 AND pb_KlientiInfo.vor=0)";
*/
/*
      "SELECT 'INSERT INTO KP_DAT_EVIZPLNOMOCNENI (id,dt_platnostod,dt_platnostdo,s_poznamka,id_adminspolecnost) "+
      "VALUES ('+convert(varchar,pb_KlientiInfo.id)+',to_date('''+convert(varchar,pb_KlientiInfo.dat_od,103)+''',''dd/mm/yyyy''),to_date('''+isnull(convert(varchar,pb_KlientiInfo.dat_do,103),'01/01/2999')+''',''dd/mm/yyyy''),'''+substring(pb_KlientiInfo.hodnota,0,249)+''','+convert(varchar,pb_KlientiInfo.idklient)+');' "+
      "FROM JTintranet.dbo.pb_KlientiInfo pb_KlientiInfo "+
      "WHERE (pb_KlientiInfo.idtyp=2 AND pb_KlientiInfo.vor=0)";
*/
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
      con = DriverManager.getConnection("jdbc:microsoft:sqlserver://192.168.100.1:8081","pb_praha","QXzcER19");
      st = con.createStatement();
      rs = st.executeQuery(SQL);
      while(rs.next()) 
      {
        String text = rs.getString(1);
        System.out.println(text);
      }
    }
    catch (Exception s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo volání procedury loadFromMs()",s);
    }
    finally {
      try {
        if(rs != null) rs.close();
        if(st != null) st.close();
        if(con != null) con.close();
      } 
      catch (SQLException s) { /* ignore */}
    }
  }
  
  public static void main (String[] argv) throws Exception
  {
    MsLoaderTest t = new MsLoaderTest();
    t.loadFromMs();
  }
}