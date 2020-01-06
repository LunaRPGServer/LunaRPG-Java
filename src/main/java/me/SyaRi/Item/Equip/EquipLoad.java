package me.SyaRi.Item.Equip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.SkillAPI;

import me.SyaRi.Util.Util;

public class EquipLoad {
	static float getVal(String s) {
        Pattern p = Pattern.compile("[+-]?\\d+(?:\\.\\d+)?");
        Matcher m = p.matcher(s);
		if(m.find()) {
			return Float.parseFloat(m.group(0));
		} else {
			return 0;
		}
	}

	static EquipData load(Player p, ItemStack held){
		Material material = held.getType();
	// 共通
		float weight = 0, exp = 1;
	// 武具
		float damage = 0, crit_rate = 0, crit_dmg = 0, hit_rate = 0, att_speed = 1, ctr_rate = 0, ctr_dmg = 0;
		if(held != null
				&& held.getItemMeta() != null
				&& held.getItemMeta().getLore() != null) {
			for(String s : held.getItemMeta().getLore()) {
				String c = Util.get.uncolor(s);
				if(s.startsWith(template.Weapon.Damage.prefix)) damage += getVal(c);
				else if(s.startsWith(template.Weapon.CritRate.prefix)) crit_rate += getVal(c);
				else if(s.startsWith(template.Weapon.CritDmg.prefix)) crit_dmg += getVal(c);
				else if(s.startsWith(template.Weapon.HitRate.prefix)) hit_rate += getVal(c);
				else if(s.startsWith(template.Weapon.AttSpeed.prefix)) att_speed += getVal(c);
				else if(s.startsWith(template.Weapon.CritRate.prefix)) ctr_rate += getVal(c);
				else if(s.startsWith(template.Weapon.CritDmg.prefix)) ctr_dmg += getVal(c);
				else if(s.startsWith(template.Weight.prefix)) weight += getVal(c);
				else if(s.startsWith(template.EXPBonus.prefix)) exp += (getVal(c) / 100);
			}
		}
	// 防具
		float defense = 0, dodge_rate = 0, block_rate = 0, max_health = 0, max_mana = 0, use_mana = 0, reg_mana = 0;
		for(ItemStack a : p.getInventory().getArmorContents()) {
			if(a != null && a.getItemMeta() != null && a.getItemMeta().getLore() != null) {
				for(String s : a.getItemMeta().getLore()) {
					String c = Util.get.uncolor(s);
					if(s.startsWith(template.Armor.Defense.prefix)) defense += getVal(c);
					else if(s.startsWith(template.Armor.DodgeRate.prefix)) dodge_rate += getVal(c);
					else if(s.startsWith(template.Armor.BlockRate.prefix)) block_rate += getVal(c);
					else if(s.startsWith(template.Armor.MaxHealth.prefix)) max_health += getVal(c);
					else if(s.startsWith(template.Armor.MaxMana.prefix)) max_mana += getVal(c);
					else if(s.startsWith(template.Armor.UseMana.prefix)) use_mana += getVal(c);
					else if(s.startsWith(template.Armor.RegMana.prefix)) reg_mana += getVal(c);
					else if(s.startsWith(template.Weight.prefix)) weight += getVal(c);
					else if(s.startsWith(template.EXPBonus.prefix)) exp += (getVal(c) / 100);
				}
			}
		}
		return new EquipData(material, damage, crit_rate, crit_dmg, hit_rate, att_speed, ctr_rate, ctr_dmg, defense, dodge_rate, block_rate, max_health, max_mana, use_mana, reg_mana, weight, exp);
	}

	static class template {
		static class Description {
			static class LevelRestriction {
				final static String prefix = Util.get.color("&6レベル制限 &7: &e");
			//	final static String suffix = Util.get.color("");
			}
			
			static class NeedSkill {
				final static String prefix = Util.get.color("&6必要スキル &7: &e");
			}

			static class Rarity {
				final static String prefix = Util.get.color("&6レア度 &7: ");
			//	final static String suffix = Util.get.color("");

