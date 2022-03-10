package io.chilborne.filmfanatic.batch;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.Person;
import io.chilborne.filmfanatic.domain.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class FilmMapper implements RowMapper {
  @Override
  public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
    Film film = (new BeanPropertyRowMapper<>(Film.class).mapRow(rs, rowNum));

    ResultSetMetaData metaData = rs.getMetaData();

    User user = new User();
    user.setId(rs.getLong("user_id"));
    Person director = new Person();
    director.setId(rs.getLong("director_id"));

    film.setUser(user);
    film.setFilmDirector(director);

    return film;
  }
}
