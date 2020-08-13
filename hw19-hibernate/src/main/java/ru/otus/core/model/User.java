package ru.otus.core.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AddressDataSet addressDataSet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PhoneDataSet> phoneDataSet;

    public User() {
    }

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(long id, String name, AddressDataSet addressDataSet, List<PhoneDataSet> phoneDataSet) {
        this.id = id;
        this.name = name;
        this.addressDataSet = addressDataSet;
        addressDataSet.setUser(this);
        this.phoneDataSet = phoneDataSet;
        for (PhoneDataSet phone : phoneDataSet) {
            phone.setUser(this);
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDataSet getAddressDataSet() {
        return addressDataSet;
    }

    public List<PhoneDataSet> getPhoneDataSet() {
        return phoneDataSet;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddressDataSet(AddressDataSet addressDataSet) {
        this.addressDataSet = addressDataSet;
    }

    public void setPhoneDataSet(List<PhoneDataSet> phoneDataSet) {
        this.phoneDataSet = phoneDataSet;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
                name.equals(user.name) &&
                Objects.equals(addressDataSet, user.addressDataSet) &&
                Objects.equals(phoneDataSet, user.phoneDataSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, addressDataSet, phoneDataSet);
    }
}

