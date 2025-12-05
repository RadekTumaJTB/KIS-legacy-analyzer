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

public class ESExportSubkons6Vazby extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkons6Vazby.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private int subkonsId;
  private int ucSkup;
  private String kurzListek;
  private java.sql.Date datum;

  private String nazevSpol;
  private String menaSpol;
  private String souborPredpona;

  private int idSpol;
  private int dokladId;
  private int colNr = 2;

  private CellStyle styleBold;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private CellStyle lightBlue;

  public ESExportSubkons6Vazby(ApplicationModule dokladyModule,
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
      ucSkup = ((Number)row.getAttribute("IdKtgucetniskupina")).intValue();
      kurzListek = (String) row.getAttribute("STypkurslistek");
    }
    voSkup.closeRowSet();

    logger.info("ExportSubkons6Vazby:nazevSpol="+nazevSpol+",datum="+datum+",menaSpol="+menaSpol);  
    
    setFileName ( "Sub6VV"+subkonsId+"_"+datum+".xlsx" );
    setFileRelativeName( souborPredpona+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaSubkons6Vazby.xlsx" );
  }
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    
    setCellValue(0,0,0,nazevSpol,style);    
    
    setCellValue(0,3,0,"VSLEDOVKA CELKEM v "+menaSpol,style);

    String txtDatum = sdf.format(datum);
    setCellValue(0,4,0,txtDatum,style);
  }

  private void outputDoklad() 
  {
    CellStyle style = null;

    ViewObject voDoklad = dm.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    voDoklad.setWhereClause("ID_SUBKONSOLIDACE = "+subkonsId+
                            " AND DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')"+
                            " AND NL_KROK = 200");
    if(voDoklad.hasNext()) 
    {
      Row rowDoklad = voDoklad.next();
      Number nDokladId = (Number) rowDoklad.getAttribute("Id");
      if(nDokladId != null) 
      {
        dokladId = nDokladId.intValue();

        int rowNr=0;   
        int listNr = 0;
        ViewObject vo = dm.findViewObject("VwSubkonDokladpolozkaView1");
        vo.clearCache();
        vo.setWhereClause("ID_DOKLAD = "+dokladId+" AND NL_PORADILIST = 5 AND ND_CASTKALOCAL <> 0");
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          int radek = ((Number)row.getAttribute("NlRadek")).intValue();
          double castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).doubleValue();
          
          rowNr = radek + 4;
          
          if(castkaLocal!=0.0) setCellValue(listNr,rowNr,colNr,castkaLocal,style);
        }
        vo.closeRowSet();
        colNr++;
      }
    }
    voDoklad.closeRowSet();
    dm.getTransaction().commit();
  }

  private void setHeader(int idSpolTo) 
  {
    CellStyle style = null;
    String nazevFrom = null, nazevTo = null;
  
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idSpol);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevFrom = (String) row.getAttribute("SNazev");
    }
    vo.closeRowSet();
    
    vo.setWhereClause("ID = "+idSpolTo);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevTo = (String) row.getAttribute("SNazev");
    }
    vo.closeRowSet();
    
    dm.getTransaction().commit();

    setCellValue(0,2,colNr,nazevFrom,styleBold);
    setCellValue(0,3,colNr,"<->",styleBold);
    setCellValue(0,4,colNr,nazevTo,styleBold);
  }

  private void outputVazby()
  {
    CellStyle style = null;

    ViewObject voRel = dm.findViewObject("KpRelSubkonsolidaceclenSpecial1");
    voRel.clearCache();
    voRel.setWhereClause("ID_KTGSUBKONSOLIDACE = "+subkonsId+
                    " and C_CLEN = '1' "+
                    " and ID_CISSUBTYPCLENSTVI = 1 "+
                    " and TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') between DT_ClenstviOD and DT_ClenstviDO");
    while(voRel.hasNext()) 
    {
      Row rowRel = voRel.next();
      Number nSpolId = (Number) rowRel.getAttribute("IdKtgucetnispolecnost");
      idSpol = nSpolId.intValue();
      int rowNr=0;   
      int listNr = 0;
      ViewObject vo = dm.findViewObject("KpDatDokladvazbySubkonsView1");
      vo.clearCache();
      vo.setWhereClauseParam(0,kurzListek);
      vo.setWhereClauseParam(1,menaSpol);
      vo.setWhereClauseParam(2,datum);
      vo.setWhereClause("dv.ID_KTGUCETNISKUPINA = "+ucSkup+
                   " and dv.NL_PORADILIST = 5"+
                   " and d.ID_KTGUCETNISPOLECNOST = "+idSpol+
                   " and dv.ID_KTGUCETNISPOLECNOSTTO not in (select c.ID_KTGUCETNISPOLECNOST from db_jt.kp_rel_subkonsolidaceClen c where c.ID_KTGSUBKONSOLIDACE = "+subkonsId+" and c.C_CLEN = '1' and c.ID_CISSUBTYPCLENSTVI = 1 and TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') between c.DT_ClenstviOD and c.DT_ClenstviDO)"+
                   " and dv.ID_KTGUCETNISPOLECNOSTTO in (select c.ID_KTGUCETNISPOLECNOST from db_jt.kp_rel_subkonsolidaceClen c, db_jt.kp_ktg_subkonsolidace sub where c.C_CLEN = '1' and c.ID_CISSUBTYPCLENSTVI = 1 and TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') between c.DT_ClenstviOD and c.DT_ClenstviDO and c.ID_KTGSUBKONSOLIDACE = sub.ID_KTGUCETNISPOLECNOST and sub.ID_KTGUCETNISKUPINA = "+ucSkup+")"+
                   " and d.DT_DATUM = TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')");
      Number lastTo = null;
      if(vo.hasNext()) colNr++;
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        int radek = ((Number)row.getAttribute("NlRadek")).intValue();
        double castkaMena = ((Number)row.getAttribute("NdCastkamena")).doubleValue();
        double castka = ((Number)row.getAttribute("NdCastka")).doubleValue();
        double origCastka = 0;
        Number to = (Number)row.getAttribute("IdKtgucetnispolecnostto");
        if(!to.equals(lastTo)) 
        {
          if(lastTo!=null) colNr++;
          lastTo = to;
          setHeader(to.intValue());
        }

        rowNr = radek + 4;

        Cell cell = wb.getSheetAt(0).getRow(rowNr).getCell((short)colNr);
        if(cell!=null && cell.getCellType() == cell.CELL_TYPE_NUMERIC)
          origCastka = cell.getNumericCellValue();
        
        if(castka!=0.0) setCellValue(listNr,rowNr,colNr,origCastka+castka,style);
      }
      vo.closeRowSet();
    }
    voRel.closeRowSet();
    dm.getTransaction().commit();
  }

  private void cleanUp() 
  {
    int listNr=0;
    for(int x=colNr;x<256;x++) {
      for(int y=0;y<=200;y++) {
        clearCell(listNr,y,x);
      }
    }
  }

  protected boolean outputData () 
  {
    Font font = wb.createFont();
    font.setFontHeight((short) 150);
    font.setBold(true);
    styleBold = wb.createCellStyle();
    styleBold.setFont(font);

    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputDoklad();
    logger.info("Dogenerovan prvni sloupec");
    outputVazby();
    logger.info("Dogenerovany vazby");

    cleanUp();
    
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
      ESExportSubkons6Vazby ed = new ESExportSubkons6Vazby(dm,
                                                           10130,
                                                           new java.sql.Date(106,8,30));
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
