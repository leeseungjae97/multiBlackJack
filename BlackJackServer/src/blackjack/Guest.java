package blackjack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import server.BlackJackChat;
import server.StrUtil;

public class Guest extends Thread{
	private int seedMoney;
	private int betMoney;
	private boolean stand;
	private Card card;
	private int index;
	private BlackJackChat bjc;
	
	public Guest(int seedMoney, int index) {
		this.seedMoney = seedMoney;
		this.index = index;
		this.stand = false;
	}
	
	
	public boolean stand() {return stand;}
	public void setStand(boolean stand) {this.stand = stand;}
	public void setBlackJackChat(BlackJackChat bjc){this.bjc = bjc;}
	public int getIndex() {return index;}
	public void setIndex(int index) {this.index = index;}
	public int getbetMoney() {return betMoney;}
	public void setBetMoney(int money) {this.betMoney = money;}
	public void setSeedMoney(int money) {this.seedMoney = money;}
	public Card getGuestCard() {return this.card;}
	public void setCard() {
		if(betMoney == 0) return;
		card = new Card(Card.START_DECK);
	}
	public void show() {card.display(bjc);}
	
	public void bet(BufferedReader br, PrintWriter pw) throws IOException {
		this.betMoney = 0;
		if(seedMoney == 0 || br == null || pw == null) {
			return;
		}
		
		System.out.println("Guest ["+this.index+"] SEEDMONEY : " + this.seedMoney);
		bjc.broadcast("Guest ["+this.index+"] SEEDMONEY : " + this.seedMoney);
		pw.println("INPUT BETMONEY>>");
		
		String string = "";
		try {
			string = br.readLine();		
		}catch(NumberFormatException e) {}
		
		if(string == null || StrUtil.clientMessageSubString(string).equals("")) this.betMoney = 0;
		else this.betMoney = Integer.parseInt( StrUtil.clientMessageSubString(string));
		
		
		if(this.betMoney > this.seedMoney || this.betMoney <= 0) {
			boolean cantBet = true;
			while(cantBet) {
				
				bjc.broadcast("BETMONEY UNDER 0, BETMONEY CAN'T OVER SEEDMONEY RE INPUT ");
				pw.println("INPUT BETMONEY>>");
				try {
					string = br.readLine();		
				}catch(NumberFormatException e) {}
				if(string == null || StrUtil.clientMessageSubString(string).equals("")) this.betMoney = 0;
				else this.betMoney = Integer.parseInt( StrUtil.clientMessageSubString(string));
				
				if(this.betMoney <= this.seedMoney) cantBet = false; 
			}	
		}		
		this.seedMoney -= this.betMoney;
		System.out.println("Guest ["+this.index+"] BETMONEY : "+this.betMoney);
		System.out.println("///////////////////////////");
		
		bjc.broadcast("Guest ["+this.index+"] BETMONEY : "+this.betMoney);
		bjc.broadcast("///////////////////////////");	
	}
	
	
	public int getSeedMoney() {return seedMoney;}
	
	public void getCardFromDealer(ArrayList<String> cards) {
		if (cards == null || cards.size()== 0 || betMoney == 0) return;
		this.card.setCard(cards);
		if(this.card.getValueToInt() == Card.MAX_POINT) {
			System.out.println("Guest ["+this.getIndex()+"] BLACK JACK!");
			bjc.broadcast("Guest ["+this.getIndex()+"] BLACK JACK!");
		}
		System.out.print("Guest ["+this.getIndex()+"] CARD : ");
		bjc.broadcastPrint("Guest ["+this.getIndex()+"] CARD : ");
		this.card.display(bjc);
		System.out.println("///////////////////////////");
		bjc.broadcast("///////////////////////////");
	}
	public void Hit(String card, BufferedReader br, PrintWriter pw) throws NumberFormatException, IOException {
		if (card ==null || betMoney == 0 || this.card.getValueToInt() >= Card.MAX_POINT || this.card.getIsBust()) return;
		pw.println("Guest ["+this.getIndex()+"] : HIT(1) OR STAND(2) :");
		
		String st = br.readLine();
		int num1 = Integer.parseInt( StrUtil.clientMessageSubString(st));

		if(num1 == 2) {
			System.out.print("Guest ["+this.getIndex()+"] CARD : ");
			bjc.broadcastPrint("Guest ["+this.getIndex()+"] CARD : ");
			this.card.display(bjc);
			this.stand = true;
			return;
		}
		this.card.addCard(card);
		System.out.print("Guest ["+this.getIndex()+"] CARD : ");
		bjc.broadcastPrint("Guest ["+this.getIndex()+"] CARD : ");
		this.card.display(bjc);
		
		System.out.println("///////////////////////////");
		bjc.broadcast("///////////////////////////");
	}
}