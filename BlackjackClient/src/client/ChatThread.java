package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatThread {
	private SocketClient sc;
	private PrintWriter pw;
	private BufferedReader br;
	private Socket socket;
	
	public ChatThread(SocketClient sc) {
		this.sc = sc;
		this.pw = sc.getPw();
		this.br = sc.getBr();
		this.socket = sc.getSocket();
	}
	public void send() {
		new Thread(new Runnable() {
			@SuppressWarnings("resource")
			public void run() {
				while(sc.getIsChat()) {
					String message = null;
					message = new Scanner(System.in).nextLine();
					if(message.equals(StrUtil.STOP)) {sc.setIsChat(false);}
					else {pw.println(StrUtil.clientSendForm(sc.getSocketIndex(), message));}
					
					if(!sc.getIsChat()) {
						System.out.println("socket closed");
						try {
					    	if(socket != null) { socket.close(); }
					        if(br != null) { br.close(); }
					        if(pw != null) { pw.close(); }
					    } catch (IOException e) {
					        System.out.println(e.getMessage());
					    }
					}
				}
			}
		}).start();
	}
	public void receive() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(sc.getIsChat()) {
						String brString = br.readLine();
						if(brString != null) {
							if(sc.getIsFirst()) {
								sc.setSocketIndex(brString);
								sc.setIsFirst(false);
								continue;
							}
							if(StrUtil.isClient(brString)) System.out.println(brString);
							else if(StrUtil.isOtherClient(brString, sc.getSocketIndex())) {System.out.println(brString);
							}else if(StrUtil.isServer(brString)){System.out.println(brString);}
						}
					}
				} catch (IOException e) {
					StrUtil.logger.error(e);
				}
			}
		}).start();
	}

}
