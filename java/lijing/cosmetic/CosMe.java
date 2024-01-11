package lijing.cosmetic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lijing.cosmetic.IO.FileIOTool;
import lijing.cosmetic.IO.ObjectIOTool;
import lijing.cosmetic.IO.easyExcelTool;

import lijing.cosmetic.entity.Cosmeticlmpl;
import lijing.cosmetic.entity.cosMetic;
import lijing.cosmetic.JDBC.DruidUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 化妆品界面控制器
 */
public class CosMe {
    @FXML
    private Button Add;
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
    private Pagination page;//分页组件
    private final int itemsPerPage = 30;//每页显示的数据条数
    @FXML
    private TextField name;
    @FXML
    private TextField ID;
    @FXML
    private TextField Price;
    @FXML
    private DatePicker Date;
    @FXML
    private TextField Days;
    @FXML
    private TextField Harm;
    @FXML
    private TextField People;
    @FXML
    private TextField quantity;
    @FXML
    private TextField Type;
    @FXML
    private ChoiceBox<String> selected;

    /**
     * 判断所有信息是否输入完整
     * @return
     */
    private boolean isNull() {

        return (!name.getText().isEmpty()) && (!ID.getText().isEmpty()) && (!Price.getText().isEmpty())&&
                (!Days.getText().isEmpty())&& (!Harm.getText().isEmpty())&& (!People.getText().isEmpty())&&
                (!selected.getValue().isEmpty())&& (!quantity.getText().isEmpty())&&
                (!Date.getEditor().getText().isEmpty());
    }

    /**
     * 检查add按钮状态的方法
     */
    // 检查按钮状态的方法
    private void checkButtonState() {
        if(!isNull()){
            Add.setDisable(true);
        }else {
            Add.setDisable(false);
        }
    }

