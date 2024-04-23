package Entity;

import java.util.ArrayList;
import java.util.List;

public class CategorieJeux {
    private int id;
    private String description;
    private String NomCat;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Game> getGames() {
        return games;
    }
    private List<Game> games = new ArrayList<>();
    public void setGames(List<Game> games)
    {
        this.games = games;
    }

    public CategorieJeux(int id,String NomCat,String description) {
        this.NomCat = NomCat;
        this.id = id;
        this.description=description;
        this.games = new ArrayList<>();
    }
    public CategorieJeux(String NomCat,String description) {
        this.NomCat = NomCat;
        this.description=description;
        this.games = new ArrayList<>();
    }

    public  CategorieJeux(String NomCat) {
        this.NomCat = NomCat;
    }

    public CategorieJeux() {

    }

    public CategorieJeux(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getNomCat() {
        return NomCat;
    }

    public void setNomCat(String NomCat) {
        this.NomCat = NomCat;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return NomCat;
    }
}
