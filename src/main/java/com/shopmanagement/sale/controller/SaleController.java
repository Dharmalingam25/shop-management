package com.shopmanagement.sale.controller;

import com.shopmanagement.sale.dto.CreateSaleRequest;
import com.shopmanagement.sale.dto.SaleResponse;
import com.shopmanagement.sale.entity.Sale;
import com.shopmanagement.sale.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleResponse> createSale(
            @Valid @RequestBody CreateSaleRequest request) {

        SaleResponse response = saleService.createSale(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}