package com.example.superligasparta.model.player;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlayerRequest {

  @NotBlank(message = "Имя не может быть пустым")
  private String name;

  private String surname;
  private String fathersName;

  @Past(message = "Дата рождения должна быть в прошлом")
  private LocalDate birthDate;
  private String nickname;
}
