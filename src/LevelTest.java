import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @org.junit.jupiter.api.Test
    void getTitle() {
        Level level = new Level("Twinkle Twinkle Little Star", null, null);
        assertEquals("Twinkle Twinkle Little Star", level.getTitle());
    }

    @org.junit.jupiter.api.Test
    void getCreator() {
        Level level = new Level(null, "U-2", null);
        assertEquals("U-2", level.getCreator());
    }

    @org.junit.jupiter.api.Test
    void getDifficulty() {
        Level level = new Level(null, null, null);
        level.setDifficulty(3);
        assertEquals(3, level.getDifficulty());
    }

    @org.junit.jupiter.api.Test
    void getDurationString() {
        Level level0 = new Level(null, null, null), level1 = new Level(null, null, null), level2 = new Level(null, null, null), level3 = new Level(null, null, null);
        level0.setDuration(30);
        level1.setDuration(180);
        level2.setDuration(3600);
        level3.setDuration(9999);
        assertAll(
                () -> assertEquals("00:00:30", level0.getDurationString()),
                () -> assertEquals("00:03:00", level1.getDurationString()),
                () -> assertEquals("01:00:00", level2.getDurationString()),
                () -> assertEquals("02:46:39", level3.getDurationString())
        );
    }

    @org.junit.jupiter.api.Test
    void getSongPath() {
        Level level = new Level(null, null, null);
        level.setSongPath("vseu.wav");
        assertEquals("vseu.wav", level.getSongPath());
    }

    @org.junit.jupiter.api.Test
    void levelReadingAndWriting() throws IOException {
        Level level = new Level("Carnival of the Animals", "Camille Saint-Saëns", new StoredNote[][] {new StoredNote[] {new StoredNote(0, 0, 0)}, new StoredNote[] {new StoredNote(208, 1, 0)}, new StoredNote[] {}, new StoredNote[] {new StoredNote(416, 3, 0)}});
        level.setTempo(144);
        level.setSongPath("animaux.wav");
        level.setStartDelay(0);
        level.setDifficulty(2);
        level.saveToFile("animaux.json");
        Level level2 = Level.loadFromFile("animaux.json");
        level2.saveToFile("animaux2.json");
        Level level3 = Level.loadFromFile("animaux2.json");

        assertAll(
                () -> assertEquals(level2.getDurationString(), level3.getDurationString()),
                () -> assertEquals(level2.getSongPath(), level3.getSongPath()),
                () -> assertEquals(level2.getCreator(), level3.getCreator()),
                () -> assertEquals(level2.getDifficulty(), level3.getDifficulty()),
                () -> assertEquals(level2.getTitle(), level3.getTitle()),
                () -> assertEquals(level2.getStoredNote(0, 0).getPos(), level3.getStoredNote(0, 0).getPos()),
                () -> assertEquals(level2.getStoredNote(1, 0).getPos(), level3.getStoredNote(1, 0).getPos()),
                () -> assertEquals(level2.getStoredNote(3, 0).getPos(), level3.getStoredNote(3, 0).getPos())
        );
    }

    @org.junit.jupiter.api.Test
    void getTrackLength() {
        Level level = new Level(null, null, new StoredNote[][] {new StoredNote[] {new StoredNote(0, 0, 0), new StoredNote(300, 0, 0)}, new StoredNote[] {}, new StoredNote[] {}, new StoredNote[] {}});
        assertAll(
                () -> assertEquals(2, level.getTrackLength(0)),
                () -> assertEquals(0, level.getTrackLength(2))
        );
    }

    @org.junit.jupiter.api.Test
    void getStoredNote() {
        Level level = new Level(null, null, new StoredNote[][] {new StoredNote[] {new StoredNote(0, 0, 0)}, new StoredNote[] {}, new StoredNote[] {new StoredNote(208, 2, 0)}, new StoredNote[] {}});
        assertAll(
                () -> assertEquals(0, level.getStoredNote(0, 0).getPos()),
                () -> assertEquals(208, level.getStoredNote(2, 0).getPos())
        );
    }

    @org.junit.jupiter.api.Test
    void getNextNote() {
        Level level = new Level(null, null, new StoredNote[][] {new StoredNote[] {new StoredNote(0, 0, 0)}, new StoredNote[] {}, new StoredNote[] {new StoredNote(208, 2, 0)}, new StoredNote[] {}});
        assertAll(
                () -> assertEquals(0, level.getNextNote(0, 0).getPos()),
                () -> assertEquals(208, level.getNextNote(2, 208).getPos())
        );
    }
}
