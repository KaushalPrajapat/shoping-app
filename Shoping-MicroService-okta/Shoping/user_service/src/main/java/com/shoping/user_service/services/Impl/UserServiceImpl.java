package com.shoping.user_service.services.Impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shoping.user_service.entities.Users;
import com.shoping.user_service.errors.CustomError;
import com.shoping.user_service.external.proxies.OrderProxyIF;
import com.shoping.user_service.external.proxies.PaymentProxyIF;
import com.shoping.user_service.external.proxies.ProductProxyIF;
import com.shoping.user_service.models.OrderModel;
import com.shoping.user_service.models.PaymentEntity;
import com.shoping.user_service.models.ProductModel;
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
            throw new CustomError("Fill All required Data", "NOT_ENOUGH_DATA");
        }
        Users usr = null;
        if (!isExistsEmail.isEmpty() && !isExistsPhone.isEmpty()) {
            throw new CustomError("Phone & Email already Exists", "TEMPERARY_ACCOUNT");
        } else if (isExistsEmail.isEmpty() && !isExistsPhone.isEmpty()) {
            throw new CustomError("Phone already Exists", "TEMPERARY_ACCOUNT");
        } else if (!isExistsEmail.isEmpty() && isExistsPhone.isEmpty())
            throw new CustomError("Email already Exists", "TEMPERARY_ACCOUNT");
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
    public Optional<Users> getUserByUserId(long userId) {
        Optional<Users> usr = null;
        try {
            usr = usersRepo.findById(userId);
        } catch (Exception e) {

        }
        if (usr == null) {
            throw new CustomError("BackEnd tables are not ready", "DATABSE_ISSUE");
        }
        System.out.println("user + " + usr.isEmpty());
        if (usr.isEmpty()) {
            throw new CustomError("User With Id + " + userId + " doesn't exists", "USER_DOESNT EXISTS");
        }
        return usr;
    }

    @Override
    public Users deleteUserByUserId(long userId) {
        Optional<Users> usr = getUserByUserId(userId);
        usersRepo.delete(usr.get());
        return usr.get();
    }

    // Balance Can't be updated from here
    @Override
    public Users updateUser(Users user, long userId) {
        Optional<Users> isExistsEmail = usersRepo.findByEmail(user.getEmail());
        Optional<Users> isExistsPhone = usersRepo.findByPhone(user.getPhone());
        if (isExistsEmail.isPresent() && isExistsEmail.get().getUserId() != userId) {
            throw new CustomError("Email already connected to other accound",
                    "USER_CAN'T BE UPDATED");

        } else if (isExistsPhone.isPresent() && isExistsPhone.get().getUserId() != userId) {
            throw new CustomError("Phone already connected to other accound",
                    "USER_CAN'T BE UPDATED");

        }
        user.setBalance(isExistsEmail.get().getBalance());
        user.setUserId(userId);
        Users usr = usersRepo.save(user);
        return usr;
    }

    @Override
    public Users addBalance(long userId, double balance) {
        Optional<Users> users = getUserByUserId(userId);
        Users user = users.get();
        user.setBalance(user.getBalance() + balance);
        return usersRepo.save(user);
    }

    // do payment means substracting balance
    @Override
    public Users doPayment(long userId, double balance) {
        Optional<Users> users = getUserByUserId(userId);
        Users user = users.get();
        if (user.getBalance() < balance) {
            throw new CustomError("Payment Cam't be done due to insufficent balance", "INSUFFICENT_BALANCE");
        }
        user.setBalance(user.getBalance() - balance);
        return usersRepo.save(user);
    }

    // Undo means adding balance
    @Override
    public Users undoPayment(long userId, double balance) {
        Optional<Users> users = getUserByUserId(userId);
        Users user = users.get();
        user.setBalance(user.getBalance() + balance);
        return usersRepo.save(user);
    }

    @Override
    public double getBalance(long userId) {
        Optional<Users> users = getUserByUserId(userId);
        Users user = users.get();
        return user.getBalance();
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    public UserOrderResponse getAllOrdersOfAUser(long userId) {
        // I need all order, If not any - It will recieve one empty order list as
        // response entity
        Optional<Users> isExists = getUserByUserId(userId);
        if (isExists.isEmpty()) {
            throw new CustomError("User doesn't exists with ID - " + userId, "USER_NOT_FOUND");
        }
        Users user = isExists.get();
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
            ProductModel pm = new ProductModel();
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
        Optional<Users> isExists = getUserByUserId(userId);
        if (isExists.isEmpty()) {
            throw new CustomError("User doesn't exists with ID - " + userId, "USER_NOT_FOUND");
        }
        Users user = isExists.get();

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
