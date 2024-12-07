// 38 lines

// class to store users settings configurations
public class SettingsConfig {
    // these should be set to defaults
    private int volumeSfx = 80;
    private int volumeMusic = 90;
    private int frameRate = 60;
    private boolean ghostTap = false;
    private int latency = 0;
    private char key1 = 'Z';
    private char key2 = 'X';
    private char key3 = '.';
    private char key4 = '/';
    public SettingsConfig() {}
    // constructor with parameters
    public SettingsConfig(int sfx, int music, int frameRate, boolean ghostTap
            , int latency, char key1, char key2, char key3, char key4) {
        this.volumeSfx = sfx;
        this.volumeMusic = music;
        this.frameRate = frameRate;
        this.ghostTap = ghostTap;
        this.latency = latency;
        this.key1 = key1;
        this.key2 = key2;
        this.key3 = key3;
        this.key4 = key4;
    }
    // getters
    public int getVolumeSfx() { return volumeSfx; }
    public int getVolumeMusic() { return volumeMusic; }
    public int getFrameRate() { return frameRate; }
    public boolean getGhostTap() { return ghostTap; }
    public int getLatency() { return latency; }
    public char getKey1() { return key1; }
    public char getKey2() { return key2; }
    public char getKey3() { return key3; }
    public char getKey4() { return key4; }
    // setters
    public void setVolumeSfx(int sfx) { this.volumeSfx = sfx; }
    public void setVolumeMusic(int music) { this.volumeMusic = music; }
    public void setFrameRate(int frameRate) { this.frameRate = frameRate; }
    public void setGhostTap(boolean ghostTap) { this.ghostTap = ghostTap; }
    public void setLatency(int latency) { this.latency = latency; }
    public void setKey1(char key1) { this.key1 = key1; }
    public void setKey2(char key2) { this.key2 = key2; }
    public void setKey3(char key3) { this.key3 = key3; }
    public void setKey4(char key4) { this.key4 = key4; }
}