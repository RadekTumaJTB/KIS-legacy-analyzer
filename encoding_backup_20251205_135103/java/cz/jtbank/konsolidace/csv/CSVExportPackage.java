package cz.jtbank.konsolidace.csv;

import cz.jtbank.konsolidace.common.Constants;
import cz.jtbank.konsolidace.common.KisException;

import java.io.IOException;

import java.sql.CallableStatement;

import java.text.SimpleDateFormat;

import java.util.HashSet;
import java.util.Iterator;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;

public class CSVExportPackage extends AbsCsvDoklad 
{
  private ApplicationModule dm;
  private Number idDoklad;
  private java.sql.Date datum;
  private boolean multisegment;

  private String period;

  private Number idKtgSpolecnost;
  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;

  private String flow;
  private String[] flowList;
  
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public CSVExportPackage(ApplicationModule dokladyModule,
                          Number idDoklad,
                          String flow)
  {
	dm = dokladyModule;
	init ( idDoklad, flow );
  }

  private boolean newExport; 
  
  private void init ( Number idDoklad, String flow)
  {
    logger.info("CSVExportPackage:idDoklad="+idDoklad+",flow="+flow);
    
    this.idDoklad = idDoklad;
    this.flow = flow;
    
    if(flow!=null) 
    {
      flowList = new String[] { flow };
      newExport = false;    
    }
    else 
    {
      flowList = new String[] {"F999","Y100"};//seznam flow, ktera chceme postupne vyexportovat
      //esc 26.6.2017 newExport = true;
    }
    
    if ( idDoklad != null ) init();
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
      multisegment = "1".equals(row.getAttribute("CMultisegment"));
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    period = sdfMesic.format(datum);

    setFileName ( idDoklad+"@PACKAGE_"+datum+".csv" );
    setFileRelativeName( souborPredponaSpol+"_"+idKtgSpolecnost+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }

  protected void addHeader() throws KisException, IOException
  {
// esc 2025 03  - zruseni prvniho sloupce
//    setValue("D_CA");
//    setComma();	
    setValue("D_DP");
    setComma();	
    setValue("D_PE");
    setComma();	
    setValue("D_RU");
    setComma();	
    setValue("D_ORU");
    setComma();	
    setValue("D_AC");
    setComma();	
    setValue("D_FL");
    setComma();	
    setValue("D_CU");
    setComma();	
    setValue("D_AU");
    setComma();	
    setValue("D_LE");
    setComma();	
    setValue("D_NU");
    setComma();	
    setValue("D_TO");
    setComma();	
    setValue("D_GO");
    setComma();	
    setValue("D_CP");
    setComma();	
    setValue("D_SH");
    setComma();	
    setValue("D_DCFS");
    setComma();	
    setValue("D_GA");
    setComma();	
    setValue("D_SEG");
    setComma();	
    setValue("D_MA");
    setComma();	
    setValue("D_TC");
    setComma();	
    setValue("D_DM");
    setComma();	
    setValue("D_DPT");
    setComma();	
    setValue("D_TY");
    setComma();	
    setValue("P_AMOUNT");
    setComma();	
    setValue("P_COMMENT");

    setNewLine();
  }

  CsvRow cacheZisk = null;
  
  protected void addRow( CsvRow aRow ) throws KisException, IOException {    
    addRow ( aRow, true );
  }
  
  protected void addRow( CsvRow aRow, boolean testuj) throws KisException, IOException
  {
    if ( aRow == null ) return;
    if ( testuj ) {
      if ( "EK000000100".equals ( aRow.ucet ) ) {
        if ( cacheZisk == null ) { 
          cacheZisk = aRow; 
        } else {
          //cacheZisk.castka += new Number ( aRow.castka.doubleValue() + cacheZisk.castka.doubleValue() );
          cacheZisk.castka = cacheZisk.castka.add(aRow.castka );
        }
        return;      
      }
    }      
    // esc 2025 03  - zruseni prvniho sloupce
    //setValue(aRow.category);
    //setComma();
    setValue(aRow.entryPeriod);
    setComma();
    setValue(aRow.period);
    setComma();
    setValue(aRow.idSpol);
    setComma();
    setValue(aRow.origIdSpol);
    setComma();
    setValue(aRow.ucet);
    setComma();
    setValue(aRow.flow);
    setComma();
    setValue(aRow.menaSpol);
    setComma();
    setValue(aRow.auditId);
    setComma();
    setValue(aRow.ledger);
    setComma();
    setValue(aRow.jnId);
    setComma();
    setValue(aRow.techOrig);
    setComma();
    setValue(aRow.geoOrig);
    setComma();
  	if ( aRow.type != null && ( !"110".equals(aRow.type)) )  {
		setValue(aRow.type);	
	} else {
		if ( aRow.protiId != null && aRow.protiId.intValue() == -987 )
		  setValue("YTP0000");
		else
		  setValue(aRow.protiId);
	}		  
  /*esc 12/2022	if ( aRow.type != null ) {
		setValue(aRow.type);	
	} else {
		if ( aRow.protiId != null && aRow.protiId.intValue() == -987 )
		  setValue("YTP0000");
		else
		  setValue(aRow.protiId);
	}		  
  */
    setComma();
    setValue(aRow.share);
    setComma();
    setValue(aRow.cashFlow);
    setComma();
    setValue(aRow.geoOrig2);
    setComma();
    setValue(aRow.projSegment);
    setComma();
    setValue(aRow.maturity);
    setComma();
    setValue(aRow.mena);
    setComma();
    setValue(aRow.dealMaker);
    setComma();
    setValue(aRow.department);
    setComma();
    	if (  "110".equals(aRow.type))  
          setValue( "110" );	
      else    
          setValue( "" );
/*esc 12/2022    setValue( "" ); // setValue(aRow.type);  Nechapu ale typ se pry dava do protiID  */
    setComma();
    setValue(aRow.castka);
    setComma();
    setValue(aRow.popis);
    if ( b_debug ) {
      setComma();
      setValue(aRow.debugInfo);
    }
	
	if ( aRow.protiId != null )
		allProtistrany.add( new Protistrana ( idDoklad, idKtgSpolecnost, aRow.protiId ) );
	if ( aRow.share != null )
		allProtistrany.add( new Protistrana ( idDoklad, idKtgSpolecnost, aRow.share ) );
	
    setNewLine();
  }
/*
  protected void addRow2(String category,
                        String entryPeriod,
                        String period,
                        Number idSpol,
                        Number origIdSpol,
                        String ucet,
                        String flow,
                        String menaSpol,
                        //
                        String auditId,
                        String ledger,
                        String jnId,
                        //
                        String techOrig,
                        String geoOrig,
                        //
                        Number protiId,
                        Number share,
                        String cashFlow,
                        String geoOrig2,
                        Number projSegment,
                        String maturity,
                        String mena,
                        String dealMaker,
                        String department,
                        String type,
                        //
                        Number castka,
                        String popis,
                        String debugInfo
                        ) throws KisException, IOException
  {
    setValue(category);
    setComma();
    setValue(entryPeriod);
    setComma();
    setValue(period);
    setComma();
    setValue(idSpol);
    setComma();
    setValue(origIdSpol);
    setComma();
    setValue(ucet);
    setComma();
    setValue(flow);
    setComma();
    setValue(menaSpol);
    setComma();
    setValue(auditId);
    setComma();
    setValue(ledger);
    setComma();
    setValue(jnId);
    setComma();
    setValue(techOrig);
    setComma();
    setValue(geoOrig);
    setComma();
    if ( protiId != null && protiId.intValue() == -987 )
      setValue("YTP0000");
    else
      setValue(protiId);
    setComma();
    setValue(share);
    setComma();
    setValue(cashFlow);
    setComma();
    setValue(geoOrig2);
    setComma();
    setValue(projSegment);
    setComma();
    setValue(maturity);
    setComma();
    setValue(mena);
    setComma();
    setValue(dealMaker);
    setComma();
    setValue(department);
    setComma();
    setValue(type);
    setComma();
    setValue(castka);
    setComma();
    setValue(popis);
    if ( b_debug ) {
      setComma();
      setValue(debugInfo);
    }
    setNewLine();
  }
*/
  protected void outputZustatkyLocal() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatZdaFlowZustatekLocalView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND S_KOD = '"+flow+"'");
    cacheZisk = null;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String ucet = (String)row.getAttribute("SUcet");
      if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      
      CsvRow csvRow = new CsvRow (
             "A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             castkaLocal,
             null,
             "outputZustatkyLocal");
             
        addRow ( csvRow );
    }
    vo.closeRowSet();
    addRow ( cacheZisk, false );
    
