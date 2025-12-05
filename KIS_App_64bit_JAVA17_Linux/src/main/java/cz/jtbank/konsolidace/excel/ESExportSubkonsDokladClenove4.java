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

public class ESExportSubkonsDokladClenove4 extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsDokladClenove4.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private int subkonsId;
  private java.sql.Date datum;
  private Number idMainDoklad;
  private boolean mis;
  private java.sql.Date dtVytvoreno;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredpona;
  private String kurzListek;
  
  private boolean skupina = false;

  private CellStyle styleAlert;
  
  private int[] colNr = new int[] {2,2,2,2,2};
  
  private int logRowNr = 0;
  
  private static final int[] radku = new int[]
                                     {178,127,41,43,162};
                                     
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private CellStyle lightBlue;
  private CellStyle[] colLocal, colMena, colVV, colKU, colNone;

  public ESExportSubkonsDokladClenove4(ApplicationModule dokladyModule,
                               int subkonsId,
                               java.sql.Date datum,
                               boolean mis)
  {
    dm = dokladyModule;
    this.subkonsId = subkonsId;
    this.datum = datum;
    this.mis = mis;
    
    if(subkonsId<9999) skupina = true;
    
    init();
  }

  private void init() {
    if(!skupina) {
      ViewObject vo = dm.findViewObject("VwKpSubkonsolidaceView1");
      vo.clearCache();
      vo.setWhereClause("ID_KTGUCETNISPOLECNOST = "+subkonsId);
      if(vo.hasNext()) 
      {
        Row row = vo.next();
        nazevSpol = (String) row.getAttribute("SNazev");
        kurzListek = (String) row.getAttribute("STypkurslistek");
        menaSpol = (String) row.getAttribute("SMena");
        souborPredpona = (String) row.getAttribute("SSouborpredpona");
      }
      vo.closeRowSet();
  
      ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
      voDoklad.clearCache();
      voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                              " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                              " AND NL_KROK = 200");
      if(voDoklad.hasNext()) 
      {
        Row rowDoklad = voDoklad.next();
        idMainDoklad = (Number) rowDoklad.getAttribute("Id");
        dtVytvoreno = ((oracle.jbo.domain.Date) rowDoklad.getAttribute("DtVytvoreno")).dateValue();
      }
      voDoklad.closeRowSet();
    }
    else 
    {
      ViewObject vo = dm.findViewObject("KpKtgUcetniskupinaView1");
      vo.clearCache();
      vo.setWhereClause("ID = "+subkonsId);
      if(vo.hasNext()) 
      {
        Row row = vo.next();
        nazevSpol = (String) row.getAttribute("SPopis");
        menaSpol = "-";
        souborPredpona = (String) row.getAttribute("SKod");
      }
      vo.closeRowSet();
    }

    dm.getTransaction().commit();
    logger.info("ExportSubkonsDokladClenove4:nazevSpol="+nazevSpol+",datum="+datum+",menaSpol="+menaSpol);  
    
    String special = mis?"Mis":"";
    setFileName ( "SubkonsClenove"+special+subkonsId+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredpona+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaSubkonsolidaceClen2.xlsx" );
  }
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    
    setCellValue(0,0,0,nazevSpol,style);    
    setCellValue(1,0,0,nazevSpol,style);    
    setCellValue(2,0,0,nazevSpol,style);    
    setCellValue(3,0,0,nazevSpol,style);    
    setCellValue(4,0,0,nazevSpol,style);    
    
    setCellValue(0,0,3,"AKTIVA CELKEM v "+menaSpol,style);
    setCellValue(1,0,3,"PASIVA CELKEM v "+menaSpol,style);
    setCellValue(2,0,3,"PODR. AKTIVA CELKEM v "+menaSpol,style);
    setCellValue(3,0,3,"PODR. PASIVA CELKEM v "+menaSpol,style);
    setCellValue(4,0,3,"VSLEDOVKA CELKEM v "+menaSpol,style);

    String txtDatum = sdf.format(datum);
    setCellValue(0,0,4,txtDatum,style);
    setCellValue(1,0,4,txtDatum,style);
    setCellValue(2,0,4,txtDatum,style);
    setCellValue(3,0,4,txtDatum,style);
    setCellValue(4,0,4,txtDatum,style);
  }


  private String nazev;

  private void cleanUp() 
  {
    for(int listNr=0;listNr<5;listNr++) {
      for(int x=colNr[listNr];x<1001;x++) {
        for(int y=0;y<=radku[listNr];y++) {
          clearCell(listNr,x,y);
        }
      }
    }
  }

  public String getNazevSpol() 
  {
    return nazevSpol;
  }

  public String getIdSubkons() 
  {
    return String.valueOf(subkonsId);
  }
