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

public class ESExportSpolecnosti extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSpolecnosti.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  private boolean sPredavanim = false;

  private String dir;

  private CellStyle styleBold;

  public ESExportSpolecnosti(ApplicationModule dokladyModule,
                             java.sql.Date datum)
  {
    logger.info("ESExportSpolecnosti:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_SPOLECNOSTI;
    init();
  }

  public ESExportSpolecnosti(ApplicationModule dokladyModule,
                             java.sql.Date datum,
                             boolean sPredavanim)
  {
    logger.info("ESExportSpolecnosti:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    this.sPredavanim = sPredavanim;
    dir = Constants.DIR_SPOL_PREDAV;
    init();
  }

  private void init() {
    setFileName ( "Spolecnosti_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private String getRokAudit() 
  {
    String rok = null;
    Calendar cal = Calendar.getInstance();
    cal.setTime(datum);
    int mesic = cal.get(Calendar.MONTH);
    if(mesic < 3) 
      rok = String.valueOf(cal.get(Calendar.YEAR)-1);
    else
      rok = String.valueOf(cal.get(Calendar.YEAR));
    return rok;
  }
  
  private void outputSpolecnosti() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat sdfRok = new SimpleDateFormat("yyyy");
    String rok = sdfRok.format(datum);
    String rokAudit = getRokAudit();
System.out.println(rokAudit);
    int listNr=0;
    int rowNr=0;
    int colNr=0;

    CellStyle lightBlue = wb.createCellStyle();
    //lightBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*4));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*8));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    if(sPredavanim) 
    {
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*40));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*30));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    }

    colNr=0;
    setCellValue( listNr, rowNr, colNr, "Id" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Nzev" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "I" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "I formtovan" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Pevodov mstek" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Mna etnictv" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "DB - Link" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Zodpovdn etn" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Datum archivace" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Opce se smlouvou" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Opce" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Nominee" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Odpovdn osoba" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "OO holdingu" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "TopMng" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Administrtor" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Kategorie spolenosti" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Holding" , lightBlue ); colNr++;

    setCellValue( listNr, rowNr, colNr, "Poznmka" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Rezidentstv (zem spol.)" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Datum dokladu" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Zkladn jmn" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Vlastn jmn" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Poet transakc" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Max. datum transakce" , lightBlue ); colNr++;

    setCellValue( listNr, rowNr, colNr, "Schvleno" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Typ projektu" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "On-line" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "On-line OD" , lightBlue ); colNr++;
    if(sPredavanim) 
    {
      setCellValue( listNr, rowNr, colNr, "Priorita" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Acc. status" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Uzaven etnictv k" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Datum" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Kategorie" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Odsouhlaseno - Jarka" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Odsouhlaseno - Janka" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Odsouhlaseno - Top" , lightBlue ); colNr++;
    }    
    rowNr++;
  
    ViewObject vo = dm.findViewObject("VwKtgUcetnispolecnostView1");
    String whereVo = "ID_KATEGORIE<>300";
    if(sPredavanim) 
    {
      whereVo += " AND EXISTS (SELECT NULL FROM DB_JT.KP_KTG_UCSPOLPREDAVANI K WHERE VwKtgUcetnispolecnost.ID = K.ID_KTGUCETNISPOLECNOST)";
    }
    vo.setWhereClause(whereVo);
    vo.clearCache();
    ViewObject voSkup = dm.findViewObject("KpRelSubkonsolidaceclenView1");
    voSkup.clearCache();
    ViewObject voFDPH = dm.findViewObject("KpCisFrekvencedphView1");
    voFDPH.clearCache();
    ViewObject voZamek = dm.findViewObject("KpDatDokladZamekView1");
    voZamek.clearCache();
    ViewObject voPredavani = dm.findViewObject("VwKtgUcspolpredavaniView1");
    voPredavani.clearCache();
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      Number hlpNum = (Number) row.getAttribute("Id");
      int id = hlpNum==null ? -1 : hlpNum.intValue();
      oracle.jbo.domain.Date dtArch = (oracle.jbo.domain.Date) row.getAttribute("DtDatumarchivace");
      String datumArchivace = dtArch==null ? "" : sdf.format(dtArch.dateValue());
      String opce = "1".equals(row.getAttribute("COpce")) ? "ANO" : "NE";

      setCellValue( listNr, rowNr, colNr, ""+id , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazev") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SIco") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SIcoformatovane") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Subject") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SDblink") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Zodpovednaucetni") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, datumArchivace , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, opce , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Cisopce") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Nominee") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Odpovednaosoba") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegmentboss") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Topmng") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Administrator") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Katspol") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Mngsegment") , null ); 
      colNr++;
      //
