public class main{
	public static void main(String[] args) throws IOException {
        String server = "localhost";
        // CHANGE THIS DEFAULT PORT TO THE PORT NUMBER PROVIDED
        // BY THE INSTRUCTOR.
        int port = 6013;
        
        if (args.length >= 1) {
            server = args[0];
        }
        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

		FileReceiver dog = new FileReceiver(port);
		dog.getFiles();
    }
}


