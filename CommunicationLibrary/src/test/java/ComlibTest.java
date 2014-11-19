import comlib.message.DummyMessage;

import comlib.manager.MessageManager;

import rescuecore2.config.Config;
import junit.framework.TestCase;


public class ComlibTest extends TestCase
{
	MessageManager manager;
	Config config;

	public ComlibTest(String name)
	{
		super(name);
	}

	protected void setUp()
	{
		config = new Config();
		try
		{
			config.read("sample.cfg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		manager = new MessageManager(config);
	}

	public void testDummyMessage()
	{
		final int testValue = 12;
		//manager.addSendMessage(new DummyMessage(testValue));
		assertEquals(1, 1);
	}
}
