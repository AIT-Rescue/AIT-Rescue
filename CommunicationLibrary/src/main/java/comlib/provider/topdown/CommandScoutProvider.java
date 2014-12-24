package comlib.provider.topdown;

import comlib.provider.CommandMessageProvider;

import comlib.event.topdown.CommandScoutEvent;
import comlib.message.topdown.CommandScout;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class CommandScoutProvider extends CommandMessageProvider<CommandScout, CommandScoutEvent>
{
	public CommandScoutProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandScout msg)
	{
		super.writeMessage(config, bos, msg);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandScout msg)
	{
		// config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandScout createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CommandScout(time, -1,
				bsr.getBits(2),
				bsr.getBits(32),
				bsr.getBits(32)
				);
	}

	protected CommandScout createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CommandScout(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
