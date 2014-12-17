package comlib.provider.topdown;

import comlib.provider.MessageProvider;
import comlib.event.topdown.EventCommandAmbulance;
import comlib.message.topdown.CommandAmbulance;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class ProviderCommandAmbulance extends MessageProvider<CommandAmbulance, EventCommandAmbulance>
{
	public ProviderCommandAmbulance(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandAmbulance msg)
	{
		bos.writeBits(msg.getValue(), config.getSizeOfDummyValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandAmbulance msg)
	{
		config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandAmbulance createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CommandAmbulance(time, -1,
				bsr.getBits(config.getSizeOfDummyValue())
				);
	}

	protected CommandAmbulance createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new CommandAmbulance(
				time, ttl,
				Integer.parseInt(data[next])
				);
	}

}
