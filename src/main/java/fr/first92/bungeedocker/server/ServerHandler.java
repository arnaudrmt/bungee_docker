package fr.first92.bungeedocker.server;

import fr.first92.bungeedocker.BungeeDocker;
import fr.first92.bungeedocker.container.ContainerCreator;
import fr.first92.commons.ServerManager;
import fr.first92.sync.data.providers.ServerProvider;
import fr.first92.sync.data.providers.ServerTypeProvider;
import fr.first92.sync.data.redis.messages.RedisMessageSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class ServerHandler {

    private ServerInfo createServerInfo() {

        InetSocketAddress socketAddress = new InetSocketAddress("localhost", ContainerCreator.getInstance().getPort());

        return ProxyServer.getInstance().constructServerInfo(ContainerCreator.getInstance().getName(),
                socketAddress, "Peu-Importe", false);
    }

    public void registerServer() {

        ServerProvider serverProvider = new ServerProvider(ContainerCreator.getInstance().getName());

        ServerManager serverManager = new ServerManager(ContainerCreator.getInstance().getName(),
                ContainerCreator.getInstance().getName().split("_")[1],
                ContainerCreator.getInstance().getPort(),
                "starting",
                0,
                new ServerTypeProvider(ContainerCreator.getInstance().getModel()).getServerType().getMaxParty(),
                new ServerTypeProvider(ContainerCreator.getInstance().getModel()).getServerType().getMaxPlayers());

        serverProvider.sendServerToRedis(serverManager);

        new RedisMessageSender().sendToBungee(Arrays.asList("api", "server", "register",
                ContainerCreator.getInstance().getName(), String.valueOf(ContainerCreator.getInstance().getPort())));
    }

    public void registerOpenServer(String name, Integer port) {

        ServerProvider serverProvider = new ServerProvider(name);
        ServerManager serverManager = new ServerManager(name,
                name.split("_")[1], port,
                "starting", 0,
                new ServerTypeProvider(ContainerCreator.getInstance().getName()).getServerType().getMaxParty(),
                new ServerTypeProvider(ContainerCreator.getInstance().getName()).getServerType().getMaxPlayers());

        new RedisMessageSender().sendToBungee(Arrays.asList("api", "register", name, String.valueOf(port)));

        serverProvider.sendServerToRedis(serverManager);
    }

    public void unregisterServer(String name) {

        ServerProvider serverProvider = new ServerProvider(name);
        ServerManager serverManager = serverProvider.getServerFromRedis();

        BungeeDocker.getInstance().getProxy().getServers().remove(serverManager.getName());
        new RedisMessageSender().sendToBungee(Arrays.asList("api", "server", "unregister", name));

        serverProvider.deleteServerFromRedis();
    }
}
