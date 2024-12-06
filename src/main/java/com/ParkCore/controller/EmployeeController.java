package com.ParkCore.controller;

import com.ParkCore.model.Employee;
import com.ParkCore.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Create employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))),
            @ApiResponse(responseCode = "400", description = "CPF already registered.")
    })
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employeeRequest) {
        var employee = employeeService.registerEmployee(employeeRequest);
        return new ResponseEntity<>(employee, CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeRequest) {
        var employee = employeeService.updateEmployee(id, employeeRequest);
        return ResponseEntity.ok(employee);
    }

	@Operation(summary = "Delete an employee", description = "This functionality deletes an employee from the system by ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Employee deleted successfully!"),
			@ApiResponse(responseCode = "404", description = "Employee not found"),
			@ApiResponse(responseCode = "400", description = "Could not delete the employee"),
			@ApiResponse(responseCode = "500", description = "Problem with the API!")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeEmployee(@PathVariable Long id) {
        employeeService.removeEmployee(id);
		return ResponseEntity.noContent().build();
	}

    @Operation(summary = "Get employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))),
    })
    @GetMapping
    public ResponseEntity<List<Employee>> listEmployees() {
        var employees = employeeService.listEmployees();
        return ResponseEntity.ok(employees);
    }
}
