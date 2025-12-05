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

public class ESExportSubkonsDoklad extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsDoklad.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private int subkonsId;
  private java.sql.Date datum;
  private boolean mis;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredpona;
  private boolean rkc;
  private int sloupcu;
  private int vazeb;
  private boolean blue;

  private int idSpol;
  private int dokladId;
  private int parentId;

  private List etapy;
  private CellStyle styleBold, biggerBold, styleVV;
  private CellStyle s, s1, s2;
  private List spolecnosti;
  private Map etapyPoSpol;
  
  private int[] colNr = new int[] {2,2,2,2,2};
  private int[] lists = new int[] {0,1,2,3,4};
  
  private int logRowNr = 0;
  
  private static final int[] radku = new int[]
                                     {178,127,41,43,162};
                                     
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private CellStyle lightBlue;
  
  private static final int POSUN = 5;
  private static final int MAX_SLOUPCU = 240;

  public ESExportSubkonsDoklad(ApplicationModule dokladyModule,
                               int subkonsId,
                               java.sql.Date datum,
                               boolean mis,
                               boolean blue)
  {
    dm = dokladyModule;
    this.subkonsId = subkonsId;
    this.datum = datum;
    this.mis = mis;
    this.blue = blue;
    init();
  }

  private void init() {
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+subkonsId);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("SNazev");
      menaSpol = (String) row.getAttribute("SMena");
      souborPredpona = (String) row.getAttribute("SSouborpredpona");
    }
    vo.closeRowSet();
