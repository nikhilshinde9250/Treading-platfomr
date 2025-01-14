package com.trade.service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.trade.domain.PaymentMethod;
import com.trade.domain.PaymentOrderStatus;
import com.trade.modal.PaymentOrder;
import com.trade.modal.User;
import com.trade.repository.PaymentOrderRepository;
import com.trade.response.PaymentResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${stipe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
      PaymentOrder paymentOrder=new PaymentOrder();
      paymentOrder.setUser(user);
      paymentOrder.setAmount(amount);
      paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentOrderRepository.findById(id).orElseThrow(()->new Exception("payment order not found"));
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder.getStatus()==null){
            paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        }
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpay=new RazorpayClient(apiKey, apiSecretKey);
                Payment payment=razorpay.payments.fetch(paymentId);

                Integer amount=payment.get("amount");
                String status=payment.get("status");

                if (status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        Long Amount=amount*100;
        try {
            // instantiate a razorpay client with your key id and secret
            RazorpayClient razorpay=new RazorpayClient(apiKey,apiSecretKey);
            // json object
            JSONObject payLinkRequest=new JSONObject();
            payLinkRequest.put("amount",amount);
            payLinkRequest.put("currency","INR");

            //create Jsonobject with the customer details
            JSONObject customer=new JSONObject();
            customer.put("name",user.getFullName());

            customer.put("email",user.getEmail());
            payLinkRequest.put("customer",customer);

            //create a json object with the notification settings
            JSONObject notify=new JSONObject();
            notify.put("email",true);
            payLinkRequest.put("notify",notify);

            //set the reminder settings
            payLinkRequest.put("reminder_enable", true);

            //set callback url
            payLinkRequest.put("callback_url","http://localhost:5173/wallet?order_id="+orderId);
            payLinkRequest.put("callback_method","get");


            //create the payment link using the paymentLink.create() method
            PaymentLink payment=razorpay.paymentLink.create(payLinkRequest);

            String paymentLinkId=payment.get("id");
            String paymentLinkUrl=payment.get("short_url");

            PaymentResponse res=new PaymentResponse();
            res.setPayment_url(paymentLinkUrl);
            return res;

        } catch (RazorpayException e) {
            System.out.println("Error creating payment link: "+e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount,
                                                   Long orderId) throws StripeException {
        Stripe.apiKey=stripeSecretKey;
        SessionCreateParams params= SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Top up wallet")
                                                .build()
                                ).build()
                ).build()
                ).build();
        Session session=Session.create(params);
        System.out.println("session _____"+session);
        PaymentResponse res=new PaymentResponse();
        res.setPayment_url(session.getUrl());
        return res;
    }
}
