package com.example.chattestapp.DataBaseClasses;

public class User {

    private String email;
    private String firstname;
    private String middlename;
    private String lastname;
    private String phoneno;
    private String uid;
    private String thumbprofilepic;
    private String profilepic;
    private String isOnline;

    public User() {
    }

    public User(String email, String firstname, String middlename, String lastname, String phoneno, String uid, String isOnline) {
        this.email = email;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.phoneno = phoneno;
        this.uid = uid;
        this.isOnline = isOnline;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getOnline() {
        return isOnline;
    }

    public void setOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getThumbprofilepic() {
        return thumbprofilepic;
    }

    public void setThumbprofilepic(String thumbprofilepic) {
        this.thumbprofilepic = thumbprofilepic;
    }
}
