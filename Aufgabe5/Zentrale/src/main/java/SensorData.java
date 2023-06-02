public class SensorData {
    private String type;
    private String name;
    private double value;
    private String kapazität;

    public SensorData(String type, String name, String value, String kapazität) {
        this.type = type;
        this.name = name;
        this.value = Double.parseDouble(value);
        this.kapazität = kapazität;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getKapazität(){return kapazität;}
}
