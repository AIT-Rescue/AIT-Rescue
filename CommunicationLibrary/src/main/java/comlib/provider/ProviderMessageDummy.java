package comlib.provider;

import comlib.event.EventMessageDummy;
import comlib.message.MessageDummy;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class ProviderMessageDummy extends MessageProvider<MessageDummy, EventMessageDummy>
{
	public ProviderMessageDummy(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessageDummy msg)
	{
		bos.writeBits(msg.getValue(), config.getSizeOfDummyValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessageDummy msg)
	{
		config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessageDummy createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessageDummy(time, -1,
				bsr.getBits(config.getSizeOfDummyValue())
				);
	}

	protected MessageDummy createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new MessageDummy(
				time, ttl,
				Integer.parseInt(data[next])
				);
	}

}
