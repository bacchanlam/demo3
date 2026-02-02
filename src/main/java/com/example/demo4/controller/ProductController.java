package com.example.demo4.controller;


import com.example.demo4.model.Product;
import com.example.demo4.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String Index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "product/products";
    }

    @GetMapping("/create")
    public String Create(Model model) {
        model.addAttribute("product", new Product());
        return "product/create";
    }

    @PostMapping("/create")
    public String Create(@Valid Product newProduct,
                         BindingResult result,
                         @RequestParam MultipartFile imageProduct,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            return "product/create";
        }

        productService.updateImage(newProduct, imageProduct);
        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String Edit(@PathVariable int id, Model model) {
        Product find = productService.get(id);
        if (find == null) {
            return "Product not found with ID: " + id; // error page
        }
        model.addAttribute("product", find);
        return "product/edit";
    }

    @PostMapping("/edit")
    public String Edit(@Valid Product editProduct,
                       BindingResult result,
                       @RequestParam MultipartFile imageProduct,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            return "product/edit";
        }

        productService.updateImage(editProduct, imageProduct);
        productService.update(editProduct);
        return "redirect:/products"; // Redirect to the products page after successful update
    }
    @GetMapping("/delete/{id}")
    public String Delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }

}
