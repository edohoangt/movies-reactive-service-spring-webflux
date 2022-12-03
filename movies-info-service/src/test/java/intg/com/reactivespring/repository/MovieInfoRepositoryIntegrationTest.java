package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@ActiveProfiles("test") // use the library embedded config profile instead of 'local'
class MovieInfoRepositoryIntegrationTest {

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(
                new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );

        movieInfoRepository.saveAll(movieinfos)
                .blockLast(); // blocking call to ensure all data are persisted before every testcases
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Test
    void findAll() {
        // when
        var moviesInfoFlux = movieInfoRepository.findAll();
        // then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        // when
        var moviesInfoMono = movieInfoRepository.findById("abc").log();
        // then
        StepVerifier.create(moviesInfoMono)
                .assertNext((movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                }))
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        // given
        var movieInfo = new MovieInfo(null, "Batman Begins 2",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        // when
        var moviesInfoMono = movieInfoRepository.save(movieInfo);
        // then
        StepVerifier.create(moviesInfoMono)
                .assertNext((movieInfo2 -> {
                    assertNotNull(movieInfo2.getMovieInfoId());
                    assertEquals("Batman Begins 2", movieInfo2.getName());
                }))
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        // given
        var movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2022);
        // when
        var moviesInfoFlux = movieInfoRepository.save(movieInfo);
        // then
        StepVerifier.create(moviesInfoFlux)
                .assertNext((movieInfo2 -> {
                    assertEquals(2022, movieInfo2.getYear());
                }))
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        // when
        movieInfoRepository.deleteById("abc")
                .block();
        var moviesInfoFlux = movieInfoRepository.findAll();
        // then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}