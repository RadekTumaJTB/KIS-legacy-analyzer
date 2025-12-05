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

public class ESExportIfrs extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportIfrs.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  public ESExportIfrs(ApplicationModule dokladyModule,
                        java.sql.Date datum)
  {
    logger.info("ESExportIfrs:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum; //prozatim neni potreba. prozatim :-)
    dir = Constants.DIR_IFRS_ZMENY;
    init();
  }

  private void init() {
    if(datum != null) 
      setFileName ( "IFRSZmeny_"+datum+".xlsx" );
    else
      setFileName ( "IFRSZmeny_Historie.xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputIfrs() {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    int listNr=0;
    int rowNr=0;
    int colNr=0;

    CellStyle lightBlue = wb.createCellStyle();
    //lightBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*2));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*3));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*3));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*25));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*15));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*100));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));

    colNr=0;
    setCellValue( listNr, rowNr, colNr, "" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Poad" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Poad detail" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Platnost od" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Platnost do" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "et m dti" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "et dal" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Typ" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "stka" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "stka lokl" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "et zbytek" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Podmnka" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Poznmka" , lightBlue ); colNr++;
    setCellValue( listNr, rowNr, colNr, "Spol. ref." , lightBlue ); 
    
    rowNr++;
  
    ViewObject vo = dm.findViewObject("ViewJtSpol1");
    vo.clearCache();
    ViewObject voIfrs = dm.findViewObject("VwJtSpolecnostpravidlaifrsView1");
    voIfrs.clearCache();
    ViewObject voIfrsDetail = dm.findViewObject("VwJtSpolecnostpravifrsdetailView1");
    voIfrsDetail.clearCache();
    ViewObject voRef = dm.findViewObject("ViewJtSpol2");
    voRef.clearCache();
    vo.setWhereClause("ID_KATEGORIE<>300");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("Id");
      String nazev = (String)row.getAttribute("SNazev");
      String whereIfrsP = "ID_KTGUCETNISPOLECNOST = "+id;
      if(datum != null)
        whereIfrsP += " AND TO_DATE('"+sdf.format(datum)+"','DD.MM.YYYY') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
      voIfrs.setWhereClause(whereIfrsP);
      while(voIfrs.hasNext()) 
      {
        Row rowIfrs = voIfrs.next();
        Number idIfrs = (Number)rowIfrs.getAttribute("Id");
        Number poradi = (Number)rowIfrs.getAttribute("IdPoradi");
        String poznamka = (String)rowIfrs.getAttribute("SPoznamka");
        String typ = (String)rowIfrs.getAttribute("SZkratka");
        Date hlp = (Date)rowIfrs.getAttribute("DtPlatnostod");
        String platnostOd = hlp==null?"":sdf.format(hlp.dateValue());
        hlp = (Date)rowIfrs.getAttribute("DtPlatnostdo");
        String platnostDo = hlp==null?"":sdf.format(hlp.dateValue());
        String validaceUcet = (String)rowIfrs.getAttribute("SValidaceucet");
        Number validaceHodnota = (Number)rowIfrs.getAttribute("NdValidacehodnota");
        
        voIfrsDetail.setWhereClause("ID_KTGIFRSPRAVIDLO = "+idIfrs);
        while(voIfrsDetail.hasNext()) 
        {
          Row rowIfrsDetail = voIfrsDetail.next();
          
          Number poradiDetail = (Number)rowIfrsDetail.getAttribute("IdPoradi");
          String poznamkaDetail = (String)rowIfrsDetail.getAttribute("SPoznamka");
          String typDetail = (String)rowIfrsDetail.getAttribute("SZkratka");
          String ucetMd = (String)rowIfrsDetail.getAttribute("SUcetmd");
          String ucetD = (String)rowIfrsDetail.getAttribute("SUcetd");
          String mena = (String)rowIfrsDetail.getAttribute("SMena");
          Number castka = (Number)rowIfrsDetail.getAttribute("NdCastka");
          Number castkaLocal = (Number)rowIfrsDetail.getAttribute("NdCastkalocal");
          String ucetZbytek = (String)rowIfrsDetail.getAttribute("SUcetzbytek");
          String podminka = (String)rowIfrsDetail.getAttribute("SPodminka");
          //orisek uc. spol.
          Number refSpol = (Number)rowIfrsDetail.getAttribute("IdKtgucetnispolecnost");
          String spolRef = "";
          voRef.setWhereClause("ID = "+refSpol);
          if(voRef.hasNext()) {
            Row rowRef = voRef.next();
            spolRef = (String)rowRef.getAttribute("SNazev");
          }
          voRef.closeRowSet();

          colNr = 0;
          
          setCellValue( listNr, rowNr, colNr, "I" , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (poradi==null?"":poradi.toString()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (poradiDetail==null?"":poradiDetail.toString()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, nazev , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, platnostOd , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, platnostDo , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, ucetMd , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, ucetD , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, typDetail , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (castka==null?0.0:castka.doubleValue()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, mena , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, (castkaLocal==null?0.0:castkaLocal.doubleValue()) , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, ucetZbytek , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, podminka , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, poznamkaDetail , null ); 
          colNr++;
          setCellValue( listNr, rowNr, colNr, spolRef , null ); 
          
          rowNr++;
        }
        voIfrsDetail.closeRowSet();

        colNr = 0;        
        setCellValue( listNr, rowNr, colNr, "I" , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (poradi==null?"":poradi.toString()) , null ); 
        colNr++;
        colNr++;
        setCellValue( listNr, rowNr, colNr, nazev , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, platnostOd , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, platnostDo , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, validaceUcet , null ); 
        colNr++;
        colNr++;
        setCellValue( listNr, rowNr, colNr, typ , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (validaceHodnota==null?0.0:validaceHodnota.doubleValue()) , null ); 
        colNr++;
        colNr++;
        colNr++;
        colNr++;
        colNr++;
        setCellValue( listNr, rowNr, colNr, poznamka , null ); 
        colNr++;

        rowNr++;
      }
      voIfrs.closeRowSet();
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputIfrs();
      end = System.currentTimeMillis();    
      logger.debug("ifrs zmeny:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.ifrs.IfrsModule","IfrsModuleLocal");
      ESExportIfrs ed = new ESExportIfrs(dm,null);
                                         //new java.sql.Date(104,8,30));
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
