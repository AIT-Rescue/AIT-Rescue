package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionTeam extends Option {

    @Override
    public String getKey() {
        return "-t";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 8) {
            config.setValue(ConfigKey.KEY_FIRE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_FIRE_STATION_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_POLICE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_POLICE_STATION_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_AMBULANCE_STATION_NAME, datas[1]);

            config.setValue(ConfigKey.KEY_FIRE_AGENT_COUNT, datas[2]);
            config.setValue(ConfigKey.KEY_FIRE_STATION_COUNT, datas[3]);

            config.setValue(ConfigKey.KEY_POLICE_AGENT_COUNT, datas[4]);
            config.setValue(ConfigKey.KEY_POLICE_STATION_COUNT, datas[5]);

            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_COUNT, datas[6]);
            config.setValue(ConfigKey.KEY_AMBULANCE_STATION_COUNT, datas[7]);
        }
    }
}
