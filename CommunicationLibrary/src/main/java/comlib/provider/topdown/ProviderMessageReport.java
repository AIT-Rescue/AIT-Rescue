package comlib.provider.topdown;

import comlib.provider.MessageProvider;
import comlib.event.topdown.EventMessageReport;
import comlib.message.topdown.MessageReport;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class ProviderMessageReport extends MessageProvider<MessageReport, EventMessageReport>
{
	public ProviderMessageReport(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessageReport msg)
	{
		bos.writeBits(msg.getValue(), config.getSizeOfDummyValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessageReport msg)
	{
		config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessageReport createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessageReport(time, -1,
				bsr.getBits(config.getSizeOfDummyValue())
				);
	}

	protected MessageReport createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new MessageReport(
				time, ttl,
				Integer.parseInt(data[next])
				);
	}

}
