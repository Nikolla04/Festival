package Festival;

import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUI frame = new GUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GUI() {
        setTitle("Evidencija festivala");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        contentPane.setLayout(new GridLayout(5, 1, 10, 10));

        JButton btnUnos = new JButton("Unos festivala");
        JButton btnPrikaz = new JButton("Prikaz festivala");
        JButton btnPretraga = new JButton("Pretraga festivala");
        JButton btnIzmena = new JButton("Izmena festivala");
        JButton btnBrisanje = new JButton("Brisanje festivala");

        contentPane.add(btnUnos);
        contentPane.add(btnPrikaz);
        contentPane.add(btnPretraga);
        contentPane.add(btnIzmena);
        contentPane.add(btnBrisanje);

        btnUnos.addActionListener(e -> {
            new UnosFestivalaFrame().setVisible(true);
        });

        btnPrikaz.addActionListener(e -> {
            new PrikazFestivalaFrame().setVisible(true);
        });

        btnPretraga.addActionListener(e -> {
            new PretragaFestivalaFrame().setVisible(true);
        });

        btnIzmena.addActionListener(e -> {
            String nazivFestivala = JOptionPane.showInputDialog(null, "Unesite naziv festivala za izmenu:");
            if (nazivFestivala != null && !nazivFestivala.trim().isEmpty()) {
                new IzmenaFestivalaFrame(nazivFestivala.trim()).setVisible(true);
            }
        });

        btnBrisanje.addActionListener(e -> {
            new BrisanjeFestivalaFrame().setVisible(true);
        });

    }
}
