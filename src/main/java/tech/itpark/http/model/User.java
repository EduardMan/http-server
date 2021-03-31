package tech.itpark.http.model;

import java.util.List;

public class User {
    String name;
    String lastName;
    Integer age;
    List<User> relatives;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<User> getRelatives() {
        return relatives;
    }

    public void setRelatives(List<User> relatives) {
        this.relatives = relatives;
    }
}
