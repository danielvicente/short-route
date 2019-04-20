package br.com.bexs.infraestructure;

import br.com.bexs.domain.RouteDTO;
import br.com.bexs.domain.ShortRouteDTO;
import br.com.bexs.domain.service.RouteServiceFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RouteServiceControllerV1 {

    RouteServiceFacade routeServiceFacade;

    @GetMapping("/routes")
    public ResponseEntity<List<RouteDTO>> getRoutes() {
        List<RouteDTO> routes = this.routeServiceFacade.findAllRoutes();
        if (routes.isEmpty()) {
            ResponseEntity.status(HttpStatus.OK).body(Collections.EMPTY_LIST);
        }
        return ResponseEntity.status(HttpStatus.OK).body(routes);
    }

    @GetMapping("/route/{origin}/{destination}")
    public ResponseEntity<ShortRouteDTO> getShortestRoute(
            @PathVariable(value = "origin") String origin,
            @PathVariable(value = "destination") String destination) {
        Optional<ShortRouteDTO> shortestRoute = this.routeServiceFacade.findShortestRoute(origin, destination);
        return shortestRoute.map(p -> ResponseEntity.status(HttpStatus.OK).body(p))
                .orElseGet(ResponseEntity.status(HttpStatus.NOT_FOUND)::build);
    }

    @PostMapping("/route/{origin}/{destination}/{price}")
    public ResponseEntity createRoute(@PathVariable(value = "origin") String origin,
                                      @PathVariable(value = "destination") String destination,
                                      @PathVariable(value = "price") Integer price) {
        RouteDTO responsepaymentDTO = this.routeServiceFacade.addRoute(origin, destination, price);
        return new ResponseEntity<>(responsepaymentDTO, HttpStatus.CREATED);
    }

}
