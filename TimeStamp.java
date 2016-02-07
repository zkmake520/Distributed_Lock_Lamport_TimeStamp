public class TimeStamp{
	private int time;
	private int receivedTime;
	public TimeStamp(){
		this(0);
	}	

	public TimeStamp(int time){
		this.time = time;
	}

	public int getTime(){
		return this.time;
	}
	public int compare(TimeStamp t2){
		return Integer.compare(this.time,t2.time);
	}
	public void increTime(){
		this.time++;
	}
	public void setTime(int time){
		this.time = time;
	}
	public void updateTimeStamp(String name){
		int pre = this.time;
		if(this.time < receivedTime){
			this.time = receivedTime+1;
		}
		else{
			this.time++;
		}
		Log.out("TimeStamp: "+name+" update time from "+pre+" to "+this.time);
	}

	public void setReceivedTime(int receivedTime, String name,String from,boolean output){
		if(this.receivedTime < receivedTime){
			this.receivedTime = receivedTime;
		}
		if(output)
		Log.out("TimeStamp: "+name+" set recevied time "+receivedTime+" from "+from);

	}
}