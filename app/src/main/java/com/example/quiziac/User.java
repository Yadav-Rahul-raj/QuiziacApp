package com.example.quiziac;

public class User {
    private String username,email,password,profileImage;
    private int pointsMarks;

    public User() {
    }




    public User(String username, String email, String password, int pointsMarks, String profileImage) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pointsMarks = pointsMarks;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getPointsMarks() {
        return pointsMarks;
    }

    public void setPointsMarks(int pointsMarks) {
        this.pointsMarks = pointsMarks;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
