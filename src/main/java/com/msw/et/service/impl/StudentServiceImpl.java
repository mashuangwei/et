package com.msw.et.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.msw.et.entity.Student;
import com.msw.et.mapper.StudentMapper;
import com.msw.et.service.StudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper,Student> implements StudentService {
    @Override
    public Student selectByMsw(Long id) {
        return baseMapper.selectByMsw(id);
    }
}
