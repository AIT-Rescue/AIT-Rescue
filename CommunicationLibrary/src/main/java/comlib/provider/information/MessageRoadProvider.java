package comlib.provider.information;

import comlib.event.MessageDummyEvent;
import comlib.event.MessageEvent;
import comlib.event.information.MessageRoadEvent;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.message.information.MessageRoad;
import comlib.provider.MapMessageProvider;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import comlib.util.BooleanHelper;

public class MessageRoadProvider extends MapMessageProvider<MessageRoad, MessageRoadEvent>
{
	public MessageRoadProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessageRoad msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getRoadID().getValue(), 32);
		bos.writeBits(msg.getBlockadeID().getValue(), 32);
		bos.writeBits(msg.getRepairCost(), 32);
		bos.writeBits(BooleanHelper.toInt(msg.isPassable()), 1);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessageRoad msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessageRoad createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessageRoad(time, -1,
				bsr.getBits(32),
				bsr.getBits(32),
				bsr.getBits(32),
				BooleanHelper.valueOf(bsr.getBits(1))
				);
	}

	protected MessageRoad createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new MessageCivilian(time, ttl, Integer.parseInt(data[next]));
	}

//	@Override
//	public void trySetEvent(MessageRoadEvent ev) {
//		if (ev instanceof MessageRoadEvent) { this.event = ev; }
//
//	}

	@Override
	public Class<? extends MessageEvent> getEventClass() {
		return MessageRoadEvent.class;
	}
}

