package comlib.provider.information;

import comlib.event.MessageEvent;
import comlib.event.information.MessagePoliceForceEvent;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.message.information.MessagePoliceForce;
import comlib.provider.HumanMessageProvider;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;


public class MessagePoliceForceProvider extends HumanMessageProvider<MessagePoliceForce, MessagePoliceForceEvent>
{
	public MessagePoliceForceProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessagePoliceForce msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfPoliceForceID());
		bos.writeBits(msg.getTargetID().getValue(), 32);
		bos.writeBits(msg.getAction(), 2);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessagePoliceForce msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessagePoliceForce createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessagePoliceForce(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfPoliceForceID()),
				bsr.getBits(32),
				bsr.getBits(2)
				);
	}

	protected MessagePoliceForce createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new MessagePoliceForce(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

//	@Override
//	public void trySetEvent(MessagePoliceForceEvent ev) {
//		if (ev instanceof MessagePoliceForceEvent) { this.event = ev; }
//
//	}

	@Override
	public Class<? extends MessageEvent> getEventClass() {
		return MessagePoliceForceEvent.class;
	}
}
