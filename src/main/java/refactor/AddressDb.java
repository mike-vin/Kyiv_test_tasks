package refactor;

import com.sun.istack.internal.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AddressDb {
    //statement "Ресурсы должны быть закрыты"
    public void addPerson(Person person) {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", "admin", "beefhead");
             PreparedStatement statement = connection.prepareStatement("insert into AddressEntry values (?, ?, ?)")) {

            statement.setLong(1, System.currentTimeMillis());
            statement.setString(2, person.getName());
            statement.setString(3, person.getPhoneNumber().getNumber());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Looks up the given person, null if not found.
     */
    @Nullable // safe warning
    public Person findPerson(String name) {
        //if (name == null || name.isEmpty()) throw new IllegalArgumentException("name = '" + name + "'");
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", "admin", "beefhead");
             PreparedStatement statement = connection.prepareStatement("select * from AddressEntry where name = ?")) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) return getPerson(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // statement "null if not found" done
    }

    public List<Person> getAll() {
        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@prod", "admin", "beefhead");
             PreparedStatement statement = connection.prepareStatement("select * from AddressEntry")) {

            ResultSet resultSet = statement.executeQuery();
            List<Person> personsList = new ArrayList<>();

            while (resultSet.next()) personsList.add(getPerson(resultSet));

            return personsList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Person getPerson(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        PhoneNumber phoneNumber = new PhoneNumber(resultSet.getString("phoneNumber"));
        return new Person(name, phoneNumber);
    }
}