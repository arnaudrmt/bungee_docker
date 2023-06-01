package fr.first92.bungeedocker.server;

import fr.first92.bungeedocker.BungeeDocker;
import fr.first92.bungeedocker.container.ContainerCreator;
import fr.first92.bungeedocker.schedules.QueueSchedule;
import fr.first92.commons.ServerManager;
import fr.first92.sync.data.providers.ServerProvider;
import fr.first92.sync.data.redis.messages.RedisMessageSender;

import java.util.Arrays;
import java.util.List;

public class ServerLinker {

    public void connect(String type, String p) {

        if(!BungeeDocker.getInstance().getWaitingOnServer().contains(p))
            BungeeDocker.getInstance().getWaitingOnServer().add(p);

        List<ServerManager> servers = new ServerProvider().getAllServers();

        if(servers.stream().anyMatch(rs -> rs.getType().equalsIgnoreCase(type) &&
                new ServerProvider(rs.getName()).getServerFromRedis().getPlayers() <
                        new ServerProvider(rs.getName()).getServerFromRedis().getMaxPlayers())) {

            ServerManager serverManager = servers.stream().filter(rs -> rs.getType().equalsIgnoreCase(type) &&
                    new ServerProvider(rs.getName()).getServerFromRedis().getPlayers() <
                            new ServerProvider(rs.getName()).getServerFromRedis().getMaxPlayers()).findFirst().get();

            if(serverManager.getStatus().equalsIgnoreCase("started")) {

                new RedisMessageSender().sendToBungee(Arrays.asList("api", "server", "connect", serverManager.getName(), p));

                BungeeDocker.getInstance().getWaitingOnServer().remove(p);
            }

            if(serverManager.getStatus().equalsIgnoreCase("loading") ||
                    serverManager.getStatus().equalsIgnoreCase("starting")) {

                new QueueSchedule(3, serverManager, p, type);
            }

        } else {

            new ContainerCreator(type).createContainer();
            new QueueSchedule(3, null, p, type);
        }
    }
}
