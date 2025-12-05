package cz.jtbank.konsolidace.jobs;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import oracle.jbo.*;
import oracle.jbo.client.*;
import oracle.jbo.domain.Number;
import java.sql.Date;
import java.text.SimpleDateFormat;

import cz.jtbank.konsolidace.protistrany.common.ProtistranyModule;
import cz.jtbank.konsolidace.doklady.common.DokladyModule;
import cz.jtbank.konsolidace.common.Utils;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

//import oracle.jdbc.pool.*;
//import javax.sql.PooledConnection;
import java.sql.*;
import javax.sql.*;

public class GeneratorThread extends Thread 
{
  static Logger logger = Logger.getLogger(GeneratorThread.class);
  static { logger.addAppender(Logging.getAppender()); }

  private static final long pauza = 10000;
  private int logCounter = 1;
  private static final int MAX_TIME = 60;
  private long startTime = 0, endTime = 0;

  DokladyModule dm = (DokladyModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");

  //private static OracleConnectionPoolDataSource ocpds;
  //private static PooledConnection lpc;
  private static Connection con;

  private static GeneratorThread instance;

  private String url=null,
          user=null,
          password=null;
  
  public static GeneratorThread getInstance()
  {
    if(instance == null) instance = new GeneratorThread();
    return instance;
  }

  private GeneratorThread() 
  {
    try {
      url = dm.getTransaction().getConnectionMetadata().getJdbcURL();
      user = dm.getTransaction().getConnectionMetadata().getUserName();
      password = dm.getAppserverDBPassword();
      con = DriverManager.getConnection(url,user,password);

      logger.info("Connection:"+con+", URL="+url+", user="+user);
      logger.debug("Connection:"+con+" URL="+url+" user="+user+" Name: "+con.getMetaData().getDriverName()+" ver. "+" - "+con.getMetaData().getDriverMajorVersion()+" . "+con.getMetaData().getDatabaseMinorVersion() );
      
    } catch(SQLException se) {logger.error("GeneratorThread",se);}
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
      try {
        con.close();
        logger.info("Konec Connection:"+con);
      } catch(SQLException se) {logger.error("reset",se);}
      con = null;
    }
    getInstance().start();
  }

  private boolean dalsiHned = false;
  public void setDalsiHned() 
  {
    dalsiHned = true;
  }

  public void run() {
    logger.debug("start proces GeneratorThread.run()");          
    System.out.println("start proces GeneratorThread.run()");
    try 
    {
      logger.info("Inicializace fronty.");
      dm.frontaInit();
    }
    catch(Exception e) 
    {
      logger.error("Selhala inicializace fronty",e);
    }
    try {
      logger.info("Mazani db_dsa.kp_radicImportu");
      dm.deleteRadicImportu();
    }
    catch(Exception e) 
    {
      logger.error("Selhalo mazani db_dsa.kp_radicImportu",e);
    }
    for (;;) {
      try {
        if(!dalsiHned && endTime-startTime < 500) {
          synchronized (this) {
            this.wait(pauza);
          }
        }
        dalsiHned = false;
        startTime = System.currentTimeMillis();
        checkCalculate();
        endTime = System.currentTimeMillis();
      } catch ( java.lang.InterruptedException e ) {
          e.printStackTrace();
//esc 11/2010...
            if (con != null) {
              try {
                  logger.info("Konec PooledConnection:"+con);
                  con.close();      
              }    catch ( Exception e1 ) {logger.error("CHYBA Konec PooledConnection:"+con);
                                           logger.debug("CHYBA Konec PooledConnection:"+con);
                                          e1.printStackTrace();              }
            }
//..esc 11/2010
          break;
      }
    }
    logger.debug("konec GeneratorThread.run()");          
    System.out.println("konec GeneratorThread.run()");
    
    try {
      logger.info("Konec PooledConnection:"+con);
      con.close();
    } catch(SQLException se) {logger.error("run - konec",se);}
    con = null;
  }
  
  private void checkCalculate() 
  {
    if(logCounter < MAX_TIME) { logCounter++; }
    else { 
      logCounter = 1; 
      logger.info("Po ca. "+(pauza/1000*MAX_TIME/60)+" minutach volani calculateDoklad()"); 
    }
    
    try {
      dm.calculateDoklad(con);
    } catch ( Exception e ) {
      e.printStackTrace();
      logger.error("Generovani dokladu:\n",e);
      try {
        if(con!=null) { con.close(); con=null; }
      }
      catch(SQLException se) {logger.error("checkCalculate (close)",se);}
      try {
        con = DriverManager.getConnection(url,user,password);
        logger.info("Restartuje se Connection:"+con);
      }
      catch(SQLException se) {logger.error("checkCalculate (open)",se);}
    }        
  }
}