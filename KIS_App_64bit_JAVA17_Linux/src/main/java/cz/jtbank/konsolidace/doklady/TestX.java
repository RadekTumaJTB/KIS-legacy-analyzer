package cz.jtbank.konsolidace.doklady;
import oracle.jbo.*;
import oracle.jbo.client.*;

public class TestX 
{
  public TestX()
  {
  }

  public static void main(String[] args) throws Exception
  {
    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
    ViewObject vo = dm.findViewObject("XMenaView1");
    try {
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      System.out.println(row.getAttribute("Id"));
    }
    vo.closeRowSet();
    } catch (Exception e) {e.printStackTrace();}
    
    System.out.println("--1");
    
    ViewObject vo2 = dm.findViewObject("XMenaView1");
    //vo2.clearCache();
    vo2.setWhereClause("ID <> 'CZK'");
    while(vo2.hasNext()) 
    {
      Row row2 = vo2.next();
      System.out.println(row2.getAttribute("Id"));
    }
    vo2.closeRowSet();
  }
}