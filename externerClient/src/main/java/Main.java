public class Main {

    public static void main(String[] args){
         externerClient extClient = new externerClient();
        try{
            Thread.sleep(5000);
        }catch(Exception e){
            e.printStackTrace();
        }
         while(true) {
            try{
                Thread.sleep(5000);
            }catch(Exception e){
               e.printStackTrace();
            }
             extClient.getStatus();
         }
    }
}
