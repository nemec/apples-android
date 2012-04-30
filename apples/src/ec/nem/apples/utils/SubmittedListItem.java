package ec.nem.apples.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import ec.nem.apples.generic.Card;

public class SubmittedListItem implements Map<String, String>{
	private static final String TAG = "SubmittedListItem";
	public String name;
	public Card card;
	
	public SubmittedListItem(String name, Card card) {
		this.name = name;
		this.card = card;
	}
	
	public String getName(){
		return name;
	}
	
	public Card getCard(){
		return card;
	}

	@Override
	public void clear() {
		name = null;
		card = null;
	}
	@Override
	public boolean containsKey(Object key) {
		return (key.equals("name") && name != null) || 
				(key.equals("word") && card != null);
	}
	@Override
	public boolean containsValue(Object value) {
		return (value.equals("name") && name.equals(value)) || 
				(value.equals("word") && card.equals(value));
	}
	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return null;
	}
	@Override
	public String get(Object key) {
		if(key.equals("name")){
			return name;
		}
		else if(key.equals("word")){
			return card.toString();
		}
		else{
			Log.d(TAG, key.toString());
			return "<null>";
		}
	}
	@Override
	public boolean isEmpty() {
		return name == null && card == null;
	}
	@Override
	public Set<String> keySet() {
		Set<String> s = new HashSet<String>();
        s.add("name");
        s.add("word");
        return s;
	}
	
	@Override
	public String put(String key, String value) {
		return put(key, (Object)value);
	}
	
	public String put(String key, Object value) {
		if(key.equals("name")){
			name = (String)value;
		}
		else if(key.equals("word")){
			card = (Card)value;
		}
		return value.toString();
	}
	@Override
	public void putAll(Map<? extends String, ? extends String> arg0) {
		// not needed
	}
	@Override
	public String remove(Object key) {
		if(key == "name"){
			name = null;
		}
		else if(key == "word"){
			card = null;
		}
		return null;
	}
	@Override
	public int size() {
		return 2;
	}
	@Override
	public Collection<String> values() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(name);
		list.add(card.getWord());
		return list;
	}
	
	public String toString(){
		return name + ": " + card.getWord();
	}
}
