package adk.launcher.option;

import rescuecore2.Constants;
import rescuecore2.config.Config;

public class OptionPort extends Option {

    @Override
    public String getKey() {
        return "-p";
    }

    @Override
    public void setValue(Config config, String[] datas) {
        if (datas.length == 2) {
            config.setValue(Constants.KERNEL_PORT_NUMBER_KEY, datas[1]);
        }
    }
}