import React, { useState, useEffect } from 'react';
import { List, Button, Input, message, Form, Avatar } from 'antd';
import { DeleteOutlined } from '@ant-design/icons';
import { getCommentUsingGet, addCommentUsingPost, deleteCommentUsingDelete } from "@/services/backend/commentsController";

const { TextArea } = Input;

interface CommentItem {
  data: API.CommentsVO;
}

interface Props {
  generatorId: number;
  userId: number;
  generatorAuthorId: number;
}

const TemplateComments: React.FC<Props> = ({ generatorId, userId, generatorAuthorId }) => {
  const [comments, setComments] = useState<CommentItem[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [commentContent, setCommentContent] = useState<string>('');

  // 获取评论
  const fetchComments = async () => {
    setLoading(true);
    try {
      const response = await getCommentUsingGet({ id: generatorId });
      // 使用空数组作为默认值
      setComments(response.data ? response.data.map(comment => ({ data: comment })) : []);
    } catch (error) {
      message.error('加载评论失败');
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchComments();
  }, []);

  const handleAddComment = async () => {
    if (!commentContent.trim()) {
      message.warning('评论内容不能为空！');
      return;
    }
    try {
      await addCommentUsingPost({
        content: commentContent,
        generator_id: generatorId,  // 修改为正确的属性名
        user_id: userId,            // 修改为正确的属性名
      });
      setCommentContent('');
      fetchComments();
      message.success('评论添加成功！');
    } catch (error) {
      message.error('添加评论失败');
    }
  };


  const handleDeleteComment = async (commentId?: number) => {
    if (commentId === undefined) {
      return; // 防止传递 undefined 到 API
    }
    try {
      await deleteCommentUsingDelete({ id: commentId });
      fetchComments();
      message.success('评论已删除！');
    } catch (error) {
      message.error('删除评论失败');
    }
  };

  return (
    <div>
      <List
        dataSource={comments}
        header={`${comments.length} 条评论`}
        itemLayout="horizontal"
        renderItem={item => (
          <List.Item
            actions={[
              (userId === item.data.user_id || userId === generatorAuthorId) && (
                <Button onClick={() => item.data.id && handleDeleteComment(item.data.id)} icon={<DeleteOutlined />}>
                  删除
                </Button>
              )
            ]}
          >
            <List.Item.Meta
              avatar={<Avatar src={item.data.users?.userAvatar} />}
              title={item.data.users?.userName}
              description={item.data.content}
            />
          </List.Item>
        )}
        loading={loading}
      />
      <Form.Item>
        <TextArea rows={4} onChange={e => setCommentContent(e.target.value)} value={commentContent} />
        <Button htmlType="submit" loading={loading} onClick={handleAddComment} type="primary" style={{ marginTop: '10px' }}>
          添加评论
        </Button>
      </Form.Item>
    </div>
  );
};

export default TemplateComments;
