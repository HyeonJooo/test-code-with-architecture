INSERT INTO `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
VALUES (1, 'testId1', 'testNickname1', 'Seoul', 'aaaaaa-aaaa-aaa-aaaaaaaaaaaa', 'ACTIVE', 0);
INSERT INTO `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
VALUES (1, 'helloworld', 1678530673958, 1678530673958, 1);
