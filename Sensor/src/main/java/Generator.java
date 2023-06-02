import java.util.Random;

public class Generator{

    String type;
    String name;
    double value;
    Random rand;

    public Generator(String t, String n){
        this.rand = new Random();
        this.type = t;
        this.name = n;
    }
    public double generate(){
        if(name.contains("Wind")){
            name = "Wind";
        }else if(name.contains("Sonnen")){
            name = "Sonnen";
        }else if(name.contains("Haus")){
            name = "Haus";
        }
        switch(this.name){
            case "Wind":
                value = rand.nextInt(250);
                break;
            case "Sonnen":
                value = rand.nextInt(175);
                break;
            case "Haus":
                value = rand.nextInt(50);
            default:
                break;
        }
        return value;
    }

}
