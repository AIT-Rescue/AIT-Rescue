package adk.launcher;

import adk.launcher.dummy.DummyTeam;
import adk.team.Team;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class TeamLoader {

    private Map<String, Team> teamMap;
    private List<String> nameList;

    private Random random;

    private Team dummy;
    
    public TeamLoader(File dir) {
        this.teamMap = new HashMap<>();
        this.nameList = new ArrayList<>();
        this.random = new Random((new Date()).getTime());
        this.dummy = new DummyTeam();
        this.load(dir);
    }

    public Team get(String name) {
        return "random".equals(name) ? this.getRandomTeam() : this.getTeam(name);
    }

    public Team getTeam(String name) {
        Team team = this.teamMap.get(name);
        return team == null ? this.getRandomTeam() : team;
    }

    public Team getDummy() {
        return this.dummy;
    }

    public Team getRandomTeam() {
        return this.teamMap.get(this.nameList.get(this.random.nextInt(this.nameList.size())));
    }

    private void load(File file) {
        if (!file.exists()) {
            if(file.mkdir()) {
                this.addDummyTeam();
                return;
            }
            else {
                System.out.println("Directory Error");
                this.addDummyTeam();
                return;
            }
        }

        URLClassLoader loader = (URLClassLoader) this.getClass().getClassLoader();
        List<String> list = new ArrayList<>();
        this.loadJar(file, loader, list);
        this.loadTeam(loader, list);
        if(this.nameList.isEmpty()) {
            this.addDummyTeam();
        }
    }

    private void addDummyTeam() {
       // Team dummy = new DummyTeam();
        String name = dummy.getTeamName();
        this.nameList.add(name);
        this.teamMap.put(name, dummy);
    }

    private void loadJar(File file, URLClassLoader loader, List<String> list) {
        if(file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                this.loadJar(file1, loader, list);
            }
        }
        else if (file.getName().endsWith(".jar")) {
            System.out.println("Found Jar : " + file.getName());
            try {
                //add url
                //Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
                Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                //method.invoke(loader, new Object[]{file.toURI().toURL()});
                method.invoke(loader, file.toURI().toURL());
                //load target class name
                JarFile jar = new JarFile(file);
                Manifest manifest = jar.getManifest();
                Attributes attributes = manifest.getMainAttributes();
                String target = attributes.getValue("Team-Class");
                if(target != null) {
                    System.out.println("Found Target Class : " + target);
                    list.add(target);
                }
            } catch (NoSuchMethodException | IOException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadTeam(URLClassLoader loader, List<String> list) {
        for (String target : list) {
            try {
                Class teamClass = loader.loadClass(target);
                Object obj = teamClass.newInstance();
                if (obj instanceof Team) {
                    Team team = (Team) obj;
                    String name = team.getTeamName();
                    System.out.println("Load Success : " + name);
                    this.nameList.add(name);
                    this.teamMap.put(name, team);
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) { //loadClass
                e.printStackTrace();
            }
        }
    }
}
