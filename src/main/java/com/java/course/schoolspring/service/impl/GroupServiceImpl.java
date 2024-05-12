package com.java.course.schoolspring.service.impl;

import com.java.course.schoolspring.dao.GroupDAO;
import com.java.course.schoolspring.model.Group;
import com.java.course.schoolspring.service.GroupService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupDAO groupDAO;


    public GroupServiceImpl(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudentCount(int maxStudentCount) {
        return groupDAO.findGroupsByStudentCount(maxStudentCount);
    }
}