    /**
     * 编辑功能
     * @param actionEvent
     */
    @FXML
    private void update(ActionEvent actionEvent) {
    if (!isNull()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        // 设置消息框的标题
        alert.setTitle("警告");
        // 设置消息框的头部文本
        alert.setHeaderText("化妆品信息不完整，无法进行编辑！");
        // 显示消息框并等待用户点击按钮
        alert.showAndWait();
    } else {
        try (Connection connection = DruidUtil.getConnection()) {
            //连接数据库查询
            Cosmeticlmpl cosmeticDao = new Cosmeticlmpl(connection);
            cosMetic selectedcometics = cosMeticTableview.getSelectionModel().getSelectedItem();
            //设置新实体
            cosMetic x1=new cosMetic(name.getText(),ID.getText(),Integer.parseInt(Price.getText()),
                    Date.getEditor().getText(),Days.getText(),
                    Harm.getText(),People.getText(),selected.getValue(),
                    Integer.parseInt(quantity.getText()),Type.getText());
            //判断主键是否一致
            if(!x1.getCosmeticID().equals(selectedcometics.getCosmeticID())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                // 设置消息框的标题
                alert.setTitle("警告");
                // 设置消息框的头部文本
                alert.setHeaderText("报告编号不能修改");
                // 显示消息框并等待用户点击按钮
                alert.showAndWait();
            }else {
                System.out.println(x1);
                System.out.println(selectedcometics);
                //根据主键更新
                cosmeticDao.updateBykey(x1,selectedcometics.getCosmeticID());
                refreshTableView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

    /**
     * 增加数据功能
     * @param actionEvent
     */
    @FXML
    private void add(ActionEvent actionEvent) {
        //创建新的实体
    cosMetic x1=new cosMetic(name.getText(),ID.getText(),Integer.parseInt(Price.getText()),
            Date.getEditor().getText(),Days.getText(), Harm.getText(),People.getText(),
            selected.getValue(),Integer.parseInt(quantity.getText()),Type.getText());
    System.out.println(x1);
    try (Connection connection = DruidUtil.getConnection()) {
        //连接数据库
        Cosmeticlmpl cosmetictDao = new Cosmeticlmpl(connection);
        //单条插入
        cosmetictDao.insert(x1);
        refreshTableView();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

    /**
     * 查询功能，跳入新界面进行模糊查询
     * @param actionEvent
     */
    @FXML
    private void seach(ActionEvent actionEvent) {
    try {
        // 加载新窗口的FXML文件
        Parent parent = new FXMLLoader(getClass().getResource("cosmeticSeach.fxml")).load();

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
     * @param cosMetics
     */
    //在JavaFX应用程序加载FXML文件时执行，通常用于初始化UI元素和设置初始状态。
    private void updateTableView(List<cosMetic> cosMetics) {
    // 传入List集合，更新TableView
        cosMeticTableview.getItems().setAll(cosMetics);
    // 为每列设置单元格值工厂，以从对象中获取属性值显示。
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
    }

    /**
     * 计算总页数
     * @return
     */
    private int calculatePageCount() {
        try (Connection connection = DruidUtil.getConnection()) {
                Cosmeticlmpl cosmeticDao = new Cosmeticlmpl(connection);
    // 计算总页数，并向上取整
                return (int) Math.ceil((double)
                        cosmeticDao.getTotalRecords() / itemsPerPage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

    /**
     * 指定页码的数据到 TableView 中
     * @param pageIndex
     */
    //加载指定页码的数据到 TableView 中
        private void loadPageData(int pageIndex) {
            try (Connection connection = DruidUtil.getConnection()) {
                Cosmeticlmpl cosmeticDao = new Cosmeticlmpl(connection);
    // 获取每页数据
                List<cosMetic> pageData = cosmeticDao.selectByPage(pageIndex + 1, itemsPerPage);
    // 将数据加载到 TableView 中
                updateTableView(pageData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    /**
     * 新的页码加载数据
     */
    @FXML
    private void initPagination() {
            page.setPageCount(calculatePageCount());
            page.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
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
            int currentPageIndex = page.getCurrentPageIndex();
                // 获取新的总页数
            int newTotalPages = calculatePageCount();
            // 更新分页控件的页数
            page.setPageCount(newTotalPages);
            // 重新加载当前页数据
            loadPageData(currentPageIndex);
                // 如果当前页为空且不是第一页，将页码减一，重新加载上一页数据
            if (cosMeticTableview.getItems().isEmpty() &&
                    currentPageIndex > 0) {
                loadPageData(currentPageIndex - 1);
                page.setCurrentPageIndex(currentPageIndex - 1);//设置当前页码
            }
        }

    /**
     * 初始化表格
     */
    @FXML
    private void initialize() {
        refreshTableView();
        checkButtonState();//设置新增按钮
        // 设置TableView的多选模式
        cosMeticTableview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        initPagination();
        loadPageData(0);
    }
    @FXML
    private void check1(ActionEvent actionEvent) {
        checkButtonState();
    }
    @FXML
    private void check2(ActionEvent actionEvent) {
        checkButtonState();
    }
    @FXML
    private void check3(ActionEvent actionEvent) {
        checkButtonState();
    }
    @FXML
    private void check4(ActionEvent actionEvent) {
        checkButtonState();
    }
    @FXML
    private void check5(ActionEvent actionEvent) {
        checkButtonState();
    }
    @FXML
    private void check6(ActionEvent actionEvent) {
        checkButtonState();
    }
    @FXML
    private void check7(ActionEvent actionEvent) {
        checkButtonState();
    }
    @FXML
    private void check9(ActionEvent actionEvent) {checkButtonState();}

    @FXML
    private void check10(ActionEvent actionEvent) {checkButtonState();}

    /**
     * 导出文件
     * @param actionEvent
     */
    @FXML
    private void OutPut(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出文件");
        fileChooser.setInitialDirectory(new File("D:\\桌面\\Cosmetic"));//选择打开的文件
        //选择文件类型
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("All Files", "*.*")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Data files (*.data)", "*.data")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")));
        // 显示文件保存对话框
        File selectedFile = fileChooser.showSaveDialog(null);
        //连接数据库
        Connection connection=DruidUtil.getConnection();
        Cosmeticlmpl cosmeticlDao=new Cosmeticlmpl(connection);
        try {
            //获得该表中的所有数据
            List<cosMetic> list=cosmeticlDao.selectAll();
            //根据类型调用不同的工具类
            if (selectedFile == null)
            {return ;}
            else if(selectedFile.getName().contains(".txt")){
                System.out.println(selectedFile.getPath());
                FileIOTool.writeFile(list,selectedFile.getPath());
            }else if(selectedFile.getName().contains(".data")){
                System.out.println(selectedFile.getPath());
                ObjectIOTool.wirteObject(list,selectedFile.getPath());
            }else if(selectedFile.getName().contains(".xlsx")){
                System.out.println(selectedFile.getPath());
                easyExcelTool.writeExcel(list,selectedFile.getPath());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 导入文件
     * @param actionEvent
     */
    @FXML
    private void Input(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出文件");
        fileChooser.setInitialDirectory(new File("D:\\桌面\\Cosmetic"));
        //选择类型
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("All Files", "*.*")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Data files (*.data)", "*.data")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")));
        //显示文件保存对话框
        File selectedFile = fileChooser.showOpenDialog(null);
        Connection connection=DruidUtil.getConnection();
        Cosmeticlmpl cosmeticlDao=new Cosmeticlmpl(connection);
        List<cosMetic> newlist=new ArrayList<>();
        try {
            //根据类型导入，用一个集合存储下来
            if (selectedFile == null)
            {return ;}
            else if(selectedFile.getName().contains(".txt")){
                System.out.println(selectedFile.getPath());
                 newlist=FileIOTool.readfile(selectedFile.getPath());
                FileIOTool.readfile(selectedFile.getPath());
            }else if(selectedFile.getName().contains(".data")){
                System.out.println(selectedFile.getPath());
                 newlist=ObjectIOTool.readObject(selectedFile.getPath(),cosMetic.class);
            }else if(selectedFile.getName().contains(".xlsx")){
                System.out.println(selectedFile.getPath());
                 newlist=easyExcelTool.readExcel(selectedFile.getPath(),cosMetic.class);
            }
           // System.out.println(newlist);
            //如果有重复的先删后插入，没有的直接插入，实现追加操作
            for (cosMetic NowCosmetic: newlist) {
                cosMetic selected=cosmeticlDao.selectBykey(NowCosmetic.getCosmeticID());
                if(!(selected ==null)){
                    //System.out.println(selected+"\n"+NowCosmetic);
                    cosmeticlDao.deleteByPrimaryKey(selected.getCosmeticID());
                    cosmeticlDao.insert(NowCosmetic);
                }else {
                    cosmeticlDao.insert(NowCosmetic);
                }
            }
            refreshTableView();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
