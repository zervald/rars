package rars.venus;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

	/*
Copyright (c) 2003-2006,  Pete Sanderson and Kenneth Vollmar

Developed by Pete Sanderson (psanderson@otterbein.edu)
and Kenneth Vollmar (kenvollmar@missouristate.edu)

Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the 
"Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, and to 
permit persons to whom the Software is furnished to do so, subject 
to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
 */

/*
 * Use this to render Monospaced and right-aligned data in JTables.
 * I am using it to render integer addresses and values that are stored as
 * Strings containing either the decimal or hexidecimal version
 * of the integer value.
 *
 * This also add the ellipsis on the left on narrow columns, so the low order bytes are show first.
 * e.g. "0x0000000000001234" is shown as "...01234" if not enough with in the column.
 *
 */
public class MonoRightCellRenderer extends DefaultTableCellRenderer {
    public static final Font MONOSPACED_PLAIN_12POINT = new Font("Monospaced", Font.PLAIN, 12);

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        cell.setFont(MONOSPACED_PLAIN_12POINT);
        cell.setHorizontalAlignment(SwingConstants.RIGHT);

        //  Determine the width available to render the text

        int availableWidth = table.getColumnModel().getColumn(column).getWidth();
        availableWidth -= (int)table.getIntercellSpacing().getWidth();
        Insets borderInsets = getBorder().getBorderInsets(cell);
        availableWidth -= (borderInsets.left + borderInsets.right);
        String cellText = cell.getText();
        FontMetrics fm = getFontMetrics( getFont() );

        //  Not enough space so start rendering from the end of the string
        //  until all the space is used up
        if (fm.stringWidth(cellText) > availableWidth) {
            String dots = "...";
            int textWidth = fm.stringWidth(dots);
            int i = cellText.length() - 1;

            for (; i > 0; i--) {
                textWidth += fm.charWidth(cellText.charAt(i));

                if (textWidth > availableWidth) {
                    break;
                }
            }

            cell.setText(dots + cellText.substring(i + 1));
        }

        return cell;
    }
}