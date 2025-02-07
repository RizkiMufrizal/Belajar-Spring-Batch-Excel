package org.rizki.mufrizal.belajar.spring.batch.excel;

import org.rizki.mufrizal.belajar.spring.batch.excel.object.Employee;
import org.springframework.batch.extensions.excel.RowMapper;
import org.springframework.batch.extensions.excel.support.rowset.RowSet;


public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(RowSet rowSet) {
        var employee = new Employee();
        employee.setFirstName(rowSet.getProperties().getProperty("first name"));
        employee.setLastName(rowSet.getProperties().getProperty("last name"));
        employee.setDepartment(rowSet.getProperties().getProperty("departement"));
        employee.setSalary(Double.valueOf(rowSet.getProperties().getProperty("salary")));
        return employee;
    }
}
