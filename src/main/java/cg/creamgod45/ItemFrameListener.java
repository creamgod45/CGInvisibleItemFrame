package cg.creamgod45;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.player.ConsolePlayer;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class ItemFrameListener implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void onClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(!ConfigReader.disable_worlds.isEmpty()){
            List<String> disable_worlds = ConfigReader.disable_worlds;
            for(String w : disable_worlds) if(player.getWorld().getName().equals(w)) return;
        }
        if (event.isCancelled()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof ItemFrame)) return;

        Boolean isInPlotWorld = false;
        Boolean isOwner = false;
        Boolean isInPlotRoad = false;

        if(CGInvisibleItemFrame.plotsquared) {
            if (!ConfigReader.PlotSquared_Worlds.isEmpty()) {
                List<String> PlotWorlds = ConfigReader.PlotSquared_Worlds;

                for (String pw : PlotWorlds) {
                    if (!player.getWorld().getName().equals(pw)) continue;
                    if (player.getWorld().getName().equals(pw)) {
                        isInPlotWorld = plot_InPlotWorld(player, pw);
                        isInPlotRoad = plot_InPlotRoad(player);
                        if (!isInPlotRoad) {
                            isOwner = plot_checkowner(player);
                        }
                    }
                    // if in plotworld and in plotroad
                    // and if have "cginvisibleitemframe.admin":
                    //      you can change everything
                    // else:
                    //      you can't change everything
                    if (isInPlotWorld && isInPlotRoad) {
                        if(HasPermission(player,"cginvisibleitemframe.admin")){
                            item_frame(event);
                            return;
                        }else{
                            if (HasPermission(player, "cginvisibleitemframe.debugConsole")) {
                                Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_inRoad.replace("*player_name*", player.getName()));
                            }
                            player.sendMessage(ConfigReader.debug_player_plot_inRoad);
                            event.setCancelled(true);
                            return;
                        }
                    }
                    // if in plotworld and is plot (owner||trust)
                    // and if have "cginvisibleitemframe.admin":
                    //      you can change everything
                    // else:
                    //
                    if(isInPlotWorld){
                        if(HasPermission(player,"cginvisibleitemframe.admin")){
                            item_frame(event);
                            return;
                        }else if(isOwner && HasPermission(player,"cginvisibleitemframe.use")){
                            item_frame(event);
                            return;
                        }else{
                            if (HasPermission(player, "cginvisibleitemframe.debugConsole")) {
                                Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_inRoad.replace("*player_name*", player.getName()));
                            }
                            player.sendMessage(ConfigReader.debug_player_plot_inRoad);
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if(ConfigReader.fast_deny_player_use && !ConfigReader.fast_deny_player_use_worlds.isEmpty() && !HasPermission(player,"cginvisibleitemframe.admin")){
            for(String w:ConfigReader.fast_deny_player_use_worlds){
                if(!player.getWorld().getName().equals(w)){
                    if (HasPermission(player, "cginvisibleitemframe.use") || HasPermission(player,"cginvisibleitemframe.admin")) {
                        item_frame(event);
                    } else {
                        if (HasPermission(player, "cginvisibleitemframe.debugConsole")) {
                            Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_permission.replace("*player_name*", player.getName()));
                        }
                        event.getPlayer().sendMessage(ConfigReader.no_permission);
                    }
                }else{
                    if (HasPermission(player, "cginvisibleitemframe.debugConsole")) {
                        Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_permission.replace("*player_name*", player.getName()));
                    }
                    event.getPlayer().sendMessage(ConfigReader.no_permission);
                    event.setCancelled(true);
                    continue;
                }
            }
            return;
        }else{
            if (HasPermission(player, "cginvisibleitemframe.use") || HasPermission(player,"cginvisibleitemframe.admin")) {
                item_frame(event);
            } else {
                if (HasPermission(player, "cginvisibleitemframe.debugConsole")) {
                    Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_permission.replace("*player_name*", player.getName()));
                }
                event.getPlayer().sendMessage(ConfigReader.no_permission);
                event.setCancelled(true);
            }
            return;
        }
    }

    public Boolean HasPermission(Player player, String Permission){
        if(Permission == null){ Permission = "" ;}
        if(player.hasPermission(Permission) && Permission.equals("cginvisibleitemframe.admin")){
            return true;
        }
        if(ConfigReader.debugConsole_msg_default_op &&
           player.isOp() &&
           Permission.equals("cginvisibleitemframe.debugConsole")) return true;
        if(ConfigReader.debugPlayer_msg_default_op &&
           player.isOp() &&
           Permission.equals("cginvisibleitemframe.debugPlayer")) return true;
        if(player.isOp()) return true; else return player.hasPermission(Permission);
    }

    public void item_frame (PlayerInteractEntityEvent event){
        if (event.isCancelled()) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof ItemFrame)) return;
        Player player = event.getPlayer();

        ItemFrame entity = (ItemFrame)event.getRightClicked();
        Block facing = entity.getLocation().getBlock().getRelative(entity.getAttachedFace());
        if (facing.getType().isInteractable()) {
            PlayerInteractEvent interactBlock = new PlayerInteractEvent(event.getPlayer(), Action.RIGHT_CLICK_BLOCK, event.getPlayer().getInventory().getItemInMainHand(), facing, entity.getAttachedFace());
            CGInvisibleItemFrame.instance.getServer().getPluginManager().callEvent((Event)interactBlock);
            if (interactBlock.useInteractedBlock() == Event.Result.DENY) {
                entity.setRotation(entity.getRotation().rotateCounterClockwise());
                return;
            }
        }
        if (event.getPlayer().isSneaking() && entity.getItem().getType() != Material.AIR ) {
            entity.setRotation(entity.getRotation().rotateCounterClockwise());
            entity.setVisible(!entity.isVisible());
            return;
        }
        if (entity.isVisible()) {
            return;
        }
        entity.setRotation(entity.getRotation().rotateCounterClockwise());
        if (!(facing.getState() instanceof InventoryHolder)) {
            return;
        }
        if (facing.getState() instanceof org.bukkit.block.Chest) {
            if (ItemFrameListener.getRelativeChestFace(facing) != null && facing.getRelative(ItemFrameListener.getRelativeChestFace(facing)).getRelative(0, 1, 0).getType().isOccluding()) {
                return;
            }
            if (facing.getRelative(0, 1, 0).getType().isOccluding()) {
                return;
            }
        }
        Inventory inv = ((InventoryHolder)facing.getState()).getInventory();
        event.getPlayer().openInventory(inv);
    }

    public Boolean plot_InPlotRoad(Player player) {
        PlotAPI api = new PlotAPI();
        api.registerListener(this);
        PlotPlayer plotplayer = api.wrapPlayer(player.getUniqueId());
        Location plotLoc = plotplayer.getLocation();
        if(plotLoc.isPlotRoad()) {
            return true;
        } else if(plotLoc.isPlotArea()){
            return false;
        }
        return null;
    }

    public Boolean plot_checkowner(Player player) {
        PlotAPI api = new PlotAPI();
        api.registerListener(this);
        PlotPlayer plotplayer = api.wrapPlayer(player.getUniqueId());
        Location plotLoc = plotplayer.getLocation();
        PlotArea plotArea = PlotSquared.get().getPlotAreaManager().getPlotArea(plotLoc);
        Plot plot = plotArea.getPlotAbs(plotLoc);
        Boolean isOwner = plot.isOwner(player.getUniqueId());
        Boolean isTrust =  plot.isAdded(player.getUniqueId());
        if(player.isOp()) { return true; }

        if(isOwner){
            if(HasPermission(player, "cginvisibleitemframe.debugConsole")){
                Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_isowner.replace("*player_name*", player.getName()));
            }
            if(HasPermission(player, "cginvisibleitemframe.debugPlayer")){
                player.sendMessage(ConfigReader.debug_player_plot_isowner);
            }
            return true;
        }else if(isTrust) {
            if(HasPermission(player, "cginvisibleitemframe.debugConsole")){
                Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_istrust.replace("*player_name*", player.getName()));
            }
            if(HasPermission(player, "cginvisibleitemframe.debugPlayer")) {
                player.sendMessage(ConfigReader.debug_check_plot_istrust);
            }
            return true;
        } else {
            if(HasPermission(player, "cginvisibleitemframe.debugConsole")){
                Bukkit.getServer().getConsoleSender().sendMessage(ConfigReader.debug_check_plot_no.replace("*player_name*", player.getName()));
            }
            if(HasPermission(player, "cginvisibleitemframe.debugPlayer")) {
                player.sendMessage(ConfigReader.debug_player_plot_no);
            }
            return false;
        }
    }

    public Boolean plot_InPlotWorld(Player player, String world) {
        PlotAPI api = new PlotAPI();
        api.registerListener(this);

        PlotPlayer plotplayer = api.wrapPlayer(player.getUniqueId());
        Location plotLoc = plotplayer.getLocation();
        PlotArea plotArea = api.getPlotSquared().getPlotAreaManager().getPlotArea(plotLoc);
        return plotArea.getWorldName().equals(world);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!Bukkit.getVersion().contains("1.16")) {
            return;
        }
        if (!(event.getInventory().getHolder() instanceof DoubleChest)) {
            return;
        }
        DoubleChest b = (DoubleChest)event.getInventory().getHolder();
        NMS.playChestCloseAnimation(((org.bukkit.block.Chest)b.getLeftSide()).getBlock());
        NMS.playChestCloseAnimation(((org.bukkit.block.Chest)b.getRightSide()).getBlock());
    }

    @EventHandler(priority=EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if(! (event.getEntityType().name().contains("ITEM_FRAME"))) return;

        ItemFrame entity = (ItemFrame)event.getEntity();
        if (entity.isVisible()) {
            return;
        }
        event.setCancelled(true);
    }

    public static BlockFace getRelativeChestFace(Block block) {
        Chest chest = (Chest)block.getBlockData();
        BlockFace face = ((Chest)block.getBlockData()).getFacing();
        BlockFace relativeFace = null;
        if (chest.getType() == Chest.Type.LEFT) {
            if (face == BlockFace.NORTH) {
                relativeFace = BlockFace.EAST;
            } else if (face == BlockFace.SOUTH) {
                relativeFace = BlockFace.WEST;
            } else if (face == BlockFace.WEST) {
                relativeFace = BlockFace.NORTH;
            } else if (face == BlockFace.EAST) {
                relativeFace = BlockFace.SOUTH;
            }
        } else if (chest.getType() == Chest.Type.RIGHT) {
            if (face == BlockFace.NORTH) {
                relativeFace = BlockFace.WEST;
            } else if (face == BlockFace.SOUTH) {
                relativeFace = BlockFace.EAST;
            } else if (face == BlockFace.WEST) {
                relativeFace = BlockFace.SOUTH;
            } else if (face == BlockFace.EAST) {
                relativeFace = BlockFace.NORTH;
            }
        }
        return relativeFace;
    }
}