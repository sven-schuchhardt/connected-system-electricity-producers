import java.util.ArrayList;

public class Sensor {

    private String name;
    private String type;
    private ArrayList<SensorData> measurements;
    private ArrayList<String>lines;
    private String kapazität;

    public Sensor(String name, String type) {
        this.name = name;
        this.type = type;
        measurements = new ArrayList<>();
        lines = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void addSensorData(String type, String name, String value) {
        measurements.add(new SensorData(type, name, value, kapazität));
    }

    public void addSensorData(SensorData measurement) {
        measurements.add(measurement);
    }

    public void addLineData(String measurement) {
        lines.add(measurement);
    }

    public int measurementCount() {
        return measurements.size();
    }

    public ArrayList<SensorData> getMeasurements() {
        return measurements;
    }


}
