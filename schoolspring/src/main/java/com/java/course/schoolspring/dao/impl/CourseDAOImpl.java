package com.java.course.schoolspring.dao.impl;

import com.java.course.schoolspring.dao.AbstractCrudDao;
import com.java.course.schoolspring.dao.CourseDAO;
import com.java.course.schoolspring.mapper.CourseRowMapper;
import com.java.course.schoolspring.mapper.StudentRowMapper;
import com.java.course.schoolspring.model.Course;
import com.java.course.schoolspring.model.Student;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CourseDAOImpl extends AbstractCrudDao<Course, Integer> implements CourseDAO {
    private final static String INSERT_QUERY = "INSERT INTO school_console_app.courses (course_name, course_description) VALUES (:courseName, :courseDescription)";

    private final static String SELECT_ALL_QUERY = "SELECT * FROM school_console_app.courses";
    private final static String DELETE_QUERY = "DELETE FROM school_console_app.courses WHERE course_id = :courseId";

    private final static String SELECT_QUERY = "SELECT * FROM school_console_app.courses WHERE course_id = :course_id";
    private final static String UPDATE_QUERY = "UPDATE school_console_app.courses SET course_name = :courseName, course_description = :courseDescription WHERE course_id = :courseId";

    private final static String INSERT_QUERY_STUDENTS_COURSES = "INSERT INTO school_console_app.students_courses (student_id, course_id) VALUES (:studentId, :courseId) ON CONFLICT (student_id, course_id) DO NOTHING";

    private final static String DELETE_QUERY_STUDENTS_COURSES = "DELETE FROM school_console_app.students_courses WHERE student_id = :studentId AND course_id = :courseId";
    private static final String FIND_STUDENTS_BY_COURSE_NAME =
            "SELECT s.* FROM school_console_app.students s " +
                    "JOIN school_console_app.students_courses sc ON s.student_id = sc.student_id " +
                    "JOIN school_console_app.courses c ON sc.course_id = c.course_id " +
                    "WHERE c.course_name = :courseName";

    private static final String FIND_COURSES_BY_STUDENT_ID = "SELECT c.* FROM school_console_app.courses c INNER JOIN school_console_app.students_courses sc ON c.course_id = sc.course_id WHERE sc.student_id = :studentId";
    private static final String COUNT = "SELECT COUNT(*) FROM school_console_app.courses";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final CourseRowMapper rowMapper;


    public CourseDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, CourseRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public int count() {
        return namedParameterJdbcTemplate.queryForObject(COUNT, new HashMap<>(), Integer.class);
    }

    @Override
    public List<Student> findStudentByCourseName(String courseName) {
        Map<String, Object> params = new HashMap<>();
        params.put("courseName", courseName);
        return namedParameterJdbcTemplate.query(FIND_STUDENTS_BY_COURSE_NAME, params, new StudentRowMapper());
    }

    @Override
    public List<Course> findCourseByStudentId(Integer studentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        return namedParameterJdbcTemplate.query(FIND_COURSES_BY_STUDENT_ID, params, rowMapper);
    }

    @Override
    public void addStudentToCourse(Integer studentId, Integer courseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        params.put("courseId", courseId);
        namedParameterJdbcTemplate.update(INSERT_QUERY_STUDENTS_COURSES, params);
    }

    @Override
    public void deleteStudentFromCourse(Integer studentId, Integer courseId) {
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        params.put("courseId", courseId);
        namedParameterJdbcTemplate.update(DELETE_QUERY_STUDENTS_COURSES, params);
    }

    @Override
    public Course create(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_QUERY, new BeanPropertySqlParameterSource(course), keyHolder);
        Number generatedId = (Number) keyHolder.getKeyList().get(0).get("course_id");
        if (generatedId != null) {
            course.setCourseId(generatedId.intValue());
            return course;

        } else {
            throw new RuntimeException("Failed");
        }
    }

    @Override
    public void saveAll(List<Course> courses) {
        for (Course course : courses) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(INSERT_QUERY, new BeanPropertySqlParameterSource(course), keyHolder);
            Number generatedId = (Number) keyHolder.getKeyList().get(0).get("course_id");
            if (generatedId != null) {
                course.setCourseId(generatedId.intValue());
            } else {
                throw new RuntimeException("###");
            }
        }
    }


    @Override
    public Optional<Course> findById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", id);
        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(SELECT_QUERY, params, rowMapper));
    }

    @Override
    public List<Course> findAll() {

        return namedParameterJdbcTemplate.query(SELECT_ALL_QUERY, rowMapper);
    }

    @Override
    public void deleteById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("courseId", id);
        namedParameterJdbcTemplate.update(DELETE_QUERY, params);
    }

    public Course update(Course course) {
        namedParameterJdbcTemplate.update(UPDATE_QUERY, new BeanPropertySqlParameterSource(course));
        return course;
    }

}