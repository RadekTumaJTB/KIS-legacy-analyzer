package cz.jtbank.konsolidace.excel;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import cz.jtbank.konsolidace.common.Constants;
import java.text.SimpleDateFormat;
import java.util.*;

public class ESExportMustky2007 extends AbsExcelDoklad 
{
  private Number idCisDoklad;
  private java.sql.Date datum;
  private String locale;
  private boolean genAll = false;
  private int colNrA = 2,
              colNrOst = 2;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportMustky2007(Number idCisDoklad, java.sql.Date datum, String locale)
  {
    this.idCisDoklad = idCisDoklad;
    this.datum = datum;
    this.locale = locale;
    init();
  }

  private void init() {
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaMustek2007.xlsx" );
    setFileName ( Constants.DIR_MUSTKY + "Mustek2007-"+idCisDoklad+"_"+datum+".xlsx" );
    setFileRelativeName( getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }

  protected boolean outputData () 
  {
    ApplicationModule mm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.mustky.MustkyModule","MustkyModuleLocal");

    setCellValue(0,0,0,sdf.format(datum),null);
    
    Map sub = new LinkedHashMap();
    ViewObject voSubj = mm.findViewObject("ListDokladView1");
    voSubj.clearCache();
    voSubj.setWhereClause("ID_CISDOKLAD = "+idCisDoklad);
    voSubj.setOrderByClause("KpTmpDefdoklad.ID");
    while(voSubj.hasNext()) {
      Row rowSubj = voSubj.next();
      Number idKtgDoklad = (Number) rowSubj.getAttribute("Id1");
      String nazev = (String) rowSubj.getAttribute("SZkratka");
      sub.put(idKtgDoklad,nazev);
      if(voSubj.hasNext()) wb.cloneSheet(0);
    }
    voSubj.closeRowSet();
    int listNr=0;


    ViewObject vo = mm.findViewObject("VwKpBilanceDokladStrukturaView1");
    vo.clearCache();
    ViewObject voDetail = mm.findViewObject("VwKpBunkadefiniceuctuView1");
    voDetail.clearCache();
    Iterator iter = sub.keySet().iterator();
    while(iter.hasNext()) 
    {
      Number id = (Number) iter.next();
      String nazev = (String) sub.get(id);
      wb.setSheetName(listNr,nazev);
      
      int rowNr = 1;
      int colNr = 0;

      String where = "S_LOCALE = '"+locale+"' AND ID_CISDOKLAD = "+idCisDoklad+" AND LISTPORADI = "+(listNr+1);
      vo.setWhereClause(where);
      vo.setOrderByClause("NL_RADEK");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        setCellValue(listNr,rowNr,0,row.getAttribute("NlRadek").toString(),null/*TBC*/);
        setCellValue(listNr,rowNr,1,(String)row.getAttribute("Radektext"),null/*TBC*/);
        
        Number idBunka = (Number) row.getAttribute("IdTmpktgdeflistbunka");
        Number idOpravna = (Number) row.getAttribute("IdTmpktgdeflistbunkaopravna");
        
        colNr = 2;
        String whereDetail = "ID_DEFLISTBUNKA = "+idBunka+" AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
        voDetail.setWhereClause(whereDetail);
        while(voDetail.hasNext()) 
        {
          Row rowDetail = voDetail.next();
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
        voDetail.closeRowSet();
        colNr++;
        whereDetail = "ID_DEFLISTBUNKA = "+idOpravna+" AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
        voDetail.setWhereClause(whereDetail);
        while(voDetail.hasNext()) 
        {
          Row rowDetail = voDetail.next();
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
        voDetail.closeRowSet();

        rowNr++;
      }
      vo.closeRowSet();
      
      for(;rowNr<201;rowNr++) 
        for(colNr=0;colNr<4;colNr++)
          clearCell(listNr,rowNr,colNr);
      
      listNr++;
    }
    mm.getTransaction().commit();
    
    return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ESExportMustky2007 em = new ESExportMustky2007(new Number(100),new java.sql.Date(new java.util.Date(1000,0,1).getTime()),"cs_CZ");
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