package uniandes.sensorsimulator.world;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class SensorSenderThread implements Runnable 
{
	private String propertyIP;
	private int propertyPort;
	private int sensorId;
	private int sensorType;
	private int sensorSleepTime;
	private int sensorCount;
	private byte[] FrameSensor = new byte[2];
	private int Status;
	private Socket socket;
	private OutputStream outputStream;
	
	private int maxDeliveries;
	
	public SensorSenderThread(int sensorId, int sensorType, int sensorSleepTime, int maxDeliveries, String houseIP, int housePort)
	{
		this.sensorId = sensorId;
		this.sensorType = sensorType;
		this.sensorSleepTime = sensorSleepTime;
		this.maxDeliveries = maxDeliveries;
		this.propertyIP = houseIP;
		this.propertyPort = housePort;
		this.sensorCount = 0;
		System.out.println(this);
	}
	
	@Override
	public void run() 
	{
		while (isEnableToRun())
		{
			sendSignal();
			
			try
			{
				Thread.sleep(sensorSleepTime);
			}
			catch (Exception e){}
		}
	}
	
	private boolean isEnableToRun() 
	{
		boolean enableToRun = false;
		
		if (maxDeliveries == -1)
		{
			enableToRun = true;
		}
		else
		{
			enableToRun = sensorCount<maxDeliveries;
		}
		
		return enableToRun;
	}

	public void sendSignal()
	{
		int numberThreads;
		sensorCount++;
		try 
		{
			//Estructura de trama FrameSensor |ID.sensor Byte[1]||Status,TypeSensor Byte[0]|
			//String message = "{count:" + SingletonSignalCounter.getInstance().incrementCounter() + ",sensorNotification:" + this.toString() + "}";
			//System.out.println(message);
	    	Random random = new Random();
	    	Status=(int)(random.nextInt(2));
	    	
	    	FrameSensor[1] = (byte)sensorId;
	    	FrameSensor[0] = (byte)((Status*2)+(sensorType));
	    	numberThreads = SingletonSignalCounter.getInstance().incrementCounter();
	    	//System.out.println("Total de hilos generados: "+numberThreads + " Numero de hilos en la casa: "+numberThreads );
			socket = new Socket(propertyIP, propertyPort);
			outputStream = socket.getOutputStream();
			outputStream.write(FrameSensor);
			outputStream.close();
			socket.close();
		} 
		catch (Exception e) 
		{
			System.out.println("Sensor " + sensorId + " on try " + sensorCount + " Couldn\'t find server at " + propertyIP + ":" + propertyPort);
		}		
	}
	
	@Override
	public String toString()
	{
		return "{sensorId:" + sensorId + ",sensorType:" + sensorType + ",sensorCount:" + sensorCount + ",sensorSleepTime:" + sensorSleepTime + "}";
	}

}
