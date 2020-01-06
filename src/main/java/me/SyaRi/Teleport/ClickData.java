package me.SyaRi.Teleport;

import org.bukkit.Location;

class ClickData {
	private Location from;
	private Location to;

	ClickData() {
		this.from = null;
		this.to = null;
	}

	void setFrom(Location loc) {
		this.from = loc;
	}

	void setTo(Location loc) {
		this.to = loc;
	}

	Location getFrom() {
		return this.from;
	}

	Location getTo() {
		return this.to;
	}

	boolean isSetFrom() {
		if(from == null) return false;
		else return true;
	}

	boolean isSetTo() {
		if(to == null) return false;
		else return true;
	}
}
