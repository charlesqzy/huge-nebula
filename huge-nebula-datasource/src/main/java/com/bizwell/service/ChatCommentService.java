package com.bizwell.service;

import com.bizwell.entity.ChatComment;
import com.bizwell.mapper.ChatCommentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liujian on 2017/10/31.
 */
@Service
public class ChatCommentService {
    
    @Autowired
    ChatCommentMapper chatCommentMapper;

    
    public PageInfo<ChatComment> getAllData(ChatComment entity, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize); //开始分页
        List<ChatComment> list=chatCommentMapper.select(entity); //查询操作
        PageInfo<ChatComment> pageInfo =new PageInfo<>(list);//将查询的信息封装到pageinfo中
        return pageInfo;
    }
    
    public List<ChatComment> getAllData(ChatComment entity){
        return chatCommentMapper.select(entity); //查询操作
    }
    

    
    public ChatComment selectById(Long id){
        return chatCommentMapper.selectById(id);
    }
    
}
