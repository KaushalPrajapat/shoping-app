package errorcheck.error.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import errorcheck.error.errors.CustomException;
import errorcheck.error.external.ProductProxy;
import errorcheck.error.sabkuch.ProductModel;

@Service
public class ErrorService {
    @Autowired
    private ProductProxy proxy;

    public ProductModel getProductOrError(long prodId){
       
        ResponseEntity<?> productById = null;
       try {
            productById = proxy.getProductById(prodId);
       } catch (CustomException e) {
        System.out.println(e.getMessage() + " back end se mila");
        throw new CustomException(e.getMessage(), e.getHttpStatus());        
       }
            
       ProductModel pm =  (ProductModel) productById.getBody();
       System.out.println(pm);


        return pm;
    }
}
