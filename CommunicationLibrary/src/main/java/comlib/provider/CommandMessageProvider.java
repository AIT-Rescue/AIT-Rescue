package comlib.provider;

import comlib.message.MessageCommand;

import comlib.event.MessageEvent;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;

import javax.swing.text.html.parser.Entity;


public abstract class CommandMessageProvider<M extends MessageCommand, E extends MessageEvent> extends MessageProvider<M, E>
{
	public CommandMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, M msg)
	{
		bos.writeBits(msg.getTargetID().getValue(), 32);
		bos.writeBits(msg.getToID().getValue(), 32);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, M msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected M createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return null;
		// return new MessageCommand(time, -1,
		// 		bsr.getBits(32),
		// 		bsr.getBits(32),
		// 		bsr.getBits(2)
		// 		);
	}

	protected M createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
	}
}
