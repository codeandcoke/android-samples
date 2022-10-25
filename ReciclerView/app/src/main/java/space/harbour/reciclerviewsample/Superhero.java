package space.harbour.reciclerviewsample;

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

    public String getSurname() {
        return surname;
    }

    public String getSuperHeroeName() {
        return superHeroeName;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}
