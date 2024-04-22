package tn.esprit;

import Entity.Reclamation;
import Outil.DataBase;
import Service.Reclamation_s;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (Connection cnx = DataBase.getInstance().getConn()) {
            Reclamation_s reclamationService = new Reclamation_s(cnx);

            // Création d'une réclamation avec des valeurs fictives
            Reclamation reclamation = new Reclamation();
            reclamation.setTitre_rec("Titre de la réclamation");
            reclamation.setContenu_rec("Contenu de la réclamation");
            reclamation.setDate_rec("2024-03-29");

            // Ajout de la réclamation à la base de données
            reclamationService.add(reclamation);
            System.out.println("Réclamation ajoutée avec succès !");

            // Affichage des réclamations existantes dans la base de données
            List<Reclamation> reclamations = reclamationService.show();
            if (!reclamations.isEmpty()) {
                System.out.println("Liste des réclamations :");
                for (Reclamation r : reclamations) {
                    System.out.println("ID : " + r.getId());
                    System.out.println("Titre : " + r.getTitre_rec());
                    System.out.println("Contenu : " + r.getContenu_rec());
                    System.out.println("Date de réclamation : " + r.getDate_rec());
                    System.out.println("--------------------------------");
                }

                // Suppression de la première réclamation de la liste
                Reclamation firstReclamation = reclamations.get(0);
                reclamationService.delete(firstReclamation.getId());
                System.out.println("Première réclamation supprimée avec succès !");

                // Modification de la deuxième réclamation de la liste si elle existe
                if (reclamations.size() > 1) {
                    Reclamation secondReclamation = reclamations.get(1);
                    secondReclamation.setTitre_rec("Nouveau titre du deuxième réclamation");
                    reclamationService.edit(secondReclamation);
                    System.out.println("Deuxième réclamation modifiée avec succès !");
                } else {
                    System.out.println("Il n'y a pas assez de réclamations pour effectuer une modification.");
                }
            } else {
                System.out.println("Aucune réclamation à afficher.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Afficher l'erreur SQL s'il y en a une
        }
    }
}
