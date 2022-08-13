package com.adminportal.controller;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.entity.Brand;
import com.adminportal.entity.Category;
import com.adminportal.entity.Item;
import com.adminportal.entity.Order;
import com.adminportal.entity.Subcategory;
import com.adminportal.service.OrderService;

@Controller
@RequestMapping("order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// -------------------Order List ------------------------
	@RequestMapping("/orderList")
	public String orderList(Model model) {

		List<Order> orderList = orderService.findAll();

		model.addAttribute("orderList", orderList);
		return "orderList";

	}

	// ------------------order Details -------------------------
	@GetMapping("orderDetails")
	public String orderDetailsGet(@RequestParam("id") Long id, Model model) {

		Order order = orderService.findOne(id);

		if (order.getOrderStatus().equals("Shipped")) {
			model.addAttribute("shipped", true);
		} else {
			model.addAttribute("shipped", false);
		}
		model.addAttribute("order", order);

		return "orderDetails";

	}

	// ---------------------Order Update ----------------------------
	@RequestMapping(value = "/orderUpdate", method = RequestMethod.POST)
	public String orderUpdate(@ModelAttribute("order") Order order, HttpServletRequest request) {

		Order curOrder = orderService.findOne(order.getId());

		curOrder.setShippingDate(LocalDate.now());
		curOrder.setOrderStatus("Shipped");
		orderService.save(curOrder);

		return "redirect:orderList";
	}

}
