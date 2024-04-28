package Entity;
import java.time.LocalDate;

public class Game {
    private int id;
    private int categorie_id;
    private String name;
    private String description;
    private LocalDate date ;
    private String image;
    private String lien;
    private CategorieJeux categorieJeux;


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

    public LocalDate getDate() {
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }
    public Game(){}
    //id`, `categorie_id`, `name`, `description`, `date`, `image`, `lien`
    public Game(String name, String description, LocalDate date, String image,int categorie_id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.image = image;
        this.categorie_id=categorie_id;

    }


    public Game(String name, String description, LocalDate date, String image,String lien,CategorieJeux categorieJeux) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.image = image;
        this.categorieJeux=categorieJeux;
    }

    public Game(String name, String description, LocalDate date, String image, String lien) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.image = image;
        this.lien = lien;
    }

    public Game(int id, String name, String description, LocalDate date, String image, String lien) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.image = image;
        this.lien = lien;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", categorie_id=" + categorie_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", image='" + image + '\'' +
                ", lien='" + lien + '\'' +
                '}';
    }
}
