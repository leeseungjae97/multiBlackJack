package server;
import org.apache.log4j.Logger;

public class StrUtil {
	public static final String CLIENT_MESSAGE_PREFIX_1 = "client";
	public static final String CLIENT_MESSAGE_PREFIX_2 = " : ";
	public static final String STOP = "�׸�";
	public static final String GAME_ENTER = "����";
	public static final String BLACK_JACK_GAME_START = "���� ����";
	public static final String BLACK_JACK_START_INFO = "������ �����մϴ�. ���� ���� �� \"����\" ��� �Է� ";
	public static final String SERVER_PREFIX = "server : ";
	public static final Logger logger = Logger.getLogger(ServerSocketThread.class);
	
	public static String clientMessageSubString(String message) {
		if(message.equals("")) message = "0";
		return message.substring
				(message.indexOf(CLIENT_MESSAGE_PREFIX_2)
						+
					CLIENT_MESSAGE_PREFIX_2.length()
				, message.length());
	}
	
	public static String clientMessageSubStringPreFix2(String message) {
		return message.substring(message.indexOf(CLIENT_MESSAGE_PREFIX_2) + " : ".length());
	}
	public static void timer30() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					int timer = 0;
					while(timer != 30) {
						Thread.sleep(1000);
						timer++;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).run();
	}
	public static void timer10() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int timer = 0;
					while (timer != 10) {
						Thread.sleep(1000);
						timer++;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).run();
	}
}
