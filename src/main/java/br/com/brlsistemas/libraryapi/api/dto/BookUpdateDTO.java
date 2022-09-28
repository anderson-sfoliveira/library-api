package br.com.brlsistemas.libraryapi.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateDTO {
        @NotEmpty
        private String title;

        @NotEmpty
        private String author;
}
