package server;

public class MainServer {
	public static void main(String[] args) {
		Server s=new Server(); // invoke the gui part
		s.waitingForClient(); // wait for client
		s.setIoStreams(); // it will set the stream for transfering data
	}
}
