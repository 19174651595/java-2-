package lijing.cosmetic.JDBC;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * JDBC操作泛型接口
 * @param <T>
 * @param <ID>
 */
public interface BaseDao<T,ID extends Serializable> {
        /**
         * 插入单条数据
         * @param entity
         * @return
         * @throws Exception
         */
        int insert(T entity)throws Exception;//插入单条数据

        /**
         * 批量数据插入
         * @param entities
         * @return
         * @throws Exception
         */
        int insertBatch(List<T> entities)throws Exception;//批量数据插入

        /**
         * 主键更新
         * @param entity
         * @param id
         * @return
         * @throws Exception
         */
        int updateBykey(T entity,String id) throws Exception;//根据主键更新

        /**
         * 条件更新
         * @param condition
         * @param updateParams
         * @return
         * @throws Exception
         */
        int updateByCondition(String condition, Map<String,Object> updateParams) throws Exception;//根据条件更新

        /**
         * 根据主键删除单条数据
         * @param id
         * @return
         * @throws Exception
         */
        int deleteByPrimaryKey(String id) throws Exception;//根据主键删除单条数据

        /**
         * 根据主键删除多条数据
         * @param idList
         * @return
         * @throws Exception
         */
        int deleteByPrimaryKey(List<String> idList) throws Exception;//根据主键删除多条数据

        /**
         * 根据条件删除数据
         * @param condition
         * @return
         * @throws Exception
         */
        int deleteByCondition(String condition) throws Exception;//根据条件删除数据

        /**
         * 根据主键查询数据
         * @param id
         * @return
         * @throws Exception
         */
        T selectBykey(ID id)throws Exception;//根据主键查询数据

        /**
         * 查询所有数据
         * @return
         * @throws Exception
         */
        List<T> selectAll() throws Exception;//查询所有数据

        /**
         * 条件查询数据
         * @param condition
         * @return
         * @throws Exception
         */
        List<T> selectByCondition(String condition) throws
                Exception;// 根据条件查询数据

        /**
         * 分页查询学生数据
         * @param pageIndex
         * @param pageSize
         * @return
         * @throws Exception
         */
        //分页查询学生数据
        List<T> selectByPage(int pageIndex, int pageSize) throws
                Exception;

        /**
         * 获取总记录数
         * @return
         * @throws Exception
         */
        // 获取总记录数
        int getTotalRecords() throws Exception;
}
