package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import java.io.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportMilos extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportMilos.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private String idDoklad;
  private String locale;
  private int typRozkladu;
//  private String idSub;
//  private String datum;

  private Number idKtgSpolecnost;
  private String nazevSpol;
  private String menaSpol;
  
  private CellStyle styleBold;
  private CellStyle lightGray;
  private CellStyle lightBlue;
  private CellStyle yellow;
  private CellStyle sub1,sub2;
  
  private int listNr = 0;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private String dokladDatum;

  public ESExportMilos(ApplicationModule dokladyModule,
                       String idDoklad,
                       String locale,
                       int typRozkladu)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    this.locale = locale;
    this.typRozkladu = typRozkladu;
    init();
  }
/*
  public ESExportMilos(ApplicationModule dokladyModule,
                       String idSub,
                       String datum,
                       String locale,
                       int typRozkladu)
  {
    dm = dokladyModule;
    this.idSub = idSub;
    this.datum = datum;
    this.locale = locale;
    this.typRozkladu = typRozkladu;
    init2();
  }
*/
  private void init() {
    ViewObject vo = dm.findViewObject("VwKpDokladzahlaviView1");
    vo.clearCache();
    vo.setWhereClause("DOKLADID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("Spolecnostnazev");
      menaSpol = (String) row.getAttribute("SMena");
      idKtgSpolecnost = (Number) row.getAttribute("Ucetnispolecnostid");
      dokladDatum = sdf.format(((oracle.jbo.domain.Date)row.getAttribute("DtDatum")).dateValue());
      if(locale==null || locale.length()==0) locale = (String) row.getAttribute("SLocale");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    logger.info("ExportMilos:nazevSpol="+nazevSpol+",idDoklad="+idDoklad);  

    setFileName ( "MilosPrehled_"+idDoklad+"_"+typRozkladu+".xlsx" );
    setFileRelativeName( Constants.DIR_DOKLAD_KAMIL+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
/*
  private void init2() {
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idSub);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("SNazev");
      menaSpol = (String) row.getAttribute("SMena");
      idKtgSpolecnost = (Number) row.getAttribute("Id");
      dokladDatum = datum;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    logger.info("ExportMilos:nazevSpol="+nazevSpol+",idSub="+idSub);  
    
    setFileName ( "MilosPrehled_"+idSub+"_"+typRozkladu+".xlsx" );
    setFileRelativeName( Constants.DIR_DOKLAD_KAMIL+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
*/  
  private void outputRozklad()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int rowNr=2;
    int colNr = 0;
    CellStyle style = null;
    
    wb.setSheetName(listNr, "Rozklad na radky");
    setCellValue( listNr, 0, 0, nazevSpol+" / "+menaSpol + " z " + dokladDatum , styleBold );

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    
    setCellValue( listNr, rowNr, 0, "SVAHA" , lightGray );
    setCellValue( listNr, rowNr, 1, "" , lightGray );
    
    ViewObject vo = dm.findViewObject("KpCisRadektextlangMilosView1");
    vo.clearCache();
    vo.setWhereClause("S_LOCALE = '"+locale+"'");
    while ( vo.hasNext() ) {
      Row rowRadek = vo.next();
      rowNr++;
      colNr = 0;

      int idRadek = ((oracle.jbo.domain.Number)rowRadek.getAttribute("Id")).intValue();
      boolean isTypV = "V".equals(rowRadek.getAttribute("CTyp"));
      if(isTypV) style = lightBlue;
      else style = yellow;
      
      if(idRadek==15100) {
        setCellValue( listNr, rowNr, 0, "VSLEDOVKA" , lightGray );
        setCellValue( listNr, rowNr, 1, "" , lightGray );
        rowNr++;
      }
      
      setCellValue( listNr, rowNr, colNr, (String)rowRadek.getAttribute("SPopis"), style );                        
      colNr++;

      String wherePrehled = "ID_DOKLAD = "+idDoklad+" AND ID_CISRADEKTYP = "+idRadek;;
      double cd = 0.0;

      ViewObject voSum = dm.findViewObject("VwKpDokladprehledView1");
      voSum.clearCache();
      voSum.setWhereClause(wherePrehled);
      while(voSum.hasNext()) 
      {
        Row rowSum = voSum.next();
        Number cast = (Number)rowSum.getAttribute("NdCastkalocal");
        if(cast!=null) cd += cast.doubleValue();
      }
      voSum.closeRowSet();
      setCellValue( listNr, rowNr, colNr, cd, style );                        
      colNr++;
      
      if(!isTypV) {
        String whereIn = wherePrehled + " AND S_LOCALE = '"+locale+"'";
        ViewObject voIn = dm.findViewObject("VwKpDokladprehledrozkladView1");
        voIn.clearCache();
        voIn.setWhereClause(whereIn);
        while(voIn.hasNext()) 
        {
          Row rowIn = voIn.next();
          rowNr++;
          colNr=0;
          setCellValue( listNr, rowNr, colNr, (String)rowIn.getAttribute("Textradek"), null );                        
          colNr++;
          Number castIn = (Number)rowIn.getAttribute("NdCastkalocal");
          setCellValue( listNr, rowNr, colNr, castIn.doubleValue(), null );                        
          colNr++;
        }
        voIn.closeRowSet();
      }
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputObdobi()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int rowNr=2;
    int colNr = 0;
    CellStyle style = null;

    wb.setSheetName(listNr, "Rozklad na obdobi");
    setCellValue( listNr, 0, 0, nazevSpol+" / "+menaSpol + " z " + dokladDatum , styleBold );

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    
    setCellValue( listNr, rowNr, 0, "SVAHA" , lightGray );
    setCellValue( listNr, rowNr, 1, "" , lightGray );
    
    ViewObject vo = dm.findViewObject("KpCisRadektextlangMilosView1");
    vo.clearCache();
    vo.setWhereClause("S_LOCALE = '"+locale+"'");
    while ( vo.hasNext() ) {
      Row rowRadek = vo.next();
      rowNr++;
      colNr = 0;

      int idRadek = ((oracle.jbo.domain.Number)rowRadek.getAttribute("Id")).intValue();
      boolean isTypV = "V".equals(rowRadek.getAttribute("CTyp"));
      if(isTypV) style = lightBlue;
      else style = yellow;
      
      if(idRadek==15100) {
        setCellValue( listNr, rowNr, 0, "VSLEDOVKA" , lightGray );
        setCellValue( listNr, rowNr, 1, "" , lightGray );
        rowNr++;
      }
      
      setCellValue( listNr, rowNr, colNr, (String)rowRadek.getAttribute("SPopis"), style );                        
      colNr++;

      String wherePrehled = "ID_DOKLAD = "+idDoklad+" AND ID_CISRADEKTYP = "+idRadek;;
      double cd = 0.0;

      ViewObject voSum = dm.findViewObject("VwKpDokladprehledView1");
      voSum.clearCache();
      voSum.setWhereClause(wherePrehled);
      while(voSum.hasNext()) 
      {
        Row rowSum = voSum.next();
        Number cast = (Number)rowSum.getAttribute("NdCastkalocal");
        if(cast!=null) cd += cast.doubleValue();
      }
      voSum.closeRowSet();
      setCellValue( listNr, rowNr, colNr, cd, style );                        
      colNr++;
      
      if(!isTypV) {
        String whereIn = wherePrehled;
        ViewObject voIn = dm.findViewObject("VwKpDokladprehledobdobiView1");
        voIn.clearCache();
        voIn.setWhereClause(whereIn);
        while(voIn.hasNext()) 
        {
          Row rowIn = voIn.next();
          rowNr++;
          colNr=0;
          setCellValue( listNr, rowNr, colNr, (String)rowIn.getAttribute("Mesic"), null );                        
          colNr++;
          Number castIn = (Number)rowIn.getAttribute("NdCastkalocal");
          setCellValue( listNr, rowNr, colNr, castIn.doubleValue(), null );                        
          colNr++;
        }
        voIn.closeRowSet();
      }
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputProtistrana()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int rowNr=2;
    int colNr = 0;
    CellStyle style = null;
    
    wb.setSheetName(listNr, "Rozklad na protistrany");
    setCellValue( listNr, 0, 0, nazevSpol+" / "+menaSpol + " z " + dokladDatum , styleBold );

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    
    setCellValue( listNr, rowNr, 0, "SVAHA" , lightGray );
    setCellValue( listNr, rowNr, 1, "" , lightGray );
    
    ViewObject vo = dm.findViewObject("KpCisRadektextlangMilosView1");
    vo.clearCache();
    vo.setWhereClause("S_LOCALE = '"+locale+"'");
    while ( vo.hasNext() ) {
      Row rowRadek = vo.next();
      rowNr++;
      colNr = 0;

      int idRadek = ((oracle.jbo.domain.Number)rowRadek.getAttribute("Id")).intValue();
      boolean isTypV = "V".equals(rowRadek.getAttribute("CTyp"));
      if(isTypV) style = lightBlue;
      else style = yellow;
      
      if(idRadek==15100) {
        setCellValue( listNr, rowNr, 0, "VSLEDOVKA" , lightGray );
        setCellValue( listNr, rowNr, 1, "" , lightGray );
        rowNr++;
      }
      
      setCellValue( listNr, rowNr, colNr, (String)rowRadek.getAttribute("SPopis"), style );                        
      colNr++;

      String wherePrehled = "ID_DOKLAD = "+idDoklad+" AND ID_CISRADEKTYP = "+idRadek;;
      double cd = 0.0;

      ViewObject voSum = dm.findViewObject("VwKpDokladprehledView1");
      voSum.clearCache();
      voSum.setWhereClause(wherePrehled);
      while(voSum.hasNext()) 
      {
        Row rowSum = voSum.next();
        Number cast = (Number)rowSum.getAttribute("NdCastkalocal");
        if(cast!=null) cd += cast.doubleValue();
      }
      voSum.closeRowSet();

      setCellValue( listNr, rowNr, colNr, cd, style );                        
      colNr++;
      if(!isTypV) {
        String whereIn = wherePrehled;
        ViewObject voIn = dm.findViewObject("VwKpDokladprehledprotistranaView1");
        voIn.clearCache();
        voIn.setWhereClause(whereIn);
        while(voIn.hasNext()) 
        {
          Row rowIn = voIn.next();
          rowNr++;
          colNr=0;
          setCellValue( listNr, rowNr, colNr, (String)rowIn.getAttribute("Spolecnost"), null );                        
          colNr++;
          Number castIn = (Number)rowIn.getAttribute("NdCastkalocal");
          setCellValue( listNr, rowNr, colNr, castIn.doubleValue(), null );                        
          colNr++;
          castIn = (Number)rowIn.getAttribute("NdCastkamena");
          setCellValue( listNr, rowNr, colNr, castIn.doubleValue(), null );                        
          colNr++;
          setCellValue( listNr, rowNr, colNr, (String)rowIn.getAttribute("SMena"), null );                        
          colNr++;
        }
        voIn.closeRowSet();
      }
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputUcet()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int rowNr=2;
    int colNr = 0;
    CellStyle style = null;
    
    wb.setSheetName(listNr, "Rozklad na ucty");
    setCellValue( listNr, 0, 0, nazevSpol+" / "+menaSpol + " z " + dokladDatum , styleBold );

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    
    setCellValue( listNr, rowNr, 0, "SVAHA" , lightGray );
    setCellValue( listNr, rowNr, 1, "" , lightGray );
    
    ViewObject vo = dm.findViewObject("KpCisRadektextlangMilosView1");
    vo.clearCache();
    vo.setWhereClause("S_LOCALE = '"+locale+"'");
    while ( vo.hasNext() ) {
      Row rowRadek = vo.next();
      rowNr++;
      colNr = 0;

      int idRadek = ((oracle.jbo.domain.Number)rowRadek.getAttribute("Id")).intValue();
      boolean isTypV = "V".equals(rowRadek.getAttribute("CTyp"));
      if(isTypV) style = lightBlue;
      else style = yellow;
      
      if(idRadek==15100) {
        setCellValue( listNr, rowNr, 0, "VSLEDOVKA" , lightGray );
        setCellValue( listNr, rowNr, 1, "" , lightGray );
        rowNr++;
      }
      
      setCellValue( listNr, rowNr, colNr, (String)rowRadek.getAttribute("SPopis"), style );                        
      colNr++;

      String wherePrehled = "ID_DOKLAD = "+idDoklad+" AND ID_CISRADEKTYP = "+idRadek;;
      double cd = 0.0;

      ViewObject voSum = dm.findViewObject("VwKpDokladprehledView1");
      voSum.clearCache();
      voSum.setWhereClause(wherePrehled);
      while(voSum.hasNext()) 
      {
        Row rowSum = voSum.next();
        Number cast = (Number)rowSum.getAttribute("NdCastkalocal");
        if(cast!=null) cd += cast.doubleValue();
      }
      voSum.closeRowSet();
      setCellValue( listNr, rowNr, colNr, cd, style );                        
      colNr++;

      if(!isTypV) {
        String whereIn = wherePrehled + " AND S_LOCALE = '"+locale+"'";
        ViewObject voIn = dm.findViewObject("VwKpDokladprehleducetView1");
        voIn.clearCache();
        voIn.setWhereClause(whereIn);
        while(voIn.hasNext()) 
        {
          Row rowIn = voIn.next();
          rowNr++;
          colNr=0;
          String txt = rowIn.getAttribute("SUcet") + " - " + rowIn.getAttribute("Textradek");
          setCellValue( listNr, rowNr, colNr, txt, null );                        
          colNr++;
          Number castIn = (Number)rowIn.getAttribute("NdCastkalocal");
          setCellValue( listNr, rowNr, colNr, castIn.doubleValue(), null );                        
          colNr++;
          castIn = (Number)rowIn.getAttribute("NdCastkamena");
          setCellValue( listNr, rowNr, colNr, castIn.doubleValue(), null );                        
          colNr++;
          setCellValue( listNr, rowNr, colNr, (String)rowIn.getAttribute("SMena"), null );                        
          colNr++;
        }
        voIn.closeRowSet();
      }
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
/*
  private void outputSubClen()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    int rowNr=2;
    int colNr = 0;
    CellStyle style = null;
    
    wb.setSheetName(listNr, "Rozklad na cleny");
    setCellValue( listNr, 0, 0, nazevSpol+" / "+menaSpol + " z " + dokladDatum , styleBold );

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    
    setCellValue( listNr, rowNr, 0, "SVAHA" , lightGray );
    setCellValue( listNr, rowNr, 1, "" , lightGray );
    
    ViewObject vo = dm.findViewObject("KpCisRadektextlangMilosView1");
    vo.clearCache();
    vo.setWhereClause("S_LOCALE = '"+locale+"'");
    while ( vo.hasNext() ) {
      Row rowRadek = vo.next();
      rowNr++;
      colNr = 0;

      int idRadek = ((oracle.jbo.domain.Number)rowRadek.getAttribute("Id")).intValue();
      boolean isTypV = "V".equals(rowRadek.getAttribute("CTyp"));
      if(isTypV) style = lightBlue;
      else style = yellow;
      
      if(idRadek==15100) {
        setCellValue( listNr, rowNr, 0, "VSLEDOVKA" , lightGray );
        setCellValue( listNr, rowNr, 1, "" , lightGray );
        rowNr++;
      }
      
      setCellValue( listNr, rowNr, colNr, (String)rowRadek.getAttribute("SPopis"), style );                        
      colNr++;

      String wherePrehled = "ID_SUBKONSOLIDACE = "+idSub+" AND DT_DATUM = TO_DATE('"+datum+"','yyyy-mm-dd')";
      wherePrehled += cz.jtbank.konsolidace.common.Utils.getWhereKamilRadek(idRadek);
      double cd = 0.0;

      ViewObject voSum = dm.findViewObject("VwKpSubkonprehledclenView1");
      voSum.clearCache();
      voSum.setWhereClause(wherePrehled+" AND NL_KROK = 400");
      while(voSum.hasNext()) 
      {
        Row rowSum = voSum.next();
        Number cast = (Number)rowSum.getAttribute("NdCastkalocal");
        if(cast!=null) cd += cast.doubleValue();
      }
      voSum.closeRowSet();
      setCellValue( listNr, rowNr, colNr, cd, style );                        
      colNr++;
      
      if(!isTypV) {
        String whereIn = wherePrehled + " AND NL_KROK BETWEEN 300 AND 399";
        ViewObject voIn = dm.findViewObject("VwKpSubkonprehledclenView2");
        voIn.clearCache();
        voIn.setWhereClause(whereIn);
        while(voIn.hasNext()) 
        {
          Row rowIn = voIn.next();
          rowNr++;
          colNr=0;

          boolean etapa = ((oracle.jbo.domain.Number)rowIn.getAttribute("NlKrok")).intValue()==100;
          boolean base = "1".equals(rowIn.getAttribute("Base"));
          style = null;
          if(etapa) style = sub1;
          if(base) style = sub2;
          
          setCellValue( listNr, rowNr, colNr, (String)rowIn.getAttribute("SNazev"), null );                        
          colNr++;
          Number castIn = (Number)rowIn.getAttribute("NdCastkalocal");
          if(castIn!=null) setCellValue( listNr, rowNr, colNr, castIn.doubleValue(), style );                        
          colNr++;
        }
        voIn.closeRowSet();
      }
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
*/
  protected boolean outputData () 
  {
    lightGray = wb.createCellStyle();
    lightGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightGray.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    yellow = wb.createCellStyle();
    yellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    yellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    sub1 = wb.createCellStyle();
    sub1.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
    sub1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    sub2 = wb.createCellStyle();
    sub2.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    sub2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    
    Font font = wb.createFont();
    font.setFontHeightInPoints((short) 12);
    font.setBold(true);
    styleBold = wb.createCellStyle();
    styleBold.setFont(font);

    switch(typRozkladu) {
      case 1: outputRozklad(); break;
      case 2: outputObdobi(); break;
      case 3: outputProtistrana(); break;
      case 4: outputUcet(); break;
      default:
        wb.cloneSheet(0);
        wb.cloneSheet(0);
        wb.cloneSheet(0);
        outputRozklad();
        listNr++;
        outputObdobi();
        listNr++;
        outputProtistrana();
        listNr++;
        outputUcet();
        break;
//      case 10: outputSubClen(); break;
    }
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportMilos ed = new ESExportMilos(dm,
                                               "535054",
                                               "cs_CZ",
                                               0);
/*
      ESExportMilos ed = new ESExportMilos(dm,
                                               "10197",
                                               "2006-03-31",
                                               "cs_CZ",
                                               10);
*/
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
