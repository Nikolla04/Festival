package Festival;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IzmenaFestivalaFrame extends JFrame {

    // Polja forme za podatke o festivalu
    private JTextField txtId, txtNaziv, txtLokacija, txtDatum;

    // Tabela i njen model za prikaz i izmenu ulaznica
    private JTable tabelaUlaznica;
    private DefaultTableModel modelUlaznica;

    // ID festivala koji trenutno uređujemo
    private int festivalId = -1;

    // Konstruktor - prima naziv festivala i učitava njegove podatke
    public IzmenaFestivalaFrame(String nazivFestivala) {
        setTitle("Izmena festivala");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Gornji panel - podaci o festivalu
        JPanel panelTop = new JPanel(new GridLayout(4, 2, 5, 5));
        panelTop.add(new JLabel("ID:"));
        txtId = new JTextField();
        txtId.setEditable(false); // ID se ne menja ručno
        panelTop.add(txtId);

        panelTop.add(new JLabel("Naziv:"));
        txtNaziv = new JTextField();
        panelTop.add(txtNaziv);

        panelTop.add(new JLabel("Lokacija:"));
        txtLokacija = new JTextField();
        panelTop.add(txtLokacija);

        panelTop.add(new JLabel("Datum:"));
        txtDatum = new JTextField();
        panelTop.add(txtDatum);

        getContentPane().add(panelTop, BorderLayout.NORTH);

        // Tabela ulaznica
        String[] koloneUlaznica = {"ID", "Tip ulaznice", "Cena"};
        modelUlaznica = new DefaultTableModel(koloneUlaznica, 0);
        tabelaUlaznica = new JTable(modelUlaznica);
        getContentPane().add(new JScrollPane(tabelaUlaznica), BorderLayout.CENTER);

        // Panel sa dugmadima ispod tabele
        JPanel panelDugmici = new JPanel();
        JButton btnDodaj = new JButton("Dodaj ulaznicu");
        JButton btnIzmeni = new JButton("Izmeni ulaznicu");
        JButton btnObrisi = new JButton("Obriši ulaznicu");
        JButton btnSacuvaj = new JButton("Sačuvaj izmene");

        panelDugmici.add(btnDodaj);
        panelDugmici.add(btnIzmeni);
        panelDugmici.add(btnObrisi);
        panelDugmici.add(btnSacuvaj);
        getContentPane().add(panelDugmici, BorderLayout.SOUTH);

        // Učitavanje podataka o festivalu iz baze
        ucitajFestivalPoNazivu(nazivFestivala);

        // Ako je festival pronađen, učitaj njegove ulaznice
        if (festivalId != -1) {
            ucitajUlaznice();
        }

        // Povezivanje dugmadi sa funkcijama
        btnDodaj.addActionListener(e -> dodajUlaznicu());
        btnIzmeni.addActionListener(e -> izmeniUlaznicu());
        btnObrisi.addActionListener(e -> obrisiUlaznicu());
        btnSacuvaj.addActionListener(e -> sacuvajIzmene());
    }

    // Dohvata festival po nazivu i popunjava polja forme
    private void ucitajFestivalPoNazivu(String naziv) {
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM festival WHERE naziv = ?")) {
            ps.setString(1, naziv);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                festivalId = rs.getInt("id");
                txtId.setText(String.valueOf(festivalId));
                txtNaziv.setText(rs.getString("naziv"));
                txtLokacija.setText(rs.getString("lokacija"));
                txtDatum.setText(rs.getString("datum"));
            } else {
                JOptionPane.showMessageDialog(this, "Festival sa tim nazivom nije pronađen!");
                dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju festivala!");
            dispose();
        }
    }

    // Učitava sve ulaznice povezane sa trenutnim festivalom
    private void ucitajUlaznice() {
        modelUlaznica.setRowCount(0); // brišemo stare redove iz tabele
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM ulaznica WHERE festival_id = ?")) {
            ps.setInt(1, festivalId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelUlaznica.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("tip"),
                        rs.getDouble("cena")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju ulaznica!");
        }
    }

    // Dodaje novu ulaznicu u tabelu (ali još ne u bazu)
    private void dodajUlaznicu() {
        String tip = JOptionPane.showInputDialog(this, "Unesite tip ulaznice:");
        if (tip == null || tip.trim().isEmpty()) return;

        String cenaStr = JOptionPane.showInputDialog(this, "Unesite cenu ulaznice:");
        if (cenaStr == null || cenaStr.trim().isEmpty()) return;

        try {
            double cena = Double.parseDouble(cenaStr);
            modelUlaznica.addRow(new Object[]{0, tip, cena}); // id 0 = nova ulaznica
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cena mora biti broj!");
        }
    }

    // Menja podatke izabrane ulaznice u tabeli
    private void izmeniUlaznicu() {
        int selectedRow = tabelaUlaznica.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Izaberite ulaznicu za izmenu!");
            return;
        }

        String trenutniTip = (String) modelUlaznica.getValueAt(selectedRow, 1);
        String noviTip = JOptionPane.showInputDialog(this, "Izmeni tip ulaznice:", trenutniTip);
        if (noviTip == null || noviTip.trim().isEmpty()) return;

        String trenutnaCena = modelUlaznica.getValueAt(selectedRow, 2).toString();
        String novaCenaStr = JOptionPane.showInputDialog(this, "Izmeni cenu ulaznice:", trenutnaCena);
        if (novaCenaStr == null || novaCenaStr.trim().isEmpty()) return;

        try {
            double novaCena = Double.parseDouble(novaCenaStr);
            modelUlaznica.setValueAt(noviTip, selectedRow, 1);
            modelUlaznica.setValueAt(novaCena, selectedRow, 2);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cena mora biti broj!");
        }
    }

    // Briše izabranu ulaznicu iz tabele
    private void obrisiUlaznicu() {
        int selectedRow = tabelaUlaznica.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Izaberite ulaznicu za brisanje!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Da li ste sigurni da želite da obrišete ovu ulaznicu?", "Potvrda", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            modelUlaznica.removeRow(selectedRow);
        }
    }

    // Čuva izmene u bazi - festival i sve njegove ulaznice
    private void sacuvajIzmene() {
        String naziv = txtNaziv.getText().trim();
        String lokacija = txtLokacija.getText().trim();
        String datum = txtDatum.getText().trim();

        if (naziv.isEmpty() || lokacija.isEmpty() || datum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Popunite sva polja festivala!");
            return;
        }

        try (Connection conn = DAO.getConnection()) {
            conn.setAutoCommit(false); // započinjemo transakciju

            // 1. Ažuriramo festival
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE festival SET naziv=?, lokacija=?, datum=? WHERE id=?")) {
                ps.setString(1, naziv);
                ps.setString(2, lokacija);
                ps.setString(3, datum);
                ps.setInt(4, festivalId);
                ps.executeUpdate();
            }

            // 2. Brišemo sve stare ulaznice
            try (PreparedStatement psDelete = conn.prepareStatement(
                    "DELETE FROM ulaznica WHERE festival_id=?")) {
                psDelete.setInt(1, festivalId);
                psDelete.executeUpdate();
            }

            // 3. Dodajemo sve nove/izmenjene ulaznice iz tabele
            try (PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO ulaznica (festival_id, tip, cena) VALUES (?, ?, ?)")) {
                for (int i = 0; i < modelUlaznica.getRowCount(); i++) {
                    String tip = (String) modelUlaznica.getValueAt(i, 1);
                    double cena = (double) modelUlaznica.getValueAt(i, 2);
                    psInsert.setInt(1, festivalId);
                    psInsert.setString(2, tip);
                    psInsert.setDouble(3, cena);
                    psInsert.addBatch();
                }
                psInsert.executeBatch();
            }

            // Potvrđujemo transakciju
            conn.commit();
            JOptionPane.showMessageDialog(this, "Izmene su sačuvane!");
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri čuvanju izmena!");
        }
    }
}
