package server;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerSocketThread extends Thread {

	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private BlackJackChat bjc;
	private Thread receive;

	private String connIp;
	private int socketIndex;
	
	private ChatThread chatThread;

	public ServerSocketThread(Socket socket, int index) {
		this.socket = socket;
		this.socketIndex = index;
		this.connIp = socket.getInetAddress().getHostAddress();
		
		System.out.println(getConnString() + " enter");
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)), true);
			chatThread = new ChatThread(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setChatThread(ChatThread chatThread) {this.chatThread = chatThread;}
	public ChatThread getChatThread() {return chatThread;}
	public void setBlackJackChat(BlackJackChat bjc) {this.bjc = bjc;}
	public BlackJackChat getBlackJackChat() {return bjc;}
	public int getSocketIndex() {return socketIndex;}
	public PrintWriter getPw() {return pw;}
	public BufferedReader getBr() {return br;}
	public String getConnString() {return connIp;}
	public Thread getRc() {return receive;}
	public void setSocket(Socket socket) {this.socket = socket;}
	public Socket getSocket() {return socket;}

	public void removeSocketSet(int index) {
		bjc.removePw(index);
		bjc.removeBr(index);
		bjc.removeGuest(index);
		bjc.removeSocket(index);
	}
	public void sendClientIndex() {pw.println(this.socketIndex);}
	public void send() {chatThread.send(this);}
	public void receive(Thread t) {chatThread.receive(t);}
	public void recive(final int index, Thread t, final PrintWriter pwl, final BufferedReader brl) 
	{chatThread.receive(index ,t, pwl, brl);}

	public void run() {
		sendClientIndex();
		send();
		receive(receive);
	}
}