package projeto.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projeto.dao.DB;
import projeto.dao.Dbexception;
import projeto.entities.Department;
import projeto.entities.Seller;
import projeto.repository.SellerDao;

public class SellerDaoJDBC implements SellerDao {

	Connection conn = null;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	//==================================== INSERT ==============================//
	@Override
	public void insert(Seller seller) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS// Retornar o ID do vendedor inserido
			);

			st.setString(1, seller.getNome());
			st.setString(2, seller.getEmail());
			st.setDate(3, new Date(seller.getBirthDate().getTime()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());

			int rownsAffected = st.executeUpdate();

			if (rownsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);// Pega o atributo da primeira fileira
					seller.setId(id);
				}
				DB.closeResultSet(rs);

			} else {
				throw new Dbexception("Unexpected erro! No rows affected");
			}
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	
	//==================================== UPDATE ============================//
	@Override
	public void update(Seller seller) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?, Id = ?"
					);
			st.setString(1, seller.getNome());
			st.setString(2, seller.getEmail());
			st.setDate(3, new Date(seller.getBirthDate().getTime()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());
			st.setInt(6, seller.getId());
			
			
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	
	// ========================================= DELETE BY ID ==========================//
	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
						"DELETE FROM seller WHERE Id = ?"
					);
			
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	//================================ FIND BY ID ======================//
	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.id " 
					+ "WHERE seller.Id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {// verifica se ha algum resultado
				Department department = instanciarDepartment(rs);
				Seller seller = instanciarSeller(rs, department);
				return seller;
			}
			return null;
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Seller instanciarSeller(ResultSet rs, Department department) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setNome(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setDepartment(department);
		return seller;
	}

	private Department instanciarDepartment(ResultSet rs) throws SQLException {
		Department department = new Department();
		department.setId(rs.getInt("DepartmentId"));
		department.setName(rs.getString("DepName"));
		return department;
	}

	//================= FIND ALL =============================//
	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id " 
					+ "ORDER BY Id");

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {// verifica se ha algum resultado

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instanciarDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller seller = instanciarSeller(rs, dep);
				list.add(seller);
			}
			return list;
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	
	//==================================== FIND BY DEPARTMENT ============================//
	@Override
	public List<Seller> findByDepartment(Department departmanet) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			st.setInt(1, departmanet.getId());
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {// verifica se ha algum resultado

				Department dep = map.get(rs.getInt("DepartmentId"));

				if (dep == null) {
					dep = instanciarDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}

				Seller seller = instanciarSeller(rs, dep);
				list.add(seller);
			}
			return list;
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
