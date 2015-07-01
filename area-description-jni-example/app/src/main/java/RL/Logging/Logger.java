package RL.Logging;

/**
 * Created by scott on 6/22/15.
 */


import android.os.Environment;

import com.projecttango.experiments.nativearealearning.TangoJNINative;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import RL.RobotState;

public class Logger {
    private String file;
    private ArrayList<Entry> log;
    private File logfile;
    private Timestamp previousTimeStamp;
    private Timestamp currentTimeStamp;

    public Logger(String filename){
        this.file = filename;
        File root = Environment.getExternalStorageDirectory();
        root = new File(root, "statelogs");
        logfile = new File(root, file);
        try {
            logfile.setReadable(true);
            logfile.setWritable(true);
            logfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeLogStartEpisode();
        this.log = new ArrayList<Entry>();
        Date date = new Date();
        previousTimeStamp = new Timestamp(date.getTime());
    }

    public void addEntry(RobotState robotState) {

            Date date = new Date();
            currentTimeStamp = new Timestamp(date.getTime());

            long deltaTime = currentTimeStamp.getTime() - previousTimeStamp.getTime();
        Entry ent = new Entry(robotState, deltaTime);
            log.add(ent);

            previousTimeStamp = currentTimeStamp;


    }

    public void writeLogStartEpisode() {
        try {
            FileWriter logFileWriter = new FileWriter(logfile, true);
            logFileWriter.write("Start Episode");
            logFileWriter.flush();
            logFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLogEndEpisode() {
        try {
            FileWriter logFileWriter = new FileWriter(logfile, true);
            logFileWriter.write("End Episode");
            logFileWriter.flush();
            logFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeLog() throws IOException {
        try {
            FileWriter logFileWriter = new FileWriter(logfile, true);
            for(Entry ent: log) {
                logFileWriter.write(ent.toString());
            }
            logFileWriter.flush();
            logFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public ArrayList<Entry> getLog() {
        return log;
    }

    public void setLog(ArrayList<Entry> log) {
        this.log = log;
    }

    public void log(RobotState robotState) {
        addEntry(robotState);
        //write log to file
        if(log.size() >= 1000){
            try {
                writeLog();
                log.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
