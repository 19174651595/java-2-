package lijing.cosmetic.Custom;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 客户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class custom implements Serializable {
    @Serial
    private static final long serialVersionUID= 7404977155905111928L;
    @ExcelProperty("客户姓名")
    private String CustomName;
    @ExcelProperty("性别")
    private String sex;
    @ExcelProperty("电话号码")
    private String phone;
    @ExcelProperty("地址")
    private String address;
    @ExcelProperty("是否为VIP")
    private String isVIP;
    @ExcelProperty("享有折扣")
    private double count;


}
