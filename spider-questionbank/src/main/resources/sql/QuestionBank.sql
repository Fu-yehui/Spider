create database QuestionBank;

USE QuestionBank;

CREATE TABLE question(
                         id bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                         title varchar(200) NOT NULL COMMENT '题目标题',
                         description text NOT NULL COMMENT '题目描述',
                         input varchar(200) NOT NULL COMMENT '输入',
                         output varchar(200) NOT NULL COMMENT '输出',
                         sample_input text NULL COMMENT '样例输入',
                         sample_output text NULL COMMENT '样例输出',
                         prompt text NULL COMMENT '提示',
                         label varchar(200) NULL COMMENT '标签',
                         PRIMARY KEY (id),
                         UNIQUE KEY (title)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='题目表'