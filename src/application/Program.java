package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;
import java.util.List;

public class Program {
  public static void main(String[] args) {

    SellerDao sellerDao = DaoFactory.createSellerDao();

    System.out.println("=== TEST 1: seller findById ===");
    Seller seller = sellerDao.findById(3);
    System.out.println(seller);

    System.out.println("\n=== TEST 2: seller findByDepartment ===");
    Department department = new Department(7, "Food");
    List<Seller> list = sellerDao.findByDepartment(department);
    list.forEach(System.out::println);

    System.out.println("\n=== TEST 3: seller findAll ===");
    list = sellerDao.findAll();
    list.forEach(System.out::println);

    System.out.println("\n=== TEST 4: seller insert ===");
    Seller newSeller = new Seller(null, "Jackson", "jackson@gmail.com", LocalDate.now(), 4000.00, department);
    sellerDao.insert(newSeller);
    System.out.println("Inserted! New id = " + newSeller.getId());

    System.out.println("\n=== TEST 5: seller update ===");
    seller = sellerDao.findById(1);
    seller.setName("Martha Wayne");
    seller.setEmail("martha@gmail.com");
    sellerDao.update(seller);
    System.out.println("Update completed!");
  }
}
