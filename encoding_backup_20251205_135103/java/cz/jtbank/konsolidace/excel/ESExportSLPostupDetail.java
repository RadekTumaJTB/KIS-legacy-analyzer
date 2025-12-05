package cz.jtbank.konsolidace.excel;
//import oracle.jbo.server.ApplicationModuleImpl.*;
import cz.jtbank.konsolidace.common.*;
//import java.sql.*;
//import oracle.jbo.server.DBTransaction;

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
//import cz.jtbank.konsolidace.dokument.common.DokumentModule.*;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;
// esc ??
//import cz.jtbank.konsolidace.dokument.DokumentModuleImpl;
//import cz.jtbank.konsolidace.dokument.common.DokumentModule;

public class ESExportSLPostupDetail extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportSLPostupDetail.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  //private ApplicationModule dm2;
  //private cz.jtbank.konsolidace.dokument.DokumentModuleImpl dm2;
   
  public  boolean budgetOver = false;
  private java.sql.Date datum;

  private String dir;

  private CellStyle styleBold;

  public ESExportSLPostupDetail(ApplicationModule dokumentyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportSLhlavaDetail:datum="+datum);  
    dm = dokumentyModule;
    
      //cz.jtbank.konsolidace.dokument.common.DokumentModule dmd =          (cz.jtbank.konsolidace.dokument.common.DokumentModule) DokumentModule.useApplicationModule();
    
     
     //dm2 = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModule","DokumentModuleLocal");
    this.datum = datum;
    dir = Constants.DIR_SL_POSTUP;
    init();
  }

  private void init() {
    setFileName ( "SLHlavaDetail_"+datum+".xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
logger.info("ESExportSLhlavaDetail:cestaD="+this.getFileAbsoluteName());  
    //dm2 = (cz.jtbank.konsolidace.dokument.DokumentModuleImpl )Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModuleImpl","DokumentModuleLocal");
    
  }
  
  private void outputSLPostupDetail() {
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
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id Radek" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id S.L." , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id Odbor" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
      setCellValue( listNr, rowNr, colNr, "Odbor" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id Projekt" , lightBlue ); colNr++;
      wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
      setCellValue( listNr, rowNr, colNr, "Projekt" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Castka" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id Odbor OO" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Schvaleno Odbor" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id Projekt man" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*25));
    setCellValue( listNr, rowNr, colNr, "Schvaleno Projekt" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id Ucetni" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Schvaleno Ucetni" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "Id Ucetni TOP" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Schvaleno Ucetni TOP" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Komentar" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Datum Prefak." , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Prefak" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Prefak" , lightBlue ); colNr++;
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Datum Zadani" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Vyuzit" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*3));
    setCellValue( listNr, rowNr, colNr, "Z Obj" , lightBlue ); colNr++;	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Zamitnuto" , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr, "Datum Zamit." , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Duvod Zamit." , lightBlue ); colNr++;
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID S.L. orig" , lightBlue ); colNr++;	  
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Odbor real" , lightBlue ); colNr++;	  
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID PM real" , lightBlue ); colNr++;	  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Ucetni real" , lightBlue ); colNr++;	  
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Ucetni Top real" , lightBlue ); colNr++;	  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*8));
    setCellValue( listNr, rowNr, colNr, "ID Typ Transakce" , lightBlue ); colNr++;	  		  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "OO Odbor" , lightBlue ); colNr++;	  		  
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Projekt Manager" , lightBlue ); colNr++;	  		  		
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "OO sek/spol." , lightBlue ); colNr++;	  		  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "TOP" , lightBlue ); colNr++;	  		  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "OO sek/spol." , lightBlue ); colNr++;	  		  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "OO Odbor real" , lightBlue ); colNr++;	  		  
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Projekt Manager real" , lightBlue ); colNr++;	  		  		
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "OO sek/spol. real" , lightBlue ); colNr++;	  		  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "TOP real" , lightBlue ); colNr++;	  		  	
	wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*30));
    setCellValue( listNr, rowNr, colNr, "Zamitnuto" , lightBlue ); colNr++;	  		  	
	
    rowNr++;
	
