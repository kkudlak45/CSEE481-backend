package com.backend.server.objects;

public class Card {

	private Integer id;
	private String name;
	private String picture;
	private String relationship;
	private Integer deckId;
	
	public Card() {}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	public Integer getDeckId() {
		return deckId;
	}
	public void setDeckId(Integer deckId) {
		this.deckId = deckId;
	}
	
}
