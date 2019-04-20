package br.com.bexs.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShortRouteDTO {

    String origin;
    String destination;
    String fullShortRoute;
    Integer priceBetween;

}
