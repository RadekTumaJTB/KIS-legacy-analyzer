package cz.jtbank.konsolidace.excel;

import cz.jtbank.konsolidace.common.*;
import cz.jtbank.konsolidace.majetek.common.*;
import java.io.*;
import java.text.*;
import java.util.*;
import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.domain.Date;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportMajetek extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportMajetek.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;
  private int co;

  private String dir;

  private CellStyle styleBold;
  private CellStyle yellow, lightBlue, red;
  
  private java.text.NumberFormat nf;

  public ESExportMajetek(ApplicationModule dokladyModule,
                        java.sql.Date datum,
                        int co)
  {
    logger.info("ESExportMajetek:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    this.co = co;
    dir = Constants.DIR_MAJETKOVE_UCASTI;
    nf = java.text.NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);
    init();
  }

  private void init() {
    String spec = "";
    if(co==1) spec="Specific";
    if(co==2) spec="Nespecific";
    setFileName ( "MajetkoveUcasti"+spec+"_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private double getPodilInvestice(Number idSpol, Number idProti) {
    double ret = 0.0;
    try {
      ret = ((MajetekModule)dm).getPodilInvestice(idSpol.intValue(), idProti.intValue(), datum);
    }
    catch(KisException e) {}
    return 100*ret;
  }
  
  private void outputMajetkoveUcasti() {
    wb.setSheetName(0,"Majetkove ucasti");
  
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    HashMap map = ((MajetekModule) dm).getEmiseMap(sdf.format(datum));
    java.util.Set set = ((MajetekModule) dm).getTypNakupProdej();
    
    int listNr=0;
    int rowNr=0;
    int colNr=0;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*8));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*8));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*11));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*5));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*20));

    colNr=0;

    ViewObject vo = dm.findViewObject("ViewJtSpol1");
    vo.clearCache();
    String whereSpol = "ID_KATEGORIE<>300";
    if(co == 1) {
      whereSpol += " AND ID_CISKATSPOL IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL;
    }
    else if(co == 2) {
      whereSpol += " AND ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL;
    }
    vo.setWhereClause(whereSpol);
    ViewObject voMajetek = dm.findViewObject("VwKpMajetkovaucastView1");
    voMajetek.clearCache();
    ViewObject voEmise = dm.findViewObject("VwFinancniinvemiseView1");
    voEmise.clearCache();
    ViewObject voTyp = dm.findViewObject("KpCisMajetkovaucasttyptranView1");
    voTyp.clearCache();
    ViewObject voZpusob = dm.findViewObject("KpCisMajetkovaucastzpusobView1");
    voZpusob.clearCache();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("Id");
      String whereMajetek = "ID_KTGUCETNISPOLECNOST = " + id +
                            " AND TO_DATE('"+sdf.format(datum)+"','DD.MM.YYYY') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO";
      voMajetek.setWhereClause(whereMajetek);
      boolean header = true;
      String nazevInvGlobal = null;
      Number lastIdProti = null;
      double sumaLocal = 0.0, sumaOrig = 0.0, celkem = 0.0;
      while(voMajetek.hasNext()) 
      {
        if(header) 
        {
          if(styleBold == null) 
          {
            Font font = wb.createFont();
            font.setBold(true);
            styleBold = wb.createCellStyle();
            styleBold.setFont(font);
          }
          String hlavicka = (String)row.getAttribute("SNazev") + " (" + 
                            (String)row.getAttribute("SMena") + ")";
          setCellValue( listNr, rowNr, 0, hlavicka, styleBold );
          rowNr++;

          colNr = 1;
          header = false;
          setCellValue( listNr, rowNr, colNr, "Podl z ceklu", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Podl z emise", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Emise", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Od", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Do", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "et", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Ks", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Typ", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Zpsob", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Mna trans.", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Cena za ks originl", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Celkem originl", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Kurz", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Mna .", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Cena za ks lokl", lightBlue ); colNr++;
          setCellValue( listNr, rowNr, colNr, "Celkem lokl", lightBlue ); colNr++;
          rowNr++;
        }

        Row rowMajetek = voMajetek.next();

        Number idProti = (Number) rowMajetek.getAttribute("IdSpolinvestice");

        String platnostOd = rowMajetek.getAttribute("DtPlatnostod")==null?null:
          sdf.format(((oracle.jbo.domain.Date) rowMajetek.getAttribute("DtPlatnostod")).dateValue());
        String platnostDo = rowMajetek.getAttribute("DtPlatnostdo")==null?null:
          sdf.format(((oracle.jbo.domain.Date) rowMajetek.getAttribute("DtPlatnostdo")).dateValue());
        Number typSel = (Number) rowMajetek.getAttribute("IdCismutyptransakce");
        Number zpusobSel = (Number) rowMajetek.getAttribute("IdCismuzpusob");

        //boolean isNakupProdej = typSel!=null && (typSel.intValue()==1 || typSel.intValue()==2 || typSel.intValue()==8);
        boolean isNakupProdej = set.contains(typSel);
        Number idInv = (Number) rowMajetek.getAttribute("IdFinancniinvestice");

        String nazevInv = (String) rowMajetek.getAttribute("SNazevinvestice");
        Number ndObjemucetni = (Number) rowMajetek.getAttribute("NdObjemucetni");
        double objLocal = getDoubleValue(ndObjemucetni);
        if(nazevInv.equals(nazevInvGlobal)) {
          sumaLocal += objLocal;
        }
        else {
          if(nazevInvGlobal!=null) {
            //double pctOrig = celkem==0.0 ? 0.0 : 100.0*sumaOrig/celkem;
            double pctOrig = getPodilInvestice(id, lastIdProti);
            setCellValue( listNr, rowNr, 1,  nf.format(pctOrig)+"%" , yellow );
            setCellValue( listNr, rowNr, 2, "Suma pro "+nazevInvGlobal , styleBold );
            setCellValue( listNr, rowNr, 16, sumaLocal , yellow );
            rowNr++;
          }
          sumaLocal = objLocal;
        }
        CellStyle chybaEmise = null;
        try { celkem = ((Number)map.get(idInv)).doubleValue(); } catch(Exception e) { celkem=0.0; chybaEmise=red; };

        colNr = 1;
        
        String textEmise = null;
        boolean nenul = true;
        Number ksEmise = null;
        Number ksDil = null;
        Number nominal = null;
        String whereEmise = "ID = " + rowMajetek.getAttribute("IdKtgfininvesticeemise");
        voEmise.setWhereClause(whereEmise);
        if(voEmise.hasNext()) 
        {
          Row rowEmise = voEmise.next();
          textEmise = rowEmise.getAttribute("NlNominal")+"/"+
                      rowEmise.getAttribute("NlPocetkusu")+"/"+
                      rowEmise.getAttribute("NdZakladnijmeni");
          ksEmise = (Number) rowEmise.getAttribute("NlPocetkusu");
          ksDil = (Number) rowMajetek.getAttribute("NlPocetkusu");
          nominal = (Number) rowEmise.getAttribute("NlNominal");
          nenul = "1".equals(rowEmise.getAttribute("CNenulovy"));
        }
        voEmise.closeRowSet();
        
        double objOrig = 0.0;
        if(isNakupProdej && ksDil!=null && nominal!=null) {
          objOrig = getDoubleValue(ksDil) * getDoubleValue(nominal);
          if(nazevInv.equals(nazevInvGlobal)) {
            sumaOrig += objOrig;
          }
          else {
            sumaOrig = objOrig;
          }
        }
        double pctOrig = !nenul||celkem==0.0 ? 0.0 : 100.0*objOrig/celkem;
        double pctEmi = (ksEmise != null && ksDil != null && getDoubleValue(ksEmise) != 0.0) ?
          100.0*getDoubleValue(ksDil)/getDoubleValue(ksEmise) : 0.0;
        if(isNakupProdej) setCellValue( listNr, rowNr, colNr, nenul?nf.format(pctOrig)+"%":"-", chybaEmise );
        colNr++;
        if(isNakupProdej) setCellValue( listNr, rowNr, colNr, nenul?nf.format(pctEmi)+"%":"-", null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, rowMajetek.getAttribute("SNazevinvestice")+" ("+textEmise+")", null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, platnostOd, null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, platnostDo, null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)rowMajetek.getAttribute("SUcet"), null );
        colNr++;
        Number hlp = (Number) rowMajetek.getAttribute("NlPocetkusu");
        if(hlp!=null)
          setCellValue( listNr, rowNr, colNr, getDoubleValue(hlp) , null );
        colNr++;
        if(typSel != null) {
          voTyp.setWhereClause("ID = "+typSel.intValue());
          if(voTyp.hasNext()) {
            Row rowTyp = voTyp.next();
            setCellValue( listNr, rowNr, colNr, (String)rowTyp.getAttribute("SPopis") , null );
          }
          voTyp.closeRowSet();
        }
        colNr++;
        if(zpusobSel != null) {
          voZpusob.setWhereClause("ID = "+zpusobSel.intValue());
          if(voZpusob.hasNext()) {
            Row rowZpusob = voZpusob.next();
            setCellValue( listNr, rowNr, colNr, (String)rowZpusob.getAttribute("SPopis") , null );
          }
          voZpusob.closeRowSet();
        }
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)rowMajetek.getAttribute("SMenatransakce"), null );
        colNr++;
        hlp = (Number) rowMajetek.getAttribute("NdCenatransakcekus");
        if(hlp!=null)
          setCellValue( listNr, rowNr, colNr, getDoubleValue(hlp) , null );
        colNr++;
        hlp = (Number) rowMajetek.getAttribute("NdObjemtransakce");
        if(hlp!=null)
          setCellValue( listNr, rowNr, colNr, getDoubleValue(hlp) , null );
        colNr++;
        hlp = (Number) rowMajetek.getAttribute("NdKurz");
        if(hlp!=null)
          setCellValue( listNr, rowNr, colNr, getDoubleValue(hlp) , null );
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)rowMajetek.getAttribute("SMenaucetni"), null );
        colNr++;
        hlp = (Number) rowMajetek.getAttribute("NdCenaucetnikus");
        if(hlp!=null)
          setCellValue( listNr, rowNr, colNr, getDoubleValue(hlp) , null );
        colNr++;
        hlp = (Number) rowMajetek.getAttribute("NdObjemucetni");
        if(hlp!=null)
          setCellValue( listNr, rowNr, colNr, getDoubleValue(hlp) , null );
        colNr++;
        
        rowNr++;
        nazevInvGlobal = nazevInv;
        lastIdProti = idProti;
      }
      if(!header) {
        //double pctOrig = celkem==0.0 ? 0.0 : 100.0*sumaOrig/celkem;
        double pctOrig = getPodilInvestice(id, lastIdProti);
        setCellValue( listNr, rowNr, 1,  nf.format(pctOrig)+"%" , yellow );
        setCellValue( listNr, rowNr, 2, "Suma pro "+nazevInvGlobal , styleBold );
        setCellValue( listNr, rowNr, 16, sumaLocal , yellow );
        //wb.getSheetAt(listNr).getRow(rowNr).getCell((short)16).getCellStyle().setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        rowNr++;
      }
      voMajetek.closeRowSet();
    }
    vo.closeRowSet();
  }

  private void outputMajetkoveUcastiPrehled() {
    wb.setSheetName(1,"MU - zjednoduseny prehled");

    int listNr=1;
    int rowNr=1;
    int colNr=0;
    
    String lastEmi = null;

    setCellValue( listNr, rowNr, colNr, "Emitent", lightBlue );
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    setCellValue( listNr, rowNr, colNr, "Podl", lightBlue );
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*10));
    setCellValue( listNr, rowNr, colNr, "Vlastnk", lightBlue );
    wb.getSheetAt(listNr).setColumnWidth((short)colNr++,(short)(256*50));
    rowNr++;

    double celkem = 0.0;

    ViewObject vo = dm.findViewObject("VwKpMajetkovaucastprehledView1");
    vo.clearCache();
    vo.setWhereClauseParam(0,datum);
    vo.setWhereClauseParam(1,datum);
    vo.setWhereClauseParam(2,datum);
    String whereSpol = "";
    if(co == 1) {
      whereSpol += "ID_CISKATSPOL IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL;
    }
    else if(co == 2) {
      whereSpol += "ID_CISKATSPOL NOT IN "+cz.jtbank.konsolidace.common.Constants.DIS_CISKATSPOL;
    }
    vo.setWhereClause(whereSpol);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String emi = (String)row.getAttribute("Emitent");

      if(lastEmi !=null && !emi.equals(lastEmi)) 
      {
        colNr = 0;
        setCellValue( listNr, rowNr, colNr, "Celkem "+ lastEmi, yellow );
        colNr++;
        setCellValue( listNr, rowNr, colNr, nf.format(celkem)+" %", yellow );
        celkem = 0.0;
        rowNr++;
      }
      
      colNr=0;
      setCellValue( listNr, rowNr, colNr, emi, null );
      colNr++;
      Number pp = (Number)row.getAttribute("Podil");
      if(pp==null) setCellValue( listNr, rowNr, colNr, "?", null );
      else {
        celkem += getDoubleValue(pp);
        setCellValue( listNr, rowNr, colNr, nf.format(getDoubleValue(pp))+" %", null );
      }
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Vlastnik"), null );
      colNr++;
      
      lastEmi = emi;
      
      rowNr++;
    }
    vo.closeRowSet();
    if(lastEmi!=null) {
      colNr = 0;
      setCellValue( listNr, rowNr, colNr, "Celkem "+ lastEmi, yellow );
      colNr++;
      setCellValue( listNr, rowNr, colNr, nf.format(celkem)+" %", yellow );
    }
  }
  
  protected boolean outputData () 
  {
    yellow = wb.createCellStyle();
    yellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    yellow.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    red = wb.createCellStyle();
    red.setFillForegroundColor(IndexedColors.RED.getIndex());
    red.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    wb.cloneSheet(0);

    long start = 0L, end = 0L, dif = 0L;
    start = System.currentTimeMillis();  
    outputMajetkoveUcasti();
    end = System.currentTimeMillis();    
    logger.debug("majetkove ucasti:"+((end-start)/1000.0)+"s");
    start = System.currentTimeMillis();  
    outputMajetkoveUcastiPrehled();
    end = System.currentTimeMillis();    
    logger.debug("majetkove ucasti prehled:"+((end-start)/1000.0)+"s");

    dm.getTransaction().commit();
    
    return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.majetek.MajetekModule","MajetekModuleLocal");
      ESExportMajetek ed = new ESExportMajetek(dm,
                                               new java.sql.Date(System.currentTimeMillis()),
                                               0);
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
