import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.InputMismatchException;

class Pasien {
    private String nama;

    public Pasien(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
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
            System.out.println(pasien.getNama() + " telah ditambahkan ke antrian.");
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat menambahkan pasien ke antrian: " + e.getMessage());
        }
    }

    public Pasien panggilBerikutnya() {
        try {
            if (!antrian.isEmpty()) {
                Pasien pasien = antrian.poll();
                System.out.println("Pasien berikutnya yang dipanggil: " + pasien.getNama());
                return pasien;
            } else {
                System.out.println("Antrian kosong.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat memanggil pasien berikutnya: " + e.getMessage());
            return null;
        }
    }
}

public class Tugas3 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int noantrian = 0;
        boolean rill = true;
        AntrianRumahSakit antrianRumahSakit = new AntrianRumahSakit();

        while(rill){
            System.out.println("Selamat datang di RS Malang");
            System.out.println("1. Mendaftar pasien");
            System.out.println("2. Keluar");

            try {
                int pilih = input.nextInt();
                input.nextLine();

                switch (pilih) {
                    case 1:
                        System.out.print("Silahkan masukkan nama: ");
                        String nama = input.nextLine();
                        if (nama.trim().isEmpty()) {
                            System.out.println("Nama tidak boleh kosong.");
                            break;
                        }
                        antrianRumahSakit.tambahAntrian(new Pasien(nama));
                        noantrian++;
                        System.out.println("Terimakasih, nomor antrian anda " + noantrian + ", silahkan tunggu");
                        break;

                    case 2:
                        rill = false;
                        break;

                    default:
                        System.out.println("Pilihan tidak valid. Silahkan pilih 1 atau 2.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Input tidak valid. Silahkan masukkan angka.");
                input.nextLine(); // Membersihkan buffer
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }

        while (true) {
            Pasien pasien = antrianRumahSakit.panggilBerikutnya();
            if (pasien == null) {
                break;
            }
        }

        input.close();
    }
}
