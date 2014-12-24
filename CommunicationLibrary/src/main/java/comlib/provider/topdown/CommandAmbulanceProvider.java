package comlib.provider.topdown;

import comlib.provider.CommandMessageProvider;

import comlib.event.topdown.CommandAmbulanceEvent;
import comlib.message.topdown.CommandAmbulance;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class CommandAmbulanceProvider extends CommandMessageProvider<CommandAmbulance, CommandAmbulanceEvent>
{
	public CommandAmbulanceProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandAmbulance msg)
	{
		super.writeMessage(config, bos, msg);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandAmbulance msg)
	{
		// config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandAmbulance createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CommandAmbulance(time, -1,
				bsr.getBits(2),
				bsr.getBits(32),
				bsr.getBits(32)
				);
	}

	protected CommandAmbulance createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CommandAmbulance(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
