/**
 * this represents a weapon in the game
 */
public class Weapon {
    private Estate estate;  // estate it is in
    private int row, col;   // position
    
    /**
     * weapon constructor
     * @param row - row of weapon
     * @param col - col of weapon
     * @param estate - the estate the weapon in inside of 
     */
    public Weapon(int row, int col, Estate estate) {
        this.estate = estate;
        this.row = row;
        this.col = col;
    }
    
    /**
     * return estate
     * @return - the estate the weapon is inside
     */
    public Estate getEstate() {
        return estate;
    }
    
    /**
     * return estate
     * @return - the row of weapon
     */
    public int getRow() {
        return row;
    }

    /**
     * return col
     * @return - the col of weapon
     */
    public int getCol() {
        return col;
    }
}
