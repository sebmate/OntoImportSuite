// See: http://www.java-forum.org/java-faq-beitraege/39070-jtable-teil-8-sortieren.html

import java.awt.Point;
import java.util.Comparator;

public class PointComparator implements Comparator<Point> {

    public int compare(Point o1, Point o2) {
        if (o1.x < o2.x) {
            return -1;
        } else if (o1.x > o2.x) {
            return 1;
        } else if (o1.y < o2.y) {
            return -1;
        } else if (o1.y > o2.y) {
            return 1;
        } else {
            return 0;
        }
    }
}
