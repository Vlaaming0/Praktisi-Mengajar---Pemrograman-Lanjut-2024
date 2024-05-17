import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Date;
import java.util.InputMismatchException;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/Alva Shaquilla R/Downloads/apalah.txt", true))) {
            String data = pasien.getNomorAntrian() + ", " + pasien.getNama() + ", " + sdf.format(pasien.getTanggal());
            writer.write(data);
            writer.newLine();
            System.out.println("Data pasien telah ditulis ke file: " + data); // Pesan debug
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis ke file: " + e.getMessage());
        }
    }

    public void panggilBerikutnya() {
        try {
            if (!antrian.isEmpty()) {
                Pasien pasien = antrian.poll();
                System.out.println("Pasien berikutnya yang dipanggil: " + pasien.getNama());
            } else {
                System.out.println("Antrian kosong.");
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat memanggil pasien berikutnya: " + e.getMessage());
        }
    }

    public void tampilkanAntrian() {
        try (Scanner scanner = new Scanner(new File("C:/Users/Alva Shaquilla R/Downloads/apalah.txt"))) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                System.out.println(data);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
    }
}

public class Tasdasd {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int noAntrian = 0;
        boolean berjalan = true;
        AntrianRumahSakit antrianRumahSakit = new AntrianRumahSakit();

        while (berjalan) {
            System.out.println("Selamat datang di RS Malang");
            System.out.println("1. Mendaftar pasien");
            System.out.println("2. Tampilkan antrian");
            System.out.println("3. Keluar");

            try {
                int pilihan = input.nextInt();
                input.nextLine();

                switch (pilihan) {
                    case 1:
                        System.out.print("Silahkan masukkan nama: ");
                        String nama = input.nextLine();
                        if (nama.trim().isEmpty()) {
                            System.out.println("Nama tidak boleh kosong.");
                            break;
                        }
                        noAntrian++;
                        Date tanggal = new Date();
                        antrianRumahSakit.tambahAntrian(new Pasien(nama, noAntrian, tanggal));
                        System.out.println("Terimakasih, nomor antrian anda " + noAntrian + ", silahkan tunggu");
                        break;

                    case 2:
                        System.out.println("Antrian saat ini:");
                        antrianRumahSakit.tampilkanAntrian();
                        break;

                    case 3:
                        berjalan = false;
                        break;

                    default:
                        System.out.println("Pilihan tidak valid. Silahkan pilih 1, 2, atau 3.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Silahkan masukkan angka.");
                input.nextLine(); // Membersihkan buffer
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }

        input.close();
    }
}
