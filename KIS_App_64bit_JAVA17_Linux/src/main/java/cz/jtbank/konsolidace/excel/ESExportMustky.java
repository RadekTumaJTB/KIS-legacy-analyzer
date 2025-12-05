package cz.jtbank.konsolidace.excel;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import cz.jtbank.konsolidace.common.Constants;
import java.text.SimpleDateFormat;

public class ESExportMustky extends AbsExcelDoklad 
{
  private Number idKtgDoklad;
  private java.sql.Date datum;
//  private String name;
  private boolean genAll = false;
  private int colNrA = 2,
              colNrOst = 2;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportMustky(Number idKtgDoklad, java.sql.Date datum)
  {
    this.idKtgDoklad = idKtgDoklad;
    this.datum = datum;
    init();
  }

  public ESExportMustky(java.sql.Date datum)
  {
    genAll = true;
    this.datum = datum;
    init();
  }

  private void init() {
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaMustek.xlsx" );
    setFileName ( Constants.DIR_MUSTKY + "Mustek_"+datum+".xlsx" );
    setFileRelativeName( getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }
/*
  private void formatSheet()
  {
    int sheetNr, rowNr, colNr;
    CellStyle style = null;

    setCellValue(0,4,2,name,style);
    setCellValue(1,5,2,name,style);
    setCellValue(2,5,2,name,style);
    setCellValue(3,5,2,name,style);
    setCellValue(4,6,2,name,style);

    sheetNr = 0;
    for(colNr=4; colNr<16; colNr++) 
    {
      for(rowNr=0; rowNr<200; rowNr++) 
      {
        clearCell(sheetNr,rowNr,colNr);
      }
    }
    for(sheetNr=1;sheetNr<5;sheetNr++) 
    {
      for(colNr=3; colNr<9; colNr++) 
      {
        for(rowNr=0; rowNr<200; rowNr++) 
        {
          clearCell(sheetNr,rowNr,colNr);
        }
      }
    }
  }
*/

  private void cleanUp() 
  {
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
    else voSubj.setWhereClause("ID_CISDOKLAD = 1");
    while(voSubj.hasNext()) {
      Row rowSubj = voSubj.next();
      
      Number idKtgDoklad = (Number) rowSubj.getAttribute("Id1");
      String name = (String) rowSubj.getAttribute("SPopis");
      for(int listNr=0; listNr<5; listNr++) {
        int rowNr = (listNr==0)?4:5;
        int colNr = (listNr==0) ? colNrA : colNrOst;
        setCellValue(listNr,rowNr,colNr,name,null);

        ViewLink vl = mm.findViewLink("ViewLinkStructBunka1");
        ViewObject vo = vl.getSource();
        String where = "S_LOCALE = 'cs_CZ' AND ID_CISDOKLAD = 1 AND LISTPORADI = "+(listNr+1);
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

/*
  protected boolean outputDataOld () 
  {
    ApplicationModule mm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.mustky.MustkyModule","MustkyModuleLocal");

    if(!genAll) {
      ViewObject voPopis = mm.findViewObject("KpTmpCissubjectView1");
      voPopis.clearCache();
      voPopis.setWhereClause("KpTmpKtgdoklad.ID = "+idKtgDoklad);
      if(voPopis.hasNext()) {
        Row firstRow = voPopis.next();
        name = (String) firstRow.getAttribute("SPopis");
        for(int i=0; i<5; i++) {
          int rowNr = (i==0)?2:1;
          CellStyle style = null;
          setCellValue(i,rowNr,0,name,style);
        }
      }
      voPopis.closeRowSet();
      mm.getTransaction().commit();
      formatSheet();
    }
      
    ViewLink vl = mm.findViewLink("ViewLinkStructBunka1");
    ViewObject vo = vl.getSource();
    String where = "S_LOCALE = 'cs_CZ' AND ID_CISDOKLAD = 1";
    vo.setWhereClause(where);
    vo.setOrderByClause("LISTPORADI,NL_RADEK");
    ViewObject voDetail = vl.getDestination();
    String whereDetail = "TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
    if(!genAll)
      whereDetail += "ID_KTGDOKLAD = "+idKtgDoklad;
    voDetail.setWhereClause(whereDetail);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      while(voDetail.hasNext()) 
      {
        Row rowDetail = voDetail.next();
        int listNr = ((Number)row.getAttribute("Listporadi")).intValue()-1;
        int rowNr = ((Number)row.getAttribute("NlRadek")).intValue()+6;
        int idKtgDoklad = ((Number)rowDetail.getAttribute("IdKtgdoklad")).intValue();
        int colNr = getColNr(idKtgDoklad,0,listNr);
        int columnNr = !genAll?2:colNr;
        String value = (String)rowDetail.getAttribute("SymbolString");
        CellStyle style = null;
        String origValue = null;
        if(wb.getSheetAt(listNr) != null &&
           wb.getSheetAt(listNr).getRow(rowNr) != null &&
           wb.getSheetAt(listNr).getRow(rowNr).getCell((short)columnNr) != null) {
          origValue = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)columnNr).getStringCellValue();
        }
        if(origValue != null && origValue.length() > 0) value = origValue+'\n'+value;
        setCellValue(listNr,rowNr,columnNr,value,style);
      }
    }
    vo.closeRowSet();
    voDetail.closeRowSet();
    mm.getTransaction().commit();
    
    ViewLink vlOpr = mm.findViewLink("ViewLinkStructBunkaOpr1");
    ViewObject voOpr = vl.getSource();
    voOpr.setWhereClause(where);
    voOpr.setOrderByClause("LISTPORADI,NL_RADEK");
    ViewObject voOprDetail = vlOpr.getDestination();
    String whereOprDetail = "TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
    if(!genAll)
      whereOprDetail += "ID_KTGDOKLAD = "+idKtgDoklad;
    voOprDetail.setWhereClause(whereDetail);
    while(voOpr.hasNext()) 
    {
      Row row = voOpr.next();
      while(voOprDetail.hasNext()) 
      {
        Row rowDetail = voOprDetail.next();
        int listNr = ((Number)row.getAttribute("Listporadi")).intValue()-1;
        int rowNr = ((Number)row.getAttribute("NlRadek")).intValue()+6;
        int idKtgDoklad = ((Number)rowDetail.getAttribute("IdKtgdoklad")).intValue();
        int colNr = getColNr(idKtgDoklad,1,listNr);
        int columnNr = !genAll?3:colNr;
        String value = (String)rowDetail.getAttribute("SymbolString");
        CellStyle style = null;
        String origValue = null;
        if(wb.getSheetAt(listNr) != null &&
           wb.getSheetAt(listNr).getRow(rowNr) != null &&
           wb.getSheetAt(listNr).getRow(rowNr).getCell((short)columnNr) != null) {
          origValue = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)columnNr).getStringCellValue();
        }
        if(origValue != null && origValue.length() > 0) value = origValue+'\n'+value;
        setCellValue(listNr,rowNr,columnNr,value,style);
      }
    }
    voOpr.closeRowSet();
    voOprDetail.closeRowSet();
    mm.getTransaction().commit();
    
    return true;
  }
*/
  private int getColNr(int idKtgdoklad,int move,int listNr) 
  {
    int retVal = 100;
    switch(idKtgdoklad) 
    {
      case 900:
        retVal = 1;
        break;
      case 901:
        retVal = 3;
        break;
      case 902:
        retVal = 2;
        break;
      case 903:
        retVal = 4;
        break;
      case 904:
        retVal = 5;
        break;
      case 905:
        retVal = 6;
        break;
      case 906:
        retVal = 7;
        break;
      case 907:
        retVal = 8;
        break;
      case 908:
        retVal = 9;
        break;
      case 909:
        retVal = 10;
        break;
    }
    
    if(listNr == 0) retVal *= 2;
    else retVal += 1;
    
    return retVal+move;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ESExportMustky em = new ESExportMustky(/*new Number(900),*/new java.sql.Date(new java.util.Date(107,0,1).getTime()));
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