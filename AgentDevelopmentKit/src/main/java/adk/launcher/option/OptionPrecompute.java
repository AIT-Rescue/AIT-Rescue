package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionPrecompute extends Option {
    @Override
    public String getKey() {
        return "-precompute";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_PRECOMPUTE, datas[1]);
        }
    }
}
