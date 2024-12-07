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
}