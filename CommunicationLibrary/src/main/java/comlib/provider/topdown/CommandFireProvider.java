package comlib.provider.topdown;

import comlib.event.MessageEvent;
import comlib.provider.CommandMessageProvider;

import comlib.event.topdown.CommandFireEvent;
import comlib.message.topdown.CommandFire;
import comlib.message.MessageCommand;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class CommandFireProvider extends CommandMessageProvider<CommandFire, CommandFireEvent>
{
	public CommandFireProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CommandFire msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getAction(), 2);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CommandFire msg)
	{
		// config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CommandFire createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		// MessageCommand parent = super.createMessage(config, time, bsr);
		return new CommandFire(time, -1,
				bsr.getBits(32),
				bsr.getBits(32),
				bsr.getBits(2)
				);
	}

	protected CommandFire createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CommandFire(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

//	@Override
//	public void trySetEvent(CommandFireEvent ev) {
//		if (ev instanceof CommandFireEvent) { this.event = ev; }
//
//	}

	@Override
	public Class<? extends MessageEvent> getEventClass() {
		return CommandFireEvent.class;
	}
}
