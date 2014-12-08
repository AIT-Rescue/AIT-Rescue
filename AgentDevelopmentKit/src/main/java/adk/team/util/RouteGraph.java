package adk.team.util;

import adk.team.util.provider.WorldProvider;
import rescuecore2.standard.entities.Road;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class RouteGraph {

    private WorldProvider<? extends StandardEntity> provider;
    private Map<EntityID, RouteEdge> edgeMap;
    private Map<EntityID, RouteNode> nodeMap;
    private List<RouteEdge> edgeList;
    private List<RouteNode> nodeList;
    private Set<EntityID> badArea;

    public RouteGraph(WorldProvider<? extends StandardEntity> worldProvider) {
        this.provider = worldProvider;
        this.edgeMap = new HashMap<>();
        this.nodeMap = new HashMap<>();
        this.edgeList = new ArrayList<>();
        this.nodeList = new ArrayList<>();
        this.badArea = new HashSet<>();
        this.init();
    }

    private void init() {
        Collection<StandardEntity> roads = this.provider.getWorld().getEntitiesOfType(StandardEntityURN.ROAD);
        Collection<StandardEntity> buildings = this.provider.getWorld().getEntitiesOfType(StandardEntityURN.BUILDING);
        for(StandardEntity entity : roads) {
            Road road = (Road)entity;
            EntityID roadID = road.getID();
            List<EntityID> neighbours = road.getNeighbours();
            int size = neighbours.size();
            if(size == 0) {
                this.badArea.add(road.getID());
            }
            else if(size == 1) {
                EntityID aradID = neighbours.get(0);
                if (buildings.contains(aradID)) {
                    this.badArea.add(aradID);
                    this.badArea.add(roadID);
                } else if(!this.nodeMap.containsKey(roadID)) {
                    RouteNode node = new RouteNode(roadID, neighbours);
                    this.nodeMap.put(roadID, node);
                    this.nodeList.add(node);
                }
            }
            else  if(size == 2) {
                if (!this.edgeMap.containsKey(roadID)) {
                    LinkedList<EntityID> edges = new LinkedList<>();
                    List<EntityID> roadList = new ArrayList<>();
                    edges.add(roadID);
                    roadList.add(roadID);
                    EntityID neighbourAreaID = neighbours.get(0);
                    if(buildings.contains(neighbourAreaID)) {

                    }

                }

            }
        }
    }
}

/*
隣接２箇所→
両側道路→Bad
片側道路→Edgeに内包されるListになる
 */