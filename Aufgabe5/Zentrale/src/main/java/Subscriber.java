import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.*;

public class Subscriber extends Thread {

    private List<String> historie = new ArrayList<String>();
    boolean notSuccessfull = true;

    MqttClient client;
    private static final String TOPICS = "sensor/+";

    Subscriber() {
        String dns = System.getenv("SERVER");
        if(dns == null){
            dns = "localhost";
        }
        historie.add("Historie vom externen Client: ");
        String port = System.getenv("BROKER_PORT");
        int portInt = Integer.parseInt(port);
        String uri = String.format("tcp://%s:%d", dns, portInt);

        while(notSuccessfull) {
            try {
                System.out.println(dns);
                client = new MqttClient(uri, UUID.randomUUID().toString());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);
                options.setConnectionTimeout(10);
                client.connect(options);
                System.out.println("Sub-Konstruktor-Erfolgreich");
                notSuccessfull = false;
            } catch (MqttException e) {
                System.err.println("Could not publish: Zentrale: Konstrutor");
                notSuccessfull = true;
            }
        }
    }

    public List<String> getHistorie(int min, int max) {
        List<String>temp = new ArrayList<String>();;
        for(int i = min; i < max; i++){
            temp.add(historie.get(i));
        }
        return temp;
    }
    public List<String> getHist(){return historie;}
    public void setHistorie(List<String> a) {
        for(int i = 0; i < a.size(); i++){
            historie.add(a.get(i));
        }
    }
    @Override
    public void run() {

        try {
            client.subscribe(TOPICS, (topic, message) -> {
                SensorData m = new Gson().fromJson(new String(message.getPayload()).trim(), SensorData.class);
                String line = new String(message.getPayload());
                historie.add(line);
                Optional<Sensor> s = DataStorage.getInstance().sensors.stream()
                        .filter(x -> x.getName().equals(m.getName())).findFirst();

                if (s.isPresent()) {
                    s.get().addSensorData(m);
                    //s.get().addLineData(line);
                } else {
                    Sensor newSens = new Sensor(m.getName(), m.getType());
                    newSens.addSensorData(m);
                    //newSens.addLineData(line);
                    DataStorage.getInstance().sensors.add(newSens);
                }
                System.out.println(String.format("Name: %s%nType: %s%nStrommenge: %s%nKapazität: %s%n", m.getName(), m.getType(), m.getValue(), m.getKapazität()));
                System.out.println();
            });
        } catch (MqttException e) {
            System.err.println("Could not Subsribe");
        }
    }

        /*try {
            Thread.sleep(1500);
        }catch(Exception e){
            System.out.println(e);
        };
        int dataLength = 65535;
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(4711);
        }catch(SocketException e){
            e.printStackTrace();
        }
        DatagramPacket inPacket;

        System.out.print("UDP Echo Server started...");
        while(true){try {
            Thread.sleep(500);
        }catch(Exception e){
            System.out.println(e);
        };
            byte[] data = new byte[dataLength];
            inPacket = new DatagramPacket(data, data.length);
            try{
                socket.receive(inPacket);
            }catch (IOException e){
                e.printStackTrace();
            }

                final String line = new String(inPacket.getData());
                int counter = 0;
                char [] tmp = new char[line.length()];
                for(int i = 0; i < line.length(); i++){
                   tmp[i] = line.charAt(i);
                }
                for(int i = 0; i < line.length(); i++){
                    if(tmp[i] != 0){
                        counter++;
                    }else{
                        break;
                    }
                }
                char array[] = new char[counter];
                for(int i = 0; i < counter; i++){
                    array[i] = line.charAt(i);
                }
                String message = new String(array);

                historie.add(message);
                SensorData m = gson.fromJson(line.trim(), SensorData.class);




        }
    }*/
    public String getStatAnd(){
        return "Windkraftanlage1 0";
    }
    public String getLeistungsAnd(){
        return "Solaranlage1 5.75";
    }
}
