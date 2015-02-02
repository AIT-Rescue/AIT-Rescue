package adk.launcher.agent;

import adk.team.action.Action;
import adk.team.tactics.Tactics;
import comlib.agent.CommunicationAgent;
import comlib.manager.MessageManager;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.standard.messages.AKRest;

import java.util.List;
import java.util.stream.Collectors;

public abstract class TacticsAgent<E extends StandardEntity> extends CommunicationAgent<E> {
	
    public static final String LOS_MAX_DISTANCE_KEY = "perception.los.max-distance";
    
    protected Tactics tactics;
    public int ignoreTime;

    public TacticsAgent(Tactics t, boolean pre) {
        super();
        this.tactics = t;
        this.tactics.pre = pre;
    }

		@SuppressWarnings("unchecked")
    @Override
    public void postConnect() {
        super.postConnect();
        this.ignoreTime = this.config.getIntValue(kernel.KernelConstants.IGNORE_AGENT_COMMANDS_KEY);
        this.tactics.sightDistance = this.config.getIntValue(this.LOS_MAX_DISTANCE_KEY);
        this.tactics.world = this.getWorld();
        this.tactics.model = this.getWorld();
        this.tactics.agentID = this.getID();
        this.tactics.refugeList = this.getRefuges();
        //this.tactics.setWorldInfo(this);
        this.setAgentUniqueValue();
        this.setAgentEntity();
        this.tactics.preparation(this.config);
    }

    protected abstract void setAgentUniqueValue();

    protected abstract void setAgentEntity();

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
            this.tactics.agentID = this.getID();
            this.tactics.ignoreTimeThink(time, changed, this.manager);
            return;
        }
        Action action = this.tactics.think(time, changed, this.manager);
        Message message = action == null ? new AKRest(this.getID(), time) : action.getCommand(this.getID(), time);
        //System.out.println(message.getClass());
        this.send(message);
    }

    @Override
    public void receiveBeforeEvent(int time, ChangeSet changed) {
        this.tactics.time = time;
        this.tactics.changed = changed;
        //this.tactics.world = this.getWorld();
        this.setAgentEntity();
    }

    @Override
    public void sendAfterEvent(int time, ChangeSet changed) {
    }

    @Override
    public List<Refuge> getRefuges() {
        return this.model.getEntitiesOfType(StandardEntityURN.REFUGE).stream().map(entity -> (Refuge) entity).collect(Collectors.toList());
    }

    public StandardWorldModel getWorld() {
        return this.model;
    }
}
