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
import oracle.jbo.server.DBTransaction;
import oracle.jbo.server.ApplicationModuleImpl;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportMajetekZmena extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportMajetekZmena.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;

  private String dir;

  public ESExportMajetekZmena(ApplicationModule dokladyModule)
  {
    logger.info("ESExportMajetekZmena");  
    dm = dokladyModule;
    dir = Constants.DIR_ZMENA_MAJ_UCAST;
    init();
  }

  private void init() {
    setFileName ( "MajetekZmeny.xlsx" );
    setFileRelativeName( dir+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"Empty.xlsx" );
  }
  
  private void outputZmeny () {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");    

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);


    int listNr=0;
    int rowNr=1;
    int colNr=1;

    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*35));
    setCellValue( listNr, rowNr, colNr++, "etn spolenost", lightBlue ); 
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr++, "Zmna" , lightBlue );         
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*35));
    setCellValue( listNr, rowNr, colNr++, "ast" , lightBlue ); 
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr++, "Platnost od", lightBlue );       
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr++, "Platnost od", lightBlue );       
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr++, "Transakce", lightBlue ); 
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*20));
    setCellValue( listNr, rowNr, colNr++, "Zpusob", lightBlue );
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*15));
    setCellValue( listNr, rowNr, colNr++, "Kus" , lightBlue ); 
    
    setCellValue( listNr, rowNr, colNr++, "Mna transakce" , lightBlue ); 
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*17));
    setCellValue( listNr, rowNr, colNr++, "Objem transakce" , lightBlue ); 
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*11));
    setCellValue( listNr, rowNr, colNr++, "Kurz", lightBlue ); 
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*17));
    setCellValue( listNr, rowNr, colNr++, "Objem etn" , lightBlue ); 
    
    wb.getSheetAt(listNr).setColumnWidth((short)colNr,(short)(256*35));
    setCellValue( listNr, rowNr, colNr++, "Protistrana" , lightBlue ); 

    rowNr=3;
    colNr=1;

    ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;

    DBTransaction dbTran = sm.getDBTransaction();
    java.sql.ResultSet rs = null;
    try {
      java.sql.Statement st = dbTran.createStatement(0);
      
      String sqlSelect = 
        "select ( select s_nazev from db_jt.kp_ktg_ucetniSpolecnost us where us.id = mu.id_ktgUcetniSPolecnost ) s_nazevUcSpol, " +
        "mu.dt_zmena, sp.s_nazev, mu.dt_platnostOd, mu.dt_platnostdo, tt.s_popis TranPopis, mz.s_popis ZpusobPopis,  " +
        "mu.nl_pocetkusu, mu.s_menatransakce, mu.nd_objemtransakce, mu.nd_kurz, mu.nd_objemucetni, " + 
        "( select s_nazev from db_jt.kp_ktg_ucetniSpolecnost us where us.id = mu.id_ktgUcetniSPolecnostKoupeno ) s_nazevKoupenoOd " +
        "from db_jt.KP_DAT_MAJETKOVAUCAST mu, db_jt.kp_cis_majetkovaucasttyptran tt, db_jt.kp_ktg_financniinvestice fi, db_jt.kp_ktg_fininvesticeemise fe, " +
        "db_jt.kp_ktg_spolecnost sp, db_jt.kp_cis_majetkovaucastzpusob mz " +
        "where mu.dt_zmena > sysdate - 1 and mu.id_cismutyptransakce = tt.id and tt.c_sumazapocitavat = '1' " +
        "and mu.id_ktgfininvesticeemise = fe.id and fe.id_ktgfinancniinvestice = fi.id " +
        "and fi.id_ktgspolecnost = sp.id and mz.id = mu.id_cismuzpusob order by mu.dt_zmena desc";

      rs =  st.executeQuery( sqlSelect );

      while ( rs.next()  && rowNr<Constants.MAX_POCET_RADKU_EXCEL ) {
    
        colNr = 1;
        double num;
        
        setCellValue( listNr, rowNr, colNr, rs.getString("S_NAZEVUCSPOL") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, sdf.format ( rs.getDate("DT_ZMENA") ) , null );         
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getString("S_NAZEV") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, sdf.format ( rs.getDate("DT_PLATNOSTOD") ) , null );         
        colNr++;        
        setCellValue( listNr, rowNr, colNr, sdf.format ( rs.getDate("DT_PLATNOSTDO") ) , null );         
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getString("TRANPOPIS") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getString("ZPUSOBPOPIS") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getInt("NL_POCETKUSU") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getString("S_MENATRANSAKCE") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getDouble("ND_OBJEMTRANSAKCE") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getDouble("ND_KURZ") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getDouble("ND_OBJEMUCETNI") , null ); 
        colNr++;        
        setCellValue( listNr, rowNr, colNr, rs.getString("S_NAZEVKOUPENOOD") , null ); 
        colNr++;                

        rowNr++;
      }
      if (rs.next()) {
        setCellValue( listNr, rowNr, 1, "D A T A    N E J S O U   K O M P L E T N  - poet dk pesahuje monosti excelu" , null );
      }
      rs.close();
      dm.getTransaction().commit();
    } catch ( Exception e ) {      
      e.printStackTrace();
    }
  }
    
  protected boolean outputData () 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputZmeny();
      wb.setSheetName(0,"Zmeny");
      end = System.currentTimeMillis();    
      logger.debug("CashFlow:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      
      ESExportMajetekZmena ed = new ESExportMajetekZmena(dm);
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
