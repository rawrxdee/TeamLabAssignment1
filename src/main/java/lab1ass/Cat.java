package lab1ass;


public class Cat extends Animal {
    private int microchip;
    /**
     METHOD:  Cat Construct
     PURPOSE: To build the animal object
     PASSED:
     @param _breed  breed of the animal
     @param _name   name of the animal
     @param _dob    dob of the animal DD MMM YYYY
     @param _gender gender of the animal
     @param _regdue date that reg is due DD MMM YYYY
     @param _microchip microchip id for the pet
     RETURNS: Cat Object
     EFFECT:  A cat object is built
     */
    Cat(String _breed, String _name, String _gender, String _dob, String _regdue, int _microchip) {
        super(_breed, _name, _gender, _dob, _regdue, _microchip);
        setMicrochip(_microchip);
    }
    private void setMicrochip(int microchip) throws IllegalArgumentException {
        if (microchip >= 0){
            this.microchip = microchip;
        } else {
            throw new IllegalArgumentException("Microchip should not be null");
        }
    }
    /**
     METHOD:  toString
     PURPOSE: Return the cat object
     PASSED: null
     RETURNS: Cat Object
     EFFECT:  An cat object in text
     */
    @Override
    public String toString() {
        return "Cat [breed=" + breed
                + ", name=" + name
                + ", dob=" + dob
                + ", gender=" + gender
                + ", regdue=" + regdue
                + ", microchip=" + microchip
                + ", registrationID=" + registrationID
                + "]";
    }
}