/*
    vo.setWhereClause("ID = (select sub.ID_KTGUCETNISPOLECNOSTMATKA from DB_JT.KP_KTG_SUBKONSOLIDACE sub where sub.ID_KTGUCETNISPOLECNOST = "+subkonsId+")");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      menaSpol = (String) row.getAttribute("SMena");
    }
    vo.closeRowSet();
*/    

    //PUVODNE BYLO PRO VSECHNY RKC
    /*
    ViewObject voSkup = dm.findViewObject("VwKpSubkonsolidaceView1");
    voSkup.clearCache();
    voSkup.setWhereClause("ID_KTGUCETNISPOLECNOST = "+subkonsId);
    if(voSkup.hasNext()) 
    {
      Row row = voSkup.next();
      int ucSkup = ((Number)row.getAttribute("IdKtgucetniskupina")).intValue();
      //rkc = "1".equals(row.getAttribute("CExportflagczk"));
    }
    voSkup.closeRowSet();
    */
    //NOVE - JEN BANKA A BEA
    rkc = 10001==subkonsId || 10150==subkonsId;

    dm.getTransaction().commit();
    logger.info("ExportSubkonsDoklad:nazevSpol="+nazevSpol+",datum="+datum+",menaSpol="+menaSpol);  
    
    if(rkc) { sloupcu = 3; vazeb = 2; }
    else { sloupcu = 2; vazeb = 1; }

    String special = mis?"Mis":"";
    if(blue) special="Blue";
    setFileName ( "Subkons"+special+subkonsId+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredpona+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaSubkonsolidace2.xlsx" );
    
    etapy = getEtapy();
    etapyPoSpol = new HashMap();
  }
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    
    for(int i=0;i<=POSUN;i+=POSUN) {
      setCellValue(i,0,0,nazevSpol,style);    
      setCellValue(i+1,0,0,nazevSpol,style);    
      setCellValue(i+2,0,0,nazevSpol,style);    
      setCellValue(i+3,0,0,nazevSpol,style);    
      setCellValue(i+4,0,0,nazevSpol,style);    
      
      setCellValue(i,3,0,"AKTIVA CELKEM v "+menaSpol,style);
      setCellValue(i+1,3,0,"PASIVA CELKEM v "+menaSpol,style);
      setCellValue(i+2,3,0,"PODR. AKTIVA CELKEM v "+menaSpol,style);
      setCellValue(i+3,3,0,"PODR. PASIVA CELKEM v "+menaSpol,style);
      setCellValue(i+4,3,0,"VSLEDOVKA CELKEM v "+menaSpol,style);
  
      String txtDatum = sdf.format(datum);
      setCellValue(i,4,0,txtDatum,style);
      setCellValue(i+1,4,0,txtDatum,style);
      setCellValue(i+2,4,0,txtDatum,style);
      setCellValue(i+3,4,0,txtDatum,style);
      setCellValue(i+4,4,0,txtDatum,style);
    }
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

    fillVazby();
    fillCelkem();

    spolecnosti = new ArrayList();
    
    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                            " AND NL_KROK = "+(!blue?100:500));
    voDoklad.setOrderByClause("ID_KTGUCETNISPOLECNOST DESC");
    while(voDoklad.hasNext()) 
    {
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

        if(s==s1) s=s2; else s=s1;

        boolean equity = setHeader(idSpol);

        int rowNr;   
        int koef = 1;
        
        for(int list=1; list<=5; list++) {
          if(!equity) {
            int listNr = list-1;
            int listCol = list==1 ? 3 : 1;
            ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
            vo.clearCache();
            vo.setWhereClause("ID_DOKLAD = "+dokladId+" AND NL_PORADILIST = "+list);
            while(vo.hasNext()) 
            {
              Row row = vo.next();
              int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
              int radek = ((Number)row.getAttribute("NlRadek")).intValue();
              double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
              double castkaCzk = ((Number)row.getAttribute("NdCastkaczk")).doubleValue();
              
              if(sloupec==3 && rkc) sloupec=4;
              rowNr = radek+4;
              if(castkaLocal!=0.0) setCellValue(lists[listNr],rowNr,colNr[listNr]+sloupec-1,koef*castkaLocal,style);
              if(rkc && castkaCzk!=0.0) setCellValue(lists[listNr],rowNr,colNr[listNr]+sloupec,koef*castkaCzk,style);
            }
            vo.closeRowSet();
    
            vo.clearCache();
            vo.setWhereClause("ID_DOKLAD = "+parentId+" AND NL_PORADILIST = "+list);
            while(vo.hasNext()) 
            {
              Row row = vo.next();
              int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
              int radek = ((Number)row.getAttribute("NlRadek")).intValue();
              double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
    
              if(sloupec==3 && rkc) sloupec=4;
              rowNr = radek+4;
              
              if(castkaLocal!=0.0) setCellValue(lists[listNr],rowNr,colNr[listNr]+sloupec+sloupcu-2,koef*castkaLocal,style);
            }
            vo.closeRowSet();
            if(list==1) calcPOP();
            colNr[listNr] += sloupcu*listCol;
          }
          fillEtapy(list,equity);
        }
        
        outputLog(dokladId, 100);
      }
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();

    cleanUp();
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
                 " AND DT_ClenstviOD <= TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                 " AND DT_ClenstviDO >= TRUNC(TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy'),'yyyy')"+
                 " AND ID_CISSUBTYPCLENSTVI=3");
    boolean equity = false;
    if(vo.hasNext()) 
    {
      equity = true;
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

if(colNr[0]>MAX_SLOUPCU) 
{
  cleanUp(0);
  setCellValue(lists[0],2,colNr[0],"Pekroen max. poet sloupc excelu - pokraovn na dalm listu (2)",styleBold);
  lists[0]+=POSUN;
  colNr[0]=2;
}

    int etColNr = colNr[0];
    if(equity) setCellValue(lists[0],1,etColNr,"EQUITY",styleBold);
    else setCellValue(lists[0],1,etColNr,"",styleBold);
    for(int i=etColNr+1; i<etColNr+(rkc?9:6); i++) setCellValue(lists[0],2,i,(String)null,s);
    setCellValue(lists[0],2,etColNr,nazev+" ("+mena+")",s);
    if(!equity) {
      setCellValue(lists[0],3,etColNr,"Poloky",style);
      setCellValue(lists[0],3,etColNr+sloupcu,"Opravn poloky",style);
      setCellValue(lists[0],3,etColNr+2*sloupcu,"Poloky bez OP",style);
      setCellValue(lists[0],4,etColNr++,"Celkem v "+menaSpol,style);
      if(rkc) setCellValue(lists[0],4,etColNr++,"z toho CZK",style);
      setCellValue(lists[0],4,etColNr++,"Celkem v "+mena,style);
      setCellValue(lists[0],4,etColNr++,"Celkem v "+menaSpol,style);
      if(rkc) setCellValue(lists[0],4,etColNr++,"z toho CZK",style);
      setCellValue(lists[0],4,etColNr++,"Celkem v "+mena,style);
      setCellValue(lists[0],4,etColNr++,"Celkem v "+menaSpol,style);
      if(rkc) setCellValue(lists[0],4,etColNr++,"z toho CZK",style);
      setCellValue(lists[0],4,etColNr++,"Celkem v "+mena,style);
  
      for(int listNr=1;listNr<5;listNr++) {

if(colNr[listNr]>MAX_SLOUPCU) 
{
  cleanUp(listNr);
  setCellValue(lists[listNr],2,colNr[listNr],"Pekroen max. poet sloupc excelu - pokraovn na dalm listu (2)",styleBold);
  lists[listNr]+=POSUN;
  colNr[listNr]=2;
}

        etColNr = colNr[listNr];
        setCellValue(lists[listNr],2,etColNr,nazev+" ("+mena+")",s);
        for(int i=etColNr+1; i<etColNr+(rkc?3:2); i++) setCellValue(lists[listNr],2,i,(String)null,s);
        setCellValue(lists[listNr],3,etColNr,"Poloky",style);
        setCellValue(lists[listNr],4,etColNr++,"Celkem v "+menaSpol,style);
        if(rkc) setCellValue(lists[listNr],4,etColNr++,"z toho CZK",style);
        setCellValue(lists[listNr],4,etColNr++,"Celkem v "+mena,style);
      }
    }
    
    return equity;
  }
  
  private void cleanUp() 
  {
    for(int listNr=0;listNr<5;listNr++) {
/*
      for(int x=colNr[listNr];x<256;x++) {
        for(int y=0;y<=radku[listNr];y++) {
          clearCell(lists[listNr],y,x);
        }
      }
*/
      cleanUp(listNr);
    }
  }

  private void cleanUp(int listNr) 
  {
    for(int x=colNr[listNr];x<256;x++) {
      for(int y=0;y<=radku[listNr];y++) {
        clearCell(lists[listNr],y,x);
      }
    }
  }
  
  private void calcPOP() 
  {
    CellStyle style = null;
    for(int y=5;y<=radku[0];y++) {
      for(int x=colNr[0]; x<colNr[0]+sloupcu; x++) {
        Cell c1 = wb.getSheetAt(lists[0]).getRow(y).getCell((short)x);
        Cell c2 = wb.getSheetAt(lists[0]).getRow(y).getCell((short)(x+sloupcu));
        boolean d1=false,d2=false;
        double valC1=0.0,valC2=0.0;
        if(c1 != null) {
          d1 = c1.getCellType() == Cell.CELL_TYPE_NUMERIC;
          if(d1) valC1 = c1.getNumericCellValue();
        }
        if(c2 != null) {
          d2 = c2.getCellType() == Cell.CELL_TYPE_NUMERIC;
          if(d2) valC2 = c2.getNumericCellValue();
        }
        
        if(d1 || d2)
          setCellValue(lists[0],y,x+2*sloupcu,(valC2+valC1),style);
      }
    }
  }

  private void fillEtapy(int list, boolean equity) 
  {
    CellStyle style = null;
    for(int i=0; i<etapy.size(); i++) 
    {
      int etapaId = ((Etapa)etapy.get(i)).id;
      ViewObject voDoklad = dm.findViewObject("KpDatDokladView2");
      voDoklad.clearCache();
      voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                              " AND ID_KTGUCETNISPOLECNOST = "+idSpol+
                              " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                              " AND ID_KTGSUBKONSOLIDACEETAPA = "+etapaId+
                              " AND NL_KROK BETWEEN "+(!blue?"101 AND 199":"501 AND 599"));
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
          vo.setWhereClause("ID_DOKLAD = "+etaDokladId+" AND NL_SLOUPEC = 1 AND NL_PORADILIST = "+list+" AND ND_CASTKALOCAL <> 0");
          boolean hasLines = vo.hasNext();
          if(hasLines) 
          {
            String etapa = ((Etapa)etapy.get(i)).popis;
            if(!equity) setCellValue(lists[listNr],2,colNr[listNr],(String) null,s);
            setCellValue(lists[listNr],3,colNr[listNr],etapa,styleBold);
          }
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
            
            if(getEtapySize(list)>0) {
              rowNr = radek+4;
              
              if((castkaLocal)!=0.0) setCellValue(lists[listNr],rowNr,colNr[listNr],koef*castkaLocal,style);
            }
          }
          vo.closeRowSet();
          if(hasLines) colNr[listNr]++;
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
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                            " AND NL_KROK = "+(!blue?200:600));
    while(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      Number nDokladId = (Number) rowDoklad.getAttribute("Id");
      if(nDokladId == null) 
      {
        logger.error("Chyba! Pro subkonsId="+subkonsId+" a krok="+(!blue?200:600)+" plati dokladId="+dokladId);
      }
      else {
        dokladId = nDokladId.intValue();

        int rowNr;   
        int koef = 1;

        for(int list=1;list<=5;list++) {
          int listNr = list-1;
          int listCol = list==1 ? 3 : 1;
        
          setCellValue(lists[listNr],2,colNr[listNr],"Vzjemn vazby",styleVV);
          setCellValue(lists[listNr],4,colNr[listNr],"Celkem v "+menaSpol,style);
//          if(rkc) setCellValue(lists[listNr],4,colNr[listNr]+1,"z toho CZK",style);
  
          ViewObject vo = dm.findViewObject("VwKpDokladvvexcelView1");
          vo.clearCache();
          vo.setWhereClause("IDDOKLAD = "+dokladId+
                            " AND NL_PORADILIST = "+list);
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();

            rowNr = radek+4;
            
            if(castkaLocal!=0.0) setCellValue(lists[listNr],rowNr,colNr[listNr],koef*castkaLocal,style);
          }
          vo.closeRowSet();
          colNr[listNr]++;
//          if(rkc) colNr[listNr]++;
        }
      }
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();
  }

  private void fillCelkem() 
  {
    CellStyle style = null;
    //list 1 (+vazby)
    int etColNr = colNr[0];
    setCellValue(lists[0],2,etColNr,"CELKEM",biggerBold);
    for(int i=etColNr+1; i<etColNr+(rkc?6:3); i++) setCellValue(lists[0],2,i,(String)null,biggerBold);
    setCellValue(lists[0],3,etColNr,"Poloky",style);
    setCellValue(lists[0],3,etColNr+(rkc?2:1),"Opravn poloky",style);
    setCellValue(lists[0],3,etColNr+(rkc?4:2),"Poloky bez OP",style);
    setCellValue(lists[0],4,etColNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(lists[0],4,etColNr++,"z toho CZK",style);
    setCellValue(lists[0],4,etColNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(lists[0],4,etColNr++,"z toho CZK",style);
    setCellValue(lists[0],4,etColNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(lists[0],4,etColNr++,"z toho CZK",style);
    //list 2-5
    for(int listNr=1;listNr<5;listNr++) {
      etColNr = colNr[listNr];
      setCellValue(lists[listNr],2,etColNr,"CELKEM",biggerBold);
      for(int i=etColNr+1; i<etColNr+(rkc?2:1); i++) setCellValue(lists[listNr],2,i,(String)null,biggerBold);
      setCellValue(lists[listNr],3,etColNr,"Poloky",style);
      setCellValue(lists[listNr],4,etColNr,"Celkem v "+menaSpol,style);
      if(rkc) setCellValue(lists[listNr],4,etColNr+1,"z toho CZK",style);
    }

    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                            " AND NL_KROK = "+(!blue?200:600));
    while(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      Number nDokladId = (Number) rowDoklad.getAttribute("Id");
      if(nDokladId == null) 
      {
        logger.error("Chyba! Pro subkonsId="+subkonsId+" a krok="+(!blue?200:600)+" plati dokladId="+dokladId);
      }
      else {
        dokladId = nDokladId.intValue();

        int rowNr;   
        int koef = 1;

        for(int list=1;list<=5;list++) {

          int listCol = list==1 ? 3 : 1;
          int listNr = list-1;

          ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
          vo.clearCache();
          vo.setWhereClause("ID_DOKLAD = "+dokladId+" AND NL_PORADILIST = "+list);
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
            double castkaCzk = ((Number)row.getAttribute("NdCastkaczk")).doubleValue();
            
            if(sloupec == 3 && !rkc) sloupec = 2;
  
            rowNr = radek+4;
            
if(list!=1 || radek!=121) {
            if(castkaLocal!=0.0) setCellValue(lists[listNr],rowNr,colNr[listNr]+sloupec-1,koef*castkaLocal,style);
            if(rkc && castkaCzk!=0.0) setCellValue(lists[listNr],rowNr,colNr[listNr]+sloupec,koef*castkaCzk,style);
}
          }
          vo.closeRowSet();
        }
        
        outputLog(dokladId, 200);
      }
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();

    for(int y=5;y<=radku[0];y++) {
      for(int x=colNr[0]; x<colNr[0]+(rkc?2:1); x++) {
        Cell c1 = wb.getSheetAt(lists[0]).getRow(y).getCell((short)x);
        Cell c2 = wb.getSheetAt(lists[0]).getRow(y).getCell((short)(x+(rkc?2:1)));
        boolean d1=false,d2=false;
        double valC1=0.0,valC2=0.0;
        if(c1 != null) {
          d1 = c1.getCellType() == Cell.CELL_TYPE_NUMERIC;
          if(d1) valC1 = c1.getNumericCellValue();
        }
        if(c2 != null) {
          d2 = c2.getCellType() == Cell.CELL_TYPE_NUMERIC;
          if(d2) valC2 = c2.getNumericCellValue();
        }
        
        if(d1 || d2)
          setCellValue(lists[0],y,x+2*(rkc?2:1),(valC2+valC1),style);
      }
    }
    
    for(int list=1;list<=5;list++) {
      colNr[list-1] += (list==1?3:1)*(rkc?2:1);
    }
  }

  private Set logged = new HashSet();

  private void outputLog(int idDoklad, int krok) {
    String key = idDoklad + "-" + krok;
    if(logged.contains(key)) return;
    else logged.add(key);
  
    int listNr=5+POSUN;
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

  private void outputLogProblem() {
    int rowNr = 0,
        colNr = 0,
        listNr = 6+POSUN;
  
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*80));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));

    colNr=0;
    setCellValue( listNr, rowNr, colNr, "Krok" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Popis" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Doklad" , lightBlue ); colNr++;
    rowNr++;
  
    ViewObject vo = dm.findViewObject("KpDatDokladprotokolView1");
    vo.setWhereClause("ID_SUBDOKLAD = (select id from db_jt.kp_dat_doklad d where d.ID_KTGUCETNISPOLECNOST = "+subkonsId+" and d.DT_DATUM = to_date('"+sdf.format(datum)+"','dd.mm.yyyy') and d.nl_krok = "+(!blue?200:600)+")");
    vo.setOrderByClause("NL_KROK, DT_DATUM, S_NAZEV");
    vo.clearCache();
    while(vo.hasNext()) 
    {
      colNr = 0;  
      Row row = vo.next();

      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("NlKrok") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SHlaska") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdDoklad") , null ); 
      colNr++;

      rowNr++;
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

    Font biggerFont = wb.createFont();
    biggerFont.setFontHeight((short) 350);
    biggerFont.setBold(true);
    biggerBold = wb.createCellStyle();
    biggerBold.setFont(biggerFont);
    biggerBold.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
    biggerBold.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    styleVV = wb.createCellStyle();
    styleVV.setFont(font);
    styleVV.setFillForegroundColor(IndexedColors.GREEN.getIndex());
    styleVV.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    s1 = wb.createCellStyle();
    s1.setFont(font);
    s1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    s1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    s2 = wb.createCellStyle();
    s2.setFont(font);
    s2.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
    s2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputDoklad();
    logger.info("Dogenerovany prvni listy");
    outputLogProblem();
    logger.info("Dogenerovan log problemy");
    
    for(int i=4;i>=0;i--) 
    {
      if(lists[i]!=POSUN+i) wb.removeSheetAt(POSUN+i);
    }
    
    return true;
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
      ESExportSubkonsDoklad ed = new ESExportSubkonsDoklad(dm,
                                                           10150,
                                                           new java.sql.Date(106,8,30),
                                                           false,
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
