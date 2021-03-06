package comlib.provider.topdown;

import comlib.event.MessageEvent;
import comlib.message.topdown.CommandAmbulance;
import comlib.provider.MessageProvider;
import comlib.event.topdown.MessageReportEvent;
import comlib.message.topdown.MessageReport;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import comlib.util.BooleanHelper;


public class MessageReportProvider extends MessageProvider<MessageReport, MessageReportEvent>
{
	public MessageReportProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessageReport msg)
	{
		bos.writeBits(BooleanHelper.toInt(msg.isDone()), 1);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessageReport msg)
	{
		// config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessageReport createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessageReport(time, -1,
				BooleanHelper.valueOf(bsr.getBits(1))
				);
	}

	protected MessageReport createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return new MessageReport(
				time, ttl,
				BooleanHelper.valueOf(Integer.parseInt(data[next]))
				);
	}

	@Override
	public Class<? extends MessageEvent> getEventClass() {
		return MessageReportEvent.class;
	}
}
