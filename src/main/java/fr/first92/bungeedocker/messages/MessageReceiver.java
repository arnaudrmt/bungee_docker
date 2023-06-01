package fr.first92.bungeedocker.messages;

import fr.first92.bungeedocker.BungeeDocker;
import fr.first92.bungeedocker.container.ContainerCollector;
import fr.first92.bungeedocker.container.ContainerCreator;
import fr.first92.bungeedocker.container.ContainerDeletor;
import fr.first92.bungeedocker.filemanager.FileHandler;
import fr.first92.bungeedocker.server.ServerLinker;
import fr.first92.commons.ServerManager;
import fr.first92.sync.data.providers.ServerProvider;
import fr.first92.sync.data.redis.redisaccess.RedisAccess;
import org.redisson.api.RedissonClient;

import java.util.List;

public class MessageReceiver {

    RedissonClient redissonClient = RedisAccess.instance.getRedissonClient();

    public void subscribe() {

        redissonClient.getTopic("bungee").addListener((s, o) -> {

            List<String> l = (List<String>) o;

            if(l.get(0).equalsIgnoreCase("docker")) {

                if(l.get(1).equalsIgnoreCase("server")) {

                    if(l.size() == 3) {

                        if(l.get(2).equalsIgnoreCase("register_open")) {

                            new ContainerCollector().collectServers();
                        }
                    }else {

                        String player = l.get(3);
                        String type = l.get(2);

                        if(!BungeeDocker.getInstance().getWaitingOnServer().contains(player))
                            new ServerLinker().connect(type, player);
                    }
                }

                if(l.get(1).equalsIgnoreCase("container")) {

                    String name = l.get(3);

                    if(l.get(2).equalsIgnoreCase("create")) {

                        if (new FileHandler().fileExist(name)) {

                            new ContainerCreator(name).createContainer();

                        } else System.out.println("This type of server does not exist");
                    }

                    if(l.get(2).equalsIgnoreCase("delete")) {

                        if(name.equalsIgnoreCase("all")) {

                            List<ServerManager> allServers = new ServerProvider().getAllServers();

                            allServers.forEach(rs -> new ContainerDeletor().deleteServer(rs.getName()));

                        } else if(new ServerProvider(name).getServerFromRedis() != null) {

                            new ContainerDeletor().deleteServer(name);
                        }
                    }
                }
            }
        });
    }
}
