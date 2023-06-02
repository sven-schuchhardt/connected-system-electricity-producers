import org.eclipse.paho.client.mqttv3.MqttException;

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
        if(System.getenv("NAME").equals("sensoren1")) {
            w1 = new Windkraft("Windkraftanlage1", "Erzeuger");

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println(e);
            }
            s1 = new Solaranlage("Solaranlage1", "Erzeuger");

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }else{
            w1 = new Windkraft("Windkraftanlage3", "Erzeuger");

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println(e);
            }
            s1 = new Solaranlage("Solaranlage4", "Erzeuger");

            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        //RPCClient rpc = new RPCClient();

        //rpc.run();
    }
}
