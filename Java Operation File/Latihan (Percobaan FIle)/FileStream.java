import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileStream {
    public static void main(String[] args) {
        int kar;
        try {
            FileInputStream filel = new FileInputStream("C:/Users/Alva Shaquilla R/Downloads/cobacoba.txt");
            FileOutputStream fileout = new FileOutputStream("cobacoba.txt");
            while ((kar = filel.read()) != -1) {
                fileout.write(kar);
                System.out.print((char) kar);
            }
            fileout.close();
            filel.close();
        } catch (Exception e) {
            System.out.println("File tidak ada");
        }
        System.out.println("");
    }
}
