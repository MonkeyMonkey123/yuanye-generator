import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = '软件开发第一组';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> 生成器源码
            </>
          ),
          href: 'https://github.com/MonkeyMonkey123/yuanye-generator.git',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
