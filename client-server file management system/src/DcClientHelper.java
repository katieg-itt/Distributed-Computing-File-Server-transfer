import java.net.*;
import java.io.*;

/**
 * This class is a module which provides the application logic
 * for an Echo client using connectionless datagram socket.
 * @author M. L. Liu
 */
public class DcClientHelper {
   private MyClientDatagramSocket mySocket;
   private InetAddress serverHost;
   private int serverPort;

   DcClientHelper(String hostName, String portNum) 
      throws SocketException, UnknownHostException { 
  	   this.serverHost = InetAddress.getByName(hostName);
  		this.serverPort = Integer.parseInt(portNum);
      // instantiates a datagram socket for both sending
      // and receiving data
   	this.mySocket = new MyClientDatagramSocket(); 
   } 
	
   public String getMessage( String message) 
      throws SocketException, IOException {                                                                                 
      String mess = "";    
      mySocket.sendMessage( serverHost, serverPort, message);
	   // now receive the echo
      mess = mySocket.receiveMessage();
      return mess;
   } //end getMess

   public void done( ) throws SocketException {
      mySocket.close( );
   }  //end done

} //end class

