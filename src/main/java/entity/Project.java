package entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * user's yarn project.
 */
@Entity
@Table(name = "project_log")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "project")
    private String project;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "progress")
    private String progress;

    @Column(name = "pattern_url")
    private String patternUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_yarn",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "yarn_id")
    )
    private Set<Yarn> yarns = new HashSet<>();
    public Project() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Yarn> getYarns() {
        return yarns;
    }

    public void setYarns(Set<Yarn> yarns) {
        this.yarns = yarns;
    }

    public String getPatternUrl() {
        return patternUrl;
    }

    public void setPatternUrl(String patternUrl) {
        this.patternUrl = patternUrl;
    }
}