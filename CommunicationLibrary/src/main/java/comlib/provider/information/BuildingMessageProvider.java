package comlib.provider.information;

import comlib.provider.MapMessageProvider;

import comlib.event.information.BuildingMessageEvent;
import comlib.message.information.BuildingMessage;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public class BuildingMessageProvider extends MapMessageProvider<BuildingMessage, BuildingMessageEvent>
{
	public BuildingMessageProvider(int id)
	{
		super(id);
	}

	protected void writeMessage(RadioConfig config, BitOutputStream bos, BuildingMessage msg)
	{
		super.writeMessage(config, bos, msg);
		bos.writeBits(msg.getBuildingID().getValue(), config.getSizeOfBuildingID());
		bos.writeBits(msg.getBrokenness(), config.getSizeOfBuildingBrokenness());
		bos.writeBits(msg.getFieryness(), config.getSizeOfBuildingFieryness());
	}

	protected void writeMessage(VoiceConfig config, StringBuilder sb, BuildingMessage msg)
	{
		//config.appendData(sb, String.valueOf(msg.getValue()));
	}

	protected BuildingMessage createMessage(RadioConfig config, int time, BitStreamReader bsr)
	{
		return new BuildingMessage(time, -1,
				bsr.getBits(config.getSizeOfBuildingID()),
				bsr.getBits(config.getSizeOfBuildingBrokenness()),
				bsr.getBits(config.getSizeOfBuildingFieryness())
				);
	}

	protected BuildingMessage createMessage(VoiceConfig config, int time, int ttl, String[] data, int next)
	{
		return null;
		// return new CivilianMessage(
		// 		time, ttl,
		// 		Integer.parseInt(data[next])
		// 		);
	}

}
