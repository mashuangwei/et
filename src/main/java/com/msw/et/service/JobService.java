package com.msw.et.service;

import com.baomidou.mybatisplus.service.IService;
import com.msw.et.entity.Teacher;
import org.springframework.stereotype.Service;

@Service
public interface JobService extends IService<Teacher> {
    public void buildGoodStockTimer();
}
