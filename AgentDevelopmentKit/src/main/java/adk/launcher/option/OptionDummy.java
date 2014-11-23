package adk.launcher.option;


import adk.launcher.ConfigKey;
import rescuecore2.config.Config;

public class OptionDummy extends Option{
    @Override
    public String getKey() {
        return "-ds";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if(datas.length == 2) {
            config.setValue(ConfigKey.KEY_DUMMY_SYSTEM, datas[1]);
        }
    }
}
