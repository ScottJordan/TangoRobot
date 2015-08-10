package robot.ai.remotevideocontrol.comm;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Created by scott on 7/11/15.
 */
public class ServerCommunicator {
    private static final int bufferSize = 1024;

    private String address;
    private int port;
    private Socket socket;
    private String response;
    private OutputStream outputStream;
    private InputStream inputStream;
    private byte[] outBuffer = new byte[bufferSize];
    private byte[] inBuffer = new byte[bufferSize];

    public ServerCommunicator(String address, int port) {
        this.address = address;
        this.port = port;
        socket = new Socket();

    }

    public void connect() throws IOException {
        socket = new Socket(address, port);
        socket.setReceiveBufferSize(bufferSize);
        socket.setSendBufferSize(bufferSize);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void closeConnection() {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connected() {
        if (socket.isClosed() || socket.isInputShutdown() || socket.isOutputShutdown()) {
            return false;
        } else {
            return true;
        }
    }

    public void sendMessage(String message) throws IOException {
        outBuffer = message.getBytes();
//            outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.write(outBuffer);//writeUTF(message);
        outputStream.flush();
    }

    public String readMessage() throws IOException {
        String message = "";
        //inputStream.read blocks if no is data returned
//            inputStream = new DataInputStream(socket.getInputStream());
        //int bytesRead = inputStream.read(inBuffer);//readUTF();
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
        message = input.readLine();
//            int bytesRead = inputStream.read(inBuffer, 0, inBuffer.length);
//        Log.v("SocketCom", "read " + bytesRead + " bytes from server");
//        message = String.valueOf(inBuffer);

        return message;
    }

    public boolean starting() throws IOException {
        sendMessage("Start?");
        String reply = readMessage();
        if (reply.compareTo("Start") == 0) {
            return true;
        } else
            return false;
    }

    public void sendImage(Bitmap image) throws IOException {
        Log.v("Socket", "Bitmap Byte count: " + image.getByteCount());
        long start = System.currentTimeMillis();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        long end = System.currentTimeMillis();
        Log.v("Socket", "Bitmap Compress time " + (end-start));
        start = System.currentTimeMillis();
        outputStream.flush();
        end = System.currentTimeMillis();
        Log.v("Socket", "Buffer flush time " + (end-start));
        start = System.currentTimeMillis();
        closeConnection();
        connect();
        end = System.currentTimeMillis();
        Log.v("Socket", "Connection Close and Open Time " + (end-start));

//        ByteBuffer imageBuffer = ByteBuffer.allocate(image.getByteCount());
//        image.copyPixelsToBuffer(imageBuffer);
//        byte buff[] = imageBuffer.array();
//        Log.v("Socket", "Image buffer size: " + buff.length);
//        sendMessage("" + buff.length);
//        readMessage();
//        outputStream.write(buff);//writeUTF(message);
//        outputStream.flush();
    }

}
