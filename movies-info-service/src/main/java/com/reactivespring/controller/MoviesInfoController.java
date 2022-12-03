package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

    private MoviesInfoService moviesInfoService;

    public MoviesInfoController(MoviesInfoService moviesInfoService) {
        this.moviesInfoService = moviesInfoService;
    }

    @PostMapping("/moviesinfo")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo) {
        return moviesInfoService.addMovieInfo(movieInfo);
    }

    @GetMapping("/moviesinfo")
    public Flux<MovieInfo> getAllMoviesInfo() {
        return moviesInfoService.getAllMoviesInfo();
    }

    @GetMapping("/moviesinfo/{id}")
    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id) {
        return moviesInfoService.getMovieInfoById(id);
    }

    @PutMapping("/moviesinfo/{id}")
    public Mono<MovieInfo> updateMovieInfo(
            @PathVariable String id,
            @RequestBody MovieInfo updatedMovieInfo) {
        return moviesInfoService.updateMovieInfo(updatedMovieInfo, id);
    }

    @DeleteMapping("/moviesinfo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfoById(@PathVariable String id) {
        return moviesInfoService.deleteMovieInfoById(id);
    }
}
