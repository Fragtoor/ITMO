package items;

public final class MedicalItem extends Item {
    public MedicalItem(String medicalType) {
        super(medicalType);
    }

    @Override
    public String use() {
        return "Использовался медицинский предмет: " + getItemTitle();
    }
}
