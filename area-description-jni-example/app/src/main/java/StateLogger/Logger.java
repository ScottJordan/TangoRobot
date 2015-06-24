package StateLogger;

/**
 * Created by scott on 6/22/15.
 */


import android.content.Context;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Logger {
    private String file;
    private ArrayList<Entry> log;

    public Logger(String filename){
        this.file = filename;
        this.log = new ArrayList<Entry>();
    }
    public void addEntry(Entry ent){
        log.add(ent);
    }
    public void addEntry(String posString) throws Exception {
        try {
            Entry ent = new Entry(posString);
            log.add(ent);
        } catch (Exception e) {
            if(e.getMessage().contentEquals("NoHandleException")){
                throw e;
            }
            e.printStackTrace();
        }

    }
    public void writeLog(FileWriter logFileWriter) throws IOException {
        if(log.size() <= 0)
            return;
        for(Entry ent: log) {
            logFileWriter.write(ent.toString());
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
}
