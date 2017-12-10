import java.io.FileInputStream;
 //import java.net.DatagramPacket;
 //import java.net.DatagramSocket;
 //import java.io.DataOutputStream;
 import java.io.IOException;

 import javax.swing.JOptionPane;

 public class DcClient {
    static final String endMessage = ".";
    private String hostName = "localhost";
    private String portNum = "7";
    public String lastMessage = new String("No Files Found");
    
    public void sendMessage(String message) {
 	   try {
 		   DcClientHelper helper = new DcClientHelper(hostName, portNum);
 	       String echo;
 	       if ((message.trim()).equals (endMessage)){
 	           helper.done( );
 	       } else {
 	    	   echo = helper.getMessage( message);
 	           parse(echo);
 	       }
 	   } catch (Exception ex) {
 		   ex.printStackTrace( );
 	   } // end catch
    }
    
    public byte[] fileToByteArray(String filePath, long fileSize) throws IOException {
 	   byte byteArray[]=new byte[(int) fileSize];	// make byte array that matches file size
        FileInputStream fileStream=new FileInputStream(filePath);
        int i=0;
        while(fileStream.available()!=0) {
     	   byteArray[i]=(byte)fileStream.read();
            i++;
        }                     
        fileStream.close();

        return byteArray;
    }
    
    private void parse(String message)  {
 	   // Get the first 3 symbols and perform the relevant action
 	   String code = message.substring(0, 3);	// code part
 	   String argument = message.substring(3).trim();	// rest of the message
 	   switch(code) {
	   	case "201":
 	   	case "202":
 	   	case "203":
 	   	case "204":
 	   	case "401":
 	   	case "402":
 	   	case "403":
 	   		JOptionPane.showMessageDialog(null, message);
 	   		break;
 	   	case "404":
 	   		JOptionPane.showMessageDialog(null, "404 File List");
 	   		lastMessage = argument;
 	   		break;
 	   	case "405":
 	   		JOptionPane.showMessageDialog(null, "405 Download File");
 	   		lastMessage = argument;
 	   		break;
 	   		
 	   	}
    }
    
    //byte[] bytearray = new byte[20000];
    //FileOuputStream fos = new FileOutputStream("C:\\DsFiles");

 } // end class