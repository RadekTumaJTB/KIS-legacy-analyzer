package cz.jtbank.konsolidace.jobs;
import cz.jtbank.konsolidace.common.*;
//import cz.jtbank.konsolidace.commonWeb.*;
import jakarta.servlet.http.HttpServlet;

import cz.jtbank.konsolidace.mail.*;
import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;
/* JH pridani konfigurace log4j podle property souboru */
import java.net.URL;
public class AutoStart extends HttpServlet 
{

/*
 * inicializace prenesena do initLog4J
  static Logger logger = Logger.getLogger(AutoStart.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_AUTO_GEN)); }
*/

  static Logger logger = null;
  private Mail email;
  private static int nrInstances = 0;

  public void init( jakarta.servlet.ServletConfig sc ) throws jakarta.servlet.ServletException {
    super.init ( sc );

	/* inicializace log4J */
	initLog4J();

    logger.debug("Startuje se AutoStart");            
  /*esc 03/2011 */    
    email = new Mail();
    email.sendMail( "kis@jtbank.cz", 
                    new String[] {"stastny@jtfg.com",   /*SmrM 07.11.2025 */
                                  "mikysek@jtfg.com",   /*SmrM 07.11.2025 */
                                  "smrecek@jtbank.cz"}, /*esc 24.10 2020 */
                    "KIS RESTART OAS: "+Constants.getHostName(),
                    "Startuje se AutoStart"+" RESTART OAS: "+Constants.getHostName(),
                    new String[] {} );
    
    if(nrInstances > 0) 
    {
      System.err.println("Servlet muze byt spusten jen jednou...");
      logger.error("Servlet muze byt spusten jen jednou...");
      throw new jakarta.servlet.ServletException("Servlet muze byt spusten jen jednou...");
    }
    else 
      nrInstances++;

    try {
      logger.info("Server="+java.net.InetAddress.getLocalHost().getHostName());            
      if ( !"tweek".equalsIgnoreCase( java.net.InetAddress.getLocalHost().getHostName() )  
//	&&	   !"czpav00007".equalsIgnoreCase( java.net.InetAddress.getLocalHost().getHostName() ) && !"czpav00027".equalsIgnoreCase( java.net.InetAddress.getLocalHost().getHostName() )
           ) { 
/*           !"timmy".equalsIgnoreCase( java.net.InetAddress.getLocalHost().getHostName() )) { */
        System.err.println("Automaticke generovani muze byt spusteno jen na Tweekovi ...");
        logger.error("Automaticke generovani muze byt spusteno jen na Tweekovi ...");
        throw new javax.servlet.ServletException("Automaticke generovani muze byt spusteno jen na Tweekovi ...");
      }
      else 
      {
        GeneratorThread.getInstance().start();
        ExportExcelThread.getInstance().start();
        ExportSubkonsExcelThread.getInstance().start();
        GenerateAll.getInstance().start();
      }
    } catch ( Exception e ) { 
      throw new javax.servlet.ServletException("Chyba v kontrole serveru",e); 
    }
  }

  public void destroy() {
    logger.debug("Konec AutoStart (destroy)......................");
    GenerateAll.getInstance().konec();
    GenerateAll.getInstance().newParam();
    GeneratorThread.getInstance().destroy();
    ExportExcelThread.getInstance().destroy();
    ExportSubkonsExcelThread.getInstance().destroy();
  }

   /* JH pridani konfigurace log4j podle property souboru */
	private void initLog4J (){
		URL log4jprops = this.getClass().getClassLoader().getResource("log4j.properties");
		PropertyConfigurator.configure(log4jprops);
		
		/* vytazeno ze staticke inicializace tridy */
		logger = Logger.getLogger(AutoStart.class);
		logger.addAppender(Logging.getAppender(Logging.LOG_AUTO_GEN)); 
	}
}

/*

package Log4jtest;

import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import cz.jtbank.konsolidace.common.Logging;

public class Main {
    
    private static Logger log = null;

    public static void main(String[] args) {
        Main m = new Main();
        m.initLog4J();
        //log.debug("prvni");
        
        TestKisLog.test();
    }
    
    private void initLog4J (){
        URL log4jprops = this.getClass().getClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(log4jprops);
        //log = Logger.getLogger(Main.class.getName());
    }
}   
  
  */