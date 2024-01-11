package lijing.cosmetic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lijing.cosmetic.Custom.Customlmpl;
import lijing.cosmetic.Custom.custom;
import lijing.cosmetic.JDBC.DruidUtil;

import java.io.IOException;
import java.sql.Connection;

import java.util.List;

/**
 * 客户信息界面控制器
 */
public class Custom1 {
    @FXML
    private TextField newAddress;
    @FXML
    private TextField newPhone;
    @FXML
    private TableView<custom> customTableView;
    @FXML
    private TableColumn<custom,String> customName;
    @FXML
    private TableColumn<custom,String> sex;
    @FXML
    private TableColumn<custom,String> phone;
    @FXML
    private TableColumn <custom,String> address;
    @FXML
    private TableColumn<custom,String> isVIP;
    @FXML
    private TableColumn<custom,String> count;

    @FXML
    private Pagination pageTable;
    private int itemsPerPage = 30;//每页显示的数据条数


    /**
     * 跳转页面进行模糊查询
     * @param actionEvent
     */
    @FXML
    private void seach(ActionEvent actionEvent) {
        try {
            // 加载新窗口的FXML文件
            Parent parent = new FXMLLoader(getClass().getResource("customSeach.fxml")).load();

            // 创建新的Stage
            Stage newStage = new Stage();
            Scene scene = new Scene(parent);
            newStage.setScene(scene);

            newStage.setTitle("查询信息");
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.setResizable(false);
            // 显示新窗口
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 在JavaFX应用程序加载FXML文件时执行，通常用于初始化UI元素和设置初始状态。
     * @param customs
     */
    //在JavaFX应用程序加载FXML文件时执行，通常用于初始化UI元素和设置初始状态。
    private void updateTableView(List<custom> customs) {
        // 传入List集合，更新TableView
        customTableView.getItems().setAll(customs);
        // 为每列设置单元格值工厂，以从对象中获取属性值显示。
        customName.setCellValueFactory(new PropertyValueFactory<>
                ("customName"));
        sex.setCellValueFactory(new PropertyValueFactory<>
                ("sex"));
        phone.setCellValueFactory(new PropertyValueFactory<>
                ("phone"));
        address.setCellValueFactory(new PropertyValueFactory<>
                ("address"));
        isVIP.setCellValueFactory(new PropertyValueFactory<>
                ("isVIP"));
        count.setCellValueFactory(new PropertyValueFactory<>
                ("count"));
    }

    /**
     * 计算总页数，基于总记录数和每页显示的条数。
     * @return
     */
    //计算总页数，基于总记录数和每页显示的条数。
    private int calculatePageCount() {
        try (Connection connection = DruidUtil.getConnection()) {
            Customlmpl customDao = new Customlmpl(connection);
// 计算总页数，并向上取整
            return (int) Math.ceil((double)
                    customDao.getTotalRecords() / itemsPerPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 加载指定页码的数据到 TableView 中
     * @param pageIndex
     */
    //加载指定页码的数据到 TableView 中
    private void loadPageData(int pageIndex) {
        try (Connection connection = DruidUtil.getConnection()) {
            Customlmpl customDao = new Customlmpl(connection);
    // 获取每页数据
            List<custom> pageData = customDao.selectByPage(pageIndex + 1, itemsPerPage);
    // 将数据加载到 TableView 中
            updateTableView(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据新的页码加载数据
     */
    @FXML
    private void initPagination() {
        pageTable.setPageCount(calculatePageCount());
        pageTable.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
    // 根据新的页码加载数据
            loadPageData(newValue.intValue());
        });
    }

    /**
     * 刷新表格数据
     */
    // 刷新表格数据
    private void refreshTableView() {
    // 获取当前页码
        int currentPageIndex = pageTable.getCurrentPageIndex();
    // 获取新的总页数
        int newTotalPages = calculatePageCount();
    // 更新分页控件的页数
        pageTable.setPageCount(newTotalPages);
    // 重新加载当前页数据
        loadPageData(currentPageIndex);
    // 如果当前页为空且不是第一页，将页码减一，重新加载上一页数据
        if (customTableView.getItems().isEmpty() &&
                currentPageIndex > 0) {
            loadPageData(currentPageIndex - 1);
            pageTable.setCurrentPageIndex(currentPageIndex - 1);//设置当前页码
        }
    }

    /**
     * 初始化表格内容
     */
    @FXML
    private void initialize() {
        refreshTableView();//刷新
        initPagination();//分页
        loadPageData(0);
    }

    /**
     * 根据主键更新
     * @param actionEvent
     */
    @FXML
    private void update(ActionEvent actionEvent) {
    if (newPhone.getText().isEmpty() && newAddress.getText().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        // 设置消息框的标题
        alert.setTitle("警告");
        // 设置消息框的头部文本
        alert.setHeaderText("化妆品信息不完整，无法进行编辑！");
        // 显示消息框并等待用户点击按钮
        alert.showAndWait();
    } else {
        try {
            Connection connection = DruidUtil.getConnection();
            Customlmpl customDao = new Customlmpl(connection);
            custom selected = customTableView.getSelectionModel().getSelectedItem();
            //根据主键更新
            custom c = customDao.selectBykey(selected.getCustomName());
            c.setPhone(newPhone.getText());
            c.setAddress(newAddress.getText());
            customDao.updateBykey(c, selected.getCustomName());
            refreshTableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
}
