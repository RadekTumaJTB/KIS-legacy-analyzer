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

public class ESExportBudgetStd extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportBudgetStd.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;

  private int userId;
  private java.sql.Date datum;
  private int exportType;
  private boolean detail;
  private boolean manual;
  private boolean rok2005;
  
  public static final int EXPORT_TYPE_OO_OJ = 1;
  public static final int EXPORT_TYPE_VIEW_SPOL = 2;
  public static final int EXPORT_TYPE_GESTOR = 3;
  public static final int EXPORT_TYPE_KONTROLING = 4;
  public static final int EXPORT_TYPE_VIEW_SPOL_ALL = 12;
  public static final int EXPORT_TYPE_GESTOR_ALL = 13;
  
  private static final int COLS_BY_BUD = 9;

  private String curNazev;
  private Map curTranNazev = new HashMap();
  private String idTran;
  private int idBud;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private CellStyle lightBlue, styleErr, styleWarn, styleBold, styleBoldRed;
  private int detRowNr = 0, detListNr;
  private ViewObject voDetail;

  public ESExportBudgetStd(ApplicationModule dokladyModule,
                           java.sql.Date datum,
                           int userId,
                           int exportType,
                           boolean detail,
                           boolean manual)
  {
    dm = dokladyModule;
    this.datum = datum;
    this.userId = userId;
    this.exportType = exportType;
    this.detail = detail;
    this.manual = manual;
    rok2005 = datum.toString().indexOf("2005")>-1;
    init();
  }

  private void init() {
    logger.info("ExportBudgetStd:userId="+userId+",exportType="+exportType);  

    setFileName ( (manual?"":"x")+"Budget_Typ"+exportType+"_"+datum+".xlsx" );
    setFileRelativeName( Constants.DIR_BUDGET_STD+"\\"+userId+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private String GROUP_NULL = "ID_TYPTRANSAKCEGROUP IS NULL";
  private String GROUP_NOT_NULL = "ID_TYPTRANSAKCEGROUP = ";

  private void outputListyStd()
  {
    CellStyle style = null;
    int listNr = 0;
    int rowNr=0;
    int colNr = 0;

    if(detail) 
    {
      wb.cloneSheet(0);
      wb.setSheetName(0,"Detail");
      listNr++;
    }

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*60));

    Map tranGroup = new TreeMap();
    Map tranProZastupce = new HashMap();
    ViewObject voGroup = dm.findViewObject("KpCisTyptransakcegroupView1");
    voGroup.clearCache();
    voGroup.setWhereClause("C_DEVELOPER = '0'");
    while ( voGroup.hasNext() ) {
      Row row = voGroup.next();

      Number id = (Number) row.getAttribute("Id");
      String kod = (String) row.getAttribute("SKod");
      String popis = (String) row.getAttribute("SPopis");
      tranGroup.put(GROUP_NOT_NULL+id, kod+" - "+popis);
    }
    voGroup.closeRowSet();
    tranGroup.put(GROUP_NULL, "x");

    List tran = new ArrayList();
    ViewObject vo = dm.findViewObject("KpCisTyptransakceView1");
    String whereTT = "C_DEVELOPER = '0' AND C_SPECIAL='0' AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
//    String whereOJ = "";
java.util.ArrayList whereList = new java.util.ArrayList();
whereList.add("TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    if(exportType == EXPORT_TYPE_OO_OJ) 
    {
      String wZastup = "(SELECT ID_KOHO FROM DB_JT.KP_DAT_BUDGETZASTUP WHERE ID_KDO = "+userId+" AND (DT_PLATNOSTOD IS NULL OR DT_PLATNOSTOD<=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')) AND (DT_PLATNOSTDO IS NULL OR DT_PLATNOSTDO>=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')) AND (VwDatBudget.ID = ID_BUDGET OR ID_BUDGET IS NULL))";
/*
      whereOJ = "(ID_ODBOROO = "+userId+
             " OR ID_UCSPOLOO = "+userId+
             " OR ID_UCSPOLTOP = "+userId+
             " OR ID_ODBOROO IN "+wZastup+
             " OR ID_UCSPOLOO IN "+wZastup+
             " OR ID_UCSPOLTOP IN "+wZastup+")";
*/
whereList.clear();
whereList.add("(ID_ODBOROO = "+userId+" OR ID_UCSPOLOO = "+userId+" OR ID_UCSPOLTOP = "+userId+") AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
whereList.add("(ID_ODBOROO IN "+wZastup+" OR ID_UCSPOLOO IN "+wZastup+" OR ID_UCSPOLTOP IN "+wZastup+") AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    }
    else if(exportType == EXPORT_TYPE_VIEW_SPOL) 
    {
/*    
      whereOJ = "EXISTS (SELECT NULL "+
                        "FROM DB_JT.KP_REL_APPUSER_SPOLECNOSTPRAVO r, "+
                             "DB_JT.KTG_APPUSER u "+
                        "WHERE VwDatBudget.ID_KTGUCETNISPOLECNOST = r.ID_KTGUCETNISPOLECNOST "+
                          "AND r.S_USERID = u.S_USERID "+
                          "AND u.ID = "+userId+")";
*/
whereList.clear();
whereList.add("EXISTS (SELECT NULL "+
                        "FROM DB_JT.KP_REL_APPUSER_SPOLECNOSTPRAVO r, "+
                             "DB_JT.KTG_APPUSER u "+
                        "WHERE VwDatBudget.ID_KTGUCETNISPOLECNOST = r.ID_KTGUCETNISPOLECNOST "+
                          "AND r.S_USERID = u.S_USERID "+
                          "AND u.ID = "+userId+") AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    }
    else if(exportType == EXPORT_TYPE_VIEW_SPOL_ALL) 
    {
whereList.clear();
whereList.add("TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    }
    
    Map refGroup = new TreeMap();
    vo.clearCache();
    vo.setWhereClause(whereTT);
    vo.setOrderByClause("ID_TYPTRANSAKCEGROUP,S_KOD");
    rowNr = 1;
    while ( vo.hasNext() ) {
      Row row = vo.next();
//      rowNr++;
      colNr = 0;
      
      String kod = (String) row.getAttribute("SKod");
      String popis = (String) row.getAttribute("SPopis");
      String idTran = ((Number) row.getAttribute("Id")).toString();
      Number idGroup = (Number) row.getAttribute("IdTyptransakcegroup");
      Boolean proZastupce = new Boolean( "1".equals(row.getAttribute("CProzastupce")) );
      tran.add(idTran);
      tranProZastupce.put(idTran,proZastupce);
      
      String nazev = kod + " / " + popis;
      curTranNazev.put(idTran, nazev);
      
      String kodGroup = null;
      if(idGroup == null) 
      {
        kodGroup = (String)tranGroup.get(GROUP_NULL);
      }
      else 
      {
        kodGroup = (String)tranGroup.get(GROUP_NOT_NULL+idGroup);
      }
      List l = null;
      if(refGroup.containsKey(kodGroup)) 
        l = (List) refGroup.get(kodGroup);
      else {
        l = new ArrayList();
        refGroup.put(kodGroup, l);
      }
      l.add(idTran);
      
//      setCellValue( listNr, rowNr, colNr, nazev , lightBlue );                        
    }
    vo.closeRowSet();
    
    Iterator iGroup = refGroup.keySet().iterator();
    while(iGroup.hasNext()) 
    {
      String key = (String) iGroup.next();
      List l = (List) refGroup.get(key);
      Iterator iTran = l.iterator();
      while(iTran.hasNext()) 
      {
        rowNr++;
        String idTran = (String) iTran.next();
        String nazevTran = (String) curTranNazev.get(idTran);
        setCellValue( listNr, rowNr, colNr, nazevTran , lightBlue );                        
      }
      rowNr++;
//      String nazevGroup = (String) tranGroup.get(key);
//      setCellValue( listNr, rowNr, colNr, nazevGroup , styleBold );                        
      setCellValue( listNr, rowNr, colNr, key , styleBold );                        
    }
    rowNr++;
    setCellValue( listNr, rowNr, colNr, "CELKEM" , styleBold );                        
    
    int celkemSloupcu = 0;
    ViewObject voBud = dm.findViewObject("VwDatBudgetView1");
    Iterator iterCnt = whereList.iterator();
    while(iterCnt.hasNext()) 
    {
      String whereOJ = (String)iterCnt.next();
      voBud.clearCache();
      voBud.setWhereClause(whereOJ);
      celkemSloupcu += voBud.getRowCount();
    }
    voBud.closeRowSet();
    int nrClones = (celkemSloupcu * COLS_BY_BUD) / 254;
    wb.setSheetName(listNr,"List 1");
    detListNr = 0;//1+nrClones;
    for(int i=0; i<nrClones; i++) 
    {
      wb.cloneSheet(listNr);
      wb.setSheetName(listNr+i+1,"List "+(i+2));
    }
    rowNr = 0;
    colNr = 1;
    ViewObject voBunka = dm.findViewObject("VwDatBudgetpolozkaView1");
    voBunka.clearCache();
    ViewObject voBunkaData1 = dm.findViewObject("VwDatBudgetpolozkadata1PARView1");
    voBunkaData1.clearCache();
    ViewObject voBunkaData2 = dm.findViewObject("VwDatBudgetpolozkadata2PARView1");
    voBunkaData2.clearCache();
    
boolean first = true;
    iterCnt = whereList.iterator();
    while(iterCnt.hasNext()) 
    {
      String whereOJ = (String)iterCnt.next();
      voBud.clearCache();
      voBud.setWhereClause(whereOJ);
      while(voBud.hasNext()) 
      {
        Row rowBud = voBud.next();
        //HLAVICKY
        idBud = ((Number)rowBud.getAttribute("Id")).intValue();
        oracle.jbo.domain.Date dt = (oracle.jbo.domain.Date)rowBud.getAttribute("DtPlatnostod");
        String dtOd = dt!=null ? sdf.format(dt.dateValue()) : "";
        dt = (oracle.jbo.domain.Date)rowBud.getAttribute("DtPlatnostdo");
        String dtDo = dt!=null ? sdf.format(dt.dateValue()) : "";
        String kod = (String)rowBud.getAttribute("SKod");
        String nazev = (String)rowBud.getAttribute("SNazev");
        String menaBud = (String)rowBud.getAttribute("SMena");
        curNazev = kod + " - " + nazev + " (" + dtOd + " - " + dtDo + ")";
        setCellValue( listNr, rowNr, colNr, curNazev , first?styleBold:styleBoldRed );
  
        int startColNr = colNr;
  
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
        setCellValue( listNr, rowNr+1, colNr, "Plnovan ve" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
        setCellValue( listNr, rowNr+1, colNr, "Mna" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
        setCellValue( listNr, rowNr+1, colNr, "erpan ve" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
        setCellValue( listNr, rowNr+1, colNr, "Mna" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*7));
        setCellValue( listNr, rowNr+1, colNr, "% erpn" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
        setCellValue( listNr, rowNr+1, colNr, "Propoet erpan ve + SL" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
        setCellValue( listNr, rowNr+1, colNr, "Mna" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*7));
        setCellValue( listNr, rowNr+1, colNr, "% erpn + SL" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*40));
        setCellValue( listNr, rowNr+1, colNr, "Poznmka" , lightBlue ); colNr++;
  
        int dataRowNr = rowNr+2;
        String mena = "";
        Number hlpNum = null;
        //DATA
        double sumaPlan = 0.0,
               sumaZd = 0.0,
               sumaSch = 0.0;
  
        iGroup = refGroup.keySet().iterator();
        while(iGroup.hasNext()) 
        {
          String key = (String) iGroup.next();
          List l = (List) refGroup.get(key);
  
          double skupPlan = 0.0,
                 skupZd = 0.0,
                 skupSch = 0.0;
  
          Iterator iter = l.iterator();
          while ( iter.hasNext() ) {
            int dataColNr = startColNr;
            idTran = (String) iter.next();
            style = null;
            
            double castNr = 0, castZdNr = 0, castSchNr = 0;
            
            String whereBunka = "ID_BUDGET = "+idBud+
                               " AND ID_CISTYPTRAN = "+idTran;
            voBunka.setWhereClause(whereBunka);
            String pozn = "";
            boolean visible = (first || ((Boolean)tranProZastupce.get(idTran)).booleanValue());
            if(voBunka.hasNext() && visible) 
            {
              Row rowBunka = voBunka.next();
              hlpNum = (Number) rowBunka.getAttribute("NdCastka");
              if(hlpNum!=null) castNr = hlpNum.doubleValue();
              sumaPlan += castNr;
              skupPlan += castNr;
              mena = (String)rowBunka.getAttribute("SMena");
              if(mena==null) mena = menaBud;
              pozn = (String)rowBunka.getAttribute("SPoznamka");
              setCellValue( listNr, dataRowNr, dataColNr, castNr , style );
              dataColNr++;
              setCellValue( listNr, dataRowNr, dataColNr, mena , style );
              dataColNr++;
            }
            else dataColNr+=2;
            voBunka.closeRowSet();
            double sum = 0;
            //voBunkaData1.setWhereClause(whereBunka);
            voBunkaData1.setWhereClauseParam(0,sdf.format(datum));
            voBunkaData1.setWhereClauseParam(1,sdf.format(datum));
            voBunkaData1.setWhereClauseParam(2,new Integer(idBud));
            voBunkaData1.setWhereClauseParam(3,new Integer(idTran));
            if(voBunkaData1.hasNext() && visible) 
            {
              Row rowBunkaData = voBunkaData1.next();
    
              hlpNum = (Number) rowBunkaData.getAttribute("NdCastkalocalZd");
              if(hlpNum!=null) castZdNr = hlpNum.doubleValue();
              sumaZd += castZdNr;
              sumaSch += castZdNr;
              skupZd += castZdNr;
              skupSch += castZdNr;
              double pctZdNr = 0;
              if(hlpNum!=null && castNr!=0.0) {
                pctZdNr = (hlpNum.doubleValue() / castNr)*100;
              }
              sum = castZdNr;
              
              /**/if(castZdNr>castNr && !rok2005) style = styleErr; else style = null;
              setCellValue( listNr, dataRowNr, dataColNr++, castZdNr , style );
              setCellValue( listNr, dataRowNr, dataColNr++, mena , style );
              setCellValue( listNr, dataRowNr, dataColNr++, pctZdNr , style );
              style = null;
              if(detail && castZdNr!=0.0) 
              {
                outputDetail();
              }
            }
            else dataColNr+=3;
            voBunkaData1.closeRowSet();
            //voBunkaData2.setWhereClause(whereBunka);
            voBunkaData2.setWhereClauseParam(0,sdf.format(datum));
            voBunkaData2.setWhereClauseParam(1,sdf.format(datum));
            voBunkaData2.setWhereClauseParam(2,new Integer(idBud));
            voBunkaData2.setWhereClauseParam(3,new Integer(idTran));
            if(voBunkaData2.hasNext() && visible) 
            {
              Row rowBunkaData = voBunkaData2.next();
    
              hlpNum = (Number) rowBunkaData.getAttribute("NdCastkalocalSch");
              if(hlpNum!=null) { 
                castSchNr = hlpNum.doubleValue();
                sum+=castSchNr;
                sumaSch += castSchNr;
                skupSch += castSchNr;
              }
              double pctSchNr = 0;
              if(sum!=0.0 && castNr!=0.0) {
                pctSchNr = (sum / castNr)*100;
              }
              
              /**/if(sum>castNr && !rok2005) style = styleWarn;  else style = null;
              setCellValue( listNr, dataRowNr, dataColNr++, castSchNr , style );
              setCellValue( listNr, dataRowNr, dataColNr++, mena , style );
              setCellValue( listNr, dataRowNr, dataColNr++, pctSchNr , style );
              style = null;
            }
            else dataColNr+=3;
            voBunkaData2.closeRowSet();
            setCellValue( listNr, dataRowNr, dataColNr++, pozn , style );
            
            dataRowNr++;
          }
          int dataColNr = startColNr;
          setCellValue( listNr, dataRowNr, dataColNr++, skupPlan , styleBold );
          dataColNr++;
          if(skupZd>skupPlan) style = styleErr;  else style = null;
          setCellValue( listNr, dataRowNr, dataColNr++, skupZd , style );
          dataColNr++;
          setCellValue( listNr, dataRowNr, dataColNr++, skupPlan==0 ? 0 : 100*skupZd/skupPlan , style );
          if(skupSch>skupPlan) style = styleWarn;  else style = null;
          setCellValue( listNr, dataRowNr, dataColNr++, skupSch , style );
          dataColNr++;
          setCellValue( listNr, dataRowNr, dataColNr++, skupPlan==0 ? 0 : 100*skupSch/skupPlan , style );
          dataRowNr++;
        }
        int dataColNr = startColNr;
        setCellValue( listNr, dataRowNr, dataColNr++, sumaPlan , styleBold );
        dataColNr++;
        setCellValue( listNr, dataRowNr, dataColNr++, sumaZd , styleBold );
        dataColNr++;
        setCellValue( listNr, dataRowNr, dataColNr++, sumaPlan==0 ? 0 : 100*sumaZd/sumaPlan , styleBold );
        setCellValue( listNr, dataRowNr, dataColNr++, sumaSch , styleBold );
        dataColNr++;
        setCellValue( listNr, dataRowNr, dataColNr++, sumaPlan==0 ? 0 : 100*sumaSch/sumaPlan , styleBold );
  
        if(colNr+COLS_BY_BUD >= 255) 
        {
          listNr++;
          colNr = 1;
        }
      }
      voBud.closeRowSet();
      first = false;
    }
    
    dm.getTransaction().commit();
  }

  private void outputListyGestor()
  {
    CellStyle style = null;
    int listNr = 0;
    int rowNr=0;
    int colNr = 0;

    if(detail) 
    {
      wb.cloneSheet(0);
      wb.setSheetName(0,"Detail");
      listNr++;
    }

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*60));

    List bud = new ArrayList();
    Map budMena = new HashMap();
    ViewObject vo = dm.findViewObject("VwDatBudgetView1");
    String whereTT = "C_DEVELOPER = '0' AND C_SPECIAL='0' AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";

// esc 03/2010   String wZastup = "(SELECT ID_KOHO FROM DB_JT.KP_DAT_BUDGETZASTUP WHERE ID_KDO = "+userId+" AND (DT_PLATNOSTOD IS NULL OR DT_PLATNOSTOD<=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')) AND (DT_PLATNOSTDO IS NULL OR DT_PLATNOSTDO>=TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')) AND ID_BUDGET IS NULL)";
    String whereZastup = userId + " IN ( select bgz.id_kdo from db_jt.KP_REL_GESTORTRANSAKCE ges, db_jt.kp_dat_budgetzastup bgz "+
                                  " where ges.id_typtransakce = KpCisTyptransakce.id and ges.id_appuser = bgz.id_koho "+
                                  " AND sysdate BETWEEN   bgz.dt_platnostod and bgz.dt_platnostdo            )";
    if(exportType != ESExportBudgetStd.EXPORT_TYPE_GESTOR_ALL) {
    whereTT += " AND " + userId  +" IN (select ges.id_appuser     from  db_jt.KP_REL_GESTORTRANSAKCE ges where ges.id_typtransakce = KpCisTyptransakce.id ) ";
    whereTT += " OR " + whereZastup;

logger.info(" WhereTT =" + whereTT);  
    
    /*  esc 03/2010    whereTT += " AND (ID_GESTOR_CZ = "+userId+
             " OR ID_GESTOR_SK = "+userId+
             " OR ID_GESTOR_Z = "+userId+
             " OR ID_GESTOR_CZ IN "+wZastup+" AND C_PROZASTUPCE='1'"+
             " OR ID_GESTOR_SK IN "+wZastup+" AND C_PROZASTUPCE='1'"+
             " OR ID_GESTOR_Z IN "+wZastup+" AND C_PROZASTUPCE='1')";
    */
    }
    String whereOJ = "TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
    
    vo.clearCache();
    vo.setWhereClause(whereOJ);
    rowNr = 1;
    while ( vo.hasNext() ) {
      Row row = vo.next();
      rowNr++;
      colNr = 0;
      
      String kod = (String) row.getAttribute("SKod");
      String popis = (String) row.getAttribute("SNazev");
      String idBud = ((Number) row.getAttribute("Id")).toString();
      oracle.jbo.domain.Date dt = (oracle.jbo.domain.Date)row.getAttribute("DtPlatnostod");
      String dtOd = dt!=null ? sdf.format(dt.dateValue()) : "";
      dt = (oracle.jbo.domain.Date)row.getAttribute("DtPlatnostdo");
      String dtDo = dt!=null ? sdf.format(dt.dateValue()) : "";
      String menaBud = (String)row.getAttribute("SMena");
      bud.add(idBud);
      budMena.put(idBud, menaBud);
      
      String nazev = kod + " - " + popis + " (" + dtOd + " - " + dtDo +")";
      curTranNazev.put(idBud, nazev);

      setCellValue( listNr, rowNr, colNr, nazev , lightBlue );                        
    }
    vo.closeRowSet();
    
    rowNr++;
    setCellValue( listNr, rowNr, colNr, "CELKEM" , styleBold );                        
    
    ViewObject voTran = dm.findViewObject("KpCisTyptransakceView1");
    voTran.clearCache();
    voTran.setWhereClause(whereTT);
    int nrClones = 0;
    if(voTran.hasNext()) {
      nrClones = (voTran.getRowCount() * COLS_BY_BUD) / 254;
    }
    wb.setSheetName(listNr,"List 1");
    detListNr = 0;//1+nrClones;
    for(int i=0; i<nrClones; i++) 
    {
      wb.cloneSheet(listNr);
      wb.setSheetName(listNr+i+1,"List "+(i+2));
    }
    rowNr = 0;
    colNr = 1;
    ViewObject voBunka = dm.findViewObject("VwDatBudgetpolozkaView1");
    voBunka.clearCache();
    ViewObject voBunkaData1 = dm.findViewObject("VwDatBudgetpolozkadata1PARView1");
    voBunkaData1.clearCache();
    ViewObject voBunkaData2 = dm.findViewObject("VwDatBudgetpolozkadata2PARView1");
    voBunkaData2.clearCache();
    while(voTran.hasNext()) 
    {
      Row rowTran = voTran.next();
      //HLAVICKY
      idTran = ((Number)rowTran.getAttribute("Id")).toString();
      String kod = (String)rowTran.getAttribute("SKod");
      String nazev = (String)rowTran.getAttribute("SPopis");
      //String menaBud = (String)rowBud.getAttribute("SMena");
      curNazev = kod + " / " + nazev;
      setCellValue( listNr, rowNr, colNr, curNazev , styleBold );

      int startColNr = colNr;

      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
      setCellValue( listNr, rowNr+1, colNr, "Plnovan ve" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
      setCellValue( listNr, rowNr+1, colNr, "Mna" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
      setCellValue( listNr, rowNr+1, colNr, "erpan ve" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
      setCellValue( listNr, rowNr+1, colNr, "Mna" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*7));
      setCellValue( listNr, rowNr+1, colNr, "% erpn" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
      setCellValue( listNr, rowNr+1, colNr, "Propoet erpan ve + SL" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
      setCellValue( listNr, rowNr+1, colNr, "Mna" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*7));
      setCellValue( listNr, rowNr+1, colNr, "% erpn + SL" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*40));
      setCellValue( listNr, rowNr+1, colNr, "Poznmka" , lightBlue ); colNr++;

      int dataRowNr = rowNr+2;
      String mena = "";
      Number hlpNum = null;
      //DATA
      double sumaPlan = 0.0,
             sumaZd = 0.0,
             sumaSch = 0.0;

      Iterator iter = bud.iterator();
      while ( iter.hasNext() ) {
        int dataColNr = startColNr;
        String strIdBud = (String) iter.next();
        idBud = Integer.parseInt(strIdBud);
        String menaBud = (String) budMena.get(strIdBud);
        style = null;
        
        double castNr = 0, castZdNr = 0, castSchNr = 0;
        
        String whereBunka = "ID_BUDGET = "+idBud+
                           " AND ID_CISTYPTRAN = "+idTran;
        voBunka.setWhereClause(whereBunka);
        String pozn = "";
        if(voBunka.hasNext()) 
        {
          Row rowBunka = voBunka.next();
          hlpNum = (Number) rowBunka.getAttribute("NdCastka");
          if(hlpNum!=null) castNr = hlpNum.doubleValue();
          sumaPlan += castNr;
          mena = (String)rowBunka.getAttribute("SMena");
          if(mena==null) mena = menaBud;
          pozn = (String)rowBunka.getAttribute("SPoznamka");
          setCellValue( listNr, dataRowNr, dataColNr++, castNr , style );
          setCellValue( listNr, dataRowNr, dataColNr++, mena , style );
        }
        else dataColNr+=2;
        voBunka.closeRowSet();
        double sum = 0;
        //voBunkaData1.setWhereClause(whereBunka);
        voBunkaData1.setWhereClauseParam(0,sdf.format(datum));
        voBunkaData1.setWhereClauseParam(1,sdf.format(datum));
        voBunkaData1.setWhereClauseParam(2,new Integer(idBud));
        voBunkaData1.setWhereClauseParam(3,new Integer(idTran));
        if(voBunkaData1.hasNext()) 
        {
          Row rowBunkaData = voBunkaData1.next();

          hlpNum = (Number) rowBunkaData.getAttribute("NdCastkalocalZd");
          if(hlpNum!=null) castZdNr = hlpNum.doubleValue();
          sumaZd += castZdNr;
          sumaSch += castZdNr;
          double pctZdNr = 0;
          if(hlpNum!=null && castNr!=0.0) {
            pctZdNr = (hlpNum.doubleValue() / castNr)*100;
          }
          sum = castZdNr;
          
          /**/if(castZdNr>castNr) style = styleErr; else style = null;
          setCellValue( listNr, dataRowNr, dataColNr++, castZdNr , style );
          setCellValue( listNr, dataRowNr, dataColNr++, mena , style );
          setCellValue( listNr, dataRowNr, dataColNr++, pctZdNr , style );
          style = null;
          if(detail && castZdNr!=0.0) 
          {
            outputDetail();
          }
        }
        else dataColNr+=3;
        voBunkaData1.closeRowSet();
        //voBunkaData2.setWhereClause(whereBunka);
        voBunkaData2.setWhereClauseParam(0,sdf.format(datum));
        voBunkaData2.setWhereClauseParam(1,sdf.format(datum));
        voBunkaData2.setWhereClauseParam(2,new Integer(idBud));
        voBunkaData2.setWhereClauseParam(3,new Integer(idTran));
        if(voBunkaData2.hasNext()) 
        {
          Row rowBunkaData = voBunkaData2.next();

          hlpNum = (Number) rowBunkaData.getAttribute("NdCastkalocalSch");
          if(hlpNum!=null) { 
            castSchNr = hlpNum.doubleValue();
            sum+=castSchNr;
            sumaSch += castSchNr;
          }
          double pctSchNr = 0;
          if(sum!=0.0 && castNr!=0.0) {
            pctSchNr = (sum / castNr)*100;
          }
          
          /**/if(sum>castNr) style = styleWarn;  else style = null;
          setCellValue( listNr, dataRowNr, dataColNr++, castSchNr , style );
          setCellValue( listNr, dataRowNr, dataColNr++, mena , style );
          setCellValue( listNr, dataRowNr, dataColNr++, pctSchNr , style );
          style = null;
        }
        else dataColNr+=3;
        voBunkaData2.closeRowSet();
        setCellValue( listNr, dataRowNr, dataColNr++, pozn , style );
        
        dataRowNr++;
      }
      int dataColNr = startColNr;
      setCellValue( listNr, dataRowNr, dataColNr++, sumaPlan , styleBold );
      dataColNr++;
      setCellValue( listNr, dataRowNr, dataColNr++, sumaZd , styleBold );
      dataColNr++;
      setCellValue( listNr, dataRowNr, dataColNr++, sumaPlan==0 ? 0 : 100*sumaZd/sumaPlan , styleBold );
      setCellValue( listNr, dataRowNr, dataColNr++, sumaSch , styleBold );
      dataColNr++;
      setCellValue( listNr, dataRowNr, dataColNr++, sumaPlan==0 ? 0 : 100*sumaSch/sumaPlan , styleBold );

      if(colNr+COLS_BY_BUD >= 255) 
      {
        listNr++;
        colNr = 1;
      }
    }
    voTran.closeRowSet();
    
    dm.getTransaction().commit();
  }

  private void outputDetail()
  {
    int colNr=0;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;

    if(detRowNr==0) {
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*60));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*60));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*50));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*15));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*5));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*15));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*11));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*30));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*12));
      wb.getSheetAt(detListNr).setColumnWidth((short)colNr++,(short)(256*30));
  
      colNr=0;
      setCellValue( detListNr, detRowNr, colNr, "Budget" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "Transakce" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "Popis" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "stka v mn" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "Mna" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "stka local" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "Datum" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "Spolenost" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "et" , lightBlue ); colNr++;
      setCellValue( detListNr, detRowNr, colNr, "Protistrana" , lightBlue ); colNr++;
      
      detRowNr++;
    }

    double castkaMena = 0.0, castkaLocal = 0.0;

    if(voDetail==null) {
      voDetail = dm.findViewObject("VwDatBudgettransakceView1");
      voDetail.clearCache();
    }
    voDetail.setWhereClause("ID_BUDGET = " + idBud + " AND ID_CISTYPTRANSAKCE = " + idTran+
                            " AND (ID_KTGPROJEKT IS NULL OR ID_KTGPROJEKT=1000001) AND SPOL_DOC<>'1'");
    while(voDetail.hasNext() && detRowNr < pocet) 
    {
      colNr = 0;    
      Row row = voDetail.next();
      
      oracle.jbo.domain.Date hlpDt = null;
      oracle.jbo.domain.Number hlpNum = null;
      
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("Datum");
      String datumTran = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastkamena");
      castkaMena = hlpNum==null ? 0.0 : hlpNum.doubleValue();
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("NdCastkalocal");
      castkaLocal = hlpNum==null ? 0.0 : hlpNum.doubleValue();

      setCellValue( detListNr, detRowNr, colNr, curNazev , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, (String)curTranNazev.get(idTran) , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, (String)row.getAttribute("SPopis") , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, castkaMena , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, (String)row.getAttribute("SMena") , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, castkaLocal , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, datumTran , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, (String)row.getAttribute("Spolecnost") , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, (String)row.getAttribute("SUcet") , null ); 
      colNr++;
      setCellValue( detListNr, detRowNr, colNr, (String)row.getAttribute("SUnifspol") , null ); 
      colNr++;

      detRowNr++;
    }
    voDetail.closeRowSet();
    if ( detRowNr >= pocet ) {
      setCellValue( detListNr, detRowNr+1, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( detListNr, detRowNr+2, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
  }

  protected boolean outputData () 
  {
    if(lightBlue==null) {
      lightBlue = wb.createCellStyle();
      lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    if(styleErr==null) {
      styleErr = wb.createCellStyle();
      styleErr.setFillForegroundColor(IndexedColors.RED.getIndex());
      styleErr.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
  
    if(styleWarn==null) {
      styleWarn = wb.createCellStyle();
      styleWarn.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
      styleWarn.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
  
    if(styleBold==null) {
      Font font = wb.createFont();
      font.setFontHeightInPoints((short) 10);
      font.setBold(true);
      styleBold = wb.createCellStyle();
      styleBold.setFont(font);
    }

    if(styleBoldRed==null) {
      Font font = wb.createFont();
      font.setFontHeightInPoints((short) 10);
      font.setBold(true);
      font.setColor(IndexedColors.RED.getIndex());
      styleBoldRed = wb.createCellStyle();
      styleBoldRed.setFont(font);
    }
 
    if(exportType != EXPORT_TYPE_GESTOR && exportType != EXPORT_TYPE_GESTOR_ALL) {
      outputListyStd();
    }
    else {
      outputListyGestor();
    }
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.budget.BudgetModule","BudgetModuleLocal");
      
      ESExportBudgetStd ed = new ESExportBudgetStd(dm,
                                                   new java.sql.Date(System.currentTimeMillis()),
                                                   24,
                                                   ESExportBudgetStd.EXPORT_TYPE_OO_OJ,
                                                   true,
                                                   true);
                                                   
/*
      ESExportBudgetStd ed = new ESExportBudgetStd(dm,
                                                   new java.sql.Date(105,11,31),
                                                   //new java.sql.Date(System.currentTimeMillis()),
                                                   1063,
                                                   ESExportBudgetStd.EXPORT_TYPE_GESTOR,
                                                   true,
                                                   true);                                                   
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
