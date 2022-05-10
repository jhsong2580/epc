package toy.epc.user.form;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {

    @NotEmpty(message = "id must not be empty")
    private String identification;

    @NotEmpty(message = "password must not be empty")
    private String password;
}
