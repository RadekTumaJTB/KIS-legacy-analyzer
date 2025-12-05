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
import org.apache.poi.ss.usermodel.Cell;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportZamekGen extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportZamekGen.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private boolean specific;
  
  private CellStyle lightGray;
  private CellStyle lightBlue;
  private CellStyle green,yellow,red,orange;
  
  private SimpleDateFormat sdfHeader = new SimpleDateFormat("yyyy-MM-dd");
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss");
  
  public ESExportZamekGen(ApplicationModule dokladyModule, boolean specific)
  {
    dm = dokladyModule;
    this.specific = specific;
    init();
  }

  private void init() {
    logger.info("ExportZamekGen");  

    setFileName ( "PrehledZamekGen_"+sdfHeader.format(new java.util.Date())+(specific?"_Specific":"")+".xlsx" );
    setFileRelativeName( Constants.DIR_ZAMEK_GENEROVANI+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaPrehledZamek.xlsx" );
  }

  private void outputDetail()
  {
    int listNr=0;
    int rowNr=0;
    int colNr = 0;
    CellStyle style = null;

    wb.setSheetName(listNr, "Schvalovani dokladu");

    wb.getSheetAt(listNr).setColumnWidth((short)0,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)1,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)2,(short)(256*10));
    wb.getSheetAt(listNr).setColumnWidth((short)3,(short)(256*10));

    setCellValue( listNr, 1, 0, "Spolenost" , lightGray );
    setCellValue( listNr, 1, 1, "On-line" , lightGray );
    setCellValue( listNr, 1, 2, "Kategorie" , lightGray );
    setCellValue( listNr, 1, 3, "Admin" , lightGray );
    
    List data = new ArrayList();
    ViewObject voDt = dm.findViewObject("VwKpSpolecnostzamekgendatumView1");
    voDt.clearCache();
    //voDt.setWhereClause("DT_DATUM > add_months(SYSDATE,-5)");
    //voDt.setWhereClause("DT_DATUM > add_months(SYSDATE,-6)");  esc 07/2010
    voDt.setWhereClause("DT_DATUM > add_months(SYSDATE,-7)");
    while(voDt.hasNext()) 
    {
      Row row = voDt.next();
      String dt = sdfHeader.format(((oracle.jbo.domain.Date)row.getAttribute("DtDatum")).dateValue());
      data.add(dt);
    }
    voDt.closeRowSet();

    ViewObject voSpol = dm.findViewObject("KpKtgUcetnispolecnostExtendedView1");
    voSpol.clearCache();

    ViewObject vo = dm.findViewObject("VwKpSpolecnostzamekgenView1");
    vo.clearCache();
 
    boolean first = true;
    colNr=4;
    Iterator iter = data.iterator();
    while(iter.hasNext()) 
    {
      rowNr=0;

      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+1),(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+2),(short)(256*4));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+3),(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+4),(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+5),(short)(256*4));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+6),(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+7),(short)(256*20));
      wb.getSheetAt(listNr).setColumnWidth((short)(colNr+8),(short)(256*4));

      String dt = (String) iter.next();
      setCellValue( listNr, rowNr++, colNr, dt , lightBlue );
      setCellValue( listNr, rowNr, colNr, "Z" , lightGray );
      setCellValue( listNr, rowNr, colNr+1, "Datum schvlen Z" , lightGray );
      setCellValue( listNr, rowNr, colNr+2, "+/-" , lightGray );
      setCellValue( listNr, rowNr, colNr+3, "OO" , lightGray );
      setCellValue( listNr, rowNr, colNr+4, "Datum schvlen OO" , lightGray );
      setCellValue( listNr, rowNr, colNr+5, "+/-" , lightGray );
      setCellValue( listNr, rowNr, colNr+6, "OOH" , lightGray );
      setCellValue( listNr, rowNr, colNr+7, "Datum schvlen OOH" , lightGray );
      setCellValue( listNr, rowNr, colNr+8, "+/-" , lightGray );
      rowNr++;
      
      String whereSpol = "id_kategorie <> 300 and DT_DATUMARCHIVACE is null and id_ciskatspol <> 3 and KpKtgUcetnispolecnost.ID not between 6041 and 6044 and c_extsystem <> 'M'";
      if(specific) whereSpol += " and id_ciskatspol in "+Constants.DIS_CISKATSPOL;
      else whereSpol += " and id_ciskatspol not in "+Constants.DIS_CISKATSPOL;
      voSpol.setWhereClause(whereSpol);
      voSpol.setOrderByClause("KpKtgUcetnispolecnost.S_NAZEV");
      while(voSpol.hasNext()) 
      {
        Row rowSpol = voSpol.next();
        Number idSpol = (Number) rowSpol.getAttribute("Id");
        if(first) 
        {
          String nazev = (String) rowSpol.getAttribute("SNazev");
          String online = "1".equals(rowSpol.getAttribute("COnline")) ? "ANO":"NE";
          String katSpol = (String)rowSpol.getAttribute("Katspol");
          Number idAdm = (Number)rowSpol.getAttribute("IdAdministrator");
          String admin = (idAdm==null||idAdm.intValue()==231) ? "NE":"ANO";
          setCellValue( listNr, rowNr, 0, nazev , lightBlue );
          setCellValue( listNr, rowNr, 1, online , lightGray );
          setCellValue( listNr, rowNr, 2, katSpol , lightBlue );
          setCellValue( listNr, rowNr, 3, admin , lightGray );
        }
        
        int myRowNr = rowNr;
        for(int i=0; i<=1; i++) {
          vo.setWhereClause("DT_DATUM = TO_DATE('"+dt+"','yyyy-mm-dd') AND ID = "+idSpol+
                            " AND NL_KROK="+(i+1));
          while(vo.hasNext()) 
          {
            Row row = vo.next();
            String zu = (String)row.getAttribute("Zodpovednaucetni");
            String oo = (String)row.getAttribute("Odpovednaosoba");
            String top = (String)row.getAttribute("Topmng");
            boolean vcasZu = "1".equals(row.getAttribute("Ucetnivlimitu"));
            boolean vcasOo = "1".equals(row.getAttribute("Oovlimitu"));
            boolean vcasTop = "1".equals(row.getAttribute("Topvlimitu"));
            oracle.jbo.domain.Date dtZuAno = (oracle.jbo.domain.Date) row.getAttribute("Schvalenoucetni");
            oracle.jbo.domain.Date dtZuNe = (oracle.jbo.domain.Date) row.getAttribute("Zamitnutoucetni");
            oracle.jbo.domain.Date dtOoAno = (oracle.jbo.domain.Date) row.getAttribute("Schvalenooo");
            oracle.jbo.domain.Date dtOoNe = (oracle.jbo.domain.Date) row.getAttribute("Zamitnutooo");
            oracle.jbo.domain.Date dtTopAno = (oracle.jbo.domain.Date) row.getAttribute("Schvalenotop");
            oracle.jbo.domain.Date dtTopNe = (oracle.jbo.domain.Date) row.getAttribute("Zamitnutotop");
            Number rozdilUcetni = (Number) row.getAttribute("Rozdilucetni");
            Number rozdilOo = (Number) row.getAttribute("Rozdiloo");
            Number rozdilTop = (Number) row.getAttribute("Rozdiltop");
            
            int myColNr = colNr;
  
            if(zu!=null) setCellValue( listNr, myRowNr, myColNr, zu , null );
            myColNr++;
            if(dtZuNe!=null) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtZuNe.timestampValue().getTime())), red );
            else if(vcasZu) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtZuAno.timestampValue().getTime())), green );
            else if(dtZuAno!=null) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtZuAno.timestampValue().getTime())), yellow );
            else setCellValue( listNr, myRowNr, myColNr, "-", yellow );
            myColNr++;
            if(rozdilUcetni!=null) setCellValue( listNr, myRowNr, myColNr, ""+rozdilUcetni , (rozdilUcetni.intValue()>0?orange:green) );
            myColNr++;
  
            if(oo!=null) setCellValue( listNr, myRowNr, myColNr, oo , null );
            myColNr++;
            if(dtOoNe!=null) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtOoNe.timestampValue().getTime())), red );
            else if(vcasOo) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtOoAno.timestampValue().getTime())), green );
            else if(dtOoAno!=null) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtOoAno.timestampValue().getTime())), yellow );
            else setCellValue( listNr, myRowNr, myColNr, "-", yellow );
            myColNr++;
            if(rozdilOo!=null) setCellValue( listNr, myRowNr, myColNr, ""+rozdilOo , (rozdilOo.intValue()>0?orange:green) );
            myColNr++;
  
            if(top!=null) setCellValue( listNr, myRowNr, myColNr, top, null );
            myColNr++;
            if(dtTopNe!=null) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtTopNe.timestampValue().getTime())), red );
            else if(vcasTop) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtTopAno.timestampValue().getTime())), green );
            else if(dtTopAno!=null) setCellValue( listNr, myRowNr, myColNr, sdf.format(new java.util.Date(dtTopAno.timestampValue().getTime())), yellow );
            else setCellValue( listNr, myRowNr, myColNr, "-", yellow );
            myColNr++;
            if(rozdilTop!=null) setCellValue( listNr, myRowNr, myColNr, ""+rozdilTop , (rozdilTop.intValue()>0?orange:green) );
            myColNr++;
            
            myRowNr++;
          }
          vo.closeRowSet();
        }
        
        rowNr+=2;
      }
      voSpol.closeRowSet();
      first = false;
      
      colNr+=9;
    }

    dm.getTransaction().commit();
    
    for(int y=1; y<rowNr; y++) 
    {
      if(wb.getSheetAt(listNr).getRow(y)==null || wb.getSheetAt(listNr).getRow(y).getPhysicalNumberOfCells()==0) 
      {
        wb.getSheetAt(listNr).shiftRows(y+1, rowNr, -1);
//System.out.println("y+1="+(y+1)+",rowNr="+rowNr);
      }
    }
  }

  protected boolean outputData () 
  {
    lightGray = wb.createCellStyle();
    lightGray.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightGray.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    green = wb.createCellStyle();
    green.setFillForegroundColor(IndexedColors.GREEN.getIndex());
    green.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    yellow = wb.createCellStyle();
    yellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    yellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    red = wb.createCellStyle();
    red.setFillForegroundColor(IndexedColors.RED.getIndex());
    red.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    orange = wb.createCellStyle();
    orange.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
    orange.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    outputDetail();
    
    return true;
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
/*
      ESExportZamekGen ed = new ESExportZamekGen(dm,
                                               "224720",
                                               "cs_CZ",
                                               0);
*/
      ESExportZamekGen ed = new ESExportZamekGen(dm, false);
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
