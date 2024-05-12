package com.java.course.schoolspring.dao.impl;

import com.java.course.schoolspring.dao.AbstractCrudDao;
import com.java.course.schoolspring.dao.GroupDAO;
import com.java.course.schoolspring.mapper.GroupRowMapper;
import com.java.course.schoolspring.model.Group;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class GroupDAOImpl extends AbstractCrudDao<Group, Integer> implements GroupDAO {
    private final static String INSERT_QUERY = "INSERT INTO school_console_app.groups (group_name) VALUES (:groupName)";
    private final static String SELECT_ALL_QUERY = "SELECT * FROM school_console_app.groups";
    private final static String DELETE_QUERY = "DELETE FROM school_console_app.groups WHERE group_id = :groupId";

    private final static String SELECT_QUERY = "SELECT * FROM school_console_app.groups WHERE group_id = :groupId";
    private final static String UPDATE_QUERY = "UPDATE school_console_app.groups SET group_name = :groupName WHERE group_id = :groupId";
    private static final String COUNT = "SELECT COUNT(*) FROM school_console_app.groups";
    private static final String SELECT_GROUPS_BY_STUDENT_COUNT =
            "SELECT g.group_id, g.group_name " +
                    "FROM school_console_app.groups g " +
                    "LEFT JOIN school_console_app.students s ON g.group_id = s.group_id " +
                    "GROUP BY g.group_id, g.group_name " +
                    "HAVING COUNT(s.group_id) <= :maxStudentCount";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final GroupRowMapper rowMapper;

    public GroupDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, GroupRowMapper rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public int count() {
        return namedParameterJdbcTemplate.queryForObject(COUNT, new HashMap<>(), Integer.class);
    }

    @Override
    public List<Group> findGroupsByStudentCount(int maxStudentCount) {
        Map<String, Object> params = new HashMap<>();
        params.put("maxStudentCount", maxStudentCount);
        return namedParameterJdbcTemplate.query(SELECT_GROUPS_BY_STUDENT_COUNT, params, rowMapper);
    }

    @Override
    public Group create(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(INSERT_QUERY, new BeanPropertySqlParameterSource(group), keyHolder);
        Number generatedId = (Number) keyHolder.getKeyList().get(0).get("group_id");
        if (generatedId != null) {
            group.setGroupId(generatedId.intValue());
            return group;

        } else {
            throw new RuntimeException("Failde to create the group. No generated key obtained.");
        }
    }

    @Override
    public Optional<Group> findById(Integer id) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("groupId", id);
            return Optional.of(namedParameterJdbcTemplate.queryForObject(SELECT_QUERY, params, new GroupRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public List<Group> findAll() {
        return namedParameterJdbcTemplate.query(SELECT_ALL_QUERY, new GroupRowMapper());
    }


    @Override
    public Group update(Group group) {
        namedParameterJdbcTemplate.update(UPDATE_QUERY, new BeanPropertySqlParameterSource(group));
        return group;
    }

    @Override
    public void deleteById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", id);
        namedParameterJdbcTemplate.update(DELETE_QUERY, params);
    }

    @Override
    public void saveAll(List<Group> groups) {
        for (Group group : groups) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            namedParameterJdbcTemplate.update(INSERT_QUERY, new BeanPropertySqlParameterSource(group), keyHolder);
            Number generatedId = (Number) keyHolder.getKeyList().get(0).get("group_id");
            if (generatedId != null) {
                group.setGroupId(generatedId.intValue());
            } else {
                throw new RuntimeException("####");
            }
        }
    }

}
