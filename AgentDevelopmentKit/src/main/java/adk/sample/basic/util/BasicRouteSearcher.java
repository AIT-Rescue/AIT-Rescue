package adk.sample.basic.util;

import adk.team.util.RouteSearcher;
import adk.team.util.provider.WorldProvider;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Human;
import rescuecore2.worldmodel.Entity;
import sample.SampleSearch;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class BasicRouteSearcher implements RouteSearcher {

    private static final int RANDOM_WALK_LENGTH = 50;

    public WorldProvider<? extends Human> provider;

    private Map<EntityID, Set<EntityID>> neighbours;
    private Random random;

    private SampleSearch search;

    public BasicRouteSearcher(WorldProvider<? extends Human> user) {
        this.provider = user;
        this.search = new SampleSearch(user.getWorld());
        this.random = new Random((new Date()).getTime());
        this.initRandomWalk();
    }

    private void initRandomWalk()
    {
        //RandomWalk向け
        this.neighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<>();
            }
        };
        for (Entity next : this.provider.getWorld()) {
            if (next instanceof Area) {
                //if(next instanceof Road) {
                Collection<EntityID> areaNeighbours = ((Area)next).getNeighbours();
                this.neighbours.get(next.getID()).addAll(areaNeighbours);
            }
        }
    }

    @Override
    public List<EntityID> noTargetWalk(int time) {
        List<EntityID> result = new ArrayList<>(RANDOM_WALK_LENGTH);
        Set<EntityID> seen = new HashSet<>();
        EntityID current = this.provider.me().getPosition();
        for (int i = 0; i < RANDOM_WALK_LENGTH; ++i) {
            result.add(current);
            seen.add(current);
            List<EntityID> possible = new ArrayList<>(this.neighbours.get(current));
            Collections.shuffle(possible, this.random);
            boolean found = false;
            for (EntityID next : possible) {
                if (seen.contains(next)) {
                    continue;
                }
                current = next;
                found = true;
                break;
            }
            if (!found) {
                break;
            }
        }
        return result;
    }

    @Override
    public List<EntityID> getPath(int time, EntityID from, EntityID to) {
        return this.search.breadthFirstSearch(from, to);
    }
}
