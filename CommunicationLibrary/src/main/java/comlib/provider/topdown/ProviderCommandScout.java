package comlib.provider.topdown;

import comlib.provider.MessageProvider;
import comlib.event.topdown.EventCommandScout;
import comlib.message.topdown.CommandScout;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class ProviderCommandScout extends MessageProvider<CommandScout, EventCommandScout>
{
	public ProviderCommandScout(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandScout msg)
	{
		bos.writeBits(msg.getValue(), config.getSizeOfDummyValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandScout msg)
	{
		config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandScout createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CommandScout(time, -1,
				bsr.getBits(config.getSizeOfDummyValue())
				);
	}

	protected CommandScout createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new CommandScout(
				time, ttl,
				Integer.parseInt(data[next])
				);
	}

}
