package ru.otus.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "addressDataSet")
public class AddressDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Column(name = "street")
    private String street;

    public AddressDataSet() {

    }

    public AddressDataSet(long id, String street) {
        this.id = id;
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDataSet)) return false;
        AddressDataSet that = (AddressDataSet) o;
        return id == that.id &&
                street.equals(that.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street);
    }
}
