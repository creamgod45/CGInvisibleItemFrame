package cg.creamgod45;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class NMS {

    public static String getVersion(Server server) {
        String packageName = server.getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(46) + 1);
    }

    public static void playChestCloseAnimation(Block chest) {
        try {
            String version = NMS.getVersion(CGInvisibleItemFrame.instance.getServer());
            String nms = "net.minecraft.server." + version + ".";
            String obc = "org.bukkit.craftbukkit." + version + ".";
            Class<?> classBlockPosition = Class.forName(nms + "BlockPosition");
            Class<?> classCraftBlock = Class.forName(obc + "block.CraftBlock");
            Class<?> classCraftWorld = Class.forName(obc + "CraftWorld");
            Class<?> classWorldServer = Class.forName(nms + "WorldServer");
            Class<?> classBlock = Class.forName(nms + "Block");
            Object craftWorld = classCraftWorld.cast(chest.getLocation().getWorld());
            Object craftBlock = classCraftBlock.cast(chest);
            Constructor<?> constructorBlockPosition = classBlockPosition.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            Method methodGetHandleBlock = classCraftBlock.getDeclaredMethod("getNMSBlock", new Class[0]);
            Method methodGetHandleWorld = classCraftWorld.getDeclaredMethod("getHandle", new Class[0]);
            Method methodPlayBlockAction = classWorldServer.getDeclaredMethod("playBlockAction", classBlockPosition, classBlock, Integer.TYPE, Integer.TYPE);
            methodGetHandleBlock.setAccessible(true);
            Object nmsBlock = methodGetHandleBlock.invoke(craftBlock, new Object[0]);
            Object handleWorld = methodGetHandleWorld.invoke(craftWorld, new Object[0]);
            Object blockPosition = constructorBlockPosition.newInstance(chest.getX(), chest.getY(), chest.getZ());
            methodPlayBlockAction.invoke(handleWorld, blockPosition, nmsBlock, 1, 0);
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // by haer0248 URL:https://github.com/haer0248/ServerCore/blob/main/src/me/haer0248/ServerCore/Main.java:297~299
    public static String format(String format){
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    // by haer0248 URL:https://github.com/haer0248/ServerCore/blob/main/src/me/haer0248/ServerCore/Main.java:301~303
    public static String decolor(String format) {
        return format.replaceAll("&1|&2|&3|&4|&5|&6|&7|&8|&9|&a|&b|&c|&d|&e|&f|&l|&m|&n|&o|&r", "");
    }

    // by haer0248 URL:https://github.com/haer0248/ServerCore/blob/main/src/me/haer0248/ServerCore/Main.java:305~314
    public static Player getPlayer(String player_name) {
        List<Player> player_list = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : player_list) {
            if (player.getName().equals(player_name)) {
                return player;
            }
        }

        return null;
    }
}
