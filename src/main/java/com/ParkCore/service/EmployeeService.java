package com.ParkCore.service;

import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.model.Employee;
import com.ParkCore.repository.EmployeeRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee registerEmployee(Employee employee) {
        if (employeeRepository.existsByCpf(employee.getCpf())) {
            throw new BadRequestException("SSN already registered.");
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employee) {
        var existingEmployee = findById(id);

        existingEmployee.setName(employee.getName());
        existingEmployee.setPosition(employee.getPosition());
        existingEmployee.setWorkingHours(employee.getWorkingHours());

        return employeeRepository.save(existingEmployee);
    }

    public void removeEmployee(Long id) {
        var employee = findById(id);

        employeeRepository.delete(employee);
    }

    public List<Employee> listEmployees() {
        return employeeRepository.findAll();
    }

    private Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Not found", Employee.class));
    }
}
