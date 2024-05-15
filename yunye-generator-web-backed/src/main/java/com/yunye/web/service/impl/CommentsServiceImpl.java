package com.yunye.web.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yunye.web.mapper.CommentsMapper;
import com.yunye.web.model.entity.Comments;
import com.yunye.web.model.entity.Generator;
import com.yunye.web.model.entity.User;
import com.yunye.web.model.vo.CommentsVO;
import com.yunye.web.service.CommentsService;
import com.yunye.web.service.GeneratorService;
import com.yunye.web.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author victor
 * @description 针对表【comments】的数据库操作Service实现
 * @createDate 2024-05-12 17:41:03
 */


@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
        implements CommentsService {

    @Resource
    private UserService userService;
    @Resource
    private GeneratorService generatorService;

    @Override
    public List<CommentsVO> getAllCommentsVO(List<Comments> commentsList) {
        List<CommentsVO> commentsVOList = new ArrayList<>();  // 初始化为空列表
        for (Comments comments : commentsList) {
            CommentsVO commentsVO = new CommentsVO();
            Long user_id = comments.getUser_id();
            Long generator_id = comments.getGenerator_id();
            User user = userService.getById(user_id);
            Generator generator = generatorService.getById(generator_id);

            if (user == null || generator == null) {
                // 可以根据实际业务需求决定是否跳过此条记录或记录错误
                continue;
            }

            Long generatorUserId = generator.getUserId();
            BeanUtils.copyProperties(comments, commentsVO);
            commentsVO.setUsers(user);
            commentsVO.setGeneratorOwnerId(generatorUserId);
            commentsVOList.add(commentsVO);  // 确保 commentsVOList 已被正确初始化
        }
        return commentsVOList;
    }
}





