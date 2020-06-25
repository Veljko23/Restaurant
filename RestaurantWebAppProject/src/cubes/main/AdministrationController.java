package cubes.main;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cubes.main.dao.CategoryDAO;
import cubes.main.dao.EmployeeDAO;
import cubes.main.dao.ProductDAO;
import cubes.main.dao.ReservationDAO;
import cubes.main.dao.TagDAO;
import cubes.main.dao.UserDAO;
import cubes.main.entity.Category;
import cubes.main.entity.ChangePassword;
import cubes.main.entity.Employee;
import cubes.main.entity.Product;
import cubes.main.entity.Reservation;
import cubes.main.entity.Tag;
import cubes.main.entity.User;

@Controller
@RequestMapping("/administration")
public class AdministrationController {

	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private TagDAO tagDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	@Autowired
	private ReservationDAO reservationDAO;
	
	@Autowired
	private EmployeeDAO employeeDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping({"/product-list", ""})
	public String getProductList(Model model) {
		
		List<Product> list = productDAO.getProducts();
		
		model.addAttribute("productList", list);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "product-list";
	}
	
	@RequestMapping("/product-form")
	public String getProductForm(Model model) {
		
		Product product = new Product();
		
		List<Category> category = categoryDAO.getCategoryList();
		
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("product", product);
		
		model.addAttribute("categoryList", category);
		
		model.addAttribute("tagList", tagList);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "product-form";
	}
	
	@RequestMapping("/form-product-update")
	public String getProductFormUpdate(@RequestParam int id, Model model) {
		
		Product product = productDAO.updateProduct(id);
		
		List<Category> categoryList = categoryDAO.getCategoryList();
		
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("product", product);
		
		model.addAttribute("categoryList", categoryList);
		
		model.addAttribute("tagList", tagList);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "product-form";
		
	}
	
	@RequestMapping("/form-product-delete")
	public String getProductFormDelete(@RequestParam int id) {
		
		productDAO.deleteProduct(id);
		
		return "redirect: product-list";
	}
	
	
	@RequestMapping("/product-save")
	public String getProductForm(@Valid @ModelAttribute Product product, BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			
			List<Category> category = categoryDAO.getCategoryList();
			List<Tag> tagList = tagDAO.getTagList();
			
			model.addAttribute("categoryList", category);
			model.addAttribute("tagList", tagList);
			
			return "product-form";
		}
		
		
		
		Category category = categoryDAO.getCategory(product.getCategory().getId());
		
		List<Integer> ids = new ArrayList<Integer>();
		
		for(Tag tag: product.getTags()) {
			ids.add(Integer.parseInt(tag.getName()));
		}
		
		List<Tag> tags = tagDAO.getTagsById(ids);
		
		product.setCategory(category);
		product.setTags(tags);
		productDAO.saveProduct(product);
		
