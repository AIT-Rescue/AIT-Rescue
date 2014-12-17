package comlib.provider.topdown;

import comlib.provider.MessageProvider;
import comlib.event.topdown.EventCommandPolice;
import comlib.message.topdown.CommandPolice;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class ProviderCommandPolice extends MessageProvider<CommandPolice, EventCommandPolice>
{
	public ProviderCommandPolice(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandPolice msg)
	{
		bos.writeBits(msg.getValue(), config.getSizeOfDummyValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandPolice msg)
	{
		config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandPolice createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CommandPolice(time, -1,
				bsr.getBits(config.getSizeOfDummyValue())
				);
	}

	protected CommandPolice createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new CommandPolice(
				time, ttl,
				Integer.parseInt(data[next])
				);
	}

}
