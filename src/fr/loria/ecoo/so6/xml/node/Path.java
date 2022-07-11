package fr.loria.ecoo.so6.xml.node;

import java.util.List;

public class Path {

    private final List<PathElement> pathElements;

    public Path(List<PathElement> pathElements) {
        this.pathElements = pathElements;
    }
    
    public String getPseudoXPath() {
        StringBuilder sb = new StringBuilder();
        for (PathElement pathElement : pathElements) {
            if (!pathElement.getElementName().isEmpty()) {
                sb.append('/');
                sb.append(pathElement.getElementName());
            }
        }
        return sb.toString();
    }

    public String getNumericPath() {
        StringBuilder sb = new StringBuilder();
        for (PathElement pathElement : pathElements) {
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append(pathElement.getPosition());
        }
        return sb.toString();
    }
    
    public List<PathElement> getPathElements() {
        return pathElements;
    }
    
    @Override
    public String toString() {
        return getPseudoXPath();
    }
}
