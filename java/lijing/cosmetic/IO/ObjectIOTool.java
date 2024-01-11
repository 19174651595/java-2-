package lijing.cosmetic.IO;

import java.io.*;
import java.util.List;

/**
 * 导入导出data文件工具类
 */
public class ObjectIOTool {
    /**
     * 将任意数据类型的List序列化到指定文件
     * @param dataList
     * @param filePath
     */
    // 方法：将任意数据类型的List序列化到指定文件
    public static void wirteObject(List<?> dataList, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(dataList);
            System.out.println("序列化完成。数据已写入文件：" + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从序列化文件中读取并返回指定类型的List集合
     * @param filePath
     * @param elementType
     * @return
     * @param <T>
     */
    // 泛型方法：从序列化文件中读取并返回指定类型的List集合
    public static <T> List<T> readObject(String filePath, Class<T> elementType) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = ois.readObject();

            if (obj instanceof List<?> dataList) {

                // 检查列表中的每个元素是否为指定类型
                for (Object element : dataList) {
                    if (!elementType.isInstance(element)) {
                        throw new ClassCastException("列表中的元素不是指定的类型");
                    }
                }
                // 如果所有元素都是指定类型，进行类型转换并返回
                @SuppressWarnings("unchecked")
                List<T> typedList = (List<T>) dataList;
                return typedList;

            } else {
                throw new ClassCastException("从文件中读取的对象不是List");
            }

        } catch (ClassNotFoundException | IOException | ClassCastException e) {
            e.printStackTrace();
            // 适当处理异常（记录日志、重新抛出异常等）
            return null;
        }
    }
}
