package comlib.agent;


import comlib.manager.MessageManager;
import comlib.message.CommunicationMessage;
import rescuecore2.messages.Command;
import rescuecore2.messages.Message;
import rescuecore2.standard.components.StandardAgent;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.ChangeSet;

import java.util.Collection;
import java.util.List;


public abstract class CommunicationAgent<E extends StandardEntity> extends StandardAgent<E> {

    public MessageManager manager;

    public CommunicationAgent()
    {
        super();
    }

    public abstract void registerEvent(MessageManager manager);

    public abstract void think(int time, ChangeSet changed);


    public void sendSpeak(CommunicationMessage msg)
    {
        this.manager.addSendMessage(msg);
    }

    public void sendSay(CommunicationMessage msg)
    {
        this.manager.addVoiceSendMessage(msg);
    }

    @Override
    public void postConnect()
    {
        super.postConnect();
        this.manager = new MessageManager(this.config);
        this.registerProvider(this.manager);
        this.registerEvent(this.manager);
    }

    public void registerProvider(MessageManager manager){}

    @Override
    protected final void think(int time, ChangeSet changed, Collection<Command> heard)
    {
        this.receiveBeforeEvent(time, changed);
        try
        {
            this.manager.receiveMessage(time, heard);
        } catch (Exception s) { System.out.println("'");}
        this.think(time, changed);
        this.send(this.manager.createSendMessage(super.getID()));
        this.sendAfterEvent(time, changed);
    }

    public void receiveBeforeEvent(int time, ChangeSet changed) {
    }

    public void sendAfterEvent(int time, ChangeSet changed) {
    }

    public void send(Message[] msgs)
    {
        for(Message msg : msgs) super.send(msg);
    }

    public void send(List<Message> msgs)
    {
        for(Message msg : msgs) super.send(msg);
    }

    // temp Leave ---
    /*
         @Override
         protected final void sendSpeak(int time, int channel, byte[] data) {
//super.sendSpeak(time, channel, data);
         }

         @Override
         protected final void sendSay(int time, byte[] data) {
//super.sendSay(time, data);
         }
         */
}
