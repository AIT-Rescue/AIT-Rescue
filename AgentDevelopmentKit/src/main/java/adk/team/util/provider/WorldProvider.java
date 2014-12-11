package adk.team.util.provider;

import comlib.message.information.*;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public interface WorldProvider<E extends StandardEntity> {

    public int getCurrentTime();

    public ChangeSet getUpdateWorldData();

    public StandardWorldModel getWorld();

    public EntityID getOwnerID();

    public E getOwner();

    public StandardEntity getOwnerLocation();

    public List<Refuge> getRefugeList();

    default E me() {
        return getOwner();
    }

    default StandardEntity location() {
        return getOwnerLocation();
    }

    default List<Refuge> getRefuges() {
        return getRefugeList();
    }

    default EntityID getID() {
        return getOwnerID();
    }

    default Building reflectedMessage(MessageBuilding message) {
        Building building = (Building) getWorld().getEntity(message.getBuildingID());
        building.setFieryness(message.getFieryness());
        building.setBrokenness(message.getBrokenness());
        return building;
    }

    default AmbulanceTeam reflectedMessage(MessageAmbulanceTeam message) {
        StandardWorldModel world = getWorld();
        AmbulanceTeam ambulanceteam = (AmbulanceTeam) world.getEntity(message.getHumanID());
        if (ambulanceteam == null) {
            world.addEntity(new AmbulanceTeam(message.getHumanID()));
            ambulanceteam = (AmbulanceTeam) world.getEntity(message.getHumanID());
        }
        ambulanceteam.setHP(message.getHP());
        ambulanceteam.setBuriedness(message.getBuriedness());
        ambulanceteam.setDamage(message.getDamage());
        ambulanceteam.setPosition(message.getPosition());
        return ambulanceteam;
    }

    default Civilian reflectedMessage(MessageCivilian message) {
        StandardWorldModel world = getWorld();
        Civilian civilian = (Civilian)world.getEntity(message.getHumanID());
        if (civilian == null) {
            world.addEntity(new Civilian(message.getHumanID()));
            civilian = (Civilian) world.getEntity(message.getHumanID());
        }
        civilian.setHP(message.getHP());
        civilian.setBuriedness(message.getBuriedness());
        civilian.setDamage(message.getDamage());
        civilian.setPosition(message.getPosition());
        return civilian;
    }

    default FireBrigade reflectedMessage(MessageFireBrigade message) {
        StandardWorldModel world = getWorld();
        FireBrigade firebrigade = (FireBrigade) world.getEntity(message.getHumanID());
        if (firebrigade == null) {
            world.addEntity(new FireBrigade(message.getHumanID()));
            firebrigade = (FireBrigade) world.getEntity(message.getHumanID());
        }
        firebrigade.setHP(message.getHP());
        firebrigade.setBuriedness(message.getBuriedness());
        firebrigade.setDamage(message.getDamage());
        firebrigade.setPosition(message.getPosition());
        firebrigade.setWater(message.getWater());
        return firebrigade;
    }

    default PoliceForce reflectedMessage(MessagePoliceForce message) {
        StandardWorldModel world = getWorld();
        PoliceForce policeforce = (PoliceForce) world.getEntity(message.getHumanID());
        if (policeforce == null) {
            world.addEntity(new PoliceForce(message.getHumanID()));
            policeforce = (PoliceForce) world.getEntity(message.getHumanID());
        }
        policeforce.setHP(message.getHP());
        policeforce.setBuriedness(message.getBuriedness());
        policeforce.setDamage(message.getDamage());
        policeforce.setPosition(message.getPosition());
        return policeforce;
    }

    default Blockade  reflectedMessage(MessageRoad message) {
        StandardWorldModel world = getWorld();
        Blockade blockade = (Blockade) world.getEntity(message.getBlockadeID());
        if (blockade == null) {
            world.addEntity(new Blockade(message.getBlockadeID()));
            blockade = (Blockade) world.getEntity(message.getBlockadeID());
        }
        // blockade.setPosition(message.getPosition());
        blockade.setRepairCost(message.getRepairCost());
        return blockade;
    }
}
