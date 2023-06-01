package container;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.RestartPolicy;
import fr.first92.bungeedocker.BungeeDocker;
import fr.first92.bungeedocker.ports.PortHandler;
import fr.first92.bungeedocker.server.ServerHandler;
import fr.first92.bungeedocker.volumes.VolumeHandler;

public class ContainerCreator {

    private static ContainerCreator instance;

    private String model;
    private String name;
    private String image;

    private Integer port;

    private PortHandler portHandler;
    private VolumeHandler volumeHandler;

    public ContainerCreator(String model) {

        instance = this;

        this.model = model;
        this.image = "octana_" + model;

        this.portHandler = new PortHandler();
        this.volumeHandler = new VolumeHandler();
    }

    public void createContainer() {

        try {

            CreateContainerResponse container = BungeeDocker.getInstance().getDockerClient()
                    .createContainerCmd(image)
                    .withName(name)
                    .withHostConfig(new HostConfig().withAutoRemove(true))
                    .withExposedPorts(portHandler.retrieveTcpServer())
                    .withPortBindings(portHandler.getPortBindings())
                    .withRestartPolicy(RestartPolicy.noRestart())
                    .withWorkingDir(volumeHandler.getWorkDir())
                    .withBinds(volumeHandler.getWorkVolume(), new VolumeHandler().getWorldVolume())
                    .withAttachStdin(true)
                    .withTty(true)
                    .exec();

            BungeeDocker.getInstance().getDockerClient().startContainerCmd(container.getId()).exec();

            new ServerHandler().registerServer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ContainerCreator getInstance() {
        return instance;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
