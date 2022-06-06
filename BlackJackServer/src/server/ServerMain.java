package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ServerMain {
	
	private static final Logger logger = Logger.getLogger(ServerMain.class);
	
	private static final int PORT_NUMBER = 4432;
	public static void main(String[] args) throws IOException{
		BlackJackChat bjc = new BlackJackChat(50);
		BasicConfigurator.configure();
		logger.info(":::                                                :::");
		logger.info(":::       Socket Application  Process Start        :::");
		logger.info(":::                                                :::");
		try(ServerSocket server = new ServerSocket(PORT_NUMBER)){
			while(true){
				bjc.setClientSocketIndex(1);
				
				Socket clientSocket = server.accept();				
				ServerSocketThread serverSocketThread = new ServerSocketThread(clientSocket, bjc.getClientSocketIndex());
				
				bjc.addSocket(clientSocket, bjc.getClientSocketIndex());
				bjc.addPw(serverSocketThread.getPw(), bjc.getClientSocketIndex());
				bjc.addBr(serverSocketThread.getBr(), bjc.getClientSocketIndex());
				
				serverSocketThread.setBlackJackChat(bjc);
				serverSocketThread.start();
			}
		}catch(IOException e){
			logger.error(e);
		}
	}
}