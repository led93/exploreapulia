package com.example.ep.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Objects;

@Document
public class Tour {
    @Id
    private String id;
    @Indexed
    private String title;
    @Indexed
    private String tourPackageCode;
    private String tourPackageName;
    private Map<String, String> details;


    public Tour(String id, String title, String tourPackageCode, String tourPackageName, Map<String, String> details) {
        this.id = id;
        this.title = title;
        this.tourPackageCode = tourPackageCode;
        this.tourPackageName = tourPackageName;
        this.details = details;
    }

    public Tour() {
    }

    public Tour(String title, TourPackage tourPackage, Map<String, String> details) {
        this.title = title;
        this.tourPackageCode = tourPackage.getCode();
        this.tourPackageName = tourPackage.getName();
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTourPackageCode() {
        return tourPackageCode;
    }

    public void setTourPackageCode(String tourPackageCode) {
        this.tourPackageCode = tourPackageCode;
    }

    public String getTourPackageName() {
        return tourPackageName;
    }

    public void setTourPackageName(String tourPackageName) {
        this.tourPackageName = tourPackageName;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tour tour = (Tour) o;

        if (!Objects.equals(id, tour.id)) return false;
        if (!Objects.equals(title, tour.title)) return false;
        if (!Objects.equals(tourPackageCode, tour.tourPackageCode))
            return false;
        if (!Objects.equals(tourPackageName, tour.tourPackageName))
            return false;
        return Objects.equals(details, tour.details);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (tourPackageCode != null ? tourPackageCode.hashCode() : 0);
        result = 31 * result + (tourPackageName != null ? tourPackageName.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", tourPackageCode='" + tourPackageCode + '\'' +
                ", tourPackageName='" + tourPackageName + '\'' +
                ", details=" + details +
                '}';
    }
}
