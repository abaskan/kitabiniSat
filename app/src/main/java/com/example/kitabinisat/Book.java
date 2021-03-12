package com.example.kitabinisat;

import java.io.Serializable;

public class Book implements Serializable {
    private String bookId;
    private String userId;
    private String bookTitle;
    private String bookImageUrl;
    private String bookDetail;
    private String bookLocation;
    private double bookPrice;
    private long addingTime;
    private long favorites;
    private long views;

    public Book() {
    }

    public Book(String bookId, String userId, String bookTitle, String bookImageUrl, String bookDetail, String bookLocation, double bookPrice, long addingTime, long favorites, long views) {
        this.bookId = bookId;
        this.userId = userId;
        this.bookTitle = bookTitle;
        this.bookImageUrl = bookImageUrl;
        this.bookDetail = bookDetail;
        this.bookLocation = bookLocation;
        this.bookPrice = bookPrice;
        this.addingTime = addingTime;
        this.favorites = favorites;
        this.views = views;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public String getBookDetail() {
        return bookDetail;
    }

    public void setBookDetail(String bookDetail) {
        this.bookDetail = bookDetail;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public long getAddingTime() {
        return addingTime;
    }

    public void setAddingTime(long addingTime) {
        this.addingTime = addingTime;
    }

    public long getFavorites() {
        return favorites;
    }

    public void setFavorites(long favorites) {
        this.favorites = favorites;
    }

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }
}
