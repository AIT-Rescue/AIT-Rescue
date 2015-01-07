package adk.launcher.connect;

import adk.launcher.TeamLoader;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;

public interface Connect {
    public abstract void connect(ComponentLauncher launcher, Config config, TeamLoader loader, String name, int count);
}
