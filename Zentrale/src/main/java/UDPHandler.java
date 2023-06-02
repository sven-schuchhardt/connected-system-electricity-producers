import com.google.gson.Gson;
import java.util.List;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Optional;
import java.util.ArrayList;
import java.util.*;

public class UDPHandler extends Thread{

    private Gson gson = new Gson();
    private String address = "localhost";
    private int port = 4711;
    private String name;
    private List<String>historie = new ArrayList<String>();

    UDPHandler(){
        historie.add("Historie vom externen Client: ");
    }

    public List<String> getHistorie(){
        return historie;
    }

    @Override
    public void run(){

        try {
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

                Optional<Sensor> s = DataStorage.getInstance().sensors.stream()
                        .filter(x -> x.getName().equals(m.getName())).findFirst();

                if (s.isPresent()) {
                    s.get().addSensorData(m);
                    s.get().addLineData(line);
                } else {
                    Sensor newSens = new Sensor(m.getName(), m.getType());
                    newSens.addSensorData(m);
                    newSens.addLineData(line);
                    DataStorage.getInstance().sensors.add(newSens);
                }

                System.out.println(String.format("Name: %s%nType: %s%nStrommenge: %s%nKapazität: %s%n", m.getName(), m.getType(), m.getValue(), m.getKapazität()));
                System.out.println();

        }
    }
    public String getStatAnd(){
        return "Windkraftanlage1 0";
    }
    public String getLeistungsAnd(){
        return "Solaranlage1 5.75";
    }
}
