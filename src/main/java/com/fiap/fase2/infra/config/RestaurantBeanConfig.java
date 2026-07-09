package com.fiap.fase2.infra.config;

import com.fiap.fase2.application.restaurant.*;
import com.fiap.fase2.domain.restaurant.RestaurantGateway;
import com.fiap.fase2.domain.user.UserGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantBeanConfig {

    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return new CreateRestaurantUseCase(restaurantGateway, userGateway);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(RestaurantGateway restaurantGateway, UserGateway userGateway) {
        return new UpdateRestaurantUseCase(restaurantGateway, userGateway);
    }

    @Bean
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        return new FindRestaurantByIdUseCase(restaurantGateway);
    }

    @Bean
    public FindAllRestaurantsUseCase findAllRestaurantsUseCase(RestaurantGateway restaurantGateway) {
        return new FindAllRestaurantsUseCase(restaurantGateway);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(RestaurantGateway restaurantGateway) {
        return new DeleteRestaurantUseCase(restaurantGateway);
    }

    @Bean
    public FindRestaurantsByOwnerUseCase findRestaurantsByOwnerUseCase(RestaurantGateway restaurantGateway) {
        return new FindRestaurantsByOwnerUseCase(restaurantGateway);
    }
}

