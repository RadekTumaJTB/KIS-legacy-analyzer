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

    public class ESExportSLHlava  extends AbsExcelDoklad 
    {
      static Logger logger = Logger.getLogger(ESExportSLHlava.class);
      static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

      private ApplicationModule dm;
      private java.sql.Date datum;

      private String dir;

      private CellStyle styleBold;

      public ESExportSLHlava(ApplicationModule dokladyModule,
                           java.sql.Date datum)
      {
        logger.info("ESExportSLHlava:datum="+datum);  
        dm = dokladyModule;
        this.datum = datum;
        dir = Constants.DIR_SL_POSTUP;
        init();
      }

      private void init() {
        setFileName ( "SLHlava_"+datum+".xlsx" );
        setFileRelativeName( dir+"\\"+getFileName() );
        setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
        setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
    logger.info("ESExportSLHlava:cesta="+this.getFileAbsoluteName());      
      }
      
      private void outputSLHlava() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        int listNr=0;
        int rowNr=0;
        int colNr=0;

        CellStyle lightBlue = wb.createCellStyle();
        lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = wb.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        CellStyle styleAlarm = wb.createCellStyle();
        styleAlarm.setFont(font);

        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
        setCellValue( listNr, rowNr, colNr, "Druh S.L." , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
        setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
        setCellValue( listNr, rowNr, colNr, "popis S.L." , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
        setCellValue( listNr, rowNr, colNr, "Status" , lightBlue ); colNr++;        
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
        setCellValue( listNr, rowNr, colNr, "Spolenost" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
        setCellValue( listNr, rowNr, colNr, "Mna" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
        setCellValue( listNr, rowNr, colNr, "Protistrana" , lightBlue ); colNr++;
        //wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        //setCellValue( listNr, rowNr, colNr, "ID typTransakce" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*50));
        setCellValue( listNr, rowNr, colNr, "Transakce" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
        setCellValue( listNr, rowNr, colNr, "Zadavatel" , lightBlue ); colNr++;        
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Zadn" , lightBlue ); colNr++;        
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Zamitnuto zadavatelem" , lightBlue ); colNr++;                
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
        setCellValue( listNr, rowNr, colNr, "Duvod zamitnuti zadavatele" , lightBlue ); colNr++;        
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
        setCellValue( listNr, rowNr, colNr, "Typizovana transakce" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Typizovana oD" , lightBlue ); colNr++;        
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Typizovana Do" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
        setCellValue( listNr, rowNr, colNr, "ID Typizovaneho Sl - prvotni" , lightBlue ); colNr++;                  
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Datum zatovan" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "slo dokladu" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
        setCellValue( listNr, rowNr, colNr, "Gestor" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Gestor schvleno" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Gestor zamtnuto" , lightBlue ); colNr++;          
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
        setCellValue( listNr, rowNr, colNr, "Gestor - Dvod zamtnut" , lightBlue ); colNr++;          
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
        setCellValue( listNr, rowNr, colNr, "Zadavatel - Dvod zruen" , lightBlue ); colNr++;          
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Datum splatnosti" , lightBlue ); colNr++;          
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
        setCellValue( listNr, rowNr, colNr, "etn" , lightBlue ); colNr++;
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "pozadovan Dtum hrady" , lightBlue ); colNr++;          
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "realn Dtum hrady" , lightBlue ); colNr++;          
        //wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        //setCellValue( listNr, rowNr, colNr, "ID spol uhrada" , lightBlue ); colNr++;          
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Datum zaztovn hrady" , lightBlue ); colNr++;                    
        //wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        //setCellValue( listNr, rowNr, colNr, "ID zauctuhrada" , lightBlue ); colNr++;          
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Datum zamtnut etn" , lightBlue ); colNr++;                    
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
        setCellValue( listNr, rowNr, colNr, "Dvod zamtnut etn" , lightBlue ); colNr++;                              
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*5));
        setCellValue( listNr, rowNr, colNr, "s DPH" , lightBlue ); colNr++;       
        wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
        setCellValue( listNr, rowNr, colNr, "Uivatel" , lightBlue ); colNr++;
        rowNr++;

        ViewObject vo;
        vo = dm.findViewObject("VwRepSchvalovakhlavaView1");            
        vo.clearCache();

        String where = "DT_DATUMZADANI BETWEEN ADD_MONTHS(TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy'),-3) AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy')";
        vo.setWhereClause(where);
        while(vo.hasNext()) 
        {
          colNr = 0;    
          Row row = vo.next();
          
          oracle.jbo.domain.Date hlpDt = null;
          oracle.jbo.domain.Number hlpNum = null;
          String strDatum=null;
          String strNum=null;
          boolean pozde = false;
          boolean anone = false;
          
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis") , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id") , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("PopisSl") , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("DocStatus") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost") , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena") , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SProtistrana") , null ); 
        colNr++;
        //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdTyptransakce") , null ); 
        //colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("TypTransakce") , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Zadavatel") , null ); 
        colNr++;         
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumzadani");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtZadavatelzamitnuto");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SZadavatelzamitnuto") , null ); 
        colNr++;            
        anone = "1".equals(row.getAttribute("TypizovanaTranskace"));
        setCellValue( listNr, rowNr, colNr, anone?"ANO":"NE", null ); 
        colNr++;
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtTypizovanaod");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtTypizovanado");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("PodleTypizovanehoSl") , null ); 
        hlpNum = (oracle.jbo.domain.Number)row.getAttribute("PodleTypizovanehoSl");
        strNum = hlpNum!=null ? hlpNum.toString() :"";
        setCellValue( listNr, rowNr, colNr, ""+strNum , null ); 
        colNr++;         
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumzauctovani");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SCislodokladu") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Gestor") , null ); 
        colNr++;        
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtGestorschvaleno");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtGestorzamitnuto");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SGestorzamitnuto") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SDuvodzruseni") , null ); 
        colNr++;        
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumsplatnosti");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ucetni") , null ); 
        colNr++;        
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtPozaddatumuhrady");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtRealdatumuhrady");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;           
        //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdUcspoluhrada") , null ); 
        //colNr++;
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtZauctuhrada");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;           
        //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdZauctuhrada") , null ); 
        //colNr++;            
        hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtUcetnizamitnuto");
        strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
        setCellValue( listNr, rowNr, colNr, strDatum , null ); 
        colNr++;                   
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUcetnizamitnuto") , null ); 
        colNr++;                
        //setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CDph") , null ); 
        anone = "1".equals(row.getAttribute("CDph"));
        setCellValue( listNr, rowNr, colNr, anone?"ANO":"NE", null ); 
        colNr++;                
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Uzivatel") , null ); 
        colNr++;                                    
        
          rowNr++;
        }
        vo.closeRowSet();
        dm.getTransaction().commit();
      }
        
      protected boolean outputData () 
      {
          long start = 0L, end = 0L, dif = 0L;
          start = System.currentTimeMillis();    
          outputSLHlava();
          end = System.currentTimeMillis();    
          logger.debug("SLHlava:"+((end-start)/1000.0)+"s");
          
          return true;
      }

      public static void main(String[] argv) 
      {
        try {
          ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModule","DokumentModuleLocal");
    //      ESExportSLHlava ed = new ESExportSLHlava(dm,new java.sql.Date(System.currentTimeMillis()));
          ESExportSLHlava ed = new ESExportSLHlava(dm,new java.sql.Date(System.currentTimeMillis()));
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



