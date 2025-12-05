package cz.jtbank.konsolidace.common;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import java.util.*;
import java.text.SimpleDateFormat;
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cz.jtbank.konsolidace.common.Logging;

public class MUProtokol
{
  private static final Logger logger = LoggerFactory.getLogger(MUProtokol.class);
  static {
    Logging.addAppenderToLogger(MUProtokol.class.getName(), Logging.getAppender(Logging.LOG_AUTO_GEN));
  }

  private static MUProtokol instance;
  
  protected ApplicationModule am = null;
  protected int ucSkup;
  
  private StringBuffer protokol;
  private java.util.Date startDate;
  
  private List ids;
  private java.sql.Date datum;
  private String dtString;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private SimpleDateFormat sdfHod = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy");

  private MUProtokol()
  {
  }
  
  public static MUProtokol getInstance() 
  {
    if(instance == null) instance = new MUProtokol();
    return instance;
  }
  
  public void init(Number[] ids, java.sql.Date datum) 
  {
    startDate = new java.util.Date();
    this.ids = Arrays.asList(ids);
    this.datum = datum;
  }
  
  public boolean createProtokol(ApplicationModule am, int ucSkup) 
  {
    if(ids == null || datum == null) return false;
    
    this.am = am;
    this.ucSkup = ucSkup;
  
    protokol = new StringBuffer();

    getLogHeader();
    getLogTableHeader();
  
    ViewObject voDoklad = am.findViewObject("KpDatDokladView1");
    voDoklad.clearCache();
    ViewObject voSpol = am.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();

    String whereSpol = "";
    if(ucSkup==1) 
    {
      whereSpol = "ID IN (SELECT distinct c.ID_KTGUCETNISPOLECNOST "+
                         "FROM DB_JT.KP_REL_SUBKONSOLIDACECLEN c, "+
                              "DB_JT.KP_KTG_SUBKONSOLIDACE s "+
                         "where c.ID_KTGSUBKONSOLIDACE = s.id_ktgucetnispolecnost "+
                           "and s.ID_KTGUCETNISKUPINA = 1 "+
                           "and c.c_clen = '1' and c.ID_CISSUBTYPCLENSTVI <> 3 "+
                           "AND (sysdate >= C.DT_ClenstviOD OR C.DT_ClenstviOD IS NULL) "+
                           "AND (sysdate <= C.DT_ClenstviDO OR C.DT_ClenstviDO IS NULL) "+
                           "and c.ID_KTGUCETNISPOLECNOST  < 10000)";
    }
    else if(ucSkup==-1) 
    {
      whereSpol = "ID IN (1,1001)";
    }
    voSpol.setWhereClause(whereSpol);
    Set enabled = new HashSet();
    while(voSpol.hasNext()) 
    {
      Row rowSpol = voSpol.next();
      Number eSpol = (Number) rowSpol.getAttribute("Id");
      enabled.add(eSpol);
    }
    voSpol.closeRowSet();
    
    Iterator iter = ids.iterator(); 
    while(iter.hasNext()) 
    {
      Number idSpol = (Number) iter.next();
      if(!enabled.contains(idSpol)) continue;
      
      String nazevSpol = null;
      String rkcFlag = null;
      voSpol.setWhereClause("ID = " + idSpol);
      if(voSpol.hasNext()) 
      {
        Row rowSpol = voSpol.next();
        nazevSpol = (String) rowSpol.getAttribute("SNazev");
        rkcFlag = (String) rowSpol.getAttribute("CRkc");
      }
      voSpol.closeRowSet();
    
      String where = "ID_KTGUCETNISPOLECNOST = " + idSpol + 
                     " AND DT_DATUM = TO_DATE('"+ sdf.format(datum) +"','DD.MM.YYYY')"+
                     " AND DT_VYTVORENO > SYSDATE-1 AND S_UZIVATEL = '"+Constants.DENNI_USER+"'";
      voDoklad.setWhereClause( where );
      boolean t = false;
      if(voDoklad.hasNext()) 
      {
        t=true;
        Row rowDoklad = voDoklad.next();
        Number idDoklad = (Number)rowDoklad.getAttribute("Id");
        oracle.jbo.domain.Date dtVytvoreno = (oracle.jbo.domain.Date) rowDoklad.getAttribute("DtVytvoreno");
        genLogRow(dtVytvoreno,"OK",nazevSpol,idSpol.toString(),idDoklad.toString(),rkcFlag);
      }
      else 
      {
        String msg = getFromFronta(idSpol, datum, nazevSpol);
        if(msg != null) {
          genLogErrorRow(msg);
        }
      }
      voDoklad.closeRowSet();
    }    

    getLogTableBottom();
    getLogBottom();
    
    saveLogFile();
    
    return true;
  }

