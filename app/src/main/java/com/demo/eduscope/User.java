package com.demo.eduscope;

class User {

    private String id, username, email;

    User(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    String getId() {
        return id;
    }

    String getUsername() {
        return username;
    }

    String getEmail() {
        return email;
    }
}