package comlib.provider;

import comlib.event.MessageEvent;
import comlib.message.MessageHuman;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public abstract class HumanMessageProvider<M extends MessageHuman, E extends MessageEvent> extends InformationMessageProvider<M, E>
{
	public HumanMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, M msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHP(), config.getSizeOfHumanHP());
		bos.writeBits(msg.getBuriedness(), config.getSizeOfHumanBuriedness());
		bos.writeBits(msg.getDamage(), config.getSizeOfHumanDamage());
		bos.writeBits(msg.getPosition().getValue(), config.getSizeOfHumanPosition());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, M msg)
	{
		super.writeMessage(config, sb, msg);
		//config.appendData(sb, String.valueOf(msg.getValue()));
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
