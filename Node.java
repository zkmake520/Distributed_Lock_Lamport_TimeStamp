public class Node{
	private String id;
	private int port;
	private Client client;
	private Server server;
	private List<Integer> ports;
	public Node(String id,int port){
		this.id = id;
		this.port = port;
		client = new Client(id,port);
		server = new Server(id,port);
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

	
}