package cz.jtbank.konsolidace.ms;

import java.util.*;
import java.sql.*;
import cz.jtbank.konsolidace.common.*;

public class MsLoader 
{
  public List loadFromMs() throws KisException
  {
    ArrayList list = new ArrayList();

    String SQL =
      "SELECT tAlco.cislo, tAlco.nazov, nNaklady.dat_prij, nNaklady.suma, xMena.nazov, nNaklady.poznamka, oSpolocnost.nazov, nDodavatelia.nazov"+
      " FROM JTintranet.dbo.nDodavatelia nDodavatelia, JTintranet.dbo.nNaklady nNaklady, JTintranet.dbo.nNakladyDruhy nNakladyDruhy, JTintranet.dbo.oSpolocnost oSpolocnost, JTintranet.dbo.tAlco tAlco, JTintranet.dbo.xMena xMena"+
      " WHERE nNaklady.d_nakladu = nNakladyDruhy.id AND nNaklady.dodavatel = nDodavatelia.id AND nNaklady.projekt = tAlco.id AND nNaklady.firma = oSpolocnost.id AND nNaklady.mena = xMena.id AND ((nNaklady.projekt Is Not Null))"+
      " ORDER BY tAlco.cislo, nNaklady.dat_prij";

    try {
      Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
    }
    catch (ClassNotFoundException e) {
      throw new KisException("Microsoft SQL Server JDBC Driver not found", e);
    }

    try (Connection con = DriverManager.getConnection("jdbc:microsoft:sqlserver://192.168.100.1:8081","pb_praha","QXzcER19");
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(SQL)) {

      while(rs.next())
      {
        String cisloOld = rs.getString(1);
        java.sql.Date datum = rs.getDate(3);
        double castka = - rs.getDouble(4);
        String mena = rs.getString(5);
        String popis = rs.getString(6);
        String spolecnost = rs.getString(7);
        String dodavatele = rs.getString(8);
        list.add(new MsData(cisloOld,popis,datum,castka,mena,spolecnost,dodavatele));
      }
    }
    catch (Exception s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo volďż˝nďż˝ procedury db_jt.kap_gui.p_loadNaklady",s);
    }

    return list;
  }
}