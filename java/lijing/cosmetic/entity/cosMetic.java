package lijing.cosmetic.entity;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 化妆品实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class cosMetic implements Serializable {
    @Serial
    private static final long serialVersionUID= 6532322376665378431L;
    @ExcelProperty("名称")
    private String CosmeticName;
@ExcelProperty("报告编号")
    private String CosmeticID;
@ExcelProperty("价格")
    private int price;
@ExcelProperty("生产日期")
    private String productDate;
@ExcelProperty("保质期")
    private String ShelfTime;
@ExcelProperty("对皮肤危害性")
    private String Harmfulness;
@ExcelProperty("适用人群")
    private String suitablePeople;
@ExcelProperty("化妆美白程度")
    private String BeautyDegree;
@ExcelProperty("本店库存")
    private int Shopquantity;
@ExcelProperty
    private String type;
}
