package com.adminportal.controller;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.entity.Brand;
import com.adminportal.entity.Category;
import com.adminportal.entity.Item;
import com.adminportal.entity.Subcategory;
import com.adminportal.service.BrandService;
import com.adminportal.service.CategoryService;
import com.adminportal.service.ItemService;
import com.adminportal.service.SubcategoryService;

@Controller
@RequestMapping("item")
public class ItemController {

	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@Autowired
	private SubcategoryService subcategoryService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BrandService brandService;

	private String catName;
	private String subcatName;
	private String brandName;
	
	private boolean updateText=false;
	
	private int newQty;
	private int qtyReceive;
	private double newBuyPrice;
	private boolean receive;
	
	// ---------------Add Item ------------------------
	@RequestMapping(value = "/addItem", method = RequestMethod.GET)
	public String addItemGet(Model model) {

		Item item = new Item();

		List<Subcategory> subcatList = subcategoryService.findAll();

		List<Category> catList = categoryService.findAll();

		List<Brand> brandList = brandService.findAll();

		model.addAttribute("item", item);
		model.addAttribute("subcatList", subcatList);
		model.addAttribute("catList", catList);
		model.addAttribute("brandList", brandList);
		
		updateText=false;
		model.addAttribute("updateText", updateText);


		return "addItem";

	}

	@RequestMapping(value = "/addItem", method = RequestMethod.POST)
	public String addItemPost(@ModelAttribute("item") Item item, HttpServletRequest request) {
		
		item.setAddDate(LocalDate.now());
		itemService.save(item);

		String fileName = item.getItemId() + ".png";
		try (FileOutputStream file = new FileOutputStream("src/main/resources/static/img/itemimages/" + fileName)) {

			MultipartFile itemImage = item.getItemImage();
			byte[] bytes = itemImage.getBytes();
			file.write(bytes);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File cannot be written " + fileName + e);
		}

		return "redirect:itemList";

	}

	// -------------------Item List ------------------------
	@RequestMapping("/itemList")
	public String itemList(Model model) {

		List<Item> itemList = itemService.findAll();
		model.addAttribute("itemList", itemList);
		model.addAttribute("item", new Item());
		this.receive = false;

		return "itemList";

	}

	// ------------------Item Details -------------------------
	@GetMapping("itemDetails")
	public String itemDetailsGet(@RequestParam("id") Long id, Model model) {

		Item item = itemService.findOne(id);
		List<Subcategory> subcatList = subcategoryService.findAll();
		List<Category> catList = categoryService.findAll();
		List<Brand> brandList = brandService.findAll();
		
		model.addAttribute("item", item);
		model.addAttribute("subcatList", subcatList);
		model.addAttribute("catList", catList);
		model.addAttribute("brandList", brandList);
		
		return "itemDetails";

	}
	
	//---------------------Item Update ----------------------------
	@RequestMapping(value = "/itemUpdate", method = RequestMethod.POST)
	public String itemUpdate(@ModelAttribute("item") Item item, HttpServletRequest request) {

		Item curItem = itemService.findOne(item.getItemId());
		item.setAddDate(curItem.getAddDate());
		itemService.save(item);

		String fileName = item.getItemId() + ".png";

		MultipartFile itemImage = item.getItemImage();
		// check if new image uploaded
		if (!itemImage.isEmpty()) {
			try (FileOutputStream file = new FileOutputStream("src/main/resources/static/img/itemimages/" + fileName)) {

				byte[] bytes = itemImage.getBytes();
				file.write(bytes);

			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("File cannot be written " + fileName + e);

			}

		}
		return "redirect:itemList";
	}
	
	// ====================== BRAND =======================
	// ------------------------Add Brand --------------------
	@GetMapping("/addBrand")
	public String addBrandGet(Model model) {

		List<Brand> brandList = brandService.findAll();

		Brand brand = new Brand();
		model.addAttribute("brandList", brandList);
		model.addAttribute("brand", brand);
		model.addAttribute("brandName", brandName);
		model.addAttribute("updateText", updateText);

		return "addBrand";
	}

	@RequestMapping(value = "/addBrand", method = RequestMethod.POST)
	public String addBrandPost(@ModelAttribute("brand") Brand brand) {
		brandService.save(brand);
		return "redirect:addBrand";
	}
	
	// ------------------Delete Brand ----------------------
	@GetMapping("/deleteBrand")
	public String deleteBrandGet(@ModelAttribute("brand") Brand brand, HttpServletRequest request) {
		brandService.delete(brand);
		return "redirect:addBrand";
	}
	
	// -----------------Update Brand ----------------------
	@GetMapping(value = "/updateFillBrand")
	public String updateFillBrandPost(@PathParam("id") Long id) {
		Brand brandNew = brandService.findById(id);
		String brandName = brandNew.getBrandName();
		this.brandName= brandName;
		this.updateText= true;
		
		
		return "redirect:addBrand";
	}
	
	@PostMapping(value = "/updateBrand")
	public String updateBrandPost(@ModelAttribute("brand") Brand brand) {
	
		Brand brandNew = brandService.findByBrandName(brandName);
		brandNew.setBrandName(brand.getBrandName());
		brandService.save(brandNew);
		this.brandName = null;
		this.updateText= false;
		return "redirect:addBrand";
	}


	// ====================== CATOGORY ====================
	
