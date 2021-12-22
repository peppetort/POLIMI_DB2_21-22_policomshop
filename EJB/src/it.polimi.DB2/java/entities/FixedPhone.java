package entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2")
public class FixedPhone extends Service {

    @Override
    protected String getHTMLFields() {
        return "";
    }
}
