package robot.ai.remotevideocontrol.main;

import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import robot.ai.remotevideocontrol.comm.ArduinoCommunicator;
import robot.ai.remotevideocontrol.comm.ServerCommunicator;

/**
 * Created by scott on 6/25/15.
 */
public class TaskManager implements Runnable {
    private ScheduledExecutorService scheduler;
    private ArduinoCommunicator arduinoCommunicator;
    private ServerCommunicator serverCommunicator;
    private ImageProcessor imageProcessor;
    private static final long initialDelay = 5000;
    private static final long delayPeriod = 5;
    private int messageTries = 0;
    private boolean stopped = true;


    public TaskManager(UsbManager manager) {
        arduinoCommunicator = new ArduinoCommunicator(manager);
        serverCommunicator = new ServerCommunicator("192.168.0.29", 9999);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        imageProcessor = new ImageProcessor();
        imageProcessor.setupCamera();
    }

    public void startScheduler(){
        scheduler.scheduleAtFixedRate(this, initialDelay, delayPeriod, TimeUnit.MILLISECONDS);
    }
    public void stopScheduler(){
        imageProcessor.setmDoProcess(false);
        scheduler.shutdown();
        serverCommunicator.closeConnection();
        arduinoCommunicator.sendMessage("L0R0\n");
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                long start = System.currentTimeMillis();
                imageProcessor.updateFrame();
                long end = System.currentTimeMillis();
                Log.v("Cycle", "Update frame time " + (end - start));
                Log.v("SocketCom", "sending message to server");
                start = System.currentTimeMillis();
                serverCommunicator.sendImage(imageProcessor.getmCurrentBitmap());
                end = System.currentTimeMillis();
                Log.v("Cycle", "Send Image time " + (end-start));
                Log.v("SocketCom", "Reading message from server");
                start = System.currentTimeMillis();
                String response = serverCommunicator.readMessage();
                end = System.currentTimeMillis();
                Log.v("Cycle", "Time to get motor command " + (end - start));
                Log.v("SocketCom", "motor action " + response);
                if (response.compareTo("End") == 0) {
                    arduinoCommunicator.sendMessage("L0R0\n"); //stop motors
                    stopped = true;
                    return;
                }
                arduinoCommunicator.sendMessage(response + "\n"); //send motor action

            } catch (IOException e) {
                messageTries++;
                e.printStackTrace();
            }

        }
    }
}
