package model;

public class Territory {
    private Map map;
    private int dice;
    private int idNum;
    private Player player;

    /*
    Constructor for creating a Territory with a specified map

    Pre-condition: map cannot be null
    Post-condition: Territory initialized with default values for dice, idNum, and player

    Parameters:
    map (Map): The map this territory belongs to

    Returns: None
     */
    public Territory(Map map) {
        this.map = map;
        this.dice = -1;
        this.idNum = -1;
        this.player = null;
    }

    /*
    Constructor for creating a Territory with a specified map

    Pre-condition: map cannot be null
    Post-condition: Territory initialized with specified values for map, owner,
                    how many dice, and the ID number

    Parameters:
    map (Map): The map this territory belongs to
    owner (Player): The player who owns this territory
    dice (int): The dice value for this territory
    idNum (int): The identifier for this territory (based on RMO numbering)

    Returns: None
     */
    public Territory(Map map, Player owner, int dice, int idNum) {
        this.map = map;
        this.dice = dice;
        this.idNum = idNum;
        this.player = owner;
    }

    public int getDice() {
        return this.dice;
    }

    public int getIdNum() {
        return this.idNum;
    }

    public Map getMap() {
        return this.map;
    }

    public Player getOwner() {
        return this.player;
    }

    public void setDice(int dice) {
        this.dice = dice;
    }

    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    public void setOwner(Player player) {
        this.player = player;
    }

    public int getRow() {
        return this.idNum / map.COLUMNS;
    }

    public int getCol() {
        return this.idNum % map.COLUMNS;
    }

    public String toString() {
        String playerName = (this.player != null) ? this.player.getName() : "null";
        return "\nDice: " + this.dice +
                "\nID: " + this.idNum +
                "\nOwner: " + playerName +
                "\n";
    }
}
