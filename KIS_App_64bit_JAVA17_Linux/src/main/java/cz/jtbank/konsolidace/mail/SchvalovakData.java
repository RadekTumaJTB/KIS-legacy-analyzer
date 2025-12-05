package cz.jtbank.konsolidace.mail;

import cz.jtbank.konsolidace.common.Utils;
import java.util.HashMap;
import java.util.TreeMap;

import oracle.jbo.Row;
import oracle.jbo.domain.Number;


/**
 *
 * @author JT8100329
 */
public class SchvalovakData implements Comparable {
    private Integer idDoc;
    private Number idSpolocnost;
    private String nazevSpolocnost;
    private int idZadavatel;
    private String zadavatel;
    private int idTypTransakce;
    private String typTransakce;
    private String mena;
    private String protistrana;
    private Double sumaCelkom;
    private Number idTypizlink;
    HashMap radky = new HashMap();

    /**
     * @param userIds
     * @param idUser
     * @param voRadek
     */
    public static void addSchvalovakRadek (HashMap userIds, oracle.jbo.domain.Number idUser, oracle.jbo.Row rowRadek ){
        // vyber dat voRadek
        Integer idDoc;
        idDoc = new Integer ( ((Number) rowRadek.getAttribute("IdDokument")).intValue());
        Number idRadek = (Number) rowRadek.getAttribute("Id");
                
        HashMap seznamSl = (HashMap)userIds.get(idUser);
        if ( seznamSl == null ){
                seznamSl = new HashMap();

        }
        
        SchvalovakData schvalovakData = (SchvalovakData)seznamSl.get( idDoc);
        if ( schvalovakData == null ){
            schvalovakData = new SchvalovakData();
            seznamSl.put(idDoc,schvalovakData);
            schvalovakData.setIdDoc(idDoc);
            schvalovakData.setIdSpolocnost((Number)rowRadek.getAttribute("IdKtgucetnispolecnost"));
            schvalovakData.setIdTypizlink((Number)rowRadek.getAttribute("IdTypizlink"));
            schvalovakData.setNazevSpolocnost((String)rowRadek.getAttribute("NazevSpolecnosti"));
            ///schvalovakData.setIdTypTransakce((Number)rowRadek.getAttribute("IdTypTransakce"));
            schvalovakData.setTypTransakce((String)rowRadek.getAttribute("Typtransakce"));
            schvalovakData.setProtistrana((String)rowRadek.getAttribute("Protistrana"));
            schvalovakData.setMena((String)rowRadek.getAttribute("Mena"));
            ///schvalovakData.setSumaCelkom();            
            //Number gestorId = (Number) rowRadek.getAttribute("IdGestor");
            //String nGestor = (String) rowRadek.getAttribute("Gestor");             
             
        }
            HashMap radkySl = schvalovakData.getRadky();
            SchvalovakRadekData schvalovakRadekData = (SchvalovakRadekData) radkySl.get(idRadek);
            if ( schvalovakRadekData == null ){
                schvalovakRadekData = new SchvalovakRadekData();
                //put SLradek
                radkySl.put(idRadek,schvalovakRadekData);
                schvalovakRadekData.setIdSl( idDoc);
                schvalovakRadekData.setOdbor((String) rowRadek.getAttribute("Odbor"));
                schvalovakRadekData.setProjekt((String) rowRadek.getAttribute("Projekt"));
                schvalovakRadekData.setCastka((Number) rowRadek.getAttribute("NdCastka"));
                schvalovakRadekData.setKomentar((String) rowRadek.getAttribute("SKomentar"));                        
            //IdTypizlink
        } 
        userIds.put(idUser,seznamSl);
        
    }


    public Integer getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(Integer idDoc) {
        this.idDoc = idDoc;
    }

    public Number getIdSpolocnost() {
        return idSpolocnost;
    }

    public void setIdSpolocnost(Number idSpolocnost) {
        this.idSpolocnost = idSpolocnost;
    }

    public int getIdTypTransakce() {
        return idTypTransakce;
    }

    public void setIdTypTransakce(int idTypTransakce) {
        this.idTypTransakce = idTypTransakce;
    }

    public int getIdZadavatel() {
        return idZadavatel;
    }

    public void setIdZadavatel(int idZadavatel) {
        this.idZadavatel = idZadavatel;
    }

    public String getMena() {
        return Utils.nvl(mena);
    }

    public void setMena(String mena) {
        this.mena = mena;
    }

    public String getNazevSpolocnost() {
        return Utils.nvl(nazevSpolocnost);
    }

    public void setNazevSpolocnost(String nazevSpolocnost) {
        this.nazevSpolocnost = nazevSpolocnost;
    }

    public String getProtistrana() {
        return Utils.nvl(protistrana);
    }

    public void setProtistrana(String protistrana) {
        this.protistrana = protistrana;
    }

    public HashMap getRadky() {
        return radky;
    }

    public void setRadky(HashMap radky) {
        this.radky = radky;
    }

    public Double getSumaCelkom() {
        return sumaCelkom;
    }

    public void setSumaCelkom(Double sumaCelkom) {
        this.sumaCelkom = sumaCelkom;
    }

    public String getTypTransakce() {
        return Utils.nvl(typTransakce);
    }

    public void setTypTransakce(String typTransakce) {
        this.typTransakce = typTransakce;
    }

    public String getZadavatel() {
        return zadavatel;
    }

    public void setZadavatel(String zadavatel) {
        this.zadavatel = zadavatel;
    }

    public Number getIdTypizlink() {
        return idTypizlink;
    }

    public void setIdTypizlink(Number idTypizlink) {
        this.idTypizlink = idTypizlink;
    }

    public int compareTo(Object o) {
        SchvalovakData s1 = (SchvalovakData)o;
        return this.getIdDoc().compareTo(s1.getIdDoc());
    }

     
}