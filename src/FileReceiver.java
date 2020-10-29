import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class FileReceiver implements Runnable {
    public int port = 6013;

    public FileReceiver(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port); // socket attached to my computer's port
            byte[] buffer = new byte[65507]; // buffer with max size of 65507
            socket.setSoTimeout(3000); // times out and closes if nothing is returned, will also break while loop

            DatagramPacket blank = new DatagramPacket(buffer, 0, port);
            socket.send(blank); //sends a blank datagram packet to the server to establish connection

            byte[] buffer2 = new byte[65507]; // buffer with max size of 65507, can accept things up to that size
            while(true){
                DatagramPacket data = new DatagramPacket(buffer2,0,buffer.length);
                socket.receive(data);

                byte[] aPacket = data.getData(); //a packet returned as an array of bytes
                
            }

        } catch (SocketException e) {
            e.printStackTrace(); // add other message here
        } catch (IOException e) { //exception for the send statement 
            e.printStackTrace(); //add other message here
        }

    }
}


