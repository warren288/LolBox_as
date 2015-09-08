package com.warren.lolbox.model;

/**
 * @author:yangsheng
 * @date:2015/9/1
 */
public class SortModel {

    private String name;
    private String title;
    private String sortLetters;
    private String sortLettersTitle;
    private String nameEng;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSortLettersTitle(String sortLettersTitle) {
        this.sortLettersTitle = sortLettersTitle;
    }

    public String getSortLettersTitle() {
        return sortLettersTitle;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

}
