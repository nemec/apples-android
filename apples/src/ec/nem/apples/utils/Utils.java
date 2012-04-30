package ec.nem.apples.utils;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import ec.nem.apples.generic.Card;

public class Utils {
	private static final String TAG = "Utils";
	
	public static ArrayList<Card> getCardsFromXML(Context context, int id){
		ArrayList<Card> cards = new ArrayList<Card>();
		XmlResourceParser adj = context.getResources().getXml(id);
	    try{
	    	int eventType = adj.getEventType();
	         while (eventType != XmlPullParser.END_DOCUMENT) {
	          if(eventType == XmlPullParser.START_TAG) {
	        	  if(adj.getName().equals("card")){
	        		  String name = null;
	        		  String desc = "";
	        		  while(!(eventType == XmlPullParser.END_TAG &&
	        				  adj.getName().equals("card"))){
	        			  eventType = adj.next();
	        			  if(eventType == XmlPullParser.START_TAG){
	        				  if(adj.getName().equals("name")){
	        					  eventType = adj.next();
	        					  name = adj.getText();
	        				  }
	        				  else if(adj.getName().equals("description")){
	        					  eventType = adj.next();
	        					  if(adj.getText() != null){
	        						  desc = adj.getText();
	        					  }
	        				  }
	        			  }
	        		  }
	        		  if(name != null){
	        			  Card c = new Card(name, desc);
	        			  cards.add(c);
	        		  }
	        	  }
	          }
	          eventType = adj.next();
	         }
	    } catch(IOException e){
	    	Log.e(TAG, "Could not read xml.", e);
	    } catch(XmlPullParserException e){
	    	Log.e(TAG, "Could not parse xml", e);
	    } finally{
	    	adj.close();
	    }
	    return cards;
	}
}
