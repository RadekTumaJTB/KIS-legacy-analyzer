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

public class TestExport extends AbsExcelDoklad 
{
  private ApplicationModule dm;
  private Number idDoklad;

  public TestExport(ApplicationModule dokladyModule,
                    Number idDoklad)
  {
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    init();
  }

  private void init() {
    setFileName ( idDoklad+"@Test.xlsx" );
    setFileRelativeName( "atest\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_FILES_PATH+"SablonaBilance.xlsx" );
  }
  
  private static  CellStyle styleOk, styleError;

  private void outputDetail(boolean genPS)
  {
    int listNr = genPS ? 6 : 8;
    int pocet = 60000;
    int rowNr=2;
    ViewObject vo;
    
    vo = dm.findViewObject("ViewExcelDetail1");
    vo.clearCache();
    if(genPS) 
    { vo.setWhereClause("ID_DOKLAD = "+ idDoklad +" AND POCATECNISTAV = '1'"); }
    else 
    { vo.setWhereClause("ID_DOKLAD = "+ idDoklad); }
    CellStyle style;
    
    if ( styleOk == null ) styleOk = wb.getSheetAt(6).getRow(3).getCell((short)1).getCellStyle(); 
    if ( styleError == null ) styleError = wb.getSheetAt(6).getRow(4).getCell((short)1).getCellStyle();
    
    if(listNr == 6) {                  
      clearCell(6,3,1);
      clearCell(6,4,1);
    }

    while ( vo.hasNext() && pocet-- > 0 ) {
      Row row = vo.next();
      rowNr++;
System.out.println("RowNr.:"+rowNr);
      int colNr = 1;
      
      Number idIntNum = (Number)row.getAttribute("IdInterni");
      //int idInterni = idIntNum==null?-1:idIntNum.intValue();
      double idInterni = idIntNum==null?-1:idIntNum.doubleValue(); //esc 19.5.2010      
      String testICO = (String)row.getAttribute("Testico");
      if ( idInterni>0 && testICO==null ) {
        style = styleOk;
      } else {
        style = styleError;
      }

      setCellValue( listNr, rowNr, colNr, idInterni, style );                        
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Textradek"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("Ucet"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SMena"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkalocal")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, ((Number)row.getAttribute("NdCastkamena")).doubleValue(), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("IdExtsystem"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SPopis"), null );
      java.sql.Date date = ((Date)row.getAttribute("Datum")).dateValue();
      String datum = ( date == null ) ? "" : date.toString();
      colNr++;
      setCellValue( listNr, rowNr, colNr, datum, null );               
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistrana"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaico"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaEo"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaicoEo"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaBanka"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaicoBanka"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaEmitent"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SExtprotistranaicoEmitent"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CMenovapozice"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("CUverovapozice"), null );
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String)row.getAttribute("SEvidovatprotistranu"), null );
      colNr++;
      if ( testICO != null ) {
        setCellValue( listNr, rowNr, colNr, testICO, styleError );
      }
    }
    if ( pocet <= 0 ) {
      setCellValue( listNr, rowNr+1, 0, "D A T A    N E J S O U   K O M P L E T N I" , null );
      setCellValue( listNr, rowNr+2, 0, "Pocet zaznamu prevysuje moznosti Excelu" , null );
    }
    vo.closeRowSet();
    dm.getTransaction().commit();
    //NEW
    //vo.remove();
  }

  protected boolean outputData () 
  {
    outputDetail(false);
    
    return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
      TestExport ed = new TestExport(dm, new Number(255097));
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
