// class for storing note and position data in the Level class' noteGrid
public class StoredNote implements Comparable<StoredNote> {
    // position of the note in milliseconds
    private long posMillis;
    // note object
    private Note note;
    // create a StoredNote
    public StoredNote(long pos, int col, int dur) {
        this.posMillis = pos;
        this.note = new Note(col, dur);
    }
    
    // get the position of the StoredNote
    public long getPos() {
        return posMillis;
    }
    // set the position of the storedNote
    public void setPos(long pos){
        posMillis = pos;
    }
    // get the note of the StoredNote
    public Note getNote() {
        return note;
    }

    // compare function to sort stored notes by pos
    @Override
    public int compareTo(StoredNote o) {
        return Long.compare(getPos(), o.getPos());
    }
}
