import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileReceiver {
    public int port = 6014;
    String server;

    public FileReceiver(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void downloadFiles() throws IOException{
        DatagramSocket socket = new DatagramSocket(port); // socket attached to my computer's port
        InetAddress address = InetAddress.getByName(server); //grabbing the server
        byte[] buffer = new byte[65507]; // buffer with max size of 65507
        socket.setSoTimeout(3000); // times out and closes if nothing is returned, will also break while loop

        DatagramPacket blank = new DatagramPacket(buffer, buffer.length, address, port); //starting connection
        socket.send(blank); //sends a blank datagram packet to the server to establish connection

        byte[] buffer2 = new byte[65507]; // buffer with max size of 65507, can accept things up to that size
        PacketManager boss = new PacketManager();

        while(true){
            DatagramPacket data = new DatagramPacket(buffer2,buffer.length);
            socket.receive(data); //receive the incoming packet
            byte[] aPacket = data.getData();
            boss.createPack(aPacket);
        }
    }

}


