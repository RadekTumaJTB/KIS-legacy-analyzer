package cz.jtbank.konsolidace.jobs;

import oracle.jbo.*;
import oracle.jbo.client.*;
import oracle.jbo.domain.Number;

import cz.jtbank.konsolidace.doklady.common.DokladyModule;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ExportSubkonsExcelThread extends Thread 
{
  static Logger logger = Logger.getLogger(ExportSubkonsExcelThread.class);
  static { logger.addAppender(Logging.getAppender()); }

  private static final long pauza = 10000;
  private int logCounter = 1;
  private static final int MAX_TIME = 60;
  private long startTime = 0, endTime = 0;
  
  DokladyModule dm = (DokladyModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");

  private static ExportSubkonsExcelThread instance;
  
  public static ExportSubkonsExcelThread getInstance()
  {
    if(instance == null) instance = new ExportSubkonsExcelThread();
    return instance;
  }

  private ExportSubkonsExcelThread()
  {
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

  private boolean dalsiHned = false;
  public void setDalsiHned() 
  {
    dalsiHned = true;
  }

  public void run() {
    logger.debug("start proces ExportSubkonsExcelThread.run()");          
    System.out.println("start proces ExportSubkonsExcelThread.run()");
    for (;;) {
      try {
        if(!dalsiHned && endTime-startTime < 500) {
          synchronized (this) {
            this.wait(pauza);
          }
        }
        dalsiHned = false;
        startTime = System.currentTimeMillis();
        checkExportDoklad();
        endTime = System.currentTimeMillis();
      } catch ( java.lang.InterruptedException e ) {
          e.printStackTrace();
          break;
      }
    }
    logger.debug("konec ExportSubkonsExcelThread.run()");          
    System.out.println("konec ExportSubkonsExcelThread.run()");
  }

  private void checkExportDoklad() 
  {
    if(logCounter < MAX_TIME) { logCounter++; }
    else { 
      logCounter = 1; 
      logger.info("SUB - Po ca. "+(pauza/1000*MAX_TIME/60)+" minutach volani checkExportDoklad()"); 
    }
    
    try {
      dm.exportSubkonsDoklad();
    } catch ( Exception e ) {
      e.printStackTrace();
      logger.error("Export excel subkons. dokladu:\n",e);
    }        
  }
}