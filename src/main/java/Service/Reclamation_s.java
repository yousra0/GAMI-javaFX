package Service;

import Entity.Reclamation;
import Entity.Reponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Reclamation_s implements Services<Reclamation> {

    private Connection cnx;  // Déclaration de la connexion

    public Reclamation_s(Connection cnx) {
        this.cnx = cnx;
    }

    public void setConnection(Connection cnx) {
        this.cnx = cnx;  // Initialisation de la connexion avec la valeur passée en paramètre
    }

    @Override
    public void add(Reclamation r) throws SQLException {
        String qry = "INSERT INTO reclamation (titre_rec, contenu_rec, date_rec) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, r.getTitre_rec());
            pstm.setString(2, r.getContenu_rec());
            pstm.setString(3, r.getDate_rec());
            pstm.executeUpdate();
            System.out.println("Réclamation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
    }

    @Override
    public List<Reclamation> show() throws SQLException {
        List<Reclamation> reclamationList = new ArrayList<>();
        String qry = "SELECT * FROM reclamation";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Reclamation t = new Reclamation();
                t.setId(rs.getInt("id"));
                t.setTitre_rec(rs.getString("titre_rec"));
                t.setContenu_rec(rs.getString("contenu_rec"));
                t.setDate_rec(rs.getString("date_rec"));
                // Récupérer les commentaires pour le post courant
                List<Reponse> reponses = getReponsesForReclamation(t.getId());
                t.setReponses(reponses);
                reclamationList.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return reclamationList;
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reclamation WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réclamation supprimée avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec l'ID : " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
            throw e;
        }
    }

    public void edit(Reclamation reclamation) throws SQLException {
        String qry = "UPDATE reclamation SET contenu_rec = ?, date_rec = ? WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, reclamation.getContenu_rec());
            pstm.setString(2, reclamation.getDate_rec());
            pstm.setInt(3, reclamation.getId());
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réclamation modifiée avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec l'ID : " + reclamation.getId());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la réclamation : " + e.getMessage());
            throw e;
        }
    }
    public void deleteByTitre(String titre_rec) throws SQLException {
        String query = "DELETE FROM reclamation WHERE titre_rec = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, titre_rec);
            preparedStatement.executeUpdate();
        }
    }
    private List<Reponse> getReponsesForReclamation(int reclamationId) throws SQLException
    {
        List<Reponse> reponses = new ArrayList<>();
        String qry = "SELECT * FROM reponse WHERE reclamation_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reclamationId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Reponse r = new Reponse();
                    r.setId(rs.getInt("id"));
                    r.setContenu_rep(rs.getString("contenu_rep"));
                    r.setreclamation_id(rs.getInt("reclamation_id"));
                    reponses.add(r);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return reponses;
    }
        public void addReponse(Reclamation reclamation, Reponse reponse) throws SQLException {
        String query = "INSERT INTO reponse (contenu_rep, reclamation_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, reponse.getContenu_rep());
            preparedStatement.setInt(2, reclamation.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Insertion du reclamation a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du reponse : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
    }

    public Reclamation getByTitre(String titre_rec) throws SQLException {
        String query = "SELECT * FROM reclamation WHERE titre_rec = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, titre_rec);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Reclamation reclamation = new Reclamation();
                    reclamation.setId(resultSet.getInt("id"));
                    reclamation.setTitre_rec(resultSet.getString("titre_rec"));
                    reclamation.setContenu_rec(resultSet.getString("contenu_rec"));
                    reclamation.setDate_rec(resultSet.getString("date_rec"));

                    return reclamation;
                }
            }
        }
        return null; // Retourne null si aucun post n'est trouvé avec ce titre
    }
    public List<Reponse> getReponsesByReclamationId(int reclamationId) throws SQLException {
        List<Reponse> reponses = new ArrayList<>();
        String qry = "SELECT * FROM reponse WHERE reclamation_id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, reclamationId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Reponse r = new Reponse();
                    r.setId(rs.getInt("id"));
                    r.setContenu_rep(rs.getString("contenu_rep"));
                    r.setreclamation_id(rs.getInt("reclamation_id"));
                    reponses.add(r);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return reponses;
    }
    public List<String> getAllTitres() throws SQLException {
        List<String> titres = new ArrayList<>();
        String query = "SELECT titre_rec FROM reclamation";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                titres.add(resultSet.getString("titre_rec"));
            }
        }
        return titres;
    }
}
