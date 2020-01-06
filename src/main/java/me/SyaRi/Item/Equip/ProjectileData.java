package me.SyaRi.Item.Equip;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import me.SyaRi.Util.Util;

public class ProjectileData {
	private float damage;
	private float crit_rate;
	private float crit_dmg;

	private ProjectileData(float damage, float crit_rate, float crit_dmg) {
		this.damage = damage;
		this.crit_rate = crit_rate;
		this.crit_dmg = crit_dmg;
	}

	static ProjectileData load(ItemStack item) {
		float damage = 0, crit_rate = 0, crit_dmg = 0;
		if(item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
			for(String s : item.getItemMeta().getLore()) {
				String c = Util.get.uncolor(s);
				if(s.startsWith(EquipLoad.template.Weapon.Damage.prefix)) damage += EquipLoad.getVal(c);
				else if(s.startsWith(EquipLoad.template.Weapon.CritRate.prefix)) crit_rate += EquipLoad.getVal(c);
				else if(s.startsWith(EquipLoad.template.Weapon.CritDmg.prefix)) crit_dmg += EquipLoad.getVal(c);
			}
		}
		return new ProjectileData(damage, crit_rate, crit_dmg);
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

	static Entry<ProjectileData, ItemStack> get(ItemStack i){
		Map<ProjectileData, ItemStack> m = new HashMap<>();
		m.put(load(i), i);
		for(Entry<ProjectileData, ItemStack> e : m.entrySet()) {
			return e;
		}
		return null;
	}
}
