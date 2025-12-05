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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportKonsPravidla extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportKonsPravidla.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private int listNr=0;

  public ESExportKonsPravidla(ApplicationModule dokladyModule,
                        java.sql.Date datum)
  {
    logger.info("ESExportKonsPravidla:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_KONSOLIDACNI_ZMENY;
    init();
  }

  private void init() {
    if(datum != null) 
      setFileName ( "KonsZmeny_"+datum+".xlsx" );
    else
      setFileName ( "KonsZmeny_Historie.xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputListy()
  {
    CellStyle style = null;

    int rowNr=0;
    int colNr=0;

    CellStyle lightBlue = wb.createCellStyle();
    //lightBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "ID" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*2));
    setCellValue( listNr, rowNr, colNr, "" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Poad" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Poad detail" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Sub-konsolidace" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna sub-konsolidace" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
    setCellValue( listNr, rowNr, colNr, "Mna matky" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Etapa" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Platnost od" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Platnost do" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "List" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*4));
    setCellValue( listNr, rowNr, colNr, "dek" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Typ" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Vzorec" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "et zbytek" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr, "Podmnka" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*100));
    setCellValue( listNr, rowNr, colNr, "Poznmka" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Spol. ref." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Skupina" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Skupina" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;

    ViewObject vo = dm.findViewObject("KpKtgUcetniskupinaView1");
    vo.clearCache();
    while(vo.hasNext()) 
    {
      vo.next();
      if(vo.hasNext()) wb.cloneSheet(0);
    }
    vo.closeRowSet();

    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String nazev = (String) row.getAttribute("SKod");
      Number idSkup = (Number) row.getAttribute("Id");

      wb.setSheetName(listNr,nazev);
      
      outputPravidla(idSkup);
      
      listNr++;
    }
    vo.closeRowSet();
  }
  
  private void outputPravidla(Number idSkup) {
    int rowNr=1;
    int colNr=0;

    String[] listy = {" ","Aktiva","Pasiva","","","Vsledovka"};

    ViewObject voSub = dm.findViewObject("VwKpSubkonsolidaceView1");
    voSub.setWhereClause("ID_KTGUCETNISKUPINA = "+idSkup);
    voSub.clearCache();
//    ViewObject voClen = dm.findViewObject("KpRelSubkonsolidaceclenView1");
//    voClen.clearCache();
    ViewObject voSpol = dm.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();
    ViewObject voEtapa = dm.findViewObject("KpKtgSubkonsolidaceetapaView1");
    voEtapa.clearCache();
    ViewObject voPravidlo = dm.findViewObject("KpKtgSubkonsolidacepravidloView1");
    voPravidlo.clearCache();
    ViewObject voPravidloDetail = dm.findViewObject("KpKtgSubkonsolpravidlodetailView1");
    voPravidloDetail.clearCache();
    ViewObject voIfrsCis = dm.findViewObject("KpCisIfrstyppravidloView1");
    voIfrsCis.clearCache();
    ViewObject voIfrsCisDetail = dm.findViewObject("KpCisIfrstyppravidlodetailView1");
    voIfrsCisDetail.clearCache();
    while(voSub.hasNext()) 
    {
      Row row = voSub.next();
      Number id = (Number)row.getAttribute("IdKtgucetnispolecnost");
      String nazev = (String)row.getAttribute("SNazev");
      String mena = (String)row.getAttribute("SMena");
      String menaMatky = (String)row.getAttribute("SMenamatka");
      String kodSkup = (String)row.getAttribute("SKod");
      String popisSkup = (String)row.getAttribute("SPopis");
      String whereKons = "ID_KTGSUBKONSOLIDACE = "+id;
      if(datum != null)
        whereKons += " AND TO_DATE('"+sdf.format(datum)+"','DD.MM.YYYY') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
      voPravidlo.setWhereClause(whereKons);
      while(voPravidlo.hasNext()) 
      {
        Row rowPravidlo = voPravidlo.next();
        Number idPravidlo = (Number)rowPravidlo.getAttribute("Id");
        Number idSpol = (Number)rowPravidlo.getAttribute("IdKtgucetnispolecnost");
        Number idEtapa = (Number)rowPravidlo.getAttribute("IdKtgsubkonsolidaceetapa");
        Number poradi = (Number)rowPravidlo.getAttribute("IdPoradi");
        String poznamka = (String)rowPravidlo.getAttribute("SPoznamka");
        Number typ = (Number)rowPravidlo.getAttribute("IdCisifrstyppravidlo");
        Date hlp = (Date)rowPravidlo.getAttribute("DtPlatnostod");
        String platnostOd = hlp==null?"":sdf.format(hlp.dateValue());
        hlp = (Date)rowPravidlo.getAttribute("DtPlatnostdo");
        String platnostDo = hlp==null?"":sdf.format(hlp.dateValue());
        Number validaceList = (Number)rowPravidlo.getAttribute("NlValidacelist");
        Number validaceRadek = (Number)rowPravidlo.getAttribute("NlValidaceradek");
        Number validaceHodnota = (Number)rowPravidlo.getAttribute("NdValidacehodnota");
        
        String spol = "";
        voSpol.setWhereClause("ID = "+idSpol);
        if(voSpol.hasNext()) {
          Row rowSpol = voSpol.next();
          spol = (String)rowSpol.getAttribute("SNazev");
        }
        voSpol.closeRowSet();

        String etapa = "";
        voEtapa.setWhereClause("ID = "+idEtapa);
        if(voEtapa.hasNext()) {
          Row rowEtapa = voEtapa.next();
          etapa = (String)rowEtapa.getAttribute("SPopis");
        }
        voEtapa.closeRowSet();

        String typIfrs = "";
        voIfrsCis.setWhereClause("ID = "+typ);
        if(voIfrsCis.hasNext()) {
          Row rowCis = voIfrsCis.next();
          typIfrs = (String)rowCis.getAttribute("SZkratka");
        }
        voIfrsCis.closeRowSet();
        
        voPravidloDetail.setWhereClause("ID_KTGSUBKONSOLIDACEPRAVIDLO = "+idPravidlo);
        while(voPravidloDetail.hasNext()) 
        {
          Row rowPravidloDetail = voPravidloDetail.next();
          
          Number idPravidloDetail = (Number)rowPravidloDetail.getAttribute("Id");
          Number poradiDetail = (Number)rowPravidloDetail.getAttribute("IdPoradi");
          String poznamkaDetail = (String)rowPravidloDetail.getAttribute("SPoznamka");
          Number typDetail = (Number)rowPravidloDetail.getAttribute("IdCisifrspravdetailtyp");
          Number list = (Number)rowPravidloDetail.getAttribute("NlPoradilist");
          Number radek = (Number)rowPravidloDetail.getAttribute("NlRadek");
          Number castka = (Number)rowPravidloDetail.getAttribute("NdCastka");
          String vzorec = (String)rowPravidloDetail.getAttribute("SVzorec");
          String ucetZbytek = (String)rowPravidloDetail.getAttribute("SUcetzbytek");
          String podminka = (String)rowPravidloDetail.getAttribute("SPodminka");

          Number refSpol = (Number)rowPravidloDetail.getAttribute("IdKtgucetnispolecnost");
          String spolRef = "";
          voSpol.setWhereClause("ID = "+refSpol);
          if(voSpol.hasNext()) {
            Row rowRef = voSpol.next();
            spolRef = (String)rowRef.getAttribute("SNazev");
          }
          voSpol.closeRowSet();

          String typIfrsDetail = "";
          voIfrsCisDetail.setWhereClause("ID = "+typDetail);
          if(voIfrsCisDetail.hasNext()) {
            Row rowCis = voIfrsCisDetail.next();
            typIfrsDetail = (String)rowCis.getAttribute("SZkratka");
          }
          voIfrsCisDetail.closeRowSet();

          Number idProtistrana = (Number)rowPravidloDetail.getAttribute("IdProtistrana");
          String protistrana = "";
          voSpol.setWhereClause("ID = "+idProtistrana);
          if(voSpol.hasNext()) {
            Row rowProti = voSpol.next();
            protistrana = (String)rowProti.getAttribute("SNazev");
          }
          voSpol.closeRowSet();

          colNr = 0;
          
          setCellValue( listNr, rowNr, colNr, ""+idPravidloDetail.intValue() , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, "K" , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (poradi==null?"":poradi.toString()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (poradiDetail==null?"":poradiDetail.toString()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, nazev , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, mena , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, menaMatky , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, spol , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, etapa , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, platnostOd , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, platnostDo , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (list==null||list.intValue()<1?"":listy[list.intValue()]) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (radek==null?"":radek.toString()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, typIfrsDetail , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (castka==null?0.0:castka.doubleValue()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, vzorec , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, ucetZbytek , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, podminka , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, poznamkaDetail , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, spolRef , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, kodSkup , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, popisSkup , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, protistrana , null ); 
          
          rowNr++;
        }
        voPravidloDetail.closeRowSet();

        colNr = 0;        
        setCellValue( listNr, rowNr, colNr, ""+idPravidlo.intValue() , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, "K" , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (poradi==null?"":poradi.toString()) , null ); 
        colNr++;
        colNr++;
        setCellValue( listNr, rowNr, colNr, nazev , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, mena , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, menaMatky , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, spol , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, etapa , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, platnostOd , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, platnostDo , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (validaceList==null||validaceList.intValue()<1?"":listy[validaceList.intValue()]) , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (validaceRadek==null?"":validaceRadek.toString()) , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, typIfrs , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (validaceHodnota==null?0.0:validaceHodnota.doubleValue()) , null ); 
        colNr++;
        colNr++;
        colNr++;
        colNr++;
        setCellValue( listNr, rowNr, colNr, poznamka , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, ""+idPravidlo.intValue() , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, kodSkup , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, popisSkup , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String) null , null ); 

        rowNr++;
      }
      voPravidlo.closeRowSet();
    }
    voSub.closeRowSet();
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputListy();
      end = System.currentTimeMillis();    
      logger.debug("Pravidlo zmeny:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.subkons.SubkonsModule","SubkonsModuleLocal");
      ESExportKonsPravidla ed = new ESExportKonsPravidla(dm,//null);
                                         new java.sql.Date(106,7,31));
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
