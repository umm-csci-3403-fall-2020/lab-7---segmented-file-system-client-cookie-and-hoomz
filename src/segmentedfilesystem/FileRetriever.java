package segmentedfilesystem;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class FileRetriever {
    int port;
    String server;
    DatagramSocket socket;

    public FileRetriever(String server, int port) throws UnknownHostException, SocketException {
        this.server = server;
        this.port = port;
        DatagramSocket socket = new DatagramSocket(); //calling FileRetriever will create a new socket
        this.socket = socket;
        //this.socket = new DatagramSocket();
    }

    public void downloadFiles() throws IOException{ //download and write out the files
        InetAddress address = InetAddress.getByName(server); //grabbing the server
        byte[] buffer = new byte[0]; // buffer with size of 0

        DatagramPacket blank = new DatagramPacket(buffer, buffer.length, address, port); //starting connection
        socket.send(blank); //sends a blank datagram packet to the server to establish connection
        System.out.println("Conversation started");
        PacketManager boss = new PacketManager();

        while(!boss.theEnd()){ //need method to end loop when all files are received
            byte[] buffer2 = new byte[1028];
            DatagramPacket data = new DatagramPacket(buffer2,buffer2.length);
            socket.receive(data); //receive the incoming packet
            System.out.println("Data received");
            byte[] aPacket = data.getData();
            int length = data.getLength();
            boss.createPack(aPacket, length);
            //aPacket = null;
        }
        System.out.println("The files have been returned");
        socket.close();
    }

}

//.\run_client.sh csci-4409.morris.umn.edu


