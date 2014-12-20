package comlib.manager;

import comlib.message.information.*;
import rescuecore2.standard.entities.*;

public class UpdateHelper {
    public static Building reflectedMessage(StandardWorldModel world, MessageBuilding message) {
        Building building = (Building)world.getEntity(message.getBuildingID());
        building.setFieryness(message.getFieryness());
        building.setBrokenness(message.getBrokenness());
        return building;
    }

    public static AmbulanceTeam reflectedMessage(StandardWorldModel world, MessageAmbulanceTeam message) {
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

    public static Civilian reflectedMessage(StandardWorldModel world, MessageCivilian message) {
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

    public static FireBrigade reflectedMessage(StandardWorldModel world, MessageFireBrigade message) {
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

    public static PoliceForce reflectedMessage(StandardWorldModel world, MessagePoliceForce message) {
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

    public static Blockade reflectedMessage(StandardWorldModel world, MessageRoad message) {
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
