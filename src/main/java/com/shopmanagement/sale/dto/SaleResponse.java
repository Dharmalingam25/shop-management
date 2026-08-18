package com.shopmanagement.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaleResponse {

    private Long saleId;
    private String status;
    private int itemCount;
}