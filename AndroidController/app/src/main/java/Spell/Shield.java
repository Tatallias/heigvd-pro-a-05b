package Spell;

import Spell.Spell;

public class Shield extends Spell {


    /**
     * forms the request readable by the server
     * @return the request to be sent
     */
    public String getRequest() {
        return "SHI$";
    }
}
