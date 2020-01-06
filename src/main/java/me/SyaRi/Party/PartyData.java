package me.SyaRi.Party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.SyaRi.Util.Util;

class PartyData {
	private UUID leader;
	private List<UUID> member;
	private List<UUID> invite;
	private Material icon;
	private boolean publicParty;

	PartyData() {}

	PartyData(UUID leader, List<UUID> member, List<UUID> invite, Material icon, boolean publicParty){
		this.leader = leader;
		this.member = member;
		this.invite = invite;
		this.icon = icon;
		this.publicParty = publicParty;
	}

	PartyData create(UUID leader, boolean publicParty) {
		return new PartyData(leader, new ArrayList<UUID>(5), new ArrayList<UUID>(9), Material.WHITE_BANNER, publicParty);
	}

	PartyData changeLeader(UUID newLeader) {
		return new PartyData(newLeader, this.member, this.invite, this.icon, this.publicParty);
	}

	UUID getLeader() {
		return this.leader;
	}

	Player getLeaderID() {
		return Util.get.player(this.leader);
	}

	List<UUID> getMember(){
		return this.member;
	}

	List<Player> getMemberID(){
		List<Player> list = new ArrayList<Player>(5);
		for(UUID uuid : this.member) {
			list.add(Util.get.player(uuid));
		}
		return list;
	}

	boolean addMember(UUID target) {
		if(this.member.size() < 5) {
			this.member.add(target);
			return true;
		} else {
			return false;
		}
	}

	boolean removeMember(UUID target) {
		if(this.member.contains(target)) {
			this.member.remove(target);
			return true;
		} else {
			return false;
		}
	}

	boolean isBelong(UUID target) {
		return this.member.contains(target);
	}

	List<UUID> getInvite(){
		return this.invite;
	}

	List<Player> getInviteID(){
		List<Player> list = new ArrayList<Player>(5);
		for(UUID uuid : this.invite) {
			list.add(Util.get.player(uuid));
		}
		return list;
	}

	boolean addInvite(UUID target) {
		if(this.invite.size() < 10) {
			this.invite.add(target);
			return true;
		} else {
			return false;
		}
	}

	boolean removeInvite(UUID target) {
		if(this.invite.contains(target)) {
			this.invite.remove(target);
			return true;
		} else {
			return false;
		}
	}

	boolean clearInvite() {
		if(this.invite.size() > 0) {
			this.invite = new ArrayList<UUID>(10);
			return true;
		} else {
			return false;
		}
	}

	boolean isInvite(UUID target) {
		if(this.invite.contains(target)) return true;
		else return false;
	}

	Material getIcon() {
		return this.icon;
	}

	void setIcon(Material icon) {
		this.icon = icon;
	}

	boolean isPublic() {
		return this.publicParty;
	}

	void setPublic() {
		this.publicParty = true;
	}

	void setPrivate() {
		this.publicParty = false;
	}

	void togglePublic() {
		this.publicParty = !this.publicParty;
	}
}
