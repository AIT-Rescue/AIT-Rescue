package comlib.provider.information;

import comlib.provider.HumanMessageProvider;

import comlib.event.information.EventMessageCivilian;
import comlib.message.information.MessageCivilian;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class ProviderMessageCivilian extends HumanMessageProvider<MessageCivilian, EventMessageCivilian>
{
	public ProviderMessageCivilian(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, MessageCivilian msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getHumanID().getValue(), config.getSizeOfCivilianID());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, MessageCivilian msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected MessageCivilian createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new MessageCivilian(time, -1,
				bsr.getBits(config.getSizeOfHumanHP()),
				bsr.getBits(config.getSizeOfHumanBuriedness()),
				bsr.getBits(config.getSizeOfHumanDamage()),
				bsr.getBits(config.getSizeOfHumanPosition()),
				bsr.getBits(config.getSizeOfCivilianID())
				);
	}

	protected MessageCivilian createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new MessageCivilian(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
