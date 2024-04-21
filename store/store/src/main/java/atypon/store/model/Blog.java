package atypon.store.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Blog {

    @NotNull
    private Integer id;

    @Size(max = 255)
    private String name;

    @Size(max = 500)
    private  String content;

}