  private String getFromFronta(Number idSpol, java.sql.Date datum, String nazevSpol) 
  {
    String ret = "Nebylo mo�no vytvo�it doklad pro spole�nost "+nazevSpol+" ("+idSpol+").";
    String where = "IDSPOLECNOST = " + idSpol + 
                   " AND ADATUM = TO_DATE('"+ sdf.format(datum) +"','DD.MM.YYYY')"+
                   " AND DATUM > SYSDATE-1 AND AUZIVATEL = '"+Constants.DENNI_USER+"'";
    ViewObject voFronta = am.findViewObject("VwKpDokladfrontaView1");
    voFronta.clearCache();
    voFronta.setWhereClause(where);
    if(voFronta.hasNext()) 
    {
      Row rowFronta = voFronta.next();
      String stav = (String) rowFronta.getAttribute("SStav");
      String sqlErr = (String) rowFronta.getAttribute("SSqlerror");
      if(stav != null) 
      {
        if(stav.startsWith("Zam"))
          ret = null;
        else if(stav.startsWith("DB"))
          ret = "Datab�zov� chyba : "+sqlErr;
        else if(stav.startsWith("Exp"))
          ret = "Chyba p�i exportu do excelu.";
      }
    }
    voFronta.closeRowSet();
    return ret;
  }
  
  private void getLogHeader() 
  {
    protokol.append("<html>\n<head>\n<LINK REL=STYLESHEET TYPE=\"text/css\" HREF=\"https://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/bc4j.css\">\n");
    //protokol.append("<html>\n<head>\n<LINK REL=STYLESHEET TYPE=\"text/css\" HREF=\"bc4j.css\">\n");
    protokol.append("<META HTTP-EQUIV='PRAGMA' CONTENT='NO-CACHE'>\n");
    protokol.append("<meta http-equiv='Content-Type' content='text/html; charset=windows-1250'>\n");
    protokol.append("<script>\n");
    protokol.append("function switchCB() {\n" +
               "cb = document.getElementById('cb');\n" +
               "arr = document.getElementsByTagName('tr');\n" +
               "for(i=0; i<arr.length; i++) {\n" +
               "if(arr[i].name == 'hideMe') {\n" +
               "if(cb.checked == true) { arr[i].style.display = 'block'; } \n" +
               "else { arr[i].style.display = 'none'; }\n" +
               "}}}\n");
    protokol.append("function switchRKC() {\n" +
               "rkc = document.getElementById('rkc');\n" +
               "arr = document.getElementsByTagName('tr');\n" +
               "for(i=0; i<arr.length; i++) {\n" +
               "if(arr[i].id == 'rkc0') {\n" +
               "if(rkc.checked == true) { arr[i].style.display = 'block'; } \n" +
               "else { arr[i].style.display = 'none'; }\n" +
               "}}}\n");
    protokol.append("function switchError() {\n" +
               "err = document.getElementById('error');\n" +
               "arr = document.getElementsByTagName('tr');\n" +
               "for(i=0; i<arr.length; i++) {\n" +
               "if(arr[i].id == 'error') {\n" +
               "if(err.checked == true) { arr[i].style.display = 'block'; } \n" +
               "else { arr[i].style.display = 'none'; }\n" +
               "}}}\n");
    protokol.append("</script>\n");
    protokol.append("</head>\n<body>\n");
  }

