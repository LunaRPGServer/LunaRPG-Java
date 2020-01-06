package me.SyaRi.Party;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.SyaRi.Chat.ChatListener;
import me.SyaRi.Inventory.Get;
import me.SyaRi.Inventory.Inv;
import me.SyaRi.Util.Server.board;
import me.SyaRi.Util.Util;

public class Party {

	private static Map<UUID, PartyData> list = new HashMap<>();

	public static class set{
		public static void join(Player p) {
			Inventory inv = Get.inv(6, "&0&lパーティー加入");
			Inv.setBottom(inv, true);
			int i = 0;
			for(PartyData data : list.values()) {
				if(i == 44) {
					break;
				} else if(data.isInvite(p.getUniqueId()) || data.isPublic()) {
					inv.setItem(i,Get.Item("&6" + data.getLeaderID().getName(), data.getIcon(), "&aパーティーに入る"));
					i++;
				}
			}
			inv.setItem(45, Get.Item("&6パーティー作成", Material.LIGHT_BLUE_STAINED_GLASS_PANE,
				"&f >> &aパーティーを作成します ", "&b 左クリック &7: &b非公開パーティー ", "&b 右クリック &7: &b公開パーティー "));
			p.openInventory(inv);
		}

		public static void list(Player p) {
			Inventory inv = Get.inv(6, "&0&lパーティー一覧");
			Inv.setBottom(inv, true);
			if (isLeader(p)) {
				inv.setItem(45, Get.Item("&6公開設定", Material.WHITE_STAINED_GLASS_PANE,
						"&7現在の設定 : " + ((list.get(p.getUniqueId()).isPublic())? "&a公開パーティー" : "&c非公開パーティー")));
				inv.setItem(46, Get.Item("&6プレイヤーを招待する", Material.WHITE_STAINED_GLASS_PANE,
						"&a 招待するプレイヤーをクリックしてください "));
				inv.setItem(47, Get.Item("&6パーティーアイコン変更", Material.WHITE_STAINED_GLASS_PANE,
						"&a パーティー一覧に表示するアイコンを変更します "));
			}
			inv.setItem(53, Get.Item("&6パーティー脱退", Material.BLUE_STAINED_GLASS_PANE, "&f >> &a所属しているパーティーから抜けます "));
			inv.setItem(0, Get.Item("&7リーダー: &6"+ getLeaderID(p).getName(), list.get(getLeader(p)).getIcon(),
					(isLeader(p))? new String[] {"&b 右クリック &7: &b除名・取消 ", "&b 左クリック &7: &bリーダー譲渡 "} : new String[] {""} ));
			int i = 2;
			for(Player member : list.get(getLeader(p)).getMemberID()) {
				inv.setItem(i, Get.ItemHead("&6" + member.getName(), member, "&a パーティーメンバー "));
				i ++;
			}
			i = 18;
			for(Player invite : list.get(getLeader(p)).getInviteID()) {
				inv.setItem(i, Get.ItemHead("&6" + invite.getName(), invite, "&a 招待プレイヤー "));
				i ++;
			}
			p.openInventory(inv);
		}

		private static void invite(Player p) {
			Inventory inv = Get.inv(6, "&0&lパーティー招待");
			Inv.setBottom(inv, true);
			int i = 0;
			for(Player pl : Bukkit.getServer().getOnlinePlayers()) {
				if(i == 45) break;
				else if(!isBelong(pl) && !list.get(p.getUniqueId()).isInvite(pl.getUniqueId())) {
					inv.setItem(i, Get.ItemHead("&6" + pl.getName(), pl, "&a プレイヤーを招待する "));
					i++;
				}
			}
			p.openInventory(inv);
		}

