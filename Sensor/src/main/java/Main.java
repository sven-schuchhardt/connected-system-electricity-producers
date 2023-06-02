

public class Main {
    public static Windkraft w1;
    public static Windkraft w2;
    public static Solaranlage s1;
    public static Haushalt h1;
    public static void main (String[] args){
        try {
            Thread.sleep(2000);
        }catch(Exception e){
            System.out.println(e);
        }
        w1 = new Windkraft("Windkraftanlage1", "Erzeuger");

        try {
            Thread.sleep(2000);
        }catch(Exception e){
            System.out.println(e);
        }
        w2 = new Windkraft("Windkraftanlage2", "Erzeuger");

       try {
            Thread.sleep(2000);
        }catch(Exception e){
            System.out.println(e);
        }
        s1 = new Solaranlage("Solaranlage1", "Erzeuger");
        try {
            Thread.sleep(2000);
        }catch(Exception e){
            System.out.println(e);
        }
        h1 = new Haushalt("Haushalt1", "Verbraucher");

        RPCClient rpc = new RPCClient();

        rpc.run();
    }
}
