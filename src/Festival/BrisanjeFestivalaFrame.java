package Festival;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BrisanjeFestivalaFrame extends JFrame {

    private JTextField txtId;
    private JButton btnObrisi;

    public BrisanjeFestivalaFrame() {
        setTitle("Brisanje festivala");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        panel.add(new JLabel("ID festivala za brisanje:"));
        txtId = new JTextField();
        panel.add(txtId);

        btnObrisi = new JButton("Obriši");
        panel.add(btnObrisi);

        add(panel);

        btnObrisi.addActionListener(e -> {
            String idStr = txtId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite ID festivala!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Da li ste sigurni da želite da obrišete festival sa ID: " + idStr + "?",
                    "Potvrda brisanja",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DAO.getConnection()) {
                    conn.setAutoCommit(false);  // počinjemo transakciju

                    try {
                        // Prvo brišemo ulaznice
                        String sqlDeleteUlaznice = "DELETE FROM ulaznica WHERE festival_id = ?";
                        PreparedStatement ps1 = conn.prepareStatement(sqlDeleteUlaznice);
                        ps1.setInt(1, Integer.parseInt(idStr));
                        ps1.executeUpdate();

                        // Onda brišemo festival
                        String sqlDeleteFestival = "DELETE FROM festival WHERE id = ?";
                        PreparedStatement ps2 = conn.prepareStatement(sqlDeleteFestival);
                        ps2.setInt(1, Integer.parseInt(idStr));
                        int deleted = ps2.executeUpdate();

                        if (deleted > 0) {
                            conn.commit();  // potvrđujemo transakciju
                            JOptionPane.showMessageDialog(this, "Festival uspešno obrisan!");
                            dispose();
                        } else {
                            conn.rollback();
                            JOptionPane.showMessageDialog(this, "Festival sa unetim ID ne postoji.");
                        }
                    } catch (Exception ex) {
                        conn.rollback();
                        throw ex;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Greška prilikom brisanja!");
                }
            }
        });

}}
