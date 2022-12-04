package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {

    private MovieInfoRepository movieInfoRepository;

    public MoviesInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMoviesInfo() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo updatedMovieInfo, String id) {
        return movieInfoRepository
                .findById(id)
                .flatMap(movieInfo -> {
                    movieInfo.setCast(updatedMovieInfo.getCast());
                    movieInfo.setName(updatedMovieInfo.getName());
                    movieInfo.setRelease_date(updatedMovieInfo.getRelease_date());
                    movieInfo.setYear(updatedMovieInfo.getYear());
                    // do not care about movieInfo's id
                    return movieInfoRepository.save(movieInfo);
                });
    }

    public Mono<Void> deleteMovieInfoById(String id) {
        return movieInfoRepository.deleteById(id);
    }

    public Flux<MovieInfo> getAllMoviesInfoByYear(Integer year) {
        return movieInfoRepository.findByYear(year);
    }
}