    if ( "Y100".equals(flow) ) return;
    
    ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;

    DBTransaction dbTran = sm.getDBTransaction();
    try {
      java.sql.Statement st = dbTran.createStatement(0);
      
      String sqlSelect = 
        "select " +
            "dp.nd_castkaLocal*db_jt.f_getSignBilanceList(nl_poradiList) nd_CastkaLocal, " +
            "dp.nd_castkaMena*db_jt.f_getSignBilanceList(nl_poradiList) nd_CastkaMena, " +
            "dp.s_mena " +
        "from " +
            "db_jt.KP_dat_DokladPolozka dp, " +
            "db_jt.KP_dat_Doklad kd " +
        "where " +
            "dp.id_doklad = kd.id and " +
            "dp.nl_sloupec = 1 and dp.nl_poradilist = 2 and " +
            "dp.id_doklad = " + idDoklad + " and dp.nl_radek = 123" ;
    java.sql.ResultSet rs =  st.executeQuery( sqlSelect );

      while ( rs.next() ) {
        String ucet = "EK000000200";
        String mena = rs.getString("S_MENA");
        Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
        Number castkaMena = new Number ( rs.getDouble("ND_CASTKAMENA") );

        CsvRow csvRow = new CsvRow (
               "A",
               period,
               period,
               idKtgSpolecnost,
               idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
               flow,
               menaSpol,
               "0PCK01",
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               castkaLocal,
               null,
               "outputZustatkyLocal_2");
        
          addRow ( csvRow );
                  // a jeste jednou s transakcni menou
//esc 2.10 - Pavel nechce EK...02 s trans. menou
//        csvRow.mena = menaSpol;
//        csvRow.debugInfo = "outputZustatkyLocal_zisk2";
//        addRow ( csvRow );

      }
      rs.close();
      st.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }    
  }

  protected void outputCartesisCode() throws KisException, IOException
  {

    ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
    DBTransaction dbTran = sm.getDBTransaction();

    try {

      java.sql.Statement st = dbTran.createStatement(0);


//System.out.println ( "doklad  " + idDoklad );      
      String sqlSelect = 
        "select dp.nd_castkaLocal, db.s_cartesiscoderow, db.s_cartesiscodecolumn, dp.s_mena, dp.nl_radek, dp.nl_poradiList " +
        "from db_jt.kp_dat_dokladPolozka dp, db_jt.kp_def_listBunka db, db_jt.kp_dat_doklad d, db_jt.kp_dat_doklad dd " +
        "where db.id = dp.id_deflistbunka and dp.id_doklad = dd.id and d.id = " + idDoklad + " and d.s_mena = dp.s_mena and dp.nd_castkalocal != 0 and " +
        "d.id_KtgUCetniSPolecnost = dd.id_KtgUCetniSPolecnost and " +
        "d.dt_Datum = dd.dt_Datum and " +
        "dd.nl_Krok = 10 and " +
        "d.id_subkonsolidace = dd.id_subkonsolidace" 
        ;

//logger.info("## SELECT# :  " + sqlSelect);      
//System.out.println ( "  " + sqlSelect );            
    java.sql.ResultSet rs =  st.executeQuery( sqlSelect );

      cacheZisk = null;
      while ( rs.next() ) {
        String mena = rs.getString("S_MENA");
        String rowCode = rs.getString("S_CARTESISCODEROW");   //      vzorka :  String rowCode = "STAT.E.0018";	
        String colCode = rs.getString("S_CARTESISCODECOLUMN"); //     vzorka String colCode = "S370";			
        

        int radek = rs.getInt("NL_RADEK");
        int poradiList = rs.getInt("NL_PORADILIST");
        int koef = 1;
        if ( poradiList == 1 && radek >= Constants.ROW_PODNIKATEL_PASIVA) 
          koef = -1;
//esc zmena znamienka pre STAT.R ucty  01.04.2008
		if ( poradiList == 2) 
          koef = -1;		  
//

        Number castkaLocal = new Number ( koef * rs.getDouble("ND_CASTKALOCAL") );

        CsvRow csvRow = new CsvRow (
               "A",
               period,
               period,
               idKtgSpolecnost,
               idKtgSpolecnost,
               rowCode,
               colCode,
               mena,
               "0PCK01",
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               castkaLocal,
               null,
               "outputCartesisCode");
        
          addRow ( csvRow );  
      }
      addRow ( cacheZisk, false );
      rs.close();
      st.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  //esc NOVY riadok do package CSV - systemovejsie by bolo dat ho do generovaneho dokladu a hodnotu dotiahnut ces mustek ...
  protected void outputCartesisCode_S370() throws KisException, IOException
  {

	//esc logger.info("## CSVExportPackage:idDoklad= "+idDoklad+" datum: "+ datum);
  
    ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;

    DBTransaction dbTran = sm.getDBTransaction();

    try {
	  //logger.info("## TRY 1");      
      java.sql.Statement st = dbTran.createStatement(0);

	  //
	  //esc novy select pre "Výsledok hospodárenia za úètovné obdobie bez menšinových podílù /+/-/   r. 001 - (r. 067 + r. 071 + r. 078 + r. 082 + r. 086 + r. 116)"
	  //esc v csv sa zobrazi ako kombinacia ucet=STAT.A.0018 /D_AC  a  flow=S370/ D_FL
	  //
	  String sqlSelect = 
	  		                               	
      "select sum(KP_DAT_DOKLADPOLOZKA.nd_castkalocal) ND_CASTKALOCAL " +                
			"from db_jt.KP_DAT_DOKLADPOLOZKA, db_jt.KP_DEF_LISTBUNKA, db_jt.KP_DAT_DOKLAD " + 
			"where "+
      //"        KP_DAT_DOKLADPOLOZKA.nl_radek between  1 and 121 and " + //suma activ 
      "       db_jt.KP_DAT_DOKLAD.nl_krok =10 and  " + 
			"	       KP_DAT_DOKLAD.dt_datum = to_date('" + datum + "','YYYY-MM-DD') " +
			"	   and KP_DAT_DOKLAD.id_ktgucetnispolecnost = "+ idKtgSpolecnost +
			"      and KP_DEF_LISTBUNKA.S_CARTESISCODECOLUMN is not null " + 
			"      and KP_DAT_DOKLADPOLOZKA.ND_CASTKALOCAL != 0 " +
			"      and KP_DEF_LISTBUNKA.ID = KP_DAT_DOKLADPOLOZKA.ID_DEFLISTBUNKA " + 
			"      and KP_DAT_DOKLADPOLOZKA.ID_DOKLAD = KP_DAT_DOKLAD.ID " +  	
			"      and KP_DAT_DOKLADPOLOZKA.nl_poradilist = '1' " +               //01.07.2011 			"      and KP_DAT_DOKLADPOLOZKA.nl_poradilist = '2' " +                  	
			"      and KP_DAT_DOKLADPOLOZKA.s_mena ='" + menaSpol +"'"
//			"      and KP_DAT_DOKLADPOLOZKA.s_mena in(select KP_KTG_UCETNISPOLECNOST.S_MENA from db_jt.KP_KTG_UCETNISPOLECNOST where id =  db_jt.KP_DAT_DOKLAD.id_ktgucetnispolecnost ) " 
		  ;

//esc logger.info("## TRY sel: " + sqlSelect);          

//System.out.println ( "  " + sqlSelect );            

	 java.sql.ResultSet rs =  st.executeQuery( sqlSelect );

      cacheZisk = null;



	  while ( rs.next() ) {
											//String mena = rs.getString("S_MENA");

        String rowCode = "STAT.E.0018";		//rs.getString("S_CARTESISCODEROW");
        String colCode = "S370";			//rs.getString("S_CARTESISCODECOLUMN");
        
											//int radek = rs.getInt("NL_RADEK");
											//int poradiList = rs.getInt("NL_PORADILIST");
		//int koef = -1;        				
    int koef = 1;
											//if ( poradiList == 1 && radek >= Constants.ROW_PODNIKATEL_PASIVA)           koef = -1;

        Number castkaLocal = new Number ( koef * rs.getDouble("ND_CASTKALOCAL") );
		
        CsvRow csvRow = new CsvRow (
               "A",
               period,
               period,
               idKtgSpolecnost,
               idKtgSpolecnost,
               rowCode,
               colCode,
               menaSpol, 		//mena,
               "0PCK01",
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
			   castkaLocal,
               null,
               "outputCartesisCode_S370");
        
          addRow ( csvRow );  
      }

	  addRow ( cacheZisk, false );
      rs.close();
      st.close();

    } catch ( Exception e ) {
      e.printStackTrace();
	  //esc 
	  logger.info("##ERR outputCartesisCode_S370 # idDoklad: " + idDoklad + " datum: " + datum + " :: " +e.getMessage());  
	}

  }

//esc oddelovac pre ladenie
protected void outputliiiiiiiiiine( String aText ) throws KisException, IOException
  {
    try {
      cacheZisk = null;
      
	    CsvRow csvRow = new CsvRow (
               aText,                            //String category;                      
               "--",  //period,                 String entryPeriod;                   
               "--",  //period,                 String period;                        
               null,  //idKtgSpolecnost,        Number idSpol;                        
               null,  //idKtgSpolecnost,        Number origIdSpol;                    
               "--",  //rowCode,                String ucet;                          
               "--",  //colCode,                String flow;                          
               "--",  //mena,                   String menaSpol;                      
               "--",  //"0PCK01",               String auditId;                       
               "--",  //null,                   String ledger;                        
               "--",  //null,                   String jnId;                          
               "--",  //null,                   String techOrig;                      
               "--",  //null,                   String geoOrig;                       
               null,  //null,                   Number protiId;                       
               null,  //null,                   Number share;                         
               "--",  //null,                   String cashFlow;                      
               "--",  //null,                   String geoOrig2;                      
               null,  //null,                   Number projSegment;                   
               "--",  //null,                   String maturity;                      
               "--",  //null,                   String mena;                          
               "--",  //null,                   String dealMaker;                     
               "--",  //null,                   String department;                    
               "--",  //null,                   String type;                          
               null,  //castkaLocal,            Number castka;                        
               "--",  //null,                   String popis;                         
               "outputliiiiiiiiiine");         //String debugInfo;
        
          addRow ( csvRow );  
      
      addRow ( cacheZisk, false );

    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

//
  protected void outputZustatky() throws KisException, IOException
  { 
    ViewObject vo = dm.findViewObject("KpDatZdaFlowZustatekView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND S_KOD = '"+flow+"'");
    cacheZisk = null;
    while(vo.hasNext()  ) 
    {
      Row row = vo.next();      
      String ucet = (String)row.getAttribute("SUcet");
      if  ( "OAZ00000100".equals( ucet ) ) continue;
      //esc 03.09.2008 if ( "EK0000002".equals ( ucet ) ) ucet = "EK0000001";	  	  
	  if ( "E".equals ( ucet.substring(0,1)) ) continue;		  
      
	  String mena = (String)row.getAttribute("SMena");
      //Number castkaMena = (Number)row.getAttribute("NdCastkamena");
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      
      CsvRow csvRow = new CsvRow (
             "A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             mena,
             null,
             null,
             null,
             castkaLocal,
             null,
             "outputZustatky");
             
        addRow ( csvRow );
    }
    vo.closeRowSet();
    addRow ( cacheZisk, false );
  }

  protected void outputProtistranyLocal() throws KisException, IOException  {
    ViewObject vo = dm.
	findViewObject("KpDatZdaFlowProtistranaLocalView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND S_KOD = '"+flow+"' and c_shares != '1'");
    cacheZisk = null;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String ucet = (String)row.getAttribute("SUcet");
      if  ( "OAZ00000100".equals( ucet ) ) continue;
      if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      Number idProti = (Number)row.getAttribute("IdKtgucetnispolecnostto");
      
      
      CsvRow csvRow = new CsvRow (
             "A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             idProti,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             castkaLocal,
             null,
             "outputProtistranyLocal");
             
        addRow ( csvRow );
    }
    vo.closeRowSet();
    addRow ( cacheZisk, false );
  }

  protected void outputSharesLocal() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatZdaFlowProtistranaLocalView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND S_KOD = '"+flow+"' and c_shares = '1' ");
    cacheZisk = null;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String ucet = (String)row.getAttribute("SUcet");
      if  ( "OAZ00000100".equals( ucet ) ) continue;
      if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      Number idProti = (Number)row.getAttribute("IdKtgucetnispolecnostto");
      
	  Number idKoupenoOd = (Number)row.getAttribute("Koupenood");
      
	  if ( idProti.intValue() > 9999  ) continue; //esc 03.09.2008 externe spolecnosti vvyhodit
	  
      CsvRow csvRow = new CsvRow (
             "A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             null, //esc 03.09.2008 idKoupenoOd,
             idProti,             
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             null,
             castkaLocal,
             null,
             "outputSharesLocal");
             
        addRow ( csvRow );
    }
    vo.closeRowSet();
    addRow ( cacheZisk, false );
	
  }


  protected void outputShares() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatZdaFlowProtistranaView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND S_KOD = '"+flow+"' and c_shares = '1'");
    cacheZisk = null;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String ucet = (String)row.getAttribute("SUcet");
      if  ( "OAZ00000100".equals( ucet ) ) continue;
      if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
      String mena = (String)row.getAttribute("SMena");
      //Number castkaMena = (Number)row.getAttribute("NdCastkamena");
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      Number idProti = (Number)row.getAttribute("IdKtgucetnispolecnostto");
      Number idKoupenoOd = (Number)row.getAttribute("Koupenood");      
	  	  
	  if ( idProti.intValue() > 9999  ) continue; //esc 03.09.2008 externe spolecnosti vvyhodit
      
	  CsvRow csvRow = new CsvRow (
             "A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             null, //esc 03.09.2008 idKoupenoOd,
             idProti,                                     
             null,
             null,
             null,
             null,
             mena,
             null,
             null,
             null,
             castkaLocal,
             null,
             "outputShares");
             
          addRow ( csvRow );
    }
    vo.closeRowSet();
    addRow ( cacheZisk, false );
  }

  protected void outputProtistrany() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatZdaFlowProtistranaView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND S_KOD = '"+flow+"' and c_shares != '1'");
    cacheZisk = null;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String ucet = (String)row.getAttribute("SUcet");
      if  ( "OAZ00000100".equals( ucet ) ) continue;
      if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
      String mena = (String)row.getAttribute("SMena");
      //Number castkaMena = (Number)row.getAttribute("NdCastkamena");
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      Number idProti = (Number)row.getAttribute("IdKtgucetnispolecnostto");
      
      CsvRow csvRow = new CsvRow (
             "A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             idProti,                        
             null,
             null,
             null,
             null,
             null,
             mena,
             null,
             null,
             null,
             castkaLocal,
             null,
             "outputProtistrany");
             
        addRow ( csvRow );
    }
    vo.closeRowSet();
    addRow ( cacheZisk, false );
  }

  protected void outputSegmentyLocal() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatZdaFlowSegmentLocalView1");
    vo.clearCache();
    vo.setWhereClause("ID_DOKLAD = "+idDoklad+" AND S_KOD = '"+flow+"'");
    cacheZisk = null;
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String ucet = (String)row.getAttribute("SUcet");
      if  ( "OAZ00000100".equals( ucet ) ) continue;
      if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      Number idSegment = (Number)row.getAttribute("IdKtgsubkonsolidace");
      
      CsvRow csvRow = new CsvRow (
             "A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             new Number(-987),
             null,
             null,
             null,
             idSegment,
             null,
             null,
             null,
             null,
             null,
             castkaLocal,
             null,
             "outputSegmentyLocal");
             
        addRow ( csvRow );
    }
    vo.closeRowSet();
    addRow ( cacheZisk, false );
  }

	private void saveProtistrany() {

		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
	
		DBTransaction dbTran = sm.getDBTransaction();
		try {
	  
			String sqlSelect = 
				"select count(*) from " +
				"db_jt.kp_rep_CartesisProtistrana " +
				"where dt_datum = ? and id_ktgSpolecnost = ?";

			String sqlInsert = 
				"insert into db_jt.kp_rep_CartesisProtistrana ( dt_datum, id_ktgSpolecnost, info ) values ( ?, ?, ? )";

			CallableStatement st = dbTran.createCallableStatement(sqlSelect, 0);
			CallableStatement stIns = dbTran.createCallableStatement(sqlInsert, 0);
			java.sql.ResultSet rs = null;
			
			for ( Iterator i = allProtistrany.iterator(); i.hasNext(); ) {
				Protistrana p = (Protistrana) i.next();
				
				st.setDate ( 1, datum );
				st.setInt ( 2, p.id_proti.intValue() );
				rs = st.executeQuery();
		
				if ( rs.next() ) {
					int pocet = rs.getInt(1);

					if ( pocet == 0 ) {
						stIns.setDate ( 1, datum );
						stIns.setInt ( 2, p.id_proti.intValue() );
						stIns.setString ( 3, p.getInfo() );
						stIns.execute();
						dbTran.commit();
					}
				}
				
			}
			if ( rs != null ) rs.close();
			if ( st != null ) st.close();
			if ( stIns != null ) stIns.close();
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
	}	

	/*
	 * zustatek unifikovaneho uctu v lokalni ménì s protistranou pro ucty, které jsou oznaceny jako CounterpartiesDetail 
	 * a které splnuji danou vysi materiality (navíc sloupec D_CP) 
	 */
	protected void outputLimitCounterPartiesLocal() throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
/* archiv s tim koupeno		
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "a.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKAMENA) ND_CASTKAMENA, " +
					   "nvl(a.ID_KTGUCETNISPOLECNOST,a.ID_KTGSPOLECNOST) ID_KTGUCETNISPOLECNOSTTO, " +
					  "(  " +
						 "select mu.id_ktgUcetniSpolecnostKoupeno " + 
						 "from db_jt.kp_dat_majetkovaucast mu, db_jt.kp_ktg_fininvesticeemise fe, db_jt.kp_ktg_financniinvestice fi " + 
						 "where  " +
						 "rownum = 1 and " +
						 "fe.id = mu.id_ktgfininvesticeemise and " +
						 "fi.id = fe.id_ktgfinancniinvestice and " +         
						 "mu.id_ktgUcetniSpolecnost = d.id_ktgUcetniSpolecnost and " +
						 "a.id_ktgSpolecnost = fi.id_ktgSpolecnost and  " +
						 "mu.s_ucet = a.s_ucetunif and " +
						 "sysdate between mu.dt_platnostod and mu.dt_platnostdo and " +
						 "mu.id_ktgUcetniSpolecnostKoupeno is not null ) koupenoOd, " +
					"uu.c_shares " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
				   "kurzy.f_getMenaKurs ( 'ÈNB', a.s_mena, 'SKK', d.dt_datum ) *  a.nd_castkalocal > uu.nd_limitcounterpartiesdetal and " +
				   "a.s_mena = d.s_mena and " +
				   "d.id = " + idDoklad +			
				"group by  " +
					"a.ID_DOKLAD, a.ID_KTGUCETNISPOLECNOST, a.S_MENA, a.ID_KTGSPOLECNOST, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET, uu.c_shares,	 " +
					"d.id_ktgUcetniSpolecnost";
*/			

			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "d.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL, " +
					   "a.ID_KTGSPOLECNOST ID_KTGUCETNISPOLECNOSTTO, " +
					"uu.c_shares " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
//				   "a.s_mena = d.s_mena and " +
				   " db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 and " +
				   "d.id = " + idDoklad +			
//esc19.05 a  01.04.2008 bez sum > limit a podla spolocnosti 
				" AND a.ID_KTGSPOLECNOST   IN ( "+
				"select aa.ID_KTGSPOLECNOST " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace aa, " +
				   "db_jt.kp_rel_ucetUnifFlow rell, " +
				   "db_jt.kp_cis_flow ff, " +
				   "db_jt.kp_cis_ucetUnif uuu, " +
				   "db_jt.kp_dat_doklad dd " +
				"where  " +
					"dd.id = aa.id_doklad and " +
					"rell.ID_CISUCETUNIF = aa.ID_CISUCETUNIF and  " +
					"rell.ID_CISFLOW = ff.ID and  " +
					"aa.ID_CISUCETUNIF = uuu.id and  " +
					"aa.ID_KTGSPOLECNOST is not null and  " +
					"ff.s_kod = '" + flow + "' and " +
				   " db_jt.f_exportCartesis ( dd.id, dd.id_ktgUcetniSpolecnost, uuu.id, aa.id_ktgUcetniSpolecnost ) > 0 and " +
					" uuu.s_ucet = uu.s_ucet and "        +
				   "dd.id = " + idDoklad +			
				"group by  " +
					"aa.ID_DOKLAD, aa.ID_KTGSPOLECNOST, aa.ID_KTGUCETNISPOLECNOST, aa.s_ucetunif, " +
					"ff.ID, ff.S_KOD, uuu.S_UCET, uuu.c_shares, uuu.nd_limitcounterpartiesdetal, " +
					"dd.id_ktgUcetniSpolecnost, dd.dt_datum, dd.s_mena " +
				"having " +
				"Abs(sum(aa.nd_castkalocal)) / kurzy.f_getMenaKurs ( 'ÈNB', dd.s_mena, 'SKK', dd.dt_datum ) > uuu.nd_limitcounterpartiesdetal "	+
				" ) " +
//esc 19.05 a 01.04.2008

				"group by  " +
					"a.ID_DOKLAD, a.ID_KTGSPOLECNOST, a.ID_KTGUCETNISPOLECNOST, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET, uu.c_shares, uu.nd_limitcounterpartiesdetal, " +
					"d.id_ktgUcetniSpolecnost, d.dt_datum, d.s_mena " 
//					+				"having " +				"Abs(sum(a.nd_castkalocal)) / kurzy.f_getMenaKurs ( 'ÈNB', d.s_mena, 'SKK', d.dt_datum ) > uu.nd_limitcounterpartiesdetal "					
				;

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
			while ( rs.next() ) {
				
				String ucet = rs.getString("S_UCET");
				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
				String mena = rs.getString("S_MENA");
				Number idProti = new Number ( rs.getInt("ID_KTGUCETNISPOLECNOSTTO") );
//				Number idKoupenoOd = new Number ( rs.getInt("KOUPENOOD") );
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",
												period,
												period,
												idKtgSpolecnost,
												idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
												flow,
												menaSpol,
												"0PCK01",
												null,
												null,
												null,
												null,
												idProti,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												castkaLocal,
												null,
												"outputLimitCounterPartiesLocal");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}

