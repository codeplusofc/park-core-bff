package com.ParkCore.service;

import com.ParkCore.model.Employee;
import com.ParkCore.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void shouldRegisterEmployee() {
        var employee = mock(Employee.class);
        given(employee.getCpf()).willReturn("13684726052");
        given(employeeRepository.save(employee)).willReturn(employee);
        var result = employeeService.registerEmployee(employee);
        assertEquals("13684726052", result.getCpf());
    }

    @Test
    public void shouldUpdateEmployee() {

        var existingEmployee = mock(Employee.class);
        given(existingEmployee.getId()).willReturn(1L);
        given(existingEmployee.getName()).willReturn("Henrique f");
        given(existingEmployee.getPosition()).willReturn("Assistant");
        given(existingEmployee.getWorkingHours()).willReturn(55);
        given(employeeRepository.save(existingEmployee)).willReturn(existingEmployee);

        given(employeeRepository.findById(1L)).willReturn(Optional.of(existingEmployee));

        var modifiedEmployee = mock(Employee.class);
        given(modifiedEmployee.getName()).willReturn("Henrique f");
        given(modifiedEmployee.getPosition()).willReturn("Assistant");
        given(modifiedEmployee.getWorkingHours()).willReturn(55);

        given(employeeRepository.save(existingEmployee)).willReturn(existingEmployee);

        var result = employeeService.updateEmployee(1L, modifiedEmployee);
        assertEquals(1L, result.getId());
        assertEquals("Henrique f", result.getName());
        assertEquals("Assistant", result.getPosition());
        assertEquals(55, result.getWorkingHours());
    }

    @Test
    public void shouldRemoveEmployee() {
        var employee = mock(Employee.class);
        given(employee.getId()).willReturn(1L);
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        employeeService.removeEmployee(1L);
    }

    @Test
    public void shouldListEmployees() {
        var employee1 = mock(Employee.class);
        var employee2 = mock(Employee.class);
        var employee3 = mock(Employee.class);
        given(employeeRepository.findAll()).willReturn(List.of(employee1, employee2, employee3));
        employeeService.listEmployees();
    }

    @Test
    public void shouldThrowExceptionWhenCpfIsAlreadyRegistered() {
        var employee = mock(Employee.class);
        given(employee.getCpf()).willReturn("13684726052");
        given(employeeRepository.existsByCpf("13684726052")).willReturn(true);
        thenThrownBy(() -> employeeService.registerEmployee(employee)).isInstanceOf(RuntimeException.class);
    }
}
