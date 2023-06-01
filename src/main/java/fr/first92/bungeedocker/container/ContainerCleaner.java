package container;

import com.github.dockerjava.api.model.Container;
import fr.first92.bungeedocker.BungeeDocker;
import fr.first92.bungeedocker.server.ServerHandler;
import fr.first92.sync.data.providers.ServerProvider;

import java.util.List;
import java.util.stream.Collectors;

public class ContainerCleaner {

    private List<Container> loopThroughServers() {

        return BungeeDocker.getInstance().getDockerClient().listContainersCmd().exec().stream().filter(rs ->
                rs.getImage().contains("octana_")).collect(Collectors.toList());
    }

    public void clean() {

        new ServerProvider().getAllServers().stream().filter(rs -> !loopThroughServers().contains(
                rs.getName())).forEach(rs -> {

            new ServerHandler().unregisterServer(rs.getName());
        });
    }
}
