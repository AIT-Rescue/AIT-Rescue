package adk.launcher.connect;

import adk.launcher.ConfigKey;
import adk.launcher.TeamLoader;
import adk.launcher.agent.PoliceForceAgent;
import adk.team.Team;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectAmbulanceCenter implements Connect {
    @Override
    public void connect(ComponentLauncher launcher, Config config, TeamLoader loader, String name, int count) {
        System.out.println("[START] Connect Police (teamName:" + name + ")");
        System.out.println("[INFO ] Load Police Team (teamName:" + name + ")");
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
                    System.out.println("[END  ] Connect Police (success:0)");
                    return;
                }
            }
        }
        if(team.getPoliceForceTactics() == null) {
            System.out.println("[ERROR] Cannot Load Police Force Tactics !!");
            if(TeamLoader.KEYWORD_RANDOM.equalsIgnoreCase(name)) {
                int limit = config.getIntValue(ConfigKey.KEY_LOAD_RETRY, loader.size());
                int i = 0;
                while (i < limit && team.getPoliceForceTactics() == null) {
                    System.out.println("[INFO ] Retry Load Team (teamName:" + name + ")");
                    team = loader.getRandomTeam();
                    i++;
                }
            }
            if(team.getPoliceForceTactics() == null) {
                if (config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
                    System.out.println("[INFO ] Load Dummy System");
                    team = loader.getDummy();
                } else {
                    System.out.println("[END  ] Connect Police (success:0)");
                    return;
                }
            }
        }
        System.out.println("[INFO ] Police Force Tactics (teamName:" + team.getTeamName() + ")");
        name = "[INFO ] Connect PoliceForceAgent (teamName:" + team.getTeamName() + ")";
        int connectAgent = 0;
        try {
            for (int i = 0; i != count; ++i) {
                launcher.connect(new PoliceForceAgent(team.getPoliceForceTactics()));
                System.out.println(name);
                connectAgent++;
            }
        } catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
        }
        System.out.println("[END  ] Connect Police (success:" + connectAgent + ")");
    }
}
