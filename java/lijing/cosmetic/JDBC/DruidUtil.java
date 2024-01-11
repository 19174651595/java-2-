package lijing.cosmetic.JDBC;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 创建数据库连接池工具类
 */
public class DruidUtil {
    private static DataSource dataSource;//定义数据源变量

    static {
    // 加载配置文件和建立连接池
        try (InputStream resourceAsStream =
                     DruidUtil.class.getClassLoader().getResourceAsStream("Druid.properties")) {
            if (resourceAsStream == null) {
                throw new IOException("Druid.properties not found");
            }
    // 创建 Properties 对象，用于读写属性文件
            Properties pro = new Properties();
    // 从配置文件输入流中加载配置信息
            pro.load(resourceAsStream);
    // 使用 DruidDataSourceFactory 创建数据源
            dataSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
    // 处理配置文件读取错误
            e.printStackTrace();
        } catch (Exception e) {
    // 处理其他异常，如DruidDataSourceFactory.createDataSource 的异常
            e.printStackTrace();
        }
    }

    /**
     * 获取数据源
     * @return
     */
    //获取数据源
    public static DataSource getDataSource() {
        return dataSource;
    }

    //获取连接池中的一个连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }
}

