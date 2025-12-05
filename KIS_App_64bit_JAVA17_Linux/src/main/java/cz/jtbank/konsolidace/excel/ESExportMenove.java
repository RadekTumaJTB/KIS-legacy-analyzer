package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.*;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportMenove extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportMenove.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  private int ucSkup;
  
  private double sumCzkVetsi, sumCzkVetsi08;

  private String dir;

  public ESExportMenove(ApplicationModule dokladyModule,
                        java.sql.Date datum,
                        int ucSkup)
  {
logger.info("ESExportMenove:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    this.ucSkup = ucSkup;
    dir = Constants.DIR_POZICE_MU+ucSkup;
    init();
  }

  private void init() {
    setFileName ( "MenovePozice_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaMenove.xlsx" );
  }

  private void outputMenovaPoziceSouhrn() {
    int listNr=1;
    int rowNr=3;
    
    sumCzkVetsi = 0.0;
    sumCzkVetsi08 = 0.0;
    
    ViewObject vo = dm.findViewObject("VwKpDokladmenovapoziceSpecialView1");
    vo.clearCache();
    String mp = "";//(ucSkup==1 || ucSkup==-1 || ucSkup==-2) ? " AND C_MENOVAPOZICE = '1'" : "";
    vo.setWhereClause("ID_DOKLAD IN ( "+Utils.getWhereDokladIds(datum, ucSkup)+" )"+mp+" AND S_LOCALE = 'cs_CZ'");
    
    java.util.SortedMap map = new java.util.TreeMap();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int colNr = 6;
      double lokalValue = 0, menaValue = 0, czkValue = 0; 
      String sMena = null;
      
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("nazevSpol"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;
      //esc 9.9.2010
      //czkValue = ((Number)row.getAttribute("CastkaCZK")).doubleValue();
      czkValue = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );
      setCellValue( listNr, rowNr, colNr, czkValue, null );
      colNr++;
      lokalValue = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      setCellValue( listNr, rowNr, colNr, lokalValue, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("menaSpol"), null );
      colNr++;
      menaValue = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      setCellValue( listNr, rowNr, colNr, menaValue, null );
      colNr++;
      sMena = (String)row.getAttribute("SMena");
      setCellValue( listNr, rowNr, colNr, sMena, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopisoriginal"), null );

      rowNr++;

      double[][] castky = null;
      Object v = map.get(sMena);
      if ( v != null )
          castky = ( double[][] ) v;
      else
          castky = new double[2][3];
      
      if ( menaValue > 0 ) {
          //castky[0][0] += lokalValue;
          castky[0][0] += czkValue;
          castky[0][1] += menaValue;
          castky[0][2] += czkValue;
      } else {
          //castky[1][0] += lokalValue;
          castky[1][0] += czkValue;
          castky[1][1] += menaValue;
          castky[1][2] += czkValue;
      }                    
      map.put ( sMena, castky );
    }
    outputSumTable(listNr, map, -1, "CZK");
    
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
  
  private void outputMenovaPozice() {
    int fpRowNr=3;
    int listNr=1;
    int rowNr=3;
    String lastNazevSpol = null;
    String menaSpol = null;
    
    sumCzkVetsi = 0.0;
    sumCzkVetsi08 = 0.0;
    
    ViewObject vo = dm.findViewObject("VwKpDokladmenovapoziceSpecialView1");
    vo.clearCache();
    String mp = "";//(ucSkup==1 || ucSkup==-1) ? " AND C_MENOVAPOZICE = '1'" : "";
    vo.setWhereClause("ID_DOKLAD IN ( "+Utils.getWhereDokladIds(datum, ucSkup)+" )"+mp+" AND S_LOCALE = 'cs_CZ'");
    
    java.util.SortedMap map = null;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int colNr = 6;
      double lokalValue = 0, menaValue = 0, czkValue = 0; 
      String sMena = null;
      
      String nazevSpol = (String)row.getAttribute("nazevSpol");
      if(!nazevSpol.equals(lastNazevSpol)) 
      {
        if(lastNazevSpol!=null) {
          outputSumTable(listNr, map, fpRowNr, menaSpol);
        }
        menaSpol = (String)row.getAttribute("menaSpol");
        setCellValue( 0, fpRowNr, 1, nazevSpol, null );
        fpRowNr++;

        String sheetName = nazevSpol.length() <= 30 ? nazevSpol : nazevSpol.substring(0,30);
        sheetName = sheetName.replaceAll("/","");
        sheetName = sheetName.replaceAll("\\\\","");
        sheetName = sheetName.replaceAll("\\*","");
        sheetName = sheetName.replaceAll("\\?","");
        sheetName = sheetName.replaceAll("\\[","");
        sheetName = sheetName.replaceAll("\\]","");
        wb.cloneSheet(1);
        listNr++;
        wb.setSheetName(listNr,sheetName);
        lastNazevSpol = nazevSpol;
        rowNr = 3;
        map = new java.util.TreeMap();
      }

      setCellValue( listNr, rowNr, colNr, nazevSpol, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      colNr++;
      //esc 9.9.2010
      //czkValue = ((Number)row.getAttribute("CastkaCZK")).doubleValue();
      czkValue = ( row.getAttribute("CastkaCZK") != null ? ((Number)row.getAttribute("CastkaCZK")).doubleValue() : 0 );
      setCellValue( listNr, rowNr, colNr, czkValue, null );
      colNr++;
      lokalValue = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      setCellValue( listNr, rowNr, colNr, lokalValue, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("menaSpol"), null );
      colNr++;
      menaValue = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      setCellValue( listNr, rowNr, colNr, menaValue, null );
      colNr++;
      sMena = (String)row.getAttribute("SMena");
      setCellValue( listNr, rowNr, colNr, sMena, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetunif"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopisoriginal"), null );

      rowNr++;

      double[][] castky = null;
      Object v = map.get(sMena);
      if ( v != null )
          castky = ( double[][] ) v;
      else
          castky = new double[2][3];
      
      if ( menaValue > 0 ) {
          castky[0][0] += lokalValue;
          castky[0][1] += menaValue;
          castky[0][2] += czkValue;
      } else {
          castky[1][0] += lokalValue;
          castky[1][1] += menaValue;
          castky[1][2] += czkValue;
      }                    
      map.put ( sMena, castky );
    }
    if(lastNazevSpol!=null) {
      outputSumTable(listNr, map, fpRowNr, menaSpol);
      setCellValue( 0, fpRowNr+1, 1, "Souet", null );
      setCellValue( 0, fpRowNr+1, 2, sumCzkVetsi, null );
      setCellValue( 0, fpRowNr+1, 3, sumCzkVetsi08, null );
    }
    
    //wb.removeSheetAt(1);
    
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputSumTable(int listNr, java.util.SortedMap map, int fpRowNr, String menaSpol) 
  {
    int rowNr=2;
    for ( Iterator i = map.keySet().iterator(); i.hasNext(); ) {
      String k = (String) i.next();
      double[][] c = (double[][]) map.get(k);
      rowNr++;
      int colNr = 0;
      setCellValue( listNr, rowNr, colNr, "Dlouh pozice", null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[0][2], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[0][0], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[0][1], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, k, null );
      rowNr++;
      colNr = 0;
      setCellValue( listNr, rowNr, colNr, "Krtk pozice", null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[1][2], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[1][0], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, c[1][1], null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, k, null );
    }

    double lokalSumaDlouhePozice = 0, lokalSumaKratkePozice = 0;
    double czkSumaDlouhePozice = 0, czkSumaKratkePozice = 0;
    
    rowNr += 3;
    for ( Iterator i = map.keySet().iterator(); i.hasNext(); ) {
        String k = (String) i.next();
        double[][] c = (double[][]) map.get(k);
        rowNr++;
        int colNr = 0;
        setCellValue( listNr, rowNr, colNr, "Agregace", null );                
        colNr++;
        setCellValue( listNr, rowNr, colNr, c[0][2]+c[1][2], null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, c[0][0]+c[1][0], null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, c[0][1]+c[1][1], null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, k, null );
        if ( ! k.equals ( menaSpol ) ) {
          if ( ( c[0][0]+c[1][0] ) > 0 ) { 
            czkSumaDlouhePozice += c[0][2]+c[1][2];
            lokalSumaDlouhePozice += c[0][0]+c[1][0];
          } else {
            czkSumaKratkePozice += c[0][2]+c[1][2];
            lokalSumaKratkePozice += c[0][0]+c[1][0];
          }
        }
    }

    rowNr += 4;
    int colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Vysledek", null );                
    rowNr++;
    setCellValue( listNr, rowNr, colNr, "Suma dlouhych pozic", null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkSumaDlouhePozice, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalSumaDlouhePozice, null );
    rowNr++;
    colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Suma kratkych pozic", null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkSumaKratkePozice, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalSumaKratkePozice, null );
    rowNr++;
    rowNr++;
    colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Vetsi z hodnot", null );
    double czkVetsi = Math.abs(czkSumaKratkePozice);
    double lokalVetsi = Math.abs(lokalSumaKratkePozice);
    if ( Math.abs( lokalSumaDlouhePozice ) > Math.abs( lokalSumaKratkePozice ) ) {
        czkVetsi = Math.abs(czkSumaDlouhePozice);
        lokalVetsi = Math.abs(lokalSumaDlouhePozice);
    }
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkVetsi, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalVetsi, null );
    rowNr++;
    colNr = 0;
    setCellValue( listNr, rowNr, colNr, "Kap. pozadavek 8%", null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, czkVetsi*.08, null );
    colNr++;
    setCellValue( listNr, rowNr, colNr, lokalVetsi*.08, null );

    if(fpRowNr > 0) {
      setCellValue( 0, fpRowNr-1, 2, czkVetsi, null );
      setCellValue( 0, fpRowNr-1, 3, czkVetsi*.08, null );
      sumCzkVetsi += czkVetsi;
      sumCzkVetsi08 += czkVetsi*.08;
    }
  }

  protected boolean outputData () 
  {
    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();    
    outputMenovaPozice();
    outputMenovaPoziceSouhrn();
    end = System.currentTimeMillis();    
    logger.debug("menova p.:"+((end-start)/1000.0)+"s");
    
    return true;
  }

  public static String getDir(int us) 
  {
    String dirUs = Constants.DIR_POZICE_MU+us;
    return getDir(dirUs, "MenovePozice", "muProtokol");
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportMenove ed = new ESExportMenove(dm,
		  Utils.getDate( 30, 9, 2007),
//                                             cz.jtbank.konsolidace.common.Utils.getLastDate(),
                                             1);
      ed.excelOutput();
      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=ed.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }
  
}
