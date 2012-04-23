package ec.nem.apples.generic;

public class Card{
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
		if(!(c instanceof Card)){
			return false;
		}
		return word.equals(((Card)c).word);
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
