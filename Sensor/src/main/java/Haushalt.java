import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import com.google.gson.JsonObject;

public class Haushalt extends Sensor {

    Haushalt(String name, String type) {
        this.name = name;
        this.type = type;
        this.start();
    }

    public void setLeistungsAnderung(double lst) {
        leistung = lst;
    }

    public void setStatusAnderung(int sta) {
        status = sta;
    }


    public void run() {
        String dns = System.getenv("SERVER");
        if (dns == null) {
            dns = "localhost";
        }
        System.out.println("HaushaltSensor aktiv");
        // Generator gen = new Generator(type, name);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("name", name);

        rand = new Random();


        while (true) {
            value = rand.nextInt(75);

            if(status == 0){
                value = 0;
                kap = "Haushalt abgeschaltet";
            }

            jsonObject.addProperty("value", value);
            byte[] toSend = jsonObject.toString().getBytes();
            try {
                InetAddress address = InetAddress.getByName(dns);
                DatagramSocket socket = new DatagramSocket();

                socket.send(new DatagramPacket(toSend, toSend.length, address, port));
                //System.out.println("Sent Package: " + new String(toSend));
            } catch (Exception e) {
                System.err.println("Could not send package!");
            }
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}
