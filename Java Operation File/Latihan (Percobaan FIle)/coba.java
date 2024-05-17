import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class coba {
    public static void main(String[] args) {
        try {
            File f = new File("C:/Users/Alva Shaquilla R/Downloads/cobacoba.txt");
            FileReader reader = new FileReader(f);
            BufferedReader buff = new BufferedReader(reader);
            String x = buff.readLine();
            while (x != null) {
                System.out.println(x);
                x = buff.readLine();
            }
            buff.close();
            reader.close();
        } catch (Exception e) {
            System.out.println("FIle tidak ada");
        }
    }
}
