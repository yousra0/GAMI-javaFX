package Service;

import Entity.Post;
import Outil.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Post_s implements Services <Post>
{

    Connection cnx;
    public Post_s(Connection cnx)
    {
        this.cnx = cnx;
    }

    public Post_s() {}

    @Override
    public void add(Post p) throws SQLException
    {
        String qry="INSERT INTO `post`(`titre`, `contenu_pub`, `date_pub`, `file`, `likes`, `dislikes`) VALUES (?,?,?,?,?,?)";
        try
        {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1,p.getTitre());
            pstm.setString(2,p.getContenu_pub());
            pstm.setString(3,p.getDate_pub());
            pstm.setString(4,p.getFile());
            pstm.setInt(5,p.getLikes());
            pstm.setInt(6,p.getDislikes());
            pstm.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Post> show() throws SQLException {
        List<Post> postList = new ArrayList<>();
        String qry = "SELECT * FROM post";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Post p = new Post();
                p.setId(rs.getInt("id"));
                p.setTitre(rs.getString("titre"));
                p.setContenu_pub(rs.getString("contenu_pub"));
                p.setDate_pub(rs.getString("date_pub"));
                p.setFile(rs.getString("file"));
                p.setLikes(rs.getInt("likes"));
                p.setDislikes(rs.getInt("dislikes"));
                postList.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return postList;
    }

    @Override
    public void delete(int id) throws SQLException
    {
        String query = "DELETE FROM post WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public void edit(Post p) throws SQLException
    {
        String sql = "UPDATE `post` SET titre = ?, contenu_pub = ?, date_pub = ?, file = ?, likes = ?, dislikes = ? WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(sql))
        {
            pstm.setString(1, p.getTitre());
            pstm.setString(2, p.getContenu_pub());
            pstm.setString(3, p.getDate_pub());
            pstm.setString(4, p.getFile());
            pstm.setInt(5, p.getLikes());
            pstm.setInt(6, p.getDislikes());
            pstm.setInt(7, p.getId());  // Set the ID parameter
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while editing post: " + e.getMessage());
            throw e;
        }
    }

    public void deleteByTitre(String titre) throws SQLException {
        String query = "DELETE FROM post WHERE titre = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, titre);
            preparedStatement.executeUpdate();
        }
    }

    public Post getPostById(int id) throws SQLException {
        String qry = "SELECT * FROM post WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Post p = new Post();
                    p.setId(rs.getInt("id"));
                    p.setTitre(rs.getString("titre"));
                    p.setContenu_pub(rs.getString("contenu_pub"));
                    p.setDate_pub(rs.getString("date_pub"));
                    p.setFile(rs.getString("file"));
                    p.setLikes(rs.getInt("likes"));
                    p.setDislikes(rs.getInt("dislikes"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while getting post by id: " + e.getMessage());
            throw e;
        }
        return null; // Return null if no post with the given id was found
    }
    public Post getPostByTitre(String titre) throws SQLException {
        String qry = "SELECT * FROM post WHERE titre = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, titre);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Post p = new Post();
                    p.setId(rs.getInt("id"));
                    p.setTitre(rs.getString("titre"));
                    p.setContenu_pub(rs.getString("contenu_pub"));
                    p.setDate_pub(rs.getString("date_pub"));
                    p.setFile(rs.getString("file"));
                    p.setLikes(rs.getInt("likes"));
                    p.setDislikes(rs.getInt("dislikes"));
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du post par titre : " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void updatePost(String nouveauTitre, String nouveauContenu, String nouvelleDate, String nouveauFichier, int nouveauxLikes, int nouveauxDislikes) throws SQLException {
        String sql = "UPDATE post SET contenu_pub = ?, date_pub = ?, file = ?, likes = ?, dislikes = ? WHERE titre = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(sql)) {
            pstm.setString(1, nouveauContenu);
            pstm.setString(2, nouvelleDate);
            pstm.setString(3, nouveauFichier);
            pstm.setInt(4, nouveauxLikes);
            pstm.setInt(5, nouveauxDislikes);
            pstm.setString(6, nouveauTitre);
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du post : " + e.getMessage());
            throw e;
        }
    }




    /*public void editByTitre(Post postS) throws SQLException {
        String qry = "UPDATE post SET contenu_pub = ?, date_pub = ?, file = ?, likes = ?, dislikes = ? WHERE titre = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, postS.getContenu_pub());
            pstm.setString(2, postS.getDate_pub());
            pstm.setString(3, postS.getFile());
            pstm.setInt(4, postS.getLikes());
            pstm.setInt(5, postS.getDislikes());
            pstm.setString(6, postS.getTitre());
            int rowsAffected = pstm.executeUpdate();
            System.out.println(rowsAffected + " lignes modifiées.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void update(int postId, Post newPost) throws SQLException {
        String query = "UPDATE post SET date = ?, titre = ?, Contenu = ?, fichier = ?, nb_likes = ?, nb_dislikes = ? WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, newPost.getDate_pub());
            statement.setString(2, newPost.getTitre());
            statement.setString(3, newPost.getContenu_pub());
            statement.setString(4, newPost.getFile());
            statement.setInt(5, newPost.getLikes());
            statement.setInt(6, newPost.getDislikes());
            statement.setInt(7, postId);

            statement.executeUpdate();
        }
    }*/


}
