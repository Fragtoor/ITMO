package env;
import creatures.Creature;

import java.util.ArrayList;
import java.util.List;

public class Environment implements Audible {
    private final String locationName;
    private final ArrayList<SoundType> soundSources;
    private ArrayList<Creature> creaturesPresent;
    public Environment(String locationName) {
        this.locationName = locationName;
        this.soundSources = new ArrayList<>();
        this.creaturesPresent = new ArrayList<>();
    }

    public Environment(String locationName, ArrayList<Creature> creaturesPresent, ArrayList<SoundType> soundSources) {
        this.locationName = locationName;
        this.creaturesPresent = creaturesPresent;
        this.soundSources = soundSources;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public ArrayList<SoundType> getSoundSources() {
        return soundSources;
    }
    
    public ArrayList<Creature> getCreaturesPresent() {
        return creaturesPresent;
    }
    
    public void addCreatures(Creature... creatures) {
        creaturesPresent.addAll(List.of(creatures));
    }
    
    public void addSoundSource(SoundType... sounds) {
        soundSources.addAll(List.of(sounds));
    }
    
    @Override
    public SoundType makeSound() {
        int index = (int)(Math.random() * soundSources.size());
        SoundType sound = soundSources.get(index);
        return sound;
    }
}
