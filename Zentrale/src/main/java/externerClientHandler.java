import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

public class externerClientHandler implements ExternerClientService.Iface{

    @Override
    public List<String>historieAbfragen(int s){
        List<String> result = Main.udpHandler.getHistorie();
        List<String> sendToClient;
        sendToClient = new ArrayList<String>();
        String temp;
        for(int i = 0; i < s; i++){
            temp = result.get(i);
            sendToClient.add(temp);
        }
        return sendToClient;
    }

    @Override
    public String statusAbfragen(){
        return "Status Erfolgreich";
    }


    @Override
    public String getStatusAnderung(){
        return Main.udpHandler.getStatAnd();
    }
    @Override
    public String getLeistungsAnderung(){
        return Main.udpHandler.getLeistungsAnd();
    }
}
