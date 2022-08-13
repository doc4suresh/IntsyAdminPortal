package com.adminportal.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.adminportal.entity.CartItem;
import com.adminportal.entity.Item;
import com.adminportal.entity.Order;
import com.adminportal.entity.Users;
import com.adminportal.service.CartItemService;
import com.adminportal.service.ItemService;
import com.adminportal.service.OrderService;
import com.adminportal.service.UserService;

@Controller
public class DashboardController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	// ------------------------------Login request -------------------------
	/**
	 * Login Get request *** Login Post request handle by Spring security
	 * 
	 * * @return login form
	 */

	@GetMapping("/login")
	public String springLogin() {
		return "login";
	}

	@GetMapping("/")
	public String login(Model model, Principal principal) {

		if (principal != null) {
			String username = principal.getName();
			Users user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}

		List<Order> orderList = orderService.findAll();
		List<Users> userPendingCusList = userService.findByStatus("Registered");
		List<Users> userCompletedCusList = userService.findByStatus("Approved");

		BigDecimal orderShippedListTotal = BigDecimal.ZERO;
		BigDecimal orderPendingListTotal = BigDecimal.ZERO;

		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();
		List<Order> orderShippedList = new ArrayList<>();
		List<Order> orderPendingList = new ArrayList<>();

		for (Order order : orderList) {
			if (order.getOrderStatus().equalsIgnoreCase("Shipped") && order.getOrderDate().getYear() == year
					&& order.getOrderDate().getMonthValue() == month) {
				orderShippedList.add(order);
				orderShippedListTotal = orderShippedListTotal.add(order.getOrderTotal());
			}
		}

		for (Order order : orderList) {

			if (order.getOrderStatus().equalsIgnoreCase("Ordered")) {
				orderPendingList.add(order);
				orderPendingListTotal = orderPendingListTotal.add(order.getOrderTotal());
			}
		}

		// Box One -- Sales For This Month
		model.addAttribute("orderShippedListTotal", orderShippedListTotal);

		// Box two -- Number of Orders Shipped This Month
		int orderShipped = orderShippedList == null ? 0 : orderShippedList.size();
		model.addAttribute("orderShipped", orderShipped);

		// Box three -- Sales For This Month
		model.addAttribute("orderPendingListTotal", orderPendingListTotal);

		// Box four -- Number of Pending Orders
		int orderPending = orderPendingList == null ? 0 : orderPendingList.size();
		model.addAttribute("orderPending", orderPending);

		// Box five -- Number of Customer to Approve
		int cusPending = userPendingCusList == null ? 0 : userPendingCusList.size();
		model.addAttribute("cusPending", cusPending);

		// Box six -- Number of Customers
		int cusCompleted = userCompletedCusList == null ? 0 : userCompletedCusList.size();
		model.addAttribute("cusCompleted", cusCompleted);

		// Item List for Sales Report
		List<Item> itemList = itemService.findAll();
		model.addAttribute("itemList", itemList);

		return "dashboard";
	}

	// Items
	// button 1

	@GetMapping("/reportLowQtyItems")
	public String reportLowQtyItems(Model model) {
		List<Item> itemList = itemService.findAll();
		ArrayList<Item> itemList20 = new ArrayList<>();
		for (Item item : itemList) {
			if (item.getQty() < 21) {
				itemList20.add(item);
			}

		}
		model.addAttribute("itemList20", itemList20);

		return "reports/reportLowQtyItems";
	}

	// button 2
	@GetMapping("/reportSpecialItems")
	public String reportSpecialItems(Model model) {
		List<Item> itemList = itemService.findAll();

		ArrayList<Item> itemListSpecial = new ArrayList<>();
		for (Item item : itemList) {
			if (item.isSpecial() == true) {
				itemListSpecial.add(item);
			}

		}
		model.addAttribute("itemListSpecial", itemListSpecial);
		return "reports/reportSpecialItems";
	}

	@GetMapping("/reportAllItems")
	public String reportAllItems(Model model) {
		List<Item> itemListAll = itemService.findAll();

		model.addAttribute("itemListAll", itemListAll);
		return "reports/reportAllItems";
	}

	@GetMapping("/reportActiveItems")
	public String reportActiveItems(Model model) {
		List<Item> itemList = itemService.findAll();

		ArrayList<Item> itemListActive = new ArrayList<>();
		for (Item item : itemList) {
			if (item.isActive() == true) {
				itemListActive.add(item);
			}

		}
		model.addAttribute("itemListActive", itemListActive);
		return "reports/reportActiveItems";
	}

	@GetMapping("/reportInactiveItems")
	public String reportInactiveItems(Model model) {
		List<Item> itemList = itemService.findAll();

		ArrayList<Item> itemListInactive = new ArrayList<>();
		for (Item item : itemList) {
			if (item.isActive() == false) {
				itemListInactive.add(item);
			}

		}
		model.addAttribute("itemListInactive", itemListInactive);
		return "reports/reportInactiveItems";
	}

	// button 3
	@GetMapping("reportNewItems")
	public String reportNewItems(Model model) {
		ArrayList<Item> itemListMonth = new ArrayList<>();

		LocalDate today = LocalDate.now();
		List<Item> itemList = itemService.findAll();

		for (Item item : itemList) {
			if (item.getAddDate().isAfter(today.minusMonths(1))) {
				itemListMonth.add(item);
			}

		}

		model.addAttribute("itemListMonth", itemListMonth);
		return "reports/reportNewItems";
	}

	// button 4
	@PostMapping("/reportDateRangeNewItems")
	public String reportDateRangeNewItems(@ModelAttribute("fromDate") String fromDate,
			@ModelAttribute("toDate") String toDate, Model model) {
		ArrayList<Item> itemListDate = new ArrayList<>();

		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);
		List<Item> itemList = itemService.findAll();

		for (Item item : itemList) {
			if (item.getAddDate().isAfter(from) && item.getAddDate().isBefore(to)) {
				itemListDate.add(item);
			}
		}

		model.addAttribute("itemListDate", itemListDate);
		return "reports/reportDateRangeNewItems";
	}

	// Customers
	// button 1
	@GetMapping("/reportCustomerPending")
	public String reportPendingCustomers(Model model) {
		List<Users> userPendingCusList = userService.findByStatus("Registered");

		model.addAttribute("userPendingCusList", userPendingCusList);
		return "reports/reportCustomerPending";
	}

	// button 2
	@GetMapping("/reportCustomerApproved")
	public String reportCustomerAll(Model model) {
		List<Users> userApprovedCusList = userService.findByStatus("Approved");

		model.addAttribute("userApprovedCusList", userApprovedCusList);
		return "reports/reportCustomerApproved";
	}

	// button 2
	@GetMapping("/reportCustomerIntegrators")
	public String reportCustomerIntegrators(Model model) {
		List<Users> customerIntegratorsList = userService.findByRegType("Integrator");

		model.addAttribute("customerIntegratorsList", customerIntegratorsList);
		return "reports/reportCustomerIntegrators";
	}

	// button 2
	@GetMapping("/reportCustomerDistributors")
	public String reportCustomerDistributors(Model model) {
		List<Users> customerDistributorsList = userService.findByRegType("Distributor");

		model.addAttribute("customerDistributorsList", customerDistributorsList);
		return "reports/reportCustomerDistributors";
	}

	// Orders
	// Orders - button 1
	@GetMapping("/orderPending")
	public String orderPending(Model model) {
		List<Order> orderList = orderService.findAll();
		List<Order> orderPendingList = new ArrayList<>();
		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();

		for (Order order : orderList) {

			if (order.getOrderStatus().equalsIgnoreCase("Ordered")) {
				orderPendingList.add(order);
			}
		}

		model.addAttribute("orderPendingList", orderPendingList);
		return "reports/reportOrderPending";
	}

	// Orders - button 2
	@GetMapping("/orderShippedMonth")
	public String db2Orders(Model model) {
		List<Order> orderList = orderService.findAll();
		List<Order> orderShippedMonthList = new ArrayList<>();

		int year = LocalDate.now().getYear();
		int month = LocalDate.now().getMonthValue();
		String monthname = LocalDate.now().getMonth().toString();
		for (Order order : orderList) {

			if (order.getOrderStatus().equalsIgnoreCase("Shipped") && order.getOrderDate().getYear() == year
					&& order.getOrderDate().getMonthValue() == month) {
				orderShippedMonthList.add(order);
			}
		}
		model.addAttribute("orderShippedMonthList", orderShippedMonthList);
		model.addAttribute("monthname", monthname);
		model.addAttribute("year", year);
		return "reports/reportOrderShippedMonth";

	}

	@PostMapping("/reportOrdersShippedDateRange")
	public String reportOrdersShippedDateRange(@ModelAttribute("fromDate") String fromDate,
			@ModelAttribute("toDate") String toDate, Model model) {

		List<Order> orderList = orderService.findAll();
		List<Order> orderShippedDateRangeList = new ArrayList<>();

		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);

		for (Order order : orderList) {

			if (order.getOrderStatus().equalsIgnoreCase("Shipped") && order.getOrderDate().isAfter(from)
					&& order.getOrderDate().isBefore(to)) {
				orderShippedDateRangeList.add(order);
			}
		}
		model.addAttribute("orderShippedDateRangeList", orderShippedDateRangeList);
		
		model.addAttribute("from", from);
		model.addAttribute("to", to);

		return "reports/reportOrdersShippedDateRange";

	}

	// Tab 5 - Sales
	// Box 1 - Sales by Item by DateRange
	@PostMapping("/reportSalesItemDateRange")
	public String reportSalesItemDateRange(@ModelAttribute("Item") Item item,
			@ModelAttribute("fromDate") String fromDate, @ModelAttribute("toDate") String toDate, Model model) {

		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);
		BigDecimal total = BigDecimal.ZERO;
		List<CartItem> cartItemDateList = new ArrayList<>();
		Item itemCurrent = itemService.findOne(item.getItemId());

		List<CartItem> cartItemList = cartItemService.findByItem(itemCurrent);

		if (cartItemList != null) {

			List<Order> orderList = new ArrayList<>();

			for (CartItem cartItem : cartItemList) {
				if (cartItem.getOrder() != null) {
					Order order = orderService.findOne(cartItem.getOrder().getId());

					if (order != null && order.getOrderStatus().equalsIgnoreCase("Shipped")
							&& order.getOrderDate().isAfter(from) && order.getOrderDate().isBefore(to)) {
						orderList.add(order);
					}
				}
			}

			for (Order order : orderList) {
				cartItemDateList.addAll(cartItemService.findByOrder(order));
			}

			for (CartItem cartItem : cartItemDateList) {
				total = total.add(cartItem.getSubtotal());

			}
		}
		model.addAttribute("itemCode", itemCurrent.getItemCode());
		model.addAttribute("total", total);
		model.addAttribute("cartItemDateList", cartItemDateList);
		model.addAttribute("from", from);
		model.addAttribute("to", to);

		return "reports/reportSalesItemDateRange";
	}

}
