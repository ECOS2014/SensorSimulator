package uniandes.sensorsimulator.world;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SensorSetSimulator 
{
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
		String houseIP = properties.getProperty(KEY_HOUSE_IP);
		
		String strHousePort = properties.getProperty(KEY_HOUSE_PORT);
		int housePort = Integer.parseInt(strHousePort);
		
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
			
			Thread sensorThread = new Thread(new SensorSenderThread(id, type, sleepTime, maxDeliveries, houseIP, housePort));
			sensorThread.setDaemon(false);
			sensorThread.start();
			System.out.println("Starting thread " + sensorThread.toString());
		}
	}

	public static void main(String[] args)
	{
		new SensorSetSimulator();
	}
}
