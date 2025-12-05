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

public class ESImportDokladManPS extends AbsReadExcel
{
  static Logger logger = Logger.getLogger(ESImportDokladManPS.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private DokladyModule dm;
  private int idDoklad;
  private String nazevSouboru;
  private int idSpol;
  private String mena;
  private Number idSub;
  
  private String souborPredponaSpol;

  public ESImportDokladManPS(DokladyModule dokladyModule, String nazevSouboru, int idDoklad)
  {
    dm = dokladyModule;
    this.nazevSouboru = nazevSouboru;
    this.idDoklad = idDoklad;
    init();
  }

  private void init() {
    logger.info("ImportDokladManPS:nazevSouboru="+nazevSouboru);  

    ViewObject vo = dm.findViewObject("VwKpDokladzahlaviView1");
    vo.clearCache();
    vo.setWhereClause("DOKLADID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      
      idSpol = ((Number) row.getAttribute("Ucetnispolecnostid")).intValue();
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
      mena = (String) row.getAttribute("SMena");
      idSub = (Number) row.getAttribute("IdSubkonsolidace");
    }
    vo.closeRowSet();

    setFileName ( nazevSouboru );
    setFileRelativeName( souborPredponaSpol+"_"+idSpol+"\\"+getFileName() );
    setFileAbsoluteName( IN_DIR + this.getFileRelativeName() );
  }
  
  private int[] getListStartSloupec(int listPoradi) 
  {
    switch (listPoradi) 
    {
      case 1: return new int[] {3,6,8};
      case 2: return new int[] {3,4,5};
    }
    return new int[] {};
  }

  private int getListStartRadek(int listPoradi) 
  {
    switch (listPoradi) 
    {
      case 1: return 8;
      case 2: return 5;
    }
    return 0;
  }
  
  private void readDokladIFRS() throws KisException
  {
    int listNr = 2;
    int rowNr=0;
    int[] colNr;

    dm.deleteAgregace(idDoklad);

    for(int listPoradi = 1; listPoradi <= 2; listPoradi++) 
    {
      listNr = getListNr("B"+listPoradi);
      colNr = getListStartSloupec(listPoradi);
      
      ViewObject vo = dm.findViewObject("VwKpDokladmanualView1");
      String where = "ID_DOKLAD = " + idDoklad + " AND NL_PORADILIST = " + listPoradi + " AND S_MENA = '"+mena+"'";
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
          int cellType = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr[i]).getCellType();
          if(cellType == Cell.CELL_TYPE_FORMULA) 
          {
            throw new KisException("Nen mono natat vzorce v poli stka ("+listNr+"/"+colNr[i]+")!",
                                   new KisException(Constants.ERR_MESSAGE_ONLY));
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
/*        
        if(listPoradi == 2) {
          if(radekPoradi == 122) {
            for(int i=0; i<colNr.length; i++) castka += getDoubleValue( listNr, rowNr+1, colNr[i] );
          }
          else if(radekPoradi == 123) castka = 0.0;
        }
*/        
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
  }

  protected boolean readData () throws KisException
  {
    readDokladIFRS();
    
    return true;
  }
  
  public static void main(String[] argv) throws KisException
  {
    try {
      DokladyModule dm = (DokladyModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
//      ESImportDokladManPS ed = new ESImportDokladManPS(dm,"skuska balik burson.xlsx",503015);
      ESImportDokladManPS ed = new ESImportDokladManPS(dm,"AeskuLab_Opening BS_2 11pre KIS.xlsx",679629);
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
