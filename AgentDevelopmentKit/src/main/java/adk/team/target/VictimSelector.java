package comlib.adk.util.target;

import comlib.adk.team.tactics.Tactics;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

public abstract class VictimSelector extends TargetSelector {

    public VictimSelector(Tactics user) {
        super(user);
    }

    public abstract void add(Civilian civilian);
    //public abstract void add(AmbulanceTeam agent);
    //public abstract void add(FireBrigade agent);
    //public abstract void add(PoliceForce agent);
    public abstract void add(Human agent);

    public abstract void add(EntityID id);

    public abstract void remove(Civilian civilian);
    public abstract void remove(Human agent);

    public abstract void remove(EntityID id);
}
