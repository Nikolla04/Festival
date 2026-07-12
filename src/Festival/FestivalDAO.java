package Festival;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FestivalDAO {

    public void dodajFestival(festival f) throws Exception {
        // Otvaramo konekciju ka bazi
        Connection conn = DAO.getConnection();

        // 1️ Dodavanje festivala u tabelu 'festival'
        String sqlFestival = "INSERT INTO festival (naziv, lokacija, datum) VALUES (?, ?, ?)";
        // Kreiramo PreparedStatement sa opcijom da nam vrati automatski generisani ID
        PreparedStatement psFestival = conn.prepareStatement(sqlFestival, Statement.RETURN_GENERATED_KEYS);
        // Postavljamo vrednosti za naziv, lokaciju i datum
        psFestival.setString(1, f.getNaziv());
        psFestival.setString(2, f.getLokacija());
        psFestival.setString(3, f.getDatum());
        // Izvršavamo INSERT upit
        psFestival.executeUpdate();

        // Dobijamo generisani ključ (ID festivala)
        ResultSet rsKeys = psFestival.getGeneratedKeys();
        int festivalId = -1;
        if (rsKeys.next()) {
            festivalId = rsKeys.getInt(1); // Uzimamo ID iz prvog reda rezultata
        }

        // 2️ Dodavanje svih ulaznica koje pripadaju tom festivalu
        String sqlUlaznica = "INSERT INTO ulaznica (tip, cena, festival_id) VALUES (?, ?, ?)";
        PreparedStatement psUlaznica = conn.prepareStatement(sqlUlaznica);

        // Prolazimo kroz listu ulaznica iz objekta 'festival'
        for (Ulaznica u : f.getUlaznice()) {
            // Postavljamo tip, cenu i ID festivala za svaku ulaznicu
            psUlaznica.setString(1, u.getTip());
            psUlaznica.setDouble(2, u.getCena());
            psUlaznica.setInt(3, festivalId);
            // Izvršavamo INSERT za svaku ulaznicu
            psUlaznica.executeUpdate();
        }

        // Zatvaramo konekciju 
        conn.close();
    }
    public List<festival> sviFestivali() throws Exception {
        // Lista u koju ćemo smestiti sve festivale
        List<festival> lista = new ArrayList<>();

        // Dobijamo konekciju ka bazi preko DAO klase
        Connection conn = DAO.getConnection();

        // Kreiramo Statement za izvršavanje osnovnog SELECT upita
        Statement st = conn.createStatement();
        
        // Izvršavamo upit koji vraća sve festivale
        ResultSet rs = st.executeQuery("SELECT * FROM festival");

        // Prolazimo kroz svaki festival iz rezultata
        while(rs.next()) {
            // Kreiramo novi objekat 'festival' iz podataka dobijenih iz baze
            festival f = new festival(
                rs.getInt("id"),
                rs.getString("naziv"),
                rs.getString("lokacija"),
                rs.getString("datum")
            );

            // Pripremamo upit za dobijanje svih ulaznica vezanih za trenutni festival
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM ulaznica WHERE festival_id = ?"
            );
            // Postavljamo ID festivala u upit
            ps.setInt(1, f.getId());
            
            // Izvršavamo upit za ulaznice
            ResultSet rsUlaznica = ps.executeQuery();

            // Prolazimo kroz sve ulaznice i dodajemo ih festivalu
            while (rsUlaznica.next()) {
                Ulaznica u = new Ulaznica(
                    rsUlaznica.getInt("id"),
                    rsUlaznica.getString("tip"),
                    rsUlaznica.getDouble("cena")
                );
                f.dodajUlaznicu(u);
            }

            // Dodajemo kompletno popunjen festival u listu
            lista.add(f);
        }

        // Zatvaramo konekciju ka bazi
        conn.close();

        // Vraćamo listu svih festivala sa njihovim ulaznicama
        return lista;
    }
}