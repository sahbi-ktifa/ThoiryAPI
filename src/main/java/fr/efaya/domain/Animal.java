package fr.efaya.domain;

/**
 * Created by sktifa on 25/11/2016.
 */
public class Animal extends CommonObject {
    private String specieId;
    private String name;
    private Integer age;

    public String getSpecieId() {
        return specieId;
    }

    public void setSpecieId(String specieId) {
        this.specieId = specieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
