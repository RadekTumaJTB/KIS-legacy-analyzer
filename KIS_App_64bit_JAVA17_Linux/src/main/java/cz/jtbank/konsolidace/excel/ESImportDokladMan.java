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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import cz.jtbank.konsolidace.doklady.common.DokladyModule;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESImportDokladMan extends AbsReadExcel
{
  static Logger logger = Logger.getLogger(ESImportDokladMan.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private DokladyModule dm;
  private int idDoklad;
  private String nazevSouboru;
  private int idSpol;
  private String mena;
  private java.sql.Date datum;
  private Number idSub;
  
  private String souborPredponaSpol;
  private boolean po_9_2006;

  public ESImportDokladMan(DokladyModule dokladyModule, String nazevSouboru, int idDoklad)
  {
    dm = dokladyModule;
    this.nazevSouboru = nazevSouboru;
    this.idDoklad = idDoklad;
    init();
  }

  private void init() {
    logger.info("ImportDokladMan:nazevSouboru="+nazevSouboru);  

    ViewObject vo = dm.findViewObject("VwKpDokladzahlaviView1");
    vo.clearCache();
    vo.setWhereClause("DOKLADID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      
      idSpol = ((Number) row.getAttribute("Ucetnispolecnostid")).intValue();
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
      mena = (String) row.getAttribute("SMena");
      datum = ((oracle.jbo.domain.Date) row.getAttribute("DtDatum")).dateValue();
      idSub = (Number) row.getAttribute("IdSubkonsolidace");
    }
    vo.closeRowSet();

    setFileName ( nazevSouboru );
    setFileRelativeName( souborPredponaSpol+"_"+idSpol+"\\"+getFileName() );
    setFileAbsoluteName( IN_DIR + this.getFileRelativeName() );
    
    Calendar cal = new GregorianCalendar(2006,Calendar.SEPTEMBER,30);
    po_9_2006 = datum.after(cal.getTime());
  }
  
  private int[] getListStartSloupec(int listPoradi) 
  {
    switch (listPoradi) 
    {
      case 1: return new int[] {3,6,8};
      case 2: return new int[] {3,4,5};
      case 3: return new int[] {3,4,5};
      case 4: return new int[] {3,4,5};
      case 5: return new int[] {3,4};
    }
    return new int[] {};
  }

  private int getListStartRadek(int listPoradi) 
  {
    switch (listPoradi) 
    {
      case 1: return 8;
      case 2: return 5;
      case 3: return 5;
      case 4: return 5;
      case 5: return 6;
    }
    return 0;
  }
  
  private void readDokladIFRS() throws KisException
  {
    int listNr = 2;
    int rowNr=0;
    int[] colNr;

    ViewObject voList = dm.findViewObject("ListDokladView1");
    voList.clearCache();
    voList.setWhereClause("KpTmpDefdoklad.ID_CISDOKLAD = 1");
    if(voList.hasNext()) 
    {
      dm.deleteAgregace(idDoklad);
    }
    while(voList.hasNext()) 
    {
      Row rowList = voList.next();
      int listPoradi = ((Number)rowList.getAttribute("NlPoradi")).intValue();
      
      listNr = getListNr("B"+listPoradi);
      colNr = getListStartSloupec(listPoradi);
      
      ViewObject vo = dm.findViewObject("VwKpDokladmanualView1");
      String where = "ID_DOKLAD = " + idDoklad + " AND NL_PORADILIST = " + listPoradi + " AND S_MENA = '"+mena+"' AND (NL_PORADILIST<>2 OR NL_RADEK<>nl_radekPasivaHV)";
      vo.clearCache();
      vo.setWhereClause(where);
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        
        //System.out.print(row.getAttribute("Idpolozkadoklad")+"/");
        Number idDlb = (Number) row.getAttribute("IdpolozkaDlb");
        //System.out.print(row.getAttribute("Idopravnapolozkadoklad")+"/");
        Number idDlbOpr = (Number) row.getAttribute("IdopravnaDlb");
        
        int radekPoradi = ((Number)row.getAttribute("NlRadek")).intValue();
        rowNr = radekPoradi + getListStartRadek(listPoradi);
        
        double castka = 0;
        double castkaOpr = 0;

        for(int i=0; i<colNr.length; i++) {
          castka += getDoubleValue( listNr, rowNr, colNr[i] );
          if( wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr[i]) != null ) {
            int cellType = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr[i]).getCellType();
            if(cellType == Cell.CELL_TYPE_FORMULA) 
            {
              throw new KisException("Nen mono natat vzorce v poli stka ("+listNr+"/"+colNr[i]+")!",
                                     new KisException(Constants.ERR_MESSAGE_ONLY));
            }
          }
        }
        
        if(listPoradi == 1) {
          for(int i=0; i<colNr.length; i++) {
            castkaOpr += getDoubleValue( listNr, rowNr, colNr[i]+1 );
            int cellType = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr[i]).getCellType();
            if(cellType == Cell.CELL_TYPE_FORMULA) 
            {
              throw new KisException("Nen mono natat vzorce v poli stka opravn ("+listNr+"/"+colNr[i]+")!",
                                     new KisException(Constants.ERR_MESSAGE_ONLY));
            }
          }
        }
        
        int koef = (listPoradi==1 || listPoradi==3) ? 1 : -1;
        //System.out.println(listNr+","+rowNr+","+colNr+"/"+radekPoradi+": "+castka+" ("+idDlb+")/"+castkaOpr+" ("+idDlbOpr+")");
        if(castka!=0.0 && idDlb!=null) 
        {
          try {
            dm.manEditBunka("I",0,idDoklad,idDlb.intValue(),"X"+idDlb,mena,koef*castka,koef*castka,0);
          }
          catch(KisException e) { e.printStackTrace(); throw e; }
        }
        if(castkaOpr!=0.0 && idDlbOpr!=null) 
        {
          try {
            dm.manEditBunka("I",0,idDoklad,idDlbOpr.intValue(),"X"+idDlbOpr,mena,koef*castkaOpr,koef*castkaOpr,0);
          }
          catch(KisException e) { e.printStackTrace(); throw e; }
        }

      }
      vo.closeRowSet();
    }
    voList.closeRowSet();
  }
  
  private int getIdFromSpolName(String spolName) 
  {
    int idUcSpol = -1;
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("trim(upper(S_NAZEV)) = trim(upper('"+spolName+"'))");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idUcSpol = ((Number)row.getAttribute("Id")).intValue();
    }
    return idUcSpol;
  }
  
  private void readDokladVazby() throws KisException
  {
    int listNr;
    int rowNr;
    int colNr;

    listNr = getListNr("34_");
    
    if(idSub!=null && listNr>0) {
      throw new KisException("Excel se sub-konsolidanm \"lutm\" dokladem by neml obsahovat list 34_... s vazbami!",
                             new KisException(Constants.ERR_MESSAGE_ONLY));
    }
    
    for(rowNr=9;rowNr<15;) 
    {
      String proti = getStringValue(listNr, rowNr, 0);
      if(proti!=null) break;
      else rowNr++;
    }
    
    for(;;rowNr++)
    {
      colNr = 1;
/*      
      String proti = getStringValue(listNr, rowNr, colNr++);
      if(proti==null) break;
      int idUcSpol = getIdFromSpolName(proti);
*/
      int idUcSpol = -1;
      String sIdSpol = getStringValue(listNr, rowNr, colNr);
      int cellType = -1;
      if(wb.getSheetAt(listNr).getRow(rowNr)!=null && 
         wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr)!=null) {
        cellType = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr).getCellType();
      }
      if(cellType == Cell.CELL_TYPE_FORMULA) 
      {
        throw new KisException("Nen mono natat vzorce v poli ID spolenosti pi natn vazeb!",
                               new KisException(Constants.ERR_MESSAGE_ONLY));
      }
      if(sIdSpol!=null) {
        try { idUcSpol = Integer.parseInt(sIdSpol); } catch(Exception e) { idUcSpol=-1; }
      }
      if(idUcSpol<0 ) {
        idUcSpol = (int) getDoubleValue(listNr, rowNr, colNr);
      }
      if(idUcSpol<0 ) 
      {
        String proti = getStringValue(listNr, rowNr, 0);
        if(proti==null) continue;
        idUcSpol = getIdFromSpolName(proti);
      }
      if(idUcSpol<1) break;
      colNr++;
      
      String clcr = getStringValue(listNr, rowNr, colNr++);
      if(clcr==null) continue;
      
      int cl = Integer.parseInt( clcr.substring( clcr.indexOf('B')+1, clcr.indexOf(';') ) );
      int cr = Integer.parseInt( clcr.substring( clcr.indexOf(';')+1 ).trim() );
      
      colNr+=2;

      String mena = getStringValue(listNr, rowNr, colNr++);
      String ucet = getStringValue(listNr, rowNr, colNr++);
      
      double castkaMena = getDoubleValue(listNr, rowNr, colNr++);
      double castkaLocal = getDoubleValue(listNr, rowNr, colNr++);
      
      int znamenko = 1;
      if(po_9_2006 && (cl==2 || cl==4 || cl==5)) znamenko = -1;

      try {      
        dm.manExcelReadVazby(idDoklad,
                             idUcSpol,
                             cl,
                             cr,
                             mena,
                             ucet,
                             znamenko * castkaMena,
                             znamenko * castkaLocal);
      }
      catch(KisException e) { e.printStackTrace(); throw e; }
    }
  }

  protected boolean readData () throws KisException
  {
    readDokladIFRS();
    
    readDokladVazby();
    
    return true;
  }
  
  public static void main(String[] argv) throws KisException
  {
    try {
      DokladyModule dm = (DokladyModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
//      ESImportDokladMan ed = new ESImportDokladMan(dm,"skuska balik burson.xlsx",503015);
      ESImportDokladMan ed = new ESImportDokladMan(dm,"IBI2.xlsx", 1853462 );
      ed.excelRead();
	  	  
	  
	  
/*
      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=ed.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
*/      
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }
  
}
