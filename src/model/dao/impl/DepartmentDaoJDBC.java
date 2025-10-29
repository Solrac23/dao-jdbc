package model.dao.impl;

import config.DB;
import config.DbException;
import config.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

  private Connection conn;

  public DepartmentDaoJDBC(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void insert(Department obj) {
    PreparedStatement st = null;

    try {
      conn.setAutoCommit(false);

      if (obj.getName().isEmpty()){
        throw new DbException("The field Name is empty!");
      }

      st = conn.prepareStatement(
          "INSERT INTO department (Name)"
            + "VALUES (?);",
          Statement.RETURN_GENERATED_KEYS
      );

      st.setString(1, obj.getName());

      int row = st.executeUpdate();

      if(row > 0){
        ResultSet rs = st.getGeneratedKeys();
        if(rs.next()){
          int id = rs.getInt(1);
          obj.setId(id);
          conn.commit();
        }
        DB.closeResultSet(rs);
      }

    }catch (SQLException e) {
      try{
        conn.rollback();
        throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
      }catch (SQLException e1){
        throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
      }
    }finally {
      DB.closeStatement(st);
    }
  }

  @Override
  public void update(Department obj) {
    PreparedStatement st = null;

    try {
      conn.setAutoCommit(false);

      if (obj.getName().isEmpty()){
        throw new DbException("The field Name is empty!");
      }

      st = conn.prepareStatement(
          "UPDATE department " +
              "SET Name = ? "
              + "WHERE Id = ?;"
      );

      st.setString(1, obj.getName());
      st.setInt(2, obj.getId());

      int row = st.executeUpdate();

      conn.commit();
      System.out.println("Done! Row affected: " + row);
    }catch (SQLException e) {
      try{
        conn.rollback();
        throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
      }catch (SQLException e1){
        throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
      }
    }finally {
      DB.closeStatement(st);
    }
  }

  @Override
  public void deleteById(Integer id) {
    PreparedStatement st = null;

    try {
      conn.setAutoCommit(false);

      st = conn.prepareStatement(
          "DELETE FROM department "
              + "WHERE Id = ?;"
      );

      st.setInt(1, id);

      int row = st.executeUpdate();

      if(row == 0){
        throw new DbException("Id is not exist!");
      }

      conn.commit();
      System.out.println("Done! Row affected: " + row);
    }catch (SQLException e) {
      try{
        conn.rollback();
        throw new DbIntegrityException("Transaction rolled back! Caused by: " + e.getMessage());
      }catch (SQLException e1){
        throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
      }
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
          "SELECT * FROM department "
              + "WHERE id  = ?;"
      );

      st.setInt(1, id);
      rs = st.executeQuery();

      if(rs.next()){
        Department obj = instantiateDepartment(rs);
        return obj;
      }
      return null;
    }catch (SQLException e) {
      throw new DbException(e.getMessage());
    }finally {
      DB.closeStatement(st);
      DB.closeResultSet(rs);
    }
  }

  @Override
  public List<Department> findAll() {
    PreparedStatement st = null;
    ResultSet rs = null;

    try {
      st = conn.prepareStatement(
          "SELECT * FROM department "
            + "ORDER BY Name;"
      );

      rs = st.executeQuery();

      List<Department> list = new ArrayList<>();

      while (rs.next()){
        Department dep = instantiateDepartment(rs);

        list.add(dep);
      }
      return list;
    }catch (SQLException e) {
      throw new DbException(e.getMessage());
    }finally {
      DB.closeStatement(st);
      DB.closeResultSet(rs);
    }
  }

  private Department instantiateDepartment(ResultSet rs) throws SQLException {
    Department obj = new  Department();
    obj.setId(rs.getInt("Id"));
    obj.setName(rs.getString("Name"));

    return obj;
  }
}
