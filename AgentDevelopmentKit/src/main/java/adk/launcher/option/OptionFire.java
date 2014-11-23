package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionFire extends Option {

    @Override
    public String getKey() {
        return "-fb";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 3) {
            config.setValue(ConfigKey.KEY_FIRE_NAME, datas[1]);
            config.setValue(ConfigKey.KEY_FIRE_COUNT, datas[2]);
        }
        else if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_FIRE_COUNT, datas[1]);
        }
    }
}