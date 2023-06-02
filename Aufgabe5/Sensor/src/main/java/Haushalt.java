import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import com.google.gson.JsonObject;
import org.eclipse.paho.client.mqttv3.*;

public class Haushalt extends Sensor {


    private IMqttClient client;

    Haushalt(String name, String type) {
        this.name = name;
        this.type = type;

        String dns = System.getenv("SERVER");
        if(dns == null){
            dns = "localhost";
        }

        try {
            this.client = new MqttClient(String.format("tcp://%s:%d", dns, 1883), name + "_publisher");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);

        } catch (MqttException e) {
            System.err.println("Could not publish message! Const");
        }


        this.start();
    }

    public void setLeistungsAnderung(double lst) {
        leistung = lst;
    }

    public void setStatusAnderung(int sta) {
        status = sta;
    }


    public void run() {
        /*String dns = System.getenv("SERVER");
        if (dns == null) {
            dns = "localhost";
        }*/
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


            MqttMessage msg = new MqttMessage(jsonObject.toString().getBytes());
            msg.setQos(1);
            msg.setRetained(true);

            try{
                client.publish(String.format("sensor/%s", name), msg);
            }catch(MqttException e){
                System.err.println("Could not publish message!");
            }

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }
}
