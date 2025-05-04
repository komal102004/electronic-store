package com.lcwd.electronic.store.dtos;


import jakarta.validation.constraints.NotBlank;
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
public class CreateOrderRequest {
     @NotBlank(message = "Cart id is required ! !")
    private String cartId;
    @NotBlank(message = "Cart id is required ! !")
    private String userId;
    @NotBlank(message = "Cart id is required ! !")
    private String orderStatus="PENDING";
    // NON-PAID, PAID
    //enum
    //Boolean-> false=>NOT-PAID || true=>PAID
    private String paymentStatus="NOTPAID";

    @NotBlank(message = "Address is required ! !")
    private String billingAddress;
    @NotBlank(message = "Phone number  is required ! !")
    private  String billingPhone;
    @NotBlank(message = "Billing Name is required ! !")
    private String billingName;



}
