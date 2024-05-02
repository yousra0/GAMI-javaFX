package Service;

import Entity.Game;
import Outil.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;




public class Game_s implements Services<Game> {
    Connection cnx=DataBase.getInstance().getConn();
    public Game_s(Connection cnx) {
        this.cnx = cnx;
    }


    public Game_s() {}

    @Override
    public void add(Game g) throws SQLException {
        String qry="INSERT INTO `game`(  `categorie_id`, `name`,`description`, `date`, `image`,`lien`) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1,g.getCategorie_id());
            pstm.setString(2,g.getName());
            pstm.setString(3,g.getDescription());
            pstm.setDate(4, java.sql.Date.valueOf(g.getDate()));
            pstm.setString(5,g.getImage());
            pstm.setString(6,g.getLien());
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
                g.setLien(rs.getString("lien"));
                g.setCategorie_id(rs.getInt("categorie_id"));
                gamesList.add(g);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return gamesList;
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

    @Override
    public void edit(Game c) throws SQLException {
        String sql = "UPDATE `game` SET `name`=?, `description`=?, `date`=?, `image`=?, `lien`=?, `categorie_id`=? WHERE `id`=?";
        try (PreparedStatement pstm = cnx.prepareStatement(sql))
        {
            pstm.setString(1, c.getName());
            pstm.setString(2, c.getDescription());
            pstm.setDate(3, java.sql.Date.valueOf(c.getDate()));
            pstm.setString(4, c.getImage());
            pstm.setString(5, c.getLien());
            pstm.setInt(6, c.getCategorie_id());
            pstm.setInt(7, c.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while editing game: " + e.getMessage());
            throw e;
        }
    }
    private Game getGameById(int id) {
        Game game = null;
        try {
            String sql = "SELECT * FROM game WHERE id = ?";
            PreparedStatement ps = this.cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                Date date = rs.getDate("date");
                String image = rs.getString("image");
                String lien = rs.getString("lien");
                // Ajoutez ici la récupération d'autres champs si nécessaire

                // Instanciez un nouvel objet Game avec les valeurs récupérées
                game = new Game(id, name, description, date.toLocalDate(), image, lien);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return game;
    }




    public List<Game> getAll() {

        List<Game> all = new ArrayList<>();
        Statement stmt;
        try {
            String sql = "select * from game";

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

    public void delete(Game t) {
        try {
            String sql = "DELETE FROM game WHERE id = ?";

            PreparedStatement stmt = cnx.prepareStatement(sql);

            stmt.setInt(1, t.getId());

            stmt.executeUpdate();
            System.out.println("Jeux supprimer avec success");


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
