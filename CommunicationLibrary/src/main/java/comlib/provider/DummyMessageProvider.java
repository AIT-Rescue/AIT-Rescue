package comlib.provider;

import comlib.event.DummyMessageEvent;
import comlib.message.DummyMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class DummyMessageProvider extends MessageProvider<DummyMessage, DummyMessageEvent>
{
	public DummyMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, DummyMessage msg)
	{
		bos.writeBits(msg.getValue(), config.getSizeOfDummyValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, DummyMessage msg)
	{
		config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected DummyMessage createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new DummyMessage(time, -1,
				bsr.getBits(config.getSizeOfDummyValue())
				);
	}

	protected DummyMessage createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new DummyMessage(
				time, ttl,
				Integer.parseInt(data[next])
				);
	}

}
