// class for storing note and position data in the Level class' noteGrid
public class StoredNote {
    // position of the note in milliseconds
    private float posMillis;
    // note object
    private Note note;
    // create a StoredNote
    public StoredNote(float pos, int col) {
        this.posMillis = pos;
        this.note = new Note(col);
    }
    // get the position of the StoredNote
    public float getPos() {
        return posMillis;
    }
    // get the note of the StoredNote
    public Note getNote() {
        return note;
    }
}
