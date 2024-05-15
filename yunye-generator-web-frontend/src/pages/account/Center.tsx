import React, { useEffect } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Form, Input, Button, message } from 'antd';
import PictureUploader from '../../components/PictureUploader';
import { useModel } from '@umijs/max';
import { updateMyUserUsingPost } from '@/services/backend/userController';

const Center: React.FC = () => {
  const { initialState, setInitialState } = useModel('@@initialState');
  const [form] = Form.useForm();

  useEffect(() => {
    if (initialState?.currentUser) {
      form.setFieldsValue(initialState.currentUser);
    }
  }, [initialState?.currentUser, form]);

  const handleSubmit = async (values: any) => {
    try {
      // 添加额外的参数以符合后端要求
      const updatedValues = { ...values, biz: 'user_avatar' };
      await updateMyUserUsingPost(updatedValues);
      if (initialState) {
        setInitialState((s) => ({
          ...s,
          currentUser: {
            ...s?.currentUser,
            ...values,
          },
        }));
      }
      message.success('更新成功！');
    } catch (error) {
      message.error('更新失败！');
    }
  };

  // 处理头像改变事件
  const handleAvatarChange = (url: string) => {
    form.setFieldsValue({ userAvatar: url });
  };

  return (
    <PageContainer>
      <Form
        form={form}
        onFinish={handleSubmit}
        layout="vertical"
      >
        <Form.Item name="userName" label="用户名">
          <Input />
        </Form.Item>
        <Form.Item name="userAvatar" label="头像">
          <PictureUploader biz="user_avatar" onChange={handleAvatarChange} />
        </Form.Item>
        <Button type="primary" htmlType="submit">
          更新信息
        </Button>
      </Form>
    </PageContainer>
  );
};

export default Center;
