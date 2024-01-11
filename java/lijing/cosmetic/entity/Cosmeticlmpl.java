package lijing.cosmetic.entity;

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
 * 化妆品的JDBC操作
 */
public class Cosmeticlmpl implements BaseDao<cosMetic,String> {
    private Connection connection;
    public Cosmeticlmpl(Connection connection){
        this.connection=connection;
    }

    /**
     * 单条插入
     * @param Cosmetic
     * @return
     * @throws Exception
     */
    @Override
    public int insert(cosMetic Cosmetic) throws Exception {
        String sql = "INSERT ignore INTO cosmetic (CosmeticName,CosmeticID,price,productDate,ShelfTime,Harmfulness,suitablePeople,BeautyDegree,Shopquantity,type) VALUES (?, ?, ?, ?, ?,?,?,?,?,?)";
        //使用 PreparedStatement 预编译 SQL 语句，防止 SQL 注入
        PreparedStatement pstmt = connection.prepareStatement(sql);
        //设置参数值
        pstmt.setString(1, Cosmetic.getCosmeticName());
        pstmt.setString(2, Cosmetic.getCosmeticID());
        pstmt.setInt(3, Cosmetic.getPrice());
        pstmt.setString(4, Cosmetic.getProductDate());
        pstmt.setString(5, Cosmetic.getShelfTime());
        pstmt.setString(6, Cosmetic.getHarmfulness());
        pstmt.setString(7, Cosmetic.getSuitablePeople());
        pstmt.setString(8, Cosmetic.getBeautyDegree());
        pstmt.setInt(9, Cosmetic.getShopquantity());
        pstmt.setString(10, Cosmetic.getType());
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
    public int insertBatch(List<cosMetic> entities) throws Exception {
        // SQL 插入语句
        String sql = "INSERT ignore INTO cosmetic (CosmeticName,CosmeticID,price,productDate,ShelfTime,Harmfulness,suitablePeople,BeautyDegree,Shopquantity,type) VALUES (?, ?, ?, ?, ?,?,?,?,?,?)";
        // 使用 PreparedStatement 预编译 SQL 语句，防止 SQL 注入
       PreparedStatement pstmt = connection.prepareStatement(sql);
        // 设置批处理模式
        connection.setAutoCommit(false);
        // 遍历列表，设置参数并添加到批处理中
        for (cosMetic Cosmetic : entities) {
            pstmt.setString(1, Cosmetic.getCosmeticName());
            pstmt.setString(2, Cosmetic.getCosmeticID());
            pstmt.setInt(3, Cosmetic.getPrice());
            pstmt.setString(4, Cosmetic.getProductDate());
            pstmt.setString(5, Cosmetic.getShelfTime());
            pstmt.setString(6, Cosmetic.getHarmfulness());
            pstmt.setString(7, Cosmetic.getSuitablePeople());
            pstmt.setString(8, Cosmetic.getBeautyDegree());
            pstmt.setInt(9, Cosmetic.getShopquantity());
            pstmt.setString(10, Cosmetic.getType());
                // 将当前语句添加到批处理中
            pstmt.addBatch();
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
    public int updateBykey(cosMetic entity,String id) throws Exception {
        String sql = "UPDATE cosmetic SET CosmeticName=?,price=?,productDate=?,ShelfTime=?," +
                "Harmfulness=?,suitablePeople=?,BeautyDegree=?,Shopquantity=? ,type=? WHERE CosmeticID=?";
        int index=1;
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(index++,entity.getCosmeticName());
        pstmt.setInt(index++,entity.getPrice());
        pstmt.setString(index++,entity.getProductDate());
        pstmt.setString(index++,entity.getShelfTime());
        pstmt.setString(index++,entity.getHarmfulness());
        pstmt.setString(index++,entity.getSuitablePeople());
        pstmt.setString(index++,entity.getBeautyDegree());
        pstmt.setInt(index++,entity.getShopquantity());
        pstmt.setString(index++,entity.getType());

        pstmt.setString(index++,id);
        // 执行更新操作
        int updateCount = pstmt.executeUpdate();
        return updateCount;
    }

    /**
     * 根据条件更新
     * @param condition
     * @param updateParams
     * @return
     * @throws Exception
     */
    @Override
    public int updateByCondition(String condition, Map<String, Object> updateParams) throws Exception {
            // 创建一个字符串构建器，用于动态构建SQL语句
        StringBuilder sql = new StringBuilder("UPDATE cosmetic SET ");
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
        PreparedStatement pstmt=connection.prepareStatement(sql.toString());
    // 遍历更新参数映射，为PreparedStatement设置参数
        for (Map.Entry<String, Object> entry :
                updateParams.entrySet()) {
            pstmt.setObject(paramIndex++, entry.getValue());
        }
    // 执行更新操作，并获取受影响的记录数
        updateCount = pstmt.executeUpdate();
    //// 返回更新记录数
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
        String sql = "DELETE FROM cosmetic WHERE CosmeticID = ?";
        PreparedStatement pstmt=connection.prepareStatement(sql);
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
        String sql = "DELETE FROM cosmetic WHERE cosmetic.CosmeticID IN (" +
                String.join(",",
                        Collections.nCopies(idList.size(), "?")) +")";
        PreparedStatement pstmt=connection.prepareStatement(sql);
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
        String sql = "DELETE FROM cosmetic WHERE " + condition;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        return pstmt.executeUpdate();
    }

    /**
     * 根据主键查询
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public cosMetic selectBykey(String id) throws Exception {
        String sql = "SELECT * FROM cosmetic WHERE CosmeticID = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1,id); // 设置参数，即学号
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        if (rs.next()) {
        // 解析结果集
            return new cosMetic(rs.getString(1),
                    rs.getString(2), rs.getInt(3), rs.getString(4),
                    rs.getString(5),rs.getString(6),rs.getString(7),
                    rs.getString(8),rs.getInt(9),rs.getString(10));
        }
        return null; // 没有找到对应
    }

    /**
     * 获得全部数据
     * @return
     * @throws Exception
     */
    @Override
    public List<cosMetic> selectAll() throws Exception {
        String sql = "SELECT * FROM cosmetic";
        List<cosMetic> cosMeticList = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        while (rs.next()) {
// 解析结果集，构建实体对象并添加到列表中
            cosMeticList.add(new cosMetic(rs.getString(1),
                    rs.getString(2), rs.getInt(3), rs.getString(4),
                    rs.getString(5),rs.getString(6),rs.getString(7),
                    rs.getString(8),rs.getInt(9),rs.getString(10)));
        }
        return cosMeticList; // 返回列表
    }

    /**
     * 根据条件查询
     * @param condition
     * @return
     * @throws Exception
     */
    @Override
    public List<cosMetic> selectByCondition(String condition) throws Exception {
        List<cosMetic> List = new ArrayList<>();
        String sql = "SELECT * FROM cosmetic WHERE " +
                condition;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            List.add(new cosMetic(rs.getString(1),
                    rs.getString(2), rs.getInt(3), rs.getString(4),
                    rs.getString(5),rs.getString(6),rs.getString(7),
                    rs.getString(8),rs.getInt(9),rs.getString(10)));
        }
        return List;

    }

    /**
     * 进行分页
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public List<cosMetic> selectByPage(int pageIndex, int pageSize) throws Exception {
        // 计算偏移量
        int offset = (pageIndex - 1) * pageSize;
    // SQL 查询语句，使用 LIMIT 和 OFFSET 进行分页
        String sql = "SELECT * FROM cosmetic LIMIT ? OFFSET ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
    // 设置参数
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, offset);
        // 执行查询，返回结果集
        ResultSet rs = pstmt.executeQuery();
    // 解析结果集，构建学生对象列表
        List<cosMetic> List = new ArrayList<>();
        while (rs.next()) {
            List.add(new cosMetic(
                    rs.getString("CosmeticName"),
                    rs.getString("CosmeticID"),
                    rs.getInt("price"),
                    rs.getString("productDate"),
                    rs.getString("ShelfTime"),
                    rs.getString("Harmfulness"),
                    rs.getString("suitablePeople"),
                    rs.getString("BeautyDegree"),
                    rs.getInt("Shopquantity"),
                    rs.getString("type")
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
        // SQL 查询语句，统计表中的总记录数
        String sql = "SELECT COUNT(*) FROM cosmetic";
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
     * 实现模糊查询
     * @param ID
     * @param beauty
     * @param name
     * @return
     */
    public List<cosMetic> getFilteredData( String ID, String beauty,String name) {
        List<cosMetic> filteredData = new ArrayList<>();

        // 构建SQL查询语句
        String sql = "SELECT * FROM cosmetic WHERE CosmeticID  LIKE ? AND BeautyDegree LIKE ? AND  CosmeticName LIKE ?";

        try (Connection connection = DruidUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 设置查询参数
            statement.setString(1, ID + "%");
            statement.setString(2, "%" + beauty+ "%");
            statement.setString(3, "%" +name + "%");

            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                // 处理查询结果
                while (resultSet.next()) {
                    String CosmeticName = resultSet.getString("CosmeticName");
                    String CosmeticID = resultSet.getString("CosmeticID");
                    int price = resultSet.getInt("price");
                    String productDate= resultSet.getString("productDate");
                    String ShelfTime = resultSet.getString("ShelfTime");
                    String Harmfulness = resultSet.getString("Harmfulness");
                    String suitablePeople = resultSet.getString("suitablePeople");
                    String BeautyDegree = resultSet.getString("BeautyDegree");
                    int Shopquantity = resultSet.getInt("Shopquantity");
                    String type=resultSet.getString("type");

                    // 创建对象并添加到列表
                    cosMetic cosMetics = new cosMetic(CosmeticName,CosmeticID,price,productDate,ShelfTime,Harmfulness,suitablePeople,BeautyDegree,Shopquantity,type);
                    filteredData.add(cosMetics);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return filteredData;
    }

}
