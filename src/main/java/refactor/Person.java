package refactor;

import java.util.Date;

public class Person { // 'POJO'
    final private String name;
    final private PhoneNumber phoneNumber;
    private Date date;

    public Person(String name, PhoneNumber phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Person(String NAME, PhoneNumber phoneNumber, Date date) {
        this(NAME, phoneNumber);
        this.date = date;
    }

    public String getName() {
        return new String(name.getBytes());
    }


    public PhoneNumber getPhoneNumber() {
        return new PhoneNumber(phoneNumber.toString());
    }


    public Date getDate() {
        return new Date(date.getTime());
    }

}
