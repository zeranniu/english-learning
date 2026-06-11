-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    avatar VARCHAR(255),
    grade VARCHAR(20) DEFAULT '三年级',
    student_no VARCHAR(20) DEFAULT '',
    level INTEGER DEFAULT 5,
    level_name VARCHAR(20) DEFAULT '小学学霸',
    exp INTEGER DEFAULT 850,
    exp_max INTEGER DEFAULT 1200,
    total_score INTEGER DEFAULT 1560,
    study_days INTEGER DEFAULT 128,
    streak_days INTEGER DEFAULT 7,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 单词表
CREATE TABLE IF NOT EXISTS vocab_word (
    id BIGSERIAL PRIMARY KEY,
    word VARCHAR(100) NOT NULL,
    phonetic VARCHAR(100),
    translation VARCHAR(200) NOT NULL,
    icon VARCHAR(50) DEFAULT 'book',
    icon_color VARCHAR(20) DEFAULT '#4A90E2',
    grade VARCHAR(20) DEFAULT '三年级',
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 用户单词学习记录
CREATE TABLE IF NOT EXISTS user_vocab_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    word_id BIGINT NOT NULL,
    known BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    learned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 听力训练表
CREATE TABLE IF NOT EXISTS listening_lesson (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    duration VARCHAR(20) DEFAULT '3:45',
    audio_url VARCHAR(500),
    grade VARCHAR(20) DEFAULT '三年级',
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 听力题目
CREATE TABLE IF NOT EXISTS listening_question (
    id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT NOT NULL,
    question_text VARCHAR(500) NOT NULL,
    option_a VARCHAR(200) NOT NULL,
    option_b VARCHAR(200) NOT NULL,
    option_c VARCHAR(200) NOT NULL,
    correct_option VARCHAR(1) NOT NULL,
    explanation VARCHAR(500),
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 阅读理解表
CREATE TABLE IF NOT EXISTS reading_passage (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    difficulty INTEGER DEFAULT 2,
    suggested_minutes INTEGER DEFAULT 5,
    grade VARCHAR(20) DEFAULT '三年级',
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 阅读生词
CREATE TABLE IF NOT EXISTS reading_vocab (
    id BIGSERIAL PRIMARY KEY,
    passage_id BIGINT NOT NULL,
    word VARCHAR(100) NOT NULL,
    meaning VARCHAR(200) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 阅读题目
CREATE TABLE IF NOT EXISTS reading_question (
    id BIGSERIAL PRIMARY KEY,
    passage_id BIGINT NOT NULL,
    question_text VARCHAR(500) NOT NULL,
    option_a VARCHAR(200) NOT NULL,
    option_b VARCHAR(200) NOT NULL,
    option_c VARCHAR(200) NOT NULL,
    correct_option INTEGER NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 语法练习表
CREATE TABLE IF NOT EXISTS grammar_question (
    id BIGSERIAL PRIMARY KEY,
    question_text VARCHAR(500) NOT NULL,
    option_a VARCHAR(200) NOT NULL,
    option_b VARCHAR(200) NOT NULL,
    option_c VARCHAR(200) NOT NULL,
    option_d VARCHAR(200) NOT NULL,
    correct_option INTEGER NOT NULL,
    explanation VARCHAR(500) NOT NULL,
    grade VARCHAR(20) DEFAULT '三年级',
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 错题本
CREATE TABLE IF NOT EXISTS mistake_record (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category VARCHAR(20) NOT NULL,
    title VARCHAR(500) NOT NULL,
    error_info VARCHAR(500),
    tag VARCHAR(50),
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PK排行榜
CREATE TABLE IF NOT EXISTS pk_ranking (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    score INTEGER DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 通知消息
CREATE TABLE IF NOT EXISTS notification (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    icon VARCHAR(50) DEFAULT 'notification',
    is_read BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 学习提醒
CREATE TABLE IF NOT EXISTS study_reminder (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    label VARCHAR(100) NOT NULL,
    reminder_time VARCHAR(10) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 意见反馈
CREATE TABLE IF NOT EXISTS feedback (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    feedback_type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    contact VARCHAR(200),
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 每日学习记录
CREATE TABLE IF NOT EXISTS daily_study_log (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    study_date DATE NOT NULL,
    study_minutes INTEGER DEFAULT 0,
    tasks_completed INTEGER DEFAULT 0,
    week_score INTEGER DEFAULT 0,
    week_rank INTEGER DEFAULT 0,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- 成就徽章
CREATE TABLE IF NOT EXISTS user_badge (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    badge_name VARCHAR(50) NOT NULL,
    unlocked BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    UNIQUE(user_id, badge_name)
);

-- ==================== 管理端系统表 ====================

-- 管理员表（独立于小程序用户表）
CREATE TABLE IF NOT EXISTS sys_admin (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    avatar VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(20),
    status INT DEFAULT 1,
    last_login_at TIMESTAMP,
    last_login_ip VARCHAR(50),
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    name VARCHAR(100) NOT NULL,
    path VARCHAR(200),
    component VARCHAR(200),
    icon VARCHAR(100),
    sort_order INT DEFAULT 0,
    visible BOOLEAN DEFAULT TRUE,
    permission VARCHAR(200),
    type INT DEFAULT 1,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL,
    role_code VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(500),
    status INT DEFAULT 1,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGSERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    UNIQUE(role_id, menu_id)
);

-- 管理员角色关联表
CREATE TABLE IF NOT EXISTS sys_admin_role (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE(admin_id, role_id)
);

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGSERIAL PRIMARY KEY,
    admin_id BIGINT,
    username VARCHAR(100),
    module VARCHAR(100),
    action VARCHAR(100),
    method VARCHAR(200),
    params TEXT,
    result TEXT,
    ip VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
