package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionAmbulance extends Option {

    @Override
    public String getKey() {
        return "-at";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 3) {
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_COUNT, datas[2]);
        }
        else if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_COUNT, datas[1]);
        }
        else if(datas.length == 4) {
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_COUNT, datas[2]);
            config.setValue(ConfigKey.KEY_AMBULANCE_STATION_COUNT, datas[3]);
        }
        else if(datas.length == 5) {
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_AMBULANCE_AGENT_COUNT, datas[2]);
            config.setValue(ConfigKey.KEY_AMBULANCE_STATION_NAME, datas[3]);
            config.setValue(ConfigKey.KEY_AMBULANCE_STATION_COUNT, datas[4]);
        }
    }
}