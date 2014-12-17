package comlib.provider.topdown;

import comlib.provider.MessageProvider;
import comlib.event.topdown.EventCommandFire;
import comlib.message.topdown.CommandFire;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class ProviderCommandFire extends MessageProvider<CommandFire, EventCommandFire>
{
	public ProviderCommandFire(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandFire msg)
	{
		bos.writeBits(msg.getValue(), config.getSizeOfDummyValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandFire msg)
	{
		config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandFire createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CommandFire(time, -1,
				bsr.getBits(config.getSizeOfDummyValue())
				);
	}

	protected CommandFire createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new CommandFire(
				time, ttl,
				Integer.parseInt(data[next])
				);
	}

}
