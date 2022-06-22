package com.backend.server.objects;

public class PersonalData {

	private int id;
	private int gameType;
	private double stat;
	private int accountId;
	
	public PersonalData() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public double getStat() {
		return stat;
	}

	public void setStat(double stat) {
		this.stat = stat;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
}
