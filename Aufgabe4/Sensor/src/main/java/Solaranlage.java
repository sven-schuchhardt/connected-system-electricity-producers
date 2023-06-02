import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import com.google.gson.JsonObject;
import org.eclipse.paho.client.mqttv3.*;

public class Solaranlage extends Sensor {

    private IMqttClient client;

    Solaranlage(String name, String type) {
        this.name = name;
        this.type = type;
        this.kapazität = 11300;

        String dns = System.getenv("SERVER");
        if(dns == null){
            dns = "localhost";
        }

        try {
            this.client = new MqttClient(String.format("tcp://%s:%d", dns, 1885), name + "_publisher");
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
        String dns = System.getenv("SERVER");
        if (dns == null) {
            dns = "localhost";
        }
        System.out.println("SolaranlagenSensoren aktiv");
        // Generator gen = new Generator(type, name);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("name", name);

        rand = new Random();

        double tempKapazität = kapazität;

        while (true) {
            for (int i = 0; i < 24; i++) {
                if (i <= 11) {
                    value = rand.nextInt(250) * leistung;
                } else {
                    value = 0;
                    if (i == 23) {
                        i = 0;
                    }
                }
                if (tempKapazität - value <= 0) {
                    tempKapazität = tempKapazität - value;
                    value = 0;
                    kap = " Kapazität erreicht";
                } else {
                    kap = "Kapazität von " + kapazität + " nicht erreicht";
                }
                tempKapazität = tempKapazität - value;
                defect = rand.nextInt(10);
                if (defect == 4) {
                    System.out.println("Solaranlage Defekt ");
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                if (status == 0) {
                    value = 0;
                    kap = "Anlage ausgeschaltet";
                }

                jsonObject.addProperty("value", value);
                jsonObject.addProperty("kapazität", kap);
                MqttMessage msg = new MqttMessage(jsonObject.toString().getBytes());
                msg.setQos(1);
                msg.setRetained(true);

                try {
                    client.publish(String.format("sensor/%s", name), msg);
                } catch (MqttException e) {
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
}
