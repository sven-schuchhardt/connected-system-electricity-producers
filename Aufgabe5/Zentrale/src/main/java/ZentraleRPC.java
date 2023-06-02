import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.*;

public class ZentraleRPC extends Thread {

    public static externerClientHandler handler;
    public static ExternerClientService.Processor processor;

    public ZentraleRPC() {
    }

    public void run() {
        boolean ausfallBool = true;
        int durchgange = 0;
        if (System.getenv("TYP").equals("master")) {
            while (ausfallBool) {
                durchgange++;
                int ausfallWahrscheinlichkeit = 5;
                Random random = new Random();
                int ausfall = random.nextInt(10);

                if (ausfallWahrscheinlichkeit <= ausfall) {
                    ausfallBool = false;
                    System.out.println("Ausfall Zentrale: " + System.getenv("NAME"));
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                // while(ausfallBool) {
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
                    if (!ausfallBool) {
                        ausfallBool = true;
                        server.stop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("MASTER erfolgreich");
                //}
            }
        } else if (System.getenv("TYP").equals("slave")) {
            int stat = 3;
            int min = 0;
            int max = 2;
            while (true) {
                String ip = System.getenv("TYP_IP");
                int port = Integer.parseInt(System.getenv("ZENTRALEN_PORT1"));
                try {
                    TTransport transport = new TSocket(ip, port);
                    transport.open();

                    TProtocol protocol = new TBinaryProtocol(transport);
                    ExternerClientService.Client client = new ExternerClientService.Client(protocol);
                    String status = client.statusAbfragen();

                    System.out.println("RPC: ZENTRALE--IP: " + ip + ":" + port);
                    System.out.println("RPC: ZENTRALE: Status: " + status);


                    List<String> historie;

                    //historie = client.historieAbfragen(stat);
                    historie = client.historieBekommen(min, max);
                    min = min + 3;
                    max = max + 3;
                    Main.subscriber.setHistorie(historie);
                    List<String> historieAddM;

                    historieAddM = Main.subscriber.getHistorie(min, max);
                    client.historieSetzen(historieAddM);
                    //historie = Main.subscriber.getHistorie();
                    /*for (int i = 0; i < historieAddM.size(); i++) {
                        System.out.println("Historie: <<" + historieAddM.get(i));
                    }*/
                    //System.out.println(" ----- Historie Ende ----- ");
                    stat = stat + 3;
                    transport.close();
                    try {
                        Thread.sleep(3000);
                    } catch (Exception w) {

                    }


                /*for (int i = 0; i < historie.size(); i++) {
                    System.out.println("Historie: <<" + historie.get(i));
                }
                System.out.println(" ----- Historie Ende ----- ");*/
                } catch (TTransportException e) {
                    //e.printStackTrace();
                } catch (TException t) {
                    //t.printStackTrace();
                }

            }
        } else {
            System.out.println("Zentralen-TYP nicht gefunden");
        }

    }


    public static void server(ExternerClientService.Processor<externerClientHandler> processor) {
        int port = 0;
        String typ = System.getenv("TYP");
        if (typ.contains("master")) {
            port = Integer.parseInt(System.getenv("ZENTRALEN_PORT1"));
        } else if (typ.contains("slave")) {
            port = Integer.parseInt(System.getenv("ZENTRALEN_PORT2"));
        } else {
            System.out.println("ZentralenTYP/PORT nicht gefunden");
        }
        try {
            TServerTransport serverTransport = new TServerSocket(port);
            TServer tServer = new TSimpleServer(new Args(serverTransport).processor(processor));

            System.out.println("Zentrale: Starting RPC-SERVER " + System.getenv("NAME") + " , " + System.getenv("TYP") + ", Port: " + port);
            tServer.serve();
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }


}
