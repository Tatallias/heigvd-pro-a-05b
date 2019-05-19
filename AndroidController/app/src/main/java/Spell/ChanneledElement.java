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

    public Element getElement() {
        return element;
    }

    public SpellQuality getQuality() {
        return quality;
    }

    @Override
    public String getRequest() {
        return "CHA "+element.getProtocolInt()+" "+quality.ordinal()+"$";
    }
}
