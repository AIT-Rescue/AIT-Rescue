package adk.team.util.refuge;

import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class ReservoirResult {
    private Reservoir reservoir;
    private int water;
    private int step;
    private List<EntityID> path;

    public ReservoirResult(Reservoir result, int resultWater, int resultStep, List<EntityID> resultPath) {
        this.reservoir = result;
        this.water = resultWater;
        this.step = resultStep;
        this.path = resultPath;
    }


    public Reservoir getReservoir() {
        return reservoir;
    }

    public EntityID getID() {
        return this.reservoir.getID();
    }

    public int getWater() {
        return water;
    }

    public int getStep() {
        return step;
    }

    public List<EntityID> getPath() {
        return path;
    }
}
