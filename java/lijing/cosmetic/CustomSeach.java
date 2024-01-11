package lijing.cosmetic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lijing.cosmetic.Custom.Customlmpl;
import lijing.cosmetic.Custom.custom;
import lijing.cosmetic.JDBC.DruidUtil;

/**
 * 客户模糊查询
 */
import java.sql.Connection;
import java.util.List;

public class CustomSeach {
    @FXML
    private TextField Customername;
    @FXML
    private TextField Phone;
    @FXML
    private RadioButton yesButton;
    @FXML
    private ToggleGroup rVIP;
    @FXML
    private RadioButton NoButton;

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
    private TableColumn<custom,String> cosmeticID;
    @FXML
    private TableColumn<custom,String> isVIP;
    @FXML
    private TableColumn<custom,String> count;
    @FXML
    private TableColumn<custom,String> buydays;

    /**
     * 更新查找到的数据到表格中
     * @param customs
     */
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
     * 模糊查询
     * @param actionEvent
     */
    @FXML
    private void seaching(ActionEvent actionEvent) {
        // 获取所选的 RadioButton
        RadioButton selectedRadioButton = (RadioButton) rVIP.getSelectedToggle();
        if(selectedRadioButton == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            // 设置消息框的标题
            alert.setTitle("警告");
            // 设置消息框的头部文本
            alert.setHeaderText("请选择是否为会员");
            // 显示消息框并等待用户点击按钮
            alert.showAndWait();
        }else {
            try (Connection connection = DruidUtil.getConnection()) {
                Customlmpl customDao = new Customlmpl(connection);
                System.out.println(Customername.getText()+Phone.getText()+CountSet());
                List<custom> list=customDao.getFilteredData(Customername.getText(),Phone.getText(),CountSet());
                updateTableView(list);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }

    /**
     * 获得按钮选择内容
     * @return
     */
    public String  CountSet(){
        // 获取所选的 RadioButton
        RadioButton selectedRadioButton = (RadioButton) rVIP.getSelectedToggle();

        // 检查所选的 RadioButton 并获取它的 userData（用户数据）
        if (selectedRadioButton != null) {
            String selectedValue = selectedRadioButton.getUserData().toString();
            return selectedValue;
        } else {
            return null;
        }
    }
}
