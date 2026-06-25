-- 默认用户 (密码: 123456, BCrypt加密)
INSERT INTO users (username, password, nickname, grade, student_no, level, level_name, exp, exp_max, total_score, study_days, streak_days)
VALUES ('xiaoming', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '小明同学', '三年级', '2024001', 5, '小学学霸', 850, 1200, 1560, 128, 7)
ON CONFLICT (username) DO NOTHING;

-- PK排行榜
INSERT INTO pk_ranking (user_id, score) VALUES (1, 2560) ON CONFLICT DO NOTHING;
INSERT INTO pk_ranking (user_id, score) VALUES (2, 2456) ON CONFLICT DO NOTHING;
INSERT INTO pk_ranking (user_id, score) VALUES (3, 2345) ON CONFLICT DO NOTHING;
INSERT INTO pk_ranking (user_id, score) VALUES (4, 2234) ON CONFLICT DO NOTHING;

-- 单词
INSERT INTO vocab_word (word, phonetic, translation, icon, icon_color) VALUES
('book', '[bʊk]', '书本', 'book', '#4A90E2'),
('car', '[kɑːr]', '汽车', 'location', '#EF4444'),
('cat', '[kæt]', '小猫', 'compass', '#FF9F43'),
('sun', '[sʌn]', '太阳', 'sun', '#EAB308'),
('apple', '[ˈæpəl]', '苹果', 'gift', '#EF4444'),
('dog', '[dɒɡ]', '狗', 'compass', '#8B5CF6'),
('fish', '[fɪʃ]', '鱼', 'location', '#06B6D4'),
('girl', '[ɡɜːrl]', '女孩', 'woman', '#EC4899'),
('hand', '[hænd]', '手', 'edit', '#F59E0B'),
('ice', '[aɪs]', '冰', 'star-fill', '#3B82F6')
ON CONFLICT DO NOTHING;

-- 听力课程
INSERT INTO listening_lesson (title, duration) VALUES ('Lesson 3 - My Friends', '3:45') ON CONFLICT DO NOTHING;

-- 听力题目
INSERT INTO listening_question (lesson_id, question_text, option_a, option_b, option_c, correct_option, explanation)
VALUES (1, 'Who is the boy in the photo?', 'The boy is Tom.', 'The boy is Sam.', 'The boy is Mike.', 'A', '音频中提到了 "The boy is Tom"')
ON CONFLICT DO NOTHING;

-- 阅读理解
INSERT INTO reading_passage (title, content, difficulty, suggested_minutes) VALUES
('A Beautiful Village', 'There is a forest near our village. In the forest, there is a long river. The river goes through the mountain. Over the river, there is a small bridge. On the other side of the bridge, there is a beautiful lake. Many fish live in the lake.', 2, 5)
ON CONFLICT DO NOTHING;

INSERT INTO reading_vocab (passage_id, word, meaning) VALUES
(1, 'forest', '森林'), (1, 'river', '河流'), (1, 'mountain', '山脉'),
(1, 'bridge', '桥'), (1, 'village', '村庄'), (1, 'lake', '湖泊')
ON CONFLICT DO NOTHING;

INSERT INTO reading_question (passage_id, question_text, option_a, option_b, option_c, correct_option) VALUES
(1, 'Where is the forest?', 'Near the village', 'On the mountain', 'Behind the lake', 0),
(1, 'What is over the river?', 'A forest', 'A bridge', 'A village', 1)
ON CONFLICT DO NOTHING;

