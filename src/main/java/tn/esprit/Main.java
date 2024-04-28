package tn.esprit;

import Outil.DataBase;
import Entity.Game;
import Service.Game_s;
import java.time.LocalDate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            // Obtenez une instance de la base de données
            DataBase db = DataBase.getInstance();

            // Obtenez une connexion à la base de données
            Connection cnx = db.getConn();

            // Créez une instance de Game_s en lui passant la connexion
            Game_s gameService = new Game_s(cnx);

            // Créez une instance de Game avec les données à ajouter
            Game gameToAdd = new Game();
            gameToAdd.setCategorie_id(1); // Remplacez 1 par l'ID de catégorie approprié
            gameToAdd.setName("Nom du jeu");
            gameToAdd.setDescription("Description du jeu");

            // Convertir la chaîne de caractères représentant la date en LocalDate
            LocalDate date = LocalDate.parse("2024-03-28"); // Remplacez par la date appropriée
            gameToAdd.setDate(date);

            gameToAdd.setImage("chemin/vers/image.jpg");
            gameToAdd.setLien("lien_vers_le_jeu");

            // Appeler la méthode add pour ajouter le jeu
            gameService.add(gameToAdd);
            System.out.println("game added");
            // Affichez la liste des jeux
            System.out.println("Liste des jeux :");
            List<Game> gamesList = gameService.show();
            for (Game game : gamesList) {
                System.out.println("ID : " + game.getId());
                System.out.println("Nom : " + game.getName());
                System.out.println("Date : " + game.getDate());
                System.out.println("Description : " + game.getDescription());
                System.out.println("Image : " + game.getImage());
                System.out.println("Lien : " + game.getLien());
                System.out.println("--------------------------------");
            }
            // Fermez la connexion à la base de données
            cnx.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
