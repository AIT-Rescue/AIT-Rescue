package adk.sample.dummy.control;

import adk.team.control.ControlPolice;
import comlib.manager.MessageManager;
import rescuecore2.config.Config;
import rescuecore2.worldmodel.ChangeSet;

public class DummyControlPolice extends ControlPolice {
    @Override
    public String getControlName() {
        return "Dummy System";
    }

    @Override
    public void preparation(Config config) {

    }

    @Override
    public void registerEvent(MessageManager manager) {

    }

    @Override
    public void think(int currentTime, ChangeSet updateWorldData, MessageManager manager) {

    }
}