/*
 *  zustatek unifikovaneho uctu v originalni ménì ve vysi ekvivalentu v lokalni ménì s protistranou pro ucty, 
 *  které jsou oznaceny jako CounterpartiesDetail a které splnuji danou vysi materiality (navíc sloupec D_CP a D_TC)
 */
	protected void outputLimitCounterPartiesMena() throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "a.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL, " +
					   "a.ID_KTGSPOLECNOST ID_KTGUCETNISPOLECNOSTTO, " +
					"uu.c_shares " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
//				   "a.s_mena != d.s_mena and " +
				   " db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 and " +
				   "d.id = " + idDoklad +			
//esc19.05 a  01.04.2008 bez sum > limit a podla spolocnosti 
				" AND a.ID_KTGSPOLECNOST   IN ( "+
				" select aa.ID_KTGSPOLECNOST " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace aa, " +
				   "db_jt.kp_rel_ucetUnifFlow rell, " +
				   "db_jt.kp_cis_flow ff, " +
				   "db_jt.kp_cis_ucetUnif uuu, " +
				   "db_jt.kp_dat_doklad dd " +
				"where  " +
					"dd.id = aa.id_doklad and " +
					"rell.ID_CISUCETUNIF = aa.ID_CISUCETUNIF and  " +
					"rell.ID_CISFLOW = ff.ID and  " +
					"aa.ID_CISUCETUNIF = uuu.id and  " +
					"aa.ID_KTGSPOLECNOST is not null and  " +
					"ff.s_kod = '" + flow + "' and " +
				    " db_jt.f_exportCartesis ( dd.id, dd.id_ktgUcetniSpolecnost, uuu.id, aa.id_ktgUcetniSpolecnost ) > 0 and " +
					" uuu.s_ucet = uu.s_ucet and "        +
				    " dd.id = " + idDoklad +			
				"group by  " +
					"aa.ID_DOKLAD, aa.ID_KTGSPOLECNOST, aa.ID_KTGUCETNISPOLECNOST, aa.s_ucetunif, " +
					"ff.ID, ff.S_KOD, uuu.S_UCET, uuu.c_shares, uuu.nd_limitcounterpartiesdetal, " +
					"dd.id_ktgUcetniSpolecnost, dd.dt_datum, dd.s_mena " +
				"having " +
				"Abs(sum(aa.nd_castkalocal)) / kurzy.f_getMenaKurs ( 'ÈNB', dd.s_mena, 'SKK', dd.dt_datum ) > uuu.nd_limitcounterpartiesdetal "	+
				" ) " +
