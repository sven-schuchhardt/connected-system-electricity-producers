import java.util.ArrayList;
import java.util.List;

public class externerClientHandler implements ExternerClientService.Iface{

    @Override
    public List<String>historieAbfragen(int s){
        List<String> result = Main.subscriber.getHist();
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
    public List<String>historieBekommen(int min, int max){
        List<String> result = Main.subscriber.getHistorie(min, max);
        return result;
    }

    @Override
    public String statusAbfragen(){
        return "Status Gesamtes System Erfolgreich";
    }
    @Override
    public void historieSetzen(List<String> a){
        Main.subscriber.setHistorie(a);
    }


    @Override
    public String getStatusAnderung(){
        return Main.subscriber.getStatAnd();
    }
    @Override
    public String getLeistungsAnderung(){
        return Main.subscriber.getLeistungsAnd();
    }
}
