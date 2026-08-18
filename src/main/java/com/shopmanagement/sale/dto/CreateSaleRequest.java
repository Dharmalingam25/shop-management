package com.shopmanagement.sale.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateSaleRequest {

    @NotEmpty(message = "Sale must contain at least one product")
    @Valid
    private List<SaleItemRequest> items;
}