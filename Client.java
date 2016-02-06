import java.util.Thread;
import java.io.*;
import java.net.*;

public class Client{
	private String name;
	private int port;
	public static String LOCAL_HOST = "localhost";
	public static String REQUEST_MESSAGE = "request lock";
	public static String REPLY = "reply";
	public Client(String id,int port,){
		this.port =port;
		this.name = id;
	}

	public boolean sendRequestMessage(List<Integer> ports){
		Log.out("Client:"+this.port+" send request locking message");
		for(int port:ports){
			try{
				Socket clientSocket = new Socket(LOCAL_HOST, port);
				OutputStream outToServer = clientSocket.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);
				out.writeUTF(REQUEST_MESSAGE);
				InputStream inFromServer = clientSocket.getInputStream();
				DataInputStream in = new DataInputStream(inFromServer);
				String reply = in.readUTF();
				if(reply.equals(REPLY)){

				}
				else{
					Log.out("Node:"+port+" reply wrong message");
				}
			}catch(Exception e){

			}finally{
				clientSocket.close();
				return false
			}
		}
		return true;
	}

}