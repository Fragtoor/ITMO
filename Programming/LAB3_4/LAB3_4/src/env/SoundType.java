package env;

public enum SoundType {
    WIND("звук ветра"), // звуки ветра
    FAMILIAR_EARTHLY("знакомый, земной звук"), // что-то знакомое, земное
    INCREDIBLE_NOISE("ужасный звук"), // ужасный шум
    SILENCE("спокойный звук"), // тишина
    SCREAM("крик"), // крик
    UNSETTLING_WHISPER("таинственный шёпот"), // таинственный шёпот
    BARKING("лай"), // лай
    LAUGHTER("смех"); // смех

    private String description;
    SoundType (String description) {
        this.description = description;
    }

    public String getDescription() {return description;}

}