package cz.jtbank.konsolidace.common;

public class KisException extends Exception 
{
  public KisException(String message, Throwable e) 
  {
    super(message, e);
  }

  public KisException(String message) 
  {
    super(message);
  }
}