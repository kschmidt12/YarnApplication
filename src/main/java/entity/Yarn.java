package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Yarn")
@Table(name = "yarn_log")

/**
 * main class for yarn
 */
public class Yarn {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int id;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "color")
    private String color;

    @Column(name = "yarn_amount")
    private String yarnAmount;

    @Column(name = "yarn_size")
    private String yarnSize;

    @Column(name = "hook_size")
    private String hookSize;

    @Column(name = "bought_from")
    private String boughtFrom;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToMany(mappedBy = "yarns")
    private Set<Project> projects = new HashSet<>();

    /**
     * Instantiates yarn
     */
    public Yarn() {

    }
    /**
     * constructor for brand name and yarn
     * @param brandName the band name
     * @param user the user
     */
    public Yarn(String brandName, User user) {
        this.brandName = brandName;
        this.user = user;
    }

    /**
     *
     * @return id the yarn id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return brandName the yarn brand
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     *
     * @return color the yarn color
     */
    public String getColor() {
        return color;
    }

    /**
     *
     * @return yarnAmount the amount of yarn owned
     */
    public String getYarnAmount() {
        return yarnAmount;
    }

    /**
     *
     * @return yarnSize the size/weight of the yarn
     */
    public String getYarnSize() {
        return yarnSize;
    }

    /**
     *
     * @return hookSize the recommended hook size
     */
    public String getHookSize() {
        return hookSize;
    }

    /**
     *
     * @return boughtFrom where the yarn was bought
     */
    public String getBoughtFrom() {
        return boughtFrom;
    }

    /**
     *
     * @return user the owner of the yarn
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param id yarn id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param brandName yarn brand
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     *
     * @param color yarn color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @param yarnAmount amount owned
     */
    public void setYarnAmount(String yarnAmount) {
        this.yarnAmount = yarnAmount;
    }

    /**
     *
     * @param yarnSize size/weight of yarn
     */
    public void setYarnSize(String yarnSize) {
        this.yarnSize = yarnSize;
    }

    /**
     *
     * @param hookSize recommended hook size
     */
    public void setHookSize(String hookSize) {
        this.hookSize = hookSize;
    }

    /**
     *
     * @param boughtFrom where the yarn was bought
     */
    public void setBoughtFrom(String boughtFrom) {
        this.boughtFrom = boughtFrom;
    }

    /**
     *
     * @param user yarn owner
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return toString yarn information
     */
    @Override
    public String toString() {
        return "Yarn{" +
                "id=" + id +
                ", brandName='" + brandName + '\'' +
                ", color='" + color + '\'' +
                ", yarnAmount='" + yarnAmount + '\'' +
                ", yarnSize='" + yarnSize + '\'' +
                ", hookSize='" + hookSize + '\'' +
                ", boughtFrom='" + boughtFrom + '\'' +
                ", user=" + user +
                '}';
    }

}
