package lijing.cosmetic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 设置舞台
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 使用FXMLLoader加载FXML界面文件
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("Main.fxml"));
        // 根据加载的FXML文件创建一个场景（Scene），并设置其尺寸
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1300, 676);
            stage.setTitle("化妆品店管理系统----李靖开发");// 设置窗口的标题
            stage.setResizable(false);//界面不可以放大
            stage.setScene(scene);// 将前面创建的场景设置到窗口上
            stage.show();// 显示窗口
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动入口
     * @param args
     */
    public static void main(String[] args) {
        launch();// 启动JavaFX应用
    }
}

