package lijing.cosmetic;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


import java.io.IOException;

/**
 * 主界面控制器，左边点击不同按钮，右边显示不同界面
 */
public class Main {
    @FXML
    private Text text;
    @FXML
    private StackPane pageContainer;
    @FXML
    private void handlePage1() {
        text.setVisible(true);
        loadFXML("CosMe.fxml");

    }
    @FXML
    private void handlePage2() {
        text.setVisible(true);
        loadFXML("custom1.fxml");
    }
    @FXML
    private void handlePage3() {
        text.setVisible(true);
        loadFXML("order2.fxml");
    }

    /**
     * 加载不同界面
     * @param fxmlFileName
     */
    private void loadFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node content = loader.load();
            pageContainer.getChildren().clear();
            pageContainer.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
