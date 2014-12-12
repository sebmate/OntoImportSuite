
import java.awt.Component;
import java.awt.Point;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sebmate
 */
// Ein Renderer für java.awt.Point
public class PointRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Point point = (Point) value;
        String text = point.x + " / " + point.y;
        return super.getTableCellRendererComponent(table, text, isSelected,
                hasFocus, row, column);
    }
}