/*
      String datumStr = "TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')";
      String skup="NE", platnostOd="", platnostDo="",equity="";
      voSkup.setWhereClause("ID_KTGUCETNISPOLECNOST = "+id+" AND ID_KTGUCETNISKUPINA = 1 AND "+datumStr+" BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
      if(voSkup.hasNext()) 
      {
        Row rowSkup = voSkup.next();
        skup = "ANO";
        oracle.jbo.domain.Date dtOd = (oracle.jbo.domain.Date) rowSkup.getAttribute("DtPlatnostod");
        oracle.jbo.domain.Date dtDo = (oracle.jbo.domain.Date) rowSkup.getAttribute("DtPlatnostdo");
        platnostOd = dtOd==null?"":sdf.format(dtOd.dateValue());
        platnostDo = dtDo==null?"":sdf.format(dtDo.dateValue());
        int tc = ((Number)rowSkup.getAttribute("IdCissubtypclenstvi")).intValue();
        Number hodn = (Number)rowSkup.getAttribute("NdPomerucasti");
        if(tc==1) equity = "Pln";
        if(tc==2 && hodn!=null) equity = hodn+" %";
        if(tc==3) equity = "Equity";
      }
      voSkup.closeRowSet();
      setCellValue( listNr, rowNr, colNr, skup , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, platnostOd , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, platnostDo , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, equity , null ); 
      colNr++;
      //
      skup="NE"; platnostOd=""; platnostDo=""; equity="";
      voSkup.setWhereClause("ID_KTGUCETNISPOLECNOST = "+id+" AND ID_KTGUCETNISKUPINA = 2 AND "+datumStr+" BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
      if(voSkup.hasNext()) 
      {
        Row rowSkup = voSkup.next();
        skup = "ANO";
        oracle.jbo.domain.Date dtOd = (oracle.jbo.domain.Date) rowSkup.getAttribute("DtPlatnostod");
        oracle.jbo.domain.Date dtDo = (oracle.jbo.domain.Date) rowSkup.getAttribute("DtPlatnostdo");
        platnostOd = dtOd==null?"":sdf.format(dtOd.dateValue());
        platnostDo = dtDo==null?"":sdf.format(dtDo.dateValue());
        int tc = ((Number)rowSkup.getAttribute("IdCissubtypclenstvi")).intValue();
        Number hodn = (Number)rowSkup.getAttribute("NdPomerucasti");
        if(tc==1) equity = "Pln";
        if(tc==2 && hodn!=null) equity = hodn+" %";
        if(tc==3) equity = "Equity";
      }
      voSkup.closeRowSet();
      setCellValue( listNr, rowNr, colNr, skup , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, platnostOd , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, platnostDo , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, equity , null ); 
      colNr++;
*/      
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPoznamka") , null ); 
      colNr++;
      String country = (String)row.getAttribute("SCountry");
      setCellValue( listNr, rowNr, colNr, country , null ); 
      colNr++;
      //
      String datumDoklad="", datumMaxTr = "";
      double zj=0.0, vj=0.0;
      int pocetTransakci = 0;
      oracle.jbo.domain.Date dtDoklad = (oracle.jbo.domain.Date) row.getAttribute("MaxDatum");
      datumDoklad = dtDoklad==null ? "" : sdf.format(dtDoklad.dateValue());
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Zj");
      if(hlpNum!=null) zj=hlpNum.doubleValue();
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Vj");
      if(hlpNum!=null) vj=hlpNum.doubleValue();
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Pocettransakci");
      if(hlpNum!=null) pocetTransakci=hlpNum.intValue();
      oracle.jbo.domain.Date dtMaxTr = (oracle.jbo.domain.Date) row.getAttribute("DtMaxtransakce");
      datumMaxTr = dtMaxTr==null ? "" : sdf.format(dtMaxTr.dateValue());

      setCellValue( listNr, rowNr, colNr, datumDoklad , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, zj , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, vj , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, pocetTransakci , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, datumMaxTr , null ); 
      colNr++;
      //
      String schvaleno = "NEN";
      voZamek.setWhereClause("ID_KTGUCETNISPOLECNOST = "+id);
      if(voZamek.hasNext()) 
      {
        Row rowZamek = voZamek.next();
        oracle.jbo.domain.Date dtSch = (oracle.jbo.domain.Date) rowZamek.getAttribute("maxDatum");
        schvaleno = dtSch==null ? "" : sdf.format(dtSch.dateValue());
      }
      voZamek.closeRowSet();
      setCellValue( listNr, rowNr, colNr, schvaleno , null ); 
      colNr++;
      String typProjektu = row.getAttribute("IdTypprojektu")==null ? "" : row.getAttribute("IdTypprojektu").toString();
      setCellValue( listNr, rowNr, colNr, typProjektu , null ); 
      colNr++;
      String online = "1".equals(row.getAttribute("COnline")) ? "ANO" : "NE";
      setCellValue( listNr, rowNr, colNr, online , null ); 
      colNr++;
      oracle.jbo.domain.Date dtOnline = (oracle.jbo.domain.Date) row.getAttribute("DtOnlineod");
      String datumOnlineOd = dtOnline==null ? "" : sdf.format(dtOnline.dateValue());
      setCellValue( listNr, rowNr, colNr, datumOnlineOd , null ); 
      colNr++;

      if(sPredavanim) 
      {
        voPredavani.setWhereClause("ID_KTGUCETNISPOLECNOST = "+id);
        if(voPredavani.hasNext()) 
        {
          Row rowPredavani = voPredavani.next();
          setCellValue( listNr, rowNr, colNr, (String)rowPredavani.getAttribute("Prenospriorita") , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (String)rowPredavani.getAttribute("SAccstatus") , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (String)rowPredavani.getAttribute("SUzavreneucetnictvi") , null ); 
          colNr++;
          oracle.jbo.domain.Date datum = (oracle.jbo.domain.Date) rowPredavani.getAttribute("DtDatum");
          String sDatum = datum==null ? "" : sdf.format(datum.dateValue());
          setCellValue( listNr, rowNr, colNr, sDatum , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (String)rowPredavani.getAttribute("Prenoskategorie") , null ); 
          colNr++;
          oracle.jbo.domain.Date dtSch = (oracle.jbo.domain.Date) rowPredavani.getAttribute("DtOdsouhlasenojarka");
          schvaleno = "NEN";
          schvaleno = dtSch==null ? "" : sdf.format(dtSch.dateValue());
          setCellValue( listNr, rowNr, colNr, schvaleno , null ); 
          colNr++;
          dtSch = (oracle.jbo.domain.Date) rowPredavani.getAttribute("DtOdsouhlasenojanka");
          schvaleno = "NEN";
          schvaleno = dtSch==null ? "" : sdf.format(dtSch.dateValue());
          setCellValue( listNr, rowNr, colNr, schvaleno , null ); 
          colNr++;
          dtSch = (oracle.jbo.domain.Date) rowPredavani.getAttribute("DtOdsouhlasenotop");
          schvaleno = "NEN";
          schvaleno = dtSch==null ? "" : sdf.format(dtSch.dateValue());
          setCellValue( listNr, rowNr, colNr, schvaleno , null ); 
          colNr++;        
        }
        voPredavani.closeRowSet();
      }
      
      rowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputSpolecnosti();
      end = System.currentTimeMillis();    
      logger.debug("spolecnosti:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.ucskup.UcSkupModule","UcSkupModuleLocal");
      ESExportSpolecnosti ed = new ESExportSpolecnosti(dm,
                                                       new java.sql.Date(System.currentTimeMillis()));
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
