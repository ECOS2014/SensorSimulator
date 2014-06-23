package uniandes.sensorsimulator.world;

public class SingletonSignalCounter 
{
	private int counter;
	
	private static SingletonSignalCounter instance;
	
	private SingletonSignalCounter()
	{
		counter = 0;
	}
	
	public int incrementCounter()
	{
		counter++;
		return counter;
	}
	
	public static SingletonSignalCounter getInstance()
	{
		if (instance == null)
		{
			instance = new SingletonSignalCounter();
		}		
		return instance;
	}
}
