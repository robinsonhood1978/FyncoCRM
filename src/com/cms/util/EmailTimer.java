package com.cms.util;

import java.util.Timer;
import java.util.TimerTask;

public class EmailTimer extends TimerTask{
	private String name;
    public EmailTimer(String name) {
        this.name = name;
    }
    public void setName(String name) {
    	 this.name = name;
	}
    public void run() {
        System.out.println("hhshshshshhs"+name);
    }

    public static void main(String args[]) {
    	Timer timer = new Timer();
        EmailTimer emailTimer = new EmailTimer("deng");
        timer.schedule(emailTimer, 1000, 5*1000); 
        System.out.println("Task scheduled.");
        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        emailTimer.setName("lu");;
    }
}
