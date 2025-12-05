package cz.jtbank.konsolidace.csv;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;

import oracle.jbo.domain.Number;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

/**
 *
 * Vzajemne vazby
 */
public abstract class AbsCsvDoklad {

  static Logger logger = Logger.getLogger(AbsCsvDoklad.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_DEFAULT)); }
  
  protected static SimpleDateFormat sdfMesic = new SimpleDateFormat("yyyy.MM");

  public String getExportRedir() 
  {
    return "filesservlet/"+System.currentTimeMillis()+"?file=" + getFileRelativeName();
  }
    
  protected void setComma() throws IOException
  {
    stream.write(COMMA);
  }

  private byte COMMA = ';';
  private byte[] NEW_LINE = {'\r','\n'};

  protected void setNewLine() throws IOException
  {
    stream.write(NEW_LINE);
  }
    
  protected void setValue(String value) throws IOException {
    if(value==null) return;
    
    if(value.indexOf(COMMA)>=0)
      value = "\""+value+"\"";
    byte[] bytes = value.getBytes();

    stream.write(bytes);
  }

  protected void setValue(double value, int minZaCarkou, int maxZaCarkou) throws IOException {
    nf.setMinimumFractionDigits(minZaCarkou);
    nf.setMaximumFractionDigits(maxZaCarkou);
    String hlp = nf.format(value);
    hlp = hlp.replaceAll(",",".");
    
    byte[] bytes = hlp.getBytes();

    stream.write(bytes);
  }

  protected void setValue(double value) throws IOException {
    setValue(value,0,2);
  }

  protected void setValue(Number value) throws IOException {
    if(value==null) return;
    
    setValue(value.doubleValue());
  }

  protected void setValue(Number value, int minZaCarkou, int maxZaCarkou) throws IOException {
    if(value==null) return;
    
    setValue(value.doubleValue(), minZaCarkou, maxZaCarkou);
  }

  protected void setValueMesic(java.util.Date value) throws IOException {
    if(value==null) return;
    
    String hlp = sdfMesic.format(value);
    
    byte[] bytes = hlp.getBytes();

    stream.write(bytes);
  }

  protected void setValueMesic(oracle.jbo.domain.Date value) throws IOException {
    if(value==null) return;
    
    setValueMesic(value.dateValue());
  }

  private String fileName, relativeFileName, absolutFileName, sablona;
  
  public String getSablona() {
      return ( sablona );
  }
  
  public void setSablona(String sablona) {
      this.sablona = sablona;
  }
  
  public String getFileName() {
      return  (fileName);
  }

  public String getFileRelativeName() {
      return  ( relativeFileName );
  }

  public String getFileAbsoluteName() {
      return  ( absolutFileName );
  }

  public void setFileName(String fileName) {
      this.fileName=fileName;
  }

  public void setFileRelativeName(String relativeFileName) {
      this.relativeFileName=relativeFileName;
  }

  public void setFileAbsoluteName(String absolutFileName) {
      this.absolutFileName=absolutFileName;
  }
  
  public static String OUT_DIR = Constants.CSV_FILES_PATH;
    
  private File fFileOutput = null;
  private FileOutputStream stream = null;
  
  private NumberFormat nf = new DecimalFormat("##################.##");

  protected abstract boolean outputData () throws KisException, IOException;
  
  public void createCsv() throws IOException, KisException {
    deleteOldFiles();
        
    try {
      fFileOutput = new java.io.File ( getFileAbsoluteName() );

      if ( !fFileOutput.getParentFile().exists() )
        fFileOutput.getParentFile().mkdirs();

      stream = new FileOutputStream( fFileOutput );
    }
    catch(Throwable t) 
    {
      logger.error("Chyba pri vytvareni CSV v "+getClass().getName()+"!",t);
    }
  }

  public void appendCsv() throws IOException, KisException {
    try {
      fFileOutput = new java.io.File ( getFileAbsoluteName() );

      if ( !fFileOutput.getParentFile().exists() )
        fFileOutput.getParentFile().mkdirs();

      stream = new FileOutputStream( fFileOutput, fFileOutput.exists() );
    }
    catch(Throwable t) 
    {
      logger.error("Chyba pri pridavani do CSV v "+getClass().getName()+"!",t);
    }
  }
  
  protected void finalize() {
    try 
    {
      if(stream!=null) stream.close();
    }
    catch (Throwable tIn) {}
  }
    
  public void csvOutput() throws IOException, KisException {
    boolean generovat = false;
    try {
      generovat = outputData();
      if ( stream!= null ) stream.flush();
    }
    catch(IOException t)
    {
      logger.error("Chyba pri generovani CSV v "+getClass().getName()+"!",t);
    }
    finally
    {
      if(stream!=null) {
        try {
          stream.close();
        }
        catch (IOException tIn) {
          logger.error("Error closing CSV stream", tIn);
        }
      }
      if(!generovat && fFileOutput!=null) fFileOutput.delete();
    }
  }

  protected FileFilter getDeleteFileFilter() 
  {
    return new FileFilter() {
      public boolean accept(File pathname) 
      {
        return true;
      }
    };
  }

  protected FileFilter getDeleteFileFilter(final String name) 
  {
    return new FileFilter() {
      public boolean accept(File pathname) 
      {
        if(pathname.getName().indexOf(name)>-1) return true;
        return false;
      }
    };
  }

  protected void deleteOldFiles() throws IOException {
  }

}
