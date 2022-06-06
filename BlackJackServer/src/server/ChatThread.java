package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ChatThread {
	public Logger logger = Logger.getLogger(ServerSocketThread.class);
	private ServerSocketThread sst;
	private BufferedReader br;
	private PrintWriter pw;
	
	public ChatThread(ServerSocketThread sst) {
		this.sst = sst;
		this.br = sst.getBr();
		this.pw = sst.getPw();
	}
	public void receive(Thread t) {
		final BlackJackChat bjc = sst.getBlackJackChat();
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String brString = null;
					while (true) {
						brString = br.readLine();
						if (brString != null) {
							String isStopString = StrUtil.clientMessageSubStringPreFix2(brString);
							if (isStopString.equals(StrUtil.GAME_ENTER)) {bjc.addGuest(sst.getSocketIndex());}
							else if (isStopString.equals(StrUtil.STOP)) {
								System.out.println(sst.getConnString() + " is closed");
								try {
									if (pw!= null) {pw.close();}
									if (br != null) {br.close();}
									if (sst.getSocket()!= null) {sst.getSocket().close();}
								} catch (IOException e) {logger.error(e);}
							}
							else {
								bjc.broadcast(brString);
								System.out.println(brString);
							}
						}
					}
				} catch (IOException e) {;}
			}
		});
		bjc.addThread(t, sst.getSocketIndex());
		
		t.start();
	}
	public void send(final ServerSocketThread sst) {
		final BlackJackChat bjc = sst.getBlackJackChat();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String sendString = null;
					try {
						System.in.mark(0);
						System.in.reset();
						sendString = new BufferedReader(new InputStreamReader(System.in)).readLine();
					} catch (IOException e1) {e1.printStackTrace();}
					if (sendString == null) continue;
					else {
						if (sendString.equals(StrUtil.BLACK_JACK_GAME_START) && bjc.getIsBlackJackEnd()) {
							sendString = StrUtil.BLACK_JACK_START_INFO;
							bjc.setIsBlackJackEnd(false);
							bjc.broadcast(sendString);
							try {
								BlackJackGame blackJack = new BlackJackGame();
								blackJack.startBlackJack(sst);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}else {
							bjc.broadcast(StrUtil.SERVER_PREFIX + sendString);
						}
						
					}
				}
			}
		}).start();
	}
	public void receive(final int index, Thread t, final PrintWriter pwl, final BufferedReader brl) {
		final BlackJackChat bjc = sst.getBlackJackChat();
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String brString = null;
					while (true) {
						brString = brl.readLine();
						if (brString != null) {
							String isStopString = StrUtil.clientMessageSubStringPreFix2(brString);
							if (isStopString.equals(StrUtil.GAME_ENTER)) {bjc.addGuest(index);}
							else if (isStopString.equals(StrUtil.STOP)) {
								System.out.println(sst.getConnString() + " is closed");
								try {
									if (pw!= null) {pw.close();}
									if (br != null) {br.close();}
									if (sst.getSocket()!= null) {sst.getSocket().close();}
								} catch (IOException e) {logger.error(e);}
							}
							else if(bjc.getIsBlackJackEnd()) {
								bjc.broadcast(brString);
								System.out.println(brString);
							}
						}
					}
				} catch (IOException e) {
					;
				}
			}
		});
		bjc.addThread(t, index);
		t.start();
	}
	public void setReceives() {
		final BlackJackChat bjc = sst.getBlackJackChat();
		for (int i = 1; i <= bjc.getSockets().length; i++) {
			Socket soc = bjc.getSockets()[i - 1];
			if (soc == null) continue;
			Thread t = null;
			receive(i, t, bjc.getPw(i), bjc.getBr(i));
		}
	}
}
