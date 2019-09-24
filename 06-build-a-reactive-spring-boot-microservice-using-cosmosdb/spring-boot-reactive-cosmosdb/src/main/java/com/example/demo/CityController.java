package com.example.demo;

import com.azure.data.cosmos.CosmosClient;
import com.azure.data.cosmos.CosmosContainer;
import com.azure.data.cosmos.FeedOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CityController {

    private CosmosContainer container = CosmosClient.builder()
                .endpoint("https://azure-spring-cloud-cosmosdb.documents.azure.com:443/")
                .key("2YiKGyQfvJR1tm0jE4iW8PCq9hZYVxlbrGb6ncbud2leWqsCn9AQbUY6ixOSyPp3qRhWO1Kt65h1Zje4Vrb4rQ==")
                .build()
                .getDatabase("azure-spring-cloud-training")
                .getContainer("City");

    @GetMapping("/cities")
    public Flux<List<City>> getCities() {
        FeedOptions options = new FeedOptions();
        options.enableCrossPartitionQuery(true);
        return container.queryItems("SELECT TOP 20 * FROM City c", options)
                .map(i -> {
                    List<City> results = new ArrayList<>();
                    i.results().forEach(props -> {
                        City city = new City();
                        city.setName(props.getString("name"));
                        results.add(city);
                    });
                    return results;
                });
    }
}

class City {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}