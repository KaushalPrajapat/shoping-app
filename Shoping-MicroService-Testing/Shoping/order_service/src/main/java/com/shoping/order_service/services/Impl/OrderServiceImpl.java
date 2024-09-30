package com.shoping.order_service.services.Impl;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoping.order_service.entities.Order;
import com.shoping.order_service.entities.PaymentEntity;
import com.shoping.order_service.entities.ProductProxy;
import com.shoping.order_service.entities.Users;
import com.shoping.order_service.errors.CustomException;
import com.shoping.order_service.external.proxies.PaymentServiceProxyIF;
import com.shoping.order_service.external.proxies.ProductServiceProxyIF;
import com.shoping.order_service.external.proxies.UserServiceProxyIF;
import com.shoping.order_service.models.OrderRequest;
import com.shoping.order_service.models.OrderResponse;
import com.shoping.order_service.repositories.OrderRepo;
import com.shoping.order_service.services.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ProductServiceProxyIF productProxy;

    @Autowired
    private PaymentServiceProxyIF paymentProxy;

    @Autowired
    private UserServiceProxyIF userProxy;

    @SuppressWarnings("null")
    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        log.info("Placing the order start Let's save to ");
        Order order = new Order();
        ResponseEntity<ProductProxy> productOptional = null;
        try {
            productOptional = productProxy.getProductById(orderRequest.getProdId());
        } catch (CustomException ex) {
            System.out.println(ex.getMessage());
            throw new CustomException(ex.getMessage(),
                    ex.getHttpStatus());
        }
        System.out.println(productOptional);
        ProductProxy product = productOptional.getBody();

        System.out.println(product.getProdQuantity());

        if (product.getProdQuantity() == 0) {
            throw new CustomException("OUT OF STOCK. Will be right back", "OUT_OF_STOCK");
        }
        if (product.getProdQuantity() < orderRequest.getOrderQuantity()) {
            throw new CustomException("Order Quantity is Greater than stock." + orderRequest.getOrderQuantity()
                    + ". You can order upto - " + product.getProdQuantity(), "NOT_ENOUGH_STOCK");
        }

        // Payment Validation + do Payment
        // Setting up som eproperties or order
        BeanUtils.copyProperties(orderRequest, order); // -- It got me properties like, prodId,
                                                       // orderQuantity,PaymentMode
        order.setOrderStatus("NEW");
        order.setOrderDate(Instant.now());
        order.setTotalAmount(order.getOrderQuantity() * product.getProdPrice());
        order.setUserId(orderRequest.getUserId());
        Order savedOrder = null;
        try {
            // First Reduce Product in Product DBT
            // ResponseEntity<?>, This update is eturn one entity or type
            // ResponseEntity<Boolean> I thoght or using but i used try catch that is
            // doesn't matter
            productProxy.outUpdateProductQuantity(orderRequest.getProdId(), orderRequest.getOrderQuantity());
        } catch (CustomException ex) {
            throw new CustomException(ex.getMessage(),
                    ex.getHttpStatus());
        }
        try {
            // Save order to database
            savedOrder = orderRepo.save(order);
        } catch (CustomException ex) {
            // Resetting Quantity to base
            productProxy.inUpdateProductQuantity(orderRequest.getProdId(),
                    product.getProdId() + orderRequest.getOrderQuantity());

                    throw new CustomException(ex.getMessage(),
                    ex.getHttpStatus());
        }
        // Prepare Payment things
        //
        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(savedOrder.getOrderId());
        payment.setUserId(savedOrder.getUserId());
        payment.setPayAmount(savedOrder.getTotalAmount());
        payment.setPaymentMode(savedOrder.getPaymentMode().toString());
        payment.setPaymentReferance(UUID.randomUUID().toString());
        System.out.println("Doing payment");
        ResponseEntity<?> doPayment = null;
        try {
            payment.setPaymentStatus("SUCCESS");
            doPayment = paymentProxy.pay(payment);
            System.out.println(doPayment);
        } catch (CustomException ex) {
            System.out.println(ex);
            orderRepo.delete(savedOrder);
            productProxy.inUpdateProductQuantity(orderRequest.getProdId(),
                    product.getProdId() + orderRequest.getOrderQuantity());

                    throw new CustomException(ex.getMessage(),
                    ex.getHttpStatus());
        }
        // 
        System.out.println("Is payment done");
        LinkedHashMap<?, ?> paymentValidations = (LinkedHashMap<?, ?>) doPayment.getBody();
        System.out.println(paymentValidations);
        if (paymentValidations.get("message") != null) {
            orderRepo.delete(savedOrder);
            productProxy.inUpdateProductQuantity(orderRequest.getProdId(),
                    product.getProdId() + orderRequest.getOrderQuantity());

            throw new CustomException(paymentValidations.get("message").toString(),
                    paymentValidations.get("httpStatus").toString());
        }
        try {
            order.setPaymentId((Integer) paymentValidations.get("paymentId"));
            order.setOrderStatus("PLACED");
            orderRepo.save(order);
        } catch (Exception e) {
            throw new CustomException("Something wrong with payment service", "PAYMENT ISSUE");
        }
        BeanUtils.copyProperties(orderRequest, order);
        //
        // Get UserData to Response in OrderResponse
        ResponseEntity<Users> userData = userProxy.getUsersByUserId(payment.getUserId());
        Users user = userData.getBody();

        if(user.getEmail() == null){
            throw new CustomException("User does not exists", "USER_NOT_FOUND");
        }

        System.out.println("IS ALL SET RETURN ORDER RESPOMNSE");
        return OrderResponse.builder()
                // ORDER
                .orderId(savedOrder.getOrderId())
                .orderDate(savedOrder.getOrderDate())
                .orderQuantity(savedOrder.getOrderQuantity())
                .orderStatus(savedOrder.getOrderStatus())
                // USER
                .userName(user.getFirstName() + user.getLastName())
                .userEmail(user.getEmail())
                // PRODUCT
                .prodName(product.getProdName())
                .prodPrice(product.getProdPrice())
                // PAYMENT
                .totalAmount(savedOrder.getTotalAmount())
                .paymentMode(savedOrder.getPaymentMode())
                .paymentId(savedOrder.getPaymentId())
                .paymentReferance(payment.getPaymentReferance())
                .message("Order Created Successfully !!")
                .build();

    }

    @SuppressWarnings("null")
    @Override
    public OrderResponse deleteOrder(long orderId) {
        Optional<Order> isExist;
        Order savedOrder;

        // try {
        // [java.util.NoSuchElementException: No value present]
        // In case no vales exists with given condition , It's throw this Exception that
        // is not my custom
        Optional<Order> check = orderRepo.findByOrderId(orderId);
        if (check.isEmpty()) {
            throw new CustomException("Order doen't exists.", "NOT_EXISTS.");
        }
        isExist = orderRepo.findByOrderIdAndOrderStatusNot(orderId, "CANCELLED");
        // } catch (Exception e) {
        if (isExist.isEmpty() && check.isPresent()) {
            throw new CustomException("Order Already Cancelled.", "ALREADY_CANCELLED.");
        }
        // }
        System.out.println(isExist.get());

        // Close and refund
        // System.out.println("Let me refund you");

        ResponseEntity<?> undoPayment = null;
        try {
            undoPayment = paymentProxy.undoPayment(isExist.get().getPaymentId());

        } catch (Exception e) {
            throw new CustomException("Refund unsuccessfull1",
                    "PAYMENT_FAILED");
        }
        // /-------------------------------
        LinkedHashMap<?, ?> paymentValidations = (LinkedHashMap<?, ?>) undoPayment.getBody();
        System.out.println(paymentValidations);
        if (paymentValidations.get("message") != null) {
            throw new CustomException(paymentValidations.get("message").toString(),
                    paymentValidations.get("httpStatus").toString());
        }
        try {
            try {
                productProxy.inUpdateProductQuantity(isExist.get().getProdId(), isExist.get().getOrderQuantity());

            } catch (Exception e) {
                throw new CustomException("Product Service is down", "PRODUCT_SERVICE_DOWN");
            }
            isExist.get().setOrderStatus("CANCELLED");
            isExist.get().setOrderDate(Instant.now());
            savedOrder = isExist.get();
            orderRepo.save(savedOrder);
        } catch (Exception e) {
            throw new CustomException("Something wrong with payment service", "PAYMENT ISSUE");
        }
        // Fetch User Data if service is up and running; If not then give empty data
        // Fetch User Data if service is up and running; If not then give empty data
        Users user = new Users(0, "", "", "");
        try {
            ResponseEntity<Users> userEntity = userProxy.getUsersByUserId((long) savedOrder.getUserId());
            user = userEntity.getBody();
        } catch (Exception e) {
        }
        System.out.println(user);
        // Close that mean delete order or cancle
        // extract isExist to savedOrder that is same as updatedOrder
        String message = "";
        if (user.getUserId() == 0) {
            message = "USER SERVICE WAS DOWN, COULD NOT FETCH USER";
        }
        System.out.println(undoPayment);

        // Fetch Product Data if service is up and running; If not then give empty data
        ProductProxy product = new ProductProxy();
        try {
            ResponseEntity<ProductProxy> productEntity = productProxy.getProductById(savedOrder.getProdId());
            product = productEntity.getBody();
        } catch (Exception e) {
            product.setProdName("Cound not found");
            product.setProdPrice(0);
        }
        return OrderResponse.builder()
                // ORDER
                .orderId(savedOrder.getOrderId())
                .orderDate(savedOrder.getOrderDate())
                .orderQuantity(savedOrder.getOrderQuantity())
                .orderStatus(savedOrder.getOrderStatus())
                // USER
                .userName(user.getFirstName() + " " + user.getLastName())
                .userEmail(user.getEmail())
                // PRODUCT
                .prodName(product.getProdName())
                .prodPrice(product.getProdPrice())
                // PAYMENT
                .totalAmount(savedOrder.getTotalAmount())
                .paymentMode(savedOrder.getPaymentMode())
                .paymentId(savedOrder.getPaymentId())
                .paymentReferance((String) paymentValidations.get("paymentReferance"))
                .message(user.getUserId() == 0 ? message : "")
                .build();
    }

    // @SuppressWarnings("null")
    @SuppressWarnings("null")
    @Override
    public OrderResponse getOrderById(long orderId) {
        Optional<Order> isExist = null;

        // [java.util.NoSuchElementException: No value present]
        // In case no vales exists with given condition , It's throw this Exception that
        // is not my custom
        try {
            isExist = orderRepo.findByOrderId(orderId);
        } catch (Exception e) {

        }
        if (isExist == null) {
            throw new CustomException("Tables are not ready", "DATABASE_ISSUE");
        }

        if (isExist.isEmpty()) {
            throw new CustomException("Order doen't exists.", "NOT_EXISTS.");
        }
        System.out.println(isExist.get());
        Order savedOrder = isExist.get();

        // Fetch User Data if service is up and running; If not then give empty data
        Users user = new Users(0, "", "", "");
        try {
            ResponseEntity<Users> userEntity = userProxy.getUsersByUserId((long) savedOrder.getUserId());
            user = userEntity.getBody();
        } catch (Exception e) {
        }
        System.out.println(user);
        // Close that mean delete order or cancle
        // extract isExist to savedOrder that is same as updatedOrder
        String message = "";
        if (user.getUserId() == 0) {
            message = "USER SERVICE WAS DOWN, COULD NOT FETCH USER";
        }
        // Fetch Product Data if service is up and running; If not then give empty data
        ProductProxy product = new ProductProxy();
        try {
            ResponseEntity<ProductProxy> productEntity = productProxy.getProductById(savedOrder.getProdId());
            product = productEntity.getBody();
        } catch (Exception e) {
            product.setProdName("Cound not found");
            product.setProdPrice(0);
        }

        return OrderResponse.builder()
                // ORDER
                .orderId(savedOrder.getOrderId())
                .orderDate(savedOrder.getOrderDate())
                .orderQuantity(savedOrder.getOrderQuantity())
                .orderStatus(savedOrder.getOrderStatus())
                // USER
                .userName(user.getFirstName() + " " + user.getLastName())
                .userEmail(user.getEmail())
                // PRODUCT
                .prodName(product.getProdName())
                .prodPrice(product.getProdPrice())
                // PAYMENT
                .totalAmount(savedOrder.getTotalAmount())
                .paymentMode(savedOrder.getPaymentMode())
                .paymentId(savedOrder.getPaymentId())
                .message(user.getUserId() == 0 ? message : "")
                .build();
    }

    @Override
    public List<Optional<Order>> getAllOrderByUserId(long userId) {

        // [java.util.NoSuchElementException: No value present]
        // In case no vales exists with given condition , It's throw this Exception that
        // is not my custom
        List<Optional<Order>> allByUserId = orderRepo.findAllByUserId(userId);

        return allByUserId;

    }

}
