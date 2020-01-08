
-- 建表语句
-- Create table
create table WEB_URL
(
  id     NUMBER(4) not null,
  url    VARCHAR2(500),
  descr  VARCHAR2(500),
  aae013 VARCHAR2(500),
  picurl VARCHAR2(500),
  type   VARCHAR2(2) default 1,
  rtnmsg VARCHAR2(4000)
)
tablespace USERS
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table
comment on table WEB_URL
  is '存放微信图文消息的网页描述的映射';
-- Add comments to the columns
comment on column WEB_URL.id
  is '网页id';
comment on column WEB_URL.url
  is '网页网址';
comment on column WEB_URL.descr
  is '网页描述';
comment on column WEB_URL.aae013
  is '备注';
comment on column WEB_URL.picurl
  is '图片网址';
comment on column WEB_URL.type
  is '消息类型,1 文本 2 图文消息 3 其他';
comment on column WEB_URL.rtnmsg
  is '返回的文本消息,只有当消息类型为1才有效';
-- Create/Recreate primary, unique and foreign key constraints
alter table WEB_URL
  add constraint PK_WEB_URL primary key (ID)
  using index
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
 
-- 序列
create sequence BXGX_SEQ_AAZ611
minvalue 1
maxvalue 999999999999
start with 331
increment by 1
nocache
order;


-- 插入数据
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (320, 'http://www.panda.tv/cate/yzdr', '熊猫tv', '2', 'http://s.dgtle.com/forum/201509/25/192432xwkyrwfyk5l5amcs.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (321, 'http://www.panda.tv/413021', '熊猫tv月亮luna', '2', 'http://s.dgtle.com/forum/201610/11/211946ys0y13g8ohsgf80u.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (322, 'http://www.dgtle.com/', '数字尾巴', '2', 'http://s.dgtle.com/forum/201610/11/212017xyh2e0ycqyjr7uf5.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (323, 'http://www.js.xinhuanet.com/', '江苏新闻', '2', 'http://s.dgtle.com/forum/201610/11/214304br1sgror1m1gr07c.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (324, 'http://www.hao123.com/?tn=94626667_hao_pg', '好123,网页导航', '2', 'http://s.dgtle.com/forum/201610/11/214316iesed1dsfgz74ued.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (325, 'https://www.jd.com/', '京东商城', '2', 'http://s.dgtle.com/forum/201610/13/033330s87s26hcp2il2s1k.jpg?szhdl=imageview/2/w/960', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (326, 'http://www.suning.com/', '苏宁易购', '2', 'http://s.dgtle.com/forum/201610/13/033331q0xhnrxqo3pxq1rx.jpg?szhdl=imageview/2/w/960', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (327, 'http://www.xhu.edu.cn/', '西华大学,锦地苑,', '2', 'http://s.dgtle.com/forum/201610/11/205225kji4tmkctcjtettm.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (328, 'https://mp.weixin.qq.com/', '微信公众号申请页面', '2', 'http://s.dgtle.com/forum/201610/11/205225kji4tmkctcjtettm.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (329, 'https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140453&token=&lang=zh_CN', '微信调试接口', '2', 'http://s.dgtle.com/forum/201610/02/044752tvb4shdfv1l3wzqh.jpg', '2', null);
insert into web_url (ID, URL, DESCR, AAE013, PICURL, TYPE, RTNMSG)
values (330, 'http://www.panda.tv/cate/yzdr', '介绍,关键字,查询,使用', null, null, '1', '查询格式 sbcx单位编号,现在只是测试， 只能够查询 单位的正常参保的总人数,查询格式 sbcx 单位编号 ,参保人数查询支持模糊查询; 例如 sbcx02123456 ,养老账户余额的查询格式： zhcx身份证号,账户余额不支持模糊查询，因为这样的话可能导致查询超时。目前支持的关键字：
熊猫tv
熊猫tv月亮luna
数字尾巴
江苏新闻
好123,网页导航
京东商城
苏宁易购
西华大学,锦地苑,
微信公众号申请页面');
