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

public class ESExportProjektDoklad extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProjektDoklad.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private int idProjektDoklad;

  private Number idProj;
  private String nazevProj;
  private String menaNaklady;
  private java.sql.Date datum;
  private String denni;
  private String nazevMatky;
  private int typBilance;

  private double bilanceRozdil = 0;
  private double sumaZiskZtrata = 0;
  
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  
  public ESExportProjektDoklad(ApplicationModule dokladyModule,
                               int idProjektDoklad)
  {
    dm = dokladyModule;
    this.idProjektDoklad = idProjektDoklad;
    init();
  }

  private void init() {
    ViewObject vo = dm.findViewObject("KpDatProjektdokladView2");
    vo.clearCache();
    vo.setWhereClause("ID = "+idProjektDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      idProj = (Number) row.getAttribute("IdKtgprojekt");
      datum = ((oracle.jbo.domain.Date) row.getAttribute("DtDatum")).dateValue();
      denni = "1".equals(row.getAttribute("CDenni")) ? "-DD" : "";
    }
    vo.closeRowSet();

    vo = dm.findViewObject("KpKtgProjektView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idProj);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevProj = (String) row.getAttribute("SNazev");
      menaNaklady = (String) row.getAttribute("SMenanaklady");
      typBilance = row.getAttribute("IdTypbilance")==null ? 0 : ((Number) row.getAttribute("IdTypbilance")).intValue();
    }
    vo.closeRowSet();
    
    if(typBilance == 1) nazevMatky = "VECHNY TRANSAKCE";
    else if(typBilance == 3) nazevMatky = "JEN NKLADY";

    vo = dm.findViewObject("KpRelProjektucspolView1");
    vo.clearCache();
    vo.setWhereClause("KpRelProjektucspol.ID_KTGPROJEKT = "+idProj+" AND C_POUZITHV = '1' AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_MATKAOD AND DT_MATKADO");
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevMatky = (String) row.getAttribute("SNazev");
    }
    else if(typBilance == 2) 
    {
      nazevMatky = "Nen vybrna matka u bilance s matkou!!!";
    }
    vo.closeRowSet();

    logger.info("ExportProjektDoklad:nazevProj="+nazevProj);  

    setFileName ( idProjektDoklad+"@BilanceProjekt_"+datum+denni+".xlsx" );
    setFileRelativeName( Constants.DIR_PROJEKTY_DOKLADY+"\\"+idProj+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    String sablona = "SablonaProjektBilance.xlsx";
    setSablona( Constants.SABLONY_FILES_PATH+sablona );
  }
  
  private void outputHeaders() 
  {
    CellStyle style = null;
    outputZahlavi(nazevProj,menaNaklady,datum);
    for(int listNr = 0; listNr<5; listNr++) {
      int colNr = 2;
      setCellValue(listNr,6,colNr,menaNaklady,style);
      colNr++;
      if(listNr==0) 
      {
        setCellValue(listNr,6,colNr,menaNaklady,style);
        colNr++;
        setCellValue(listNr,6,colNr,menaNaklady,style);
        colNr++;
      }
    }
  }

  private void outputDoklad() 
  {
    //nazev matky na list Kamil
    setCellValue(7,0,2,nazevMatky,null);

    CellStyle style = null;
    
    ViewObject vo = dm.findViewObject("VwKpProjektdokladView1");
    vo.clearCache();
    vo.setWhereClause("ID_PROJEKTDOKLAD = " + idProjektDoklad);
    int listNr;    
    int colNr;    
    int rowNr;    
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      double castkaNaklady = ((Number)row.getAttribute("NdCastkanaklady")).doubleValue();
      
      int koef = 1;//(list==1 || list==3) ? 1 : -1;

      listNr = list - 1;
      rowNr = radek + 6;
      colNr = 1 + sloupec;

      setCellValue(listNr,rowNr,colNr,koef*castkaNaklady,style);
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
  }

  private boolean allreadyRead = false;
  private CellStyle[] styles;
  private CellStyle[] localStyle;
  private CellStyle[] menaStyle;

  private void outputBilance() 
  {
    CellStyle style = null;
      
    int listNr = 5;
    int rowNr = 14;
    int colNr;
    
    int lastList = 0, lastRadek = 0;
    
    if(!allreadyRead) {
      allreadyRead = true;
      localStyle = new CellStyle[9];
      menaStyle = new CellStyle[9];
      for(short i=8; i<=16; i++) {
        localStyle[i-8] = wb.getSheetAt(listNr).getRow(rowNr).getCell(i).getCellStyle();
        menaStyle[i-8] = wb.getSheetAt(listNr).getRow(rowNr+1).getCell(i).getCellStyle();
        clearCell(listNr,rowNr,i);
        clearCell(listNr,rowNr+1,i);
      }
    }
    
    double sumaAkt = 0.0D, sumaAktOpr = 0.0D;
    double sumaPas = 0.0D, sumaPasOpr = 0.0D;
    
    rowNr=13;
    
    ViewObject vo = dm.findViewObject("VwKpProjektdokladView1");
    vo.clearCache();
    vo.setWhereClause("ID_PROJEKTDOKLAD = " + idProjektDoklad);
    vo.setOrderByClause("NL_PORADILIST, NL_RADEK, NL_SLOUPEC");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String listNazev = (String) row.getAttribute("List");
      int radek = ((Number)row.getAttribute("NlRadek")).intValue();
      int sloupec = ((Number)row.getAttribute("NlSloupec")).intValue();
      int list = ((Number)row.getAttribute("NlPoradilist")).intValue();
      int koef = (list==1 || list==3) ? 1 : -1;
      double castkaNaklady = koef * ((Number)row.getAttribute("NdCastkanaklady")).doubleValue();
      
      if(list!=lastList || radek!=lastRadek) rowNr++;
      if(radek!=1000) {

        if(list == 1) {
          if(sloupec==1) sumaAkt += castkaNaklady;
          //else sumaAktOpr += castkaNaklady;
        }
        else if(list == 2) {
          if(sloupec==1) sumaPas += castkaNaklady;
          //else sumaPasOpr += castkaNaklady;
        }
        if ( list == 5 && radek < 156 ) {
          sumaZiskZtrata += castkaNaklady;
        }
        styles = localStyle;
  
        colNr = 9;
        setCellValue(listNr,rowNr,colNr,listNazev,styles[0]);
        colNr+=2;
        setCellValue(listNr,rowNr,colNr,""+radek,styles[3]);
        colNr+=sloupec;
        setCellValue(listNr,rowNr,colNr,castkaNaklady,styles[4+sloupec-1]);
        colNr+=(5-sloupec);
        setCellValue(listNr,rowNr,colNr,menaNaklady,styles[8]);
  /*      
        if(list==2 && radek==123) 
        {
          for(int i=0; i<3; i++) 
          {
            Cell cc = wb.getSheetAt(1).getRow(129).getCell((short)(3+i));
            if(cc!=null) 
            {
              double value = cc.getCellFormula();
              setCellValue(listNr,rowNr,colNr-3+i,value,styles[4+sloupec-1]);
              sumaPasOpr += value;
            }
          }
        }
  */      
      }
      lastList = list;
      lastRadek = radek;
    }    
    vo.closeRowSet();

    setCellValue(listNr,8,12,sumaAkt,null);
    setCellValue(listNr,8,13,sumaAktOpr,null);
    setCellValue(listNr,9,12,sumaPas,null);
    setCellValue(listNr,9,13,sumaPasOpr,null);
    setCellValue(listNr,8,11,sumaAkt+sumaAktOpr,null);
    setCellValue(listNr,9,11,sumaPas+sumaPasOpr,null);
    bilanceRozdil = sumaAkt+sumaAktOpr+sumaPas+sumaPasOpr;
    setCellValue(listNr,10,11,bilanceRozdil,null);
  }

  private void outputDetail()
  {
    int listNr = 6;
    int pocet = Constants.MAX_POCET_RADKU_EXCEL;
    int rowNr=2;

    ViewObject vo = dm.findViewObject("VwKpProjektdokladdetailView1");
    vo.clearCache();
    vo.setWhereClause("ID_PROJEKTDOKLAD = " + idProjektDoklad);
    CellStyle style = null;
    
    Number hlpNum = null;
    
    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
      int colNr = 1;
      
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("Id"), style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Spolecnost"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Textradek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ucet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      hlpNum = (Number)row.getAttribute("NdCastkanaklady");
      if(hlpNum != null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue(), null );
      colNr++;
      hlpNum = (Number)row.getAttribute("NdCastkamena");
      if(hlpNum != null) setCellValue( listNr, rowNr, colNr, hlpNum.doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("IdExtsystem"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      java.sql.Date date = ((Date)row.getAttribute("Datum")).dateValue();
      String datum = ( date == null ) ? "" : date.toString();
      colNr++;
      setCellValue( listNr, rowNr, colNr, datum, null );               
      colNr++;
      boolean clenJTFG = "1".equals(row.getAttribute("Clenjtfgkons"));
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SUnifspol"), (clenJTFG ? styleJtfg : null) );
      colNr++;
      String lhlp = row.getAttribute("IdKtgodbor")==null ? "" : ((Number)row.getAttribute("IdKtgodbor")).toString();
      setCellValue( listNr, rowNr, colNr, lhlp, null );
      colNr++;
      lhlp = row.getAttribute("IdKtgprojekt")==null ? "" : ((Number)row.getAttribute("IdKtgprojekt")).toString();
      setCellValue( listNr, rowNr, colNr, lhlp, null );
      colNr++;
      lhlp = row.getAttribute("Xidsl")==null ? "" : ((Number)row.getAttribute("Xidsl")).toString();
      setCellValue( listNr, rowNr, colNr, lhlp, null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CMenovapozice"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CUverovapozice"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SEvidovatprotistranu"), null );
      colNr++;
      if(clenJTFG) setCellValue(listNr,rowNr,colNr,"len JTFG kons.",null);
	  colNr++;
	  setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SUcetoriginal"),null);
	  colNr++;
      setCellValue(listNr,rowNr,colNr,(String)row.getAttribute("SPopisoriginal"),null);

    }
    if ( pocet <= 0 ) {
      setCellValue( listNr, rowNr+1, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( listNr, rowNr+2, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
    vo.closeRowSet();
  }

  private void outputDetailAgr()
  {
    int listNr = 8;
    int rowNr=0;

    ViewObject vo = dm.findViewObject("VwKpProjektdokladdetailView1");
    vo.clearCache();
    vo.setWhereClause("ID_PROJEKTDOKLAD = " + idProjektDoklad);
    vo.setOrderByClause("UCETZACATEK, S_POPIS, DATUM");
    CellStyle style = null;

    Map ucty = new TreeMap();
    while ( vo.hasNext() ) {
      Row row = vo.next();

      String ucet = (String)row.getAttribute("Ucetzacatek");
      String popis = (String)row.getAttribute("SPopis");
      Number castka = (Number)row.getAttribute("NdCastkanaklady");
      if(castka==null) castka=new Number(0);

      Map radky = null;
      if(ucty.containsKey(ucet)) 
      {
        radky = (Map) ucty.get(ucet);
        if(radky.containsKey(popis)) 
        {
          Number c = (Number)radky.get(popis);
          c = c.add(castka);
          radky.put(popis, c);
        }
        else 
        {
          radky.put(popis, castka);
        }
      }
      else 
      {
        radky = new TreeMap();
        radky.put(popis, castka);
        ucty.put(ucet, radky);
      }
      
    }
    vo.closeRowSet();

    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    CellStyle blueGray = wb.createCellStyle();
    blueGray.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
    blueGray.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    double sumaUcet = 0.0,
           suma = 0.0;
           
    if(!ucty.isEmpty()) {
      Iterator iterUcty = ucty.keySet().iterator();
      while(iterUcty.hasNext()) 
      {
        String ucet = (String)iterUcty.next();
        setCellValue( listNr, rowNr, 0, ucet, lightBlue );
        Map radky = (Map) ucty.get(ucet);
        if(!radky.isEmpty()) {
          Iterator iterRadky = radky.keySet().iterator();
          sumaUcet = 0.0;
          while(iterRadky.hasNext()) 
          {
            String popis = (String) iterRadky.next();
            Number castka = (Number) radky.get(popis);
            setCellValue( listNr, rowNr, 1, popis, style );
            setCellValue( listNr, rowNr, 2, castka.doubleValue(), style );
            sumaUcet += castka.doubleValue();
            suma += castka.doubleValue();
            rowNr++;
          }
          setCellValue( listNr, rowNr, 0, "Celkem z "+ucet, lightBlue );
          setCellValue( listNr, rowNr, 1, "", lightBlue );
          setCellValue( listNr, rowNr, 2, sumaUcet, lightBlue );
          rowNr+=2;
        }
      }
      setCellValue( listNr, rowNr, 0, "Celkov souet", blueGray );
      setCellValue( listNr, rowNr, 1, "", blueGray );
      setCellValue( listNr, rowNr, 2, suma, blueGray );
    }
  }

  protected void deleteOldFiles() throws IOException {
    int indexDir = getFileAbsoluteName().lastIndexOf('\\');
    String dirName = getFileAbsoluteName().substring(0,indexDir);
    File dir = new File(dirName);
    
    String nm = getFileRelativeName().substring(getFileRelativeName().indexOf('@'));

    FileFilter ff = getDeleteFileFilter(nm);
    File[] arr = dir.listFiles(ff);
    if(arr != null) {
      for( int i=0; i<arr.length; i++ ) 
      {
        System.out.println("Mazani souboru "+arr[i].getName()+": "+arr[i].delete());        
      }
    }

    if(getFileRelativeName().indexOf("-DD")>-1) {
      ff = getDeleteFileFilter("-DD");
      arr = dir.listFiles(ff);
      if(arr != null) {
        for( int i=0; i<arr.length; i++ ) 
        {
          System.out.println("Mazani souboru "+arr[i].getName()+": "+arr[i].delete());        
        }
      }
    }
  }

  private CellStyle styleJtfg = null;
    
  protected boolean outputData () 
  {
    Font font = wb.createFont();
    font.setColor(IndexedColors.BLUE.getIndex());
    styleJtfg = wb.createCellStyle();
    styleJtfg.setFont(font);

    outputHeaders(); 
    logger.info("Dogenerovany hlavicky");
    outputDoklad();
    logger.info("Dogenerovany prvni listy");
    outputBilance();
    logger.info("Dogenerovan list bilance");
    outputDetail();
    logger.info("Dogenerovan list detail");
    outputDetailAgr();
    logger.info("Dogenerovan list detail agr.");

    return true;
  }
 
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      ESExportProjektDoklad ed = new ESExportProjektDoklad(dm,151922); //177032);
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
