import java.util.*;
import java.io.*;
import java.net.*;

public class Server extends Thread{
	String name;
	int port;
	Queue<String> waitingQueue;
	NodeState state;
	TimeStamp timeStamp;
	HashMap<String, Socket> waitingSockets ;
	public Server(String id,int port,NodeState state, TimeStamp timeStamp,Queue<String> waitingQueue,
					HashMap<String,Socket> waitingSockets){
		this.name = id;
		this.port = port;
		this.state = state;
		this.waitingQueue = waitingQueue;
		this.timeStamp = timeStamp;
		this.waitingSockets = waitingSockets;
	}

	@Override
	public void run(){
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(port);
			Log.out("Server:"+port+" has started");
		}catch(Exception e){

		}
		while(true){
			try{
				Socket connected = serverSocket.accept();
				String clientAddr = Util.composeClientAddr(connected);
				InputStream inFromClient = connected.getInputStream();
				DataInputStream in = new DataInputStream(inFromClient);
				String inMessage = in.readUTF();
				String body = Util.getContentFromMessage(inMessage);
				String clientName = Util.getNameFromMessage(inMessage);
				int requestTime = Util.getTimeFromMessage(inMessage);

				Log.out("Node "+this.name+" recevied: Client "+clientName+" send lock request with timeStamp "+requestTime);

				OutputStream outToClient = connected.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToClient);
				if(body.equals(Util.REQUEST_MESSAGE)){
					switch(state){
						case FREE:
							timeStamp.setReceivedTime(requestTime);
							timeStamp.updateTimeStamp();
							String reply = Util.composeRespondMessage(this.name,timeStamp.getTime());
							out.writeUTF(reply);
							connected.close();
							break;
						case HOLD:
							timeStamp.setReceivedTime(requestTime);
							waitingQueue.add(clientAddr);
							waitingSockets.put(clientAddr,connected);
							break;
						case WAIT:
							if(timeStamp.getTime() < requestTime){
								timeStamp.setReceivedTime(requestTime);
								waitingQueue.add(clientAddr);
								waitingSockets.put(clientAddr,connected);
							}
							else if(timeStamp.getTime() == requestTime && this.name.compareTo(clientName)==1){
								waitingQueue.add(clientAddr);
								waitingSockets.put(clientAddr,connected);
							}
							else{
								reply = Util.composeRespondMessage(this.name,timeStamp.getTime());
								out.writeUTF(reply);
								connected.close();
							}
							break;
						default:
							Log.out("Client "+clientAddr+" sent wrong message");
					}
				}
				else{
					Log.out("Client "+clientAddr+" sent wrong message");

				}
			}catch(Exception e){

			}
		}
	}
	public void setState(NodeState state){
		this.state = state;
	}

}