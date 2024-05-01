package Entity;

import java.util.ArrayList;
import java.util.List;

public class Reclamation
{
    private int id;
    private String titre_rec;
    private String contenu_rec;
    private String date_rec;
    public List<Reponse> getReponses() {
        return reponses;
    }
    private List<Reponse> reponses= new ArrayList<>();
    public void setReponses(List<Reponse> reponses)
    {
        this.reponses = reponses;
    }


    public Reclamation(){};
    public Reclamation(String titre_rec, String contenu_rec, String date_rec)
    {
        this.titre_rec = titre_rec;
        this.contenu_rec = contenu_rec;
        this.date_rec = date_rec;
        this.reponses = new ArrayList<>();

    }
    public Reclamation(int id, String titre_rec, String contenu_rec, String date_rec)
    {

            this.id = id;
            this.titre_rec = titre_rec;
            this.contenu_rec = contenu_rec;
            this.date_rec = date_rec;
            this.reponses = new ArrayList<>();


    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre_rec() {
        return titre_rec;
    }

    public void setTitre_rec(String titre_rec) {
        this.titre_rec = titre_rec;
    }

    public String getContenu_rec() {
        return contenu_rec;
    }

    public void setContenu_rec(String contenu_rec) {
        this.contenu_rec = contenu_rec;
    }

    public String getDate_rec() {
        return date_rec;
    }

    public void setDate_rec(String date_rec) {
        this.date_rec = date_rec;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", titre_rec='" + titre_rec + '\'' +
                ", contenu_rec='" + contenu_rec + '\'' +
                ", date_rec='" + date_rec + '\'' +
                ", reponses=" + reponses +
                '}';
    }

}