				final static String COMMON = Util.get.color("&f");
				final static String UNCOMMON = Util.get.color("&9");
				final static String RARE = Util.get.color("&2");
				final static String EPIC = Util.get.color("&5");
				final static String LEGENDRY = Util.get.color("&e");
				final static String BAD = Util.get.color("&8");
				final static String BROKEN = Util.get.color("&c");
				final static String UNIQUE = Util.get.color("&d");
				final static String GODDES = Util.get.color("&b");
			}

			static class Restriction {
				final static String Trade = Util.get.color("&4交換不可");
				final static String Sell = Util.get.color("&4売却不可");
			}
		}


		static class Weapon {
			static class Damage {
				final static String prefix = Util.get.color("&7- &6攻撃力 &7: &e");
			//	final static String suffix = Util.get.color("");
			}

			static class CritRate {
				final static String prefix = Util.get.color("&7- &6会心率 &7: &e");
				final static String suffix = Util.get.color("%");
			}

			static class CritDmg {
				final static String prefix = Util.get.color("&7- &6会心攻撃力 &7: &e");
				final static String suffix = Util.get.color("%");
			}

			static class HitRate {
				final static String prefix = Util.get.color("&7- &6命中率 &7: &e");
				final static String suffix = Util.get.color("%");
			}

			static class AttSpeed {
				final static String prefix = Util.get.color("&7- &6攻撃速度 &7: &e");
			//	final static String suffix = Util.get.color("");
			}

			static class CtrRate {
				final static String prefix = Util.get.color("&7- &6反撃率&7: &e");
				final static String suffix = Util.get.color("%");
			}

			static class CtrDmg {
				final static String prefix = Util.get.color("&7- &6反撃攻撃力 &7: &e");
				final static String suffix = Util.get.color("%");
			}

		}

		static class Armor {
			static class Defense {
				final static String prefix = Util.get.color("&7- &6防御力 &7: &e");
			//	final static String suffix = Util.get.color("");
			}

			static class DodgeRate {
				final static String prefix = Util.get.color("&7- &6回避率 &7: &e");
				final static String suffix = Util.get.color("%");
			}

			static class BlockRate {
				final static String prefix = Util.get.color("&7- &6ブロック率 &7: &e");
				final static String suffix = Util.get.color("%");
			}

			static class MaxHealth {
				final static String prefix = Util.get.color("&7- &6体力 &7: &e");
			//	final static String suffix = Util.get.color("");
			}

			static class MaxMana {
				final static String prefix = Util.get.color("&7- &6最大マナ &7: &e");
			//	final static String suffix = Util.get.color("");
			}

			static class UseMana {
				final static String prefix = Util.get.color("&7- &6マナ消費 &7: &e");
				final static String suffix = Util.get.color("%");
			}

			static class RegMana {
				final static String prefix = Util.get.color("&7- &6マナ回復 &7: &e");
			//	final static String suffix = Util.get.color("");
			}

			static class RegHealth {
				final static String prefix = Util.get.color("&7- &6体力回復 &7: &e");
			//	final static String suffix = Util.get.color("");
			}
		}

		static class Weight {
			final static String prefix = Util.get.color("&7- &6重さ &7: &e");
		// 	final static String suffix = Util.get.color("");
		}

		static class EXPBonus {
			final static String prefix = Util.get.color("&7- &6経験値 &7: &e");
			final static String suffix = Util.get.color("%");
		}
	}

	public static boolean canTrade(ItemStack item) {
		if(item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
			for(String s : item.getItemMeta().getLore()) {
				if(s.startsWith(template.Description.Restriction.Trade)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean canSell(ItemStack item) {
		if(item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
			for(String s : item.getItemMeta().getLore()) {
				if(s.startsWith(template.Description.Restriction.Sell)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean canUseLevel(Player p, ItemStack item) {
		if(item != null && item.getItemMeta() != null && item.getItemMeta().getLore() != null) {
			for(String s : item.getItemMeta().getLore()) {
				if(s.startsWith(template.Description.LevelRestriction.prefix)) {
					return getVal(s) <= SkillAPI.getPlayerAccountData(p).getActiveData().getMainClass().getLevel();
				}
			}
		}
		return true;
	}
}
