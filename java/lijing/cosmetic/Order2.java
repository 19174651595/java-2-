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
import lijing.cosmetic.Custom.Customlmpl;
import lijing.cosmetic.Custom.custom;
import lijing.cosmetic.IO.FileIOTool;
import lijing.cosmetic.IO.ObjectIOTool;
import lijing.cosmetic.IO.easyExcelTool;
import lijing.cosmetic.JDBC.DruidUtil;
import lijing.cosmetic.Order.Orderlmpl;
import lijing.cosmetic.Order.orDer;
import lijing.cosmetic.entity.Cosmeticlmpl;
import lijing.cosmetic.entity.cosMetic;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * 账单购买管理系统
 */
public class Order2 {
    @FXML
    private RadioButton WomanButton;
    @FXML
    private RadioButton ManButton;
    @FXML
    private TableView<orDer> orderTableView;
    @FXML
    private Pagination pageTable;
    private int itemsPerPage = 30;//每页显示的数据条数
    @FXML
    private TableColumn<orDer,String> orderID;
    @FXML
    private TableColumn<orDer,String> customerName;
    @FXML
    private TableColumn<orDer,String> cosmeticID;
    @FXML
    private TableColumn<orDer,String> cosmeticName;
    @FXML
    private TableColumn<orDer,String> Quantity;
    @FXML
    private TableColumn<orDer,String> count;
    @FXML
    private TableColumn<orDer,String> Totalprice;
    @FXML
    private TableColumn<Order2,String> buydays;
    @FXML
    private TextField CosmeticID;
    @FXML
    private TextField Customername;
    @FXML
    private ToggleGroup rsex;
    @FXML
    private TextField Phone;
    @FXML
    private TextField Address;
    @FXML
    private ToggleGroup rVIP;
    @FXML
    private TextField Count;
    @FXML
    private TextField Days;
    @FXML
    private Button Add;
    @FXML
    private RadioButton yesButton;
    @FXML
    private RadioButton NoButton;

