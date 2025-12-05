package cz.jtbank.konsolidace.common;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cz.jtbank.konsolidace.common.Logging;
import oracle.jbo.ApplicationModule;
import oracle.jbo.domain.Number;
import cz.jtbank.konsolidace.doklady.common.DokladyModule;

public class AutoMUCounter
{
  private static final Logger logger = LoggerFactory.getLogger(AutoMUCounter.class);
  static {
    Logging.addAppenderToLogger(AutoMUCounter.class.getName(), Logging.getAppender(Logging.LOG_AUTO_GEN));
  }

  private static AutoMUCounter instance;

  private List ids;

  private AutoMUCounter()
  {
    logger.debug("COUNTER M/U - vytvoreni");
  }
  
  public static AutoMUCounter getInstance() 
  {
    if(instance == null) instance = new AutoMUCounter();
    return instance;
  }

  public void init(Number[] ids) 
  {
    this.ids = Arrays.asList(ids);
  }

  public boolean checkFinished(ApplicationModule am) 
  {
    if(ids == null) return false;
  
    int cnt = ((DokladyModule)am).checkAutoGenFronta(2);
    if(cnt != 0) return false;
    
    ids = null;
    
    logger.debug("COUNTER M/U - konec");
    return true;
  }
}