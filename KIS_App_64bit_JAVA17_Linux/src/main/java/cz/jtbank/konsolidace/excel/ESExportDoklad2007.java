package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.nio.file.Paths;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportDoklad2007 extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportDoklad2007.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private Number idDoklad;
  private Number idDokladPodnikIFRS;
  private Number idDokladPodnikLocal;
  private Number idDokladRKC;
  private Number idDoklad15;
  private Number idDoklad16;
  private Number idDoklad17;
  
  private Number idKtgUcetniSpolecnost;
  private java.sql.Date datum;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;
  private String localeSpol;
  
  private String specialDoklad = "";

  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportDoklad2007(ApplicationModule dokladyModule,
                            Number idKtgUcetniSpolecnost,
                            java.sql.Date datum)
  {
    dm = dokladyModule;
    this.idKtgUcetniSpolecnost = idKtgUcetniSpolecnost;
    this.datum = datum;
    init();
  }

  private void init() {
    ViewObject vo;
    
    //DOKLAD
    vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgUcetniSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=11");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDoklad = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje doklad pro "+idKtgUcetniSpolecnost+" a "+datum);
    }
    vo.closeRowSet();

    //DOKLAD PODNIKATEL IFRS
    vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgUcetniSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=13");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDokladPodnikIFRS = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje podnikatelskďż˝ doklad IFRS pro "+idKtgUcetniSpolecnost+" a "+datum);
    }
    vo.closeRowSet();

    //DOKLAD PODNIKATEL LOKAL
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgUcetniSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=10");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDokladPodnikLocal = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje podnikatelskďż˝ doklad Local pro "+idKtgUcetniSpolecnost+" a "+datum);
    }
    vo.closeRowSet();

    //DOKLAD RKC
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgUcetniSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=14");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDokladRKC = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje RKC doklad pro "+idKtgUcetniSpolecnost+" a "+datum);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    //ZKRACENE DOKLADY
    //DOKLAD 15
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgUcetniSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=15");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDoklad15 = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje 15 doklad pro "+idKtgUcetniSpolecnost+" a "+datum);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //DOKLAD 16
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgUcetniSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=16");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDoklad16 = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje 15 doklad pro "+idKtgUcetniSpolecnost+" a "+datum);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //DOKLAD 17
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idKtgUcetniSpolecnost+" AND DT_DATUM=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') AND NL_KROK=17");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idDoklad17 = (Number) row.getAttribute("Id");
    }
    else 
    {
      logger.warn("Neexistuje 15 doklad pro "+idKtgUcetniSpolecnost+" a "+datum);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idKtgUcetniSpolecnost);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("SNazev");
      menaSpol = (String) row.getAttribute("SMena");
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
      localeSpol = (String) row.getAttribute("SLocale");
    }
    vo.closeRowSet();
    
    //NEW
    //vo.remove();
    logger.info("ExportDoklad2007:nazevSpol="+nazevSpol+",idDoklad="+idDoklad+",datum="+datum);  

    setFileName ( "Bilance2007"+specialDoklad+"_"+datum+".xlsx" );

    // MIGRATED: Platform-independent path construction
    setFileRelativeName( Paths.get(souborPredponaSpol+"_"+idKtgUcetniSpolecnost, getFileName()).toString() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona = "SablonaBilance2007.xlsx";

    setSablona( Paths.get(Constants.SABLONY_FILES_PATH, sablona).toString() );
  }

  private void outputHeaders() 
  {
    CellStyle style = null;
    String special = "";

//    outputZahlavi(nazevSpol,menaSpol,datum,special,0);
/*
    org.apache.poi.hssf.usermodel.Cell cell = wb.getSheetAt(0).getRow(5).getCell((short)2);
    System.out.println(cell);
*/
/*    
    for(listNr = 0; listNr<(jenomPs==-1?2:5); listNr++) {
      int colNr = 2;
      setCellValue(listNr,6,colNr,menaSpol,style);
      colNr++;
      if(listNr==0) 
      {
        if(sCNB) colNr++;
        setCellValue(listNr,6,colNr,menaSpol,style);
        colNr++;

        if(sCNB) colNr++;
        setCellValue(listNr,6,colNr,menaSpol,style);
        colNr++;
      }
    }
*/
    String dtStr = sdf.format(datum);

    setCellValue(0,0,0,nazevSpol+" ("+menaSpol+")",style);

    setCellValue(0,3,0,dtStr,style);
    setCellValue(0,3,1,dtStr,style);
    setCellValue(0,5,3,menaSpol,style);
    setCellValue(0,5,4,menaSpol,style);
    setCellValue(0,5,5,menaSpol,style);
    
    setCellValue(1,3,0,dtStr,style);
    setCellValue(1,3,1,dtStr,style);
    setCellValue(1,5,3,menaSpol,style);

    setCellValue(2,3,0,dtStr,style);
    setCellValue(2,3,1,dtStr,style);
    setCellValue(2,6,3,menaSpol,style);
    setCellValue(2,6,4,menaSpol,style);
    setCellValue(2,6,5,menaSpol,style);
    
    setCellValue(3,3,0,dtStr,style);
    setCellValue(3,3,1,dtStr,style);
    setCellValue(3,4,3,menaSpol,style);

    setCellValue(4,3,0,dtStr,style);
    setCellValue(4,3,1,dtStr,style);
    setCellValue(4,5,3,menaSpol,style);
    setCellValue(4,5,4,menaSpol,style);
    setCellValue(4,5,5,menaSpol,style);
    
    setCellValue(5,3,0,dtStr,style);
    setCellValue(5,3,1,dtStr,style);
    setCellValue(5,4,3,menaSpol,style);

    setCellValue(6,3,0,dtStr,style);
    setCellValue(6,3,1,dtStr,style);
    setCellValue(6,9,3,menaSpol,style);
    setCellValue(6,9,4,menaSpol,style);
    setCellValue(6,9,5,menaSpol,style);
    setCellValue(6,9,6,menaSpol,style);

    setCellValue(7,3,0,dtStr,style);
    setCellValue(7,3,1,dtStr,style);
    setCellValue(7,5,3,menaSpol,style);

    setCellValue(8,3,0,dtStr,style);
    setCellValue(8,3,1,dtStr,style);
    setCellValue(8,7,3,menaSpol,style);
    setCellValue(8,7,4,menaSpol,style);
    setCellValue(8,7,5,menaSpol,style);
    setCellValue(8,28,3,menaSpol,style);
    setCellValue(8,28,4,menaSpol,style);
    setCellValue(8,28,5,menaSpol,style);

    setCellValue(9,2,0,dtStr,style);
    setCellValue(10,2,0,dtStr,style);
    setCellValue(11,2,0,dtStr,style);
  }
  
  private Pozice getPozice(int dokladTyp, Pozice pozice) 
  {
    switch(dokladTyp) 
    {
      case 0: //fin.
        if(pozice.list==1) {
          pozice.list = 0;
          if(pozice.sloupec==1) pozice.sloupec = 3;
          else pozice.sloupec = 4;
          pozice.radek += 4;
        }
        else if(pozice.list==2) {
          pozice.list = 0;
          pozice.sloupec = 3;
          if(pozice.radek>29) pozice.radek += 7;
          else pozice.radek += 6;
        }
        else if(pozice.list==3) {
          pozice.list = 1;
          pozice.sloupec = 3;
          pozice.radek += 4;
        }
        break;
      case 1: //podnik. IFRS
        if(pozice.list==1) {
          pozice.list = 2;
          if(pozice.sloupec==1) pozice.sloupec = 3;
          else pozice.sloupec = 4;
          pozice.radek += 5;
        }
        else if(pozice.list==2) {
          pozice.list = 2;
          pozice.sloupec = 3;
          pozice.radek += 6;
        }
        else if(pozice.list==3) {
          pozice.list = 3;
          pozice.sloupec = 3;
          pozice.radek += 3;
        }
        break;
      case 2: //podnik. local
        if(pozice.list==1) {
          pozice.list = 4;
          if(pozice.sloupec==1) pozice.sloupec = 3;
          else pozice.sloupec = 4;
          pozice.radek += 5;
        }
        else if(pozice.list==2) {
          pozice.list = 5;
          pozice.sloupec = 3;
          pozice.radek += 3;
        }        
/*        else if(pozice.list==2) {
          pozice.list = 4;
          pozice.sloupec = 3;
          pozice.radek += 5;
        }
        else if(pozice.list==3) {
          pozice.list = 5;
          pozice.sloupec = 3;
          pozice.radek += 3;
        }*/
        break;
      case 3: //rkc
        if(pozice.list==1) {
          pozice.list = 6;
          if(pozice.sloupec==1) pozice.sloupec = 3;
          else pozice.sloupec = 5;
          pozice.radek += 9;
        }
        else if(pozice.list==2) {
          pozice.list = 6;
          pozice.sloupec = 3;
          pozice.radek += 69;
        }
        else if(pozice.list==4) {
          pozice.list = 8;
          pozice.sloupec = 4;          
          pozice.radek += 7;
        }
        else if(pozice.list==5) {
          pozice.list = 8;
          pozice.sloupec = 4;
          pozice.radek += 28;
        }
        else if(pozice.list==3) {
          pozice.list = 7;
          pozice.sloupec = 3;
          pozice.radek += 4;
        }
        break;
      case 4: //15
        if(pozice.list==1) {
          pozice.radek += 3;
        }
        else if(pozice.list==2) {
          pozice.radek += 3;
        }
        else if(pozice.list==3) {
          pozice.radek += 5;
        }
        pozice.list = 9;
        pozice.sloupec = 2;
        break;
      case 5: //16
        if(pozice.list==1) {
          pozice.radek += 3;
        }
        else if(pozice.list==2) {
          pozice.radek += 3;
        }
        else if(pozice.list==3) {
          pozice.radek += 5;
        }
        pozice.list = 10;
        pozice.sloupec = 2;
        break;
      case 6: //17
        if(pozice.list==1) {
          pozice.radek += 3;
        }
        else if(pozice.list==2) {
          pozice.radek += 3;
        }
        else if(pozice.list==3) {
          pozice.radek += 4;
        }
        pozice.list = 11;
        pozice.sloupec = 3;
        break;
      default:
        logger.error("Neznďż˝mďż˝ druh dokladu:" + dokladTyp);
    }
    pozice.radek ++;//Posunuti o 1 radek kvuli popisu
    return pozice;
  }
  
  private void outputDoklad(Number lIdDoklad, int intPozice) 
  {
    CellStyle style = null;
    
    ViewObject vo = dm.findViewObject("KpDatDokladpolozkaView1");
    vo.clearCache();    
    vo.setWhereClause("ID_DOKLAD = " + lIdDoklad + " AND S_MENA='"+menaSpol+"' AND ND_CASTKALOCAL<>0");
    vo.setOrderByClause("NL_PORADILIST,NL_RADEK,NL_SLOUPEC");
    int koef = 1;
    Pozice pozice = new Pozice();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      pozice.radek = ((Number)row.getAttribute("NlRadek")).intValue();
      pozice.sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      pozice.list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();

      if(pozice.list==1) koef=1; else koef=-1;

//pozice.print(11);
      getPozice(intPozice, pozice);
      
      double castkaOrig = 0;
      if(wb.getSheetAt(pozice.list).getRow(pozice.radek)!=null &&
         wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec)!=null &&
         wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).getCellType()==wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).CELL_TYPE_NUMERIC) 
      {
        castkaOrig = wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).getNumericCellValue();
      }
      setCellValue(pozice.list, pozice.radek, pozice.sloupec, castkaOrig + koef*castkaLocal, style);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputPodnikatel(boolean ifrs) 
  {
    CellStyle style = null;
    
    ViewObject vo = dm.findViewObject("KpDatDokladpolozkaView1");
    vo.clearCache();    
    vo.setWhereClause("ID_DOKLAD = " + (ifrs ? idDokladPodnikIFRS : idDokladPodnikLocal) + " AND S_MENA='"+menaSpol+"' AND ND_CASTKALOCAL<>0");
    
//System.out.println ( ifrs + "   " + (ifrs ? idDokladPodnikIFRS : idDokladPodnikLocal) + " AND S_MENA='"+menaSpol+"' AND ND_CASTKALOCAL<>0");    

    vo.setOrderByClause("NL_PORADILIST,NL_RADEK,NL_SLOUPEC");
    int koef = 1;
    Pozice pozice = new Pozice();
    int llist = ifrs ? 1 : 2;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      pozice.radek = ((Number)row.getAttribute("NlRadek")).intValue();
      pozice.sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      pozice.list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      //if(pozice.list==1 && pozice.radek< 69 ) koef=1; else koef=-1;
      if ( ifrs )
        if(pozice.list==1 ) koef=1; else koef=-1;
      else        
        if(pozice.list==1 && pozice.radek<69 ) koef=1; else koef=-1;
//System.out.println ( pozice.list + "  " + pozice.radek + "   " + castkaLocal + "  " + koef*castkaLocal);
//pozice.print(ifrs?12:13);
      getPozice(llist,pozice);
      
      double castkaOrig = 0;
      if(wb.getSheetAt(pozice.list).getRow(pozice.radek)!=null &&
         wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec)!=null &&
         wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).getCellType()==wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).CELL_TYPE_NUMERIC) 
      {
        castkaOrig = wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).getNumericCellValue();
      }
      setCellValue(pozice.list, pozice.radek, pozice.sloupec, castkaOrig + koef*castkaLocal, style);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
  
  private void outputRKC() 
  {
    CellStyle style = null;
    
    ViewObject vo = dm.findViewObject("KpDatDokladpolozkaView1");
    vo.clearCache();    
    vo.setWhereClause("ID_DOKLAD = " + idDokladRKC+" AND ND_CASTKALOCAL<>0");
    vo.setOrderByClause("NL_PORADILIST,NL_RADEK,NL_SLOUPEC");
    int koef = 1;
    Pozice pozice = new Pozice();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      pozice.radek = ((Number)row.getAttribute("NlRadek")).intValue();
      int sloupec = pozice.sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      int list = pozice.list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      String mena = (String)row.getAttribute("SMena");
      double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
      double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
      double castka = 0;

      if(pozice.list==1) koef=1; else koef=-1;
      
//pozice.print(14);
      getPozice(3,pozice);

      if(sloupec==1 && list!=3) {
        if("CZK".equals(menaSpol)) 
        {
          if("CZK".equals(mena)) {
            castka = castkaMena;
          }
          else {
            castka = castkaLocal;
            pozice.sloupec += 1;
          }
        }
        else 
        {
          if(menaSpol.equals(mena)) 
          {
            castka = castkaMena;
            pozice.sloupec += 1;
          }
          else if("CZK".equals(mena)) {
            castka = castkaLocal;
          }
          else {
            castka = castkaLocal;
            pozice.sloupec += 1;
          }
        }
      }
      else 
      {
        if(menaSpol.equals(mena)) 
        {
          castka = castkaLocal;
        }
      }

      double castkaOrig = 0;
      if(wb.getSheetAt(pozice.list).getRow(pozice.radek)!=null &&
         wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec)!=null &&
         wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).getCellType()==wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).CELL_TYPE_NUMERIC) 
      {
        castkaOrig = wb.getSheetAt(pozice.list).getRow(pozice.radek).getCell((short)pozice.sloupec).getNumericCellValue();
      }
      setCellValue(pozice.list, pozice.radek, pozice.sloupec, castkaOrig + koef*castka, style);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  protected boolean outputData () 
  {
/*
    Font font = wb.createFont();
    font.setColor(IndexedColors.BLUE.getIndex());
    styleJtfg = wb.createCellStyle();
    styleJtfg.setFont(font);

    Font font2 = wb.createFont();
    font2.setColor(IndexedColors.GREEN.getIndex());
    styleMis = wb.createCellStyle();
    styleMis.setFont(font2);
*/
    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputDoklad(idDoklad, 0);
setCellValue(0, 0, 3, "ID_DOKLAD="+idDoklad, null);
    logger.info("Dogenerovany listy std. dokladu");
    outputPodnikatel(true);
setCellValue(2, 0, 3, "ID_DOKLAD="+idDokladPodnikIFRS, null);
    logger.info("Dogenerovany listy bilance podnikatel IFRS");
    outputPodnikatel(false);
setCellValue(4, 0, 3, "ID_DOKLAD="+idDokladPodnikLocal, null);
    logger.info("Dogenerovany listy bilance podnikatel lokal");
    outputRKC();
setCellValue(6, 0, 3, "ID_DOKLAD="+idDokladRKC, null);
    logger.info("Dogenerovany listy RKC");
    outputDoklad(idDoklad15, 4);
setCellValue(9, 0, 2, "ID_DOKLAD="+idDoklad15, null);
    logger.info("Dogenerovan list krok 15");
    outputDoklad(idDoklad16, 5);
setCellValue(10, 0, 2, "ID_DOKLAD="+idDoklad16, null);
    logger.info("Dogenerovan list krok 16");
    outputDoklad(idDoklad17, 6);
setCellValue(11, 0, 3, "ID_DOKLAD="+idDoklad17, null);
    logger.info("Dogenerovan list krok 17");
    
    //if(idDoklad==null && idDokladPodnikIFRS==null && idDokladPodnikLocal==null && idDokladRKC==null) return false;

    return true;
  }

  public void deleteAllOriginalFiles() throws IOException {
    // MIGRATED: Platform-independent file separator
    int indexDir = getFileAbsoluteName().lastIndexOf(File.separator);
    String dirName = getFileAbsoluteName().substring(0,indexDir);
    int podtrzitko = getFileName().lastIndexOf('_');
    int tecka = getFileName().lastIndexOf('.');
    String filter = getFileName().substring(podtrzitko+1,tecka);
    File dir = new File(dirName);
    FileFilter ff = getDeleteFileFilter(filter,specialDoklad);
    File[] arr = dir.listFiles(ff);
    if(arr != null) {
      for( int i=0; i<arr.length; i++ ) 
      {
        logger.info("Mazani souboru "+arr[i].getName()+": "+arr[i].delete());        
        //arr[i].delete();
      }
    }
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportDoklad2007 ed = new ESExportDoklad2007(dm,
                                               new Number(1045),
                                               new java.sql.Date((new java.util.Date(107,7,31)).getTime()));
      ed.deleteAllOriginalFiles();
      ed.excelOutput();

      // MIGRATED: Platform-independent Excel invocation
      Runtime rt = Runtime.getRuntime();
      String excelPath = System.getProperty("kis.excel.path",
                                           PathConstants.isWindows()
                                           ? "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE"
                                           : "libreoffice --calc");
      String[] callAndArgs = { excelPath, "" };
      callAndArgs[1]=ed.getFileAbsoluteName();
      if (!PathConstants.isLinux() || System.getenv("DISPLAY") != null) {
        Process pExcel = rt.exec(callAndArgs);
      } else {
        logger.info("Skipping Excel auto-open in headless Linux environment. File: " + ed.getFileAbsoluteName());
      }
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
}

class Pozice 
{
  int list;
  int radek;
  int sloupec;
  public void print(int krok) 
  {
    System.out.println("krok="+krok+",(list,radek,sloupec)=("+list+","+radek+","+sloupec+")");
  }
}
