package tn.esprit;

import Outil.DataBase;
import Entity.Post;
import Service.Post_s;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataBase d = DataBase.getInstance();
        Post_s postService = null; // Déclaration à l'extérieur du bloc try

        try {
            Connection cnx = d.getConn(); // Obtenir la connexion à la base de données
            postService = new Post_s(cnx);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Créer un post avec des valeurs fictives
            Post post = new Post();
            post.setTitre("titre");
            post.setContenu_pub("Contenu");
            Date   datePub = dateFormat.parse("2024-03-29");
            post.setDate_pub(datePub);
            post.setFile("Chemin/vers/fichier");
            post.setLikes(1);
            post.setDislikes(0);

            // Ajouter le post à la base de données
            postService.add(post);

            System.out.println("Post ajouté avec succès !");

            // Afficher les posts existants dans la base de données
            List<Post> posts = postService.show();
            System.out.println("Liste des posts :");
            for (Post p : posts) {
                System.out.println("ID : " + p.getId());
                System.out.println("Titre : " + p.getTitre());
                System.out.println("Contenu : " + p.getContenu_pub());
                System.out.println("Date de publication : " + p.getDate_pub());
                System.out.println("Fichier : " + p.getFile());
                System.out.println("Likes : " + p.getLikes());
                System.out.println("Dislikes : " + p.getDislikes());
                System.out.println("--------------------------------");
            }

            // Supprimer le premier post de la liste
            int firstPostId = posts.get(0).getId();
            postService.delete(firstPostId);
            System.out.println("Premier post supprimé avec succès !");

            // Modifier le deuxième post de la liste
            Post secondPost = posts.get(1);
            secondPost.setTitre("Nouveau titre du deuxième post");
            postService.edit(secondPost);
            System.out.println("Deuxième post modifié avec succès !");

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }
}
