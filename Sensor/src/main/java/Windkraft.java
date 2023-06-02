import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import com.google.gson.JsonObject;

public class Windkraft extends Sensor{

    Windkraft(String name, String type){
        this.name = name;
        this.type = type;
        this.kapazität = 1000;
        this.start();
    }

    public void setLeistungsAnderung(double lst){
        leistung = lst;
    }
    public void setStatusAnderung(int sta){
        status = sta;
    }


    public void run(){
        String dns = System.getenv("SERVER");
        if(dns == null){
            dns = "localhost";
        }
        System.out.println("WindkraftSensoren aktiv");
       // Generator gen = new Generator(type, name);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("name", name);

        rand = new Random();


        double tempKapazität = kapazität;
        defect = rand.nextInt(10);
        if(defect == 4){
            System.out.println("Windkraftanlage Defekt ");
            try{
                Thread.sleep(5);
            }catch(Exception e){
                System.out.println(e);
            }
        }
        while(true){
            value = rand.nextInt(250) * leistung;
            if(tempKapazität-value  <= 0){
                tempKapazität = tempKapazität - value;
                value = 0;
               kap = " Kapazität erreicht";
            }else{
                kap = "Kapazität von " + kapazität + " nicht erreicht";
            }
            tempKapazität = tempKapazität - value;

            if(status == 0){
                value = 0;
                kap = "Anlage ausgeschaltet";
            }

            jsonObject.addProperty("value", value);
            jsonObject.addProperty("kapazität", kap);
            byte[] toSend = jsonObject.toString().getBytes();
            try{
                InetAddress address = InetAddress.getByName(dns);
                DatagramSocket socket = new DatagramSocket();

                socket.send(new DatagramPacket(toSend, toSend.length, address, port));
                //System.out.println("Sent Package: " + new String(toSend));
            }catch(Exception e){
                System.err.println("Could not send package!");
            }
            try {
                Thread.sleep(3000);
            }catch(Exception e){
                System.out.println(e);
            }
        }

    }
}
