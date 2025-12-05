package cz.jtbank.konsolidace.excel;

import oracle.jbo.*;
import oracle.jbo.domain.Number;
import oracle.jbo.client.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.FillPatternType;
import cz.jtbank.konsolidace.common.Constants;
import java.util.*;
import java.text.SimpleDateFormat;

public class ESExportBudgetMustky extends AbsExcelDoklad 
{
  private ApplicationModule am;
  private String whereSub = "";
  private Map cisSub = new HashMap();
  private Map cisTran = new HashMap();
  private List listSub = new ArrayList();
  private List listTran = new ArrayList();

  private java.sql.Date datum;
  private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
  private String dev;

  public ESExportBudgetMustky(ApplicationModule am, int idSubject, java.sql.Date datum, String dev)
  {
    this.am = am;
    this.datum = datum;
    this.dev = dev;
    whereSub = "ID = " + idSubject;
    init();
  }

  public ESExportBudgetMustky(ApplicationModule am, java.sql.Date datum, String dev)
  {
    this.am = am;
    this.datum = datum;
    this.dev = dev;
    init();
  }

  private void init() {
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaBudgetMustek.xlsx" );
    setFileName ( "BudgetMustek.xlsx" );
    setFileRelativeName( getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );

    ViewObject vo = am.findViewObject("KpCisSubject1");
    vo.clearCache();
    vo.setWhereClause(whereSub);
    vo.setOrderByClause("ID");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number) row.getAttribute("Id");
      String popis = (String) row.getAttribute("SPopis");
      listSub.add(id);
      cisSub.put(id, popis);
    }
    vo.closeRowSet();

    vo = am.findViewObject("KpCisTyptransakceView1");
    vo.clearCache();
    vo.setWhereClause("C_DEVELOPER = '"+dev+"' AND C_SPECIAL='0' AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number) row.getAttribute("Id");
      String kod = (String) row.getAttribute("SKod");
      String popis = (String) row.getAttribute("SPopis");
      listTran.add(id);
      cisTran.put(id, popis+" ("+kod+")");
    }
    vo.closeRowSet();
  }

  protected boolean outputData () 
  {
    CellStyle lightBlue = wb.createCellStyle();
    lightBlue.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    lightBlue.setFillPattern(FillPatternType.SOLID_FOREGROUND);
  
    CellStyle style = null;
    
    int listNr = 0, rowNr = 1, colNr = 0;

    Iterator iterTran = listTran.iterator();
    while(iterTran.hasNext()) 
    {
      Number idTran = (Number)iterTran.next();
      String popis = (String)cisTran.get(idTran);
      setCellValue(listNr,rowNr,colNr,popis,lightBlue);
      rowNr++;
    }
    colNr++;

    ViewObject vo = am.findViewObject("KpDefBudgetmustekView1");
    vo.clearCache();
    
    Iterator iter = listSub.iterator();
    while(iter.hasNext()) 
    {
      rowNr = 0;
      
      Number idSubject = (Number)iter.next();
      String popis = (String)cisSub.get(idSubject);
      if(rowNr==0) setCellValue(listNr,rowNr,colNr,popis,lightBlue);
      rowNr++;
      
      iterTran = listTran.iterator();
      while(iterTran.hasNext()) 
      {
        Number idTran = (Number)iterTran.next();
        setCellValue(listNr,rowNr,colNr,popis,style);

        String ucet="";
        vo.setWhereClause("ID_CISSUBJECT = " + idSubject +
                     " AND ID_CISTYPTRANSAKCE = " + idTran + 
                     " AND TO_DATE('"+sdf.format(datum)+"','dd.mm.yyyy') BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
        while(vo.hasNext()) 
        {
          Row row = vo.next();
          ucet += ((String) row.getAttribute("SUcet")) + '\n';
        }
        vo.closeRowSet();
        setCellValue(listNr,rowNr,colNr,ucet,style);

        rowNr++;
      }
      colNr++;
    }
    am.getTransaction().commit();
    
    return true;
  }

  public String getExportRedir() 
  {
    return "excelservlet/"+System.currentTimeMillis()+"?file=" + getFileRelativeName();
  }
  
  public static void main(String[] argv) 
  {
    try {
      ApplicationModule bm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.budget.BudgetModule","BudgetModuleLocal");

      ESExportBudgetMustky em = new ESExportBudgetMustky(bm, new java.sql.Date(System.currentTimeMillis()), "0");
      em.excelOutput();
      Runtime rt = Runtime.getRuntime();
      String[] callAndArgs = { "C:\\Program Files\\Microsoft Office\\OFFICE11\\EXCEL.EXE", "" };
      callAndArgs[1]=em.getFileAbsoluteName();
      Process pExcel = rt.exec(callAndArgs);
      //pExcel.waitFor();
      System.out.println("konec");
      System.exit(0);
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
  }
}