	@GetMapping("/addCategory")
	public String addCategoryGet(Model model) {

		List<Category> categoryList = categoryService.findAll();

		Category category = new Category();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("category", category);
		model.addAttribute("catName", catName);
		model.addAttribute("updateText", updateText);

		return "addCategory";
	}
	
	// ------------------------Add Category -----------------------
	@PostMapping(value = "/addCategory")
	public String addCategoryPost(@ModelAttribute("category") Category category) {
		categoryService.save(category);
		return "redirect:addCategory";
	}
	
	//------------------------Update Category--------------------------
	@GetMapping(value = "/updateFillCategory")
	public String updateFillCategoryGet(@PathParam("id") Long id) {
		Category categoryNew = categoryService.findById(id);
		String catName = categoryNew.getCatName();
		this.catName= catName;
		this.updateText= true;
		return "redirect:addCategory";
	}
	
	@PostMapping(value = "/updateCategory")
	public String updateCategoryPost(@ModelAttribute("category") Category category) {
	
		Category categoryNew = categoryService.findByCatName(catName);
		categoryNew.setCatName(category.getCatName());
		categoryService.save(categoryNew);
		this.catName = null;
		this.updateText= false;
		return "redirect:addCategory";
	}
	
	//----------------------Delete Category ------------------------
	@GetMapping("/deleteCategory")
	public String deleteCategoryGet(@ModelAttribute("Category") Category category) {
				
		categoryService.delete(category);
		return "redirect:addCategory";
	}
	
	// ================= Sub Category =====================
	@GetMapping("/addSubcategory")
	public String addSubcategoryGet(Model model) {

		List<Category> categoryList = categoryService.findAll();
		List<Subcategory> subcategoryList = subcategoryService.findAll();

		Category category = new Category();
		Subcategory subcategory = new Subcategory();

		model.addAttribute("categoryList", categoryList);
		model.addAttribute("category", category);

		model.addAttribute("subcategoryList", subcategoryList);
		model.addAttribute("subcategory", subcategory);
		
		model.addAttribute("subcatName", subcatName);
		model.addAttribute("updateText", updateText);
		return "addSubcategory";
	}

	@PostMapping(value = "/addSubcategory")
	public String addSubcategoryPost(@ModelAttribute("category") Category category,
			@ModelAttribute("subcategory") Subcategory subcategory, HttpServletRequest request) {

		subcategoryService.Save(subcategory);
		return "redirect:addSubcategory";
	}
	
	//------------------------Update Subcategory--------------------------
		@GetMapping(value = "/updateFillSubcategory")
		public String updateFillSubcategoryGet(@PathParam("id") Long id) {
			Subcategory subcategoryNew = subcategoryService.findById(id);
			String subcatName = subcategoryNew.getSubcatName();
			this.subcatName= subcatName;
			this.updateText= true;
			return "redirect:addSubcategory";
		}
		
		@PostMapping(value = "/updateSubcategory")
		public String updateSubcategoryPost(@ModelAttribute("subcategory") Subcategory subcategory) {
		
			Subcategory subcategoryNew = subcategoryService.findBySubcatName(subcatName);
			subcategoryNew.setSubcatName(subcategory.getSubcatName());
			subcategoryService.save(subcategoryNew);
			this.subcatName = null;
			this.updateText= false;
			return "redirect:addSubcategory";
		}
		
		//----------------------Delete Subcategory ------------------------
		@GetMapping("/deleteSubcategory")
		public String deleteSubcategoryGet(@ModelAttribute("Subcategory") Subcategory subcategory) {
					
			subcategoryService.delete(subcategory);
			return "redirect:addSubcategory";
		}
	
	
	//================== Receive Item ======================
	
	@RequestMapping("autoComplete")
	@ResponseBody
	public List<String> autoComplete(@RequestParam(value="term", required=false,defaultValue="") String term){
		List<String> autoComplete= new ArrayList<>();
		List<Item> itemList= itemService.findAll();
		for (Item item : itemList) {
			
			autoComplete.add(item.getItemCode());
		}
		
		return autoComplete;
		
		
	}

	@GetMapping("receiveItem")
	public String receiveItemGet(@RequestParam("id") Long id, Model model) {
	
		Item item = itemService.findOne(id);
		model.addAttribute("item", item);
		
		model.addAttribute("qtyReceive", qtyReceive);
		model.addAttribute("newQty", newQty);

		model.addAttribute("newBuyPrice",newBuyPrice);
		model.addAttribute("receive", receive);
		
		return "receiveItem";
	}
	
	@PostMapping("receiveItem")
	public String receiveItemPost(@ModelAttribute("item") Item item, HttpServletRequest request,Model model) {
		
		Item itemCurrent = itemService.findByItemCode(item.getItemCode()); 		
		int qtyReceive = item.getQty();
		int qtyCurrent= itemCurrent.getQty();
		int newQty =qtyCurrent+qtyReceive;
		itemCurrent.setQty(newQty);
		
		Double buyPriceReceive = item.getBuyPrice();
		Double buyPriceCurrent = itemCurrent.getBuyPrice();
		Double newBuyPrice =(qtyCurrent*buyPriceCurrent+ qtyReceive*buyPriceReceive)/(newQty);
		itemCurrent.setBuyPrice(newBuyPrice);
		itemService.save(itemCurrent);

		this.receive = true;
		this.newQty = newQty;
		this.qtyReceive = qtyReceive;
		this.newBuyPrice = newBuyPrice;

		return "redirect:receiveItem?id="+itemCurrent.getItemId();
	}

}
