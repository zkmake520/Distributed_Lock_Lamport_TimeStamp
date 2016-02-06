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
	public void updateTimeStamp(){
		if(this.time < receivedTime){
			this.time = receivedTime;
		}
	}

	public void setReceivedTime(int receivedTime){
		if(this.receivedTime < receivedTime){
			this.receivedTime = receivedTime;
		}

	}
}