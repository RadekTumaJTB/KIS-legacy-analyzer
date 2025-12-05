package cz.jtbank.konsolidace.mail;

import oracle.jbo.domain.Number;
import cz.jtbank.konsolidace.common.Utils;

/**
 *
 * @author JT8100329
 */
 
public class SchvalovakRadekData {
    private int idSlRadek;
    private String odbor;
    private String projekt;
    private Number castka;
    private String komentar;    
    //private SchvalovakData sl;    
    private Integer idSl;

    public int getIdSlRadek() {
        return idSlRadek;
    }

    public void setIdSlRadek(int idSlRadek) {
        this.idSlRadek = idSlRadek;
    }

    public String getKomentar() {
        return Utils.nvl(komentar);
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public String getOdbor() {
        return Utils.nvl(odbor);
    }

    public void setOdbor(String odbor) {
        this.odbor = odbor;
    }

    public String getProjekt() {
        return Utils.nvl(projekt);
    }

    public void setProjekt(String projekt) {
        this.projekt = projekt;
    }

    public Integer getIdSl() {
        return idSl;
    }

    public void setIdSl(Integer  idSl) {
        this.idSl = idSl;
    }

    public Number getCastka() {
        return castka;
    }

    public void setCastka(Number castka) {
        this.castka = castka;
    }

}