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

-- Seed data: categories
INSERT INTO t_category (name, slug, description) VALUES
('技术', 'tech', '技术相关文章'),
('生活', 'life', '日常生活记录'),
('动画', 'anime', '动画推荐与感想'),
('音乐', 'music', '吉他练习与音乐分享');

-- Seed data: tags
INSERT INTO t_tag (name, slug) VALUES
('Java', 'java'),
('Vue', 'vue'),
('Spring Boot', 'spring-boot'),
('前端', 'frontend'),
('吉他', 'guitar'),
('孤独摇滚', 'bocchi-the-rock'),
('动画', 'anime');
