package org.management.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.management.domain.Employee;
import org.management.service.impl.InMemoryDao;

public class EmployeeDaoTest {

    private GenericDao<Employee, Long> employeeDaoFixture = new InMemoryDao<>();
    private int oldEmployeeSize;

    @Before
    public void setUp() {
        for (int i = 0; i < 5; i++) {
            Employee e = new Employee("Anuj" + i, "Parikh", "emp" + i, "team1", "Developer", "emp10");
            employeeDaoFixture.add(e);
        }
        oldEmployeeSize = employeeDaoFixture.list().size();
    }

    @Test
    public void test_add_success() {
        Employee e = new Employee("Palak", "Parikh", "emp" + (oldEmployeeSize + 1), "team2", "Developer", "emp10");
        employeeDaoFixture.add(e);
        int newEmployeeSize = employeeDaoFixture.list().size();
        Assert.assertEquals(oldEmployeeSize + 1, newEmployeeSize);
    }

    @Test
    public void test_remove_success() {
        Employee e = new Employee("Anuj1", "Parikh", "emp1", "team1", "Developer", "emp10");
        employeeDaoFixture.remove(e);
        int newEmployeeSize = employeeDaoFixture.list().size();
        Assert.assertEquals(oldEmployeeSize - 1, newEmployeeSize);
    }

    @Test
    @Ignore("Still not implemented")
    public void test_update_success() {

    }

    @Test
    public void test_list_success() {
        Assert.assertEquals(5, employeeDaoFixture.list().size());
    }
}
