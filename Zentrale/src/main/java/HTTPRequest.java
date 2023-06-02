import java.io.BufferedReader;
import java.io.IOException;

public class HTTPRequest {

    public String path;
    public String method;

    public String protocolversion;
    public String cache;
    public String connection;
    public String useragent;
    public String accept;

    public HTTPRequest(BufferedReader httpIn) throws IOException{
        String line = httpIn.readLine();
        boolean firstrow = true;
        while(line != null && !line.equals("")){

            if(line.contains("\r\n\r\n")) {
                return;
            }

            if(firstrow){
                firstrow = false;

                this.method = line.split(" ")[0];
                this.protocolversion = line.split(" ")[2];

                this.path = line.split(" ")[1];
            }else{
                int seperatorIndex = line.indexOf(":");
                if(seperatorIndex != -1){
                    String key = line.substring(0, seperatorIndex);
                    String value = line.substring(seperatorIndex + 1);

                    switch(key){
                        case "Connection":
                            this.connection = value;
                            break;
                        case "User-Agent:":
                            this.useragent = value;
                            break;
                        case "Accept":
                            this.accept = value;
                            break;
                        case "Cache-Control":
                            this.cache = value;
                            break;
                    }
                }
            }
            line = httpIn.readLine();
        }
    }
}
