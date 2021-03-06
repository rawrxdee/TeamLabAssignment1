/**
 *
 */
package lab2ass;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;

/**
 * The Base Animal Class
 *
 * @author RAWR-XD (Nathan Blaney)
 * @version 1.0
 */
public abstract class Animal implements Serializable {
    // General Animal Attributes
    private static int nextValidRegistrationID = 1;
    Integer registrationID;
    String breed;
    String name;
    String dob;
    String gender;
    String regdue;
    int microchip;

    /**
     * To build the animal object
     *
     * @param _breed     breed of the animal
     * @param _name      name of the animal
     * @param _dob       dob of the animal DD MMM YYYY
     * @param _gender    gender of the animal
     * @param _regdue    date that reg is due DD MMM YYYY
     * @param _microchip microchip id for the pet
     */
    Animal(String _breed, String _name, String _gender, String _dob, String _regdue, int _microchip) {
        this.setRegistrationID();
        this.breed = _breed;
        this.name = _name;
        this.gender = _gender;
        this.dob = _dob;
        this.regdue = _regdue;
        this.microchip = _microchip;
    }

    public Integer getRegistrationID() {
        return registrationID;
    }

    private void setRegistrationID() { // allocate next available ID
        this.registrationID = nextValidRegistrationID++;
    }

    String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegdue() {
        return regdue;
    }

    public void setRegdue(String regdue) {
        this.regdue = regdue;
    }

    public int getMicrochip() {
        return microchip;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    /**
     * Return the animal object
     */
    @Override
    public String toString() {
        return breed + "," + name + "," + dob + "," + gender + "," + regdue + "," + microchip;
    }

    public double calcRates() {
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");

        Calendar regoDate = Calendar.getInstance();
        try {
            regoDate.setTime(format.parse(this.regdue));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long regoYear = regoDate.get(Calendar.YEAR);
        double fee = 20.00;
        long currentYear = Year.now().getValue();
        if (regoYear == currentYear) {
            return fee;
        }
        for (int i = 0; i < currentYear - regoYear; i++) {
            fee = fee - (fee * 0.01);
        }
        return fee;
    }

}
