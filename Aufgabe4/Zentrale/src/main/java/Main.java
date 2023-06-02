import java.net.UnknownHostException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class Main {

    public static externerClientHandler handler;
    public static ExternerClientService.Processor processor;
    public static Subscriber subscriber;

    public static void main(String args[]) {
        Subscriber udp = new Subscriber();
        TCPHandler tcp = null;

        try {
            tcp = new TCPHandler();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        subscriber = new Subscriber();

        subscriber.start();

        tcp.start();
        try{
            Thread.sleep(5000);
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            handler = new externerClientHandler();
            processor = new ExternerClientService.Processor<externerClientHandler>(handler);

            Thread server = new Thread() {
                @Override
                public void run() {
                    server(processor);
                }
            };
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public static void server(ExternerClientService.Processor<externerClientHandler> processor){
                try{
                    TServerTransport serverTransport = new TServerSocket(9090);
                    TServer tServer = new TSimpleServer(new Args(serverTransport).processor(processor));

                    System.out.println("Zentrale: Starting RPC-SERVER....");
                    tServer.serve();
                }catch(Exception e){
                    e.printStackTrace();
                }
        }


    }
