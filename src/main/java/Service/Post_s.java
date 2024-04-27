package Service;

import Entity.Comment;
import Entity.Post;

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
            pstm.setDate(3, new java.sql.Date(p.getDate_pub().getTime()));
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
                p.setDate_pub(new Date(rs.getDate("date_pub").getTime())); // Convertir la date SQL en java.util.Date
                p.setFile(rs.getString("file"));
                p.setLikes(rs.getInt("likes"));
                p.setDislikes(rs.getInt("dislikes"));

                // Récupérer les commentaires pour le post courant
                List<Comment> comments = getCommentsForPost(p.getId());
                p.setComments(comments);

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
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query))
        {
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
            pstm.setDate(3, new java.sql.Date(p.getDate_pub().getTime()));
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
    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM post";
            PreparedStatement ps = this.cnx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String titre = rs.getString("titre");
                String contenu_pub = rs.getString("contenu_pub");
                String date_pub = rs.getString("date_pub");
                String file = rs.getString("file");
                int likes = rs.getInt("likes");
                int dislikes = rs.getInt("dislikes");
                Post p = new Post(titre, contenu_pub, new java.util.Date(), file, likes, dislikes);
                // Correction de la création de l'objet Post
                posts.add(p);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return posts;
    }
    public void updateLikes(String titre, int newLikes) throws SQLException {
        String query = "UPDATE post SET likes = ? WHERE titre = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, newLikes);
            preparedStatement.setString(2, titre);
            preparedStatement.executeUpdate();
        }
    }
    public void updateDislikes(String titre, int newDislikes) throws SQLException {
        String query = "UPDATE post SET dislikes = ? WHERE titre = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, newDislikes);
            preparedStatement.setString(2, titre);
            preparedStatement.executeUpdate();
        }
    }
    public void updatePost(String nouveauTitre, String nouveauContenu, String nouvelleDate, String nouveauFichier, int nouveauxLikes, int nouveauxDislikes) throws SQLException
    {
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
    private List<Comment> getCommentsForPost(int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String qry = "SELECT * FROM comment WHERE post_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, postId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setId(rs.getInt("id"));
                    c.setContenu_comment(rs.getString("contenu_comment"));
                    c.setPost_id(rs.getInt("post_id"));
                    comments.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return comments;
    }
    public void addComment(Post post, Comment comment) throws SQLException {
        String query = "INSERT INTO comment (contenu_comment, post_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, comment.getContenu_comment());
            preparedStatement.setInt(2, post.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insertion du commentaire a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du commentaire : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
    }
    public Post getById(int id) throws SQLException {
        String qry = "SELECT * FROM post WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Post p = new Post();
                    p.setId(rs.getInt("id"));
                    p.setTitre(rs.getString("titre"));
                    p.setContenu_pub(rs.getString("contenu_pub"));
                    p.setDate_pub(new Date(rs.getDate("date_pub").getTime())); // Convertir la date SQL en java.util.Date
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
    public List<String> getAllTitres() throws SQLException {
        List<String> titres = new ArrayList<>();
        String query = "SELECT titre FROM post";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                titres.add(resultSet.getString("titre"));
            }
        }
        return titres;
    }
    public Post getByTitre(String titre) throws SQLException {
        String query = "SELECT * FROM post WHERE titre = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, titre);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Post post = new Post();
                    post.setId(resultSet.getInt("id"));
                    post.setTitre(resultSet.getString("titre"));
                    post.setContenu_pub(resultSet.getString("contenu_pub"));
                    post.setDate_pub(new Date(resultSet.getDate("date_pub").getTime())); // Convertir la date SQL en java.util.Date
                    post.setFile(resultSet.getString("file"));
                    post.setLikes(resultSet.getInt("likes"));
                    post.setDislikes(resultSet.getInt("dislikes"));
                    return post;
                }
            }
        }
        return null; // Retourne null si aucun post n'est trouvé avec ce titre
    }
    public List<Comment> getCommentsByPostId(int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String qry = "SELECT * FROM comment WHERE post_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, postId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setId(rs.getInt("id"));
                    c.setContenu_comment(rs.getString("contenu_comment"));
                    c.setPost_id(rs.getInt("post_id"));
                    comments.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return comments;
    }
    public void deleteComment(int commentId) throws SQLException {
        String query = "DELETE FROM comment WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();
        }
    }
    public List<Comment> getCommentsByContenu(String contenu) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String qry = "SELECT * FROM comment WHERE contenu_comment = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, contenu);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Comment c = new Comment();
                    c.setId(rs.getInt("id"));
                    c.setContenu_comment(rs.getString("contenu_comment"));
                    c.setPost_id(rs.getInt("post_id"));
                    comments.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return comments;
    }
}