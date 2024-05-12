package com.java.course.schoolspring.dao.impl;

import com.java.course.schoolspring.dao.GroupDAO;
import com.java.course.schoolspring.mapper.GroupRowMapper;
import com.java.course.schoolspring.model.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        NamedParameterJdbcTemplate.class,
        GroupDAOImpl.class,
        GroupRowMapper.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/clear_data.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GroupDAOImplTest {
    @Autowired
    private GroupDAO groupDAO;

    @Test
    void shouldFindAllGroups() {
        List<Group> courses = groupDAO.findAll();
        assertEquals(10, courses.size());
    }

    @Test
    void shouldFindGroupById() {
        Optional<Group> optionalGroup = groupDAO.findById(10001);
        assertTrue(optionalGroup.isPresent());
        assertEquals("A-01", optionalGroup.get().getGroupName());
    }

    @Test
    void shouldCreatNewGroup() {
        Group group = new Group();
        group.setGroupName("JC-90");
        Group createdGroup = groupDAO.save(group);
        assertNotNull(createdGroup.getGroupId());
        assertEquals("JC-90", createdGroup.getGroupName());
    }

    @Test
    void shouldUpdateGroup() {
        Group updateGroup = groupDAO.findById(10001).orElseThrow();
        updateGroup.setGroupName("New Group name");
        groupDAO.save(updateGroup);
        assertEquals("New Group name", updateGroup.getGroupName());
    }


    @Test
    void shouldDeleteGroupById() {
        groupDAO.deleteById(10009);
        Optional<Group> deletedGroup = groupDAO.findById(10009);
        assertTrue(deletedGroup.isEmpty());
    }

    @Test
    void shouldFindGroupByStudentCount() {
        List<Group> groupsByStudentCount = groupDAO.findGroupsByStudentCount(30);

        assertEquals(10, groupsByStudentCount.size());
    }
}
