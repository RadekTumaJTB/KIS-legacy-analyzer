package cz.jtbank.konsolidace.excel;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import cz.jtbank.konsolidace.common.Constants;
import java.text.SimpleDateFormat;
import java.util.*;

public class ESExportMustkyUnif extends AbsExcelDoklad 
{
  private Number idCisSubject;
  private java.sql.Date datum;
  private String locale;
  private boolean genAll = false;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportMustkyUnif(Number idCisSubject, java.sql.Date datum, String locale)
  {
    this.idCisSubject = idCisSubject;
    this.datum = datum;
    this.locale = locale;
    init();
  }

  public ESExportMustkyUnif(java.sql.Date datum, String locale)
  {
    genAll = true;
    this.datum = datum;
    this.locale = locale;
    init();
  }

  private void init() {
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaMustekUnif.xlsx" );
    setFileName ( Constants.DIR_MUSTKY + "MustekUnif_"+datum+".xlsx" );
    setFileRelativeName( getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }

  private void cleanUp() 
  {
/*
    for(int x=colNrA;x<256;x++) {
      for(int y=0;y<=200;y++) {
        clearCell(0,y,x);
      }
    }

    for(int listNr=1;listNr<5;listNr++) {
      for(int x=colNrOst;x<256;x++) {
        for(int y=0;y<=200;y++) {
          clearCell(listNr,y,x);
        }
      }
    }*/
  }
  
  protected boolean outputData () 
  {
    ApplicationModule mm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.mustky.MustkyModule","MustkyModuleLocal");

    String strDatum = sdf.format(datum);
    setCellValue(0,0,0,strDatum,null);
    
    List subjects = new ArrayList();
    
    ViewObject voSubj = mm.findViewObject("KpTmpCissubjectView1");
    voSubj.clearCache();
    if(!genAll) 
      voSubj.setWhereClause("KpTmpCissubject.ID = "+idCisSubject);
    else voSubj.setWhereClause("ID_CISDOKLAD = 1 AND KpTmpCissubject.ID <> 10"+
      " and exists (select null from db_jt.Kp_Def_Ucet2ucetunif where ID_CISSUBJECT = KpTmpCissubject.ID AND to_date('"+strDatum+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO)"
    );
    voSubj.setOrderByClause("KpTmpCissubject.ID");
    voSubj.clearCache();
    int rowNr=0, 
        colNr=2;
    while(voSubj.hasNext()) {
      Row rowSubj = voSubj.next();
      
      Number idSubject = (Number) rowSubj.getAttribute("Id");
      String name = (String) rowSubj.getAttribute("SPopis");
      
      subjects.add(idSubject);
      setCellValue(0,rowNr,colNr,name,null);
      colNr++;
    }
    voSubj.closeRowSet();

    ViewObject voUcet = mm.findViewObject("KpCisUcetuniflangView1");
    voUcet.setWhereClause("S_LOCALE = '"+locale+"'");
    voUcet.setOrderByClause("S_UCET");
    voUcet.clearCache();
    ViewObject vo = mm.findViewObject("KpDefUcet2ucetunifView1");
    vo.clearCache();
    while(voUcet.hasNext()) {
      rowNr++;
      Row rowUcet = voUcet.next();

      Number idUcet = (Number) rowUcet.getAttribute("Id");
      String ucet = (String) rowUcet.getAttribute("SUcet");
      String popis = (String) rowUcet.getAttribute("SPopis");
//System.out.println(ucet);

      colNr=0;
      setCellValue(0,rowNr,colNr++,ucet,null);
      setCellValue(0,rowNr,colNr++,popis,null);

      Iterator iter = subjects.iterator();
      while(iter.hasNext()) 
      {
        Number idSubject = (Number) iter.next();
        
        vo.setWhereClause("ID_CISUCETUNIF = "+idUcet+
                     " AND ID_CISSUBJECT = "+idSubject+
                     " AND to_date('"+strDatum+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
        while(vo.hasNext()) 
        {
          Row rowDetail = vo.next();

          String value = (String)rowDetail.getAttribute("SymbolString");
          String origValue = null;
          if(wb.getSheetAt(0) != null &&
             wb.getSheetAt(0).getRow(rowNr) != null &&
             wb.getSheetAt(0).getRow(rowNr).getCell((short)colNr) != null) {
            origValue = wb.getSheetAt(0).getRow(rowNr).getCell((short)colNr).getStringCellValue();
          }
          if(origValue != null && origValue.length() > 0) value = origValue+'\n'+value;
          setCellValue(0,rowNr,colNr,value,null);
        }
        vo.closeRowSet();
        colNr++;
      }
    }
    voSubj.closeRowSet();

    mm.getTransaction().commit();
    
//    cleanUp();
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ESExportMustkyUnif em = new ESExportMustkyUnif(/*new Number(900),*/new java.sql.Date(new java.util.Date(107,0,1).getTime()),"cs_CZ");
      em.excelOutput();
      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=em.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
}