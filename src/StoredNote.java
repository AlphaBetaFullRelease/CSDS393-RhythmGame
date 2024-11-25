// class for storing note and position data in the Level class' noteGrid
public class StoredNote {
    // position of the note in milliseconds
    private int posMillis;
    // note object
    private Note note;
    // create a StoredNote
    public StoredNote(int pos, int col) {
        this.posMillis = pos;
        this.note = new Note(col, dur);
    }
    // get the position of the StoredNote
    public int getPos() {
        return posMillis;
    }
    // get the note of the StoredNote
    public Note getNote() {
        return note;
    }
}
