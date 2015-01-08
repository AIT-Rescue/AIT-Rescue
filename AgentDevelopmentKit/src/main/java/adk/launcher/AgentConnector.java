package adk.launcher;

import adk.launcher.connect.*;
import rescuecore2.registry.Registry;
import rescuecore2.standard.entities.StandardEntityFactory;
import rescuecore2.standard.entities.StandardPropertyFactory;
import rescuecore2.standard.messages.StandardMessageFactory;

import rescuecore2.Constants;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.components.TCPComponentLauncher;
import rescuecore2.config.Config;

import java.io.File;

public class AgentConnector {

	public static final File DIRECTORY_CONFIG = new File(System.getProperty("user.dir"), "config");
	public static final File DIRECTORY_TACTICS = new File(System.getProperty("user.dir"), "tactics");
    
    private Config config;
    
    private TeamLoader loader;

    //private List<Connect> connectList;
    
    public AgentConnector(String[] args) {
        this(DIRECTORY_CONFIG, DIRECTORY_TACTICS, args);
    }

	public AgentConnector(File configPath, File tacticsPath, String[] args) {
		this.init(configPath, tacticsPath, args);
	}
    
    private void init(File configPath, File tacticsPath, String[] args) {
        //register rescue system
        Registry.SYSTEM_REGISTRY.registerEntityFactory(StandardEntityFactory.INSTANCE);
        Registry.SYSTEM_REGISTRY.registerMessageFactory(StandardMessageFactory.INSTANCE);
        Registry.SYSTEM_REGISTRY.registerPropertyFactory(StandardPropertyFactory.INSTANCE);
        //init config
        this.config = ConfigInitializer.getConfig(configPath, args);
        //load team jar
		this.loader = new TeamLoader(tacticsPath);
        /*this.connectList = Lists.newArrayList(
                new ConnectAmbulanceAgent(),
                new ConnectFireAgent(),
                new ConnectPoliceAgent(),
                new ConnectAmbulanceCenter()
        );*/
    }
    
    public void start() {
        String host = this.config.getValue(Constants.KERNEL_HOST_NAME_KEY, Constants.DEFAULT_KERNEL_HOST_NAME);
        int port = this.config.getIntValue(Constants.KERNEL_PORT_NUMBER_KEY, Constants.DEFAULT_KERNEL_PORT_NUMBER);
        ComponentLauncher cl = new TCPComponentLauncher(host, port, this.config);
        System.out.println("[START] Connect Server (host:" + host + ", port:" + port + ")");
        this.connectAmbulance(cl);
        this.connectFire(cl);
        this.connectPolice(cl);
        new ConnectAmbulanceCenter().connect(cl, this.config, this.loader, this.config.getValue(ConfigKey.KEY_AMBULANCE_NAME), this.config.getIntValue(ConfigKey.KEY_AMBULANCE_COUNT));
        new ConnectFireCenter().connect(cl, this.config, this.loader, this.config.getValue(ConfigKey.KEY_FIRE_NAME), this.config.getIntValue(ConfigKey.KEY_FIRE_COUNT));
        new ConnectPoliceCenter().connect(cl, this.config, this.loader, this.config.getValue(ConfigKey.KEY_POLICE_NAME), this.config.getIntValue(ConfigKey.KEY_FIRE_COUNT));
        System.out.println("[END  ] Success Connect Server");
    }
    
    private void connectAmbulance(ComponentLauncher cl) {
        this.connectAmbulance(cl, this.config.getValue(ConfigKey.KEY_AMBULANCE_NAME), this.config.getIntValue(ConfigKey.KEY_AMBULANCE_COUNT));
    }
    
    private void connectAmbulance(ComponentLauncher cl, String name, int count) {
		Connect connect = new ConnectAmbulanceAgent();
		connect.connect(cl, this.config, this.loader, name, count);
    }

	private void connectFire(ComponentLauncher cl) {
		this.connectFire(cl, this.config.getValue(ConfigKey.KEY_FIRE_NAME), this.config.getIntValue(ConfigKey.KEY_FIRE_COUNT));
	}

	private void connectFire(ComponentLauncher cl, String name, int count) {
		Connect connect = new ConnectFireAgent();
		connect.connect(cl, this.config, this.loader, name, count);
	}

	private void connectPolice(ComponentLauncher cl) {
		this.connectPolice(cl, this.config.getValue(ConfigKey.KEY_POLICE_NAME), this.config.getIntValue(ConfigKey.KEY_POLICE_COUNT));
	}

	private void connectPolice(ComponentLauncher cl, String name, int count) {
		Connect connect = new ConnectPoliceAgent();
		connect.connect(cl, this.config, this.loader, name, count);
	}
}
