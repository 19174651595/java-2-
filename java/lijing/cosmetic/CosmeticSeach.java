package lijing.cosmetic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lijing.cosmetic.JDBC.DruidUtil;
import lijing.cosmetic.entity.Cosmeticlmpl;
import lijing.cosmetic.entity.cosMetic;

import java.sql.Connection;
import java.util.List;

/**
 * 化妆品模糊查询
 */
public class CosmeticSeach {
@FXML
    private TextField name;
    @FXML
    private TableView<cosMetic> cosMeticTableview;
    @FXML
    private TableColumn<cosMetic,String> CosmeticName;
    @FXML
    private TableColumn<cosMetic,String> CosmeticID;
    @FXML
    private TableColumn<cosMetic, Integer> price;
    @FXML
    private TableColumn<cosMetic,String> productDate;
    @FXML
    private TableColumn<cosMetic,String> ShelfTime;
    @FXML
    private TableColumn<cosMetic,String> Harmfulness;
    @FXML
    private TableColumn<cosMetic,String> suitablePeople;
    @FXML
    private TableColumn<cosMetic,String> BeautyDegree;
    @FXML
    private TableColumn<cosMetic,String> Shopquantity;
    @FXML
    private TableColumn<cosMetic,String> type;
    @FXML
    private TextField ID;
    @FXML
    private ChoiceBox<String> selected;

    /**
     * 更新查询后的表格内容
     * @param cosMetics
     */
    private void updateTableView(List<cosMetic> cosMetics) {
    // 传入List集合，更新TableView
        cosMeticTableview.getItems().setAll(cosMetics);
        //为每列设置单元格值工厂，以从学生对象中获取属性值显示。
            CosmeticName.setCellValueFactory(new PropertyValueFactory<>
                ("CosmeticName"));
        CosmeticID.setCellValueFactory(new PropertyValueFactory<>
                ("CosmeticID"));
        price.setCellValueFactory(new PropertyValueFactory<>
                ("price"));
        productDate.setCellValueFactory(new PropertyValueFactory<>
                ("productDate"));
        ShelfTime.setCellValueFactory(new PropertyValueFactory<>
                ("ShelfTime"));
        Harmfulness.setCellValueFactory(new PropertyValueFactory<>
                ("Harmfulness"));
        suitablePeople.setCellValueFactory(new PropertyValueFactory<>
                ("suitablePeople"));
        BeautyDegree.setCellValueFactory(new PropertyValueFactory<>
                ("BeautyDegree"));
        Shopquantity.setCellValueFactory(new PropertyValueFactory<>
                ("Shopquantity"));
        type.setCellValueFactory(new PropertyValueFactory<>
                ("type"));
        // 传入List集合，更新TableView
        /*studentTableView.getColumns().addAll(ID,name,birthday,sex,phone);
        ObservableList<Stu> data = FXCollections.observableList(students);
        studentTableView.setItems(data);*/
    }

    /**
     * 查询
     * @param actionEvent
     */
    @FXML
    private void seaching(ActionEvent actionEvent) {
        try (Connection connection = DruidUtil.getConnection()) {
        Cosmeticlmpl cosmeticDao = new Cosmeticlmpl(connection);
        //调用JDBC里的方法
        List<cosMetic> list=cosmeticDao.getFilteredData(ID.getText(),selected.getValue(),name.getText());
        updateTableView(list);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
}
