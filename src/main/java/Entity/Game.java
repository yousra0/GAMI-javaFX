package Entity;

public class Game {

    public int getId() {
        return id;
    }

    public int getCategorie_id() {
        return categorie_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getLien() {
        return lien;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategorie_id(int categorie_id) {
        this.categorie_id = categorie_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }
    public Game(){

    }
    private int id;
    private int categorie_id;
    private String name;
    private CategorieJeux categorieJeux;



    private String description;
    private String date ;

    public Game(String name, String description, String date, String image,int categorie_id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.image = image;
        this.categorie_id=categorie_id;

    }
    public Game(String name, String description, String date, String image) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.image = image;

    }

    public Game(String name, String description, String date, String image,CategorieJeux categorieJeux) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.image = image;
        this.categorieJeux=categorieJeux;
    }

    private String image;
    private String lien;
}
