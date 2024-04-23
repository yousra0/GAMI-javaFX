package Service;

import Entity.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Game_s implements Services<Game> {
    Connection cnx;
    public Game_s(Connection cnx) {
        this.cnx = cnx;
    }
    @Override
    public void add(Game g) throws SQLException {
        String qry="INSERT INTO `game`( `name`, `description`, `date`, `image`) VALUES (?,?,?,?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,g.getName());
            pstm.setString(2,g.getDescription());
            pstm.setString(3,g.getDate());
            pstm.setString(4,g.getImage());
            pstm.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Game> show() throws SQLException {
        List<Game> gamesList = new ArrayList<>();
        String qry = "SELECT * FROM game";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Game g = new Game();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                g.setDescription(rs.getString("description"));
                g.setDate(rs.getString("date"));
                g.setImage(rs.getString("image"));
                gamesList.add(g);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return gamesList;
    }


    @Override
    public void edit(Game game) throws SQLException {

    }
    @Override
    public void delete(int id) throws SQLException
    {
        String query = "DELETE FROM game WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteByName(String name) throws SQLException {
        String query = "DELETE FROM game WHERE name = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }
    /////////////////////////////aa
    public List<Game> rechercherParNom(String name) throws SQLException {
        List<Game> result = new ArrayList<>();
        String sql = "SELECT * FROM game WHERE LOWER(name) LIKE LOWER(?)";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Game g = new Game();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                g.setDescription(rs.getString("description"));
                g.setDate(rs.getString("date"));
                g.setImage(rs.getString("image"));
                result.add(g);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex; // Vous pouvez propager l'exception pour être traitée plus haut
        }

        return result;
    }


}
