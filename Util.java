import java.net.*;
public class Util{
	public static String LOCAL_HOST = "localhost";
	public static String REQUEST_MESSAGE = "request lock";
	public static String REPLY = "reply";

	public static String composeClientAddr(Socket connected){
		return connected.getInetAddress()+":"+connected.getPort();
	}

	public static String composeRequestMessage(String name, int time){
		return Util.REQUEST_MESSAGE+":"+name+":"+ time;
	}

	public static String composeRespondMessage(String name, int time){
		return Util.REPLY+":"+name+":"+ time;
	}
	public static int getTimeFromMessage(String message){
		return Integer.parseInt(message.split(":")[2]);
	}
	public static Strinng getContentFromMessage(String message){
		return message.split(":")[0];
	} 
	public static String getNameFromMessage(String message){
		return message.split(":")[1];
	} 
}