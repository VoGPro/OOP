package org.example;

import javax.swing.table.TableModel;

public interface ICarView {
    void setVisible(boolean visible);
    void refreshView();
    void showMessage(String message, String title, MessageType type);
    void setPageInfo(int currentPage, int totalPages);
    void updateNavigationButtons(boolean hasPrevious, boolean hasNext);
    String getVinFilter();
    String getBrandFilter();
    String getModelFilter();
    void clearFilters();
    void setTableData(TableModel model);
    int getSelectedRow();
    String getValueAt(int row, int column);
    void addActionListener(String actionName, Runnable action);
}