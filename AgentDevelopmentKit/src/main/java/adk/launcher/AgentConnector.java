package adk.launcher;

import adk.launcher.connect.Connect;
import adk.launcher.connect.ConnectAmbulanceAgent;
import adk.launcher.connect.ConnectPoliceAgent;
import rescuecore2.registry.Registry;
import rescuecore2.standard.entities.StandardEntityFactory;
import rescuecore2.standard.entities.StandardPropertyFactory;
import rescuecore2.standard.messages.StandardMessageFactory;

import adk.launcher.agent.FireBrigadeAgent;
import adk.launcher.agent.PoliceForceAgent;
import adk.team.Team;
import rescuecore2.Constants;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.components.TCPComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

import java.io.File;

public class AgentConnector {
    
    private Config config;
    
    private TeamLoader loader;
    
    public AgentConnector(String[] args) {
        this.init(args);
    }
    
    private void init(String[] args) {
        //register rescue system
        Registry.SYSTEM_REGISTRY.registerEntityFactory(StandardEntityFactory.INSTANCE);
        Registry.SYSTEM_REGISTRY.registerMessageFactory(StandardMessageFactory.INSTANCE);
        Registry.SYSTEM_REGISTRY.registerPropertyFactory(StandardPropertyFactory.INSTANCE);
        //init config
        this.config = ConfigInitializer.getConfig(args);
        //load team jar
		this.loader = new TeamLoader(new File(System.getProperty("user.dir"), "tactics"));
    }
    
    public void start() {
        String host = this.config.getValue(Constants.KERNEL_HOST_NAME_KEY, Constants.DEFAULT_KERNEL_HOST_NAME);
        int port = this.config.getIntValue(Constants.KERNEL_PORT_NUMBER_KEY, Constants.DEFAULT_KERNEL_PORT_NUMBER);
        ComponentLauncher cl = new TCPComponentLauncher(host, port, this.config);
        System.out.println("[START] Connect Server (host:" + host + ", port:" + port + ")");
        this.connectAmbulance(cl);
        this.connectFire(cl);
        this.connectPolice(cl);
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
		System.out.println("[START] Connect Fire (teamName:" + name + ")");
		System.out.println("[INFO ] Load Fire Team (teamName:" + name + ")");
		Team team = this.loader.get(name);
		if(team == null) {
			System.out.println("[ERROR] Team is Null !!");
			if(TeamLoader.KEYWORD_RANDOM.equalsIgnoreCase(name)) {
				for (int i = 0; i < this.config.getIntValue(ConfigKey.KEY_LOAD_RETRY, 10); i++) {
					System.out.println("[INFO ] Retry Load Team (teamName:" + name + ")");
					team = this.loader.getRandomTeam();
					if(team != null) {
						break;
					}
				}
			}
			if(team == null) {
				if (this.config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
					System.out.println("[INFO ] Load Dummy System");
					team = this.loader.getDummy();
				} else {
					System.out.println("[ERROR] Cannot Load Team !!");
					System.out.println("[END  ] Connect Fire (success:0)");
					return;
				}
			}
		}
		if(team.getFireBrigadeTactics() == null) {
			System.out.println("[ERROR] Cannot Load Fire Brigade Tactics !!");
			if(TeamLoader.KEYWORD_RANDOM.equalsIgnoreCase(name)) {
				for (int i = 0; i < this.config.getIntValue(ConfigKey.KEY_LOAD_RETRY, 10); i++) {
					System.out.println("[INFO ] Retry Load Team (teamName:" + name + ")");
					team = this.loader.getRandomTeam();
					if(team.getFireBrigadeTactics() != null) {
						break;
					}
				}
			}
			if(team.getFireBrigadeTactics() == null) {
				if (this.config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
					System.out.println("[INFO ] Load Dummy System");
					team = this.loader.getDummy();
				} else {
					System.out.println("[END  ] Connect Fire (success:0)");
					return;
				}
			}
		}
		System.out.println("[INFO ] Fire Brigade Tactics (teamName:" + team.getTeamName() + ")");
		name = "[INFO ] Connect FireBrigadeAgent (teamName:" + team.getTeamName() + ")";
		int connectAgent = 0;
		try {
			for (int i = 0; i != count; ++i) {
				cl.connect(new FireBrigadeAgent(team.getFireBrigadeTactics()));
				System.out.println(name);
				connectAgent++;
			}
		} catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
		}
		System.out.println("[END  ] Connect Fire (success:" + connectAgent + ")");
	}

	private void connectPolice(ComponentLauncher cl) {
		this.connectPolice(cl, this.config.getValue(ConfigKey.KEY_POLICE_NAME), this.config.getIntValue(ConfigKey.KEY_POLICE_COUNT));
	}

	private void connectPolice(ComponentLauncher cl, String name, int count) {
		Connect connect = new ConnectPoliceAgent();
		connect.connect(cl, this.config, this.loader, name, count);
	}
}
