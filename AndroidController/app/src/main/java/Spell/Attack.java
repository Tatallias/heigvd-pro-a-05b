package Spell;


import Utility.Vector2;

/**
 * class representing an attack with a Vector2 representing the direction of the attack
 */
public class Attack extends Spell {
    Vector2 direction;

    public Attack( Vector2 direction) {
        this.direction=direction;
    }

    /**
     * forms the request readable by the server
     * @return the request to be sent
     */
    public String getRequest(){
        String s = "ATT "+ direction.getX()+" "+direction.getY()+"$";
        return s;
    }
}