//esc 19.05 a 01.04.2008
				" group by  " +
					"a.ID_DOKLAD, a.ID_KTGUCETNISPOLECNOST, a.S_MENA, a.ID_KTGSPOLECNOST, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET, uu.c_shares, uu.nd_limitcounterpartiesdetal, " +
					"d.id_ktgUcetniSpolecnost, d.dt_datum, d.s_mena " 
				;
//esc 19.05 rusim				+ "having " +				" Abs( sum(a.nd_castkalocal)) / kurzy.f_getMenaKurs ( 'ÈNB', d.s_mena, 'SKK', d.dt_datum ) > uu.nd_limitcounterpartiesdetal "									

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
			while ( rs.next() ) {
				
				String ucet = rs.getString("S_UCET");
				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
				String mena = rs.getString("S_MENA");
				Number idProti = new Number ( rs.getInt("ID_KTGUCETNISPOLECNOSTTO") );
//				Number idKoupenoOd = new Number ( rs.getInt("KOUPENOOD") );
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",
												period,
												period,
												idKtgSpolecnost,
												idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
												flow,
												menaSpol,
												"0PCK01",
												null,
												null,
												null,
												null,
												idProti,
												null,
												null,
												null,
												null,
												null,
												mena,
												null,
												null,
												null,
												castkaLocal,
												null,
												"outputLimitCounterPartiesMena");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}

	protected void outputCountryLocal() throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
					   "s.s_country, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "d.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_ktg_spolecnost s, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"s.id = a.id_ktgSpolecnost and " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