/*
 Osoby: ID_ODBOROO, ID_PROJEKTMAN, ID_UCSPOLOO, ID_UCSPOLTOP 
 reprezentuj editele odboru, projekt managera, OO spolenosti pp. seku a fa segmentu. 
 Sloupce se stejnmi nzvy konc na REAL reprezentuj osoby, kter fyzicky provedli schvlen (zstupci a admisnistrtoi). 
 */	
	if (dm == null){
		logger.debug("dm==nul");
	}
        //ViewObject vo = dm.findViewObject("VwRepSchvalovakpostupDetailView1");
        ViewObject vo = dm.findViewObject("VwRepSchvalovakhlavadetailView1");
	if (vo == null){
	
		logger.debug("dm.findViewObject(\"VwRepSchvalovakhlavadetailView1\")==null");
		
		String [] tmp = dm.getViewObjectNames();
		logger.debug("ViewObjectNames");
		if (tmp != null)
			for (int i=0; i < tmp.length; i++)				logger.debug("ViewObjectNames-" + tmp[i]);
			
		tmp = dm.getApplicationModuleNames();
		logger.debug("ApplicationModuleNames");
		if (tmp != null)
			for (int i=0; i < tmp.length; i++)
				logger.debug("ApplicationModuleNames-" + tmp[i]);
			
		tmp = dm.getViewLinkNames();
		logger.debug("ViewLinkNames");
		if (tmp != null)
			for (int i=0; i < tmp.length; i++)
				logger.debug("ViewLinkNames-" + tmp[i]);
	}
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
      boolean pozde = false;
      
      int idRadek ;
    
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("Id");
      idRadek =  hlpNum.intValue();
            //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id") , null ); 
      setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;      
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdDokument") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdDokument");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdOdbor") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdOdbor");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazevOdbor") , null ); 
      colNr++;      
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdProjekt") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdProjekt");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  
        setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SNazevRoejekt") , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("NdCastka") , null ); 
      /*try {
             budgetOver = checkBudgetRep(idRadek);
             //dm2.checkBudget
            } catch (KisException e) {
                //logger.debug(" BUM !!!ESExportSLhlavaDetail:checkBudget="+idRadek);  
                //e.printStackTrace();             }
           */ 
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("NdCastka") , budgetOver?styleAlarm:null);
            colNr++;	  
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdOdboroo") , null ); 
      colNr++;	  	  
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtSchvalenoodbor");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdProjektman") , null ); 
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdProjektman");
      setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtSchvalenoprojekt");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdUcspoloo") , null ); 
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdUcspoloo");
      setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtSchvalenoucspol");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdUcspoltop") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdUcspoltop");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtSchvalenotop");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
	  setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SKomentar") , null ); 
      colNr++;	  
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumprefakt");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdPrefakt") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdPrefakt");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CKprefakt") , null ); 
      colNr++;
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtDatumzadani");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CRemoved") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CZobjednavky") , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdZamitnuto") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdZamitnuto");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  
      hlpDt = (oracle.jbo.domain.Date) row.getAttribute("DtZamitnuto");
      strDatum = hlpDt!=null ? sdf.format(hlpDt.dateValue()) : "";
      setCellValue( listNr, rowNr, colNr, strDatum , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SZamitnuto") , null ); 
      colNr++;
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdDokumentorig") , null ); 
      hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdDokumentorig");
      setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdOdborooreal") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdOdborooreal");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdProjektmanreal") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdProjektmanreal");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  	  
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdUcspolooreal") , null ); 
       hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdProjektmanreal");
       setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdUcspoltopreal") , null ); 
       hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdUcspoltopreal");
       setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  
      //setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdTyptransakce") , null ); 
        hlpNum = (oracle.jbo.domain.Number) row.getAttribute("IdTyptransakce");
        setCellValue( listNr, rowNr, colNr, ""+  (hlpNum==null?"":""+hlpNum.intValue()) , null ); 
      colNr++;	  	  	  	  

      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ro") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Pm") , null ); 
      colNr++;	  
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Oo") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Top") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Rro") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Rpm") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Roo") , null ); 
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Rtop") , null ); 
      colNr++;	  
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Za") , null ); 
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
      outputSLPostupDetail();
      end = System.currentTimeMillis();    
      logger.debug("SLhlavaDetail:"+((end-start)/1000.0)+"s");
      
      return true;
  }
/*
public  boolean checkBudgetRep(int id) throws KisException
  {
    boolean ret = true;
    
    DBTransaction dbTran = this.getDBTransaction();
    
    CallableStatement st = null;
    try {
      st = dbTran.createCallableStatement("begin ? := db_jt.kap_dokument.checkBudget(?); end;",0);
      st.registerOutParameter(1, Types.VARCHAR);
      st.setInt(2, id);
      st.execute();
      ret = null==st.getString(1);
    }
    catch (SQLException s) {
      s.printStackTrace(); //pro zacatek
      throw new KisException("Selhalo voln funkce db_jt.kap_dokument.checkBudget",s);
    }
    finally {
      try {
        if (st != null) st.close();
      } 
      catch (SQLException s) { // ignore 
      }
    }
  
    return ret;
  }
*/
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModule","DokumentModuleLocal");
//      ESExportSLPostup ed = new ESExportSLPostup(dm,new java.sql.Date(System.currentTimeMillis()));
      ESExportSLPostupDetail ed = new ESExportSLPostupDetail(dm,new java.sql.Date(System.currentTimeMillis()));
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
