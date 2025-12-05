package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

/**
 *
 * Vzajemne vazby
 */
public abstract class AbsExcelDoklad {

  static Logger logger = Logger.getLogger(AbsExcelDoklad.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_DEFAULT)); }

    public String getExportRedir() 
    {
      return "excelservlet/"+System.currentTimeMillis()+"?file=" + getFileRelativeName();
    }
    
    protected Workbook wb; 
    
    protected boolean checkMax(int aList, int aRow, int aColumn) 
    {
      if(aColumn>254) {
        //setCellValue(aList, 0, 254, "Pďż˝ekroďż˝en maximďż˝lnďż˝ poďż˝et sloupcďż˝!!!", null);
        return false;
      }
      return true;
    }
    
    protected double getDoubleValue( oracle.jbo.domain.Number numVal ) 
    {
      if(numVal == null) return 0.0;
      
      double ret = 0.0;
      try 
      {
        ret = numVal.doubleValue();
      }
      catch(Exception e) 
      {
        ret = 0.0;
      }
      
      return ret;
    }

    protected void setCellValue( int aList, int aRow, int aColumn, String value, CellStyle style ) {
        if(!checkMax(aList, aRow, aColumn)) return;

        Sheet sheet  = wb.getSheetAt(aList);

        Row row  = sheet.getRow(aRow);
        if ( row == null ) {
            row = sheet.createRow(aRow);
        }
        Cell cell = row.getCell(aColumn);
        if ( cell == null ) {
            cell = row.createCell(aColumn);
        }

        // XSSF automatically uses UTF-16 encoding, no need to set it
        cell.setCellType(CellType.STRING);

        cell.setCellValue(value);

        if ( style != null )
            cell.setCellStyle(style);
    }

    protected void clearCell ( int aList, int aRow, int aColumn ) {
//        if(!checkMax(aList, aRow, aColumn)) return;

        Sheet sheet  = wb.getSheetAt(aList);
        if ( sheet == null )
            return;

        Row row  = sheet.getRow(aRow);
        if ( row == null )
            return;

        Cell cell = row.getCell(aColumn);
        if ( cell == null )
            return;

        row.removeCell( cell );
    }
    
    protected void outputZahlavi ( String spol, String mena, java.util.Date den ) {
        outputZahlavi(spol, mena, den, "", 0);
    }
    
    protected void outputZahlavi ( String spol, String mena, java.util.Date den, String special, int ps ) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String denStr = sdf.format(den);
        for ( int i = 0; i < (ps==-1?2:5); i++ ) {
            //wb.getSheetAt(i).getRow(1).getCell((short)(0)).
            //  setCellValue( spol + "  : " + mena + "  ke dni " + denStr + special);
            setCellValue(i, 1, 0, spol + "  : " + mena + "  ke dni " + denStr + special, null);
        }
        
    }

    protected void outputZahlaviNew ( String spol, String mena, java.util.Date den, String special, int ps ) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String denStr = sdf.format(den);
		setCellValue ( 0, 0, 0, spol + "  : " + mena + "  ke dni " + denStr + special, null);
    }

   protected void outputZahlaviPodnikatel ( String spol, String mena, java.util.Date den,int listNr,  int ps ) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String denStr = sdf.format(den);
    //rozvaha 2011
		setCellValue ( listNr, 3 ,  4, denStr , null);
    setCellValue ( listNr, 4 ,  4, mena   , null);
    setCellValue ( listNr, 2 , 10, spol   , null);

    //vysledovka
		setCellValue ( listNr+1, 3 ,  4, denStr , null);
    setCellValue ( listNr+1, 4 ,  4, mena   , null);    
    setCellValue ( listNr+1, 3 , 10, spol   , null);
    }


    
    protected void setCellValue( int aList, int aRow, int aColumn, double value, CellStyle style ) {
        setCellValue( aList, aRow, aColumn, value, style, false );
    }

    protected void setCellValue( int aList, int aRow, int aColumn, double value, CellStyle style, boolean longFraction) {
      setCellValue( aList, aRow, aColumn, value, style, longFraction, null );
    /*
        if(!checkMax(aList, aRow, aColumn)) return;

        if ( style == null ) style = styleDouble;
        
        Sheet sheet  = wb.getSheetAt(aList);
        Row row  = sheet.getRow(aRow);
        if ( row == null ) {
            row = sheet.createRow(aRow);
        }
        Cell cell = row.getCell((short)aColumn);
        if ( cell == null ) {
            cell =row.createCell(( short ) (aColumn));
        }
        cell.setCellType( Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
            
        if(style == null) {
            style = cell.getCellStyle();
        }
        if(style != null) {
            if(longFraction) style.setDataFormat(DataFormat.getBuiltinFormat("#,##0.00########"));
            else style.setDataFormat(DataFormat.getBuiltinFormat("#,##0.00"));
            cell.setCellStyle(style);
        }
      */      
    }

    protected void setCellValue( int aList, int aRow, int aColumn, double value, CellStyle style, boolean longFraction, String dataFormat ) {
        if(!checkMax(aList, aRow, aColumn)) return;

        if ( style == null ) style = styleDouble;

        Sheet sheet  = wb.getSheetAt(aList);
        Row row  = sheet.getRow(aRow);
        if ( row == null ) {
            row = sheet.createRow(aRow);
        }
        Cell cell = row.getCell(aColumn);
        if ( cell == null ) {
            cell = row.createCell(aColumn);
        }
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(value);

        if(style == null) {
            style = cell.getCellStyle();
        }
        if(style != null) {
          if (dataFormat == null){
            DataFormat format = wb.createDataFormat();
            if(longFraction) style.setDataFormat(format.getFormat("#,##0.00########"));
            else style.setDataFormat(format.getFormat("#,##0.00"));
          }
          //else {
               //style.setDataFormat(wb.createDataFormat().getFormat(dataFormat));
          //}
            cell.setCellStyle(style);
        }

    }

