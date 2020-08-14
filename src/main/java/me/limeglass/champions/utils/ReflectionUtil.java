package me.limeglass.champions.utils;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {
	
	public static String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	}
	
	public static Class<?> getNMSClass(String classString) throws ClassNotFoundException {
		String name = "net.minecraft.server." + getVersion() + classString;
        return Class.forName(name);
	}
	
	public static Class<?> getOBCClass(String classString) {
		String name = "org.bukkit.craftbukkit." + getVersion() + classString;
		@SuppressWarnings("rawtypes")
		Class obcClass = null;
		try {
			obcClass = Class.forName(name);
		}
		catch (ClassNotFoundException error) {
			error.printStackTrace();
			return null;
		}
		return obcClass;
	}
	
	public static Object getConnection(Player player) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Object nmsPlayer = getHandle(player);
		Field connectionField = nmsPlayer.getClass().getField("playerConnection");
		return connectionField.get(nmsPlayer);
	}
	
	public static <T> boolean setField(Class<T> from, Object obj, String field, Object newValue){
		try {
			Field f = from.getDeclaredField(field);
			f.setAccessible(true);
			f.set(obj, newValue);
			return true;
		} catch (Exception ignored){}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getField(String field, Class<?> from, Object obj){
		try{
			Field f = from.getDeclaredField(field);
			f.setAccessible(true);
			return (T) f.get(obj);
		} catch (Exception ignored){}
		return null;	
	}
	
	public static Object getHandle(Object obj) {
		if (obj != null) {
			try {
				Method getHandle = obj.getClass().getMethod("getHandle");
				getHandle.setAccessible(true);
				return getHandle.invoke(obj);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static void sendPacket(Object object, Player... players) throws NoSuchMethodException {
		try {
			for (Player player: players) {
				Method method = getConnection(player).getClass().getMethod("sendPacket", getNMSClass("Packet"));
				method.invoke(getConnection(player), object);
			}
		} catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Object getNMSBlock(Block block) {
		try {
			Method method = ReflectionUtil.getOBCClass("util.CraftMagicNumbers").getDeclaredMethod("getBlock", Block.class);
			method.setAccessible(true);
			return method.invoke(ReflectionUtil.getOBCClass("util.CraftMagicNumbers"), block);
		} catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
			e1.printStackTrace();
		}
		return null;
	}
}
