package errorcheck.error.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import errorcheck.error.sabkuch.ProductModel;

@FeignClient(url = "http://localhost:8100/product" , name = "PRODUCT-SERVICE")
public interface ProductProxy {
    @GetMapping("/{prodId}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable(name = "prodId") long prodId);

}