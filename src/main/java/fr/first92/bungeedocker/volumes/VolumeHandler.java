package fr.first92.bungeedocker.volumes;

import com.github.dockerjava.api.model.Bind;
import fr.first92.bungeedocker.container.ContainerCreator;

import java.io.File;
import java.util.Random;

public class VolumeHandler {

    public Bind getWorkVolume() {

        return Bind.parse("/home/octana/network/models/" + ContainerCreator.getInstance().getModel() + ":" +
                "/home/octana/network/models/" + ContainerCreator.getInstance().getName());
    }

    public Bind getWorldVolume() {

        File[] files = getWorldDir().listFiles();

        Random rand = new Random();

        File file = files[rand.nextInt(files.length)];

        return Bind.parse(file.getAbsolutePath()
                + ":/home/octana/network/models/" + ContainerCreator.getInstance().getName() + "/world");
    }

    public String getWorkDir() {

        return "/home/octana/network/models/" + ContainerCreator.getInstance().getName() + "/";
    }

    private File getWorldDir() {

        return new File("/home/octana/network/models/maps/" + ContainerCreator.getInstance().getModel());
    }
}