package com.example.studymateriallocator;

public class Material {
    public String id, title, url, uploadedBy;
    public long downloads;

    public Material() {} // empty constructor for Firestore

    public Material(String id, String title, String url, String uploadedBy, long downloads) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.uploadedBy = uploadedBy;
        this.downloads = downloads;
    }
}