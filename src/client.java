import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.netInetAddress;
import java.util.Arrays;

public class dataSocket {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName("localhost");
		int port = 6013;
		byte[] buffer = new byte[65507]; //max size of possible things returned
		byte[] buffer2 = new byte[65507];
		socket.setSoTimeout(3000); //times out and closes client if nothing is returned

		DatagramPacket blank = new DatagramPacket(buffer,0,address,port); //sending a blank packet to the server
		socket.send(blank);
		
		while(true){ //the FileRetriever that gets all the files
			DatagramPacket data = new DatagramPacket(buffer2,0,buffer.length);
			socket.receive(data);

			
		}

	}

}
