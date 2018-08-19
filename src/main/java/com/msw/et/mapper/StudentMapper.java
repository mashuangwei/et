package com.msw.et.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.msw.et.entity.Student;

public interface StudentMapper extends BaseMapper<Student> {
    Student selectByMsw(Long id);
}
