package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Providers;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.validate.ImageNameValid;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDto {


    private String userId;
@Size(min=3,max=25,message="Invalid Name !!")

    private String name;
@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "Email is required !!")
@NotBlank(message = "Email is required !!")
    private String email;
@NotBlank(message = "Password is required !!")
    private String password;
@Size(min=4,max=6,message = "invalid gender ! !")
    private String gender;
@NotBlank(message = "Write something about yourself !!")
    private String about;

private List<RoleDto> roles;
@ImageNameValid
    private String imageName;
private Providers providers;

}
