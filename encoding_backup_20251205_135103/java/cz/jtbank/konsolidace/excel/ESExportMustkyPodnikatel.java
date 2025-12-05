package cz.jtbank.konsolidace.excel;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import cz.jtbank.konsolidace.common.Constants;
import java.text.SimpleDateFormat;

public class ESExportMustkyPodnikatel extends AbsExcelDoklad 
{
  private Number idKtgDoklad;
  private java.sql.Date datum;
  private boolean genAll = false;
  private int colNrA = 2,
              colNrOst = 2;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportMustkyPodnikatel(Number idKtgDoklad, java.sql.Date datum)
  {
    this.idKtgDoklad = idKtgDoklad;
    this.datum = datum;
    init();
  }

  public ESExportMustkyPodnikatel(java.sql.Date datum)
  {
    genAll = true;
    this.datum = datum;
    init();
  }

  private void init() {
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaMustekPodnikatel.xlsx" );
    setFileName ( Constants.DIR_MUSTKY + "MustekPodnikatel_"+datum+".xlsx" );
    setFileRelativeName( getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }

  private void cleanUp() 
  {
    for(int x=colNrA;x<256;x++) {
      for(int y=0;y<=200;y++) {
        clearCell(0,y,x);
      }
    }
  
    for(int listNr=1;listNr<2;listNr++) {
      for(int x=colNrOst;x<256;x++) {
        for(int y=0;y<=200;y++) {
          clearCell(listNr,y,x);
        }
      }
    }
  }

  protected boolean outputData () 
  {
    ApplicationModule mm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.mustky.MustkyModule","MustkyModuleLocal");

    setCellValue(0,1,0,sdf.format(datum),null);
    ViewObject voSubj = mm.findViewObject("KpTmpCissubjectView1");
    voSubj.clearCache();
    if(!genAll) 
      voSubj.setWhereClause("KpTmpKtgdoklad.ID = "+idKtgDoklad);
    else voSubj.setWhereClause("ID_CISDOKLAD = 3");
    while(voSubj.hasNext()) {
      Row rowSubj = voSubj.next();
      
      Number idKtgDoklad = (Number) rowSubj.getAttribute("Id1");
      String name = (String) rowSubj.getAttribute("SPopis");
      for(int listNr=0; listNr<2; listNr++) {
        int rowNr = (listNr==0)?4:5;
        int colNr = (listNr==0) ? colNrA : colNrOst;
        setCellValue(listNr,rowNr,colNr,name,null);

        ViewLink vl = mm.findViewLink("ViewLinkStructBunka1");
        ViewObject vo = vl.getSource();
        String where = "S_LOCALE = 'cs_CZ' AND ID_CISDOKLAD = 3 AND LISTPORADI = "+(listNr+1);
        vo.setWhereClause(where);
        vo.setOrderByClause("NL_RADEK");
        ViewObject voDetail = vl.getDestination();
        String whereDetail = "TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
        whereDetail += " AND ID_KTGDOKLAD = "+idKtgDoklad;
        voDetail.setWhereClause(whereDetail);
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          while(voDetail.hasNext()) 
          {
            Row rowDetail = voDetail.next();
            rowNr = ((Number)row.getAttribute("NlRadek")).intValue()+6;
            colNr = (listNr==0) ? colNrA : colNrOst;
            String value = (String)rowDetail.getAttribute("SymbolString");
            CellStyle style = null;
            String origValue = null;
            if(wb.getSheetAt(listNr) != null &&
               wb.getSheetAt(listNr).getRow(rowNr) != null &&
               wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr) != null) {
              origValue = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr).getStringCellValue();
            }
            if(origValue != null && origValue.length() > 0) value = origValue+'\n'+value;
            setCellValue(listNr,rowNr,colNr,value,style);
          }
        }
        vo.closeRowSet();
        voDetail.closeRowSet();
//        mm.getTransaction().commit();
        
        if(listNr==0) {
          ViewLink vlOpr = mm.findViewLink("ViewLinkStructBunkaOpr1");
          ViewObject voOpr = vl.getSource();
          voOpr.setWhereClause(where);
          voOpr.setOrderByClause("NL_RADEK");
          ViewObject voOprDetail = vlOpr.getDestination();
          String whereOprDetail = "TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
          whereOprDetail += " AND ID_KTGDOKLAD = "+idKtgDoklad;
          voOprDetail.setWhereClause(whereDetail);
          while(voOpr.hasNext()) 
          {
            Row row = voOpr.next();
            while(voOprDetail.hasNext()) 
            {
              Row rowDetail = voOprDetail.next();
              rowNr = ((Number)row.getAttribute("NlRadek")).intValue()+6;
              colNr = (listNr==0) ? colNrA+1 : colNrOst+1;
              String value = (String)rowDetail.getAttribute("SymbolString");
              CellStyle style = null;
              String origValue = null;
              if(wb.getSheetAt(listNr) != null &&
                 wb.getSheetAt(listNr).getRow(rowNr) != null &&
                 wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr) != null) {
                origValue = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr).getStringCellValue();
              }
              if(origValue != null && origValue.length() > 0) value = origValue+'\n'+value;
              setCellValue(listNr,rowNr,colNr,value,style);
            }
          }
          voOpr.closeRowSet();
          voOprDetail.closeRowSet();
//          mm.getTransaction().commit();
        }

      }
      colNrA+=2;
      colNrOst++;
//      formatSheet();
    }
    voSubj.closeRowSet();
    mm.getTransaction().commit();
    
    cleanUp();
    
    return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ESExportMustkyPodnikatel em = new ESExportMustkyPodnikatel(/*new Number(900),*/new java.sql.Date(new java.util.Date(1000,0,1).getTime()));
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