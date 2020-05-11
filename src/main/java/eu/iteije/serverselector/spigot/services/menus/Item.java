package eu.iteije.serverselector.spigot.services.menus;

import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class Item {

    @Getter private ItemStack item;

    @Getter private BiConsumer<Player, Item> onClick = (a, b) -> {};

    public Item(ItemStack itemStack) {
        if (itemStack == null) itemStack = new ItemStack(Material.AIR, 1);

        this.item = itemStack.clone();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
    }

    public Item(Material material) {
        this.item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    public Item setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public Item onClick(BiConsumer<Player, Item> onClick) {
        this.onClick = onClick;
        return this;
    }

    public Item setUnbreakable() {
        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
        return this;
    }

    public Item setColor(int r, int g, int b) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
        leatherArmorMeta.setColor(Color.fromRGB(r, g, b));
        item.setItemMeta(leatherArmorMeta);
        return this;
    }

    public Item setLore(String[] lines) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lines));
        item.setItemMeta(meta);
        return this;
    }

    public Item setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public Item setEnchanted() {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.ARROW_FIRE, 10 , true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return this;
    }

    public Item setEnchanted(Boolean state) {
        if (state) {
            setEnchanted();
        } else {
            ItemMeta meta = item.getItemMeta();
            meta.removeEnchant(Enchantment.ARROW_FIRE);
            item.setItemMeta(meta);
        }
        return this;
    }

}
