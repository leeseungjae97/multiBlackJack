package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import blackjack.Card;
import blackjack.Dealer;
import blackjack.Guest;

public class BlackJackChat {
	private Guest[] guests;
	private PrintWriter[] pws;
	private BufferedReader []brs;
	private Socket[] sockets;
	private Thread[] reciThreads;
	private Dealer dealer;
	private boolean isDealerMade;
	private boolean isBlackJackEnd;
	private int clientSocketIndex; 
	private int blackJackTop;
	private int size;
	
	public BlackJackChat(int size) {
		this.size = size;
		guests = new Guest[size];
		pws = new PrintWriter[size];
		sockets = new Socket[size];
		brs = new BufferedReader[size];
		reciThreads = new Thread[size];
		blackJackTop = 0;
		clientSocketIndex = 0;
		isDealerMade = false;
		isBlackJackEnd = true;
	}
	
	public void addThread(Thread t, int index) {reciThreads[index - 1] = t;}
	public void removeThread(int index) {reciThreads[index - 1] = null;}
	public void interruptThread(int index) {
		if(reciThreads[index - 1] != null)
		reciThreads[index - 1].interrupt();
	}
	public void removeThread(Thread t) {
		for (int i = 1; i <= reciThreads.length; i++) {
			Thread t1 = reciThreads[i-1];
			if(t1 == t) {
				reciThreads[i-1] = null;
				t.interrupt();
			}
		}
	}
	public void removeAllThread() {
		for (int i = 0; i < reciThreads.length; i++) {
			if(reciThreads[i] != null ) {
				Thread thread = reciThreads[i];
				thread.interrupt();
				reciThreads[i] = null;
			}
		}
	}
	public Thread[] getReciThreads() {return reciThreads;}
	public void displayReciThreads() {
		for (int i = 0; i < reciThreads.length; i++) {
			if(reciThreads[i] != null) System.out.println("[ "+ i +"] : " + reciThreads[i]);
			else System.out.println("[ "+ i +"] :  null");
		}
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public void addBr(BufferedReader br, int index) {brs[index-1] = br;}
	public BufferedReader getBr(int index) {return brs[index-1];}
	public void removeBr(int index) {brs[index-1] = null;}
	public void closeBr(int index) throws IOException {brs[index-1].close();}
	public BufferedReader[] getBrs() {return brs;}
	public void removeBrAll() {
		for (int i = 1; i <= brs.length; i++) {
			 brs[i-1] = null;
			 if(brs[i-1] == null) System.out.println( i + " :  null");
		}
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public PrintWriter[] getPws() {return pws;}
	public void setPws(PrintWriter[] pws) {this.pws = pws;}
	public PrintWriter getPw(int index) {return pws[index-1];} 
	public void addPw(PrintWriter pw, int index) {pws[index-1] = pw;}
	public void removePw(int index) {pws[index-1] = null;}
	public void closePw(int index) {pws[index- 1].close();}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public void addSocket(Socket socket, int index) {sockets[index -1] =socket;}
	public void removeSocket(int index) {sockets[index - 1] = null;}
	public void closeSocket(int index) throws IOException{
		sockets[index - 1].close();
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public boolean getIsBlackJackEnd() {return isBlackJackEnd;}
	public void setIsBlackJackEnd(boolean isBlackJackEnd) {this.isBlackJackEnd = isBlackJackEnd;}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public boolean getIsDealerMade() {return isDealerMade;}
	public void setIsDealerMade(boolean isDealerMade) {this.isDealerMade = isDealerMade;}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public int getBlackJackTop() {return blackJackTop;}
	public void setBlackJackTop(int num) {blackJackTop+=num;}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public int getClientSocketIndex() {return clientSocketIndex;}	
	public void setClientSocketIndex(int num) {clientSocketIndex+=num;}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public void setDealer() {dealer = new Dealer();}
	public Dealer getDealer() {return dealer;}
	public void setDealerBlackJackChat() {
		if(dealer == null) return;
		dealer.setBlackJackChat(this);
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public Guest[] getGuests() {return guests;}
	public void setGuests(Guest[] guests) {this.guests = guests;}
	public Guest getGuest(int index) {return guests[index-1];}
	public void addGuest(int index) {
		Guest guest = new Guest(10000, index);
		guest.setBlackJackChat(this);
		guests[index-1] = guest;
	}
	public void removeGuest(int index) {guests[index-1] = null;}
	public void removeAllGuest() {
		for (int i = 0; i < guests.length; i++) {
			guests[i] = null;
		}
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public Socket getSocket(int index) {return sockets[index- 1];}
	public Socket[] getSockets() {return sockets;}
	public void setSockets(Socket[] sockets) {this.sockets = sockets;}
	public void setAllSocketTimeout(int timeout) throws SocketException {
		for (Socket socket : sockets) {
			if(socket != null) socket.setSoTimeout(timeout);
		}
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public void removeClient(int index) throws IOException {
		removeGuest(index);
		
		interruptThread(index);
		removeThread(index);
		
		closeSocket(index);
		removeSocket(index);
		
		closeBr(index);
		removeBr(index);
		
		closePw(index);
		removePw(index);
		
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public boolean blackjackAllGuest() {
		boolean checkBlackJack = false;
		int sizeNum = 0;
		for (Guest guest : guests) {
			if(guest.getGuestCard().getIsBlackJack()) {
				checkBlackJack = true;
				break;
			}
			sizeNum++;
		}
		return (sizeNum == guests.length && checkBlackJack) ? true : false;
	}
	public boolean bustStandAllGuest() {
		boolean []checkBustStand = new boolean[this.size];
		int getTop = 0;
		for (Guest guest : guests) {
			if (guest == null || guest.getSeedMoney() == 0 && guest.getbetMoney() == 0)
				continue;
			getTop++;
			if(guest.stand()) {
				broadcast("Guest ["+guest.getIndex()+"] : IS STAND");
				checkBustStand[guest.getIndex()] = true;
				break;
			}
			if(guest.getGuestCard().getIsBust()) {
				broadcast("Guest ["+guest.getIndex()+"] : IS BUST");
				checkBustStand[guest.getIndex()] = true;
				break;
			}
		}
		int rGt = 0;
		for (boolean cbs : checkBustStand) {
			if(cbs == false) continue;
			rGt++;
		}
		if(getTop == rGt) return true;
		return false;
	}
	public void betAllGuest() {
		for (Guest guest : guests) {
			if (guest == null || guest.getSeedMoney() == 0 && guest.getbetMoney() == 0)
				continue;
			BufferedReader bReader = getBr(guest.getIndex());
			PrintWriter pWriter = getPw(guest.getIndex());
			try {
				guest.bet(bReader, pWriter);
			} catch (IOException e) {
				broadcast("GUEST ["+guest.getIndex()+"] : connection lost");
				System.out.println("GUEST ["+guest.getIndex()+"] : connection lost");
				try {removeClient(guest.getIndex());}
				catch (IOException e1) {e1.printStackTrace();}
			}
		}
	}
	public void setCardAllGuest() {
		for (Guest guest : guests) {
			if (guest == null || guest.getSeedMoney() == 0 && guest.getbetMoney() == 0)
				continue;
			guest.setCard();
			guest.getCardFromDealer(dealer.giveCardPair());
		}
	}
	public void hitStandAllGuest() throws NumberFormatException {
		for (Guest guest : guests) {
			if ( guest == null || guest.getSeedMoney() == 0 && guest.getbetMoney() == 0 
					|| guest.getGuestCard().getIsBlackJack() || guest.getGuestCard().getIsBust() || guest.stand() ) {
				continue;
			}
			BufferedReader bReader = getBr(guest.getIndex());
			PrintWriter pWriter = getPw(guest.getIndex());
			try {
				guest.Hit(dealer.Hit(), bReader, pWriter);
			} catch (IOException e) {
				broadcast("GUEST ["+guest.getIndex()+"] : connection lost");
				System.out.println("GUEST ["+guest.getIndex()+"] : connection lost");
				try {removeClient(guest.getIndex());}
				catch (IOException e1) {e1.printStackTrace();}
			}
		}
	}
	public void cardCompareAllGuest() {
		for (Guest guest : guests) {
			if (guest == null || guest.getSeedMoney() == 0 && guest.getbetMoney() == 0)
				continue;
			guest.setStand(false);
			guest.getGuestCard().setIsBlackJack(false);
			guest.getGuestCard().setIsBust(false);
			Card.cardCompare(dealer.getDealerCard(), guest, this);
		}
	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	public void broadcast(String message) {
		if(message==null)return;
		for (PrintWriter pw : pws) {
			if(pw == null) continue;
			pw.println(message);
		}
		message = null;
	}
	public void broadcastPrint(String message) {
		if(message==null)return;
		for (PrintWriter pw : pws) {
			if(pw == null) continue;
			pw.print(message);
			pw.flush();
		}
		message = null;
	}
	public void broadcastNewLine() {
		for (PrintWriter pw : pws) {
			if(pw == null) continue;
			pw.println();
		}
	}
}
