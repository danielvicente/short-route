package br.com.bexs;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        while (true) {

            System.out.println("Please enter the route or 'exit':");

            String input = scan.next();

            if ("exit".equals(input)) {
                System.exit(0);
            } else {

                String[] split = input.split("-");

                if (split.length < 2) {
                    System.out.println("Comando inválido. Tente usar o padrão XXX-YYY.");
                } else {

                    String origin = split[0];
                    String destination = split[1];

                    try {

                        HttpResponse<JsonNode> jsonResponse = Unirest
                                .get("http://localhost:8080/route/" + origin + "/" + destination + "/")
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .asJson();

                        int status = jsonResponse.getStatus();

                        if (status != 200) {
                            System.out.println(jsonResponse.getBody().getObject().getJSONObject("ApiError").getString("message"));
                        } else {
                            JsonNode body = jsonResponse.getBody();
                            String fullShortRoute = body.getObject().getString("fullShortRoute");
                            Integer priceBetween = body.getObject().getInt("priceBetween");


                            System.out.println(
                                    String.format("best route: %s > $%d", fullShortRoute, priceBetween));
                        }

                    } catch (Exception exception) {
                        System.out.println("Erro ao buscar rota.");
                    }

                }

            }

        }
    }

}
