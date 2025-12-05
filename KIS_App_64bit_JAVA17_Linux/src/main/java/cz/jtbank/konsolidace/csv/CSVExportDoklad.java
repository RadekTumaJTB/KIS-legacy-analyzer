package cz.jtbank.konsolidace.csv;

import cz.jtbank.konsolidace.common.*;
import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;
import oracle.jbo.server.ApplicationModuleImpl;
import oracle.jbo.server.DBTransaction;
import java.sql.*;

public class CSVExportDoklad extends AbsCsvDoklad 
{
  private ApplicationModule dm;
  private Number idDoklad;
  private java.sql.Date datum;

  private Number idKtgSpolecnost;
  private String nazevSpol;
  private String menaSpol;
  private String souborPredponaSpol;

  private java.sql.Date dtOd, dtDo;
  
  private int typ;
  private String pridavek;
  
  private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public CSVExportDoklad(ApplicationModule dokladyModule,
                        Number idDoklad,
                        int typ)
  {
    logger.info("CSVExportDoklad:idDoklad="+idDoklad);
    dm = dokladyModule;
    this.idDoklad = idDoklad;
    this.typ = typ;

    init();
  }

  public CSVExportDoklad(ApplicationModule dokladyModule,
                         Number idSpol,
                         java.sql.Date dtOd,
                         java.sql.Date dtDo)
  {
    logger.info("CSVExportDoklad:idDoklad="+idDoklad);
    dm = dokladyModule;
    this.idKtgSpolecnost = idSpol;
    this.dtOd = dtOd;
    this.dtDo = dtDo;

    init2();
  }

private Number prozatimni() {
Number p = null;
ViewObject vo = dm.findViewObject("KpDatDokladView2");
vo.clearCache();
vo.setWhereClause("C_BASEDOKLAD = '1' AND DT_DATUM = '31.12.2006' AND ID_KTGUCETNISPOLECNOST = "+idKtgSpolecnost);
if(vo.hasNext()) 
{
Row row = vo.next();
p = (Number) row.getAttribute("Id");
System.out.println("-"+p);
}
vo.closeRowSet();
return p;
}

