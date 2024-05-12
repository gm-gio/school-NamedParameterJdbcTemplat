package com.java.course.schoolspring.dao.impl;

import com.java.course.schoolspring.dao.AbstractCrudDao;
import com.java.course.schoolspring.dao.StudentDAO;
import com.java.course.schoolspring.mapper.StudentRowMapper;
import com.java.course.schoolspring.model.Student;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StudentDAOImpl extends AbstractCrudDao<Student, Integer> implements StudentDAO {
    private final static String INSERT_QUERY = "INSERT INTO school_console_app.students (group_id, first_name, last_name) VALUES (:groupId, :firstName, :lastName)";
    private final static String SELECT_ALL_QUERY = "SELECT * FROM school_console_app.students";
    private final static String DELETE_QUERY = "DELETE FROM school_console_app.students WHERE student_id = :studentId";

    private final static String SELECT_QUERY = "SELECT * FROM school_console_app.students WHERE student_id = :studentId";
    private final static String UPDATE_QUERY = "UPDATE school_console_app.students SET group_id = :groupId, first_name = :firstName, last_name = :lastName WHERE student_id = :studentId";

    private final static String FIND_STUDENT_BY_COURSE = """
                        SELECT * FROM school_console_app.students 
                        JOIN school_console_app.students_courses ON students.student_id = students_courses.student_id 
                        JOIN school_console_app.courses ON students_courses.course_id = courses.course_id 
                        WHERE courses.course_name = :courseName;
            """;
    private static final String COUNT = "SELECT COUNT(*) FROM school_console_app.courses";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final StudentRowMapper rowMapper;

    public StudentDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, StudentRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }


    @Override
    public int count() {
        return namedParameterJdbcTemplate.queryForObject(COUNT, new HashMap<>(), Integer.class);
    }

    @Override
    public Student create(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_QUERY, new BeanPropertySqlParameterSource(student), keyHolder);
        Number generatedId = (Number) keyHolder.getKeyList().get(0).get("student_id");
        if (generatedId != null) {
            student.setStudentId(generatedId.intValue());
            return student;

        } else {
            throw new RuntimeException("###");
        }
    }


    @Override
    public List<Student> findByCourse(String courseName) {
        Map<String, Object> params = new HashMap<>();
        params.put("courseName", courseName);
        return namedParameterJdbcTemplate.query(FIND_STUDENT_BY_COURSE, params, rowMapper);
    }

    @Override
    public void deleteById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", id);
        namedParameterJdbcTemplate.update(DELETE_QUERY, params);

    }

    @Override
    public Optional<Student> findById(Integer id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("studentId", id);
            return Optional.of(namedParameterJdbcTemplate.queryForObject(SELECT_QUERY, params, rowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Student update(Student student) {
        namedParameterJdbcTemplate.update(UPDATE_QUERY, new BeanPropertySqlParameterSource(student));
        return student;
    }

    @Override
    public List<Student> findAll() {
        return namedParameterJdbcTemplate.query(SELECT_ALL_QUERY, rowMapper);
    }

    @Override
    public void saveAll(List<Student> students) {
        for (Student student : students) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(INSERT_QUERY, new BeanPropertySqlParameterSource(student), keyHolder);
            Number generatedId = (Number) keyHolder.getKeyList().get(0).get("student_id");
            if (generatedId != null) {
                student.setStudentId(generatedId.intValue());
            } else {
                throw new RuntimeException("###");
            }
        }
    }

}
