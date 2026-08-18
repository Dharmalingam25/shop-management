package com.shopmanagement.sale.service;

import com.shopmanagement.product.entity.Product;
import com.shopmanagement.product.exception.ProductNotFoundException;
import com.shopmanagement.product.repository.ProductRepository;
import com.shopmanagement.sale.dto.CreateSaleRequest;
import com.shopmanagement.sale.dto.SaleItemRequest;
import com.shopmanagement.sale.dto.SaleResponse;
import com.shopmanagement.sale.entity.Sale;
import com.shopmanagement.sale.entity.SaleItem;
import com.shopmanagement.sale.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleService(
            SaleRepository saleRepository,
            ProductRepository productRepository) {

        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public SaleResponse createSale(CreateSaleRequest request) {

        Sale sale = new Sale();

        for (SaleItemRequest itemRequest : request.getItems()) {

            Product product = productRepository
                    .findByProductCode(itemRequest.getProductCode())
                    .orElseThrow(() ->
                            new ProductNotFoundException(
                                    "Product not found with code: "
                                            + itemRequest.getProductCode()
                            )
                    );

            if (!Boolean.TRUE.equals(product.getActive())) {
                throw new IllegalStateException(
                        "Product is inactive: "
                                + product.getProductName()
                );
            }

            if (product.getQuantity() < itemRequest.getQuantity()) {

                throw new IllegalStateException(
                        "Insufficient stock for product: "
                                + product.getProductName()
                                + ". Available: "
                                + product.getQuantity()
                );
            }
        }

        // All products validated successfully
        for (SaleItemRequest itemRequest : request.getItems()) {

            Product product = productRepository
                    .findByProductCode(itemRequest.getProductCode())
                    .orElseThrow(() ->
                            new ProductNotFoundException(
                                    "Product not found with code: "
                                            + itemRequest.getProductCode()
                            )
                    );

            int soldQuantity = itemRequest.getQuantity();

            product.setQuantity(
                    product.getQuantity() - soldQuantity
            );

            productRepository.save(product);

            SaleItem saleItem = new SaleItem();

            saleItem.setProduct(product);
            saleItem.setQuantity(soldQuantity);
            saleItem.setSellingPrice(product.getSellingPrice());

            sale.addItem(saleItem);
        }

        Sale savedSale = saleRepository.save(sale);

        return new SaleResponse(
                savedSale.getId(),
                "COMPLETED",
                savedSale.getItems().size()
        );
    }
}