  private void init() {
    /*if(typ==1 || typ==-1) pridavek = "Orig";
    else if(typ==2) pridavek = "OVazby";
    else if(typ==4) pridavek = "OShares";
    else*/ pridavek = "";

Number idDoklad = this.idDoklad; //PODVOD PRO NACTENI POC. STAVU

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
if(typ==1 || typ==4 || typ==-1) { //PODVOD PRO NACTENI POC. STAVU
datum = cz.jtbank.konsolidace.common.Utils.getDate ( 31, 12, 2006 ) ;
idDoklad = prozatimni();
}
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    setFileName ( idDoklad+"@Doklad"+pridavek+"_"+datum+".csv" );
    setFileRelativeName( souborPredponaSpol+"_"+idKtgSpolecnost+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }

  private void init2() {
    ViewObject vo = dm.findViewObject("KpKtgUcetnispolecnostView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+idKtgSpolecnost);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      nazevSpol = (String) row.getAttribute("SNazev");
      menaSpol = (String) row.getAttribute("SMena");
      souborPredponaSpol = (String) row.getAttribute("SSouborpredpona");
    }
    vo.closeRowSet();
    dm.getTransaction().commit();

    setFileName ( "DokladMIS"+(idKtgSpolecnost==null?"":"_"+idKtgSpolecnost)+"_"+dtOd+"_"+dtDo+".csv" );
    if(idKtgSpolecnost!=null) setFileRelativeName( souborPredponaSpol+"_"+idKtgSpolecnost+"\\"+getFileName() );
    else setFileRelativeName(  Constants.DIR_DATA+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
  }

  protected void addHeader() throws KisException, IOException
  {
    setValue("D_CA");
    setComma();	
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
//esc 16.07
    setComma();	
    setValue("ID_DOKUMENT");
    setComma();	
    setValue("ID_KTGPROJEKT");	
    setNewLine();
  }

private String replaceUcet(String ucet) 
{
  if("AH1300001".equals(ucet)) ucet = "AH1310001";
  else if("AH1300002".equals(ucet)) ucet = "AH1310002";
  else if("AH1300003".equals(ucet)) ucet = "AH1310003";
  
  return ucet;
}

    protected void addRow( CsvRow aRow ) throws KisException, IOException {
        if ( aRow == null ) return;
        setValue(aRow.category);
        setComma();
        setValue(aRow.entryPeriod);
        setComma();
        setValue(aRow.period);
        setComma();
        setValue(aRow.idSpol);
        setComma();
        setValue(aRow.origIdSpol);
        setComma();
        setValue(replaceUcet(aRow.ucet));
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
        setValue(aRow.protiId);
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
        setValue(aRow.type);
        setComma();
        setValue(aRow.castka);
        setComma();
        setValue(aRow.popis);
//esc 16.07
		setComma();	
		setValue(aRow.idDokument);
		setComma();	
		setValue(aRow.idKtgProjekt);	
    
        setNewLine();
        
    }

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
						//
						Number idDokument,
						Number idKtgProjekt						
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
    setValue(replaceUcet(ucet));
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
//esc 16.07
    setComma();	
    setValue(idDokument);
    setComma();	
    setValue(idKtgProjekt);	

    setNewLine();
  }

  protected void outputDataStarsi() throws KisException, IOException
  {
    String period = sdfMesic.format(datum);
  
    ViewObject vo = dm.findViewObject("KpDatDokladzdrojdatagregaceAgr1View1");
    vo.clearCache();
    vo.setWhereClause("s_kod in ( 'F999' ) and ID_DOKLAD = "+idDoklad );
    vo.setOrderByClause("s_ucet, s_kod");
    Number sumaLocal = null;
    String lastUcet = "???";
    String lastKod = "???";
    boolean nasumovano = false;
    
    String sKod="";
    
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      String ucet = (String)row.getAttribute("SUcet");
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
      if ( "EK0000002".equals ( ucet ) ) ucet = "EK0000001";
      String mena = (String)row.getAttribute("SMena");      
//      Number castkaMena = (Number)row.getAttribute("NdCastkamena");
      sKod = (String)row.getAttribute("SKod");

      if ( (!lastUcet.equals( ucet ) || !lastKod.equals( sKod ) ) && nasumovano ) {        
        CsvRow csvRow = new CsvRow("A",
               period,
               period,
               idKtgSpolecnost,
               idKtgSpolecnost,
               lastUcet,
               lastKod,
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
               sumaLocal,
//esc 16.07               null);                        
               null,
			   null,
			   null);                        
               
               addRow ( csvRow );
               
               sumaLocal = null;
               
      } else {
        nasumovano = true;        
      }
      
      lastUcet = ucet;
      lastKod = sKod;
      
      CsvRow csvRow = new CsvRow("A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,
             sKod,
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
//esc 16.07               null);                        
               null,
			   null,
			   null);                        

             addRow(csvRow);
        if ( sumaLocal == null ) 
          sumaLocal = castkaLocal;
        else
          sumaLocal = sumaLocal.add( castkaLocal );
    }
    
    vo.closeRowSet();
    
    if ( sumaLocal != null ) {
      CsvRow row = new CsvRow("A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             lastUcet,
             sKod,
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
             sumaLocal,
//esc 16.07               null);                        
               null, 
			   null,
			   null);                        
             addRow(row);
                          
    }    
    
    ApplicationModuleImpl sm = (ApplicationModuleImpl)dm;
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
//System.out.println ( sqlSelect );            
    java.sql.ResultSet rs =  st.executeQuery( sqlSelect );

      while ( rs.next() ) {
        String ucet = "EK0000002";
        String mena = rs.getString("S_MENA");
        Number castkaLocal = new Number ( rs.getDouble("ND_CASTKALOCAL") );
        Number castkaMena = new Number ( rs.getDouble("ND_CASTKAMENA") );
        sKod = "F999";
        
        CsvRow row = new CsvRow("A",
               period,
               period,
               idKtgSpolecnost,
               idKtgSpolecnost,
               ucet,
               sKod,
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
//esc 16.07               null);                        
               null,
			   null,
			   null);                        
               
               addRow(row);
  
        row = new CsvRow("A",
               period,
               period,
               idKtgSpolecnost,
               idKtgSpolecnost,
               ucet,
               sKod,
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
               castkaMena,
//esc 16.07               null);                        
               null,
			   null,
			   null);                        
            
                addRow(row);
      }
      rs.close();
      st.close();
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    
    
  }

