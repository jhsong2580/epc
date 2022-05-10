package toy.epc.user.form;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterForm {
    @NotEmpty
    private String email;

    @NotEmpty
    private String handPhone;

    @NotEmpty
    private String identification;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @NotEmpty
    private String city;

    @NotEmpty
    private String gu;




}
