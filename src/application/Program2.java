package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;
import java.util.Scanner;

public class Program2 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    System.out.println("=== TEST 1: department findAll ===");
    List<Department> list = departmentDao.findAll();
    list.forEach(System.out::println);

    System.out.println("\n=== TEST 2: department findById ===");
    Department department = departmentDao.findById(2);
    System.out.println(department);

    System.out.println("\n=== TEST 3: department insert ===");
    Department newDepartment = new Department(null, "Fashion");
    departmentDao.insert(newDepartment);
    System.out.println("Inserted! New id = " + newDepartment.getId());

    System.out.println("\n=== TEST 4: department update ===");
    department = departmentDao.findById(6);
    department.setName("Sport");
    departmentDao.update(department);
    System.out.println("Updated Completed!");

    System.out.println("\n=== TEST 4: department delete ===");
    System.out.print("Enter Id for delete test: ");
    int id = sc.nextInt();

    departmentDao.deleteById(id);
    System.out.println("Delete completed");
    sc.close();
  }
}
