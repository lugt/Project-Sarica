package net.worlds.basic;

import org.lwjgl.glfw.GLFW;
import org.wraith.engine.loop.InputHandler;

public class Input implements InputHandler{
	private float pulseStrength;
	private float screenTintStrength;
	private float vinegette = 0;
	private float warpAmount = 0.0000001f;
	private double lastPulse = 0;
	public float getPulseStrength(){
		return pulseStrength;
	}
	public float getScreenTintStrength(){
		return screenTintStrength;
	}
	public float getVinegette(){
		return vinegette;
	}
	public float getWarpAmount(){
		return warpAmount;
	}
	public void keyPressed(long arg0, int arg1, int arg2){
		// TODO Auto-generated method stub
	}
	public void mouseClicked(long window, int button, int action){
		if(button==GLFW.GLFW_MOUSE_BUTTON_LEFT&&action==GLFW.GLFW_PRESS)
			if(lastPulse==0)
				lastPulse = GLFW.glfwGetTime();
	}
	public void mouseMove(long arg0, double arg1, double arg2){
		// TODO Auto-generated method stub
	}
	public void mouseWheel(long arg0, double arg1, double y){
		y *= 1.05f;
		if(y>0)
			warpAmount *= y;
		else
			warpAmount /= Math.abs(y);
	}
	public void update(double time){
		double passed = (time-lastPulse)/0.45f;
		if(passed>=1){
			lastPulse = 0;
			screenTintStrength = 0;
			vinegette = 0;
		}else{
			screenTintStrength = (float)Math.max(Math.sin(Math.pow(passed, 0.5)*Math.PI), 0);
			pulseStrength = screenTintStrength*2;
			screenTintStrength *= 0.3f;
			vinegette = screenTintStrength*1.5f;
		}
	}
}