  private Set usedIdSpol = new HashSet();

  protected void outputDataStarsiVazby() throws KisException, IOException, SQLException
  {
    String period = sdfMesic.format(datum);
  
    ApplicationModuleImpl sm = (ApplicationModuleImpl)dm;
    DBTransaction dbTran = sm.getDBTransaction();
    PreparedStatement relSelect = dbTran.createPreparedStatement(
        "select count(*) from db_jt.kp_rel_subkonsolidaceclen rr where rr.id_ktgucetniskupina = 8 and rr.id_ktgucetnispolecnost = ? and ( (sysdate between rr.dt_clenstviOd and rr.dt_clenstviDo) or ( sysdate - rr.dt_clenstviDo < 365 ) )"
        , 0);
    java.sql.Statement st = dbTran.createStatement(0);
    
    for(int i=0; i<3; i++) {
    
      Number ids;
      if(i==0) ids = new Number(2);
      else if(i==1) ids = new Number(4);
      else ids = new Number(1);
  
      ViewObject vo = dm.findViewObject("KpDatDokladzdrojdatagregaceVazby1View1");
      vo.clearCache();
      vo.setWhereClause("s_kod in ( 'F999' ) and ID_DOKLAD = "+idDoklad+" AND ID_KTGUCETNISKUPINA = "+ids);
      //vo.setOrderByClause("ID_KTGUCETNISPOLECNOSTFROM,ID_KTGUCETNISPOLECNOSTTO");
      Map mapCastka = new HashMap();
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        String ucet = (String)row.getAttribute("SUcetunif");
        String mena = (String)row.getAttribute("SMena");
        Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
//        Number castkaMena = (Number)row.getAttribute("NdCastkamena");
        Number idProti = (Number)row.getAttribute("IdKtgucetnispolecnostto");

        relSelect.setInt( 1, idProti.intValue() );
        ResultSet rs = relSelect.executeQuery();
        int ber = 0;
        if ( rs.next() ) ber = rs.getInt(1);
        rs.close();

        if ( ber == 0 ) {
            continue; 
        }

        String sKod = (String)row.getAttribute("SKod");
        
        String key = idProti+mena+ucet;
        if(usedIdSpol.contains(key)) continue;
        else usedIdSpol.add(key);
        
        ItemVazby item = new ItemVazby ( ucet, mena, sKod, castkaLocal, idProti );
        String sKey = item.getKey();
        
        if ( mapCastka.containsKey(sKey) ) {
            ItemVazby ii = (ItemVazby)mapCastka.get(sKey);
            ii.add(castkaLocal);      
//            mapCastka.put(sKey, ii);
        } else {
            mapCastka.put(sKey, item);
        }
        
        CsvRow csvRow = new CsvRow("A",
               period,
               period,
               idKtgSpolecnost,
               idKtgSpolecnost,
               ucet,
               sKod,
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
//esc 16.07               null);                        
               null,
			   null,
			   null);                        
               
               addRow(csvRow);
      }
      vo.closeRowSet();
      
      Collection coll = mapCastka.values();      
      for ( Iterator it = coll.iterator(); it.hasNext();  ) {
            ItemVazby item = (ItemVazby) it.next();
            CsvRow csvRow = new CsvRow("A",
                   period,
                   period,
                   idKtgSpolecnost,
                   idKtgSpolecnost,
                   item.ucet,
                   item.kod,
                   menaSpol,
                   "0PCK01",
                   null,
                   null,
                   null,
                   null,
                   item.idProti,
                   null,
                   null,
                   null,
                   null,
                   null,
                   null,
                   null,
                   null,
                   null,
                   item.castkaLocal,
//esc 16.07               null);                        
               null,
			   null,
			   null);                        
            addRow(csvRow );                   
      }
    }
    relSelect.close();
  }

  protected void outputDataStarsiShares() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatDokladzdrojdatagregaceShares1View1");
    vo.clearCache();
    String where = "flow != 'Y100' AND ID_DOKLAD = "+idDoklad;
    vo.setWhereClause(where);

    Map mapShares = new HashMap();
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      oracle.jbo.domain.Date dt = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      String period = dt==null?"":sdfMesic.format(dt.dateValue());
      String ucet = (String)row.getAttribute("SUcet");
      if ( "EK0000002".equals ( ucet ) ) ucet = "EK0000001";
      String mena = (String)row.getAttribute("SMena");
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
//      Number castkaMena = (Number)row.getAttribute("NdCastkamena");
      Number idProti = (Number)row.getAttribute("IdKtgucetnispolecnostto");
      Number koupenoOd = (Number)row.getAttribute("Koupenood");
      String flow = (String)row.getAttribute("Flow");
      
      CsvRow csvRow = new CsvRow("A",
             period,
             period,
             idKtgSpolecnost,
             idKtgSpolecnost,
             ucet,
             flow,
             menaSpol,
             "0PCK01",
             null,
             null,
             null,
             null,
             koupenoOd,
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
//esc 16.07               null);                        
               null,
			   null,
			   null);                        

      String sKey = csvRow.getKeyShares();
      

        addRow(csvRow);
        if ( mapShares.containsKey(sKey) ) {
            CsvRow r = (CsvRow) mapShares.get(sKey);
            r.add (castkaLocal);
        } else {
            mapShares.put ( sKey, csvRow );            
        }
    }
    vo.closeRowSet();
    
    Collection coll = mapShares.values();      
    for ( Iterator it = coll.iterator(); it.hasNext();  ) {
        CsvRow csvRow = (CsvRow) it.next();
        csvRow.mena = null;
        addRow(csvRow);
    }    
  }

  protected void outputDataMIS() throws KisException, IOException
  {
    ViewObject vo = dm.findViewObject("KpDatDokladzdrojdatagregaceMis1View1");
    vo.clearCache();
    //--esc 22.01.2008 String where = "DT_DATUM BETWEEN TO_DATE('"+sdf.format(dtOd)+"','dd.mm.yyyy') AND TO_DATE('"+sdf.format(dtDo)+"','dd.mm.yyyy') and s_kod in ( 'F999' )";
	String where = "DT_DATUM BETWEEN TO_DATE('"+sdf.format(dtOd)+"','dd.mm.yyyy') AND TO_DATE('"+sdf.format(dtDo)+"','dd.mm.yyyy') and s_kod in ( 'F999','Y100' )";
    if(idKtgSpolecnost!=null) where+=" AND ID_KTGUCETNISPOLECNOST = "+idKtgSpolecnost;
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      oracle.jbo.domain.Date dt = (oracle.jbo.domain.Date) row.getAttribute("DtDatum");
      String period = dt==null?"":sdfMesic.format(dt.dateValue());
      Number idSpol = (Number)row.getAttribute("IdKtgucetnispolecnost");
      String ucet = (String)row.getAttribute("SUcetunif");
      if ( "EK0000002".equals ( ucet ) ) ucet = "EK0000001";
      String mena = (String)row.getAttribute("SMena");
      String menaUcSpol = (String)row.getAttribute("Menaucspol");
      String sKod = (String)row.getAttribute("SKod");
      Number castkaLocal = (Number)row.getAttribute("NdCastkalocal");
//      Number castkaMena = (Number)row.getAttribute("NdCastkamena");
      Number idProti = (Number)row.getAttribute("IdKtgucetnispolecnostto");
      Number idSegment = (Number)row.getAttribute("IdSegment");
//esc 16.07 - doplnene 2 nove stlpce ,mis.ID_DOKUMENT , mis.ID_KTGPROJEKT
      Number ID_DOKUMENT   = (Number)row.getAttribute("IdDokument");
	  Number ID_KTGPROJEKT = (Number)row.getAttribute("IdKtgprojekt");
	  
      CsvRow csvRow = new CsvRow("A",
             period,
             period,
             idSpol,
             idSpol,
             ucet,
             sKod,
             menaUcSpol,
             "0PCK20",
             null,
             null,
             null,
             null,
             idProti,
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
			 ID_DOKUMENT,
			 ID_KTGPROJEKT);
        addRow(csvRow);
        
      csvRow = new CsvRow("A",
             period,
             period,
             idSpol,
             idSpol,
             ucet,
             sKod,
             menaUcSpol,
             "0PCK20",
             null,
             null,
             null,
             null,
             idProti,
             null,
             null,
             null,
             idSegment,
             null,
             mena,
             null,
             null,
             null,
             castkaLocal,
             null,
			 ID_DOKUMENT,
			 ID_KTGPROJEKT);			 
        addRow(csvRow);             
    }
    vo.closeRowSet();
  }

  protected boolean outputData() throws KisException, IOException
  {
    try {
        if(typ!=2) addHeader();
      
        if(typ==1) outputDataStarsi();
        else if(typ==2) outputDataStarsiVazby();
        else if(dtOd!=null) outputDataMIS();
        else if(typ==4) outputDataStarsiShares();
        else if(typ==-1) 
        {
          outputDataStarsi();
          outputDataStarsiShares();
        }
    } catch ( Exception e ) {
        throw new KisException("chyba", e);
    }
    
    return true;
  }
  
  public static void main(String[] args)
  {
  
    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");

    CSVExportDoklad cSVExportDoklad = null;
/*
//ZAKLAD - ZUSTATKY+SHARES (test)
    ViewObject vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    
    vo.setWhereClause("C_BASEDOKLAD = '1' AND DT_DATUM = '31.12.1999' and dt_datumexportolap = '31.12.1999'");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      Number idDoklad = (Number) row.getAttribute("Id");
System.out.println(idDoklad);      

      cSVExportDoklad = new CSVExportDoklad(dm, idDoklad, -1);
      try {
        cSVExportDoklad.createCsv();
        cSVExportDoklad.csvOutput();
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
    vo.closeRowSet();

*/

//VAZBY (produkce)
    ViewObject vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("C_BASEDOKLAD = '1' AND DT_DATUM = '31.12.2006' and dt_datumexportolap = '31.12.1999' ");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      Number idDoklad = (Number) row.getAttribute("Id");
System.out.println(idDoklad);  

      try {
        ((cz.jtbank.konsolidace.doklady.common.DokladyModule) dm).muUnif(idDoklad);
      }
      catch (Throwable t) {
        t.printStackTrace();
      }

      cSVExportDoklad = new CSVExportDoklad(dm, idDoklad, 2);
      try {
        cSVExportDoklad.appendCsv();
        cSVExportDoklad.csvOutput();
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
    vo.closeRowSet();

















/*
// MIS Transakce
    cSVExportDoklad = new CSVExportDoklad(dm, null, 
        cz.jtbank.konsolidace.common.Utils.getDate ( 1, 1, 2006 ),
        cz.jtbank.konsolidace.common.Utils.getDate ( 31, 12, 2006 ));
    try {
      cSVExportDoklad.createCsv();
      cSVExportDoklad.csvOutput();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
*/


/*
    cSVExportDoklad = new CSVExportDoklad(dm, new Number(1236849), -1);
    try {
      cSVExportDoklad.createCsv();
      cSVExportDoklad.csvOutput();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
*/




/*
    cSVExportDoklad = new CSVExportDoklad(dm, new Number(1236165), 1);
    try {
      cSVExportDoklad.createCsv();
      cSVExportDoklad.csvOutput();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
*/
/*
    cSVExportDoklad = new CSVExportDoklad(dm, new Number(765544), 2);
    try {
      cSVExportDoklad.createCsv();
      cSVExportDoklad.csvOutput();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
*/  

/*
    cSVExportDoklad = new CSVExportDoklad(dm, new Number( 1236185 ), -1);
    try {
      cSVExportDoklad.createCsv();
      cSVExportDoklad.csvOutput();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
*/
/*
    cSVExportDoklad = new CSVExportDoklad(dm, new Number(937923), 2);
    try {
      cSVExportDoklad.appendCsv();
      cSVExportDoklad.csvOutput();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
*/

  }

  public static void main2(String[] args)
  {
  
    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");

    CSVExportDoklad cSVExportDoklad = null;

//ZAKLAD - ZUSTATKY+SHARES (test)
    ViewObject vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
/*
    vo.setWhereClause("C_BASEDOKLAD = '1' AND dt_datum = '31.12.1999' and id_ktgUcetniSpolecnost in ( 5002 )");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      Number idDoklad = (Number) row.getAttribute("Id");
System.out.println(idDoklad);      

      cSVExportDoklad = new CSVExportDoklad(dm, idDoklad, -1);
      try {
        cSVExportDoklad.createCsv();
        cSVExportDoklad.csvOutput();
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
    vo.closeRowSet();
*/

//VAZBY (produkce)
     vo = dm.findViewObject("KpDatDokladView1");
    vo.clearCache();
    vo.setWhereClause("C_BASEDOKLAD = '1' AND dt_datum = '31.12.2006' and id_ktgUcetniSpolecnost in ( 4001)");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      Number idDoklad = (Number) row.getAttribute("Id");
System.out.println(idDoklad);  

      cSVExportDoklad = new CSVExportDoklad(dm, idDoklad, 2);
      try {
        cSVExportDoklad.appendCsv();
//cSVExportDoklad.createCsv();
        cSVExportDoklad.csvOutput();
      }
      catch (Throwable t) {
        t.printStackTrace();
      }
    }
    vo.closeRowSet();

  }

  
}

