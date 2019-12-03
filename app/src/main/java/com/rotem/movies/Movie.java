package com.rotem.movies;

import java.io.Serializable;

public class Movie implements Serializable
{
    private int id;
    private String imageUrl = "";
    private String name;
    private String description;
    private String score;
    private String actors;

    public Movie()
    {
    }

    public Movie(int id, String name, String description, String score, String actors, String imageUrl)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.score = score;
        this.actors = actors;
        this.imageUrl = imageUrl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getScore()
    {
        return score;
    }

    public String getActors()
    {
        return actors;
    }

    public void setActors(String actors)
    {
        this.actors = actors;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public String getDescription()
    {
        return description;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setScore(String score)
    {
        this.score = score;
    }
}
