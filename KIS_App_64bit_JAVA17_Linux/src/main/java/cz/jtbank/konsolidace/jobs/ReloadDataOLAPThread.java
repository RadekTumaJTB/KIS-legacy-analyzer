package cz.jtbank.konsolidace.jobs;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import cz.jtbank.konsolidace.common.Utils;
import java.sql.*;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ReloadDataOLAPThread extends Thread 
{
  static Logger logger = Logger.getLogger(ReloadDataOLAPThread.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_AUTO_GEN)); }

  public ReloadDataOLAPThread()
  {
  }
  
  public void run() {
    logger.debug("start proces ReloadDataOLAPThread.run()");          
    System.out.println("start proces ReloadDataOLAPThread.run()");

    Connection con = null;
    Statement st = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      con = DriverManager.getConnection("jdbc:oracle:thin:@tweek.pa.jtfg.com:1521:kis","APPSERVER","JT_SERVER");

      logger.info("reloadDataOLAP - reload dat pro OLAP - probíhá paralelně");
      SystemStatus.getInstance().setStatus("Reload dat pro OLAP - probíhá paralelně");
      st = con.createStatement();
      st.execute("begin db_jt.kap_guiv.p_reloadDataOLAP; end;");
      logger.info("reloadDataOLAP - dokonceno");
    }
    catch (Exception s) {
      s.printStackTrace(); //pro zacatek
      logger.error("Selhalo volání procedury db_jt.kap_guiv.p_reloadDataOLAP ",s);
    }
    finally {
      try {
        if (st != null) { st.close(); st=null; }
      } 
      catch (SQLException s) {}
      try {
        if (con != null) { con.close(); con=null; }
      } 
      catch (SQLException s) {}
      
      SystemStatus.getInstance().reset();
    }

    logger.debug("konec ReloadDataOLAPThread.run()");          
    System.out.println("konec ReloadDataOLAPThread.run()");
  }
  
  public static void main(String[] argv) 
  {
    ReloadDataOLAPThread rm = new ReloadDataOLAPThread();
    rm.start();
  }
}