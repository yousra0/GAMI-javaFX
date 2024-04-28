package Service;

import Entity.Game;
import Outil.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;




public class Game_s implements Services<Game> {
    Connection cnx;
    public Game_s(Connection cnx) {
        this.cnx = cnx;
    }


    public Game_s() {

    }

    @Override
    public void add(Game g) throws SQLException {
        String qry="INSERT INTO `game`( `name`, `description`, `date`, `image`,`lien`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,g.getName());
            pstm.setString(2,g.getDescription());
            pstm.setDate(3, java.sql.Date.valueOf(g.getDate()));
            pstm.setString(4,g.getImage());
            pstm.setString(5,g.getLien());
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
                g.setDate(rs.getDate("date").toLocalDate());
                g.setImage(rs.getString("image"));
                gamesList.add(g);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return gamesList;
    }
    public List<Game> getAll() {

        List<Game> all = new ArrayList<>();
        Statement stmt;
        try {
            String sql = "select * from jeux";

            stmt = cnx.createStatement();

            ResultSet rs =  stmt.executeQuery(sql);

            while(rs.next()){
                Game b = new Game();
                b.setId(rs.getInt("id"));
                b.setName(rs.getString("name"));
                b.setDescription(rs.getString("description"));
                b.setDate(rs.getDate("date").toLocalDate());
                b.setImage(rs.getString("image"));

                all.add(b);
            }
            return all;


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return all;
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
            System.out.println("Jeux supprimer avec success");
        }
    }

    public void deleteByName(String name) throws SQLException {
        String query = "DELETE FROM game WHERE name = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            System.out.println("Jeux supprimer avec success");
        }
    }
    /////////////////////////////aa
    public List<Game> rechercherJeuxParNom(String name) throws SQLException {
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
                g.setDate(LocalDate.parse(rs.getString("date")));
                g.setImage(rs.getString("image"));
                g.setLien(rs.getString("lien"));
                result.add(g);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw ex; // Vous pouvez propager l'exception pour être traitée plus haut
        }

        return result;
    }
    ///////////////////////////////////////////////////////////////
    public void initConnection() {
        cnx = DataBase.getInstance().getConn();
    }



}
