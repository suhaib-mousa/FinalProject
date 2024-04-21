package atypon.store.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Like {

    @Size(max = 255)
    private String username;
    @NotNull
    private Integer id;
    @NotNull
    private Integer blogId;

}