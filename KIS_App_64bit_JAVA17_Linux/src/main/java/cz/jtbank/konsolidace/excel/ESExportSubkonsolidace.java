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

public class ESExportSubkonsolidace extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSubkonsolidace.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;

  private java.sql.Date datum;

  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  private CellStyle lightBlue, styleBold, styleRed;

  private int listNr = 0;
  private int rowNr=0;
  private int colNr = 0;

  public ESExportSubkonsolidace(ApplicationModule subModule,
                               java.sql.Date datum)
  {
    dm = subModule;
    this.datum = datum;
    init();
  }

  private void init() {
    logger.info("ExportSubkonsolidace:datum="+datum);  

    setFileName ( "Subkonsolidace_"+datum+".xlsx" );
    setFileRelativeName( Constants.DIR_SUBKONSOLIDACE+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }

  private void outputListy()
  {
    CellStyle style = null;

    colNr=0;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*80));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    
    ViewObject vo = dm.findViewObject("KpKtgUcetniskupinaView1");
    vo.clearCache();
    while(vo.hasNext()) 
    {
      vo.next();
      wb.cloneSheet(0);
    }
    vo.closeRowSet();

    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String nazev = (String) row.getAttribute("SKod");
      Number idSkup = (Number) row.getAttribute("Id");

      wb.setSheetName(listNr,nazev);
      
      outputSkup(idSkup);
      
      listNr++;
    }
    vo.closeRowSet();
  }
  
  private void outputSkup(Number idSkup) {
    rowNr = 0;
    ViewObject vo = dm.findViewObject("VwKpSubkonsolidaceView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGUCETNISKUPINA = "+idSkup);
    while ( vo.hasNext() ) {
      Row row = vo.next();
      colNr = 0;
      
      String nazev = (String) row.getAttribute("SNazev");
      String mena = (String) row.getAttribute("SMena");
      String listek = (String) row.getAttribute("STypkurslistek");
      Number idSub = (Number) row.getAttribute("IdKtgucetnispolecnost");
      
      String text = nazev + " / " + mena;
      if(listek!=null) text += ", kurz. lstek: " + listek;
      setCellValue( listNr, rowNr, colNr, text , styleBold );                        
      rowNr++;
      
      outputSubkons(idSub);
    }
    vo.closeRowSet();
  }

  private void outputSubkons(Number idSub) {
    CellStyle style = null;
    ViewObject vo = dm.findViewObject("KpRelSubkonsolidaceclenView1");
    vo.clearCache();
    vo.setWhereClause("ID_KTGSUBKONSOLIDACE = "+idSub);
    ViewObject voSpol = dm.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();
    ViewObject voTc = dm.findViewObject("KpCisSubtypclenstviView1");
    voTc.clearCache();
    if(vo.hasNext()) 
    {
      colNr = 0;
      setCellValue( listNr, rowNr, colNr, "len subkonsolidace" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Typ lenstv" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Podl" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Od" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "Do" , lightBlue ); colNr++;
      setCellValue( listNr, rowNr, colNr, "len ?" , lightBlue ); colNr++;
      rowNr++;
    }
    while ( vo.hasNext() ) {
      Row row = vo.next();
      colNr = 0;
      
      Number idSpol = (Number) row.getAttribute("IdKtgucetnispolecnost");
      Number tc =  (Number) row.getAttribute("IdCissubtypclenstvi");
      oracle.jbo.domain.Date dtOd = (oracle.jbo.domain.Date)row.getAttribute("DtClenstviod");
      oracle.jbo.domain.Date dtDo = (oracle.jbo.domain.Date)row.getAttribute("DtClenstvido");
      String clen = "1".equals(row.getAttribute("CClen"))?"ANO":"NE";
      
      style = null;
      
      voSpol.setWhereClause("ID = "+idSpol);
      if(voSpol.hasNext()) {
        Row rowSpol = voSpol.next();
        setCellValue( listNr, rowNr, colNr, (String)rowSpol.getAttribute("SNazev") , style );
      }
      else 
      {
        setCellValue( listNr, rowNr, colNr, "???" , style );
      }
      voSpol.closeRowSet();
      colNr++;

      voTc.setWhereClause("ID = "+tc);
      if(voTc.hasNext()) {
        Row rowTc = voTc.next();
        setCellValue( listNr, rowNr, colNr, (String)rowTc.getAttribute("SPopis") , style );
      }
      else 
      {
        setCellValue( listNr, rowNr, colNr, "???" , style );
      }
      voTc.closeRowSet();
      colNr++;
      
      String pomer = row.getAttribute("NdPomerucasti")==null?"":(((Number)row.getAttribute("NdPomerucasti")).toString() + " %");
      setCellValue( listNr, rowNr, colNr, pomer , style );
      colNr++;
      if(dtOd!=null) setCellValue( listNr, rowNr, colNr, sdf.format(dtOd.dateValue()) , style );
      colNr++;
      if(dtDo!=null) setCellValue( listNr, rowNr, colNr, sdf.format(dtDo.dateValue()) , style );
      colNr++;
      if(dtDo!=null) setCellValue( listNr, rowNr, colNr, clen , style );
      colNr++;

      rowNr++;
    }
    vo.closeRowSet();
    rowNr++;
  }
  
  private void outputSrovnani() 
  {
    int MAX = 3;
    wb.setSheetName(listNr,"Porovnani 2-6-7");

    rowNr = 0;
    colNr = 0;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*80));
    for(int i=0; i<MAX; i++) {
      int ucSkup=2;
      if(i>0) ucSkup=5+i;
      setCellValue( listNr, rowNr, colNr, "Skupina "+ucSkup , styleBold );
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
      wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    }
    
    rowNr = 2;
    colNr = 0;
    ViewObject voSpol = dm.findViewObject("VwKpUcspolskupinaporovnaniView1");
    voSpol.clearCache();
    ViewObject vo = dm.findViewObject("VwKpUcspolskupinadataView1");
    vo.clearCache();
    CellStyle style = null;
    while ( voSpol.hasNext() ) {
      colNr = 0;
      Row rowSpol = voSpol.next();
      int idSpol = ((Number)rowSpol.getAttribute("IdKtgucetnispolecnost")).intValue();
      setCellValue( listNr, rowNr, colNr, (String)rowSpol.getAttribute("SNazev") , lightBlue );
      colNr++;
      int index[] = new int[MAX];
      
      String lastTC = null;
      java.sql.Date lastDtOd = null,
                    lastDtDo = null;
      for(int i=0; i<MAX; i++) 
      {
        int ucSkup=2;
        if(i>0) ucSkup=5+i;

        vo.setWhereClause("ID_KTGUCETNISKUPINA = "+ucSkup+" AND ID_KTGUCETNISPOLECNOST = "+idSpol);
        vo.setOrderByClause("DT_PLATNOSTOD ASC");
        if(!vo.hasNext()) 
        {
          setCellValue( listNr, rowNr, colNr, "" , styleRed );
          setCellValue( listNr, rowNr, colNr+1, "" , styleRed );
          setCellValue( listNr, rowNr, colNr+2, "" , styleRed );
        }
        while ( vo.hasNext() ) {
          Row row = vo.next();
          String typClenstvi = (String)row.getAttribute("Typclenstvi");
          java.sql.Date dtOd = ((oracle.jbo.domain.Date) row.getAttribute("DtPlatnostod")).dateValue();
          java.sql.Date dtDo = ((oracle.jbo.domain.Date) row.getAttribute("DtPlatnostdo")).dateValue();
          if(i==0) 
          {
            style = null;
            lastTC = typClenstvi;
            if(index[i]==0) lastDtOd = dtOd;
            lastDtDo = dtDo;
          }

          if(i>0 && lastTC!=null && !lastTC.equals(typClenstvi))
            style = styleRed;
          else style = null;
          setCellValue( listNr, rowNr+index[i], colNr, typClenstvi , style );
          if(i>0 && lastDtOd!=null && !lastDtOd.equals(dtOd))
            style = styleRed;
          else style = null;
          setCellValue( listNr, rowNr+index[i], colNr+1, sdf.format(dtOd) , style );
          if(i>0 && lastDtDo!=null && !lastDtDo.equals(dtDo))
            style = styleRed;
          else style = null;
          setCellValue( listNr, rowNr+index[i], colNr+2, sdf.format(dtDo) , style );        
          index[i]++;
        }
        vo.closeRowSet();
        colNr+=3;
      }
      
      int max = index[0];
      for(int i=1; i<MAX; i++) max=Math.max(max, index[i]);
      rowNr+=max;
    }
    voSpol.closeRowSet();
  }

  protected boolean outputData () 
  {
    if(lightBlue==null) {
      lightBlue = wb.createCellStyle();
      lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
      lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    if(styleRed==null) {
      styleRed = wb.createCellStyle();
      styleRed.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
      styleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    if(styleBold==null) {
      Font font = wb.createFont();
      font.setFontHeightInPoints((short) 14);
      font.setBold(true);
      styleBold = wb.createCellStyle();
      styleBold.setFont(font);
    }

    outputListy();
    
    outputSrovnani();
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.subkons.SubkonsModule","SubkonsModuleLocal");
      ESExportSubkonsolidace ed = new ESExportSubkonsolidace(dm,
                                                             new java.sql.Date(System.currentTimeMillis()));
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
