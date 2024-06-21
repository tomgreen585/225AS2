
/**
 * Door class represents a door for the estates
 * 
 * Stores doors
 *  - row
 *  - column
 *  - estate the door is from
 */
public class Door {
    private Estate estate;  // estate where the door is
    private int row, col;   // position

    /*
     * Constructor for the door
     * takes in door row, column, and estate its connected to 
     * 
     */
    public Door(int row, int col, Estate estate) {
        this.estate = estate;
        this.row = row;
        this.col = col;
    }
    
    /**
     * returns the estate the door is connected to
     * @return - returns estate eunum
     */
    public Estate getEstate() {
        return estate;
    }
    
    /**
     * returns the row of the door
     * @return - row, as an integer 
     */
    public int getRow() {
        return row;
    }

    /**
     * returns the column of the door
     * @return - col, as an integer
     */
    public int getCol() {
        return col;
    }
}
