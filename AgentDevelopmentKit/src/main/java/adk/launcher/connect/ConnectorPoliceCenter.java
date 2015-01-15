package adk.launcher.connect;

import adk.launcher.ConfigKey;
import adk.launcher.TeamLoader;
import adk.launcher.station.PoliceForceStation;
import adk.team.Team;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectorPoliceCenter implements Connector {
    @Override
    public void connect(ComponentLauncher launcher, Config config, TeamLoader loader) {
        String name = config.getValue(ConfigKey.KEY_POLICE_STATION_NAME, "dummy");
        int count = config.getIntValue(ConfigKey.KEY_POLICE_STATION_COUNT, -1);
        System.out.println("[START] Connect Police Center (teamName:" + name + ")");
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
                    System.out.println("[END  ] Connect Police Center (success:0)");
                    return;
                }
            }
        }
        if(team.getPoliceOfficeControl() == null) {
            System.out.println("[ERROR] Cannot Load Police Office Control !!");
            if(TeamLoader.KEYWORD_RANDOM.equalsIgnoreCase(name)) {
                int limit = config.getIntValue(ConfigKey.KEY_LOAD_RETRY, loader.size());
                int i = 0;
                while (i < limit && team.getPoliceOfficeControl() == null) {
                    System.out.println("[INFO ] Retry Load Team (teamName:" + name + ")");
                    team = loader.getRandomTeam();
                    i++;
                }
            }
            if(team.getPoliceOfficeControl() == null) {
                if (config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
                    System.out.println("[INFO ] Load Dummy System");
                    team = loader.getDummy();
                } else {
                    System.out.println("[END  ] Connect Police Center (success:0)");
                    return;
                }
            }
        }
        System.out.println("[INFO ] Police Office Control (teamName:" + team.getTeamName() + ")");
        name = "[INFO ] Connect PoliceForceStation(teamName:" + team.getTeamName() + ")";
        int connectAgent = 0;
        try {
            for (int i = 0; i != count; ++i) {
                launcher.connect(new PoliceForceStation(team.getPoliceOfficeControl(), config.getBooleanValue(ConfigKey.KEY_PRE, false)));
                System.out.println(name);
                connectAgent++;
            }
        } catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
        }
        System.out.println("[END  ] Connect Police (success:" + connectAgent + ")");
    }
}
