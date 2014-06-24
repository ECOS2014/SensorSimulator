package uniandes.sensorsimulator.world;

import java.io.OutputStream;
import java.net.Socket;

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
		sensorCount++;
		try 
		{
			//Estructura de trama FrameSensor |ID.sensor Byte[1]||Status,TypeSensor Byte[0]|
			//String message = "{count:" + SingletonSignalCounter.getInstance().incrementCounter() + ",sensorNotification:" + this.toString() + "}";
			//System.out.println(message);
	    	
	    	Status=(int)(Math.random()*2);
	    	
	    	FrameSensor[1] = (byte)sensorId;
	    	FrameSensor[0] = (byte)((Status*2)+(sensorType));
	    	
	    	System.out.println("byte "+FrameSensor[1]+FrameSensor[0]+" S: "+Status+" C:" + SingletonSignalCounter.getInstance().incrementCounter());
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