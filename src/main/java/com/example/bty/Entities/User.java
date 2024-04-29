package com.example.bty.Entities;



public class User {

    private int id;
    private String name;
    private String email;
    private String password ;
    private String telephone ;
    private Role role;
    private  boolean etat =false;    // 0 = desactiver , 1 = activer
    private String image;
    private boolean mfaEnabled ;//si l'utilisateur a activé l'authentification à deux facteurs
    private String mfaSecret ;//la clé secrète de l'authentification à deux facteurs

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    public User(int id, String name, String email, String telephone, Role role, boolean b, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.etat = b;
        this.image = image;

    }
    public User(int id, String name, String email, String telephone, boolean etat) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.etat = etat;


    }
    public User(int id, String name, String email, String telephone,Role role, boolean etat) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.etat = etat;


    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public User(int id, String name, String email, String password, String telephone, Role role,String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.telephone=telephone;
        this.role=role;
        this.image=image;

    }

    public User( String name, String email, String password, String telephone, Role role,Boolean etat,String image) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.telephone=telephone;
        this.role=role;
        this.etat=etat;
        this.image=image;

    }

    public User() {
    }

    public User(String name, String email, String password,String telephone,Role role,String image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.role=role;
        this.etat=etat;
        this.image=image;
    }

    public Integer getId() {
        return id;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public Role getRole() {
        return  role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", telephone='" + telephone + '\'' +
                ", role=" + role +
                ", etat=" + etat +
                ", image='" + image + '\'' +
                ", mfaEnabled=" + mfaEnabled +
                ", mfaSecret='" + mfaSecret + '\'' +
                '}';
    }

    public String getTelephone() {
        return telephone;
    }



}