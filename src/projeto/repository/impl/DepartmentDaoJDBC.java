package projeto.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projeto.dao.DB;
import projeto.dao.Dbexception;
import projeto.entities.Department;
import projeto.repository.DepartmentDao;

public class DepartmentDaoJDBC implements DepartmentDao{

	Connection conn = null;
	
	 public DepartmentDaoJDBC(Connection conn) {
		 this.conn = conn;
	}
	
	@Override
	public void insert(Department department) {
		PreparedStatement st = null;
		
		try {
			
				st = conn.prepareStatement(
					"INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS
				);
			
			st.setString(1, department.getName());
			
			int rownsAffect = st.executeUpdate();
			
			if (rownsAffect > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					department.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new Dbexception("Unexpected Erro! No rows affected");
			}
			
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department department) {

		PreparedStatement st = null;
		
		try {
			
				st= conn.prepareStatement(
					"UPDATE department SET Name = ?, Id = ? "
				);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			
				st = conn.prepareStatement(
					"DELETE FROM department WHERE Id = ?"
				);
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
				st = conn.prepareStatement(
					"SELECT * FROM department WHERE Id = ?"	
						
				);
				
				st.setInt(1, id);
				rs = st.executeQuery();
				
				if (rs.next()) {
					Department department = new Department();
					department.setId(rs.getInt("Id"));
					department.setName(rs.getString("Name"));
					return department;
				}
				return null;
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	@Override
	public List<Department> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement(
						"SELECT * FROM department ORDER BY Id"
					);
			rs = st.executeQuery();
			List<Department> list = new ArrayList<>();
			
			while (rs.next()) {
				Department department = new Department();
				department.setId(rs.getInt("Id"));
				department.setName(rs.getString("Name"));
				list.add(department);
			}
			return list;
		} catch (SQLException e) {
			throw new Dbexception(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
