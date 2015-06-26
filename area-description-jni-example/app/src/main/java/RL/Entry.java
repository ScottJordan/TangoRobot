package RL;

/**
 * Created by scott on 6/22/15.
 */
public class Entry {
    private Position pos;
    private Orientation ori;
    private String status;
    private int frameCount;
    private long deltaTime;

    public Entry(Position pos, Orientation ori, String status, int frameCount, long deltaTime) {
        this.pos = pos;
        this.ori = ori;
        this.status = status;
        this.frameCount = frameCount;
        this.deltaTime = deltaTime;
    }
    public Entry(String all, long deltaTime) throws Exception {
        String idv[] = all.split(",");
        if(idv[0].contentEquals("N/A")){
            throw new Exception("NoHandleException");
        }
        this.status = idv[0];
        this.frameCount = Integer.parseInt(idv[1]);
        this.deltaTime = deltaTime;
        this.pos = new Position(Float.parseFloat(idv[3]), //x
                                Float.parseFloat(idv[4]), //y
                                Float.parseFloat(idv[5]));//z
        this.ori = new Orientation( Float.parseFloat(idv[6]), //x
                                    Float.parseFloat(idv[7]), //y
                                    Float.parseFloat(idv[8]), //z
                                    Float.parseFloat(idv[9]));//w
    }

    @Override
    public String toString() {
        return status + "," +
                frameCount + "," +
                deltaTime + "," +
                pos.toString() + "," +
                ori.toString() + "\n";
    }
    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Orientation getOri() {
        return ori;
    }

    public void setOri(Orientation ori) {
        this.ori = ori;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public long getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(long deltaTime) {
        this.deltaTime = deltaTime;
    }
}
