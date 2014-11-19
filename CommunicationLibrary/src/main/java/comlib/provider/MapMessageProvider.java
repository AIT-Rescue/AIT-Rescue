package comlib.provider;

import comlib.event.MessageEvent;
import comlib.message.MapMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public abstract class MapMessageProvider<M extends MapMessage, E extends MessageEvent> extends InformationMessageProvider<M, E>
{
	public MapMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, M msg)
	{
		super.writeMessage(config, bos, msg);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, M msg)
	{
		super.writeMessage(config, sb, msg);
	}

	protected M createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return null;
	}

	protected M createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
	}

}
