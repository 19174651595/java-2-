package lijing.cosmetic.IO;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入导出excel文件工具类
 */
public class easyExcelTool {
    /**
     *导出excel文件
     * @param data
     * @param filePath
     * @param <T>
     */
    public static <T> void writeExcel(List<T> data, String filePath) {

        EasyExcel.write(filePath, data.get(0).getClass())
                .sheet("列表")
                .doWrite(data);

    }

    /**
     * 导入excel文件
     * @param fileName
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> List<T> readExcel(String fileName, Class<T> clazz) {
        // 创建一个空的对象列表，用于存储从Excel读取的数据
            List<T> lists = new ArrayList<>();
                File file = new File(fileName); // 根据文件名创建一个File对象
        // 检查文件是否存在
            if (file.exists()) {
        // 使用EasyExcel读取Excel文件，并指定clazz为读取的数据类型
                EasyExcel.read(file, clazz, new
                                AnalysisEventListener<T>() {
                                    // 当读取到一个对象时，该方法会被调用
                                    @Override
                                    public void invoke(T object, AnalysisContext a) {
                                    // 将读取到的对象添加到lists列表中
                                        lists.add(object);
                                    }
                                    // 当所有数据分析完成后，该方法会被调用
                                    @Override
                                    public void doAfterAllAnalysed(AnalysisContext a)
                                    {
                                    // 打印提示信息，表示数据已经读取完毕
                                        System.out.println("数据读取完毕！");
                                    }
                                })
                            //开始读取数据
                        .sheet()
                        .doRead();
            }
    // 返回存储了读取到的对象的列表
            return lists;
    }

}
