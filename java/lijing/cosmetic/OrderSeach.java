package lijing.cosmetic;

import com.alibaba.excel.event.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lijing.cosmetic.JDBC.DruidUtil;
import lijing.cosmetic.Order.Orderlmpl;
import lijing.cosmetic.Order.orDer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class OrderSeach {
    @FXML
    private TextField ID;
    @FXML
    private TextField Name;
    @FXML
    private TextField CosmeticID;
    @FXML
    private TableView<orDer> orderTableView;
    @FXML
    private TableColumn<orDer, String> orderID;
    @FXML
    private TableColumn<orDer, String> customerName;
    @FXML
    private TableColumn<orDer, String> cosmeticID;
    @FXML
    private TableColumn<orDer, String> cosmeticName;
    @FXML
    private TableColumn<orDer, String> Quantity;
    @FXML
    private TableColumn<orDer, String> count;
    @FXML
    private TableColumn<orDer, String> Totalprice;


    @FXML
    private void seaching(ActionEvent actionEvent) {

            try (Connection connection = DruidUtil.getConnection()) {
                Orderlmpl orderDao = new Orderlmpl(connection);
                List<orDer> list=orderDao.getFilteredData(ID.getText(),Name.getText(),CosmeticID.getText());

                updateTableView(list);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }


    //在JavaFX应用程序加载FXML文件时执行，通常用于初始化UI元素和设置初始状态。
    private void updateTableView(List<orDer> orders) {
// 传入List集合，更新TableView
        orderTableView.getItems().setAll(orders);
// 为每列设置单元格值工厂，以从学生对象中获取属性值显示。
        orderID.setCellValueFactory(new PropertyValueFactory<>
                ("orderID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>
                ("customerName"));
        cosmeticID.setCellValueFactory(new PropertyValueFactory<>
                ("cosmeticID"));
        cosmeticName.setCellValueFactory(new PropertyValueFactory<>
                ("cosmeticName"));
        Quantity.setCellValueFactory(new PropertyValueFactory<>
                ("Quantity"));
        count.setCellValueFactory(new PropertyValueFactory<>
                ("count"));
        Totalprice.setCellValueFactory(new PropertyValueFactory<>
                ("Totalprice"));
        // 传入List集合，更新TableView
        /*studentTableView.getColumns().addAll(ID,name,birthday,sex,phone);
        ObservableList<Stu> data = FXCollections.observableList(students);
        studentTableView.setItems(data);*/
    }

}
