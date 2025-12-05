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
import cz.jtbank.konsolidace.projekt.common.ProjektModule;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportProtiGroup extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProtiGroup.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  private CellStyle lightBlue;

  public ESExportProtiGroup(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportProtiGroup:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_PROTI_GROUP;
    init();
  }

  private void init() {
    setFileName ( "ProtiGroup_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputProtiGroup() {
    int listNr=0;
    int rowNr=0;
    int colNr=0;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*70));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    
    ViewObject voGr = dm.findViewObject("KpCisCustomerGroupView1");
    voGr.clearCache();
    voGr.setWhereClause("TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(voGr.hasNext()) 
    {
      colNr=0;
      Row rowGr = voGr.next();
      String curGrCode = (String)rowGr.getAttribute("CustGroupCode");
      String curStCode = (String)rowGr.getAttribute("CustStrucCode");
      String curGrName = (String)rowGr.getAttribute("CustGroupName");
      String curStName = (String)rowGr.getAttribute("CustStrucName");
      
      setCellValue( listNr, rowNr, colNr, curGrCode+" - "+curGrName+" ("+curStCode+" - "+curStName+")" , lightBlue ); 
      for(int i=0;i<2;i++) {
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)null , lightBlue ); 
      }
  
      rowNr++;
      
      String where = "CUST_GROUP_CODE = '" + curGrCode + "' AND CUST_STRUC_CODE = '" + curStCode +"' AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
      ViewObject vo = dm.findViewObject("KpKtgSpolecnostGroupView1");
      vo.clearCache();
      vo.setWhereClause(where);
      vo.setOrderByClause("S_NAZEV");
      while(vo.hasNext()) 
      {
        colNr = 0;    
        Row row = vo.next();

        String psName = (String) row.getAttribute("SNazev");
        oracle.jbo.domain.Date hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostod");
        String dtOd = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostdo");
        String dtDo = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
        
        setCellValue( listNr, rowNr, colNr, psName , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, dtOd , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, dtDo , null ); 
  
        rowNr++;
      }
      vo.closeRowSet();
      
      rowNr++;
    }
    voGr.closeRowSet();
    dm.getTransaction().commit();
  }

  private void outputProtiGroup2() {
    int listNr=1;
    int rowNr=0;
    int colNr=0;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Kd sk." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Nzev sk." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*6));
    setCellValue( listNr, rowNr, colNr, "Kd es.sk." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Nzev es.sk." , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Od" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr, "Do" , lightBlue ); colNr++;
    
    rowNr++;

    ViewObject voGr = dm.findViewObject("KpCisCustomerGroupView1");
    voGr.clearCache();
    voGr.setWhereClause("TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(voGr.hasNext()) 
    {
      colNr=0;
      Row rowGr = voGr.next();
      String curGrCode = (String)rowGr.getAttribute("CustGroupCode");
      String curStCode = (String)rowGr.getAttribute("CustStrucCode");
      String curGrName = (String)rowGr.getAttribute("CustGroupName");
      String curStName = (String)rowGr.getAttribute("CustStrucName");
      
      String where = "CUST_GROUP_CODE = '" + curGrCode + "' AND CUST_STRUC_CODE = '" + curStCode +"' AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
      ViewObject vo = dm.findViewObject("KpKtgSpolecnostGroupView1");
      vo.clearCache();
      vo.setWhereClause(where);
      vo.setOrderByClause("S_NAZEV");
      while(vo.hasNext()) 
      {
        colNr = 0;    
        Row row = vo.next();

        String psName = (String) row.getAttribute("SNazev");
        oracle.jbo.domain.Date hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostod");
        String dtOd = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPlatnostdo");
        String dtDo = hlpDt==null ? "" : sdf.format(hlpDt.dateValue());
        
        setCellValue( listNr, rowNr, colNr, curGrCode , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, curGrName , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, curStCode , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, curStName , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, psName , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, dtOd , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, dtDo , null ); 
  
        rowNr++;
      }
      vo.closeRowSet();
    }
    voGr.closeRowSet();
    dm.getTransaction().commit();
  }

  protected boolean outputData () 
  {
    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    
    wb.cloneSheet(0);
    wb.setSheetName(1,"Pro filtraci");
  
    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();    
    outputProtiGroup();
    end = System.currentTimeMillis();    
    logger.debug("ProtiGroup:"+((end-start)/1000.0)+"s");
    start = System.currentTimeMillis();    
    outputProtiGroup2();
    end = System.currentTimeMillis();    
    logger.debug("ProtiGroup2:"+((end-start)/1000.0)+"s");
    
    return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.protistrany.ProtistranyModule","ProtistranyModuleLocal");
//      ESExportProtiGroup ed = new ESExportProtiGroup(dm,new java.sql.Date(System.currentTimeMillis()));
      ESExportProtiGroup ed = new ESExportProtiGroup(dm,new java.sql.Date(106,10,30));
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
