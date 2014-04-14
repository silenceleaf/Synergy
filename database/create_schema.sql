--开启外键约束
PRAGMA foreign_keys = ON;

drop table if exists user;
CREATE TABLE "user" (
  pk_user integer primary key autoincrement,
  user_name text unique,
  password text,
  status text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0
);
CREATE UNIQUE INDEX i_user_user_name on "user"(user_name);

DROP TABLE IF EXISTS bd_prompt;
CREATE TABLE "bd_prompt" (
  pk_prompt integer primary key autoincrement,
  level integer DEFAULT 0,
  module text,
  detail_en text,
  detail_zh text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS "bd_user_role";
CREATE TABLE "bd_user_role" (
  pk_user_role integer primary key autoincrement,
  fk_user integer,
  fk_role integer,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0,
  constraint fk_user foreign key(fk_user) references bd_user(pk_user),
  constraint fk_role foreign key(fk_role) references bd_role(pk_role)
);

DROP TABLE IF EXISTS "bd_role";
CREATE TABLE "bd_role" (
  pk_role integer primary key autoincrement,
  name text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS "bd_template";
CREATE TABLE "bd_template" (
  pk_template integer primary key autoincrement,
  fk_function integer,
  fk_metadata integer,
  type integer,
  module text,
  user text,
  description text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0
);

INSERT INTO bd_template (pk_template, module) VALUES (100, 'common_form');
INSERT INTO bd_template (pk_template, fk_function, module) VALUES (200, 1,'login');


DROP TABLE IF EXISTS "bd_template_field";
CREATE TABLE "bd_template_field" (
  pk_template_field integer primary key autoincrement,
  fk_template integer not null,
  fk_metadata_detail integer,
  name text,
  type text,
  field_index integer,
  default_value text,
  text_en text,
  text_zh text,
  description text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0,
  constraint fk_template foreign key(fk_template) references bd_template(pk_template)
);

INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (101, 100, 'formFieldNotEmpty',  'Field can not be empty', '字段不能为空');
INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (201, 200, 'userName', 'User Name', '用户名');
INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (202, 200, 'password', 'Password', '密码');
INSERT INTO bd_template_field (pk_template_field, fk_template, name,  text_en, text_zh) VALUES (203, 200, 'title', 'Login', '登录');

DROP TABLE IF EXISTS "bd_function";
CREATE TABLE "bd_function" (
  pk_function integer primary key autoincrement,
  function_no text,
  name_en text,
  name_zh text,
  parent integer,
  display boolean,
  node_index integer,
  description_en text,
  description_zh text,
  file text,
  url text,
  leaf boolean,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0
);


INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (1, null, 'Common Form', '公共表单组件', null, 0, null, '', null, null, null, null, 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (101, null, 'Login', '登录页面', null, 1, null, 'Login Page', null, null, 1, '2014-02-19 14:44:41', 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (109, null, 'Main', '主页面', null, 1, null, 'Main Page', '主页面', null, 1, '2014-02-19 14:44:41', 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (900, null, 'Assignment', '作业', 1000, 1, 0, null, null, null, 0, null, 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (901, null, 'Assignment 5', null, 900, 1, 1, null, null, 'assignment5', 1, null, 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (1000, null, 'Root', '根节点', null, 1, null, 'Root Node', '默认根节点', '', 0, '2014-02-19 14:44:41', 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (1001, null, 'Function', null, 1000, 1, 1, 'Function List', null, null, 0, '2014-02-08 18:25:25', 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (1002, null, 'System Management', null, 1001, 1, 1, 'Provide system setting', null, null, 0, '2014-02-08 18:25:25', 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (1003, null, 'User Management', null, 1002, 1, 1, 'Setting user privilidge', '用户管理', 'base/user', 1, '2014-02-08 18:25:25', 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (1004, null, 'Business', null, 1001, 1, 2, 'Business model', null, '', 0, '2014-02-08 18:25:25', 0);
INSERT INTO bd_function (pk_function, function_no, name_en, name_zh, parent, display, node_index, description_en, description_zh, url, leaf, ts, dr) VALUES (1005, null, 'Test', null, 1004, 1, 1, 'Test', null, 'test', 1, '2014-02-08 18:25:25', 0);

DROP TABLE IF EXISTS "bd_operation_group";
CREATE TABLE "bd_operation_group" (
  pk_operation_group integer primary key autoincrement,
  fk_function integer not null,
  url text not null,
  group_name_en text,
  group_name_zh text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime')),
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS "bd_operation";
CREATE TABLE "bd_operation" (
  pk_operation integer primary key autoincrement,
  fk_operation_group integer not null,
  name_en text,
  name_zh text,
  description_en text,
  description_zh text,
  xtype text,
  width integer,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')),
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS "bd_metadata";
CREATE TABLE "bd_metadata" (
  pk_metadata integer primary key autoincrement,
  name text,
  entity_type integer,
  display_name_en text,
  display_name_zh text,
  primary_table text,
  primary_pk text,
  json_entity text,
  description_en text,
  description_zh text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')),
  dr integer not null DEFAULT 0
);

DROP TABLE IF EXISTS "bd_metadata_detail";
CREATE TABLE "bd_metadata_detail" (
  pk_metadata_detail integer primary key autoincrement,
  fk_metadata integer not null,
  name text,
  type integer,
  data_type text,
  field_name text,
  display_level integer default 1,
  description_en text,
  description_zh text,
  ts DATETIME DEFAULT (datetime(CURRENT_TIMESTAMP, 'localtime')),
  dr integer not null DEFAULT 0
);


DROP TABLE IF EXISTS "handshake";
CREATE TABLE "handshake" (
  pk_handshake integer primary key autoincrement,
  user_id integer,
  activity text,
  card_pic text,
  profile_pic text,
  activity_date text
);

update bd_template_field set type = "string";
