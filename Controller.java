package bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.TwitterException;

public class Controller {
	
        private final static String KEYS_FILENAME = "myConsumerKeys.txt";
        private static String consumerKey;
	private static String consumerPrivate;
        
	public static void main(String [] args){
		
                loadMyPrivateKeys();
		LoginWithTwitter lg = new LoginWithTwitter(consumerKey, consumerPrivate);
		lg.login();
                lg.queryTwitter();
	}
        
        public static void loadMyPrivateKeys(){
            try{
                Scanner sc = new Scanner(new File(KEYS_FILENAME));
                consumerKey = sc.nextLine();
                consumerPrivate = sc.nextLine();
                sc.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Could not find consumer keys for app... now closing");
                System.exit(0);
            }
        }
	
}
