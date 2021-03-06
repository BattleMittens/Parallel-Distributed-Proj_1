import java.io.*;
import java.net.*;

    public class TCPClient {
       public static void main(String[] args) throws IOException
       {
  	
		// Variables for setting up connection and communication
	    Socket Socket = null; // socket to connect with ServerRouter
	    PrintWriter out = null; // for writing to ServerRouter
	    BufferedReader in = null; // for reading form ServerRouter
		InetAddress addr = InetAddress.getLocalHost();
		
		//this line gives hostName/hostIP, which can be used for setup of
		//router name and client address (when used locally)
		String host = addr.getHostAddress(); // Server machine's IP
		String[] iNetHost = new String[2];
		
		System.out.println("Host: " + addr);

		iNetHost = addr.toString().split("/");
					
		System.out.println("Currently the serverhost is " + iNetHost[0]);
		System.out.println("Currently the serverip is " + iNetHost[1]);
		
		String routerName = iNetHost[0]; // ServerRouter host name
		
		//String host = addr.getHostAddress(); // Client machine's IP
		//String routerName = "j263-08.cse1.spsu.edu"; // ServerRouter host name
		int SockNum = 5555; // port number
		
		// Tries to connect to the ServerRouter
         try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
         } 
             catch (UnknownHostException e) {
               System.err.println("Don't know about router: " + routerName);
               System.exit(1);
            } 
             catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + routerName);
               System.exit(1);
            }
         
         //handles the FILE now, if not created will make an empty one
         File f1 = new File("file.txt");
         f1.createNewFile();
         Reader reader = new FileReader(f1);
        	 
		 BufferedReader fromFile =  new BufferedReader(reader); // reader for the string file
				 
         String fromServer; // messages received from ServerRouter
         String fromUser; // messages sent to ServerRouter
		 String address = iNetHost[1]; // destination IP (Server)
		 long t0, t1, t;
			
			// Communication process (initial sends/receives
			out.println(address);// initial send (IP of the destination Server)
			fromServer = in.readLine();//initial receive from router (verification of connection)
			System.out.println("ServerRouter: " + fromServer);
			out.println(address); // Client sends the IP of its machine as initial send
			t0 = System.currentTimeMillis();
      	
			// Communication while loop
         while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
				t1 = System.currentTimeMillis();
            if (fromServer.equals("Bye.")) // exit statement
               break;
				t = t1 - t0;
				System.out.println("Cycle time: " + t);
          
            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
               System.out.println("Client: " + fromUser);
               out.println(fromUser); // sending the strings to the Server via ServerRouter
					t0 = System.currentTimeMillis();
            }
         }
			// closing connections
         out.close();
         in.close();
         Socket.close();
      }
   }
