package comlib.provider;


import comlib.event.MessageEvent;
import comlib.message.CommunicationMessage;
import comlib.manager.MessageManager;
import comlib.manager.RadioConfig;
import comlib.manager.VoiceConfig;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;
import rescuecore2.worldmodel.EntityID;


public abstract class MessageProvider<M extends CommunicationMessage, E extends MessageEvent>
{
	protected int messageID;

	protected E event;

	protected EntityID fromID = null;


	public MessageProvider(int id)
	{
        this.messageID = id;
	}

	public int getMessageID()
	{
		return this.messageID;
	}


	protected abstract void writeMessage(RadioConfig config, BitOutputStream bos, M msg);

	protected abstract void writeMessage(VoiceConfig config, StringBuilder sb, M msg);

	protected abstract M createMessage(RadioConfig config, int time, BitStreamReader bsr);

	protected abstract M createMessage(VoiceConfig config, int time, int ttl, String[] datas, int next);
	
	public void write(MessageManager manager, BitOutputStream bos, M msg)
	{
		RadioConfig config = manager.getRadioConfig();
		if (bos.size() <= 0)
		{ bos.writeBits(this.messageID, config.getSizeOfMessageID()); }
		this.writeMessage(config, bos, msg);
	}
	
	public void write(MessageManager manager, StringBuilder sb, M msg)
	{
		if(msg.getTTL() == 0)
		{ return; }
	
		VoiceConfig config = manager.getVoiceConfig();
		config.appendMessageID(sb, this.messageID);
		config.appendData(sb, String.valueOf(manager.getTime()));

		if(msg.getTTL() < 0)
		{ config.appendLimit(sb); }
		else
		{ config.appendData(sb, String.valueOf(msg.getTTL() - 1)); }

		this.writeMessage(config, sb, msg);
		config.appendMessageSeparator(sb);
	}

	public M create(MessageManager manager, BitStreamReader bsr, EntityID fromID)
	{
		RadioConfig config = manager.getRadioConfig();
		M msg = null;

		this.fromID = fromID;

		try
		{
			msg = this.createMessage(config, manager.getTime() -1, bsr);
		} catch (Exception e)
		{ return null; }

		if (this.event != null)
		{
//			System.out.println("[INFO] MP : " + this.messageID + " : Radio event called : " + this.event.getClass());
			this.event.receivedRadio(msg);
		}

		return msg;
	}

	public M create(MessageManager manager, String[] data, EntityID fromID)
	{
		int next = 0;
		M msg = null;

		this.fromID = fromID;

		try
		{
			msg = this.createMessage(
					manager.getVoiceConfig(),
					Integer.parseInt(data[next++]),
					Integer.parseInt(data[next++]),
					data, next);
		} catch (Exception e)
		{ return null; }

		if (this.event != null)
		{
//			System.out.println("[INFO] MP: Voice Event called : " + this.event.getClass());
			this.event.receivedVoice(msg);
		}

		return msg;
	}

	public EntityID getFromID()
	{
		return this.fromID;
	}

	//public abstract void trySetEvent(MessageEvent ev);
//	{
		// TODO: check!!
		//if (ev instanceof E) //こうかけないからクソ

//		if (ev != null)
//		{ this.event = (E) ev; }
//	}
	public abstract Class<? extends MessageEvent> getEventClass();

	public void trySetEvent(MessageEvent event) {
		if(event != null && this.getEventClass().isInstance(event)) {
			this.event = (E) event;
		}
	}
}
