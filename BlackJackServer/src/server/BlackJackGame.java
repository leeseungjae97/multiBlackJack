package server;
import java.io.IOException;

public class BlackJackGame {
	public void startBlackJack(ServerSocketThread sst) throws IOException {
		BlackJackChat bjc = sst.getBlackJackChat();
		ChatThread chatThread = sst.getChatThread();
		
		bjc.setAllSocketTimeout(1);
		bjc.removeAllThread();

		StrUtil.timer10();

		if (bjc == null || bjc.getGuests() == null || bjc.getGuests().length == 0)
			return;
		while (true) {
			if (!bjc.getIsDealerMade()) {
				bjc.setDealer();
				bjc.setDealerBlackJackChat();
				bjc.setIsDealerMade(true);
			}

			if (!bjc.getDealer().startGame(bjc.getGuests())) {
				bjc.setIsBlackJackEnd(true);
				break;
			}

			bjc.setAllSocketTimeout(0);

			bjc.betAllGuest();
			bjc.setCardAllGuest();

			bjc.getDealer().show();
			while(!bjc.bustStandAllGuest()) {
				bjc.hitStandAllGuest();
			}
			bjc.getDealer().show();

			bjc.cardCompareAllGuest();

			bjc.setIsDealerMade(false);
		}
		chatThread.setReceives();
		bjc.removeAllGuest();
		bjc.setIsBlackJackEnd(true);
	}
}
