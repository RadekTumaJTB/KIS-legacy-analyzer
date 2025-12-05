package cz.jtbank.konsolidace.common;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cz.jtbank.konsolidace.common.Logging;
import oracle.jbo.ApplicationModule;
import oracle.jbo.domain.Number;
import cz.jtbank.konsolidace.doklady.common.DokladyModule;

public class AutoCounterNew
{
  private static final Logger logger = LoggerFactory.getLogger(AutoCounterNew.class);
  static {
    Logging.addAppenderToLogger(AutoCounterNew.class.getName(), Logging.getAppender(Logging.LOG_AUTO_GEN));
  }

  private static AutoCounterNew instance;

  private List ids;

  private AutoCounterNew()
  {
    logger.debug("COUNTER - vytvoreni");
  }
  
  public static AutoCounterNew getInstance() 
  {
    if(instance == null) instance = new AutoCounterNew();
    return instance;
  }

  public void init(Number[] ids) 
  {
    this.ids = Arrays.asList(ids);
  }

  public boolean checkFinished(ApplicationModule am) 
  {
    if(ids == null) return false;
  
    int cnt = ((DokladyModule)am).checkAutoGenFronta(0);
    if(cnt != 0) return false;
    
    ids = null;
    
    logger.debug("COUNTER - konec");
    return true;
  }
}