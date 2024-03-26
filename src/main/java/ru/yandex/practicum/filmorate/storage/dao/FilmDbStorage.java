package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository(value = "filmDB")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> findById(Long id) {
        String sqlQuery = "SELECT F.*," +
                "       M.name               as mpa_name," +
                "       GROUP_CONCAT(G.GENRE_ID)   as genre_id," +
                "       GROUP_CONCAT(G.NAME) as genre_name " +
                "FROM FILMS AS F" +
                "         LEFT JOIN PUBLIC.MPA M on M.MPA_ID = F.MPA_ID" +
                "         LEFT JOIN PUBLIC.FILM_GENRE FG on F.FILM_ID = FG.FILM_ID" +
                "         LEFT JOIN PUBLIC.GENRES G on G.GENRE_ID = FG.GENRE_ID " +
                "WHERE F.FILM_ID = ?" +
                "GROUP BY F.FILM_ID";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilms, id);

        return films.stream().findFirst();
    }


    private Set<Genre> mapRowToGenre(ResultSet resultSet) throws SQLException {
        Set<Genre> genreList = new HashSet<>();
        String genId = resultSet.getString("GENRE_ID");
        String genName = resultSet.getString("genre_name");
        if (genId != null) {
            String[] genIds = genId.split(",");
            String[] genNames = genName.split(",");
            for (int i = 0; i < genIds.length; i++) {
                if (i < genNames.length) {
                    genreList.add(Genre.builder()
                            .id(Long.parseLong(genIds[i]))
                            .name(genNames[i])
                            .build());
                }
            }
        }
        return genreList;
    }


    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT F.*," +
                "       M.name as mpa_name," +
                "       GROUP_CONCAT(G.GENRE_ID)   as genre_id," +
                "       GROUP_CONCAT(G.name) as genre_name " +
                "FROM FILMS AS F" +
                "         LEFT JOIN PUBLIC.MPA M on M.MPA_ID = F.MPA_ID" +
                "         LEFT JOIN PUBLIC.FILM_GENRE FG on F.FILM_ID = FG.FILM_ID" +
                "         LEFT JOIN PUBLIC.GENRES G on G.GENRE_ID = FG.GENRE_ID " +
                "GROUP BY F.FILM_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms);
    }


    @Override
    public Film save(Film film) {

        if (film.getMpa().getId()>5){
            throw new NotFoundException("nest", HttpStatus.BAD_REQUEST);
        }
        if (film.getGenres() != null) {
        for (Genre genre: film.getGenres()) {
            if (genre.getId()>6){
                throw new NotFoundException("nest", HttpStatus.BAD_REQUEST);
            }
        }
        }
        String sqlQuery = "INSERT INTO FILMS(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setInt(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId().intValue());
                return stmt;
            }, keyHolder);
            Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
            film.setId(filmId);
            updateFilmGenresLinks(film);
            //return findById(filmId).orElse(null);
            //return findById(filmId).orElse(null);
            return  film;
        } catch (NotFoundException e){
            return null;
        }
    }

    @Override
    public Optional<Film> update(Film film) {
        String sqlQuery = "UPDATE FILMS SET " +
                "FILM_NAME = ?,DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        int update = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (update == 0) {
            throw new NotFoundException("Фильм не найден!", HttpStatus.NOT_FOUND);
        }
        updateFilmGenresLinks(film);
        Film film1 = findById(film.getId()).orElse(null);
        log.info("DEBAGGGGG{}", film1);

        return findById(film.getId());
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO FILMS_LIKES (USER_ID,FILM_ID)" +
                "VALUES (?,?) ";
        try {
            jdbcTemplate.update(sqlQuery, userId, filmId);
            return true;
        } catch (DuplicateKeyException exception) {
            return false;
        }
    }

    @Override
    public boolean isExistById(Long id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM FILMS WHERE FILM_ID = ?)";
        return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id);
    }

    @Override
    public boolean delete(Long id) {
        String sqlQuery = "DELETE FROM FILMS " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    @Override
    public boolean deleteLike(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM FILMS_LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        String sqlQuery = "SELECT F.*," +
                "       M.NAME              as mpa_name," +
                "       GROUP_CONCAT(G.GENRE_ID)   as genre_id," +
                "       GROUP_CONCAT(G.NAME) as genre_name," +
                "       COUNT(FU.USER_ID)" +
                "FROM FILMS AS F" +
                "         LEFT JOIN PUBLIC.MPA M on M.MPA_ID = F.MPA_ID" +
                "         LEFT JOIN PUBLIC.FILM_GENRE FG on F.FILM_ID = FG.FILM_ID" +
                "         LEFT JOIN PUBLIC.GENRES G on G.GENRE_ID= FG.GENRE_ID" +
                "         LEFT JOIN PUBLIC.FILMS_LIKES FU on F.FILM_ID = FU.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(USER_ID) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilms, count);
    }

    private Film mapRowToFilms(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(mapRowToMpa(resultSet))
                .genres(mapRowToGenre(resultSet))
                .build();
    }

    private void deleteFilmGenresLinks(Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void updateFilmGenresLinks(Film film) {


        if (film.getGenres() == null) {
            return;
        }

        deleteFilmGenresLinks(film.getId());
        String sqlQuery = "INSERT INTO FILM_GENRE(FILM_ID, GENRE_ID) " +
                "VALUES (?, ?)";
        Set<Genre> genres = new TreeSet<>(Comparator.comparingLong(Genre::getId));
        genres.addAll(film.getGenres());
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = new ArrayList<>(genres).get(i);
                ps.setLong(1, film.getId());
                ps.setLong(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }


    private MPA mapRowToMpa(ResultSet resultSet) throws SQLException {
        return MPA.builder()
                .id(resultSet.getLong("MPA_ID"))
                .name(resultSet.getString("NAME"))
                .build();
    }
}