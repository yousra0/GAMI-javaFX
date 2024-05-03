package Service;

import Entity.Comment;
import Entity.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Comment_s implements Services <Comment>
{
    Connection cnx;
    private PreparedStatement pst;
    public Comment_s()
    {}
    public Comment_s(Connection cnx)
    {
        this.cnx = cnx;
    }
    public void add(Comment c) throws SQLException
    {
        String qry= "INSERT INTO `comment`(`contenu_comment`, `date_comment`, `post_id`) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, c.getContenu_comment());
            //pstm.setDate(2, java.sql.Date.valueOf(LocalDate.now())); // Convert LocalDate to java.sql.Date
            pstm.setInt(3, c.getPost_id());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Comment> show() throws SQLException
    {
        List<Comment> commentList = new ArrayList<>();
        String qry = "SELECT * FROM comment";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Comment c = new Comment();
                c.setId(rs.getInt("id"));
                c.setContenu_comment(rs.getString("contenu_comment"));
                //c.setDate_comment(rs.getDate("date_comment").toLocalDate());
                c.setPost_id(rs.getInt("post_id"));
                commentList.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return commentList;
    }
    @Override
    public void delete(int id) throws SQLException
    {
        String query = "DELETE FROM comment WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void edit(Comment c) throws SQLException
    {
        String sql = "UPDATE `comment` SET `contenu_comment`=?,/*`date_comment`=?,*/`post_id`=? WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(sql))
        {
            pstm.setString(1, c.getContenu_comment());
            //pstm.setDate(2, java.sql.Date.valueOf(c.getDate_comment()));
            pstm.setInt(3, c.getPost_id());
            pstm.setInt(4, c.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while editing post: " + e.getMessage());
            throw e;
        }
    }
    private Post getPostById(int id)
    {
        Post post = null;
        try
        {
            String sql = "SELECT * FROM post WHERE id = ?";
            PreparedStatement ps = this.cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                String titre = rs.getString("titre");
                String contenu_pub = rs.getString("contenu_pub");
                Date date_pub = rs.getDate("date_pub");
                String file = rs.getString("file");
                int likes = rs.getInt("likes");
                int dislikes = rs.getInt("dislikes");
                post = new Post(id, titre, contenu_pub, date_pub, file, likes, dislikes);
            }
            rs.close();
            ps.close();
        }
        catch (SQLException var16)
        {
            var16.printStackTrace();
        }
        return post;
    }
}