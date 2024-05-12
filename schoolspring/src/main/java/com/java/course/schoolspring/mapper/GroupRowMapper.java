package com.java.course.schoolspring.mapper;

import com.java.course.schoolspring.model.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupRowMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        Group group = new Group();
        group.setGroupId(resultSet.getInt("Group_id"));
        group.setGroupName(resultSet.getString("Group_name"));
        return group;
    }
}
