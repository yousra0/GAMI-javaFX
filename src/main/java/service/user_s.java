package service;
import entity.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class user_s implements service <user>  {

    Connection cnx;
    public user_s(Connection cnx) {
        this.cnx = cnx;
    }
    @Override
    public void add(user u) throws SQLException {

        String encodedPassword = PasswordEncoder.encode(u.getPassword());

        // Vérifiez que les champs obligatoires ne sont pas vides ou nuls
        if (u.getEmail() == null || u.getEmail().isEmpty() ||
                u.getRoles() == null || u.getRoles().isEmpty() ||
                u.getPassword() == null || u.getPassword().isEmpty() ||
                u.getNom() == null || u.getNom().isEmpty() ||
                u.getPrenom() == null || u.getPrenom().isEmpty() ||
                u.getPays() == null || u.getPays().isEmpty() ||
                u.getDatenai() == null || u.getDatenai().isEmpty() ||
                u.getPprofile() == null || u.getPprofile().isEmpty()) {
            throw new SQLException("Tous les champs doivent être remplis.");
        }

        // Vérifiez que l'adresse e-mail est valide
        if (!u.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new SQLException("L'adresse e-mail n'est pas valide.");
        }

        // Vérifiez que le nom et le prénom contiennent uniquement des lettres
        if (!u.getNom().matches("^[A-Za-z]+$")) {
            throw new SQLException("Le nom ne doit contenir que des lettres.");
        }
        if (!u.getPrenom().matches("^[A-Za-z]+$")) {
            throw new SQLException("Le prénom ne doit contenir que des lettres.");
        }

        // Vérifiez que le mot de passe a une longueur minimale
        if (u.getPassword().length() < 8) {
            throw new SQLException("Le mot de passe doit contenir au moins 8 caractères.");
        }

        // Vérifiez que le pays est valide (exemple simple)
        if (!u.getPays().matches("^[A-Za-z ]+$")) {
            throw new SQLException("Le pays doit contenir uniquement des lettres et des espaces.");
        }

        // Vérifiez que la date de naissance est au format AAAA-MM-JJ
        if (!u.getDatenai().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new SQLException("La date de naissance doit être au format AAAA-MM-JJ.");
        }

        // Ajoutez l'utilisateur à la base de données
        String qry = "INSERT INTO `user`( `email`, `roles`, `password`, `nom`, `prenom`, `pays`, `datenai`, `pprofile`, `is_verified`, `is_banned`) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, u.getEmail());
            pstm.setString(2, u.getRoles());
            pstm.setString(3, encodedPassword);
            pstm.setString(4, u.getNom());
            pstm.setString(5, u.getPrenom());
            pstm.setString(6, u.getPays());
            pstm.setString(7, u.getDatenai());
            pstm.setString(8, u.getPprofile());
            pstm.setByte(9, (byte) 0);  // Assuming 'status' is the byte variable
            pstm.setByte(10, (byte) 0); // Assuming 'type' is the byte variable
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
    }


    @Override
    public List<user> show() throws SQLException {
        List<user> userListList = new ArrayList<>();
        String qry = "SELECT * FROM user";
        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                user u = new user();
                u.setId(rs.getInt("id"));
                u.setEmail(rs.getString("email"));
                u.setRoles(rs.getString("roles"));
                u.setPassword(rs.getString("password"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setPays(rs.getString("pays"));
                u.setDatenai(rs.getString("datenai"));
                u.setPprofile(rs.getString("pprofile"));





                userListList.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
        return userListList;
    }

    @Override
    public void delete(int id) throws SQLException {
        String qry = "DELETE FROM user WHERE id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID : " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
    }


    @Override
    public void edit(user u) throws SQLException {
        String qry = "UPDATE user SET email = ?, roles = ?, password = ?, nom = ?, prenom = ?, pays = ?, datenai = ?, pprofile = ? WHERE id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, u.getEmail());
            pstm.setString(2, u.getRoles());
            pstm.setString(3, u.getPassword());
            pstm.setString(4, u.getNom());
            pstm.setString(5, u.getPrenom());
            pstm.setString(6, u.getPays());
            pstm.setString(7, u.getDatenai());
            pstm.setString(8, u.getPprofile());
            pstm.setInt(9, u.getId());

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID : " + u.getId());
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            throw e; // Rethrow the exception to handle it in the caller
        }
    }
    public boolean signIn(String email, String password) throws SQLException {
        String qry = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                String encodedPassword = rs.getString("password");
                // Use BCrypt to check the provided password against the hashed password from the database
                boolean passwordMatch = BCrypt.checkpw(password, encodedPassword);
                return passwordMatch; // Return true if the passwords match, false otherwise
            } else {
                // No user found with the given email, return false
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
            throw e;
        }
    }

    // Method in user_s class to fetch user details
    public user getUserByEmail(String email) throws SQLException {
        String qry = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, email);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                user u = new user();
                u.setId(rs.getInt("id"));
                u.setEmail(rs.getString("email"));
                u.setRoles(rs.getString("roles"));
                u.setPassword(rs.getString("password"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setPays(rs.getString("pays"));
                u.setDatenai(rs.getString("datenai"));
                u.setPprofile(rs.getString("pprofile"));
                return u;
            }
        }
        return null;
    }


}
