import java.util.*;
import java.io.*;
import java.net.*;

public class Node extends Thread{
	private String id;
	private int port;
	private Client client;
	private Server server;
	private List<Integer> ports;
	private TimeStamp timeStamp;
	private Queue<String> waitingQueue;
	private HashMap<String,Socket> waitingSockets;
	private NodeState state;


	public Node(String id,int port){
		this.id = id;
		this.port = port;
		timeStamp = new TimeStamp();
		waitingQueue = new LinkedList<>();
		waitingSockets = new HashMap<>();
		client = new Client(id,port,timeStamp,waitingQueue);
		server = new Server(id,port,state,timeStamp,waitingQueue,waitingSockets);
		startListen();
	}	
	public void setNeighboors(List<Integer> ports){
		this.ports = ports;
	}

	public List<Integer> getNeighboors(){
		return ports;
	}

	// public String getId(){
	// 	return id;
	// }

	// public void setId(){

	// }

	// public int getPort(){
	// 	return port;
	// }

	// public int setPort(){

	// }

	private boolean sendRequestMessage(){
		timeStamp.increTime();
		// this.server.setTimeStamp(timeStamp);
		setState(NodeState.WAIT);
		Log.out("Node "+id+" current timeStamp "+timeStamp.getTime()+" state "+state);
		setState(NodeState.HOLD);
		boolean rst = this.client.sendRequestMessage(ports);
		timeStamp.updateTimeStamp();
		return rst;
	}

	private void setState(NodeState state){
		this.state = state;
		this.server.setState(state);
	}
	private void startListen(){
		server.start();
	}

	private void releaseLock(){
		setState(NodeState.FREE);
		this.client.sendLockReleaseMessage(waitingSockets);
	}

	@Override
	public void run(){
		int cnt = 0;
		while(cnt++ < 2){
			try{
				Thread.sleep(500);
				boolean requestLock = sendRequestMessage();
				Log.out("Node "+id+" achieved the lock current timeStamp "+timeStamp.getTime()+" state "+state);
				if(requestLock == true){
					//Critical Section
					Log.out("Node "+id+" enter critical section");
					Thread.sleep(500);
					Log.out("Node "+id+" leave critial section and release lock");
					releaseLock();
				}
				else{
					Log.out("Node "+id+" request lock failed");
				}	
			}catch(Exception e){
				
			}
		}

	}
}