/*  esc zaloha z 27.04.2011
    protected void setCellValue( int aList, int aRow, int aColumn, int value, CellStyle style ) {
        if(!checkMax(aList, aRow, aColumn)) return;

        Sheet sheet  = wb.getSheetAt(aList);
        Row row  = sheet.getRow(aRow);
        if ( row == null ) {
            row = sheet.createRow(aRow);
        }
        Cell cell = row.getCell((short)aColumn);
        if ( cell == null ) {
            cell =row.createCell(( short ) (aColumn));
        }
        cell.setCellType( Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
            
        if(style == null) {
            style = cell.getCellStyle();
        }
        if(style != null) {
            style.setDataFormat(DataFormat.getBuiltinFormat("#,##0"));
            cell.setCellStyle(style);
        }
            
    }
*/                        
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
    
    public static String OUT_DIR = Constants.XLS_FILES_PATH;
    
//    public static List getFiles ( com.jt.tridy.table.KP_tmp_KtgSpolecnost spol ) {
//        List ret = new ArrayList();
//        File dir = new File ( OUT_DIR + spol.getS_souborPredpona() + "_" + spol.getId());
//        File[] dirTypy = dir.listFiles();
//        if ( dirTypy == null ) return ( ret );
//        for ( int i=0; i<dirTypy.length; i++ ) {
//            ret.add(dirTypy[i]);
//       }
//        return ( ret );
//    }

    
    private String newFile = null;  
    private File fFileOutput = null; // posledni vytvoreny

    public File getLastFileOutput () {
        return ( fFileOutput );
    }
    

    protected abstract boolean outputData () throws KisException;
    
    public void excelOutput() throws IOException, KisException {
        deleteOldFiles();

        java.io.File fdir = new java.io.File ( getFileAbsoluteName() );

        if ( !fdir.getParentFile().exists() )
            fdir.getParentFile().mkdirs();

        fFileOutput = new java.io.File ( getFileAbsoluteName() );

        try (FileInputStream fIn = new FileInputStream( sablona )) {
            // XSSF supports .xlsx format - no need for POIFSFileSystem
            wb = new XSSFWorkbook(fIn);

            boolean generovat = false;
            try {
              generovat = outputData();
            }
            catch(Throwable t)
            {
              logger.error("Chyba pri generovani excelu v "+getClass().getName()+"! Zkousim to jeste jednou...",t);
              generovat = outputData();
            }

            if(generovat) {
                ByteArrayOutputStream pomStream = new ByteArrayOutputStream();
                wb.write(pomStream);

                try (FileOutputStream stream = new FileOutputStream( fFileOutput )) {
                    pomStream.writeTo(stream);
                    newFile = fFileOutput.toString();
                }
            }
            else
            {
              //to by se melo zalogovat nebo tak neco...
              System.out.println("Nevygenerovan excel "+getFileAbsoluteName());
              logger.debug("Nevygenerovan excel "+getFileAbsoluteName());
              setFileName(null);
            }

        } catch ( IOException e ) {
            e.printStackTrace();
            throw e;
        }
    }

  protected FileFilter getDeleteFileFilter(final String name, final String sub) 
  {
    return new FileFilter() {
      public boolean accept(File pathname) 
      {
        if(pathname.getName().indexOf(name)>-1) {
          if(sub.indexOf("Sub")>-1 && pathname.getName().indexOf("Sub")>-1) 
          {
            return true;
          }
          if(sub.indexOf("Mis")>-1 && pathname.getName().indexOf("Mis")>-1) 
          {
            return true;
          }
          if(sub.indexOf("-PS")>-1 && pathname.getName().indexOf("-PS")>-1) 
          {
            return true;
          }
          if(sub.indexOf("Sub")<0 && pathname.getName().indexOf("Sub")<0 && 
             sub.indexOf("Mis")<0 && pathname.getName().indexOf("Mis")<0 && 
             sub.indexOf("-PS")<0 && pathname.getName().indexOf("-PS")<0) 
          {
            return true;
          }
        }
        return false;
      }
    };
  }

  protected FileFilter getDeleteFileFilter(final String name) 
  {
    return new FileFilter() {
      public boolean accept(File pathname) 
      {
        if(pathname!=null && 
           pathname.getName()!=null && 
           name!=null && 
           pathname.getName().indexOf(name)>-1) return true;
        return false;
      }
    };
  }

  protected void deleteOldFiles() throws IOException {
    int indexDir = getFileAbsoluteName().lastIndexOf('\\');
    String dirName = getFileAbsoluteName().substring(0,indexDir);
    int zavinac = getFileName().indexOf('@');
    String filter = getFileName().substring(zavinac+1);
    File dir = new File(dirName);
    FileFilter ff = getDeleteFileFilter(filter);
    File[] arr = dir.listFiles(ff);
    if(arr != null) {
      for( int i=0; i<arr.length; i++ ) 
      {
        System.out.println("Mazani souboru "+arr[i].getName()+": "+arr[i].delete());        
        logger.debug("Smazan soubor "+arr[i].getName());
        //arr[i].delete();
      }
    }
  }

    private CellStyle styleDouble;    

    private static java.text.DateFormat df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.MEDIUM, java.text.DateFormat.MEDIUM);

    public static String getHTMLRefDownloadExcel ( File soubor, int jak ) {
        String ret = "";
        String relativeSoubor = null;
        try {
            relativeSoubor = java.net.URLEncoder.encode( soubor.toString().substring(OUT_DIR.length()),"ISO-8859-2" ).replace('%', '|');
        } catch ( java.io.UnsupportedEncodingException e ) {
            e.printStackTrace();
        }
        ret = "<a href=\"excelservlet/"+System.currentTimeMillis()+"?file=" + relativeSoubor + "\" target=\"_blank\">" + soubor.getName() + "</a>";
        switch ( jak ) {
            case 1:
                ret += "&nbsp;" + df.format( new java.util.Date(soubor.lastModified()));
        }
        
        return ( ret );
    }
    
    public String getHTMLRefDownloadLastFile ( int jak ) {
        return getHTMLRefDownloadExcel ( fFileOutput, jak );
    }    
    
  protected static FileFilter getFileFilter(final String name) 
  {
    return new FileFilter() {
      public boolean accept(File pathname) 
      {
        if(pathname.getName().startsWith(name)) return true;
        return false;
      }
    };
  }

  protected static String getProtokol(String rkcDir, String name, String secFilter) 
  {
    int z = name.lastIndexOf('_')+1;
    int k = name.lastIndexOf('.');
    String fileName = secFilter+"|5C"+rkcDir+"|5CP_"+name.substring(z,k)+".html";
    String datStr = Constants.PROTOKOL_FILES_PATH+secFilter+"\\"+rkcDir+"\\P_"+name.substring(z,k)+".html";
    File ff = new File(datStr);
    String htmlTag = "";
    if(ff.exists()) {
      htmlTag = "&nbsp;<a target=\"_blank\" href=\"logsservlet?file="+fileName+"\">Protokol</a>";
    }
    
    return htmlTag;
  }
  
  private static Comparator c = new Comparator() 
  {
    public int compare(Object obj1, Object obj2) 
    {
      File o1 = (File) obj1;
      File o2 = (File) obj2;
      return -1*(o1.getName().compareTo(o2.getName()));
    }
  };
    
  protected static String getDir(String rkcDir, String filter, String secFilter) 
  {
    String dirName = Constants.XLS_FILES_PATH + rkcDir;
    File dir = new File(dirName);
    StringBuffer buf = new StringBuffer();
    FileFilter ff = getFileFilter(filter);
    File[] arr = dir.listFiles(ff);
    if(arr!=null) {
      Arrays.sort(arr, c);
      if(arr != null) {
        for( int i=0; i<arr.length; i++ ) 
        {
          buf.append("<a href=\"FileDelete.jsp?file="+rkcDir+"|5C" + arr[i].getName() + "\">DEL</a>&nbsp;");
  
          buf.append("<a target=\"_blank\" href=\"excelservlet/"+arr[i].getName()+"?file="+rkcDir+"|5C" + arr[i].getName() + "\">" + arr[i].getName()+ "</a>");
          buf.append("&nbsp;" + df.format( new java.util.Date(arr[i].lastModified())));
          if(secFilter!=null && secFilter.length()>0) {
            buf.append(getProtokol(rkcDir, arr[i].getName(), secFilter));
          }
          buf.append("<br>\n");
        }
      }
    }
    return buf.toString();
  }

}
