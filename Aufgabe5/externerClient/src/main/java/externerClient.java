import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;

import java.util.List;
import java.util.Random;

class externerClient {
    int stat = 3;
    int anzahlZentralen = 2;
    public externerClient(){

   }

    public String getStatus() {
        while (true) {
            try{
                Thread.sleep(5000);
            }catch(Exception e){

            }
            Random rand = new Random();
            int zentrale = 0;
            int port = 0;
            zentrale = rand.nextInt(3) ;
            String ip = "";
            if(zentrale == 1){
                ip = System.getenv("ZENTRALE1");
                port = Integer.parseInt(System.getenv("ZENTRALE1_PORT"));
            }else if(zentrale == 2){
                ip = System.getenv("ZENTRALE2");
                port = Integer.parseInt(System.getenv("ZENTRALE2_PORT"));
            }else{
                ip = System.getenv("ZENTRALE2");
                port = Integer.parseInt(System.getenv("ZENTRALE2_PORT"));
            }
            /*String dns = System.getenv("SERVER");
            if (dns == null) {
                dns = "localhost";
            }*/
            try {
                TTransport transport = new TSocket(ip, port);
                transport.open();

                TProtocol protocol = new TBinaryProtocol(transport);
                ExternerClientService.Client client = new ExternerClientService.Client(protocol);
                String status = client.statusAbfragen();

                System.out.println("RPC: EXTERNER CLIENT--IP: " + ip + ":" + port);
                System.out.println("RPC: Status: " + status);
                List<String> historie;

                historie = client.historieAbfragen(stat);
                //historie = client.historieBekommen();
               for (int i = 0; i < historie.size(); i++) {
                    System.out.println("Historie: <<" + historie.get(i));
                }
                //System.out.println(" ----- Historie Ende ----- ");
                stat = stat + 8;

                transport.close();

                return status;

            } catch (TTransportException e) {
                e.printStackTrace();
            } catch (TException t) {
                t.printStackTrace();
            }


            return "";
        }
    }
}