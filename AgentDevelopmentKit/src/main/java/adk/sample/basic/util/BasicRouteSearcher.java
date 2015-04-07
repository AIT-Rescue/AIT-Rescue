package adk.sample.basic.util;

import adk.team.util.RouteSearcher;
import adk.team.util.provider.WorldProvider;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.Entity;
import sample.SampleSearch;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class BasicRouteSearcher implements RouteSearcher {

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

    private void initRandomWalk() {
        this.neighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<>();
            }
        };
        for (Entity next : this.provider.getWorld()) {
            if (next instanceof Area) {
                Set<EntityID> roadNeighbours = new HashSet<>();
                for(EntityID areaID : ((Area)next).getNeighbours()) {
                    StandardEntity area = this.provider.getWorld().getEntity(areaID);
                    if(area instanceof Road || area instanceof Refuge) {
                        roadNeighbours.add(areaID);
                    }
                }
                this.neighbours.put(next.getID(), roadNeighbours);
            }
        }
    }

    @Override
    public List<EntityID> noTargetMove(int time, EntityID from) {
        List<EntityID> result = new ArrayList<>(50);
        Set<EntityID> seen = new HashSet<>();
        EntityID current = from;//this.provider.getOwner().getPosition();
        for (int i = 0; i < 50; ++i) {
            result.add(current);
            seen.add(current);
            List<EntityID> possible = new ArrayList<>(this.neighbours.get(current));
            Collections.shuffle(possible, this.random);
            boolean noTarget = true;
            for (EntityID next : possible) {
                if (seen.contains(next)) {
                    continue;
                }
                current = next;
                noTarget = false;
                break;
            }
            if (noTarget) {
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
