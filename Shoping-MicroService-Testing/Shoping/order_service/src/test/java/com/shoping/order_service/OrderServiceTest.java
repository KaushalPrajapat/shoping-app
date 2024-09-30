package com.shoping.order_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.shoping.order_service.entities.Order;
import com.shoping.order_service.entities.ProductProxy;
import com.shoping.order_service.entities.Users;
import com.shoping.order_service.errors.CustomException;
import com.shoping.order_service.external.proxies.PaymentServiceProxyIF;
import com.shoping.order_service.external.proxies.ProductServiceProxyIF;
import com.shoping.order_service.external.proxies.UserServiceProxyIF;
import com.shoping.order_service.models.OrderResponse;
import com.shoping.order_service.models.PaymentMode;
import com.shoping.order_service.repositories.OrderRepo;
import com.shoping.order_service.services.OrderService;
import com.shoping.order_service.services.Impl.OrderServiceImpl;

@SpringBootTest
public class OrderServiceTest {
    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductServiceProxyIF productProxy;

    @Mock
    private PaymentServiceProxyIF paymentProxy;

    @Mock
    private UserServiceProxyIF userProxy;
    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    // Get order Deatils
    @DisplayName("Success Case for get Order sceanerio")
    @Test
    void testWhenOrderIsSuccess() {
        // Mocking
        Order order = getOptionalOrder();
        Mockito.when(orderRepo.findByOrderId(anyLong())).thenReturn(Optional.of(order));
        Users user = getOptionalUsers();
        Mockito.when(userProxy.getUsersByUserId(anyLong())).thenReturn(ResponseEntity.ok(user));
        ProductProxy product = getOptionalProduct();
        Mockito.when(productProxy.getProductById(anyLong())).thenReturn(ResponseEntity.ok(product));
        
        // Actual
        OrderResponse orderResponse = orderService.getOrderById(1);
        // Validation
        Mockito.verify(orderRepo,times(1)).findByOrderId(anyLong());
        Mockito.verify(userProxy,times(1)).getUsersByUserId(anyLong());
        Mockito.verify(productProxy,times(1)).getProductById(anyLong());
        // Asserts

        Assertions.assertNotNull(orderResponse);
        Assertions.assertEquals(order.getOrderId(), orderResponse.getOrderId());
    }

@DisplayName("get order by order -id failed sceanerio")
    @Test
    void testWhenOrderIsNotFound(){
        Mockito.when(orderRepo.findByOrderId(anyLong())).thenReturn(null);
        // OrderResponse orderResponse = orderService.getOrderById(1);
        
        CustomException exp = assertThrows(CustomException.class, () -> orderService.getOrderById(1));
        assertEquals("DATABASE_ISSUE", exp.getHttpStatus());
        Mockito.verify(orderRepo,times(1)).findByOrderId(anyLong());
        
    }

    Order getOptionalOrder(){
        return Order.builder()
        .orderId(1)
        .orderQuantity(2)
        .orderStatus("SUCCESS")
        .paymentId(1)
        .paymentMode(PaymentMode.APPLE_PAY)
        .orderDate(Instant.now())
        .prodId(1L)
        .userId(1)
        .orderDate(Instant.now())        
        .build();
    }

    ProductProxy getOptionalProduct(){
        return ProductProxy.builder()
        .prodId(1L)
        .prodDesc("Some Product")
        .prodName("Iphone le le")
        .prodPrice(999)
        .prodQuantity(98)  
        .build();
    }

    Users getOptionalUsers(){
        return Users.builder()
        .userId(1)
        .firstName("Kaushal")
        .lastName("Suman")
        .email("Xyz@gmail.com")
            
        .build();
    }

}
