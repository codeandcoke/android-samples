package space.harbour.reciclerviewsample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Superhero
 */
public class Superhero {
    private String name;
    private String surname;
    private String superHeroeName;

    public Superhero(String name, String surname, String superHeroeName) {
        this.name = name;
        this.surname = surname;
        this.superHeroeName = superHeroeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSuperHeroeName() {
        return superHeroeName;
    }

    public void setSuperHeroeName(String superHeroeName) {
        this.superHeroeName = superHeroeName;
    }

    public String getFullName() {
        return name + " " + surname;

    }
}
