package items;

public final class EquipmentItem extends Item {
    public EquipmentItem(String equipmentType) {super(equipmentType);
    }
    public String use(){
        return "Использовалось снаряжение: " + getItemTitle();
    }
}
