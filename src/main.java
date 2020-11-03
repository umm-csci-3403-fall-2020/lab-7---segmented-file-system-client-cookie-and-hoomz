import java.io.IOException;

public class main {
	public static void main(String[] args) throws IOException {
        
        String server = "localhost";

        int port = 6014;
        
        if (args.length >= 1) {
            server = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

		FileReceiver receiver = new FileReceiver(server, port);
		receiver.downloadFiles(); //gets the files which is made up of the packets
    }
}


