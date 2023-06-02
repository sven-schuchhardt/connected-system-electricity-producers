import java.util.ArrayList;

public class DataStorage
{
    // static variable single_instance of type Singleton
    private static DataStorage single_instance = null;


    public ArrayList<Sensor> sensors;
    public ArrayList<SensorData> data;
    private DataStorage()
    {
        sensors = new ArrayList<>();
        data = new ArrayList<>();
    }



    public static DataStorage getInstance()
    {
        if (single_instance == null)
            single_instance = new DataStorage();

        return single_instance;
    }

    public void addData(SensorData measurement) {
        data.add(measurement);
    }
}

