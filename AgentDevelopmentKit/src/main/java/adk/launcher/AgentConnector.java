package adk.launcher;

import rescuecore2.registry.Registry;
import rescuecore2.standard.entities.StandardEntityFactory;
import rescuecore2.standard.entities.StandardPropertyFactory;
import rescuecore2.standard.messages.StandardMessageFactory;

import adk.launcher.agent.AmbulanceTeamAgent;
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
        this.loader = new TeamLoader(new File(config.getValue(ConfigKey.KEY_DIRECTORY, "."), "tactics"));
    }
    
    public void start() {
        String host = this.config.getValue(Constants.KERNEL_HOST_NAME_KEY, Constants.DEFAULT_KERNEL_HOST_NAME);
        int port = this.config.getIntValue(Constants.KERNEL_PORT_NUMBER_KEY, Constants.DEFAULT_KERNEL_PORT_NUMBER);
        ComponentLauncher cl = new TCPComponentLauncher(host, port, this.config);
        System.out.println("Start Connect Server [ host: " + host + " port: " + port + "]");
        
        this.connectAmbulance(cl);
        this.connectFire(cl);
        this.connectPolice(cl);
        System.out.println("Success Connect Server");
    }
    
    private void connectAmbulance(ComponentLauncher cl) {
        this.connectAmbulance(cl, this.config.getValue(ConfigKey.KEY_AMBULANCE_NAME), this.config.getIntValue(ConfigKey.KEY_AMBULANCE_COUNT));
    }
    
    private void connectAmbulance(ComponentLauncher cl, String name, int count) {
		System.out.println("Start Ambulance Connect");
		Team team = this.loader.get(name);
		if(team == null || team.getAmbulanceTeamTactics() == null) {
			if(this.config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
				System.out.println("Error : AmbulanceTactics is Null !!");
				System.out.println("Load  : Dummy System");
				team = this.loader.getDummy();
			}
			else {
				System.out.println("Error : Ambulance Connect");
				System.out.println("AmbulanceTeamTactics is Null !!");
				System.out.println("End Ambulance Connect");
				return;
			}
		}
        name = "Connect Ambulance (Team Name : " + team.getTeamName() + ")";
        try {
            for (int i = 0; i != count; ++i) {
                cl.connect(new AmbulanceTeamAgent(team.getAmbulanceTeamTactics()));
                System.out.println(name);
            }
        } catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
        }
		System.out.println("Done Ambulance Connect");
    }

	private void connectFire(ComponentLauncher cl) {
		this.connectFire(cl, this.config.getValue(ConfigKey.KEY_FIRE_NAME), this.config.getIntValue(ConfigKey.KEY_FIRE_COUNT));
	}

	private void connectFire(ComponentLauncher cl, String name, int count) {
		System.out.println("Start Fire Connect");
		Team team = this.loader.get(name);
		if(team == null || team.getFireBrigadeTactics() == null) {
			if(this.config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
				System.out.println("Error : FireTactics is Null !!");
				System.out.println("Load  : Dummy System");
				team = this.loader.getDummy();
			}
			else {
				System.out.println("Error : Fire Connect");
				System.out.println("FireTactics is Null !!");
				System.out.println("End Fire Connect");
				return;
			}
		}
		name = "Connect Fire (Team Name : " + team.getTeamName() + ")";
		try {
			for (int i = 0; i != count; ++i) {
				cl.connect(new FireBrigadeAgent(team.getFireBrigadeTactics()));
				System.out.println(name);
			}
		} catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
		}
		System.out.println("Done Fire Connect");
	}

	private void connectPolice(ComponentLauncher cl) {
		this.connectPolice(cl, this.config.getValue(ConfigKey.KEY_POLICE_NAME), this.config.getIntValue(ConfigKey.KEY_POLICE_COUNT));
	}

	private void connectPolice(ComponentLauncher cl, String name, int count) {
		System.out.println("Start Police Connect");
		Team team = this.loader.get(name);
		if(team == null || team.getPoliceForceTactics() == null) {
			if(this.config.getBooleanValue(ConfigKey.KEY_DUMMY_SYSTEM, false)) {
				System.out.println("Error : PoliceTactics is Null !!");
				System.out.println("Load  : Dummy System");
				team = this.loader.getDummy();
			}
			else {
				System.out.println("Error : Police Connect");
				System.out.println("PoliceTactics is Null !!");
				System.out.println("End Police Connect");
				return;
			}
		}
		name = "Connect Police (Team Name : " + team.getTeamName() + ")";
		try {
			for (int i = 0; i != count; ++i) {
				cl.connect(new PoliceForceAgent(team.getPoliceForceTactics()));
				System.out.println(name);
			}
		} catch (ComponentConnectionException | InterruptedException | ConnectionException ignored) {
		}
		System.out.println("Done Police Connect");
	}
}
