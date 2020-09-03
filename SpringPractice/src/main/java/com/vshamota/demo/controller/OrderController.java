package com.vshamota.demo.controller;

import com.vshamota.demo.DTO.FinalOrderDTO;
import com.vshamota.demo.DTO.OrderDTO;
import com.vshamota.demo.domain.*;
import com.vshamota.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class OrderController {
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final DeviceRepo deviceRepo;
    private final OrderRepo orderRepo;
    private final Order_device orderDeviceRepo;

    List<FinalOrderDTO> finalList;
    OrderDTO order;
//    @GetMapping("/makeOrder/{id}")
//    public String makeOrderPage(@RequestParam(name = "qty") Integer qty, @PathVariable Integer id, Model model){
//    Device device = deviceRepo.findById(id).orElse(null);
//     model.addAttribute("orderedDevice", device);
//     model.addAttribute("QTY", qty);
//     Float totalPrice = qty*device.getPrice();
//     model.addAttribute("totalPrice", totalPrice);
//        return "products/order";
//    }
//
//    @PostMapping("/submitOrder")
//    public String submitOrder(Model model, @RequestParam(name="qty") Integer qty,
//                              @RequestParam(name="deviceId") Integer id,
//                              @AuthenticationPrincipal User user){
//        Device device =deviceRepo.findById(id).orElseThrow(NoSuchElementException::new);
//        device.setQTY(device.getQTY()-qty);
//        Orders newOrder = new Orders();
//        newOrder.setDate(LocalDate.now());
//        newOrder.setDevices(Collections.singletonList(device));
//        newOrder.setUser(user);
//        newOrder.setQTY(qty);
//        orderRepo.save(newOrder);
//        Cart cart = cartRepo.findCartByUser(user);
//        cart.deleteProduct(deviceRepo.findById(id).orElseThrow(NoSuchElementException::new));
//        cartRepo.save(cart);
//        model.addAttribute("submitMessage", "Order was submitted!");
//        return "products/order";
//    }

    @PostMapping("/makeOrder")
    public String makeOrderPage(Model model, OrderDTO order) {
//        FinalOrderDTO finalOrder = new FinalOrderDTO();
        Float finalPrice = 0f;
        List<Device> devices = order.getDev();
        for (int i = 0; i < order.getDev().size(); i++) {
            finalPrice += order.getQty().get(i) * order.getDev().get(i).getPrice();
//            finalOrder.addDevice(devices.get(i));
//            finalOrder.addDeviceQTY(order.getQty().get(i));
        }
        order.setTotalPrice(finalPrice);
        finalList = new ArrayList<>();
//        List<FinalOrderDTO> finalList = new ArrayList<>();
        for (int i = 0; i < order.getDev().size(); i++) {
            FinalOrderDTO tmp = new FinalOrderDTO();
            tmp.setDevice(order.getDev().get(i));
            tmp.setQty(order.getQty().get(i));
            tmp.setTotalPrice(finalPrice);
            finalList.add(tmp);
            // finalList.add(order.getQty().get(i));
        }
//        finalList.add(finalPrice);

        model.addAttribute("finalOrderList", finalList);

        return "products/order";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(Model model, @AuthenticationPrincipal User user) {
        Orders newOrder = new Orders();
        List<OrderDevice> orderDeviceList = new ArrayList<>();
        newOrder.setOrderDevices(orderDeviceList);
        newOrder.setDate(LocalDate.now());
        newOrder.setUser(user);
//            newOrder.setQTY(order.getQty().stream().findFirst().orElse(null));
        orderRepo.save(newOrder);
        for (int i = 0; i < finalList.size(); i++) {
            Device device = finalList.get(i).getDevice();
            device.setQTY(device.getQTY() - finalList.get(i).getQty());
            OrderDevice od = new OrderDevice();
            od.setDevice(device);
            od.setQty(finalList.get(i).getQty());
            od.setOrder(newOrder);
            orderDeviceRepo.save(od);
            orderDeviceList.add(od);
        }
//            newOrder.setOrderDevices(orderDeviceList);
//            newOrder.setDate(LocalDate.now());
//            newOrder.setUser(user);
////            newOrder.setQTY(order.getQty().stream().findFirst().orElse(null));
//        orderRepo.save(newOrder);
        Cart cart = cartRepo.findCartByUser(user);
        for (FinalOrderDTO finalOrder : finalList
        ) {
            cart.deleteProduct(finalOrder.getDevice());
        }
        cartRepo.save(cart);
        model.addAttribute("submitMessage", "Order was submitted!");
        return "products/order";
    }

}
