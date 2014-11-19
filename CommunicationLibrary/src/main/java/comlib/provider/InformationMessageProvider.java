package comlib.provider;

import comlib.event.MessageEvent;
import comlib.message.CommunicationMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public abstract class InformationMessageProvider<M extends CommunicationMessage, E extends MessageEvent> extends MessageProvider<M, E>
{
	public InformationMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, M msg)
	{
		//bos.writeBits(msg.getValue(), config.getSizeOfCivilianValue());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, M msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected M createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return null;
		// return new CivilianMessage(time, -1,
		// 		bsr.getBits(config.getSizeOfCivilianValue())
		// 		);
	}

	protected M createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CivilianMessage(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
