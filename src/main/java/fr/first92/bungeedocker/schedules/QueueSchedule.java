package fr.first92.bungeedocker.schedules;

import fr.first92.bungeedocker.BungeeDocker;
import fr.first92.bungeedocker.server.ServerLinker;
import fr.first92.commons.ServerManager;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class QueueSchedule {

    int timer;
    ServerManager serverManager;
    String p;
    String type;

    public QueueSchedule(int timer, ServerManager serverManager, String p, String type) {
        this.timer = timer;
        this.serverManager = serverManager;
        this.p = p;
        this.type = type;

        task.getTask().run();
    }

    ScheduledTask task = BungeeDocker.getInstance().getProxy().getScheduler().schedule(BungeeDocker.getInstance(), new Runnable() {
        @Override
        public void run() {

            if(serverManager != null) System.out.println(serverManager.getStatus());

            if(timer == 0 || (serverManager != null && serverManager.getStatus().equalsIgnoreCase("started"))) {

                new ServerLinker().connect(type, p);
                task.cancel();
            }

            timer --;
        }
    }, 1, 1,TimeUnit.SECONDS);
}
