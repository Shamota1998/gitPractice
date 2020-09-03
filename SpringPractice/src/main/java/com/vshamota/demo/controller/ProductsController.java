package com.vshamota.demo.controller;

import com.vshamota.demo.DTO.FilterDTO;
import com.vshamota.demo.domain.Cart;
import com.vshamota.demo.domain.Device;
import com.vshamota.demo.domain.User;
import com.vshamota.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller(value = "/products")
public class ProductsController {

    private final CategoryRepo categoryRepo;
    private final ProducerRepo producerRepo;
    private final DeviceRepo deviceRepo;
    private final CartRepo cartRepo;
    private final UserRepo userRepo;

    @GetMapping(value = "/list")
    public String getListOfProducts(Model model, @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(required = false) Boolean filtered) {
        if (filtered == null || !filtered) {
            model.addAttribute("categoryList", categoryRepo.findAll());
            model.addAttribute("producerList", producerRepo.findAll());
            model.addAttribute("filterObj", new FilterDTO());
            model.addAttribute("allProducts", deviceRepo.findAll(PageRequest.of(page, 3)));
            model.addAttribute("filtered", false);
        }
        return "products/showProducts";
    }

    @PostMapping(value = "/filter")
    public String getFilterObj(@ModelAttribute("filterObj") FilterDTO filterObj, Model model) {
        model.addAttribute("categoryList", categoryRepo.findAll());
        model.addAttribute("producerList", producerRepo.findAll());
        model.addAttribute("filtered", true);
        if (filterObj.getSort().equals("ASC")) {

            model.addAttribute("filtredProducts",
                    deviceRepo.findAllByCategoryInAndPriceBetweenAndProducerEqualsOrderByPriceAsc
                            (
                                    filterObj.getCategory(),
                                    filterObj.getMinPrice(),
                                    filterObj.getMaxPrice(),
                                    filterObj.getProducer()
                            ));

            return "products/showProducts";
        }

        model.addAttribute("filtredProducts",
                deviceRepo.findAllByCategoryInAndPriceBetweenAndProducerEqualsOrderByPriceDesc
                        (
                                filterObj.getCategory(),
                                filterObj.getMinPrice(),
                                filterObj.getMaxPrice(),
                                filterObj.getProducer()
                        ));
        return "products/showProducts";
    }

    @GetMapping(value = "/addToCart/{id}")
    public String addToCart(@PathVariable Integer id, @AuthenticationPrincipal User user) {

        Device addDevice = deviceRepo.findById(id).orElse(null);
        Cart cartByUser = cartRepo.findCartByUser(user);
        cartByUser.addDevice(addDevice);
        cartRepo.save(cartByUser);

        return "redirect:/list";
    }
}
