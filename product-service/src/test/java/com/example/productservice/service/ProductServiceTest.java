package com.example.productservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.productservice.client.InventoryClient;
import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.UpsertStockRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryService categoryService;

	@Mock
	private ProductMapper mapper;

	@Mock
	private InventoryClient inventoryClient;

	@InjectMocks
	private ProductService productService;

	@Test
	void createShouldCreateInventoryRegisterWithZeroStock() {
		ProductCreateRequest request = new ProductCreateRequest(null, "Notebook", "Gaming", new BigDecimal("199900"),
				"USD", true);
		Product saved = new Product(UUID.randomUUID(), null, null, "Notebook", "Gaming", new BigDecimal("199900"),
				"USD", true);
		ProductResponse mapped = new ProductResponse(saved.getId(), null, null, "Notebook", "Gaming",
				new BigDecimal("199900"), "USD", true, Instant.now(), Instant.now());

		when(productRepository.save(any(Product.class))).thenReturn(saved);
		when(mapper.toResponse(saved)).thenReturn(mapped);

		ProductResponse response = productService.create(request);

		ArgumentCaptor<UpsertStockRequest> captor = ArgumentCaptor.forClass(UpsertStockRequest.class);
		verify(inventoryClient).upsertStock(captor.capture());
		UpsertStockRequest upsertRequest = captor.getValue();
		assertEquals(saved.getId(), upsertRequest.productId());
		assertEquals(0, upsertRequest.availableQuantity());
		assertSame(mapped, response);
	}

}
