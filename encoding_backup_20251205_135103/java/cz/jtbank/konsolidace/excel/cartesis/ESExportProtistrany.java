package cz.jtbank.konsolidace.excel.cartesis;

import cz.jtbank.konsolidace.excel.*;
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

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

public class ESExportProtistrany extends AbsExcelDoklad 
{
  static Logger logger = Logger.getLogger(ESExportProtistrany.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EXPORT_DOKLADY)); }

  private ApplicationModule dm;
  private java.sql.Date datum;

  protected static SimpleDateFormat sdfMesic = new SimpleDateFormat("yyyy.MM");
  protected static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

  public ESExportProtistrany(ApplicationModule dokladyModule,
                       java.sql.Date datum)
  {
    logger.info("ESExportProtistrany:datum="+datum);  
    dm = dokladyModule;
    this.datum = datum;
    init();
  }

  private void init() {
    setFileName ( "Protistrany_"+(datum==null?"ALL":datum.toString())+".xlsx" );
    setFileRelativeName( Constants.DIR_CARTESIS+"\\"+getFileName() );
    setFileAbsoluteName( OUT_DIR + this.getFileRelativeName() );
    setSablona( Constants.SABLONY_CARTESIS+"SablonaProtistrany.xlsx" );
//System.out.println ( Constants.SABLONY_CARTESIS+"SablonaProtistrany.xlsx"    );
  }