-- 语法题目
INSERT INTO grammar_question (question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
('I _____ a student.', 'A. am', 'B. is', 'C. are', 'D. be', 0, '解析：主语第一人称单数 I 对应的 be 动词形式必须是 am。'),
('She _____ like apples.', 'A. don''t', 'B. doesn''t', 'C. isn''t', 'D. not', 1, '解析：单三人称否定助动词使用 doesn''t。'),
('They _____ playing football.', 'A. is', 'B. am', 'C. are', 'D. be', 2, '解析：主语 They 是复数，对应的 be 动词是 are。'),
('He _____ to school every day.', 'A. go', 'B. goes', 'C. going', 'D. went', 1, '解析：主语 He 是单三人称，一般现在时动词加 es。')
ON CONFLICT DO NOTHING;

-- 通知
INSERT INTO notification (user_id, title, description, icon, is_read) VALUES
(1, '系统通知', '恭喜完成今日单词学习！', 'notification', false),
(1, '成就解锁', '你已解锁"单词达人"徽章！', 'gift', false),
(1, '学习提醒', '今天的阅读任务还没完成哦', 'clock-circle', true),
(1, 'PK挑战', '小红同学向你发起了PK挑战', 'trophy', true)
ON CONFLICT DO NOTHING;

-- 学习提醒
INSERT INTO study_reminder (user_id, label, reminder_time, enabled) VALUES
(1, '每日单词学习', '08:00', true),
(1, '听力训练', '16:00', true),
(1, '阅读打卡', '19:00', false),
(1, '语法练习', '20:00', false)
ON CONFLICT DO NOTHING;

-- 每日学习记录
INSERT INTO daily_study_log (user_id, study_date, study_minutes, tasks_completed, week_score, week_rank)
VALUES (1, CURRENT_DATE, 25, 8, 156, 12)
ON CONFLICT DO NOTHING;

-- 成就徽章
INSERT INTO user_badge (user_id, badge_name, unlocked) VALUES
(1, '单词达人', true), (1, '阅读之星', true), (1, '听力大师', false), (1, '学习冠军', false)
ON CONFLICT DO NOTHING;

-- 错题记录
INSERT INTO mistake_record (user_id, category, title, error_info, tag) VALUES
(1, 'vocab', '1. apple', '拼写错误', '词语'),
(1, 'vocab', '2. family', '拼写错误', '词语'),
(1, 'vocab', '3. beautiful', '多字母错写', '词语'),
(1, 'vocab', '4. because', '发音混淆', '词语'),
(1, 'listening', 'Who is the boy?', '主旨混淆（误选 Tom）', '单选'),
(1, 'reading', 'Where does Bear live?', '细节抓取错误', '主旨'),
(1, 'grammar', 'I _____ a student. (选 be)', 'be动词搭配不清', '选词')
ON CONFLICT DO NOTHING;

-- ==================== 管理端初始数据 ====================

-- 默认超级管理员 (密码: 123456, BCrypt加密)
INSERT INTO sys_admin (username, password, nickname, email, status)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', 'admin@english-learning.com', 1)
ON CONFLICT (username) DO NOTHING;

-- 默认角色
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'super_admin', '拥有所有权限', 1),
('教研员', 'teacher', '题库维护权限', 1),
('普通管理员', 'admin', '用户管理和数据查看权限', 1)
ON CONFLICT (role_code) DO NOTHING;

-- 默认菜单树
INSERT INTO sys_menu (parent_id, name, path, component, icon, sort_order, visible, permission, type) VALUES
(0, '数据看板', '/dashboard', 'dashboard/DashboardView', 'DataBoard', 1, TRUE, 'dashboard:view', 1),
(0, '用户管理', '/user', '', 'User', 2, TRUE, 'user', 1),
(0, '系统管理', '/system', '', 'Setting', 3, TRUE, 'system', 1),
(0, '题库管理', '/content', '', 'Collection', 4, TRUE, 'content', 1),
(0, '数据统计', '/stats', '', 'TrendCharts', 5, TRUE, 'stats', 1),

(2, '学生列表', '/user/list', 'user/UserListView', 'UserFilled', 1, TRUE, 'user:list', 1),

(3, '用户管理', '/system/user', 'system/AdminUserListView', 'User', 1, TRUE, 'system:user', 1),
(3, '角色管理', '/system/role', 'system/RoleListView', 'User', 2, TRUE, 'system:role', 1),
(3, '菜单管理', '/system/menu', 'system/MenuListView', 'Menu', 3, TRUE, 'system:menu', 1),
(3, '操作日志', '/system/log', 'system/LogListView', 'Document', 4, TRUE, 'system:log', 1),

(4, '单词库', '/content/vocab', 'content/VocabListView', 'Notebook', 1, TRUE, 'content:vocab', 1),
(4, '听力题库', '/content/listening', 'content/ListeningListView', 'Headset', 2, TRUE, 'content:listening', 1),
(4, '阅读题库', '/content/reading', 'content/ReadingListView', 'Document', 3, TRUE, 'content:reading', 1),
(4, '语法题库', '/content/grammar', 'content/GrammarListView', 'Edit', 4, TRUE, 'content:grammar', 1),

