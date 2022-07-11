package fr.loria.ecoo.so6.xml.node;

public class PathElement {

    private final int position;
    private final String elementName;

    public PathElement(int position, String elementName) {
        this.position = position;
        this.elementName = elementName;
    }

    public int getPosition() {
        return position;
    }

    public String getElementName() {
        return elementName;
    }

}
