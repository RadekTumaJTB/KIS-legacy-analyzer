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

public class ESExportSubkonsDokladKamilDoklad extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsDokladKamilDoklad.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private int subkonsId;
  private java.sql.Date datum;
  private boolean mis;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredpona;
  private String listek;
  private boolean rkc;
  private int sloupcu;
  private int vazeb;

  private int idSpol;
  private int dokladId;
  private int parentId;

  private List etapy;
  private CellStyle styleBold;
  private List spolecnosti;
  private Map etapyPoSpol;
  
  private int colNr = 3;
  
  private int logRowNr = 0;
  
  private static final int[] radku = new int[]
                                     {178,127,41,43,162};
                                     
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private String strDatum;
  
  private CellStyle lightBlue;
  
  private boolean hasData = false;
  
  private int YELLOWS = 50;
  
  private double celkemPK = 0.0,
                 celkemPBN = 0.0,
                 celkemAK = 0.0;
  private double vvPK = 0.0,
                 vvPBN = 0.0,
                 vvAK = 0.0;

  public ESExportSubkonsDokladKamilDoklad(ApplicationModule dokladyModule,
                               int subkonsId,
                               java.sql.Date datum,
                               boolean mis)
  {
    dm = dokladyModule;
    this.subkonsId = subkonsId;
    this.datum = datum;
    this.mis = mis;
    strDatum = sdf.format(datum);
    init();
  }

  private void init() {
    ViewObject vo = dm.findViewObject("VwKpSubkonsolidaceView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+subkonsId);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("SNazev");
      menaSpol = (String) row.getAttribute("SMena");
      souborPredpona = (String) row.getAttribute("SSouborpredpona");
      listek = (String) row.getAttribute("STypkurslistek");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    logger.info("ExportSubkonsDokladKamilDoklad:nazevSpol="+nazevSpol+",datum="+datum+",menaSpol="+menaSpol);  
    
    String special = mis?"Mis":"";
    setFileName ( "SubkonsKD"+special+subkonsId+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredpona+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaSubkonsolidaceKamil.xlsx" );
    
    etapy = getEtapy();
    etapyPoSpol = new HashMap();
  }
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    
    setCellValue(0,4,0,strDatum,style);
  }

  private List getEtapy() 
  {
    ArrayList list = new ArrayList();
  
    ViewObject vo = dm.findViewObject("KpKtgSubkonsolidaceetapaView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGSUBKONSOLIDACE = "+subkonsId);
    vo.setOrderByClause("NL_PORADI");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int id = ((Number) row.getAttribute("Id")).intValue();
      String popis = (String) row.getAttribute("SPopis");
      list.add(new Etapa(id,popis));
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    return list;
  }
  
  private int getEtapySize(int list) 
  {
    if(list==3 || list==4) return 0;
    else return etapy.size();
  }

  private void outputDoklad() 
  {
    CellStyle style = null;

    spolecnosti = new ArrayList();
    
    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+strDatum+"','dd.mm.yyyy')"+
                            " AND NL_KROK = 300");
    while(voDoklad.hasNext()) 
    {
      hasData = true;
      Row rowDoklad = voDoklad.next();
      Number nIdSpol = (Number) rowDoklad.getAttribute("IdKtgucetnispolecnost"); 
      Number nDokladId = (Number) rowDoklad.getAttribute("Id");
      Number nParentId = (Number) rowDoklad.getAttribute("IdParent");
      if(nDokladId == null || nParentId == null || nIdSpol == null) 
      {
        logger.error("Chyba! Pro subkonsId="+subkonsId+" plati dokladId="+nDokladId+",parentId="+nParentId+",idSpol="+nIdSpol);
      }
      else {
        spolecnosti.add(nIdSpol);
        idSpol = nIdSpol.intValue();
        dokladId = nDokladId.intValue();
        parentId = nParentId.intValue();

        boolean equity = setHeader(idSpol);

        int rowNr;   
        int koef = 1;

        int list=1;
        if(!equity) {
          int listNr = list-1;
          ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
          vo.clearCache();
          vo.setWhereClause("ID_DOKLAD = "+dokladId);
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
            
            rowNr = radek+4;
            
            if(castkaLocal!=null) setCellValue(listNr,rowNr,colNr,koef*castkaLocal.doubleValue(),style);
          }
          vo.closeRowSet();
          
          addYellows(idSpol,listNr);
  
          colNr ++;
        }
        fillEtapy(list);
        
        outputLog(dokladId, 300);//100?
      }
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();

    fillVazby();
    fillCelkem();

    cleanUp();
  }
  
  private void addYellows(int idSpol,int listNr) 
  {
    ViewObject vo = dm.findViewObject("KpDatDokladView2");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+idSpol+
                 " and DT_DATUM = TO_DATE('"+strDatum+"','dd.mm.yyyy')"+
                 //" and nl_krok = 100");//PREJ KONSOLIDACE NEJSOU!
                 " and nl_krok in (1,2) and c_baseDoklad = '1'");
    Number idPodDoklad = null;
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idPodDoklad = (Number) row.getAttribute("Id");
    }
    vo.closeRowSet();
    if(idPodDoklad==null) return;
    
    //PASIVA JTPE
    double castka=0.0;
    vo = dm.findViewObject("VwKpDokladprehledprotistranaSum1");
    vo.clearCache();
    vo.setWhereClauseParams(new Object[] {listek,
                                          menaSpol,
                                          strDatum,
                                          strDatum,
                                          String.valueOf(subkonsId),
                                          idPodDoklad,
                                          "2",
                                          strDatum
                                          });
    vo.setWhereClause("substr(s_ucet,1,1) = 'L'");                                      
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number nCastka = (Number) row.getAttribute("NdCastka");
      if(nCastka!=null) castka += nCastka.doubleValue();      
      boolean cntVazby = "1".equals(row.getAttribute("CntVazby"));
      if(cntVazby && nCastka!=null) vvPK += nCastka.doubleValue();
    }
    vo.closeRowSet();
    setCellValue(listNr,YELLOWS,colNr,castka,null);
    celkemPK += castka;

    //PASIVA NASE BANKY
    castka=0.0;
    vo.clearCache();
    vo.setWhereClauseParams(new Object[] {listek,
                                          menaSpol,
                                          strDatum,
                                          strDatum,
                                          String.valueOf(subkonsId),
                                          idPodDoklad,
                                          "1",
                                          strDatum
                                          });
    vo.setWhereClause("NL_PORADILIST = 2 and NL_RADEK between 2 and 14");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number nCastka = (Number) row.getAttribute("NdCastka");
      if(nCastka!=null) castka += nCastka.doubleValue();
      boolean cntVazby = "1".equals(row.getAttribute("CntVazby"));
      if(cntVazby && nCastka!=null) vvPBN += nCastka.doubleValue();
    }
    vo.closeRowSet();
    setCellValue(listNr,YELLOWS+1,colNr,castka,null);
    celkemPBN += castka;

    //AKTIVA JTPE
    castka=0.0;
    vo.clearCache();
    vo.setWhereClauseParams(new Object[] {listek,
                                          menaSpol,
                                          strDatum,
                                          strDatum,
                                          String.valueOf(subkonsId),
                                          idPodDoklad,
                                          "2",
                                          strDatum
                                          });
    vo.setWhereClause("NL_PORADILIST = 1");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number nCastka = (Number) row.getAttribute("NdCastka");
      if(nCastka!=null) castka += nCastka.doubleValue();
      boolean cntVazby = "1".equals(row.getAttribute("CntVazby"));
      if(cntVazby && nCastka!=null) vvAK += nCastka.doubleValue();
    }
    vo.closeRowSet();
    setCellValue(listNr,YELLOWS+4,colNr,castka,null);
    celkemAK += castka;
  }

  private void addYellows(int listNr,boolean vazby) 
  {
    if(!vazby) {
      setCellValue(listNr,YELLOWS,colNr,celkemPK-vvPK,null);
      setCellValue(listNr,YELLOWS+1,colNr,celkemPBN-vvPBN,null);
      setCellValue(listNr,YELLOWS+4,colNr,celkemAK-vvAK,null);
    }
    else 
    {
      setCellValue(listNr,YELLOWS,colNr,-vvPK,null);
      setCellValue(listNr,YELLOWS+1,colNr,-vvPBN,null);
      setCellValue(listNr,YELLOWS+4,colNr,-vvAK,null);
    }
  }
  
  private boolean setHeader(int idSpol) 
  {
    CellStyle style = null;
    String nazev = null;
    String mena = null;
  
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idSpol);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazev = (String) row.getAttribute("SNazev");
      mena = (String) row.getAttribute("SMena");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    vo = dm.findViewObject("KpRelSubkonsolidaceclenView1");
    vo.setWhereClause("ID_KTGSUBKONSOLIDACE = " + subkonsId +  
                 " AND ID_KTGUCETNISPOLECNOST = "+idSpol +
//                 " AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO"+
                 " AND DT_ClenstviOD <= TO_DATE('"+strDatum+"','dd.mm.yyyy')"+
                 " AND DT_ClenstviOD >= TRUNC(TO_DATE('"+strDatum+"','dd.mm.yyyy'),'yyyy')"+
                 " AND ID_CISSUBTYPCLENSTVI=3");
    boolean equity = false;
    if(vo.hasNext()) 
    {
      equity = true;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    
    int etColNr = colNr;
    if(equity) setCellValue(0,1,etColNr,"EQUITY",styleBold);
    else setCellValue(0,1,etColNr,"",styleBold);
    setCellValue(0,2,etColNr,nazev+" ("+mena+")",styleBold);
    setCellValue(0,3,etColNr,"Poloky",style);
    setCellValue(0,4,etColNr++,"Celkem v "+menaSpol,style);
    
    return equity;
  }
  
  private void cleanUp() 
  {
    for(int x=colNr;x<256;x++) {
      for(int y=0;y<=60;y++) {
        clearCell(0,y,x);
      }
    }
  }

  private void fillEtapy(int list) 
  {
    CellStyle style = null;
    for(int i=0; i<etapy.size(); i++) 
    {
      int etapaId = ((Etapa)etapy.get(i)).id;
      ViewObject voDoklad = dm.findViewObject("KpDatDokladView2");
      voDoklad.clearCache();
      voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                              " AND ID_KTGUCETNISPOLECNOST = "+idSpol+
                              " AND DT_DATUM = TO_DATE('"+strDatum+"','dd.mm.yyyy')"+
                              " AND ID_KTGSUBKONSOLIDACEETAPA = "+etapaId);
      while(voDoklad.hasNext()) 
      {
        Row rowDoklad = voDoklad.next();
        Number nDokladId = (Number) rowDoklad.getAttribute("Id");
        Number nKrok = (Number) rowDoklad.getAttribute("NlKrok");
        Number nIdSpol = (Number) rowDoklad.getAttribute("IdKtgucetnispolecnost");
        if(nDokladId == null) 
        {
          logger.error("Chyba! Pro subkonsId="+subkonsId+" a etapu="+etapaId+" plati dokladId="+nDokladId);
        }
        else {
          int etaDokladId = nDokladId.intValue();
  
          int listNr;    
          int rowNr;   
          int koef = 1;

          int listCol = list==1 ? 3 : 1;
          listNr = list-1;
  
          ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView2");
          vo.clearCache();
          vo.setWhereClause("ID_DOKLAD = "+etaDokladId+" AND ND_CASTKALOCAL <> 0");
          boolean hasLines = vo.hasNext();
          if(hasLines) 
          {
            String etapa = ((Etapa)etapy.get(i)).popis;
            setCellValue(listNr,3,colNr,etapa,styleBold);
          }
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
            
            if(getEtapySize(list)>0) {
              rowNr = radek+4;
              
              if(castkaLocal!=null) setCellValue(listNr,rowNr,colNr,koef*castkaLocal.doubleValue(),style);
            }
          }
          vo.closeRowSet();
          if(hasLines) colNr++;
          outputLog(etaDokladId, nKrok.intValue());
        }
      }
      voDoklad.closeRowSet();
    }
  }

  private void fillVazby() 
  {
    CellStyle style = null;

    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+strDatum+"','dd.mm.yyyy')"+
                            " AND NL_KROK = 200");
    while(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      Number nDokladId = (Number) rowDoklad.getAttribute("Id");
      if(nDokladId == null) 
      {
        logger.error("Chyba! Pro subkonsId="+subkonsId+" a krok=200 plati dokladId="+dokladId);
      }
      else {
        dokladId = nDokladId.intValue();

        int rowNr;   
        int koef = 1;

        int listNr = 0;
      
        setCellValue(listNr,2,colNr,"Vzjemn vazby",styleBold);
        setCellValue(listNr,4,colNr,"Celkem v "+menaSpol,style);

        ViewObject vo = dm.findViewObject("VwKpDokladprehledvazbyView1");
        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = "+dokladId);
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();

          rowNr = radek+4;
          
          if(castkaLocal!=0.0) setCellValue(listNr,rowNr,colNr,koef*castkaLocal,style);
        }
        vo.closeRowSet();
        
        addYellows(listNr,true);
        
        colNr++;
      }
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();
  }

  private void fillCelkem() 
  {
    CellStyle style = null;
    //list 1 (+vazby)
    int etColNr = colNr;
    setCellValue(0,2,etColNr,"CELKEM",styleBold);
    setCellValue(0,3,etColNr,"Poloky",style);
    setCellValue(0,4,etColNr++,"Celkem v "+menaSpol,style);

    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+strDatum+"','dd.mm.yyyy')"+
                            " AND NL_KROK = 400");
    while(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      Number nDokladId = (Number) rowDoklad.getAttribute("Id");
      if(nDokladId == null) 
      {
        logger.error("Chyba! Pro subkonsId="+subkonsId+" a krok=400 plati dokladId="+dokladId);
      }
      else {
        dokladId = nDokladId.intValue();

        int rowNr;   
        int koef = 1;

        int listNr = 0;

        ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = "+dokladId);
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");

          rowNr = radek+4;
          
          if(castkaLocal!=null) setCellValue(listNr,rowNr,colNr,koef*castkaLocal.doubleValue(),style);
        }
        vo.closeRowSet();
        
        addYellows(listNr,false);
        
        colNr++;
        outputLog(dokladId, 400);//200?
      }
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();
  }

  private Set logged = new HashSet();

  private void outputLog(int idDoklad, int krok) {
    String key = idDoklad + "-" + krok;
    if(logged.contains(key)) return;
    else logged.add(key);
  
    int listNr=1;
    int colNr=0;

    if(logRowNr == 0) {
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
  
      colNr=0;
      setCellValue( listNr, logRowNr, colNr, "Krok" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "Spolenost" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "List" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "Sloupec" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "dek" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "Popis" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "Pvodn stka" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "Nov stka" , lightBlue ); colNr++;
      setCellValue( listNr, logRowNr, colNr, "Zmna" , lightBlue ); colNr++;
      
      logRowNr++;
    }
  
    ViewObject vo = dm.findViewObject("VwKpDokladpolozkadenikView1");
    vo.setWhereClause("ID_DOKLAD = "+idDoklad);
    vo.clearCache();
    while(vo.hasNext()) 
    {
      colNr = 0;    
      Row row = vo.next();
      String nazev = (String)row.getAttribute("SNazevSpol");
      String list = (String)row.getAttribute("SPopisList");
      Number hlpNum = (Number) row.getAttribute("NlSloupec");
      String sloupec = hlpNum==null ? "" : hlpNum.toString();
      hlpNum = (Number) row.getAttribute("NlRadek");
      String radek = hlpNum==null ? "" : hlpNum.toString();
      String popis = (String)row.getAttribute("SPopis");
      hlpNum = (Number) row.getAttribute("NdOldValue");
      double oldValue = hlpNum==null ? -1 : hlpNum.doubleValue();
      hlpNum = (Number) row.getAttribute("NdNewValue");
      double newValue = hlpNum==null ? -1 : hlpNum.doubleValue();
      hlpNum = (Number) row.getAttribute("NdZmena");
      double zmena = hlpNum==null ? -1 : hlpNum.doubleValue();

      setCellValue( listNr, logRowNr, colNr, ""+krok , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, nazev , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, list , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, sloupec , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, radek , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, popis , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, oldValue , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, newValue , null ); 
      colNr++;
      setCellValue( listNr, logRowNr, colNr, zmena , null ); 
      colNr++;

      logRowNr++;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  protected boolean outputData () 
  {
    Font font = wb.createFont();
    font.setFontHeight((short) 250);
    font.setBold(true);
    styleBold = wb.createCellStyle();
    styleBold.setFont(font);

    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputDoklad();
    logger.info("Dogenerovany prvni listy");
//    outputLogProblem();
//    logger.info("Dogenerovan log problemy");
    
    return hasData;
  }

  public String getNazevSpol() 
  {
    return nazevSpol;
  }

  public String getIdSubkons() 
  {
    return String.valueOf(subkonsId);
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportSubkonsDokladKamilDoklad ed = new ESExportSubkonsDokladKamilDoklad(dm,
                                                           10221,
                                                           new java.sql.Date(106,11,31),
                                                           false);
      ed.excelOutput();

      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=ed.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
  
}