class ItemVazby {
    String ucet, mena, kod;
    Number castkaLocal;
    Number idProti;
  
    ItemVazby ( String ucet, String mena, String kod, Number castkaLocal, Number idProti ) {
        this.ucet = ucet;
        this.mena = mena; 
        this.kod = kod; 
        this.castkaLocal = castkaLocal;
        this.idProti = idProti;
    }
    
    String getKey () {
        return ( ucet + ";" + kod + ";" + idProti.intValue() );
    }

    void add ( Number castka ) {
        castkaLocal = castkaLocal.add(castka);
    }
}

 class CsvRow {
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
//esc 16.07 
	  Number idDokument;
	  Number idKtgProjekt; 
	  
      String debugInfo;

    public CsvRow (String category,
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
						Number idDokument,
						Number idKtgProjekt


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
	//esc 16.07
	  this.idDokument=idDokument;
	  this.idKtgProjekt=idKtgProjekt;

      this.debugInfo=debugInfo;
    }

    String getKeyShares() {
        String ret="";
        if ( ucet == null ) ret += "null"; else ret+=ucet;
        ret += ";";
        if ( protiId == null ) ret += "null"; else ret+=protiId;
        ret += ";";
        if ( share == null ) ret += "null"; else ret+=share;
        ret += ";";
        if ( flow == null ) ret += "null"; else ret+=flow;
        ret += ";";
        
        return ( ret);
    }

    void add ( Number aCastka ) {
        castka = castka.add(aCastka);
    }

  
  }
