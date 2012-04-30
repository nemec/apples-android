package ec.nem.apples.generic;

import java.io.Serializable;

public class Card implements Serializable{
	private static final long serialVersionUID = 5832564091321470516L;
	private String word;
	private String description;
	
	public Card(String[]args){
		// Received as {word, description}
		word = null;
		description = "";
		
		if(args.length > 0){
			word = args[0];
		}
		if(args.length > 1){
			description = args[1];
		}
	}
	
	public Card(){
		this(null, "");
	}
	
	public Card(String word){
		this(word, "");
	}
	
	public Card(String word, String description){
		this.word = word;
		this.description = description;
	}
	
	public String getWord(){
		return word;
	}
	
	public String getDescription(){
		return description;
	}
	
	public boolean equals(Object c){
		if(c instanceof String){
			return word == (String)c;
		}
		else if(c instanceof Card){
			return word.equals(((Card)c).word);
		}
		return false;
	}
	
	public int hashCode(){
		return word.hashCode();
	}

	public String toString(){
		if(description.equals("")){
			return word;
		}
		else{
			return word+": "+description;
		}
	}
}
