/**
 *  Class responsible for creating the application layer and initial all those nodes in system
 *
 */
import java.util.*;
public class Application{

	List<Node> nodeList = new List<>();
	List<Integer> portList = new List<>();
	static String idPrefix = "Node";
	static int portBase = 3000;

	public void applicationRun(int clusterSize){
		initialNodes(clusterSize);
		startAllNodes();
	}

	public void startAllNodes(){
		for(Node node : nodeList){
			node.start();
		}
	}

	public void initialNodes(){
		for(int i = 0;i < num; i++){
			String id = idPrefix+i;
			int port = i+portBase;	
			Node node = new Node(id,port);
			nodeList.add(node);
		}
		return;
	}

	public static void main(String[] args){
		Application application = new Application();
		application.applicationRun(3);
	}
}