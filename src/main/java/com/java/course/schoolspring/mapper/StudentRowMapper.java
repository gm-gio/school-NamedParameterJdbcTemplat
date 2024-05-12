package com.java.course.schoolspring.mapper;

import com.java.course.schoolspring.model.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        Student student = new Student();
        student.setStudentId(resultSet.getInt("Student_id"));
        student.setGroupId(resultSet.getInt("Group_id"));
        student.setFirstName(resultSet.getString("First_name"));
        student.setLastName(resultSet.getString("last_name"));
        return student;
    }
}
