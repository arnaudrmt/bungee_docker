package fr.first92.bungeedocker.ports;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import fr.first92.bungeedocker.container.ContainerCreator;

import java.io.IOException;
import java.net.Socket;

public class PortHandler {

    private Integer port;
    Ports portBindings = new Ports();

    public PortHandler() {

        this.port = getFreePort();

        ContainerCreator.getInstance().setName("octana_" + ContainerCreator.getInstance().getModel() + "_" + this.port);
        ContainerCreator.getInstance().setPort(port);
    }

    public ExposedPort retrieveTcpServer() {

        Integer port = getFreePort();

        ExposedPort tcpServer = ExposedPort.tcp(25565);
        portBindings.bind(tcpServer, Ports.Binding.bindPort(port));

        return tcpServer;
    }

    public Ports getPortBindings() {
        return portBindings;
    }

    public Integer getFreePort() {

        for (int port = 10000; port < 65536; port++) {

            Socket tempSocket = new Socket();

            try {
                tempSocket = new Socket("localhost", port);
            } catch (IOException e) {
                return port;
            } finally {

                try {
                    tempSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
