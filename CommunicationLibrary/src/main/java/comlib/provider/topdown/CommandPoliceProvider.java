package comlib.provider.topdown;

import comlib.provider.CommandMessageProvider;

import comlib.event.topdown.CommandPoliceEvent;
import comlib.message.topdown.CommandPolice;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class CommandPoliceProvider extends CommandMessageProvider<CommandPolice, CommandPoliceEvent>
{
	public CommandPoliceProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandPolice msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getAction(), 2);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandPolice msg)
	{
		// config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandPolice createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CommandPolice(time, -1,
				bsr.getBits(32),
				bsr.getBits(32),
				bsr.getBits(2)
				);
	}

	protected CommandPolice createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CommandPolice(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
