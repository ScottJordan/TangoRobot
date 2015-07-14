package robot.RL;

import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import robot.Comm.ServerCommunicator;
import robot.Logging.Logger;
import robot.Comm.ArduinoCommunicator;

/**
 * Created by scott on 6/25/15.
 */
public class RLTaskManager implements Runnable {
    private Logger logger;
    private ScheduledExecutorService scheduler;
    private ArduinoCommunicator arduinoCommunicator;
    private ServerCommunicator serverCommunicator;
    private static final long initialDelay = 5000;
    private static final long delayPeriod = 50;
    private int messageTries = 0;
    private boolean stopped = true;

    public RLTaskManager(UsbManager manager) {
        logger = new Logger("episodeLog.txt");
        arduinoCommunicator = new ArduinoCommunicator(manager);
        serverCommunicator = new ServerCommunicator("192.168.0.29", 9999);
        RobotState.getInstance().update();
        scheduler = Executors.newSingleThreadScheduledExecutor();

    }

    public void startScheduler(){
        scheduler.scheduleAtFixedRate(this, initialDelay, delayPeriod, TimeUnit.MILLISECONDS);
    }
    public void stopScheduler(){
        scheduler.shutdown();
        logger.writeLog();
        logger.writeLogEndEpisode();
        serverCommunicator.closeConnection();
        RobotState.getInstance().reset();
    }

    @Override
    public void run() {
        if (messageTries == 0 || serverCommunicator.connected() == false) {
            try {
                Log.v("Socket", "Attempting Connection Number: " + messageTries);
                serverCommunicator.connect();
                Log.v("Socket", "Connection Succeded?");
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("Socket", "Error Trying to connect");
            }
            messageTries++;
        }
        if (messageTries > 3) {
            Log.v("Socket", "connection attempts exceeded " + messageTries);
            stopScheduler();
            messageTries = 0;
        }
        if (stopped) {
            try {
                if (serverCommunicator.starting()) {
                    stopped = false;
                    logger.writeLogStartEpisode();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            RobotState.getInstance().update();
            try {
                Log.v("SocketCom", "sending message to server");
                serverCommunicator.sendMessage(RobotState.getInstance().serverString());

                Log.v("SocketCom", "Reading message from server");
                String response = serverCommunicator.readMessage();
                Log.v("SocketCom", "motor action " + response);
                if (response.compareTo("End") == 0) {
                    arduinoCommunicator.sendMessage("L0R0\n"); //stop motors
                    logger.writeLogEndEpisode();
                    stopped = true;
                    return;
                }
                RobotState.getInstance().setAction(response);
                arduinoCommunicator.sendMessage(response + "\n"); //send motor action
                logger.log(RobotState.getInstance());
                RobotState.getInstance().updateMotors();

            } catch (IOException e) {
                messageTries++;
                e.printStackTrace();
            }

        }
    }
}
