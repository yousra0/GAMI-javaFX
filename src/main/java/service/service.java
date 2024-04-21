package service;

import java.sql.SQLException;
import java.util.List;

public interface service <T>{
    public void add(T t) throws SQLException;
    public List<T> show() throws SQLException;
    public void delete(int id) throws SQLException;
    public void edit(T t) throws SQLException;
    public boolean signIn(String email, String password) throws SQLException;



}