package client;

import org.apache.log4j.Logger;

public class StrUtil {
	public static final String STOP = "±×¸¸";
	public static final String CLIENT = "client";
	public static final String SERVER = "server";
	public static final String DELI = " : ";
	
	public static final Logger logger = Logger.getLogger(SocketClient.class);
	public static String clientSendForm(String index, String message) {
		return CLIENT + index + DELI + message;
	}
	public static boolean isClient(String brString ) {
		return brString.length() < CLIENT.length();
	}
	public static boolean isOtherClient(String brString, String socketIndex) {
		return !brString.substring(0, CLIENT.length()).equals(CLIENT) || !brString.substring(CLIENT.length(), brString.indexOf(DELI)).equals(socketIndex);
	}
	public static boolean isServer(String brString) {
		return brString.substring(0, SERVER.length()).equals(SERVER);
	}
}
