package blackjack;
import java.util.ArrayList;
import java.util.Random;

import server.BlackJackChat;

public class Dealer {
	private Card cards;
	private Card dealerCard;
	private int showCount;
	private BlackJackChat bjc;

	public Dealer() {
		cards = new Card(Card.FULL_CARD);
		dealerCard = new Card(Card.START_DECK);
		dealerCard.setCard(this.giveCardPair());
		showCount = 0;
	}

	public Card getDealerCard() {
		return dealerCard;
	}

	public void setBlackJackChat(BlackJackChat bjc) {
		this.bjc = bjc;
	}
	public ArrayList<String> giveCardPair() {
		ArrayList<String> cardPair = new ArrayList<>();
		int top = 0;
		while (top < Card.PAIR) {
			int r = new Random().nextInt(Card.CARD_MAX - 1);
			if (cards.checkCardByIndex(r)) {
				String card = cards.getCardByIndex(r);
				if (card == null)
					continue;
				cardPair.add(card);
				top++;
			}

		}
		return cardPair;
	}

	public String Hit() {
		String cardPair = null;
		while (cardPair == null) {
			int r = new Random().nextInt(Card.CARD_MAX - 1);
			if (cards.checkCardByIndex(r)) {
				cardPair = cards.getCardByIndex(r);
			}
		}
		return cardPair;
	}

	public void show() {
		if (showCount == 0) {
			ArrayList<String> dC = dealerCard.getCard();
			System.out.println("Dealer : " + dC.get(0) + ", * ");

			bjc.broadcast("Dealer : " + dC.get(0) + ", *");
		} else {
			System.out.print("Dealer : ");
			bjc.broadcastPrint("Dealer : ");
			dealerCard.display(bjc);
			
			if (this.dealerCard.getValueToInt() == Card.MAX_POINT) {
				System.out.println("Dealer \t  BLACK JACK!");
				bjc.broadcast("Dealer \t  BLACK JACK!");
			}
			System.out.println("Dealer \t  POINT : " + this.dealerCard.getValueToInt());
			bjc.broadcast("Dealer \t  POINT : " + this.dealerCard.getValueToInt());

		}

		System.out.println("///////////////////////////");
		bjc.broadcast("///////////////////////////");
		this.showCount++;
	}

	public boolean startGame(Guest[] guests) {
		int guestNum = 0;
		for (int i = 0; i < guests.length; i++) {
			if(guests[i] == null) continue;
			if(guests[i].getSeedMoney() == 0) {
				bjc.broadcast("Guest ["+i+1+"] NO SEEDMONEY, EXIT GAME");
				guests[i] = null;
			}else {
				guestNum++;
			}
		}
		if (guestNum == 0) {
			System.out.println("GAME END");
			bjc.broadcast("GAME END");
			return false;
		} else {
			return true;
		}
	}
}