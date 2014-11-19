package comlib.provider.information;

import comlib.provider.HumanMessageProvider;

import comlib.event.information.FireBrigadeMessageEvent;
import comlib.message.information.FireBrigadeMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class FireBrigadeMessageProvider extends HumanMessageProvider<FireBrigadeMessage, FireBrigadeMessageEvent>
{
	public FireBrigadeMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, FireBrigadeMessage msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfFireBrigadeID());
		bos.writeBits(msg.getWater(), config.getSizeOfFireBrigadeWater());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, FireBrigadeMessage msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected FireBrigadeMessage createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new FireBrigadeMessage(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfFireBrigadeID()),
				bsr.getBits(config.getSizeOfFireBrigadeWater())
				);
	}

	protected FireBrigadeMessage createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new FireBrigadeMessage(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
