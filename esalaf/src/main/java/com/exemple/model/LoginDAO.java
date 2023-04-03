package com.exemple.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LoginDAO extends BaseDAO <Login> {

    public LoginDAO() throws SQLException {
    }
    @Override
    public void save(Login var1) throws SQLException {

    }
    @Override
    public void update(Login var1) throws SQLException {

    }
    @Override
    public void delete(Login var1) throws SQLException {

    }
    @Override
    public Login getOne(Long var1) throws SQLException {
        return null;
    }
    @Override
    public List<Login> getAll() throws SQLException {
        return null;
    }

    public Login checklogin(Login object ) throws SQLException {
        String verifyLogin = "SELECT * FROM login WHERE email = ? AND password = ?";

        this.preparedStatement = this.connection.prepareStatement(verifyLogin);

        this.preparedStatement.setString(1 , object.getEmail());
        this.preparedStatement.setString(2 , object.getPassword());


        ResultSet result = this.preparedStatement.executeQuery();

        Login resultLogin = null;
        if(result.next()){
            resultLogin = new Login(result.getString("email") , result.getString("password"));
        }

        return resultLogin;
    }
}
