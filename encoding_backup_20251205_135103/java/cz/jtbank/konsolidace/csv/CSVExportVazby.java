package cz.jtbank.konsolidace.csv;

import cz.jtbank.konsolidace.common.*;
import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;
import java.io.IOException;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;

public class CSVExportVazby extends AbsCsvDoklad 
{
  private ApplicationModule dm;
  private Number idDoklad;
  private Number ucetniSkupina;
  private java.sql.Date datum;

  private Number idKtgSpolecnost;
  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;

  public CSVExportVazby(ApplicationModule dokladyModule,
                        Number idDoklad,
                        Number ucetniSkupina)
  {
    logger.info("CSVExportVazby:idDoklad="+idDoklad+",ucetniSkupina="+ucetniSkupina+",datum="+datum);
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    this.ucetniSkupina = ucetniSkupina;

    init();
  }

  private void init() {
    ViewObject vo = dm.findViewObject("VwKpDokladzahlaviView1");
    vo.clearCache();
    vo.setWhereClause("DOKLADID = "+idDoklad);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("Spolecnostnazev");
      menaSpol = (String) row.getAttribute("SMena");
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
      idKtgSpolecnost = (Number) row.getAttribute("Ucetnispolecnostid");
      datum = ((oracle.jbo.domain.Date) row.getAttribute("DtDatum")).dateValue();
      //datum = new java.sql.Date((new java.util.Date(106,11,31)).getTime()); //PODVOD PRO NACTENI POC. STAVU
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    setFileName ( idDoklad+"@INTERCOMPANY"+ucetniSkupina+"_"+datum+".csv" );
    setFileRelativeName( souborPredponaSpol+"_"+idKtgSpolecnost+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }

  protected void addHeader() throws KisException, IOException
  {
    setValue("Invoice Reference");
    setComma();	
    setValue("D_RU");
    setComma();	
    setValue("Local Currency");
    setComma();	
    setValue("D_AC");
    setComma();	
    setValue("D_T1");
    setComma();	
    setValue("D_CU");
    setComma();	
    setValue("Flow");
    setComma();	
    setValue("P_AMOUNT");

    setNewLine();
  }


protected boolean outputData() throws KisException, IOException  {
	ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
	DBTransaction dbTran = sm.getDBTransaction();
	int radku = 0;
	try {
		java.sql.Statement st = dbTran.createStatement(0);
	
		String sqlSelect = 
			" select uf.s_ucet, uf.c_class, vv.id_ktgucetnispolecnostfrom, vv.id_ktgucetnispolecnostto, vv.id_doklad, " +
			" vv.s_mena, vv.id_cisucetunif, sum ( vv.nd_castkamena ) nd_castkamena, sum ( vv.nd_castkalocal ) nd_castkalocal " +
			" from  " +
			"	db_jt.kp_cis_ucetunif uf, " +
			"	( " +
			"      select  " +
			"         v.id_ktgucetnispolecnostfrom, NVL ( db_jt.f_getSpolecnostMatka ( v.id_ktgucetnispolecnostto, v.id_ktgucetniskupina, d.dt_datum ), v.id_ktgucetnispolecnostto ) id_ktgucetnispolecnostto, " +
			"         v.s_mena, v.id_cisucetunif, v.nd_castkamena, v.nd_castkalocal, d.id id_doklad " +
			"      from db_jt.kp_dat_dokladVazby v, db_jt.kp_dat_doklad d " +
			"      where d.id = v.id_doklad and v.id_ktgucetniskupina =  " + ucetniSkupina + "  and " +
			"      v.id_ktgucetnispolecnostfrom = d.id_ktgucetnispolecnost " + 
			"   ) vv " +
			" where " +
			"	uf.id = vv.id_cisucetunif and " +
			"   vv.id_ktgucetnispolecnostfrom = " + idKtgSpolecnost + " and " +
			"   vv.id_doklad = " + idDoklad +			
			" group by " +
			" 	vv.id_ktgucetnispolecnostfrom, vv.id_ktgucetnispolecnostto, vv.id_doklad, vv.s_mena, vv.id_cisucetunif, uf.s_ucet, uf.c_class ";

//System.out.println ( sqlSelect );
		java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
	
		while ( rs.next() ) {
			String mena = rs.getString("S_MENA");
			Number idFrom = new Number ( rs.getInt("ID_KTGUCETNISPOLECNOSTFROM") );
			String ucet = rs.getString("S_UCET");
			Number idTo = new Number ( rs.getInt("ID_KTGUCETNISPOLECNOSTTO") );
			String cls = rs.getString("C_CLASS");
			
			int koef = -1;
			if("A".equals(cls) || "3".equals(cls)) koef = 1;

			Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") ).multiply(koef);
			Number castkaMena = new Number ( rs.getDouble("ND_CASTKAMENA") ).multiply(koef);
			
      
      //esc 26.06.2017 IFRS9 			
      //02.10 2017 ucet +="00";
      
      
			setValueMesic(datum);
			setComma();
			setValue(idFrom);
			setComma();
			setValue(menaSpol);
			setComma();
			setValue(ucet);
			setComma();
			setValue(idTo);
			setComma();
			
			setValue(mena);
			setComma();
			setValue("M99");
			setComma();
			setValue(castkaMena);
			setNewLine();
			
			setValueMesic(datum);
			setComma();
			setValue(idFrom);
			setComma();
			setValue(menaSpol);
			setComma();
			setValue(ucet);
			setComma();
			setValue(idTo);
			setComma();
			setValue(mena);
			setComma();
			setValue("F99");
			setComma();
			setValue(castkaLocal);
			setNewLine();
			radku++;
		}
		rs.close();
		st.close();
	} catch ( Exception e ) {
		e.printStackTrace();
	}    
	return ( radku != 0 );
}


  protected boolean outputData_old() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatDokladvazbyView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND ID_KTGUCETNISKUPINA = "+ucetniSkupina+" AND ID_KTGUCETNISPOLECNOSTFROM = "+idKtgSpolecnost);
    vo.setOrderByClause("ID_KTGUCETNISPOLECNOSTFROM,ID_KTGUCETNISPOLECNOSTTO");
    while(vo.hasNext()) {
      Row row = vo.next();
      Number idFrom = (Number)row.getAttribute("IdKtgucetnispolecnostfrom");
      String ucet = (String)row.getAttribute("SUcetunif");
      Number idTo = (Number)row.getAttribute("IdKtgucetnispolecnostto");
      String mena = (String)row.getAttribute("SMena");
      String cls = (String)row.getAttribute("CClass");

      int koef = -1;
      if("A".equals(cls) || "3".equals(cls)) koef = 1;
      
      Number castkaMena = ((Number)row.getAttribute("NdCastkamena")).multiply(koef);
      Number castkaLocal = ((Number)row.getAttribute("NdCastkalocal")).multiply(koef);

      //esc 26.06.2017 IFRS9 			
      //02.10.2017ucet +="00";
      
      
      setValueMesic(datum);
      setComma();
      setValue(idFrom);
      setComma();
      setValue(menaSpol);
      setComma();
      setValue(ucet);
      setComma();
      setValue(idTo);
      setComma();
      setValue(mena);
      setComma();
      setValue("M99");
      setComma();
      setValue(castkaMena);
      setNewLine();

      setValueMesic(datum);
      setComma();
      setValue(idFrom);
      setComma();
      setValue(menaSpol);
      setComma();
      setValue(ucet);
      setComma();
      setValue(idTo);
      setComma();
      setValue(mena);
      setComma();
      setValue("F99");
      setComma();
      setValue(castkaLocal);
      setNewLine();
    }
    vo.closeRowSet();

    return true;
  }

  public static void main(String[] args)
  {
    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");


    //CSVExportVazby cSVExportVazby = new CSVExportVazby(dm, new Number(739058), new Number(2));
	CSVExportVazby cSVExportVazby = new CSVExportVazby(dm, new Number(1371037), new Number(8));
	
    try {
      cSVExportVazby.createCsv();
      cSVExportVazby.csvOutput();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }

/*
    CSVExportVazby cSVExportVazby;
    ViewObject vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("C_vazby = '1' AND DT_DATUM = '30.9.2007'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      Number idDoklad = (Number) row.getAttribute("Id");

      cSVExportVazby = new CSVExportVazby(dm, idDoklad, new Number(8));
      try {
        cSVExportVazby.createCsv();
        cSVExportVazby.csvOutput();
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
    vo.closeRowSet();
*/    
  }
}