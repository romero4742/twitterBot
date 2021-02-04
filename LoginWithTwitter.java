package bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class LoginWithTwitter {

        private final String STORED_TOKEN_FILE = "storedToken.txt";
	private final Twitter twitter;
	private String consumerKey, consumerPrivate;
        private StatusListener listener;
	
	/**
	 * Constructor which receives a consumerKey and a privateKey.
	 * @param consumerKey
	 * @param consumerPrivate
	 */
	public LoginWithTwitter(String consumerKey, String consumerPrivate){
		twitter = TwitterFactory.getSingleton();
		this.consumerKey = consumerKey;
		this.consumerPrivate = consumerPrivate;
	}
	
	/**
	 * attempts to login with the provided keys. Else it will 
	 * request new ones.
	 */
	public void login(){
		
		twitter.setOAuthConsumer(consumerKey, consumerPrivate);
				
		AccessToken accessToken = checkStoredToken();
                twitter.setOAuthAccessToken(accessToken);
		
                try{
                    if(twitter.verifyCredentials() != null){
                        System.out.println("Logged in.. welcome: "+
                                            twitter.getId());
                        return;
                    }
                }catch(Exception e){
                    System.out.println("No stored or failed credentials");
                    accessToken = null;
                    twitter.setOAuthAccessToken(accessToken);
                }
		try{
			RequestToken requestToken = twitter.getOAuthRequestToken();
			Scanner kb = new Scanner(System.in);
			while(accessToken == null){
				System.out.println("Follow this URL and allow this app in order to login: " 
								   + requestToken.getAuthorizationURL());
				System.out.print("Enter pin: ");
				String pin = kb.nextLine();
				try{
					if(pin.length() > 0){
						accessToken = twitter.getOAuthAccessToken(requestToken,pin);
					} else {
						accessToken = twitter.getOAuthAccessToken();
					}
				}catch(TwitterException te){
					if(401 == te.getStatusCode()){
						System.out.println("Unable to access token");
					} else {
						te.printStackTrace();
					}
				}
			}
                       //closing scanner closes system.in and causes error further in app
			//kb.close();
			
			twitter.setOAuthAccessToken(accessToken);
			try {
				if(twitter.verifyCredentials() != null){
					System.out.println("token was verified... welcome "+
                                                        twitter.getId());
				}
			} catch (TwitterException e) {
				System.out.println("invalid credentials");
			}
                        saveToken(accessToken);
			
		} catch(TwitterException e){
			System.out.println("Invalid consumer tokens?");
			System.out.println(e.toString());
		}
	}
        
        private AccessToken checkStoredToken(){
            File f = new File(STORED_TOKEN_FILE);
                try{
                    Scanner sc = new Scanner(f);
                    String token = sc.nextLine();
                    String tokenSecret = sc.nextLine();
                    sc.close();
                    return new AccessToken(token,tokenSecret);
                } catch(FileNotFoundException e){
                    System.out.println("No access token stored.Continue to login.");
                }
            return null;
        }
        
        private void saveToken(AccessToken accessToken){
            String fileName = STORED_TOKEN_FILE;
            try{
                PrintWriter pw = new PrintWriter(new File(fileName));
                pw.println(accessToken.getToken());
                pw.println(accessToken.getTokenSecret());
                pw.close();
            } catch (FileNotFoundException ex) {
                System.out.println("File to store tokens not found");
            }
        }
        
        //queries twitter based on keyword input
        public void queryTwitter() {
            listener = new MyTwitterListener();
            
            TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
            twitterStream.setOAuthConsumer(consumerKey, consumerPrivate);
            try {
                twitterStream.setOAuthAccessToken(twitter.getOAuthAccessToken());
            } catch (TwitterException ex) {
                java.util.logging.Logger.getLogger(LoginWithTwitter.class.getName()).log(Level.SEVERE, null, ex);
            }
            twitterStream.addListener(listener);
            
            Scanner kb = new Scanner(System.in);
            System.out.println("Enter keyword to query: ");
            String word = kb.nextLine();
            kb.close();
            
            FilterQuery fq = new FilterQuery();
            String[] keyWord = {word};
            
            fq.track(keyWord);
            twitterStream.filter(fq);
            
        }
       
}
