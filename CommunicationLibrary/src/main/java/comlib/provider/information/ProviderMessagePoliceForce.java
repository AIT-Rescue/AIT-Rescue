package comlib.provider.information;

import comlib.provider.HumanMessageProvider;

import comlib.event.information.EventMessagePoliceForce;
import comlib.message.information.MessagePoliceForce;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class ProviderMessagePoliceForce extends HumanMessageProvider<MessagePoliceForce, EventMessagePoliceForce>
{
	public ProviderMessagePoliceForce(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessagePoliceForce msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfPoliceForceID());
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
				bsr.getBits(config.getSizeOfPoliceForceID())
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

}