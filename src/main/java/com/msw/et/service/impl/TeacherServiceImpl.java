package com.msw.et.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.msw.et.annotation.DataSource;
import com.msw.et.entity.Teacher;
import com.msw.et.enums.DataSourceEnum;
import com.msw.et.mapper.TeacherMapper;
import com.msw.et.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper,Teacher> implements TeacherService {

    @Override
    @DataSource(DataSourceEnum.EASY)
    public boolean insert(Teacher entity) {
        return super.insert(entity);
    }

    @Override
    @DataSource(DataSourceEnum.EASY)
    public boolean deleteById(Serializable id) {
        return super.deleteById(id);
    }

    @Override
    @DataSource(DataSourceEnum.EASY)
    public boolean updateById(Teacher entity) {
        return super.updateById(entity);
    }

    @Override
    @DataSource(DataSourceEnum.EASY)
    public Teacher selectById(Serializable id) {
        return super.selectById(id);
    }

    @Override
    @DataSource(DataSourceEnum.EASY)
    public List<Teacher> selectList(Wrapper<Teacher> wrapper) {
        return super.selectList(wrapper);
    }

    @Override
    @DataSource(DataSourceEnum.EASY)
    public Page<Teacher> selectPage(Page<Teacher> page) {
        return super.selectPage(page);
    }

    @Override
    @DataSource(DataSourceEnum.EASY)
    public Page<Teacher> selectPage(Page<Teacher> page, Wrapper<Teacher> wrapper) {
        return super.selectPage(page, wrapper);
    }
}
