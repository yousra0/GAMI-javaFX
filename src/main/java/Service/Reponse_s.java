package Service;

import Entity.Reponse;
import Entity.Reclamation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reponse_s implements Services <Reponse>
{
    Connection cnx;
    private PreparedStatement pst;

    public Reponse_s()
    {}
    public Reponse_s(Connection cnx)
    {
        this.cnx = cnx;
    }
    public void add(Reponse r) throws SQLException
    {
        String qry= "INSERT INTO `reponse`(`contenu_rep`, `date_rep`, `reclamation_id`) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, r.getContenu_rep());
            //pstm.setObject(2, LocalDateTime.now());
            pstm.setInt(3, r.getreclamation_id());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Reponse> show() throws SQLException
    {
        List<Reponse> reponseList = new ArrayList<>();
        String qry = "SELECT * FROM reponse";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setContenu_rep(rs.getString("contenu_rep"));
                //r.setDate_rep(rs.getDate("date_rep").toLocalDate());
                r.setreclamation_id(rs.getInt("reclamation_id"));
                reponseList.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return reponseList;
    }
    @Override
    public void delete(int id) throws SQLException
    {
        String query = "DELETE FROM reponse WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
    @Override
    public void edit(Reponse r) throws SQLException
    {
        String sql = "UPDATE `reponse` SET `contenu_rep`=?,/*date_rep`=?,*/`reclamation_id`=? WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(sql))
        {
            pstm.setString(1, r.getContenu_rep());
            //pstm.setDate(2, java.sql.Date.valueOf(c.getDate_rep()));
            pstm.setInt(3, r.getreclamation_id());
            pstm.setInt(4, r.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while editing reclamation: " + e.getMessage());
            throw e;
        }
    }
    public List<Reponse> getAllReponsesByReclamationId(int reclamation_id) {
        List<Reponse> reponses = new ArrayList<>();

        try {
            String sql = "SELECT * FROM reponse WHERE reclamation_id = ?";
            PreparedStatement ps = this.cnx.prepareStatement(sql);
            ps.setInt(1, reclamation_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String contenu_rep = rs.getString("contenu_rep");
                String date_rep = rs.getString("date_rep");
                Reclamation reclamation = getReclamationById(reclamation_id);
                Reponse r = new Reponse(id, contenu_rep, date_rep, reclamation);
                reponses.add(r);
            }

            rs.close();
            ps.close();
        } catch (SQLException var15) {
            var15.printStackTrace();
        }

        return reponses;
    }

    private Reclamation getReclamationById(int id) {
        Reclamation reclamation = null;

        try {
            String sql = "SELECT * FROM reclamation WHERE id = ?";
            PreparedStatement ps = this.cnx.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String titre_rec = rs.getString("titre_rec");
                String contenu_rec = rs.getString("contenu_rec");
                String date_rec = rs.getString("date_rec");
                reclamation = new Reclamation(id, titre_rec, contenu_rec, date_rec);
            }

            rs.close();
            ps.close();
        } catch (SQLException var16) {
            var16.printStackTrace();
        }

        return reclamation;
    }

}