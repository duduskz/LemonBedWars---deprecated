package cn.lemoncraft.bedwars.Command;

import cn.lemoncraft.bedwars.BedWars;
import cn.lemoncraft.bedwars.NoMAP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Player player = (Player) sender;
        Plugin plugin = BedWars.getPlugin(BedWars.class);
        if (args.length == 0){
            sender.sendMessage("§c你好，你是想检查这个服务器运行什么起床插件？这个服务器运行着§bLemon§aBedWars");
            sender.sendMessage("§a作者: §bLemonNetwork(duduskz)");
            sender.sendMessage("什么？你觉得我们用的是1058？？？");
        }
        if (sender instanceof ConsoleCommandSender || sender.hasPermission("LemonBedWars.admin")){
            if (args.length == 1){
                if (Objects.equals(args[0], "reload")){
                    plugin.reloadConfig();
                    sender.sendMessage("§a插件配置已重载");
                } else {

                }
                if (Objects.equals(args[0], "save")){
                    plugin.saveConfig();
                    sender.sendMessage("§a插件配置已保存");
                } else {

                }
                if (Objects.equals(args[0], "build")){
                    if (sender instanceof Player) {
                        if (plugin.getConfig().getString("BungeeMode").equalsIgnoreCase("Lobby")) {
                            if (BedWars.Build.contains(sender.getName())) {
                                sender.sendMessage("§7已关闭 §c建筑模式");
                                BedWars.Build.add(sender.getName());
                            } else {
                                sender.sendMessage("§7已打开 §a建筑模式");
                                BedWars.Build.remove(sender.getName());
                            }
                        } else {
                            sender.sendMessage("§c请在大厅输入此指令！");
                        }
                    } else {
                        sender.sendMessage("§c请不要在控制台输入此指令！");
                    }
                } else {

                }
            }
            if (args.length == 2){
                if (Objects.equals(args[0], "join")){
                    
                }
                if (args[0].equalsIgnoreCase("editteam") && sender instanceof Player){
                    Player player = (Player) sender;
                    NoMAP.editteam(player, args);
                }

            }
        }
        return false;
    }
}
