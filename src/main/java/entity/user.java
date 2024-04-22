package entity;

public class user {
    private int id ;
    private String email ;
    private String password ;
    private String roles ;

    private String nom ;

    private byte is_verified,is_banned	;
    private String prenom ;
    private String pays ;
    private String datenai ;
    private String pprofile ;
    public user (){
    }


    public user(int id, String email, String password, String roles, String nom, String prenom, String pays, String datenai, String pprofile) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.datenai = datenai;
        this.pprofile = pprofile;
    }

    public user(int id, String email, String password, String roles, String nom, String prenom, String pays, String datenai, String pprofile,byte is_verified,byte is_banned) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.nom = nom;
        this.prenom = prenom;
        this.pays = pays;
        this.datenai = datenai;
        this.pprofile = pprofile;
        this.is_banned = is_banned;
        this.is_verified = is_verified;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDatenai() {
        return datenai;
    }

    public void setDatenai(String datenai) {
        this.datenai = datenai;
    }

    public String getPprofile() {
        return pprofile;
    }

    public void setPprofile(String pprofile) {
        this.pprofile = pprofile;
    }

    public void is_banned	(byte is_banned) {
        this.is_banned = is_banned;
    }

    public void is_verified	(byte is_verified) {
        this.is_verified = is_verified;
    }
    



}

