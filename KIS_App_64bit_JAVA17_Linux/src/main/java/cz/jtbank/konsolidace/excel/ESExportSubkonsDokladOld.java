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

public class ESExportSubkonsDokladOld extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsDokladOld.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private int subkonsId;
  private java.sql.Date datum;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredpona;
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
  
  private int logRowNr = 0;
  
  private static final int[] radku = new int[]
                                     {178,127,41,43,162};
                                     
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportSubkonsDokladOld(ApplicationModule dokladyModule,
                               int subkonsId,
                               java.sql.Date datum)
  {
    dm = dokladyModule;
    this.subkonsId = subkonsId;
    this.datum = datum;
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
    ViewObject voSkup = dm.findViewObject("VwKpSubkonsolidaceView1");
    voSkup.clearCache();
    voSkup.setWhereClause("ID_KTGUCETNISPOLECNOST = "+subkonsId);
    if(voSkup.hasNext()) 
    {
      Row row = voSkup.next();
      int ucSkup = ((Number)row.getAttribute("IdKtgucetniskupina")).intValue();
      rkc = (ucSkup==1);
    }
    voSkup.closeRowSet();
    dm.getTransaction().commit();
    logger.info("ExportSubkonsDoklad:nazevSpol="+nazevSpol+",datum="+datum+",menaSpol="+menaSpol);  
    
    if(rkc) { sloupcu = 3; vazeb = 2; }
    else { sloupcu = 2; vazeb = 1; }

    setFileName ( "Subkons"+subkonsId+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredpona+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaSubkonsolidace.xlsx" );
    
    etapy = getEtapy();
    etapyPoSpol = new HashMap();
  }
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    
    setCellValue(0,0,0,nazevSpol,style);    
    setCellValue(1,0,0,nazevSpol,style);    
    setCellValue(2,0,0,nazevSpol,style);    
    setCellValue(3,0,0,nazevSpol,style);    
    setCellValue(4,0,0,nazevSpol,style);    
    
    setCellValue(0,3,0,"AKTIVA CELKEM v "+menaSpol,style);
    setCellValue(1,3,0,"PASIVA CELKEM v "+menaSpol,style);
    setCellValue(2,3,0,"PODR. AKTIVA CELKEM v "+menaSpol,style);
    setCellValue(3,3,0,"PODR. PASIVA CELKEM v "+menaSpol,style);
    setCellValue(4,3,0,"VSLEDOVKA CELKEM v "+menaSpol,style);

    String txtDatum = sdf.format(datum);
    setCellValue(0,4,0,txtDatum,style);
    setCellValue(1,4,0,txtDatum,style);
    setCellValue(2,4,0,txtDatum,style);
    setCellValue(3,4,0,txtDatum,style);
    setCellValue(4,4,0,txtDatum,style);
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
    int spolCnt = 0;
    
    spolecnosti = new ArrayList();
  
    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                            " AND NL_KROK = 100");
    while(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      Number nIdSpol = (Number) rowDoklad.getAttribute("IdKtgucetnispolecnost"); 
      Number nDokladId = (Number) rowDoklad.getAttribute("Id");
      Number nParentId = (Number) rowDoklad.getAttribute("IdParent");
      if(nDokladId == null || nParentId == null || nIdSpol == null) 
      {
        logger.error("Chyba! Pro subkonsId="+subkonsId+" plati dokladId="+dokladId+",parentId="+parentId+",idSpol="+idSpol);
      }
      else {
        spolecnosti.add(nIdSpol);
        idSpol = nIdSpol.intValue();
        dokladId = nDokladId.intValue();
        parentId = nParentId.intValue();
//System.out.println("subkonsId="+subkonsId+" plati dokladId="+dokladId+",parentId="+parentId+",idSpol="+idSpol);

        setHeader(idSpol,spolCnt);

        int listNr;    
        int rowNr;   
        int colNr;
        int koef = 1;

        ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = "+dokladId);
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
          int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
          double castkaCzk = ((Number)row.getAttribute("NdCastkaczk")).doubleValue();
          
          if(sloupec==3 && rkc) sloupec=4;
          
          int listCol = list==1 ? 3 : 1;
/*          
          if(list == 2 || list == 4 || list == 5) 
            koef = -1;
          else 
            koef = 1;
*/
          listNr = list-1;
          colNr = (sloupcu*listCol+getEtapySize(list))*spolCnt + sloupec+1;
          rowNr = radek+4;
          
          if(castkaLocal!=0.0) setCellValue(listNr,rowNr,colNr,koef*castkaLocal,style);
          if(rkc && castkaCzk!=0.0) setCellValue(listNr,rowNr,colNr+1,koef*castkaCzk,style);
        }
        vo.closeRowSet();

        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = "+parentId);
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
          int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();

          if(sloupec==3 && rkc) sloupec=4;
          
          int listCol = list==1 ? 3 : 1;
/*          
          if(list == 2 || list == 4 || list == 5) 
            koef = -1;
          else 
            koef = 1;
*/          
          listNr = list-1;
          colNr = (sloupcu*listCol+getEtapySize(list))*spolCnt + sloupec+sloupcu;
          rowNr = radek+4;
          
          if(castkaLocal!=0.0) setCellValue(listNr,rowNr,colNr,koef*castkaLocal,style);
        }
        vo.closeRowSet();
        
        outputLog(dokladId, 100);
      }
      spolCnt++;
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();
    
    calcPOP(spolCnt);
    fillEtapy();
    fillVazby(spolCnt);
    //calcCelkem(spolCnt); spolCnt+=1; //prozatimni reseni
    fillCelkem(spolCnt); //finalni reseni

    cleanUp(spolCnt);
  }
  
  private void setHeader(int idSpol, int spolCnt) 
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
    
    int colNr;
    
    int etColNr = colNr = 2+(3*sloupcu+getEtapySize(1))*spolCnt;
    setCellValue(0,2,colNr,nazev+" ("+mena+")",styleBold);
    setCellValue(0,3,colNr,"Poloky",style);
    setCellValue(0,3,colNr+sloupcu,"Opravn poloky",style);
    setCellValue(0,3,colNr+2*sloupcu,"Poloky bez OP",style);
    setCellValue(0,4,colNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(0,4,colNr++,"z toho CZK",style);
    setCellValue(0,4,colNr++,"Celkem v "+mena,style);
    setCellValue(0,4,colNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(0,4,colNr++,"z toho CZK",style);
    setCellValue(0,4,colNr++,"Celkem v "+mena,style);
    setCellValue(0,4,colNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(0,4,colNr++,"z toho CZK",style);
    setCellValue(0,4,colNr++,"Celkem v "+mena,style);
    for(int i=0; i<getEtapySize(1); i++) 
    {
      String etapa = ((Etapa)etapy.get(i)).popis;
      setCellValue(0,3,etColNr+3*sloupcu+i,etapa,style);
    }
    
    for(int listNr=1;listNr<5;listNr++) {
      etColNr = colNr = 2+(sloupcu+getEtapySize(listNr+1))*spolCnt;
      setCellValue(listNr,2,colNr,nazev+" ("+mena+")",styleBold);
      setCellValue(listNr,3,colNr,"Poloky",style);
      setCellValue(listNr,4,colNr++,"Celkem v "+menaSpol,style);
      if(rkc) setCellValue(listNr,4,colNr++,"z toho CZK",style);
      setCellValue(listNr,4,colNr++,"Celkem v "+mena,style);
      for(int i=0; i<getEtapySize(listNr+1); i++) 
      {
        String etapa = ((Etapa)etapy.get(i)).popis;
        setCellValue(listNr,3,etColNr+sloupcu+i,etapa,style);
      }
    }
  }
  
  private void cleanUp(int spolCnt) 
  {
    //vazby+celkem+posun
    //list 1
    int max = vazeb+3*(rkc?2:1)+2+(3*sloupcu+getEtapySize(1))*spolCnt;
    for(int x=max;x<256;x++) {
      for(int y=0;y<=radku[0];y++) {
        clearCell(0,y,x);
      }
    }
    //list 2-5
    for(int listNr=1;listNr<5;listNr++) {
      max = vazeb+(rkc?2:1)+2+(sloupcu+getEtapySize(listNr+1))*spolCnt;
      for(int x=max;x<256;x++) {
        for(int y=0;y<=radku[listNr];y++) {
          clearCell(listNr,y,x);
        }
      }
    }
  }
  
  private void calcPOP(int spolCnt) 
  {
    CellStyle style = null;
    for(int i=0;i<spolCnt;i++) {
      int max=2+(3*sloupcu+getEtapySize(1))*i;
      for(int y=5;y<=radku[0];y++) {
        for(int x=max; x<max+sloupcu; x++) {
          Cell c1 = wb.getSheetAt(0).getRow(y).getCell((short)x);
          Cell c2 = wb.getSheetAt(0).getRow(y).getCell((short)(x+sloupcu));
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
            setCellValue(0,y,x+2*sloupcu,(valC2+valC1),style);
        }
      }
    }
  }

  private void setEtapyPoSpol(Number idSpol) 
  {
    for(int i=0; i<etapy.size(); i++) 
    {
      int etapaId = ((Etapa)etapy.get(i)).id;
      ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
      voDoklad.clearCache();
      voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                              " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                              " AND ID_KTGSUBKONSOLIDACEETAPA = "+etapaId);
      while(voDoklad.hasNext()) 
      {
        Row rowDoklad = voDoklad.next();
        Number nDokladId = (Number) rowDoklad.getAttribute("Id");
        Number nKrok = (Number) rowDoklad.getAttribute("NlKrok");
        Number nIdSpol = (Number) rowDoklad.getAttribute("IdKtgucetnispolecnost");
        if(nDokladId == null) 
        {
          logger.error("Chyba! Pro subkonsId="+subkonsId+" a etapu="+etapaId+" plati dokladId="+dokladId);
        }
        else {
          int poradiSpol = spolecnosti.indexOf(nIdSpol);
        
          dokladId = nDokladId.intValue();
  
          int listNr;    
          int rowNr;   
          int colNr;
          int koef = 1;
  
          ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
          vo.clearCache();
          vo.setWhereClause("ID_DOKLAD = "+dokladId+" AND NL_SLOUPEC = 1");
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
            
            if(getEtapySize(list)>0) {
            
              int listCol = list==1 ? 3 : 1;
/*            
            if(list == 2 || list == 4 || list == 5) 
              koef = -1;
            else 
              koef = 1;
*/  
              listNr = list-1;
              colNr = (sloupcu*listCol+getEtapySize(list))*poradiSpol + i + listCol*sloupcu + 2;
              rowNr = radek+4;
              
//              int colMinus = sloupcu*listCol+i;
//              Cell c1 = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)(colNr-colMinus));
//              double valC1 = 0;
//              if(c1 != null) {
//                boolean d1 = c1.getCellType() == Cell.CELL_TYPE_NUMERIC;
//                if(d1) valC1 = c1.getNumericCellValue();
//              }
//            
//              if((castkaLocal-valC1)!=0.0) setCellValue(listNr,rowNr,colNr,koef*(castkaLocal-valC1),style);
            }
          }
          vo.closeRowSet();
          
          outputLog(dokladId, nKrok.intValue());
        }
//        spolCnt++;
      }
      voDoklad.closeRowSet();
      dm.getTransaction().commit();
    }
  }

  private void fillEtapy() 
  {
    CellStyle style = null;
    for(int i=0; i<etapy.size(); i++) 
    {
//      int spolCnt = 0;

      int etapaId = ((Etapa)etapy.get(i)).id;
      ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
      voDoklad.clearCache();
      voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                              " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                              " AND ID_KTGSUBKONSOLIDACEETAPA = "+etapaId);
      while(voDoklad.hasNext()) 
      {
        Row rowDoklad = voDoklad.next();
        Number nDokladId = (Number) rowDoklad.getAttribute("Id");
        Number nKrok = (Number) rowDoklad.getAttribute("NlKrok");
        Number nIdSpol = (Number) rowDoklad.getAttribute("IdKtgucetnispolecnost");
        if(nDokladId == null) 
        {
          logger.error("Chyba! Pro subkonsId="+subkonsId+" a etapu="+etapaId+" plati dokladId="+dokladId);
        }
        else {
          int poradiSpol = spolecnosti.indexOf(nIdSpol);
        
          dokladId = nDokladId.intValue();
  
          int listNr;    
          int rowNr;   
          int colNr;
          int koef = 1;
  
          ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
          vo.clearCache();
          vo.setWhereClause("ID_DOKLAD = "+dokladId+" AND NL_SLOUPEC = 1");
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
            
            if(getEtapySize(list)>0) {
            
              int listCol = list==1 ? 3 : 1;
/*            
            if(list == 2 || list == 4 || list == 5) 
              koef = -1;
            else 
              koef = 1;
*/  
              listNr = list-1;
              colNr = (sloupcu*listCol+getEtapySize(list))*poradiSpol + i + listCol*sloupcu + 2;
              rowNr = radek+4;
              
//              int colMinus = sloupcu*listCol+i;
//              Cell c1 = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)(colNr-colMinus));
//              double valC1 = 0;
//              if(c1 != null) {
//                boolean d1 = c1.getCellType() == Cell.CELL_TYPE_NUMERIC;
//                if(d1) valC1 = c1.getNumericCellValue();
//              }
//            
//              if((castkaLocal-valC1)!=0.0) setCellValue(listNr,rowNr,colNr,koef*(castkaLocal-valC1),style);
              if((castkaLocal)!=0.0) setCellValue(listNr,rowNr,colNr,koef*castkaLocal,style);
            }
          }
          vo.closeRowSet();
          
          outputLog(dokladId, nKrok.intValue());
        }
//        spolCnt++;
      }
      voDoklad.closeRowSet();
      dm.getTransaction().commit();
    }
  }

  private void fillVazby(int spolCnt) 
  {
    CellStyle style = null;
    int listNr;
    int colNr;
    //list 1
    colNr = 2+(3*sloupcu+getEtapySize(1))*spolCnt;
    setCellValue(0,2,colNr,"Vzjemn vazby",styleBold);
    setCellValue(0,4,colNr,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(0,4,colNr+1,"z toho CZK",style);
    //list 2-5
    for(listNr=1;listNr<5;listNr++) {
      colNr = 2+(sloupcu+getEtapySize(listNr+1))*spolCnt;
      setCellValue(listNr,2,colNr,"Vzjemn vazby",styleBold);
      setCellValue(listNr,4,colNr,"Celkem v "+menaSpol,style);
      if(rkc) setCellValue(listNr,4,colNr+1,"z toho CZK",style);
    }

    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
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

        ViewObject vo = dm.findViewObject("VwKpDokladvvexcelView1");
        vo.clearCache();
        vo.setWhereClause("IDDOKLAD = "+dokladId);
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
          
          int listCol = list==1 ? 3 : 1;
/*
          if(list == 2 || list == 4 || list == 5) 
            koef = -1;
          else 
            koef = 1;
*/
          listNr = list-1;
          colNr = (sloupcu*listCol+getEtapySize(list))*spolCnt + 2;
          rowNr = radek+4;
          
          if(castkaLocal!=0.0) setCellValue(listNr,rowNr,colNr,koef*castkaLocal,style);
        }
        vo.closeRowSet();
      }
      spolCnt++;
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();
  }

  private void fillCelkem(int spolCnt) 
  {
    CellStyle style = null;
    int listNr;
    int colNr;
    //list 1 (+vazby)
    colNr = vazeb+2+(3*sloupcu+getEtapySize(1))*spolCnt;
    setCellValue(0,2,colNr,"CELKEM",styleBold);
    setCellValue(0,3,colNr,"Poloky",style);
    setCellValue(0,3,colNr+(rkc?2:1),"Opravn poloky",style);
    setCellValue(0,3,colNr+(rkc?4:2),"Poloky bez OP",style);
    setCellValue(0,4,colNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(0,4,colNr++,"z toho CZK",style);
    setCellValue(0,4,colNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(0,4,colNr++,"z toho CZK",style);
    setCellValue(0,4,colNr++,"Celkem v "+menaSpol,style);
    if(rkc) setCellValue(0,4,colNr++,"z toho CZK",style);
    //list 2-5
    for(listNr=1;listNr<5;listNr++) {
      colNr = vazeb+2+(sloupcu+getEtapySize(listNr+1))*spolCnt;
      setCellValue(listNr,2,colNr,"CELKEM",styleBold);
      setCellValue(listNr,3,colNr,"Poloky",style);
      setCellValue(listNr,4,colNr,"Celkem v "+menaSpol,style);
      if(rkc) setCellValue(listNr,4,colNr+1,"z toho CZK",style);
    }

    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
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

        ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = "+dokladId);
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
          int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
          double castkaCzk = ((Number)row.getAttribute("NdCastkaczk")).doubleValue();
          
          int listCol = list==1 ? 3 : 1;
          if(sloupec == 3 && !rkc) sloupec = 2;
/*          
          if(list == 2 || list == 4 || list == 5) 
            koef = -1;
          else 
            koef = 1;
*/
          listNr = list-1;
          colNr = (sloupcu*listCol+getEtapySize(list))*spolCnt + sloupec+1 + vazeb; //+vazby
          rowNr = radek+4;
          
          if(castkaLocal!=0.0) setCellValue(listNr,rowNr,colNr,koef*castkaLocal,style);
          if(rkc && castkaCzk!=0.0) setCellValue(listNr,rowNr,colNr+1,koef*castkaCzk,style);
        }
        vo.closeRowSet();
        
        outputLog(dokladId, 200);
      }
      spolCnt++;
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();

    int max=vazeb+2+(3*sloupcu+getEtapySize(1))*(spolCnt-1); //+vazby
    for(int y=5;y<=radku[0];y++) {
      for(int x=max; x<max+(rkc?2:1); x++) {
        Cell c1 = wb.getSheetAt(0).getRow(y).getCell((short)x);
        Cell c2 = wb.getSheetAt(0).getRow(y).getCell((short)(x+(rkc?2:1)));
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
          setCellValue(0,y,x+2*(rkc?2:1),(valC2+valC1),style);
      }
    }
  }

  private void outputLog(int idDoklad, int krok) {
    int listNr=5;
    int colNr=0;

    if(logRowNr == 0) {
      CellStyle lightBlue = wb.createCellStyle();
      lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
  
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

    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputDoklad();
    logger.info("Dogenerovany prvni listy");
    
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
      ESExportSubkonsDokladOld ed = new ESExportSubkonsDokladOld(dm,
                                                           10005,
                                                           new java.sql.Date(104,11,31));
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
/*
class Etapa 
{
  public Etapa(int id, String popis)
  {
    this.id = id;
    this.popis = popis;
  }
  protected int id;
  protected String popis;
}
*/