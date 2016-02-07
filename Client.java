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
	
		Socket clientSocket = null;
		for(int port:ports){
			if(port != this.port){
				try{
					Log.out("Client: "+this.name+" send lock request to " + port);
				    clientSocket = new Socket(Util.LOCAL_HOST, port);
					OutputStream outToServer = clientSocket.getOutputStream();
					DataOutputStream out = new DataOutputStream(outToServer);
					String requestMessage = Util.composeRequestMessage(this.name,timeStamp.getTime());
					out.writeUTF(requestMessage);
					InputStream inFromServer = clientSocket.getInputStream();
					DataInputStream in = new DataInputStream(inFromServer);
					String reply = in.readUTF();
					String body =  Util.getContentFromMessage(reply);
					int time = Util.getTimeFromMessage(reply);
					String name = Util.getNameFromMessage(reply);
					timeStamp.setReceivedTime(time,this.name,name,true);
					if(body.equals(Util.REPLY)){

					}
					else{
						Log.out("Node:"+port+" reply wrong message");
					}
					clientSocket.close();
				}catch(Exception e){

				}
			}
		}
		return true;
	}

	public void sendLockReleaseMessage(HashMap<String,Socket> waitingSockets){
		try{	
			while(!waitingQueue.isEmpty()){
				String addr = waitingQueue.poll();	
				Socket socket = waitingSockets.get(addr);
				waitingSockets.remove(addr);
				OutputStream outToClient = socket.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToClient);
				String respondMessage = Util.composeRespondMessage(this.name,timeStamp.getTime());
				out.writeUTF(respondMessage);
			}	
		}catch(Exception e){

		}
	}

}