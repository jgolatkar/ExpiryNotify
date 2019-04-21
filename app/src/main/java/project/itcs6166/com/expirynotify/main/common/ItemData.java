package project.itcs6166.com.expirynotify.main.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemData {
    private static List<Map<String, Object>> itemData = new ArrayList<>();
    private static List<String> itemNames = new ArrayList<>();
    private static List<String> expiryDates = new ArrayList<>();
    public static List<Map<String, Object>> getItemData() {
        return itemData;
    }

    public static void setItemData(List<Map<String, Object>> itemData) {
        ItemData.itemData = itemData;
    }

    public static List<String> getItemNames() {
        return itemNames;
    }

    public static List<String> getExpiryDates() {
        return expiryDates;
    }

    public static void removeItemData(int position){
        itemData.remove(position);
    }
}