    /**
     * 删除账单购买信息
     * @param actionEvent
     */
    @FXML
    private void delete(ActionEvent actionEvent) {
        orDer selectedorder = orderTableView.getSelectionModel().getSelectedItem();
        try (Connection connection = DruidUtil.getConnection()) {
            //先将三个数据库连接
            Customlmpl customDao = new Customlmpl(connection);
            Orderlmpl orderDao=new Orderlmpl(connection);
            Cosmeticlmpl cosmeticDao=new Cosmeticlmpl(connection);
            //根据账单里的客户名获得其客户信息对象
            custom selectedcustoms=customDao.selectSometThing(selectedorder.getCustomerName());
            System.out.println(selectedcustoms);
            System.out.println(selectedorder);
        /* 使用系统确认框询问*/
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("您确定要删除选定的用户信息吗？");
        // 显示对话框，并等待按钮返回
        Optional<ButtonType> result = alert.showAndWait();
        //判断返回的按钮类型是确定还是取消，再据此分别进一步处理
        if (result.get() == ButtonType.OK && selectedcustoms != null && selectedorder.getBuydays() < 7) //判断购买天数是否超过7天，超过7天无法退款
        {
            System.out.println("-----");
            //根据账单中的化妆品ID获得其化妆品对象
            cosMetic x=cosmeticDao.selectBykey(selectedorder.getCosmeticID());
            //删除后化妆品库存应该增加
            // System.out.println(x.getShopquantity());
                int newQuqutity =x.getShopquantity()+selectedorder.getQuantity();
                //设置新的获得的化妆品对象的库存数量
                x.setShopquantity(newQuqutity);
               System.out.println(x.getShopquantity());
               //再根据化妆品ID与对象更新化妆品信息表
                cosmeticDao.updateBykey(x,x.getCosmeticID());
                //删除该账单信息
                orderDao.deleteByPrimaryKey(selectedorder.getOrderID());
                //获得删除后其账单中所有客户名的信息，存入orDerCustomList
                List<orDer> orDerList=orderDao.selectAll();
                List<String> orDerCustomList=new ArrayList<>();
            for (lijing.cosmetic.Order.orDer orDer : orDerList) {
                orDerCustomList.add(orDer.getCustomerName());
            }
            //如果删除后这个账单中没有这个客户，说明这个客户只购买了一个物品，则删除，反之，不能删除客户信息
            if(!orDerCustomList.contains(selectedcustoms.getCustomName())){
                //调用方法根据客户名在删除客户信息表中该客户的信息
                customDao.deleteBySomething(selectedorder.getCustomerName());
            }
                //刷新数据表格
                refreshTableView();

        }else {
            Alert alert2 = new Alert(Alert.AlertType.WARNING);
            // 设置消息框的标题
            alert2.setTitle("警告");
            // 设置消息框的头部文本
            alert2.setHeaderText("已超过7天无理由退款时间，无法删除！");
            // 显示消息框并等待用户点击按钮
            alert2.showAndWait();
        }} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新增账单
     * @param actionEvent
     */
    @FXML
    private void add(ActionEvent actionEvent) {
        RadioButton selected = (RadioButton) rsex.getSelectedToggle();
        RadioButton selected2 = (RadioButton) rVIP.getSelectedToggle();
        //先根据输入框的内容创建客户对象
        custom c1 = new custom(Customername.getText(),(String) selected.getUserData(), Phone.getText(),
                Address.getText(),(String) selected2.getUserData(),
                Double.parseDouble(Count.getText()));
        //连接三个数据库
        try (Connection connection = DruidUtil.getConnection()) {
            Cosmeticlmpl coke2=new Cosmeticlmpl(connection);
            //先判断买的化妆品ID本店是否存在，使用selectAll()方法，再循环加入idList
            List<cosMetic> cosMeticList=coke2.selectAll();
            List<String> idList=new ArrayList<>();

            for (cosMetic cosMetic : cosMeticList) {
                idList.add(cosMetic.getCosmeticID());
            }
            //如果存在，则进行购买操作
            if(idList.contains(CosmeticID.getText())){
                cosMetic x=coke2.selectBykey(CosmeticID.getText());
                //先根据输入的化妆品ID获得对象,和创建好的客户对象传入方法，进行单个增加Addone();
                Addone(c1,x,connection);
                //是否需要购买多个化妆品
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("该户还有新的化妆品购买吗？");
                // 显示对话框，并等待按钮返回
                Optional<ButtonType> result = alert.showAndWait();

                //设置一个while循环，只有点击取消才不会继续购物
                while (result.get()==ButtonType.OK){
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("输入对话框");
                    dialog.setHeaderText("请输入新的化妆品编号：");
                    // 获取对话框的 "确定" 按钮
                    Optional<String> result2 = dialog.showAndWait();
                    // 判断是否存在输入结果
                    if (result.isPresent()) {
                        String userInput = result2.get();
                        // 判断用户输入是否，再将输入文本框的内容替换掉
                        if (!CosmeticID.getText().equals(userInput)) {
                            CosmeticID.setText(userInput);
                            dialog.close();
                        }
                    }
                    if(idList.contains(x.getCosmeticID())){//再进行重复操作，判断是否存在，然后调用方法
                        Addone(c1,coke2.selectBykey(CosmeticID.getText()),connection);
                        result=alert.showAndWait();
                    }else {
                        Alert alert3 = new Alert(Alert.AlertType.WARNING);
                        // 设置消息框的标题
                        alert3.setTitle("警告");
                        // 设置消息框的头部文本
                        alert3.setHeaderText("化妆品中无该报告编号！请重新输入");
                        // 显示消息框并等待用户点击按钮
                        alert3.showAndWait();
                    }
                }
            }else {
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                // 设置消息框的标题
                alert2.setTitle("警告");
                // 设置消息框的头部文本
                alert2.setHeaderText("化妆品中无该报告编号！请重新输入");
                // 显示消息框并等待用户点击按钮
                alert2.showAndWait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新增单条数据方法
     * @param c1
     * @param x
     * @param connection
     * @throws Exception
     */
    private void Addone(custom c1,cosMetic x, Connection connection) throws Exception {
        //先连接数据库
        Customlmpl customDao = new Customlmpl(connection);
        Cosmeticlmpl cosmeticDao=new Cosmeticlmpl(connection);
        List<cosMetic> List=cosmeticDao.selectAll();
        List<String> idList=new ArrayList<>();
        //进行化妆品ID是否存在
        for (cosMetic cosMetic : List) {
            idList.add(cosMetic.getCosmeticID());
        }
        if(idList.contains(CosmeticID.getText())){
            //选择购买数量
            TextInputDialog dialog = new TextInputDialog();
            String quantity="0";
            dialog.setTitle("输入对话框");
            dialog.setHeaderText("请输入该化妆品客户购买的数量:");
            // 获取对话框的 "确定" 按钮
            Optional<String> result = dialog.showAndWait();
            // 判断是否存在输入结果
            if (result.isPresent()) {
                String userInput = result.get();
                // 将quantity设置为输入的内容
                if (!quantity.equals(userInput)) {
                    quantity = userInput;
                    dialog.close();
                }
            }
            //判断化妆品库存是否充足
            Orderlmpl orderDao=new Orderlmpl(connection);
            System.out.println(x);
            if((x.getShopquantity()-Integer.parseInt(quantity))<0){//如果库存数量-输入数量小于0，则显示库存数量不足
                Alert alert = new Alert(Alert.AlertType.WARNING);
                // 设置消息框的标题
                alert.setTitle("警告");
                // 设置消息框的头部文本
                alert.setHeaderText("该化妆品库存只剩："+x.getShopquantity()+"件");
                // 显示消息框并等待用户点击按钮
                alert.showAndWait();
            }else {
                //然后设置新的库存，newquantiay=原库存-输入的数量
                int newquantiay=x.getShopquantity()-Integer.parseInt(quantity);
                x.setShopquantity(newquantiay);//再将化妆品库存设置为新库存
                System.out.println(x.getShopquantity());//对比库存
                //再进行化妆品表的更新
                cosmeticDao.updateBykey(x,x.getCosmeticID());

                //然后根据输入的内容创建订单对象，并加入到订单表中
                if(Days.getText()!=null&&quantity!=null){
                   //根据createRandomOrder()方法创建对象
                    orderDao.insert(createRandomOrder(c1.getCustomName(),x.getCosmeticName(), c1.getCount(),Integer.parseInt(quantity),x.getPrice(), Integer.parseInt(Days.getText()),x.getCosmeticID()));
                }
                //然后还要进行客户信息是否增加的判断
                List<custom> customList=customDao.selectAll();
                //获取所有客户名的集合customnameList
                java.util.List<String> customnameList=new ArrayList<>();
                for (int i = 0; i < customList.size(); i++) {
                    customnameList.add(customList.get(i).getCustomName());
                }
                //如果customnameList集合中含有该账单的客户名，则不添加，而是进行更新操作
                if(!customnameList.contains(c1.getCustomName())){
                    customDao.insert(c1);
                }else {
                    customDao.updateBykey(c1,c1.getCustomName());
                }
            }

        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            // 设置消息框的标题
            alert.setTitle("警告");
            // 设置消息框的头部文本
            alert.setHeaderText("该化妆品编号本店无法搜索到，请重新输入化妆品编号！");
            // 显示消息框并等待用户点击按钮
            alert.showAndWait();
        }
        refreshTableView();
    }

    /**
     * 创建新界面进行模糊查询
     * @param actionEvent
     */
    @FXML
    private void seach(ActionEvent actionEvent) {
        try {
            // 加载新窗口的FXML文件
            Parent parent = new FXMLLoader(getClass().getResource("Ordersearch.fxml")).load();

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
     * avaFX应用程序加载FXML文件时执行，通常用于初始化UI元素和设置初始状态。
     * @param orders
     */
    //在JavaFX应用程序加载FXML文件时执行，通常用于初始化UI元素和设置初始状态。
    private void updateTableView(List<orDer> orders) {
    // 传入List集合，更新TableView
        orderTableView.getItems().setAll(orders);
    // 为每列设置单元格值工厂，以从对象中获取属性值显示。
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
        buydays.setCellValueFactory(new PropertyValueFactory<>
                ("buydays"));
        // 传入List集合，更新TableView
        /*studentTableView.getColumns().addAll(ID,name,birthday,sex,phone);
        ObservableList<Stu> data = FXCollections.observableList(students);
        studentTableView.setItems(data);*/
    }

    /**
     * 计算总页数
     * @return
     */
    private int calculatePageCount() {
        try (Connection connection = DruidUtil.getConnection()) {
                Orderlmpl orderDao = new Orderlmpl(connection);
    // 计算总页数，并向上取整
            return (int) Math.ceil((double)
                    orderDao.getTotalRecords() / itemsPerPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 加载指定页码的数据到 TableView中
     * @param pageIndex
     */
    //加载指定页码的数据到 TableView 中
    private void loadPageData(int pageIndex) {
        try (Connection connection = DruidUtil.getConnection()) {
            Orderlmpl orderDao = new Orderlmpl(connection);
    // 获取每页数据
            List<orDer> pageData = orderDao.selectByPage(pageIndex + 1, itemsPerPage);
    // 将数据加载到 TableView 中
            updateTableView(pageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * // 根据新的页码加载数据
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
     * 刷新界面
     */
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
        if (orderTableView.getItems().isEmpty() &&
                currentPageIndex > 0) {
            loadPageData(currentPageIndex - 1);
            pageTable.setCurrentPageIndex(currentPageIndex - 1);//设置当前页码
        }
    }

    /**
     * 初始化
     */
    @FXML
    private void initialize() {
        CountSet();
        checkButtonState();
        refreshTableView();
        initPagination();
        loadPageData(0);

    }

    /**
     * 判断按钮是否选择，并根据是否为VIP，自动设置折扣
     */
    public void  CountSet(){
        // 创建 ToggleGroup 并将 RadioButton 添加到其中
        yesButton.setToggleGroup(rVIP);
        NoButton.setToggleGroup(rVIP);
        // 初始化折扣为1.0
        Count.setText("1.0");
        // 添加事件监听器
        rVIP.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == yesButton) {
                // 如果选择了“是”，设置折扣为0.75
                Count.setText("0.75");
            } else if (newValue == NoButton) {
                // 如果选择了“不是”，设置折扣为1.0
                Count.setText("1.0");
            }
        });
    }

    /**
     * 判断所有信息是否输入完整
     * @return
     */
    private boolean isNull() {
        Toggle flag=rsex.getSelectedToggle();
        Toggle flag2=rVIP.getSelectedToggle();
        return (!Customername.getText().isEmpty()) && (!Phone.getText().isEmpty()) && (!Address.getText().isEmpty())
                &&(flag!=null)&& (!CosmeticID.getText().isEmpty())&&(flag2!=null)&&(!Count.getText().isEmpty());
    }

    /**
     * 检查ADD按钮状态
     */
    private void checkButtonState() {
        if(!isNull()){
            Add.setDisable(true);
        }else {
            Add.setDisable(false);
        }
    }

    /**
     * 随机生成订单对象
     * @param customerName
     * @param cosmeticName
     * @param count
     * @param quqatity
     * @param price
     * @param days
     * @param cosmeticID
     * @return
     */
    private static orDer createRandomOrder(String customerName,String cosmeticName,double count,int quqatity,int price,int days,String cosmeticID ) {
        orDer order = new orDer();
        order.setQuantity(quqatity);
        order.setOrderID(getRandomID());//订单ID调用方法随机生成
        order.setCount(count);
        order.setCustomerName(customerName);
        order.setCosmeticID(cosmeticID);
        order.setCosmeticName(cosmeticName);
        order.setTotalprice(price*count*quqatity);
        order.setBuydays(days);
        return order;
    }

    /**
     * 生成1000-9999之间的随机数作为订单ID
     * @return
     */
    private static String getRandomID() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9000) + 1000); // 生成1000-9999之间的随机数
    }

    public void check1(ActionEvent actionEvent) {
        checkButtonState();
    }

    /**
     * 检查电话号码文本框
     * @param actionEvent
     */
    public void check2(ActionEvent actionEvent) {
        checkButtonState();
        try {
            Connection connection = DruidUtil.getConnection();
            Customlmpl customDao = new Customlmpl(connection);
            //根据电话号码获取用户信息
            custom c=customDao.selectByPhone(Phone.getText());
            System.out.println(Phone.getText());
            System.out.println(c);
            //如果获取的用户信息存在，则说明之前来消费过，则将该用户信息自动设置在输入框中
            if(c!=null){
                Address.setText(c.getAddress());
                Customername.setText(c.getCustomName());
                if(c.getIsVIP().equals("是")){
                    // 如果，选中 Yes 按钮
                    rVIP.selectToggle(yesButton);
                }else {
                    // 如果是 "0"，选中 No 按钮
                    rVIP.selectToggle(NoButton);
                }
                if (c.getSex().equals("男")){
                    rsex.selectToggle(ManButton);
                }else {
                    rsex.selectToggle(WomanButton);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void check3(ActionEvent actionEvent) {
        checkButtonState();
    }

    public void check4(ActionEvent actionEvent) {
        checkButtonState();
    }

    public void check5(ActionEvent actionEvent) {
        checkButtonState();
    }

    public void check6(ActionEvent actionEvent) {
        checkButtonState();
    }

    /**
     * 导出订单信息
     * @param actionEvent
     */
    @FXML
    private void OutPut(ActionEvent actionEvent)  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出文件");
        fileChooser.setInitialDirectory(new File("D:\\桌面\\Cosmetic"));
        //选择信息
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("All Files", "*.*")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Data files (*.data)", "*.data")));
        fileChooser.getExtensionFilters().add((new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx")));
    // 显示文件保存对话框
    File selectedFile = fileChooser.showSaveDialog(null);
    Connection connection=DruidUtil.getConnection();
    Orderlmpl orderDao=new Orderlmpl(connection);
    try {
        List<orDer> list=orderDao.selectAll();
        //根据不同类型使用不同的工具类导出
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
}

