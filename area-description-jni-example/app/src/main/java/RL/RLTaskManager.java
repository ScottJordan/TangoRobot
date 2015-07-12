package RL;

import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import RL.Logging.Logger;
import USBComm.ArduinoCommunicator;

/**
 * Created by scott on 6/25/15.
 */
public class RLTaskManager implements Runnable {
    private Logger logger;
    private ScheduledExecutorService scheduler;
    private RobotState robotState;
    private RobotBrain robotBrain;
    private ArduinoCommunicator communicator;
    private static final long initialDelay = 5000;
    private static final long delayPeriod = 50;
    private static final String brainFilename = "BrainWeights.nn";
    private static final String predFilename = "PredWeights.nn";

    public RLTaskManager(UsbManager manager) {
        logger = new Logger("episodeLog.txt");
        communicator = new ArduinoCommunicator(manager);
        RobotState.getInstance().update();
        RobotPlayerPerception robotPerception = new RobotPlayerPerception(false, RobotState.getInstance());
        int hiddenNo[] = new int[]{5};
        int hiddenPredNo[] = new int[]{5};
        double alpha = 0.01;
        double lambda = 0.9;
        double gamma = 0.9;
        double random = 0.15;
        double maxWeight = 0.9;
        robotBrain = new RobotBrain(robotPerception, MotorAction.generateCommands(communicator),
                hiddenNo, hiddenPredNo, alpha, lambda, gamma, true, 0.0, random, maxWeight);
        loadBrainNet(brainFilename);
        loadPredNet(predFilename);
        scheduler = Executors.newSingleThreadScheduledExecutor();

    }

    public void startScheduler(){
        scheduler.scheduleAtFixedRate(this, initialDelay, delayPeriod, TimeUnit.MILLISECONDS);
    }
    public void stopScheduler(){
        scheduler.shutdown();
        MotorAction action = new MotorAction(0, 0, communicator);
        action.execute();
        logger.writeLog();
        logger.writeLogEndEpisode();
        robotState.reset();
        saveBrainNet(brainFilename);
        savePredNet(predFilename);
    }

    @Override
    public void run() {
        robotState = RobotState.getInstance();
        robotState.update();
        Log.v("Cycle", "Updated Pose");
        robotBrain.count();
        Log.v("Cycle", "Ran Learning cycle");
        robotState.setAction(robotBrain.getMotorAction());
        logger.log(robotState);
        if (robotState.getLapCounter() >= 3) {
            stopScheduler();
            return;
        }

        robotBrain.executeAction();
        Log.v("Cycle", "Executed Actions");
        robotState.updateMotors();
    }

    private void saveBrainNet(String filename) {
        File root = Environment.getExternalStorageDirectory();
        root = new File(root, "statelogs");
        File brainFile = new File(root, filename);
        try {
            robotBrain.save(brainFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePredNet(String filename) {
        File root = Environment.getExternalStorageDirectory();
        root = new File(root, "statelogs");
        File predFile = new File(root, filename);
        try {
            robotBrain.getRobot().getNn().save(predFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBrainNet(String filename) {
        File root = Environment.getExternalStorageDirectory();
        root = new File(root, "statelogs");
        File brainFile = new File(root, filename);
        if (brainFile.exists()) {
            try {
                robotBrain.load(brainFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPredNet(String filename) {
        File root = Environment.getExternalStorageDirectory();
        root = new File(root, "statelogs");
        File predFile = new File(root, filename);
        if (predFile.exists()) {
            try {
                robotBrain.getRobot().getNn().load(predFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
