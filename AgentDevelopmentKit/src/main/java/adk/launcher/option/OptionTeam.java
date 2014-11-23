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
        if(datas.length == 5) {
            config.setValue(ConfigKey.KEY_AMBULANCE_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_AMBULANCE_COUNT, datas[2]);
            config.setValue(ConfigKey.KEY_FIRE_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_FIRE_COUNT, datas[3]);
            config.setValue(ConfigKey.KEY_POLICE_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_POLICE_COUNT, datas[4]);
        }
    }
}