package br.com.brlsistemas.librayapi.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanDTO {

    private Long id;
    private String isbn;
    private String customer;
    private BookDTO book;
}
