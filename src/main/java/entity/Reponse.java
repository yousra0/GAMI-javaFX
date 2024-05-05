package entity;

public class Reponse
{
    private int id;

    private String contenu_rep;
    private String date_rep;
    private int reclamation_id;
    private Reclamation reclamation ;

    public Reponse(){};
    public Reponse(int id, String contenu_rep,   int reclamation_id)
    {
        this.contenu_rep = contenu_rep;
        this.id = id;
        this.reclamation_id = reclamation_id;

    }
    public Reponse(String contenu_rep)
    {
        this.contenu_rep = contenu_rep;
    }

    public String getContent()
    {
        return contenu_rep;
    }
    public Reponse(int id, String contenu_rep, String date_rep, Reclamation reclamation)
    {
        this.contenu_rep = contenu_rep;
        this.date_rep = date_rep;
        this.id = id;
        this.reclamation = reclamation;
    }
    public int getId() {
        return id;
    }
    public int getreclamation_id() {
        return reclamation_id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setreclamation_id(int reclamation_id) {
        this.reclamation_id = reclamation_id;
    }



    public String getContenu_rep() {
        return contenu_rep;
    }

    public void setContenu_rep(String contenu_rep) {
        this.contenu_rep = contenu_rep;
    }


    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", contenu_rep='" + contenu_rep + '\'' +
                ", reclamation_id=" + reclamation_id +
                '}';
    }
}
