package comlib.provider.information;

import comlib.event.MessageEvent;
import comlib.provider.HumanMessageProvider;

import comlib.event.information.MessageFireBrigadeEvent;
import comlib.message.information.MessageFireBrigade;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class MessageFireBrigadeProvider extends HumanMessageProvider<MessageFireBrigade, MessageFireBrigadeEvent>
{
	public MessageFireBrigadeProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessageFireBrigade msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfFireBrigadeID());
		bos.writeBits(msg.getWater(), config.getSizeOfFireBrigadeWater());
		bos.writeBits(msg.getTargetID().getValue(), 32);
		bos.writeBits(msg.getAction(), 2);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessageFireBrigade msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessageFireBrigade createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessageFireBrigade(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfFireBrigadeID()),
				bsr.getBits(config.getSizeOfFireBrigadeWater()),
				bsr.getBits(32),
				bsr.getBits(2)
				);
	}

	protected MessageFireBrigade createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new MessageFireBrigade(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

//	@Override
//	public void trySetEvent(MessageFireBrigadeEvent ev) {
//		if (ev instanceof MessageFireBrigadeEvent) { this.event = ev; }
//
//	}

	@Override
	public Class<? extends MessageEvent> getEventClass() {
		return MessageFireBrigadeEvent.class;
	}
}
