import java.util.*;
import java.io.*;
import java.net.*;

public class Client{
	private String name;
	private int port;
	private TimeStamp timeStamp;
	private Queue<String> waitingQueue;
	public Client(String id,int port,TimeStamp timeStamp, Queue<String> waitingQueue){
		this.port =port;
		this.name = id;
		this.timeStamp = timeStamp;
		this.waitingQueue = waitingQueue;
	}

	public boolean sendRequestMessage(List<Integer> ports){
		Log.out("Client:"+this.name+" send request locking message");
		for(int port:ports){
			try{
				Socket clientSocket = new Socket(Util.LOCAL_HOST, port);
				OutputStream outToServer = clientSocket.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToServer);
				String requestMessage = Util.composeRequestMessage(this.name,timeStamp.getTime());
				out.writeUTF(requestMessage);
				InputStream inFromServer = clientSocket.getInputStream();
				DataInputStream in = new DataInputStream(inFromServer);
				String reply = in.readUTF();
				String body =  Util.getContentFromMessage(reply);
				String time = Util.getTimeFromMessage(reply);
				timeStamp.setReceivedTime();
				if(body.equals(Util.REPLY)){

				}
				else{
					Log.out("Node:"+port+" reply wrong message");
				}
			}catch(Exception e){

			}finally{
				clientSocket.close();
				return false;
			}
		}
		return true;
	}

	public void sendLockReleaseMessage(HashMap<String,Socket> waitingSockets){
		while(!waitingQueue.isEmpty()){
			String addr = waitingQueue.front();	
			waitingQueue.pop();
			Socket socket = waitingSockets.get(addr);
			waitingSockets.remove(addr);
			OutputStream outToClient = socket.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToClient);
			String respondMessage = Util.composeRespondMessage(this.name,timeStamp.getTime());
			out.writeUTF(respondMessage);
		}	
	}

}