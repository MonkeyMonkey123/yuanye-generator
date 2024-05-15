package com.yunye.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yunye.web.common.BaseResponse;
import com.yunye.web.common.ResultUtils;
import com.yunye.web.model.entity.Comments;
import com.yunye.web.model.vo.CommentsVO;
import com.yunye.web.service.CommentsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    @PostMapping
    public BaseResponse<Boolean> addComment(@RequestBody Comments comment) {

        boolean save = commentsService.save(comment);
        return ResultUtils.success(save);
    }

    @GetMapping("/{id}")
    public BaseResponse<List<CommentsVO>> getComment(@PathVariable Integer id) {
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("generator_id", id);
        List<Comments> commentsList = commentsService.list(queryWrapper);
        List<CommentsVO> allCommentsVO = commentsService.getAllCommentsVO(commentsList);
        return ResultUtils.success(allCommentsVO);
    }

    @PutMapping
    public void updateComment(@RequestBody Comments comment) {
        commentsService.updateById(comment);
    }

    @DeleteMapping("/{id}")
    public  BaseResponse<Boolean> deleteComment(@PathVariable Integer id) {

        boolean b = commentsService.removeById(id);
        return ResultUtils.success(b);
    }
}
