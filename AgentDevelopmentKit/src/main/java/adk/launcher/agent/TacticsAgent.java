package adk.launcher.agent;

import adk.team.action.Action;
import adk.team.tactics.Tactics;
import comlib.agent.CommunicationAgent;
import comlib.manager.MessageManager;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.standard.messages.AKRest;
import rescuecore2.messages.Message;

public abstract class TacticsAgent<T extends Tactics, E extends StandardEntity> extends CommunicationAgent<E> {
    
    public Tactics tactics;
    public int ignoreTime;

    public TacticsAgent(T t) {
        super();
        this.tactics = t;
    }
    
    @Override
    public void postConnect() {
        super.postConnect();
        this.ignoreTime = this.config.getIntValue(kernel.KernelConstants.IGNORE_AGENT_COMMANDS_KEY);
        this.tactics.model = this.model;
        this.tactics.agentID = this.getID(); //AgentのEntityIDはかわるのか？？
        this.setAgentEntity();
        this.setAgentUniqueValue();
        
        this.tactics.preparation(this.config);
    }
    
    public abstract void setAgentEntity();
    
    public abstract void setAgentUniqueValue();

    @Override
    public void registerProvider(MessageManager manager) {
        this.tactics.registerProvider(manager);
    }
    
    @Override
    public void registerEvent(MessageManager manager) {
        this.tactics.registerEvent(manager);
    }
    
    @Override
    public void think(int time, ChangeSet changed) {
        if(time <= this.ignoreTime) {
            this.tactics.ignoreTimeThink(changed, this.manager);
            return;
        }

        Action actMessage = this.tactics.think(time, changed, this.manager);
        this.send(actMessage == null ? new AKRest(this.getID(), time) : actMessage.getMessage());
    }

    @Override
    public void receiveBeforeEvent(int time, ChangeSet changed) {
        //set value

        this.tactics.time = time;
        this.tactics.model = this.model;
        this.tactics.agentID = this.getID();
        this.tactics.location = this.location();
        this.setAgentEntity();
    }

    @Override
    public void sendAfterEvent(int time, ChangeSet changed) {

    }
}
