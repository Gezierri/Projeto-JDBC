package projeto.dao;

import projeto.repository.DepartmentDao;
import projeto.repository.SellerDao;
import projeto.repository.impl.DepartmentDaoJDBC;
import projeto.repository.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