//				   "a.s_mena = d.s_mena and " +
				   "uu.c_country = '1' and " +
//esc 18.01.2008 zadani z 5.11				   " db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 and " +
				   "( db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 " +
				   "  OR db_jt.f_is_interni_equity ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0) and "  +
				   "d.id = " + idDoklad +							   
				"group by  " +
					"a.ID_DOKLAD, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET,  " +
					"s.s_country, d.s_mena"
				;

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
			while ( rs.next() ) {

				if ( "Y100".equals(flow) ) continue;				//esc
				
				String ucet = rs.getString("S_UCET");
				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
				String mena = rs.getString("S_MENA");
				String zeme = rs.getString("S_COUNTRY");
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",
												period,
												period,
												idKtgSpolecnost,
												idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
												flow,
												menaSpol,
												"0PCK01",
												null,
												null,
												null,
												null,
												new Number(-987), //null,	--esc D_CP vraj sa nastavi na "YTP0000"
												null,
												null,
												zeme,  //D_GA
												null,
												null,
												null, //mena,
												null,
												null,
												null, //null, //TYPE --esc druha moznost ... "YTP0000" .. podla toho sa nastavi D_CP ...
												castkaLocal,
												null,
												"outputCountryLocal");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}

	protected void outputCountryMena() throws KisException, IOException {
		if (true) return; //esc 19.12.07
		
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
					   "s.s_country, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "a.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_ktg_spolecnost s, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"s.id = a.id_ktgSpolecnost and " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
//esc 18.01.2008 zadani z 5.11				   " db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 and " +
				   "( db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 " +
				   "  OR db_jt.f_is_interni_equity ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0) and "  +
				   
//				   "a.s_mena != d.s_mena and " +
				   "uu.c_country = '1' and " +
				   "d.id = " + idDoklad +							   			
				"group by  " +
					"a.ID_DOKLAD, a.S_MENA, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET,  " +
					"d.id_ktgUcetniSpolecnost, d.s_mena, s.s_country"
				;

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
			while ( rs.next() ) {
				
				String ucet = rs.getString("S_UCET");
				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
				String mena = rs.getString("S_MENA");
				String zeme = rs.getString("S_COUNTRY");
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",
												period,
												period,
												idKtgSpolecnost,
												idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
												flow,
												menaSpol,
												"0PCK01",
												null,
												null,
												null,
												null,
												new Number(-987), //null,	--esc D_CP vraj sa nastavi na "YTP0000"
												null,
												null,
												zeme,   //D_GA
												null,
												null,
												mena,
												null,
												null,
												null, //null, //TYPE --esc druha moznost ... "YTP0000" .. podla toho sa nastavi D_CP ...
												castkaLocal,
												null,
												"outputCountryMena");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}

	protected void outputCountryTypeLocal() throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
//bck esc 30.01.2009   "db_jt.f_getKodSubjectTyp(s.s_kodkategorieprotistrana) typ, " +
					   "db_jt.f_getKodSubjectTyp( nvl(s.s_kodkategorieprotistrana_2,s.s_kodkategorieprotistrana) ) typ, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "d.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_ktg_spolecnost s, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"s.id = a.id_ktgSpolecnost and " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
//				   "a.s_mena = d.s_mena and " +
				   "uu.c_typecounterparties = '1' and " +
//esc 18.01.2008 zadani z 5.11				   " db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 and " +
				   "( db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 " +
				   "  OR db_jt.f_is_interni_equity ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0) and "  +
				   "d.id = " + idDoklad +					
//esc 01.04.2008 bez sum > limit a podla spolocnosti 
				"AND a.ID_KTGSPOLECNOST  NOT IN ( "+
				"select aa.ID_KTGSPOLECNOST " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace aa, " +
				   "db_jt.kp_rel_ucetUnifFlow rell, " +
				   "db_jt.kp_cis_flow ff, " +
				   "db_jt.kp_cis_ucetUnif uuu, " +
				   "db_jt.kp_dat_doklad dd " +
				"where  " +
					"dd.id = aa.id_doklad and " +
					"rell.ID_CISUCETUNIF = aa.ID_CISUCETUNIF and  " +
					"rell.ID_CISFLOW = ff.ID and  " +
					"aa.ID_CISUCETUNIF = uuu.id and  " +
					"aa.ID_KTGSPOLECNOST is not null and  " +
					"ff.s_kod = '" + flow + "' and " +
				   " db_jt.f_exportCartesis ( dd.id, dd.id_ktgUcetniSpolecnost, uuu.id, aa.id_ktgUcetniSpolecnost ) > 0 and " +
					" uuu.s_ucet = uu.s_ucet and "        +
				   "dd.id = " + idDoklad +			
				"group by  " +
					"aa.ID_DOKLAD, aa.ID_KTGSPOLECNOST, aa.ID_KTGUCETNISPOLECNOST, aa.s_ucetunif, " +
					"ff.ID, ff.S_KOD, uuu.S_UCET, uuu.c_shares, uuu.nd_limitcounterpartiesdetal, " +
					"dd.id_ktgUcetniSpolecnost, dd.dt_datum, dd.s_mena " +
				"having " +
				"Abs(sum(aa.nd_castkalocal)) / kurzy.f_getMenaKurs ( 'ÈNB', dd.s_mena, 'SKK', dd.dt_datum ) > uuu.nd_limitcounterpartiesdetal "	+
				" ) " +
//esc 01.04.2008
				"group by  " +
					"a.ID_DOKLAD, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET,  " +
					"d.id_ktgUcetniSpolecnost, d.s_mena, "+
//bck esc30.01.0209 db_jt.f_getKodSubjectTyp(s.s_kodkategorieprotistrana)"					
					" db_jt.f_getKodSubjectTyp( nvl(s.s_kodkategorieprotistrana_2,s.s_kodkategorieprotistrana) )"
				;

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
			while ( rs.next() ) {
				
				String ucet = rs.getString("S_UCET");
				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
				String mena = rs.getString("S_MENA");
				String typ = "IFRSTP-" + rs.getString("TYP");
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",
												period,
												period,
												idKtgSpolecnost,
												idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
												flow,
												menaSpol,
												"0PCK01",
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												typ,
												castkaLocal,
												null,
												"outputCountryTypeLocal");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}


	protected void outputCountryTypeMena() throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
