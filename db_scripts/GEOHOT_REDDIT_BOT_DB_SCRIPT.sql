CREATE DATABASE IF NOT EXISTS geohot_reddit_bot;
USE geohot_reddit_bot;

DROP TABLE IF EXISTS bot_config;
CREATE TABLE bot_config
(
    config_id    int(11) NOT NULL AUTO_INCREMENT,
    config_key   varchar(100)  NOT NULL,
    config_value varchar(2000) NOT NULL,
    PRIMARY KEY (config_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS comment_tracker;
CREATE TABLE comment_tracker
(
    tracker_id    int(11) NOT NULL AUTO_INCREMENT,
    tracker_value varchar(2000) NOT NULL,
    PRIMARY KEY (tracker_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Records of bot_config
-- ----------------------------
insert into bot_config(config_key, config_value)
values ('APP_ID', 'provide your reddit appId');
insert into bot_config(config_key, config_value)
values ('SECRET', 'provide your reddit secret');
insert into bot_config(config_key, config_value)
values ('AUTHOR', 'provide your reddit name');
insert into bot_config(config_key, config_value)
values ('ACCOUNT_PASSWORD', 'provide your reddit/bot account password');
commit;
