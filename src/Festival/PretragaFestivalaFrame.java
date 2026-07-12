package Festival;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PretragaFestivalaFrame extends JFrame {

    // Polja za unos i prikaz rezultata
    private JTextField txtNaziv;
    private DefaultTableModel modelFestivala;
    private DefaultTableModel modelUlaznica;
    private JTable tabelaFestivala;
    private JTable tabelaUlaznica;

    public PretragaFestivalaFrame() {
        setTitle("Pretraga festivala");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Gornji panel - unos naziva festivala i dugme za pretragu
        JPanel panelTop = new JPanel();
        panelTop.add(new JLabel("Unesite naziv festivala:"));
        txtNaziv = new JTextField(20);
        panelTop.add(txtNaziv);
        JButton btnPretrazi = new JButton("Pretraži");
        panelTop.add(btnPretrazi);

        // Tabela za prikaz festivala
        String[] koloneFestivala = {"ID", "Naziv", "Lokacija", "Datum"};
        modelFestivala = new DefaultTableModel(koloneFestivala, 0);
        tabelaFestivala = new JTable(modelFestivala);

        // Tabela za prikaz ulaznica odabranog festivala
        String[] koloneUlaznica = {"ID", "Tip ulaznice", "Cena"};
        modelUlaznica = new DefaultTableModel(koloneUlaznica, 0);
        tabelaUlaznica = new JTable(modelUlaznica);

        // Centralni panel - gornja polovina festivali, donja ulaznice
        JPanel panelCenter = new JPanel(new GridLayout(2, 1));
        panelCenter.add(new JScrollPane(tabelaFestivala));
        panelCenter.add(new JScrollPane(tabelaUlaznica));

        getContentPane().add(panelTop, BorderLayout.NORTH);
        getContentPane().add(panelCenter, BorderLayout.CENTER);

        // Klik na "Pretraži" pokreće upit
        btnPretrazi.addActionListener(e -> {
            String naziv = txtNaziv.getText().trim();
            if (naziv.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite naziv festivala!");
                return;
            }
            pretraziFestivale(naziv);
        });

        // Klik na red u tabeli festivala učitava njegove ulaznice
        tabelaFestivala.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaFestivala.getSelectedRow() != -1) {
                int festivalId = (int) modelFestivala.getValueAt(tabelaFestivala.getSelectedRow(), 0);
                ucitajUlaznice(festivalId);
            }
        });
    }

    // Pretražuje festivale po nazivu i popunjava tabelu
    private void pretraziFestivale(String naziv) {
        modelFestivala.setRowCount(0); // brišemo prethodne rezultate
        modelUlaznica.setRowCount(0);  // i ulaznice
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM festival WHERE naziv LIKE ?")) {
            ps.setString(1, "%" + naziv + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelFestivala.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("naziv"),
                        rs.getString("lokacija"),
                        rs.getString("datum")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška prilikom pretrage!");
        }
    }

    // Učitava sve ulaznice za dati festival i popunjava donju tabelu
    private void ucitajUlaznice(int festivalId) {
        modelUlaznica.setRowCount(0);
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
            JOptionPane.showMessageDialog(this, "Greška prilikom učitavanja ulaznica!");
        }
    }
}
