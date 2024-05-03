package generic;

public class genericApp {
    public static void main(String[] args) {
        genericClass <Integer> Mydata = new genericClass<Integer>(69);
        System.out.println(Mydata.getData());
    }
}
