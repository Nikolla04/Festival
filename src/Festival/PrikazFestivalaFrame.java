package Festival;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrikazFestivalaFrame extends JFrame {

    private DefaultTableModel modelFestivala;
    private DefaultTableModel modelUlaznica;
    private JTable tabelaFestivala;
    private JTable tabelaUlaznica;

    public PrikazFestivalaFrame() {
        setTitle("Prikaz festivala i ulaznica");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Model za festivale
        String[] koloneFestivala = {"ID", "Naziv", "Lokacija", "Datum"};
        modelFestivala = new DefaultTableModel(koloneFestivala, 0);
        tabelaFestivala = new JTable(modelFestivala);

        // Model za ulaznice
        String[] koloneUlaznica = {"ID", "Tip ulaznice", "Cena"};
        modelUlaznica = new DefaultTableModel(koloneUlaznica, 0);
        tabelaUlaznica = new JTable(modelUlaznica);

        // Dodavanje u panel
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JScrollPane(tabelaFestivala));
        panel.add(new JScrollPane(tabelaUlaznica));

        getContentPane().add(panel, BorderLayout.CENTER);

        ucitajFestivale();

        // Kada korisnik selektuje festival, prikazujemo njegove ulaznice
        tabelaFestivala.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaFestivala.getSelectedRow() != -1) {
                int festivalId = (int) modelFestivala.getValueAt(tabelaFestivala.getSelectedRow(), 0);
                ucitajUlaznice(festivalId);
            }
        });
    }

    private void ucitajFestivale() {
        modelFestivala.setRowCount(0);
        try (Connection conn = DAO.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM festival");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                modelFestivala.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("naziv"),
                        rs.getString("lokacija"),
                        rs.getString("datum")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju festivala.");
        }
    }

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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju ulaznica.");
        }
    }
}
