import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Pasien {
    private String nama;
    private int nomorAntrian;
    private Date tanggal;

    public Pasien(String nama, int nomorAntrian, Date tanggal) {
        this.nama = nama;
        this.nomorAntrian = nomorAntrian;
        this.tanggal = tanggal;
    }

    public String getNama() {
        return nama;
    }

    public int getNomorAntrian() {
        return nomorAntrian;
    }

    public Date getTanggal() {
        return tanggal;
    }
}

class AntrianRumahSakit {
    private Queue<Pasien> antrian;
    private static final String FILE_NAME = "data_pasien.txt";

    public AntrianRumahSakit() {
        antrian = new LinkedList<>();
    }

    public void tambahAntrian(Pasien pasien) {
        try {
            antrian.offer(pasien);
            tulisKeFile(pasien);
            System.out.println(pasien.getNama() + " telah ditambahkan ke antrian.");
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menambahkan pasien ke antrian: " + e.getMessage());
        }
    }

    private void tulisKeFile(Pasien pasien) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            String data = pasien.getNomorAntrian() + ", " + pasien.getNama() + ", " + sdf.format(pasien.getTanggal());
            writer.write(data);
            writer.newLine();
        }
    }

    public void tampilkanAntrian(JTextArea textArea) {
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            textArea.setText("");
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                textArea.append(data + "\n");
            }
        } catch (IOException e) {
            textArea.setText("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
    }
}

public class Tugas5 extends JFrame {
    private AntrianRumahSakit antrianRumahSakit;
    private int noAntrian;
    private JTextField namaField;
    private JTextArea antrianArea;

    public Tugas5() {
        antrianRumahSakit = new AntrianRumahSakit();
        noAntrian = 0;

        setTitle("RS Malang");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(173, 216, 230));

        JLabel namaLabel = new JLabel("Masukkan Nama:");
        namaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        namaLabel.setForeground(new Color(0, 102, 102));
        namaField = new JTextField();

        JButton daftarButton = new JButton("Daftar Pasien");
        daftarButton.setFont(new Font("Arial", Font.BOLD, 14));
        daftarButton.setBackground(new Color(50, 205, 50));
        daftarButton.setForeground(Color.WHITE);
        daftarButton.setIcon(new ImageIcon("icons/add.png"));
        daftarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nama = namaField.getText().trim();
                if (nama.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nama tidak boleh kosong.");
                } else {
                    noAntrian++;
                    Date tanggal = new Date();
                    antrianRumahSakit.tambahAntrian(new Pasien(nama, noAntrian, tanggal));
                    JOptionPane.showMessageDialog(null, "Terimakasih, nomor antrian anda " + noAntrian + ", silahkan tunggu");
                    namaField.setText("");
                }
            }
        });

        JButton tampilkanButton = new JButton("Tampilkan Antrian");
        tampilkanButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        tampilkanButton.setBackground(new Color(70, 130, 180));
        tampilkanButton.setForeground(Color.WHITE);
        tampilkanButton.setIcon(new ImageIcon("icons/view.png"));
        tampilkanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                antrianRumahSakit.tampilkanAntrian(antrianArea);
            }
        });

        JButton keluarButton = new JButton("Keluar");
        keluarButton.setFont(new Font("Arial", Font.BOLD, 14));
        keluarButton.setBackground(new Color(255, 69, 0));
        keluarButton.setForeground(Color.WHITE);
        keluarButton.setIcon(new ImageIcon("icons/exit.png"));
        keluarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin keluar?", "Konfirmasi Keluar",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        inputPanel.add(namaLabel);
        inputPanel.add(namaField);
        inputPanel.add(daftarButton);
        inputPanel.add(tampilkanButton);

        container.add(inputPanel, BorderLayout.NORTH);

        antrianArea = new JTextArea();
        antrianArea.setEditable(false);
        antrianArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        antrianArea.setBackground(new Color(255, 250, 205));
        JScrollPane scrollPane = new JScrollPane(antrianArea);

        container.add(scrollPane, BorderLayout.CENTER);
        container.add(keluarButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Tugas5().setVisible(true);
            }
        });
    }
}
