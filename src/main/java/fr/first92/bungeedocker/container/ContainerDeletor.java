package container;

import fr.first92.bungeedocker.server.ServerHandler;
import fr.first92.sync.data.redis.messages.RedisMessageSender;

import java.util.Arrays;

public class ContainerDeletor {

    public void deleteServer(String name) {

        new RedisMessageSender().sendToSpigot(Arrays.asList(name, "stop"));

        new ServerHandler().unregisterServer(name);
    }
}
