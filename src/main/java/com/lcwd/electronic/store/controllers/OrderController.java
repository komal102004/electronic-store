package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.*;
import com.lcwd.electronic.store.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    //create
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request)
    {
        OrderDto order=orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId){
        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage=ApiResponseMessage.builder()
                .status(HttpStatus.OK)
                .message("order is removed")
                .success(true)
                .build();
        return new ResponseEntity<>(responseMessage,HttpStatus.OK);

    }
    //get orders of the user
    //normal user
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrdersOfUser(@PathVariable String userId){
        List<OrderDto> orderOfUser=orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(orderOfUser,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getOrders(
                                                                @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
                                                                @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
                                                                @RequestParam(value = "sortBy",defaultValue = "orderedDate",required = false) String sortBy,
                                                                @RequestParam(value = "sortDir",defaultValue = "des",required = false)String sortDir
    ){
      PageableResponse<OrderDto> orders=orderService.getOrders(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable String orderId, @RequestBody OrderDto orderDto)
    {
       OrderDto updatedOrder =orderService.update(orderDto,orderId);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}
