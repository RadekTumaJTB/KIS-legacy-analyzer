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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import cz.jtbank.konsolidace.doklady.common.DokladyModule;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESImportPoziceMan extends AbsReadExcel
{
  static Logger logger = Logger.getLogger(ESImportPoziceMan.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private DokladyModule dm;
  private int idSpol;
  private java.sql.Date datum;
  private String nazevSouboru;
  private boolean uverova;
  
  private String souborPredponaSpol;

  public ESImportPoziceMan(DokladyModule dokladyModule, String nazevSouboru, int idSpol, java.sql.Date datum, boolean uverova)
  {
    dm = dokladyModule;
    this.nazevSouboru = nazevSouboru;
    this.idSpol = idSpol;
    this.datum = datum;
    this.uverova = uverova;
    init();
  }

  private void init() {
    logger.info("ImportPoziceMan:nazevSouboru="+nazevSouboru);  
//esc 25.07.2016    
    logger.info("ImportPoziceMan: idSpol="+idSpol+" datum="+datum);  
//
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idSpol);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
    }
    vo.closeRowSet();

    setFileName ( nazevSouboru );
    setFileRelativeName( souborPredponaSpol+"_"+idSpol+"\\"+getFileName() );
    setFileAbsoluteName( IN_DIR + this.getFileRelativeName() );
  }
  
  private void readPoziceUverova() throws KisException
  {
    int listNr = 0;
    int rowNr = 5;
    int colNr = 12;

    for(;rowNr<10;) 
    {
      String ico = getStringValue(listNr, rowNr, colNr);
      if(ico!=null && ico.trim().length()>0) break;
      else rowNr++;
    }
    
    for(;;rowNr++)
    {
      String ico = getStringValue(listNr, rowNr, colNr);
      String nazev = getStringValue(listNr, rowNr, colNr+1);
      String ucet = getStringValue(listNr, rowNr, colNr-7);
      String text = getStringValue(listNr, rowNr, colNr-6);
      double castkaCNB = getDoubleValue(listNr, rowNr, colNr-5);
      double castka = getDoubleValue(listNr, rowNr, colNr-2);
      double castkaLocal = getDoubleValue(listNr, rowNr, colNr-4);
      String mena = getStringValue(listNr, rowNr, colNr-1);
//esc 22.9 2009
      double rizikovost = getDoubleValue(listNr, rowNr, colNr+4);
//esc 25.3 2011
      double cistaangazovanost = getDoubleValue(listNr, rowNr, colNr+5);
//esc 16.12.2010
      String skupina  = getStringValue(listNr, rowNr, colNr+2);
      String Sskupina = Utils.getString(skupina) ;
//esc 23.02.2011
      String banka =   getStringValue(listNr, rowNr, colNr+7);

logger.debug("ImportPoziceMan:rizikovost:"+rizikovost+" nazev:"+nazev+" skupina:"+skupina+" banka:"+banka+" cistaAng:"+cistaangazovanost);        

      int cellType = -1;
      if(wb.getSheetAt(listNr).getRow(rowNr)!=null && 
         wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr)!=null) {
        cellType = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr).getCellType();
      }
      if(cellType == Cell.CELL_TYPE_FORMULA) 
      {
        throw new KisException("Nen mono natat vzorce v poli I protistrany pi natn pozic!",
                               new KisException(Constants.ERR_MESSAGE_ONLY));
      }
      if(ico==null || ico.trim().length()==0) {
        if(nazev!=null) {
          throw new KisException("Nen mono natat protistrany bez I pi natn pozic!",
                                 new KisException(Constants.ERR_MESSAGE_ONLY));
        }
        else break;
      }

      
      try {      
        dm.manTestInsertUverova(ico.trim(), 
                                nazev==null?null:nazev.trim(),
                                ucet==null?null:ucet.trim(),
                                text==null?null:text.trim(),
                                castkaCNB,
                                castka,
                                castkaLocal,
                                mena==null?null:mena.trim()
                                ,rizikovost //esc 22.9 2009
                                //,skupina==null?null:skupina.substring(0,5) //esc 16.12.2010
                                ,Sskupina.substring(0, Math.min(Sskupina.length(), 5))                                
                                ,banka==null?null:banka.trim()             //esc 23.02.2011
                                ,cistaangazovanost                         //esc 25.03.2011
                                );
      }
      catch(KisException e) { e.printStackTrace(); throw new KisException("ico="+ico+",ucet="+ucet+",mena="+mena+",castka="+castka+" skupina"+skupina+",banka="+banka+" cistaAng"+cistaangazovanost,e); }
      //                catch(KisException e) { e.printStackTrace(); throw new KisException("ico="+ico+",ucet="+ucet+",mena="+mena+",castka="+castka,e); }
    }
  }

  
  private void readPoziceMenova() throws KisException
  {
    int listNr = 0;
    int rowNr = 4;
    int colNr = 7;

    for(;rowNr<10;) 
    {
      String spol = getStringValue(listNr, rowNr, colNr);
      if(spol!=null) break;
      else rowNr++;
    }
    
    for(;;rowNr++)
    {
      String ucet = getStringValue(listNr, rowNr, colNr);
      String text = getStringValue(listNr, rowNr, colNr+1);
      double castkaCNB = getDoubleValue(listNr, rowNr, colNr+2);
      double castka = getDoubleValue(listNr, rowNr, colNr+5);
      double castkaLocal = getDoubleValue(listNr, rowNr, colNr+3);
      String mena = getStringValue(listNr, rowNr, colNr+6);

      int cellType = -1;
      if(wb.getSheetAt(listNr).getRow(rowNr)!=null && 
         wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr)!=null) {
        cellType = wb.getSheetAt(listNr).getRow(rowNr).getCell((short)colNr).getCellType();
      }
      if(ucet==null) break;
      
      try {      
        dm.manTestInsertMenova(ucet,
                                             text,
                                             castkaCNB,
                                             castka,
                                             castkaLocal,
                                             mena);
      }
      catch(KisException e) { e.printStackTrace(); throw new KisException("ucet="+ucet+",mena="+mena+",castka="+castka,e); }
    }
  }

  protected boolean readData () throws KisException
  {
    dm.manStartImportPozice(idSpol, datum, uverova?"U":"M");
  
    if(uverova)
      readPoziceUverova();
    else
      readPoziceMenova();
    
    return true;
  }
  
  public static void main(String[] argv) throws KisException
  {
    try {
      DokladyModule dm = (DokladyModule) Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
//      ESImportDokladMan ed = new ESImportDokladMan(dm,"skuska balik burson.xlsx",503015);
      ESImportPoziceMan ed = new ESImportPoziceMan(dm,"45I_RKC Uverova pozicia_13082007.xlsx",6455,
                                                   new java.sql.Date((new java.util.Date(107,7,13)).getTime()),
                                                   true);
      ed.excelRead();

      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }
  
}