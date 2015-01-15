package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionPre extends Option {
    @Override
    public String getKey() {
        return "-pre";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_PRE, datas[1]);
        }
    }
}
