package com.kk.designPattern.observer;
/**
 *  Observer模式  主要是观察者与被观察者之间的关系
 *  观察者为羊，被观察者为狼  模仿的场景为狼叫羊跑
 * http://blog.csdn.net/dada360778512/article/details/6977758
 * @author kk
 *
 */
public class TestObserver {
	public static void main(String[] args) {  
        Wolf wolf = new Wolf("wolf1");  
        Sheep sheep1 = new Sheep("sheep1");  
        Sheep sheep2 = new Sheep("sheep2");  
        Sheep sheep3 = new Sheep("sheep3");  
        //注册观察者,sheep1,sheep2加入，sheep3未加入  
        wolf.addObserver(sheep1);  
        wolf.addObserver(sheep2);  
        String wolfStat = "hungry";  
        //wolf begin cry  
        wolf.cry(wolfStat);  
    }  
}
