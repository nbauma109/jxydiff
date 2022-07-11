package fr.loria.ecoo.so6.xml.node;

public class AttributePath extends Path {

    private final String attrName;

    public AttributePath(Path path, String attrName) {
        super(path.getPathElements());
        this.attrName = attrName;
    }

    public String getAttrName() {
        return attrName;
    }

    @Override
    public String getPseudoXPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.getPseudoXPath());
        sb.append("/@");
        sb.append(attrName);
        return sb.toString();
    }
}
