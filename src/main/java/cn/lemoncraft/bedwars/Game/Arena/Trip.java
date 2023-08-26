package cn.lemoncraft.bedwars.Game.Arena;

import cn.lemoncraft.bedwars.BedWars;
import cn.lemoncraft.bedwars.Utils.LocationUtil;
import cn.lemoncraft.bedwars.Utils.TitleUtil;
import cn.lemoncraft.duduskz.achievement.message;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public class Trip implements Listener {
    @EventHandler
    public void onDrink(PlayerItemConsumeEvent event) {
        if (event.getItem().getType().equals(Material.MILK_BUCKET)) {
            if (BedWars.Milk.contains(event.getPlayer().getName())) {
                event.getPlayer().sendMessage("§c你已经喝过一个魔法牛奶了!");
                event.setCancelled(true);
            } else {

                BedWars.Milk.add(event.getPlayer().getName());
                event.getPlayer().sendMessage("§a你喝下了一桶 §6魔法牛奶 §a现在你可以在 1 分钟内免疫敌人家的陷阱");
                event.setItem(null);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (BedWars.Milk.contains(event.getPlayer().getName())) {
                            event.getPlayer().sendMessage("§c你的魔法牛奶已过期!");
                            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_LAND, 1, 1);
                        }
                    }
                }.runTaskLater(BedWars.getPlugin(BedWars.class), 1200);
            }

        }

    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        Plugin plugin = BedWars.getPlugin(BedWars.class);
        FileConfiguration config = plugin.getConfig();
        World world = Bukkit.getWorld(config.getString("Map.WorldName"));
        Player player = event.getPlayer();
        for (Team team : GameStart.getScoreboard().getTeams()) {
            if (!GameStart.getScoreboard().getEntryTeam(player.getName()).getName().equalsIgnoreCase(team.getName())) {
                if (GameStart.getScoreboard().getEntryTeam(player.getName()).getName().equalsIgnoreCase("旁观者")) {
                    if (!BedWars.Milk.contains(player.getName())) {
                        //if (BedWars.Trap.get(team.getName()).size() != 0) {
                        String[] bed = LocationUtil.getStringLocation(config.getString("Map." + team.getName() + ".Bed"));
                        Location bedloc = new Location(world, Double.parseDouble(bed[0]), Double.parseDouble(bed[1]), Double.parseDouble(bed[2]));
                        if (event.getPlayer().getLocation().distance(bedloc) < 10) {
                            if (BedWars.Trap.get(team.getName()).size() != 0) {
                                String name = "你妈死了";
                                if (BedWars.Trap.get(team.getName()).get(0).equals(0)) {
                                    name = "这是一个陷阱！";
                                }
                                if (BedWars.Trap.get(team.getName()).get(0).equals(1)) {
                                    name = "反击陷阱";
                                }
                                if (BedWars.Trap.get(team.getName()).get(0).equals(3)) {
                                    name = "挖掘疲劳陷阱";
                                }
                                if (BedWars.Trap.get(team.getName()).get(0).equals(2)) {
                                    name = "报警陷阱";
                                }
                                player.sendMessage("§c§l你触发了 " + team.getSuffix() + team.getName() + " §c§l的陷阱 " + name + "!");

                                for (String playerlist : team.getEntries()) {
                                    assert Bukkit.getPlayer(playerlist) != null;
                                    Player forplayer = Bukkit.getPlayer(playerlist);
                                    if (BedWars.Trap.get(team.getName()).get(0).equals(0) || BedWars.Trap.get(team.getName()).get(0).equals(4)) {
                                        forplayer.sendMessage("§c§l" + name + " 被触发了！");
                                        player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(8, 1));
                                        player.addPotionEffect(PotionEffectType.SLOW.createEffect(8, 1));
                                        TitleUtil.sendTitle(forplayer, 20, 40, 20, "§c陷阱触发！", "有人触发了你们队伍的陷阱！");
                                    } else if (BedWars.Trap.get(team.getName()).get(0).equals(1)) {
                                        forplayer.sendMessage("§c§l" + name + " 被触发了！");
                                        forplayer.sendMessage("§a你们因为 §6反击陷阱§a，获得了速度 II 跳跃提升 II 的效果");
                                        forplayer.addPotionEffect(PotionEffectType.SPEED.createEffect(15, 2));
                                        forplayer.addPotionEffect(PotionEffectType.JUMP.createEffect(15, 2));
                                        TitleUtil.sendTitle(forplayer, 20, 40, 20, "§c陷阱触发！", "有人触发了你们队伍的陷阱！");
                                    } else if (BedWars.Trap.get(team.getName()).get(0).equals(2)) {
                                        forplayer.sendMessage("§c§l" + name + " 被触发了！");
                                        TitleUtil.sendTitle(forplayer, 20, 40, 20, "§c陷阱触发！", GameStart.getScoreboard().getEntryTeam(player.getName()).getPrefix() + player.getName() + "§f触发了你们队伍的陷阱！");
                                    } else if (BedWars.Trap.get(team.getName()).get(0).equals(3)) {
                                        player.addPotionEffect(PotionEffectType.SLOW_DIGGING.createEffect(10, 1));
                                        forplayer.sendMessage("§c§l" + name + " 被触发了！");
                                        TitleUtil.sendTitle(forplayer, 20, 40, 20, "§c陷阱触发！", "有人触发了你们队伍的陷阱！");
                                    }
                                    forplayer.playSound(forplayer.getLocation(), Sound.ENDERMAN_TELEPORT, 0, 0);

                                }
                                ArrayList<Integer> traplist = BedWars.Trap.get(team.getName());
                                traplist.remove(BedWars.Trap.get(team.getName()).get(0));
                                BedWars.Trap.replace(team.getName(), traplist);
                            }
                        }
                    } else {
                        BedWars.Milk.remove(player.getName());
                        player.sendMessage("§a你使用了 魔法牛奶");
                        message.Unlock(player, "你的陷阱不是很厉害吗？？？", "使用一个魔法牛奶", "bedwars_usemilk", 10);
                    }
                }
            }
            // }
        }
    }
}
