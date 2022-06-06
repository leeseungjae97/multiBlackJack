package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketClient {
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private String socketIndex;
	private String connIp;
	
	private ChatThread chatThread;
	
	private boolean isChat = true;
	private boolean isFirst = true;
	
	public SocketClient(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			connIp = socket.getInetAddress().getHostAddress();
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream() , StandardCharsets.UTF_8), true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			chatThread = new ChatThread(this);
		} catch (IOException e) {System.out.println(e.getMessage());}
	}
	
	public String getConnIp() {return connIp;}
	public boolean getIsChat() {return isChat;}
	public boolean getIsFirst() {return isFirst;}
	public void setIsChat(boolean isChat) {this.isChat = isChat;}
	public void setIsFirst(boolean isFirst) {this.isFirst = isFirst;}
	public String getSocketIndex() {return socketIndex;}
	public void setSocketIndex(String socketIndex) {this.socketIndex = socketIndex;}
	public PrintWriter getPw() {return pw;}
	public BufferedReader getBr() {return br;}
	public Socket getSocket() {return socket;}
	public void receive() {chatThread.receive();}
	public void send() {chatThread.send();}
	
	
	public void startSocket() {
		if(socket == null) return;
		System.out.println(socket.getInetAddress().getHostAddress() + " connect");
		send();
		receive();
	}
	
}