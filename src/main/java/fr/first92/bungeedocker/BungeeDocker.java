package fr.first92.bungeedocker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import fr.first92.bungeedocker.messages.MessageReceiver;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public final class BungeeDocker extends Plugin {

    private static BungeeDocker instance;
    private DockerClient dockerClient;

    private List<String> waitingOnServer = new ArrayList<>();

    @Override
    public void onEnable() {

        instance = this;
        dockerClient = DockerClientBuilder.getInstance().build();

        new MessageReceiver().subscribe();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static BungeeDocker getInstance() {
        return instance;
    }

    public DockerClient getDockerClient() {
        return dockerClient;
    }

    public List<String> getWaitingOnServer() {
        return waitingOnServer;
    }
}
