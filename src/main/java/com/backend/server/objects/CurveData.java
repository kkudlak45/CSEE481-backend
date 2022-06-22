package com.backend.server.objects;

public class CurveData {

	private int id;
	private int gameType;
	private double bestStat;
	private double recentStat;
	private int accountId;
	
	public CurveData() {}

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

	public double getBestStat() {
		return bestStat;
	}

	public void setBestStat(double bestStat) {
		this.bestStat = bestStat;
	}

	public double getRecentStat() {
		return recentStat;
	}

	public void setRecentStat(double recentStat) {
		this.recentStat = recentStat;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	
	
}
