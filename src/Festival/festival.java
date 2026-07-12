package Festival;

import java.util.ArrayList;
import java.util.List;

public class festival {
    private int id;
    private String naziv;
    private String lokacija;
    private String datum;
    private List<Ulaznica> ulaznice;

    public festival(int id, String naziv, String lokacija, String datum) {
        this.id = id;
        this.naziv = naziv;
        this.lokacija = lokacija;
        this.datum = datum;
        this.ulaznice = new ArrayList<>();
    }

    public void dodajUlaznicu(Ulaznica u) {
        ulaznice.add(u);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public List<Ulaznica> getUlaznice() {
        return ulaznice;
    }

    public void setUlaznice(List<Ulaznica> ulaznice) {
        this.ulaznice = ulaznice;
    }
}
