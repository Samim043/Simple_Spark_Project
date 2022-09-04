package com.sam.courses.model;

import com.github.slugify.Slugify;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CourseIdea {
    private String title;
    private String creator;
    private String slug;
    private Set<String> voters;

    public CourseIdea(String title, String creator) {
        voters = new HashSet<>();
        this.title = title;
        this.creator = creator;
        Slugify slugify = Slugify.builder().underscoreSeparator(true).build();
        slug = slugify.slugify(title);

    }
    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }
    public String getSlug() {
        return slug;
    }

    public boolean addVoter(String voterUsername){
        return voters.add(voterUsername);
    }

    public int getVoterCounts(){
        return voters.size();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseIdea that = (CourseIdea) o;
        return Objects.equals(title, that.title) && Objects.equals(creator, that.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, creator);
    }
}
