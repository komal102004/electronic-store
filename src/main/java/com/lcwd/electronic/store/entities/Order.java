package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@Entity
@Builder
public class Order {
    @Id
    private String orderId;
    //PENDING ,DELIVERED,DISPATCHED
    //enum
    private String orderStatus;
    // NON-PAID, PAID
    //enum
    //Boolean-> false=>NOT-PAID || true=>PAID
    private String payementStatus;

    private int orderAmount;
    private String billingAddress;
    private  String billingPhone;
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;
    //user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();
}
