import java.util.Thread;
import java.io.*;
import java.net.*;

public class Client extends Thread{
	String name;
	public Client(String id,int port){
		this.name = id;
	}

}