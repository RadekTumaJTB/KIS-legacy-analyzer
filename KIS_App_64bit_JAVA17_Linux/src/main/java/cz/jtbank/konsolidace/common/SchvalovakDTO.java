package cz.jtbank.konsolidace.common;
import java.util.List;
import java.util.ArrayList;

public class SchvalovakDTO {

    private String id;
    private String Spolecnost = null;
    private String typTransakce = null;
    private String Popis = null;
    private String Mena = null;
    private String Protistrana = null;


    private List<SchvalovakRadekDTO> radky = null;
	
	public SchvalovakDTO (String id){
		super();
		this.id = id;
	}

    public String getMena() {
        return Mena;
    }

    public void setMena(String Mena) {
        this.Mena = Mena;
    }

    public String getPopis() {
        return Popis;
    }

    public void setPopis(String Popis) {
        this.Popis = Popis;
    }

    public String getProtistrana() {
        return Protistrana;
    }

    public void setProtistrana(String Protistrana) {
        this.Protistrana = Protistrana;
    }

    public void addProtistrana(String Protistrana) {
        this.Protistrana = Protistrana;
    }

    public String getSpolecnost() {
        return Spolecnost;
    }

    public void setSpolecnost(String Spolecnost) {
        this.Spolecnost = Spolecnost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SchvalovakRadekDTO> getRadky() {
        return radky;
    }

    public void setRadky(List<SchvalovakRadekDTO> radky) {
        this.radky = radky;
    }

    public void addRadek(SchvalovakRadekDTO radek) {
        this.radky.add(radek);
    }


    public String getTypTransakce() {
        return typTransakce;
    }

    public void setTypTransakce(String typTransakce) {
        this.typTransakce = typTransakce;
    }

    
}