		private static void icon(Player p) {
			Inventory inv = Get.inv(6, "&0&lパーティーアイコン");
			Inv.setBottom(inv, true);
			inv.setItem(0, getIcon(p, "&6白色の旗", Material.WHITE_BANNER));
			inv.setItem(1, getIcon(p, "&6石の剣", Material.STONE_SWORD));
			inv.setItem(2, getIcon(p, "&6石のツルハシ", Material.STONE_PICKAXE));
			inv.setItem(3, getIcon(p, "&6釣竿", Material.FISHING_ROD));
			inv.setItem(4, getIcon(p, "&6地図", Material.MAP));
			inv.setItem(5, getIcon(p, "&6タンポポ", Material.DANDELION));
			inv.setItem(6, getIcon(p, "&6ポピー", Material.POPPY));
			inv.setItem(7, getIcon(p, "&6キノコ", Material.RED_MUSHROOM));
			inv.setItem(8, getIcon(p, "&6松明", Material.TORCH));
			inv.setItem(9, getIcon(p, "&6エメラルド", Material.EMERALD));
			inv.setItem(10, getIcon(p, "&6エンチャントの瓶", Material.EXPERIENCE_BOTTLE));
			inv.setItem(11, getIcon(p, "&6ポーション", Material.POTION));
			inv.setItem(12, getIcon(p, "&6リンゴ", Material.APPLE));
			inv.setItem(13, getIcon(p, "&6スイカ", Material.MELON));
			inv.setItem(14, getIcon(p, "&6クッキー", Material.COOKIE));
			inv.setItem(15, getIcon(p, "&6生魚", Material.COD));
			inv.setItem(16, getIcon(p, "&6腐った肉", Material.ROTTEN_FLESH));
			inv.setItem(17, getIcon(p, "&6骨", Material.BONE));
			inv.setItem(18, getIcon(p, "&6草", Material.GRASS));
			inv.setItem(19, getIcon(p, "&6砂", Material.SAND));
			inv.setItem(20, getIcon(p, "&6石", Material.STONE));
			inv.setItem(21, getIcon(p, "&6原木", Material.OAK_LOG));
			inv.setItem(22, getIcon(p, "&6作業台", Material.CRAFTING_TABLE));
			p.openInventory(inv);
		}

		private static ItemStack getIcon(Player p, String name, Material mat) {
			return Get.Item(name, mat, list.get(getLeader(p)).getIcon().equals(mat)? "&a 現在の設定 " : "");
		}
	}

	public static class click{
		public static void join(InventoryClickEvent e) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			if(Get.equal(e, "戻る")) {
				Inv.open(p, false);
			} else if(Get.equal(e, "パーティー作成")) {
				if (!isBelong(p)) {
					boolean publicParty;
					if (e.getClick().isLeftClick()) publicParty = true;
					else if (e.getClick().isRightClick()) publicParty = false;
					else return;
					list.put(p.getUniqueId(), new PartyData().create(p.getUniqueId(), publicParty));
					list.get(p.getUniqueId()).addMember(p.getUniqueId());
					board.set(list.get(p.getUniqueId()).getMemberID());
					if(publicParty) reopenJoin();
				}
				set.list(p);
			} else if (!isBelong(p)) {
				Player target = Util.get.player(Util.get.uncolor(e.getCurrentItem().getItemMeta().getDisplayName()));
				if (target == null) {
					set.join(p);
				} else {
					UUID leader = target.getUniqueId();
					if (list.containsKey(leader)) {
						if (list.get(leader).isPublic() || list.get(leader).isInvite(p.getUniqueId())) {
							boolean suc = list.get(leader).addMember(p.getUniqueId());
							board.set(list.get(leader).getMemberID());
							if(suc) {
								SendinParty(leader, Util.get.color(Util.get.prefix.msg() + " &6" + p.getName() + " &fがパーティーに入りました"));
								reopenList(leader);
								list.values().forEach(data -> data.removeInvite(p.getUniqueId()));
							} else {
								SendinParty(leader, Util.get.color(Util.get.prefix.msg() +  " &fパーティーが満員で &6" + p.getName() + " &fがパーティーには入れませんでした"));
								Util.send.msg(p, "&6" + target.getName() + " &fのパーティーは満員です");
								list.get(leader).removeInvite(p.getUniqueId());
							}
						}
					}
					set.list(p);
				}
			}
		}

