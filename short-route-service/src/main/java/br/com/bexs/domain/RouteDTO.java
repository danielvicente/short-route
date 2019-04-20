package br.com.bexs.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {

    private String origin;
    private String destination;
    private Integer price;

}
