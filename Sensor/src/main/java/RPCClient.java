import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;

public class RPCClient extends Thread{

    public RPCClient() {

    }

    public void run() {
        System.out.println("RPC-Sensor started");
        while (true) {
            String dns = System.getenv("SERVER");
            if (dns == null) {
                dns = "localhost";
            }
            try {
                TTransport transport = new TSocket(dns, 9090);
                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                ExternerClientService.Client client = new ExternerClientService.Client(protocol);
                String temp = client.getLeistungsAnderung();
                String []split;
                split = temp.split(" ");
                if(client.getLeistungsAnderung().contains("Windkraftanlage1")){
                    Main.w1.setLeistungsAnderung(Double.parseDouble(split[1]));
                }
                if(client.getLeistungsAnderung().contains("Windkraftanlage2")){
                    Main.w2.setLeistungsAnderung(Double.parseDouble(split[1]));

                }
                if(client.getLeistungsAnderung().contains("Solaranlage1")){
                    Main.s1.setLeistungsAnderung(Double.parseDouble(split[1]));
                }
                String tmp = client.getStatusAnderung();
                String []sp;
                sp = tmp.split(" ");
                if(client.getStatusAnderung().contains("Windkraftanlage1")){
                    Main.w1.setStatusAnderung(Integer.parseInt(sp[1]));
                }
                if(client.getStatusAnderung().contains("Windkraftanlage2")){
                    Main.w2.setStatusAnderung(Integer.parseInt(sp[1]));
                }
                if(client.getStatusAnderung().contains("Solaranlage1")){
                    Main.s1.setStatusAnderung(Integer.parseInt(sp[1]));
                }
                if(client.getStatusAnderung().contains("Haushalt1")){
                    Main.h1.setStatusAnderung(Integer.parseInt(sp[1]));
                }
                transport.close();

            }catch (TTransportException e) {
                e.printStackTrace();
            }catch (TException t) {
                t.printStackTrace();
            }

        }
    }
}
