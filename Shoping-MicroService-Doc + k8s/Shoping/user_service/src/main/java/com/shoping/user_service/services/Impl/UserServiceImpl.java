package com.shoping.user_service.services.Impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoping.user_service.entities.Users;
import com.shoping.user_service.errors.CustomException;
import com.shoping.user_service.external.proxies.OrderProxyIF;
import com.shoping.user_service.external.proxies.PaymentProxyIF;
import com.shoping.user_service.external.proxies.ProductProxyIF;
import com.shoping.user_service.models.OrderModel;
import com.shoping.user_service.models.PaymentEntity;
import com.shoping.user_service.models.ProductEntity;
import com.shoping.user_service.models.UserOrderResponse;
import com.shoping.user_service.repositories.UsersRepo;
import com.shoping.user_service.services.UsersService;

@Service
public class UserServiceImpl implements UsersService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private OrderProxyIF orderProxy;

    @Autowired
    private PaymentProxyIF paymentProxy;

    @Autowired
    private ProductProxyIF productProxy;

    @Override
    public Users addUser(Users user) {
        Optional<Users> isExistsEmail = usersRepo.findByEmail(user.getEmail());
        Optional<Users> isExistsPhone = usersRepo.findByPhone(user.getPhone());
        if (user.getEmail() == null || user.getPhone() == null || user.getFirstName() == null) {
            throw new CustomException("Fill All required Data", "NOT_ENOUGH_DATA");
        }
        Users usr = null;
        if (!isExistsEmail.isEmpty() && !isExistsPhone.isEmpty()) {
            throw new CustomException("Phone & Email already Exists", "TEMPERARY_ACCOUNT");
        } else if (isExistsEmail.isEmpty() && !isExistsPhone.isEmpty()) {
            throw new CustomException("Phone already Exists", "TEMPERARY_ACCOUNT");
        } else if (!isExistsEmail.isEmpty() && isExistsPhone.isEmpty())
            throw new CustomException("Email already Exists", "TEMPERARY_ACCOUNT");
        else {
            usr = usersRepo.save(user);
        }
        return usr;
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }

    @Override
    public Users getUserByUserId(long userId) {
        Optional<Users> usr = null;
        try {
            usr = usersRepo.findById(userId);
        } catch (Exception e) {
            throw new CustomException("users_Details table are not ready", "DATABSE_ISSUE");
        }
        // if (usr == null) {
        //     throw new CustomError("BackEnd tables are not ready", "DATABSE_ISSUE");
        // }
        System.out.println("user + " + usr.isEmpty());
        if (usr.isEmpty()) {
            throw new CustomException("User With Id + " + userId + " doesn't exists", "USER_NOT_FOUND");
        }
        return usr.get();
    }

    @Override
    public Users deleteUserByUserId(long userId) {
        Users usr = getUserByUserId(userId);
        usersRepo.delete(usr);
        return usr;
    }

    // Balance Can't be updated from here
    @Override
    public Users updateUser(Users user, long userId) {
        Optional<Users> isExistsEmail = usersRepo.findByEmail(user.getEmail());
        Optional<Users> isExistsPhone = usersRepo.findByPhone(user.getPhone());
        if (isExistsEmail.isPresent() && isExistsEmail.get().getUserId() != userId) {
            throw new CustomException("Email already connected to other accound",
                    "USER_CAN'T_BE_UPDATED");

        } else if (isExistsPhone.isPresent() && isExistsPhone.get().getUserId() != userId) {
            throw new CustomException("Phone already connected to other accound",
                    "USER_CAN'T_BE_UPDATED");

        }
        user.setBalance(isExistsEmail.get().getBalance());
        user.setUserId(userId);
        Users usr = usersRepo.save(user);
        return usr;
    }

    @Override
    public Users addBalance(long userId, double balance) {
        Users users = getUserByUserId(userId);
        users.setBalance(users.getBalance() + balance);
        return usersRepo.save(users);
    }

    // do payment means substracting balance
    @Override
    public Users doPayment(long userId, double balance) {
        Users user = getUserByUserId(userId);
        if (user.getBalance() < balance) {
            throw new CustomException("Payment Cam't be done due to insufficent balance", "INSUFFICENT_BALANCE_USER");
        }
        user.setBalance(user.getBalance() - balance);
        return usersRepo.save(user);
    }

    // Undo means adding balance
    @Override
    public Users undoPayment(long userId, double balance) {
        Users user = getUserByUserId(userId);
        user.setBalance(user.getBalance() + balance);
        return usersRepo.save(user);
    }

    @Override
    public double getBalance(long userId) {
        Users user = getUserByUserId(userId);
        return user.getBalance();
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    public UserOrderResponse getAllOrdersOfAUser(long userId) {
        // I need all order, If not any - It will recieve one empty order list as
        // response entity
        Users user = getUserByUserId(userId);
        
        // Getting all order for user
        ResponseEntity<?> allOrderByUserId = orderProxy.getAllOrderByUserId(userId);

        ArrayList<LinkedHashMap<?, ?>> orders = (ArrayList<LinkedHashMap<?, ?>>) allOrderByUserId.getBody();
        List<OrderModel> odrs = new ArrayList<>();
        for (LinkedHashMap<?, ?> order : orders) {
            OrderModel om = new OrderModel();
            om.setOrderDate(order.get("orderDate").toString());
            om.setOrderId((Integer) order.get("orderId"));
            om.setOrderQuantity((Integer) order.get("orderQuantity"));
            om.setOrderStatus(order.get("orderStatus").toString());

            // Try to fetch product details; If fails then return null
            long prodId = (Integer) order.get("prodId");
            ProductEntity pm = new ProductEntity();
            ResponseEntity<?> productById = productProxy.getProductById(prodId);
            LinkedHashMap<?, ?> product = (LinkedHashMap<?, ?>) productById.getBody();
            pm.setProdDesc(product.get("prodDesc") == null ? "NON" : product.get("prodDesc").toString());
            pm.setProdName(product.get("prodName") == null ? "NON" : product.get("prodName").toString());
            pm.setProdPrice(product.get("prodPrice") == null ? 0.00 : (double) product.get("prodPrice"));
            om.setProducts(pm);

            // Try to fetch Payment details; If fails then return null
            PaymentEntity payment = new PaymentEntity();
            long paymentId = (Integer) order.get("paymentId");
            ResponseEntity<?> paymentByPaymentId = paymentProxy.getPaymentByPaymentId(paymentId);
            LinkedHashMap<?, ?> paymentHashMap = (LinkedHashMap<?, ?>) paymentByPaymentId.getBody();
            payment.setPaymentId(paymentId);
            payment.setPayAmount(
                    paymentHashMap.get("payAmount") == null ? 0 : (double) paymentHashMap.get("payAmount"));
            payment.setPaymentMode(
                    paymentHashMap.get("paymentMode") == null ? "NON" : paymentHashMap.get("paymentMode").toString());
            payment.setPaymentReferance(paymentHashMap.get("paymentReferance") == null ? "NON"
                    : paymentHashMap.get("paymentReferance").toString());
            payment.setPaymentDate(
                    paymentHashMap.get("paymentDate") == null ? "NON" : paymentHashMap.get("paymentDate").toString());
            om.setPayment(payment);
            odrs.add(om);

        }

        System.out.println(orders);
        System.out.println(user);
        return UserOrderResponse.builder()
                .userId(userId)
                .userEmail(user.getEmail())
                .userName(user.getFirstName() + " " + user.getLastName())
                .userPhone(user.getPhone())
                .orders(odrs)
                .build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserOrderResponse getAllPaymentsOfAUser(long userId) {
        // I need all payment, If not any - It will recieve one empty order list as
        // response entity
        Users user = getUserByUserId(userId);
        

        ResponseEntity<?> allPaymentsByUserId = paymentProxy.getAllPaymentsByUserId(userId);
        List<PaymentEntity> payments = (List<PaymentEntity>) allPaymentsByUserId.getBody();
        System.out.println(payments);
        return UserOrderResponse.builder()
                .userId(userId)
                .userEmail(user.getEmail())
                .userName(user.getFirstName() + " " + user.getLastName())
                .userPhone(user.getPhone())
                .payments(payments)
                .build();

    }

}
