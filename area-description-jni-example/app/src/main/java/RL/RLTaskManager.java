package RL;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by scott on 6/25/15.
 */
public class RLTaskManager {
    private Logger logger;
    private ScheduledExecutorService scheduler;
    private  static final long delayPeriod = 25;
    public RLTaskManager(){
        logger = new Logger("statelog.txt");
        scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void startScheduler(){
        scheduler.scheduleAtFixedRate(logger, 0, delayPeriod, TimeUnit.MILLISECONDS);
    }
    public void stopScheduler(){
        scheduler.shutdown();
    }

}
