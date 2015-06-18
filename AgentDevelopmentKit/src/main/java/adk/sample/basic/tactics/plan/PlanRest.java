package adk.sample.basic.tactics.plan;

import adk.team.tactics.Tactics;

public class PlanRest extends Plan {

    int count;
    int limit;

    public PlanRest(int restTime) {
        this.count = 0;
        this.limit = restTime;
    }

    @Override
    public boolean complete(Tactics tactics) {
        return true;
    }
}
