package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionRetry extends Option {
    @Override
    public String getKey() {
        return "-r";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_LOAD_RETRY, datas[1]);
        }
    }
}
