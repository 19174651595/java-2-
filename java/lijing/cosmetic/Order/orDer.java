package lijing.cosmetic.Order;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 购买账单实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class orDer {
    @Serial
    private static final long serialVersionUID= -1885731095575134138L;
    @ExcelProperty("订单编号")
    private String orderID;
    @ExcelProperty("客户名称")
    private String customerName;
    @ExcelProperty("购买的化妆品编号")
    private String cosmeticID;
    @ExcelProperty("化妆品名称")
    private String cosmeticName;
    @ExcelProperty("数量")
    private int Quantity;
    @ExcelProperty("该客户所享有折扣")
    private double count;
    @ExcelProperty("总价")
    private double Totalprice;
    @ExcelProperty("已购买时间")
    private int buydays;
}
