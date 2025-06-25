package com.example.superligasparta.model.referee;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefereeRequest {

  @NotBlank
  private String name;

  @NotBlank
  private String surname;

  private String fathersName;
}

