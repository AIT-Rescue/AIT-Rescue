package adk.team.util;

import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.Human;

public interface VictimSelector extends TargetSelector {

    public void add(Civilian civilian);

    public void add(Human agent);

    public void remove(Civilian civilian);

    public void remove(Human agent);
}
