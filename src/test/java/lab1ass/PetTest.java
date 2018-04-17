package lab1ass;
import static org.junit.Assert.*;

public class PetTest {

    @org.junit.Test
    public void testAddPet()
    {
        Person p1test = new Person("25 Somewhere Street", "Kathleen", "2000", "Casey");
        Animal p1testPet = new Dog("Pug", "Fido", "F", "05 01 1998", "05 01 1998", 13, true, false);
        p1test.addAPet(p1testPet);
        assertEquals("Dog [breed=Pug, name=Fido, dob=05 01 1998, gender=F, regdue=05 01 1998, microchip=13, desexed=true, vaccinated=false, registrationID=1]", p1test.pet.toString());
    }
    @org.junit.Test
    public void testAddPetRestricted()
    {
        Person p1test = new Person("25 Somewhere Street", "Kathleen", "2000", "Casey");
        Animal p1testPet = new Dog("Presa Canario", "Fido", "F", "05 01 1998", "05 01 1998", 13, true, false);
        p1test.addAPet(p1testPet);
        assertEquals("Unable to create java.lang.IllegalArgumentException: Dog is on the Restricted Breed's list", p1test.pet.toString());
    }
    @org.junit.Test
    public void testAddPetFailure()
    {
        Person p1test = new Person("25 Somewhere Street", "Kathleen", "2000", "Casey");
        Animal p1testPet = new Dog("Presa Canario", "Fido", "F", "05 01 1998", "05 01 1998", 13, true, false);
        p1test.addAPet(p1testPet);
        assertEquals("Dog [breed=Presa Canario, name=Fido, dob=05 01 1998, gender=F, regdue=05 01 1998, microchip=13, desexed=true, vaccinated=false, registrationID=1]", p1test.pet.toString());
    }
}
