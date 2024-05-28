import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class GUIantrianbarber {

    private Queue<String> antrian;
    private File daftarantrian;
    private ScheduledExecutorService urutan;
    private ScheduledFuture<?> pengerjaan;
    private Timer waktu;

    private JTextArea textArea;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextField nameField;

    private final Color PRIMARY_COLOR = Color.decode("#343F56");
    private final Color SECONDARY_COLOR = Color.decode("#F5E6CA");

    public GUIantrianbarber() {
        antrian = new LinkedList<>();
        daftarantrian = new File("daftarantrian.txt");
        urutan = Executors.newScheduledThreadPool(1);
        dataantrian();
        proses();
        guibarber();
        mulaiwaktu();
    }

    private void guibarber() {
        JFrame frame = new JFrame("Sistem Antrian Barbershop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel tampilanclient = tampilkanclient();
        cardPanel.add(tampilanclient, "AddClient");

        JPanel tampilkanantri = tampilanantri();
        cardPanel.add(tampilkanantri, "lihat antri");

        frame.getContentPane().add(cardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel tampilkanclient() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(PRIMARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Barbershop Filkom");
        titleLabel.setForeground(SECONDARY_COLOR);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(titleLabel, gbc);

        JLabel nameLabel = new JLabel("Masukkan Nama");
        nameLabel.setForeground(SECONDARY_COLOR);
        nameLabel.setFont(new Font("Serif", Font.PLAIN, 16));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(nameLabel, gbc);

        nameField = new JTextField(15);
        nameField.setBackground(SECONDARY_COLOR);
        nameField.setForeground(PRIMARY_COLOR);

        JButton addButton = new JButton("Tambah Client");
        addButton.setBackground(SECONDARY_COLOR);
        addButton.setForeground(PRIMARY_COLOR);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nama = nameField.getText();
                if (!nama.trim().isEmpty()) {
                    tambahclient(nama);
                    nameField.setText("");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(addButton, gbc);

        JButton tampilantri = new JButton("Lihat Antrian");
        tampilantri.setBackground(SECONDARY_COLOR);
        tampilantri.setForeground(PRIMARY_COLOR);
        tampilantri.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "lihat antri");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 5, 0, 5);
        panel.add(tampilantri, gbc);

        JButton exitButton = new JButton("Keluar");
        exitButton.setBackground(SECONDARY_COLOR);
        exitButton.setForeground(PRIMARY_COLOR);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                urutan.shutdown();
                waktu.stop();
                hapusSemuaAntrian();
                System.exit(0);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(exitButton, gbc);

        return panel;
    }

    private JPanel tampilanantri() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(SECONDARY_COLOR);
        textArea.setForeground(PRIMARY_COLOR);
        textArea.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(PRIMARY_COLOR);

        JButton backButton = new JButton("Kembali");
        backButton.setBackground(SECONDARY_COLOR);
        backButton.setForeground(PRIMARY_COLOR);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "tambah client");
            }
        });

        JButton exitButton = new JButton("Keluar");
        exitButton.setBackground(SECONDARY_COLOR);
        exitButton.setForeground(PRIMARY_COLOR);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                urutan.shutdown();
                waktu.stop();
                hapusSemuaAntrian();
                System.exit(0);
            }
        });

        buttonPanel.add(backButton);
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void tambahclient(String nama) {
        antrian.add(nama);
        textArea.append(nama + " telah ditambahkan ke dalam antrian.\n");
        simpanantrian();
        if (antrian.size() == 1) {
            proses();
        }
    }

    public void lihatantrian() {
        textArea.setText("");
        if (antrian.isEmpty()) {
            textArea.append("Antrian Kosong\n");
        } else {
            textArea.append("Daftar antrian saat ini:\n");
            int i = 1;
            for (String name : antrian) {
                long sisawaktu = (pengerjaan != null && i == 1) ? pengerjaan.getDelay(TimeUnit.SECONDS) : -1;
                textArea.append(i + ". " + name + (sisawaktu != -1 ? " - Sisa waktu: " + sisawaktu + " detik\n" : "\n"));
                i++;
            }
        }
    }

    private void mulaiwaktu() {
        waktu = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lihatantrian();
            }
        });
        waktu.start();
    }

    private void proses() {
        if (!antrian.isEmpty()) {
            String client = antrian.peek();
            pengerjaan = urutan.schedule(() -> {
                if (antrian.remove(client)) {
                    textArea.append("Client " + client + " telah selesai dicukur\n");
                    simpanantrian();
                    panggilclient();
                    proses();
                }
            }, 1, TimeUnit.MINUTES);
        }
    }

    private void panggilclient() {
        if (!antrian.isEmpty()) {
            String client = antrian.peek();
            textArea.append("Client " + client + " silahkan maju untuk dicukur\n");
        }
    }

    private void dataantrian() {
        try (Scanner scanner = new Scanner(daftarantrian)) {
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                antrian.add(name);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File antrian tidak ditemukan. Membuat file baru.");
        }
    }

    private void simpanantrian() {
        try (PrintWriter writer = new PrintWriter(daftarantrian)) {
            for (String nama : antrian) {
                writer.println(nama);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menyimpan antrian ke file.");
        }
    }

    private void hapusSemuaAntrian() {
        try (PrintWriter writer = new PrintWriter(daftarantrian)) {
            writer.print("");
            antrian.clear();
            System.out.println("Semua data antrian telah dihapus.");
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menghapus antrian dari file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUIantrianbarber();
            }
        });
    }
}
