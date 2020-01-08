
create table article 
(
id number(4)  primary key,
pid number(4),
rootid number(4),
title varchar2(255),
cont varchar2(255),
pdate date,
isleaf varchar2(2)
);

insert into article values (bxgx_seq_aaz611.nextval, 0, 1, '蚂蚁大战大象', '蚂蚁大战大象', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 1);
insert into article values (bxgx_seq_aaz611.nextval, 1, 1, '大象被打趴下了', '大象被打趴下了',to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 1);
insert into article values (bxgx_seq_aaz611.nextval, 2, 1, '蚂蚁也不好过','蚂蚁也不好过', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 0);
insert into article values (bxgx_seq_aaz611.nextval, 2, 1, '瞎说', '瞎说', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 1);
insert into article values (bxgx_seq_aaz611.nextval, 4, 1, '没有瞎说', '没有瞎说', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 0);
insert into article values (bxgx_seq_aaz611.nextval, 1, 1, '怎么可能', '怎么可能', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 1);
insert into article values (bxgx_seq_aaz611.nextval, 6, 1, '怎么没有可能', '怎么没有可能', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 0);
insert into article values (bxgx_seq_aaz611.nextval, 6, 1, '可能性是很大的', '可能性是很大的', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 0);
insert into article values (bxgx_seq_aaz611.nextval, 2, 1, '大象进医院了', '大象进医院了', to_date(to_char(sysdate,'yyyy-MM-dd'),'yyyy-MM-dd'), 1);