package br.com.bexs.domain.service.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationGraph {

    private Set<Location> nodes = new HashSet<>();

    public void addNode(Location location) {
        nodes.add(location);
    }
}
