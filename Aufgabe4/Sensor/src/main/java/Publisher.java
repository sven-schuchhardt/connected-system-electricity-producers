import com.google.gson.JsonObject;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class Publisher {
    private String ip = "localhost";
    private String port = "4711";
    private IMqttClient client;
    private Generator gen;
    private String id;

    Publisher(Generator generator, String name) throws MqttException{
        this.gen = generator;
        this.id = id;
        this.client = new MqttClient(String .format("tcp://%s:%d", ip, port),id + "_publisher");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
    }

    //@Override
    public void run(){
        DecimalFormat df = new DecimalFormat("#.##");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("type", gen.getType());
        jsonObject.addProperty("name", gen.getName());
        jsonObject.addProperty("value", df.format(gen.generate()));

        MqttMessage msg = new MqttMessage(jsonObject.toString().getBytes());
        msg.setQos(0);
        msg.setRetained(true);

        try{
            client.publish(String.format("sensor/%s", gen.getName()), msg);
        }catch(MqttException e){
            System.err.println("Could not publish message!");
        }

    }

}
