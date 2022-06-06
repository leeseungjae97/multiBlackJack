package client;

import org.apache.log4j.BasicConfigurator;

public class SocketMain{
    public static void main( String[] args ) {
    	BasicConfigurator.configure();
    	String homeString = "192.168.0.3";
    	String t1 = "10.10.1.26";
        int port = 4432;
        SocketClient socketClient = new SocketClient(homeString, port);
        socketClient.startSocket();
    }
}