/*  
  private void outputProtistrany_old() {
    int listNr=0;
    int rowNr=3;
    int colNr=0;
    int delka_long = 120, delka_short = 30;
    ViewObject vo = dm.findViewObject("VwRepSpolecnostcartesisView1");
    vo.clearCache();
    String where = datum==null ? "C_AKTIVNI in ( 'U', 'N' )" : "C_AKTIVNI in ( 'U', 'N' ) AND to_char(DT_DATUM,'yyyy.mm') = '"+sdfMesic.format(datum)+"'";
    vo.setWhereClause(where);
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      
      colNr=0;
      String nazev = (String)row.getAttribute("SNazev");
      String nazev_short = nazev, nazev_long = nazev;
      
      if ( nazev_short != null && nazev_short.length() > delka_short ) {
        nazev_short = nazev_short.substring(0,delka_short);
      }
      if ( nazev_long != null && nazev_long.length() > delka_long ) {
        nazev_long = nazev_long.substring(0,delka_long);
      }
      
      
      String zu = row.getAttribute("IdZodpovednaucetni")==null ? "" : ""+row.getAttribute("IdZodpovednaucetni");
      String sk = row.getAttribute("IdSpravcekonsolidace")==null ? "" : ""+row.getAttribute("IdSpravcekonsolidace");
      String oo = row.getAttribute("IdOdpovednaosoba")==null ? "" : ""+row.getAttribute("IdOdpovednaosoba");
      
      setCellValue( listNr, rowNr, colNr, ""+row.getAttribute("IdKtgspolecnost"), null );  // P_NAME

      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_long, null );    // P_LDESC2

      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_long, null );    // P_LDESC5

      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_short, null );    // P_SDESC2

      colNr++;
      setCellValue( listNr, rowNr, colNr, nazev_short, null );    // P_SDESC5

      colNr++;
      setCellValue( listNr, rowNr, colNr, row.getAttribute("IdKtgucetnispolecnost")==null?"":""+row.getAttribute("IdKtgucetnispolecnost"), null );  // C_COMPANY

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Companyprofile"), null );  // C_IAC-EAC

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("SMena"), null );   // C_CURRENCY

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("SCountry"), null );  //C_COUNTRY

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("IfrsSegment"), null );   // C_IFRS-SSEG

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("MngSegment"), null );  // C_MNG-SSEG

      colNr++;
      oracle.jbo.domain.Date dtOd = (oracle.jbo.domain.Date) row.getAttribute("DtIn1Rkc");  
      oracle.jbo.domain.Date dtDo = (oracle.jbo.domain.Date) row.getAttribute("DtOut1Rkc"); 
      setCellValue( listNr, rowNr, colNr, dtOd==null?"":sdf.format(dtOd.dateValue()), null ); // P_DATEACQ-RKC
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtDo==null?"":sdf.format(dtDo.dateValue()), null );   // P_DATEDSP-RKC

      colNr++;
      dtOd = (oracle.jbo.domain.Date) row.getAttribute("DtIn2Jtfg");  

      dtDo = (oracle.jbo.domain.Date) row.getAttribute("DtOut2Jtfg");
      setCellValue( listNr, rowNr, colNr, dtOd==null?"":sdf.format(dtOd.dateValue()), null );   // P_DATEACQ-JTFG

      colNr++;
      setCellValue( listNr, rowNr, colNr, dtDo==null?"":sdf.format(dtDo.dateValue()), null );   // P_DATEDSP-JTFG

      colNr++;
      dtOd = (oracle.jbo.domain.Date) row.getAttribute("DtIn2Techno");  
      dtDo = (oracle.jbo.domain.Date) row.getAttribute("DtOut2Techno");
      setCellValue( listNr, rowNr, colNr, dtOd==null?"":sdf.format(dtOd.dateValue()), null ); // P_DATEACQ-TCN
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtDo==null?"":sdf.format(dtDo.dateValue()), null ); // P_DATEDSP-TCN

      colNr++;
      dtOd = (oracle.jbo.domain.Date) row.getAttribute("DtIn4Mng"); 
      dtDo = (oracle.jbo.domain.Date) row.getAttribute("DtOut4Mng");
      setCellValue( listNr, rowNr, colNr, dtOd==null?"":sdf.format(dtOd.dateValue()), null );  // P_DATEACQ-MNG
      
      colNr++;
      setCellValue( listNr, rowNr, colNr, dtDo==null?"":sdf.format(dtDo.dateValue()), null );  // P_DATEDSP-MNG
      
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Katps"), null );   // C_IFRS-TYPE

      colNr++;
      String pom = (String) row.getAttribute("CAktivni");
      if ( "Y".equals ( pom ) ) {
        pom = "1";
      } else {
        pom = "0";
      }
      setCellValue( listNr, rowNr, colNr, pom, null );  // P_ACTIVE

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Cmethod1"), null );  // C_CMETHOD

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Amethod"), null );   // C_AMETHOD

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Cmethod2j"), null ); // neni popis
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Amethod"), null );   // neni popis
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Cmethod2t"), null ); // neni popis
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Amethod"), null );   // neni popis
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Cmethod4"), null );  // neni popis
      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Amethod"), null );   // neni popis
      colNr++;
      pom = (String) row.getAttribute("Accnted");
      
	  if ( "I".equals ( pom ) ) {
        pom = "1";
      } else {
        pom = "0";
      }
      setCellValue( listNr, rowNr, colNr, pom, null );   // P_ACCNTED

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Deg"), null );       // C_RUSEC-DE

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Dag"), null );       // C_RUSEC-DA

      colNr++;
      setCellValue( listNr, rowNr, colNr, (String) row.getAttribute("Cg"), null );        // C_RUSEC-CO

      colNr++;
      setCellValue( listNr, rowNr, colNr, zu, null );                                     // C_RESP-RA

      colNr++;
      setCellValue( listNr, rowNr, colNr, sk, null );                                     // C_RESP-CM

      colNr++;
      setCellValue( listNr, rowNr, colNr, oo, null );                                     // C_RESP-OO


      rowNr++;
    }
    vo.closeRowSet();
  }
*/    
  private void outputProtistrany() throws KisException {
	ViewObject vo = dm.findViewObject("VwRepSpolecnostcartesisView1");
	vo.clearCache();
	String where = datum==null ? "(ID_KTGUCETNISPOLECNOSTMATKA IS NOT NULL OR  C_AKTIVNI in ( 'U', 'N' ))" : "(ID_KTGUCETNISPOLECNOSTMATKA IS NOT NULL OR  C_AKTIVNI in ( 'U', 'N' )) AND to_char(DT_DATUM,'yyyy.mm') = '"+sdfMesic.format(datum)+"'";

//	String where = datum==null ? "id_ktgSpolecnost in ( 5008, 6021, 6253 ) and C_AKTIVNI in ( 'U', 'N' )" : "(id_ktgUcetniSpolecnost in ( 5008, 6021, 6253 ) or id_ktgSpolecnost = 32351 ) AND to_char(DT_DATUM,'yyyy.mm') = '"+sdfMesic.format(datum)+"'";
//	String where = datum==null ? "id_ktgSpolecnost in ( 5008, 6021, 6253 ) and C_AKTIVNI in ( 'U', 'N' )" : " to_char(DT_DATUM,'yyyy.mm') = '"+sdfMesic.format(datum)+"'";

	vo.setWhereClause(where);

	java.util.List outputRow = new java.util.ArrayList();
	java.util.Map mapMatka = new java.util.HashMap();
	while(vo.hasNext()) {
		Row row = vo.next();
		Radek radekData = new Radek();
		radekData.setFromRow ( row );	  
		outputRow.add ( radekData );
		if ( radekData.isMatka() ) {
			mapMatka.put( radekData.idKtgUcetniSpolecnost, new Integer ( outputRow.size()-1 ));
		}			
	}
	vo.closeRowSet();

	int listNr =0, rowNr=3;
	
	for ( int i = 0; i < outputRow.size(); i++ ) {
		Radek r = null;
		Radek r_dcera = (Radek) outputRow.get(i);
		
		if ( r_dcera.typ == Radek.TYP_DCERA ) {
			Integer indexMatka = (Integer) mapMatka.get( r_dcera.idKtgUcetniSpolecnostMatka  );
			if ( indexMatka == null ) {
				// problem nenalezena matka				
				throw ( new KisException ( "Nenalezena matka " + r_dcera.idKtgUcetniSpolecnostMatka + "  pro dceru " + r_dcera.idKtgUcetniSpolecnost ) );
			}
			Radek r_matka = (Radek) outputRow.get ( indexMatka.intValue() );
			r = Radek.newRadek ( r_matka, r_dcera );
			
		} else {
			r = r_dcera;
		}
		
		Object[] s = r.getOutputExcelValue();
		if ( s != null ) {
			for ( int k = 0, colNr = 0; k < s.length; k++, colNr++ ) {
				setCellValue( listNr, rowNr, colNr, (String)s[k], null );  
			}
			rowNr++;		
		}			
	}
	
  }
	
	
  protected boolean outputData () throws KisException 
  {
      long start = 0L, end = 0L, dif = 0L;
      start = System.currentTimeMillis();    
      outputProtistrany();
      end = System.currentTimeMillis();    
      logger.debug("Protistrany:"+((end-start)/1000.0)+"s");
      
      return true;
  }

  public static void main(String[] argv) 
  {
    try {
      ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.cartesis.CartesisModule","CartesisModuleLocal");

      ESExportProtistrany ed = new ESExportProtistrany(dm, Utils.getDate( 30, 9, 2007 ));
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

class Radek implements Cloneable {
	int delka_long = 120, delka_short = 30;
	
	String nazev, nazev_short, nazev_long;
	
	oracle.jbo.domain.Number zodpovednaUcetni; 
	oracle.jbo.domain.Number spravceKonsolidace; 
	oracle.jbo.domain.Number odpovednaOsoba; 
	
	oracle.jbo.domain.Number idKtgSpolecnost;
	oracle.jbo.domain.Number idKtgUcetniSpolecnost;
	oracle.jbo.domain.Number idKtgUcetniSpolecnostMatka;

	String companyProfile;
	String mena;
	String country;
	String ifrsSegment;
	String mngSegment;

	oracle.jbo.domain.Date dtIn_1_Rkc;  
	oracle.jbo.domain.Date dtOut_1_Rkc;  

	oracle.jbo.domain.Date dtIn_2_Jtfg;  
	oracle.jbo.domain.Date dtOut_2_Jtfg;  

	oracle.jbo.domain.Date dtIn_2_Techno;  
	oracle.jbo.domain.Date dtOut_2_Techno;  

	oracle.jbo.domain.Date dtIn_4_Mng;  
	oracle.jbo.domain.Date dtOut_4_Mng;  

	String katPS;	
	String aktivni, aktivniOriginal;
      
	String method_c;
	String method_a;
	String method_c2;
	String method_c3;
	String method_c4;
	
	String accneted;
	String deg;
	String dag;
	String cq;
		
	public static int TYP_PROTISTRANA = 1;
	public static int TYP_UCETNISPOLECNOST = 2;
	public static int TYP_DCERA = 3;
	
	int typ = TYP_PROTISTRANA;

	static Radek newRadek ( Radek matka, Radek dcera ) {
		// na zaklade dat matky a dcery dava novy objekt s daty jak se bude dcera exportovat
		Radek r = (Radek) matka.clone();
		r.idKtgSpolecnost = dcera.idKtgSpolecnost;
		r.idKtgUcetniSpolecnost = null;
		r.nazev = dcera.nazev;
		r.nazev_short = dcera.nazev_short; 
		r.nazev_long = dcera.nazev_long;
		r.typ = dcera.typ;
		r.aktivniOriginal = dcera.aktivniOriginal;
		r.accneted = "0";
		return ( r );
	}

	private void setTyp() {
	
		typ = TYP_PROTISTRANA;
	
		if ( idKtgUcetniSpolecnostMatka != null ) {
			typ = TYP_UCETNISPOLECNOST;	
			if ( idKtgUcetniSpolecnostMatka.equals( idKtgUcetniSpolecnost ) ) {
				// je to matka
				idKtgSpolecnost = idKtgUcetniSpolecnost;				
			} else {
				idKtgSpolecnost = idKtgUcetniSpolecnost;
				typ = TYP_DCERA;	
			}
			return;
		} else if ( idKtgUcetniSpolecnost != null ) {
			// je to ucetni spolecnost bez oznacene matky
			idKtgSpolecnost = idKtgUcetniSpolecnost;
			typ = TYP_UCETNISPOLECNOST;	
		}
		
	}

	private void upravData() {

		//esc 26.6.08 ... pavlove drobnosti	
		if( ifrsSegment == null ) {
				ifrsSegment = "NA";			// C_IFRS-SSEG
		}
	
		if ( mngSegment  == null ) {
				mngSegment  = "NA";			// C_MNG-SSEG
		}
		//esc ... 26.06.08

		nazev_short = nazev_long = nazev;
      
		if ( nazev_short != null && nazev_short.length() > delka_short ) {
			nazev_short = nazev_short.substring(0,delka_short);	// P_SDESC2, P_SDESC5
		}
		if ( nazev_long != null && nazev_long.length() > delka_long ) {
			nazev_long = nazev_long.substring(0,delka_long); 	// P_LDESC2, P_LDESC5
		}
			
		if ( "Y".equals ( aktivni ) ) {			// P_ACTIVE
			aktivni = "1";
		} else {
			aktivni = "0";
		}
/*
		if ( "I".equals ( accneted ) ) {		// P_ACCNTED
			accneted = "1";
		} else {
			accneted = "0";
		}
*/				
		setTyp();
		
		if ( typ == TYP_PROTISTRANA ) {
/*		
			companyProfile = null;		// C_IAC-EAC
			mena = null;				// C_CURRENCY
			ifrsSegment = null;			// C_IFRS-SSEG
			mngSegment = null;			// C_MNG-SSEG
	
	
			dtIn_1_Rkc = null;  		// P_DATEACQ-RKC
			dtOut_1_Rkc = null;			// P_DATEDSP-RKC  
			
			dtIn_2_Jtfg = null;			// P_DATEACQ-JTFG
			dtOut_2_Jtfg = null;		// P_DATEDSP-JTFG  
			
			dtIn_2_Techno = null;		// P_DATEACQ-TCN
			dtOut_2_Techno = null;		// P_DATEDSP-TCN
			
			dtIn_4_Mng = null;			// P_DATEACQ-MNG
			dtOut_4_Mng = null;			// P_DATEDSP-MNG  
	
	
			katPS = null;				// C_IFRS-TYPE;	
*/		  
			method_c = "NC";			// C_CMETHOD
/*			
			method_a = null;			// C_AMETHOD;
			method_c2 = null;			// neni popis;
			method_c3 = null; 			// neni popis;
			method_c4 = null;			// neni popis;
	
			accneted = "0";				// P_ACCNTED
			
			deg = null;					// C_RUSEC-DE;
			dag = null;					// C_RUSEC-DA;
			cq = null;					// C_RUSEC-CO

			zodpovednaUcetni = null;	// C_RESP-RA
			spravceKonsolidace = null;	// C_RESP-CM
			odpovednaOsoba = null;		// C_RESP-OO
*/
		}

	
	}

	public void setFromRow ( Row row ) {
		nazev = (String)row.getAttribute("SNazev");
	  
		zodpovednaUcetni = (oracle.jbo.domain.Number)row.getAttribute("IdZodpovednaucetni");			// C_RESP-RA
		spravceKonsolidace = (oracle.jbo.domain.Number)row.getAttribute("IdSpravcekonsolidace");		// C_RESP-CM
		odpovednaOsoba = (oracle.jbo.domain.Number)row.getAttribute("IdOdpovednaosoba");				// C_RESP-OO

		idKtgSpolecnost = (oracle.jbo.domain.Number) row.getAttribute("IdKtgspolecnost");  // P_NAME
		idKtgUcetniSpolecnost = (oracle.jbo.domain.Number) row.getAttribute("IdKtgucetnispolecnost");  // C_COMPANY

		companyProfile = (String) row.getAttribute("Companyprofile");		// C_IAC-EAC
		mena = (String) row.getAttribute("SMena");							// C_CURRENCY
		country = (String) row.getAttribute("SCountry");					// C_COUNTRY
		ifrsSegment = (String) row.getAttribute("IfrsSegment");				// C_IFRS-SSEG
		mngSegment = (String) row.getAttribute("MngSegment");				// C_MNG-SSEG


		dtIn_1_Rkc = (oracle.jbo.domain.Date) row.getAttribute("DtIn1Rkc");  	// P_DATEACQ-RKC
		dtOut_1_Rkc = (oracle.jbo.domain.Date) row.getAttribute("DtOut1Rkc");	// P_DATEDSP-RKC  
		
		dtIn_2_Jtfg = (oracle.jbo.domain.Date) row.getAttribute("DtIn2Jtfg");	// P_DATEACQ-JTFG
		dtOut_2_Jtfg = (oracle.jbo.domain.Date) row.getAttribute("DtOut2Jtfg");	// P_DATEDSP-JTFG  
		
		dtIn_2_Techno = (oracle.jbo.domain.Date) row.getAttribute("DtIn2Techno");		// P_DATEACQ-TCN
		dtOut_2_Techno = (oracle.jbo.domain.Date) row.getAttribute("DtOut2Techno");		// P_DATEDSP-TCN
		
		dtIn_4_Mng = (oracle.jbo.domain.Date) row.getAttribute("DtIn4Mng");		// P_DATEACQ-MNG
		dtOut_4_Mng = (oracle.jbo.domain.Date) row.getAttribute("DtOut4Mng");	// P_DATEDSP-MNG  


		katPS = (String) row.getAttribute("Katps");			// C_IFRS-TYPE;	
		aktivni = (String) row.getAttribute("CAktivni");	// P_ACTIVE
		aktivniOriginal = aktivni;

		method_c = (String) row.getAttribute("Cmethod1");	// C_CMETHOD
		method_a = (String) row.getAttribute("Amethod");	// C_AMETHOD;
		method_c2 = (String) row.getAttribute("Cmethod2j");	// neni popis;
		method_c3 = (String) row.getAttribute("Cmethod2t"); // neni popis;
		method_c4 = (String) row.getAttribute("Cmethod4");	// neni popis;

		accneted = (String) row.getAttribute("Accnted");	// P_ACCNTED
		deg = (String) row.getAttribute("Deg");				// C_RUSEC-DE;
		dag = (String) row.getAttribute("Dag");				// C_RUSEC-DA;
		cq = (String) row.getAttribute("Cg");				// C_RUSEC-CO
		
		idKtgUcetniSpolecnostMatka = (oracle.jbo.domain.Number)row.getAttribute ( "IdKtgucetnispolecnostmatka" );

		upravData();
	}

	private String getValueForExcel ( oracle.jbo.domain.Number n ) {
		return ( ( n == null ) ? "" : n.toString() );
	}

	private String getValueForExcel ( String s ) {
		return ( ( s == null ) ? "" : s.toString() );
	}

	private String getValueForExcel ( oracle.jbo.domain.Date d ) {
		return ( ( d == null ) ? "" : ESExportProtistrany.sdf.format( d.dateValue()) );
	}
	
	public boolean isMatka() {
		return ( 
			idKtgUcetniSpolecnostMatka != null && 
			idKtgUcetniSpolecnostMatka.equals(idKtgUcetniSpolecnost) );
	}

	public boolean isDcera() {
		return ( 
			idKtgUcetniSpolecnostMatka != null && 
			! idKtgUcetniSpolecnostMatka.equals(idKtgUcetniSpolecnost) );
	}

	public Object[] getOutputExcelValue () { 
	
	
		if ( "Y".equals( aktivniOriginal ) && isMatka() ) {
			return null;
		} 
	
		java.util.List outputList = new ArrayList();

		outputList.add ( getValueForExcel ( idKtgSpolecnost ) );
		outputList.add ( getValueForExcel ( nazev_long ) );
//outputList.add ( getValueForExcel ( idKtgUcetniSpolecnostMatka ) );		
		outputList.add ( getValueForExcel ( nazev_long ) );
//outputList.add ( getValueForExcel ( "" + typ ) );
		outputList.add ( getValueForExcel ( nazev_short ) );
		outputList.add ( getValueForExcel ( nazev_short ) );
		
//esc 26.6.08 ... pavlove drobnosti
//esc 25.11.08 ... 

		// C_COMPANY pre 5-miestny kod (externe spol ) => YTP0000					
		if ( idKtgSpolecnost != null && idKtgSpolecnost.toString().length() > 4 )
			{  outputList.add ( getValueForExcel ( "YTP0000" ) ); 											//  C_COMPANY
			}
		else if ( idKtgUcetniSpolecnost != null ) 
			{ if
				(  "1".equals(accneted) 						// 1- interni spol.
					&& 
					( (idKtgUcetniSpolecnostMatka != null && 	idKtgUcetniSpolecnostMatka.equals(idKtgUcetniSpolecnost)) // + matka
						||
					  ( idKtgUcetniSpolecnostMatka == null )						// + neni dcera = nema matku
					)			
				)  			
				{	outputList.add ( getValueForExcel ( idKtgUcetniSpolecnost ) );
				} 
			 else 
			 { outputList.add("")  ;	   //  C_COMPANY natvrdo medzera !!
			 }			
			}
		else 
			 { outputList.add("")  ;	   //  C_COMPANY natvrdo medzera !!
			 }
			 
		outputList.add ( getValueForExcel ( companyProfile ) );							// C_IAC-EAC
		outputList.add ( getValueForExcel ( mena ) );									// C_CURRENCY
		outputList.add ( getValueForExcel ( country ) );								// C_COUNTRY
		outputList.add ( getValueForExcel ( ifrsSegment ) );							// C_IFRS-SSEG
		outputList.add ( getValueForExcel ( mngSegment ) );								// C_MNG-SSEG
		outputList.add ( getValueForExcel ( dtIn_1_Rkc ) );								// P_DATEACQ-RKC
		outputList.add ( getValueForExcel ( dtOut_1_Rkc ) );							// P_DATEDSP-RKC
		outputList.add ( getValueForExcel ( dtIn_2_Jtfg ) );							// P_DATEACQ-JTFG
		outputList.add ( getValueForExcel ( dtOut_2_Jtfg ) );							// P_DATEDSP-JTFG
		outputList.add ( getValueForExcel ( dtIn_2_Techno ) );							// P_DATEACQ-TCN
		outputList.add ( getValueForExcel ( dtOut_2_Techno ) );							// P_DATEDSP-TCN
		outputList.add ( getValueForExcel ( dtIn_4_Mng ) );								// P_DATEACQ-MNG
		outputList.add ( getValueForExcel ( dtOut_4_Mng ) );							// P_DATEDSP-MNG
		outputList.add ( getValueForExcel ( katPS ) );									// C_IFRS-TYPE
		outputList.add ( getValueForExcel ( aktivni ) );								// P_ACTIVE
		outputList.add ( getValueForExcel ( method_c ) );								// C_CMETHOD
		outputList.add ( getValueForExcel ( method_a ) );								// C_AMETHOD

		outputList.add ( getValueForExcel ( method_c2 ) );								// neni popis
		outputList.add ( getValueForExcel ( method_a ) );								// neni popis
		outputList.add ( getValueForExcel ( method_c3 ) );								// neni popis
		outputList.add ( getValueForExcel ( method_a ) );								// neni popis
		outputList.add ( getValueForExcel ( method_c4 ) );								// neni popis
		outputList.add ( getValueForExcel ( method_a ) );								// neni popis
		
		outputList.add ( getValueForExcel ( accneted ) );								// C_RUSEC-DE
		outputList.add ( getValueForExcel ( deg ) );									// C_RUSEC-DA
		outputList.add ( getValueForExcel ( dag ) );									// C_RUSEC-CO
		outputList.add ( getValueForExcel ( cq ) );										// C_RUSEC-OO
		
		outputList.add ( getValueForExcel ( zodpovednaUcetni ) );						// C_RESP-RA
		outputList.add ( getValueForExcel ( spravceKonsolidace ) );						// C_RESP-CM
		outputList.add ( getValueForExcel ( odpovednaOsoba ) );							// C_RESP-OO
		


		return ( outputList.toArray() );
	}
 
	public Object clone () {
		try  {
			return super.clone ();
		}
		catch (CloneNotSupportedException e) {
			throw new Error ("This should never happen!");
		}
	} 
	  
}