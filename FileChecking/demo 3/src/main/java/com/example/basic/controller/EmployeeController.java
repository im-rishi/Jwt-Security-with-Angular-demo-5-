package com.example.basic.controller;

import com.example.basic.entity.Employee;
import com.example.basic.entity.Role;
import com.example.basic.entity.User;
import com.example.basic.exception.ResourceNotFoundException;
import com.example.basic.repository.employeeRepository;
import com.example.basic.service.RoleService;
import com.example.basic.service.UserService;
import com.example.basic.service.employeeservice;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//@CrossOrigin("*")
@RestController
public class EmployeeController {

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private employeeRepository employeeRepository;
    private final employeeservice employeeservice;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;



    public String currentUser()
    {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        return loggedInUser.getName();
    }

    public EmployeeController(employeeservice employeeservice){
        super();
        this.employeeservice = employeeservice;
    }
    @GetMapping("/employees/list")
    public List<Employee> listEmployees(){
        return employeeservice.getAllemployees();
    }

    @PostMapping("/employees/create")
    public Employee saveStudent(@RequestBody Employee employee) {
        return employeeservice.saveStudent(employee);
    }

    /*@PostMapping("/register")
    public User register(@RequestBody User user){
        return employeeservice.register(user);
    }*/


    @PutMapping("/employees/edit/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employeeDetails, @PathVariable long id) {
        Employee updateEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        updateEmployee.setFirstName(employeeDetails.getFirstName());
        updateEmployee.setLastName(employeeDetails.getLastName());
        updateEmployee.setEmail(employeeDetails.getEmail());
        updateEmployee.setSalary(employeeDetails.getSalary());

        Employee updatedEmployee = employeeRepository.save(updateEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/employees/remove/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new <String>ResourceNotFoundException("Not Found"));


        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployeeById(@PathVariable long id) {

        return employeeservice.getEmployeebyId(id);
    }

    @PostMapping("/createRole")
    public Role CreateNewRole(@RequestBody Role role){
        return roleService.CreateNewRole(role);
    }

    @PostMapping("/registerUser")
    public User RegisterNewUser( @RequestBody User user){
        return userService.RegisterNewUser(user);
    }

    @PostConstruct
    public void initRolesAndUsers(){
        userService.initRoleAndUser();
    }



    /*@GetMapping("/")
    public String listEmployees(Model model,String keyword){
        model.addAttribute("employees",employeeservice.getAllemployees());
        model.addAttribute("User",currentUser());
        logger.trace("Home Page Accessed");
        return "employees";

    }

    @GetMapping("/employee/new")
    public String CreateEmployee(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee",employee);
        logger.trace("Create page Accessed");
        return "Create_employee";
    }
    @PostMapping("/employees")
    public String saveStudent(@ModelAttribute("employee") Employee employee) {
        employeeservice.saveStudent(employee);
        return "redirect:/";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployeeForm(@PathVariable Long id, Model model){
        model.addAttribute("employee", employeeservice.getEmployeebyId(id));
        logger.trace("update page accessed");
        return "edit_employee";
    }

    @PostMapping("/employees/{id}")
    public String updateEmployee(@PathVariable Long id,
                                @ModelAttribute("employee") Employee employee) {

        Employee existingEmployee = employeeservice.getEmployeebyId(id);
        existingEmployee.setId(id);
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());

        employeeservice.updateEmployee(existingEmployee);
        return "redirect:/";
    }

    @GetMapping("/employees/remove/{id}")
    public String DeleteEmployee(@PathVariable Long id){
        employeeservice.DeleteEmployee(id);
        logger.trace("Successfully deleted");
        return "redirect:/";

    }
    @GetMapping("/search")
    public String home(Employee employee, Model model, String keyword) {
        List<Employee> employees;
        employees = employeeservice.getByKeyword(keyword);
        model.addAttribute("employees", employees);
        model.addAttribute("user",currentUser());
        return "employees";
    }
/*
    @GetMapping("/admin")
    public String admin()
    {
        return "redirect:/";
    }

    @GetMapping("/adminModified")
    public String listNewEmployees(Model model){
        model.addAttribute("employees",employeeservice.getAllemployees());
        logger.trace("Home Page Accessed");
        return "employees";

    }
    @GetMapping("/user")
    public String user(){
        return "redirect:/";
    }
    @GetMapping("/userModified")
    public String listNewUserEmployees(Model model) {
        model.addAttribute("employees", employeeservice.getAllemployees());
        logger.trace("Home Page Accessed");
        return "User_employees";
    }
    @GetMapping("/403")
    public String error(){
        return "403";
    }
*/
}
