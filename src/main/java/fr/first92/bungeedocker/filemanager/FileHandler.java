package fr.first92.bungeedocker.filemanager;

import fr.first92.bungeedocker.container.ContainerCreator;

import java.io.File;

public class FileHandler {

    private String getPath() {

        return "/home/octana/network/models/" + ContainerCreator.getInstance().getModel() + "/server.properties";
    }

    private String getPath(String model) {

        return "/home/octana/network/models/" + model + "/server.properties";
    }

    public boolean fileExist(String model) {

        File f = new File(getPath(model));

        return (f.exists() && !f.isDirectory());
    }
}
