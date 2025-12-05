package cz.jtbank.konsolidace.ms;

public class MsData 
{
  public String cisloOld;
  public String popis;
  public java.sql.Date datum;
  public double castka;
  public String mena;
  public String spolecnost;
  public String dodavatele;
  
  public MsData(String cisloOld,
                String popis,
                java.sql.Date datum,
                double castka,
                String mena,
                String spolecnost,
                String dodavatele) 
  {
    this.cisloOld = cisloOld;
    this.popis = popis;
    this.datum = datum;
    this.castka = castka;
    this.mena = mena;
    this.spolecnost = spolecnost;
    this.dodavatele = dodavatele;
  }
}