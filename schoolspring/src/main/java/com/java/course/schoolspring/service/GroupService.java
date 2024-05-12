package com.java.course.schoolspring.service;

import com.java.course.schoolspring.model.Group;

import java.util.List;

public interface GroupService {
    public List<Group> findGroupsWithLessOrEqualStudentCount(int maxStudentCount);
}
