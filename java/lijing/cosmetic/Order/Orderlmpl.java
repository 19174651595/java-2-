package lijing.cosmetic.Order;


import lijing.cosmetic.entity.Cosmeticlmpl;
import lijing.cosmetic.entity.cosMetic;
import lijing.cosmetic.JDBC.BaseDao;
import lijing.cosmetic.JDBC.DruidUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 账单的JDBC操作
 */
public class Orderlmpl implements BaseDao<orDer,String> {
    private Connection connection;

    public Orderlmpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * 单条插入
     * @param orders
     * @return
     * @throws Exception
     */
    @Override
    public int insert(orDer orders) throws Exception {
        String sql = "INSERT ignore INTO `order` (orderID,customerName,cosmeticID,cosmeticName,Quantity,count,Totalprice,buydays) VALUES (?, ?, ?, ?, ?,?,?,?)";
    // 使用 PreparedStatement 预编译 SQL 语句，防止 SQL 注入
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, orders.getOrderID());
        pstmt.setString(2, orders.getCustomerName());
        pstmt.setString(3, orders.getCosmeticID());
        pstmt.setString(4, orders.getCosmeticName());
        pstmt.setInt(5, orders.getQuantity());
        pstmt.setDouble(6, orders.getCount());
        pstmt.setDouble(7, orders.getTotalprice());
        pstmt.setInt(8, orders.getBuydays());
    // 设置参数值
    // 执行 SQL 语句
        return pstmt.executeUpdate();

    }

    /**
     * 批插入
     * @param entities
     * @return
     * @throws Exception
     */
    @Override
    public int insertBatch(List<orDer> entities) throws Exception {
        // SQL 插入语句
        String sql = "INSERT ignore INTO order (orderID,customerName,cosmeticID,cosmeticName,Quantity,count,Totalprice,buydays) VALUES (?, ?, ?, ?, ?,?,?,?)";
    // 使用 PreparedStatement 预编译 SQL 语句，防止 SQL 注入
        PreparedStatement pstmt = connection.prepareStatement(sql);
    // 设置批处理模式
        connection.setAutoCommit(false);
        try {
            for (orDer orders : entities) {
                Connection conn = DruidUtil.getConnection();
                Cosmeticlmpl coke = new Cosmeticlmpl(conn);
                cosMetic x = coke.selectBykey(orders.getCosmeticID());
                int price = x.getPrice();
                pstmt.setString(1, orders.getOrderID());
                pstmt.setString(2, orders.getCustomerName());
                pstmt.setString(3, orders.getCosmeticID());
                pstmt.setString(4, orders.getCosmeticName());
                pstmt.setInt(5, orders.getQuantity());
                pstmt.setDouble(6, orders.getCount());
                pstmt.setDouble(7, orders.getCount() * price * orders.getQuantity());
                pstmt.setInt(7, orders.getBuydays());
                pstmt.addBatch();
            }
        } catch (Exception e) {
            System.out.println("发生异常");
        }
        // 执行批处理，如果某个插入语句执行成功并插入一行数据，则对应的数组元素为 1，否则为0
        int[] rowsAffectedArray = pstmt.executeBatch();
        // 提交事务
        connection.commit();
        connection.setAutoCommit(true);
        // 统计总影响行数
        int totalRowsAffected = 0;
        for (int rowsAffected : rowsAffectedArray) {
            totalRowsAffected += rowsAffected;
        }
        return totalRowsAffected;

    }

    /**
     * 根据主键更新
     * @param entity
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public int updateBykey(orDer entity, String id) throws Exception {
        String sql = "UPDATE `order` SET customerName=?,cosmeticID=?,customerName=?," +
                "cosmeticID=?,Quantity=?,count=?,Totalprice=? ,buydays=? where orderID =?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        try {
            int index = 1;
            Connection conn = DruidUtil.getConnection();
            Cosmeticlmpl coke = new Cosmeticlmpl(conn);
            cosMetic x = coke.selectBykey(id);
            int price = x.getPrice();
            pstmt.setString(index++, entity.getCustomerName());
            pstmt.setString(index++, entity.getCosmeticID());
            pstmt.setString(index++, entity.getCosmeticName());
            pstmt.setInt(index++, entity.getQuantity());
            pstmt.setDouble(index++, entity.getCount());
            pstmt.setDouble(index++, entity.getCount() * price * entity.getQuantity());
            pstmt.setInt(index++, entity.getBuydays());
            pstmt.setString(index++, id);
            // 执行更新操作
        } catch (Exception e) {
            System.out.println("发生异常");
        }
        int updateCount = pstmt.executeUpdate();
        return updateCount;
    }

    /**、
     * 根据条件更新
     * @param condition
     * @param updateParams
     * @return
     * @throws Exception
     */
    @Override
    public int updateByCondition(String condition, Map<String, Object> updateParams) throws Exception {
            // 创建一个字符串构建器，用于动态构建SQL语句
            StringBuilder sql = new StringBuilder("UPDATE order SET ");
    // 参数索引，用于设置PreparedStatement的参数
            int paramIndex = 1;
    // 遍历更新参数映射，构建UPDATE语句的SET部分
            for (Map.Entry<String, Object> entry :
                    updateParams.entrySet()) {
                sql.append(entry.getKey()).append("=?, ");
            }
    // 去掉SET部分最后多余的逗号
            sql.delete(sql.length() - 2, sql.length());
    // 在SQL语句末尾添加WHERE条件
            sql.append(" WHERE ").append(condition);
    // 初始化更新记录数
            int updateCount;
            PreparedStatement pstmt = connection.prepareStatement(sql.toString());
    // 遍历更新参数映射，为PreparedStatement设置参数
            for (Map.Entry<String, Object> entry :
                    updateParams.entrySet()) {
                pstmt.setObject(paramIndex++, entry.getValue());
            }
    // 执行更新操作，并获取受影响的记录数
            updateCount = pstmt.executeUpdate();
    // 返回更新记录数
            return updateCount;
    }

    /**
     * 根据主键删除
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public int deleteByPrimaryKey(String id) throws Exception {
        String sql = "DELETE FROM `order` WHERE orderID = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, id);
        return pstmt.executeUpdate();

    }

    /**
     * 根据主键批删除
     * @param idList
     * @return
     * @throws Exception
     */
    @Override
    public int deleteByPrimaryKey(List<String> idList) throws Exception {
        if (idList.isEmpty()) {
            return 0; // 没有主键，不执行删除操作
        }
    // 构造 SQL 语句，根据 idList 的大小生成对应数量的占位符
        String sql = "DELETE FROM `order` WHERE `order`.orderID IN (" +
                String.join(",",
                        Collections.nCopies(idList.size(), "?")) + ")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        // 设置参数值
        for (int i = 0; i < idList.size(); i++) {
            pstmt.setString(i + 1, idList.get(i));
        }
        return pstmt.executeUpdate();

    }

    /**
     * 根据条件删除
     * @param condition
     * @return
     * @throws Exception
     */
    @Override
    public int deleteByCondition(String condition) throws Exception {
        String sql = "DELETE FROM `order` WHERE " + condition;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        return pstmt.executeUpdate();
    }

    /**
     * 根据主键查询
     * @param s
     * @return
     * @throws Exception
     */
    @Override
    public orDer selectBykey(String s) throws Exception {
        return null;
    }

    /**
     * 根据用户名与化妆品ID查询
     * @param name
     * @param cosmeticID
     * @return
     * @throws Exception
     */
    public orDer selectBySomThing(String name,String cosmeticID) throws Exception {
        String sql = "SELECT * FROM `order` WHERE customerName = ? AND cosmeticID=?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, name); // 设置参数，客户名称
        pstmt.setString(2, cosmeticID); // 设置参数，客户名称
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        if (rs.next()) {
    // 解析结果集
            return new orDer(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getInt(5), rs.getDouble(6), rs.getDouble(7),rs.getInt(8));
        }
        return null; // 没有找到对应
    }

    /**
     * 获得全部数据
     * @return
     * @throws Exception
     */
    @Override
    public List<orDer> selectAll() throws Exception {
        String sql = "SELECT * FROM `order`";
        List<orDer> orderList = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        while (rs.next()) {
    // 解析结果集，构建实体对象并添加到列表中
            orderList.add(new orDer(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getInt(5), rs.getDouble(6), rs.getDouble(7),rs.getInt(8)));
        }
        return orderList; // 返回列表
    }

    /**
     * 根据条件查询
     * @param condition
     * @return
     * @throws Exception
     */
    @Override
    public List<orDer> selectByCondition(String condition) throws Exception {
        List<orDer> List = new ArrayList<>();
        String sql = "SELECT * FROM `order` WHERE " +
                condition;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            List.add(new orDer(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getInt(5), rs.getDouble(6), rs.getDouble(7),rs.getInt(8)));
        }
        return List;

    }

    /**
     * 分页功能
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public List<orDer> selectByPage(int pageIndex, int pageSize) throws Exception {
        // 计算偏移量
        int offset = (pageIndex - 1) * pageSize;
    // SQL 查询语句，使用 LIMIT 和 OFFSET 进行分页
        String sql = "SELECT * FROM `order` LIMIT ? OFFSET ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
    // 设置参数
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, offset);
        // 执行查询，返回结果集
        ResultSet rs = pstmt.executeQuery();
    // 解析结果集，构建列表
        List<orDer> List = new ArrayList<>();
        while (rs.next()) {
            List.add(new orDer(
                    rs.getString("orderID"),
                    rs.getString("customerName"),
                    rs.getString("cosmeticID"),
                    rs.getString("cosmeticName"),
                    rs.getInt("Quantity"),
                    rs.getDouble("count"),
                    rs.getDouble("Totalprice"),
                    rs.getInt("buydays")
            ));
        }
        return List;
    }

    /**
     * 获得总记录数
     * @return
     * @throws Exception
     */
    @Override
    public int getTotalRecords() throws Exception {
        // SQL 查询语句，统计学生表中的总记录数
        String sql = "SELECT COUNT(*) FROM `order`";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    // 如果查询结果包含数据，返回第一列的值（总记录数）
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
    // 如果没有数据或发生异常，返回0
        return 0;
    }

    /**
     * 模糊查询
     * @param id
     * @param name
     * @param cosMeticID
     * @return
     * @throws SQLException
     */
    //模糊查询
    public static List<orDer> getFilteredData(String id, String name, String cosMeticID) throws SQLException {
        List<orDer> filteredData = new ArrayList<>();

        // 构建SQL查询语句
        String sql = "SELECT * FROM `order` WHERE orderID LIKE ? AND `order`.customerName LIKE ? AND `order`.cosmeticID LIKE ?";

        try (Connection connection = DruidUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 设置查询参数
            statement.setString(1, "%" + id + "%");
            statement.setString(2, "%" + name + "%");
            statement.setString(3, "%" + cosMeticID + "%");

            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                // 处理查询结果
                while (resultSet.next()) {
                    String orderID = resultSet.getString("orderID");
                    String customerName = resultSet.getString("customerName");
                    String cosmeticID = resultSet.getString("cosmeticID");
                    String cosmeticName = resultSet.getString("cosmeticName");
                    int Quantity = resultSet.getInt("Quantity");
                    double count = resultSet.getDouble("count");
                    double Totalprice = resultSet.getDouble("Totalprice");
                    int buydays = resultSet.getInt("buydays");

                    // 创建对象并添加到列表
                    orDer orDers = new orDer(orderID, customerName, cosmeticID, cosmeticName, Quantity, count, Totalprice,buydays);
                    filteredData.add(orDers);
                }
            }
        }

        return filteredData;
    }

}
