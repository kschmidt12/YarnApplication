package entity;


import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent an entity.user.
 *
 * @author Katie Schmidt
 */
@Entity
@Table(name = "user")



public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String userName;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Yarn> yarnList = new ArrayList<>();

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param name     the  name
     * @param email    the email
     * @param userName the username
     * @param id       the id
     * @param password the password
     */
    public User(int id, String name, String email, String userName, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userName = userName;
    }

    public void addYarn(Yarn yarn) {
        yarnList.add(yarn);
        yarn.setUser(this);
    }

    public void removeYarn(Yarn yarn) {
        yarnList.remove(yarn);
        yarn.setUser(null);
    }

    public List<Yarn> getYarn() {
        return yarnList;
    }

    public void setYarn(List<Yarn> yarn) {
        this.yarnList = yarn;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the  name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets entity.user name.
     *
     * @return the entity.user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets entity.user name.
     *
     * @param userName the entity.user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }


}