		return "redirect: product-list";
	}
	
	
	
	//=========================================================
	@RequestMapping("/category-list")
	public String getCategoryList(Model model) {
		
		System.out.println("Proslo je sve kako treba!");
		
		List<Category> list = categoryDAO.getCategoryList();
		
		System.out.println(list.toString());
		
		model.addAttribute("categoryList",list);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "category-list";
	}
	
	
	@RequestMapping("/category-form")
	public String getCategoryForm(Model model) {
		
		Category category = new Category();
		
		model.addAttribute("category", category);
		
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		
		return "category-form";
	}
	
	@RequestMapping("/category-form-update")
	public String getCategoryUpdateForm(@RequestParam int id,Model model) {
		
		Category category = categoryDAO.getCategory(id);
		
		model.addAttribute("category", category);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "category-form";
	}
	
	
	@RequestMapping("/category-delete")
	public String deleteCategory(@RequestParam int id) {
		
		categoryDAO.deleteCategory(id);
		
		return "redirect:/administration/category-list";
	}
	
	
	@RequestMapping("/category-save")
	public String saveCategory(@Valid @ModelAttribute Category category, BindingResult result) {
		
		if(result.hasErrors()) {
			return "category-form";
		}
		
		categoryDAO.saveCategory(category);
		
		return "redirect:/administration/category-list";
	}
	
	//==============================================================
	
	@RequestMapping("/tag-list")
	public String getTagList(Model model) {
		
		List<Tag> tag = tagDAO.getTagList();
		
		model.addAttribute("tag", tag);
		
		System.out.println(tag.toString());
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "tag-list";
	}
	
	@RequestMapping("/tag-form")
	public String getTagForm(Model model) {
		
		Tag tag = new Tag();
		
		model.addAttribute("tag", tag);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "tag-form";
	}
	
	@RequestMapping("/form-tag-update")
	public String getTagUpdateForm(@RequestParam int id, Model model) {
		
		Tag tag = tagDAO.updateTag(id);
		
		model.addAttribute("tag", tag);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "tag-form";
	}
	
	
	@RequestMapping("/form-tag-delete")
	public String getDeleteTag(@RequestParam int id) {
		
		tagDAO.deleteTag(id);
		
		return "redirect:/administration/tag-list";
	}
	
	
	@RequestMapping("/form-tag-save")
	public String getSaveTag(@Valid @ModelAttribute Tag tag, BindingResult result) {
		
		if(result.hasErrors()) {
			return "tag-form";
		}
		
		tagDAO.saveTag(tag);
		
		return "redirect: tag-list";
	}
	
	//============================================================
	@RequestMapping("/reservation-list")
	public String getReservationListPage(Model model) {
		
		model.addAttribute("reservationList", reservationDAO.getAllReservation());
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "reservation-list";
	}
	
	@RequestMapping("/reservation-seen")
	public String getMarkReservationSeen(@RequestParam int id) {
		
		Reservation r = reservationDAO.getReservation(id);
		r.setIsSeen(true);
		
		reservationDAO.saveReservation(r);
		
		return "redirect:/administration/reservation-list";
	}
	
	
	
	//========================================================
	// EMPLOYEE
	@RequestMapping("/employee-list")
	public String getEmployeeList(Model model) {
		
		List<Employee> list = employeeDAO.getEmployeeList();
		
		model.addAttribute("employeeList", list);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "employee-list";
	}
	
	@RequestMapping("/employee-form")
	public String getEmployeeForm(Model model) {
		
		Employee employee = new Employee();
		
		model.addAttribute("employee", employee);
		
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		
		return "employee-form";
	}
	
	@RequestMapping("/form-employee-update")
	public String getEmployeeUpdateForm(@RequestParam int id,Model model) {
		
		Employee employee = employeeDAO.getEmployee(id);
		
		model.addAttribute("employee", employee);
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());
		return "employee-form";
	}
	
	@RequestMapping("/employee-save")
	public String saveEmployee(@Valid @ModelAttribute Employee employee, BindingResult result) {
		
		if(result.hasErrors()) {
			return "employee-form";
		}
		
		employeeDAO.saveEmployee(employee);
		
		return "redirect:/administration/employee-list";
	}
	
	@RequestMapping("/form-employee-delete")
	public String deleteEmployee(@RequestParam int id) {
		
		employeeDAO.deleteEmployee(id);
		
		return "redirect:/administration/employee-list";
	}
	
	//========================================================================
	// USERS
	
	@RequestMapping("/users-list")
	public String getUserListPage(Model model) {
		
		List<User> list = userDAO.getUserList();
		
		model.addAttribute("userList", list);
				
		model.addAttribute("reservationCount", reservationDAO.getUnreadReservationCount());

		model.addAttribute("user", userDAO.getUserByUsername("veljko"));
		
		return "users-list";
	}
	
	@RequestMapping("/form-user-enable")
	public String enableUser(@RequestParam String username) {
		
		userDAO.enableUser(username);
		
		return "redirect:/administration/users-list";
	}
	
	@RequestMapping("/user-form")
	public String getUserForm(Model model) {
		
		User user = new User();
		
		model.addAttribute("user", user);
		model.addAttribute("roles", userDAO.getRoles());
		
		return "user-form";
	}
	
	@RequestMapping("/form-user-delete")
	public String getDeleteUser(@RequestParam String username) {
		
		userDAO.deleteUser(username);
		
		return "redirect:/administration/users-list";
	}
	
	@RequestMapping("/user-save")
	public String saveUser(@ModelAttribute User user) {
		
		System.out.println("Lozinka je: " + user.getPassword());
		
		String passwordEncode = new BCryptPasswordEncoder().encode(user.getPassword());
		System.out.println("Lozinka je: " + passwordEncode);
		user.setEnabled(true);
		user.setPassword("{bcrypt}" + passwordEncode);

		userDAO.saveUser(user);
		
		return "redirect:/administration/users-list";
	}
	
	@RequestMapping("/user-edit-profile")
	public String getUserEditPage(Principal principal, Model model) {
		
		User user = userDAO.getUserByUsername(principal.getName());
		
		model.addAttribute("user", user);
		
		return "user-edit-profile";
	}
	
	@RequestMapping("/user-edit")
	public String editUser(@ModelAttribute User user) {

		userDAO.saveUser(user);
		
		return "redirect:/administration/users-list";
	}
	
	@RequestMapping("/user-change-password")
	public String getUserChangePassword(Principal principal, Model model) {
		
		
		model.addAttribute("changePassword", new ChangePassword());
		
		return "user-change-password";
	}
	
	@RequestMapping("/user-change-password-action")
	public String getUserChangePasswordAction(@ModelAttribute ChangePassword changePassword ,Principal principal, Model model) {
		
		if(changePassword.getNewPassword().equalsIgnoreCase(changePassword.getConfirmPassword())) {
			User user = userDAO.getUserByUsername(principal.getName());
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			if(encoder.matches(changePassword.getOldPassword(), user.getPassword().replace("{bcrypt}", ""))) {
				user.setPassword("{bcrypt}" + encoder.encode(changePassword.getNewPassword()));
				
				userDAO.saveUser(user);

			}
			else {
				//nije unet dobar stari password
				
				return "user-change-password";
			}
		}
		else {
			//ne poklapaju se passwordi
			return "user-change-password";
		}
	
		return "redirect:/administration/users-list";
	}
}
