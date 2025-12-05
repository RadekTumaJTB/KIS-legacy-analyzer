package cz.jtbank.konsolidace.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import java.util.HashMap;

/**
 * Logging utility class migrated from Log4j 1.x to SLF4J/Logback.
 * Provides centralized appender management for different log types.
 *
 * MIGRATION NOTE: This class maintains backward compatibility with the original
 * Log4j-based API while using SLF4J/Logback internally.
 */
public class Logging
{
  public static final int LOG_DEFAULT = 0;
  public static final int LOG_AUTO_GEN = 1;
  public static final int LOG_EXPORT_DOKLADY = 2;
  public static final int LOG_EXPORT_VAZBY = 3;
  public static final int LOG_EMAIL = 4;

  private static final String PATTERN = "%-5p [%d{dd.MM.yyyy,HH:mm.ss}]: %m%n";
  private static final HashMap<Integer, ch.qos.logback.core.Appender<ILoggingEvent>> appenders = new HashMap<>();

  /**
   * Get or create a rolling file appender for the specified log type.
   * This method is thread-safe and caches appenders.
   *
   * @param type One of LOG_DEFAULT, LOG_AUTO_GEN, LOG_EXPORT_DOKLADY, LOG_EXPORT_VAZBY, LOG_EMAIL
   * @return Logback RollingFileAppender configured for the specified type
   */
  public static ch.qos.logback.core.Appender<ILoggingEvent> getAppender(int type)
  {
    Integer key = Integer.valueOf(type);
    if(appenders.containsKey(key))
      return appenders.get(key);

    String file = null;

    switch(type)
    {
      case LOG_AUTO_GEN:
        file = Constants.PROTOKOL_FILES_PATH + "automatGen.log";
        break;
      case LOG_EXPORT_DOKLADY:
        file = Constants.PROTOKOL_FILES_PATH + "exportDoklady.log";
        break;
      case LOG_EXPORT_VAZBY:
        file = Constants.PROTOKOL_FILES_PATH + "exportVazby.log";
        break;
      case LOG_EMAIL:
        file = Constants.PROTOKOL_FILES_PATH + "eMails.log";
        break;
      default:
        file = Constants.PROTOKOL_FILES_PATH + "konsolidace.log";
    }

    RollingFileAppender<ILoggingEvent> ret = null;
    try {
      LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

      ret = new RollingFileAppender<>();
      ret.setContext(context);
      ret.setFile(file);
      ret.setImmediateFlush(true);

      // Configure rolling policy (daily rotation)
      TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
      rollingPolicy.setContext(context);
      rollingPolicy.setParent(ret);
      rollingPolicy.setFileNamePattern(file + ".%d{yyyy-MM-dd}");
      rollingPolicy.start();
      ret.setRollingPolicy(rollingPolicy);

      // Configure encoder (pattern layout)
      PatternLayoutEncoder encoder = new PatternLayoutEncoder();
      encoder.setContext(context);
      encoder.setPattern(PATTERN);
      encoder.start();
      ret.setEncoder(encoder);

      ret.start();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    appenders.put(key, ret);

    return ret;
  }

  /**
   * Get the default appender.
   *
   * @return Default rolling file appender
   */
  public static ch.qos.logback.core.Appender<ILoggingEvent> getAppender()
  {
    return getAppender(LOG_DEFAULT);
  }

  /**
   * Attach an appender to a logger by name.
   * Helper method for classes that need to programmatically configure logging.
   *
   * @param loggerName Name of the logger
   * @param appender Appender to attach
   */
  public static void addAppenderToLogger(String loggerName, ch.qos.logback.core.Appender<ILoggingEvent> appender) {
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    ch.qos.logback.classic.Logger logger = context.getLogger(loggerName);
    logger.addAppender(appender);
  }
}