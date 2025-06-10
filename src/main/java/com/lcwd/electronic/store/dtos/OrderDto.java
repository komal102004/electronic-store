package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderDto {
    private String orderId;
    //PENDING ,DELIVERED,DISPATCHED
    //enum
    private String orderStatus="PENDING";
    // NON-PAID, PAID
    //enum
    //Boolean-> false=>NOT-PAID || true=>PAID
    private String payementStatus="NOTPAID";

    private int orderAmount;
    private String billingAddress;
    private  String billingPhone;
    private String billingName;
    private Date orderedDate=new Date();
    private Date deliveredDate;
    //user



    private List<OrderItemDto> orderItems=new ArrayList<>();



}
