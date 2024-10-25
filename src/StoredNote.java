// class for storing note and position data in the Level class' noteGrid
public class StoredNote {
    // position of the note in milliseconds
    private long posMillis;
    // note object
    private Note note;
    // create a StoredNote
    public StoredNote(long pos, int col) {
        this.posMillis = pos;
        this.note = new Note(col);
    }
    
    // get the position of the StoredNote
    public long getPos() {
        return posMillis;
    }
    // get the note of the StoredNote
    public Note getNote() {
        return note;
    }
}
