package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionPolice extends Option {

    @Override
    public String getKey() {
        return "-pf";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 3) {
            config.setValue(ConfigKey.KEY_POLICE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_POLICE_AGENT_COUNT, datas[2]);
        }
        else if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_POLICE_AGENT_COUNT, datas[1]);
        }
        else if(datas.length == 4) {
            config.setValue(ConfigKey.KEY_POLICE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_POLICE_AGENT_COUNT, datas[2]);
            config.setValue(ConfigKey.KEY_POLICE_STATION_COUNT, datas[3]);
        }
        else if(datas.length == 5) {
            config.setValue(ConfigKey.KEY_POLICE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_POLICE_AGENT_COUNT, datas[2]);
            config.setValue(ConfigKey.KEY_POLICE_STATION_NAME, datas[3]);
            config.setValue(ConfigKey.KEY_POLICE_STATION_COUNT, datas[4]);
        }
    }
}