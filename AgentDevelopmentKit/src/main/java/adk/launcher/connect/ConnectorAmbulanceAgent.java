package adk.launcher.connect;

import adk.launcher.ConfigKey;
import adk.launcher.TeamLoader;
import adk.launcher.agent.AmbulanceTeamAgent;
import adk.team.Team;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectorAmbulanceAgent implements Connector {

    @Override
    public void connect(ComponentLauncher launcher, Config config, TeamLoader loader) {
        String name = config.getValue(ConfigKey.KEY_AMBULANCE_AGENT_NAME, "dummy");
        int count = config.getIntValue(ConfigKey.KEY_AMBULANCE_AGENT_COUNT, -1);
        System.out.println("[START] Connect Ambulance (teamName:" + name + ")");
        System.out.println("[INFO ] Load Ambulance Team (teamName:" + name + ")");
        Team team = loader.get(name);
        if(team == null) {
            System.out.println("[ERROR] Team is Null !!");
            if(TeamLoader.KEYWORD_RANDOM.equalsIgnoreCase(name)) {
                int limit = config.getIntValue(ConfigKey.KEY_LOAD_RETRY, loader.size());
                int i = 0;
                while (i < limit && team == null) {
                    System.out.println("[INFO ] Retry Load Team (teamName:" + name + ")");
                    team = loader.getRandomTeam();
                    i++;
                }
            }
            if(team == null) {
                if (config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
                    System.out.println("[INFO ] Load Dummy System");
                    team = loader.getDummy();
                } else {
                    System.out.println("[ERROR] Cannot Load Team !!");
                    System.out.println("[END  ] Connect Ambulance (success:0)");
                    return;
                }
            }
        }
        if(team.getAmbulanceTeamTactics() == null) {
            System.out.println("[ERROR] Cannot Load Ambulance Team Tactics !!");
            if(TeamLoader.KEYWORD_RANDOM.equalsIgnoreCase(name)) {
                int limit = config.getIntValue(ConfigKey.KEY_LOAD_RETRY, loader.size());
                int i = 0;
                while (i < limit && team.getAmbulanceTeamTactics() == null) {
                    System.out.println("[INFO ] Retry Load Team (teamName:" + name + ")");
                    team = loader.getRandomTeam();
                    i++;
                }
            }
            if(team.getAmbulanceTeamTactics() == null) {
                if (config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
                    System.out.println("[INFO ] Load Dummy System");
                    team = loader.getDummy();
                } else {
                    System.out.println("[END  ] Connect Ambulance (success:0)");
                    return;
                }
            }
        }
        System.out.println("[INFO ] Ambulance Team Tactics (teamName:" + team.getTeamName() + ")");
        name = "[INFO ] Connect AmbulanceTeamAgent (teamName:" + team.getTeamName() + ")";
        int connectAgent = 0;
        try {
            for (int i = 0; i != count; ++i) {
                launcher.connect(new AmbulanceTeamAgent(team.getAmbulanceTeamTactics(), config.getBooleanValue(ConfigKey.KEY_PRECOMPUTE, false)));
                System.out.println(name);
                connectAgent++;
            }
        } catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
        }
        System.out.println("[END  ] Connect Ambulance (success:" + connectAgent + ")");
    }
}
