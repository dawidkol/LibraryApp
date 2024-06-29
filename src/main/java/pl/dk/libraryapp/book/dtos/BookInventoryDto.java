package pl.dk.libraryapp.book.dtos;

import lombok.Builder;

@Builder
public record BookInventoryDto(String id,
                               String title,
                               String author,
                               String publisher,
                               int quantity) {
}
