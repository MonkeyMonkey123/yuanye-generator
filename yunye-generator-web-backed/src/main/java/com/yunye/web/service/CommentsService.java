package com.yunye.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunye.web.model.entity.Comments;
import com.yunye.web.model.entity.Generator;
import com.yunye.web.model.vo.CommentsVO;
import com.yunye.web.model.vo.GeneratorVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author victor
* @description 针对表【comments】的数据库操作Service
* @createDate 2024-05-12 17:41:03
*/
public interface CommentsService extends IService<Comments> {
    List<CommentsVO> getAllCommentsVO(List<Comments> commentsList);

}
