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
	private static final int TIMEOUT = 6000;
	boolean finished = false;
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
			serverSocket.setSoTimeout(TIMEOUT);
			Log.out("Server: "+this.name+" has started");
		}catch(Exception e){

		}
		while(finished == false){
			try{
				Socket connected = null;
				try{
				    connected = serverSocket.accept();
				}catch (SocketTimeoutException e){
					if(finished == true){
						break;
					}
				}
				String clientAddr = Util.composeClientAddr(connected);
				InputStream inFromClient = connected.getInputStream();
				DataInputStream in = new DataInputStream(inFromClient);
				String inMessage = in.readUTF();
				String body = Util.getContentFromMessage(inMessage);
				String clientName = Util.getNameFromMessage(inMessage);
				int requestTime = Util.getTimeFromMessage(inMessage);
				OutputStream outToClient = connected.getOutputStream();
				DataOutputStream out = new DataOutputStream(outToClient);
				timeStamp.setReceivedTime(requestTime,name,clientName,true);
				if(body.equals(Util.REQUEST_MESSAGE)){
					switch(state){
						case FREE:
							Log.out("Server: "+this.name+" state "+state+
				 " reply to client "+clientName+" with timestamp "+requestTime );
							timeStamp.updateTimeStamp(name);
							String reply = Util.composeRespondMessage(this.name,timeStamp.getTime());
							out.writeUTF(reply);
							connected.close();
							break;
						case HOLD:
							Log.out("Server: "+this.name+" state "+state+
				 " add client "+clientName+" with timestamp "+requestTime + " to queue");
							waitingQueue.add(clientAddr);
							waitingSockets.put(clientAddr,connected);
							break;
						case WAIT:
							if(timeStamp.getTime() < requestTime){
								Log.out("Server: "+this.name+" state "+state+
				 " add client "+clientName+" with timestamp "+requestTime + " to queue");
								waitingQueue.add(clientAddr);
								waitingSockets.put(clientAddr,connected);
							}
							else if(timeStamp.getTime() == requestTime && this.name.compareTo(clientName)<0){
								Log.out("Server: "+this.name+" state "+state+
				 " add client "+clientName+" with timestamp "+requestTime + " to queue");
								waitingQueue.add(clientAddr);
								waitingSockets.put(clientAddr,connected);
							}
							else{
								Log.out("Server: "+this.name+" state "+state+
				 " reply to client "+clientName+" with timestamp "+requestTime);
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
				e.printStackTrace();
			} 
		}
		try{
			serverSocket.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		Log.out("Server: "+name+" server finished");
	}
	public void setState(NodeState state){
		this.state = state;
	}
	public void finishServer(){
		this.finished = true;
	}

}