/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bot;

import twitter4j.*;

/**
 *
 * @author Victor
 * Provide my own implementation of StatusListener methods.
 */
public class MyTwitterListener implements StatusListener{

    @Override
    public void onStatus(Status status) {
        System.out.println("Name: " + status.getUser().getScreenName() +
				" \nlocation " + status.getUser().getLocation() + 
                                " \ncontent: " + status.getText()+
                                " \nlink: " + status.getUser().getURL()) ;
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice sdn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onStallWarning(StallWarning sw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onException(Exception excptn) {
        System.out.println("what is this exception: ? " +
                        excptn.toString());
    }
    
}

