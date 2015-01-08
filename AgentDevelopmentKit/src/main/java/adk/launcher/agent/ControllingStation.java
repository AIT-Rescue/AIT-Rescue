package adk.launcher.agent;

import adk.team.control.Control;
import comlib.agent.CommunicationAgent;
import comlib.manager.MessageManager;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ControllingStation<E extends Building> extends CommunicationAgent<E> {

    protected Control control;
    public int ignoreTime;

    public ControllingStation(Control i) {
        super();
        this.control = i;
    }

    @Override
    public void postConnect() {
        super.postConnect();
        this.ignoreTime = this.config.getIntValue(kernel.KernelConstants.IGNORE_AGENT_COMMANDS_KEY);
        this.control.world = this.getWorld();
        this.control.model = this.getWorld();
        this.control.stationID = this.getID();
        this.control.refugeList = this.getRefuges();
        this.setCenterUniqueValue();
        this.setCenterEntity();
        this.control.preparation(this.config);
    }

    protected abstract void setCenterUniqueValue();

    protected abstract void setCenterEntity();

    @Override
    public void registerProvider(MessageManager manager) {
        this.control.registerProvider(manager);
    }

    @Override
    public void registerEvent(MessageManager manager) {
        this.control.registerEvent(manager);
    }

    @Override
    public void think(int time, ChangeSet changed) {
        if(time <= this.ignoreTime) {
            this.control.stationID = this.getID();
            this.control.ignoreTimeThink(time, changed, this.manager);
            return;
        }
        this.control.think(time, changed, this.manager);
    }

    @Override
    public void receiveBeforeEvent(int time, ChangeSet changed) {
        this.control.time = time;
        this.control.changed = changed;
        this.setCenterEntity();
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