(5, '错题统计', '/stats/mistake', 'stats/MistakeStatsView', 'WarningFilled', 1, TRUE, 'stats:mistake', 1),
(5, '学习统计', '/stats/data', 'stats/DataStatsView', 'TrendCharts', 2, TRUE, 'stats:data', 1)
ON CONFLICT DO NOTHING;

-- 超级管理员关联所有菜单 (假设菜单id从1开始)
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, id FROM sys_menu
ON CONFLICT DO NOTHING;

-- 教研员关联题库管理相关菜单
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 2, id FROM sys_menu WHERE path LIKE '/content/%' OR path = '/content'
ON CONFLICT DO NOTHING;

-- 普通管理员关联用户管理+系统管理+数据看板+数据统计
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 3, id FROM sys_menu WHERE path = '/dashboard' OR path = '/user' OR path LIKE '/user/%' OR path = '/system' OR path LIKE '/system/log' OR path = '/stats' OR path LIKE '/stats/%'
ON CONFLICT DO NOTHING;

-- 管理员-角色关联: admin用户 -> 超级管理员角色
INSERT INTO sys_admin_role (admin_id, role_id)
SELECT a.id, r.id FROM sys_admin a, sys_role r WHERE a.username = 'admin' AND r.role_code = 'super_admin'
ON CONFLICT DO NOTHING;

-- ==================== 任务配置初始数据 ====================

-- 一年级任务
INSERT INTO task_config (grade, task_type, task_title, task_target, sort_order, enabled) VALUES
('一年级', 'vocab', '学习 5 个单词', 5, 1, true),
('一年级', 'listening', '完成 1 篇听力', 1, 2, true),
('一年级', 'reading', '完成 1 篇阅读', 1, 3, true)
ON CONFLICT DO NOTHING;

-- 二年级任务
INSERT INTO task_config (grade, task_type, task_title, task_target, sort_order, enabled) VALUES
('二年级', 'vocab', '学习 8 个单词', 8, 1, true),
('二年级', 'listening', '完成 1 篇听力', 1, 2, true),
('二年级', 'reading', '完成 1 篇阅读', 1, 3, true)
ON CONFLICT DO NOTHING;

-- 三年级任务
INSERT INTO task_config (grade, task_type, task_title, task_target, sort_order, enabled) VALUES
('三年级', 'vocab', '学习 10 个单词', 10, 1, true),
('三年级', 'listening', '完成 1 篇听力', 1, 2, true),
('三年级', 'reading', '完成 1 篇阅读', 1, 3, true)
ON CONFLICT DO NOTHING;

-- 四年级任务
INSERT INTO task_config (grade, task_type, task_title, task_target, sort_order, enabled) VALUES
('四年级', 'vocab', '学习 12 个单词', 12, 1, true),
('四年级', 'listening', '完成 1 篇听力', 1, 2, true),
('四年级', 'reading', '完成 1 篇阅读', 1, 3, true),
('四年级', 'grammar', '完成 1 篇语法', 1, 4, true)
ON CONFLICT DO NOTHING;

-- 五年级任务
INSERT INTO task_config (grade, task_type, task_title, task_target, sort_order, enabled) VALUES
('五年级', 'vocab', '学习 15 个单词', 15, 1, true),
('五年级', 'listening', '完成 1 篇听力', 1, 2, true),
('五年级', 'reading', '完成 1 篇阅读', 1, 3, true),
('五年级', 'grammar', '完成 1 篇语法', 1, 4, true)
ON CONFLICT DO NOTHING;

-- 六年级任务
INSERT INTO task_config (grade, task_type, task_title, task_target, sort_order, enabled) VALUES
('六年级', 'vocab', '学习 20 个单词', 20, 1, true),
('六年级', 'listening', '完成 1 篇听力', 1, 2, true),
('六年级', 'reading', '完成 1 篇阅读', 1, 3, true),
('六年级', 'grammar', '完成 2 篇语法', 2, 4, true)
ON CONFLICT DO NOTHING;
