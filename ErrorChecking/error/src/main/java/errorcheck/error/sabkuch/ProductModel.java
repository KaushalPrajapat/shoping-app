package errorcheck.error.sabkuch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductModel {
    private long prodId;
    private String prodName;
    private String prodDesc;
    private long prodQuantity;
    private double prodPrice;
}
