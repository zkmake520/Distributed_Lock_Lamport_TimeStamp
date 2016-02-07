/**
 *  Class responsible for creating the application layer and initial all those nodes in system
 *
 */
import java.util.*;
public class Application{

	List<Node> nodeList = new ArrayList<>();
	List<Integer> portList = new ArrayList<>();
	static String idPrefix = "Node";
	static int portBase = 3000;

	public void applicationRun(int clusterSize){
		initialNodes(clusterSize);
		startAllNodes();
	}

	public void startAllNodes(){
		try{
			for(Node node : nodeList){
				node.setNeighboors(portList);
				node.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void initialNodes(int num){
		for(int i = 0;i < num; i++){
			String id = idPrefix+i;
			int port = i+portBase;	
			Node node = new Node(id,port);
			nodeList.add(node);
			portList.add(port);
		}
		return;
	}

	public static void main(String[] args){
		Application application = new Application();
		application.applicationRun(3);
	}
}