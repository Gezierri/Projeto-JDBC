package projeto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import projeto.dao.DaoFactory;
import projeto.entities.Department;
import projeto.entities.Seller;
import projeto.repository.DepartmentDao;
import projeto.repository.SellerDao;

public class Main {
	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

		System.out.println("======= TESTE 1 === SELLER BY ID\n");
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
		
		System.out.println("\n======= TESTE 2 === SELLER BY DEPARTMENT\n");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		for (Seller obj : list) {
			System.out.println(obj);
		}
		
		System.out.println("\n======= TESTE 3 === SELLER FINDALL\n");
		List<Seller> listAll = sellerDao.findAll();
		for (Seller obj : listAll) {
			System.out.println(obj);
		}
		
		System.out.println("\n======= TESTE 4 === SELLER INSERT\n");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted! New id = " + seller.getId());
		
		System.out.println("\n====================== TESTE 5 === SELLER UPDATE\n");
		newSeller = sellerDao.findById(2);
		System.out.println(newSeller);
		newSeller.setNome("Martha Waine");
		sellerDao.update(newSeller);
		System.out.println(newSeller);
		
		//System.out.println("\n==================== TESTE 6 ==== DELETE BY ID\n");
		//sellerDao.deleteById(1);
		
		//System.out.println("\n==================== DEPARTMENT INSERT =================\n");
		//Department newDepartment = new Department(null, "Gym");
		//departmentDao.insert(newDepartment);
		//System.out.println(newDepartment);
		
		System.out.println("\n===================== DEPARTMENT FIND BY ID ===============");
		Department dep = departmentDao.findById(2);
		System.out.println(dep);
		
		System.out.println("\n======================= DEPARTMENT FIND ALL ================");
		List<Department> listDepartment = departmentDao.findAll();
		for (Department obj : listDepartment) {
			System.out.println(obj);
		}
	}
}
