package com.test.observer;

import java.util.Observable;
import java.util.Observer;

public class Sheep implements Observer {

	private String state = "eating";
	private String name;

	public Sheep(String name) {
		this.name = name;
	}

	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Wolf wolf = (Wolf) o;
		System.out.println(wolf.getName() + " crying and " + arg + " "
				+ this.getName() + " running.....");
		setState("running");
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
