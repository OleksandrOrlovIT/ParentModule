package orlov.oleksandr.programming.backendgateway.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HomeController {

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("Hello, Home!");
    }

    @GetMapping("/secured")
    public Mono<String> secured() {
        return Mono.just("Hello, Secured!");
    }

    @GetMapping("/profile")
    public Mono<String> profile(@AuthenticationPrincipal OAuth2User principal) {
        String userName = principal.getAttribute("name");
        return Mono.just(userName);
    }
}