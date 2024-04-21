package atypon.store.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Comment {

    @Size(max = 255)
    private String username;

    @NotNull
    private Integer id;

    @Size(max = 500)
    private String text;

    @NotNull
    private Integer blogId;

}
