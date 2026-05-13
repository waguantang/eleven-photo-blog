-- Blog database initialization
CREATE DATABASE IF NOT EXISTS blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blog;

-- Admin user
CREATE TABLE IF NOT EXISTS t_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Category
CREATE TABLE IF NOT EXISTS t_category (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    slug        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tag
CREATE TABLE IF NOT EXISTS t_tag (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    slug        VARCHAR(50)  NOT NULL UNIQUE,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Post
CREATE TABLE IF NOT EXISTS t_post (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    slug          VARCHAR(200) NOT NULL UNIQUE,
    content       LONGTEXT     NOT NULL,
    excerpt       VARCHAR(500),
    cover_image   VARCHAR(500),
    status        VARCHAR(20)  DEFAULT 'draft',
    category_id   BIGINT,
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    published_at  DATETIME,
    FOREIGN KEY (category_id) REFERENCES t_category(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Post-Tag junction
CREATE TABLE IF NOT EXISTS t_post_tag (
    post_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES t_post(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id)  REFERENCES t_tag(id)  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Comment
CREATE TABLE IF NOT EXISTS t_comment (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id      BIGINT       NOT NULL,
    parent_id    BIGINT,
    author_name  VARCHAR(50)  NOT NULL,
    author_email VARCHAR(100),
    content      TEXT         NOT NULL,
    status       VARCHAR(20)  DEFAULT 'pending',
    created_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id)   REFERENCES t_post(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES t_comment(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed admin user: run BlogApplication, then POST /api/auth/setup with {"username":"admin","password":"admin123"}
-- Or generate with: new BCryptPasswordEncoder().encode("admin123")
-- INSERT INTO t_user (username, password) VALUES ('admin', '<BCRYPT_HASH_HERE>');

-- Photo gallery
CREATE TABLE IF NOT EXISTS t_photo (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(100)  NOT NULL,
    description  TEXT,
    src          VARCHAR(500)  NOT NULL,
    category     VARCHAR(50),
    sort_order   INT           DEFAULT 0,
    created_at   DATETIME      DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed data: photos
INSERT INTO t_photo (title, description, src, category, sort_order) VALUES
('晨光熹微', '清晨第一缕阳光穿过薄雾，洒在静谧的街道上。柔和的光线营造出朦胧而温暖的氛围，整个画面仿佛被镀上了一层金色的薄纱。', '/YouName/1006667.jpg', '风光', 1),
('城市脉动', '繁华都市的一角，行人与车流交织成独特的城市节奏。利用慢速快门捕捉到动静结合的画面——静止的建筑背景下，流动的人群形成了有趣的对比。', '/YouName/1006672.jpg', '街拍', 2),
('静谧时光', '一个安静的午后，光影透过窗户洒在木质地板上。画面中弥漫着慵懒而温暖的气息，让人想起那些值得被定格的平凡瞬间。', '/YouName/1006728.jpg', '静物', 3),
('回眸一瞬', '人像摄影的魅力在于捕捉那些转瞬即逝的表情。这张照片利用了侧逆光，让发丝边缘泛起温柔的光晕，为人物增添了一份灵动。', '/YouName/1007102.jpg', '人像', 4),
('山水之间', '远山如黛，近水含烟。这是一张典型的风光摄影作品，运用了前景、中景、远景的三层构图法，让画面有了丰富的层次感和空间感。', '/YouName/1007172.jpg', '风光', 5),
('夜色阑珊', '夜幕降临，城市的灯光开始闪烁。这张夜景用了低ISO和长曝光，让灯光的轨迹在画面中流动。三脚架是夜景拍摄的必备利器。', '/YouName/1007178.jpg', '夜景', 6),
('花语', '微距镜头下的花朵，花瓣的纹理和露珠都清晰可见。浅景深让背景化为柔美的色块，主体更加突出——这是静物摄影中常用的手法。', '/YouName/1244598.jpg', '静物', 7),
('街角故事', '街头摄影的有趣之处在于偶然性。一个不起眼的街角，在特定的光线和路人的配合下，就成了一个有故事感的画面。低角度拍摄让画面更有电影感。', '/YouName/1254303.png', '街拍', 8),
('窗外的世界', '利用窗户作为天然画框，将室外的景色框住。框架式构图让观众的视线自然而然地聚焦在窗外的景色上，同时也增添了画面的纵深。', '/YouName/731739.png', '构图练习', 9),
('落日余晖', '黄金时刻的最后一抹阳光。逆光下的剪影效果，天空呈现出由橙到紫的渐变——这是大自然最美的调色盘。拍日落的关键：宁可欠曝也不要过曝。', '/YouName/740275.png', '风光', 10),
('黑白映像', '剥离了色彩之后，光影和构图就成了画面唯一的主角。这张黑白作品强调了画面的线条感和对比度，适合表现建筑或人物的情绪。', '/YouName/748076.png', '黑白', 11),
('人间烟火', '菜市场、老街道、手艺人——这些充满生活气息的场景是人文摄影的宝库。拍好人文的关键是融入环境，让被摄者忽略你的存在。', '/YouName/764987.jpg', '人文', 12),
('夏日的风', '用高速快门凝固了被风吹起的发丝和裙摆。自然光下的人像，没有刻意摆拍，而是捕捉了人物最自然的状态——这才是好的人像。', '/YouName/765121.png', '人像', 13),
('几何之美', '建筑的线条、楼梯的螺旋、走廊的纵深——抽象几何是极简摄影的精髓。找到有趣的角度，让画面变成线与面的艺术构成。', '/YouName/815592.png', '建筑', 14),
('雨后', '雨后地面的积水形成的倒影，让平凡的场景多了一份诗意。低角度贴近水面拍摄，让倒影和实物形成对称的构图——这是雨天特有的魔法。', '/YouName/903819.png', '街拍', 15);

-- Background music tracks
CREATE TABLE IF NOT EXISTS t_track (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(100)  NOT NULL,
    artist       VARCHAR(100),
    url          VARCHAR(500),
    cover        VARCHAR(500),
    duration     INT,
    sort_order   INT           DEFAULT 0,
    created_at   DATETIME      DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Site config (key-value)
CREATE TABLE IF NOT EXISTS t_site_config (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key    VARCHAR(100)  NOT NULL UNIQUE,
    config_value  TEXT,
    updated_at    DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed data: tracks
INSERT INTO t_track (title, artist, url, duration, sort_order) VALUES
('安静的午后', 'Lofi Study', '', 240, 1),
('咖啡馆小调', 'Jazz Cafe', '', 240, 2),
('阳光下的街道', 'Acoustic Vibes', '', 240, 3),
('黄昏时分', 'Chill Evening', '', 240, 4);

-- Seed data: site config
INSERT INTO t_site_config (config_key, config_value) VALUES
('about_title', '关于本站'),
('about_subtitle', 'ABOUT THIS BLOG'),
('about_intro', '你好，我是 Eleven。这里是我记录摄影学习历程的个人博客。'),
('about_sections', '[{"title":"在这里你会看到","items":["📷 摄影构图与用光技巧分享","📱 手机摄影实用教程","🎨 调色与后期处理心得","🌆 街拍、人像、风光实拍经验"]},{"title":"关于我","items":["我不是专业摄影师，只是一个热爱按下快门记录生活的人。","如果你也喜欢摄影，欢迎一起交流探讨。"]}]'),
('about_quote', '「摄影是把瞬间变成永恒的艺术」'),
('about_quote_author', '每一个热爱记录的人');

-- Seed data: categories
INSERT INTO t_category (name, slug, description) VALUES
('摄影技巧', 'photography-tips', '构图、用光、后期等摄影技术分享'),
('街拍纪实', 'street-photography', '街头摄影与人文纪实'),
('风光旅行', 'landscape', '风光摄影与旅行拍摄经验'),
('器材评测', 'gear-review', '相机、镜头与摄影配件评测');

-- Seed data: tags
INSERT INTO t_tag (name, slug) VALUES
('构图', 'composition'),
('用光', 'lighting'),
('后期', 'post-processing'),
('手机摄影', 'mobile-photography'),
('人像', 'portrait'),
('街拍', 'street'),
('风光', 'landscape'),
('黑白', 'black-and-white'),
('胶片', 'film'),
('新手入门', 'beginner');
