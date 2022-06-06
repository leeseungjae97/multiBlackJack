package blackjack;
import java.util.ArrayList;

import server.BlackJackChat;

public class Card {	
	public static final int CARD_MAX = 52;
	public static final int VALUE_MAX = 13;
	public static final int MAX_POINT = 21;
	public static final int PAIR = 2;
	public static final int FULL_CARD = 0;
	public static final int START_DECK = 1;
	
	private final boolean CARD_USED = false;
	private final boolean CARD_NOT_USED = true;
	public final String[] CARD_VALUE = {"A", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "J", "Q", "K"};
	public final String[] CARD_SHAPE = {"H", "S", "C", "D"};
	
	private ArrayList<String> cards;
	private int valueToInt;
	private boolean containsAce;
	private boolean isBust;
	private boolean isBlackJack;
	
	public Card(int ch) {
		this.valueToInt = 0;
		this.containsAce = false;
		this.isBust = false;
		this.cards = new ArrayList<>();
		if(ch == FULL_CARD) {
			setCard();
		}
		
	}
	private void setCard() {
		for (int i = 0; i < CARD_MAX; i++) {
			 StringBuilder inputData = new StringBuilder(CARD_SHAPE[i/VALUE_MAX]);
			 inputData.append(CARD_VALUE[i%VALUE_MAX]);
			 this.cards.add(i, inputData.toString()); 
		}
	}
	public void setCard(ArrayList<String> cards) {
		if(cards == null || cards.size() == 0) return;
		this.cards = cards;
		cardsValueToInt(this.cards);
	}
	public void display(BlackJackChat bjc) {
		for (int i = 0; i < this.cards.size(); i++) {
			System.out.print(this.cards.get(i)+" ");
			bjc.broadcastPrint(this.cards.get(i)+" ");
			if(i != 0 && (i + 1) % VALUE_MAX == 0 ) {
				System.out.println();
				bjc.broadcastNewLine();
			}
		}
		System.out.println();
		bjc.broadcastNewLine();
	}
	public ArrayList<String> getCard() {
		return this.cards;
	}
	public String getCardByIndex(int index) {
		if(index > CARD_MAX) return null;
		String value = cards.get(index);
		cards.remove(index);
		cards.add(index, null);
		return value;
	}
	public boolean checkCardByIndex(int index) {
		if(cards.get(index) != null ) return CARD_NOT_USED;
		else return CARD_USED;
	}
	public int getCardSize() {
		return cards.size();
	}
	public boolean getIsBust() {
		return isBust;
	}
	public boolean getIsBlackJack() {
		return isBlackJack;
	}
	public void setIsBlackJack(boolean blackJack) {
		this.isBlackJack = blackJack;
	}
	public void setIsBust(boolean isBust) {
		this.isBust = isBust;
	}
	public int getValueToInt() {
		this.cardsValueToInt(this.cards);
		return valueToInt;
	}
	
	public void cardsValueToInt(ArrayList<String> cards) {
		boolean isAceCalc = false;
		this.valueToInt = 0;
		for (int i = 0; i < cards.size(); i++) {
			if(CARD_VALUE[0].equals(cards.get(i).substring(1))) {
				containsAce = true;
				if(this.valueToInt < 10) this.valueToInt += 11;
				else this.valueToInt++;
			}
			else if(CARD_VALUE[1].equals(cards.get(i).substring(1))) this.valueToInt += 2;
			else if(CARD_VALUE[2].equals(cards.get(i).substring(1))) this.valueToInt += 3;
			else if(CARD_VALUE[3].equals(cards.get(i).substring(1))) this.valueToInt += 4;
			else if(CARD_VALUE[4].equals(cards.get(i).substring(1))) this.valueToInt += 5;
			else if(CARD_VALUE[5].equals(cards.get(i).substring(1))) this.valueToInt += 6;
			else if(CARD_VALUE[6].equals(cards.get(i).substring(1))) this.valueToInt += 7;
			else if(CARD_VALUE[7].equals(cards.get(i).substring(1))) this.valueToInt += 8;
			else if(CARD_VALUE[8].equals(cards.get(i).substring(1))) this.valueToInt += 9;
			else if(CARD_VALUE[9].equals(cards.get(i).substring(1))) this.valueToInt += 10;
			else this.valueToInt += 10;
			
			if(this.valueToInt > MAX_POINT  && containsAce && !isAceCalc) {this.valueToInt -= 10;isAceCalc=true;}
			if(this.valueToInt > MAX_POINT) {isBust = true;break;}
		}
		if(this.valueToInt == MAX_POINT) isBlackJack = true;
	}
	public void addCard(String card) {
		if(card == null) return;
		cards.add(card);
		cardsValueToInt(cards);
	}
	
	public static void cardCompare(Card dealer, Guest guest, BlackJackChat bjc) {
		int dealerValue = dealer.getValueToInt();
		Card gCard = guest.getGuestCard();
		int guestValue = gCard.getValueToInt(); 
		
		System.out.println("Guest ["+guest.getIndex()+"] POINT : "+ gCard.getValueToInt());
		bjc.broadcast("Guest ["+guest.getIndex()+"] POINT : "+ gCard.getValueToInt());
		
		if(dealerValue < guestValue && !gCard.isBust) {
			System.out.println("Guest ["+guest.getIndex()+"] WIN");
			bjc.broadcast("Guest ["+guest.getIndex()+"] WIN");
			guest.setBetMoney(guest.getbetMoney() * 2);
			guest.setSeedMoney(guest.getSeedMoney() + guest.getbetMoney());
		}
		else if(dealerValue == guestValue) {
			System.out.println("Guest ["+guest.getIndex()+"] PUSH");
			bjc.broadcast("Guest ["+guest.getIndex()+"] PUSH");
			guest.setSeedMoney(guest.getSeedMoney() + guest.getbetMoney());
		}
		else if(gCard.isBust) {
			System.out.println("Guest ["+guest.getIndex()+"] BUST");
			bjc.broadcast("Guest ["+guest.getIndex()+"] BUST");
		}
		else {
			System.out.println("Guest ["+guest.getIndex()+"] LOSE");
			bjc.broadcast("Guest ["+guest.getIndex()+"] LOSE");
		}
		
		System.out.println("Guest ["+guest.getIndex()+"] SEEDMONEY : "+ guest.getSeedMoney());
		System.out.println("///////////////////////////");
		bjc.broadcast("Guest ["+guest.getIndex()+"] SEEDMONEY : "+ guest.getSeedMoney());
		bjc.broadcast("///////////////////////////");
	}
}
