package com.example.ep.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Tour {
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String title;
    @Column(length = 2000)
    private String description;
    @Column(length = 2000)
    private String blurp;
    @Column
    private Integer price;
    @Column
    private String duration;
    @Column(length = 2000)
    private String bullets;
    @Column
    private String keywords;
    @ManyToOne
    private TourPackage tourPackage;
    @Column
    @Enumerated
    private Difficulty difficulty;
    @Column
    @Enumerated
    private Region region;

    public Tour(Integer id, String title, String description, String blurp, Integer price, String duration,
                String bullets, String keywords, TourPackage tourPackage, Difficulty difficulty, Region region) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.blurp = blurp;
        this.price = price;
        this.duration = duration;
        this.bullets = bullets;
        this.keywords = keywords;
        this.tourPackage = tourPackage;
        this.difficulty = difficulty;
        this.region = region;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBlurp() {
        return blurp;
    }

    public void setBlurp(String blurp) {
        this.blurp = blurp;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBullets() {
        return bullets;
    }

    public void setBullets(String bullets) {
        this.bullets = bullets;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public TourPackage getTourPackage() {
        return tourPackage;
    }

    public void setTourPackage(TourPackage tourPackage) {
        this.tourPackage = tourPackage;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", blurp='" + blurp + '\'' +
                ", price=" + price +
                ", duration='" + duration + '\'' +
                ", bullets='" + bullets + '\'' +
                ", keywords='" + keywords + '\'' +
                ", tourPackage=" + tourPackage +
                ", difficulty=" + difficulty +
                ", region=" + region +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tour tour = (Tour) o;

        if (!Objects.equals(id, tour.id)) return false;
        if (!Objects.equals(title, tour.title)) return false;
        if (!Objects.equals(description, tour.description)) return false;
        if (!Objects.equals(blurp, tour.blurp)) return false;
        if (!Objects.equals(price, tour.price)) return false;
        if (!Objects.equals(duration, tour.duration)) return false;
        if (!Objects.equals(bullets, tour.bullets)) return false;
        if (!Objects.equals(keywords, tour.keywords)) return false;
        if (!Objects.equals(tourPackage, tour.tourPackage)) return false;
        if (difficulty != tour.difficulty) return false;
        return region == tour.region;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (blurp != null ? blurp.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (bullets != null ? bullets.hashCode() : 0);
        result = 31 * result + (keywords != null ? keywords.hashCode() : 0);
        result = 31 * result + (tourPackage != null ? tourPackage.hashCode() : 0);
        result = 31 * result + (difficulty != null ? difficulty.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        return result;
    }
}
