package comlib.provider.information;

import comlib.event.information.RoadMessageEvent;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.message.information.RoadMessage;
import comlib.provider.MapMessageProvider;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;

public class RoadMessageProvider extends MapMessageProvider<RoadMessage, RoadMessageEvent>
{
	public RoadMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, RoadMessage msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getRoadID().getValue(), 32);
		bos.writeBits(msg.getBlockadeID().getValue(), 32);
		bos.writeBits(msg.getRepairCost(), 32);
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, RoadMessage msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected RoadMessage createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new RoadMessage(time, -1,
				bsr.getBits(32),
				bsr.getBits(32),
				bsr.getBits(32)
				);
	}

	protected RoadMessage createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CivilianMessage(time, ttl, Integer.parseInt(data[next]));
	}
}

