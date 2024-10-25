// class for storing note and position data in the Level class' noteGrid
public class StoredNote {
    // position of the note in milliseconds
    private int posMillis;
    // note object
    private Note note;
    // create a StoredNote
    public StoredNote(int pos, Note n) {
        this.posMillis = pos;
        this.note = n;
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
