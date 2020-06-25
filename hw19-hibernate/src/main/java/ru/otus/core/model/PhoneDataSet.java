package ru.otus.core.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "phoneDataSet")
public class PhoneDataSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(cascade = CascadeType.ALL)
    public User user;

    public PhoneDataSet() {

    }

    public PhoneDataSet(long id, String number) {
        this.id = id;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return id == that.id &&
                number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }
}