//bck  esc 30.01.2009  "db_jt.f_getKodSubjectTyp(s.s_kodkategorieprotistrana) typ, " +
					   "db_jt.f_getKodSubjectTyp( nvl(s.s_kodkategorieprotistrana_2,s.s_kodkategorieprotistrana) ) typ, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
 					   "a.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_ktg_spolecnost s, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"s.id = a.id_ktgSpolecnost and " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
//				   "a.s_mena != d.s_mena and " +
				   "uu.c_typecounterparties = '1' and " +
//esc 18.01.2008 zadani z 5.11				   " db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 and " +
				   "( db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 " +
				   "  OR db_jt.f_is_interni_equity ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0) and "  +
				   "d.id = " + idDoklad +	
//esc 01.04.2008 bez sum > limit a podla spolocnosti 
				"AND a.ID_KTGSPOLECNOST  NOT IN ( "+
				"select aa.ID_KTGSPOLECNOST " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace aa, " +
				   "db_jt.kp_rel_ucetUnifFlow rell, " +
				   "db_jt.kp_cis_flow ff, " +
				   "db_jt.kp_cis_ucetUnif uuu, " +
				   "db_jt.kp_dat_doklad dd " +
				"where  " +
//esc 19.05                    "aa.s_mena = a.s_mena and "+
					"dd.id = aa.id_doklad and " +
					"rell.ID_CISUCETUNIF = aa.ID_CISUCETUNIF and  " +
					"rell.ID_CISFLOW = ff.ID and  " +
					"aa.ID_CISUCETUNIF = uuu.id and  " +
					"aa.ID_KTGSPOLECNOST is not null and  " +
					"ff.s_kod = '" + flow + "' and " +
				    " db_jt.f_exportCartesis ( dd.id, dd.id_ktgUcetniSpolecnost, uuu.id, aa.id_ktgUcetniSpolecnost ) > 0 and " +
					" uuu.s_ucet = uu.s_ucet  and " +
				   "dd.id = " + idDoklad +			
				"group by  " +
					"aa.ID_DOKLAD, aa.ID_KTGUCETNISPOLECNOST, 					aa.ID_KTGSPOLECNOST, aa.s_ucetunif, " +
//esc 19.05					aa.S_MENA, 
					"ff.ID, ff.S_KOD, uuu.S_UCET, uuu.c_shares, uuu.nd_limitcounterpartiesdetal, " +
					"dd.id_ktgUcetniSpolecnost, dd.dt_datum, dd.s_mena " +
				"having " +
				" Abs( sum(aa.nd_castkalocal)) / kurzy.f_getMenaKurs ( 'ÈNB', dd.s_mena, 'SKK', dd.dt_datum ) > uu.nd_limitcounterpartiesdetal "+
				" ) "+
//esc 01.04.2008				   			   
				"group by  " +
					"a.ID_DOKLAD, a.S_MENA, a.s_ucetunif, " +
					"a.ID_DOKLAD, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET,  " +
					"d.id_ktgUcetniSpolecnost, " +
//bck esc30.01.0209 db_jt.f_getKodSubjectTyp(s.s_kodkategorieprotistrana)"					
					"db_jt.f_getKodSubjectTyp( nvl(s.s_kodkategorieprotistrana_2,s.s_kodkategorieprotistrana ) )" 					
				;

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
			while ( rs.next() ) {
				
				String ucet = rs.getString("S_UCET");
				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
				String mena = rs.getString("S_MENA");
				String typ = "IFRSTP-" + rs.getString("TYP");
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",
												period,
												period,
												idKtgSpolecnost,
												idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
												flow,
												menaSpol,
												"0PCK01",
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												null,
												mena,
												null,
												null,
												typ,
												castkaLocal,
												null,
												"outputCountryTypeMena");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}


/* esc 10-2022  */
protected void outputCountryRisk() throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
					   "s.s_country, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "d.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
/*				   "db_jt.kp_cis_ucetUnif uu, " +*/
				   "db_jt.VW_CIS_UCETUNIF_EXT uu, " +
				   "db_jt.kp_ktg_spolecnost s, " +
				   "db_jt.kp_dat_doklad d " +
/*           " , db_jt.EXT_CRT_DWH_CT_ACCOUNT_RISK crt " +  */
				"where  " +
					"s.id = a.id_ktgSpolecnost and " +
          "s.s_country is NOT NULL and " +   /*esc */
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
				   "uu.c_country = '1' and " +
				   "( db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 " +
				   "  OR db_jt.f_is_interni_equity ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0) and "  +
				   "d.id = " + idDoklad +							   
/*           " and uu.S_UCET=crt.name " +*/
				"group by  " +
					"a.ID_DOKLAD, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET,  " +
					"s.s_country, d.s_mena"
				;

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
    int RSsize = 0;
    try {
        RSsize = rs.getRow();
    outputliiiiiiiiiine(" outputCountryRisk - sqlSelect RSsize init = "+RSsize);
        rs.last();
    outputliiiiiiiiiine(" outputCountryRisk - sqlSelect RSsize last = "+RSsize);
        RSsize = rs.getRow();
    outputliiiiiiiiiine(" outputCountryRisk - sqlSelect RSsize first= "+RSsize);
        rs.beforeFirst();
        }
    catch(Exception ex) {
          outputliiiiiiiiiine(" outputCountryRisk - sqlSelect RSsize ERROR");      
        }
        
    outputliiiiiiiiiine("outputCountryRisk - sqlSelect RSsize= "+RSsize);
    
			while ( rs.next() ) {

				if ( "Y100".equals(flow) ) continue;				//esc
				
				String ucet = rs.getString("S_UCET");
				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
				String mena = rs.getString("S_MENA");
				String zeme = rs.getString("S_COUNTRY");
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",
												period,
												period,
												idKtgSpolecnost,
												idKtgSpolecnost,
             ucet,    /*IFRS9 */
              /* ucet+"00", */ 
												flow,
												menaSpol,
												"0PCK01",
												null,
												null,
												null,
												null,
												new Number(-987), //null,	--esc D_CP vraj sa nastavi na "YTP0000"
												null,
												null,
												zeme,  //D_GA
												null,
												null,
												null, //mena,
												null,
												null,
												null, //null, //TYPE --esc druha moznost ... "YTP0000" .. podla toho sa nastavi D_CP ...
												castkaLocal,
												null,
												"outputCountryRisk");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}
/*10-2022*/
/*12-2022*/
/*Výkonné a nevýkonné expozice – I-RKC-019 – úvìry a pohledávky, které jsou pøedmìtem znehodnocení*/
	protected void outputCountryRKC019() throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlSelect = 
				"select a.ID_DOKLAD, " +
					   "f.ID id_cisFlow, " +
					   "s.s_country, " +
					   "f.S_KOD, " +
					   "uu.S_UCET, " +
					   "d.S_MENA, " +
					   "sum(decode(uu.c_class,'A',1,'3',1,-1)*a.ND_CASTKALOCAL) ND_CASTKALOCAL " +
				"from  " +
				   "db_jt.kp_dat_dokladZdrojDatAgregace a, " +
				   "db_jt.kp_rel_ucetUnifFlow rel, " +
				   "db_jt.kp_cis_flow f, " +
				   "db_jt.kp_cis_ucetUnif uu, " +
				   "db_jt.kp_ktg_spolecnost s, " +
				   "db_jt.kp_dat_doklad d " +
				"where  " +
					"s.id = a.id_ktgSpolecnost and " +
					"d.id = a.id_doklad and " +
					"rel.ID_CISUCETUNIF = a.ID_CISUCETUNIF and  " +
					"rel.ID_CISFLOW = f.ID and  " +
					"a.ID_CISUCETUNIF = uu.id and  " +
					"a.ID_KTGSPOLECNOST is not null and  " +
					"f.s_kod = '" + flow + "' and " +
//				   "a.s_mena = d.s_mena and " +
				   "uu.c_country = '1' and " +
				   "( db_jt.f_exportCartesis ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0 " +
				   "  OR db_jt.f_is_interni_equity ( d.id, d.id_ktgUcetniSpolecnost, uu.id, a.id_ktgUcetniSpolecnost ) > 0) and "  +
				   "d.id = " + idDoklad +							   
				"group by  " +
					"a.ID_DOKLAD, a.s_ucetunif, " +
					"f.ID, f.S_KOD, uu.S_UCET,  " +
					"s.s_country, d.s_mena"
				;

			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );

