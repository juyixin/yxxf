package com.eazytec.webapp.socket;
import org.springframework.stereotype.Component;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by min on 2016/10/24.
 */
@Component
public class ClientSend {
    public  void sendFile(File file) {
        int length = 0;
        byte[] sendByte = null;
        Socket socket = null;
        DataOutputStream dout = null;
        FileInputStream fin = null;
        try {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("192.168.1.101", 33456),10 * 1000);
                dout = new DataOutputStream(socket.getOutputStream());
                fin = new FileInputStream(file);
                sendByte = new byte[1024];
                dout.writeUTF(file.getName());
                while((length = fin.read(sendByte, 0, sendByte.length))>0){
                    dout.write(sendByte,0,length);
                    dout.flush();
                }
            } catch (Exception e) {
            } finally{
                if (dout != null)
                    dout.close();
                if (fin != null)
                    fin.close();
                if (socket != null)
                    socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
