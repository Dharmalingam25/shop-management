package com.shopmanagement.product.service;

import com.shopmanagement.product.dto.ProductRequest;
import com.shopmanagement.product.entity.Product;
import com.shopmanagement.product.exception.DuplicateProductException;
import com.shopmanagement.product.exception.ProductNotFoundException;
import com.shopmanagement.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // CREATE
    public Product addProduct(ProductRequest request) {

        if (productRepository.existsByProductCode(request.getProductCode())) {
            throw new DuplicateProductException(
                    "Product already exists with product code: "
                            + request.getProductCode()
            );
        }

        Product product = new Product();

        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setUnit(request.getUnit());
        product.setQuantity(request.getQuantity());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setSupplierId(request.getSupplierId());

        return productRepository.save(product);
    }

    // GET ALL
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET BY DATABASE ID
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        )
                );
    }

    // GET BY PRODUCT CODE
    public Product getProductByCode(String productCode) {

        return productRepository.findByProductCode(productCode)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with code: "
                                        + productCode
                        )
                );
    }

    // GET BY CATEGORY
    public List<Product> getProductsByCategory(String category) {

        return productRepository.findByCategoryIgnoreCase(category);
    }

    // SEARCH BY PRODUCT NAME
    public List<Product> searchProducts(String productName) {

        return productRepository
                .findByProductNameContainingIgnoreCase(productName);
    }

    // UPDATE
    public Product updateProduct(
            Long id,
            ProductRequest request) {

        Product product = getProductById(id);

        /*
         * If the product code is changed,
         * make sure the new code is not already used.
         */
        if (!product.getProductCode().equals(request.getProductCode())
                && productRepository.existsByProductCode(
                request.getProductCode())) {

            throw new DuplicateProductException(
                    "Product already exists with product code: "
                            + request.getProductCode()
            );
        }

        product.setProductCode(request.getProductCode());
        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setUnit(request.getUnit());
        product.setQuantity(request.getQuantity());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setSupplierId(request.getSupplierId());

        return productRepository.save(product);
    }

    // DELETE
    public void deleteProduct(Long id) {

        Product product = getProductById(id);

        productRepository.delete(product);
    }
}