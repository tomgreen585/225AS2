import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Estate enums that represent the estates in the game
 */
public enum Estate {
    VISITATION_VILLA("Visitation Villa"),
    HAUNTED_HOUSE("Haunted House"),
    MANIC_MANOR("Manic Manor"),
    PERIL_PALACE("Peril Palace"),
    CALAMITY_CASTLE("Calamity Castle");
    
    // variables to hold estate info
    private String name;
    private List<Point> doorLocations;
    private List<Point> weaponLocations;
    private List<Player> playersInEstate = new ArrayList<>();
    private List<String> weaponCards;

    Estate(String name) {
        this.name = name;
        doorLocations = new ArrayList<>();
        weaponCards = new ArrayList<>();
        weaponLocations = new ArrayList<>();
        addDoors();
    }
    
    /**
     *  Add weapon cards to estate
     * @param card - card that will be added to estate
     */
    public void addWeaponCard(String card) {
        switch (name.toLowerCase()) {
            case "visitation villa":
                weaponCards.add(card);
                break;
            case "haunted house":
                weaponCards.add(card);
                break;
            case "manic manor":
                weaponCards.add(card);
                break;
            case "peril palace":
                weaponCards.add(card);
                break;
            case "calamity castle":
                weaponCards.add(card);
                break;
            default:
                throw new IllegalArgumentException("Unknown card in estate: " + name);
        }
    }
    
    // add doors and their locations to estate
    private void addDoors() {
        switch (name.toLowerCase()) {
            case "visitation villa":
                doorLocations.add(new Point(11, 13));
                doorLocations.add(new Point(14, 12));
                doorLocations.add(new Point(13, 10));
                doorLocations.add(new Point(12, 15));
                break;
            case "haunted house":
                doorLocations.add(new Point(4, 7));
                doorLocations.add(new Point(7, 6));
                break;
            case "manic manor":
                doorLocations.add(new Point(6, 18));
                doorLocations.add(new Point(7, 21));
                break;
            case "peril palace":
                doorLocations.add(new Point(18, 19));
                doorLocations.add(new Point(21, 18));
                break;
            case "calamity castle":
                doorLocations.add(new Point(18, 4));
                doorLocations.add(new Point(19, 7));
                break;
            default:
                throw new IllegalArgumentException("Unknown estate: " + name);
        }
    }
    
    /**
     * return list of door locations for the given state
     * @return - list of doors locations, as points
     */
    public List<Point> getDoorLocations() {
        return doorLocations;
    }

    /**
     * return list of weapons in estate
     * @return - list of weapons as strings
     */
    public List<String> getWeaponCards() {
        return weaponCards;
    }
    
    /**
     * return players in estate
     * @return - list of players
     */
    public List<Player> getPlayersInEstate() {
        return playersInEstate;
    }

    /**
     * return name of estate
     * @return - estate name
     */
    public String getName() {
        return name;
    }

    /**
     * Add all weapons to a string to then be printed out
     * @return - string of weapons
     */
    public String getWeaponString(){
        String weaponString = "";
        for (String weapon : weaponCards){
            weaponString += " " + weapon;
        }
        return weaponString;
    }
}
