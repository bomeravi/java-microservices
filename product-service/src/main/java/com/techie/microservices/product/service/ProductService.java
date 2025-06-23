package com.techie.microservices.product.service;

import com.techie.microservices.product.dto.ProductRequest;
import com.techie.microservices.product.dto.ProductResponse;
import com.techie.microservices.product.external.client.InventoryClient;
import com.techie.microservices.product.external.dto.InventoryRequest;
import com.techie.microservices.product.external.dto.InventoryResponse;
import com.techie.microservices.product.model.Product;
import com.techie.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final InventoryClient inventoryClient;

    public ProductResponse createProduct(ProductRequest productRequest) {
        // Logic to create a product
        Product product = Product.builder()
                .skuCode(productRequest.skuCode())
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        productRepository.save(product);
        log.info("Product created successfully: {}", product);

        InventoryResponse inventoryResponse = inventoryClient.upsertInventory(new InventoryRequest(productRequest.skuCode(), productRequest.quantity())).getBody();
        log.info("Product's inventory created: {}", inventoryResponse);

//        assert inventoryResponse != null;
        return new ProductResponse(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                inventoryResponse.quantity());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productsList = productRepository.findAll();
        for(Product product: productsList){
            System.out.println(product.getSkuCode());
        }
        return productsList
                .stream()
                .map(product -> new ProductResponse(product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        inventoryClient.getInventoryBySkuCode(product.getSkuCode()).getBody().quantity()))
                .toList();
    }
}
