package comlib.message;


public class ActionID
{
	public static final int commonRest = 0;
	public static final int commonMove = 1;

	public static final int ambulanceRescue = 2;
	public static final int ambulanceLoad = 3;

	public static final int fireExtinguish = 2;
	public static final int fireRefill = 3;

	public static final int policeClear = 2;

	private int id;

	ActionID(int value)
	{
		this.id = value;
	}

	public int get()
	{ return this.id; }

	public void set(int value)
	{
		this.id = value;
	}

	public boolean isRest()
	{ return ( id == commonRest ); }

	public boolean isMove()
	{ return ( id == commonMove ); }

	public boolean isAmbulanceRescue()
	{ return ( id == ambulanceRescue ); }

	public boolean isAmbulanceLoad()
	{ return ( id == ambulanceLoad ); }

	public boolean isFireExtinguish()
	{ return ( id == fireExtinguish ); }

	public boolean isFireRefill()
	{ return ( id == fireRefill ); }

	public boolean isPoliceClear()
	{ return ( id == policeClear ); }
}
