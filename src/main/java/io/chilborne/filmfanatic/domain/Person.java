package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity(name = "people")
@Table(indexes = @Index(name = "firstname_lastname_index", columnList = "name, surname"))
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String surname;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PersonTypeEnum type;

  @Override
  public String toString() {
    return name + " " + surname;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (name != null ? !name.equals(person.name) : person.name != null) return false;
    if (surname != null ? !surname.equals(person.surname) : person.surname != null) return false;
    return type == person.type;
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode() + this.type.hashCode();
  }

}
