package adk.launcher.connect;

import adk.launcher.ConfigKey;
import adk.launcher.TeamLoader;
import adk.launcher.agent.FireBrigadeAgent;
import adk.team.Team;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectorFireAgent implements Connector {
    @Override
    public void connect(ComponentLauncher launcher, Config config, TeamLoader loader) {
        String name = config.getValue(ConfigKey.KEY_FIRE_AGENT_NAME, "dummy");
        int count = config.getIntValue(ConfigKey.KEY_FIRE_AGENT_COUNT, -1);
        System.out.println("[START] Connect Fire (teamName:" + name + ")");
        System.out.println("[INFO ] Load Fire Team (teamName:" + name + ")");
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
                    System.out.println("[END  ] Connect Fire (success:0)");
                    return;
                }
            }
        }
        if(team.getFireBrigadeTactics() == null) {
            System.out.println("[ERROR] Cannot Load Fire Brigade Tactics !!");
            if(TeamLoader.KEYWORD_RANDOM.equalsIgnoreCase(name)) {
                int limit = config.getIntValue(ConfigKey.KEY_LOAD_RETRY, loader.size());
                int i = 0;
                while (i < limit && team.getFireBrigadeTactics() == null) {
                    System.out.println("[INFO ] Retry Load Team (teamName:" + name + ")");
                    team = loader.getRandomTeam();
                    i++;
                }
            }
            if(team.getFireBrigadeTactics() == null) {
                if (config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
                    System.out.println("[INFO ] Load Dummy System");
                    team = loader.getDummy();
                } else {
                    System.out.println("[END  ] Connect Fire (success:0)");
                    return;
                }
            }
        }
        System.out.println("[INFO ] Fire Brigade Tactics (teamName:" + team.getTeamName() + ")");
        name = "[INFO ] Connect FireBrigadeAgent (teamName:" + team.getTeamName() + ")";
        int connectAgent = 0;
        try {
            for (int i = 0; i != count; ++i) {
                launcher.connect(new FireBrigadeAgent(team.getFireBrigadeTactics(), config.getBooleanValue(ConfigKey.KEY_PRE, false)));
                System.out.println(name);
                connectAgent++;
            }
        } catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
        }
        System.out.println("[END  ] Connect Fire (success:" + connectAgent + ")");
    }
}
