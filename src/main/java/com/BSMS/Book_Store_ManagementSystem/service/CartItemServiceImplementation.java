package com.BSMS.Book_Store_ManagementSystem.service;

import com.BSMS.Book_Store_ManagementSystem.model.Cart;
import com.BSMS.Book_Store_ManagementSystem.model.CartItem;
import com.BSMS.Book_Store_ManagementSystem.model.Products;
import com.BSMS.Book_Store_ManagementSystem.repository.CartItemRepository;
import com.BSMS.Book_Store_ManagementSystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImplementation implements CartItemService{
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ProductRepository productRepository;


    @Override
    public ResponseEntity<String> addCartItem(Long productId) {
        Products productOpt = productRepository.findById(productId).orElse(null);
        if (productOpt==null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        Products product = productRepository.findById(productId).get();
        System.out.println(product);
        if (product.getStock() <= 0) {
            return ResponseEntity.badRequest().body("Product is out of stock");
        }

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setProduct(product);
        cartItemRepository.save(cartItem);
        System.out.println(cartItem);

        return ResponseEntity.ok("Product added to cart");
    }

    @Override
    public ResponseEntity<String> updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItem cartItemOpt = cartItemRepository.findById(cartItemId).orElse(null);
        if (cartItemOpt==null) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }

        CartItem cartItem = cartItemOpt.getProduct().getCartItem();
        Products product = cartItem.getProduct();
        if (product.getStock() < quantity) {
            return ResponseEntity.badRequest().body("Insufficient stock");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return ResponseEntity.ok("Cart item quantity updated");
    }

    @Override
    public ResponseEntity<String> removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            return ResponseEntity.badRequest().body("Cart item not found");
        }

        cartItemRepository.deleteById(cartItemId);
        return ResponseEntity.ok("Cart item removed");
    }

    @Override
    public ResponseEntity<List<CartItem>> getCartItems() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        return ResponseEntity.ok(cartItems);
    }


}
