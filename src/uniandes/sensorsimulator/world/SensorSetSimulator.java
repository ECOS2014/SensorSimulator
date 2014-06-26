package uniandes.sensorsimulator.world;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;

public class SensorSetSimulator extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String CONFIG_FILE_PATH = "./data/config.properties";
	
	private static final String KEY_HOUSE_IP = "houseIP";
	private static final String KEY_HOUSE_PORT = "housePort";
	private static final String KEY_TOTAL_SENSORS = "totalSensors";
	private static final String KEY_SENSOR = "sensor";
	private static final String KEY_ID = "id";
	private static final String KEY_TYPE = "type";
	private static final String KEY_SLEEP_TIME = "sleepTime";
	private static final String KEY_MAX_DELIVERIES = "maxDeliveries";
	
	public SensorSetSimulator()
	{
		Properties properties = loadProperties();
		createSensorsThreads(properties);
	}

	public SensorSetSimulator(String propertyIP, int propertyPort) {
		Properties properties = loadProperties();
		createSensorsThreads(properties, propertyIP, propertyPort);
	}

	private Properties loadProperties() 
	{
		Properties properties = null;
		
		try 
		{
			FileInputStream inputStream = new FileInputStream(CONFIG_FILE_PATH);
			properties = new Properties();
			properties.load(inputStream);
			inputStream.close();			
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}		
		
		return properties;
	}
	
	private void createSensorsThreads(Properties properties) 
	{
		String propertyIP = properties.getProperty(KEY_HOUSE_IP);
		
		String strPropertyPort = properties.getProperty(KEY_HOUSE_PORT);
		int propertyPort = Integer.parseInt(strPropertyPort);
		
		createSensorsThreads(properties, propertyIP, propertyPort);
	}
	
	private void createSensorsThreads(Properties properties, String propertyIP,	int propertyPort) 
	{
		String strTotalSensors = properties.getProperty(KEY_TOTAL_SENSORS);
		int totalSensors = Integer.parseInt(strTotalSensors);
		
		for (int i = 1; i <= totalSensors; i++)
		{
			String keyId = KEY_SENSOR + i + "." + KEY_ID;
			int id = Integer.parseInt(properties.getProperty(keyId));
			
			String keyType = KEY_SENSOR + i + "." + KEY_TYPE;
			int type = Integer.parseInt(properties.getProperty(keyType));
			
			String keySleepTime = KEY_SENSOR + i + "." + KEY_SLEEP_TIME;
			int sleepTime = Integer.parseInt(properties.getProperty(keySleepTime));
			
			String keyMaxDeliveries = KEY_SENSOR + i + "." + KEY_MAX_DELIVERIES;
			int maxDeliveries = Integer.parseInt(properties.getProperty(keyMaxDeliveries));
			
			Thread sensorThread = new Thread(new SensorSenderThread(id, type, sleepTime, maxDeliveries, propertyIP, propertyPort));
			sensorThread.setDaemon(false);
			sensorThread.start();
			System.out.println("Starting thread " + sensorThread.toString());
		}
	}

	public static void main(String[] args)
	{
		SensorSetSimulator sss = null;
		
		
		if (args.length == 2)
		{
			String propertyIP = args[0];
			String strPropertyPort = args[1];
			int propertyPort = Integer.parseInt(strPropertyPort);
			sss = new SensorSetSimulator(propertyIP, propertyPort);
			sss.setTitle("Sensor from property at " + propertyIP + ":" + propertyPort);	
		}
		else
		{
			sss = new SensorSetSimulator();
			sss.setVisible(false);
			sss.setTitle("Sensor, no properties were found");
		}
		
		sss.setSize(500,100);
		sss.setVisible(true);
		sss.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
