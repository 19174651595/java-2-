package lijing.cosmetic.Custom;


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
 * 客户实体类的JDBC的方法
 */
public class Customlmpl implements BaseDao<custom,String> {
    private Connection connection;
    public Customlmpl(Connection connection){
        this.connection=connection;
    }

    /**
     * 插入
     * @param customs
     * @return
     * @throws Exception
     */
    @Override
    public int insert(custom customs) throws Exception {
        String sql = "INSERT ignore INTO custom (customName,sex,phone,address,isVIP,count) VALUES (?, ?, ?, ?, ?,?)";
        // 使用 PreparedStatement 预编译 SQL 语句，防止 SQL 注入
        PreparedStatement pstmt = connection.prepareStatement(sql);
        // 设置参数值
        pstmt.setString(1,customs.getCustomName());
        pstmt.setString(2,customs.getSex());
        pstmt.setString(3, customs.getPhone());
        pstmt.setString(4, customs.getAddress());
        pstmt.setString(5, customs.getIsVIP());
        pstmt.setDouble(6, customs.getCount());

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
    public int insertBatch(List<custom> entities) throws Exception {
        // SQL 插入语句
        String sql = "INSERT ignore INTO custom (customName,sex,phone,address,isVIP,count) VALUES (?, ?, ?, ?, ?,?)";
        // 使用 PreparedStatement 预编译 SQL 语句，防止 SQL 注入
       PreparedStatement pstmt = connection.prepareStatement(sql);
        // 设置批处理模式
        connection.setAutoCommit(false);
        // 遍历列表，设置参数并添加到批处理中
        for (custom customs : entities) {
            pstmt.setString(1,customs.getCustomName());
            pstmt.setString(2,customs.getSex());
            pstmt.setString(3, customs.getPhone());
            pstmt.setString(4, customs.getAddress());
            pstmt.setString(5, customs.getIsVIP());
            pstmt.setDouble(6, customs.getCount());
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
    public int updateBykey(custom entity,String id) throws Exception {
        String sql = "UPDATE custom SET phone=?,sex=?,address=?,isVIP=?,count=? where customName=?";

        int index=1;
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(index++,entity.getPhone());
        pstmt.setString(index++,entity.getSex());
        pstmt.setString(index++,entity.getAddress());
        pstmt.setString(index++,entity.getIsVIP());
        pstmt.setDouble(index++,entity.getCount());
        pstmt.setString(index++,entity.getCustomName());
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
        StringBuilder sql = new StringBuilder("UPDATE custom SET ");
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
      // 返回更新记录数
        return updateCount;
    }

    /**
     * 根据客户名删除
     * @param name
     * @return
     * @throws Exception
     */
    public int deleteBySomething(String name) throws Exception {
        String sql = "DELETE FROM custom WHERE customName = ?";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1, name);
        return pstmt.executeUpdate();
    }

    /**
     * 根据客户主键删除（电话号码）
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public int deleteByPrimaryKey(String id) throws Exception {
        String sql = "DELETE FROM custom WHERE phone = ?";
        PreparedStatement pstmt=connection.prepareStatement(sql);
        pstmt.setString(1, id);
        return pstmt.executeUpdate();
    }

    /**
     * 根据主键集合批删除
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
        String sql = "DELETE FROM custom WHERE custom.phone IN (" +
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
     * 根据客户名查询
     * @param name
     * @return
     * @throws Exception
     */
    public custom selectSometThing(String name) throws Exception {
        String sql = "SELECT * FROM custom WHERE customName = ? ";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1,name); // 设置参数
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        if (rs.next()) {
         // 解析结果集
            return new custom(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5),rs.getDouble(6));
        }
        return null; // 没有找到对应
    }

    /**
     * 根据电话号码查询
     * @param phone
     * @return
     * @throws Exception
     */
    public custom selectByPhone(String phone) throws Exception {
        String sql = "SELECT * FROM custom WHERE phone = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1,phone); // 设置参数
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        if (rs.next()) {
       // 解析结果集
            return new custom(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5),rs.getDouble(6));
        }
        return null; // 没有找到对应
    }

    /**
     * 根据主键查询
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public custom selectBykey(String name) throws Exception {
        String sql = "SELECT * FROM custom WHERE customName = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1,name); // 设置参数，即姓名
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        if (rs.next()) {
            // 解析结果集
            return new custom(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5),rs.getDouble(6));
        }
        return null; // 没有找到对应
    }

    /**
     * 获得全部数据
     * @return
     * @throws Exception
     */
    @Override
    public List<custom> selectAll() throws Exception {
        String sql = "SELECT * FROM custom";
        List<custom> customList = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery(); // 执行查询，返回结果集
        while (rs.next()) {
        // 解析结果集，构建实体对象并添加到列表中
            customList.add(new custom(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5),rs.getDouble(6)));
        }
        return customList; // 返回列表
    }

    /**
     *
     * @param condition
     * @return
     * @throws Exception
     */
    @Override
    public List<custom> selectByCondition(String condition) throws Exception {
        List<custom> List = new ArrayList<>();
        String sql = "SELECT * FROM cosmetic WHERE " +
                condition;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            List.add(new custom(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5),rs.getDouble(6)));
        }
        return List;

    }

    /**
     * 实现分页功能
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public List<custom> selectByPage(int pageIndex, int pageSize) throws Exception {
        // 计算偏移量
        int offset = (pageIndex - 1) * pageSize;
      // SQL 查询语句，使用 LIMIT 和 OFFSET 进行分页
        String sql = "SELECT * FROM custom LIMIT ? OFFSET ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
       // 设置参数
        pstmt.setInt(1, pageSize);
        pstmt.setInt(2, offset);
        // 执行查询，返回结果集
        ResultSet rs = pstmt.executeQuery();
       // 解析结果集，构建对象列表
        List<custom> List = new ArrayList<>();
        while (rs.next()) {
            List.add(new custom(
                    rs.getString("customName"),
                    rs.getString("sex"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("isVIP"),
                    rs.getDouble("count")
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
        String sql = "SELECT COUNT(*) FROM custom";
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
     * @param name
     * @param Phone
     * @param VIP
     * @return
     * @throws SQLException
     */
    //模糊查询
    public static List<custom> getFilteredData(String name, String Phone, String VIP) throws SQLException {
        List<custom> filteredData = new ArrayList<>();

        // 构建SQL查询语句
        String sql = "SELECT * FROM custom WHERE custom.customName LIKE ? AND phone LIKE ? AND isVIP =?";

        try (Connection connection = DruidUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // 设置查询参数
            statement.setString(1, "%" + name + "%");
            statement.setString(2, Phone+ "%");
            statement.setString(3,  VIP );

            // 执行查询
            try (ResultSet resultSet = statement.executeQuery()) {
                // 处理查询结果
                while (resultSet.next()) {
                    String customName = resultSet.getString("customName");
                    String sex = resultSet.getString("sex");
                    String phone = resultSet.getString("phone");
                    String address= resultSet.getString("address");
                    String isVIP = resultSet.getString("isVIP");
                    double count = resultSet.getDouble("count");

                    // 创建对象并添加到列表
                    custom customs = new custom(customName,sex,phone,address,isVIP,count);
                    filteredData.add(customs);
                }
            }
        }

        return filteredData;
    }

}