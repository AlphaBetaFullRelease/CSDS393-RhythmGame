// class to store information about an active note on the screen
public class Note {
    // position (goes from 0-1 where 0 is beginning of the screen and 1 is the end of the screen)
    private double pos = 0;

    // which of the 4 possible columns the note will appear in(where 0-3 is left-more to right-most column)
    private int col;

    // duration of the note (where 0, 1, 2, ..., n determines its length in divisions 1/32, 1/16, 1/8, 1/4, 1/2, 1/1, ...)
    private int dur;
    
    public Note(int col, int dur){
        this.col = col % 4;
        this.dur = dur;
    }

    public double getPos(){
        return pos;
    }

    public int getCol(){
        return col;
    }

    public int getDur(){
        return dur;
    }

    public void updatePos(double val){
        this.pos += val;
    }

    public void setPos(double val){
        this.pos = val;
    }
}
