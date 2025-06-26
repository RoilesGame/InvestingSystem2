package view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class AppColors {
    public static final Color PRIMARY = new Color(0, 102, 204);
    public static final Color SECONDARY = new Color(51, 153, 255);
    public static final Color BACKGROUND = new Color(240, 248, 255);
    public static final Color TEXT = new Color(50, 50, 50);
    public static final Color ERROR = new Color(204, 0, 0);
    public static final Color BUTTON_TEXT = new Color(70, 70, 70);

    public static final Color TABLE_HEADER_BACKGROUND = new Color(240, 240, 240);
    public static final Color TABLE_HEADER_FOREGROUND = Color.BLACK;
    public static final Color TABLE_GRID_COLOR = new Color(200, 200, 200);

    public static final Color BUTTON_BORDER = new Color(180, 180, 180);

    public static void styleTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BACKGROUND);
        header.setForeground(TABLE_HEADER_FOREGROUND);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setSelectionBackground(SECONDARY);
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
    }
}