package comlib.util;


public class BooleanHelper
{
	public static int toInt(boolean b)
	{
		return ( b ? 1 : 0);
	}

	public static boolean valueOf(int i)
	{
		return ( i != 0 );
	}
}
