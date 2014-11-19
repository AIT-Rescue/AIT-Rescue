package comlib.provider.information;

import comlib.provider.HumanMessageProvider;

import comlib.event.information.CivilianMessageEvent;
import comlib.message.information.CivilianMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class CivilianMessageProvider extends HumanMessageProvider<CivilianMessage, CivilianMessageEvent>
{
	public CivilianMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, CivilianMessage msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfCivilianID());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, CivilianMessage msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected CivilianMessage createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new CivilianMessage(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfCivilianID())
				);
	}

	protected CivilianMessage createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CivilianMessage(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
