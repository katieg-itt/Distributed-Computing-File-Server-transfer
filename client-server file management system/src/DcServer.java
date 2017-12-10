import java.io.*;
import java.lang.StringBuilder;

public class DcServer {
   private static MyServerDatagramSocket mySocket;
   private static String returnMessage;
   private static String currentUser;
   private static String nextFileName;
   
   public static void main(String[] args) {
	   try {
		   mySocket = new MyServerDatagramSocket(7);
		   System.out.println("File Transfer server ready."); 
		   
		   while (true) {  // forever loop
			   DatagramMessage request = mySocket.receiveMessageAndSender();
			   parse(request.getMessage());
			   mySocket.sendMessage(request.getAddress( ), request.getPort( ), returnMessage);
			   //System.out.println(outputMessage);
		   } //end while
	   } catch (Exception ex) {
		   ex.printStackTrace( );
	   } // end catch
   } //end main
   
   public static void parse(String message)  {
	   // Get the first 3 symbols and perform the relevant action
	   String code = message.substring(0, 3);	// code part
	   message = message.substring(3).trim();	// rest of the message
	   switch(code) {
	   	case "301":
	   		login(message);
	   		break;
	   	case "302":
	   		logout(message);
	   		break;
	   	case "501":
	   		setFileName(message);
	   		break;
	   	case "504":
	   		try {
	   			listFiles(message);
	   		} catch (IOException e) {
	   			e.printStackTrace();
	   		}
	   		break;
	   	case "502":
	   		try {
	   			upload(message);
	   		} catch (IOException e) {
	   			e.printStackTrace();
	   		}
	   		break;
	   	case "503":
			try {
				download(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
	   		break;
	   	}
   }
   // 504 List Files
   public static void listFiles(String inputMessage) throws IOException {
	   
	   	File userDir = new File("C:\\DsFiles\\"+currentUser);
	   
	   	File[] listOfFiles = userDir.listFiles();
	   	StringBuilder filesString = new StringBuilder();

		for (int i = 0; i < listOfFiles.length; i++) {
			filesString.append(listOfFiles[i].getName());
		    if (i != listOfFiles.length - 1) {
		    	filesString.append(";");
		    }
		}
		String files = filesString.toString();
		if(files.length() == 0) {
			files = "No Files Found";
		}
		returnMessage = "404 "+files;
   }
   	// 503 download
   public static void download(String inputMessage) throws IOException {
	   
	   File newFile = new File("C:\\DsFiles\\" +currentUser+"\\"+inputMessage);

	   try {
		   byte[] fileInBytes = fileToByteArray(newFile.getAbsolutePath(), newFile.length());
		   returnMessage = new String(fileInBytes);
		   returnMessage = "405 "+returnMessage;
		} catch (IOException ex) {
			ex.printStackTrace( );
		}
   }
   private static byte[] fileToByteArray(String filePath, long fileSize) throws IOException {
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
   
   public static void setFileName(String fileName) {
	   System.out.println("Preparing to upload "+fileName);
	   nextFileName = fileName;
	   returnMessage = "406" + " Ready for Upload";
   }
   	//502 upload
   public static void upload(String inputMessage) throws IOException {
	   //do upload here
	   byte[] fileInBytes = inputMessage.getBytes();
	   File newFile = new File("C:\\DsFiles\\"+currentUser+"\\"+nextFileName);
	   newFile.createNewFile();
	   FileOutputStream f=new FileOutputStream("C:/DsFiles/"+ currentUser + "/"+nextFileName);
	   f.write(fileInBytes);
	   f.close();
	   //System.out.print(inputMessage + "File uploaded ");
	   returnMessage = "401" + " Your file has been uploaded to the server";
   }
   	//302 logout
   public static void logout(String inputMessage) {
	   //do logout here
	   System.out.print(inputMessage + " Logged out ");
	   returnMessage = "204 You have sucessfully logged out";
	   currentUser = null;
   }
   	//301 login
   public static void login(String username) {
	   File userDir = new File("C:\\DsFiles\\"+username);
	   //new File("C:\\Directory2").mkdir();
	   if(userDir.mkdirs() ) {
		   // directory did not exist, created
		   System.out.println("Login from new user "+username+" .. creating folder "+userDir.getPath());
		   returnMessage = "203 Login. Welcome "+username;
	   } else {
		   // folder already existed
		   System.out.println("Login from existing user "+username);
		   returnMessage = "203 Login. Welcome back "+username;
	   }
	   currentUser = username;
   }
   
} // end class      

