import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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
        antrian.offer(pasien);
        System.out.println(pasien.getNama() + " telah ditambahkan ke antrian.");
    }

    public Pasien panggilBerikutnya() {
        if (!antrian.isEmpty()) {
            Pasien pasien = antrian.poll();
            System.out.println("Pasien berikutnya yang dipanggil: " + pasien.getNama());
            return pasien;
        } else {
            System.out.println("Antrian kosong.");
            return null;
        }
    }
}

public class Tugas2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int noantrian = 0;
        boolean rill = true;
        AntrianRumahSakit antrianRumahSakit = new AntrianRumahSakit();

        while(rill){
        System.out.println("Selamat datang di RS Malang");
        System.out.println("1. Mendaftar pasien");
        System.out.println("2. Keluar");
        int pilih = input.nextInt();
        input.nextLine();
            switch (pilih) {
                case 1:
                System.out.print("Silahkan masukkan nama: ");
                String nama = input.nextLine();
                antrianRumahSakit.tambahAntrian(new Pasien(nama));
                noantrian++;
                
                System.out.println("Terimakasih, nomor antrian anda " + noantrian + ", silahkan tunggu");
                break;

                case 2:
                rill = false;
                break;
            
                default:
                System.out.println("Invalid");
                break;
            }
            
        }

        for(int i = 0; i < noantrian; i++){
        antrianRumahSakit.panggilBerikutnya();
        }
    }
}
