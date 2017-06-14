package refactor;

public class PhoneNumber extends Num {

    public PhoneNumber(String number) {
        super(number);
    }

    @Override
    public String toString() {
        return super.getNumber();
    }
}