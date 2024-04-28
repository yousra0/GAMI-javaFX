package Service;

import Entity.CategorieJeux;
import Entity.Game;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategorieJeux_s implements Services<CategorieJeux> {

  Connection cnx;
    public CategorieJeux_s(Connection cnx)
    {
        this.cnx = cnx;
    }

    public CategorieJeux_s() {}

    @Override
    public void add(CategorieJeux c) throws SQLException
    {
        String qry="INSERT INTO `categorie`( `name`, `description`) VALUES (?,?)";
        try
        {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,c.getNomCat());
            pstm.setString(2,c.getDescription());
            pstm.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<CategorieJeux> show() throws SQLException {
        List<CategorieJeux> categorieList = new ArrayList<>();
        String qry = "SELECT * FROM categorie";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                CategorieJeux c = new CategorieJeux();
                c.setId(rs.getInt("id"));
                c.setNomCat(rs.getString("name"));
                c.setDescription(rs.getString("description"));

                // Récupérer les games pour la categorie courante
                List<Game> games = getGamesForCategorie(c.getId());
                c.setGames(games);

                categorieList.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return categorieList;
    }
    @Override
    public void delete(int id) throws SQLException
    {
        String query = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Categorie supprimer avec success");
        }
    }
    public void edit(CategorieJeux c) throws SQLException
    {
        String sql = "UPDATE `categorie` SET `id`=?,`name`=?,`description`=? WHERE id = ? ";
        try (PreparedStatement pstm = cnx.prepareStatement(sql))
        {
            pstm.setInt(1, c.getId());  // Set the ID parameter
            pstm.setString(2, c.getNomCat());
            pstm.setString(3, c.getDescription());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while editing categorie: " + e.getMessage());
            throw e;
        }
    }
    public void deleteByName(String name) throws SQLException {
        String query = "DELETE FROM categorie WHERE name = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }
    public List<CategorieJeux> getAllCategories() throws SQLException {
        List<CategorieJeux> categoriesList = new ArrayList<>();
        String query = "SELECT * FROM categorie";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                CategorieJeux categorie = new CategorieJeux();
                categorie.setNomCat(rs.getString("name"));
                categorie.setDescription(rs.getString("description"));
                categoriesList.add(categorie);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return categoriesList;
    }

    private List<Game> getGamesForCategorie(int categorie_id) throws SQLException {
        List<Game> games = new ArrayList<>();
        String qry = "SELECT * FROM game WHERE categorie_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, categorie_id);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Game g = new Game();
                    g.setId(rs.getInt("id"));
                    g.setDescription(rs.getString("description_game"));
                    g.setId(rs.getInt("categorie_id"));
                    games.add(g);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return games;
    }
    public void addGame(CategorieJeux categorieJeux, Game game) throws SQLException {
        String query = "INSERT INTO game (description, categorie_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, game.getDescription());
            preparedStatement.setInt(2, categorieJeux.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insertion du game a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du game : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
    }
    public CategorieJeux getByName(String name) throws SQLException {
        String query = "SELECT * FROM categorie WHERE name = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CategorieJeux categorieJeux = new CategorieJeux();
                    categorieJeux.setId(resultSet.getInt("id"));
                    categorieJeux.setNomCat(resultSet.getString("name"));
                    categorieJeux.setDescription(resultSet.getString("description"));
                    return categorieJeux;
                }
            }
        }
        return null; // Retourne null si aucun categorie n'est trouvé avec ce titre
    }
}
