package comlib.manager;

import comlib.provider.MessageDummyProvider;
import comlib.provider.MessageProvider;
import comlib.event.MessageEvent;
import comlib.message.CommunicationMessage;
import comlib.message.MessageID;
import comlib.provider.information.*;
import comlib.provider.topdown.*;
import comlib.util.BitOutputStream;
import comlib.util.BitStreamReader;

import rescuecore2.Constants;
import rescuecore2.config.Config;
import rescuecore2.messages.Command;
import rescuecore2.messages.Message;
import rescuecore2.standard.kernel.comms.ChannelCommunicationModel;
import rescuecore2.standard.messages.AKSpeak;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class MessageManager
{
	private boolean developerMode;

	private RadioConfig radioConfig;
	private VoiceConfig voiceConfig;

	private boolean useRadio;
	private int numRadio;
	private int numVoice;

	private int kernelTime;

	private MessageProvider[] providerList;
	private List<MessageEvent> eventList;
//	private MessageEvent[] eventList;

	private List<CommunicationMessage> receivedMessages; // FOR-COMPATIBLE
	private List<CommunicationMessage> sendMessages;
	private BitOutputStream[] bitOutputStreamList;
	private int[] maxBandWidthList;

	private EntityID agentID;

	private boolean agentCalled = false;

	public MessageManager(Config config, EntityID agentID)
	{
		this.init(config);
		this.agentID = agentID;
	}

	private void init(Config config)
	{
		this.developerMode = config.getBooleanValue("comlib.develop.developerMode", false);
		this.radioConfig = new RadioConfig(config);
		this.voiceConfig = new VoiceConfig(config);
		this.kernelTime = -1;

		this.numRadio = config.getIntValue("comms.channels.max.platoon");
		this.numVoice = ((config.getValue("comms.channels.0.type").equals("voice")) ? 1 : 0);
		this.useRadio = ( this.numRadio >= 1 );

		this.providerList = new MessageProvider[config.getIntValue("comlib.default.messageID", 16)];
		this.bitOutputStreamList = new BitOutputStream[config.getIntValue("comlib.default.messageID", 16)];
		this.maxBandWidthList = new int[numRadio];
		this.eventList = new ArrayList<>();
		this.receivedMessages = new ArrayList<>();
		this.sendMessages = new ArrayList<>();

        for(int bosl = 0; bosl < this.bitOutputStreamList.length; bosl++) {
            this.bitOutputStreamList[bosl] = new BitOutputStream();
        }

		for (int ch = 1; ch <= numRadio; ch++)
		{ maxBandWidthList[ch -1] = config.getIntValue("comms.channels." + ch + ".bandwidth"); }

		this.initLoadProvider();
	}

	public boolean canUseRadio()
	{ return this.useRadio; }

	public RadioConfig getRadioConfig()
	{ return this.radioConfig; }

	public VoiceConfig getVoiceConfig()
	{ return this.voiceConfig; }

	public int getTime()
	{ return this.kernelTime; }

	public int getMaxBandWidth(int ch)
	{ return this.maxBandWidthList[ch -1]; }

	public boolean isAgentCalled()
	{ return this.agentCalled; }

	public void receiveMessage(int time, Collection<Command> heard)
	{
		this.kernelTime = time;
		this.receivedMessages.clear();
		this.agentCalled = false;

		for (BitOutputStream bos : bitOutputStreamList)
		{ bos.reset(); }

		for (Command command : heard)
		{
			if (command instanceof AKSpeak)
			{
				if (agentID == command.getAgentID()) { continue; }

				byte[] data = ((AKSpeak)command).getContent();
				if (data.length <= 0) { continue; }

				if (((AKSpeak) command).getChannel() == 0) {
					String voice = new String(data);
					if ("Help".equalsIgnoreCase(voice) || "Ouch".equalsIgnoreCase(voice)) {
//						System.out.println(voice + " : " + command.getAgentID() + " : " );
						this.agentCalled = true;
						continue;
					}
					String[] voiceData = voice.split(this.voiceConfig.getMessageSeparator());
					this.receiveVoiceMessage((AKSpeak)command, Arrays.copyOfRange(voiceData, 1, voiceData.length - 1), this.receivedMessages);
					// TODO: refactoring
				}
				else
				{
					this.receiveRadioMessage((AKSpeak)command, this.receivedMessages);
				}
			}
		}
	}

	private void receiveRadioMessage(AKSpeak akSpeak, List<CommunicationMessage> list)
	{
		if (akSpeak.getContent() == null || list == null)
		{ return; }
		BitStreamReader bsr = new BitStreamReader(akSpeak.getContent());
		int msgID = bsr.getBits(this.radioConfig.getSizeOfMessageID());
//		MessageProvider provider = this.providerList[bsr.getBits(this.radioConfig.getSizeOfMessageID())];
		MessageProvider provider = this.providerList[msgID];
//		System.out.println("MSGID: " + msgID);
		int lastRemainBufferSize = bsr.getRemainBuffer();
		while(bsr.getRemainBuffer() > 0)
		{
			try
			{
				CommunicationMessage msg = provider.create(this, bsr, akSpeak.getAgentID());
				list.add(msg);
			} catch(Exception e) {
				//System.err.println("Received message is corrupt or format is different.");
				e.printStackTrace();
				return;
			}

			// TODO: Check!!
			if (bsr.getRemainBuffer() == lastRemainBufferSize)  { break; }
			else { lastRemainBufferSize = bsr.getRemainBuffer(); }

//			System.out.println("MSG : " + msgID + ", RemainBuf : " + bsr.getRemainBuffer());
		}
	}

	// TODO: refactoring
	private void receiveVoiceMessage(AKSpeak akSpeak, String[] data, List<CommunicationMessage> list)
	{
		if (data == null || (data.length & 0x01) == 1 || list == null) //?
		{ return; }
		for (int count = 0; count < data.length; count += 2)
		{
			int id = Integer.parseInt(data[count]);
			String[] messageData = data[count + 1].split(this.voiceConfig.getDataSeparator());
			list.add(this.providerList[id].create(this, messageData, akSpeak.getAgentID()));
		}
	}

	public List<Message> createSendMessage(EntityID agentID)
	{
		List<Message> messages = new ArrayList<Message>();

		int bosNum = 0;
		boolean isFirstLoop = true;
		for (int ch = 1; ch <= numRadio; ch++)
		{
			int sentMessageSize = 0;

			for (; bosNum < bitOutputStreamList.length; bosNum++)
			{
				BitOutputStream bos = bitOutputStreamList[bosNum];
				if (bos.size() <= 0)
				{ continue; }
				if ((sentMessageSize + bos.size()) > getMaxBandWidth(ch))
				{ continue; }
				sentMessageSize += bos.size();
				messages.add(new AKSpeak(agentID, this.getTime(), ch, bos.toByteArray()));
			}

			if (ch == numRadio && isFirstLoop)
			{
				isFirstLoop = false;
				ch = 1;
				bosNum = 0;
			}
//			System.out.println("@MSG:" + sentMessageSize);
		}

//		messages.add(new AKSpeak(agentID, this.getTime(), 1, ("TEST").getBytes()));
		// StringBuilder sb = new StringBuilder();
		// for (CommunicationMessage msg : this.sendMessages)
		// { this.providerList[msg.getMessageID()].write(this, sb, msg); }

		return messages;
	}

	public List<CommunicationMessage> getReceivedMessage() // FOR-COMPATIBLE
	{
		return this.receivedMessages;
	}

	public <M extends CommunicationMessage> void addSendMessage(M msg)
	{
		this.sendMessages.add(msg);
		int msgID = msg.getMessageID();
//		System.out.println("msgID:" + msgID);
		// TODO: need cutting data
		this.providerList[msgID].write(this, bitOutputStreamList[msgID], msg);
	}

	// public void old_addSendMessage(CommunicationMessage msg)
	// {
	// 	this.sendMessages.add(msg);
	// }

	public void addVoiceSendMessage(CommunicationMessage msg)
	{
		// TODO: NFCのリストを用意して．．．いろいろ
		this.sendMessages.add(msg);
	}

	private void initLoadProvider()
	{
		// TODO: Load provider
		this.registerStandardProvider(new MessageDummyProvider(MessageID.dummyMessage));
		this.registerStandardProvider(new MessageCivilianProvider(MessageID.civilianMessage));
		this.registerStandardProvider(new MessageFireBrigadeProvider(MessageID.fireBrigadeMessage));
		this.registerStandardProvider(new MessagePoliceForceProvider(MessageID.policeForceMessage));
		this.registerStandardProvider(new MessageAmbulanceTeamProvider(MessageID.ambulanceTeamMessage));
		this.registerStandardProvider(new MessageBuildingProvider(MessageID.buildingMessage));
		this.registerStandardProvider(new MessageRoadProvider(MessageID.roadMessage));
		this.registerStandardProvider(new MessageReportProvider(MessageID.reportMessage));
		this.registerStandardProvider(new CommandPoliceProvider(MessageID.policeCommand));
		this.registerStandardProvider(new CommandAmbulanceProvider(MessageID.ambulanceCommand));
		this.registerStandardProvider(new CommandFireProvider(MessageID.fireCommand));
		this.registerStandardProvider(new CommandScoutProvider(MessageID.scoutCommand));
		//this.register(CommunicationMessage.buildingMessageID, new MessageBuildingProvider(this.event));
		//this.register(CommunicationMessage.blockadeMessageID, new BlockadeMessageProvider(this.event));
		//this.register(CommunicationMessage.victimMessageID,   new VictimMessageProvider());
		//this.register(CommunicationMessage.positionMessageID, new PositionMessageProvider(this.event));
	}

	private void registerStandardProvider(MessageProvider provider)
	{
		this.providerList[provider.getMessageID()] = provider;
	}

	public boolean registerProvider(MessageProvider provider)
	{
		int messageID = provider.getMessageID();
		if (!this.developerMode || this.kernelTime != -1 || provider == null || messageID < 0)
		{ return false; }

		if (messageID >= this.providerList.length)
		{
			this.providerList = Arrays.copyOf(this.providerList, messageID +1);
			this.bitOutputStreamList = Arrays.copyOf(this.bitOutputStreamList, messageID +1);
		}
		else if (this.providerList[messageID] != null)
		{ return false; }

		this.registerStandardProvider(provider);
		this.radioConfig.updateMessageIDSize(messageID);
		this.searchEvent(this.providerList[messageID]);
		return true;
	}

	public boolean registerEvent(MessageEvent event)
	{
		if (event == null)
		{ return false; }
		this.eventList.add(event);
		this.searchProvider(event);
		return true;
	}

	private void searchProvider(MessageEvent event)
	{
		for (MessageProvider provider : this.providerList) {
			if(provider != null) {
				provider.trySetEvent(event);
			}
		}
	}

	private void searchEvent(MessageProvider provider)
	{
		// if (this.eventList.size() < 1)
		// {	return; }

		for (MessageEvent event : this.eventList) {
			provider.trySetEvent(event);
		}
	}
}