		public static void list(InventoryClickEvent e) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			if(Get.equal(e, "戻る")) {
				Inv.open((Player) e.getWhoClicked(), false);
			} else if(Get.equal(e, "公開設定")) {
				if(isLeader(p)) {
					toggle(p);
					set.list(p);
				}
			} else if(Get.equal(e, "プレイヤーを招待する")) {
				if(isLeader(p)) {
					set.invite(p);
				}
			} else if(Get.equal(e, "パーティーアイコン変更")) {
				if(isLeader(p)) {
					set.icon(p);
				}
			} else if(Get.equal(e, "パーティー脱退")) {
				if(isBelong(p) && !isLeader(p)) {
					UUID leader = getLeader(p);
					SendinParty(leader, Util.get.color("&7[&a#&7] &6" + p.getName() + " &fがパーティーから去りました"));
					list.get(leader).removeMember(p.getUniqueId());
					ChatListener.set.remove(p.getUniqueId());
					board.set(list.get(leader).getMemberID());
					reopenList(leader);
				} else if (isBelong(p) && isLeader(p)) {
					SendinParty(getLeader(p), Util.get.color("&7[&a#&7] &fパーティーが解散しました"));
					List<Player> tmp = list.get(p.getUniqueId()).getMemberID();
					list.remove(p.getUniqueId());
					tmp.forEach(t -> ChatListener.set.remove(t.getUniqueId()));
					board.set(tmp);
					reopenJoin();
				}
				Inv.open(p, false);
			} else if (isLeader(p) && !Get.equal(e, p.getName())){
				Player target = Util.get.player(Util.get.uncolor(e.getCurrentItem().getItemMeta().getDisplayName()));
				if (target == null) set.list(p);
				if (list.get(p.getUniqueId()).isBelong(target.getUniqueId())) {
					switch(e.getClick()) {
					case RIGHT:
						List<Player> tmp = list.get(p.getUniqueId()).getMemberID();
						list.get(p.getUniqueId()).removeMember(target.getUniqueId());
						ChatListener.set.remove(target.getUniqueId());
						board.set(tmp);
						SendinParty(getLeader(p), Util.get.color("&7[&a#&7] &6" + target.getName() + " &fがパーティーから去りました"));
						target.sendMessage(Util.get.color("&7[&a#&7] &fパーティーからキックされました"));
						reopenList(getLeader(p));
						Inv.open(target, false);
						break;
					case LEFT:
						list.put(target.getUniqueId(), list.get(p.getUniqueId()).changeLeader(target.getUniqueId()));
						list.remove(p.getUniqueId());
						SendinParty(target.getUniqueId(),
								Util.get.color(Util.get.prefix.msg() + " &fパーティーリーダーが &6" +
								p.getName() + " &fから &6" + target.getName() + " &fに代わりました"));
						reopenList(target.getUniqueId());
						break;
					default:
					}
				}
			}
		}

		private static void toggle(Player p) {
			if (isLeader(p)) {
				list.get(p.getUniqueId()).togglePublic();
				set.list(p);
				reopenJoin();
			}
			Inv.open(p, false);
		}

		public static void invite(InventoryClickEvent e) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			if(Get.equal(e, "戻る")) {
				set.list((Player) e.getWhoClicked());
			} else if (isLeader(p)) {
				Player target = Util.get.player(Util.get.uncolor(e.getCurrentItem().getItemMeta().getDisplayName()));
				if (target == null || ! target.isOnline()) {
					Util.send.error.OfflinePlayer(p);
				} else {
					if (!isBelong(target)) {
						if (!list.get(p.getUniqueId()).isInvite(target.getUniqueId())) {
							list.get(p.getUniqueId()).addInvite(target.getUniqueId());
							Util.send.msg(p, "&6" + target.getName() + " &fを招待しました");
							Util.send.msg(target, "&6" + p.getName() + " &fのパーティーに招待されました");
							set.invite(p);
							reopenJoin();
						}
					}
				}
			}
		}

		public static void icon(InventoryClickEvent e) {
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			if(Get.equal(e, "戻る")) {
				set.list((Player) e.getWhoClicked());
			} else if (isLeader(p)) {
				list.get(getLeader(p)).setIcon(e.getCurrentItem().getType());
				set.icon(p);
				reopenList(getLeader(p));
				reopenJoin();
			}
		}
	}

	public static void SendinParty(UUID leader, String ... msg) {
		list.get(leader).getMemberID().forEach(to -> to.sendMessage(msg));
	}

	public static List<Player> getParty(Player p) {
		return list.get(getLeader(p)).getMemberID();
	}

	public static boolean isBelong(Player p) {
		for (PartyData data : list.values()) {
			if(data.isBelong(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	private static boolean isLeader(Player p) {
		return list.containsKey(p.getUniqueId());
	}

	public static UUID getLeader(Player p) {
		for (PartyData data : list.values()) {
			if(data.isBelong(p.getUniqueId())) {
				return data.getLeader();
			}
		}
		return null;
	}

	public static Player getLeaderID(Player p) {
		for (PartyData data : list.values()) {
			if(data.isBelong(p.getUniqueId())) {
				return data.getLeaderID();
			}
		}
		return null;
	}

	private static void reopenList(UUID leader) {
		for(Player member : list.get(leader).getMemberID()) {
			if(Util.get.uncolor(member.getOpenInventory().getTopInventory().getName()).equals("パーティー一覧")) {
				set.list(member);
			}
		}
	}

	private static void reopenJoin() {
		for(Player p : Bukkit.getServer().getOnlinePlayers()) {
			if(Util.get.uncolor(p.getOpenInventory().getTopInventory().getName()).equals("パーティー加入")) {
				set.join(p);
			}
		}
	}

	public static int Partysize(Player p) {
		return list.get(getLeader(p)).getMember().size();
	}
}