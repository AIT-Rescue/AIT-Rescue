package adk.launcher.agent;

import adk.team.action.Action;
import adk.team.tactics.Tactics;
import comlib.agent.CommunicationAgent;
import comlib.manager.MessageManager;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.standard.messages.AKRest;

import java.util.List;
import java.util.stream.Collectors;

public abstract class TacticsAgent<T extends Tactics, E extends StandardEntity> extends CommunicationAgent<E> {
    
    private Tactics tactics;
    private int ignoreTime;

    public TacticsAgent(T t) {
        super();
        this.tactics = t;
    }
    
    @Override
    public void postConnect() {
        super.postConnect();
        this.ignoreTime = this.config.getIntValue(kernel.KernelConstants.IGNORE_AGENT_COMMANDS_KEY);
        this.tactics.model = this.model;
        this.tactics.agentID = this.getID();
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
            this.tactics.ignoreTimeThink(time, changed, this.manager);
            return;
        }
        Action action = this.tactics.think(time, changed, this.manager);
        this.send(action == null ? new AKRest(this.getID(), time) : action.getCommand());
    }

    @Override
    public void receiveBeforeEvent(int time, ChangeSet changed) {
        this.tactics.time = time;
        this.setAgentEntity();
    }

    @Override
    public void sendAfterEvent(int time, ChangeSet changed) {
    }

    @Override
    public List<Refuge> getRefuges() {
        /*
        List<StandardEntity> entityList = new ArrayList<>(this.getWorld().getEntitiesOfType(StandardEntityURN.REFUGE));
        List<Refuge> refugeList = Lists.transform(entityList, new Function<StandardEntity, Refuge>() {
            @Override
            public Refuge apply(StandardEntity input) {
                return (Refuge)input;
            }
        });
        */
        return this.model.getEntitiesOfType(StandardEntityURN.REFUGE).stream().map(entity -> (Refuge) entity).collect(Collectors.toList());
    }
}