  private void getLogTableHeader() 
  {
    String ret = "<form><input type='checkbox' id='cb' checked onClick='javascript:switchCB()'>Zobrazit logy, kter� jsou v po��dku.\n";
    ret += "<input type='checkbox' id='rkc' checked onClick='javascript:switchRKC()'>Zobrazit i ne-RKC spole�nosti.\n";
    ret += "<input type='checkbox' id='error' checked onClick='javascript:switchError()'>Zobrazit i chybov� hl�ky.</form>\n";
    ret += "Generov�n� k "+sdf.format(datum)+"\n";
    ret += "<table class=\"clsTable\">\n"+
           "<tr class=\"clsTableRow\">\n"+
           "<th class=\"clsTableHeader\">Dogenerov�no v</th>\n"+
           "<th class=\"clsTableHeader\">Spole�nost</th>\n"+
           "<th class=\"clsTableHeader\">V�sledek</th>\n"+
           "<th class=\"clsTableHeader\">Id spol.</th>\n"+
           "<th class=\"clsTableHeader\">Detailn� log</th>\n"+
           "</tr>\n";
    protokol.append(ret);
  }

  private void getLogTableBottom() 
  {
    protokol.append("</table>\n");
  }

  private void getLogBottom() 
  {
    protokol.append("</body>\n</html>\n");
  }

  private void genLogRow(oracle.jbo.domain.Date start,
                         String text,
                         String nazevSpol,
                         String idSpol,
                         String idDoklad,
                         String rkcFlag)
  {
    logger.debug("PROTOKOL M/U (normal) {}", idDoklad);
    
    String id = "rkc"+rkcFlag;
    
    String trHeader;
    if("OK".equals(text)) {
      trHeader = "<tr class='clsLogRowOk' name='hideMe' id='"+id+"'>\n";
    }
    else {
      trHeader = "<tr class='clsLogRowNotOk' id='"+id+"'>\n";
    }
    protokol.append(trHeader);
    protokol.append("<td>");
    protokol.append(sdfHod.format(new java.util.Date(start.timestampValue().getTime())));
    protokol.append("</td>\n");  
    protokol.append("<td>");
    protokol.append(nazevSpol);
    protokol.append("</td>\n");  
    protokol.append("<td>");
    protokol.append(text);
    protokol.append("</td>\n");  
    protokol.append("<td>");
    protokol.append(idSpol);
    protokol.append("</td>\n");  
    protokol.append("<td>");
    protokol.append("<a href=\"LogsDokladDetail.jsp?idDoklad="+idDoklad+"\">"+idDoklad+"</a>");
    protokol.append("</td>\n");  
    protokol.append("</tr>\n");
  }

  private void genLogErrorRow(String errorMsg)
  {
    logger.debug("PROTOKOL M/U (error)");
    
    protokol.append("<tr class='clsLogRowErr' id='error'>\n");
    protokol.append("<td>Probl�m</td>\n");  
    protokol.append("<td colspan='4'>");
    protokol.append(errorMsg);
    protokol.append("</td>\n");  
    protokol.append("</tr>\n");  
  }

  private String saveLogFile()
  {
    SimpleDateFormat sdfName = new SimpleDateFormat("yyyy-MM-dd");
    String fileName = Constants.PROTOKOL_FILES_PATH+Constants.DIR_POZICE_MU_LOGS+ucSkup+"\\P_"+sdfName.format(datum)+".html";
    FileOutputStream fos = null;
    try {
      File file = new File(fileName);
      logger.debug("PROTOKOL - ukladani souboru {}", file.getName());
      if ( !file.getParentFile().exists() ) file.getParentFile().mkdirs();
      fos = new FileOutputStream(file);
      fos.write(protokol.toString().getBytes());      
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    finally 
    {
      try {
        if(fos != null) fos.close();
      }
      catch(IOException ioe) {}
    }
    return fileName;
  }
  
  public static void main(String[] argv) 
  {
    ApplicationModule dm = oracle.jbo.client.Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
    MUProtokol.getInstance().init(new Number[] {new Number(1),
                                                new Number(1001),
                                                new Number(1002),
                                                new Number(5001),
                                                new Number(5002),
                                                new Number(5003)},
                                   new java.sql.Date(105,4,25));
    MUProtokol.getInstance().createProtokol(dm,1);
  }
}