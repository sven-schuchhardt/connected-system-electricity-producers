import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

class externerClient {
    int stat = 3;

    public externerClient(){

    }

    public String getStatus() {
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
                String status = client.statusAbfragen();


                System.out.println("RPC: EXTERNER CLIENT: Status: " + status);
                List<String> historie;
                historie = client.historieAbfragen(stat);
                for (int i = 0; i < historie.size(); i++) {
                    System.out.println("Historie: <<" + historie.get(i));
                }
                System.out.println(" ----- Historie Ende ----- ");
                stat = stat + 3;

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
