import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Sensor extends Thread{

    protected static int port = 4711;
    protected static String ip = "localhost";
    protected static String name;
    protected static String type;
    protected double value;
    protected  Random rand;
    protected double kapazität;
    protected String kap;
    protected int defect;
    protected int status = 1;
    protected double leistung = 1;

}