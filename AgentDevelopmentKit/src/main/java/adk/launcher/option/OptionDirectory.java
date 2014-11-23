package adk.launcher.option;

import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionDirectory extends Option {

    @Override
    public String getKey() {
        return "-d";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_DIRECTORY, datas[1]);
        }
    }
}