//-----------------------------------------------------------------------------------------------------------------------------------  
  
  private List tree = new ArrayList();
  private Set dokladyVazby = new LinkedHashSet();// HashSet();
  private Map spolMena = new HashMap();
  private Map spolOrig = new HashMap();
  private Map spolUpravy = new HashMap();
  private Map subUpravy = new HashMap();
  private Number current = null;
//  private StringBuffer buf = new StringBuffer();

  private void colorHeader(CellStyle[] style) 
  {
    if(current!=null && subkonsId==current.intValue()) style = colNone;
    
    for(int listNr=0;listNr<5;listNr++) {
      int x=colNr[listNr];
      for(int y=0;y<=4;y++) {
        setCellValue(listNr,x,y,(String)null,style[y]);
      }
    }
  }

  private void outputHeader(String mena) 
  {
    CellStyle style = null;
    nazev = null;
  
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+current);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazev = (String) row.getAttribute("SNazev");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    for(int listNr=0;listNr<5;listNr++) {
/*      if(equity) setCellValue(listNr,colNr[listNr],1,"EQUITY",null);//styleBold);
      else
        setCellValue(listNr,1,colNr[listNr],(String)null,styleBold);*/
      
      setCellValue(listNr,colNr[listNr],2,nazev,null);//styleBold);
      
//      if(!equity) 
        setCellValue(listNr,colNr[listNr],3,"Poloky bez OP",null);//style);
        
      setCellValue(listNr,colNr[listNr],4,"Celkem v "+mena,null);//style);
    }
  }

  private String getMenaDoklad(Number idDoklad) 
  {
    String menaDoklad = menaSpol;
    ViewObject voDoklad = dm.findViewObject("KpDatDokladView2");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID = "+idDoklad);
    while(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      menaDoklad = (String) rowDoklad.getAttribute("SMena");
    }
    voDoklad.closeRowSet();
    return menaDoklad;
  }

  private void outputDoklad(Set setDokladu, boolean parent) 
  {
    if(setDokladu==null || setDokladu.isEmpty()) return;

    Iterator iter = setDokladu.iterator();
    while(iter.hasNext()) {
      Number idDoklad = (Number) iter.next();
    
      String menaDoklad = menaSpol;
      if(parent) {
        menaDoklad = getMenaDoklad(idDoklad);
        if(menaSpol.equals(menaDoklad)) return;
      }
  
      colorHeader(parent?colMena:colLocal);
      outputHeader(menaDoklad);
  
      int rowNr;   
      int koef = 1;
  
      ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaNewView1");
      for(int list=1; list<=5; list++) {
        int listNr = list-1;
  
        vo.setWhereClauseParams(new Object[] {
                                  kurzListek,
                                  menaDoklad,
                                  kurzListek,
                                  menaDoklad
                                });
        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND NL_PORADILIST = "+list
        /*           
                    +" and exists (select null"+
                                " from db_jt.kp_rel_subkonsolidaceClen sub"+
                                " where sub.ID_KTGSUBKONSOLIDACE = "+idSub+
                                  " and KpDatDokladsubkonlogdoklady.ID_KTGSUBKONSOLIDACE = sub.ID_KTGSUBKONSOLIDACE"+
                                  " and KpDatDokladsubkonlogdoklady.ID_KTGUCETNISPOLECNOSTCLEN = sub.ID_KTGUCETNISPOLECNOST"+
                                  " and KpDatDokladsubkonlogdoklady.DT_DATUM between sub.DT_CLENSTVIOD and sub.DT_CLENSTVIDO)"
                                  */
        );
        
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
  
          rowNr = radek+4;
          
          if(castkaLocal!=0.0) {
            double puvCastkaLocal = 0.0;
            if(sloupec==3) 
              puvCastkaLocal = wb.getSheetAt(listNr).getRow(colNr[listNr]).getCell((short)rowNr).getNumericCellValue();
  
            setCellValue(listNr,colNr[listNr],rowNr,puvCastkaLocal+koef*castkaLocal,null);
          }
        }
        vo.closeRowSet();
  
        colNr[listNr]++;
      }
    }
  }
  
  private void outputUpravy(Set doklady) 
  {
    colorHeader(colKU);
    outputHeader(menaSpol);

    for(int list=1; list<=5; list++) {
      int listNr = list-1;
      setCellValue(listNr,colNr[listNr],3,"Kons. pravy",null);//styleBold);
      setCellValue(listNr,colNr[listNr],4,"Celkem v "+menaSpol,null);
    }

    if(doklady!=null && !doklady.isEmpty()) {
      int koef = 1;
      
      Iterator iter = doklady.iterator();
      while(iter.hasNext()) 
      {
        Number idDoklad = (Number) iter.next();
        for(int list=1; list<=5; list++) {
          int listNr = list-1;
    
          ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaNewView1");
          vo.setWhereClauseParams(new Object[] {
                                    kurzListek,
                                    menaSpol,
                                    kurzListek,
                                    menaSpol
                                  });
          vo.clearCache();
          vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND NL_SLOUPEC = 1 AND NL_PORADILIST = "+list+" AND ND_CASTKALOCAL <> 0");
          
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
            int radek = ((Number)row.getAttribute("NlRadek")).intValue();
            double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
    
            int rowNr = radek+4;
            
            double puvCastkaLocal = 0.0;
            puvCastkaLocal = wb.getSheetAt(listNr).getRow(colNr[listNr]).getCell((short)rowNr).getNumericCellValue();
    
            setCellValue(listNr,colNr[listNr],rowNr,puvCastkaLocal+koef*castkaLocal,null);
          }
          vo.closeRowSet();
        }
      }
    }

    for(int list=1; list<=5; list++) {
      int listNr = list-1;
      colNr[listNr]++;
    }
  }

  private void outputVazbyClen() 
  {
    colorHeader(colVV);
    outputHeader(menaSpol);
  
    CellStyle style = null;

    int rowNr;   

    Set usedTo = new HashSet();
    
    ViewObject vo = dm.findViewObject("VwDatDokladsubkonvazbydenikView1");
    for(int list=1;list<=5;list++) {
      int listNr = list-1;
      Number lNum = new Number(list);

      int koef = (list==1 || list==3) ? -1 : 1;
    
      setCellValue(listNr,colNr[listNr],3,"Vzjemn vazby",null);//styleBold);
      setCellValue(listNr,colNr[listNr],4,"Celkem v "+menaSpol,style);
  
      usedTo.clear();
    
      Iterator vvIter = dokladyVazby.iterator();
      while(vvIter.hasNext()) 
      {
        Number idDoklad = (Number) vvIter.next();

        vo.clearCache();
        vo.setWhereClauseParams(new Object[] {
                                  kurzListek,
                                  menaSpol,
                                  datum,
                                  kurzListek,
                                  menaSpol,
                                  datum,
                                  current,
                                  lNum,
                                  idDoklad
                                });
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastka")).doubleValue();
          Number spolTo = (Number)row.getAttribute("IdKtgucetnispolecnostto");
          
          String key = spolTo+"/"+radek;
          if(!usedTo.contains(key)) {
            //System.out.println("VV:"+current+" - > "+spolTo+" r="+radek+" ("+idDoklad+")");
          
            usedTo.add(key);
          
            rowNr = radek+4;
            
            double puvCastkaLocal = 0.0;
            puvCastkaLocal = wb.getSheetAt(listNr).getRow(colNr[listNr]).getCell((short)rowNr).getNumericCellValue();
    
            if(castkaLocal!=0.0) setCellValue(listNr,colNr[listNr],rowNr,puvCastkaLocal+koef*castkaLocal,style);
          }
          else System.out.println("VV duplicita:"+current+" - > "+spolTo+" l="+lNum+" r="+radek+" ("+idDoklad+")");
        }
        vo.closeRowSet();
      }

      colNr[listNr]++;
    }
    dm.getTransaction().commit();
  }

  private void outputVazbyMain() 
  {
    colorHeader(colVV);
    outputHeader(menaSpol);
  
    CellStyle style = null;

    int rowNr;   
    int koef = 1;

    ViewObject vo = dm.findViewObject("VwKpDokladvvexcelView1");
    for(int list=1;list<=5;list++) {
      int listNr = list-1;
    
      setCellValue(listNr,colNr[listNr],3,"Vzjemn vazby",null);//styleBold);
      setCellValue(listNr,colNr[listNr],4,"Celkem v "+menaSpol,style);

      vo.clearCache();
      vo.setWhereClause("IDDOKLAD = "+idMainDoklad+
                        " AND NL_PORADILIST = "+list);
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        int radek = ((Number)row.getAttribute("NlRadek")).intValue();
        double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();

        rowNr = radek+4;
        
        if(castkaLocal!=0.0) setCellValue(listNr,colNr[listNr],rowNr,koef*castkaLocal,style);
      }
      vo.closeRowSet();
      colNr[listNr]++;
    }
    dm.getTransaction().commit();
  }

  private void outputZaver() 
  {
    current = new Number(subkonsId);
    outputVazbyMain();
    Set set = new HashSet();
    set.add(idMainDoklad);
    outputDoklad(set,false);
    cleanUp();
  }
  
  private void buildTree(Number idSub, List list, java.sql.Date dtSuper) throws KisException
  {
    ViewObject vo = dm.findViewObject("KpDatDokladsubkonlogdokladyView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGSUBKONSOLIDACE = "+idSub+
                    " and KpDatDokladsubkonlogdoklady.DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                    " and not (nl_krok between 101 and 199 and id_ktgSubkonsolidaceEtapa is null)"
                    );
    vo.setOrderByClause("s_nazev,nl_krokDokladClen");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      LogRecord lr = new LogRecord();
      lr.idSub = idSub;
      lr.idDokladClen = (Number)row.getAttribute("IdDokladclen");
      lr.idUcSpolClen = (Number)row.getAttribute("IdKtgucetnispolecnostclen");
      lr.idSubClen = (Number)row.getAttribute("IdKtgsubkonsolidaceclen");
      lr.krok = (Number)row.getAttribute("NlKrokdokladclen");
      lr.idParent = (Number)row.getAttribute("IdParent");
      lr.dtVytvoreno = ((oracle.jbo.domain.Date) row.getAttribute("DtVytvoreno")).dateValue();
      
      list.add(lr);
    }
    vo.closeRowSet();

    Iterator iter = list.iterator();
    while(iter.hasNext()) 
    {
      LogRecord lr = (LogRecord) iter.next();
      
      if(lr.idUcSpolClen != null && lr.krok!=null && lr.krok.intValue()==100) 
      {
        //spolMena.put(lr.idUcSpolClen, lr.idDokladClen);

        Set doklady = null;
        if(spolMena.containsKey(lr.idUcSpolClen)) 
        {
          doklady = (Set) spolMena.get(lr.idUcSpolClen);
        }
        else 
        {
          doklady = new HashSet();
          spolMena.put(lr.idUcSpolClen, doklady);
        }
        doklady.add(lr.idDokladClen);
      }
      else if(lr.idUcSpolClen != null && lr.krok!=null && lr.krok.intValue()<100) 
      {
        //spolOrig.put(lr.idUcSpolClen, lr.idDokladClen);

        Set doklady = null;
        if(spolOrig.containsKey(lr.idUcSpolClen)) 
        {
          doklady = (Set) spolOrig.get(lr.idUcSpolClen);
        }
        else 
        {
          doklady = new HashSet();
          spolOrig.put(lr.idUcSpolClen, doklady);
        }
        doklady.add(lr.idDokladClen);
      }
      else if(lr.idUcSpolClen != null && lr.krok!=null && lr.krok.intValue()>100 && lr.krok.intValue()<200) 
      {
        Set doklady = null;
        if(spolUpravy.containsKey(lr.idUcSpolClen)) 
        {
          doklady = (Set) spolUpravy.get(lr.idUcSpolClen);
        }
        else 
        {
          doklady = new HashSet();
          spolUpravy.put(lr.idUcSpolClen, doklady);
        }
        doklady.add(lr.idDokladClen);
      }
      else if(lr.idSubClen != null && lr.krok!=null && lr.krok.intValue()==100) 
      {
        if(lr.dtVytvoreno.after(dtSuper)) 
        {
          throw new KisException("Doklad lensk sub-konsolidace "+lr.idSubClen+" je erstvj ne doklad nadzen!!!",
                                 new KisException(Constants.ERR_MESSAGE_ONLY));
        }
      
        dokladyVazby.add(lr.idParent);
        if(!lr.idSubClen.equals(idSub)) {
          lr.clenove = new ArrayList();
          buildTree(lr.idSubClen, lr.clenove, lr.dtVytvoreno);
        }
      }
      else if(lr.idSubClen != null && lr.krok!=null && lr.krok.intValue()>100 && lr.krok.intValue()<200) 
      {
        Set doklady = null;
        if(subUpravy.containsKey(lr.idSubClen)) 
        {
          doklady = (Set) subUpravy.get(lr.idSubClen);
        }
        else 
        {
          doklady = new HashSet();
          subUpravy.put(lr.idSubClen, doklady);
        }
        doklady.add(lr.idDokladClen);
      }
    }
  }

  protected boolean outputData () throws KisException
  {
    colLocal = new CellStyle[5];
    for(int i=0;i<5;i++) colLocal[i] = wb.getSheetAt(0).getRow(2).getCell((short)i).getCellStyle();

    colMena = new CellStyle[5];
    for(int i=0;i<5;i++) colMena[i] = wb.getSheetAt(0).getRow(3).getCell((short)i).getCellStyle();

    colVV = new CellStyle[5];
    for(int i=0;i<5;i++) colVV[i] = wb.getSheetAt(0).getRow(4).getCell((short)i).getCellStyle();

    colKU = new CellStyle[5];
    for(int i=0;i<5;i++) colKU[i] = wb.getSheetAt(0).getRow(5).getCell((short)i).getCellStyle();

    colNone = new CellStyle[5];
    for(int i=0;i<5;i++) colNone[i] = wb.getSheetAt(0).getRow(6).getCell((short)i).getCellStyle();

    styleAlert = wb.createCellStyle();
    styleAlert.setFillForegroundColor(IndexedColors.RED.getIndex());
    styleAlert.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleAlert.setFont(wb.getFontAt(colLocal[0].getFontIndex()));

    outputHeaders(); 
    
    dokladyVazby.add(idMainDoklad);

    buildTree(new Number(subkonsId), tree, dtVytvoreno);
    
/*
    buf.append(idMainDoklad);
    Iterator vvIter = dokladyVazby.iterator();
    while(vvIter.hasNext()) 
    {
      buf.append(",");
      buf.append(vvIter.next());
    }
    System.out.println(buf);
*/
    System.out.println(dokladyVazby);
    
    if(spolMena != null && spolMena.keySet()!=null) {
      Iterator iter = spolMena.keySet().iterator();
      while(iter.hasNext()) 
      {
        current = (Number) iter.next();
        Set idDoklad = (Set) spolMena.get(current);
        Set idParent = (Set) spolOrig.get(current);
        Set kroky = (Set) spolUpravy.get(current);
        
        outputDoklad(idDoklad, false);

        outputDoklad(idParent, true);
        
        outputVazbyClen();
        
        outputUpravy(kroky);
      }
    }

    if(subUpravy != null && subUpravy.keySet()!=null) {
      Iterator iter = subUpravy.keySet().iterator();
      while(iter.hasNext()) 
      {
        current = (Number) iter.next();
        Set kroky = (Set) subUpravy.get(current);
        
        outputUpravy(kroky);
        
        for(int list=1; list<=5; list++) {
          int listNr = list-1;
          setCellValue(listNr,colNr[listNr]-1,3,"Kons. pravy",styleAlert);
          //setCellValue(listNr,colNr[listNr]-1,4,"Celkem v "+menaSpol,null);
        }
      }
    }
    
    outputZaver();
    
    return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportSubkonsDokladClenove4 ed = new ESExportSubkonsDokladClenove4(dm,
                                                           10023,
                                                           new java.sql.Date(106,11,31),
                                                           false);
      try {
      ed.excelOutput();
      } catch (Exception e) 
      {
        e.printStackTrace();
      }

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


class LogRecord 
{
  Number idSub;
  Number idDokladClen;
  Number idUcSpolClen;
  Number idSubClen;
  Number krok;
  Number idParent;
  java.sql.Date dtVytvoreno;
  
  List clenove;
}
