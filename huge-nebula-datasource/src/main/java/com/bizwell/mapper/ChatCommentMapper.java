package com.bizwell.mapper;

import com.bizwell.entity.ChatComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by charles on 2017/10/25.
 */
@Mapper
public interface ChatCommentMapper {

    List<ChatComment> select(ChatComment entity);
    
    ChatComment selectById(@Param("id")Long id);

    void update(ChatComment comment);
    
    Integer insert(ChatComment comment);
    

}
