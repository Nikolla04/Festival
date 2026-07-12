package Festival;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnosFestivalaFrame extends JFrame {
    private JTextField txtNaziv, txtLokacija, txtDatum;
    private JTextField txtTipUlaznice, txtCenaUlaznice;
    private DefaultListModel<String> ulazniceListModel;
    private List<Ulaznica> ulaznice; // Lista unetih ulaznica

    public UnosFestivalaFrame() {
        setTitle("Unos festivala");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ulaznice = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 5, 5));

        // Festival podaci
        panel.add(new JLabel("Naziv:"));
        txtNaziv = new JTextField();
        panel.add(txtNaziv);

        panel.add(new JLabel("Lokacija:"));
        txtLokacija = new JTextField();
        panel.add(txtLokacija);

        panel.add(new JLabel("Datum:"));
        txtDatum = new JTextField();
        panel.add(txtDatum);

        // Ulaznice - unos
        panel.add(new JLabel("Tip ulaznice:"));
        txtTipUlaznice = new JTextField();
        panel.add(txtTipUlaznice);

        panel.add(new JLabel("Cena ulaznice:"));
        txtCenaUlaznice = new JTextField();
        panel.add(txtCenaUlaznice);

        JButton btnDodajUlaznicu = new JButton("Dodaj ulaznicu");
        panel.add(btnDodajUlaznicu);

        // Prazno mesto da raspored bude uredan
        panel.add(new JLabel());

        // Lista unetih ulaznica
        ulazniceListModel = new DefaultListModel<>();
        JList<String> listaUlaznica = new JList<>(ulazniceListModel);
        JScrollPane scrollUlaznice = new JScrollPane(listaUlaznica);
        scrollUlaznice.setPreferredSize(new Dimension(350, 100));

        // Dugme za cuvanje festivala
        JButton btnSacuvaj = new JButton("Sačuvaj festival");

        // Glavni layout
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        content.add(panel, BorderLayout.NORTH);
        content.add(scrollUlaznice, BorderLayout.CENTER);
        content.add(btnSacuvaj, BorderLayout.SOUTH);

        // Dodavanje ulaznice u listu
        btnDodajUlaznicu.addActionListener(e -> {
            String tip = txtTipUlaznice.getText().trim();
            String cenaStr = txtCenaUlaznice.getText().trim();

            if (tip.isEmpty() || cenaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Popunite polja za tip i cenu ulaznice.");
                return;
            }

            double cena;
            try {
                cena = Double.parseDouble(cenaStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cena mora biti broj.");
                return;
            }

            Ulaznica ulaznica = new Ulaznica(0, tip, cena); // id ce baza dodeliti
            ulaznice.add(ulaznica);
            ulazniceListModel.addElement(tip + " - " + cena + " RSD");

            // Ocisti polja za novu ulaznicu
            txtTipUlaznice.setText("");
            txtCenaUlaznice.setText("");
        });

        // Sacuvaj festival sa ulaznicama
        btnSacuvaj.addActionListener(e -> {
            String naziv = txtNaziv.getText().trim();
            String lokacija = txtLokacija.getText().trim();
            String datum = txtDatum.getText().trim();

            if (naziv.isEmpty() || lokacija.isEmpty() || datum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Popunite sva polja festivala.");
                return;
            }
            if (ulaznice.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Dodajte bar jednu ulaznicu.");
                return;
            }

            try {
                FestivalDAO dao = new FestivalDAO();
                festival f = new festival(0, naziv, lokacija, datum);
                f.setUlaznice(ulaznice);

                dao.dodajFestival(f);

                JOptionPane.showMessageDialog(this, "Festival i ulaznice uspešno sačuvani!");
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Greška prilikom čuvanja.");
            }
        });
    }
}
