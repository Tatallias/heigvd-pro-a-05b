package Spell;

/**
 * class representing a newly channeled element with a quality qualifier
 */

public class ChanneledElement extends Spell {
    private Element element;
    private SpellQuality quality;
    public ChanneledElement(Element element, SpellQuality quality) {
        this.element = element;
        this.quality = quality;

    }

    /**
     * {@link Element} getter
     * @return Element
     */
    public Element getElement() {
        return element;
    }

    /**
     *  SpellQuality getter
     * @return SpellQuality
     */
    public SpellQuality getQuality() {
        return quality;
    }

    /**
     * forms the request readable by the server
     * @return the request to be sent
     */
    @Override
    public String getRequest() {
        return "CHA "+element.getProtocolInt()+" "+quality.ordinal()+"$";
    }
}
