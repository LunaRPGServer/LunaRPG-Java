package me.SyaRi.Item.Equip;

import org.bukkit.Material;

public class EquipData {

	private Material material;
	private float damage;
	private float crit_rate;
	private float crit_dmg;
	private float hit_rate;
	private float att_speed;
	private float ctr_rate;
	private float ctr_dmg;
	private float defense;
	private float dodge_rate;
	private float block_rate;
	private float max_health;
	private float max_mana;
	private float use_mana;
	private float reg_mana;
	private float weight;
	private float exp;



	EquipData(Material material,
			  float damage, float crit_rate, float crit_dmg, float hit_rate, float att_speed, float ctr_rate, float ctr_dmg,
			  float defense, float dodge_rate, float block_rate, float max_health, float max_mana, float use_mana, float reg_mana,
			  float weight, float exp) {
		this.material = material;

		this.damage = damage;
		this.crit_rate = crit_rate;
		this.crit_dmg = crit_dmg;
		this.hit_rate = hit_rate;
		this.att_speed = att_speed;
		this.ctr_rate = ctr_rate;
		this.ctr_dmg = ctr_dmg;

		this.defense = defense;
		this.dodge_rate = dodge_rate;
		this.block_rate = block_rate;
		this.max_health = max_health;
		this.max_mana = max_mana;
		this.use_mana = use_mana;
		this.reg_mana = reg_mana;

		this.weight = weight;
		this.exp = exp;
	}

	boolean isBow() {
		return material.equals(Material.BOW);
	}

	float getDamage() {
		return damage;
	}

	float getCritRate() {
		return crit_rate;
	}

	float getCritDmg() {
		return crit_dmg;
	}

	float getHitRate() {
		return hit_rate;
	}

	float getAttSpeed() {
		return att_speed;
	}

	float getCtrRate() {
		return ctr_rate;
	}

	float getCtrDmg() {
		return ctr_dmg;
	}

	float getDefense() {
		return defense / 4;
	}

	float getDodgeRate() {
		return dodge_rate;
	}

	float getBlockRate() {
		return block_rate;
	}

	float getMaxHealth() {
		return max_health;
	}

	float getMaxMana() {
		return max_mana;
	}

	float getUseMana() {
		return use_mana;
	}

	float getRegMana() {
		return reg_mana;
	}

	float getWeight() {
		return weight;
	}

	float getEXPBonus() {
		return exp;
	}
}
