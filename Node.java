public class Node{
	private String id;
	private int port;
	private Client client;
	private Server server;
	private List<Integer> ports;
	private int state
	public Node(String id,int port){
		this.id = id;
		this.port = port;
		client = new Client(id,port);
		server = new Server(id,port);
		startListen();
	}	
	public void setNeighboors(List<Integer> ports){
		this.ports = ports;
	}

	public List<Integer> getNeighboors(){
		return ports;
	}

	public String getId(){
		return id;
	}

	public void setId(){

	}

	public int getPort(){
		return port;
	}

	public int setPort(){

	}

	private boolean sendRequestMessage(){
		return this.client.sendRequestMessage(ports);
	}

	private void startListen(){
		server.start();
	}

}