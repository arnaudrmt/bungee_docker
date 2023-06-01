package container;

import fr.first92.bungeedocker.BungeeDocker;
import fr.first92.commons.ServerManager;
import fr.first92.sync.data.providers.ServerProvider;
import fr.first92.sync.data.redis.messages.RedisMessageSender;

import java.util.Arrays;

public class ContainerCollector {

    public void collectServers() {

        BungeeDocker.getInstance().getDockerClient().listContainersCmd().exec().stream().filter(rs ->
                rs.getImage().contains("octana_")).forEach(rs -> {

            int port = Integer.parseInt(Arrays.stream(rs.getPorts()).findFirst().toString()
                    .split("publicPort=")[1].split(",")[0]);

            String name = rs.getImage() + "_" + port;
            String type = rs.getImage().split("_")[1];
            String status = "started";
            Integer maxParty = 0;

            ServerProvider serverProvider = new ServerProvider(name);
            ServerManager serverManager = new ServerManager(name, type, port, "started", 0, 0, 0);

            serverProvider.sendServerToRedis(serverManager);

            new RedisMessageSender().sendToBungee(Arrays.asList("api", "server", "register", name,
                    String.valueOf(port)));
        });
    }
}
