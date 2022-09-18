package br.com.brlsistemas.librayapi.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanDTO {

    String isbn;
    String customer;
}