int RSsize = 0;
    try {
        RSsize = rs.getRow();
    /*if (esc_debug)  outputliiiiiiiiiine(" outputCountryRKC019 - sqlSelect RSsize init = "+RSsize);  */
        rs.last();
    /*if (esc_debug) outputliiiiiiiiiine(" outputCountryRKC019 - sqlSelect RSsize last = "+RSsize); */
        RSsize = rs.getRow();
    /*if (esc_debug) outputliiiiiiiiiine(" outputCountryRKC019 - sqlSelect RSsize first= "+RSsize); */
        rs.beforeFirst();
        }
    catch(Exception ex) {
          outputliiiiiiiiiine(" outputCountryRKC019 - sqlSelect RSsize ERROR");      
        }
		
    /*if (esc_debug) outputliiiiiiiiiine("outputCountryRKC019 - sqlSelect RSsize= "+RSsize); */
    
			while ( rs.next() ) {

				if ( "Y100".equals(flow) ) continue;				//esc jenom F999  - ok ?
				
				String ucet = rs.getString("S_UCET");
/*				if  ( "OAZ00000100".equals( ucet ) ) continue;
				if ( "EK000000200".equals ( ucet ) ) ucet = "EK000000100";
esc ok ?
*/				
        String mena = rs.getString("S_MENA");
				String zeme = rs.getString("S_COUNTRY");
							
				Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
							
							
				CsvRow csvRow = new CsvRow (
												"A",                //D_CA category
												period,             //D_DP entryPeriod
												period,             //D_PE period
												idKtgSpolecnost,    //D_RU idSpol
												idKtgSpolecnost,    //D_ORU  orig spol
                        ucet,    /*IFRS9 */ //D_AC ucet
												flow,               //D_FL flow
												menaSpol,           //D_CU menaSpol
												"0PCK01",           //D_AU  audit ID
												null,               //D_LE ledger
												null,               //D_NU  jnId
												null,               //D_TO  techOrig
												null,               //D_GO  geoOrig
												null,            // chceme idProtistrany ... D_CP protiId   ... = D_TY  alebo ak mame protistranu = protID alebo spolu s (-987) =>  nastavi sa, na "YTP0000"
												null,               // D_SH  share
												null,               // D_DCFS cash flow
												zeme,               // D_GA geoOrig2
												null,               // D_SEG projSegment
												null,               // D_MA maturity
												null,               // D_TC mena, 
												null,               // D_DM DelMaker
												null,               // D_DPT Deppartment
											  "110",               // chceme=110 //D_TY   aRow.type nastavuje sa podla toho D_CP 
												castkaLocal,        // P_AMMOUNT  castka
												null,               // P_COMMENT  popis
												"outputCountryRKC019");
				
				addRow ( csvRow );
				
			}
			rs.close();
			st.close();
			addRow ( cacheZisk, false );
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}
/*12-2022*/
	
	java.util.Set allProtistrany = new HashSet();

  protected void exportRow ( String aD_AC, String aD_FL, String aD_CP, String aD_GA, String aD_TC, Number aP_AMOUNT, String aDebug ) throws KisException, IOException
  {
    setValue( "A" );    // D_CA
    setComma();
    setValue( period);  // D_DP
    setComma();	
    setValue( period);  // D_PE
    setComma();	
    setValue(idKtgSpolecnost);  // D_RU
    setComma();	
    setValue(idKtgSpolecnost);  // D_ORU
    setComma();	
    setValue( aD_AC);           // D_AC
    setComma();	
    setValue(aD_FL);            // D_FL
    setComma();	
    setValue(menaSpol);         // D_CU
    setComma();	
    setValue("0PCK01");         // D_AU
    setComma();	
    setValue("");               // D_LE
    setComma();	
    setValue("");               // D_NU
    setComma();	
    setValue("");               //  D_TO
    setComma();	
    setValue("");               // D_GO
    setComma();	
    setValue(aD_CP);            // D_CP
    setComma();	
    setValue("");               // D_SH
    setComma();	
    setValue("");               // D_DCFS
    setComma();	
    setValue(aD_GA);            // D_GA
    setComma();	
    setValue("");               // D_SEG
    setComma();	
    setValue("");               // D_MA
    setComma();	
    setValue(aD_TC);            // D_TC
    setComma();	
    setValue("");               // D_DM
    setComma();	
    setValue("");               // D_DPT
    setComma();	
    setValue("");               // D_TY
    setComma();	
    setValue(aP_AMOUNT);        // P_AMOUNT
    setComma();	
    setValue("");               // P_COMMENT
    
    if ( aDebug != null )  {
      setComma();	
      setValue(aDebug);               
    }

    setNewLine();
  }

	
  // jz - 17.9.2014
  protected boolean exportAllData ( boolean esc_debug ) throws KisException, IOException	{
    char c_debug = ( esc_debug ) ? '1' : '0';
    
    c_debug = '0';  //esc 2017
    
    StringBuffer sqlCommand = new StringBuffer ( "begin db_jt.rep_kons_package.p_setReportParam (" );    
    sqlCommand.append ( this.idDoklad ).append (",");
    sqlCommand.append ( "null,");  // bude generovat F999 a Y100 coz je natvrdo v Oracle funkci
    sqlCommand.append ( "'" ).append ( c_debug ).append ( "'" );
    sqlCommand.append ( "); end;" );

		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		try {
			java.sql.Statement st = dbTran.createStatement(0);
      
      st.execute(sqlCommand.toString());
      //esc 26.6.2017 			java.sql.ResultSet rs =  st.executeQuery( "select ucet, castka, mena, id_ktgSpolecnost, zeme, flow_kod, debug from db_jt.vw_kp_kons_package order by idSortGroup, ucet" );
			java.sql.ResultSet rs =  st.executeQuery( "select ucet||'00' ucet, castka, mena, id_ktgSpolecnost, zeme, flow_kod, debug from db_jt.vw_kp_kons_package order by idSortGroup, ucet" );
		
			while ( rs.next() ) {
				
				String aD_AC = rs.getString("UCET");				
        String aD_FL = rs.getString("FLOW_KOD");				 
        String aD_CP = rs.getString("ID_KTGSPOLECNOST");				 
        String aD_GA = rs.getString("ZEME");
        String aD_TC = rs.getString("MENA");
        Number aP_AMOUNT = new Number ( rs.getDouble("CASTKA") );
        String aDebug = null;
        if ( esc_debug ) aDebug = rs.getString ( "DEBUG" );

        exportRow ( aD_AC, aD_FL, aD_CP, aD_GA, aD_TC, aP_AMOUNT, aDebug );
				
			}
			rs.close();
			st.close();			
      
      return true;
      
		} catch ( Exception e ) {
			e.printStackTrace();
		} 
    
    return false;
  }



  // jz - 17.9.2014
  protected boolean outputData_new ( boolean esc_debug ) throws KisException, IOException	{
    exportAllData ( esc_debug );
    saveProtistrany();
    return true;  
  }
  
  /*
   *   jz - 17.9.2014  - kompletne predelano - data se stavi na serveru pres package rep_kons_package.getReport funkci, ktera vraci hotovou sestavu
   *   Java to ted pouze vyexportuje do Excelu a udela nejakou aktualizaci protistran, coz uz delala i drive
   */
	protected boolean outputData() throws KisException, IOException	{
		addHeader();
		allProtistrany.clear();

    /*esc 13.10.2016  - vypnutie debug hlasok
      boolean esc_debug = true; */
    //2017
    boolean esc_debug = false;
    boolean ret;
    
    if ( newExport ) {
      // novy kod - nestavi data na urovni Javy, ale na urovni Oracle - take aktualizace protistran v tabulce kp_rep_CartesisProtistrana je nove na urovni Oraclu 
      // aplikacni server by to mel poustet vzdy s parametrem NULL a konkretni flow co se generuji jsou natvrdo v kodu
      ret = outputData_new ( esc_debug );
    } else {
      ret = outputData_old ( esc_debug );
    }
    
		return ret;
	}
  
  
	protected boolean outputData_old ( boolean esc_debug ) throws KisException, IOException	{
		// jz - 17.9.2014 - presunuto do outputData - jinak je to puvodni kod 
    //addHeader();
		//allProtistrany.clear();

		for(int i=0; i<flowList.length; i++) {
			flow = flowList[i];
      
 
//HD #15961 rozpady v importních souborech do balíku - prva cast  Výsledovka dle lokace protistrany 
/*if (esc_debug) outputliiiiiiiiiine("outputCountryRKC019");
			outputCountryRKC019();				// c) .
*/
if (esc_debug) outputliiiiiiiiiine("outputCountryRISK");
			outputCountryRisk();				// c) .
      
if (esc_debug) outputliiiiiiiiiine("outputZustatkyLocal");			
			outputZustatkyLocal(); 				// ii.
if (esc_debug) outputliiiiiiiiiine("outputZustatky");
			outputZustatky();					// i.

//			outputProtistranyLocal();			 
//			outputProtistrany();      			// vi.
if (esc_debug) outputliiiiiiiiiine("outputLimitCounterPartiesLocal");			
			outputLimitCounterPartiesLocal();	// c) i.
//SD #112241 
//19.03.2019 Do SAPu již není potøeba dotahovat rozpad významných protistran s transakèní mìnou z KISu – sekce LimitCounterPartiesMena.
//if (esc_debug) outputliiiiiiiiiine("outputLimitCounterPartiesMena");
//			outputLimitCounterPartiesMena();	// c) ii.
//SD #112241 
if (esc_debug) outputliiiiiiiiiine("outputCountryLocal");
			outputCountryLocal();				// c) iii.
if (esc_debug) outputliiiiiiiiiine("outputCountryMena");
			outputCountryMena();				// c) iv.
//SD #109172
//02.01.2019 Do SAPu již není potøeba dotahovat rozpad IFRS typù s transakèní mìnou z KISu – sekce TypeMena.      
//if (esc_debug) outputliiiiiiiiiine("outputCountryTypeMena");
//			outputCountryTypeMena();			// c) v.
//SD #109172 
if (esc_debug) outputliiiiiiiiiine("outputCountryTypeLocal");
			outputCountryTypeLocal();			// c) vi.
//02.01.2019 zrusene ..
//esc zrusit 
//if (esc_debug) outputliiiiiiiiiine("outputShares");			
//			outputShares();
//esc zrusit 
//if (esc_debug) outputliiiiiiiiiine("outputSharesLocal");
//			outputSharesLocal();				// v.
//
//if (esc_debug) outputliiiiiiiiiine("outputSegmentyLocal");
//			if(multisegment) outputSegmentyLocal();
//.. 02.01.2019 zrusene		
		}
if (esc_debug) outputliiiiiiiiiine("******************");

//SD #101283
//29.08 2018 Pro KIS to znamená, že již nepotøebujeme v exportním souboru package.csv øádky se STAT.A, STAT.L, STAT.E, STAT.R
//if (esc_debug) outputliiiiiiiiiine("outputCartesisCode");	
//		outputCartesisCode();					// vii.
//if (esc_debug) outputliiiiiiiiiine("outputCartesisCode_S370");		
//		outputCartesisCode_S370();  			// xi. esc 8.1.2008
//SD #101283

if (esc_debug) outputliiiiiiiiiine("saveProtistrany");		
		saveProtistrany();
		return true;
	}


	public void reExport( String aDatum ) throws KisException, IOException {
		ApplicationModuleImpl sm = (ApplicationModuleImpl) dm;
		DBTransaction dbTran = sm.getDBTransaction();
		cacheZisk = null;
		try {
			java.sql.Statement st = dbTran.createStatement(0);
			String sqlDelete = 
				"delete db_jt.kp_rep_cartesisprotistrana where dt_datum = '" + aDatum + "'";
			
			st.execute(sqlDelete);
			
			String sqlSelect = 
				"select d.id from db_jt.kp_ktg_ucetniSpolecnost us, db_jt.kp_dat_doklad d " +
				"where db_jt.f_jeSpolecnostClenSkupiny ( us.id, 8, '" + aDatum + "' ) > 0 and us.c_extsystem = 'T' and us.id = d.id_ktgucetnispolecnost " +
				"and d.dt_datum = '" + aDatum + "' and d.c_basedoklad = '1'";
			
			java.sql.ResultSet rs =  st.executeQuery( sqlSelect );
		
			while ( rs.next() ) {
				Number idDoklad = new Number ( rs.getInt(1) );
				init ( idDoklad, null );
				createCsv();
				csvOutput();
			}
			dbTran.commit();
			rs.close();
			st.close();			
		} catch ( Exception e ) {
			e.printStackTrace();
		}    
		
	}


  private boolean b_debug = false;
  
  public static void main(String[] args)
  {  
    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.cartesis.CartesisModule","CartesisModuleLocal");

    CSVExportPackage cSVExportPackage = null;
    long start = System.currentTimeMillis();

    cSVExportPackage = new CSVExportPackage(dm, new Number(10487501), null );
    cSVExportPackage.b_debug = true;
    try {
      cSVExportPackage.createCsv();
      cSVExportPackage.csvOutput();
    } catch (Throwable t) {
      t.printStackTrace();
    }
    
    long konec = System.currentTimeMillis();
/*esc 13.10.2016
    System.out.println ( "celkem cas : " +  Math.round (konec-start)/60000.0 );    
    System.out.println ( "old 2001   : 32.946" );    
*/    
	
/*
	
    try {
		cSVExportPackage = new CSVExportPackage(dm, null, null);
		cSVExportPackage.reExport("30.9.2007");
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
*/


	
	
	
/*
    ViewObject vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("C_BASEDOKLAD = '1' AND DT_DATUM = '31.12.1999'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      Number idDoklad = (Number) row.getAttribute("Id");
System.out.println(idDoklad);      

      cSVExportPackage = new CSVExportPackage(dm, idDoklad, 1);
      try {
        cSVExportPackage.createCsv();
        cSVExportPackage.csvOutput();
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
    vo.closeRowSet();
*/    
  }

  
  private class Protistrana {
	
	Number id_proti;
	Number id_doklad;
	Number id_ktgUcetniSpolecnost;
	Protistrana ( Number idDoklad, Number idKtgUcetniSpolecnost, Number id_proti ) {
		id_doklad = idDoklad;
		id_ktgUcetniSpolecnost = idKtgUcetniSpolecnost;
		this.id_proti = id_proti;
	}
	
	String getInfo() {
		return ( "" + id_doklad + ";" + id_ktgUcetniSpolecnost );
	}
  }

  
  private class CsvRow {
      String category;
      String entryPeriod;
      String period;
      Number idSpol;
      Number origIdSpol;
      String ucet;
      String flow;
      String menaSpol;
      //
      String auditId;
      String ledger;
      String jnId;
      //
      String techOrig;
      String geoOrig;
      //
      Number protiId;
      Number share;
      String cashFlow;
      String geoOrig2;
      Number projSegment;
      String maturity;
      String mena;
      String dealMaker;
      String department;
      String type;
      //
      Number castka;
      String popis;
      String debugInfo;


    CsvRow (
      String category,
      String entryPeriod,
      String period,
      Number idSpol,
      Number origIdSpol,
      String ucet,
      String flow,
      String menaSpol,
      //
      String auditId,
      String ledger,
      String jnId,
      //
      String techOrig,
      String geoOrig,
      //
      Number protiId,
      Number share,
      String cashFlow,
      String geoOrig2,
      Number projSegment,
      String maturity,
      String mena,
      String dealMaker,
      String department,
      String type,
      //
      Number castka,
      String popis,
      String debugInfo
    ) {
      this.category=category;
      this.entryPeriod=entryPeriod;
      this.period=period;
      this.idSpol=idSpol;
      this.origIdSpol=origIdSpol;
      this.ucet=ucet;
      this.flow=flow;
      this.menaSpol=menaSpol;
      
      this.auditId=auditId;
      this.ledger=ledger;
      this.jnId=jnId;
      
      this.techOrig=techOrig;
      this.geoOrig=geoOrig;
      
      this.protiId=protiId;
      this.share=share;
      this.cashFlow=cashFlow;
      this.geoOrig2=geoOrig2;
      this.projSegment=projSegment;
      this.maturity=maturity;
      this.mena=mena;
      this.dealMaker=dealMaker;
      this.department=department;
      this.type=type;
      
      this.castka=castka;
      this.popis=popis;
      this.debugInfo=debugInfo;
      
    }
  
  }
    
  
}