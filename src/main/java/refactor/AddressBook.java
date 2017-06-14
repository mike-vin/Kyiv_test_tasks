package refactor;

import java.util.List;
import java.util.stream.Collectors;

public class AddressBook {
    private final AddressDb ADDRESS_DB = new AddressDb();

    public boolean hasMobile(String name) {
        Person person = ADDRESS_DB.findPerson(name);
        return person != null && person.getPhoneNumber().getNumber().startsWith("070");
    }

    public int getSize() {
        return ADDRESS_DB.getAll().size();
    }

    /**
     * Gets the given user's mobile phone number,
     * or null if he doesn't have one.
     */
    public String getMobile(String name) {
        Person person = ADDRESS_DB.findPerson(name);
        if (person != null && person.getPhoneNumber() != null) return person.getPhoneNumber().toString();
        return null;// "null if he doesn't have one" -> done
    }

    /**
     * Returns all names in the book truncated to the given length.
     */
    public List getNames(int maxLength) {
        return ADDRESS_DB
                .getAll()
                .stream()
                .map(Person::getName)
                .map(name -> name.length() > maxLength ? name.substring(0, maxLength) : name)
                .collect(Collectors.toList());
    }

    /**
     * Returns all people who have mobile phone numbers.
     */
    public List getList() {
        return ADDRESS_DB
                .getAll()
                .stream()
                .filter(p -> hasMobile(p.getName()))
                .collect(Collectors.toList());
    }
}