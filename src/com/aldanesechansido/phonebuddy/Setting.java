package com.aldanesechansido.phonebuddy;

public class Setting {
	
	private boolean isActive;
	private String name;
	private String desc;
	private boolean hasSwitch;
	
	public Setting(boolean isActive, String name, String desc, boolean hSwitch) {
		super();
		this.isActive = isActive;
		this.name = name;
		this.desc = desc;
		this.hasSwitch = hSwitch;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public boolean hasSwitch()
	{
		return hasSwitch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	

}
