package com.kk.designPattern.observer;

import java.util.Observable;

public class Wolf extends Observable {

	private String name;

	Wolf(String name) {
		this.name = name;
	}

	public void cry(String state) {
		System.out.println(this.getName() + " crying ");
		this.setChanged();
		this.notifyObservers(state);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
