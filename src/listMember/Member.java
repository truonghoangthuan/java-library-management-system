package listMember;

import javafx.beans.property.SimpleStringProperty;

public class Member {
    private SimpleStringProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty phone;
    private SimpleStringProperty email;

    public Member(String id, String name, String phone, String email) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() {
        return email.get();
    }
}
