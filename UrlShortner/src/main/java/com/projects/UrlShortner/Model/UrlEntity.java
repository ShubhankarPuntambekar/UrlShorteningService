package com.projects.UrlShortner.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
public class UrlEntity {

    @Id
    @Column(name = "hash_code")
    private String shortCode;

    @NotBlank(message="Required Field")
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$", message = "Invalid URL format")
    @Column(name = "original_url", nullable = false)
    private String url;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name="clicks_number")
    private int clickedTimes;

    @Column(name = "updated_on", nullable = false)
    private  LocalDateTime updatedOn;

    public UrlEntity()
    {
        this.createdOn = LocalDateTime.now();
        this.updatedOn = LocalDateTime.now();
        this.clickedTimes = 0;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getClickedTimes() {
        return clickedTimes;
    }

    public void setClickedTimes(int clickedTimes) {
        this.clickedTimes = clickedTimes;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
