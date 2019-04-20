package br.com.bexs.domain.service.utils;

import br.com.bexs.domain.RouteDTO;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class FileUtils {

    private Function<String, RouteDTO> mapToRouteDto = (line) -> {
        String[] p = line.split(",");
        RouteDTO item = new RouteDTO();
        item.setOrigin(p[0]);
        item.setDestination(p[1]);
        item.setPrice(Integer.valueOf(p[2]));
        return item;
    };

    public File getInputFile(String inputFilePath) {
        return new File(inputFilePath);
    }

    public List<RouteDTO> processInputFile(File inputFile) throws IOException {
        List<RouteDTO> inputList = new ArrayList<>();
        InputStream inputFS = new FileInputStream(inputFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
        inputList = br.lines().skip(1).map(mapToRouteDto).collect(Collectors.toList());
        br.close();
        return inputList;
    }
}
