package br.com.brlsistemas.librayapi.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoanFilterDTO {
    private String isbn;
    private String customer;
}
